package emreaktrk.edgecontact.ui.tutorial;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import emreaktrk.edgecontact.R;
import emreaktrk.edgecontact.ui.BaseFragment;

public abstract class Tutorial extends BaseFragment implements ITutorial {

    private ImageView mImageView;
    private TextView mDescriptionView;

    @Override protected int getLayoutResId() {
        return R.layout.cell_tutorial;
    }

    @Override protected void onViewInflated(View view) {
        mImageView = (ImageView) view.findViewById(R.id.tutorial_image);
        mDescriptionView = (TextView) view.findViewById(R.id.tutorial_description);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        
    }
}
