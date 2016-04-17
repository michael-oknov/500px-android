package com.gmail.mo.a500px;

import com.gmail.mo.a500px.http.RestService;
import com.gmail.mo.a500px.models.Photo;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

public class DataManager {
  private final PublishSubject<List<Photo>> subject = PublishSubject.create();
  private final CompositeSubscription subscriptions = new CompositeSubscription();
  private final RestService restService;
  private int mPage = 1;
  private boolean isDownloading;
  private List<Photo> photos = new ArrayList<>();

  public DataManager(RestService restService) {
    this.restService = restService;
    download();
  }

  private void updatePhotos(List<Photo> newPhotos) {
    isDownloading = false;
    this.photos.addAll(newPhotos);
    subject.onNext(newPhotos);
  }

  private void download() {
    isDownloading = true;
    subscriptions.add(restService.getPhotos(mPage)
        .doOnError(Throwable::printStackTrace)
        .retry()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(answer -> updatePhotos(answer.getPhotos())));
  }

  public void loadMore() {
    mPage++;
    download();
  }

  public List<Photo> getPhotos() {
    return photos;
  }

  public Photo getPhoto(int position) {
    try {
      if (position > photos.size() - 3 && !isDownloading) loadMore();
      return photos.get(position);
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }

  public Observable<List<Photo>> subscribeNewChanges() {
    return subject.asObservable();
  }
}
