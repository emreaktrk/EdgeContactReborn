package emreaktrk.edgecontact.agent.shortcut;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;

public interface IShortcut {

    String getId();

    String getLongLabel();

    String getShortLabel();

    Icon getIcon(Context context);

    Intent getIntent();
}
