package com.therapdroid.entity.remote.request;

import com.therapdroid.entity.remote.model.Extras;
import com.therapdroid.entity.remote.model.RemoteEvidence;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisRequest {

    private String sex;
    private int age;
    private List<RemoteEvidence> evidence;
    private final Extras extras = new Extras();

}
