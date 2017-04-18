package emreaktrk.edgecontact.ui.edge;

import android.app.WallpaperManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.v4.content.ContextCompat;

import com.facebook.drawee.view.SimpleDraweeView;

import emreaktrk.edgecontact.R;
import emreaktrk.edgecontact.ui.BaseActivity;

public final class EdgeActivity extends BaseActivity {

    private SimpleDraweeView mWallpaperView;

    @Override protected int getLayoutResId() {
        return R.layout.layout_launch;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadWallpaper();
    }


    @Override public void onContentChanged() {
        super.onContentChanged();

        mWallpaperView = (SimpleDraweeView) findViewById(R.id.edge_wallpaper);
    }

    @WorkerThread
    @SuppressWarnings("deprecation") private void loadWallpaper() {
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
}