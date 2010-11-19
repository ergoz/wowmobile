// World of Warcraft Mobile
//
// Generic cacheable object

package wow.cache;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import wow.*;

public class Cacheable {

    protected int m_entry;
    protected boolean m_valid;

    public Cacheable(int entry) {
	m_entry = entry;
	m_valid = false;
    }

    public int entry() {
	return m_entry;
    }

    public boolean valid() {
	return m_valid;
    }

    public int hashCode() {
	return m_entry;
    }

    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (!(obj instanceof Cacheable))
	    return false;
	return entry() == ((Cacheable)obj).entry();
    }

    public void read(DataInputStream stream) throws IOException {
	m_valid = false;
	m_entry = stream.readInt();
    }

    public void write(DataOutputStream stream) throws IOException{
	stream.writeInt(m_entry);
    }

}
