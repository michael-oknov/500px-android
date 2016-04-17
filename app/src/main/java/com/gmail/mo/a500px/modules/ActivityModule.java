package com.gmail.mo.a500px.modules;

import com.gmail.mo.a500px.ui.DetailsActivity;
import com.gmail.mo.a500px.ui.MainActivity;
import dagger.Module;

@Module(injects = { MainActivity.class, DetailsActivity.class }, library = false,
        addsTo = AppModule.class)
public class ActivityModule {
}
