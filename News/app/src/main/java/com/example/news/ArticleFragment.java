package com.example.news;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleFragment extends Fragment {

    public ArticleFragment() {
        // Required empty public constructor
    }


    public static ArticleFragment newInstance(Article article, int index, int max)
    {
        ArticleFragment a = new ArticleFragment();
        Bundle bdl = new Bundle(1);
        bdl.putSerializable("ARTICLE_DATA", article);
        bdl.putSerializable("INDEX", index);
        bdl.putSerializable("TOTAL_COUNT", max);
        a.setArguments(bdl);
        return a;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment_layout = inflater.inflate(R.layout.fragment_article, container, false);


        final Article currentArticle = (Article) getArguments().getSerializable("ARTICLE_DATA");
        int index = getArguments().getInt("INDEX");
        int total = getArguments().getInt("TOTAL_COUNT");

        TextView headline = fragment_layout.findViewById(R.id.headline);
        headline.setText(currentArticle.getTitle());

        TextView date = fragment_layout.findViewById(R.id.date);
        // // 2019-04-26T12:33:00Z
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        // Month Day, Year (24 Hr Time)
        // Nov 01, 2018 14:57
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy HH:mm", Locale.ENGLISH);
        LocalDateTime local = LocalDateTime.parse(currentArticle.getDate(), inputFormatter);
        String formattedDate = outputFormatter.format(local);
        Log.d(TAG, "onCreateView: " + formattedDate);
        date.setText(formattedDate);

        TextView author = fragment_layout.findViewById(R.id.author);
        author.setText(currentArticle.getAuthor().split(",")[0]);

        TextView text = fragment_layout.findViewById(R.id.text);
        text.setText(currentArticle.getDescription());

        TextView pageNum = fragment_layout.findViewById(R.id.page_num);
        pageNum.setText(String.format(Locale.US, "%d of %d", index, total));

        ImageView imageView = fragment_layout.findViewById(R.id.imageView);
        //imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        String urlToImage = currentArticle.getUrlToImage();
        if(urlToImage.equals("null")) {
            imageView.setImageResource(R.drawable.no_image_available);
        }
        else {
            Picasso.get().load(urlToImage).into(imageView);
        }

        ConstraintLayout cL = fragment_layout.findViewById(R.id.page);
        final String url = currentArticle.getUrl();

        cL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                website(url);
            }
        });

        return fragment_layout;
    }

    public void website(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }


}
