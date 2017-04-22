package emreaktrk.edgecontact.ui.edge.contact;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.WorkerThread;

final class ContractResolver {

    private static final int POSITION_DISPLAY_NAME = 0;
    private static final int POSITION_DATA1 = 1;
    private static final int POSITION_PHOTO_URI = 2;

    private static final String[] sProjection = {
            ContactsContract.Contacts.Entity.DISPLAY_NAME,
            ContactsContract.Contacts.Entity.DATA1,
            ContactsContract.Contacts.Entity.PHOTO_URI};

    private Context mContext;
    private Uri mUri;
    private int mId;

    public static ContractResolver from(Context context) {
        ContractResolver builder = new ContractResolver();
        builder.mContext = context;
        return builder;
    }

    ContractResolver setUri(Uri uri) {
        this.mUri = uri;

        return this;
    }

    ContractResolver setId(int id) {
        this.mId = id;

        return this;
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @WorkerThread Contact query() {
        Cursor cursor = mContext
                .getContentResolver()
                .query(
                        mUri,
                        sProjection,
                        null,
                        null,
                        null);

        if (cursor == null) {
            return null;
        }

        if (!cursor.isFirst()) {
            cursor.moveToFirst();
        }

        Contact contact = new Contact();
        contact.mId = mId;

        String name = cursor.getString(POSITION_DISPLAY_NAME);
        contact.mName = name;

        String data = cursor.getString(POSITION_DATA1);
        contact.mPhone = new Phone(data);

        String uri = cursor.getString(POSITION_PHOTO_URI);
        contact.mPhoto = uri;

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return contact;
    }
}