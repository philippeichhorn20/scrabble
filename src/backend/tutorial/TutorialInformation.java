package backend.tutorial;

import backend.basic.Profile;

/**
 * A holder class for the tutorial.
 *
 * @author nilschae
 */
public final class TutorialInformation {
  private Profile profile;
  private TutorialMatch match;

  private static final TutorialInformation INSTANCE = new TutorialInformation();

  private TutorialInformation() {}

  public static TutorialInformation getInstance() {
    return INSTANCE;
  }

  public TutorialMatch getTutorialMatch() {
    return match;
  }

  public void setTutorialmatch(TutorialMatch tutorialmatch) {
    this.match = tutorialmatch;
  }

  public void setProfile(Profile p) {
    this.profile = p;
  }

  public Profile getProfile() {
    return this.profile;
  }
}
