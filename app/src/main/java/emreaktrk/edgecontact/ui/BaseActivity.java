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
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResId());

        Logger.v(getTag() + " created");
    }

    @LayoutRes protected abstract int getLayoutResId();

    protected String getTag() {
        return getClass().getSimpleName();
    }

    @Override public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}