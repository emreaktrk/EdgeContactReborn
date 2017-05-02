package emreaktrk.edgecontact.ui.edge;

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
import emreaktrk.edgecontact.ui.BaseActivity;
import emreaktrk.edgecontact.ui.tutorial.TutorialActivity;

public final class EdgeActivity extends BaseActivity {

    private SimpleDraweeView mWallpaperView;
    private ViewPager mPager;

    @Override protected int getLayoutResId() {
        return R.layout.layout_launch;
    }

    @Override public void onAttachedToWindow() {
        super.onAttachedToWindow();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadWallpaper();
        loadEdges();
    }

    @Override protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        showTutorialIfNecessary();
    }

    @Override public void onContentChanged() {
        super.onContentChanged();

        mWallpaperView = (SimpleDraweeView) findViewById(R.id.edge_wallpaper);
        mPager = (ViewPager) findViewById(R.id.edge_pager);
    }

    @SuppressWarnings("deprecation")
    @WorkerThread private void loadWallpaper() {
        Drawable wallpaper = WallpaperManager
                .getInstance(this)
                .getFastDrawable();
        Drawable gradient = ContextCompat.getDrawable(this, R.drawable.gradient_purple);
        Drawable[] layers = {wallpaper, gradient};
        final LayerDrawable background = new LayerDrawable(layers);

        runOnUiThread(new Runnable() {
            @Override public void run() {
                mWallpaperView.setImageDrawable(background);
            }
        });
    }

    @WorkerThread private void loadEdges() {
        final EdgeAdapter adapter = new EdgeAdapter(getSupportFragmentManager());

        runOnUiThread(new Runnable() {
            @Override public void run() {
                mPager.setAdapter(adapter);
            }
        });
    }

    @SuppressWarnings("UnusedReturnValue")
    @WorkerThread private boolean showTutorialIfNecessary() {
        if (PrefsManager
                .getInstance(this)
                .isFirst()) {
            TutorialActivity.start(this);
            return true;
        }

        return false;
    }
}