package com.viduka;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import java.io.InputStream;
import java.util.List;

public class AdapterListe extends RecyclerView.Adapter<AdapterListe.Red> {

    private List<Cat> cats;
    private final LayoutInflater layoutInflater;
    private ItemClickInterface itemClickInterface;

    public AdapterListe(Context context){
        layoutInflater = LayoutInflater.from(context);
    }

    public void setCats(List<Cat> cats) {
        this.cats = cats;
    }


    @Override
    public Red onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.red_liste, parent,false);
        return new Red(view);
    }

    @Override
    public void onBindViewHolder(Red holder, int position) {
        Cat cat = cats.get(position);
        new DownloadImageTask((ImageView) holder.imageView)
                .execute(cat.getUrl());
    }

    @Override
    public int getItemCount() {
        return cats==null ? 0 : cats.size();
    }

    public Cat getCat(int position) {
        return cats.get(position);
    }


    public class Red extends RecyclerView.ViewHolder implements  View.OnClickListener{

        private final ImageView imageView;

        public Red(View view){
            super(view);
            imageView = view.findViewById(R.id.imageView);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(itemClickInterface != null){
                itemClickInterface.onItemClick(v,getAdapterPosition());
            }
        }
    }


    public void setClickListener(ItemClickInterface clickListener) {
        this.itemClickInterface=clickListener;
    }

    public interface ItemClickInterface{
        void onItemClick(View view, int position);
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
