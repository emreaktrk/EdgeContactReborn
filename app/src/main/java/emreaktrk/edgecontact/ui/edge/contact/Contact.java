package emreaktrk.edgecontact.ui.edge.contact;


import android.net.Uri;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Contact extends RealmObject implements IContact {

    @PrimaryKey long mId;
    Phone mPhone;
    String mName;
    String mPhoto;

    @SuppressWarnings("WeakerAccess") public Contact() {
    }

    Uri uri() {
        return Uri.parse("tel:" + mPhone);
    }

    @Override public String toString() {
        return "{" +
                "id=" + mId +
                ", phone=" + mPhone.toString() +
                ", name='" + mName + '\'' +
                ", photo='" + mPhoto + '\'' +
                '}';
    }
}
