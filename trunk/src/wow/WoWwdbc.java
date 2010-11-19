// World of Warcraft Mobile
//
// WDBC file reader

package wow;

import java.io.InputStream;
import java.io.IOException;
import java.util.Hashtable;
import javax.microedition.io.Connector;

public class WoWwdbc {

    private int m_records;
    private int m_fields;
    private short[] m_mapped;
    private int[][] m_values;
    private byte[] m_strings;
    private static Hashtable s_cache = new Hashtable();

    public WoWwdbc() {
	m_records = 0;
	m_fields = 0;
	m_mapped = null;
	m_values = null;
	m_strings = null;
    }

    private int getInt(InputStream stream) throws IOException {
	byte b[] = new byte[4];
	if (stream.read(b) != 4)
	    throw new IOException("EOF");
	return (b[0] & 0xff) | (b[1] & 0xff) << 8 |
	    (b[2] & 0xff) << 16 | (b[3] & 0xff) << 24;
    }

    private int getWord(InputStream stream) throws IOException {
	byte b[] = new byte[2];
	if (stream.read(b) != 2)
	    throw new IOException("EOF");
	return (b[0] & 0xff) | (b[1] & 0xff) << 8;
    }

    private int getByte(InputStream stream) throws IOException {
	byte b[] = new byte[1];
	if (stream.read(b) != 1)
	    throw new IOException("EOF");
	return b[0] & 0xff;
    }

    public boolean load(InputStream stream) {
	try {
	    boolean compr = false;
	    switch (getInt(stream)) {
		// XDBC
		case 0x43424458:
		    compr = true;
		// WDBC
		case 0x43424457:
		    break;
		default:
		    return false;
	    }
	    int records = getInt(stream);
	    int fields = getInt(stream);
	    int recsize = getInt(stream);
	    int sbs = getInt(stream);
	    short[] mapped = null;
	    int values[][] = null;
	    if (compr) {
		byte[] map = new byte[fields];
		if (stream.read(map) != fields)
		    return false;
		mapped = new short[fields];
		int stored = 0;
		for (int f = 0; f < fields; f++)
		    mapped[f] = (short)((map[f] == 0) ? 0xffff : stored++);
		values = new int[records][stored];
		for (int r = 0; r < records; r++) {
		    stored = 0;
		    int flds[] = values[r];
		    for (int f = 0; f < fields; f++) {
			switch (map[f]) {
			    case 0:
				break;
			    case 1:
				flds[stored++] = getByte(stream);
				break;
			    case 2:
				flds[stored++] = getWord(stream);
				break;
			    case 4:
				flds[stored++] = getInt(stream);
				break;
			    default:
				return false;
			}
		    }
		}
	    }
	    else {
		values = new int[records][fields];
		for (int r = 0; r < records; r++) {
		    int flds[] = values[r];
		    for (int f = 0; f < fields; f++)
			flds[f] = getInt(stream);
		}
	    }
	    byte[] strings = new byte[sbs];
	    stream.read(strings);
	    m_records = records;
	    m_fields = fields;
	    m_mapped = mapped;
	    m_values = values;
	    m_strings = strings;
	    return true;
	}
	catch (IOException e) {
	}
	return false;
    }

    public boolean load(final String locator) {
	boolean ok = false;
	try {
	    InputStream str = null;
	    if (locator.startsWith("resource:"))
		str = getClass().getResourceAsStream(locator.substring(9));
	    else
		str = Connector.openInputStream(locator);
	    ok = load(str);
	    str.close();
	}
	catch (IOException e) {
	}
	return ok;
    }

    public static WoWwdbc cached(final String locator) {
	synchronized (s_cache) {
	    WoWwdbc db = (WoWwdbc)s_cache.get(locator);
	    if (db == null) {
		db = new WoWwdbc();
		if (db.load(locator))
		    s_cache.put(locator,db);
		else
		    db = null;
	    }
	    return db;
	}
    }

    public static void clear() {
	synchronized (s_cache) {
	    s_cache.clear();
	}
    }

    public static void clear(final String locator) {
	synchronized (s_cache) {
	    s_cache.remove(locator);
	}
    }

    public int records() {
	return m_records;
    }

    public int fields() {
	return m_fields;
    }

    public int getValue(int field, int record) {
	if (m_mapped != null) {
	    if (field < 0 || field >= m_mapped.length)
		return 0;
	    field = m_mapped[field] & 0xffff;
	    if (field == 0xffff)
		return 0;
	}
	if (m_values == null || record < 0 || record >= m_values.length)
	    return 0;
	int fields[] = m_values[record];
	if (fields == null || field < 0 || field >= fields.length)
	    return 0;
	return fields[field];
    }

    public int getRecord(int field, int value) {
	if (m_values == null || field < 0 || field >= m_fields)
	    return -1;
	if (m_mapped != null) {
	    if (field >= m_mapped.length)
		return -1;
	    field = m_mapped[field] & 0xffff;
	    if (field == 0xffff)
		return (value == 0 && m_values.length > 0) ? 0 : -1;
	}
	for (int r = 0; r < m_values.length; r++) {
	    int fields[] = m_values[r];
	    if (fields == null || field >= fields.length)
		continue;
	    if (fields[field] == value)
		return r;
	}
	return -1;
    }

    public boolean hasString(int index) {
	if (m_strings == null || index < 1 || index >= m_strings.length)
	    return false;
	return m_strings[index-1] == 0;
    }

    public String getString(int index) {
	if (!hasString(index))
	    return null;
	int pos = index;
	while (m_strings[pos] != 0)
	    pos++;
	return new String(m_strings,index,pos-index);
    }

    public String getString(int field, int record) {
	return getString(getValue(field,record));
    }

    public float getFloat(int field, int record) {
	int val = getValue(field,record);
	return (val == -1) ? Float.NaN : Float.intBitsToFloat(val);
    }
}
