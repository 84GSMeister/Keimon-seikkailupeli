package keimo.keimoengine.fontit;

import java.awt.Color;

/**
 * Wrapper-luokka AWT:n Color-luokan ympärille. Helpottaa mahdollista uudelleentoteutusta ilman AWT:ta.
 */

public class Väri extends Color {

    public Väri(int rgb) {
        super(rgb);
    }

    public Väri(int r, int g, int b) {
        super(r, g, b);
    }

    public Väri(int r, int g, int b, int a) {
        super(r, g, b, a);
    }

    public static Väri haeVäri(Color väri) {
        return new Väri(väri.getRGB());
    }

    /**
     * The color white.  In the default sRGB space.
     */
    public static final Väri white     = new Väri(255, 255, 255);

    /**
     * The color white.  In the default sRGB space.
     * @since 1.4
     */
    public static final Väri WHITE = white;

    /**
     * The color light gray.  In the default sRGB space.
     */
    public static final Väri lightGray = new Väri(192, 192, 192);

    /**
     * The color light gray.  In the default sRGB space.
     * @since 1.4
     */
    public static final Väri LIGHT_GRAY = lightGray;

    /**
     * The color gray.  In the default sRGB space.
     */
    public static final Väri gray      = new Väri(128, 128, 128);

    /**
     * The color gray.  In the default sRGB space.
     * @since 1.4
     */
    public static final Väri GRAY = gray;

    /**
     * The color dark gray.  In the default sRGB space.
     */
    public static final Väri darkGray  = new Väri(64, 64, 64);

    /**
     * The color dark gray.  In the default sRGB space.
     * @since 1.4
     */
    public static final Väri DARK_GRAY = darkGray;

    /**
     * The color black.  In the default sRGB space.
     */
    public static final Väri black     = new Väri(0, 0, 0);

    /**
     * The color black.  In the default sRGB space.
     * @since 1.4
     */
    public static final Väri BLACK = black;

    /**
     * The color red.  In the default sRGB space.
     */
    public static final Väri red       = new Väri(255, 0, 0);

    /**
     * The color red.  In the default sRGB space.
     * @since 1.4
     */
    public static final Väri RED = red;

    /**
     * The color pink.  In the default sRGB space.
     */
    public static final Väri pink      = new Väri(255, 175, 175);

    /**
     * The color pink.  In the default sRGB space.
     * @since 1.4
     */
    public static final Väri PINK = pink;

    /**
     * The color orange.  In the default sRGB space.
     */
    public static final Väri orange    = new Väri(255, 200, 0);

    /**
     * The color orange.  In the default sRGB space.
     * @since 1.4
     */
    public static final Väri ORANGE = orange;

    /**
     * The color yellow.  In the default sRGB space.
     */
    public static final Väri yellow    = new Väri(255, 255, 0);

    /**
     * The color yellow.  In the default sRGB space.
     * @since 1.4
     */
    public static final Väri YELLOW = yellow;

    /**
     * The color green.  In the default sRGB space.
     */
    public static final Väri green     = new Väri(0, 255, 0);

    /**
     * The color green.  In the default sRGB space.
     * @since 1.4
     */
    public static final Väri GREEN = green;

    /**
     * The color magenta.  In the default sRGB space.
     */
    public static final Väri magenta   = new Väri(255, 0, 255);

    /**
     * The color magenta.  In the default sRGB space.
     * @since 1.4
     */
    public static final Väri MAGENTA = magenta;

    /**
     * The color cyan.  In the default sRGB space.
     */
    public static final Väri cyan      = new Väri(0, 255, 255);

    /**
     * The color cyan.  In the default sRGB space.
     * @since 1.4
     */
    public static final Väri CYAN = cyan;

    /**
     * The color blue.  In the default sRGB space.
     */
    public static final Väri blue      = new Väri(0, 0, 255);

    /**
     * The color blue.  In the default sRGB space.
     * @since 1.4
     */
    public static final Väri BLUE = blue;

    public static final Väri valkoinen = white;
    public static final Väri vaaleanharmaa = lightGray;
    public static final Väri harmaa = gray;
    public static final Väri tummanharmaa = darkGray;
    public static final Väri musta = black;
    public static final Väri punainen = red;
    public static final Väri pinkki = pink;
    public static final Väri oranssi = orange;
    public static final Väri keltainen = yellow;
    public static final Väri vihreä = green;
    public static final Väri syaani = cyan;
    public static final Väri sininen = blue;

    public static final Väri V0 = musta;
    public static final Väri V1 = new Väri(0, 0, 128);
    public static final Väri V2 = new Väri(0, 128, 0);
    public static final Väri V3 = new Väri(0, 128, 128);
    public static final Väri V4 = new Väri(128, 0, 0);
    public static final Väri V5 = new Väri(128, 0, 128);
    public static final Väri V6 = new Väri(128, 128, 0);
    public static final Väri V7 = vaaleanharmaa;
    public static final Väri V8 = harmaa;
    public static final Väri V9 = sininen;
    public static final Väri VA = vihreä;
    public static final Väri VB = syaani;
    public static final Väri VC = punainen;
    public static final Väri VD = magenta;
    public static final Väri VE = keltainen;
    public static final Väri VF = valkoinen;
}
