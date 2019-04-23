package emreaktrk.edgecontact.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public final class NonScrollRecyclerView extends RecyclerView {

  public NonScrollRecyclerView(Context context) {
    super(context);

    init();
  }

  public NonScrollRecyclerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);

    init();
  }

  public NonScrollRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    init();
  }

  private void init() {
    setLayoutManager(
        new LinearLayoutManager(getContext()) {
          @Override
          public boolean canScrollHorizontally() {
            return false;
          }

          @Override
          public boolean canScrollVertically() {
            return false;
          }
        });
  }
}
