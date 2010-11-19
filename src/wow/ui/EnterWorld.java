// World of Warcraft Mobile
//
// Enter World screen implementation
// From: EnterRealm or (game logic)
// To: Player

package wow.ui;

import javax.microedition.lcdui.*;
import wow.*;

public class EnterWorld extends WoWscreen {

    public void activate(boolean active) {
	WoWgame.self().showDebug("EnterWorld.activate() active="+active);
	if (active) {
	    WoWgame.self().setDebug("Waiting for world...");
	    WoWgame.self().setBgPath("/sounds/music/cities/Darnassus/Darnassus_Intro.mp3");
	}
    }

    public void sizeEvent(int width, int height) {
	WoWgame.self().showDebug("EnterWorld.sizeEvent() "+width+" x "+height);
    }

    public boolean paintEvent(Graphics g) {
	return false;
    }

    public boolean keyEvent(int key, int action, boolean pressed, boolean repeated) {
	WoWgame.self().showDebug("EnterWorld.keyEvent() key="+key+" act="+action+" press="+pressed);
	if (pressed && !repeated && (action == Canvas.FIRE)) {
	    WoWgame.self().jumpScreen("Player");
	    return true;
	}
	return false;
    }

    public int idle() {
	if (WoWgame.self().player() != null)
	    WoWgame.self().jumpScreen("Player");
	return 200;
    }
}
