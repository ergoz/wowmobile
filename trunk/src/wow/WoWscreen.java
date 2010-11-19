// World of Warcraft Mobile
//
// Screen interface

package wow;

import javax.microedition.lcdui.*;

public abstract class WoWscreen implements WoWrender {

    public abstract void activate(boolean active);

    public abstract void sizeEvent(int width, int height);

    public abstract boolean paintEvent(Graphics g);

    public abstract boolean keyEvent(int key, int action, boolean pressed, boolean repeated);

    public boolean ptrEvent(int x, int y, boolean pressed, boolean dragged) {
	return false;
    }

    public boolean netEvent(WoWpacket pkt) {
	return false;
    }

    public int idle() {
	return 200;
    }

    public void update(long elapsed) {
    }
}
