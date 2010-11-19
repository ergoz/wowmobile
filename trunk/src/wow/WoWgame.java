// World of Warcraft Mobile
//
// Game status implementation

package wow;

import java.io.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.lang.System;
import com.jcraft.jzlib.*;
import wow.cache.*;

public class WoWgame {

    private static WoWgame s_self = null;
    private boolean m_paused;
    private boolean m_visible;
    private boolean m_silent;
    private boolean m_detail;
    private boolean m_initial;
    private boolean m_interact;
    private boolean m_hasPointer;
    private boolean m_shiftKey;
    private boolean m_shiftLck;
    private boolean m_dragging;
    private boolean m_stopping;
    private boolean m_debugging;
    private int m_width;
    private int m_height;
    private int m_dragStartX;
    private int m_dragStartY;
    private int m_bgColor;
    private int m_bgVolume;
    private int m_fgVolume;
    private long m_lastFps;
    private WoWauth m_auth;
    private WoWconn m_conn;
    private WoWrender m_screen;
    private WoWrender m_prevScreen;
    private WoWsound m_bgSound;
    private WoWsound m_fgSound;
    private long m_lastDrawn;
    private long m_lastUpdate;
    private WoWarea m_area;
    private WoWplayer m_player;
    private String m_playerRealm;
    private String m_playerName;
    private String m_playerLevel;
    private Image m_playerIcon;
    private long m_playerGuid;
    private long m_selectGuid;
    private boolean m_threatened;
    private boolean m_following;
    private String m_mapArea;
    private double m_mapX;
    private double m_mapY;
    private double m_mapZ;
    private double m_mapHeading;
    private double m_mapTurning;
    private double m_mapSpeed;
    private Vector m_actions;
    private Vector m_storage;
    private Hashtable m_effects;
    private Hashtable m_blips;
    private Hashtable m_objects;
    private Hashtable m_cacheCreatures;
    private Hashtable m_cacheGameObjects;
    private Hashtable m_cacheItemNames;
    private boolean m_cacheChanged;
    private String m_altPath;
    private String m_debug;
    private String[] m_logs;
    private String[] m_info;
    private long m_logUp;
    private long m_infoUp;

    private WoWgame() {
	m_paused = true;
	m_visible = true;
	m_silent = false;
	m_detail = true;
	m_initial = true;
	m_interact = false;
	m_shiftKey = false;
	m_shiftLck = false;
	m_dragging = false;
	m_stopping = false;
	m_debugging = false;
	m_width = 0;
	m_height = 0;
	m_dragStartX = 0;
	m_dragStartY = 0;
	m_bgVolume = 50;
	m_fgVolume = 50;
	m_lastFps = 0;
	m_auth = null;
	m_conn = null;
	m_screen = null;
	m_prevScreen = null;
	m_bgColor = 0;
	m_bgSound = null;
	m_fgSound = null;
	m_lastDrawn = 0;
	m_lastUpdate = 0;
	m_playerRealm = "Test Realm";
	m_logs = new String[4];
	for (int i = 0; i < m_logs.length; i++)
	    m_logs[i] = null;
	m_logUp = 0;
	m_info = new String[3];
	for (int i = 0; i < m_info.length; i++)
	    m_info[i] = null;
	m_infoUp = 0;
	loggedOut();
	m_cacheCreatures = new Hashtable();
	m_cacheGameObjects = new Hashtable();
	m_cacheItemNames = new Hashtable();
	m_cacheChanged = false;
	loadCache();
	m_effects = new Hashtable();
	m_effects.put("error",new WoWeffect(0x404040,"/sounds/interface/Error.mp3"));
	m_effects.put("normal",new WoWeffect(0xc00000));
	m_effects.put("holy",new WoWeffect(0xc0c0c0));
	m_effects.put("fire",new WoWeffect(0xff8000));
	m_effects.put("nature",new WoWeffect(0x008000));
	m_effects.put("frost",new WoWeffect(0x4040ff));
	m_effects.put("shadow",new WoWeffect(0x404040));
	m_effects.put("arcane",new WoWeffect(0x808000));
	m_effects.put("swing_sword",new WoWeffect(0xc00000,
	    "/sounds/spells/WarriorSwings/SwingWeaponSpecialWarriorA.mp3",
	    "/sounds/spells/WarriorSwings/SwingWeaponSpecialWarriorB.mp3",
	    "/sounds/spells/WarriorSwings/SwingWeaponSpecialWarriorC.mp3"));
	m_effects.put("immolate",new WoWeffect(0xff8000,"/sounds/spells/Immolate.mp3"));
	m_effects.put("cleave",new WoWeffect(0x40ff40,"/sounds/spells/CleaveTarget.mp3"));
	m_effects.put("thunder_clap",new WoWeffect(0x000080,"/sounds/spells/ThunderClap.mp3"));
	m_effects.put("freeze",new WoWeffect(0x4040ff,"/sounds/spells/Fizzle/FizzleFrostA.mp3"));
	m_effects.put("poison",new WoWeffect(0x008000,"/sounds/spells/RendTarget.mp3"));
	m_effects.put("teleport",new WoWeffect(0x80ff80,"/sounds/spells/Teleport.mp3"));
	m_actions = new Vector(0,1);
	m_storage = new Vector(0,1);
	if (m_actions.isEmpty()) {
	    setAction("abilities/DualWield");
	    setAction("abilities/Gouge");
	    setAction("abilities/ShockWave");
	    setAction("abilities/Warrior_BattleShout");
	    setAction("abilities/Warrior_Charge");
	    setAction("abilities/Warrior_Cleave");
	    setAction("abilities/Warrior_Sunder");
	    setAction("spells/Nature_ThunderClap");
	    setAction("inventory/Sword_05");
	    setAction("inventory/Sword_48");
	    setAction("spells_/Frost_Stun");
	}
	if (m_storage.isEmpty()) {
	    setStorage("inventory/Bag_01");
	    setStorage("inventory/Bag_07_Red");
	    setStorage("inventory/Bag_09");
	    setStorage("inventory/Bag_10_Green");
	    setStorage("inventory/Bag_18");
	    setStorage("inventory/Bag_20");
	}
    }

    public static WoWgame self() {
	if (s_self == null)
	    s_self = new WoWgame();
	return s_self;
    }

    public static boolean stop(boolean unconditional) {
	if (s_self == null)
	    return true;
	return unconditional || s_self.stopEvent();
    }

    public void setExtra(WoWmobile client) {
	m_debugging = m_debugging || "true".equalsIgnoreCase(client.getAppProperty("WoW-Debugging"));
	m_detail = m_detail && !"false".equalsIgnoreCase(client.getAppProperty("WoW-Detailed"));
	if (client.checkPermission("javax.microedition.io.Connector.file.read") > 0) {
	    m_altPath = System.getProperty("fileconn.dir.private");
	    if (m_altPath != null) {
		try {
		    Connection c = Connector.open(m_altPath+"exist.txt",Connector.READ);
		    c.close();
		}
		catch (Exception e) {
		    System.err.println(m_altPath+" "+e.toString());
		    m_altPath = null;
		}
	    }
	    if (m_altPath == null) {
		try {
		    m_altPath = System.getProperty("fileconn.dir.memorycard");
		    if (m_altPath != null) {
			m_altPath = m_altPath + "WoWmobile/";
			Connection c = Connector.open(m_altPath+"exist.txt",Connector.READ);
			c.close();
		    }
		}
		catch (Exception e) {
		    System.err.println(m_altPath+" "+e.toString());
		    m_altPath = null;
		}
	    }
	}
	if (m_altPath == null)
	    m_altPath = client.getAppProperty("WoW-Alternate-Path");
	showDebug("Alternate path: "+m_altPath);
	m_debug = System.getProperty("microedition.platform");
    }

    public void setExtra(WoWcanvas canvas) {
	m_hasPointer = canvas.hasPointerEvents();
    }

