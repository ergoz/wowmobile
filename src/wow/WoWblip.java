// World of Warcraft Mobile
//
// A blip on a (mini)map

package wow;

public class WoWblip extends WoWguid {

    public double X;
    public double Y;
    public double Z;
    public int color;

    public WoWblip(long uid, int clr) {
	super(uid);
	X = 0;
	Y = 0;
	Z = 0;
	color = clr;
    }

}
