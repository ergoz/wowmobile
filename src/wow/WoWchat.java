// World of Warcraft Mobile
//
// Chat message object

package wow;

public class WoWchat {

    public static final int MSG_SYSTEM                   = 0x00;
    public static final int MSG_SAY                      = 0x01;
    public static final int MSG_YELL                     = 0x06;
    public static final int MSG_WHISPER                  = 0x07;
    public static final int MSG_WHISPER_FOREIGN          = 0x08;
    public static final int MSG_WHISPER_INFORM           = 0x09;
    public static final int MSG_MONSTER_SAY              = 0x0C;
    public static final int MSG_MONSTER_PARTY            = 0x0D;
    public static final int MSG_MONSTER_YELL             = 0x0E;
    public static final int MSG_MONSTER_WHISPER          = 0x0F;
    public static final int MSG_MONSTER_EMOTE            = 0x10;
    public static final int MSG_CHANNEL                  = 0x11;
    public static final int MSG_RAID_BOSS_EMOTE          = 0x29;
    public static final int MSG_RAID_BOSS_WHISPER        = 0x2A;
    public static final int MSG_BATTLENET                = 0x2F;

    private int m_type;
    private int m_lang;
    private long m_guid;
    private String m_name;
    private long m_targetGuid;
    private String m_targetName;
    private String m_text;
    private int m_tag;
    
    public WoWchat(WoWpacket pkt) {
	pkt.reset();
	unpack(pkt);
    }

    public int type() {
	return m_type;
    }

    public String name() {
	return m_name;
    }

    public String text() {
	return m_text;
    }

    public static String textType(int type) {
	switch (type) {
	    case MSG_YELL:
	    case MSG_MONSTER_YELL:
		return "yells";
	    case MSG_WHISPER:
	    case MSG_WHISPER_FOREIGN:
	    case MSG_WHISPER_INFORM:
	    case MSG_MONSTER_WHISPER:
	    case MSG_RAID_BOSS_WHISPER:
		return "whispers";
	    default:
		return "says";
	}
    }

    public String textType() {
	return textType(m_type);
    }

    public void unpack(WoWpacket pkt) {
	m_type = pkt.getByte();
	m_lang = pkt.getInt();
	m_guid = pkt.getLong();
	pkt.getInt();
	m_name = null;
	m_targetGuid = 0;
	m_targetName = null;
	boolean repl = false;
	switch (m_type) {
	    case MSG_MONSTER_SAY:
	    case MSG_MONSTER_PARTY:
	    case MSG_MONSTER_YELL:
	    case MSG_MONSTER_WHISPER:
	    case MSG_MONSTER_EMOTE:
	    case MSG_RAID_BOSS_WHISPER:
	    case MSG_RAID_BOSS_EMOTE:
		repl = true;
	    case MSG_BATTLENET:
		pkt.getInt(); // name length
		m_name = pkt.getString();
		m_targetGuid = pkt.getLong();
		if (m_targetGuid != 0 && WoWobject.guidType(m_targetGuid) != WoWobject.HIGHGUID_PLAYER) {
		    pkt.getInt(); // target length
		    m_targetName = pkt.getString();
		}
		break;
	    case MSG_CHANNEL:
		m_name = pkt.getString();
		// fall through
	    default:
		m_targetGuid = pkt.getLong();
		break;
	}
	pkt.getInt(); // text length
	String s = pkt.getString();
	m_tag = pkt.getByte();
	if (repl) {
	    int p;
	    while ((p = s.indexOf("%s")) >= 0)
		s = s.substring(0,p)+m_name+s.substring(p+2);
	    while ((p = s.toUpperCase().indexOf("$N")) >= 0)
		s = s.substring(0,p)+WoWgame.self().playerName()+s.substring(p+2);
//	    while ((p = s.toUpperCase().indexOf("$C")) >= 0)
//	    	s = s.substring(0,p)+WoWgame.self().playerClass()+s.substring(p+2);
//	    while ((p = s.toUpperCase().indexOf("$R")) >= 0)
//	    	s = s.substring(0,p)+WoWgame.self().playerRace()+s.substring(p+2);
//	    while ((p = s.toUpperCase().indexOf("$B")) >= 0)
//	    	s = s.substring(0,p)+"\n"+s.substring(p+2);
	}
	m_text = s;
	WoWgame.self().showDebug(m_name+" "+textType()+": "+m_text+" (type "+m_type+")");
    }

}
