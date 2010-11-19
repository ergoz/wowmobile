// World of Warcraft Mobile
//
// Character object

package wow;

import javax.microedition.lcdui.*;

public class WoWchar {

    public static final int RACE_HUMAN           =  1;
    public static final int RACE_ORC             =  2;
    public static final int RACE_DWARF           =  3;
    public static final int RACE_NIGHTELF        =  4;
    public static final int RACE_UNDEAD_PLAYER   =  5;
    public static final int RACE_TAUREN          =  6;
    public static final int RACE_GNOME           =  7;
    public static final int RACE_TROLL           =  8;
    public static final int RACE_BLOODELF        = 10;
    public static final int RACE_DRAENEI         = 11;

    public static final int CLASS_WARRIOR        =  1;
    public static final int CLASS_PALADIN        =  2;
    public static final int CLASS_HUNTER         =  3;
    public static final int CLASS_ROGUE          =  4;
    public static final int CLASS_PRIEST         =  5;
    public static final int CLASS_DEATH_KNIGHT   =  6;
    public static final int CLASS_SHAMAN         =  7;
    public static final int CLASS_MAGE           =  8;
    public static final int CLASS_WARLOCK        =  9;
    public static final int CLASS_DRUID          = 11;

    public static final int GENDER_MALE          =  0;
    public static final int GENDER_FEMALE        =  1;
    public static final int GENDER_NONE          =  2;

    public static final int CHAR_LOCKED_TRANSFER = 0x00000004;
    public static final int CHAR_HIDE_HELM       = 0x00000400;
    public static final int CHAR_HIDE_CLOAK      = 0x00000800;
    public static final int CHAR_GHOST           = 0x00002000;
    public static final int CHAR_RENAME          = 0x00004000;
    public static final int CHAR_LOCKED_BILLING  = 0x01000000;
    public static final int CHAR_DECLINED        = 0x02000000;

    private String m_name;
    private String m_level;
    private String m_area;
    private Image m_icon;
    public long m_guid;
    public int m_race;
    public int m_class;
    public int m_gender;
    public int m_flags;

    public WoWchar(final String name, final String level, final String area) {
	m_name = name;
	m_level = level;
	m_area = area;
	m_icon = null;
	m_guid = 0;
    }

    public WoWchar(final String name, final String level, final String area, Image icon) {
	m_name = name;
	m_level = level;
	m_area = area;
	m_icon = icon;
	m_guid = 0;
    }

    public WoWchar(final String name, final String level, final String area, final String icon) {
	m_name = name;
	m_level = level;
	m_area = area;
	m_icon = null;
	m_guid = 0;
	try {
	    m_icon = Image.createImage(icon);
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public String name() {
	return m_name;
    }

    public long guid() {
	return m_guid;
    }

    public String level() {
	return m_level;
    }

    public String area() {
	return m_area;
    }

    public Image icon() {
	return m_icon;
    }

    public static final String textRace(int race) {
	switch (race) {
	    case RACE_HUMAN:
		return "Human";
	    case RACE_ORC:
		return "Orc";
	    case RACE_DWARF:
		return "Dwarf";
	    case RACE_NIGHTELF:
		return "Night Elf";
	    case RACE_UNDEAD_PLAYER:
		return "Undead";
	    case RACE_TAUREN:
		return "Tauren";
	    case RACE_GNOME:
		return "Gnome";
	    case RACE_TROLL:
		return "Troll";
	    case RACE_BLOODELF:
		return "Blood Elf";
	    case RACE_DRAENEI:
		return "Draenei";
	}
	return null;
    }

    public final String textRace() {
	return textRace(m_race);
    }

    public static final String textClass(int cls) {
	switch (cls) {
	    case CLASS_WARRIOR:
		return "Warrior";
	    case CLASS_PALADIN:
		return "Paladin";
	    case CLASS_HUNTER:
		return "Hunter";
	    case CLASS_ROGUE:
		return "Rogue";
	    case CLASS_PRIEST:
		return "Priest";
	    case CLASS_DEATH_KNIGHT:
		return "Death Knight";
	    case CLASS_SHAMAN:
		return "Shaman";
	    case CLASS_MAGE:
		return "Mage";
	    case CLASS_WARLOCK:
		return "Warlock";
	    case CLASS_DRUID:
		return "Druid";
	}
	return null;
    }

    public final String textClass() {
	return textRace(m_class);
    }

    public static final String textGender(int gen) {
	switch (gen) {
	    case GENDER_MALE:
		return "Male";
	    case GENDER_FEMALE:
		return "Female";
	}
	return null;
    }

    public final String textGender() {
	return textRace(m_gender);
    }

}
