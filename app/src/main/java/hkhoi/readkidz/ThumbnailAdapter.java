package hkhoi.readkidz;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hkhoi on 6/29/15.
 */
public class ThumbnailAdapter extends BaseAdapter {

    private Context context;
    private static LayoutInflater inflater;
    ArrayList<BookThumbnail> thumbnails;

    public ThumbnailAdapter(Context context, ArrayList<BookThumbnail> thumbnails) {
        this.context = context;
        this.thumbnails = thumbnails;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return thumbnails.size();
    }

    @Override
    public Object getItem(int position) {
        return thumbnails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.thumbnail, null);
        WebView image = (WebView) rowView.findViewById(R.id.image);
        TextView title = (TextView) rowView.findViewById(R.id.title);
        TextView description = (TextView) rowView.findViewById(R.id.description);

        image.loadData(thumbnails.get(position).getImageData(), BookContent.SETTING_1, BookContent.SETTING_2);
        title.setText(thumbnails.get(position).getTitle());
        description.setText(thumbnails.get(position).getDescription());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookContent.class);
                intent.putExtra("id", thumbnails.get(position).getBookId());
                context.startActivity(intent);
            }
        });

        return rowView;
        // under construction
    }
}
