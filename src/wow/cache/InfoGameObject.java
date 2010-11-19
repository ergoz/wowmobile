// World of Warcraft Mobile
//
// Game object information and parser

package wow.cache;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import wow.*;

public class InfoGameObject extends Cacheable {

    private int m_type;
    private int m_disp;
    private String m_name;

    public InfoGameObject() {
	super(0);
    }

    public InfoGameObject(int entry) {
	super(entry);
	m_type = 0;
	m_disp = 0;
	m_name = "?";
    }

    public InfoGameObject(WoWpacket pkt) {
	super(0);
	pkt.reset();
	unpack(pkt,pkt.getInt());
    }

    public String name() {
	return m_name;
    }

    public void unpack(WoWpacket pkt, int entry) {
	if ((entry & 0x80000000) != 0) {
	    m_entry = entry & 0x7fffffff;
	    m_valid = false;
	    m_name = "?";
	    WoWgame.self().showDebug("GameObject "+m_entry+" not found");
	    return;
	}
	m_entry = entry;
	m_type = pkt.getInt();
	m_disp = pkt.getInt();
	m_name = pkt.getString();
	// more to decode
	WoWgame.self().showDebug("GameObject "+m_entry+" is "+m_name);
	m_valid = true;
    }

    public void read(DataInputStream stream) throws IOException {
	super.read(stream);
	m_type = stream.readInt();
	m_disp = stream.readInt();
	m_name = stream.readUTF();
	m_valid = stream.readBoolean();
    }

    public void write(DataOutputStream stream) throws IOException {
	super.write(stream);
	stream.writeInt(m_type);
	stream.writeInt(m_disp);
	stream.writeUTF(m_name);
	stream.writeBoolean(m_valid);
    }

}
