// World of Warcraft Mobile
//
// Network packet object

package wow;

public class WoWpacket {

    public static final int MSG_NULL_ACTION                           = 0x000;
    public static final int CMSG_CHAR_ENUM                            = 0x037;
    public static final int SMSG_CHAR_ENUM                            = 0x03B;
    public static final int CMSG_PLAYER_LOGIN                         = 0x03D;
    public static final int SMSG_LOGIN_SETTIMESPEED                   = 0x042;
    public static final int CMSG_NAME_QUERY                           = 0x050;
    public static final int SMSG_NAME_QUERY_RESPONSE                  = 0x051;
    public static final int CMSG_PET_NAME_QUERY                       = 0x052;
    public static final int SMSG_PET_NAME_QUERY_RESPONSE              = 0x053;
    public static final int CMSG_QUEST_QUERY                          = 0x05C;
    public static final int SMSG_QUEST_QUERY_RESPONSE                 = 0x05D;
    public static final int CMSG_GAMEOBJECT_QUERY                     = 0x05E;
    public static final int SMSG_GAMEOBJECT_QUERY_RESPONSE            = 0x05F;
    public static final int CMSG_CREATURE_QUERY                       = 0x060;
    public static final int SMSG_CREATURE_QUERY_RESPONSE              = 0x061;
    public static final int SMSG_MESSAGECHAT                          = 0x096;
    public static final int SMSG_UPDATE_OBJECT                        = 0x0A9;
    public static final int SMSG_DESTROY_OBJECT                       = 0x0AA;
    public static final int MSG_MOVE_START_FORWARD                    = 0x0B5;
    public static final int MSG_MOVE_START_BACKWARD                   = 0x0B6;
    public static final int MSG_MOVE_STOP                             = 0x0B7;
    public static final int MSG_MOVE_START_STRAFE_LEFT                = 0x0B8;
    public static final int MSG_MOVE_START_STRAFE_RIGHT               = 0x0B9;
    public static final int MSG_MOVE_STOP_STRAFE                      = 0x0BA;
    public static final int MSG_MOVE_JUMP                             = 0x0BB;
    public static final int MSG_MOVE_START_TURN_LEFT                  = 0x0BC;
    public static final int MSG_MOVE_START_TURN_RIGHT                 = 0x0BD;
    public static final int MSG_MOVE_STOP_TURN                        = 0x0BE;
    public static final int MSG_MOVE_FALL_LAND                        = 0x0C9;
    public static final int MSG_MOVE_SET_RUN_SPEED                    = 0x0CD;
    public static final int MSG_MOVE_SET_WALK_SPEED                   = 0x0D1;
    public static final int MSG_MOVE_SET_FACING                       = 0x0DA;
    public static final int MSG_MOVE_SET_PITCH                        = 0x0DB;
    public static final int MSG_MOVE_WORLDPORT_ACK                    = 0x0DC;
    public static final int SMSG_MONSTER_MOVE                         = 0x0DD;
    public static final int SMSG_MOVE_WATER_WALK                      = 0x0DE;
    public static final int SMSG_MOVE_LAND_WALK                       = 0x0DF;
    public static final int SMSG_FORCE_RUN_SPEED_CHANGE               = 0x0E2;
    public static final int SMSG_FORCE_SWIM_SPEED_CHANGE              = 0x0E6;
    public static final int SMSG_FORCE_MOVE_ROOT                      = 0x0E8;
    public static final int SMSG_FORCE_MOVE_UNROOT                    = 0x0EA;
    public static final int MSG_MOVE_HEARTBEAT                        = 0x0EE;
    public static final int SMSG_TUTORIAL_FLAGS                       = 0x0FD;
    public static final int SMSG_EMOTE                                = 0x103;
    public static final int SMSG_TEXT_EMOTE                           = 0x105;
    public static final int SMSG_TRADE_STATUS                         = 0x120;
    public static final int SMSG_INITIALIZE_FACTIONS                  = 0x122;
    public static final int SMSG_SET_PROFICIENCY                      = 0x127;
    public static final int SMSG_ACTION_BUTTONS                       = 0x129;
    public static final int SMSG_INITIAL_SPELLS                       = 0x12A;
    public static final int CMSG_CAST_SPELL                           = 0x12E;
    public static final int CMSG_CANCEL_CAST                          = 0x12F;
    public static final int SMSG_CAST_FAILED                          = 0x130;
    public static final int SMSG_SPELL_START                          = 0x131;
    public static final int SMSG_SPELL_GO                             = 0x132;
    public static final int SMSG_SPELL_FAILURE                        = 0x133;
    public static final int SMSG_SPELL_COOLDOWN                       = 0x134;
    public static final int SMSG_AI_REACTION                          = 0x13C;
    public static final int CMSG_ATTACKSWING                          = 0x141;
    public static final int CMSG_ATTACKSTOP                           = 0x142;
    public static final int SMSG_ATTACKSTART                          = 0x143;
    public static final int SMSG_ATTACKSTOP                           = 0x144;
    public static final int SMSG_ATTACKSWING_NOTINRANGE               = 0x145;
    public static final int SMSG_ATTACKSWING_BADFACING                = 0x146;
    public static final int SMSG_ATTACKSWING_DEADTARGET               = 0x148;
    public static final int SMSG_ATTACKSWING_CANT_ATTACK              = 0x149;
    public static final int SMSG_ATTACKERSTATEUPDATE                  = 0x14A;
    public static final int SMSG_CANCEL_COMBAT                        = 0x14E;
    public static final int SMSG_BINDPOINTUPDATE                      = 0x155;
    public static final int SMSG_DUEL_REQUESTED                       = 0x167;
    public static final int SMSG_DUEL_OUTOFBOUNDS                     = 0x168;
    public static final int SMSG_DUEL_INBOUNDS                        = 0x169;
    public static final int SMSG_DUEL_COMPLETE                        = 0x16A;
    public static final int SMSG_DUEL_WINNER                          = 0x16B;
    public static final int CMSG_DUEL_ACCEPTED                        = 0x16C;
    public static final int CMSG_DUEL_CANCELLED                       = 0x16D;
    public static final int SMSG_LOG_XPGAIN                           = 0x1D0;
    public static final int CMSG_RECLAIM_CORPSE                       = 0x1D2;
    public static final int SMSG_LEVELUP_INFO                         = 0x1D4;
    public static final int CMSG_PING                                 = 0x1DC;
    public static final int SMSG_PONG                                 = 0x1DD;
    public static final int SMSG_AUTH_CHALLENGE                       = 0x1EC;
    public static final int CMSG_AUTH_SESSION                         = 0x1ED;
    public static final int SMSG_AUTH_RESPONSE                        = 0x1EE;
    public static final int SMSG_PARTYKILLLOG                         = 0x1F5;
    public static final int SMSG_COMPRESSED_UPDATE_OBJECT             = 0x1F6;
    public static final int SMSG_ENVIRONMENTALDAMAGELOG               = 0x1FC;
    public static final int SMSG_ACCOUNT_DATA_TIMES                   = 0x209;
    public static final int SMSG_LOGIN_VERIFY_WORLD                   = 0x236;
    public static final int CMSG_ITEM_TEXT_QUERY                      = 0x243;
    public static final int SMSG_ITEM_TEXT_QUERY_RESPONSE             = 0x244;
    public static final int SMSG_SPELLLOGEXECUTE                      = 0x24C;
    public static final int SMSG_PERIODICAURALOG                      = 0x24E;
    public static final int SMSG_SPELLDAMAGESHIELD                    = 0x24F;
    public static final int SMSG_SPELLNONMELEEDAMAGELOG               = 0x250;
    public static final int SMSG_SERVER_MESSAGE                       = 0x291;
    public static final int SMSG_STANDSTATE_UPDATE                    = 0x29D;
    public static final int SMSG_SET_FORCED_REACTIONS                 = 0x2A5;
    public static final int SMSG_SPELL_FAILED_OTHER                   = 0x2A6;
    public static final int SMSG_DUEL_COUNTDOWN                       = 0x2B7;
    public static final int SMSG_DURABILITY_DAMAGE_DEATH              = 0x2BD;
    public static final int SMSG_INIT_WORLD_STATES                    = 0x2C2;
    public static final int SMSG_UPDATE_WORLD_STATE                   = 0x2C3;
    public static final int CMSG_ITEM_NAME_QUERY                      = 0x2C4;
    public static final int SMSG_ITEM_NAME_QUERY_RESPONSE             = 0x2C5;
    public static final int SMSG_ADDON_INFO                           = 0x2EF;
    public static final int SMSG_WEATHER                              = 0x2F4;
    public static final int MSG_SET_DUNGEON_DIFFICULTY                = 0x329;
    public static final int SMSG_INSTANCE_DIFFICULTY                  = 0x33B;
    public static final int SMSG_MOTD                                 = 0x33D;
    public static final int SMSG_TIME_SYNC_REQ                        = 0x390;
    public static final int CMSG_TIME_SYNC_RESP                       = 0x391;
    public static final int SMSG_FEATURE_SYSTEM_STATUS                = 0x3C9;
    public static final int SMSG_LOOT_LIST                            = 0x3F9;
    public static final int SMSG_SEND_UNLEARN_SPELLS                  = 0x41E;
    public static final int SMSG_LEARNED_DANCE_MOVES                  = 0x455;
    public static final int SMSG_ACHIEVEMENT_EARNED                   = 0x468;
    public static final int SMSG_CRITERIA_UPDATE                      = 0x46A;
    public static final int SMSG_ALL_ACHIEVEMENT_DATA                 = 0x47D;
    public static final int SMSG_POWER_UPDATE                         = 0x480;
    public static final int SMSG_HIGHEST_THREAT_UPDATE                = 0x482;
    public static final int SMSG_THREAT_UPDATE                        = 0x483;
    public static final int SMSG_THREAT_REMOVE                        = 0x484;
    public static final int SMSG_THREAT_CLEAR                         = 0x485;
    public static final int SMSG_AURA_UPDATE_ALL                      = 0x495;
    public static final int SMSG_AURA_UPDATE                          = 0x496;
    public static final int SMSG_CLIENTCACHE_VERSION                  = 0x4AB;
    public static final int SMSG_EQUIPMENT_SET_LIST                   = 0x4BC;
    public static final int SMSG_TALENTS_INFO                         = 0x4C0;

