package keimo;

import keimo.Kenttäkohteet.*;
import keimo.NPCt.*;
import keimo.Ruudut.PeliRuutu;
import keimo.Säikeet.*;
import keimo.Liikkuminen.*;

import javax.swing.*;
import java.awt.Rectangle;

public class Pelaaja implements Käännettävä{
    
    public static final Esine[] esineet = new Esine[6];
    static int valittuEsine = 0;
    public static int sijX;
    public static int sijY;
    public static Rectangle hitbox = new Rectangle(0, 0, PeliRuutu.pelaajanKokoPx, PeliRuutu.pelaajanKokoPx);
    public static int hp;
    public static float raha;
    public static int kuparit;
    static int syödytRuoat = 0;
    public static ImageIcon kuvake;
    public static int kuolemattomuusAika = 0;
    static int reaktioAika = 0;
    static boolean vihollisenKohdalla = false;
    static Vihollinen vihollinenKohdalla;
    public static Vihollinen viimeisinOsunutVihollinen;
    public static int känninVoimakkuus = 0;
    public static float känninVoimakkuusFloat = 0f;

    /**
     * Valitse tila, jonka mukaan kuvake valitaan grafiikkasäikeessä sekä
     * @param parannus kasvata hp:ta
     */

    void syöRuoka(int parannus) {
        this.paranna(parannus);
        syödytRuoat++;
        switch (syödytRuoat) {
            case 0:
                keimonKylläisyys = KeimonKylläisyys.LAIHA;
                break;
            case 1:
                keimonKylläisyys = KeimonKylläisyys.NORMAALI;
                break;
            case 2:
                keimonKylläisyys = KeimonKylläisyys.LIHAVA;
                break;
            case 3:
                keimonKylläisyys = KeimonKylläisyys.ERITTÄIN_LIHAVA;
                break;
            case 4:
                keimonKylläisyys = KeimonKylläisyys.YLENSYÖNTI;
                break;
            default:
                keimonKylläisyys = KeimonKylläisyys.LAIHA;
                break;
        }
    }

    public static void nollaaKylläisyys() {
        keimonKylläisyys = KeimonKylläisyys.LAIHA;
        syödytRuoat = 0;
    }

    /**
     * Siirrä pelaajan hitboxia (java.awt.Rectangle) 8 pikseliä valittuun suuntaan
     * Sen jälkeen päivitä pelaajan sijainti pelikentällä vastaamaan hitboxin keskipistettä lähinnä olevaa ruutua.
     * @param liikkuminen
     * @return siirtyikö pelaaja (valitussa suunnassa ei este tai kentän reuna)
     */

    static boolean siirry(Liikkuminen liikkuminen) {
        boolean pelaajaSiirtyi = false;
        if (liikkuminen instanceof LiikkuminenVasemmalle) {
            if ((int)hitbox.getMinX() > Peli.kentänAlaraja) {
                hitbox.setLocation((int)hitbox.getMinX() - 8, (int)hitbox.getMinY());
                pelaajaSiirtyi = true;
            }
        }
        else if (liikkuminen instanceof LiikkuminenOikealle) {
            if ((int)hitbox.getMaxX() < Peli.kentänKoko * PeliRuutu.pelaajanKokoPx) {
                hitbox.setLocation((int)hitbox.getMinX() + 8, (int)hitbox.getMinY());
                pelaajaSiirtyi = true;
            }
        }
        else if (liikkuminen instanceof LiikkuminenYlös) {
            if ((int)hitbox.getMinY() > Peli.kentänAlaraja) {
                hitbox.setLocation((int)hitbox.getMinX(), (int)hitbox.getMinY() - 8);
                pelaajaSiirtyi = true;
            }
        }
        else if (liikkuminen instanceof LiikkuminenAlas) {
            if ((int)hitbox.getMaxY() < Peli.kentänKoko * PeliRuutu.pelaajanKokoPx) {
                hitbox.setLocation((int)hitbox.getMinX(), (int)hitbox.getMinY() + 8);
                pelaajaSiirtyi = true;
            }
        }
        sijX = (int)hitbox.getCenterX() / PeliRuutu.pelaajanKokoPx;
        sijY = (int)hitbox.getCenterY() / PeliRuutu.pelaajanKokoPx;
        return pelaajaSiirtyi;
    }

    /**
     * Tarkista, voiko valittuun suuntaan liikkua
     * Jos voi, kutsu siirry() -metodia
     * Tätä metodia toistetaan niin kauan, kun jokin nuolinäppäin on pohjassa
     * @param suunta
     * @return siirtyikö pelaaja (valitussa suunnassa ei este tai kentän reuna)
     */

