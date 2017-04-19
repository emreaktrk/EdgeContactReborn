package emreaktrk.edgecontact.ui.edge.contact;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

public final class ContactView extends FloatingActionButton implements View.OnClickListener {

    private OnClickListener mListener;
    private Contact mContact;


    public ContactView(Context context) {
        super(context);

        init();
    }

    public ContactView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public ContactView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private void init() {
        setOnClickListener(this);
    }

    public void setOnClickListener(OnClickListener listener) {
        mListener = listener;
    }

    @Override public void onClick(View view) {
        if (mListener == null) {
            return;
        }

        if (mContact == null) {
            mListener.onAddClicked(this);
        } else {
            mListener.onCallClicked(mContact, this);
        }
    }

    public void setContact(@Nullable Contact contact) {
        if (contact == null) {
            clear();
            return;
        }

        apply();
    }

    private void clear() {
        // TODO Clear state
    }

    private void apply() {
        // TODO Apply contact
    }

    public interface OnClickListener {

        void onCallClicked(Contact contact, View view);

        void onAddClicked(View view);
    }
}