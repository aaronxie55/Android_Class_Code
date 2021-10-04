package com.assignment.newsgateway;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.assignment.newsgateway.api.Fetcher;
import com.assignment.newsgateway.bean.Article;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleFragment extends Fragment implements View.OnClickListener {

    private Article article;
    private ImageView ivImage;

    public ArticleFragment(Article article) {
        this.article = article;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_article, container, false);

        TextView tvHeadline = view.findViewById(R.id.tv_headline);
        tvHeadline.setText(article.getTitle());
        tvHeadline.setOnClickListener(this);

        TextView tvDate = view.findViewById(R.id.tv_date);
        tvDate.setText(article.getPublishedAt());

        TextView tvAuthor = view.findViewById(R.id.tv_author);
        tvAuthor.setText(article.getAuthor());

        ivImage = view.findViewById(R.id.iv_image);
        new ImageAsyncTask().execute(article.getUrlToImage());
        ivImage.setOnClickListener(this);

        TextView tvText = view.findViewById(R.id.tv_text);
        tvText.setText(article.getDescription());
        tvText.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_headline:
            case R.id.tv_text:
            case R.id.iv_image:
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("url", article.getUrl());
                startActivity(intent);
                break;
        }
    }

    class ImageAsyncTask extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            return Fetcher.getInstance().loadImage(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ivImage.setImageBitmap(bitmap);
        }
    }

}
