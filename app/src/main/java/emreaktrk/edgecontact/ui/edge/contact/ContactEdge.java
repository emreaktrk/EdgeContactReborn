package emreaktrk.edgecontact.ui.edge.contact;


import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import emreaktrk.edgecontact.R;
import emreaktrk.edgecontact.ui.edge.Edge;

public final class ContactEdge extends Edge implements ContactView.OnClickListener {

    private static final int REQUEST_CODE = 568;

    private ContactView mFirstView;

    @Override protected int getLayoutResId() {
        return R.layout.cell_contact;
    }

    @Override protected void onViewInflated(View view) {
        mFirstView = (ContactView) view.findViewById(R.id.contract_first);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFirstView.setOnClickListener(this);
    }

    @Override public void onCallClicked(Contact contact, View view) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(contact.uri());
        startActivity(intent);

        getActivity().finish();
    }

    @Override public void onAddClicked(View view) {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, REQUEST_CODE);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != REQUEST_CODE) {
            return;
        }

        if (resultCode != AppCompatActivity.RESULT_OK) {
            return;
        }

        Contact contact = ContractResolver
                .from(getContext())
                .setUri(data.getData())
                .setPosition(0)
                .query();

        // TODO Save to realm
    }
}