package com.example.basillink.androidbasicquiz;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    int totalScore = 0;
    TextView usernameTV, scoreTV;
    Button submit, reset;
    CheckBox checkBox1A, checkBox1B, checkBox1C, checkBox5A, checkBox5B, checkBox5C;
    RadioButton radio2A, radio4A, radio6B, radio7A, radio9B, radio10C;
    EditText answerFor3, answerFor8;
    RadioGroup rGroup2, rGroup4, rGroup5, rGroup6, rGroup7, rGroup9, rGroup10;
    MediaPlayer scoreRangeSound;
    View root;
    String gottenUsername, answer3, answer8;
    boolean a1, b1, c1, a2, a4, a5, b5, c5, b6, a7, b9, c10;
    boolean checkSelection = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        root = findViewById(R.id.root);
        gottenUsername = getIntent().getExtras().getString("KEY_USERNAME");
        usernameTV = findViewById(R.id.username_tv);
        StringBuilder stringBuilder = new StringBuilder();
        usernameTV.setText(stringBuilder.append(usernameTV.getText()).append(" ")
                .append(gottenUsername).toString());
        resetAnswers(reset);
    }

    public boolean validityCheck() {
        getObjectReferences();
        int checkedId, indexbtn;
        RadioButton rb;
        RadioGroup[] radioGroups = {rGroup2, rGroup4, rGroup6, rGroup7, rGroup9, rGroup10};
        if (!a1 && !b1 && !c1) {
            Toast.makeText(this, "Please select at least one option in question 1",
                    Toast.LENGTH_SHORT).show();
            checkSelection = false;
        } else if (a1 && b1 && c1) {
            Toast.makeText(this, "You cannot select more than two options in question 1",
                    Toast.LENGTH_SHORT).show();
            checkSelection = false;
        } else if (!a5 && !b5 && !c5) {
            Toast.makeText(this, "Please select at least one option in question 5",
                    Toast.LENGTH_SHORT).show();
            checkSelection = false;
        } else if (a5 && b5 && c5) {
            Toast.makeText(this, "You cannot select more than two options in question 5",
                    Toast.LENGTH_SHORT).show();
            checkSelection = false;
        } else if (answer3.isEmpty()) {
            Toast.makeText(this, "Question 3 cannot be empty", Toast.LENGTH_SHORT).show();
            checkSelection = false;
        } else if (answer8.isEmpty()) {
            Toast.makeText(this, "Question 8 cannot be empty", Toast.LENGTH_SHORT).show();
            checkSelection = false;
        } else {
            for (RadioGroup radioGroup : radioGroups) {
                checkedId = radioGroup.getCheckedRadioButtonId();
                rb = radioGroup.findViewById(checkedId);
                indexbtn = radioGroup.indexOfChild(rb);
                if (indexbtn == -1) {
                    checkSelection = false;
                    return checkSelection;
                } else {
                    checkSelection = true;
                }
            }
        }
        return checkSelection;
    }

    public void submitAnswers(View view) {
        /**
         * Function call to the getObjectReferences
         * this returns the different views used in the quiz using the "findViewById" method
         */
        getObjectReferences();

        answer3 = answerFor3.getText().toString().trim();
        answer8 = answerFor8.getText().toString().trim();

        if (!validityCheck()) {
            Toast.makeText(this, "Please attempt all question", Toast.LENGTH_SHORT).show();
        } else {
            calculateScore(a1, c1, a2, a4, a5, b5, b6, a7, b9, c10, answer3, answer8);

            StringBuilder builder = new StringBuilder();

            if (totalScore == 100) {
                scoreRangeSound = MediaPlayer.create(this, R.raw.score_high_unbelievable);
                scoreRangeSound.start();
            } else if (totalScore >= 80) {
                scoreRangeSound = MediaPlayer.create(this, R.raw.score_high_applause);
                scoreRangeSound.start();
            } else if ((totalScore >= 50) && (totalScore <= 75)) {
                scoreRangeSound = MediaPlayer.create(this, R.raw.score_medium);
                scoreRangeSound.start();
            } else {
                scoreRangeSound = MediaPlayer.create(this, R.raw.score_low);
                scoreRangeSound.start();
            }
            scoreTV.setText(builder.append("Score: ").append(totalScore).append("%").toString());
        }
    }

    public void resetAnswers(View view) {
        getObjectReferences();

        RadioGroup[] radioGroups = {rGroup2, rGroup4, rGroup5, rGroup6, rGroup7, rGroup9, rGroup10};
        for (RadioGroup radioGroup : radioGroups) {
            radioGroup.clearCheck();
            RadioButton[] radioButtons = {radio2A, radio4A, radio6B, radio7A, radio9B, radio10C};
            for (RadioButton radioButton : radioButtons) {
                radioButton.setTextColor(Color.WHITE);
            }
        }
        CheckBox[] checkBoxes = {checkBox1A, checkBox1B, checkBox1C, checkBox5A, checkBox5B, checkBox5C};
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setChecked(false);
            checkBox.setTextColor(Color.WHITE);
        }
        answerFor3.setText("");
        answerFor8.setText("");
        totalScore = 0;
    }

    public void showAnswers(View view) {
        getObjectReferences();
        StringBuilder builder = new StringBuilder();
        RadioButton[] radioButtons = {radio2A, radio4A, radio6B, radio7A, radio9B, radio10C};
        for (RadioButton radioButton : radioButtons) {
            radioButton.setChecked(false);
            radioButton.setTextColor(getResources().getColor(R.color.colorAccent));
        }

        CheckBox[] checkBoxes = {checkBox1A, checkBox1C, checkBox5A, checkBox5B};
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setChecked(false);
            checkBox.setTextColor(getResources().getColor(R.color.colorAccent));
        }

        answerFor3.setText(getResources().getString(R.string.question_3_answer));
        answerFor8.setText(getResources().getString(R.string.question_8_answer));
        answerFor3.setTextColor(getResources().getColor(R.color.colorAccent));
        answerFor8.setTextColor(getResources().getColor(R.color.colorAccent));

        totalScore = 0;
        scoreTV.setText("Score: " + totalScore + "%");
    }

    /**
     * @param ans1A checks for option 1A as an answer, and adds 5% to the score
     * @param ans1C checks for option 1C as an answer, and adds 5% to the score
     * @param ans2  checks for option A as an answer, and adds 10% to the score
     * @param ans4  checks for option A as the answer, and adds 10% to the score
     * @param ans5A checks for option 5A as an answer, and adds 5% to the score
     * @param ans5B checks for option 5B as an answer, and adds 5% to the score
     * @param ans6  checks for option B as an answer, and adds 10% to the score
     * @param ans7  checks for option A as an answer, and adds 10% to the score
     * @param ans9  checks for option B as an answer, and adds 10% to the score
     * @param ans10 checks for option C as an answer, and adds 10% to the score
     * @param ans3  checks if the answer entered matches the correct answer for question 3
     * @param ans8  checks if the answer entered matches the correct answer for question 10
     * @return totalScore for displayed to the UI.
     */
    private void calculateScore(boolean ans1A, boolean ans1C, boolean ans2, boolean ans4,
                                boolean ans5A, boolean ans5B, boolean ans6, boolean ans7,
                                boolean ans9, boolean ans10, String ans3, String ans8) {
        int score = 0;
        if (ans1A) score += 5;
        if (ans1C) score += 5;
        if (ans5A) score += 5;
        if (ans5B) score += 5;
        if (ans2) score += 10;
        if (ans4) score += 10;
        if (ans6) score += 10;
        if (ans7) score += 10;
        if (ans9) score += 10;
        if (ans10) score += 10;
        if (ans3.equalsIgnoreCase(getResources().getString(R.string.question_3_answer)))
            score += 10;
        if (ans8.equalsIgnoreCase(getResources().getString(R.string.question_8_answer)))
            score += 10;
        totalScore = score;
    }


    /**
     * Creates
     */
    private void getObjectReferences() {
        checkBox1A = findViewById(R.id.checkbox_option_1A);
        checkBox1B = findViewById(R.id.checkbox_option_1B);
        checkBox1C = findViewById(R.id.checkbox_option_1C);
        checkBox5A = findViewById(R.id.checkbox_option_5A);
        checkBox5B = findViewById(R.id.checkbox_option_5B);
        checkBox5C = findViewById(R.id.checkbox_option_5C);

        radio2A = findViewById(R.id.radiobutton_2A);
        radio4A = findViewById(R.id.radiobutton_option_4A);
        radio6B = findViewById(R.id.radiobutton_option_6B);
        radio7A = findViewById(R.id.radiobutton_option_7A);
        radio9B = findViewById(R.id.radiobutton_option_9B);
        radio10C = findViewById(R.id.radiobutton_option_10C);

        rGroup2 = findViewById(R.id.radiogroup_2);
        rGroup4 = findViewById(R.id.radiogroup_4);
        rGroup5 = findViewById(R.id.radiogroup_5);
        rGroup6 = findViewById(R.id.radiogroup_6);
        rGroup7 = findViewById(R.id.radiogroup_7);
        rGroup9 = findViewById(R.id.radiogroup_9);
        rGroup10 = findViewById(R.id.radiogroup_10);

        answerFor3 = findViewById(R.id.edittext_answer_Q3);
        answerFor8 = findViewById(R.id.edittext_answer_Q8);

        scoreTV = findViewById(R.id.score_tv);

        submit = findViewById(R.id.submit_answers);
        reset = findViewById(R.id.clear_selection);

        a2 = radio2A.isChecked();
        a4 = radio4A.isChecked();
        b6 = radio6B.isChecked();
        a7 = radio7A.isChecked();
        b9 = radio9B.isChecked();
        c10 = radio10C.isChecked();

        a1 = checkBox1A.isChecked();
        b1 = checkBox1B.isChecked();
        c1 = checkBox1C.isChecked();
        a5 = checkBox5A.isChecked();
        b5 = checkBox5B.isChecked();
        c5 = checkBox5C.isChecked();
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
}
