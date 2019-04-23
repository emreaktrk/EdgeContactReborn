package emreaktrk.edgecontact.ui.tutorial;

import android.content.Context;
import android.util.AttributeSet;
import emreaktrk.edgecontact.view.NonScrollViewPager;

public final class TutorialViewPager extends NonScrollViewPager {

  public TutorialViewPager(Context context) {
    super(context);
  }

  public TutorialViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public boolean isLastPage() {
    return getAdapter() != null && (getAdapter().getCount() - 1) == getCurrentItem();
  }

  public boolean next() {
    if (getAdapter() == null) {
      return false;
    }

    if ((getAdapter().getCount() - 1) > getCurrentItem()) {
      setCurrentItem(getCurrentItem() + 1, true);
      return true;
    }

    return false;
  }
}
