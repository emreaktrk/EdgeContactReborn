package emreaktrk.edgecontact.ui.tutorial;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

final class TutorialAdapter extends FragmentPagerAdapter {

    private TutorialFactory mFactory;

    TutorialAdapter(FragmentManager manager) {
        super(manager);

        mFactory = new TutorialFactory();
    }

    @Override public Fragment getItem(int position) {
        return mFactory.getItem(position);
    }

    @Override public int getCount() {
        return mFactory.getCount();
    }
}