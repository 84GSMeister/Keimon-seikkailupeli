package keimo.NPCt;
import keimo.Peli;
import keimo.PääIkkuna;
import keimo.TarkistettavatArvot;
import keimo.ÄänentoistamisSäie;
import java.awt.Rectangle;
import javax.swing.*;

public abstract class NPC {
    
    public int id = 0;
    boolean määritettySijainti = true;
    int sijX;
    int sijY;
    public int sijX_PX_vy, sijX_PX_oa;
    public int sijY_PX_vy, sijY_PX_oa;
    public Rectangle hitbox = new Rectangle(sijX_PX_vy, sijY_PX_vy, PääIkkuna.pelaajanKokoPx, PääIkkuna.pelaajanKokoPx);
    protected int hp;
    public ImageIcon kuvake;
    String nimi = "";

    boolean lisäOminaisuuksia = false;
    String[] lisäOminaisuudet;

    String tiedot = "";
    void asetaTiedot() {
        tiedot += "Nimi: " + this.annaNimi() + "\n";
        //tiedot += "Satunnainen sijainti: " + (!this.määritettySijainti ? "Kyllä" : "Ei") + "\n";
    }
    
    public String annaTiedot() {
        return tiedot;
    }

    public boolean onkoMääritettySijainti() {
        return määritettySijainti;
    }

    public int annaSijX() {
        return sijX;
    }

    public int annaSijY() {
        return sijY;
    }

    public boolean onkolisäOminaisuuksia() {
        return lisäOminaisuuksia;
    }

    public String[] annalisäOminaisuudet() {
        return lisäOminaisuudet;
    }

    public Icon annaKuvake() {
        return kuvake;
    }

    boolean päivitäSijainti(String sij) {
        int sijXInt;
        int sijYInt;
        boolean NPCSiirrettiin = false;

        try {
            String sijXString = sij.substring(0, sij.indexOf(","));
            String sijYString = sij.substring(sij.indexOf(",") + 1);

            sijXInt = Integer.parseInt(sijXString);
            sijYInt = Integer.parseInt(sijYString);

            if (sijXInt < 0 || sijXInt > 9 || sijYInt < 0 || sijYInt > 9) {
                System.out.println("Kentän ulkopuolella! Sallitut arvot ovat 0-9");
                NPCSiirrettiin = false;
            }
            else {
                this.sijX = sijXInt;
                this.sijY = sijYInt;
                System.out.println("Pelaaja siirrettiin sijaintiin (" + sijX + ", " + sijY + ")");
                NPCSiirrettiin = true;
            }
            
        }
        catch (NumberFormatException e) {
            System.out.println("Virheellinen syöte. Syötä x- ja y-koordinaatit pilkulla erotettuina ilman välilyöntiä.");
            NPCSiirrettiin = false;
        }
        catch (NullPointerException e) {
            System.out.println("Virheellinen syöte. Syötä x- ja y-koordinaatit pilkulla erotettuina ilman välilyöntiä.");
            NPCSiirrettiin = false;
        }
        catch (StringIndexOutOfBoundsException e) {
            System.out.println("Virheellinen syöte. Syötä x- ja y-koordinaatit pilkulla erotettuina ilman välilyöntiä.");
            NPCSiirrettiin = false;
        }
        return NPCSiirrettiin;
    }

    public boolean siirrä(String suunta) {
        boolean NPCSiirtyi = false;
        switch (suunta) {
            case "vasen":
                // if (sijX > 0) {
                //     sijX--;
                // }
                if (sijX_PX_vy > Peli.kentänAlaraja) {
                    sijX_PX_vy -= 8;
                    sijX_PX_oa = sijX_PX_vy + PääIkkuna.pelaajanKokoPx;
                    hitbox.setLocation(sijX_PX_vy, sijY_PX_vy);
                    NPCSiirtyi = true;
                }
                break;
            case "oikea":
                // if (sijX < Peli.kentänKoko -1) {
                //     sijX++;
                // }
                if (sijX_PX_oa < Peli.kentänYläraja * PääIkkuna.pelaajanKokoPx) {
                    sijX_PX_vy += 8;
                    sijX_PX_oa = sijX_PX_vy + PääIkkuna.pelaajanKokoPx;
                    hitbox.setLocation(sijX_PX_vy, sijY_PX_vy);
                    NPCSiirtyi = true;
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
                    NPCSiirtyi = true;
                }
                break;
            case "alas":
                // if (sijY > 0) {
                //     sijY--;
                // }
                if (sijY_PX_oa < Peli.kentänYläraja * PääIkkuna.pelaajanKokoPx) {
                    sijY_PX_vy += 8;
                    sijY_PX_oa = sijY_PX_vy + PääIkkuna.pelaajanKokoPx;
                    hitbox.setLocation(sijX_PX_vy, sijY_PX_vy);
                    NPCSiirtyi = true;
                }
                break;
            default:
                return false;
        }
        sijX = ((sijX_PX_vy + sijX_PX_oa) /2 ) / PääIkkuna.pelaajanKokoPx;
        sijY = ((sijY_PX_vy + sijY_PX_oa) /2 ) / PääIkkuna.pelaajanKokoPx;
        return NPCSiirtyi;
    }

