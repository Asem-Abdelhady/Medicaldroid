package com.therapdroid.ui.home.encyclopedia.static_info;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.medicaldroid.R;

public class OneToFortyDaysActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_to_forty_days);
        TextView nutrition = findViewById(R.id.nutrition);
        TextView wrongHabits = findViewById(R.id.wrong_habits);
        TextView vaccination = findViewById(R.id.vaccination);
        TextView babyInfo = findViewById(R.id.baby_info);

        nutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BabyNutritionActivity.class));
            }
        });

        wrongHabits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BabyWrongHabitsActivity.class));
            }
        });

        vaccination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BabyVaccinationActivity.class));
            }
        });

        babyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BabyWrongInfoActivity.class));
            }
        });

    }
}
