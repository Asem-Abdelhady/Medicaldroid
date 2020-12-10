package com.therapdroid.entity.remote.response;

import com.google.gson.annotations.SerializedName;
import com.therapdroid.entity.remote.model.RemoteCondition;
import com.therapdroid.entity.remote.model.RemoteQuestion;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiagnosisResponse {

    @SerializedName("question") private RemoteQuestion remoteQuestion;
    @SerializedName("conditions") private List<RemoteCondition> remoteConditions;
    @SerializedName("should_stop") private boolean shouldStop;

}
