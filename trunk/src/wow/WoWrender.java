// World of Warcraft Mobile
//
// Screen render interface

package wow;

import javax.microedition.lcdui.*;

public interface WoWrender {

    public void activate(boolean active);

    public void sizeEvent(int width, int height);

    public boolean paintEvent(Graphics g);

    public boolean keyEvent(int key, int action, boolean pressed, boolean repeated);

    public boolean ptrEvent(int x, int y, boolean pressed, boolean dragged);

    public boolean netEvent(WoWpacket pkt);

    public int idle();

    public void update(long elapsed);
}
