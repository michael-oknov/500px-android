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
  private List<Photo> photos = new ArrayList<>();

  public DataManager(RestService restService) {
    this.restService = restService;
    download();
  }

  private void updatePhotos(List<Photo> newPhotos) {
    this.photos.addAll(newPhotos);
    subject.onNext(newPhotos);
  }

  private void download() {
    subscriptions.add(restService.getPhotos(mPage)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(answer -> updatePhotos(answer.getPhotos()), Throwable::printStackTrace));
  }

  public void loadMore() {
    mPage++;
    download();
  }

  public List<Photo> getPhotos() {
    return photos;
  }

  public Photo getPhoto(int position) {
    if (position > photos.size() - 3) loadMore();
    return photos.get(position);
  }

  public Observable<List<Photo>> subscribeNewChanges() {
    return subject.asObservable();
  }
}
