package keimo.keimoEngine.gui.hud;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.keimoEngine.KeimoEngine;
import keimo.keimoEngine.grafiikat.Teksti;
import keimo.keimoEngine.gui.HUD;
import keimo.keimoEngine.ikkuna.Window;
import keimo.keimoEngine.kenttä.Maailma;

import java.awt.Color;
import java.text.DecimalFormat;

public class DebugTeksti {

    static Teksti näppäinInfoTeksti = new Teksti("debug", Color.green, 600, 30);
    static Teksti debugInfoTeksti = new Teksti("debug", Color.red, 400, 30);
    static Teksti lisäMoodiTeksti = new Teksti("moodi", Color.orange, 400, 30);
    static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");
	static DecimalFormat neljäDesimaalia = new DecimalFormat("##.####");
    
    public static void renderöiDebugTeksti(double tileAika, double pelaajaAika, double hudAika, Window window) {
        try {
            int sijx = (int)(window.getWidth()/5.5);
            näppäinInfoTeksti.päivitäTeksti("Keimon seikkailupeli v1.0.1 Alfa");
            HUD.renderöiTeksti(näppäinInfoTeksti, sijx, 40, window);
            näppäinInfoTeksti.päivitäTeksti("F1: Käynistä uudelleen, F2: Kaada peli");
            HUD.renderöiTeksti(näppäinInfoTeksti, sijx, 45, window);
            näppäinInfoTeksti.päivitäTeksti("Huijauskoodit F5: noclip, F6: ohita tavoitteet");
            HUD.renderöiTeksti(näppäinInfoTeksti, sijx, 60, window);

            if (KeimoEngine.frameTime > 0) debugInfoTeksti.päivitäTeksti("fps: " + kaksiDesimaalia.format(1d / (KeimoEngine.frameTime / KeimoEngine.frames)));
            else debugInfoTeksti.päivitäTeksti("fps: " + kaksiDesimaalia.format(1d / (KeimoEngine.frameTime+0.00001 / KeimoEngine.frames)));
            HUD.renderöiTeksti(debugInfoTeksti, sijx, 120, window);
            debugInfoTeksti.päivitäTeksti("maailma: " + kaksiDesimaalia.format(tileAika/1_000_000d) + " ms");
            HUD.renderöiTeksti(debugInfoTeksti, sijx, 140, window);
            debugInfoTeksti.päivitäTeksti("pelaaja: " + kaksiDesimaalia.format(pelaajaAika/1_000_000d) + " ms");
            HUD.renderöiTeksti(debugInfoTeksti, sijx, 160, window);
            debugInfoTeksti.päivitäTeksti("hud: " + kaksiDesimaalia.format(hudAika/1_000_000d) + " ms");
            HUD.renderöiTeksti(debugInfoTeksti, sijx, 180, window);

            debugInfoTeksti.päivitäTeksti("Huone: " + Peli.huone.annaId() + " (" + Peli.huone.annaNimi() + ")");
            HUD.renderöiTeksti(debugInfoTeksti, sijx, 220, window);
            debugInfoTeksti.päivitäTeksti("sij X: " + Pelaaja.sijX + " (" + Pelaaja.hitbox.getCenterX() + ")");
            HUD.renderöiTeksti(debugInfoTeksti, sijx, 240, window);
            debugInfoTeksti.päivitäTeksti("sij Y: " + Pelaaja.sijY + " (" + Pelaaja.hitbox.getCenterY() + ")");
            HUD.renderöiTeksti(debugInfoTeksti, sijx, 260, window);

            debugInfoTeksti.päivitäTeksti("Tilejä: " + Maailma.tileMäärä);
            HUD.renderöiTeksti(debugInfoTeksti, sijx, 300, window);
            debugInfoTeksti.päivitäTeksti("Objekteja: " + Maailma.objektiMäärä);
            HUD.renderöiTeksti(debugInfoTeksti, sijx, 320, window);
            debugInfoTeksti.päivitäTeksti("Entityjä: " + Maailma.entityMäärä);
            HUD.renderöiTeksti(debugInfoTeksti, sijx, 340, window);

            debugInfoTeksti.päivitäTeksti("Keimon toiminto: " + Pelaaja.keimonState);
            HUD.renderöiTeksti(debugInfoTeksti, sijx, 380, window);
            debugInfoTeksti.päivitäTeksti("Keimon kylläisyys: " + Pelaaja.keimonKylläisyys);
            HUD.renderöiTeksti(debugInfoTeksti, sijx, 400, window);
            debugInfoTeksti.päivitäTeksti("Keimon terveys: " + Pelaaja.keimonTerveys);
            HUD.renderöiTeksti(debugInfoTeksti, sijx, 420, window);
            debugInfoTeksti.päivitäTeksti("Keimon suunta: " + Pelaaja.keimonSuunta + " (" + Pelaaja.keimonSuuntaVasenOikea + ")");
            HUD.renderöiTeksti(debugInfoTeksti, sijx, 440, window);
            debugInfoTeksti.päivitäTeksti("Reaktioaika: " + Pelaaja.reaktioAika);
            HUD.renderöiTeksti(debugInfoTeksti, sijx, 460, window);
            debugInfoTeksti.päivitäTeksti("Kuolemattomuusaika: " + Pelaaja.kuolemattomuusAika);
            HUD.renderöiTeksti(debugInfoTeksti, sijx, 480, window);
            debugInfoTeksti.päivitäTeksti("Kännikuolemattomuus: " + Pelaaja.känniKuolemattomuus);
            HUD.renderöiTeksti(debugInfoTeksti, sijx, 500, window);
            debugInfoTeksti.päivitäTeksti("Kännin voimakkuus: " + kaksiDesimaalia.format(Pelaaja.känninVoimakkuusFloat) + " (" + kaksiDesimaalia.format(Pelaaja.känninVoimakkuusFloat*(1.5f/4f)) + " ‰)");
            HUD.renderöiTeksti(debugInfoTeksti, sijx, 520, window);
        }
        catch (NullPointerException npe) {
            System.out.println("Debug-tekstin näyttämisessä virhe");
            npe.printStackTrace();
        }
    }

    public static void renderöiLisäMoodiTekstit(Window window) {
        int sijx = (int)(window.getWidth()/5.5);
        if (Pelaaja.noclip) {
            lisäMoodiTeksti.päivitäTeksti("Noclip");
            HUD.renderöiTeksti(lisäMoodiTeksti, sijx, 560, window);
        }
        if (Pelaaja.ohitaTavoitteet) {
            lisäMoodiTeksti.päivitäTeksti("Ohita tavoitteet");
            HUD.renderöiTeksti(lisäMoodiTeksti, sijx, 580, window);
        }
    }
}
