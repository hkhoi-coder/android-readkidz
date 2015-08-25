package hkhoi.readkidz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Question extends ActionBarActivity {

    private QuizHandler quizHandler;
    private int point = 0;
    private boolean helpFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        ArrayList<Quiz> quizList = (ArrayList<Quiz>) getIntent().getSerializableExtra("quizList");
        quizHandler = new QuizHandler(quizList);

        WebView webView = (WebView) findViewById(R.id.html_content);
        webView.getSettings().setJavaScriptEnabled(true);

        setContents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.quiz_help) {
            helpSkipQuestion();
        }
        return super.onOptionsItemSelected(item);
    }

    private void helpSkipQuestion() {
        helpFlag = true;
        int radioId = 0;
        int[] choiceId = {R.id.option0, R.id.option1, R.id.option2, R.id.option3};
        RadioButton hint = (RadioButton) findViewById(choiceId[quizHandler.getAnswer()]);
        hint.setChecked(true);
        Toast.makeText(this, "The answer has been checked :)", Toast.LENGTH_SHORT).show();
    }

    public void onOptionSelected(View view) {
        int option = 0;
        switch (view.getId()) {
            case R.id.option0:
                option = 0;
                break;
            case R.id.option1:
                option = 1;
                break;
            case R.id.option2:
                option = 2;
                break;
            case R.id.option3:
                option = 3;
                break;
        }

        if (quizHandler.isCorrected(option)) {
            correctOption();
        } else {
            incorrectOption();
        }
    }

    public void nextQuiz(View view) {
        if (!quizHandler.isLastQuiz()) {
            hideMessage();
            quizHandler.nextQuiz();
            setContents();
        } else {
            Intent intent = new Intent(this, Result.class);
            intent.putExtra("score", point);
            intent.putExtra("isPerfect", point == quizHandler.numberOfQuiz());
            startActivity(intent);
            finish();
        }
    }

    private void incorrectOption() {
        hideQuestion();
        betterLuckMessage();
        popUpMessage();
    }

    private void correctOption() {
        hideQuestion();
        congratMessage();
        popUpMessage();
        if (!helpFlag) {
            ++point;
            TextView score = (TextView) findViewById(R.id.score);
            score.setText("Score: " + point);
        }
        helpFlag = false;
    }

    private void congratMessage() {
        ImageView resultImage = (ImageView) findViewById(R.id.result_image);
        resultImage.setImageResource(R.drawable.congrats);
    }

    private void betterLuckMessage() {
        ImageView resultImage = (ImageView) findViewById(R.id.result_image);
        resultImage.setImageResource(R.drawable.badluck);
    }

    private void setContents() {
        hideMessage();
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);

        WebView webView = (WebView) findViewById(R.id.html_content);
        webView.loadData(quizHandler.getCurContent(), BookContent.SETTING_1, BookContent.SETTING_2);

        String[] options = quizHandler.getCurOptions();

        radioGroup.clearCheck();
        for (int i = 0; i < radioGroup.getChildCount(); ++i) {
            RadioButton child = (RadioButton) radioGroup.getChildAt(i);
            child.setText(options[i]);
        }

        popUpQuestion();
    }

    private void popUpQuestion() {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        WebView webView = (WebView) findViewById(R.id.html_content);

        webView.setVisibility(View.VISIBLE);
        radioGroup.setVisibility(View.VISIBLE);
    }

    private void hideQuestion() {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        WebView webView = (WebView) findViewById(R.id.html_content);

        webView.setVisibility(View.GONE);
        radioGroup.setVisibility(View.GONE);
    }

    private void popUpMessage() {
        ImageView resultImage = (ImageView) findViewById(R.id.result_image);

        resultImage.setVisibility(View.VISIBLE);
    }

    private void hideMessage() {
        ImageView resultImage = (ImageView) findViewById(R.id.result_image);

        resultImage.setVisibility(View.GONE);
    }
}
