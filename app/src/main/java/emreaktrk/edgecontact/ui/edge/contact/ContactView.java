package emreaktrk.edgecontact.ui.edge.contact;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.AttributeSet;
import android.view.View;

import com.amulyakhare.textdrawable.TextDrawable;
import com.facebook.drawee.drawable.RoundedBitmapDrawable;
import com.scalified.fab.ActionButton;

import java.io.IOException;

import emreaktrk.edgecontact.logger.Logger;

public final class ContactView extends ActionButton implements View.OnClickListener {

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

    private RoundedBitmapDrawable getRoundedPhoto() {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), mContact.photo());
            RoundedBitmapDrawable rounded = new RoundedBitmapDrawable(getResources(), bitmap);
            rounded.setCircle(true);

            return rounded;
        } catch (IOException e) {
            Logger.e("Photo Uri is invalid.");
        }

        return null;
    }

    @WorkerThread
    private void clear() {
        // TODO Clear state
    }

    @SuppressLint("WrongThread")
    @WorkerThread private void apply() {
        final Drawable drawable = mContact.hasPhoto() ?
                getRoundedPhoto()
                :
                TextDrawable
                        .builder()
                        .beginConfig()
                        .textColor(Color.BLACK)
                        .bold()
                        .endConfig()
                        .buildRound(mContact.letter(), Color.TRANSPARENT);

        post(new Runnable() {
            @Override public void run() {
                setImageSize(64);
                setImageDrawable(drawable);
            }
        });
    }

    public interface OnClickListener {

        void onCallClicked(Contact contact, View view);

        void onAddClicked(View view);
    }
}