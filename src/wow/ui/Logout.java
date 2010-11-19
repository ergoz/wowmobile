// World of Warcraft Mobile
//
// Logout screen implementation
// From: GameMenu or (system request)

package wow.ui;

import javax.microedition.lcdui.*;
import wow.*;

public class Logout extends WoWscreen {

    private int m_width;
    private int m_height;

    public void activate(boolean active) {
	System.err.println("Logout.activate() active="+active);
	if (active) {
	}
    }

    public void sizeEvent(int width, int height) {
	System.err.println("Logout.sizeEvent() "+width+" x "+height);
	m_width = width;
	m_height = height;
    }

    public boolean paintEvent(Graphics g) {
	if ((m_width == 0) || (m_height == 0))
	    return false;
	g.setColor(0xff0000);
	g.drawString("LOGOUT",m_width/2,m_height/2,Graphics.HCENTER|Graphics.TOP);
	return false;
    }

    public boolean keyEvent(int key, int action, boolean pressed, boolean repeated) {
	System.err.println("Logout.keyEvent() key="+key+" act="+action+" press="+pressed);
	if (pressed && !repeated && (action == Canvas.FIRE)) {
	    return WoWgame.self().popScreen() || WoWgame.self().jumpScreen("Player");
	}
	return false;
    }
}
