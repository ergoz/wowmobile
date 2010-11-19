// World of Warcraft Mobile
//
// Action implementation

package wow;

public class WoWaction {

    private String m_name;
    private int m_grayed;
    private boolean m_enabled;
    private int m_id;

    public WoWaction(final String name) {
	m_name = name;
	m_grayed = 0;
	m_enabled = true;
	m_id = 0;
    }

    public String name() {
	return m_name;
    }

    public int grayed() {
	return m_grayed;
    }

    public boolean enabled() {
	return m_enabled;
    }

    public boolean active() {
	return m_enabled && (m_grayed == 0);
    }

    public void setGrayed(int level) {
	m_grayed = level;
    }

    public void setGrayed() {
	m_grayed = 100;
    }

    public void clearGrayed(int amount) {
	m_grayed -= amount;
	if (m_grayed < 0)
	    m_grayed = 0;
    }

    public void clearGrayed() {
	m_grayed = 0;
    }

    public int id() {
	return m_id;
    }

    public void setId(int id) {
	m_id = id;
    }
}
