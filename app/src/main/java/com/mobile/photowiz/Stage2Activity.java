package com.mobile.photowiz;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class Stage2Activity extends FullScreenActivity {
    private ProgressBar progressBar;
    private CountDownTimer timer;
    private RatingBar ratingBar;
    private ImageView incorrectImage, correctImage, findImage, stageClearImage, gameOverImage, readyImage, goImage;
    private int remainingHearts = 5;
    private int progress = 100;
    private int correctAnswers = 0;
    private boolean isPaused = false;
    private ImageButton button1, button2, button3, button4, button5, pauseResumeButton;
    private List<View> allButtons = new ArrayList<>();
    private Button overlayButton, pauseButton;
    private boolean[] foundAnswers = {false, false, false, false, false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stage2);

        progressBar = findViewById(R.id.progress_bar);
        ratingBar = findViewById(R.id.hart);
        findImage = findViewById(R.id.find);
        correctImage = findViewById(R.id.correct_image);
        incorrectImage = findViewById(R.id.incorrect_image);
        stageClearImage = findViewById(R.id.stage_clear);
        goImage = findViewById(R.id.go);
        readyImage = findViewById(R.id.ready);
        gameOverImage = findViewById(R.id.game_over);

        readyImage.setVisibility(View.VISIBLE);
        readyImagePostDelayed(readyImage, goImage);

        gameOverImage.setVisibility(View.GONE);
        setAllButton();

        // 화면 전환 후 3초 뒤에 시작 타이머 실행
        new Handler().postDelayed(this::startTimer, 3000);
    }

    private static void readyImagePostDelayed(ImageView readyImage, ImageView goImage) {
        readyImage.postDelayed(() -> {
            readyImage.setVisibility(View.GONE);
            goImage.setVisibility(View.VISIBLE);

            goImage.postDelayed(() -> goImage.setVisibility(View.GONE), 1500);
        }, 1500);
    }

    private void setAllButton() {
        button1 = findViewById(R.id.answer1);
        button2 = findViewById(R.id.answer2);
        button3 = findViewById(R.id.answer3);
        button4 = findViewById(R.id.answer4);
        button5 = findViewById(R.id.answer5);

        allButtons.add(button1);
        allButtons.add(button2);
        allButtons.add(button3);
        allButtons.add(button4);
        allButtons.add(button5);

        for (int i = 0; i < allButtons.size(); i++) {
            setAnswerButtonClickListener((ImageButton) allButtons.get(i), i);
        }

        overlayButton = findViewById(R.id.overlay_button);
        allButtons.add(overlayButton);
        setOverlayButtonClickListener(overlayButton);
        overlayButton.setVisibility(View.VISIBLE);

        pauseButton = findViewById(R.id.pause);
        setPauseButtonClickListener(pauseButton);
        allButtons.add(pauseButton);

        pauseResumeButton = findViewById(R.id.pause_resume_button);
        setPauseResumeButtonClickListener(pauseButton);
        allButtons.add(pauseResumeButton);
    }

    private void setOverlayButtonClickListener(Button overlayButton) {
        overlayButton.setOnClickListener(v -> {
            decreaseHeart();
            showIncorrectImage();
        });
    }

    private void setPauseButtonClickListener(Button pauseButton) {
        pauseButton.setOnClickListener(v -> {
            if (!isPaused) {
                isPaused = true;

                if (timer != null) {
                    timer.cancel();
                }

                pauseResumeButton.setVisibility(View.VISIBLE);
                pauseResumeButton.setClickable(true);
                pauseButton.setClickable(false);

                Toast.makeText(this, "게임 정지", Toast.LENGTH_SHORT).show();

                setAllButtonsEnabled(false);
            }
        });
    }

    private void setPauseResumeButtonClickListener(Button pauseButton) {
        pauseResumeButton.setOnClickListener(v -> {
            if (isPaused) {
                isPaused = false;

                startTimer();

                pauseResumeButton.setVisibility(View.INVISIBLE);
                pauseResumeButton.setClickable(false);
                pauseButton.setClickable(true);

                Toast.makeText(this, "게임 재개", Toast.LENGTH_SHORT).show();
                setAllButtonsEnabled(true);
            }
        });
    }

    private void setAnswerButtonClickListener(ImageButton answerButton, int answerIndex) {
        answerButton.setClickable(true);

        answerButton.setOnClickListener(v -> {
            if (foundAnswers[answerIndex]) {
                Toast.makeText(this, "이미 찾은 부분입니다!", Toast.LENGTH_SHORT).show();
                return;
            }

            foundAnswers[answerIndex] = true;
            correctAnswers++;
            updateFindImage();
            showCorrectImage();
            answerButton.setAlpha(1.0f);

            if (correctAnswers == 5) {
                if (timer != null) timer.cancel();
                showStageClear();
            }
        });
    }

    private void setAllButtonsEnabled(boolean enabled) {
        for (View button : allButtons) {
            button.setEnabled(enabled);
        }
    }

    private void startTimer() {
        timer = new CountDownTimer(60000, 600) {
            @Override
            public void onTick(long millisUntilFinished) {
                progress -= 1;
                progressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(0);
                Toast.makeText(Stage2Activity.this, "시간이 다 됐어요..", Toast.LENGTH_SHORT).show();
                showGameOver();
            }
        };
        timer.start();
    }

    private void updateFindImage() {
        int resId = getResources().getIdentifier("find" + correctAnswers, "drawable", getPackageName());
        if (resId != 0) {
            findImage.setImageResource(resId);
        }
    }

    private void decreaseHeart() {
        if (remainingHearts > 0) {
            remainingHearts--;
            ratingBar.setRating(remainingHearts);

            if (remainingHearts == 0) {
                Toast.makeText(this, "남은 목숨이 없습니다", Toast.LENGTH_SHORT).show();
                showGameOver();
            }
        }
    }

    private void showCorrectImage() {
        correctImage.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> correctImage.setVisibility(View.GONE), 500);
    }

    private void showIncorrectImage() {
        incorrectImage.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> incorrectImage.setVisibility(View.GONE), 500);
    }

    private void showStageClear() {
        stageClearImage.setVisibility(View.VISIBLE);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, Stage3Activity.class));
            finish();
        }, 1500);
    }

    private void showGameOver() {
        ImageView gameOverImage = findViewById(R.id.game_over);
        gameOverImage.setVisibility(View.VISIBLE);
        timer.cancel();
        // 첫 화면으로 이동
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }, 1500);
    }
}