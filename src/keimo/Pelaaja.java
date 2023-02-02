package keimo;
import javax.swing.*;

import java.awt.Rectangle;
import keimo.Kenttäkohteet.Esine;
import keimo.NPCt.*;

public class Pelaaja {
    
    static Esine[] esineet = new Esine[5];
    static int sijX;
    static int sijY;
    static int sijX_PX_vy, sijX_PX_oa;
    static int sijY_PX_vy, sijY_PX_oa;
    static Rectangle hitbox = new Rectangle(sijX_PX_vy, sijY_PX_vy, PääIkkuna.pelaajanKokoPx, PääIkkuna.pelaajanKokoPx);
    protected static int hp;
    static boolean kylläinen = false;
    static int syödytRuoat = 0;
    protected static ImageIcon kuvake;
    static int pelaajanKylläisyys = 0;
    static int kuolemattomuusAika = 0;
    static int reaktioAika = 0;
    static boolean vihollisenKohdalla = false;
    static Vihollinen vihollinenKohdalla;

    boolean päivitäPelaajanSijainti(String sij) {
        int sijXInt;
        int sijYInt;
        boolean pelaajaSiirrettiin = false;


        try {
            String sijXString = sij.substring(0, sij.indexOf(","));
            String sijYString = sij.substring(sij.indexOf(",") + 1);

            sijXInt = Integer.parseInt(sijXString);
            sijYInt = Integer.parseInt(sijYString);

            if (sijXInt < 0 || sijXInt > 9 || sijYInt < 0 || sijYInt > 9) {
                System.out.println("Kentän ulkopuolella! Sallitut arvot ovat 0-9");
                pelaajaSiirrettiin = false;
            }
            else {
                this.sijX = sijXInt;
                this.sijY = sijYInt;
                System.out.println("Pelaaja siirrettiin sijaintiin (" + sijX + ", " + sijY + ")");
                pelaajaSiirrettiin = true;
            }
            
        }
        catch (NumberFormatException e) {
            System.out.println("Virheellinen syöte. Syötä x- ja y-koordinaatit pilkulla erotettuina ilman välilyöntiä.");
            pelaajaSiirrettiin = false;
        }
        catch (NullPointerException e) {
            System.out.println("Virheellinen syöte. Syötä x- ja y-koordinaatit pilkulla erotettuina ilman välilyöntiä.");
            pelaajaSiirrettiin = false;
        }
        catch (StringIndexOutOfBoundsException e) {
            System.out.println("Virheellinen syöte. Syötä x- ja y-koordinaatit pilkulla erotettuina ilman välilyöntiä.");
            pelaajaSiirrettiin = false;
        }
        return pelaajaSiirrettiin;
    }

    enum Suunta {
        VASEN,
        OIKEA,
        ALAS,
        YLÖS;
    }

    void syöRuoka(int parannus) {
        this.paranna(parannus);
        this.kylläinen = true;
        this.syödytRuoat++;
        pelaajanKylläisyys = syödytRuoat;
        switch (syödytRuoat) {
            case 0:
                keimonKylläisyys = KeimonKylläisyys.LIHAVUUS_0;
                this.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja.png");
                break;
            case 1:
                keimonKylläisyys = KeimonKylläisyys.LIHAVUUS_1;
                this.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja_1.png");
                break;
            case 2:
                keimonKylläisyys = KeimonKylläisyys.LIHAVUUS_2;
                this.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja_2.png");
                break;
            case 3:
                keimonKylläisyys = KeimonKylläisyys.LIHAVUUS_3;
                this.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja_3.png");
                break;
            case 4:
                keimonKylläisyys = KeimonKylläisyys.YLENSYÖNTI;
                this.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja_4.png");
                break;
            default:
                keimonKylläisyys = KeimonKylläisyys.LIHAVUUS_0;
                this.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja.png");
                break;
        }
    }

