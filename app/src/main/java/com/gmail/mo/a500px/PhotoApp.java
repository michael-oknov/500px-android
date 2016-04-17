package com.gmail.mo.a500px;

import com.fizzbuzz.android.dagger.InjectingApplication;
import com.gmail.mo.a500px.modules.AppModule;
import com.squareup.leakcanary.LeakCanary;
import java.util.List;

public class PhotoApp extends InjectingApplication {
  @Override
  public void onCreate() {
    super.onCreate();
    LeakCanary.install(this);
  }

  @Override
  protected List<Object> getModules() {
    List<Object> modules = super.getModules();
    modules.add(new AppModule(this));
    return modules;
  }
}
