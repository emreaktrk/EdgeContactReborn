package emreaktrk.edgecontact.ui.edge.contact;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import emreaktrk.edgecontact.R;
import emreaktrk.edgecontact.logger.Logger;
import emreaktrk.edgecontact.permission.PermissionHelper;
import emreaktrk.edgecontact.ui.edge.Edge;
import io.realm.Realm;

public final class ContactEdge extends Edge implements ContactView.OnClickListener {

    private static final int REQUEST_CODE = 568;

    private ContactView mFirstView;

    @Override protected int getLayoutResId() {
        return R.layout.cell_contact;
    }

    @Override protected void onViewInflated(View view) {
        mFirstView = (ContactView) view.findViewById(R.id.contact_first);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFirstView.setOnClickListener(this);

        PermissionHelper.contact(getActivity(), new PermissionHelper.Callback() {
            @Override public void onRationale() {

            }

            @Override public void onGranted() {

            }
        });
    }

    @Override public void onCallClicked(final Contact contact, View view) {
        PermissionHelper.call(getActivity(), new PermissionHelper.Callback() {
            @Override public void onRationale() {
                // TODO Show information dialog
            }

            @Override public void onGranted() {
                call(contact.uri());
            }
        });
    }

    @Override public void onAddClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != REQUEST_CODE) {
            return;
        }

        if (resultCode != AppCompatActivity.RESULT_OK) {
            return;
        }

        final Contact contact = ContractResolver
                .from(getContext())
                .setUri(data.getData())
                .setId(0)
                .query();

        if (contact == null) {
            return;
        }

        Realm
                .getDefaultInstance()
                .executeTransaction(
                        new Realm.Transaction() {
                            @Override public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(contact);
                                mFirstView.setContact(contact);

                                Logger.json(contact);
                            }
                        });
    }

    private void call(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(uri);
        startActivity(intent);

        getActivity().finish();
    }
}