package com.therapdroid.data.remote;

import com.therapdroid.entity.remote.request.DiagnosisRequest;
import com.therapdroid.entity.remote.request.ParseRequest;
import com.therapdroid.entity.remote.response.DiagnosisResponse;
import com.therapdroid.entity.remote.response.ParseResponse;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {

    @POST("parse")
    @Headers({
            "Content-Type: application/json",
            "Dev-Mode: true",
            "App-Id: 2221de61",
            "App-Key: b96184c47dd3f09ceca1f3a2cfad8531"
    })
    Single<Response<ParseResponse>> parseFreeText(@Body ParseRequest request);

    @POST("diagnosis")
    @Headers({
            "Content-Type: application/json",
            "Dev-Mode: true",
            "App-Id: 2221de61",
            "App-Key: b96184c47dd3f09ceca1f3a2cfad8531"
    })
    Single<Response<DiagnosisResponse>> diagnose(@Body DiagnosisRequest request);

}
