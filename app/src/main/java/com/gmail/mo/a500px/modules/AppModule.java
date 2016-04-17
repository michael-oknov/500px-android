package com.gmail.mo.a500px.modules;

import com.gmail.mo.a500px.DataManager;
import com.gmail.mo.a500px.PhotoApp;
import com.gmail.mo.a500px.http.RestService;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(injects = PhotoApp.class, library = true, includes = NetworkModule.class)
public class AppModule {
  private PhotoApp app;

  public AppModule(PhotoApp app) {
    this.app = app;
  }

  @Provides
  @Singleton
  PhotoApp providGroceryApplication() {
    return app;
  }

  @Provides
  @Singleton
  DataManager provideDataManager(RestService service) {
    return new DataManager(service);
  }
}
