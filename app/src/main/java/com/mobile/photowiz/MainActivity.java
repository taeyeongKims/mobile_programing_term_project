package com.mobile.photowiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton startButton = findViewById(R.id.start_button);

        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.title_fade_in);

        startButton.startAnimation(fadeInAnimation);

        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Stage1Activity.class);
            startActivity(intent);
        });
    }
}
