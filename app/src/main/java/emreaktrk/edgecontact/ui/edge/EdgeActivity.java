package emreaktrk.edgecontact.ui.edge;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
import com.facebook.drawee.view.SimpleDraweeView;
import emreaktrk.edgecontact.R;
import emreaktrk.edgecontact.agent.prefs.PrefsManager;
import emreaktrk.edgecontact.permission.PermissionHelper;
import emreaktrk.edgecontact.ui.BaseActivity;
import emreaktrk.edgecontact.ui.tutorial.TutorialActivity;

public final class EdgeActivity extends BaseActivity {

  private SimpleDraweeView mWallpaperView;
  private ViewPager mPager;

  @Override
  protected int getLayoutResId() {
    return R.layout.layout_launch;
  }

  @Override
  public void onAttachedToWindow() {
    super.onAttachedToWindow();

    getWindow()
        .setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    loadWallpaper();
    loadEdges();
  }

  @Override
  protected void onPostCreate(@Nullable Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);

    showTutorialIfNecessary();
  }

  @Override
  public void onContentChanged() {
    super.onContentChanged();

    mWallpaperView = findViewById(R.id.edge_wallpaper);
    mPager = findViewById(R.id.edge_pager);
  }

  @SuppressLint("MissingPermission")
  @SuppressWarnings("deprecation")
  @WorkerThread
  private void loadWallpaper() {
    PermissionHelper.storage(
        this,
        new PermissionHelper.Callback() {
          @Override
          public void onRationale() {}

          @Override
          public void onGranted() {
            Drawable wallpaper = WallpaperManager.getInstance(EdgeActivity.this).getFastDrawable();
            Drawable gradient =
                ContextCompat.getDrawable(EdgeActivity.this, R.drawable.gradient_purple);
            Drawable[] layers = {wallpaper, gradient};
            final LayerDrawable background = new LayerDrawable(layers);

            runOnUiThread(() -> mWallpaperView.setImageDrawable(background));
          }
        });
  }

  @WorkerThread
  private void loadEdges() {
    final EdgeAdapter adapter = new EdgeAdapter(getSupportFragmentManager());

    runOnUiThread(() -> mPager.setAdapter(adapter));
  }

  @SuppressWarnings("UnusedReturnValue")
  @WorkerThread
  private boolean showTutorialIfNecessary() {
    if (PrefsManager.getInstance(this).isFirst()) {
      TutorialActivity.start(this);
      return true;
    }

    return false;
  }
}
