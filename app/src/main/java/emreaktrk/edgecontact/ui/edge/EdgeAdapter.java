package emreaktrk.edgecontact.ui.edge;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public final class EdgeAdapter extends FragmentPagerAdapter {

    private EdgeFactory mFactory;

    public EdgeAdapter(FragmentManager manager) {
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