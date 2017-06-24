package com.example.android.booksearch;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * {@link Book} represents a book.
 * It contains all the relevant data about a book: title,
 * author, cover image, publisher, published date, number
 * of pages, readers' rating, description and url of Google
 * Books web page related with it.
 * It implements Parcelable to parsing retrieved data for every book
 * in the GridView from BookActivity to BookDetailActivity.
 */

public class Book implements Parcelable {

    /**
     * Title of the book
     */
    private String mBookTitle;

    /**
     * Author of the book
     */
    private String mBookAuthor;

    /**
     * Url related to the cover image of the book
     */
    private String mBookCoverImage;

    /**
     * Publisher of the book
     */
    private String mBookPublisher;

    /**
     * Published date of the book
     */
    private String mBookPublishedDate;

    /**
     * Number of pages of the book
     */
    private int mBookNumberPages;

    /**
     * Rating of the book
     */
    private double mBookRating;

    /**
     * Description of the book
     */
    private String mBookDescription;

    /**
     * Url of Google Books web page related with the book
     */
    private String mBookUrl;

    /**
     * Create a new Book object
     *
     * @param bookTitle         is the title of the book
     * @param bookAuthor        is the name of the author of the book
     * @param coverImage        is the url related with the cover image (thumbnail format) of the book
     * @param bookPublisher     is the publisher of the author of the book
     * @param publishedDate     is the published date of the author of the book
     * @param bookNumberPages   is the number of pages of the book
     * @param bookRating        is the rating of the book given by readers
     * @param bookDescription   is the description of the book
     * @param bookUrl           is the url of Google Books web page realted with the book
     */
    public Book(String bookTitle, String bookAuthor, String coverImage, String bookPublisher,
                String publishedDate, int bookNumberPages, double bookRating, String bookDescription,
                String bookUrl) {
        mBookTitle = bookTitle;
        mBookAuthor = bookAuthor;
        mBookCoverImage = coverImage;
        mBookPublisher = bookPublisher;
        mBookPublishedDate = publishedDate;
        mBookNumberPages = bookNumberPages;
        mBookRating = bookRating;
        mBookDescription = bookDescription;
        mBookUrl = bookUrl;
}

    protected Book(Parcel in) {
        mBookTitle = in.readString();
        mBookAuthor = in.readString();
        mBookCoverImage = in.readString();
        mBookPublisher = in.readString();
        mBookPublishedDate = in.readString();
        mBookNumberPages = in.readInt();
        mBookRating = in.readDouble();
        mBookDescription = in.readString();
        mBookUrl = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    /**
     * Get the title of the book.
     */
    public String getmBookTitle() {
        return mBookTitle;
    }

    /**
     * Get the author of the book.
     */
    public String getmBookAuthor() {
        return mBookAuthor;
    }

    /**
     * Get the url of the cover image of the book.
     */
    public String getmBookCoverImage() {
        return mBookCoverImage;
    }

    /**
     * Get the publisher of the book.
     */
    public String getmBookPublisher() {
        return mBookPublisher;
    }

    /**
     * Get the published date of the book.
     */
    public String getmBookPublishedDate() {
        return mBookPublishedDate;
    }

    /**
     * Get the number of pages of the book.
     */
    public int getmBookNumberPages() {
        return mBookNumberPages;
    }

    /**
     * Get the readers' rating of the book.
     */
    public double getmBookRating() {
        return mBookRating;
    }

    /**
     * Get the description of the book.
     */
    public String getmBookDescription() {
        return mBookDescription;
    }

    /**
     * Get the url of Google Books web page
     * related with the book.
     */
    public String getmBookUrl() {
        return mBookUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mBookTitle);
        dest.writeString(mBookAuthor);
        dest.writeString(mBookCoverImage);
        dest.writeString(mBookPublisher);
        dest.writeString(mBookPublishedDate);
        dest.writeInt(mBookNumberPages);
        dest.writeDouble(mBookRating);
        dest.writeString(mBookDescription);
        dest.writeString(mBookUrl);
    }
}