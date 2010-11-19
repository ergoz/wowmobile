// World of Warcraft Mobile
//
// Standalone Minimap view UI

package wow.ui;

import java.util.Hashtable;
import java.util.Enumeration;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.Sprite;
import wow.*;

public class Minimap extends WoWscreen {

    private static final int IMG_SIZE = 256;
    private int m_width;
    private int m_height;
    private int m_dx;
    private int m_dy;
    private int m_x;
    private int m_y;
    private Hashtable m_cache;
    private Image m_missing;

    public Minimap() {
	m_width = 0;
	m_height = 0;
	m_dx = 0;
	m_dy = 0;
	m_x = 0;
	m_y = 0;
	m_cache = new Hashtable();
	m_missing = null;
	try {
	    m_missing = Image.createImage("/images/minimap/missing.jpg");
	}
	catch (Exception e) {
	}
    }

    public void activate(boolean active) {
	WoWgame.self().showDebug("Minimap.activate() active="+active);
    }

    private static String fixed(int val, int digits) {
	String s = "" + val;
	while (s.length() < digits)
	    s = "0"+s;
	return s;
    }

    private Image getGrid(int map, int x, int y) {
	String s = fixed(map,3)+"_"+fixed(x,2)+"_"+fixed(y,2)+".jpg";
	Image img = (Image)m_cache.get(s);
	if (img != null)
	    return img;
	try {
	    img = Image.createImage("/images/minimap/"+s);
	}
	catch (Exception e) {
	}
	if (img == null)
	    img = m_missing;
	if (img != null)
	    m_cache.put(s,img);
	return img;
    }

    public void sizeEvent(int width, int height) {
	WoWgame.self().showDebug("Minimap.sizeEvent() "+width+" x "+height);
	m_width = width;
	m_height = height;
    }

    private void paintBlip(Graphics g, int x, int y, int color) {
	if (x < 0 || y < 0 || x >= m_width || y >= m_height)
	    return; 
	g.setColor(color);
	g.drawArc(x-2,y-2,4,4,0,360);
    }

    private void paintChunk(Graphics g, int map, int gx, int gy, int x, int y) {
	Image img = getGrid(map,gx,gy);
	if (img != null)
	    g.drawRegion(img,0,0,IMG_SIZE,IMG_SIZE,Sprite.TRANS_NONE,x,y,Graphics.LEFT|Graphics.TOP);
    }

    private void paintChunks(Graphics g, int map, int gx, int gy, int x, int y, int dx, int dy) {
	for (;;) {
	    gx += dx;
	    if (gx < 0 || gx >= WoWgrid.MAX_GRIDS)
		break;
	    gy += dy;
	    if (gy < 0 || gy >= WoWgrid.MAX_GRIDS)
		break;
	    x += IMG_SIZE*dx;
	    if (x < -IMG_SIZE || x >= m_width)
		break;
	    y += IMG_SIZE*dy;
	    if (y < -IMG_SIZE || y >= m_height)
		break;
	    paintChunk(g,map,gx,gy,x,y);
	    if (dy != 0) {
		paintChunks(g,map,gx,gy,x,y,1,0);
		paintChunks(g,map,gx,gy,x,y,-1,0);
	    }
	}
    }

    public boolean paintEvent(Graphics g) {
	if ((m_width == 0) || (m_height == 0))
	    return false;
	float x = (float)WoWgame.self().mapLocationX();
	float y = (float)WoWgame.self().mapLocationY();
	WoWarea a = WoWgame.self().area();
	int map = (a != null) ? a.mapId() : 0;
	int gx = WoWgrid.getGridId(y);
	int gy = WoWgrid.getGridId(x);
	int px = (m_width/2)-m_dx;
	int py = (m_height/2)-m_dy;
	int ox = px-(int)Math.floor(WoWgrid.getGridOffs((float)WoWgame.self().mapLocationY())*IMG_SIZE);
	int oy = py-(int)Math.floor(WoWgrid.getGridOffs((float)WoWgame.self().mapLocationX())*IMG_SIZE);
	paintChunk(g,map,gx,gy,ox,oy);
	paintChunks(g,map,gx,gy,ox,oy,1,0);
	paintChunks(g,map,gx,gy,ox,oy,-1,0);
	paintChunks(g,map,gx,gy,ox,oy,0,1);
	paintChunks(g,map,gx,gy,ox,oy,0,-1);
	g.setColor(0xffff00);
	g.drawString(WoWgame.self().mapArea(),m_width/2,4,Graphics.HCENTER|Graphics.TOP);
	if (WoWgame.self().debugging()) {
	    String s = map+": "+(int)WoWgame.self().mapLocationX()+", "+(int)WoWgame.self().mapLocationY()+", "+(int)WoWgame.self().mapLocationZ();
	    g.drawString(s,m_width/2,m_height-4,Graphics.HCENTER|Graphics.BOTTOM);
	    s = "Grid: "+gx+", "+gy+" "+ox+", "+oy;
	    g.drawString(s,m_width/2,m_height-20,Graphics.HCENTER|Graphics.BOTTOM);
	}
	float sc = IMG_SIZE / WoWgrid.GRID_SIZE;
	Enumeration e = WoWgame.self().blips();
	while (e.hasMoreElements()) {
	    WoWblip b = (WoWblip)e.nextElement();
	    paintBlip(g,px-(int)(sc*(b.Y-y)),py-(int)(sc*(b.X-x)),b.color);
	}
	paintBlip(g,px,py,0xffffff);
	return false;
    }

    public boolean keyEvent(int key, int action, boolean pressed, boolean repeated) {
	//WoWgame.self().showDebug("Minimap.keyEvent() key="+key+" act="+action+" press="+pressed);
	if (pressed && (action != 0) && (key < 0)) {
	    switch (action) {
		case Canvas.FIRE:
		    if (!repeated) {
			if (WoWgame.self().shifted())
			    WoWgame.self().showScreen("MapView");
			else if (!WoWgame.self().popScreen())
			    WoWgame.self().showScreen("Player");
		    }
		    return true;
		case Canvas.LEFT:
		    m_dx -= 32;
		    if (m_dx < -(m_width/2))
			m_dx = -m_width/2;
		    return true;
		case Canvas.RIGHT:
		    m_dx += 32;
		    if (m_dx > (m_width/2))
			m_dx = m_width/2;
		    return true;
		case Canvas.UP:
		    m_dy -= 32;
		    if (m_dy < -(m_height/2))
			m_dy = -m_height/2;
		    return true;
		case Canvas.DOWN:
		    m_dy += 32;
		    if (m_dy > (m_height/2))
			m_dy = m_height/2;
		    return true;
	    }
	}
	if (pressed && !repeated) {
	    switch (key) {
		case '0':
		    if (WoWgame.self().shifted())
			WoWgame.self().jumpScreen("MapView");
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
	if (dragged) {
	    m_dx -= dx;
	    m_dy -= dy;
	    if (m_dx < -(m_width/2))
		m_dx = -m_width/2;
	    else if (m_dx > (m_width/2))
		m_dx = m_width/2;
	    if (m_dy < -(m_height/2))
		m_dy = -m_height/2;
	    else if (m_dy > (m_height/2))
		m_dy = m_height/2;
	    return true;
	}
	return false;
    }
}
