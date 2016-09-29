package com.silmood.spotify_streamer.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.silmood.spotify_streamer.R;
import com.silmood.spotify_streamer.SpotifyStreamerComponent;
import com.silmood.spotify_streamer.common.BaseFragment;
import com.silmood.spotify_streamer.common.BasePresenter;
import com.silmood.spotify_streamer.component.DaggerArtistSearchComponent;
import com.silmood.spotify_streamer.domain.Artist;
import com.silmood.spotify_streamer.module.ArtistSearchModule;
import com.silmood.spotify_streamer.presenter.ArtistSearchPresenter;
import com.silmood.spotify_streamer.ui.adapter.SearchResultsAdapter;
import com.silmood.spotify_streamer.ui.viewmodel.ArtistSearchView;
import com.silmood.spotify_streamer.ui.view.ClearableEditText;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnTextChanged;


/**
 * Created by Pedro Antonio Hernández on 14/06/2015.
 *
 * <p>
 *     Provide a UI that allow the user to
 *     issue a search for an artist.
 * </p>
 */
public class ArtistSearchFragment extends BaseFragment implements ArtistSearchView, SearchResultsAdapter.ItemClickListener {
    public static final String LOG_TAG = ArtistSearchFragment.class.getSimpleName();

    @Inject
    ArtistSearchPresenter mSearchPresenter;

    @Inject
    SearchResultsAdapter mResultsAdapter;

    @Bind(R.id.etxt_search)
    ClearableEditText mArtistSearchInput;

    @Bind(R.id.list_artist)
    RecyclerView mArtistResultsList;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_artist_search;
    }

    @Override
    protected BasePresenter getPresenter() {
        return mSearchPresenter;
    }

    @Override
    protected void setUpComponent(SpotifyStreamerComponent component) {
        DaggerArtistSearchComponent.builder()
                .spotifyStreamerComponent(component)
                .artistSearchModule(new ArtistSearchModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setupList() {
        mArtistResultsList.setLayoutManager(new LinearLayoutManager(CONTEXT));
        mArtistResultsList.setAdapter(mResultsAdapter);
    }

    @Override
    public void setupAdapter() {
        mResultsAdapter.setOnItemClickListener(this);
    }

    @OnTextChanged(R.id.etxt_search)
    public void onQueryChanged(CharSequence query){
        if (query.length() >= 3)
            mSearchPresenter.searchArtists(query.toString());
        else if (query.length() <= 2)
            mResultsAdapter.clear();
    }

    @Override
    public void displayFoundArtists(ArrayList<Artist> artists) {
        mResultsAdapter.replace(artists);
    }

    @Override
    public void displayFailedSearch() {
        Toast.makeText(CONTEXT, R.string.failed_search, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayNetworkError() {
        Toast.makeText(CONTEXT, R.string.network_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayServerError() {
        Toast.makeText(CONTEXT, R.string.server_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClicked(int position) {
        //Launch artist detail
    }
}
