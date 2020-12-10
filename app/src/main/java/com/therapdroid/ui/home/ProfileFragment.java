package com.therapdroid.ui.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.medicaldroid.R;
import com.therapdroid.data.repository.AuthRepository;
import com.therapdroid.ui.LoginActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    private View parent;
    private TextView username, email;
    private Button logoutButton;

    private AuthRepository authRepository = AuthRepository.getInstance();

    public ProfileFragment() {}


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent =  inflater.inflate(R.layout.fragment_profile, container, false);
        return parent;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        username = parent.findViewById(R.id.profile_username);
        email = parent.findViewById(R.id.profile_email);

        logoutButton = parent.findViewById(R.id.logout);

        logoutButton.setOnClickListener(this);

        fetchUser();

    }

    // get user data
    private void fetchUser () {

        FirebaseUser user = authRepository.getCurrentUser();

        username.setText(user.getDisplayName());
        email.setText(user.getEmail());
    }

    // logout
    @Override
    public void onClick(View v) {
        authRepository.logout();
        startActivity(new Intent(getContext(), LoginActivity.class));
        Toast.makeText(getContext(), "Bye :(", Toast.LENGTH_SHORT).show();
    }
}
