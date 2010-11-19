// World of Warcraft Mobile
//
// Realm object

package wow;

public class WoWrealm {

    private String m_name;
    private String m_addr;

    public WoWrealm(final String name, final String addr) {
	m_name = name;
	m_addr = addr;
    }

    public String name() {
	return m_name;
    }

    public String addr() {
	return m_addr;
    }

}
