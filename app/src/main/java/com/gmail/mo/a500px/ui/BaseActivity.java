package com.gmail.mo.a500px.ui;

import butterknife.ButterKnife;
import com.fizzbuzz.android.dagger.InjectingActivity;
import com.gmail.mo.a500px.modules.ActivityModule;
import java.util.List;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseActivity extends InjectingActivity {
  private CompositeSubscription subscriptions = new CompositeSubscription();
  @Override
  public void setContentView(int layoutResID) {
    super.setContentView(layoutResID);
    ButterKnife.bind(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    subscriptions.unsubscribe();
  }

  @Override
  protected List<Object> getModules() {
    List<Object> modules = super.getModules();
    modules.add(new ActivityModule());
    return modules;
  }
}
