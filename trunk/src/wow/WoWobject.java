// World of Warcraft Mobile
//
// Generic world object

package wow;

public class WoWobject extends WoWguid {

    public static final short HIGHGUID_PLAYER        = (short)0x0000;
    public static final short HIGHGUID_GROUP         = (short)0x1F50;
    public static final short HIGHGUID_MO_TRANSPORT  = (short)0x1FC0;
    public static final short HIGHGUID_ITEM          = (short)0x4000;
    public static final short HIGHGUID_DYNAMICOBJECT = (short)0xF100;
    public static final short HIGHGUID_CORPSE        = (short)0xF101;
    public static final short HIGHGUID_GAMEOBJECT    = (short)0xF110;
    public static final short HIGHGUID_TRANSPORT     = (short)0xF120;
    public static final short HIGHGUID_UNIT          = (short)0xF130;
    public static final short HIGHGUID_PET           = (short)0xF140;
    public static final short HIGHGUID_VEHICLE       = (short)0xF150;

    public static final short FIELD_GUID             = 0x0000; // long
    public static final short FIELD_TYPE             = 0x0002;
    public static final short FIELD_ENTRY            = 0x0003;
    public static final short FIELD_SCALE_X          = 0x0004; // float
    public static final short FIELD_END_OBJECT       = 0x0006;

    public WoWcoord m_pos;
    private int m_type;
    private int m_entry;
    private float m_scale;

    public WoWobject(long guid) {
	super(guid);
	m_pos = null;
	m_type = 0;
	m_entry = 0;
	m_scale = 1f;
    }

    public static short guidType(long uid) {
	return (short)(uid >> 48);
    }

    public short guidType() {
	return guidType(guid());
    }

    public static final String textType(long uid) {
	if (uid == 0)
	    return "none";
	switch (guidType(uid)) {
	    case HIGHGUID_PLAYER:
		return "player";
	    case HIGHGUID_GROUP:
		return "group";
	    case HIGHGUID_MO_TRANSPORT:
		return "motransport";
	    case HIGHGUID_ITEM:
		return "item";
	    case HIGHGUID_DYNAMICOBJECT:
		return "dynobject";
	    case HIGHGUID_CORPSE:
		return "corpse";
	    case HIGHGUID_GAMEOBJECT:
		return "gameobj";
	    case HIGHGUID_TRANSPORT:
		return "transport";
	    case HIGHGUID_UNIT:
		return "creature";
	    case HIGHGUID_PET:
		return "pet";
	    case HIGHGUID_VEHICLE:
		return "vehicle";
	}
	return null;
    }

    public final String textType() {
	return textType(guid());
    }

    public synchronized WoWcoord pos(boolean create) {
	if (create && m_pos == null)
	    m_pos = new WoWcoord();
	return m_pos;
    }

    public WoWcoord pos() {
	return pos(true);
    }

    public int type() {
	return m_type;
    }

    public int entry() {
	return m_entry;
    }

    public float scale() {
	return m_scale;
    }

    public void setValue(short idx, int value) {
	switch (idx) {
	    case FIELD_TYPE:
		m_type = value;
		break;
	    case FIELD_ENTRY:
		m_entry = value;
		break;
	    case FIELD_SCALE_X:
		m_scale = Float.intBitsToFloat(value);
		break;
	}
    }

    public static long updateLow(long value, int loWord) {
	return (value & 0xffffffff00000000l) | loWord;
    }

    public static long updateHigh(long value, int hiWord) {
	return (value & 0xffffffff) | ((long)hiWord) << 32;
    }

    public static WoWobject create(long uid) {
	if (uid == 0)
	    return null;
	switch (guidType(uid)) {
	    case HIGHGUID_PLAYER:
		return new WoWplayer(uid);
	    case HIGHGUID_CORPSE:
		return new WoWcorpse(uid);
	    case HIGHGUID_GAMEOBJECT:
		return new WoWgobj(uid);
	    case HIGHGUID_UNIT:
		return new WoWunit(uid);
	    default:
		return new WoWobject(uid);
	}
    }

}
