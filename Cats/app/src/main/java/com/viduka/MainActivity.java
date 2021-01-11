package com.viduka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterListe.ItemClickInterface{
    private AdapterListe adapterListe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 20);
        recyclerView.setItemViewCacheSize(20);
        adapterListe = new AdapterListe(this);
        adapterListe.setClickListener(this);
        recyclerView.setAdapter(adapterListe);
        RESTTask restTask = new RESTTask();
        restTask.execute("https://api.thecatapi.com/v1/images/search?limit=20&page=1&order=ASC&api_key=d99cf12d-98f4-4f48-9461-34563a8683fc");
    }

    private class RESTTask extends AsyncTask<String, Void, List<Cat>> {

        @Override
        protected List<Cat> doInBackground(String... strings) {
            List<Cat> cats = new ArrayList<>();
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);
                connection.connect();
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder output = new StringBuilder();
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
                try {
                    JSONArray jsonarray = new JSONArray(output.toString());
                    for (int i = 0; i < jsonarray.length(); i++) {
                        Cat cat = new Cat();
                        cat.setId(jsonarray.getJSONObject(i).getString("id"));
                        cat.setUrl(jsonarray.getJSONObject(i).getString("url"));
                        cat.setHeight(jsonarray.getJSONObject(i).getInt("height"));
                        cat.setWidth(jsonarray.getJSONObject(i).getInt("width"));
                        cats.add(cat);
                    }
                } catch (JSONException e) {
                    e.getMessage();
                }
                reader.close();
                streamReader.close();
                return cats;

            } catch (Exception e) {
                e.getMessage();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        @Override
        protected void onPostExecute(List<Cat> cats) {
            adapterListe.setCats(cats);
            adapterListe.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Cat cat = adapterListe.getCat(position);
        Intent intent = new Intent(this, DetaljiActivity.class);
        intent.putExtra("cat", cat);
        startActivity(intent);
    }
}