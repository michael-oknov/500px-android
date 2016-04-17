package com.gmail.mo.a500px.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.gmail.mo.a500px.R;
import com.gmail.mo.a500px.models.Photo;
import com.squareup.picasso.Picasso;

public class PhotoView extends RelativeLayout {
  private final Picasso picasso;
  @Bind(R.id.photo_view) uk.co.senab.photoview.PhotoView imageView;
  @Bind(R.id.author_title) TextView authorView;
  @Bind(R.id.camera_title) TextView cameraView;
  @Bind(R.id.photo_title) TextView nameView;

  public PhotoView(Context context) {
    super(context);
    LayoutInflater.from(context).inflate(R.layout.layout_photo, this, true);
    ButterKnife.bind(this);
    picasso = Picasso.with(context);
  }

  public void setPhoto(Photo photo) {
    picasso.load(photo.getImageUrl()).into(imageView);
    nameView.setText(getResources().getString(R.string.photo_title, photo.getName()));
    cameraView.setText(getResources().getString(R.string.camera_title, photo.getCameraName()));
    authorView.setText(
        getResources().getString(R.string.author_title, photo.getUser().getFullname()));
  }
}
