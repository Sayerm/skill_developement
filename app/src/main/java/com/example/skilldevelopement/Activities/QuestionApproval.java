package com.example.skilldevelopement.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.skilldevelopement.Fragments.QuestionsListFragment;
import com.example.skilldevelopement.R;

public class QuestionApproval extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_approval);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, new QuestionsListFragment()).commit();




    }
}