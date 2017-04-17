package emreaktrk.edgecontact.ui.tutorial;


import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

interface ITutorial {

    @DrawableRes int getImage();

    @StringRes int getDescription();

}