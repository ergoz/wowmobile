// World of Warcraft Mobile
//
// Full screen narration / interlude / intro implementation
// From: CharCreate
// To: EnterWorld

package wow.ui;

import javax.microedition.lcdui.*;
import wow.*;

public class Narration extends WoWscreen {

    public void activate(boolean active) {
	System.err.println("Narration.activate() active="+active);
	if (active) {
	    WoWgame.self().setBgPath("/sounds/narration/NightElfNarration.mp3");
	}
    }

    public void sizeEvent(int width, int height) {
	System.err.println("Narration.sizeEvent() "+width+" x "+height);
    }

    public boolean paintEvent(Graphics g) {
	return false;
    }

    public boolean keyEvent(int key, int action, boolean pressed, boolean repeated) {
	System.err.println("Narration.keyEvent() key="+key+" act="+action+" press="+pressed);
	if (pressed && !repeated && (action == Canvas.FIRE)) {
	    WoWgame.self().showScreen("MapView");
	    return true;
	}
	return false;
    }
}
