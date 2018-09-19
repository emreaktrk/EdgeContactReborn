package emreaktrk.edgecontact.ui.edge.contact;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import emreaktrk.edgecontact.R;

public final class ContactView extends FloatingActionButton implements View.OnClickListener {

    private OnClickListener mListener;
    private Contact mContact;

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

    @Override
    public void onClick(View view) {
        if (mListener == null) {
            return;
        }

        if (hasContact()) {
            mListener.onCallClicked(mContact, this);
        } else {
            mListener.onAddClicked(this);
        }
    }

    public void setContact(@Nullable Contact contact) {
        mContact = contact;
        update();
    }

    private void update() {
        if (hasContact()) {
            apply();
            return;
        }

        clear();
    }

    public boolean hasContact() {
        return mContact != null;
    }

    @UiThread
    private void clear() {
        post(() -> setImageResource(R.drawable.ic_add));
    }

    @UiThread
    private void apply() {
        final Drawable drawable = mContact.hasPhoto() ? mContact.roundedPhoto(getContext()) : mContact.letterDrawable();

        post(() -> setImageDrawable(drawable));
    }

    public interface OnClickListener {

        void onCallClicked(Contact contact, View view);

        void onAddClicked(View view);
    }
}