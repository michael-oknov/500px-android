package com.gmail.mo.a500px;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import butterknife.ButterKnife;
import com.fivehundredpx.greedolayout.GreedoLayoutManager;
import com.fivehundredpx.greedolayout.GreedoSpacingItemDecoration;
import com.gmail.mo.a500px.http.RestService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
  private PhotosAdapter recyclerAdapter;

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

    // Set the max row height in pixels
    layoutManager.setMaxRowHeight(300);

    int spacing = 28;//px
    recyclerView.addItemDecoration(new GreedoSpacingItemDecoration(spacing));
    Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl("https://api.500px.com")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .client(new OkHttpClient.Builder().build());
    loadImages(retrofitBuilder.build());
  }

  private void loadImages(Retrofit retrofit) {
    RestService service = retrofit.create(RestService.class);
    service.getPhotos()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(answer -> {
          recyclerAdapter.setDataset(answer.getPhotos());
        }, Throwable::printStackTrace);
  }
}