    public void setAuth(WoWauth auth) {
	m_auth = auth;
	if (auth != null) {
	    Vector realms = auth.realms();
	    if (realms != null && !realms.isEmpty()) {
		WoWrealm realm = (WoWrealm)realms.firstElement();
		if (realm != null) {
		    m_playerRealm = realm.name();
		    m_conn = new WoWconn(realm.addr());
		    m_conn.setAuth(auth.getAccount(),auth.sessionKey(),auth.buildNo());
		    m_conn.start();
		    auth.disconnect();
		}
	    }
	}
    }

    public void stop() {
	m_paused = true;
	m_visible = false;
	m_silent = true;
	setScreen(null);
	m_width = 0;
	m_height = 0;
	stopSounds(true);
	saveCache();
	if (m_auth != null) {
	    m_auth.disconnect();
	    m_auth = null;
	}
	if (m_conn != null) {
	    m_conn.disconnect();
	    m_conn = null;
	}
    }

    public boolean paused() {
	return m_paused;
    }

    public boolean visible() {
	return m_visible;
    }

    public boolean silent() {
	return m_silent;
    }

    public boolean detailed() {
	return m_detail;
    }

    public boolean debugging() {
	return m_debugging;
    }

    public boolean hasPointer() {
	return m_hasPointer;
    }

    public boolean shifted() {
	return m_shiftKey || m_shiftLck;
    }

    public boolean dragging() {
	return m_dragging;
    }

    public int width() {
	return m_width;
    }

    public int height() {
	return m_height;
    }

    public WoWauth auth() {
	return m_auth;
    }

    public WoWconn conn() {
	return m_conn;
    }

    public WoWarea area() {
	return m_area;
    }

    public WoWplayer player() {
	return m_player;
    }

    public String playerRealm() {
	return m_playerRealm;
    }

    public String playerName() {
	return m_playerName;
    }

    public String playerLevel() {
	return m_playerLevel;
    }

    public Image playerIcon() {
	return m_playerIcon;
    }

    public long playerGuid() {
	return m_playerGuid;
    }

    public long selectGuid() {
	return m_selectGuid;
    }

    public int health() {
	return m_player != null ? m_player.health() : 50;
    }

    public int power() {
	return m_player != null ? m_player.power() : 50;
    }

    public int xp() {
	return m_player != null ? m_player.xp() : 0;
    }

    public int healthMax() {
	return m_player != null ? m_player.healthMax() : 100;
    }

    public int powerMax() {
	return m_player != null ? m_player.powerMax() : 100;
    }

    public int xpNext() {
	return m_player != null ? m_player.xpNext() : 1000;
    }

    public boolean threatened() {
	return m_threatened;
    }

    public String mapArea() {
	return m_mapArea;
    }

    public double mapHeading() {
	return m_mapHeading;
    }

    public double mapLocationX() {
	return m_mapX;
    }

    public double mapLocationY() {
	return m_mapY;
    }

    public double mapLocationZ() {
	return m_mapZ;
    }

    public synchronized String[] logs() {
	return m_logs;
    }

    public synchronized String[] info() {
	return m_info;
    }

    public void setCharacter(WoWchar chr) {
	m_playerGuid = chr.guid();
	m_playerName = chr.name();
	m_playerLevel = chr.level();
	m_mapArea = chr.area();
	if (chr.icon() != null)
	    m_playerIcon = chr.icon();
	m_area = WoWarea.create(m_mapArea);
	if (m_area != null) {
	    m_mapX = (m_area.minX() + m_area.maxX()) / 2;
	    m_mapY = (m_area.minY() + m_area.maxY()) / 2;
	}
    }

    public void setBlip(long guid, double x, double y, double z, int color) {
	WoWblip b = new WoWblip(guid,color);
	b.X = x;
	b.Y = y;
	b.Z = z;
	int old = m_blips.size();
	m_blips.put(b,b);
	if (m_blips.size() > old)
	    showDebug("Number of blips: "+old+" -> "+m_blips.size());
	WoWrender screen = m_screen;
	if (screen instanceof wow.ui.Player)
	    ((wow.ui.Player)screen).setBlip(b);
    }

    public void delBlip(long guid) {
	WoWblip b = new WoWblip(guid,0);
	int old = m_blips.size();
	m_blips.remove(b);
	if (m_blips.size() < old) {
	    showDebug("Number of blips: "+old+" -> "+m_blips.size());
	    WoWrender screen = m_screen;
	    if (screen instanceof wow.ui.Player)
		((wow.ui.Player)screen).delBlip(guid);
	}
    }

    public WoWblip getBlip(long guid) {
	return (WoWblip)m_blips.get(new WoWguid(guid));
    }

    public void loggedOut() {
	m_playerName = null;
	m_playerLevel = null;
	m_playerIcon = null;
	m_playerGuid = 0;
	m_selectGuid = 0;
	m_threatened = false;
	m_following = false;
	m_mapArea = null;
	m_area = null;
	m_player = null;
	m_blips = new Hashtable();
	m_objects = new Hashtable();
	m_mapX = 0.0;
	m_mapY = 0.0;
	m_mapZ = 0.0;
	m_mapHeading = 0.0;
	m_mapTurning = 0.0;
	m_mapSpeed = 0.0;
	try {
	    m_playerIcon = Image.createImage("/images/spells/Frost_Stun.png");
	}
	catch (Exception e) {
	    m_debugging = true;
	    e.printStackTrace();
	    m_debug = e.toString();
	}
    }

