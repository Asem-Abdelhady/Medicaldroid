package com.therapdroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.medicaldroid.R;
import com.therapdroid.data.repository.AuthRepository;
import com.therapdroid.model.database.User;
import com.therapdroid.ui.home.HomeActivity;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameText, emailText, passwordText, confirmPasswordText;
    private Button submit;
    private TextView goLogin;

    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameText = findViewById(R.id.register_username);
        emailText = findViewById(R.id.register_email);
        passwordText = findViewById(R.id.register_password);
        confirmPasswordText = findViewById(R.id.register_confirm_password);
        submit = findViewById(R.id.register_submit);
        goLogin = findViewById(R.id.register_go_login);

        authRepository = AuthRepository.getInstance();

        submit.setOnClickListener(this);
        goLogin.setOnClickListener(this);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register_submit) {


            String username = usernameText.getText().toString().trim();
            String email = emailText.getText().toString().trim();
            String password = passwordText.getText().toString().trim();
            String confirmPassword = confirmPasswordText.getText().toString().trim();

            // validation
            if (username.equals("") || email.equals("") || password.equals("") || confirmPassword.equals("")) {
                Toast.makeText(this, "Please, fill out all the fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "You should confirm password correctly", Toast.LENGTH_SHORT).show();
                return;
            }

            final User user = User.builder()
                    .username(username)
                    .email(email)
                    .password(password)
                    .build();

            authRepository.register(user).subscribe(new SingleObserver<FirebaseUser>() {
                @Override
                public void onSubscribe(Disposable d) {
                    Toast.makeText(RegisterActivity.this, "Processing request", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(FirebaseUser user) {
                    Toast.makeText(RegisterActivity.this, "Welcome " + username, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(RegisterActivity.this, "You are offline", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });

        } else if (v.getId() == R.id.register_go_login) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
