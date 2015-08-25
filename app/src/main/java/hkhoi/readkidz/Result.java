package hkhoi.readkidz;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

public class Result extends ActionBarActivity {

    private String VID_SRC = "<iframe width=\"100%\" height=\"315\" src=\"https://www.youtube.com/embed/WpObsl9QGow\" frameborder=\"0\" allowfullscreen></iframe>";
    private int point;
    private boolean isPerfect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        point = getIntent().getIntExtra("score", 0);
        isPerfect = getIntent().getBooleanExtra("isPerfect", false);

        TextView message = (TextView) findViewById(R.id.message);
        TextView comment = (TextView) findViewById(R.id.comment);


        if (isPerfect) {
            message.setText("CONGRATULATION!!");
            comment.setText("Flawless victory");
            WebView video = (WebView) findViewById(R.id.gif_html);
            video.getSettings().setJavaScriptEnabled(true);
            video.loadData(VID_SRC, BookContent.SETTING_1, BookContent.SETTING_2);
            video.setVisibility(View.VISIBLE);
        } else {
            message.setText("Well done :)");
            comment.setText("Your score is: ");
            TextView score = (TextView) findViewById(R.id.score_display);
            score.setText("" + point);
            score.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.share) {
            share();
        }

        return super.onOptionsItemSelected(item);
    }

    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String toShare;
        if (isPerfect) {
            toShare = "I got a perfect score: " + Integer.toString(point) + " in this quiz, how badass I am!";
        } else {
            toShare = "I got " + point + " in this makeshift app, how about you?";
        }
        intent.putExtra(Intent.EXTRA_TEXT, toShare);
        startActivity(Intent.createChooser(intent, "Let the world know!"));
    }
}
