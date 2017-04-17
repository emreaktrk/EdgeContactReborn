package emreaktrk.edgecontact.ui.tutorial;

import emreaktrk.edgecontact.pattern.factory.PagerFactory;
import emreaktrk.edgecontact.ui.tutorial.pages.FirstTutorial;

final class TutorialFactory extends PagerFactory<Tutorial> {

    private static final int COUNT = 5;

    @Override public Tutorial getItem(int position) {
        return new FirstTutorial();
    }

    int getCount() {
        return COUNT;
    }
}