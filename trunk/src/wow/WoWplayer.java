// World of Warcraft Mobile
//
// Player object

package wow;

public class WoWplayer extends WoWunit {

    public static final short FIELD_DUEL_ARBITER     = FIELD_END_UNIT + 0x0000;
    public static final short FIELD_FLAGS            = FIELD_END_UNIT + 0x0002;
    public static final short FIELD_QUEST_LOGS       = FIELD_END_UNIT + 0x000A;
    public static final short FIELD_VISIBLE_ITEMS    = FIELD_END_UNIT + 0x0087;
    public static final short FIELD_XP               = FIELD_END_UNIT + 0x01E6;
    public static final short FIELD_NEXT_LEVEL_XP    = FIELD_END_UNIT + 0x01E7;
    public static final short FIELD_END_PLAYER       = FIELD_END_UNIT + 0x049A;

    private int m_flags;
    private int m_xp;
    private int m_xpNext;
    private String m_name;

    public WoWplayer(long guid) {
	super(guid);
	m_flags = 0;
	m_xp = 0;
	m_xpNext = 1000;
	m_name = null;
    }

    public int xp() {
	return m_xp;
    }

    public int xpNext() {
	return m_xpNext;
    }

    public String name() {
	return m_name;
    }

    public void setValue(short idx, int value) {
	switch (idx) {
	    case FIELD_FLAGS:
		m_flags = value;
		break;
	    case FIELD_XP:
		m_xp = value;
		break;
	    case FIELD_NEXT_LEVEL_XP:
		m_xpNext = value;
		break;
	}
	super.setValue(idx,value);
    }

    public void unpack(WoWpacket pkt) {
	// SMSG_NAME_QUERY_RESPONSE - guid skipped
	pkt.getByte();
	m_name = pkt.getString();
	pkt.getString(); // realm
	int race = pkt.getByte();
	int gender = pkt.getByte();
	int pclass = pkt.getByte();
	// decode declined names
	WoWgame.self().setDebug("Player "+WoWgame.self().guidStr(guid())+" is "+m_name);
    }

    public void levelUp(WoWpacket pkt) {
	// SMSG_LEVELUP_INFO - full
	m_level = pkt.getInt();
	WoWgame.self().setLog("Advanced to level "+m_level);
	m_healthMax += pkt.getInt();
	m_powerMax += pkt.getInt();
	// more powers and stats here but we can ignore for now
    }

}
