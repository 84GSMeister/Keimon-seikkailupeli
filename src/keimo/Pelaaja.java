package keimo;

import keimo.Kenttäkohteet.*;
import keimo.NPCt.*;
import keimo.Ruudut.PeliRuutu;
import keimo.Säikeet.*;
import keimo.Utility.Käännettävä;
import keimo.Utility.SkaalattavaKuvake;
import keimo.Liikkuminen.*;
import keimo.Maastot.EsteTile;

import javax.swing.*;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class Pelaaja implements Käännettävä {
    
    public static final Esine[] esineet = new Esine[6];
    static int valittuEsine = 0;
    public static int sijX;
    public static int sijY;
    public static Rectangle hitbox = new Rectangle(0, 0, PeliRuutu.pelaajanKokoPx, PeliRuutu.pelaajanKokoPx);
    public static int hp;
    public static double raha;
    public static int kuparit;
    static int syödytRuoat;
    public static int nopeus;
    public static int vakionopeus = 8;
    public static ImageIcon kuvake;
    public static SkaalattavaKuvake vilkkuvaKuvake;
    public static int kuolemattomuusAika;
    public static int reaktioAika;
    public static int hyökkäysAika;
    public static int hyökkäysViive;
    public static Ase käytettyAse;
    static boolean vihollisenKohdalla = false;
    static Vihollinen vihollinenKohdalla;
    public static Vihollinen viimeisinOsunutVihollinen;
    public static ArrayList<Esine> ostosKori = new ArrayList<Esine>();
    public static double ostostenHintaYhteensä;
    public static float känninVoimakkuusFloat;
    private static Random r = new Random();

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
     * Siirrä pelaajan hitboxia (java.awt.Rectangle) nopeuden verran (pikseleissä) valittuun suuntaan.
     * Sen jälkeen päivitä pelaajan sijainti pelikentällä (tile) vastaamaan hitboxin keskipistettä lähinnä olevaa ruutua.
     * @param liikkuminen
     * @return siirtyikö pelaaja (valitussa suunnassa ei este tai kentän reuna)
     */

    static boolean siirry(Liikkuminen liikkuminen) {
        boolean pelaajaSiirtyi = false;
        if (liikkuminen instanceof LiikkuminenVasemmalle) {
            if ((int)hitbox.getMinX() > Peli.kentänAlaraja) {
                hitbox.setLocation((int)hitbox.getMinX() - nopeus, (int)hitbox.getMinY());
                pelaajaSiirtyi = true;
            }
        }
        else if (liikkuminen instanceof LiikkuminenOikealle) {
            if ((int)hitbox.getMaxX() < Peli.kentänKoko * PeliRuutu.pelaajanKokoPx) {
                hitbox.setLocation((int)hitbox.getMinX() + nopeus, (int)hitbox.getMinY());
                pelaajaSiirtyi = true;
            }
        }
        else if (liikkuminen instanceof LiikkuminenYlös) {
            if ((int)hitbox.getMinY() > Peli.kentänAlaraja) {
                hitbox.setLocation((int)hitbox.getMinX(), (int)hitbox.getMinY() - nopeus);
                pelaajaSiirtyi = true;
            }
        }
        else if (liikkuminen instanceof LiikkuminenAlas) {
            if ((int)hitbox.getMaxY() < Peli.kentänKoko * PeliRuutu.pelaajanKokoPx) {
                hitbox.setLocation((int)hitbox.getMinX(), (int)hitbox.getMinY() + nopeus);
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

    static boolean kokeileLiikkumista(Suunta suunta) {
        boolean pelaajaSiirtyi = false;
        float harhaliikkeenTodennäköisyys = känninVoimakkuusFloat/5 - 0.3f;
        int tarkistaVasen = (int)(hitbox.getMinX()-nopeus)/PeliRuutu.pelaajanKokoPx;
        int tarkistaOikea = (int)(hitbox.getMaxX())/PeliRuutu.pelaajanKokoPx;
        int tarkistaAlas = (int)(hitbox.getMaxY())/PeliRuutu.pelaajanKokoPx;
        int tarkistaYlös = (int)(hitbox.getMinY()-nopeus)/PeliRuutu.pelaajanKokoPx;
        try {
            switch (suunta) {
                case VASEN:
                    if (hitbox.getMinX() > 0) {
                        // Visuaalinen objekti vasemmalla
                        if (Peli.maastokenttä[tarkistaVasen][sijY] != null && Peli.pelikenttä[tarkistaVasen][sijY] != null) {
                            if (!Peli.maastokenttä[tarkistaVasen][sijY].estääköLiikkumisen(suunta)) {
                                if (Peli.pelikenttä[tarkistaVasen][sijY] instanceof VisuaalinenObjekti) {
                                    VisuaalinenObjekti vo = (VisuaalinenObjekti)Peli.pelikenttä[tarkistaVasen][sijY];
                                    if (!vo.onkoEste()) {
                                        pelaajaSiirtyi = siirry(new LiikkuminenVasemmalle());
                                    }
                                }
                                else if (Peli.pelikenttä[tarkistaVasen][sijY] instanceof AvattavaEste) {
                                    AvattavaEste ae = (AvattavaEste)Peli.pelikenttä[tarkistaVasen][sijY];
                                    if (ae.onkoAvattu()) {
                                        pelaajaSiirtyi = siirry(new LiikkuminenVasemmalle());
                                    }
                                }
                                else {
                                    if (harhaliikkeenTodennäköisyys > Math.random()) {
                                        if (r.nextBoolean()) {
                                            if (Peli.maastokenttä[sijX][tarkistaAlas] != null) {
                                                if (!Peli.maastokenttä[sijX][tarkistaAlas].estääköLiikkumisen(suunta)) {
                                                    siirry(new LiikkuminenAlas());
                                                }
                                            }
                                        }
                                        else {
                                            if (Peli.maastokenttä[sijX][tarkistaYlös] != null) {
                                                if (!Peli.maastokenttä[sijX][tarkistaYlös].estääköLiikkumisen(suunta)) {
                                                    siirry(new LiikkuminenYlös());
                                                }
                                            }
                                        }
                                    }
                                    pelaajaSiirtyi = siirry(new LiikkuminenVasemmalle());
                                }
                            }
                        }
                        // Maasto vasemmalla
                        else if (Peli.maastokenttä[tarkistaVasen][sijY] != null) {
                            if (!Peli.maastokenttä[tarkistaVasen][sijY].estääköLiikkumisen(suunta)) {
                                if (harhaliikkeenTodennäköisyys > Math.random()) {
                                    if (r.nextBoolean()) {
                                        if (Peli.maastokenttä[sijX][tarkistaAlas] != null) {
                                            if (!Peli.maastokenttä[sijX][tarkistaAlas].estääköLiikkumisen(suunta)) {
                                                siirry(new LiikkuminenAlas());
                                            }
                                        }
                                    }
                                    else {
                                        if (Peli.maastokenttä[sijX][tarkistaYlös] != null) {
                                            if (!Peli.maastokenttä[sijX][tarkistaYlös].estääköLiikkumisen(suunta)) {
                                                siirry(new LiikkuminenYlös());
                                            }
                                        }
                                    }
                                }
                                pelaajaSiirtyi = siirry(new LiikkuminenVasemmalle());
                            }
                        }
                        // Tyhjä vasemmalla
                        else {
                            if (harhaliikkeenTodennäköisyys > Math.random()) {
                                if (r.nextBoolean()) {
                                    siirry(new LiikkuminenAlas());
                                }
                                else {
                                    siirry(new LiikkuminenYlös());
                                }
                            }
                            pelaajaSiirtyi = siirry(new LiikkuminenVasemmalle());
                        }
                    }
                break;
                case OIKEA:
                    if (hitbox.getMaxX() < Peli.kentänKoko * PeliRuutu.pelaajanKokoPx) {
                        // Visuaalinen objekti oikealla
                        if (Peli.maastokenttä[tarkistaOikea][sijY] != null && Peli.pelikenttä[tarkistaOikea][sijY] != null) {
                            if (!Peli.maastokenttä[tarkistaOikea][sijY].estääköLiikkumisen(suunta)) {
                                if (Peli.pelikenttä[tarkistaOikea][sijY] instanceof VisuaalinenObjekti) {
                                    VisuaalinenObjekti vo = (VisuaalinenObjekti)Peli.pelikenttä[tarkistaOikea][sijY];
                                    if (!vo.onkoEste()) {
                                        pelaajaSiirtyi = siirry(new LiikkuminenOikealle());
                                    }
                                }
                                else if (Peli.pelikenttä[tarkistaOikea][sijY] instanceof AvattavaEste) {
                                    AvattavaEste ae = (AvattavaEste)Peli.pelikenttä[tarkistaOikea][sijY];
                                    if (ae.onkoAvattu()) {
                                        pelaajaSiirtyi = siirry(new LiikkuminenOikealle());
                                    }
                                }
                                else {
                                    if (harhaliikkeenTodennäköisyys > Math.random()) {
                                        if (r.nextBoolean()) {
                                            if (Peli.maastokenttä[sijX][tarkistaAlas] != null) {
                                                if (!Peli.maastokenttä[sijX][tarkistaAlas].estääköLiikkumisen(suunta)) {
                                                    siirry(new LiikkuminenAlas());
                                                }
                                            }
                                        }
                                        else {
                                            if (Peli.maastokenttä[sijX][tarkistaYlös] != null) {
                                                if (!Peli.maastokenttä[sijX][tarkistaYlös].estääköLiikkumisen(suunta)) {
                                                    siirry(new LiikkuminenYlös());
                                                }
                                            }
                                        }
                                    }
                                    pelaajaSiirtyi = siirry(new LiikkuminenOikealle());
                                }
                            }
                        }
                        // Maasto oikealla
                        else if (Peli.maastokenttä[tarkistaOikea][sijY] != null) {
                            if (!Peli.maastokenttä[tarkistaOikea][sijY].estääköLiikkumisen(suunta)) {
                                if (harhaliikkeenTodennäköisyys > Math.random()) {
                                    if (r.nextBoolean()) {
                                        if (Peli.maastokenttä[sijX][tarkistaAlas] != null) {
                                            if (!Peli.maastokenttä[sijX][tarkistaAlas].estääköLiikkumisen(suunta)) {
                                                siirry(new LiikkuminenAlas());
                                            }
                                        }
                                    }
                                    else {
                                        if (Peli.maastokenttä[sijX][tarkistaYlös] != null) {
                                            if (!Peli.maastokenttä[sijX][tarkistaYlös].estääköLiikkumisen(suunta)) {
                                                siirry(new LiikkuminenYlös());
                                            }
                                        }
                                    }
                                }
                                pelaajaSiirtyi = siirry(new LiikkuminenOikealle());
                            }
                        }
                        // Tyhjä oikealla
                        else {
                            if (harhaliikkeenTodennäköisyys > Math.random()) {
                                if (r.nextBoolean()) {
                                    siirry(new LiikkuminenAlas());
                                }
                                else {
                                    siirry(new LiikkuminenYlös());
                                }
                            }
                            pelaajaSiirtyi = siirry(new LiikkuminenOikealle());
                        }
                    }
                break;
                case ALAS:
                    if (hitbox.getMaxY() < Peli.kentänKoko * PeliRuutu.pelaajanKokoPx) {
                        // Visuaalinen objekti alhaalla
                        if (Peli.maastokenttä[sijX][tarkistaAlas] != null && Peli.pelikenttä[sijX][tarkistaAlas] != null) {
                            if (!Peli.maastokenttä[sijX][tarkistaAlas].estääköLiikkumisen(suunta)) {
                                if (Peli.pelikenttä[sijX][tarkistaAlas] instanceof VisuaalinenObjekti) {
                                    VisuaalinenObjekti vo = (VisuaalinenObjekti)Peli.pelikenttä[sijX][tarkistaAlas];
                                    if (!vo.onkoEste()) {
                                        pelaajaSiirtyi = siirry(new LiikkuminenAlas());
                                    }
                                }
                                else if (Peli.pelikenttä[sijX][tarkistaAlas] instanceof AvattavaEste) {
                                    AvattavaEste ae = (AvattavaEste)Peli.pelikenttä[sijX][tarkistaAlas];
                                    if (ae.onkoAvattu()) {
                                        pelaajaSiirtyi = siirry(new LiikkuminenAlas());
                                    }
                                }
                                else {
                                    if (harhaliikkeenTodennäköisyys > Math.random()) {
                                        if (r.nextBoolean()) {
                                            if (!Peli.maastokenttä[tarkistaVasen][sijY].estääköLiikkumisen(suunta)) {
                                                siirry(new LiikkuminenVasemmalle());
                                            }
                                        }
                                        else {
                                            if (!Peli.maastokenttä[tarkistaOikea][sijY].estääköLiikkumisen(suunta)) {
                                                siirry(new LiikkuminenOikealle());
                                            }
                                        }
                                    }
                                    pelaajaSiirtyi = siirry(new LiikkuminenAlas());
                                }
                            }
                        }
                        // Maasto alhaalla
                        else if (Peli.maastokenttä[sijX][tarkistaAlas] != null) {
                            if (!Peli.maastokenttä[sijX][tarkistaAlas].estääköLiikkumisen(suunta)) {
                                if (harhaliikkeenTodennäköisyys > Math.random()) {
                                    if (r.nextBoolean()) {
                                        if (Peli.maastokenttä[tarkistaVasen][sijY] != null) {
                                            if (!Peli.maastokenttä[tarkistaVasen][sijY].estääköLiikkumisen(Suunta.VASEN)) {
                                                siirry(new LiikkuminenVasemmalle());
                                            }
                                        }
                                    }
                                    else {
                                        if (Peli.maastokenttä[tarkistaOikea][sijY] != null) {
                                            if (!Peli.maastokenttä[tarkistaOikea][sijY].estääköLiikkumisen(Suunta.OIKEA)) {
                                                siirry(new LiikkuminenOikealle());
                                            }
                                        }
                                    }
                                }
                                pelaajaSiirtyi = siirry(new LiikkuminenAlas());
                            }
                        }
                        // Tyhjä alhaalla
                        else {
                            if (harhaliikkeenTodennäköisyys > Math.random()) {
                                if (r.nextBoolean()) {
                                    siirry(new LiikkuminenVasemmalle());
                                }
                                else {
                                    siirry(new LiikkuminenOikealle());
                                }
                            }
                            pelaajaSiirtyi = siirry(new LiikkuminenAlas());
                        }
                    }
                break;
                case YLÖS:
                    if (hitbox.getMinY() > 0) {
                        // Visuaalinen objekti ylhäällä
                        if (Peli.maastokenttä[sijX][tarkistaYlös] != null && Peli.pelikenttä[sijX][tarkistaYlös] != null) {
                            if (!Peli.maastokenttä[sijX][tarkistaYlös].estääköLiikkumisen(suunta)) {
                                if (Peli.pelikenttä[sijX][tarkistaYlös] instanceof VisuaalinenObjekti) {
                                    VisuaalinenObjekti vo = (VisuaalinenObjekti)Peli.pelikenttä[sijX][tarkistaYlös];
                                    if (!vo.onkoEste()) {
                                        pelaajaSiirtyi = siirry(new LiikkuminenYlös());
                                    }
                                }
                                else if (Peli.pelikenttä[sijX][tarkistaYlös] instanceof AvattavaEste) {
                                    AvattavaEste ae = (AvattavaEste)Peli.pelikenttä[sijX][tarkistaYlös];
                                    if (ae.onkoAvattu()) {
                                        pelaajaSiirtyi = siirry(new LiikkuminenYlös());
                                    }
                                }
                                else {
                                    if (harhaliikkeenTodennäköisyys > Math.random()) {
                                        if (r.nextBoolean()) {
                                            if (!Peli.maastokenttä[tarkistaVasen][sijY].estääköLiikkumisen(suunta)) {
                                                siirry(new LiikkuminenVasemmalle());
                                            }
                                        }
                                        else {
                                            if (!Peli.maastokenttä[tarkistaOikea][sijY].estääköLiikkumisen(suunta)) {
                                                siirry(new LiikkuminenOikealle());
                                            }
                                        }
                                    }
                                    pelaajaSiirtyi = siirry(new LiikkuminenYlös());
                                }
                            }
                        }
                        // Maasto ylhäällä
                        else if (Peli.maastokenttä[sijX][tarkistaYlös] != null) {
                            if (!Peli.maastokenttä[sijX][tarkistaYlös].estääköLiikkumisen(suunta)) {
                                if (harhaliikkeenTodennäköisyys > Math.random()) {
                                    if (r.nextBoolean()) {
                                        if (Peli.maastokenttä[tarkistaVasen][sijY] != null) {
                                            if (!Peli.maastokenttä[tarkistaVasen][sijY].estääköLiikkumisen(Suunta.VASEN)) {
                                                siirry(new LiikkuminenVasemmalle());
                                            }
                                        }
                                    }
                                    else {
                                        if (Peli.maastokenttä[tarkistaOikea][sijY] != null) {
                                            if (!Peli.maastokenttä[tarkistaOikea][sijY].estääköLiikkumisen(Suunta.OIKEA)) {
                                                siirry(new LiikkuminenOikealle());
                                            }
                                        }
                                    }
                                }
                                pelaajaSiirtyi = siirry(new LiikkuminenYlös());
                            }
                        }
                        // Tyhjä ylhäällä
                        else {
                            if (harhaliikkeenTodennäköisyys > Math.random()) {
                                if (r.nextBoolean()) {
                                    siirry(new LiikkuminenVasemmalle());
                                }
                                else {
                                    siirry(new LiikkuminenOikealle());
                                }
                            }
                            pelaajaSiirtyi = siirry(new LiikkuminenYlös());
                        }
                    }
                break;
                default:
                break;
            }
        }
        catch (NullPointerException npe) {
            System.out.println("Ongelma liikkeessä! Viimeisin pelaajan liike perutaan.");
            npe.printStackTrace();
        }
        catch (ArrayIndexOutOfBoundsException aioobe) {
            System.out.println("Ongelma liikkeessä! Viimeisin pelaajan liike perutaan (kentän ulkopuolella).");
            aioobe.printStackTrace();
        }
        return pelaajaSiirtyi;
    }

    public static void annaEsine(Esine e) {
        if (e != null) {
            if (annaEsineidenMäärä() < annaTavaraluettelonKoko()) {
                for (int i = 0; i < esineet.length; i++) {
                    if (esineet[i] == null) {
                        esineet[i] = e;
                        break;
                    }
                }
                PääIkkuna.hudTeksti.setText("Sait uuden " + e.annaNimiSijamuodossa("genetiivi"));
            }
            else {
                PääIkkuna.hudTeksti.setText("Ei voida poimia! Tavaraluettelo täynnä! Kokeile pudottaa jokin esine tyhjään ruutuun");
            }
            Peli.valittuEsine = esineet[Peli.esineValInt];
        }
    }

    public static KeimonState keimonState = KeimonState.IDLE;
    public enum KeimonState {
        IDLE,
        JUOKSU,
        KUOLLUT;

        @Override
        public String toString() {
            char x = this.name().charAt(0);
            String uusiNimi = x + this.name().substring(1).toLowerCase();
            return uusiNimi;
        }
    }

    public static KeimonKylläisyys keimonKylläisyys = KeimonKylläisyys.LAIHA;
    public enum KeimonKylläisyys {
        LAIHA,
        NORMAALI,
        LIHAVA,
        ERITTÄIN_LIHAVA,
        YLENSYÖNTI;

        @Override
        public String toString() {
            char x = this.name().charAt(0);
            String uusiNimi = x + this.name().substring(1).toLowerCase();
            if (uusiNimi.contains("_")) {
                uusiNimi.replace("_", " ");
            }
            return uusiNimi;
        }
    }

    public static KeimonTerveys keimonTerveys = KeimonTerveys.OK;
    public enum KeimonTerveys {
        HUONO,
        OK,
        HYVÄ,
        ÜBER;

        @Override
        public String toString() {
            char x = this.name().charAt(0);
            String uusiNimi = x + this.name().substring(1).toLowerCase();
            return uusiNimi;
        }
    }

    public static Suunta keimonSuunta = Suunta.ALAS;

    public static SuuntaVasenOikea keimonSuuntaVasenOikea = SuuntaVasenOikea.OIKEA;
    public enum SuuntaVasenOikea {
        VASEN,
        OIKEA;

        @Override
        public String toString() {
            char x = this.name().charAt(0);
            String uusiNimi = x + this.name().substring(1).toLowerCase();
            return uusiNimi;
        }
    }
    /**
     * Pakottaa pelaaja pysähtymään. Hyödyllinen tapauksissa, joissa näppäimet voivat jäädä jumiin.
     */
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
    /**
     * Tarkista, onko pelaajan liikkumiselle esteitä, esim. huoneen lataus.
     * Jos ei, tarkista, onko liikkumisnäppäimet painettuna.
     * Jos on, tarkista, voiko pelaaja liikkua painallusten suuntiin.
     * @return liikkuiko pelaaja
     */
    static boolean liikutaPelaajaa() {
        boolean pelaajaLiikkui = false;
        if (!GrafiikanPäivitysSäie.huoneenGrafiikanLatausKäynnissä) {
            if (pelaajaLiikkuuVasen) {
                kokeileLiikkumista(Suunta.VASEN);
                pelaajaLiikkui = true;
            }
            if (pelaajaLiikkuuOikea) {
                kokeileLiikkumista(Suunta.OIKEA);
                pelaajaLiikkui = true;
            }
            if (pelaajaLiikkuuYlös) {
                kokeileLiikkumista(Suunta.YLÖS);
                pelaajaLiikkui = true;
            }
            if (pelaajaLiikkuuAlas) {
                kokeileLiikkumista(Suunta.ALAS);
                pelaajaLiikkui = true;
            }
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
            case null, default:
                pelaajaLiikkuuVasen = false;
                pelaajaLiikkuuOikea = false;
                pelaajaLiikkuuYlös = false;
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

    public static void teleport(int kohdeX, int kohdeY) {
        sijX = kohdeX;
        sijY = kohdeY;
        hitbox.setLocation(sijX * PeliRuutu.pelaajanKokoPx, sijY * PeliRuutu.pelaajanKokoPx);
        if (PeliRuutu.ylätekstiSij != null) {
            PeliRuutu.ylätekstiSij.setText("Pelaaja siirrettiin sijaintiin " + sijX + ", " + sijY + " (" + hitbox.getMinX() + "-" + hitbox.getMaxX() + ", " + hitbox.getMinY() + "-" + hitbox.getMaxY() + ")");
        }
    }

    public static void teleporttaaLähimpäänTurvalliseenKohtaan(int alkuX, int alkuY) {
        boolean kenttäTurvallinen = false;
        boolean maastoTurvallinen = false;
        for (int y = alkuY; y < Peli.kentänKoko; y++) {
            for (int x = alkuX; x < Peli.kentänKoko; x++) {
                if (Peli.maastokenttä[x][y] instanceof EsteTile) {
                    maastoTurvallinen = false;
                }
                else {
                    maastoTurvallinen = true;
                }
                if (Peli.pelikenttä[x][y] instanceof VisuaalinenObjekti) {
                    VisuaalinenObjekti vo = (VisuaalinenObjekti)Peli.pelikenttä[x][y];
                    if (vo.onkoEste()) {
                        kenttäTurvallinen = false;
                    }
                    else {
                        kenttäTurvallinen = true;
                    }
                }
                else {
                    kenttäTurvallinen = true;
                }

                if (kenttäTurvallinen && maastoTurvallinen) {
                    teleport(x, y);
                    return;
                }
            }
        }
        if (alkuX == 0 && alkuY == 0) {
            PääIkkuna.avaaDialogi(null, "Huoneessa ei ole turvallisia ruutuja", "Turvallinen teleporttaus epäonnistui");
        }
        else {
            teleporttaaLähimpäänTurvalliseenKohtaan(0, 0);
        }
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
        päivitäTerveys();
    }

    void paranna(int määrä) {
        hp += määrä;
        päivitäTerveys();
    }

    public static void päivitäTerveys() {
        if (hp >= 30) {
            keimonTerveys = KeimonTerveys.ÜBER;
        }
        else if (hp >= 20) {
            keimonTerveys = KeimonTerveys.HYVÄ;
        }
        else if (hp < 6) {
            keimonTerveys = KeimonTerveys.HUONO;
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

    public static void vähennäHyökkäysAikaa() {
        if (hyökkäysAika > 0) {
            hyökkäysAika--;
        }
        if (hyökkäysViive > 0) {
            hyökkäysViive--;
        }
    }

    public static String lisääOstosKoriin(Esine e) {
        int tyhjätPaikat = 0;
        for (Esine esine : esineet) {
            if (esine == null) {
                tyhjätPaikat++;
            }
        }
        if (ostosKori.size() >= tyhjätPaikat) {
            PääIkkuna.avaaDialogi(null, "Ostoskoriin ei voi lisätä enempää tavaraa kuin tavaraluettelossa on tyhjiä paikkoja!", "Kauppahylly");
            return "Ostoskoriin ei voi lisätä enempää tavaraa kuin tavaraluettelossa on tyhjiä paikkoja!";
        }
        else if (e != null) {
            ostosKori.add(e);
            PeliRuutu.päivitäOstosPaneli();
            nopeus = Math.round((8 - Pelaaja.ostosKori.size()) * PeliRuutu.pelaajanKokoPx / 64f);
            PääIkkuna.avaaDialogi(e.annaKuvake(), "Ostoskoriin lisättiin " + e.annaNimi(), "Kauppahylly");
            return "Ostoskoriin lisättiin " + e.annaNimi() + " (+ " + e.annaHinta() + "€)";
        }
        else {
            return "tyhjä hylly";
        }
    }

    public static void tyhjennäOstoskori() {
        ostosKori.removeAll(ostosKori);
        PeliRuutu.päivitäOstosPaneli();
        nopeus = Math.round((8 - Pelaaja.ostosKori.size()) * PeliRuutu.pelaajanKokoPx / 64f);
    }

    Pelaaja() {
        hp = Peli.aloitusHp;
        raha = 0;
        kuparit = 0;
        syödytRuoat = 0;
        nopeus = 8;
        kuvake = new ImageIcon("tiedostot/kuvat/keimo_idle.gif");
        vilkkuvaKuvake = new SkaalattavaKuvake("tiedostot/kuvat/keimo_idle.gif", 0.05f);
        keimonState = KeimonState.IDLE;
        keimonKylläisyys = KeimonKylläisyys.LAIHA;
        keimonTerveys = KeimonTerveys.OK;
        sijX = 13;
        sijY = 13;
        //hitbox.setLocation(sijX * PeliRuutu.pelaajanKokoPx +10, sijY * PeliRuutu.pelaajanKokoPx +10);
        pelaajaLiikkuuVasen = false;
        pelaajaLiikkuuOikea = false;
        pelaajaLiikkuuAlas = false;
        pelaajaLiikkuuYlös = false;
        kuolemattomuusAika = 0;
        reaktioAika = 0;
        känninVoimakkuusFloat = 0;
        ostostenHintaYhteensä = 0;
        ostosKori.removeAll(ostosKori);
    }
}