    static boolean siirry(String suunta) {
        boolean pelaajaSiirtyi = false;
        switch (suunta) {
            case "vasen":
                // if (sijX > 0) {
                //     sijX--;
                // }
                if (sijX_PX_vy > Peli.kentänAlaraja) {
                    sijX_PX_vy -= 8;
                    sijX_PX_oa = sijX_PX_vy + PääIkkuna.pelaajanKokoPx;
                    hitbox.setLocation(sijX_PX_vy, sijY_PX_vy);
                    pelaajaSiirtyi = true;
                }
                break;
            case "oikea":
                // if (sijX < Peli.kentänKoko -1) {
                //     sijX++;
                // }
                if (sijX_PX_oa < Peli.kentänKoko * PääIkkuna.pelaajanKokoPx) {
                    sijX_PX_vy += 8;
                    sijX_PX_oa = sijX_PX_vy + PääIkkuna.pelaajanKokoPx;
                    hitbox.setLocation(sijX_PX_vy, sijY_PX_vy);
                    pelaajaSiirtyi = true;
                }
                break;
            case "ylös":
                // if (sijY < Peli.kentänKoko -1) {
                //     sijY++;
                // }
                if (sijY_PX_vy > Peli.kentänAlaraja) {
                    sijY_PX_vy -= 8;
                    sijY_PX_oa = sijY_PX_vy + PääIkkuna.pelaajanKokoPx;
                    hitbox.setLocation(sijX_PX_vy, sijY_PX_vy);
                    pelaajaSiirtyi = true;
                }
                break;
            case "alas":
                // if (sijY > 0) {
                //     sijY--;
                // }
                if (sijY_PX_oa < Peli.kentänKoko * PääIkkuna.pelaajanKokoPx) {
                    sijY_PX_vy += 8;
                    sijY_PX_oa = sijY_PX_vy + PääIkkuna.pelaajanKokoPx;
                    hitbox.setLocation(sijX_PX_vy, sijY_PX_vy);
                    pelaajaSiirtyi = true;
                }
                break;
            default:
                return false;
        }
        sijX = ((sijX_PX_vy + sijX_PX_oa) /2) / PääIkkuna.pelaajanKokoPx;
        sijY = ((sijY_PX_vy + sijY_PX_oa) /2) / PääIkkuna.pelaajanKokoPx;
        return pelaajaSiirtyi;
    }

