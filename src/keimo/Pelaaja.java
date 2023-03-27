package keimo;
import javax.swing.*;

import java.awt.Rectangle;
import keimo.Kenttäkohteet.Esine;
import keimo.NPCt.*;

public class Pelaaja {
    
    static Esine[] esineet = new Esine[5];
    static int valittuEsine = 0;
    static int sijX;
    static int sijY;
    //static int sijX_PX_vy, sijX_PX_oa;
    //static int sijY_PX_vy, sijY_PX_oa;
    static Rectangle hitbox = new Rectangle(0, 0, PääIkkuna.pelaajanKokoPx, PääIkkuna.pelaajanKokoPx);
    public static int hp;
    static boolean kylläinen = false;
    static int syödytRuoat = 0;
    protected static ImageIcon kuvake;
    static int kuolemattomuusAika = 0;
    static int reaktioAika = 0;
    static boolean vihollisenKohdalla = false;
    static Vihollinen vihollinenKohdalla;

    // boolean päivitäPelaajanSijainti(String sij) {
    //     int sijXInt;
    //     int sijYInt;
    //     boolean pelaajaSiirrettiin = false;


    //     try {
    //         String sijXString = sij.substring(0, sij.indexOf(","));
    //         String sijYString = sij.substring(sij.indexOf(",") + 1);

    //         sijXInt = Integer.parseInt(sijXString);
    //         sijYInt = Integer.parseInt(sijYString);

    //         if (sijXInt < 0 || sijXInt > 9 || sijYInt < 0 || sijYInt > 9) {
    //             System.out.println("Kentän ulkopuolella! Sallitut arvot ovat 0-9");
    //             pelaajaSiirrettiin = false;
    //         }
    //         else {
    //             this.sijX = sijXInt;
    //             this.sijY = sijYInt;
    //             System.out.println("Pelaaja siirrettiin sijaintiin (" + sijX + ", " + sijY + ")");
    //             pelaajaSiirrettiin = true;
    //         }
            
    //     }
    //     catch (NumberFormatException e) {
    //         System.out.println("Virheellinen syöte. Syötä x- ja y-koordinaatit pilkulla erotettuina ilman välilyöntiä.");
    //         pelaajaSiirrettiin = false;
    //     }
    //     catch (NullPointerException e) {
    //         System.out.println("Virheellinen syöte. Syötä x- ja y-koordinaatit pilkulla erotettuina ilman välilyöntiä.");
    //         pelaajaSiirrettiin = false;
    //     }
    //     catch (StringIndexOutOfBoundsException e) {
    //         System.out.println("Virheellinen syöte. Syötä x- ja y-koordinaatit pilkulla erotettuina ilman välilyöntiä.");
    //         pelaajaSiirrettiin = false;
    //     }
    //     return pelaajaSiirrettiin;
    // }

    /**
     * Valitse tila, jonka mukaan kuvake valitaan grafiikkasäikeessä sekä
     * @param parannus kasvata hp:ta
     */