    static boolean kokeileLiikkumista(String suunta) {
        boolean pelaajaSiirtyi = false;
        try {
            switch (suunta) {
                case "vasen":
                    int tarkistaVasen = (int)(hitbox.getMinX()-8)/64;
                    if (hitbox.getMinX() > 0) {
                        if (Peli.maastokenttä[tarkistaVasen][sijY] != null && Peli.pelikenttä[tarkistaVasen][sijY] != null) {
                            if (!Peli.maastokenttä[tarkistaVasen][sijY].estääköLiikkumisen()) {
                                if (Peli.pelikenttä[tarkistaVasen][sijY] instanceof VisuaalinenObjekti) {
                                    VisuaalinenObjekti vo = (VisuaalinenObjekti)Peli.pelikenttä[tarkistaVasen][sijY];
                                    if (!vo.onkoEste()) {
                                        pelaajaSiirtyi = siirry(new LiikkuminenVasemmalle());
                                    }
                                }
                                else {
                                    pelaajaSiirtyi = siirry(new LiikkuminenVasemmalle());
                                }
                            }
                        }
                        else if (Peli.maastokenttä[tarkistaVasen][sijY] != null) {
                            if (!Peli.maastokenttä[tarkistaVasen][sijY].estääköLiikkumisen()) {
                                pelaajaSiirtyi = siirry(new LiikkuminenVasemmalle());
                            }
                        }
                        else {
                            pelaajaSiirtyi = siirry(new LiikkuminenVasemmalle());
                        }
                    }
                break;
                case "oikea":   
                    int tarkistaOikea = (int)hitbox.getMinX()/64 +1;
                    if (hitbox.getMaxX() < Peli.kentänKoko * PeliRuutu.pelaajanKokoPx) {
                        if (Peli.maastokenttä[tarkistaOikea][sijY] != null && Peli.pelikenttä[tarkistaOikea][sijY] != null) {
                            if (!Peli.maastokenttä[tarkistaOikea][sijY].estääköLiikkumisen()) {
                                if (Peli.pelikenttä[tarkistaOikea][sijY] instanceof VisuaalinenObjekti) {
                                    VisuaalinenObjekti vo = (VisuaalinenObjekti)Peli.pelikenttä[tarkistaOikea][sijY];
                                    if (!vo.onkoEste()) {
                                        pelaajaSiirtyi = siirry(new LiikkuminenOikealle());
                                    }
                                }
                                else {
                                    pelaajaSiirtyi = siirry(new LiikkuminenOikealle());
                                }
                            }
                        }
                        else if (Peli.maastokenttä[tarkistaOikea][sijY] != null) {
                            if (!Peli.maastokenttä[tarkistaOikea][sijY].estääköLiikkumisen()) {
                                pelaajaSiirtyi = siirry(new LiikkuminenOikealle());
                            }
                        }
                        else {
                            pelaajaSiirtyi = siirry(new LiikkuminenOikealle());
                        }
                    }
                break;
                case "alas":
                    int tarkistaAlas = (int)hitbox.getMinY()/64 +1;
                    if (hitbox.getMaxY() < Peli.kentänKoko * PeliRuutu.pelaajanKokoPx) {
                        if (Peli.maastokenttä[sijX][tarkistaAlas] != null && Peli.pelikenttä[sijX][tarkistaAlas] != null) {
                            if (!Peli.maastokenttä[sijX][tarkistaAlas].estääköLiikkumisen()) {
                                if (Peli.pelikenttä[sijX][tarkistaAlas] instanceof VisuaalinenObjekti) {
                                    VisuaalinenObjekti vo = (VisuaalinenObjekti)Peli.pelikenttä[sijX][tarkistaAlas];
                                    if (!vo.onkoEste()) {
                                        pelaajaSiirtyi = siirry(new LiikkuminenAlas());
                                    }
                                }
                                else {
                                    pelaajaSiirtyi = siirry(new LiikkuminenAlas());
                                }
                            }
                        }
                        else if (Peli.maastokenttä[sijX][tarkistaAlas] != null) {
                            if (!Peli.maastokenttä[sijX][tarkistaAlas].estääköLiikkumisen()) {
                                pelaajaSiirtyi = siirry(new LiikkuminenAlas());
                            }
                        }
                        else {
                            pelaajaSiirtyi = siirry(new LiikkuminenAlas());
                        }
                    }
                break;
                case "ylös":
                    int tarkistaYlös = (int)(hitbox.getMinY()-8)/64;
                    if (hitbox.getMinY() > 0) {
                        if (Peli.maastokenttä[sijX][tarkistaYlös] != null && Peli.pelikenttä[sijX][tarkistaYlös] != null) {
                            if (!Peli.maastokenttä[sijX][tarkistaYlös].estääköLiikkumisen()) {
                                if (Peli.pelikenttä[sijX][tarkistaYlös] instanceof VisuaalinenObjekti) {
                                    VisuaalinenObjekti vo = (VisuaalinenObjekti)Peli.pelikenttä[sijX][tarkistaYlös];
                                    if (!vo.onkoEste()) {
                                        pelaajaSiirtyi = siirry(new LiikkuminenYlös());
                                    }
                                }
                                else {
                                    pelaajaSiirtyi = siirry(new LiikkuminenYlös());
                                }
                            }
                        }
                        else if (Peli.maastokenttä[sijX][tarkistaYlös] != null) {
                            if (!Peli.maastokenttä[sijX][tarkistaYlös].estääköLiikkumisen()) {
                                pelaajaSiirtyi = siirry(new LiikkuminenYlös());
                            }
                        }
                        else {
                            pelaajaSiirtyi = siirry(new LiikkuminenYlös());
                        }
                    }
                break;
                default:
                break;
            }
        }
        catch (ArrayIndexOutOfBoundsException aioobe) {
            aioobe.printStackTrace();
        }
        return pelaajaSiirtyi;
    }

