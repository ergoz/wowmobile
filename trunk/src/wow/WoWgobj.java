// World of Warcraft Mobile
//
// Game object

package wow;

public class WoWgobj extends WoWobject {

    public static final short FIELD_FLAGS        = FIELD_END_OBJECT + 0x0003;
    public static final short FIELD_LEVEL        = FIELD_END_OBJECT + 0x000A;
    public static final short FIELD_END_GOBJ     = FIELD_END_OBJECT + 0x000C;

    protected int m_flags;
    protected int m_level;

    public WoWgobj(long guid) {
	super(guid);
	m_flags = 0;
	m_level = 1;
    }

    public int flags() {
	return m_flags;
    }

    public int level() {
	return m_level;
    }

    public void setValue(short idx, int value) {
	switch (idx) {
	    case FIELD_FLAGS:
		m_flags = value;
		break;
	    case FIELD_LEVEL:
		m_level = value;
		break;
	}
	super.setValue(idx,value);
    }

}
