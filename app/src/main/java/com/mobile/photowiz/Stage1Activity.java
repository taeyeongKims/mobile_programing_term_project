package com.mobile.photowiz;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Stage1Activity extends AppCompatActivity {

    private ProgressBar progressBar;
    private CountDownTimer timer;
    private RatingBar ratingBar;
    private ImageView incorrectImage;
    private int remainingHearts = 5;
    private int progress = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stage1);

        // ProgressBar 설정
        progressBar = findViewById(R.id.progress_bar);
        startTimer();

        // RatingBar 설정
        ratingBar = findViewById(R.id.hart);

        // 틀렸다는 이미지 설정
        incorrectImage = findViewById(R.id.incorrect_image);
        // Pause 버튼 처리
        Button pauseButton = findViewById(R.id.pause);
        pauseButton.setVisibility(View.VISIBLE);
        pauseButton.setOnClickListener(v -> {
            if (timer != null) {
                timer.cancel();
                Toast.makeText(this, "Game Paused", Toast.LENGTH_SHORT).show();
            }
        });

        // Ready / Go 이미지 설정
        ImageView readyImage = findViewById(R.id.ready);
        ImageView goImage = findViewById(R.id.go);

        // "Ready" -> "Go" 애니메이션
        readyImage.setVisibility(View.VISIBLE);
        readyImage.postDelayed(() -> {
            readyImage.setVisibility(View.GONE);
            goImage.setVisibility(View.VISIBLE);

            goImage.postDelayed(() -> goImage.setVisibility(View.GONE), 1000);
        }, 1000);

        // "Game Over" 이미지 설정
        ImageView gameOverImage = findViewById(R.id.game_over);
        gameOverImage.setVisibility(View.GONE);

        // 맵을 덮는 버튼 설정
        Button overlayButton = findViewById(R.id.overlay_button);
        overlayButton.setOnClickListener(v -> {
            decreaseHeart();
            showIncorrectImage();
        });
        overlayButton.setVisibility(View.VISIBLE);

        // Answer 버튼 처리
        setAnswerButton(findViewById(R.id.answer1));
        setAnswerButton(findViewById(R.id.answer2));
        setAnswerButton(findViewById(R.id.answer3));
        setAnswerButton(findViewById(R.id.answer4));
        setAnswerButton(findViewById(R.id.answer5));
    }

    private void startTimer() {
        timer = new CountDownTimer(30000, 300) { // 30초 타이머
            @Override
            public void onTick(long millisUntilFinished) {
                progress -= 1;
                progressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(0);
                Toast.makeText(Stage1Activity.this, "Time's up!", Toast.LENGTH_SHORT).show();
                showGameOver();
            }
        };
        timer.start();
    }

    private void setAnswerButton(ImageButton answerButton) {
        answerButton.setVisibility(View.VISIBLE);
        answerButton.setOnClickListener(v -> {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            answerButton.setVisibility(View.INVISIBLE);
        });
    }

    private void showGameOver() {
        ImageView gameOverImage = findViewById(R.id.game_over);
        gameOverImage.setVisibility(View.VISIBLE);

        // 게임 오버 후 추가 동작 (예: 다른 화면으로 이동)
    }

    private void decreaseHeart() {
        if (remainingHearts > 0) {
            remainingHearts--;
            ratingBar.setRating(remainingHearts);

            if (remainingHearts == 0) {
                Toast.makeText(this, "No hearts left!", Toast.LENGTH_SHORT).show();
                showGameOver();
            }
        }
    }

    private void showIncorrectImage() {
        incorrectImage.setVisibility(View.VISIBLE); // 이미지 보이기

        // 1초 뒤에 틀렸다는 이미지 숨기기
        new Handler().postDelayed(() -> incorrectImage.setVisibility(View.GONE), 1000);
    }
}
