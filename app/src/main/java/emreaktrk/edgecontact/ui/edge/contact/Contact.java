package emreaktrk.edgecontact.ui.edge.contact;


import android.net.Uri;

class Contact implements IContact {

    Phone mPhone;
    String mName;
    Uri mPhoto;
    int position;

    Uri uri() {
        return Uri.parse("tel:" + mPhone.toString());
    }
}
