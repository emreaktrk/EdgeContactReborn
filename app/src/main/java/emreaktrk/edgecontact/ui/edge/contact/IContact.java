package emreaktrk.edgecontact.ui.edge.contact;


import android.net.Uri;

interface IContact {

    boolean hasPhoto();

    Uri photo();

    String letter();

    Uri uri();
}
