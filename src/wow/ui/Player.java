// World of Warcraft Mobile
//
// Main player screen UI

package wow.ui;

import java.util.Enumeration;
import java.io.IOException;
import javax.microedition.lcdui.*;
import javax.microedition.m3g.*;
import wow.*;
import wow.cache.*;

public class Player extends WoWscreen {

    private WoWbutton m_buttons[];
    private int m_width;
    private int m_buttonY;
    private int m_buttonCount;
    private int m_cursorX;
    private int m_cursorY;
    private long m_cursorShow;
    private int m_x;
    private int m_y;
    private int m_zoom;

    private Graphics3D m_g3d;
    private World m_wld;
    private Group m_ply;
    private int m_time;
    private float m_rotate;
    private float m_orient;

    public Player() {
	m_width = 0;
	m_buttons = null;
	m_buttonCount = 0;
	m_x = 0;
	m_y = 0;
	m_zoom = 4;
	m_g3d = null;
	m_wld = null;
	m_ply = null;
	m_cursorShow = 0;
	m_time = 0;
	m_rotate = 0f;
	m_orient = 0f;
    }

    private void drawButton(Graphics g, int x, int y, WoWbutton action) {
	if (WoWgame.self().detailed()) {
	    g.setColor(0x404040);
	    g.fillRoundRect(x-17,y-17,34,34,8,8);
	}
	boolean active = true;
	if (action.image() != null)
	    g.drawImage(action.image(),x,y,Graphics.HCENTER|Graphics.VCENTER);
	if (action.grayed() > 0) {
	    int angle = (action.grayed() * 360) / 100;
	    if (angle < 5)
		angle = 5;
	    g.setColor(0x800000);
	    g.fillArc(x-14,y-14,28,28,90,angle);
	    g.setColor(0xff0000);
	    g.drawArc(x-14,y-14,28,28,90,angle);
	    active = false;
	}
	else if (!action.enabled()) {
	    g.setColor(0xff0000);
	    g.drawLine(x-14,y-14,x+14,y+14);
	    g.drawLine(x-14,y+14,x+14,y-14);
	    active = false;
	}
	if (active && (action.label() != null) && (action.label().length() != 0)) {
	    if (WoWgame.self().detailed()) {
		Font f = g.getFont();
		int h = f.getHeight();
		g.setColor(0x202020);
		g.fillRoundRect(x-15,y+14-h,f.stringWidth(action.label())+1,h,4,4);
	    }
	    g.setColor(WoWgame.self().shifted() ? 0x0080ff : 0x00ff00);
	    g.drawString(action.label(),x-15,y+14,Graphics.LEFT|Graphics.BOTTOM);
	}
    }

        private Material materialInitialization(int diffuseColor, 
                int specularColor, float shininess) {
            Material material = new Material();
            try{
                //set color for Material object. if 
                material.setColor(Material.DIFFUSE, diffuseColor); 
                material.setColor(Material.SPECULAR, specularColor);
                material.setShininess(shininess);
            }catch(Exception e) { 
                //TODO: write handler code
            }
            return material;
        }

