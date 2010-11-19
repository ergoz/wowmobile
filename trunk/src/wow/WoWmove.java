// World of Warcraft Mobile
//
// Movement object

package wow;

public class WoWmove {

    public static final int FORWARD                 = 0x00000001;
    public static final int BACKWARD                = 0x00000002;
    public static final int STRAFE_LEFT             = 0x00000004;
    public static final int STRAFE_RIGHT            = 0x00000008;
    public static final int LEFT                    = 0x00000010;
    public static final int RIGHT                   = 0x00000020;
    public static final int PITCH_UP                = 0x00000040;
    public static final int PITCH_DOWN              = 0x00000080;
    public static final int WALKING                 = 0x00000100;
    public static final int ONTRANSPORT             = 0x00000200;
    public static final int LEVITATING              = 0x00000400;
    public static final int ROOT                    = 0x00000800;
    public static final int JUMPING                 = 0x00001000;
    public static final int FALLING                 = 0x00002000;
    public static final int PENDING_STOP            = 0x00004000;
    public static final int PENDING_STRAFE_STOP     = 0x00008000;
    public static final int PENDING_FORWARD         = 0x00010000;
    public static final int PENDING_BACKWARD        = 0x00020000;
    public static final int PENDING_STRAFE_LEFT     = 0x00040000;
    public static final int PENDING_STRAFE_RIGHT    = 0x00080000;
    public static final int PENDING_ROOT            = 0x00100000;
    public static final int SWIMMING                = 0x00200000;
    public static final int ASCENDING               = 0x00400000;
    public static final int DESCENDING              = 0x00800000;
    public static final int CAN_FLY                 = 0x01000000;
    public static final int FLYING                  = 0x02000000;
    public static final int SPLINE_ELEVATION        = 0x04000000;
    public static final int SPLINE_ENABLED          = 0x08000000;
    public static final int WATERWALKING            = 0x10000000;
    public static final int FALLING_SLOW            = 0x20000000;
    public static final int HOVER                   = 0x40000000;

    public static final short FULL_SPEED_TURNING        = 0x0008;
    public static final short FULL_SPEED_PITCHING       = 0x0010;
    public static final short ALWAYS_ALLOW_PITCHING     = 0x0020;
    public static final short INTERPOLATED_MOVEMENT     = 0x0400;
    public static final short INTERPOLATED_TURNING      = 0x0800;
    public static final short INTERPOLATED_PITCHING     = 0x1000;

    public long guid;
    public int flags;
    public short flags2;
    public int time;
    public float xyzo[];
    public long guidTransport;
    public float xyzoTransport[];
    public int timeTransport;
    public int seatTransport;
    public int time2Transport;
    public float pitch;
    public int timeFall;
    public float[] jumping;
    public float splineElevation;

    public WoWmove() {
	guid = 0;
	flags = 0;
	flags2 = 0;
	time = 0;
	xyzo = null;
	guidTransport = 0;
	xyzoTransport = null;
	timeTransport = 0;
	seatTransport = 0;
	time2Transport = 0;
	pitch = (float)0.0;
	timeFall = 0;
	jumping = null;
	splineElevation = (float)0.0;
    }

    public WoWmove(WoWpacket pkt) {
	pkt.reset();
	unpack(pkt,pkt.getGuid());
    }

    public void unpack(WoWpacket pkt, long uid) {
	guid = uid;
	flags = pkt.getInt();
	flags2 = (short)pkt.getWord();
	time = pkt.getInt();
	xyzo = pkt.getFloats(4);
	if ((flags & ONTRANSPORT) != 0) {
	    guidTransport = pkt.getGuid();
	    xyzoTransport = pkt.getFloats(4);
	    timeTransport = pkt.getInt();
	    seatTransport = pkt.getByte();
	    time2Transport = ((flags2 & INTERPOLATED_MOVEMENT) != 0) ? pkt.getInt() : 0;
	}
	else {
	    guidTransport = 0;
	    xyzoTransport = null;
	    timeTransport = 0;
	    seatTransport = 0;
	    time2Transport = 0;
	}
	pitch = ((flags & (SWIMMING|FLYING)) != 0 ||
	    (flags2 & ALWAYS_ALLOW_PITCHING) != 0) ? pkt.getFloat() : (float)0.0;
	timeFall = pkt.getInt();
	jumping = ((flags & JUMPING) != 0) ? pkt.getFloats(4) : null;
	splineElevation = ((flags & SPLINE_ELEVATION) != 0) ? pkt.getFloat() : (float)0.0;
    }

    public WoWpacket pack(int cmd) {
	WoWpacket pkt = new WoWpacket(cmd);
	pkt.addGuid(guid);
	pkt.addInt(flags);
	pkt.addWord(flags2);
	pkt.addInt(time);
	pkt.addFloats(xyzo);
	if ((flags & ONTRANSPORT) != 0) {
	    pkt.addGuid(guidTransport);
	    pkt.addFloats(xyzoTransport);
	    pkt.addInt(timeTransport);
	    pkt.addByte(seatTransport);
	    if ((flags2 & INTERPOLATED_MOVEMENT) != 0)
		pkt.addInt(time2Transport);
	}
	pkt.addInt(timeFall);
	if ((flags & JUMPING) != 0)
	    pkt.addFloats(jumping);
	if ((flags & SPLINE_ELEVATION) != 0)
	    pkt.addFloat(splineElevation);
	return pkt;
    }
}
