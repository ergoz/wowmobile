// World of Warcraft Mobile
//
// Creature information and parser

package wow.cache;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import wow.*;

public class InfoCreature extends Cacheable {

    private int m_flags;
    private int m_type;
    private String m_name;
    private String m_description;
    private String m_iconName;
    private String m_typeName;

    public InfoCreature() {
	super(0);
    }

    public InfoCreature(int entry) {
	super(entry);
	m_flags = 0;
	m_type = 0;
	m_name = "?";
	m_description = null;
	m_iconName = null;
	m_typeName = null;
    }

    public InfoCreature(WoWpacket pkt) {
	super(0);
	pkt.reset();
	unpack(pkt,pkt.getInt());
    }

    public String name() {
	return m_name;
    }

    public String typeName() {
	return m_typeName;
    }

    public String description() {
	return m_description;
    }

    public void unpack(WoWpacket pkt, int entry) {
	if ((entry & 0x80000000) != 0) {
	    m_entry = entry & 0x7fffffff;
	    m_valid = false;
	    m_name = "?";
	    WoWgame.self().showDebug("Creature "+m_entry+" not found");
	    return;
	}
	m_entry = entry;
	m_name = pkt.getString();
	pkt.getString();
	pkt.getString();
	pkt.getString();
	m_description = pkt.getString();
	m_iconName = pkt.getString();
	m_flags = pkt.getInt();
	m_type = pkt.getInt();
	// more to decode
	WoWwdbc db = WoWwdbc.cached("resource:/dbc/CreatureType.dbc");
	if (db != null) {
	    int idx = db.getRecord(0,m_type);
	    m_typeName = db.getString(1,idx);
	}
	WoWgame.self().showDebug("Creature "+m_entry+" is "+m_name+(m_description.length() == 0 ? "" : " : ")+m_description);
	m_valid = true;
    }

    public void read(DataInputStream stream) throws IOException {
	super.read(stream);
	m_flags = stream.readInt();
	m_type = stream.readInt();
	m_name = stream.readUTF();
	m_description = stream.readUTF();
	m_iconName = stream.readUTF();
	m_typeName = null;
	WoWwdbc db = WoWwdbc.cached("resource:/dbc/CreatureType.dbc");
	if (db != null) {
	    int idx = db.getRecord(0,m_type);
	    m_typeName = db.getString(1,idx);
	}
	m_valid = stream.readBoolean();
    }

    public void write(DataOutputStream stream) throws IOException {
	super.write(stream);
	stream.writeInt(m_flags);
	stream.writeInt(m_type);
	stream.writeUTF(m_name);
	stream.writeUTF(m_description);
	stream.writeUTF(m_iconName);
	stream.writeBoolean(m_valid);
    }

}
