// World of Warcraft Mobile
//
// World coordinates: X, Y, Z, Orientation

package wow;

public class WoWcoord {

    private float m_x;
    private float m_y;
    private float m_z;
    private float m_o;

    public WoWcoord() {
	m_x = 0f;
	m_y = 0f;
	m_z = 0f;
	m_o = 0f;
    }

    public WoWcoord(final WoWcoord pos) {
	m_x = pos.x();
	m_y = pos.y();
	m_z = pos.z();
	m_o = pos.o();
    }

    public WoWcoord(float x, float y, float z, float o) {
	m_x = x;
	m_y = y;
	m_z = z;
	m_o = o;
    }

    public WoWcoord(float x, float y, float z) {
	m_x = x;
	m_y = y;
	m_z = z;
	m_o = 0f;
    }

    public void moveTo(final WoWcoord pos) {
	m_x = pos.x();
	m_y = pos.y();
	m_z = pos.z();
	m_o = pos.o();
    }

    public void moveTo(float x, float y, float z, float o) {
	m_x = x;
	m_y = y;
	if (z != Float.NaN)
	    m_z = z;
	if (o != Float.NaN)
	    m_o = o;
    }

    public void moveTo(float x, float y, float z) {
	m_x = x;
	m_y = y;
	if (z != Float.NaN)
	    m_z = z;
    }

    public void moveTo(float x, float y) {
	m_x = x;
	m_y = y;
    }

    public void orient(float o) {
	m_o = o;
    }

    public void moveTo(float[] pos) {
	if (pos == null)
	    return;
	if (pos.length >= 4)
	    moveTo(pos[0],pos[1],pos[2],pos[3]);
	else if (pos.length >= 3)
	    moveTo(pos[0],pos[1],pos[2]);
	else if (pos.length >= 2)
	    moveTo(pos[0],pos[1]);
    }

    public void moveRel(float dx, float dy, float dz, float rot) {
	m_x += dx;
	m_y += dy;
	if (dz != Float.NaN)
	    m_z += dz;
	if (rot != Float.NaN)
	    m_o += rot;
    }

    public void moveRel(float dx, float dy, float dz) {
	m_x += dx;
	m_y += dy;
	if (dz != Float.NaN)
	    m_z += dz;
    }

    public void moveRel(float dx, float dy) {
	m_x += dx;
	m_y += dy;
    }

    public void rotate(float rot) {
	m_o += rot;
    }

    public void moveRel(float[] pos) {
	if (pos == null)
	    return;
	if (pos.length >= 4)
	    moveRel(pos[0],pos[1],pos[2],pos[3]);
	else if (pos.length >= 3)
	    moveRel(pos[0],pos[1],pos[2]);
	else if (pos.length >= 2)
	    moveRel(pos[0],pos[1]);
    }

    float x() {
	return m_x;
    }

    float y() {
	return m_y;
    }

    float z() {
	return m_z;
    }

    float o() {
	return m_o;
    }

}
