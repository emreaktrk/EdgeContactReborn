package emreaktrk.edgecontact.ui.edge.contact;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.text.TextUtils;
import com.amulyakhare.textdrawable.TextDrawable;
import com.facebook.drawee.drawable.RoundedBitmapDrawable;
import emreaktrk.edgecontact.agent.shortcut.IShortcut;
import emreaktrk.edgecontact.logger.Logger;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

import java.io.IOException;

public class Contact extends RealmObject implements IContact, IShortcut {

  @PrimaryKey long mId;
  Phone mPhone;
  String mName;
  String mPhoto;
  String mUri;
  int mPosition;
  @Ignore Uri mPhotoUri;

  public Contact() {}

  @Override
  public Uri phone() {
    return Uri.parse("tel:" + mPhone.mData);
  }

  @Override
  public Drawable roundedPhoto(Context context) {
    try {
      Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), photo());
      RoundedBitmapDrawable rounded = new RoundedBitmapDrawable(context.getResources(), bitmap);
      rounded.setCircle(true);

      return rounded;
    } catch (IOException e) {
      Logger.e("Photo Uri is invalid.");
    }

    return null;
  }

  @Override
  public Drawable letterDrawable() {
    return letterDrawable(Color.BLACK, Color.TRANSPARENT);
  }

  @Override
  public Drawable letterDrawable(@ColorInt int text, @ColorInt int color) {
    return TextDrawable.builder()
        .beginConfig()
        .textColor(text)
        .bold()
        .fontSize(72)
        .endConfig()
        .buildRound(letter(), color);
  }

  @Override
  public String letter() {
    return mName.substring(0, 1);
  }

  @Override
  public boolean hasPhoto() {
    return !TextUtils.isEmpty(mPhoto);
  }

  public Uri photo() {
    return hasPhoto() ? mPhotoUri == null ? Uri.parse(mPhoto) : mPhotoUri : null;
  }

  public Uri data() {
    return Uri.parse(mUri);
  }

  @Override
  public String getId() {
    return mUri;
  }

  @Override
  public Icon getIcon(Context context) {
    Drawable drawable = hasPhoto() ? roundedPhoto(context) : letterDrawable();
    Bitmap bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, 256, 256);
    drawable.draw(canvas);

    return Icon.createWithBitmap(bitmap);
  }

  @Override
  public String getLongLabel() {
    return mName;
  }

  @Override
  public String getShortLabel() {
    if (mName.contains(" ")) {
      return mName.split(" ")[0];
    }

    return mName;
  }

  @Override
  public Intent getIntent() {
    Intent intent = new Intent(Intent.ACTION_CALL);
    intent.setData(phone());

    return intent;
  }

  @Override
  public String toString() {
    return "{"
        + "id="
        + mId
        + ", phone="
        + mPhone.toString()
        + ", name='"
        + mName
        + '\''
        + ", photo='"
        + mPhoto
        + '\''
        + ", uri='"
        + mUri
        + '\''
        + ", position='"
        + mPosition
        + '\''
        + '}';
  }
}