    boolean kokeileLiikkumista(String suunta) {
        boolean NPCSiirtyi = false;
        try {
            switch (suunta) {
                case "vasen":
                    if (sijX_PX_vy > 0) {
                        if (Peli.annaMaastoKenttä()[sijX_PX_vy/64 -1][sijY] == null) {
                            //pelaajaSiirtyi = siirry(new LiikkuminenVasemmalle());
                            NPCSiirtyi = siirrä("vasen");
                        }
                        else {
                            if (!Peli.annaMaastoKenttä()[sijX_PX_vy/64 -1][sijY].estääköLiikkumisen()) {
                                //pelaajaSiirtyi = siirry(new LiikkuminenVasemmalle());
                                NPCSiirtyi = siirrä("vasen");
                            }
                        }
                    }
                    break;
                case "oikea":
                    if (sijX_PX_oa < Peli.kentänYläraja * PääIkkuna.pelaajanKokoPx) {
                        if (Peli.annaMaastoKenttä()[sijX_PX_vy/64 +1][sijY] == null) {
                            //pelaajaSiirtyi = siirry(new LiikkuminenOikealle());
                            NPCSiirtyi = siirrä("oikea");
                        }
                        else {
                            if (!Peli.annaMaastoKenttä()[sijX_PX_vy/64 +1][sijY].estääköLiikkumisen()) {
                                //pelaajaSiirtyi = siirry(new LiikkuminenOikealle());
                                NPCSiirtyi = siirrä("oikea");
                            }
                        }
                    }
                    break;
                case "alas":
                    if (sijY_PX_oa < Peli.kentänYläraja * PääIkkuna.pelaajanKokoPx) {
                        if (Peli.annaMaastoKenttä()[sijX][sijY_PX_vy/64 +1] == null) {
                            //pelaajaSiirtyi = siirry(new LiikkuminenAlas());
                            NPCSiirtyi = siirrä("alas");
                        }
                        else {
                            if (!Peli.annaMaastoKenttä()[sijX][sijY_PX_vy/64 +1].estääköLiikkumisen()) {
                                //pelaajaSiirtyi = siirry(new LiikkuminenAlas());
                                NPCSiirtyi = siirrä("alas");
                            }
                        }
                    }
                    break;
                case "ylös":
                    if (sijY_PX_vy > 0) {
                        if (Peli.annaMaastoKenttä()[sijX][sijY_PX_vy/64 -1] == null) {
                            //pelaajaSiirtyi = siirry(new LiikkuminenYlös());
                            NPCSiirtyi = siirrä("ylös");
                        }
                        else {
                            if (!Peli.annaMaastoKenttä()[sijX][sijY_PX_vy/64 -1].estääköLiikkumisen()) {
                                //pelaajaSiirtyi = siirry(new LiikkuminenYlös());
                                NPCSiirtyi = siirrä("ylös");
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        catch (ArrayIndexOutOfBoundsException aioobe) {
            //aioobe.printStackTrace();
        }
        return NPCSiirtyi;
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


    public int annaHp() {
        return hp;
    }

    public String annaNimi() {
        return nimi;
    }

    void vahingoita(int määrä) {
        hp -= määrä;
    }

    void paranna(int määrä) {
        this.hp += määrä;
    }

    public String annaNimiSijamuodossa(String sijamuoto) {
        return "Tältä kohteelta puuttuu sijamuotojen määritys.";
    }

    NPC() {
        this.id = TarkistettavatArvot.npcId;
        TarkistettavatArvot.npcId++;
        this.sijX = 0;
        this.sijY = 0;
        this.sijX_PX_vy = 0;
        this.sijX_PX_oa = this.sijX_PX_vy + PääIkkuna.pelaajanKokoPx;
        this.sijY_PX_vy = 0;
        this.sijY_PX_oa = this.sijY_PX_vy + PääIkkuna.pelaajanKokoPx;
        if (Peli.npcLista != null) {
            Peli.lisääNPC(this);
        }
    }
}
