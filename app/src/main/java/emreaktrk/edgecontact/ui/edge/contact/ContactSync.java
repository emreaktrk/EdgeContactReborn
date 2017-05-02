package emreaktrk.edgecontact.ui.edge.contact;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import java.util.Collections;

import emreaktrk.edgecontact.logger.Logger;
import io.realm.Realm;
import io.realm.RealmResults;

public final class ContactSync extends Service {

    private ContactObserver mObserver;

    public ContactSync() {
        mObserver = new ContactObserver();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        getContentResolver().registerContentObserver(ContactsContract.ProfileSyncState.CONTENT_URI, true, mObserver);

        Logger.i("Sync registered.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getContentResolver().unregisterContentObserver(mObserver);

        Logger.i("Sync unregistered.");
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
            Contact raw = ContactResolver
                    .from(getApplicationContext())
                    .setUri(proxy.data())
                    .setPosition(proxy.mPosition)
                    .query();

            if (raw == null) {
                delete(proxy);
            } else {
                update(raw);
            }
        }

        publish();
    }

    private void publish() {
        LocalBroadcastManager
                .getInstance(getApplicationContext())
                .sendBroadcastSync(ContactEdge.PublishEvent.getIntent());

        Logger.i("Published contacts");
    }

    private void update(@NonNull final Contact contact) {
        Realm
                .getDefaultInstance()
                .executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(contact);

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

        Logger.i("Updated contact");
    }

    private void delete(@NonNull final Contact contact) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            ShortcutManager manager = getSystemService(ShortcutManager.class);
            manager.removeDynamicShortcuts(Collections.singletonList(contact.getId()));
        }

        Realm
                .getDefaultInstance()
                .executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        contact.deleteFromRealm();
                    }
                });

        Logger.i("Deleted contact");
    }

    final class ContactObserver extends ContentObserver {

        ContactObserver() {
            super(new Handler(Looper.getMainLooper()));
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);

            lookup();
        }
    }
}
