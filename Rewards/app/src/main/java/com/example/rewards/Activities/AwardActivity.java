package com.example.rewards.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rewards.R;
import com.example.rewards.UserProfile;

public class AwardActivity extends AppCompatActivity {

    private TextView nameAward;
    private TextView numberPointsAwardedAward;
    private TextView departmentTextProfileAward;
    private TextView positionTextProfileAward;
    private TextView storyTextAward;
    private EditText commentTextAward;
    private EditText rewardPointsAward;
    private ImageView imageView;

    private TextView charCountText;
    private UserProfile dh;
    public static int MAX_CHARS = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);

        nameAward = findViewById(R.id.nameAward);
        numberPointsAwardedAward = findViewById(R.id.numberPointsAwardedAward);
        departmentTextProfileAward = findViewById(R.id.departmentTextAward);
        positionTextProfileAward = findViewById(R.id.positionTextAward);
        storyTextAward = findViewById(R.id.storyTextAward);
        rewardPointsAward = findViewById(R.id.rewardPointsAward);
        commentTextAward = findViewById(R.id.commentTextAward);
        imageView = findViewById(R.id.imageAward);

        Intent intent = getIntent();

        dh = (UserProfile) intent.getSerializableExtra("AWARD");

        nameAward.setText(dh.getLast_name() + ", " + dh.getFirst_name() );
        numberPointsAwardedAward.setText(String.valueOf(dh.getPoints_awarded()));
        departmentTextProfileAward.setText(dh.getDepartment());
        positionTextProfileAward.setText(dh.getPosition());
        storyTextAward.setText(dh.getStory());
        imageView.setImageResource(R.drawable.default_photo);

        charCountText = findViewById(R.id.counter4);
        commentTextAward.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_CHARS)});
        addTextListener();
    }

    private void addTextListener() {
        commentTextAward.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // Nothing to do here
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // Nothing to do here
                String countText = "(" + 0 + " of " + MAX_CHARS + ")";
                charCountText.setText(countText);
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                int len = s.toString().length();
                String countText = "(" + len + " of " + MAX_CHARS + ")";
                charCountText.setText(countText);
            }
        });
    }
}
