// World of Warcraft Mobile
//
// Quest log screen UI

package wow.ui;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.Sprite;
import wow.*;

public class QuestLog extends WoWscreen {

    private int m_width;
    private int m_height;
    private int m_dx;
    private int m_dy;
    private int m_x;
    private int m_y;
    private Image m_map;

    public QuestLog() {
	m_width = 0;
	m_height = 0;
	m_dx = 200;
	m_dy = 200;
	m_x = 0;
	m_y = 0;
	m_map = null;
	try {
	    m_map = Image.createImage("/images/maps/"+WoWgame.self().mapArea()+".png");
	}
	catch (Exception e) {
	    e.printStackTrace();
	    WoWgame.self().setDebug(e.toString());
	}
    }

    public void activate(boolean active) {
	System.err.println("QuestLog.activate() active="+active);
	if (active) {
	    WoWgame.self().setBgPath("/sounds/music/cities/Darnassus/Darnassus_Intro.mp3");
	    WoWgame.self().fireFgSound(WoWmobile.resource("/sounds/universal/TomeUnSheath.mp3"));
	}
	else
	    WoWgame.self().fireFgSound(WoWmobile.resource("/sounds/universal/TomeSheath.mp3"));
    }

    public void sizeEvent(int width, int height) {
	System.err.println("QuestLog.sizeEvent() "+width+" x "+height);
	m_width = width;
	m_height = height;
    }

    public boolean paintEvent(Graphics g) {
	if ((m_width == 0) || (m_height == 0))
	    return false;
	if (m_map != null)
	    g.drawRegion(m_map,m_dx,m_dy,m_width,m_height,Sprite.TRANS_NONE,0,0,Graphics.LEFT|Graphics.TOP);
	g.setColor(0xffff00);
	g.drawString(WoWgame.self().mapArea(),m_width/2,4,Graphics.HCENTER|Graphics.TOP);
	g.setColor(0xffffff);
	int px = (int)((1.0+WoWgame.self().mapLocationX())/2*m_width);
	int py = (int)((1.0+WoWgame.self().mapLocationY())/2*m_height);
	g.drawArc(px-m_dx-2,py-m_dy-2,4,4,0,360);
	return false;
    }

    public boolean keyEvent(int key, int action, boolean pressed, boolean repeated) {
	System.err.println("QuestLog.keyEvent() key="+key+" act="+action+" press="+pressed);
	if (pressed && (action != 0) && (key < 0)) {
	    switch (action) {
		case Canvas.FIRE:
		    if (!repeated)
			WoWgame.self().showScreen("Player");
		    return true;
		case Canvas.LEFT:
		    m_dx -= 32;
		    if (m_dx < 0)
			m_dx = 0;
		    return true;
		case Canvas.RIGHT:
		    if (m_map == null)
			return true;
		    m_dx += 32;
		    if (m_dx > m_map.getWidth()-m_width)
			m_dx = m_map.getWidth()-m_width;
		    return true;
		case Canvas.UP:
		    m_dy -= 32;
		    if (m_dy < 0)
			m_dy = 0;
		    return true;
		case Canvas.DOWN:
		    if (m_map == null)
			return true;
		    m_dy += 32;
		    if (m_dy > m_map.getHeight()-m_height)
			m_dy = m_map.getHeight()-m_height;
		    return true;
	    }
	}
	if (pressed && !repeated) {
	    switch (key) {
		case '0':
		    WoWgame.self().pushScreen("GameMenu");
		    return true;
	    }
	}
	return false;
    }

    public boolean ptrEvent(int x, int y, boolean pressed, boolean dragged)
    {
	int dx = x - m_x;
	int dy = y - m_y;
	m_x = x;
	m_y = y;
	if (m_map != null && dragged) {
	    m_dx -= dx;
	    m_dy -= dy;
	    if (m_dx < 0)
		m_dx = 0;
	    else if (m_dx > m_map.getWidth()-m_width)
		m_dx = m_map.getWidth()-m_width;
	    if (m_dy < 0)
		m_dy = 0;
	    else if (m_dy > m_map.getHeight()-m_height)
		m_dy = m_map.getHeight()-m_height;
	    return true;
	}
	return false;
    }
}