    static boolean kokeileLiikkumista(String suunta) {
        boolean pelaajaSiirtyi = false;
        try {
            switch (suunta) {
                case "vasen":
                    if (sijX_PX_vy > 0) {
                        if (Peli.maastokenttä[sijX_PX_oa/64 -1][sijY] == null) {
                            //pelaajaSiirtyi = siirry(new LiikkuminenVasemmalle());
                            pelaajaSiirtyi = siirry("vasen");
                        }
                        else {
                            if (!Peli.maastokenttä[sijX_PX_oa/64 -1][sijY].estääköLiikkumisen()) {
                                //pelaajaSiirtyi = siirry(new LiikkuminenVasemmalle());
                                pelaajaSiirtyi = siirry("vasen");
                            }
                        }
                    }
                    break;
                case "oikea":
                    if (sijX_PX_oa < Peli.kentänKoko * PääIkkuna.pelaajanKokoPx) {
                        if (Peli.maastokenttä[sijX_PX_vy/64 +1][sijY] == null) {
                            //pelaajaSiirtyi = siirry(new LiikkuminenOikealle());
                            pelaajaSiirtyi = siirry("oikea");
                        }
                        else {
                            if (!Peli.maastokenttä[sijX_PX_vy/64 +1][sijY].estääköLiikkumisen()) {
                                //pelaajaSiirtyi = siirry(new LiikkuminenOikealle());
                                pelaajaSiirtyi = siirry("oikea");
                            }
                        }
                    }
                    break;
                case "alas":
                    if (sijY_PX_oa < Peli.kentänKoko * PääIkkuna.pelaajanKokoPx) {
                        if (Peli.maastokenttä[sijX][sijY_PX_vy/64 +1] == null) {
                            //pelaajaSiirtyi = siirry(new LiikkuminenAlas());
                            pelaajaSiirtyi = siirry("alas");
                        }
                        else {
                            if (!Peli.maastokenttä[sijX][sijY_PX_vy/64 +1].estääköLiikkumisen()) {
                                //pelaajaSiirtyi = siirry(new LiikkuminenAlas());
                                pelaajaSiirtyi = siirry("alas");
                            }
                        }
                    }
                    break;
                case "ylös":
                    if (sijY_PX_vy > 0) {
                        if (Peli.maastokenttä[sijX][sijY_PX_oa/64 -1] == null) {
                            //pelaajaSiirtyi = siirry(new LiikkuminenYlös());
                            pelaajaSiirtyi = siirry("ylös");
                        }
                        else {
                            if (!Peli.maastokenttä[sijX][sijY_PX_oa/64 -1].estääköLiikkumisen()) {
                                //pelaajaSiirtyi = siirry(new LiikkuminenYlös());
                                pelaajaSiirtyi = siirry("ylös");
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

    static KeimonState keimonState = KeimonState.IDLE;
    enum KeimonState {
        IDLE,
        JUOKSU,
        KUOLLUT;
    }

    static KeimonKylläisyys keimonKylläisyys = KeimonKylläisyys.LIHAVUUS_0;
    enum KeimonKylläisyys {
        LIHAVUUS_0,
        LIHAVUUS_1,
        LIHAVUUS_2,
        LIHAVUUS_3,
        YLENSYÖNTI;
    }

    static KeimonTerveys keimonTerveys = KeimonTerveys.OK;
    enum KeimonTerveys {
        HYVÄ,
        OK,
        HUONO;
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
                break;
            case OIKEA:
                pelaajaLiikkuuOikea = true;
                break;
            case YLÖS:
                pelaajaLiikkuuYlös = true;
                break;
            case ALAS:
                pelaajaLiikkuuAlas = true;
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
        keimonState = KeimonState.IDLE;

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
    }

    // boolean siirry (Liikkuminen liikkuminen) {
    //     boolean pelaajaSiirtyi = false;
    //     if (liikkuminen instanceof LiikkuminenVasemmalle) {
    //         // if (sijX > Peli.kentänAlaraja) {
    //         //     sijX--;
    //         //     pelaajaSiirtyi = true;
    //         // }
    //         if (sijX_PX > Peli.kentänAlaraja) {
    //             sijX_PX--;
    //             pelaajaSiirtyi = true;
    //         }
    //     }
    //     else if (liikkuminen instanceof LiikkuminenOikealle) {
    //         // if (sijX < Peli.kentänYläraja) {
    //         //     sijX++;
    //         //     pelaajaSiirtyi = true;
    //         // }
    //         if (sijX_PX < Peli.kentänKoko * PääIkkuna.pelaajanKokoPx) {
    //             sijX_PX++;
    //             pelaajaSiirtyi = true;
    //         }
    //     }
    //     else if (liikkuminen instanceof LiikkuminenYlös) {
    //         // if (sijY > Peli.kentänAlaraja) {
    //         //     sijY--;
    //         //     pelaajaSiirtyi = true;
    //         // }
    //         if (sijY_PX > Peli.kentänAlaraja) {
    //             sijY_PX--;
    //             pelaajaSiirtyi = true;
    //         }
    //     }
    //     else if (liikkuminen instanceof LiikkuminenAlas) {
    //         // if (sijY < Peli.kentänYläraja) {
    //         //     sijY++;
    //         //     pelaajaSiirtyi = true;
    //         // }
    //         if (sijY_PX < Peli.kentänKoko * PääIkkuna.pelaajanKokoPx) {
    //             sijY_PX++;
    //             pelaajaSiirtyi = true;
    //         }
    //     }
    //     sijX = sijX_PX / 64;
    //     sijY = sijY_PX / 64;
    //     return pelaajaSiirtyi;
    // }

    void teleport(int kohdeX, int kohdeY) {
        sijX = kohdeX;
        sijY = kohdeY;
        sijX_PX_vy = kohdeX * PääIkkuna.pelaajanKokoPx;
        sijX_PX_oa = sijX_PX_vy + PääIkkuna.pelaajanKokoPx;
        sijY_PX_vy = kohdeY * PääIkkuna.pelaajanKokoPx;
        sijY_PX_oa = sijY_PX_vy + PääIkkuna.pelaajanKokoPx;
    }

    int annaEsineidenMäärä() {
        int määrä = 0;
        for (int i = 0; i < esineet.length; i++) {
            if (esineet[i] != null) {
                määrä++;
            }
        }
        return määrä;
    }
    int annaTavaraluettelonKoko() {
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
    }

    void paranna(int määrä) {
        this.hp += määrä;
        PääIkkuna.ylätekstiHP.setText("HP: " + hp);
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
        this.keimonKylläisyys = KeimonKylläisyys.LIHAVUUS_0;
        this.keimonTerveys = KeimonTerveys.OK;
        sijX = 0;
        sijY = 0;
        sijX_PX_vy = 0;
        sijX_PX_oa = sijX_PX_vy + PääIkkuna.pelaajanKokoPx;
        sijY_PX_vy = 0;
        sijY_PX_oa = sijY_PX_vy + PääIkkuna.pelaajanKokoPx;
        pelaajanKylläisyys = syödytRuoat;
    }
}
