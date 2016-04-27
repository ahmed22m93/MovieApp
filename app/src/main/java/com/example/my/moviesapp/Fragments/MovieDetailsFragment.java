package com.example.my.moviesapp.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my.moviesapp.Adapters.ReviewsAdapter;
import com.example.my.moviesapp.Adapters.TrailersAdapter;
import com.example.my.moviesapp.Constants.Constants;
import com.example.my.moviesapp.DataBase.FavoriteDBParser;
import com.example.my.moviesapp.Models.Movie;
import com.example.my.moviesapp.Models.Review;
import com.example.my.moviesapp.Network.MoviesDataTask;
import com.example.my.moviesapp.R;
import com.example.my.moviesapp.Utilities.Parser;
import com.example.my.moviesapp.Utilities.Sender;
import com.example.my.moviesapp.Utilities.WorkWithSharedPreference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MovieDetailsFragment extends Fragment {

    Movie movieData;
    TextView movieName_details, movieDate_details, movieOverview_details, trailers_label, reviews_label;
    ImageView movieImage_details, favoriteImage;
    RatingBar movieRating_details;
    ListView trailers_list, reviews_list;
    ArrayAdapter trailersAdapter, reviewsAdapter;
    int favoriteFlag;
    String isFavorite;
    FavoriteDBParser favoriteDBParser;
    ArrayList<String> trailerParser;
    ArrayList<Review> reviewParser;
    ScrollView movie_details_layout;

    public static final String DETAIL_MOVIE = "MOVIE";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        movieName_details = (TextView) rootView.findViewById(R.id.movieName_details);
        movieImage_details = (ImageView) rootView.findViewById(R.id.movieImage_details);
        movieRating_details = (RatingBar) rootView.findViewById(R.id.movieRating_details);
        movieDate_details = (TextView) rootView.findViewById(R.id.movieDate_details);
        movieOverview_details = (TextView) rootView.findViewById(R.id.movieOverview_details);
        favoriteImage = (ImageView) rootView.findViewById(R.id.favoriteImage);
        favoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (favoriteFlag) {
                    case 1: { //Favorite
                        favoriteImage.setImageResource(R.drawable.ic_star_black);
                        favoriteFlag = 2;
                        int checkAddMovie = favoriteDBParser.addMovie(movieData);
                        if (checkAddMovie == 1) {
                            if (trailerParser.size() > 0) {
                                favoriteDBParser.addMovieTrailers(movieData.getId(), trailerParser);
                            }
                            if (reviewParser.size() > 0) {
                                favoriteDBParser.addMovieReviews(movieData.getId(), reviewParser);
                            }
                            Toast.makeText(getContext(), "Add to favorite", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    }
                    case 2: { //unFavorite
                        favoriteImage.setImageResource(R.drawable.ic_star_border_black);
                        favoriteFlag = 1;
                        int checkRemoveMovie = favoriteDBParser.deleteMovie(movieData.getId());
                        if (checkRemoveMovie == 1) {
                            if (trailerParser.size() > 0) {
                                favoriteDBParser.deleteMovieTrailers(movieData.getId());
                            }
                            if (reviewParser.size() > 0) {
                                favoriteDBParser.deleteMovieReviews(movieData.getId());
                            }
                            Toast.makeText(getContext(), "Remove from favorite", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }
            }
        });

        trailers_label = (TextView) rootView.findViewById(R.id.trailers_label);
        trailers_list = (ListView) rootView.findViewById(R.id.trailers_list);
        trailersAdapter = new TrailersAdapter(getContext(), R.layout.trailer_row);
        trailers_list.setAdapter(trailersAdapter);
        trailers_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String restUrl = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.BASE_YOUTUBE_URL + restUrl));
                startActivity(intent);
            }
        });


        reviews_label = (TextView) rootView.findViewById(R.id.reviews_label);
        reviews_list = (ListView) rootView.findViewById(R.id.reviews_list);
        reviewsAdapter = new ReviewsAdapter(getContext(), R.layout.review_row);
        reviews_list.setAdapter(reviewsAdapter);


        Bundle arguments = getArguments();
        if (arguments != null) {
            movieData = (Movie) arguments.getSerializable(DETAIL_MOVIE);
        }


        movie_details_layout = (ScrollView) rootView.findViewById(R.id.movie_details_layout);
        if (movieData != null) {
            movie_details_layout.setVisibility(View.VISIBLE);

            viewMovieDetails();

            favoriteDBParser = new FavoriteDBParser(getContext());
            if (favoriteDBParser.checkMovieExist(movieData.getId())) {
                favoriteFlag = 2;
            } else {
                favoriteFlag = 1;
            }
            setImageFavorite();

            isFavorite = WorkWithSharedPreference.getDataFromShared(getContext());
            if (isFavorite.equals(Constants.FAVORITE)) {
                trailerParser = favoriteDBParser.displayMovieTrailers(movieData.getId());
                if (trailerParser.size() != 0) {
                    trailers_label.setVisibility(View.VISIBLE);
                    int pixels = setTrailersListViewHeightBasedOnChildren();
                    trailers_list.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, pixels));
                    trailersAdapter.addAll(trailerParser);
                    trailersAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Now trailers found", Toast.LENGTH_SHORT).show();
                }

                ///////////////
                reviewParser = favoriteDBParser.displayMovieReviews(movieData.getId());
                if (reviewParser.size() != 0) {
                    reviews_label.setVisibility(View.VISIBLE);
                    reviewsAdapter.addAll(reviewParser);
                    setReviewsListViewHeightBasedOnChildren(reviews_list);
                    reviewsAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getContext(), "Now reviews found", Toast.LENGTH_SHORT).show();
                }
            } else {
                getTrailerData();
                getReviewData();
            }


        } else {
            movie_details_layout.setVisibility(View.INVISIBLE);
        }


        return rootView;
    }


    public void viewMovieDetails() {
        movieName_details.setText(movieData.getTitle());
        Picasso.with(getContext()).load(Constants.BASE_IMAGE_URL + movieData.getPosterPath()).into(movieImage_details);
        movieRating_details.setRating(movieData.getVoteAverage() / 2);
        movieDate_details.setText(movieData.getReleaseDate());
        movieOverview_details.setText(movieData.getOverview());
    }

    public void setImageFavorite() {
        switch (favoriteFlag) {
            case 1: { //unFavorite
                favoriteImage.setImageResource(R.drawable.ic_star_border_black);
                break;
            }
            case 2: { //favorite
                favoriteImage.setImageResource(R.drawable.ic_star_black);
                break;
            }
        }
    }

    //////////////////////////////////////////
    public void getTrailerData() {
        MoviesDataTask getTrailerTask = new MoviesDataTask(Constants.MOVIE_TRAILER, Constants.TYPE_TRAILER, new Sender() {
            @Override
            public void sendData(String TrailersData) {
                if (TrailersData != null) {
                    trailerParser = Parser.parseTrailerData(TrailersData);
                    if (trailerParser.size() != 0) {
                        trailers_label.setVisibility(View.VISIBLE);
                        int pixels = setTrailersListViewHeightBasedOnChildren();
                        trailers_list.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, pixels));
                        trailersAdapter.addAll(trailerParser);
                        trailersAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Now trailers found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "connection Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getTrailerTask.execute(movieData.getId());
    }

    public int setTrailersListViewHeightBasedOnChildren() {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (61 * scale + 0.5f) * trailerParser.size();
        return pixels;
    }

    //////////////////////////////////////////
    public void getReviewData() {
        MoviesDataTask getReviewTask = new MoviesDataTask(Constants.MOVIE_REVIEWS, Constants.TYPE_REVIEWS, new Sender() {
            @Override
            public void sendData(String reviewsData) {
                if (reviewsData != null) {
                    reviewParser = Parser.parseReviewData(reviewsData);
                    if (reviewParser.size() != 0) {
                        reviews_label.setVisibility(View.VISIBLE);
                        reviewsAdapter.addAll(reviewParser);
                        setReviewsListViewHeightBasedOnChildren(reviews_list);

                        reviewsAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Now reviews found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "connection Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getReviewTask.execute(movieData.getId());
    }




    public void setReviewsListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

   /* public void setReviewsListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 350 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int) px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            // Get padding
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding;
            listView.setLayoutParams(params);
            listView.requestLayout();

        }
    }*/
}




