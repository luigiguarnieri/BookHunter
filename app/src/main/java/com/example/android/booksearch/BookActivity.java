package com.example.android.booksearch;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.example.android.booksearch.R.id.grid;


public class BookActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = BookActivity.class.getName();

    /**
     * URL for search book data from the Google Books API
     */
    private static final String BOOKS_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=";

    /**
     * Adapter for the grid of books
     */
    private BookAdapter mAdapter;

    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;

    /**
     * TextView that is displayed when the grid is empty
     */
    private TextView mEmptyStateTextView;

    /**
     * View that show a progress bar while data are received from Google Books API
     */
    private View mLoadingIndicator;

    /**
     * GridView that is filled with book results when app is launched
     * and when a search is performed using a keyword into the SearchView.
     */
    private GridView mBookGridView;

    /**
     * String to query Google Books API when app is launched
     */
    public String query = "";

    /**
     * String to query Google Books API when user put his keyword(s) in the SearchView
     */
    public String searchQuery = "";

    /**
     * Initial state of boolean variable searching necessary for SearchView
     */
    public boolean searching = false;

    /**
     * Initial and final index to add to searchQuery to set how many results to show
     */
    int startIndex = 0;
    int endIndex = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_activity);

        // Get the actual date to use it as keyword for the initial query
        // of the app when it's launched. In this way user sees results about
        // books published in this date or related with the actual date
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        // Set actual date in the format "yyyy-MM-dd" (i.e. "2017-06-21") and
        // put its value into the String formattedDate
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String formattedDate = formatDate.format(cal.getTime());

        // Find a reference to the {@link GridView} in the layout
        mBookGridView = (GridView) findViewById(grid);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

        mLoadingIndicator = findViewById(R.id.loading_indicator);


        assert mBookGridView != null;
        mBookGridView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty grid of books as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link GridView}
        // so the grid can be populated in the user interface
        mBookGridView.setAdapter(mAdapter);

        // Check if network connectivity is available
        if (deviceIsConnected()) {
            // Set the initial query to show results about actual date when app is launched
            query = BOOKS_REQUEST_URL + formattedDate + "&orderBy=newest&maxResults=12";
            getLoaderManager().initLoader(BOOK_LOADER_ID, null, this).forceLoad();
        } else {
            // If network connectivity isn't available, it's shown a message
            // saying "No internet connection"
            mLoadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setText(R.string.no_network_connection);
        }

        // Set an item click listener on the GridView, which sends an intent to BookDetailActivity
        // to put all the retrieved data about a book to this activity
        mBookGridView.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position,
                                    long l) {
                // Find the current book that was clicked on
                Book currentBook = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                assert currentBook != null;

                // Pass title, author, coverImage, publisher, publishedDate, numberPages, rating,
                // description and url about clicked book to the BookDetailActivity
                Intent newIntent = new Intent(view.getContext(), BookDetailActivity.class);
                newIntent.putExtra("mBookTitle", currentBook.getmBookTitle());
                newIntent.putExtra("mBookAuthor", currentBook.getmBookAuthor());
                newIntent.putExtra("mBookCoverImage", currentBook.getmBookCoverImage());
                newIntent.putExtra("mBookPublisher", currentBook.getmBookPublisher());
                newIntent.putExtra("mBookPublishedDate", currentBook.getmBookPublishedDate());
                newIntent.putExtra("mBookNumberPages", currentBook.getmBookNumberPages());
                newIntent.putExtra("mBookRating", currentBook.getmBookRating());
                newIntent.putExtra("mBookDescription", currentBook.getmBookDescription());
                newIntent.putExtra("mBookUrl", currentBook.getmBookUrl());

                startActivityForResult(newIntent, 0);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_grid, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //search query
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform a search only if network connectivity is available
                if (deviceIsConnected()) {
                    searching = true;
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    String clientQuery = searchView.getQuery().toString();
                    // For the query every blank space that user puts in the SearchView
                    // is translated as "+" (valid operator for the query)
                    clientQuery = clientQuery.replace(" ", "+");
                    searchQuery = "https://www.googleapis.com/books/v1/volumes?q=" + clientQuery.trim() + "&orderBy=relevance&maxResults=40&startIndex=" + startIndex + "&endIndex=" + endIndex;

                    Log.v(LOG_TAG, clientQuery);
                    getLoaderManager().restartLoader(BOOK_LOADER_ID, null, BookActivity.this);
                    searchView.clearFocus();
                } else {
                    // If network connectivity isn't available, it's shown a message
                    // saying "No internet connection"
                    mLoadingIndicator.setVisibility(View.GONE);
                    mEmptyStateTextView.setVisibility(View.VISIBLE);
                    mBookGridView.setVisibility(View.INVISIBLE);
                    mEmptyStateTextView.setText(R.string.no_network_connection);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    // Helper method to verify if network connectivity is available
    public boolean deviceIsConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        if (searching) {
            return new BookLoader(this, searchQuery);
        }
        return new BookLoader(this, query);
    }


    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        // Hide loading indicator because the data has been loaded
        mLoadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No book found."
        mEmptyStateTextView.setText(R.string.empty_view);

        // Clear the adapter of previous book data
        mAdapter.clear();

        // If there is a valid grid of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the GridView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}
