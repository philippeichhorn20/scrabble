package backend.tutorial;

import backend.basic.ClientMatch;
import backend.basic.Player;
import backend.basic.Profile;
import backend.basic.ServerMatch;

public final class TutorialInformation {
  private Profile profile;
  private TutorialMatch match;

  private final static TutorialInformation INSTANCE = new TutorialInformation();

  private TutorialInformation() {}

  public static TutorialInformation getInstance(){
    return INSTANCE;
  }

  public TutorialMatch getTutorialMatch() {
    return match;
  }

  public void setTutorialmatch(TutorialMatch tutorialmatch) {
    this.match = tutorialmatch;
  }

  public ServerMatch getServermatch() {
    return servermatch;
  }

  public void setServermatch(ServerMatch servermatch) {
    this.servermatch = servermatch;
  }

  private ServerMatch servermatch;


  public void setProfile(Profile p){
    this.profile = p;
  }

  public Profile getProfile() {
    return this.profile;
  }
}
