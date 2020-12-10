package com.therapdroid.data.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.therapdroid.model.database.User;
import com.therapdroid.util.LocalStorageUtil;

import durdinapps.rxfirebase2.RxFirebaseAuth;

import io.reactivex.Single;
import io.realm.Realm;

public class AuthRepository {

    private static AuthRepository instance;

    private FirebaseAuth auth;
    private Realm realm;
    private LocalStorageUtil localStorageUtil;

    private AuthRepository () {
        auth = FirebaseAuth.getInstance();
        realm = Realm.getDefaultInstance();
        localStorageUtil = LocalStorageUtil.getInstance();
    }

    public static AuthRepository getInstance() {
        if (instance == null) instance = new AuthRepository();
        return instance;
    }

    public Single<FirebaseUser> register (User user) {
        return Single.create(emitter -> {
            auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(task -> {
                if (!task.isSuccessful() && task.getException() != null) emitter.onError(task.getException());

                UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                        .setDisplayName(user.getUsername())
                        .build();

                task.getResult().getUser().updateProfile(request).addOnCompleteListener(profileTask -> {

                    if (!profileTask.isSuccessful() && profileTask.getException() != null) emitter.onError(profileTask.getException());

                    emitter.onSuccess(auth.getCurrentUser());

                });
            });
        });
    }


    public Single<FirebaseUser> login (String email, String password) {
        return RxFirebaseAuth.signInWithEmailAndPassword(auth, email, password)
                .toSingle()
                .map(authResult -> authResult.getUser());
    }

    public boolean isAuthenticated () {
        return auth.getCurrentUser() != null;
    }

    public FirebaseUser getCurrentUser () {
        return auth.getCurrentUser();
    }

    public void logout () {
        auth.signOut();
        realm.executeTransaction(realm -> realm.deleteAll());
        localStorageUtil.clear();
    }
}
