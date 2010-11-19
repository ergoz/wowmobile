// World of Warcraft Mobile
//
// Corpse object

package wow;

public class WoWcorpse extends WoWobject {

    public static final short FIELD_OWNER        = FIELD_END_OBJECT + 0x0000;
    public static final short FIELD_ITEMS        = FIELD_END_OBJECT + 0x0005;
    public static final short FIELD_FLAGS        = FIELD_END_OBJECT + 0x001B;
    public static final short FIELD_END_CORPSE   = FIELD_END_OBJECT + 0x001E;

    private long m_owner;
    private int m_flags;

    public WoWcorpse(long guid) {
	super(guid);
	m_owner = 0;
	m_flags = 0;
    }

    public long owner() {
	return m_owner;
    }

    public int flags() {
	return m_flags;
    }

    public void setValue(short idx, int value) {
	switch (idx) {
	    case FIELD_OWNER:
		m_owner = updateLow(m_owner,value);
		break;
	    case FIELD_OWNER+1:
		m_owner = updateHigh(m_owner,value);
		break;
	    case FIELD_FLAGS:
		m_flags = value;
		break;
	}
	super.setValue(idx,value);
    }

}
