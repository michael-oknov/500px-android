package com.gmail.mo.a500px;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.fivehundredpx.greedolayout.GreedoLayoutSizeCalculator;
import com.gmail.mo.a500px.models.Photo;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> implements
    GreedoLayoutSizeCalculator.SizeCalculatorDelegate {

  List<Photo> photos = new ArrayList<>();
  private double[] mImageAspectRatios = new double[0];

  private Context mContext;

  @Override
  public double aspectRatioForIndex(int index) {
    if (index >= getItemCount()) return 1.0;
    return mImageAspectRatios[getLoopedIndex(index)];
  }

  public class PhotoViewHolder extends RecyclerView.ViewHolder {
    private ImageView mImageView;
    public PhotoViewHolder(ImageView imageView) {
      super(imageView);
      mImageView = imageView;
      itemView.setOnClickListener(v->{
        Photo photo = photos.get(getAdapterPosition());
        Intent intent = new Intent(mContext, DetailsActivity.class);
        int[] screenLocation = new int[2];
        imageView.getLocationOnScreen(screenLocation);

        //Pass the image title and url to DetailsActivity
        intent.putExtra("left", screenLocation[0]).
            putExtra("top", screenLocation[1]).
            putExtra("width", imageView.getWidth()).
            putExtra("height", imageView.getHeight()).
            putExtra("title", photo.getName()).
            putExtra("image", photo.getImageUrl());

        mContext.startActivity(intent);
      });
    }
  }

  public PhotosAdapter(Context context) {
    mContext = context;
  }

  @Override
  public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    ImageView imageView = new ImageView(mContext);
    imageView.setScaleType(ImageView.ScaleType.FIT_XY);

    imageView.setLayoutParams(new ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    ));
    return new PhotoViewHolder(imageView);
  }

  public void setDataset(List<Photo> photos){
    this.photos.clear();
    this.photos.addAll(photos);
    mImageAspectRatios = new double[this.photos.size()];

    calculateImageAspectRatios();
    notifyDataSetChanged();
  }

  public void addAll(List<Photo> photos){
    int startPosition = this.photos.size();
    this.photos.addAll(photos);
    mImageAspectRatios = new double[this.photos.size()];

    calculateImageAspectRatios();
    notifyItemRangeInserted(startPosition, photos.size());
  }

  @Override
  public void onBindViewHolder(PhotoViewHolder holder, int position) {
    Picasso.with(mContext)
        .load(photos.get(getLoopedIndex(position)).getImageUrl())
        .fit()
        .into(holder.mImageView);
  }

  @Override
  public int getItemCount() {
    return photos.size();
  }

  private void calculateImageAspectRatios() {
    for (int i = 0; i < photos.size(); i++) {
      mImageAspectRatios[i] = photos.get(i).getWidth() / (double) photos.get(i).getHeight();
    }
  }

  // Index gets wrapped around <code>Constants.IMAGES.length</code> so we can loop content.
  private int getLoopedIndex(int index) {
    return index % photos.size(); // wrap around
  }
}
