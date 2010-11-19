// World of Warcraft Mobile
//
// Screen and sound effect

package wow;

import java.util.Random;

public class WoWeffect {

    private final int m_color;
    private String m_sound[];

    public WoWeffect(int color, final String sound) {
	m_color = color;
	m_sound = new String[1];
	m_sound[0] = sound;
    }

    public WoWeffect(int color, final String sound, final String sound2) {
	m_color = color;
	m_sound = new String[2];
	m_sound[0] = sound;
	m_sound[1] = sound2;
    }

    public WoWeffect(int color, final String sound, final String sound2, final String sound3) {
	m_color = color;
	m_sound = new String[3];
	m_sound[0] = sound;
	m_sound[1] = sound2;
	m_sound[2] = sound3;
    }

    public WoWeffect(int color) {
	m_color = color;
	m_sound = null;
    }

    public WoWeffect(final String sound) {
	m_color = 0;
	m_sound = new String[1];
	m_sound[0] = sound;
    }

    public int color() {
	return m_color;
    }

    public final String sound() {
	if (m_sound == null)
	    return null;
	Random rnd = new Random();
	return m_sound[rnd.nextInt(m_sound.length)];
    }
}
