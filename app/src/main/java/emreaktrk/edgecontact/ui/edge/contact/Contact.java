package emreaktrk.edgecontact.ui.edge.contact;


import android.net.Uri;
import android.text.TextUtils;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class Contact extends RealmObject implements IContact {

    @PrimaryKey long mId;
    Phone mPhone;
    String mName;
    String mPhoto;
    @Ignore Uri mPhotoUri;

    @SuppressWarnings("WeakerAccess") public Contact() {
    }

    Uri uri() {
        return Uri.parse("tel:" + mPhone);
    }

    String letter() {
        return mName.substring(0, 1);
    }

    boolean hasPhoto() {
        return !TextUtils.isEmpty(mPhoto);
    }

    Uri photo() {
        return hasPhoto() ?
                mPhotoUri == null ? Uri.parse(mPhoto) : mPhotoUri
                :
                null;
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
