package emreaktrk.edgecontact.ui.edge.contact;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import emreaktrk.edgecontact.agent.shortcut.IShortcut;
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

        getContentResolver().registerContentObserver(ContactsContract.ProfileSyncState.CONTENT_URI, false, mObserver);
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

        shortcuts(all);

        if (mPublisher != null) {
            mPublisher.onUpdated();

            Logger.i("Publish updated contacts");
        }
    }

    @SuppressWarnings("ConstantConditions") private void shortcuts(RealmResults<Contact> all) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
            return;
        }

        ArrayList<ShortcutInfo> shortcuts = new ArrayList<>();

        for (Contact proxy : all) {
            Contact concrete = Realm
                    .getDefaultInstance()
                    .copyFromRealm(proxy);

            IShortcut raw = ContactResolver
                    .from(getApplicationContext())
                    .setUri(concrete.data())
                    .setPosition(concrete.mPosition)
                    .query();

            ShortcutInfo shortcut = new ShortcutInfo.Builder(
                    getApplicationContext(), raw.getId())
                    .setShortLabel(raw.getShortLabel())
                    .setLongLabel(raw.getLongLabel())
                    .setIcon(raw.getIcon(getApplicationContext()))
                    .setIntent(raw.getIntent())
                    .build();

            shortcuts.add(shortcut);
        }

        ShortcutManager manager = getSystemService(ShortcutManager.class);
        manager.addDynamicShortcuts(shortcuts);

        Logger.i("Updated shortcuts");
    }

    private void update(@Nullable final Contact contact) {
        if (contact == null) {
            // TODO Delete contact from realm
            return;
        }

        Realm
                .getDefaultInstance()
                .executeTransaction(
                        new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(contact);

                                Logger.i("Updated contacts");
                                Logger.json(contact);
                            }
                        });
    }

    interface Publisher {
        void onUpdated();
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
}
