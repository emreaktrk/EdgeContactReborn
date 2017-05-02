package emreaktrk.edgecontact.ui.edge;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


final class EdgeAdapter extends FragmentPagerAdapter {

    private EdgeFactory mFactory;

    EdgeAdapter(FragmentManager manager) {
        super(manager);

        mFactory = new EdgeFactory();
    }

    @Override public Fragment getItem(int position) {
        return mFactory.getItem(position);
    }

    @Override public int getCount() {
        return mFactory.getCount();
    }
}