    public static void annaEsine(Esine e) {
        if (e != null) {
            if (annaEsineidenMäärä() < annaTavaraluettelonKoko()) {
                for (int i = 0; i < esineet.length; i++) {
                    if (esineet[i] == null) {
                        //esineet[i] = new Vesiämpäri(false, 0, 0);
                        esineet[i] = e;
                        break;
                    }
                }
                PääIkkuna.hudTeksti.setText("Sait uuden " + e.annaNimiSijamuodossa("genetiivi"));
            }
            else {
                PääIkkuna.hudTeksti.setText("Ei voida poimia! Tavaraluettelo täynnä! Kokeile pudottaa jokin esine tyhjään ruutuun");
            }
        }
    }

    public static KeimonState keimonState = KeimonState.IDLE;
    public enum KeimonState {
        IDLE,
        JUOKSU,
        KUOLLUT;
    }

    public static KeimonKylläisyys keimonKylläisyys = KeimonKylläisyys.LAIHA;
    public enum KeimonKylläisyys {
        LAIHA,
        NORMAALI,
        LIHAVA,
        ERITTÄIN_LIHAVA,
        YLENSYÖNTI;
    }

    public static KeimonTerveys keimonTerveys = KeimonTerveys.OK;
    public enum KeimonTerveys {
        HUONO,
        OK,
        HYVÄ,
        ÜBER;
    }

    public static Suunta keimonSuunta = Suunta.ALAS;
    // public enum Suunta {
    //     VASEN,
    //     OIKEA,
    //     ALAS,
    //     YLÖS;
    // }

    public static SuuntaVasenOikea keimonSuuntaVasenOikea = SuuntaVasenOikea.OIKEA;
    public enum SuuntaVasenOikea {
        VASEN,
        OIKEA;
    }

    public static void pakotaPelaajanPysäytys() {
        pelaajaLiikkuuVasen = false;
        pelaajaLiikkuuOikea = false;
        pelaajaLiikkuuYlös = false;
        pelaajaLiikkuuAlas = false;
        keimonState = KeimonState.IDLE;
    }
    
    public static boolean pelaajaLiikkuuVasen = false;
    public static boolean pelaajaLiikkuuOikea = false;
    public static boolean pelaajaLiikkuuYlös = false;
    public static boolean pelaajaLiikkuuAlas = false;

    void aloitaLiike(Suunta suunta) {
        keimonState = KeimonState.JUOKSU;
        switch (suunta) {
            case VASEN:
                pelaajaLiikkuuVasen = true;
                keimonSuunta = Suunta.VASEN;
                keimonSuuntaVasenOikea = SuuntaVasenOikea.VASEN;
                break;
            case OIKEA:
                pelaajaLiikkuuOikea = true;
                keimonSuunta = Suunta.OIKEA;
                keimonSuuntaVasenOikea = SuuntaVasenOikea.OIKEA;
                break;
            case YLÖS:
                pelaajaLiikkuuYlös = true;
                keimonSuunta = Suunta.YLÖS;
                break;
            case ALAS:
                pelaajaLiikkuuAlas = true;
                keimonSuunta = Suunta.ALAS;
                break;
        }
    }

