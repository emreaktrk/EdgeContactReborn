package emreaktrk.edgecontact.ui.tutorial;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import emreaktrk.edgecontact.R;
import emreaktrk.edgecontact.ui.BaseActivity;


public final class TutorialActivity extends BaseActivity {

    private ViewPager mPager;

    public static void start(Context context) {
        Intent starter = new Intent(context, TutorialActivity.class);
        context.startActivity(starter);
    }

    @Override protected int getLayoutResId() {
        return R.layout.layout_tutorial;
    }

    @Override public void onContentChanged() {
        super.onContentChanged();

        mPager = (ViewPager) findViewById(R.id.tutorial_pager);
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TutorialAdapter adapter = new TutorialAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
    }

    public void onSkipClicked(View view) {
        finish();
    }
}