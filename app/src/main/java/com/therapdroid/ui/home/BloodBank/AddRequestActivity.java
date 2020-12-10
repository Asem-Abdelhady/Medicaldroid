package com.therapdroid.ui.home.BloodBank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.medicaldroid.R;
import com.therapdroid.data.repository.BloodRepository;
import com.therapdroid.model.database.BloodRequest;
import com.therapdroid.util.LocalStorageUtil;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class AddRequestActivity extends AppCompatActivity {

    // UI Elements
    private Spinner bloodTypeSpinner;
    private EditText infoText;
    private Button addRequestButton;

    // Data Providers
    private BloodRepository bloodRepository = BloodRepository.getInstance();
    private LocalStorageUtil localStorageUtil = LocalStorageUtil.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_request_info);

        bloodTypeSpinner = findViewById(R.id.blood_type_spinner);
        infoText = findViewById(R.id.add_info_text);
        addRequestButton = findViewById(R.id.add_request_button);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.BloodTypes,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodTypeSpinner.setAdapter(adapter);

        addRequestButton.setOnClickListener(view -> createRequest());
    }

    private void createRequest () {
        String info = infoText.getText().toString().trim();
        String bloodType = (String) bloodTypeSpinner.getSelectedItem();

        if (TextUtils.isEmpty(info)) {
            Toast.makeText(this, "You must provide some additional information", Toast.LENGTH_SHORT).show();
            return;
        }

        double lat = getIntent().getExtras().getDouble("lat");
        double lng = getIntent().getExtras().getDouble("lng");

        BloodRequest request = new BloodRequest();
        request.setBlood(bloodType);
        request.setInfo(info);
        request.setX(lat);
        request.setY(lng);

        bloodRepository.addRequest(request).subscribe(new SingleObserver<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Toast.makeText(AddRequestActivity.this, "Processing Request", Toast.LENGTH_SHORT).show();
                addRequestButton.setEnabled(false);
            }

            @Override
            public void onSuccess(String requestId) {
                localStorageUtil.saveRequestId(requestId);
                Toast.makeText(AddRequestActivity.this, "Request Added", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(Throwable e) {
                addRequestButton.setEnabled(true);
                e.printStackTrace();
                Toast.makeText(AddRequestActivity.this, "You are offline", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
