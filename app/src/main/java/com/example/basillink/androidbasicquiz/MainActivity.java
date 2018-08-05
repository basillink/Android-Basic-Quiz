package com.example.basillink.androidbasicquiz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Onyeka Nwachuya <onyeka.basil@gmail.com>
 * @version 2.0
 * @since 1.0
 */

public class MainActivity extends AppCompatActivity {
    boolean backPressed = false;
    EditText userInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                Rect rect = new Rect();
                view.getGlobalVisibleRect(rect);
                if (!rect.contains((int) event.getRawX(), (int) event.getRawX())) {
                    view.clearFocus();
                    InputMethodManager manager =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public void takeQuiz(View view) {
        userInput = findViewById(R.id.username_et);
        Editable username = userInput.getText();
        String gottenUsername = username.toString();

        if (gottenUsername.trim().isEmpty() || gottenUsername.length() < 2) {
            Toast.makeText(this, "Username cannot be less than 2 characters", Toast.LENGTH_SHORT).show();
        } else {
            Intent quizActivityIntent = new Intent(this, QuizActivity.class);
            quizActivityIntent.putExtra("KEY_USERNAME", gottenUsername);
            startActivity(quizActivityIntent);
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressed) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(this, "Press the BACK button again to exit!", Toast.LENGTH_SHORT).show();
            backPressed = true;
        }
        Runnable exitRunnable = new Runnable() {
            @Override
            public void run() {
                backPressed = false;
            }
        };
        new Handler().postDelayed(exitRunnable, 2000);
    }
}
