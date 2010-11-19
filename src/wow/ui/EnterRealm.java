// World of Warcraft Mobile
//
// Enter Realm screen implementation
// From: CharSelect or Narration
// To: EnterWorld

package wow.ui;

import javax.microedition.lcdui.*;
import wow.*;

public class EnterRealm extends WoWscreen {

    public void activate(boolean active) {
	System.err.println("EnterRealm.activate() active="+active);
	if (active) {
	    System.gc();
	    if (WoWgame.self().detailed())
		WoWgame.self().fireFgSound(WoWmobile.resource("/sounds/interface/iEnterWorldA.mp3"));
	    WoWgame.self().jumpScreen("EnterWorld");
	}
    }

    public void sizeEvent(int width, int height) {
	System.err.println("EnterRealm.sizeEvent() "+width+" x "+height);
    }

    public boolean paintEvent(Graphics g) {
	return false;
    }

    public boolean keyEvent(int key, int action, boolean pressed, boolean repeated) {
	System.err.println("EnterRealm.keyEvent() key="+key+" act="+action+" press="+pressed);
	if (pressed && !repeated && (action == Canvas.FIRE)) {
	    WoWgame.self().jumpScreen("Player");
	    return true;
	}
	return false;
    }

    public int idle() {
	return 0;
    }
}
