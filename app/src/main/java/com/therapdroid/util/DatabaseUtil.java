package com.therapdroid.util;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.therapdroid.model.database.BloodRequest;
import com.therapdroid.model.database.User;

public class DatabaseUtil {

    private static DatabaseUtil instance = null;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private final String USERS_COLLECTION = "users";
    private final String BLOOD_REQUESTS_COLLECTION = "blood_requests";

    private DatabaseUtil() {
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public static DatabaseUtil getInstance() {
        if (instance == null) instance = new DatabaseUtil();
        return instance;
    }

    // save user data
    void saveUser (User user, OnCompleteListener<Void> onCompleteListener) {
        db.collection(USERS_COLLECTION).document(user.getId()).set(user)
                .addOnCompleteListener(onCompleteListener);
    }

    // save the user blood type
    public void addBloodType(String bloodType, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        db.collection(USERS_COLLECTION).document(firebaseAuth.getUid())
                .update("blood", bloodType)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    // get all blood requests
    public Task<QuerySnapshot> getBloodRequests () {
        return db.collection(BLOOD_REQUESTS_COLLECTION).get();
    }

    // submit a new blood request
    public Task<DocumentReference> saveBloodRequest (BloodRequest bloodRequest) {
        return db.collection(BLOOD_REQUESTS_COLLECTION).add(bloodRequest);
    }

    // cancel a blood request
    public Task<Void> cancelBloodRequest (String id) {
        return db.collection(BLOOD_REQUESTS_COLLECTION).document(id).delete();
    }

    // listen for changes in the requests collection
    public CollectionReference listenForBloodRequestsChanges() {
        return db.collection(BLOOD_REQUESTS_COLLECTION);
    }

}