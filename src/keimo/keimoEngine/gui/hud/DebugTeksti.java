package keimo.keimoEngine.gui.hud;

import keimo.Pelaaja;
import keimo.Peli;
import keimo.keimoEngine.KeimoEngine;
import keimo.keimoEngine.grafiikat.Teksti;
import keimo.keimoEngine.ikkuna.Window;
import keimo.keimoEngine.kenttä.Maailma;

import java.awt.Color;
import java.text.DecimalFormat;

public class DebugTeksti {

    static Teksti versioInfoTeksti = new Teksti("debug", Color.green, 600, 30);
    static Teksti näppäinInfoTeksti1 = new Teksti("debug", Color.green, 600, 30);
    static Teksti näppäinInfoTeksti2 = new Teksti("debug", Color.green, 600, 30);

    static Teksti debugInfoTekstiFPS = new Teksti("debug", Color.red, 400, 30);
    static Teksti debugInfoTekstiMaailmaMs = new Teksti("debug", Color.red, 400, 30);
    static Teksti debugInfoTekstiPelaajaMs = new Teksti("debug", Color.red, 400, 30);
    static Teksti debugInfoTekstiHudMs = new Teksti("debug", Color.red, 400, 30);
    static Teksti debugInfoTekstiHuone = new Teksti("debug", Color.red, 400, 30);
    static Teksti debugInfoTekstiSijX = new Teksti("debug", Color.red, 400, 30);
    static Teksti debugInfoTekstiSijY = new Teksti("debug", Color.red, 400, 30);
    static Teksti debugInfoTekstiTileMäärä = new Teksti("debug", Color.red, 400, 30);
    static Teksti debugInfoTekstiObjektiMäärä = new Teksti("debug", Color.red, 400, 30);
    static Teksti debugInfoTekstiEntityMäärä = new Teksti("debug", Color.red, 400, 30);
    static Teksti debugInfoTekstiKeimonToiminto = new Teksti("debug", Color.red, 400, 30);
    static Teksti debugInfoTekstiKeimonKylläisyys = new Teksti("debug", Color.red, 400, 30);
    static Teksti debugInfoTekstiKeimonTerveys = new Teksti("debug", Color.red, 400, 30);
    static Teksti debugInfoTekstiKeimonSuunta = new Teksti("debug", Color.red, 400, 30);
    static Teksti debugInfoTekstiReaktioaika = new Teksti("debug", Color.red, 400, 30);
    static Teksti debugInfoTekstiKuolemattomuusaika = new Teksti("debug", Color.red, 400, 30);
    static Teksti debugInfoTekstiKänniKuolemattomuus = new Teksti("debug", Color.red, 400, 30);
    static Teksti debugInfoTekstiKänninVoimakkuus = new Teksti("debug", Color.red, 400, 30);

    static Teksti huijauskoodiTeksti1 = new Teksti("koodi", Color.orange, 400, 30);
    static Teksti huijauskoodiTeksti2 = new Teksti("koodi", Color.orange, 400, 30);

    static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");
	static DecimalFormat neljäDesimaalia = new DecimalFormat("##.####");
    
