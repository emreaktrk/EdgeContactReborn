package emreaktrk.edgecontact.ui.edge.contact;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import java.util.Collections;

import emreaktrk.edgecontact.logger.Logger;
import io.realm.Realm;
import io.realm.RealmResults;

public final class ContactSync extends Service {

    private volatile static Publisher mPublisher;
    private ContactObserver mObserver;

    public ContactSync() {
        mObserver = new ContactObserver();
    }

    public static void register(Publisher publisher) {
        mPublisher = publisher;
    }

    public static void unregister() {
        mPublisher = null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        getContentResolver().registerContentObserver(ContactsContract.ProfileSyncState.CONTENT_URI, true, mObserver);

        Logger.i("Sync service is ready");
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
            if (!proxy.isValid()) {
                continue;
            }

            Contact concrete = Realm
                    .getDefaultInstance()
                    .copyFromRealm(proxy);

            Contact raw = ContactResolver
                    .from(getApplicationContext())
                    .setUri(concrete.data())
                    .setPosition(concrete.mPosition)
                    .query();

            if (raw == null) {
                delete(proxy);
                continue;
            }

            update(raw);
        }

        if (mPublisher != null) {
            mPublisher.onSync();

            Logger.i("Published contacts");
        }
    }

    private void update(@NonNull final Contact contact) {
        Realm
                .getDefaultInstance()
                .executeTransaction(new Realm.Transaction() {
                    @Override public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(contact);

                        Logger.i("Updated contact");
                        Logger.json(contact);
                    }
                });

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            ShortcutInfo shortcut = new ShortcutInfo.Builder(
                    getApplicationContext(), contact.getId())
                    .setShortLabel(contact.getShortLabel())
                    .setLongLabel(contact.getLongLabel())
                    .setIcon(contact.getIcon(getApplicationContext()))
                    .setIntent(contact.getIntent())
                    .build();

            ShortcutManager manager = getSystemService(ShortcutManager.class);
            manager.addDynamicShortcuts(Collections.singletonList(shortcut));
        }
    }

    private void delete(@NonNull final Contact contact) {
        Realm
                .getDefaultInstance()
                .executeTransaction(new Realm.Transaction() {
                    @Override public void execute(Realm realm) {
                        contact.deleteFromRealm();

                        Logger.i("Deleted contact");
                    }
                });

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            ShortcutManager manager = getSystemService(ShortcutManager.class);
            manager.removeDynamicShortcuts(Collections.singletonList(contact.getId()));
        }
    }

    interface Publisher {
        void onSync();
    }

    final class ContactObserver extends ContentObserver {

        ContactObserver() {
            super(null);
        }


        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);

            lookup();
        }
    }
}
