package com.therapdroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.medicaldroid.R;
import com.therapdroid.data.repository.AuthRepository;
import com.therapdroid.ui.home.HomeActivity;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailText;
    private EditText passwordText;
    private Button submit;
    private TextView goRegister;

    private AuthRepository authRepository = AuthRepository.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (authRepository.isAuthenticated()) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        emailText = findViewById(R.id.login_email);
        passwordText = findViewById(R.id.login_password);
        submit = findViewById(R.id.login_submit);
        goRegister = findViewById(R.id.login_go_register);

        submit.setOnClickListener(this);

        goRegister.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    // login
    @Override
    public void onClick(View v) {
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please, fill out all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        authRepository.login(email, password)
                .subscribe(new SingleObserver<FirebaseUser>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Toast.makeText(LoginActivity.this, "Processing Request", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(FirebaseUser user) {
                        Toast.makeText(LoginActivity.this, "Welcome " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(LoginActivity.this, "You are offline", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
    }
}
