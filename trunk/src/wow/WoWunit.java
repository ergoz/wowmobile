// World of Warcraft Mobile
//
// Generic unit object - creature or player

package wow;

public class WoWunit extends WoWobject {

    public static final short FIELD_HEALTH       = FIELD_END_OBJECT + 0x0012;
    public static final short FIELD_POWER        = FIELD_END_OBJECT + 0x0013;
    public static final short FIELD_MAXHEALTH    = FIELD_END_OBJECT + 0x001A;
    public static final short FIELD_MAXPOWER     = FIELD_END_OBJECT + 0x001B;
    public static final short FIELD_LEVEL        = FIELD_END_OBJECT + 0x0030;
    public static final short FIELD_FLAGS        = FIELD_END_OBJECT + 0x0035;
    public static final short FIELD_FLAGS2       = FIELD_END_OBJECT + 0x0036;
    public static final short FIELD_NPC_FLAGS    = FIELD_END_OBJECT + 0x004C;
    public static final short FIELD_END_UNIT     = FIELD_END_OBJECT + 0x008E;

    public static final int NPC_GOSSIP                       = 0x00000001;
    public static final int NPC_QUESTGIVER                   = 0x00000002;
    public static final int NPC_TRAINER                      = 0x00000010;

    protected int m_health;
    protected int m_healthMax;
    protected int m_power;
    protected int m_powerMax;
    protected int m_level;
    protected int m_flags;
    protected int m_flags2;
    protected int m_npcFlags;

    public WoWunit(long guid) {
	super(guid);
	m_health = 50;
	m_healthMax = 100;
	m_power = 50;
	m_powerMax = 100;
	m_level = 1;
	m_flags = 0;
	m_flags2 = 0;
	m_npcFlags = 0;
    }

    public int health() {
	return m_health;
    }

    public int healthMax() {
	return m_healthMax;
    }

    public int power() {
	return m_power;
    }

    public int powerMax() {
	return m_powerMax;
    }

    public int level() {
	return m_level;
    }

    public int flags() {
	return m_flags;
    }

    public int flags2() {
	return m_flags2;
    }

    public int npcFlags() {
	return m_npcFlags;
    }

    public void setValue(short idx, int value) {
	switch (idx) {
	    case FIELD_HEALTH:
		m_health = value;
		break;
	    case FIELD_POWER:
		m_power = value;
		break;
	    case FIELD_MAXHEALTH:
		m_healthMax = value;
		break;
	    case FIELD_MAXPOWER:
		m_powerMax = value;
		break;
	    case FIELD_LEVEL:
		m_level = value;
		break;
	    case FIELD_FLAGS:
		m_flags = value;
		break;
	    case FIELD_FLAGS2:
		m_flags2 = value;
		break;
	    case FIELD_NPC_FLAGS:
		m_npcFlags = value;
		break;
	}
	super.setValue(idx,value);
    }

}
