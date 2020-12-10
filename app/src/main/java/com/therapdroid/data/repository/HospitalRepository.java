package com.therapdroid.data.repository;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.therapdroid.model.database.Hospital;

import java.util.List;

import durdinapps.rxfirebase2.RxFirestore;
import io.reactivex.Flowable;

public class HospitalRepository {

    private final String HOSPITALS_COLLECTION = "hospitals";
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private static final HospitalRepository ourInstance = new HospitalRepository();
    public static HospitalRepository getInstance() {
        return ourInstance;
    }
    private HospitalRepository() {}

    public Flowable<List<Hospital>> loadHospitals () {
        Query query = firestore.collection(HOSPITALS_COLLECTION);
        return Flowable.concat(
                RxFirestore.getCollection(query).toFlowable(),
                RxFirestore.observeQueryRef(query)
        ).map(snapshots -> snapshots.toObjects(Hospital.class));
    }
}
