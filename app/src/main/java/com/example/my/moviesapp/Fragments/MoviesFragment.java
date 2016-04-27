package com.example.my.moviesapp.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.my.moviesapp.Activities.MainActivity;
import com.example.my.moviesapp.Activities.MovieDetails;
import com.example.my.moviesapp.Constants.Constants;
import com.example.my.moviesapp.Adapters.ImageAdapter;
import com.example.my.moviesapp.DataBase.FavoriteDBParser;
import com.example.my.moviesapp.Models.Movie;
import com.example.my.moviesapp.Network.MoviesDataTask;
import com.example.my.moviesapp.R;
import com.example.my.moviesapp.Utilities.Parser;
import com.example.my.moviesapp.Utilities.Sender;
import com.example.my.moviesapp.Utilities.WorkWithSharedPreference;

import java.util.ArrayList;
import java.util.Collection;


public class MoviesFragment extends Fragment {

    GridView gridView_movies;
    ArrayAdapter<Movie> moviesAdapter;
    ArrayList<Movie> MoviesParser;
    String sortBy;
    FavoriteDBParser favoriteDBParser;
    ProgressBar progressBar;
    boolean isTablet;

    private static final String SELECTED_KEY = "selected_position";
    private int mPosition = GridView.INVALID_POSITION;

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Movie movie);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView_movies = (GridView) rootView.findViewById(R.id.gridView_movies);
        moviesAdapter = new ImageAdapter(getActivity(), R.layout.content_gridview);

        MoviesParser = new ArrayList<>();
        gridView_movies.setAdapter(moviesAdapter);
        favoriteDBParser = new FavoriteDBParser(getContext());
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        gridView_movies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movieData = (Movie) parent.getItemAtPosition(position);
                mPosition = position;
                ((Callback) getActivity()).onItemSelected(movieData);

            }
        });


        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
            gridView_movies.requestFocusFromTouch();
            gridView_movies.setSelection(mPosition);
            gridView_movies.performItemClick(gridView_movies.getAdapter().getView(mPosition, null, null), mPosition, mPosition);
        }


        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onResume() {
        super.onResume();
        sortBy = WorkWithSharedPreference.getDataFromShared(getContext());
        moviesAdapter.clear();
        if (sortBy.equals(Constants.FAVORITE)) {
            MoviesParser = favoriteDBParser.displayMovies();
            moviesAdapter.addAll(MoviesParser);
            moviesAdapter.notifyDataSetChanged();
            if (MoviesParser.size() == 0) {
                Toast.makeText(getContext(), "No favorite found", Toast.LENGTH_SHORT).show();
            } else {
                if(MainActivity.mTwoPane) {
                    gridView_movies.requestFocusFromTouch();
                    gridView_movies.setSelection(0);
                    gridView_movies.performItemClick(gridView_movies.getAdapter().getView(0, null, null), 0, 0);
                }
            }
        } else {
            getMovies(sortBy);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        if (sortBy.equals(Constants.POPULAR))
            menu.findItem(R.id.menu_popular).setChecked(true);
        else if (sortBy.equals(Constants.TOP_RATE))
            menu.findItem(R.id.menu_top_rated).setChecked(true);
        else if (sortBy.equals(Constants.FAVORITE))
            menu.findItem(R.id.menu_favorite).setChecked(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_popular:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                    moviesAdapter.clear();
                    sortBy = Constants.POPULAR;
                    getMovies(Constants.POPULAR);
                    WorkWithSharedPreference.setDataToShared(getContext(), Constants.POPULAR);
                }
                return true;
            case R.id.menu_top_rated:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                    moviesAdapter.clear();
                    sortBy = Constants.TOP_RATE;
                    getMovies(Constants.TOP_RATE);
                    WorkWithSharedPreference.setDataToShared(getContext(), Constants.TOP_RATE);
                }
                return true;
            case R.id.menu_favorite:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                    MoviesParser = favoriteDBParser.displayMovies();
                    WorkWithSharedPreference.setDataToShared(getContext(), Constants.FAVORITE);
                    sortBy = Constants.FAVORITE;
                    moviesAdapter.clear();
                    moviesAdapter.addAll(MoviesParser);
                    moviesAdapter.notifyDataSetChanged();
                    if (MoviesParser.size() == 0) {
                        Toast.makeText(getContext(), "No favorite found", Toast.LENGTH_SHORT).show();
                    } else {
                        gridView_movies.requestFocusFromTouch();
                        gridView_movies.setSelection(0);
                        gridView_movies.performItemClick(gridView_movies.getAdapter().getView(0, null, null), 0, 0);
                    }
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getMovies(String sort) {
        progressBar.setVisibility(View.VISIBLE);
        MoviesDataTask moviesDataTask = new MoviesDataTask(sort, Constants.TYPE_MOVIES, new Sender() {
            @Override
            public void sendData(String moviesData) {
                progressBar.setVisibility(View.GONE);
                if (moviesData != null) {
                    MoviesParser = Parser.parseApiData(moviesData);
                    moviesAdapter.clear();
                    moviesAdapter.addAll(MoviesParser);
                    moviesAdapter.notifyDataSetChanged();
                    if(MainActivity.mTwoPane) {
                        gridView_movies.requestFocusFromTouch();
                        gridView_movies.setSelection(0);
                        gridView_movies.performItemClick(gridView_movies.getAdapter().getView(0, null, null), 0, 0);
                    }
                } else {
                    Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        moviesDataTask.execute();
    }

}
