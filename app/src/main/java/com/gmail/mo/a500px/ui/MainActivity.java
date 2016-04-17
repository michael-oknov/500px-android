package com.gmail.mo.a500px.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import butterknife.ButterKnife;
import com.fivehundredpx.greedolayout.GreedoLayoutManager;
import com.fivehundredpx.greedolayout.GreedoSpacingItemDecoration;
import com.gmail.mo.a500px.DataManager;
import com.gmail.mo.a500px.PhotosAdapter;
import com.gmail.mo.a500px.R;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {
  @Inject DataManager mDataManager;
  private PhotosAdapter recyclerAdapter;
  private boolean mScrolledToLastItem;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    recyclerAdapter = new PhotosAdapter(this);
    GreedoLayoutManager layoutManager = new GreedoLayoutManager(recyclerAdapter);

    RecyclerView recyclerView = ButterKnife.findById(this, R.id.recylcer_view);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(recyclerAdapter);

    layoutManager.setMaxRowHeight(500);//px

    int spacing = 2;//px
    recyclerView.addItemDecoration(new GreedoSpacingItemDecoration(spacing));
    mDataManager.subscribeNewChanges()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(photos -> {
          mScrolledToLastItem = false;
          recyclerAdapter.addAll(photos);
        }, Throwable::printStackTrace);

    recyclerAdapter.setDataset(mDataManager.getPhotos());

    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

        if (!mScrolledToLastItem) {
          if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
            mScrolledToLastItem = true;
            mDataManager.loadMore();
          }
        }
      }
    });
  }
}
