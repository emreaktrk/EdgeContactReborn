package emreaktrk.edgecontact.ui;


import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import emreaktrk.edgecontact.logger.Logger;

public abstract class BaseActivity extends AppCompatActivity {

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResId());

        Logger.v(getTag() + " created");
    }

    @LayoutRes protected abstract int getLayoutResId();

    protected String getTag() {
        return getClass().getSimpleName();
    }
}