    void syöRuoka(int parannus) {
        this.paranna(parannus);
        this.kylläinen = true;
        this.syödytRuoat++;
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
            if ((int)hitbox.getMaxX() < Peli.kentänKoko * PääIkkuna.pelaajanKokoPx) {
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
            if ((int)hitbox.getMaxY() < Peli.kentänKoko * PääIkkuna.pelaajanKokoPx) {
                hitbox.setLocation((int)hitbox.getMinX(), (int)hitbox.getMinY() + 8);
                pelaajaSiirtyi = true;
            }
        }
        sijX = (int)hitbox.getCenterX() / PääIkkuna.pelaajanKokoPx;
        sijY = (int)hitbox.getCenterY() / PääIkkuna.pelaajanKokoPx;
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
                    if (hitbox.getMinX() > 0) {
                        if (Peli.maastokenttä[(int)hitbox.getMaxX()/64 -1][sijY] == null) {
                            pelaajaSiirtyi = siirry(new LiikkuminenVasemmalle());
                        }
                        else {
                            if (!Peli.maastokenttä[(int)hitbox.getMaxX()/64 -1][sijY].estääköLiikkumisen()) {
                                pelaajaSiirtyi = siirry(new LiikkuminenVasemmalle());
                            }
                        }
                    }
                    break;
                case "oikea":   
                    if (hitbox.getMaxX() < Peli.kentänKoko * PääIkkuna.pelaajanKokoPx) {
                        if (Peli.maastokenttä[(int)hitbox.getMinX()/64 +1][sijY] == null) {
                            pelaajaSiirtyi = siirry(new LiikkuminenOikealle());
                        }
                        else {
                            if (!Peli.maastokenttä[(int)hitbox.getMinX()/64 +1][sijY].estääköLiikkumisen()) {
                                pelaajaSiirtyi = siirry(new LiikkuminenOikealle());
                            }
                        }
                    }
                    break;
                case "alas":
                    if (hitbox.getMaxY() < Peli.kentänKoko * PääIkkuna.pelaajanKokoPx) {
                        if (Peli.maastokenttä[sijX][(int)hitbox.getMinY()/64 +1] == null) {
                            pelaajaSiirtyi = siirry(new LiikkuminenAlas());
                        }
                        else {
                            if (!Peli.maastokenttä[sijX][(int)hitbox.getMinY()/64 +1].estääköLiikkumisen()) {
                                pelaajaSiirtyi = siirry(new LiikkuminenAlas());
                            }
                        }
                    }
                    break;
                case "ylös":
                    if (hitbox.getMinY() > 0) {
                        if (Peli.maastokenttä[sijX][(int)hitbox.getMaxY()/64 -1] == null) {
                            pelaajaSiirtyi = siirry(new LiikkuminenYlös());
                        }
                        else {
                            if (!Peli.maastokenttä[sijX][(int)hitbox.getMaxY()/64 -1].estääköLiikkumisen()) {
                                pelaajaSiirtyi = siirry(new LiikkuminenYlös());
                            }
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

    static KeimonState keimonState = KeimonState.IDLE;
    enum KeimonState {
        IDLE,
        JUOKSU,
        KUOLLUT;
    }

    static KeimonKylläisyys keimonKylläisyys = KeimonKylläisyys.LAIHA;
    enum KeimonKylläisyys {
        LAIHA,
        NORMAALI,
        LIHAVA,
        ERITTÄIN_LIHAVA,
        YLENSYÖNTI;
    }

    static KeimonTerveys keimonTerveys = KeimonTerveys.OK;
    enum KeimonTerveys {
        HUONO,
        OK,
        HYVÄ,
        ÜBER;
    }

    static Suunta keimonSuunta = Suunta.ALAS;
    enum Suunta {
        VASEN,
        OIKEA,
        ALAS,
        YLÖS;
    }

    static SuuntaVasenOikea keimonSuuntaVasenOikea = SuuntaVasenOikea.OIKEA;
    enum SuuntaVasenOikea {
        VASEN,
        OIKEA;
    }
    
    static boolean pelaajaLiikkuuVasen = false;
    static boolean pelaajaLiikkuuOikea = false;
    static boolean pelaajaLiikkuuYlös = false;
    static boolean pelaajaLiikkuuAlas = false;
    static String liikeSuunta = "";
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
        hitbox.setLocation(kohdeX * PääIkkuna.pelaajanKokoPx, kohdeY * PääIkkuna.pelaajanKokoPx);
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
        hitbox.setLocation(sijX * PääIkkuna.pelaajanKokoPx, sijY * PääIkkuna.pelaajanKokoPx);
        PääIkkuna.ylätekstiSij.setText("Pelaaja siirrettiin sijaintiin " + sijX + ", " + sijY + " (" + hitbox.getMinX() + "-" + hitbox.getMaxX() + ", " + hitbox.getMinY() + "-" + hitbox.getMaxY() + ")");
        //while ((int)hitbox.getMinX() != kohdeX * PääIkkuna.pelaajanKokoPx || (int)hitbox.getMinY() != kohdeY * PääIkkuna.pelaajanKokoPx) {
        //    System.out.println((int)hitbox.getMinX() + " " + sijX * PääIkkuna.pelaajanKokoPx + " " + (int)hitbox.getMinY() + " " + sijY * PääIkkuna.pelaajanKokoPx);
        //    päivitäHitboxPositio(kohdeX, kohdeY);
        //}
    }

    static int annaEsineidenMäärä() {
        int määrä = 0;
        for (int i = 0; i < esineet.length; i++) {
            if (esineet[i] != null) {
                määrä++;
            }
        }
        return määrä;
    }
    static int annaTavaraluettelonKoko() {
        return esineet.length;
    }

    int annaHp() {
        return hp;
    }

    static void vahingoita(int määrä) {
        hp -= määrä;
        kuolemattomuusAika = 60;
        ÄänentoistamisSäie.toistaSFX("pelaaja_damage");
        PääIkkuna.ylätekstiHP.setText("HP: " + hp);
        päivitäTerveys();
    }

    static void paranna(int määrä) {
        hp += määrä;
        PääIkkuna.ylätekstiHP.setText("HP: " + hp);
        päivitäTerveys();
    }

    static void päivitäTerveys() {
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
        this.hp = Peli.aloitusHp;
        this.syödytRuoat = 0;
        this.kuvake = new ImageIcon("tiedostot/kuvat/keimo_idle.gif");
        this.keimonState = KeimonState.IDLE;
        this.keimonKylläisyys = KeimonKylläisyys.LAIHA;
        this.keimonTerveys = KeimonTerveys.OK;
        sijX = 0;
        sijY = 0;
        hitbox.setLocation(0, 0);
    }
}