    private int m_code;
    private byte[] m_data;
    private int m_pos;

    public WoWpacket(int code, final byte[] data) {
	m_code = code;
	m_data = data;
	m_pos = 0;
    }

    public WoWpacket(int code) {
	m_code = code;
	m_data = null;
	m_pos = 0;
    }

    public int code() {
	return m_code;
    }

    public final byte[] data() {
	return m_data;
    }

    public int size() {
	return (m_data != null) ? m_data.length : 0;
    }

    public boolean eof() {
	return (m_data == null) || (m_pos >= m_data.length);
    }

    public int pos() {
	return m_pos;
    }

    public int avail() {
	return (m_data != null) ? (m_data.length - m_pos) : 0;
    }

    public void reset() {
	m_pos = 0;
    }

    public void seek(int pos) {
	m_pos = pos;
    }

    public void skip(int ofs) {
	m_pos += ofs;
    }

    public int getByte(int defVal) {
	if (eof())
	    return defVal;
	int i = m_pos;
	m_pos++;
	return (m_data[i] & 0xff);
    }

    public int getByte() {
	return getByte(0);
    }

    public int getWord(int defVal) {
	if (avail() < 2)
	    return defVal;
	int i = m_pos;
	m_pos += 2;
	return (m_data[i] & 0xff) | (m_data[i+1] & 0xff) << 8;
    }

