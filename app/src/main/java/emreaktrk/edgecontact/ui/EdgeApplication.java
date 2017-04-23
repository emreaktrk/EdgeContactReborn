package emreaktrk.edgecontact.ui;

import android.app.Application;
import android.content.Intent;

import com.facebook.drawee.backends.pipeline.Fresco;

import emreaktrk.edgecontact.ui.edge.contact.Contact;
import emreaktrk.edgecontact.ui.edge.contact.ContactSync;
import io.realm.Realm;


public final class EdgeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
        Realm.init(this);

        startContactSync();
    }

    private void startContactSync() {
        Intent service = new Intent(this, ContactSync.class);
        startService(service);
    }
}