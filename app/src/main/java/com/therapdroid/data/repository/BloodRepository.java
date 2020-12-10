package com.therapdroid.data.repository;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.therapdroid.model.database.BloodRequest;

import java.util.List;

import durdinapps.rxfirebase2.RxFirestore;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class BloodRepository {

    private final String BLOOD_REQUESTS_COLLECTION = "blood_requests";

    private static final BloodRepository ourInstance = new BloodRepository();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public static BloodRepository getInstance() {
        return ourInstance;
    }

    private BloodRepository() {}

    public Flowable<List<BloodRequest>> loadBloodRequest () {
        Query query = firestore.collection(BLOOD_REQUESTS_COLLECTION);
        return Flowable.concat(
                RxFirestore.getCollection(query).toFlowable(),
                RxFirestore.observeQueryRef(query)
        ).map(snapshots -> snapshots.toObjects(BloodRequest.class));
    }

    public Single<String> addRequest (BloodRequest request) {
        CollectionReference reference = firestore.collection(BLOOD_REQUESTS_COLLECTION);
        return RxFirestore
                .addDocument(reference, request)
                .map(DocumentReference::getId);
    }

    public Completable deleteRequest (String requestId) {
        DocumentReference reference = firestore.collection(BLOOD_REQUESTS_COLLECTION).document(requestId);
        return RxFirestore.deleteDocument(reference);
    }
}
