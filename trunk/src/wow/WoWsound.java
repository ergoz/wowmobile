// World of Warcraft Mobile
//
// Sound implementation

package wow;

import javax.microedition.media.*;
import javax.microedition.media.control.*;
import java.io.InputStream;
import java.io.IOException;

public class WoWsound {

    private Player m_player;

    public WoWsound() {
	m_player = null;
    }

    public void start(final String locator, final boolean repeated, final int level) {
	start(locator,null,repeated,level);
    }

    public synchronized void start(final String locator, final String alternate, final boolean repeated, final int level) {
	stop();
	Thread async = new Thread() {
	    public void run() {
		try {
		    if (locator.startsWith("resource:")) {
			InputStream str = getClass()
			    .getResourceAsStream(locator.substring(9));
			if (str != null) {
			    String type = "";
			    if (locator.endsWith(".wav"))
				type = "audio/x-wav";
			    else if (locator.endsWith(".au"))
				type = "audio/basic";
			    else if (locator.endsWith(".mp3"))
				type = "audio/mpeg";
			    else if (locator.endsWith(".mid"))
				type = "audio/midi";
			    m_player = Manager.createPlayer(str,type);
			}
			else if (alternate != null)
			    m_player = Manager.createPlayer(alternate);
		    }
		    else
			m_player = Manager.createPlayer(locator);
		    if (repeated)
			m_player.setLoopCount(-1);
		    m_player.realize();
		    if (level >= 0)
			volume(level);
		    m_player.start();
		}
		catch (IllegalArgumentException e) {
		    e.printStackTrace();
		    WoWgame.self().setDebug(e.toString());
		}
		catch (IOException e) {
		    e.printStackTrace();
		    WoWgame.self().setDebug(e.toString());
		}
		catch (MediaException e) {
		    e.printStackTrace();
		    WoWgame.self().setDebug(e.toString());
		}
	    }
	};
	async.start();
    }

    public synchronized void stop() {
	pause();
	if (m_player != null) {
	    m_player.close();
	    m_player = null;
	}
    }

    public synchronized void pause() {
	if (m_player == null)
	    return;
	try {
	    m_player.stop();
	}
	catch (MediaException e) {
	    e.printStackTrace();
	    WoWgame.self().setDebug(e.toString());
	}
    }

    public synchronized void resume() {
	if (m_player == null)
	    return;
	try {
	    m_player.start();
	}
	catch (MediaException e) {
	    e.printStackTrace();
	    WoWgame.self().setDebug(e.toString());
	}
    }

    public synchronized int volume(int level) {
	if ((m_player == null) || (level < 0))
	    return -1;
	try {
	    VolumeControl ctrl = (VolumeControl)m_player.getControl("VolumeControl");
	    if (ctrl != null)
		return ctrl.setLevel(level);
	}
	catch (Exception e) {
	    e.printStackTrace();
	    WoWgame.self().setDebug(e.toString());
	}
	return -1;
    }

    public synchronized boolean playing() {
	return (m_player != null) && (m_player.getState() == Player.STARTED);
    }
}
