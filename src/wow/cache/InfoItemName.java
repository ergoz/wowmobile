// World of Warcraft Mobile
//
// Item name information and parser

package wow.cache;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import wow.*;

public class InfoItemName extends Cacheable {

    private int m_type;
    private String m_name;

    public InfoItemName() {
	super(0);
    }

    public InfoItemName(int entry) {
	super(entry);
	m_type = 0;
	m_name = "?";
    }

    public InfoItemName(WoWpacket pkt) {
	super(0);
	pkt.reset();
	unpack(pkt,pkt.getInt());
    }

    public String name() {
	return m_name;
    }

    public void unpack(WoWpacket pkt, int entry) {
	m_entry = entry;
	m_name = pkt.getString();
	m_type = pkt.getInt();
	WoWgame.self().showDebug("ItemName "+m_entry+" is "+m_name+" type "+m_type);
	m_valid = true;
    }

    public void read(DataInputStream stream) throws IOException {
	super.read(stream);
	m_name = stream.readUTF();
	m_type = stream.readInt();
	m_valid = stream.readBoolean();
    }

    public void write(DataOutputStream stream) throws IOException {
	super.write(stream);
	stream.writeUTF(m_name);
	stream.writeInt(m_type);
	stream.writeBoolean(m_valid);
    }

}
