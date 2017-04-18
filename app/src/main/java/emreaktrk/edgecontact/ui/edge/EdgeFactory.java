package emreaktrk.edgecontact.ui.edge;

import emreaktrk.edgecontact.pattern.factory.PagerFactory;
import emreaktrk.edgecontact.ui.edge.pages.ContactEdge;

final class EdgeFactory extends PagerFactory<Edge> {

    private static final int COUNT = 5;

    @Override public Edge getItem(int position) {
        return new ContactEdge();
    }

    @Override public int getCount() {
        return COUNT;
    }
}