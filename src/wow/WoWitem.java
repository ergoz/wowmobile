// World of Warcraft Mobile
//
// Game item object

package wow;

public class WoWitem extends WoWobject {

    public static final short FIELD_OWNER        = FIELD_END_OBJECT + 0x0000;
    public static final short FIELD_FLAGS        = FIELD_END_OBJECT + 0x000F;
    public static final short FIELD_ENCHANTMENTS = FIELD_END_OBJECT + 0x0010;
    public static final short FIELD_END_ITEM     = FIELD_END_OBJECT + 0x003A;

    private long m_owner;
    private int m_flags;

    public WoWitem(long guid) {
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
