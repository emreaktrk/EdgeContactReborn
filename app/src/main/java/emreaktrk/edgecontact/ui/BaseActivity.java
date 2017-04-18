package emreaktrk.edgecontact.ui;


import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import emreaktrk.edgecontact.R;
import emreaktrk.edgecontact.logger.Logger;

public abstract class BaseActivity extends AppCompatActivity {

    @Override public void onAttachedToWindow() {
        super.onAttachedToWindow();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResId());

        Logger.v(getName() + " created");
    }

    @Override protected void onResume() {
        super.onResume();

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override protected void onPause() {
        super.onPause();

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @LayoutRes protected abstract int getLayoutResId();

    protected String getName() {
        return getClass().getSimpleName();
    }
}