package keimo.seikkailupeli.gui.hud;

import keimo.keimoengine.KeimoEngine;
import keimo.keimoengine.Kello;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.ikkuna.Window;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.kenttä.Maailma;
import keimo.seikkailupeli.objektit.Pelaaja;

import java.awt.Color;
import java.text.DecimalFormat;

public class DebugTeksti {

    static Teksti versioInfoTeksti = new Teksti("debug", Color.white, 1200, 48);
    static Teksti näppäinInfoTeksti1 = new Teksti("debug", Color.gray, 1500, 48);
    static Teksti näppäinInfoTeksti2 = new Teksti("debug", Color.gray, 1200, 48);

    static Teksti debugInfoTekstiFPS = new Teksti("debug", Color.red, 1200, 48);
    static Teksti debugInfoTekstiMaailmaMs = new Teksti("debug", Color.red, 1200, 48);
    static Teksti debugInfoTekstiPelaajaMs = new Teksti("debug", Color.red, 1200, 48);
    static Teksti debugInfoTekstiHudMs = new Teksti("debug", Color.red, 1200, 48);
    static Teksti debugInfoTekstiGlobaaliTickit = new Teksti("debug", Color.red, 1200, 48);
    static Teksti debugInfoTekstiHuone = new Teksti("debug", Color.red, 1200, 48);
    static Teksti debugInfoTekstiSijX = new Teksti("debug", Color.red, 1200, 48);
    static Teksti debugInfoTekstiSijY = new Teksti("debug", Color.red, 1200, 48);
    static Teksti debugInfoTekstiTileMäärä = new Teksti("debug", Color.red, 1200, 48);
    static Teksti debugInfoTekstiObjektiMäärä = new Teksti("debug", Color.red, 1200, 48);
    static Teksti debugInfoTekstiEntityMäärä = new Teksti("debug", Color.red, 1200, 48);
    static Teksti debugInfoTekstiKeimonToiminto = new Teksti("debug", Color.red, 1200, 48);
    static Teksti debugInfoTekstiKeimonKylläisyys = new Teksti("debug", Color.red, 1200, 48);
    static Teksti debugInfoTekstiKeimonTerveys = new Teksti("debug", Color.red, 1200, 48);
    static Teksti debugInfoTekstiKeimonSuunta = new Teksti("debug", Color.red, 1200, 48);
    static Teksti debugInfoTekstiReaktioaika = new Teksti("debug", Color.red, 1200, 48);
    static Teksti debugInfoTekstiKuolemattomuusaika = new Teksti("debug", Color.red, 1200, 48);
    static Teksti debugInfoTekstiKänniKuolemattomuus = new Teksti("debug", Color.red, 1200, 48);
    static Teksti debugInfoTekstiKänninVoimakkuus = new Teksti("debug", Color.red, 1200, 48);

    static Teksti huijauskoodiTeksti1 = new Teksti("koodi", Color.orange, 1000, 48);
    static Teksti huijauskoodiTeksti2 = new Teksti("koodi", Color.orange, 1000, 48);
    static Teksti huijauskoodiTeksti3 = new Teksti("koodi", Color.orange, 1000, 48);

    static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");
	static DecimalFormat neljäDesimaalia = new DecimalFormat("##.####");
    
