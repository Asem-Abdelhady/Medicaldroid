package com.therapdroid.model.database;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Hospital {

    private String name;
    private String phone;
    private String manager;
    private String info;
    private long joinedAt;
    private double x;
    private double y;

}
