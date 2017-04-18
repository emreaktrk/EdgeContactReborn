package emreaktrk.edgecontact.pattern.factory;


import emreaktrk.edgecontact.ui.BaseFragment;

public abstract class PagerFactory<T extends BaseFragment> extends Factory<T> {

    public abstract int getCount();
}