    public int getWord() {
	return getWord(0);
    }

    public int getInt(int defVal) {
	if (avail() < 4)
	    return defVal;
	int i = m_pos;
	m_pos += 4;
	return (m_data[i] & 0xff) | (m_data[i+1] & 0xff) << 8 |
	    (m_data[i+2] & 0xff) << 16 | (m_data[i+3] & 0xff) << 24;
    }

    public int getInt() {
	return getInt(0);
    }

    public long getLong(long defVal) {
	if (avail() < 8)
	    return defVal;
	int i = m_pos;
	m_pos += 8;
	int lw = (m_data[i] & 0xff) | (m_data[i+1] & 0xff) << 8 |
	    (m_data[i+2] & 0xff) << 16 | (m_data[i+3] & 0xff) << 24;
	int hw = (m_data[i+4] & 0xff) | (m_data[i+5] & 0xff) << 8 |
	    (m_data[i+6] & 0xff) << 16 | (m_data[i+7] & 0xff) << 24;
	return lw | ((long)hw) << 32;
    }

    public long getLong() {
	return getLong(0);
    }

    public long getGuid(long defVal) {
	if (avail() < 1)
	    return defVal;
	long uid = 0;
	int i = m_pos;
	int mask = m_data[i++] & 0xff;
	for (int b = 0; b < 8; b++) {
	    if ((mask & (1 << b)) == 0)
		continue;
	    if (i >= m_data.length)
		return defVal;
	    uid |= (long)(m_data[i++] & 0xff) << (8 * b);
	}
	m_pos = i;
	return uid;
    }

