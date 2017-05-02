package emreaktrk.edgecontact.ui.edge.contact;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import emreaktrk.edgecontact.R;
import io.realm.Realm;

final class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.Holder> {

    private IDelegate mIDelegate;
    private int mPosition;

    ContactAdapter(IDelegate delegate) {
        mIDelegate = delegate;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.row_contact, parent, false);

        return new Holder(row);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Contact proxy = Realm
                .getDefaultInstance()
                .where(Contact.class)
                .equalTo(ContactContractor.Columns.POSITION, position)
                .findFirst();

        holder.mView.setContact(proxy);
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    int getSelectedPosition() {
        return mPosition;
    }

    public interface IDelegate {

        void onCallClicked(Contact contact);

        void onAddClicked();
    }

    class Holder extends RecyclerView.ViewHolder implements ContactView.OnClickListener {

        private ContactView mView;

        Holder(View itemView) {
            super(itemView);

            bind();
        }

        private void bind() {
            mView = (ContactView) itemView;
            mView.setOnClickListener(this);
        }

        @Override
        public void onCallClicked(Contact contact, View view) {
            mIDelegate.onCallClicked(contact);
        }

        @Override
        public void onAddClicked(View view) {
            mPosition = getAdapterPosition();
            mIDelegate.onAddClicked();
        }
    }
}
