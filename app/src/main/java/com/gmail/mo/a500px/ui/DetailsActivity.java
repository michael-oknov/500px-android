package com.gmail.mo.a500px.ui;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import com.gmail.mo.a500px.DataManager;
import com.gmail.mo.a500px.R;
import com.gmail.mo.a500px.models.Photo;
import javax.inject.Inject;

public class DetailsActivity extends BaseActivity {
  private static final int ANIM_DURATION = 600;
  @Inject DataManager mDataManager;
  @Bind(R.id.viewPager) HackyViewPager viewPager;
  private int mLeftDelta;
  private int mTopDelta;
  private float mWidthScale;
  private float mHeightScale;
  private FrameLayout frameLayout;
  private ColorDrawable colorDrawable;
  private int thumbnailTop;
  private int thumbnailLeft;
  private int thumbnailWidth;
  private int thumbnailHeight;
  private int startPosition;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //Setting details screen layout
    setContentView(R.layout.activity_details);

    //retrieves the thumbnail data
    Bundle bundle = getIntent().getExtras();
    thumbnailTop = bundle.getInt("top");
    thumbnailLeft = bundle.getInt("left");
    thumbnailWidth = bundle.getInt("width");
    thumbnailHeight = bundle.getInt("height");
    startPosition = bundle.getInt("position");

    //Set the background color to black
    frameLayout = (FrameLayout) findViewById(R.id.main_background);
    colorDrawable = new ColorDrawable(Color.BLACK);
    frameLayout.setBackground(colorDrawable);

    // Only run the animation if we're coming from the parent activity, not if
    // we're recreated automatically by the window manager (e.g., device rotation)
    viewPager.setAdapter(new SamplePagerAdapter());
    viewPager.setCurrentItem(startPosition);
    if (savedInstanceState == null) {
      ViewTreeObserver observer = viewPager.getViewTreeObserver();
      observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

        @Override
        public boolean onPreDraw() {
          viewPager.getViewTreeObserver().removeOnPreDrawListener(this);

          // Figure out where the thumbnail and full size versions are, relative
          // to the screen and each other
          int[] screenLocation = new int[2];
          viewPager.getLocationOnScreen(screenLocation);
          mLeftDelta = thumbnailLeft - screenLocation[0];
          mTopDelta = thumbnailTop - screenLocation[1];

          // Scale factors to make the large version the same size as the thumbnail
          mWidthScale = (float) thumbnailWidth / viewPager.getWidth();
          mHeightScale = (float) thumbnailHeight / viewPager.getHeight();

          enterAnimation();

          return true;
        }
      });
    }
  }

  @OnClick(R.id.fab)
  public void onFabClicked() {
    Intent sendIntent = new Intent();
    sendIntent.setAction(Intent.ACTION_SEND);
    sendIntent.putExtra(Intent.EXTRA_TEXT,
        mDataManager.getPhoto(viewPager.getCurrentItem()).getImageUrl());
    sendIntent.setType("text/plain");
    startActivity(sendIntent);
  }

  public void enterAnimation() {
    viewPager.setPivotX(0);
    viewPager.setPivotY(0);
    viewPager.setScaleX(mWidthScale);
    viewPager.setScaleY(mHeightScale);
    viewPager.setTranslationX(mLeftDelta);
    viewPager.setTranslationY(mTopDelta);

    // interpolator where the rate of change starts out quickly and then decelerates.
    TimeInterpolator sDecelerator = new DecelerateInterpolator();

    // Animate scale and translation to go from thumbnail to full size
    viewPager.animate().setDuration(ANIM_DURATION).scaleX(1).scaleY(1).
        translationX(0).translationY(0).setInterpolator(sDecelerator);

    // Fade in the black background
    ObjectAnimator bgAnim = ObjectAnimator.ofInt(colorDrawable, "alpha", 0, 255);
    bgAnim.setDuration(ANIM_DURATION);
    bgAnim.start();
  }

  public void exitAnimation(final Runnable endAction) {
    TimeInterpolator sInterpolator = new AccelerateInterpolator();
    viewPager.animate()
        .setDuration(ANIM_DURATION)
        .scaleX(mWidthScale)
        .scaleY(mHeightScale)
        .translationX(mLeftDelta)
        .translationY(mTopDelta)
        .setInterpolator(sInterpolator)
        .withEndAction(endAction);

    // Fade out background
    ObjectAnimator bgAnim = ObjectAnimator.ofInt(colorDrawable, "alpha", 0);
    bgAnim.setDuration(ANIM_DURATION);
    bgAnim.start();
  }

  @Override
  public void onBackPressed() {
    viewPager.setCurrentItem(startPosition);
    exitAnimation(() -> {
      finish();
      overridePendingTransition(0, 0);
    });
  }

  class SamplePagerAdapter extends PagerAdapter {

    @Override
    public int getCount() {
      return 3000;// I don't use Integer.MAX_VALUE, because ViewPager.java 1029:0
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
      Photo photo = mDataManager.getPhoto(position);
      if (photo == null) {
        ProgressBar progressBar =
            new ProgressBar(container.getContext(), null, android.R.attr.progressBarStyleLarge);
        container.addView(progressBar, ViewPager.LayoutParams.MATCH_PARENT,
            ViewPager.LayoutParams.MATCH_PARENT);

        return progressBar;
      } else {

        PhotoView photoView = new PhotoView(container.getContext());
        photoView.setPhoto(photo);
        container.addView(photoView, ViewPager.LayoutParams.MATCH_PARENT,
            ViewPager.LayoutParams.MATCH_PARENT);

        return photoView;
      }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
      return view == object;
    }
  }
}
