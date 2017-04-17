package emreaktrk.edgecontact.ui.launch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import emreaktrk.edgecontact.R;
import emreaktrk.edgecontact.ui.BaseActivity;
import emreaktrk.edgecontact.ui.tutorial.TutorialActivity;

public final class LaunchActivity extends BaseActivity {

    @Override protected int getLayoutResId() {
        return R.layout.layout_launch;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TutorialActivity.start(this);
    }
}