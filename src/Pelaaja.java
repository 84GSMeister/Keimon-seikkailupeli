import javax.swing.ImageIcon;
import javax.swing.Icon;

public class Pelaaja {
    
    static Esine[] esineet = new Esine[5];
    int sijX;
    int sijY;
    protected int hp;
    boolean kylläinen = false;
    int syödytRuoat = 0;
    protected Icon kuvake;

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
        Main.pelaajanKylläisyys = syödytRuoat;
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
                if (sijX < Main.kentänKoko -1) {
                    sijX++;
                }
                break;
            case "ylös":
                if (sijY < Main.kentänKoko -1) {
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

    void siirry (Liikkuminen liikkuminen) {
        if (liikkuminen instanceof LiikkuminenVasemmalle) {
            if (sijX > Main.kentänAlaraja) {
                sijX--;
            }
        }
        else if (liikkuminen instanceof LiikkuminenOikealle) {
            if (sijX < Main.kentänYläraja) {
                sijX++;
            }
        }
        else if (liikkuminen instanceof LiikkuminenYlös) {
            if (sijY > Main.kentänAlaraja) {
                sijY--;
            }
        }
        else if (liikkuminen instanceof LiikkuminenAlas) {
            if (sijY < Main.kentänYläraja) {
                sijY++;
            }
        }
    }

    void teleport(int kohdeX, int kohdeY) {
        sijX = kohdeX;
        sijY = kohdeY;
        Main.pelaajanSijX = sijX;
        Main.pelaajanSijY = sijY;
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

    void vahingoita(int määrä) {
        this.hp -= määrä;
        PääIkkuna.ylätekstiHP.setText("HP: " + hp);
    }

    void paranna(int määrä) {
        this.hp += määrä;
        PääIkkuna.ylätekstiHP.setText("HP: " + hp);
    }

    Pelaaja() {
        this.hp = Main.aloitusHp;
        this.kuvake = new ImageIcon("tiedostot/kuvat/pelaaja.png");
        this.sijX = 0;
        this.sijY = 0;
        Main.pelaajanSijX = this.sijX;
        Main.pelaajanSijY = this.sijY;
        Main.pelaajanKylläisyys = syödytRuoat;
    }
}
