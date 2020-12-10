package com.therapdroid.ui.results;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.medicaldroid.R;
import com.therapdroid.entity.local.Chat;
import com.therapdroid.entity.local.LocalCondition;

public class ResultsActivity extends AppCompatActivity {

    // ui
    private TextView resultText;

    // data
    private ResultsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        resultText = findViewById(R.id.results_text);

        viewModel = ViewModelProviders.of(this).get(ResultsViewModel.class);

        Chat chat = viewModel.getChat(getIntent().getStringExtra("chatId"));
        LocalCondition condition = chat.getLocalConditions().first();

        resultText.setText("You are " + (int) (condition.getProbability() * 100) + "% to have " + condition.getName());
    }
}