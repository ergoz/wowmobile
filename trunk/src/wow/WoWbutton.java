// World of Warcraft Mobile
//
// Button implementation

package wow;

import javax.microedition.lcdui.*;

public class WoWbutton {

    private WoWaction m_action;
    private Image m_image;
    private String m_label;
    private int m_key;

    public WoWbutton(WoWaction action, final String locator) {
	m_action = action;
	m_key = 0;
	m_image = null;
	try {
	    m_image = Image.createImage(locator);
	}
	catch (Exception e) {
	    e.printStackTrace();
	    WoWgame.self().setDebug(e.toString());
	}
    }

    public Image image() {
	return m_image;
    }

    public String label() {
	return m_label;
    }

    public String name() {
	return (m_action != null) ? m_action.name() : null;
    }

    public WoWaction action() {
	return m_action;
    }

    public int key() {
	return m_key;
    }

    public int grayed() {
	return (m_action != null) ? m_action.grayed() : 0;
    }

    public boolean enabled() {
	return m_action != null && m_action.enabled();
    }

    public boolean active() {
	return m_action != null && m_action.active();
    }

    public boolean matches(int key) {
	return active() && (key != 0) && (m_key == key);
    }

    public void setGrayed(int level) {
	if (m_action != null)
	    m_action.setGrayed(level);
    }

    public void setGrayed() {
	setGrayed(100);
    }

    public void setKey(int key, String label) {
	m_key = key;
	if ((label == null) && (key > '0') && (key <= '9'))
	    m_label = String.valueOf((char)key);
	else
	    m_label = label;
    }
}
