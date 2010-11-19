// World of Warcraft Mobile
//
// Map view UI

package wow.ui;

import java.util.Enumeration;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.Sprite;
import wow.*;

public class MapView extends WoWscreen {

    private int m_width;
    private int m_height;
    private int m_dx;
    private int m_dy;
    private int m_x;
    private int m_y;
    private Image m_map;

    public MapView() {
	m_width = 0;
	m_height = 0;
	m_dx = 0;
	m_dy = 0;
	m_x = 0;
	m_y = 0;
	m_map = null;
	try {
	    m_map = Image.createImage("/images/maps/"+WoWgame.self().mapArea()+".jpg");
	}
	catch (Exception e) {
	    e.printStackTrace();
	    WoWgame.self().setDebug(e.toString());
	}
    }

    public void activate(boolean active) {
	WoWgame.self().showDebug("MapView.activate() active="+active);
	if (WoWgame.self().detailed()) {
	    if (active)
		WoWgame.self().fireFgSound(WoWmobile.resource("/sounds/universal/TomeUnSheath.mp3"));
	    else
		WoWgame.self().fireFgSound(WoWmobile.resource("/sounds/universal/TomeSheath.mp3"));
	}
    }

    private int world2map(double pos, float minVal, float maxVal, int size) {
	return (int)(size*(maxVal-pos)/(maxVal-minVal));
    }

    private int world2mapX(double pos, int size) {
	WoWarea a = WoWgame.self().area();
	if (a != null)
	    return world2map(pos,a.minX(),a.maxX(),size);
	return world2map(pos,-WoWgrid.MAP_HALFSIZE,WoWgrid.MAP_HALFSIZE,size);
    }

    private int world2mapY(double pos, int size) {
	WoWarea a = WoWgame.self().area();
	if (a != null)
	    return world2map(pos,a.minY(),a.maxY(),size);
	return world2map(pos,-WoWgrid.MAP_HALFSIZE,WoWgrid.MAP_HALFSIZE,size);
    }

    private int clip2mapX(double pos, int size) {
	int loc = world2mapX(pos,size);
	if (loc < 0)
	    return 0;
	if (loc >= size)
	    return size-1;
	return loc;
    }

    private int clip2mapY(double pos, int size) {
	int loc = world2mapY(pos,size);
	if (loc < 0)
	    return 0;
	if (loc >= size)
	    return size-1;
	return loc;
    }

    public void sizeEvent(int width, int height) {
	WoWgame.self().showDebug("MapView.sizeEvent() "+width+" x "+height);
	m_width = width;
	m_height = height;
	if (m_map != null) {
	    // center map on player
	    int px = clip2mapY(WoWgame.self().mapLocationY(),m_map.getWidth()) - (m_width/2);
	    int py = clip2mapX(WoWgame.self().mapLocationX(),m_map.getHeight()) - (m_height/2);
	    if (px < 0)
		px = 0;
	    else if (px > m_map.getWidth()-m_width)
		px = m_map.getWidth()-m_width;
	    if (py < 0)
		py = 0;
	    else if (py > m_map.getHeight()-m_height)
		py = m_map.getHeight()-m_height;
	    m_dx = px;
	    m_dy = py;
	}
    }

    private void paintBlip(Graphics g, double x, double y, int color) {
	g.setColor(color);
	int px = m_width;
	int py = m_height;
	if (m_map != null) {
	    px = m_map.getWidth();
	    py = m_map.getHeight();
	}
	px = world2mapY(y,px);
	py = world2mapX(x,py);
	g.drawArc(px-m_dx-2,py-m_dy-2,4,4,0,360);
    }

    public boolean paintEvent(Graphics g) {
	if ((m_width == 0) || (m_height == 0))
	    return false;
	if (m_map != null)
	    g.drawRegion(m_map,m_dx,m_dy,m_width,m_height,Sprite.TRANS_NONE,0,0,Graphics.LEFT|Graphics.TOP);
	g.setColor(0xffff00);
	g.drawString(WoWgame.self().mapArea(),m_width/2,4,Graphics.HCENTER|Graphics.TOP);
	if (WoWgame.self().detailed()) {
	    String s = "("+clip2mapY(WoWgame.self().mapLocationY(),100)+","+clip2mapX(WoWgame.self().mapLocationX(),100)+")";
	    g.drawString(s,m_width/2,20,Graphics.HCENTER|Graphics.TOP);
	}
	if (WoWgame.self().debugging()) {
	    String s = (int)WoWgame.self().mapLocationX()+", "+(int)WoWgame.self().mapLocationY()+", "+(int)WoWgame.self().mapLocationZ();
	    g.drawString(s,m_width/2,m_height-4,Graphics.HCENTER|Graphics.BOTTOM);
	    s = "Grid: "+WoWgrid.getGridId((float)WoWgame.self().mapLocationX())+", "+WoWgrid.getGridId((float)WoWgame.self().mapLocationY());
	    g.drawString(s,m_width/2,m_height-20,Graphics.HCENTER|Graphics.BOTTOM);
	    s = "Cell: "+WoWgrid.getCellId((float)WoWgame.self().mapLocationX())+", "+WoWgrid.getCellId((float)WoWgame.self().mapLocationY());
	    g.drawString(s,m_width/2,m_height-36,Graphics.HCENTER|Graphics.BOTTOM);
	}
	Enumeration e = WoWgame.self().blips();
	while (e.hasMoreElements()) {
	    WoWblip b = (WoWblip)e.nextElement();
	    paintBlip(g,b.X,b.Y,b.color);
	}
	paintBlip(g,WoWgame.self().mapLocationX(),WoWgame.self().mapLocationY(),0xffffff);
	return false;
    }

    public boolean keyEvent(int key, int action, boolean pressed, boolean repeated) {
	//WoWgame.self().showDebug("MapView.keyEvent() key="+key+" act="+action+" press="+pressed);
	if (pressed && (action != 0) && (key < 0)) {
	    switch (action) {
		case Canvas.FIRE:
		    if (!repeated) {
			if (WoWgame.self().shifted())
			    WoWgame.self().showScreen("Minimap");
			else if (!WoWgame.self().popScreen())
			    WoWgame.self().showScreen("Player");
		    }
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
		    if (WoWgame.self().shifted())
			WoWgame.self().jumpScreen("Minimap");
		    else
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
