package emreaktrk.edgecontact.ui.edge;

import emreaktrk.edgecontact.pattern.factory.PagerFactory;
import emreaktrk.edgecontact.ui.edge.contact.ContactEdge;
import emreaktrk.edgecontact.ui.edge.task.TaskEdge;
import emreaktrk.edgecontact.ui.edge.weather.WeatherEdge;

final class EdgeFactory extends PagerFactory<Edge> {

    private static final int COUNT = 3;

    @Override public Edge getItem(int position) {
        switch (position) {
            case 0:
                return new ContactEdge();
            case 1:
                return new TaskEdge();
            case 2:
                return new WeatherEdge();
            default:
                throw new IllegalArgumentException("Position must be within 0 to " + COUNT);
        }
    }

    @Override public int getCount() {
        return COUNT;
    }
}