    static boolean liikutaPelaajaa() {
        boolean pelaajaLiikkui = false;
        if (pelaajaLiikkuuVasen) {
            kokeileLiikkumista("vasen");
            pelaajaLiikkui = true;
        }
        if (pelaajaLiikkuuOikea) {
            kokeileLiikkumista("oikea");
            pelaajaLiikkui = true;
        }
        if (pelaajaLiikkuuYlös) {
            kokeileLiikkumista("ylös");
            pelaajaLiikkui = true;
        }
        if (pelaajaLiikkuuAlas) {
            kokeileLiikkumista("alas");
            pelaajaLiikkui = true;
        }
        return pelaajaLiikkui;
    }

    void lopetaLiike(Suunta suunta) {
        PääIkkuna.pelaajaSiirtyi = true;

        switch (suunta) {
            case VASEN:
                pelaajaLiikkuuVasen = false;
                break;
            case OIKEA:
                pelaajaLiikkuuOikea = false;
                break;
            case YLÖS:
                pelaajaLiikkuuYlös = false;
                break;
            case ALAS:
                pelaajaLiikkuuAlas = false;
                break;
        }

        if (pelaajaLiikkuuVasen == false && pelaajaLiikkuuOikea == false && pelaajaLiikkuuYlös == false && pelaajaLiikkuuAlas == false) {
            keimonState = KeimonState.IDLE;
        }
    }

    void päivitäHitboxPositio(int kohdeX, int kohdeY) {
        hitbox.setLocation(kohdeX * PeliRuutu.pelaajanKokoPx, kohdeY * PeliRuutu.pelaajanKokoPx);
    }

    /**
     * Vaihda pelaajan sijainti valittuun ruutuun (oviruutuja varten)
     * Päivitä hitbox ruudun kohdalle
     * @param kohdeX
     * @param kohdeY
     */

    void teleport(int kohdeX, int kohdeY) {
        sijX = kohdeX;
        sijY = kohdeY;
        hitbox.setLocation(sijX * PeliRuutu.pelaajanKokoPx, sijY * PeliRuutu.pelaajanKokoPx);
        PeliRuutu.ylätekstiSij.setText("Pelaaja siirrettiin sijaintiin " + sijX + ", " + sijY + " (" + hitbox.getMinX() + "-" + hitbox.getMaxX() + ", " + hitbox.getMinY() + "-" + hitbox.getMaxY() + ")");
    }

    public static int annaEsineidenMäärä() {
        int määrä = 0;
        for (int i = 0; i < esineet.length; i++) {
            if (esineet[i] != null) {
                määrä++;
            }
        }
        return määrä;
    }
    public static int annaTavaraluettelonKoko() {
        return esineet.length;
    }

    int annaHp() {
        return hp;
    }

    static void vahingoita(int määrä) {
        hp -= määrä;
        kuolemattomuusAika = 120;
        ÄänentoistamisSäie.toistaSFX("pelaaja_damage");
        PeliRuutu.ylätekstiHP.setText("HP: " + hp);
        päivitäTerveys();
    }

    void paranna(int määrä) {
        hp += määrä;
        PeliRuutu.ylätekstiHP.setText("HP: " + hp);
        päivitäTerveys();
    }

    public static void päivitäTerveys() {
        if (hp < 6) {
            keimonTerveys = KeimonTerveys.HUONO;
        }
        else if (hp >= 20) {
            keimonTerveys = KeimonTerveys.HYVÄ;
        }
        else {
            keimonTerveys = KeimonTerveys.OK;
        }
    }

    public static void vähennäKuolemattomuusAikaa() {
        if (kuolemattomuusAika > 0) {
            kuolemattomuusAika--;
        }
    }

    public static void vähennäReaktioAikaa() {
        if (reaktioAika > 0) {
            reaktioAika--;
        }
    }

    Pelaaja() {
        hp = Peli.aloitusHp;
        raha = 0f;
        kuparit = 0;
        syödytRuoat = 0;
        kuvake = new ImageIcon("tiedostot/kuvat/keimo_idle.gif");
        keimonState = KeimonState.IDLE;
        keimonKylläisyys = KeimonKylläisyys.LAIHA;
        keimonTerveys = KeimonTerveys.OK;
        sijX = 0;
        sijY = 0;
        hitbox.setLocation(0, 0);
        pelaajaLiikkuuVasen = false;
        pelaajaLiikkuuOikea = false;
        pelaajaLiikkuuAlas = false;
        pelaajaLiikkuuYlös = false;
        känninVoimakkuus = 0;
        känninVoimakkuusFloat = 0f;
    }
}
