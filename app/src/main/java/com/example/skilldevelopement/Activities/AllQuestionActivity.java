package com.example.skilldevelopement.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.skilldevelopement.Adapters.QuestionListAdapter;
import com.example.skilldevelopement.Fragments.QuestionsListFragment;
import com.example.skilldevelopement.Models.PostsModel;
import com.example.skilldevelopement.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class AllQuestionActivity extends AppCompatActivity {

    QuestionsListFragment questionsListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_question);

        questionsListFragment=new QuestionsListFragment(AllQuestionActivity.this,"All Question");
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .addToBackStack(null).replace(R.id.frame, questionsListFragment).commit();
        Bundle bundle = new Bundle();
        bundle.putString("from", "From");
        questionsListFragment.setArguments(bundle);




    }
}