        private Mesh cubeMeshInitialization(Material material) {
            // initialize some arrays for our object (cube) 
            // Each line in this array declaration represents a triangle 
            // strip for one side of a cube. 
            // 1 * * * * * 0 
            //   * *     * 
            //   *   *   * 
            //   *     * * 
            // 3 * * * * * 2 
            // The ascii diagram above represents the vertices in the first line 
            // (the first tri-strip) 
            short[] vert = { 
                10, 10, 10, -10, 10, 10, 10,-10, 10, -10,-10, 10, // front 
                -10, 10,-10, 10, 10,-10, -10,-10,-10, 10,-10,-10, // back 
                -10, 10, 10, -10, 10,-10, -10,-10, 10, -10,-10,-10, // left 
                10, 10,-10, 10, 10, 10, 10,-10,-10, 10,-10, 10, // right 
                10, 10,-10, -10, 10,-10, 10, 10, 10, -10, 10, 10, // top 
                10,-10, 10, -10,-10, 10, 10,-10,-10, -10,-10,-10 }; // bottom 
            // create a VertexArray to hold the vertices for the object 
            VertexArray vertArray = new VertexArray(vert.length / 3, 3, 2); 
            vertArray.set(0, vert.length/3, vert); 
            // normal vectors for the cube. used for lighting
            byte[] norm = { 
                0, 0, 127,  0, 0, 127,  0, 0, 127,  0, 0, 127, 
                0, 0,-127,  0, 0,-127,  0, 0,-127,  0, 0,-127, 
                -127, 0, 0, -127, 0, 0, -127, 0, 0, -127, 0, 0, 
                127, 0, 0,  127, 0, 0,  127, 0, 0,  127, 0, 0, 0, 
                127, 0, 0,  127, 0, 0,  127, 0, 0,  127, 0, 0,-127, 
                0, 0,-127, 0, 0,-127, 0, 0,-127, 0 }; 
 
            // create a vertex array for the normals of the object 
            VertexArray normArray = new VertexArray(norm.length / 3, 3, 1); 
            normArray.set(0, norm.length/3, norm); 
            // the length of each triangle strip 
            int[] stripLen = { 4, 4, 4, 4, 4, 4 }; 
 
            //Creating of instance of TriangleStripArray
            //(derived from IndexBuffer). It 
            //represents an array of triangle strips. 
            //In a triangle strip, the first three vertex indices define 
            //the first triangle. Each subsequent index together with the two 
            //previous indices define a new triangle.
            // create the index buffer for our object (this tells how to 
            // create triangle strips from the contents of the vertex buffer). 
            IndexBuffer indexBuffer = new TriangleStripArray( 0, stripLen ); 
 
            // per vertex texture coordinates 
            //values represents mapping of texture to vertices of 3d object
            short[] tex = { 
                1, 0, 0, 0, 1, 1, 0, 1, 
                1, 0, 0, 0, 1, 1, 0, 1, 
                1, 0, 0, 0, 1, 1, 0, 1, 
                1, 0, 0, 0, 1, 1, 0, 1, 
                1, 0, 0, 0, 1, 1, 0, 1, 
                1, 0, 0, 0, 1, 1, 0, 1 }; 
            // create a vertex array for the texture coordinates of the object 
            VertexArray texArray = new VertexArray(tex.length / 2, 2, 2); 
            texArray.set(0, tex.length/2, tex); 
            //VertexBuffer holds references to VertexArrays that contain the 
            //positions, colors, normals, and texture coordinates for a set 
            //of vertices create the VertexBuffer for our object 
            VertexBuffer vertexBuffer = new VertexBuffer(); 
            vertexBuffer.setPositions(vertArray, 1.0f, null); 
            // unit scale, zero bias 
            vertexBuffer.setNormals(normArray); 
            vertexBuffer.setTexCoords(0, texArray, 1.0f, null);
            //Texture creating
            Image textureImage = null;
            try {
                // load the image for the texture
                textureImage = Image.createImage("/images/spells/Frost_Stun.png");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // create the Image2D (we need this so we can make a Texture2D) 
            Image2D image2D = new Image2D( Image2D.RGB, textureImage ); 
            // create the Texture2D and enable mipmapping             
            Texture2D texture = new Texture2D( image2D ); 
            texture.setFiltering(Texture2D.FILTER_LINEAR, 
                                Texture2D.FILTER_LINEAR); 
            //specifying that the texture image is to be repeated only once
            texture.setWrapping(Texture2D.WRAP_CLAMP, Texture2D.WRAP_CLAMP); 
            // texture color is to be modulated with the lit material color            
            texture.setBlending(Texture2D.FUNC_MODULATE); 
 
            // create the appearance object
            //It's a set of component objects that define the rendering 
            //attributes. Using during render operation.            
            Appearance cubeAppearance = new Appearance(); 
            cubeAppearance.setTexture(0, texture); 
            cubeAppearance.setMaterial(material); 
 
            //creating Mesh object and passing as parameters
            //vertex, index buffers and Appearance component
            Mesh mesh = new Mesh(vertexBuffer, indexBuffer, cubeAppearance);
            return mesh;
        }

        private Mesh terrainInit(Material material, final String name) {

            short[] vert = { 32, 32, 0,  -32, 32, 0,  32,-32, 0,  -32,-32, 0 };
            // create a VertexArray to hold the vertices for the object 
            VertexArray vertArray = new VertexArray(vert.length / 3, 3, 2); 
            vertArray.set(0, vert.length/3, vert); 
            // normal vectors for the cube. used for lighting
            byte[] norm = { 0, 0, 127,  0, 0, 127,  0, 0, 127,  0, 0, 127 };
 
            // create a vertex array for the normals of the object 
            VertexArray normArray = new VertexArray(norm.length / 3, 3, 1); 
            normArray.set(0, norm.length/3, norm); 
            // the length of each triangle strip 
            int[] stripLen = { 4 }; 
 
            //Creating of instance of TriangleStripArray
            //(derived from IndexBuffer). It 
            //represents an array of triangle strips. 
            //In a triangle strip, the first three vertex indices define 
            //the first triangle. Each subsequent index together with the two 
            //previous indices define a new triangle.
            // create the index buffer for our object (this tells how to 
            // create triangle strips from the contents of the vertex buffer). 
            IndexBuffer indexBuffer = new TriangleStripArray( 0, stripLen ); 
 
            // per vertex texture coordinates
            //values represents mapping of texture to vertices of 3d object
            short[] tex = { 1, 0,  0, 0,  1, 1,  0, 1 }; 
            // create a vertex array for the texture coordinates of the object
            VertexArray texArray = new VertexArray(tex.length / 2, 2, 2);
            texArray.set(0, tex.length/2, tex);
            //VertexBuffer holds references to VertexArrays that contain the
            //positions, colors, normals, and texture coordinates for a set
            //of vertices create the VertexBuffer for our object
            VertexBuffer vertexBuffer = new VertexBuffer();
            vertexBuffer.setPositions(vertArray, 1.0f, null);
            // unit scale, zero bias
            vertexBuffer.setNormals(normArray);
            vertexBuffer.setTexCoords(0, texArray, 1.0f, null);
            //Texture creating
            Image textureImage = null;
            try {
                // load the image for the texture
                textureImage = Image.createImage(name);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // create the Image2D (we need this so we can make a Texture2D) 
            Image2D image2D = new Image2D( Image2D.RGB, textureImage ); 
            // create the Texture2D and enable mipmapping             
            Texture2D texture = new Texture2D( image2D ); 
            texture.setFiltering(Texture2D.FILTER_LINEAR,
                                Texture2D.FILTER_NEAREST); 
            //specifying that the texture image is to be repeated only once
            texture.setWrapping(Texture2D.WRAP_CLAMP, Texture2D.WRAP_CLAMP); 
            // texture color is to be modulated with the lit material color            
            texture.setBlending(Texture2D.FUNC_REPLACE); 
 
            // create the appearance object
            //It's a set of component objects that define the rendering 
            //attributes. Using during render operation.            
            Appearance app = new Appearance(); 
            app.setTexture(0, texture);
            app.setMaterial(material);
	    PolygonMode poly = new PolygonMode();
	    poly.setPerspectiveCorrectionEnable(true);
	    app.setPolygonMode(poly);
	    /*
	    Fog fog = new Fog();
	    fog.setLinear(10f,100f);
	    fog.setColor(0x808080);
	    app.setFog(fog);
	    */

            //creating Mesh object and passing as parameters
            //vertex, index buffers and Appearance component
            return new Mesh(vertexBuffer, indexBuffer, app);
        }


    private Node mark(final String name, float x, float y, float z) throws IOException {
	WoWgame.self().showDebug("Mark "+name+" at: "+x+" "+y+" "+z);
	Appearance app = new Appearance();
	Node obj = new Sprite3D(true,new Image2D(Image2D.RGB,Image.createImage("/test/"+name+".png")),app);
	obj.setTranslation(x,y,z);
	return obj;
    }

    private void bgRotate() {
	if (m_wld == null)
	    return;
	Background b = m_wld.getBackground();
	if (b == null)
	    return;
	Image2D bg = b.getImage();
	if (bg == null)
	    return;

	int x = (int)(bg.getWidth()*(1f-((m_rotate-m_orient)/360))) % bg.getWidth();
	b.setCrop(x,0,bg.getWidth()/2,bg.getHeight());
	WoWgame.self().showDebug("X Cropping: "+x);
    }

    private void populate3D() throws IOException {
	float ox = (float)WoWgame.self().mapLocationX();
	float oy = (float)WoWgame.self().mapLocationY();
	float oz = (float)WoWgame.self().mapLocationZ();
	WoWgame.self().showDebug("3D origin: "+ox+" "+oy+" "+oz);
	World wld = new World();
	Background b = new Background();
	b.setColorClearEnable(true);
	b.setImage(new Image2D(Image2D.RGB,Image.createImage("/images/sky.jpg")));
	b.setImageMode(Background.REPEAT,Background.BORDER);
	b.setColor(WoWgame.self().getBgColor());
	wld.setBackground(b);
	Group ply = new Group();
	ply.setTranslation(ox,oy,oz);
	Camera cam = new Camera();
	cam.setPerspective( 70.0f,1.0f,1.0f,100.0f );
	cam.postRotate(90f,0f,0f,1f);
	cam.preRotate(-96f,0f,1f,0f);
	cam.preRotate(180f,1f,0f,0f);
	Node p = mark("P",0f,0f,0f);
	p.setUserID(WoWblip.hashOf(WoWgame.self().playerGuid()));
	ply.addChild(p);
	ply.addChild(cam);
	cam.setTranslation(-8f,0f,2f);
	wld.addChild(ply);
	wld.setActiveCamera(cam);
	Light light = new Light();
	light.setMode(Light.AMBIENT);
	wld.addChild(light);
	if (WoWgame.self().debugging()) {
	    wld.addChild(mark("O",ox+0f,oy+0f,oz+0f));
	    wld.addChild(mark("Xp",ox+10f,oy+0f,oz+0f));
	    wld.addChild(mark("Xp",ox+20f,oy+0f,oz+0f));
	    wld.addChild(mark("Xp",ox+30f,oy+0f,oz+0f));
	    wld.addChild(mark("Xm",ox-10f,oy+0f,oz+0f));
	    wld.addChild(mark("Xm",ox-20f,oy+0f,oz+0f));
	    wld.addChild(mark("Xm",ox-30f,oy+0f,oz+0f));
	    wld.addChild(mark("Yp",ox+0f,oy+10f,oz+0f));
	    wld.addChild(mark("Yp",ox+0f,oy+20f,oz+0f));
	    wld.addChild(mark("Yp",ox+0f,oy+30f,oz+0f));
	    wld.addChild(mark("Ym",ox+0f,oy-10f,oz+0f));
	    wld.addChild(mark("Ym",ox+0f,oy-20f,oz+0f));
	    wld.addChild(mark("Ym",ox+0f,oy-30f,oz+0f));
	    wld.addChild(mark("Zp",ox+0f,oy+0f,oz+10f));
	    wld.addChild(mark("Zp",ox+0f,oy+0f,oz+20f));
	    wld.addChild(mark("Zp",ox+0f,oy+0f,oz+30f));
	    wld.addChild(mark("Zm",ox+0f,oy+0f,oz-10f));
	    wld.addChild(mark("Zm",ox+0f,oy+0f,oz-20f));
	    wld.addChild(mark("Zm",ox+0f,oy+0f,oz-30f));
	}
//	Node obj = cubeMeshInitialization(materialInitialization(0xffff00,0x80ffff,0.5f));
//	wld.addChild(obj);
//	obj.setTranslation(0f,14f,0f);
	Node terrain = terrainInit(materialInitialization(0xffff00,0x80ffff,0.5f),"/images/minimap/Northrend_24_22.jpg");
	terrain.setTranslation(ox+0f,oy+0f,oz-1f);
	wld.addChild(terrain);
	terrain = terrainInit(materialInitialization(0xffff00,0x80ffff,0.5f),"/images/minimap/Northrend_24_23.jpg");
	terrain.setTranslation(ox+0f,oy-64f,oz-1f);
	wld.addChild(terrain);
	terrain = terrainInit(materialInitialization(0xffff00,0x80ffff,0.5f),"/images/minimap/Northrend_24_24.jpg");
	terrain.setTranslation(ox+0f,oy-128f,oz-1f);
	wld.addChild(terrain);
	m_wld = wld;
	m_ply = ply;
	bgRotate();
    }

    public void setBlip(WoWblip blip) {
	if (m_wld == null)
	    return;
	int hash = blip.hashCode();
	synchronized (this) {
	    Node n = (Node)m_wld.find(hash);
	    if (n == null) {
		try {
		    n = mark("M",(float)blip.X,(float)blip.Y,(float)blip.Z);
		    n.setUserID(hash);
		    m_wld.addChild(n);
		}
		catch (Exception e) {
		}
	    }
	    else
		n.setTranslation((float)blip.X,(float)blip.Y,(float)blip.Z);
	}
    }

    public void delBlip(long guid) {
	if (m_wld == null)
	    return;
	int hash = WoWblip.hashOf(guid);
	synchronized (this) {
	    Node n = (Node)m_wld.find(hash);
	    if (n != null)
		m_wld.removeChild(n);
	}
    }

    public void activate(boolean active) {
	WoWgame.self().showDebug("Player.activate() active="+active);
	if (active) {
	    try {
//		m_wld = (World)Loader.load("/m3g/swerve.m3g")[0];
		populate3D();
		m_g3d = Graphics3D.getInstance();
		m_time = 0;
	    }
	    catch (Exception e) {
		e.printStackTrace();
		WoWgame.self().setDebug(e.toString());
	    }
	    Enumeration e = WoWgame.self().blips();
	    while (e.hasMoreElements())
		setBlip((WoWblip)e.nextElement());
	}
	else {
	    m_g3d = null;
	    m_wld = null;
	    m_ply = null;
	}
    }

    public void sizeEvent(int width, int height) {
	WoWgame.self().showDebug("Player.sizeEvent() "+width+" x "+height);
	m_width = width;
	m_buttonY = height - 18;
	m_cursorX = width / 2;
	m_cursorY = (m_buttonY-18)/2;
	int n = (width-2) / 34;
	m_buttons = new WoWbutton[2*n];
	for (int i = 0; i < 2*n; i++) {
	    int idx = i % n;
	    WoWaction action = WoWgame.self().getAction(i);
	    if (action == null)
		m_buttons[i] = null;
	    else {
		m_buttons[i] = new WoWbutton(action,"/images/"+action.name()+".png");
		m_buttons[i].setKey('1'+idx,null);
	    }
	}
	m_buttonCount = n;
	WoWgame.self().setDebug("Action buttons: "+n);
    }

    private void paintBlip(Graphics g, double x, double y, int color) {
	int px = (int)(-y*0.072*m_zoom);
	int py = (int)(-x*0.072*m_zoom);
	if (Math.abs(px) > 16 || Math.abs(py) > 16)
	    return;
	if ((px*px + py*py) <= 256) {
	    g.setColor(color);
	    g.drawArc(px+m_width-21,py+19,2,2,0,360);
	}
    }

    public boolean paintEvent(Graphics g) {
	double ox = WoWgame.self().mapLocationX();
	double oy = WoWgame.self().mapLocationY();
	double oz = WoWgame.self().mapLocationZ();
	float heading = (float)WoWgame.self().mapHeading();
	synchronized (this) {
	    if (m_orient != heading) {
		if (m_ply != null)
		    m_ply.preRotate(m_orient-heading,0f,0f,1f);
		m_orient = heading;
		bgRotate();
	    }
	    if (m_ply != null)
		m_ply.setTranslation((float)ox,(float)oy,(float)oz);
	}
	paint3d(g);
	g.setColor(0x008080);
	g.fillArc(m_width-40,4,36,36,(420-(int)heading)%360,60);
	Enumeration e = WoWgame.self().blips();
	while (e.hasMoreElements()) {
	    WoWblip b = (WoWblip)e.nextElement();
	    paintBlip(g,b.X-ox,b.Y-oy,b.color);
	}
	// paint a white blip at the origin (0.0,0.0)
	paintBlip(g,-ox,-oy,0xffffff);
	g.setColor(0x0000ff);
	g.drawArc(m_width-40,4,36,36,0,360);
	if (WoWgame.self().detailed()) {
	    if (WoWgame.self().threatened())
		g.setColor(0xff0000);
	    g.fillRoundRect(4,4,36,36,8,8);
	    if (WoWgame.self().playerIcon() != null)
		g.drawImage(WoWgame.self().playerIcon(),22,22,Graphics.HCENTER|Graphics.VCENTER);
	    String s = WoWgame.self().playerName() + " : " + WoWgame.self().xp() + " / " + WoWgame.self().xpNext() + " XP";
	    g.drawString(s,44,2,Graphics.LEFT|Graphics.TOP);
	    g.drawString(WoWgame.self().playerLevel(),44,40,Graphics.LEFT|Graphics.BOTTOM);
	    long sel = WoWgame.self().selectGuid();
	    if (sel != 0) {
		String tmp;
		String tmp2 = null;
		WoWobject o = WoWgame.self().getObject(sel);
		if (o != null) {
		    tmp = o.textType()+" type "+o.type()+" entry "+o.entry();
		    if (o instanceof WoWplayer) {
			WoWplayer p = (WoWplayer)o;
			tmp = p.name() + " - Level " + p.level() + " (Player)";
		    }
		    else if (o instanceof WoWunit) {
			WoWunit u = (WoWunit)o;
			InfoCreature c = WoWgame.self().getCreature(u.entry());
			if (c != null && c.valid()) {
			    tmp = c.name() + " - Level " + u.level();
			    if (c.typeName() != null)
				tmp += " " + c.typeName();
			    if (c.description() != null && !c.description().equals(""))
				tmp2 = c.description();
			    else if ((u.npcFlags() & WoWunit.NPC_QUESTGIVER) != 0)
				tmp2 = "Quest giver";
			}
			else
			    tmp += " level " + u.level();
		    }
		    else if (o instanceof WoWgobj) {
			WoWgobj go = (WoWgobj)o;
			InfoGameObject ig = WoWgame.self().getGameObject(go.entry());
			if (ig != null && ig.valid()) {
			    tmp = ig.name() + " - Level " + go.level();
			}
		    }
		    else if (o instanceof WoWcorpse) {
			WoWcorpse c = (WoWcorpse)o;
			tmp = "Corpse of " + WoWgame.self().toGuid(c.owner());
		    }
		}
		else
		    tmp = WoWobject.textType(sel)+" "+WoWgame.guidStr(sel);
		g.drawString(tmp,4,44,Graphics.LEFT|Graphics.TOP);
		if (tmp2 != null)
		    g.drawString(tmp2,4,60,Graphics.LEFT|Graphics.TOP);
	    }
	    int w = m_width-88;
	    double ratio = WoWgame.self().health() / (double)WoWgame.self().healthMax();
	    if (ratio > 0.0 && ratio <= 1.0) {
		g.setColor(0x00ff00);
		g.drawLine(44,21,(int)(44+ratio*w),21);
	    }
	    ratio = WoWgame.self().power() / (double)WoWgame.self().powerMax();
	    if (ratio > 0.0 && ratio <= 1.0) {
		g.setColor(0x0040ff);
		g.drawLine(44,23,(int)(44+ratio*w),23);
	    }
	}
	if (m_cursorShow != 0 || !WoWgame.self().hasPointer()) {
	    g.setColor(0xff0080);
	    g.drawLine(m_cursorX,m_cursorY-10,m_cursorX,m_cursorY+10);
	    g.drawLine(m_cursorX-10,m_cursorY,m_cursorX+10,m_cursorY);
	}
	Font df = g.getFont();
	g.setFont(Font.getFont(Font.FACE_PROPORTIONAL,Font.STYLE_PLAIN,Font.SIZE_SMALL));
	g.setColor(0xffc000);
	String[] logs = WoWgame.self().logs();
	int y = m_buttonY - 22;
	for (int i = 0; i < logs.length; i++) {
	    String l = logs[i];
	    if (l != null)
		g.drawString(l,4,y,Graphics.LEFT|Graphics.BOTTOM);
	    y -= (g.getFont().getHeight() + 1);
	}
	g.setColor(0xff0000);
	String[] info = WoWgame.self().info();
	y = 64;
	for (int i = info.length - 1; i >= 0; i--) {
	    String l = info[i];
	    if (l != null)
		g.drawString(l,m_width/2,y,Graphics.HCENTER|Graphics.TOP);
	    y += (g.getFont().getHeight() + 1);
	}
	int offs = WoWgame.self().shifted() ? m_buttonCount : 0;
	for (int i = 0; i < m_buttonCount; i++) {
	    WoWbutton act = m_buttons[i+offs];
	    if (act != null)
		drawButton(g,34*i+18,m_buttonY,act);
	}
	g.setFont(df);
	return false;
    }

    public boolean keyEvent(int key, int action, boolean pressed, boolean repeated) {
	//WoWgame.self().showDebug("Player.keyEvent() key="+key+" act="+action+" press="+pressed);
	if (pressed && WoWgame.self().shifted() && (action != 0) && (key < 0)) {
	    switch (action) {
		case Canvas.LEFT:
		    cameraSpin(5f);
		    return true;
		case Canvas.RIGHT:
		    cameraSpin(-5f);
		    return true;
		case Canvas.UP:
		    m_cursorY -= 8;
		    if (m_cursorY < 10)
			m_cursorY = 10;
		    return true;
		case Canvas.DOWN:
		    m_cursorY += 8;
		    if (m_cursorY > m_buttonY-28)
			m_cursorY = m_buttonY-28;
		    return true;
	    }
	}
	if (!repeated && (action != 0) && (key < 0)) {
	    if (pressed && WoWgame.self().shifted()) {
		switch (action) {
		    case Canvas.FIRE:
			int y = (m_buttonY-18)/2;
			long sel = pickWorld(m_width/2,m_cursorY);
			if (sel == -1)
			    WoWgame.self().fireEffect("swing_sword");
			else if (sel != 0)
			    WoWgame.self().setSelect(sel);
			return true;
		    case Canvas.UP:
			cameraZoom(-0.5f);
			return true;
		    case Canvas.DOWN:
			cameraZoom(0.5f);
			return true;
		}
	    }
	    switch (action) {
		case Canvas.FIRE:
		    if (pressed && WoWgame.self().selectGuid() != 0)
			WoWgame.self().action("query");
		    return true;
		case Canvas.LEFT:
		    WoWgame.self().action("rotate",pressed ? "left" : "none");
		    return true;
		case Canvas.RIGHT:
		    WoWgame.self().action("rotate",pressed ? "right" : "none");
		    return true;
		case Canvas.UP:
		    WoWgame.self().action("move",pressed ? "forward" : "none");
		    return true;
		case Canvas.DOWN:
		    WoWgame.self().action("move",pressed ? "backward" : "none");
		    return true;
	    }
	}
	if (pressed && !repeated) {
	    int offs = WoWgame.self().shifted() ? m_buttonCount : 0;
	    for (int i = 0; i < m_buttonCount; i++) {
		WoWbutton act = m_buttons[i+offs];
		if ((act != null) && act.matches(key)) {
		    WoWgame.self().setDebug("Matched button "+act.label()+" action "+act.name());
		    act.setGrayed();
		    WoWgame.self().action(act.action());
		    return true;
		}
	    }
	    switch (key) {
		case '0':
		    if (WoWgame.self().shifted())
			WoWgame.self().jumpScreen("CharSelect");
		    else
			WoWgame.self().pushScreen("GameMenu");
		    return true;
		case '*':
		    if (!WoWgame.self().shifted()) {
			WoWgame.self().setSelect(0);
			return true;
		    }
		case '#':
		    if (!WoWgame.self().shifted() && WoWgame.self().selectGuid() != 0) {
			WoWgame.self().action("follow");
			return true;
		    }
		case -6:
		    if (!WoWgame.self().shifted()) {
			if (m_zoom > 2)
			    m_zoom--;
			return true;
		    }
		    return false;
		case -7:
		    if (!WoWgame.self().shifted()) {
			if (m_zoom < 6)
			    m_zoom++;
		    }
		    return false;
	    }
	}
	return false;
    }

    public boolean ptrEvent(int x, int y, boolean pressed, boolean dragged)
    {
	int dx = x - m_x;
	int dy = y - m_y;
	m_x = x;
	m_y = y;
	if (y >= m_buttonY-18) {
	    int idx = x / 34;
	    if (WoWgame.self().shifted())
		idx += m_buttonCount;
	    WoWbutton act = m_buttons[idx];
	    if (act != null) {
		if (!pressed && !dragged && act.active()) {
		    WoWgame.self().setDebug("Matched button "+act.label()+" action "+act.name());
		    act.setGrayed();
		    WoWgame.self().action(act.name());
		    return true;
		}
	    }
	}
	if (m_wld != null && dragged) {
	    if (dy != 0)
		cameraZoom(0.01f*dy);
	    if (dx != 0)
		cameraSpin(0.2f*dx);
	    return true;
	}
	if (pressed) {
	    dx = x - m_width + 20;
	    dy = y - 20;
	    if (dx*dx + dy*dy <= 272) {
		WoWgame.self().pushScreen("MapView");
		return true;
	    }
	    long sel = 0;
	    if (y < m_buttonY-18 && (sel = pickWorld(x,y)) != 0) {
		if (sel == -1)
		    WoWgame.self().fireEffect("swing_sword");
		else
		    WoWgame.self().setSelect(sel);
		return true;
	    }
	}
	return false;
    }

    public void update(long elapsed) {
	m_time += elapsed;
	if (m_wld == null)
	    return;
	synchronized (this) {
	    m_wld.animate(m_time);
	}
    }

    public int idle() {
	if (m_cursorShow != 0 && System.currentTimeMillis() > m_cursorShow)
	    m_cursorShow = 0;
	return 100;
    }

    private long pickWorld(int x, int y) {
	if (m_wld == null)
	    return 0;
	m_cursorX = x;
	m_cursorY = y;
	m_cursorShow = 1500 + System.currentTimeMillis();
	synchronized (this) {
	    RayIntersection ri = new RayIntersection();
	    float px = x * 1.0f / m_width;
	    float py = y * 1.0f / (m_buttonY-18);
	    if (m_wld.pick(-1,px,py,m_wld.getActiveCamera(),ri)) {
		Node n = ri.getIntersected();
		WoWgame.self().setDebug(n.getClass().getName());
		int hash = n.getUserID();
		if (hash != 0 && WoWgame.self().conn() != null) {
		    if (hash == WoWblip.hashOf(WoWgame.self().playerGuid()))
			return WoWgame.self().playerGuid();
		    Enumeration e = WoWgame.self().blips();
		    while (e.hasMoreElements()) {
			WoWblip b = (WoWblip)e.nextElement();
			if (b.hashCode() == hash) {
			    WoWgame.self().showDebug("Hit Guid "+WoWgame.guidStr(b.guid()));
			    return b.guid();
			}
		    }
		}
		return -1;
	    }
	}
	WoWgame.self().showDebug("Not picked "+x+","+y);
	return 0;
    }

    private void cameraZoom(float factor) {
	if (m_wld == null)
	    return;
	synchronized (this) {
	    Camera c = m_wld.getActiveCamera();
	    c.translate(-factor,0f,0f);
	}
    }

    private void cameraSpin(float angle) {
	if (m_wld == null)
	    return;
	synchronized (this) {
	    Camera c = m_wld.getActiveCamera();
	    c.preRotate(angle,0f,0f,1f);
	    m_rotate += angle;
	    bgRotate();
	}
    }

    private void paint3d(Graphics g) {
	if (m_g3d == null || m_wld == null)
	    return;
	try {
	    m_g3d.bindTarget(g);
	    m_g3d.setViewport(0,0,m_width,m_buttonY-18);
	    synchronized (this) {
		Background b = m_wld.getBackground();
		if (b != null)
		    b.setColor(WoWgame.self().getBgColor());
		m_g3d.render(m_wld);
	    }
	}
	catch (Exception e) {
	    e.printStackTrace();
	    WoWgame.self().setDebug(e.toString());
	}
	finally {
	    m_g3d.releaseTarget();
	}
    }
}
