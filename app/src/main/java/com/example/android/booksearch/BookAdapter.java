package com.example.android.booksearch;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link BookAdapter} is an {@link android.widget.ArrayAdapter} that can provide the layout for each list item
 * based on a data source, which is a list of {@link Book} objects.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    /**
     * Create a new {@link BookAdapter} object.
     *
     * @param context is the current context (i.e. Activity) that the adapter is being created in.
     * @param books   is the list of {@link Book}s to be displayed.
     */
    public BookAdapter(Activity context, List<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_grid_item, parent, false);
        }

        // Get the {@link Book} object located at this position in the list
        Book currentBook = getItem(position);

        // Find the TextView in the book_grid_item.xml layout with the ID version_name
        TextView bookTitleView = (TextView) listItemView.findViewById(R.id.book_title);
        // Display the title of the book in that TextView
        assert currentBook != null;
        bookTitleView.setText(currentBook.getmBookTitle());

        // Find the TextView in the book_grid_item.xml layout with the ID version_name
        TextView bookAuthorView = (TextView) listItemView.findViewById(R.id.book_author);
        // Display the author of the book in that TextView
        bookAuthorView.setText(currentBook.getmBookAuthor());

        // Find the ImageView in the book_grid_item.xml layout with the ID version_name
        final ImageView bookCoverImage = (ImageView) listItemView.findViewById(R.id.book_cover);

        // Check if a retrieved an image from Google Books API and it's no empty
        if (currentBook.getmBookCoverImage() != null && !currentBook.getmBookCoverImage().isEmpty()) {
            // Use the Picasso library to download cover book image and set it in the ImageView
            Picasso.with(getContext()).load(currentBook.getmBookCoverImage()).into(bookCoverImage);
        } else {
            // If no image is retrieved, it's set a dummy book cover in the ImageView
            bookCoverImage.setImageResource(R.drawable.no_book_cover);
        }

        // Return the whole list item layout (containing 5 TextViews, 1 RatingBar adn 1 ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }
}
