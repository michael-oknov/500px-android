package com.gmail.mo.a500px.modules;

import com.gmail.mo.a500px.http.RestService;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(library = true, complete = false)
public class NetworkModule {

  @Provides
  @Singleton
  @PhotoApiUrl
  String providePhotoEndpoint() {
    return "https://api.500px.com";
  }

  @Provides
  @Singleton
  RestService provideRestService(Retrofit restAdapter) {
    return restAdapter.create(RestService.class);
  }

  @Provides
  @Singleton
  Retrofit provideRestAdapter(@PhotoApiUrl String baseUrl, OkHttpClient httpClient) {
    Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .client(httpClient);
    return retrofitBuilder.build();
  }

  @Provides
  @Singleton
  OkHttpClient provideHttpClient() {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
    return client;
  }
}
