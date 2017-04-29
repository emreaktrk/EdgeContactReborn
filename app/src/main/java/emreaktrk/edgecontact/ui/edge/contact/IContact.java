package emreaktrk.edgecontact.ui.edge.contact;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;

interface IContact {

    boolean hasPhoto();

    Uri photo();

    String letter();

    Uri phone();

    Drawable roundedPhoto(Context context);

    Drawable letterDrawable();

    Drawable borderedPhoto(Context context);
}