    public static void renderöiDebugTeksti(double tileAika, double pelaajaAika, double hudAika, Window window) {
        try {
            int sijx = (int)(window.getWidth()/5.5);
            int sijy = (int)(window.getHeight()/8f);
            int sijyOffset = (int)(window.getHeight()/54f);
            versioInfoTeksti.päivitäTeksti("Keimon seikkailupeli v1.2 Alfa");
            HUD.renderöiTeksti(versioInfoTeksti, sijx, sijy, window);
            näppäinInfoTeksti1.päivitäTeksti("F1: Käynistä uudelleen, F2: Kaada peli");
            HUD.renderöiTeksti(näppäinInfoTeksti1, sijx, sijy + sijyOffset, window);
            näppäinInfoTeksti2.päivitäTeksti("F3: Debug, F5: Huijauskoodit");
            HUD.renderöiTeksti(näppäinInfoTeksti2, sijx, sijy + 2*sijyOffset, window);

            if (KeimoEngine.frameTime > 0) debugInfoTekstiFPS.päivitäTeksti("fps: " + kaksiDesimaalia.format(1d / (KeimoEngine.frameTime / KeimoEngine.frames)));
            else debugInfoTekstiFPS.päivitäTeksti("fps: " + kaksiDesimaalia.format(1d / (KeimoEngine.frameTime+0.00001 / KeimoEngine.frames)));
            HUD.renderöiTeksti(debugInfoTekstiFPS, sijx, sijy + 4*sijyOffset, window);
            debugInfoTekstiMaailmaMs.päivitäTeksti("maailma: " + kaksiDesimaalia.format(tileAika/1_000_000d) + " ms");
            HUD.renderöiTeksti(debugInfoTekstiMaailmaMs, sijx, sijy + 5*sijyOffset, window);
            debugInfoTekstiPelaajaMs.päivitäTeksti("pelaaja: " + kaksiDesimaalia.format(pelaajaAika/1_000_000d) + " ms");
            HUD.renderöiTeksti(debugInfoTekstiPelaajaMs, sijx, sijy + 6*sijyOffset, window);
            debugInfoTekstiHudMs.päivitäTeksti("hud: " + kaksiDesimaalia.format(hudAika/1_000_000d) + " ms");
            HUD.renderöiTeksti(debugInfoTekstiHudMs, sijx, sijy + 7*sijyOffset, window);
            debugInfoTekstiGlobaaliTickit.päivitäTeksti("globaali ajastin: " + Kello.globaaliTickit());
            HUD.renderöiTeksti(debugInfoTekstiGlobaaliTickit, sijx, sijy + 8*sijyOffset, window);

            debugInfoTekstiHuone.päivitäTeksti("Huone: " + Peli.huone.annaId() + " (" + Peli.huone.annaNimi() + ")");
            HUD.renderöiTeksti(debugInfoTekstiHuone, sijx, sijy + 10*sijyOffset, window);
            debugInfoTekstiSijX.päivitäTeksti("sij X: " + Pelaaja.sijX + " (" + Pelaaja.hitbox.getCenterX() + ")");
            HUD.renderöiTeksti(debugInfoTekstiSijX, sijx, sijy + 11*sijyOffset, window);
            debugInfoTekstiSijY.päivitäTeksti("sij Y: " + Pelaaja.sijY + " (" + Pelaaja.hitbox.getCenterY() + ")");
            HUD.renderöiTeksti(debugInfoTekstiSijY, sijx, sijy + 12*sijyOffset, window);

            debugInfoTekstiTileMäärä.päivitäTeksti("Tilejä: " + Maailma.tileMäärä);
            HUD.renderöiTeksti(debugInfoTekstiTileMäärä, sijx, sijy + 14*sijyOffset, window);
            debugInfoTekstiObjektiMäärä.päivitäTeksti("Objekteja: " + Maailma.objektiMäärä);
            HUD.renderöiTeksti(debugInfoTekstiObjektiMäärä, sijx, sijy + 15*sijyOffset, window);
            debugInfoTekstiEntityMäärä.päivitäTeksti("Entityjä: " + Maailma.entityMäärä);
            HUD.renderöiTeksti(debugInfoTekstiEntityMäärä, sijx, sijy + 16*sijyOffset, window);

            debugInfoTekstiKeimonToiminto.päivitäTeksti("Keimon toiminto: " + Pelaaja.keimonState);
            HUD.renderöiTeksti(debugInfoTekstiKeimonToiminto, sijx, sijy + 18*sijyOffset, window);
            debugInfoTekstiKeimonKylläisyys.päivitäTeksti("Keimon kylläisyys: " + Pelaaja.keimonKylläisyys);
            HUD.renderöiTeksti(debugInfoTekstiKeimonKylläisyys, sijx, sijy + 19*sijyOffset, window);
            debugInfoTekstiKeimonTerveys.päivitäTeksti("Keimon terveys: " + Pelaaja.keimonTerveys);
            HUD.renderöiTeksti(debugInfoTekstiKeimonTerveys, sijx, sijy + 20*sijyOffset, window);
            debugInfoTekstiKeimonSuunta.päivitäTeksti("Keimon suunta: " + Pelaaja.keimonSuunta + " (" + Pelaaja.keimonSuuntaVasenOikea + ")");
            HUD.renderöiTeksti(debugInfoTekstiKeimonSuunta, sijx, sijy + 21*sijyOffset, window);
            debugInfoTekstiReaktioaika.päivitäTeksti("Reaktioaika: " + Pelaaja.reaktioAika);
            HUD.renderöiTeksti(debugInfoTekstiReaktioaika, sijx, sijy + 22*sijyOffset, window);
            debugInfoTekstiKuolemattomuusaika.päivitäTeksti("Kuolemattomuusaika: " + Pelaaja.kuolemattomuusAika);
            HUD.renderöiTeksti(debugInfoTekstiKuolemattomuusaika, sijx, sijy + 23*sijyOffset, window);
            debugInfoTekstiKänniKuolemattomuus.päivitäTeksti("Kännikuolemattomuus: " + Pelaaja.känniKuolemattomuus);
            HUD.renderöiTeksti(debugInfoTekstiKänniKuolemattomuus, sijx, sijy + 24*sijyOffset, window);
            debugInfoTekstiKänninVoimakkuus.päivitäTeksti("Kännin voimakkuus: " + kaksiDesimaalia.format(Pelaaja.känninVoimakkuusFloat) + " (" + kaksiDesimaalia.format(Pelaaja.känninVoimakkuusFloat*(1.5f/4f)) + " ‰)");
            HUD.renderöiTeksti(debugInfoTekstiKänninVoimakkuus, sijx, sijy + 25*sijyOffset, window);
        }
        catch (NullPointerException npe) {
            System.out.println("Debug-tekstin näyttämisessä virhe");
            npe.printStackTrace();
        }
    }

    public static void renderöiLisäMoodiTekstit(Window window) {
        int sijx = (int)(window.getWidth()/1.6);
        int sijy = (int)(window.getHeight()/8f);
        int sijyOffset = (int)(window.getHeight()/54f);
        if (Pelaaja.noclip) {
            huijauskoodiTeksti1.päivitäTeksti("Noclip");
            HUD.renderöiTeksti(huijauskoodiTeksti1, sijx, sijy, window);
        }
        if (Pelaaja.ohitaTavoitteet) {
            huijauskoodiTeksti2.päivitäTeksti("Ohita tavoitteet");
            HUD.renderöiTeksti(huijauskoodiTeksti2, sijx, sijy + sijyOffset, window);
        }
        if (Pelaaja.loputonRaha) {
            huijauskoodiTeksti3.päivitäTeksti("Loputon raha");
            HUD.renderöiTeksti(huijauskoodiTeksti3, sijx, sijy + 2*sijyOffset, window);
        }
    }
}
