// World of Warcraft Mobile
//
// A GUID that can be hashed

package wow;

public class WoWguid {

    private long m_guid;

    public WoWguid(long uid) {
	m_guid = uid;
    }

    public long guid() {
	return m_guid;
    }

    public static int hashOf(long uid) {
	return (int)(uid ^ (uid >> 32));
    }

    public int hashCode() {
	return hashOf(m_guid);
    }

    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (!(obj instanceof WoWguid))
	    return false;
	return guid() == ((WoWguid)obj).guid();
    }
}