    private void loadCache(final String recName, final String className, Hashtable cache) {
	Class cc = null;
	try {
	    cc = Class.forName("wow.cache."+className);
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	if (cc == null) {
	    System.err.println("Failed to find cache class "+className);
	    return;
	}
	byte[] data = WoWmobile.getRecord(recName);
	if (data == null)
	    return;
	ByteArrayInputStream bistr = new ByteArrayInputStream(data);
	DataInputStream istr = new DataInputStream(bistr);
	for (;;) {
	    try {
		if (istr.available() <= 0)
		    break;
		Cacheable co = (Cacheable)cc.newInstance();
		co.read(istr);
		cache.put(co,co);
	    }
	    catch (Exception e) {
		e.printStackTrace();
		break;
	    }
	}
	showDebug("Loaded "+cache.size()+" elements from "+recName+" ("+data.length+" bytes)");
	try {
	    istr.close();
	    bistr.close();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void saveCache(final String recName, Hashtable cache) {
	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	DataOutputStream os = new DataOutputStream(bos);
	Enumeration en = cache.elements();
	while (en.hasMoreElements()) {
	    Cacheable c = (Cacheable)en.nextElement();
	    try {
		c.write(os);
	    }
	    catch (Exception e) {
		e.printStackTrace();
		break;
	    }
	}
	byte[] data = bos.toByteArray();
	WoWmobile.setRecord(recName,data);
	showDebug("Saved "+cache.size()+" elements to "+recName+" ("+data.length+" bytes)");
	try {
	    os.close();
	    bos.close();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void loadCache() {
	loadCache("creaturecache","InfoCreature",m_cacheCreatures);
	loadCache("gameobjectcache","InfoGameObject",m_cacheGameObjects);
	loadCache("itemnamecache","InfoItemName",m_cacheItemNames);
	m_cacheChanged = false;
    }

    private void saveCache() {
	if (m_cacheChanged) {
	    saveCache("creaturecache",m_cacheCreatures);
	    saveCache("gameobjectcache",m_cacheGameObjects);
	    saveCache("itemnamecache",m_cacheItemNames);
	}
	else
	    showDebug("Not saving unmodified cache content");
    }

    public WoWaction getAction(int slot) {
	if (slot >= m_actions.size())
	    return null;
	return (WoWaction)m_actions.elementAt(slot);
    }

    public void setAction(int slot, WoWaction action) {
	m_actions.ensureCapacity(slot+1);
	m_actions.setElementAt(action,slot);
    }

    public void setAction(WoWaction action) {
	m_actions.addElement(action);
    }

    public void setAction(final String action) {
	m_actions.addElement(new WoWaction(action));
    }

    public WoWaction getStorage(int slot) {
	if (slot >= m_storage.size())
	    return null;
	return (WoWaction)m_storage.elementAt(slot);
    }

    public void setStorage(int slot, WoWaction item) {
	m_storage.ensureCapacity(slot+1);
	m_storage.setElementAt(item,slot);
    }

    public void setStorage(WoWaction item) {
	m_storage.addElement(item);
    }

    public void setStorage(String item) {
	m_storage.addElement(new WoWaction(item));
    }

    public void setVolume(int level) {
	if (level == 0)
	    m_silent = true;
	else {
	    m_silent = false;
	    m_fgVolume = m_bgVolume = level;
	}
    }

    public void setBgColor(int color) {
	m_bgColor = color;
    }

    public int getBgColor() {
	return m_bgColor;
    }

    public Enumeration blips() {
	return m_blips.elements();
    }

    public Enumeration objects() {
	return m_objects.elements();
    }

    public WoWobject getObject(WoWguid guid) {
	return (WoWobject)m_objects.get(guid);
    }

    public WoWobject getObject(long guid) {
	return getObject(new WoWguid(guid));
    }

    private void addObject(long guid, int typeId) {
	WoWguid uid = new WoWguid(guid);
	if (m_objects.get(uid) == null) {
	    WoWobject obj = WoWobject.create(guid);
	    m_objects.put(uid,obj);
	    if (guid == m_playerGuid)
		m_player = (WoWplayer)obj;
	}
    }

    private void delObject(WoWguid guid) {
	m_objects.remove(guid);
	if (guid.guid() == m_playerGuid)
	    m_player = null;
    }

    private void delObject(long guid) {
	delObject(new WoWguid(guid));
	if (guid == m_playerGuid)
	    m_player = null;
    }

    public void setSelect(long guid) {
	m_following = false;
	m_selectGuid = guid;
    }

    public InfoCreature getCreature(int id) {
	return (InfoCreature)m_cacheCreatures.get(new Cacheable(id));
    }

    public InfoGameObject getGameObject(int id) {
	return (InfoGameObject)m_cacheGameObjects.get(new Cacheable(id));
    }

    public synchronized void setDebug(String message) {
	if (message == null || message.equals(m_debug))
	    return;
	System.err.println("Debug: "+message);
	m_debug = message;
    }

    public void showDebug(String message) {
	if (message == null)
	    return;
	System.err.println(message);
    }

    public synchronized void setLog(String message) {
	if (message == null || message.equals(m_logs[0]))
	    return;
	System.err.println("Log: "+message);
	pushLog(message);
    }

    private void pushLog(String message) {
	for (int i = m_logs.length-1; i > 0; i--)
	    m_logs[i] = m_logs[i-1];
	m_logs[0] = message;
	m_logUp = 10000 + System.currentTimeMillis();
    }

    public synchronized void setInfo(String message) {
	if (message == null || message.equals(m_info[0]))
	    return;
	System.err.println("Info: "+message);
	pushInfo(message);
    }

    private void pushInfo(String message) {
	for (int i = m_info.length-1; i > 0; i--)
	    m_info[i] = m_info[i-1];
	m_info[0] = message;
	m_infoUp = 2500 + System.currentTimeMillis();
    }

    private void setValue(long guid, short idx, int value) {
	WoWobject obj = getObject(guid);
	if (obj != null)
	    obj.setValue(idx,value);
    }

    private void setPosition(long guid, float[] pos) {
	WoWobject obj = getObject(guid);
	if (obj != null)
	    obj.pos().moveTo(pos);
    }

    public void action(final String name, final String arg) {
	int cmd = 0;
	if (name.equals("rotate")) {
	    m_following = false;
	    if (arg.equals("none")) {
		m_mapTurning = 0.0;
		cmd = WoWpacket.MSG_MOVE_STOP_TURN;
	    }
	    else if (arg.equals("left")) {
		m_mapTurning = -0.2;
		cmd = WoWpacket.MSG_MOVE_START_TURN_LEFT;
	    }
	    else if (arg.equals("right")) {
		m_mapTurning = 0.2;
		cmd = WoWpacket.MSG_MOVE_START_TURN_RIGHT;
	    }
	}
	else if (name.equals("move")) {
	    m_following = false;
	    if (arg.equals("none")) {
		m_mapSpeed = 0.0;
		cmd = WoWpacket.MSG_MOVE_STOP;
	    }
	    else if (arg.equals("forward")) {
		m_mapSpeed = 0.01;
		cmd = WoWpacket.MSG_MOVE_START_FORWARD;
	    }
	    else if (arg.equals("backward")) {
		m_mapSpeed = -0.0075;
		cmd = WoWpacket.MSG_MOVE_START_BACKWARD;
	    }
	}
	else if (name.equals("query")) {
	    if (m_conn != null && m_selectGuid != 0) {
		WoWobject obj = getObject(m_selectGuid);
		if (obj != null) {
		    WoWpacket pkt = null;
		    switch (obj.guidType()) {
			case WoWobject.HIGHGUID_PLAYER:
			    pkt = new WoWpacket(WoWpacket.CMSG_NAME_QUERY);
			    pkt.addLong(obj.guid());
			    break;
			case WoWobject.HIGHGUID_ITEM:
			    pkt = new WoWpacket(WoWpacket.CMSG_ITEM_TEXT_QUERY);
			    pkt.addLong(obj.guid());
			    break;
			case WoWobject.HIGHGUID_UNIT:
			    pkt = new WoWpacket(WoWpacket.CMSG_CREATURE_QUERY);
			    pkt.addInt(obj.entry());
			    pkt.addLong(obj.guid());
			    break;
			case WoWobject.HIGHGUID_GAMEOBJECT:
			    pkt = new WoWpacket(WoWpacket.CMSG_GAMEOBJECT_QUERY);
			    pkt.addInt(obj.entry());
			    pkt.addLong(obj.guid());
			    break;
			case WoWobject.HIGHGUID_CORPSE:
			    if (((WoWcorpse)obj).owner() == m_playerGuid) {
				pkt = new WoWpacket(WoWpacket.CMSG_RECLAIM_CORPSE);
				pkt.addLong(m_playerGuid);
			    }
			    break;
		    }
		    if (pkt != null)
			m_conn.writePacket(pkt);
		}
	    }
	}
	else if (name.equals("follow")) {
	    if (m_following)
		m_following = false;
	    else if (m_selectGuid != 0)
		m_following = true;
	}
	if (cmd != 0)
	    netMovement(cmd);
    }

    public void action(final String name) {
	action(name,null);
    }

    public void action(WoWaction act) {
	if (act == null)
	    return;
	int id = act.id();
	if (id != 0 && m_conn != null) {
	    showDebug("Casting spell "+id);
	    WoWpacket pkt = new WoWpacket(WoWpacket.CMSG_CAST_SPELL);
	    pkt.addByte(0);
	    pkt.addInt(id);
	    pkt.addByte(0);
	    if (m_selectGuid != 0) {
		pkt.addInt(2); // target unit
		pkt.addGuid(m_selectGuid);
		pkt.addGuid(0); // source translation
	    }
	    else
		pkt.addInt(0); // target self
	    m_conn.writePacket(pkt);
	}
	action(act.name());
    }

    private static double atan2approx(double y, double x) {
	double coeff_1 = Math.PI / 4d;
	double coeff_2 = 3d * coeff_1;
	double abs_y = Math.abs(y);
	double angle;
	if (x >= 0d) {
	    double r = (x - abs_y) / (x + abs_y);
	    angle = coeff_1 - coeff_1 * r;
	} else {
	    double r = (x + abs_y) / (abs_y - x);
	    angle = coeff_2 - coeff_1 * r;
	}
	return y < 0d ? -angle : angle;
    }

    private static double adjust(double angle) {
	while (angle < 0.0)
	    angle += 360.0;
	while (angle >= 360.0)
	    angle -= 360.0;
	if (angle < 0.0)
	    angle = 0.0;
	return angle;
    }

    private void update(long elapsed) {
	boolean moved = false;
	if (m_following) {
	    WoWblip b = getBlip(m_selectGuid);
	    if (b != null) {
		if (m_mapZ != b.Z) {
		    moved = true;
		    m_mapZ = b.Z;
		}
		double dx = b.X - m_mapX;
		double dy = b.Y - m_mapY;
		double heading = adjust(Math.toDegrees(atan2approx(-dy,dx)));
		heading -= m_mapHeading;
		if (heading > 180.0)
		    heading -= 360.0;
		if (Math.abs(heading) > 5.0) {
		    double rotate = 0.2 * elapsed;
		    if (heading < 0)
			rotate = -rotate;
		    if (Math.abs(rotate) > Math.abs(heading))
			rotate = heading;
		    moved = true;
		    m_mapHeading = adjust(rotate + m_mapHeading);
		}
		else {
		    double dist = Math.sqrt(dx*dx + dy*dy);
		    if (dist > 5.0) {
			double advance = 0.01 * elapsed;
			if (advance > dist)
			    advance = dist;
			double rad = Math.toRadians(m_mapHeading);
			moved = true;
			m_mapX += advance * Math.cos(rad);
			m_mapY -= advance * Math.sin(rad);
		    }
		}
	    }
	}
	if (m_mapTurning != 0.0) {
	    moved = true;
	    m_mapHeading = adjust((m_mapTurning * elapsed) + m_mapHeading);
	}
	if (m_mapSpeed != 0.0) {
	    moved = true;
	    double rad = Math.toRadians(m_mapHeading);
	    m_mapX += m_mapSpeed * elapsed * Math.cos(rad);
	    m_mapY -= m_mapSpeed * elapsed * Math.sin(rad);
	    if (m_mapX < -16383.0)
		m_mapX = -16383.0;
	    else if (m_mapX > 16383.0)
		m_mapX = 16383.0;
	    if (m_mapY < -16383.0)
		m_mapY = -16383.0;
	    else if (m_mapY > 16383.0)
		m_mapY = 16383.0;
	}
	WoWrender screen = m_screen;
	if (screen != null)
	    screen.update(elapsed);
	int i;
	for (i = 0; i < m_actions.size(); i++) {
	    WoWaction act = (WoWaction)m_actions.elementAt(i);
	    if (act != null)
		act.clearGrayed(1);
	}
	for (i = 0; i < m_storage.size(); i++) {
	    WoWaction act = (WoWaction)m_storage.elementAt(i);
	    if (act != null)
		act.clearGrayed(1);
	}
	if (moved)
	    netMovement(WoWpacket.MSG_MOVE_HEARTBEAT);
    }

    public static String guidStr(long guid) {
	StringBuffer s = new StringBuffer(Long.toString(guid & 0x7fffffffffffffffl,16).toUpperCase());
	if (guid < 0) {
	    switch (s.charAt(0)) {
		case '0':
		    s.setCharAt(0,'8');
		    break;
		case '1':
		    s.setCharAt(0,'9');
		    break;
		case '2':
		    s.setCharAt(0,'A');
		    break;
		case '3':
		    s.setCharAt(0,'B');
		    break;
		case '4':
		    s.setCharAt(0,'C');
		    break;
		case '5':
		    s.setCharAt(0,'D');
		    break;
		case '6':
		    s.setCharAt(0,'E');
		    break;
		case '7':
		    s.setCharAt(0,'F');
		    break;
	    }
	}
	return "["+s+"]";
    }

    public String toGuid(long guid) {
	if (guid != 0) {
	    if (guid == m_playerGuid)
		return "[SELF]";
	    if (guid == m_selectGuid)
		return "[SELECT]";
	}
	return guidStr(guid);
    }

    public String toName(long guid) {
	String s = null;
	WoWobject o = getObject(guid);

	if (o instanceof WoWplayer)
	    s = ((WoWplayer)o).name();
	else if (o instanceof WoWunit) {
	    InfoCreature c = getCreature(((WoWunit)o).entry());
	    if (c != null && c.valid())
		s = c.name();
	}
	else if (o instanceof WoWgobj) {
	    WoWgobj go = (WoWgobj)o;
	    InfoGameObject ig = getGameObject(go.entry());
	    if (ig != null && ig.valid())
		s = ig.name();
	}
	else if (o instanceof WoWcorpse) {
	    WoWcorpse c = (WoWcorpse)o;
	    if (c.owner() != 0)
		s = "Corpse of "+toName(c.owner());
	}

	if (s == null || s.equals(""))
	    s = toGuid(guid);
	return s;
    }

    public int idle() {
	int msec = 100;
	WoWrender screen = m_screen;
	if (screen != null)
	    msec = screen.idle();
	else if (m_initial && m_visible && !m_paused) {
	    m_initial = false;
	    jumpScreen("CharSelect");
	}
	if (m_interact) {
	    m_interact = false;
	    msec = 0;
	}

	if (m_conn != null) {
	    WoWpacket pkt;
	    while ((pkt = m_conn.readPacket()) != null) {
		msec = 0;
		netEvent(pkt);
	    }
	    setDebug(m_conn.getError());
	}

	if (msec < 20)
	    msec = 20;
	else if (msec > 250)
	    msec = 250;

	long t = System.currentTimeMillis();
	if (t >= m_logUp)
	    pushLog(null);
	if (t >= m_infoUp)
	    pushInfo(null);
	if (m_lastUpdate == 0)
	    m_lastUpdate = t;
	else {
	    t -= m_lastUpdate;
	    if (t > 0) {
		m_lastUpdate += t;
		update(t);
	    }
	}
	return msec;
    }

    public void netMovement(int cmd) {
	if (m_conn == null)
	    return;
	WoWmove move = new WoWmove();
	move.guid = m_playerGuid;
	move.time = (int)System.currentTimeMillis();
	move.xyzo = new float[4];
	move.xyzo[0] = (float)m_mapX;
	move.xyzo[1] = (float)m_mapY;
	move.xyzo[2] = (float)m_mapZ;
	move.xyzo[3] = (float)Math.toRadians(-m_mapHeading);
	m_conn.writePacket(move.pack(cmd));
    }

    private void updateCache(WoWobject obj) {
	if (obj == null)
	    return;
	WoWpacket pkt = null;
	switch (obj.guidType()) {
	    case WoWobject.HIGHGUID_PLAYER:
		pkt = new WoWpacket(WoWpacket.CMSG_NAME_QUERY);
		pkt.addLong(obj.guid());
		break;
	    case WoWobject.HIGHGUID_ITEM:
		if (obj.entry() == 0)
		    return;
		synchronized (m_cacheItemNames) {
		    InfoItemName dummy = new InfoItemName(obj.entry());
		    if (m_cacheItemNames.containsKey(dummy))
			return;
		    m_cacheItemNames.put(dummy,dummy);
		    m_cacheChanged = true;
		}
		pkt = new WoWpacket(WoWpacket.CMSG_ITEM_NAME_QUERY);
		pkt.addInt(obj.entry());
		pkt.addLong(obj.guid());
		break;
	    case WoWobject.HIGHGUID_UNIT:
		if (obj.entry() == 0)
		    return;
		synchronized (m_cacheCreatures) {
		    InfoCreature dummy = new InfoCreature(obj.entry());
		    if (m_cacheCreatures.containsKey(dummy))
			return;
		    m_cacheCreatures.put(dummy,dummy);
		    m_cacheChanged = true;
		}
		pkt = new WoWpacket(WoWpacket.CMSG_CREATURE_QUERY);
		pkt.addInt(obj.entry());
		pkt.addLong(obj.guid());
		break;
	    case WoWobject.HIGHGUID_GAMEOBJECT:
		if (obj.entry() == 0)
		    return;
		synchronized (m_cacheGameObjects) {
		    InfoGameObject dummy = new InfoGameObject(obj.entry());
		    if (m_cacheGameObjects.containsKey(dummy))
			return;
		    m_cacheGameObjects.put(dummy,dummy);
		    m_cacheChanged = true;
		}
		pkt = new WoWpacket(WoWpacket.CMSG_GAMEOBJECT_QUERY);
		pkt.addInt(obj.entry());
		pkt.addLong(obj.guid());
		break;
	}
	if (pkt != null)
	    m_conn.writePacket(pkt);
    }

    private void updateCache(long guid) {
	WoWobject obj = getObject(guid);
	if (obj != null)
	    updateCache(obj);
    }

    private boolean netUpdateMovement(WoWpacket pkt, long guid) {
	int flags = pkt.getWord();
	showDebug("Movement flags: "+flags);
	if ((flags & 0x0020) != 0) {
	    // living thing
	    WoWmove move = new WoWmove();
	    move.unpack(pkt,guid);
	    float speeds[] = pkt.getFloats(9);
	    // parse inflight player data here
	    // if () { }
	    if (guid == m_playerGuid)
		showDebug("Created or moved myself!");
	    else if (move.xyzo != null) {
		setPosition(guid,move.xyzo);
		setBlip(guid,(double)move.xyzo[0],(double)move.xyzo[1],(double)move.xyzo[2],0x808080);
	    }
	}
	else {
	    // position
	    if ((flags & 0x0100) != 0) {
		pkt.getGuid();
		float pos[] = pkt.getFloats(8);
		setPosition(guid,pos);
		setBlip(guid,(double)pos[0],(double)pos[1],(double)pos[2],0xff00ff);
	    }
	    else {
		if ((flags & 0x0040) != 0) {
		    // has position
		    float xyzo[] = pkt.getFloats(4);
		    if (xyzo[0] != 0f || xyzo[1] != 0f || xyzo[2] != 0f) {
			setPosition(guid,xyzo);
			setBlip(guid,(double)xyzo[0],(double)xyzo[1],(double)xyzo[2],0xff00ff);
		    }
		}
	    }
	}
	if ((flags & 0x0008) != 0)
	    showDebug("Lowguid value: "+pkt.getInt());
	if ((flags & 0x0010) != 0)
	    showDebug("Highguid value: "+pkt.getInt());
	if ((flags & 0x0004) != 0)
	    showDebug("Target Guid: "+toGuid(pkt.getGuid()));
	if ((flags & 0x0002) != 0)
	    showDebug("Transport time: "+pkt.getInt());
	if ((flags & 0x0080) != 0)
	    showDebug("Vehicle: "+pkt.getInt()+" facing adj. "+pkt.getFloat());
	if ((flags & 0x0200) != 0)
	    showDebug("Rotation: "+pkt.getLong());
	return true;
    }

    private boolean netUpdateValues(WoWpacket pkt, long guid) {
	int bMask = 4 * pkt.getByte();
	byte mask[] = pkt.getBytes(bMask);
	if (mask == null) {
	    showDebug("Apparently "+bMask+" bytes in mask but available "+pkt.avail());
	    return false;
	}
	//System.err.println("There are "+bMask+" bytes in mask");
	String s = "";
	for (int i = 0; i < bMask; i++) {
	    for (int j = 0; j < 8; j++) {
		if ((mask[i] & (1 << j)) == 0)
		    continue;
		int idx = (8 * i) + j;
		int val = pkt.getInt();
		s += " {"+idx+"=>"+val+"}";
		setValue(guid,(short)idx,val);
	    }
	}
	showDebug(toGuid(guid)+" Values:"+s);
	return true;
    }

    private void netUpdate(WoWpacket pkt) {
	int blk = pkt.getInt();
	//System.err.println("Updating "+blk+" blocks, size "+pkt.avail());
	while (blk-- > 0) {
	    int type = pkt.getByte();
	    //System.err.println("Update block type: "+type);
	    switch (type) {
		case 0: // values
		    {
			long guid = pkt.getGuid();
			showDebug("Values of: "+toGuid(guid)+" "+WoWobject.textType(guid));
			if (!netUpdateValues(pkt,guid))
			    break;
			//updateCache(guid);
		    }
		    continue;
		case 1: // movement
		    {
			long guid = pkt.getLong();
			showDebug("Movement of: "+toGuid(guid)+" "+WoWobject.textType(guid));
			if (!netUpdateMovement(pkt,guid))
			    break;
		    }
		    continue;
		case 2: // create object
		case 3: // create self
		    {
			long guid = pkt.getGuid();
			int typeId = pkt.getByte();
			showDebug("Creation of: "+toGuid(guid)+" "+WoWobject.textType(guid)+" type "+typeId);
			addObject(guid,typeId);
			if (!netUpdateMovement(pkt,guid))
			    break;
			if (!netUpdateValues(pkt,guid))
			    break;
			updateCache(guid);
		    }
		    continue;
		case 4: // out of range
		    {
			for (int cnt = pkt.getInt(); cnt > 0; cnt--) {
			    long guid = pkt.getGuid();
			    showDebug("Out of visible range: "+toGuid(guid)+" "+WoWobject.textType(guid));
			    delBlip(guid);
			    if (guid == m_selectGuid)
				setSelect(0);
			}
		    }
		    continue;
		case 5: // near objects
		    break;
	    }
	    showDebug("Update type "+type+" unsupported, skipping "+blk+" blocks!");
	    return;
	}
    }

    public void netEvent(WoWpacket pkt) {
	WoWrender screen = m_screen;
	if ((screen != null) && screen.netEvent(pkt))
	    return;
	long guid = 0;
	WoWwdbc db = null;
	switch (pkt.code()) {
	    case WoWpacket.SMSG_MOTD:
		int lines = pkt.getInt();
		for (int i = 0; i < lines; i++)
		    setLog(pkt.getString());
		break;
	    case WoWpacket.SMSG_SERVER_MESSAGE:
		pkt.getInt();
		setLog(pkt.getString());
		break;
	    case WoWpacket.SMSG_LOGIN_VERIFY_WORLD:
		int mapId = pkt.getInt();
		float pos[] = pkt.getFloats(4);
		showDebug("World map "+mapId+" to: "+
		    pos[0]+" x "+pos[1]+" x "+pos[2]+" x "+pos[3]);
		m_mapX = pos[0];
		m_mapY = pos[1];
		m_mapZ = pos[2];
		m_mapHeading = Math.toDegrees(-pos[3]);
		break;
	    case WoWpacket.SMSG_TIME_SYNC_REQ:
		int count = pkt.getInt();
		//System.err.println("Time Sync Req. "+count);
		WoWpacket resp = new WoWpacket(WoWpacket.CMSG_TIME_SYNC_RESP);
		resp.addInt(count);
		resp.addInt((int)System.currentTimeMillis());
		m_conn.writePacket(resp);
		break;
	    case WoWpacket.SMSG_INITIAL_SPELLS:
		{
		    pkt.getByte();
		    int cnt = pkt.getWord();
		    showDebug("Received "+cnt+" initial spells");
		    for (int i = 0; i < cnt; i++) {
			int id = pkt.getInt();
			pkt.getWord();
		    }
		    // decode cooldowns
		}
		break;
	    case WoWpacket.SMSG_ACTION_BUTTONS:
		{
		    pkt.getByte();
		    int cnt = pkt.avail() / 4;
		    showDebug("Received "+cnt+" action buttons");
		    for (int i = 0; i < cnt; i++) {
			int id = pkt.getInt();
			switch ((id >> 24) & 0xff) {
			    case 0x00: // spell
			    case 0x80: // item
				WoWaction act = getAction(i);
				if (act != null)
				    act.setId(id & 0x00ffffff);
			}
		    }
		}
		break;
	    case WoWpacket.SMSG_SET_PROFICIENCY:
		{
		    int cls = pkt.getByte();
		    int val = pkt.getInt();
		    db = WoWwdbc.cached("resource:/dbc/ItemClass.dbc");
		    if (db != null) {
			int idx = db.getRecord(0,cls);
			String s = db.getString(3,idx);
			if (s != null) {
			    setDebug("Proficiency in "+s+" is "+val);
			    break;
			}
		    }
		    setDebug("Proficiency "+cls+" is "+val);
		}
		break;
	    case WoWpacket.SMSG_UPDATE_WORLD_STATE:
		showDebug("Field "+pkt.getInt()+" value is "+pkt.getInt());
		break;
	    case WoWpacket.SMSG_ITEM_NAME_QUERY_RESPONSE:
		synchronized (m_cacheItemNames) {
		    InfoItemName info = new InfoItemName(pkt);
		    m_cacheItemNames.put(info,info);
		    m_cacheChanged = true;
		}
		break;
	    case WoWpacket.SMSG_ITEM_TEXT_QUERY_RESPONSE:
		if (pkt.getByte() == 0)
		    setDebug("Guid "+toGuid(pkt.getLong())+" is '"+pkt.getString()+"'");
		break;
	    case WoWpacket.SMSG_NAME_QUERY_RESPONSE:
		{
		    guid = pkt.getGuid();
		    WoWplayer p = (WoWplayer)getObject(guid);
		    if (p != null)
			p.unpack(pkt);
		}
		break;
	    case WoWpacket.SMSG_CREATURE_QUERY_RESPONSE:
		synchronized (m_cacheCreatures) {
		    InfoCreature info = new InfoCreature(pkt);
		    m_cacheCreatures.put(info,info);
		    m_cacheChanged = true;
		}
		break;
	    case WoWpacket.SMSG_GAMEOBJECT_QUERY_RESPONSE:
		synchronized (m_cacheGameObjects) {
		    InfoGameObject info = new InfoGameObject(pkt);
		    m_cacheGameObjects.put(info,info);
		    m_cacheChanged = true;
		}
		break;
	    case WoWpacket.SMSG_UPDATE_OBJECT:
		netUpdate(pkt);
		break;
	    case WoWpacket.SMSG_COMPRESSED_UPDATE_OBJECT:
		{
		    int len = pkt.getInt();
		    //System.err.println("Uncompressing "+len+" bytes update from "+pkt.avail());
		    byte decomp[] = new byte[len];
		    ZStream zs = new ZStream();
		    zs.next_in = pkt.data();
		    zs.next_in_index = pkt.pos();
		    zs.next_out = decomp;
		    zs.next_out_index = 0;
		    int err = zs.inflateInit();
		    if (err != JZlib.Z_OK) {
			System.err.println("JZlib inflateInit error: "+err);
			return;
		    }
		    while (zs.total_out < len && zs.total_in < pkt.avail()) {
			zs.avail_in = zs.avail_out = 1;
			err = zs.inflate(JZlib.Z_NO_FLUSH);
			if (err == JZlib.Z_STREAM_END)
			    break;
			if (err != JZlib.Z_OK) {
			    System.err.println("JZlib inflate error: "+err);
			    return;
			}
		    }
		    err = zs.inflateEnd();
		    if (err != JZlib.Z_OK) {
			System.err.println("JZlib inflateEnd error: "+err);
			return;
		    }
		    netUpdate(new WoWpacket(pkt.code(),decomp));
		}
		break;
	    case WoWpacket.SMSG_DESTROY_OBJECT:
		guid = pkt.getLong();
		showDebug("Guid "+toGuid(guid)+" "+WoWobject.textType(guid)+" destroyed "+pkt.getByte());
		delBlip(guid);
		if (guid == m_selectGuid)
		    setSelect(0);
		delObject(guid);
		break;
	    case WoWpacket.MSG_MOVE_START_FORWARD:
	    case WoWpacket.MSG_MOVE_START_BACKWARD:
	    case WoWpacket.MSG_MOVE_STOP:
	    case WoWpacket.MSG_MOVE_START_STRAFE_LEFT:
	    case WoWpacket.MSG_MOVE_START_STRAFE_RIGHT:
	    case WoWpacket.MSG_MOVE_STOP_STRAFE:
	    case WoWpacket.MSG_MOVE_JUMP:
	    case WoWpacket.MSG_MOVE_START_TURN_LEFT:
	    case WoWpacket.MSG_MOVE_START_TURN_RIGHT:
	    case WoWpacket.MSG_MOVE_STOP_TURN:
	    case WoWpacket.MSG_MOVE_FALL_LAND:
	    case WoWpacket.MSG_MOVE_HEARTBEAT:
		WoWmove move = new WoWmove(pkt);
/*
		System.err.println("Move of guid: "+move.guid+" to: "+
		    move.xyzo[0]+" x "+move.xyzo[1]+" x "+move.xyzo[2]+" x "+move.xyzo[3]);
*/
		setPosition(guid,move.xyzo);
		if (move.guid != m_playerGuid)
		    setBlip(move.guid,move.xyzo[0],move.xyzo[1],move.xyzo[2],0x80ff00);
		break;
	    case WoWpacket.SMSG_MONSTER_MOVE:
		guid = pkt.getGuid();
		pkt.getByte();
		float mob[] = pkt.getFloats(3);
		setPosition(guid,mob);
		setBlip(guid,mob[0],mob[1],mob[2],0xff8000);
		break;
	    case WoWpacket.SMSG_AI_REACTION:
		guid = pkt.getGuid();
		showDebug(toName(guid)+" AI reaction "+pkt.getInt());
		break;
	    case WoWpacket.SMSG_CRITERIA_UPDATE:
		showDebug("Criteria "+pkt.getInt()+" counter is "+pkt.getGuid());
		// more to decode
		break;
	    case WoWpacket.SMSG_LOG_XPGAIN:
		guid = pkt.getLong();
		setLog(pkt.getInt()+" XP from "+toName(guid)+" type "+pkt.getByte());
		break;
	    case WoWpacket.SMSG_LEVELUP_INFO:
		if (m_player != null)
		    m_player.levelUp(pkt);
		break;
	    case WoWpacket.SMSG_ACHIEVEMENT_EARNED:
		guid = pkt.getGuid();
		setDebug(toName(guid)+" earned achievement "+pkt.getInt());
		// more to decode
		break;
	    case WoWpacket.SMSG_DUEL_REQUESTED:
		guid = pkt.getLong();
		long caster = pkt.getLong();
		if (caster == m_playerGuid)
		    setLog("Requesting duel!");
		else {
		    // we are the target
		    if (shifted()) {
			WoWpacket ack = null;
			if (m_shiftKey) {
			    setLog("Duel accepted!");
			    setSelect(caster);
			    ack = new WoWpacket(WoWpacket.CMSG_DUEL_ACCEPTED);
			}
			else {
			    setLog("Duel refused!");
			    ack = new WoWpacket(WoWpacket.CMSG_DUEL_CANCELLED);
			}
			ack.addLong(caster);
			m_conn.writePacket(ack);
		    }
		    else
			setLog("Duel requested!");
		}
		break;
	    case WoWpacket.SMSG_DUEL_COUNTDOWN:
		setInfo("Duel countdown: "+pkt.getInt());
		break;
	    case WoWpacket.SMSG_DUEL_COMPLETE:
		setSelect(0);
		if (pkt.getByte() == 0)
		    showDebug("Duel complete!");
		else
		    showDebug("Duel interrupted!");
		break;
	    case WoWpacket.SMSG_DUEL_WINNER:
		if (pkt.getByte() == 0)
		    setLog(pkt.getString()+" has defeated "+pkt.getString()+" in a duel");
		else
		    setLog(pkt.getString()+" chased away "+pkt.getString()+" from a duel");
		break;
	    case WoWpacket.SMSG_POWER_UPDATE:
		guid = pkt.getGuid();
		setDebug(toName(guid)+" power "+pkt.getByte()+" is "+pkt.getInt());
		break;
	    case WoWpacket.SMSG_AURA_UPDATE:
		guid = pkt.getGuid();
		showDebug("Aura "+pkt.getByte()+" is "+pkt.getInt());
		// more to decode here
		break;
	    case WoWpacket.SMSG_SPELL_START:
	    case WoWpacket.SMSG_SPELL_GO:
		guid = pkt.getGuid();
		showDebug("Spell on "+toName(guid)+" by "+toName(pkt.getGuid())+" count "+pkt.getByte()+" id "+pkt.getInt());
		break;
	    case WoWpacket.SMSG_CAST_FAILED:
		pkt.getByte();
		setDebug("Spell "+pkt.getInt()+" failed with reason "+pkt.getByte());
		// more to decode
		break;
	    case WoWpacket.SMSG_ATTACKSWING_NOTINRANGE:
		setInfo("You are too far away!");
		break;
	    case WoWpacket.SMSG_ATTACKSWING_BADFACING:
		setInfo("You are facing the wrong way!");
		break;
	    case WoWpacket.SMSG_ATTACKSWING_DEADTARGET:
		setInfo("Your target is dead!");
		break;
	    case WoWpacket.SMSG_ATTACKSWING_CANT_ATTACK:
		setInfo("Cannot attack that target!");
		break;
	    case WoWpacket.SMSG_CANCEL_COMBAT:
		showDebug("Combat cancelled");
		m_threatened = false;
		break;
	    case WoWpacket.SMSG_ATTACKSTART:
		{
		    guid = pkt.getLong();
		    long victim = pkt.getLong();
		    showDebug(toName(victim)+" is attacked by "+toName(guid));
		    if (victim == m_playerGuid) {
			m_threatened = true;
			if (m_selectGuid == 0)
			    setSelect(guid);
		    }
		}
		break;
	    case WoWpacket.SMSG_ATTACKSTOP:
		{
		    guid = pkt.getGuid();
		    long victim = pkt.getGuid();
		    showDebug(toName(victim)+" no longer attacked by "+toName(guid));
		    // not quite correct...
		    if (victim == m_playerGuid)
			m_threatened = false;
		}
		break;
	    case WoWpacket.SMSG_PARTYKILLLOG:
		{
		    guid = pkt.getLong();
		    long victim = pkt.getLong();
		    setLog(toName(victim)+" was killed by "+toName(guid));
		}
		break;
	    case WoWpacket.SMSG_HIGHEST_THREAT_UPDATE:
	    case WoWpacket.SMSG_THREAT_UPDATE:
		{
		    guid = pkt.getGuid();
		    long hostile = 0;
		    if (pkt.code() == WoWpacket.SMSG_HIGHEST_THREAT_UPDATE)
			hostile = pkt.getGuid();
		    if (guid == m_playerGuid && m_selectGuid == 0)
			setSelect(hostile);
		    int cnt = pkt.getInt();
		    while (cnt-- > 0) {
			long attacker = pkt.getGuid();
			int level = pkt.getInt();
			showDebug("Threat on "+toName(guid)+" from "+toName(attacker)+" is "+level);
			if (guid == m_playerGuid && level > 0) {
			    if (m_selectGuid == 0)
				setSelect(attacker);
			    m_threatened = true;
			}
		    }
		}
		break;
	    case WoWpacket.SMSG_THREAT_REMOVE:
		{
		    guid = pkt.getGuid();
		    long hostile = pkt.getGuid();
		    showDebug("Threat on "+toName(guid)+" by "+toName(hostile)+" removed");
		    // not quite correct...
		    if (guid == m_playerGuid)
			m_threatened = false;
		}
		break;
	    case WoWpacket.SMSG_THREAT_CLEAR:
		guid = pkt.getGuid();
		showDebug("Threat on "+toName(guid)+" cleared");
		if (guid == m_playerGuid)
		    m_threatened = false;
		break;
	    case WoWpacket.SMSG_ENVIRONMENTALDAMAGELOG:
		guid = pkt.getLong();
		showDebug(toName(guid)+" damage type "+pkt.getByte()+" amount "+pkt.getInt()+" absorbed "+pkt.getInt()+" resisted "+pkt.getInt());
		break;
	    case WoWpacket.SMSG_SPELLNONMELEEDAMAGELOG:
		{
		    guid = pkt.getGuid();
		    long attacker = pkt.getGuid();
		    int spell = pkt.getInt();
		    int dmg = pkt.getInt();
		    int over = pkt.getInt();
		    int school = pkt.getByte();
		    showDebug(toName(attacker)+" dealt to "+toName(guid)+" spell "+spell+" "+WoWspell.textSchool(school)+" damage "+dmg+" overkill "+over);
		    // more to decode
		    if (guid == m_playerGuid && dmg > 0)
			fireEffect(WoWspell.textSchool(school));
		}
		break;
	    case WoWpacket.SMSG_ATTACKERSTATEUPDATE:
		{
		    int hitinfo = pkt.getInt();
		    long attacker = pkt.getGuid();
		    guid = pkt.getGuid();
		    int dmg = pkt.getInt();
		    showDebug("Attacker "+toName(attacker)+" dealt to "+toName(guid)+" damage "+dmg+" overkill "+pkt.getInt()+" hit info "+hitinfo);
		    // more to decode
		    if (guid == m_playerGuid && dmg > 0)
			fireEffect("normal");
		}
		break;
	    case WoWpacket.SMSG_DURABILITY_DAMAGE_DEATH:
		setInfo("You are dead. RIP.");
		setLog("You are dead. RIP.");
		setSelect(0);
		m_threatened = false;
		break;
	    case WoWpacket.SMSG_EMOTE:
		{
		    int emo = pkt.getInt();
		    guid = pkt.getLong();
		    db = WoWwdbc.cached("resource:/dbc/Emotes.dbc");
		    if (db != null) {
			int idx = db.getRecord(0,emo);
			String s = db.getString(1,idx);
			if (s != null) {
			    showDebug(s+" from "+toName(guid));
			    break;
			}
		    }
		    showDebug("Emote "+emo+" from "+toName(guid));
		}
		break;
	    case WoWpacket.SMSG_TEXT_EMOTE:
		{
		    guid = pkt.getLong();
		    int emo = pkt.getInt();
		    pkt.getInt();
		    pkt.getInt();
		    String name = pkt.getString();
		    db = WoWwdbc.cached("resource:/dbc/EmotesText.dbc");
		    if (db != null) {
			int idx = db.getRecord(0,emo);
			String s = db.getString(1,idx);
			if (s != null) {
			    setDebug(s+" to '"+name+"' from "+toName(guid));
			    break;
			}
		    }
		    setDebug("TextEmote "+emo+" to '"+name+"' from "+toName(guid));
		}
		break;
	    case WoWpacket.SMSG_MESSAGECHAT:
		{
		    WoWchat c = new WoWchat(pkt);
		    if (c.name() != null)
			setLog(c.name()+": "+c.text());
		}
		break;
	    default:
		String dump = "";
		if (pkt.size() > 0)
		    dump = ": "+WoWconn.dumpHex(pkt.data(),12);
		showDebug(pkt.name()+" Len="+pkt.size()+dump);
	}
    }

    public void sizeEvent(int width, int height) {
	showDebug("WoWgame.sizeEvent() "+width+" x "+height);
	m_width = width;
	m_height = height;
	WoWrender screen = m_screen;
	if (screen != null)
	    screen.sizeEvent(width,height);
    }

    public void paintEvent(Graphics g) {
	int bg = m_bgColor;
	g.setColor(bg);
	g.fillRect(0,0,width(),height());
	g.setColor(0xffffff);
	// fade background color
	if (bg != 0)
	    m_bgColor = (bg >> 1) & 0x7f7f7f;
	WoWrender screen = m_screen;
	boolean done = (screen != null) && screen.paintEvent(g);
	Font df = g.getFont();
	g.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
	long t = System.currentTimeMillis();
	if (m_lastDrawn != 0) {
	    long dt = t - m_lastDrawn;
	    if (dt > 0)
		dt = 10000 / dt;
	    else
		dt = 0;
	    // compute a smoothed FPS*10
	    m_lastFps = (9*m_lastFps + dt)/10;
	    if (m_debugging) {
		String fps = (m_lastFps / 10) + "." + (m_lastFps % 10) + " fps";
		g.setColor(0x00ff00);
		g.drawString(fps,width()/2,height()/2,Graphics.HCENTER|Graphics.BOTTOM);
	    }
	}
	m_lastDrawn = t;
	synchronized (this) {
	    if (m_debugging && (m_debug.length() != 0)) {
		g.setColor(0xff8000);
		g.drawString(m_debug,width()/2,1+height()/2,Graphics.HCENTER|Graphics.TOP);
	    }
	}
	g.setFont(df);
	if (m_shiftLck) {
	    g.setColor(0x00ffff);
	    Font f = g.getFont();
            int h = f.getHeight();
            int w = f.stringWidth("S");
            g.fillRoundRect(width()-6-w,height()-57,w+4,h+2,4,4);
	    g.setColor(0x000000);
	    g.drawString("S",width()-4,height()-56,Graphics.RIGHT|Graphics.TOP);
	}
	if (done)
	    return;
    }

    public void showEvent(boolean visible) {
	System.err.println("WoWgame.showEvent() visible="+visible);
	boolean wakeup = visible && !m_visible;
	m_visible = visible;
	if (m_paused || !m_visible)
	    stopSounds(false);
	else if (wakeup && !m_paused) {
	    m_interact = true;
	    startSounds();
	}
    }

    public void pauseEvent(boolean paused) {
	System.err.println("WoWgame.pauseEvent() paused="+paused);
	boolean wakeup = m_paused && !paused;
	m_paused = paused;
	if (m_paused || !m_visible)
	    stopSounds(false);
	else if (wakeup && m_visible)
	    startSounds();
	if (paused) {
	    WoWwdbc.clear();
	    System.gc();
	}
    }

    public boolean stopEvent() {
	if (m_stopping || m_playerName == null)
	    return true;
	m_stopping = true;
	return !pushScreen("Logout");
    }

    public void keyEvent(int key, int action, boolean pressed, boolean repeated) {
	//showDebug("WoWgame.keyEvent() key="+key+" action="+action+" press="+pressed);
	m_interact = true;
	if (key == -8 || key == 8) {
	    // delete and backspace keys are used as a shift
	    m_shiftKey = pressed;
	    if (pressed && !repeated)
		m_shiftLck = !m_shiftLck;
	    return;
	}
	keyEventFwd(key,action,pressed,repeated);
	m_shiftLck = false;
    }

    private void keyEventFwd(int key, int action, boolean pressed, boolean repeated) {
	if (shifted() && pressed && !repeated) {
	    switch (key) {
		case -6: // left softkey
		    if (m_silent)
			break;
		    m_fgVolume -= 10;
		    if (m_fgVolume < 0)
			m_fgVolume = 0;
		    m_bgVolume -= 10;
		    if (m_bgVolume < 0)
			m_bgVolume = 0;
		    setDebug("Vol: "+m_bgVolume);
		    applyVolume();
		    return;
		case -7: // right softkey
		    if (m_silent)
			break;
		    m_fgVolume += 10;
		    if (m_fgVolume > 100)
			m_fgVolume = 100;
		    m_bgVolume += 10;
		    if (m_bgVolume > 100)
			m_bgVolume = 100;
		    setDebug("Vol: "+m_bgVolume);
		    applyVolume();
		    return;
		case -10: // dial key
		    m_silent = !m_silent;
		    setDebug("Silent: "+m_silent);
		    return;
		case '#':
		    m_debugging = !m_debugging;
		    return;
		case '*':
		    m_detail = !m_detail;
		    setDebug("Detailed: "+m_detail);
		    return;
	    }
	    if (action == Canvas.FIRE && m_selectGuid != 0 && m_conn != null) {
		WoWpacket pkt = new WoWpacket(WoWpacket.CMSG_ATTACKSWING);
		pkt.addLong(m_selectGuid);
		m_conn.writePacket(pkt);
		return;
	    }
	}
	WoWrender screen = m_screen;
	if ((screen != null) && screen.keyEvent(key,action,pressed,repeated))
	    return;
	if (pressed && !repeated)
	    showDebug("key="+key+" act="+action);
    }

    public void ptrEvent(int x, int y, boolean pressed, boolean dragged)
    {
	showDebug("WoWgame.ptrEvent("+x+","+y+") press="+pressed+" drag="+dragged);
	m_interact = true;
	if (!pressed) {
	    dragged = m_dragging;
	    m_dragging = false;
	}
	else if (dragged && !m_dragging) {
	    m_dragStartX = x;
	    m_dragStartY = y;
	    m_dragging = true;
	}
	WoWrender screen = m_screen;
	if ((screen != null) && screen.ptrEvent(x,y,pressed,dragged))
	    return;
    }

    public void setScreen(WoWrender screen) {
	if (screen == m_screen)
	    return;
	if (m_screen != null)
	    m_screen.activate(false);
	m_screen = null;
	if (screen != null) {
	    screen.sizeEvent(m_width,m_height);
	    screen.activate(true);
	}
	if (m_screen == null)
	    m_screen = screen;
    }

    public boolean showScreen(final String name) {
	try {
	    Class sc = Class.forName("wow.ui."+name);
	    if (sc != null) {
		showDebug("Loaded UI class: "+sc.getName());
		setScreen((WoWrender)sc.newInstance());
		return true;
	    }
	}
	catch (Exception e) {
	    m_debugging = true;
	    e.printStackTrace();
	    setDebug(e.toString());
	}
	return false;
    }

    public boolean showScreen(final String name, boolean clearFirst) {
	if (clearFirst)
	    setScreen(null);
	return showScreen(name);
    }

    public boolean pushScreen(final String name) {
	if (m_prevScreen == null)
	    m_prevScreen = m_screen;
	return showScreen(name);
    }

    public boolean popScreen() {
	if (m_prevScreen == null)
	    return false;
	WoWrender scr = m_prevScreen;
	m_prevScreen = null;
	setScreen(scr);
	return true;
    }

    public boolean jumpScreen(final String name) {
	m_prevScreen = null;
	return showScreen(name);
    }

    public void fireEffect(int color, final String sound) {
	if (sound != null)
	    WoWgame.self().fireFgSound(WoWmobile.resource(sound));
	if (color != 0)
	    WoWgame.self().setBgColor(color);
    }

    public void fireEffect(WoWeffect effect) {
	if (effect != null)
	    fireEffect(effect.color(),effect.sound());
    }

    public void fireEffect(final String effect) {
	if (effect != null)
	    fireEffect((WoWeffect)m_effects.get(effect));
    }

    public synchronized void setBgSound(final String locator) {
	if (locator == null)
	    return;
	if (m_bgSound != null)
	    m_bgSound.stop();
	if (m_silent)
	    return;
	m_bgSound = new WoWsound();
	m_bgSound.start(locator,true,m_bgVolume);
    }

    public synchronized void setBgPath(final String path) {
	if (m_silent || (path == null))
	    return;
	if (m_bgSound != null)
	    m_bgSound.stop();
	m_bgSound = new WoWsound();
	m_bgSound.start(WoWmobile.resource(path),m_altPath+path,true,m_bgVolume);
    }

    public synchronized void fireFgSound(final String locator) {
	if (m_silent)
	    return;
	if (m_fgSound != null)
	    m_fgSound.stop();
	if ((locator != null) && (m_fgVolume > 0)) {
	    m_fgSound = new WoWsound();
	    m_fgSound.start(locator,false,m_fgVolume);
	}
	else
	    m_fgSound = null;
    }

    private synchronized void stopSounds(boolean forGood) {
	if (m_bgSound != null) {
	    if (forGood) {
		m_bgSound.stop();
		m_bgSound = null;
	    }
	    else
		m_bgSound.pause();
	}
	if (m_fgSound != null) {
	    m_fgSound.stop();
	    m_fgSound = null;
	}
    }

    private synchronized void startSounds() {
	if (m_silent)
	    return;
	if (m_bgSound != null)
	    m_bgSound.resume();
    }

    private synchronized void applyVolume() {
	if (m_bgSound != null)
	    m_bgSound.volume(m_bgVolume);
    }
}
