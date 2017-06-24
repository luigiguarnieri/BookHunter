package com.example.android.booksearch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class BookDetailActivity extends AppCompatActivity {

    /**
     * Constant value for the word "pages" to add in the TextView
     * bookNumberPages to obtain the complete locution "XXX pages"
     * if numberPages is different from zero.
     */
    private static final String PAGES_WORD = " pages";


    /** Tag for log messages */
    private static final String LOG_TAG = BookDetailActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_detail_activity);

        // Get all data passed from BookActivity located
        // in the position where user has clicked
        String title = getIntent().getStringExtra("mBookTitle");
        String author = getIntent().getStringExtra("mBookAuthor");
        String bookCover = getIntent().getStringExtra("mBookCoverImage");
        String publisher = getIntent().getStringExtra("mBookPublisher");
        String publishedDate = getIntent().getStringExtra("mBookPublishedDate");
        int numberPages = getIntent().getIntExtra("mBookNumberPages", 0);
        double rating = getIntent().getDoubleExtra("mBookRating", 0);
        String description = getIntent().getStringExtra("mBookDescription");
        final String url = getIntent().getStringExtra("mBookUrl");


        // Find the TextView in the book_detail_activity.xml layout with the ID version_name
        TextView bookTitleView = (TextView) findViewById(R.id.book_title);
        // Display the title of the book in that TextView
        bookTitleView.setText(title);

        // Find the TextView in the book_detail_activity.xml layout with the ID version_name
        TextView bookAuthorView = (TextView) findViewById(R.id.book_author);
        // Display the author of the book in that TextView
        bookAuthorView.setText(author);

        // Find the TextView in the book_detail_activity.xml layout with the ID version_name
        TextView bookPublisherView = (TextView) findViewById(R.id.book_publisher);
        // Display the publisher of the book in that TextView
        bookPublisherView.setText(publisher);

        // Find the TextView in the book_detail_activity.xml layout with the ID version_name
        TextView bookPublishedDateView = (TextView) findViewById(R.id.book_publish_date);

        // Format the date retrieved with JSON parsing from ISO 8601 format to a
        // local format (i.e. "Jun 21, 2017").
        String finalDate = publishedDate;
        try {
            SimpleDateFormat startFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat finalFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
            finalDate = finalFormat.format(startFormat.parse(publishedDate));
        } catch (java.text.ParseException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            e.printStackTrace();
            Log.e(LOG_TAG, "problem with date formatting", e.getCause());
        }

        // Display the published date of the book in that TextView
        bookPublishedDateView.setText(finalDate);

        // Find the TextView in the book_detail_activity.xml layout with the ID version_name
        TextView bookNumberPagesView = (TextView) findViewById(R.id.book_pages);
        String bookPages = String.valueOf(numberPages);
        // Check if number of pages is different from zero to set the correct visualization
        if (numberPages != 0) {
            bookPages += PAGES_WORD;
            // Display the number of pages the book in that TextView
            bookNumberPagesView.setText(bookPages);
        } else {
            String noBookPages = "";
            // Display a blank value in that TextView
            bookNumberPagesView.setText(noBookPages);
        }

        // Find the TextView in the book_detail_activity.xml layout with the ID version_name
        RatingBar bookRatingView = (RatingBar) findViewById(R.id.book_rating);
        // Display the rating of the book in that TextView
        bookRatingView.setRating((float) rating);

        // Find the TextView in the book_detail_activity.xml layout with the ID version_name
        TextView bookDescriptionView = (TextView) findViewById(R.id.book_description);
        // Display the description of the book in that TextView
        bookDescriptionView.setText(description);

        // Find the ImageView in the book_detail_activity.xml layout with the ID version_name
       ImageView bookCoverImage = (ImageView) findViewById(R.id.book_cover);

        // Check if a retrieved an image from Google Books API and if it's no empty
        if (bookCover != null && !bookCover.isEmpty()) {
            // Use the Picasso library to download cover book image and set it in the ImageView
            Picasso.with(this).load(bookCover).into(bookCoverImage);
        } else {
            // If no image is retrieved, it's set a dummy book cover in the ImageView
            bookCoverImage.setImageResource(R.drawable.no_book_cover);
        }

        // Set OnClickListener on bookCoverImage to open Google Books web page
        // related with the book when user touches book cover image
        bookCoverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri bookUri = Uri.parse(url);

                // Create a new intent to view the book URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
    }
}
