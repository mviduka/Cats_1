package com.viduka;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.InputStream;

public class DetaljiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalji);
        Intent intent = getIntent();
        Cat cat = (Cat)intent.getSerializableExtra("cat");
        TextView id = findViewById(R.id.id);
        id.setText("Id: "+cat.getId());
        TextView url = findViewById(R.id.url);
        url.setText("Url: "+cat.getUrl());
        TextView height = findViewById(R.id.height);
        height.setText("Visina: "+cat.getHeight().toString());
        TextView width = findViewById(R.id.width);
        width.setText("Širina: "+cat.getWidth().toString());
        Button nazad = findViewById(R.id.nazad);
        nazad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new DownloadImageTask((ImageView) findViewById(R.id.image))
                .execute(cat.getUrl());
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}

