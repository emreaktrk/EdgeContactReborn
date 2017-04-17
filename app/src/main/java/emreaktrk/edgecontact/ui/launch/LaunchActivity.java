package emreaktrk.edgecontact.ui.launch;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import emreaktrk.edgecontact.R;
import emreaktrk.edgecontact.ui.BaseActivity;

public final class LaunchActivity extends BaseActivity {

    @Override protected int getLayoutResId() {
        return R.layout.layout_launch;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }
}
