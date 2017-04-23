package emreaktrk.edgecontact.ui.edge.contact;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;

import java.util.Observable;
import java.util.Observer;

import emreaktrk.edgecontact.logger.Logger;
import io.realm.Realm;
import io.realm.RealmResults;

public final class ContactSync extends Service {

    private ContactObserver mObserver;
    private volatile static Publisher mPublisher;

    public ContactSync() {
        mObserver = new ContactObserver();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, false, mObserver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getContentResolver().unregisterContentObserver(mObserver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void lookup() {
        RealmResults<Contact> all = Realm
                .getDefaultInstance()
                .where(Contact.class)
                .findAll();

        for (Contact proxy : all) {
            Contact concrete = Realm
                    .getDefaultInstance()
                    .copyFromRealm(proxy);

            Contact raw = ContactResolver
                    .from(getApplicationContext())
                    .setUri(concrete.data())
                    .setPosition(concrete.mPosition)
                    .query();

            update(raw);
        }

        if (mPublisher != null) {
            mPublisher.onUpdated();

            Logger.i("Publish updated contacts");
        }
    }

    public static void register(Publisher publisher) {
        mPublisher = publisher;
    }

    public static void unregister() {
        mPublisher = null;
    }

    private void update(@Nullable final Contact contact) {
        if (contact == null) {
            return;
        }

        Realm
                .getDefaultInstance()
                .executeTransaction(
                        new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(contact);

                                Logger.i("Contact update");
                                Logger.json(contact);
                            }
                        });
    }

    final class ContactObserver extends ContentObserver {

        ContactObserver() {
            super(null);
        }


        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);

            lookup();

            Logger.i(uri.toString());
        }
    }

    interface Publisher {
        void onUpdated();
    }
}
