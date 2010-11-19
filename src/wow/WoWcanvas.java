// World of Warcraft Mobile
//
// Full screen canvas implementation

package wow;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.GameCanvas;
import wow.*;

public class WoWcanvas extends GameCanvas implements Runnable {

    private int m_running;

    public WoWcanvas() {
	super(false);
	m_running = 0;
    }

    public boolean running() {
	return m_running != 0;
    }

    public void start() {
	System.err.println("WoWcanvas.start()");
	if (m_running != 0)
	    return;
	WoWgame.self();
	WoWgame.self().setExtra(this);
	m_running = 1;
	Thread worker = new Thread(this);
	worker.start();
    }

    public void stop() {
	System.err.println("WoWcanvas.stop()");
	if (m_running > 0) {
	    m_running = -1;
	    while (m_running != 0) {
		try {
		    Thread.sleep(10);
		}
		catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	}
	WoWgame.self().stop();
    }

    public void run() {
	System.err.println("WoWcanvas.run()");
	try {
	    getGraphics().setFont(Font.getFont(Font.FACE_SYSTEM,Font.STYLE_PLAIN,Font.SIZE_SMALL));
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	WoWgame.self().sizeEvent(getWidth(),getHeight());
	WoWgame.self().pauseEvent(false);
	while (m_running > 0) {
	    WoWgame.self().paintEvent(getGraphics());
	    flushGraphics();
	    try {
		Thread.sleep(WoWgame.self().idle());
	    }
	    catch (Exception e) {
		e.printStackTrace();
	    }
	}
	System.err.println("WoWcanvas.run() exited!");
	m_running = 0;
    }

    public void paint(Graphics g) {
	WoWgame.self().paintEvent(g);
	flushGraphics();
    }

    protected void hideNotify() {
	WoWgame.self().showEvent(false);
    }

    protected void showNotify() {
	WoWgame.self().showEvent(true);
    }

    protected void keyPressed(int key) {
	WoWgame.self().keyEvent(key,getGameAction(key),true,false);
    }

    protected void keyRepeated(int key) {
	WoWgame.self().keyEvent(key,getGameAction(key),true,true);
    }

    protected void keyReleased(int key) {
	WoWgame.self().keyEvent(key,getGameAction(key),false,false);
    }

    protected void pointerPressed(int x, int y) {
	WoWgame.self().ptrEvent(x,y,true,false);
    }

    protected void pointerReleased(int x, int y) {
	WoWgame.self().ptrEvent(x,y,false,false);
    }

    protected void pointerDragged(int x, int y) {
	WoWgame.self().ptrEvent(x,y,true,true);
    }
}
