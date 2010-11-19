// World of Warcraft Mobile
//
// Spell object

package wow;

public class WoWspell {

    public static final int SCHOOL_NORMAL        = 0;
    public static final int SCHOOL_HOLY          = 1;
    public static final int SCHOOL_FIRE          = 2;
    public static final int SCHOOL_NATURE        = 3;
    public static final int SCHOOL_FROST         = 4;
    public static final int SCHOOL_SHADOW        = 5;
    public static final int SCHOOL_ARCANE        = 6;

    public static final String textSchool(int school) {
	switch (school) {
	    case SCHOOL_NORMAL:
		return "normal";
	    case SCHOOL_HOLY:
		return "holy";
	    case SCHOOL_FIRE:
		return "fire";
	    case SCHOOL_NATURE:
		return "nature";
	    case SCHOOL_FROST:
		return "frost";
	    case SCHOOL_SHADOW:
		return "shadow";
	    case SCHOOL_ARCANE:
		return "arcane";
	}
	return null;
    }

}