    public long getGuid() {
	return getGuid(0);
    }

    public String getString(final String defVal) {
	if (eof())
	    return defVal;
	for (int i = m_pos; i < m_data.length; i++) {
	    if (m_data[i] == 0) {
		String s = new String(m_data,m_pos,i-m_pos);
		m_pos = i+1;
		return s;
	    }
	}
	return defVal;
    }

    public String getString() {
	return getString(null);
    }

    public float getFloat(float defVal) {
	if (avail() < 4)
	    return defVal;
	return Float.intBitsToFloat(getInt());
    }

    public float getFloat() {
	return getFloat((float)0.0);
    }

    public float[] getFloats(int num) {
	if (avail() < (4 * num))
	    return null;
	float tmp[] = new float[num];
	for (int i = 0; i < num; i++)
	    tmp[i] = getFloat();
	return tmp;
    }

    public byte[] getBytes(int num) {
	if (avail() < num)
	    return null;
	byte[] buf = new byte[num];
	System.arraycopy(m_data,m_pos,buf,0,num);
	m_pos += num;
	return buf;
    }

    public void addBytes(byte[] data) {
	int l1 = (m_data != null) ? m_data.length : 0;
	byte[] tmp = new byte[l1+data.length];
	int d = 0;
	int i;
	for (i = 0; i < l1; i++)
	    tmp[d++] = m_data[i];
	for (i = 0; i < data.length; i++)
	    tmp[d++] = data[i];
	m_data = tmp;
    }

    public void addByte(int val) {
	byte[] data = new byte[1];
	data[0] = (byte)val;
	addBytes(data);
    }

    public void addWord(int val) {
	byte[] data = new byte[2];
	data[0] = (byte)val;
	data[1] = (byte)(val >> 8);
	addBytes(data);
    }

    public void addInt(int val) {
	byte[] data = new byte[4];
	data[0] = (byte)val;
	data[1] = (byte)(val >> 8);
	data[2] = (byte)(val >> 16);
	data[3] = (byte)(val >> 24);
	addBytes(data);
    }

    public void addLong(long val) {
	byte[] data = new byte[8];
	data[0] = (byte)val;
	data[1] = (byte)(val >> 8);
	data[2] = (byte)(val >> 16);
	data[3] = (byte)(val >> 24);
	data[4] = (byte)(val >> 32);
	data[5] = (byte)(val >> 40);
	data[6] = (byte)(val >> 48);
	data[7] = (byte)(val >> 56);
	addBytes(data);
    }

    public void addGuid(long guid) {
	int mask = 0;
	int bytes = 1;
	for (int b = 0; b < 8; b++) {
	    if ((guid & (((long)0xff) << (8 * b))) != 0) {
		mask |= (1 << b);
		bytes++;
	    }
	}
	byte[] data = new byte[bytes];
	data[0] = (byte)mask;
	bytes = 1;
	for (int b = 0; b < 8; b++) {
	    if ((mask & (1 << b)) != 0)
		data[bytes++] = (byte)(guid >> (8 * b));
	}
	addBytes(data);
    }

    public void addString(final String val) {
	addBytes(val.getBytes());
	addByte(0);
    }

    public void addFloat(float val) {
	addInt(Float.floatToIntBits(val));
    }

    public void addFloats(float val[]) {
	if (val == null)
	    return;
	for (int i = 0; i < val.length; i++)
	    addFloat(val[i]);
    }

