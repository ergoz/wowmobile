// World of Warcraft Mobile
//
// Map Area

package wow;

public class WoWarea {

    private int m_index;
    private int m_mapId;
    private int m_vmapId;
    private int m_areaId;
    private String m_name;
    private float m_limits[];

    public WoWarea() {
	m_index = -1;
	loadInfo();
    }

    public WoWarea(float x, float y, int mapId) {
	m_index = getRecord(x,y,mapId,-1);
	loadInfo();
    }

    public WoWarea(String area) {
	m_index = getRecord(area);
	loadInfo();
    }

    private WoWarea(int idx) {
	m_index = idx;
	loadInfo();
    }

    public String name() {
	return m_name;
    }

    public int mapId() {
	return m_mapId;
    }

    public int vmapId() {
	return (m_vmapId >= 0) ? m_vmapId : m_mapId;
    }

    public int areaId() {
	return m_areaId;
    }

    public float minX() {
	return (m_limits != null) ? m_limits[3] : Float.NaN;
    }

    public float maxX() {
	return (m_limits != null) ? m_limits[2] : Float.NaN;
    }

    public float minY() {
	return (m_limits != null) ? m_limits[1] : Float.NaN;
    }

    public float maxY() {
	return (m_limits != null) ? m_limits[0] : Float.NaN;
    }

    public static WoWarea create(float x, float y, int mapId) {
	int idx = getRecord(x,y,mapId,-1);
	if (idx < 0)
	    return null;
	return new WoWarea(idx);
    }

    public static WoWarea create(String area) {
	int idx = getRecord(area);
	if (idx < 0)
	    return null;
	return new WoWarea(idx);
    }

    public boolean update(float x, float y, int mapId) {
	int idx = getRecord(x,y,mapId,m_index);
	if (idx == m_index)
	    return false;
	m_index = idx;
	return loadInfo();
    }

    public boolean update(float x, float y) {
	return update(x,y,m_mapId);
    }

    private static int getRecord(float x, float y, int mapId, int hint) {
	WoWwdbc wma = WoWwdbc.cached("resource:/dbc/WorldMapArea.dbc");
	if (wma == null)
	    return -1;
	if (hint >= 0 && isInArea(x,y,hint,wma))
	    return hint;
	int bestIdx = -1;
	int bestId = -1;
	for (int n = 0; n < wma.records(); n++) {
	    if (wma.getValue(1,n) != mapId)
		continue;
	    int id = wma.getValue(2,n);
	    if (id < bestId)
		continue;
	    if (isInArea(x,y,n,wma)) {
		bestId = id;
		bestIdx = n;
	    }
	}
	return bestIdx;
    }

    private static int getRecord(String area) {
	if (area == null || area.equals(""))
	    return -1;
	WoWwdbc wma = WoWwdbc.cached("resource:/dbc/WorldMapArea.dbc");
	if (wma == null)
	    return -1;
	for (int n = 0; n < wma.records(); n++) {
	    if (area.equals(wma.getString(3,n)))
		return n;
	}
	return -1;
    }

    private static boolean isInArea(float x, float y, int idx, WoWwdbc wma) {
	return x >= wma.getFloat(7,idx) && x <= wma.getFloat(6,idx) &&
	    y >= wma.getFloat(5,idx) && y <= wma.getFloat(4,idx);
    }

    private boolean loadInfo() {
	m_name = null;
	m_mapId = -1;
	m_vmapId = -1;
	m_areaId = -1;
	m_limits = null;
	if (m_index < 0)
	    return false;
	WoWwdbc wma = WoWwdbc.cached("resource:/dbc/WorldMapArea.dbc");
	if (wma == null)
	    return false;
	m_mapId = wma.getValue(1,m_index);
	m_areaId = wma.getValue(2,m_index);
	m_name = wma.getString(3,m_index);
	m_vmapId = wma.getValue(8,m_index);
	m_limits = new float[4];
	for (int i = 0; i < 4; i++)
	    m_limits[i] = wma.getFloat(i+4,m_index);
	return true;
    }
}
