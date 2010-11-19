// World of Warcraft Mobile
//
// MIDlet implementation

package wow;

import java.io.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.*;

public class WoWmobile extends MIDlet implements CommandListener {

    private Display m_display;
    private Form m_form;
    private Gauge m_volume;
    private TextField m_user;
    private TextField m_pass;
    private TextField m_serv;
    private WoWcanvas m_canvas;
    private Thread m_async;
    private Command m_exit;
    private Command m_start;
    private Command m_stop;
    private static String s_prefix = "resource:";

    public WoWmobile() {
	m_display = null;
	m_canvas = null;
	m_async = null;
    }

    public static String resource(String name) {
	return s_prefix + name;
    }

    private boolean supports(final String className) {
	try {
	    Class.forName(className);
	    return true;
	}
	catch (Exception e) {
	}
	return false;
    }

    public static byte[] getRecord(final String store) {
	System.err.println("WoWmobile.getRecord() "+store);
	byte[] data = null;
	try {
	    RecordStore rec = RecordStore.openRecordStore(store,false);
	    try {
		data = rec.enumerateRecords(null,null,false).nextRecord();
	    }
	    catch (Exception e) {
		e.printStackTrace();
	    }
	    rec.closeRecordStore();
	}
	catch (RecordStoreNotFoundException e) {
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return data;
    }

    public static boolean setRecord(final String store, final byte[] data) {
	System.err.println("WoWmobile.setRecord() "+store);
	try {
	    RecordStore rec = RecordStore.openRecordStore(store,true);
	    try {
		RecordEnumeration re = rec.enumerateRecords(null,null,false);
		while (re.hasNextElement())
		    rec.deleteRecord(re.nextRecordId());
		rec.addRecord(data,0,data.length);
		rec.closeRecordStore();
		return true;
	    }
	    catch (Exception e) {
		e.printStackTrace();
	    }
	    rec.closeRecordStore();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return false;
    }

    private void ingame(WoWauth auth) {
	System.err.println("WoWmobile.ingame()");
	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	DataOutputStream os = new DataOutputStream(bos);
	try {
	    os.writeUTF(m_user.getString());
	    os.writeUTF(m_pass.getString());
	    os.writeUTF(m_serv.getString());
	    os.writeInt(m_volume.getValue());
	    setRecord("login",bos.toByteArray());
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	try {
	    os.close();
	    bos.close();
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	if (m_canvas == null)
	    m_canvas = new WoWcanvas();
	WoWgame.self().setExtra(this);
	WoWgame.self().setVolume(m_volume.getValue()*10);
	WoWgame.self().setAuth(auth);
	m_canvas.setFullScreenMode(true);
	m_canvas.start();
	m_display.setCurrent(m_canvas);
    }

    private void logfail(final String error) {
	System.err.println("WoWmobile.logfail() "+error);
	Alert alert = new Alert("Login Error",error,null,AlertType.ERROR);
	alert.setTimeout(Alert.FOREVER);
	m_display.setCurrent(alert,m_form);
    }

    private boolean login(final String user, final String pass, final String serv) {
	System.err.println("WoWmobile.login() "+user);
	if (user == null || pass == null || serv == null || user.equals("") || pass.equals("") || serv.equals(""))
	    return false;
	String tmp = serv;
	if (tmp.indexOf(':') < 0)
	    tmp += ":3724";
	final String addr = tmp;
	Alert alert = new Alert("Logging in "+user,"Connecting to "+addr,null,AlertType.INFO);
	alert.setTimeout(Alert.FOREVER);
	alert.setIndicator(new Gauge(null,false,Gauge.INDEFINITE,Gauge.CONTINUOUS_RUNNING));
	alert.addCommand(m_stop);
	alert.setCommandListener(this);
	m_display.setCurrent(alert);
	Thread login = new Thread() {
	    public void run() {
		m_async = this;
		WoWauth auth = new WoWauth(addr,(byte)3,(byte)3,(byte)5,12340);
		boolean ok = auth.doLogin(user,pass);
		System.err.println("Conn: "+auth.isConnected()+", Auth: "+ok+", error: "+auth.getError());
		if (ok)
		    ok = auth.doRealms();
		if (m_async == this)
		    m_async = null;
		if (ok) {
		    auth.disconnect();
		    ingame(auth);
		}
		else {
		    logfail(auth.getError());
		    auth.cleanup();
		}
	    }
	};
	login.start();
	return true;
    }

    public void destroyApp(boolean unconditional) throws MIDletStateChangeException {
	System.err.println("WoWmobile.destroyApp() "+unconditional);
	System.err.flush();
	if (m_canvas == null)
	    return;
	if (WoWgame.stop(unconditional))
	    m_canvas.stop();
	else
	    throw new MIDletStateChangeException("Logging out");
    }

    public void pauseApp() {
	WoWgame.self().pauseEvent(true);
    }

    public void startApp() {
	System.err.println("WoWmobile.startApp()");

	if (m_display == null) {
	    String user = "";
	    String pass = "";
	    String serv = getAppProperty("WoW-AuthServer");
	    if (serv == null)
		serv = "";
	    String defServ = serv;
	    int vol = 5;
	    byte[] data = getRecord("login");
	    if (data != null) {
		ByteArrayInputStream bistr = new ByteArrayInputStream(data);
		DataInputStream istr = new DataInputStream(bistr);
		try {
		    user = istr.readUTF();
		    pass = istr.readUTF();
		    serv = istr.readUTF();
		    vol = istr.readInt();
		}
		catch (Exception e) {
		    e.printStackTrace();
		}
		try {
		    istr.close();
		    bistr.close();
		}
		catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	    if (serv == null || serv.equals(""))
		serv = defServ;
	    m_user = new TextField("Blizzard Login",user,50,TextField.EMAILADDR);
	    m_pass = new TextField("Password",pass,50,TextField.PASSWORD);
	    m_serv = new TextField("Server",serv,50,TextField.ANY);
	    m_volume = new Gauge("Volume",true,10,vol);
	    m_form = new Form(null, new Item[] { m_user, m_pass, m_serv, m_volume });
	    m_exit = new Command("Exit", Command.EXIT, 1);
	    m_start = new Command("Start", Command.OK, 1);
	    m_stop = new Command("Stop", Command.STOP, 1);
	    m_form.addCommand(m_exit);
	    m_form.addCommand(m_start);
	    m_form.setCommandListener(this);

	    m_display = Display.getDisplay(this);
	    m_display.setCurrent(m_form);
	}
	else
	    WoWgame.self().pauseEvent(false);
    }

    public void commandAction(Command cmd, Displayable disp) {
	System.err.println("WoWmobile.commandAction()");

	if (cmd == m_exit) {
	    m_display.setCurrent(null);
	    try {
		destroyApp(false);
		notifyDestroyed();
	    }
	    catch (MIDletStateChangeException e) {
	    }
	}
	else if (cmd == m_start) {
	    if (!login(m_user.getString(),m_pass.getString(),m_serv.getString()))
		ingame(null);
	}
	else if (cmd == m_stop) {
	    Thread tmp = m_async;
	    if (tmp != null) {
		tmp.interrupt();
		m_async = null;
		m_display.setCurrent(m_form);
	    }
	}
    }
}