    public static final String name(int code) {
	switch (code) {
	    case MSG_NULL_ACTION:
		return "MSG_NULL_ACTION";
	    case SMSG_CHAR_ENUM:
		return "SMSG_CHAR_ENUM";
	    case SMSG_LOGIN_SETTIMESPEED:
		return "SMSG_LOGIN_SETTIMESPEED";
	    case SMSG_NAME_QUERY_RESPONSE:
		return "SMSG_NAME_QUERY_RESPONSE";
	    case SMSG_PET_NAME_QUERY_RESPONSE:
		return "SMSG_PET_NAME_QUERY_RESPONSE";
	    case SMSG_QUEST_QUERY_RESPONSE:
		return "SMSG_QUEST_QUERY_RESPONSE";
	    case SMSG_GAMEOBJECT_QUERY_RESPONSE:
		return "SMSG_GAMEOBJECT_QUERY_RESPONSE";
	    case SMSG_CREATURE_QUERY_RESPONSE:
		return "SMSG_CREATURE_QUERY_RESPONSE";
	    case SMSG_MESSAGECHAT:
		return "SMSG_MESSAGECHAT";
	    case SMSG_UPDATE_OBJECT:
		return "SMSG_UPDATE_OBJECT";
	    case SMSG_DESTROY_OBJECT:
		return "SMSG_DESTROY_OBJECT";
	    case MSG_MOVE_START_FORWARD:
		return "MSG_MOVE_START_FORWARD";
	    case MSG_MOVE_START_BACKWARD:
		return "MSG_MOVE_START_BACKWARD";
	    case MSG_MOVE_STOP:
		return "MSG_MOVE_STOP";
	    case MSG_MOVE_START_STRAFE_LEFT:
		return "MSG_MOVE_START_STRAFE_LEFT";
	    case MSG_MOVE_START_STRAFE_RIGHT:
		return "MSG_MOVE_START_STRAFE_RIGHT";
	    case MSG_MOVE_STOP_STRAFE:
		return "MSG_MOVE_STOP_STRAFE";
	    case MSG_MOVE_JUMP:
		return "MSG_MOVE_JUMP";
	    case MSG_MOVE_START_TURN_LEFT:
		return "MSG_MOVE_START_TURN_LEFT";
	    case MSG_MOVE_START_TURN_RIGHT:
		return "MSG_MOVE_START_TURN_RIGHT";
	    case MSG_MOVE_STOP_TURN:
		return "MSG_MOVE_STOP_TURN";
	    case MSG_MOVE_FALL_LAND:
		return "MSG_MOVE_FALL_LAND";
	    case MSG_MOVE_SET_RUN_SPEED:
		return "MSG_MOVE_SET_RUN_SPEED";
	    case MSG_MOVE_SET_WALK_SPEED:
		return "MSG_MOVE_SET_WALK_SPEED";
	    case MSG_MOVE_SET_FACING:
		return "MSG_MOVE_SET_FACING";
	    case MSG_MOVE_SET_PITCH:
		return "MSG_MOVE_SET_PITCH";
	    case SMSG_MONSTER_MOVE:
		return "SMSG_MONSTER_MOVE";
	    case SMSG_MOVE_WATER_WALK:
		return "SMSG_MOVE_WATER_WALK";
	    case SMSG_MOVE_LAND_WALK:
		return "SMSG_MOVE_LAND_WALK";
	    case SMSG_FORCE_RUN_SPEED_CHANGE:
		return "SMSG_FORCE_RUN_SPEED_CHANGE";
	    case SMSG_FORCE_SWIM_SPEED_CHANGE:
		return "SMSG_FORCE_SWIM_SPEED_CHANGE";
	    case SMSG_FORCE_MOVE_ROOT:
		return "SMSG_FORCE_MOVE_ROOT";
	    case SMSG_FORCE_MOVE_UNROOT:
		return "SMSG_FORCE_MOVE_UNROOT";
	    case MSG_MOVE_HEARTBEAT:
		return "MSG_MOVE_HEARTBEAT";
	    case SMSG_TUTORIAL_FLAGS:
		return "SMSG_TUTORIAL_FLAGS";
	    case SMSG_EMOTE:
		return "SMSG_EMOTE";
	    case SMSG_TEXT_EMOTE:
		return "SMSG_TEXT_EMOTE";
	    case SMSG_TRADE_STATUS:
		return "SMSG_TRADE_STATUS";
	    case SMSG_INITIALIZE_FACTIONS:
		return "SMSG_INITIALIZE_FACTIONS";
	    case SMSG_SET_PROFICIENCY:
		return "SMSG_SET_PROFICIENCY";
	    case SMSG_ACTION_BUTTONS:
		return "SMSG_ACTION_BUTTONS";
	    case SMSG_INITIAL_SPELLS:
		return "SMSG_INITIAL_SPELLS";
	    case SMSG_CAST_FAILED:
		return "SMSG_CAST_FAILED";
	    case SMSG_SPELL_START:
		return "SMSG_SPELL_START";
	    case SMSG_SPELL_GO:
		return "SMSG_SPELL_GO";
	    case SMSG_SPELL_FAILURE:
		return "SMSG_SPELL_FAILURE";
	    case SMSG_SPELL_COOLDOWN:
		return "SMSG_SPELL_COOLDOWN";
	    case SMSG_AI_REACTION:
		return "SMSG_AI_REACTION";
	    case SMSG_ATTACKSTART:
		return "SMSG_ATTACKSTART";
	    case SMSG_ATTACKSTOP:
		return "SMSG_ATTACKSTOP";
	    case SMSG_ATTACKSWING_NOTINRANGE:
		return "SMSG_ATTACKSWING_NOTINRANGE";
	    case SMSG_ATTACKSWING_BADFACING:
		return "SMSG_ATTACKSWING_BADFACING";
	    case SMSG_ATTACKSWING_DEADTARGET:
		return "SMSG_ATTACKSWING_DEADTARGET";
	    case SMSG_ATTACKSWING_CANT_ATTACK:
		return "SMSG_ATTACKSWING_CANT_ATTACK";
	    case SMSG_ATTACKERSTATEUPDATE:
		return "SMSG_ATTACKERSTATEUPDATE";
	    case SMSG_CANCEL_COMBAT:
		return "SMSG_CANCEL_COMBAT";
	    case SMSG_BINDPOINTUPDATE:
		return "SMSG_BINDPOINTUPDATE";
	    case SMSG_DUEL_REQUESTED:
		return "SMSG_DUEL_REQUESTED";
	    case SMSG_DUEL_OUTOFBOUNDS:
		return "SMSG_DUEL_OUTOFBOUNDS";
	    case SMSG_DUEL_INBOUNDS:
		return "SMSG_DUEL_INBOUNDS";
	    case SMSG_DUEL_COMPLETE:
		return "SMSG_DUEL_COMPLETE";
	    case SMSG_DUEL_WINNER:
		return "SMSG_DUEL_WINNER";
	    case SMSG_LOG_XPGAIN:
		return "SMSG_LOG_XPGAIN";
	    case SMSG_LEVELUP_INFO:
		return "SMSG_LEVELUP_INFO";
	    case SMSG_PONG:
		return "SMSG_PONG";
	    case SMSG_AUTH_CHALLENGE:
		return "SMSG_AUTH_CHALLENGE";
	    case SMSG_AUTH_RESPONSE:
		return "SMSG_AUTH_RESPONSE";
	    case SMSG_PARTYKILLLOG:
		return "SMSG_PARTYKILLLOG";
	    case SMSG_COMPRESSED_UPDATE_OBJECT:
		return "SMSG_COMPRESSED_UPDATE_OBJECT";
	    case SMSG_ENVIRONMENTALDAMAGELOG:
		return "SMSG_ENVIRONMENTALDAMAGELOG";
	    case SMSG_ACCOUNT_DATA_TIMES:
		return "SMSG_ACCOUNT_DATA_TIMES";
	    case SMSG_LOGIN_VERIFY_WORLD:
		return "SMSG_LOGIN_VERIFY_WORLD";
	    case SMSG_ITEM_TEXT_QUERY_RESPONSE:
		return "SMSG_ITEM_TEXT_QUERY_RESPONSE";
	    case SMSG_SPELLLOGEXECUTE:
		return "SMSG_SPELLLOGEXECUTE";
	    case SMSG_PERIODICAURALOG:
		return "SMSG_PERIODICAURALOG";
	    case SMSG_SPELLDAMAGESHIELD:
		return "SMSG_SPELLDAMAGESHIELD";
	    case SMSG_SPELLNONMELEEDAMAGELOG:
		return "SMSG_SPELLNONMELEEDAMAGELOG";
	    case SMSG_SERVER_MESSAGE:
		return "SMSG_SERVER_MESSAGE";
	    case SMSG_STANDSTATE_UPDATE:
		return "SMSG_STANDSTATE_UPDATE";
	    case SMSG_SET_FORCED_REACTIONS:
		return "SMSG_SET_FORCED_REACTIONS";
	    case SMSG_SPELL_FAILED_OTHER:
		return "SMSG_SPELL_FAILED_OTHER";
	    case SMSG_DUEL_COUNTDOWN:
		return "SMSG_DUEL_COUNTDOWN";
	    case SMSG_DURABILITY_DAMAGE_DEATH:
		return "SMSG_DURABILITY_DAMAGE_DEATH";
	    case SMSG_INIT_WORLD_STATES:
		return "SMSG_INIT_WORLD_STATES";
	    case SMSG_UPDATE_WORLD_STATE:
		return "SMSG_UPDATE_WORLD_STATE";
	    case SMSG_ITEM_NAME_QUERY_RESPONSE:
		return "SMSG_ITEM_NAME_QUERY_RESPONSE";
	    case SMSG_ADDON_INFO:
		return "SMSG_ADDON_INFO";
	    case SMSG_WEATHER:
		return "SMSG_WEATHER";
	    case MSG_SET_DUNGEON_DIFFICULTY:
		return "MSG_SET_DUNGEON_DIFFICULTY";
	    case SMSG_INSTANCE_DIFFICULTY:
		return "SMSG_INSTANCE_DIFFICULTY";
	    case SMSG_MOTD:
		return "SMSG_MOTD";
	    case SMSG_TIME_SYNC_REQ:
		return "SMSG_TIME_SYNC_REQ";
	    case SMSG_FEATURE_SYSTEM_STATUS:
		return "SMSG_FEATURE_SYSTEM_STATUS";
	    case SMSG_LOOT_LIST:
		return "SMSG_LOOT_LIST";
	    case SMSG_SEND_UNLEARN_SPELLS:
		return "SMSG_SEND_UNLEARN_SPELLS";
	    case SMSG_LEARNED_DANCE_MOVES:
		return "SMSG_LEARNED_DANCE_MOVES";
	    case SMSG_ACHIEVEMENT_EARNED:
		return "SMSG_ACHIEVEMENT_EARNED";
	    case SMSG_CRITERIA_UPDATE:
		return "SMSG_CRITERIA_UPDATE";
	    case SMSG_ALL_ACHIEVEMENT_DATA:
		return "SMSG_ALL_ACHIEVEMENT_DATA";
	    case SMSG_POWER_UPDATE:
		return "SMSG_POWER_UPDATE";
	    case SMSG_HIGHEST_THREAT_UPDATE:
		return "SMSG_HIGHEST_THREAT_UPDATE";
	    case SMSG_THREAT_UPDATE:
		return "SMSG_THREAT_UPDATE";
	    case SMSG_THREAT_REMOVE:
		return "SMSG_THREAT_REMOVE";
	    case SMSG_THREAT_CLEAR:
		return "SMSG_THREAT_CLEAR";
	    case SMSG_AURA_UPDATE_ALL:
		return "SMSG_AURA_UPDATE_ALL";
	    case SMSG_AURA_UPDATE:
		return "SMSG_AURA_UPDATE";
	    case SMSG_CLIENTCACHE_VERSION:
		return "SMSG_CLIENTCACHE_VERSION";
	    case SMSG_EQUIPMENT_SET_LIST:
		return "SMSG_EQUIPMENT_SET_LIST";
	    case SMSG_TALENTS_INFO:
		return "SMSG_TALENTS_INFO";
	}
	return "UNKN_0x"+Integer.toHexString(code);
    }

    public final String name() {
	return name(m_code);
    }
}
