package hkhoi.readkidz;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;


public class Home extends ActionBarActivity {

    private static final int REQ = 108;
    private static final int QR_CODE = 135;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    private ArrayList<BookThumbnail> thumbnails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        /**
         * LOADING CONTENTS STATICALLY!
         * FILE BROWSING WILL BE IMPLEMENTED IN LATER VERSIONS.
         */
        // Load menu
        thumbnails = LoadMenu(R.raw.menu);
        thumbnails.get(0).setBookId(R.raw.grimm1);
        thumbnails.get(1).setBookId(R.raw.grimm2);
        thumbnails.get(2).setBookId(R.raw.grimm3);
        thumbnails.get(3).setBookId(R.raw.science1);
        thumbnails.get(4).setBookId(R.raw.science2);

        /**
         * DONE LOADING THUMBNAILS, START INFLATE LISTVIEW
         */


        ListView listView = (ListView) findViewById(R.id.list_view);
        ThumbnailAdapter adapter = new ThumbnailAdapter(this, thumbnails);
        listView.setAdapter(adapter);

        /**
         * DONE INFLATING, SHOW GREETING NOTE
         */
        Toast.makeText(this, "Choose your book!", Toast.LENGTH_SHORT).show();

    }

    private ArrayList<BookThumbnail> LoadMenu(int menu) {
        InputStream is = getResources().openRawResource(menu);
        ArrayList<BookThumbnail> thumbnails = new ArrayList<>();
        BufferedReader in =
                new BufferedReader(new InputStreamReader(is));

        try {
            int num = Integer.parseInt(in.readLine());
            for (int i = 0; i < num; ++i) {
                String image = in.readLine();
                String title = in.readLine();
                String description = in.readLine();
                String speech = in.readLine();
                thumbnails.add(new BookThumbnail(image, 0, title, description, speech));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return thumbnails;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.voice_menu) {
            voiceSelect();
        } else if (id == R.id.exit) {
            System.exit(0);
        } else if (id == R.id.qr) {
            QrScan();
        }

        return super.onOptionsItemSelected(item);
    }

    private void QrScan() {
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, QR_CODE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Go to Play Store and download it!", Toast.LENGTH_SHORT);
        }
    }

    private void voiceSelect() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Which book?");
        startActivityForResult(intent, REQ);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                int bookToGo = selectBookFromVoice(list);
                if (bookToGo == -1) {
                    Toast.makeText(this, "Nothing found!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(this, BookContent.class);
                    intent.putExtra("id", bookToGo);
                    startActivity(intent);
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You have cancelled!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == QR_CODE) {
            if (resultCode == RESULT_OK) {
                String content = data.getStringExtra("SCAN_RESULT");
                int bookToGo = chooseBookFromQR(content);
                Intent intent = new Intent(this, BookContent.class);
                if (bookToGo == -1) {
                    Toast.makeText(this, "Nothing found!", Toast.LENGTH_LONG);
                } else {
                    intent.putExtra("id", bookToGo);
                    startActivity(intent);
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You have cancelled!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private int chooseBookFromQR(String content) {
        int result = -1;

        for (BookThumbnail thumbnail : thumbnails) {
            if (content.equals(thumbnail.getSpeech())) {
                return thumbnail.getBookId();
            }
        }

        return result;
    }

    private int selectBookFromVoice(ArrayList<String> list) {
        for (String result : list) {
            for (BookThumbnail thumbnail : thumbnails) {
                if (result.equals(thumbnail.getSpeech())) {
                    return thumbnail.getBookId();
                }
            }
        }
        return -1;
    }

}