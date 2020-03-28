package emreaktrk.edgecontact.ui.edge.contact;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.WorkerThread;

final class ContactResolver {

  private static final int POSITION_CONTACT_ID = 0;
  private static final int POSITION_DISPLAY_NAME = 1;
  private static final int POSITION_DATA1 = 2;
  private static final int POSITION_PHOTO_URI = 3;

  private static final String[] sProjection = {
    ContactsContract.Contacts.Entity.CONTACT_ID,
    ContactsContract.Contacts.Entity.DISPLAY_NAME,
    ContactsContract.Contacts.Entity.DATA1,
    ContactsContract.Contacts.Entity.PHOTO_URI
  };

  private Context mContext;
  private Uri mUri;
  private int mPosition;

  public static ContactResolver from(Context context) {
    ContactResolver builder = new ContactResolver();
    builder.mContext = context;
    return builder;
  }

  ContactResolver setUri(Uri uri) {
    this.mUri = uri;

    return this;
  }

  ContactResolver setPosition(int position) {
    this.mPosition = position;

    return this;
  }

  @SuppressWarnings("UnnecessaryLocalVariable")
  @WorkerThread
  Contact query() {
    Cursor cursor = mContext.getContentResolver().query(mUri, sProjection, null, null, null);

    if (cursor == null || cursor.getCount() == 0) {
      return null;
    }

    if (!cursor.isFirst()) {
      cursor.moveToFirst();
    }

    Contact contact = new Contact();
    contact.mPosition = mPosition;

    long id = cursor.getLong(POSITION_CONTACT_ID);
    contact.mId = id;

    String name = cursor.getString(POSITION_DISPLAY_NAME);
    contact.mName = name;

    String data = cursor.getString(POSITION_DATA1);
    contact.mPhone = new Phone(data);

    String photo = cursor.getString(POSITION_PHOTO_URI);
    contact.mPhoto = photo;

    contact.mUri = mUri.toString();

    if (!cursor.isClosed()) {
      cursor.close();
    }

    return contact;
  }
}
