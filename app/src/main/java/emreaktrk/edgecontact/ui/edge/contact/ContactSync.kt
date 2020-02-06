package emreaktrk.edgecontact.ui.edge.contact

import android.app.Service
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.ContactsContract
import android.support.v4.content.LocalBroadcastManager
import emreaktrk.edgecontact.logger.Logger
import io.realm.Realm

class ContactSync : Service() {
    private val observer: ContactObserver by lazy { ContactObserver() }
    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    override fun onCreate() {
        super.onCreate()

        contentResolver.registerContentObserver(ContactsContract.ProfileSyncState.CONTENT_URI, true, observer)
        Logger.i("Sync registered.")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()

        contentResolver.unregisterContentObserver(observer)
        Logger.i("Sync unregistered.")
    }

    private fun lookup() {
        val all = Realm.getDefaultInstance().where(Contact::class.java).findAll()
        for (proxy in all) {
            val raw = ContactResolver
                    .from(applicationContext)
                    .setUri(proxy.data())
                    .setPosition(proxy.mPosition)
                    .query()
            if (raw == null) {
                delete(proxy)
            } else {
                update(raw)
            }
        }

        realm.close()
        publish()
    }

    private fun publish() {
        LocalBroadcastManager
                .getInstance(applicationContext)
                .sendBroadcastSync(ContactEdge.PublishEvent.getIntent())

        Logger.i("Published contacts")
    }

    private fun update(contact: Contact) {
        realm.executeTransaction { realm: Realm -> realm.copyToRealmOrUpdate(contact) }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val shortcut = ShortcutInfo
                    .Builder(applicationContext, contact.id)
                    .setShortLabel(contact.shortLabel)
                    .setLongLabel(contact.longLabel)
                    .setIcon(contact.getIcon(applicationContext))
                    .setIntent(contact.intent)
                    .build()

            val manager = getSystemService(ShortcutManager::class.java)
            manager?.addDynamicShortcuts(listOf(shortcut))
        }

        Logger.i("Updated contact")
        Logger.json(contact)
    }

    private fun delete(contact: Contact) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val manager = getSystemService(ShortcutManager::class.java)
            manager?.removeDynamicShortcuts(listOf(contact.id))
        }

        realm.executeTransaction { contact.deleteFromRealm() }
        Logger.i("Deleted contact")
    }

    internal inner class ContactObserver : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean, uri: Uri) {
            super.onChange(selfChange, uri)
            lookup()
        }
    }
}