package keimo.Utility;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;

public class KeimoFontit {

    public static Font fontti_keimo_10, fontti_keimo_12, fontti_keimo_14, fontti_keimo_16, fontti_keimo_20, fontti_keimo_30;
    
    private static void openFontTTF(String name) {
        String fontPath = "tiedostot/kuvat/font/" + name + ".ttf";
        File fonttiTiedosto = new File(fontPath);
        try {
            Font fontti;
            fontti = Font.createFont(Font.TRUETYPE_FONT, fonttiTiedosto);
            fontti_keimo_10 = fontti.deriveFont(10f);
            fontti_keimo_12 = fontti.deriveFont(12f);
            fontti_keimo_14 = fontti.deriveFont(14f);
            fontti_keimo_16 = fontti.deriveFont(16f);
            fontti_keimo_20 = fontti.deriveFont(20f);
            fontti_keimo_30 = fontti.deriveFont(30f);
        }
        catch (Exception e) {
            System.out.println("Fonttia ei voitu ladata");
            e.printStackTrace();
        }
    }

    public static void rekisteröiFontit() {
        openFontTTF("keimon_paskafontti");
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(fontti_keimo_10);
        ge.registerFont(fontti_keimo_12);
        ge.registerFont(fontti_keimo_14);
        ge.registerFont(fontti_keimo_16);
        ge.registerFont(fontti_keimo_20);
        ge.registerFont(fontti_keimo_30);
    }
}
