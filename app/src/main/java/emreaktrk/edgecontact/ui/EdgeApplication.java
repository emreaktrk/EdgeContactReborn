package emreaktrk.edgecontact.ui;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import io.realm.Realm;


public final class EdgeApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
        Realm.init(this);
    }
}