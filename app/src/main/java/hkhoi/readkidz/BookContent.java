package hkhoi.readkidz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;


public class BookContent extends Activity {

    public final static String SETTING_1 = "text/html";
    public final static String SETTING_2 = "UTF-8";
    private final static int LEFT_DIST = 12;
    private final static int RIGHT_DIST = 400;
    private final static int NAV_CODE = 118;

    private static ContentReader reader = null;
    private static Book curBook = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_content);

        /**
         * Receive signal
         */
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", R.raw.grimm1);

        // Import the book

        curBook = new Book(getInputStream(id));
        reader = new ContentReader(curBook);

        final WebView display = (WebView) findViewById(R.id.web_view);
        display.getSettings().setJavaScriptEnabled(true);
        display.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        display.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        display.loadData(reader.startReading(), SETTING_1, SETTING_2);
        display.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean result = false;
                float down = 0, up = 0;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        down = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        up = event.getX();
                        float dif = up - down;
                        if (dif < LEFT_DIST) {
                            swipeNextPage();
                        } else if (dif > RIGHT_DIST) {
                            swipePreviousPage();
                        }
                        break;
                }
                return result;
            }
        });

        final EditText page = (EditText) findViewById(R.id.page);
        page.setText("" + reader.getCurPage());
        page.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    String str = page.getText().toString();
                    if (!str.isEmpty()) {
                        int pageToGo = Integer.parseInt(str);
                        String content = reader.getHtmlFromPage(pageToGo);
                        display.loadData(content, SETTING_1, SETTING_2);
                        page.setText("" + reader.getCurPage());
                        return true;
                    }
                }
                return false;
            }
        });
        page.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                voiceNavigate();
                return true;
            }
        });
    }

    private InputStream getInputStream(int input) {
        return getResources().openRawResource(input);
    }

    public void swipeNextPage() {
        if (!reader.isLastPage()) {
            WebView webView = (WebView) findViewById(R.id.web_view);
            Animation slide_left = AnimationUtils.loadAnimation(this, R.anim.slide_left);
            webView.startAnimation(slide_left);
            webView.loadData(reader.nextPage(), SETTING_1, SETTING_2);
            EditText page = (EditText) findViewById(R.id.page);
            page.setText("" + reader.getCurPage());
        } else {
            /**
             * Go to quiz section, send quiz list to next activity
             */
            Intent intent = new Intent(this, Question.class);
            intent.putExtra("quizList", curBook.getQuizList());
            startActivity(intent);
            finish();
        }
    }

    public void swipePreviousPage() {
        if (!reader.isFirstPage()) {
            WebView webView = (WebView) findViewById(R.id.web_view);
            Animation slide_right = AnimationUtils.loadAnimation(this, R.anim.slide_right);
            webView.startAnimation(slide_right);
            webView.loadData(reader.prevPage(), SETTING_1, SETTING_2);
            EditText page = (EditText) findViewById(R.id.page);
            page.setText("" + reader.getCurPage());
        }
    }

    private void voiceNavigate() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Which page?");
        startActivityForResult(intent, NAV_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NAV_CODE) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                int pageToGo = isNumeric(list);
                if (pageToGo > 0) {
                    String content = reader.getHtmlFromPage(pageToGo);
                    WebView display = (WebView) findViewById(R.id.web_view);
                    display.loadData(content, SETTING_1, SETTING_2);
                    EditText page = (EditText) findViewById(R.id.page);
                    page.setText("" + reader.getCurPage());
                } else {
                    Toast.makeText(this, "Number only please :)", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You have cancelled!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private int isNumeric(ArrayList<String> list) {
        for (String result : list) {
            try {
                int num = Integer.parseInt(result);
                return num;
            } catch (NumberFormatException ex) {
                // Do nothing
            }
        }
        return -1;
    }
}
