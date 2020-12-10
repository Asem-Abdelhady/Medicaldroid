package com.therapdroid.ui.home.encyclopedia;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medicaldroid.R;
import com.therapdroid.ui.home.encyclopedia.static_info.CommonDiseasesActivity;
import com.therapdroid.ui.home.encyclopedia.static_info.FortyDaysToTwoYearsActivity;
import com.therapdroid.ui.home.encyclopedia.static_info.OneToFortyDaysActivity;
import com.therapdroid.ui.home.encyclopedia.static_info.VitaminsActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class EncyclopediaFragment extends Fragment implements View.OnClickListener {

    private TextView oneToFortyDays, fortyDaysToTwoYears, vitamins, commonDiseases;
    private View parent;

    public EncyclopediaFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parent = inflater.inflate(R.layout.fragment_encyclopedia, container, false);
        return parent;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        oneToFortyDays = parent.findViewById(R.id.one_40days);
        fortyDaysToTwoYears = parent.findViewById(R.id.forty_two);
        vitamins = parent.findViewById(R.id.vitamins);
        commonDiseases = parent.findViewById(R.id.common_diseases);

        oneToFortyDays.setOnClickListener(this);
        fortyDaysToTwoYears.setOnClickListener(this);
        vitamins.setOnClickListener(this);
        commonDiseases.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
       switch (v.getId()) {

           case R.id.one_40days:
               startActivity(new Intent(getContext(), OneToFortyDaysActivity.class));
               break;

           case R.id.forty_two:
               startActivity(new Intent(getContext(), FortyDaysToTwoYearsActivity.class));
               break;

           case R.id.vitamins:
               startActivity(new Intent(getContext(), VitaminsActivity.class));
               break;

           case R.id.common_diseases:
               startActivity(new Intent(getContext(), CommonDiseasesActivity.class));
               break;
       }
    }
}