    public static void renderöiDebugTeksti(double tileAika, double pelaajaAika, double hudAika, Window window) {
        try {
            int sijx = (int)(window.getWidth()/5.5);
            versioInfoTeksti.päivitäTeksti("Keimon seikkailupeli v1.0.2 Alfa");
            HUD.renderöiTeksti(versioInfoTeksti, sijx, 40, window);
            näppäinInfoTeksti1.päivitäTeksti("F1: Käynistä uudelleen, F2: Kaada peli");
            HUD.renderöiTeksti(näppäinInfoTeksti1, sijx, 65, window);
            näppäinInfoTeksti2.päivitäTeksti("Huijauskoodit F5: noclip, F6: ohita tavoitteet");
            HUD.renderöiTeksti(näppäinInfoTeksti2, sijx, 80, window);

            if (KeimoEngine.frameTime > 0) debugInfoTekstiFPS.päivitäTeksti("fps: " + kaksiDesimaalia.format(1d / (KeimoEngine.frameTime / KeimoEngine.frames)));
            else debugInfoTekstiFPS.päivitäTeksti("fps: " + kaksiDesimaalia.format(1d / (KeimoEngine.frameTime+0.00001 / KeimoEngine.frames)));
            HUD.renderöiTeksti(debugInfoTekstiFPS, sijx, 120, window);
            debugInfoTekstiMaailmaMs.päivitäTeksti("maailma: " + kaksiDesimaalia.format(tileAika/1_000_000d) + " ms");
            HUD.renderöiTeksti(debugInfoTekstiMaailmaMs, sijx, 140, window);
            debugInfoTekstiPelaajaMs.päivitäTeksti("pelaaja: " + kaksiDesimaalia.format(pelaajaAika/1_000_000d) + " ms");
            HUD.renderöiTeksti(debugInfoTekstiPelaajaMs, sijx, 160, window);
            debugInfoTekstiHudMs.päivitäTeksti("hud: " + kaksiDesimaalia.format(hudAika/1_000_000d) + " ms");
            HUD.renderöiTeksti(debugInfoTekstiHudMs, sijx, 180, window);

            debugInfoTekstiHuone.päivitäTeksti("Huone: " + Peli.huone.annaId() + " (" + Peli.huone.annaNimi() + ")");
            HUD.renderöiTeksti(debugInfoTekstiHuone, sijx, 220, window);
            debugInfoTekstiSijX.päivitäTeksti("sij X: " + Pelaaja.sijX + " (" + Pelaaja.hitbox.getCenterX() + ")");
            HUD.renderöiTeksti(debugInfoTekstiSijX, sijx, 240, window);
            debugInfoTekstiSijY.päivitäTeksti("sij Y: " + Pelaaja.sijY + " (" + Pelaaja.hitbox.getCenterY() + ")");
            HUD.renderöiTeksti(debugInfoTekstiSijY, sijx, 260, window);

            debugInfoTekstiTileMäärä.päivitäTeksti("Tilejä: " + Maailma.tileMäärä);
            HUD.renderöiTeksti(debugInfoTekstiTileMäärä, sijx, 300, window);
            debugInfoTekstiObjektiMäärä.päivitäTeksti("Objekteja: " + Maailma.objektiMäärä);
            HUD.renderöiTeksti(debugInfoTekstiObjektiMäärä, sijx, 320, window);
            debugInfoTekstiEntityMäärä.päivitäTeksti("Entityjä: " + Maailma.entityMäärä);
            HUD.renderöiTeksti(debugInfoTekstiEntityMäärä, sijx, 340, window);

            debugInfoTekstiKeimonToiminto.päivitäTeksti("Keimon toiminto: " + Pelaaja.keimonState);
            HUD.renderöiTeksti(debugInfoTekstiKeimonToiminto, sijx, 380, window);
            debugInfoTekstiKeimonKylläisyys.päivitäTeksti("Keimon kylläisyys: " + Pelaaja.keimonKylläisyys);
            HUD.renderöiTeksti(debugInfoTekstiKeimonKylläisyys, sijx, 400, window);
            debugInfoTekstiKeimonTerveys.päivitäTeksti("Keimon terveys: " + Pelaaja.keimonTerveys);
            HUD.renderöiTeksti(debugInfoTekstiKeimonTerveys, sijx, 420, window);
            debugInfoTekstiKeimonSuunta.päivitäTeksti("Keimon suunta: " + Pelaaja.keimonSuunta + " (" + Pelaaja.keimonSuuntaVasenOikea + ")");
            HUD.renderöiTeksti(debugInfoTekstiKeimonSuunta, sijx, 440, window);
            debugInfoTekstiReaktioaika.päivitäTeksti("Reaktioaika: " + Pelaaja.reaktioAika);
            HUD.renderöiTeksti(debugInfoTekstiReaktioaika, sijx, 460, window);
            debugInfoTekstiKuolemattomuusaika.päivitäTeksti("Kuolemattomuusaika: " + Pelaaja.kuolemattomuusAika);
            HUD.renderöiTeksti(debugInfoTekstiKuolemattomuusaika, sijx, 480, window);
            debugInfoTekstiKänniKuolemattomuus.päivitäTeksti("Kännikuolemattomuus: " + Pelaaja.känniKuolemattomuus);
            HUD.renderöiTeksti(debugInfoTekstiKänniKuolemattomuus, sijx, 500, window);
            debugInfoTekstiKänninVoimakkuus.päivitäTeksti("Kännin voimakkuus: " + kaksiDesimaalia.format(Pelaaja.känninVoimakkuusFloat) + " (" + kaksiDesimaalia.format(Pelaaja.känninVoimakkuusFloat*(1.5f/4f)) + " ‰)");
            HUD.renderöiTeksti(debugInfoTekstiKänninVoimakkuus, sijx, 520, window);
        }
        catch (NullPointerException npe) {
            System.out.println("Debug-tekstin näyttämisessä virhe");
            npe.printStackTrace();
        }
    }

    public static void renderöiLisäMoodiTekstit(Window window) {
        int sijx = (int)(window.getWidth()/5.5);
        if (Pelaaja.noclip) {
            huijauskoodiTeksti1.päivitäTeksti("Noclip");
            HUD.renderöiTeksti(huijauskoodiTeksti1, sijx, 560, window);
        }
        if (Pelaaja.ohitaTavoitteet) {
            huijauskoodiTeksti2.päivitäTeksti("Ohita tavoitteet");
            HUD.renderöiTeksti(huijauskoodiTeksti2, sijx, 580, window);
        }
    }
}
