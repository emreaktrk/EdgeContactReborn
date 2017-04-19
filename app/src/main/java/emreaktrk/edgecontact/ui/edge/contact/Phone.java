package emreaktrk.edgecontact.ui.edge.contact;


import io.realm.RealmObject;

public class Phone extends RealmObject implements IPhone {

    String mData;

    public Phone() {
    }

    @SuppressWarnings("WeakerAccess") public Phone(String data) {
        mData = data;
    }

    @Override public String toString() {
        return "{" +
                "data='" + mData + '\'' +
                '}';
    }
}
