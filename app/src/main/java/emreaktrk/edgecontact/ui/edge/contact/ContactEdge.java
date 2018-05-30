package emreaktrk.edgecontact.ui.edge.contact;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Collections;

import emreaktrk.edgecontact.R;
import emreaktrk.edgecontact.agent.event.Event;
import emreaktrk.edgecontact.logger.Logger;
import emreaktrk.edgecontact.permission.PermissionHelper;
import emreaktrk.edgecontact.ui.edge.Edge;
import io.realm.Realm;

public final class ContactEdge extends Edge implements ContactAdapter.IDelegate {

    private static final int REQUEST_CODE = 568;

    private RecyclerView mRecyclerView;
    private ContactAdapter mAdapter;
    private PublishEvent mEvent;

    @Override
    protected int getLayoutResId() {
        return R.layout.cell_contact;
    }

    @Override
    protected void onViewInflated(View view) {
        mRecyclerView = view.findViewById(R.id.contact_recycler);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new ContactAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        PermissionHelper.contact(getActivity(), new PermissionHelper.Callback() {
            @Override
            public void onRationale() {
                // TODO Show information dialog
            }

            @Override
            public void onGranted() {

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerEvent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterEvent();
    }

    private void registerEvent() {
        if (mEvent == null) {
            mEvent = new PublishEvent();
        }

        LocalBroadcastManager
                .getInstance(getContext())
                .registerReceiver(mEvent, PublishEvent.getIntentFilter());
    }

    private void unregisterEvent() {
        LocalBroadcastManager
                .getInstance(getContext())
                .unregisterReceiver(mEvent);
    }

    @Override
    public void onCallClicked(final Contact contact) {
        PermissionHelper.call(getActivity(), new PermissionHelper.Callback() {
            @Override
            public void onRationale() {
                // TODO Show information dialog
            }

            @Override
            public void onGranted() {
                call(contact.phone());
            }
        });

    }

    @Override
    public void onAddClicked() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != REQUEST_CODE) {
            return;
        }

        if (resultCode != AppCompatActivity.RESULT_OK) {
            return;
        }

        final Contact contact = ContactResolver
                .from(getContext())
                .setUri(data.getData())
                .setPosition(mAdapter.getSelectedPosition())
                .query();

        if (contact == null) {
            return;
        }

        Realm
                .getDefaultInstance()
                .executeTransaction(
                        realm -> {
                            realm.copyToRealmOrUpdate(contact);
                            realm.close();
                        });

        mAdapter.notifyDataSetChanged();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            ShortcutInfo shortcut = new ShortcutInfo.Builder(
                    getContext(), contact.getId())
                    .setShortLabel(contact.getShortLabel())
                    .setLongLabel(contact.getLongLabel())
                    .setIcon(contact.getIcon(getContext()))
                    .setIntent(contact.getIntent())
                    .build();

            ShortcutManager manager = getContext().getSystemService(ShortcutManager.class);
            manager.addDynamicShortcuts(Collections.singletonList(shortcut));
        }

        Logger.json(contact);
        Logger.i("Contact added or updated.");
    }

    private void call(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(uri);
        startActivity(intent);

        getActivity().finish();
    }

    public class PublishEvent extends Event {

        @Override public void onReceive(Context context, Intent intent) {
            mAdapter.notifyDataSetChanged();
        }
    }
}