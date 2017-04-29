package emreaktrk.edgecontact.ui.edge;

import emreaktrk.edgecontact.BuildConfig;
import emreaktrk.edgecontact.pattern.factory.PagerFactory;
import emreaktrk.edgecontact.ui.edge.contact.ContactEdge;
import emreaktrk.edgecontact.ui.edge.task.TaskEdge;
import emreaktrk.edgecontact.ui.edge.weather.WeatherEdge;

final class EdgeFactory extends PagerFactory<Edge> {

    private static final int COUNT = BuildConfig.HAS_PRO ? 3 : 1;

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