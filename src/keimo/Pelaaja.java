package keimo;
import javax.swing.ImageIcon;
import javax.swing.Icon;

import keimo.Kenttäkohteet.Esine;

public class Pelaaja {
    
    static Esine[] esineet = new Esine[5];
    static int sijX;
    static int sijY;
    protected static int hp;
    boolean kylläinen = false;
    int syödytRuoat = 0;
    protected Icon kuvake;
    static int pelaajanKylläisyys = 0;

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

    void syöRuoka(int parannus) {
        this.paranna(parannus);
        this.kylläinen = true;
        this.syödytRuoat++;
        pelaajanKylläisyys = syödytRuoat;
        switch (syödytRuoat) {
            case 0:
                this.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja.png");
                break;
            case 1:
                this.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja_1.png");
                break;
            case 2:
                this.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja_2.png");
                break;
            case 3:
                this.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja_3.png");
                break;
            case 4:
                this.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja_4.png");
                break;
            default:
                this.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja.png");
                break;
        }
    }

    void siirry(String suunta) {

        switch (suunta) {
            case "vasen":
                if (sijX > 0) {
                    sijX--;
                }
                break;
            case "oikea":
                if (sijX < Peli.kentänKoko -1) {
                    sijX++;
                }
                break;
            case "ylös":
                if (sijY < Peli.kentänKoko -1) {
                    sijY++;
                }
                break;
            case "alas":
                if (sijY > 0) {
                    sijY--;
                }
                break;
            default: break;
        }
    }

    boolean kokeileLiikkumista(String suunta) {
        boolean pelaajaSiirtyi = false;
        switch (suunta) {
            case "vasen":
                if (sijX > 0) {
                    if (Peli.maastokenttä[sijX-1][sijY] == null) {
                        pelaajaSiirtyi = siirry(new LiikkuminenVasemmalle());
                    }
                    else {
                        if (!Peli.maastokenttä[sijX-1][sijY].estääköLiikkumisen()) {
                            pelaajaSiirtyi = siirry(new LiikkuminenVasemmalle());
                        }
                    }
                }
                break;
            case "oikea":
                if (sijX < Peli.kentänKoko -1) {
                    if (Peli.maastokenttä[sijX+1][sijY] == null) {
                        pelaajaSiirtyi = siirry(new LiikkuminenOikealle());
                    }
                    else {
                        if (!Peli.maastokenttä[sijX+1][sijY].estääköLiikkumisen()) {
                            pelaajaSiirtyi = siirry(new LiikkuminenOikealle());
                        }
                    }
                }
                break;
            case "alas":
                if (sijY < Peli.kentänKoko -1) {
                    if (Peli.maastokenttä[sijX][sijY+1] == null) {
                        pelaajaSiirtyi = siirry(new LiikkuminenAlas());
                    }
                    else {
                        if (!Peli.maastokenttä[sijX][sijY+1].estääköLiikkumisen()) {
                            pelaajaSiirtyi = siirry(new LiikkuminenAlas());
                        }
                    }
                }
                break;
            case "ylös":
                if (sijY > 0) {
                    if (Peli.maastokenttä[sijX][sijY-1] == null) {
                        pelaajaSiirtyi = siirry(new LiikkuminenYlös());
                    }
                    else {
                        if (!Peli.maastokenttä[sijX][sijY-1].estääköLiikkumisen()) {
                            pelaajaSiirtyi = siirry(new LiikkuminenYlös());
                        }
                    }
                }
                break;
            default:
                break;
        }
        return pelaajaSiirtyi;
    }

    boolean siirry (Liikkuminen liikkuminen) {
        boolean pelaajaSiirtyi = false;
        if (liikkuminen instanceof LiikkuminenVasemmalle) {
            if (sijX > Peli.kentänAlaraja) {
                sijX--;
                pelaajaSiirtyi = true;
            }
        }
        else if (liikkuminen instanceof LiikkuminenOikealle) {
            if (sijX < Peli.kentänYläraja) {
                sijX++;
                pelaajaSiirtyi = true;
            }
        }
        else if (liikkuminen instanceof LiikkuminenYlös) {
            if (sijY > Peli.kentänAlaraja) {
                sijY--;
                pelaajaSiirtyi = true;
            }
        }
        else if (liikkuminen instanceof LiikkuminenAlas) {
            if (sijY < Peli.kentänYläraja) {
                sijY++;
                pelaajaSiirtyi = true;
            }
        }
        return pelaajaSiirtyi;
    }

    void teleport(int kohdeX, int kohdeY) {
        sijX = kohdeX;
        sijY = kohdeY;
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
        ÄänentoistamisSäie.toistaSFX("damage");
        PääIkkuna.ylätekstiHP.setText("HP: " + hp);
    }

    void paranna(int määrä) {
        this.hp += määrä;
        PääIkkuna.ylätekstiHP.setText("HP: " + hp);
    }

    Pelaaja() {
        this.hp = Peli.aloitusHp;
        this.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja.png");
        sijX = 0;
        sijY = 0;
        pelaajanKylläisyys = syödytRuoat;
    }
}
