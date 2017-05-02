package emreaktrk.edgecontact.agent.event;


import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

public abstract class Event extends BroadcastReceiver implements IEvent {

    public static IntentFilter getIntentFilter() {
        return new IntentFilter(Event.class.getSimpleName());
    }

    public static Intent getIntent() {
        return new Intent(Event.class.getSimpleName());
    }


}
