package com.gmail.mo.a500px.http;

import com.gmail.mo.a500px.models.Answer;
import retrofit2.http.GET;
import rx.Observable;

public interface RestService {

  @GET("v1/photos?feature=popular&consumer_key=wB4ozJxTijCwNuggJvPGtBGCRqaZVcF6jsrzUadF")
  Observable<Answer> getPhotos();

}
