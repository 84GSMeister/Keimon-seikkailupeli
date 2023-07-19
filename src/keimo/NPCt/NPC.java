package keimo.NPCt;

import keimo.Peli;
import keimo.TarkistettavatArvot;
import keimo.Kenttäkohteet.Käännettävä;
import keimo.Ruudut.PeliRuutu;

import java.awt.Rectangle;
import javax.swing.*;

public abstract class NPC implements Käännettävä{
    
    public int id = 0;
    boolean määritettySijainti = true;
    public int sijX;
    public int sijY;
    protected int alkuSijX;
    protected int alkuSijY;
    public int nopeus;
    public Rectangle hitbox = new Rectangle(0, 0, PeliRuutu.pelaajanKokoPx, PeliRuutu.pelaajanKokoPx);
    protected int hp;
    public ImageIcon kuvake;
    String nimi = "";
    public boolean onLadattuPelissä = false;

    boolean lisäOminaisuuksia = false;
    String[] lisäOminaisuudet;

    String tiedot = "";
    void asetaTiedot() {
        tiedot += "Nimi: " + this.annaNimi() + "\n";
        //tiedot += "Satunnainen sijainti: " + (!this.määritettySijainti ? "Kyllä" : "Ei") + "\n";
        if (this instanceof Vihollinen) {
            Vihollinen v = (Vihollinen)this;
            if (v.tehoavatAseet != null) {
                tiedot += "Tehoavat aseet: ";
                for (String tehoavaAse : v.tehoavatAseet) {
                    tiedot += tehoavaAse + ", ";
                }
                tiedot = tiedot.substring(0, tiedot.length()-2);
                tiedot += "\n";
            }
            tiedot += "Kilpi tehoaa: " + (v.kilpiTehoaa ? "Kyllä" : "Ei") + "\n";
        }   
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

    public int annaAlkuSijX() {
        return alkuSijX;
    }

    public int annaAlkuSijY() {
        return alkuSijY;
    }

    public boolean onkolisäOminaisuuksia() {
        return lisäOminaisuuksia;
    }

    public String[] annalisäOminaisuudet() {
        return lisäOminaisuudet;
    }

    public String annaLisäOminaisuudetYhtenäMjonona() {
        String mjono = "";
		if (annalisäOminaisuudet() != null) {
			for (String s : annalisäOminaisuudet()) {
				mjono += s + ",";
			}
            mjono = mjono.substring(0, mjono.length()-1);
		}
        return mjono;
    }

    public Icon annaKuvake() {
        return kuvake;
    }

    public SuuntaVasenOikea npcnSuuntaVasenOikea = SuuntaVasenOikea.OIKEA;
    public enum SuuntaVasenOikea {
        VASEN,
        OIKEA;
    }

    private boolean siirrä(Suunta suunta) {
        boolean NPCSiirtyi = false;
        switch (suunta) {
            case VASEN:
                if (this.hitbox.getMinX() > Peli.kentänAlaraja) {
                    this.hitbox.setLocation((int)this.hitbox.getMinX() - this.nopeus, (int)this.hitbox.getMinY());
                    NPCSiirtyi = true;
                }
                break;
            case OIKEA:
                if (this.hitbox.getMaxX() < Peli.kentänKoko * PeliRuutu.pelaajanKokoPx) {
                    this.hitbox.setLocation((int)this.hitbox.getMinX() + this.nopeus, (int)this.hitbox.getMinY());
                    NPCSiirtyi = true;
                }
                break;
            case YLÖS:
                if (this.hitbox.getMinY() > Peli.kentänAlaraja) {
                    this.hitbox.setLocation((int)this.hitbox.getMinX(), (int)this.hitbox.getMinY() - this.nopeus);
                    NPCSiirtyi = true;
                }
                break;
            case ALAS:
                if (this.hitbox.getMaxY() < Peli.kentänKoko * PeliRuutu.pelaajanKokoPx) {
                    this.hitbox.setLocation((int)this.hitbox.getMinX(), (int)this.hitbox.getMinY() + this.nopeus);
                    NPCSiirtyi = true;
                }
                break;
            default:
                return false;
        }
        this.sijX = (int)this.hitbox.getCenterX() / PeliRuutu.pelaajanKokoPx;
        this.sijY = (int)this.hitbox.getCenterY() / PeliRuutu.pelaajanKokoPx;
        return NPCSiirtyi;
    }

    public boolean kokeileLiikkumista(Suunta suunta) {
        boolean NPCSiirtyi = false;
        try {
            switch (suunta) {
                case VASEN:
                    this.npcnSuuntaVasenOikea = SuuntaVasenOikea.VASEN;
                    if (hitbox.getMinX() > 0) {
                        if (Peli.annaMaastoKenttä()[(int)hitbox.getMinX()/PeliRuutu.pelaajanKokoPx][sijY] == null) {
                            NPCSiirtyi = siirrä(Suunta.VASEN);
                        }
                        else {
                            if (!Peli.annaMaastoKenttä()[(int)hitbox.getMinX()/PeliRuutu.pelaajanKokoPx][sijY].estääköLiikkumisen(suunta)) {
                                NPCSiirtyi = siirrä(Suunta.VASEN);
                            }
                        }
                    }
                    break;
                case OIKEA:
                    this.npcnSuuntaVasenOikea = SuuntaVasenOikea.OIKEA;
                    if (hitbox.getMaxX() < Peli.kentänKoko * PeliRuutu.pelaajanKokoPx) {
                        if (Peli.annaMaastoKenttä()[(int)hitbox.getMaxX()/PeliRuutu.pelaajanKokoPx][sijY] == null) {
                            NPCSiirtyi = siirrä(Suunta.OIKEA);
                        }
                        else {
                            if (!Peli.annaMaastoKenttä()[(int)hitbox.getMaxX()/PeliRuutu.pelaajanKokoPx][sijY].estääköLiikkumisen(suunta)) {
                                NPCSiirtyi = siirrä(Suunta.OIKEA);
                            }
                        }
                    }
                    break;
                case ALAS:
                    if (hitbox.getMaxY() < Peli.kentänKoko * PeliRuutu.pelaajanKokoPx) {
                        if (Peli.annaMaastoKenttä()[sijX][(int)hitbox.getMaxY()/PeliRuutu.pelaajanKokoPx] == null) {
                            NPCSiirtyi = siirrä(Suunta.ALAS);
                        }
                        else {
                            if (!Peli.annaMaastoKenttä()[sijX][(int)hitbox.getMaxY()/PeliRuutu.pelaajanKokoPx].estääköLiikkumisen(suunta)) {
                                NPCSiirtyi = siirrä(Suunta.ALAS);
                            }
                        }
                    }
                    break;
                case YLÖS:
                    if (hitbox.getMinY() > 0) {
                        if (Peli.annaMaastoKenttä()[sijX][(int)hitbox.getMinY()/PeliRuutu.pelaajanKokoPx] == null) {
                            NPCSiirtyi = siirrä(Suunta.YLÖS);
                        }
                        else {
                            if (!Peli.annaMaastoKenttä()[sijX][(int)hitbox.getMinY()/PeliRuutu.pelaajanKokoPx].estääköLiikkumisen(suunta)) {
                                NPCSiirtyi = siirrä(Suunta.YLÖS);
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

    public void teleport(int kohdeX, int kohdeY) {
        sijX = kohdeX;
        sijY = kohdeY;
        this.hitbox.setLocation(kohdeX * PeliRuutu.pelaajanKokoPx, kohdeY * PeliRuutu.pelaajanKokoPx);
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

    public static NPC luoNPCTiedoilla(String npcnNimi, boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {

        NPC luotavaNPC;

        switch (npcnNimi) {

            case "Pikkuvihu":
                luotavaNPC = new Pikkuvihu(sijX, sijY, ominaisuusLista);
                break;

            case "Pahavihu":
                luotavaNPC = new Pahavihu(sijX, sijY, ominaisuusLista);
                break;
            
            case "Vartija":
                luotavaNPC = new Vartija(sijX, sijY, ominaisuusLista);
                break;

            default:
                luotavaNPC = null;
                break;
        }

        if (luotavaNPC == null) {
            JOptionPane.showMessageDialog(null, "Ei voi ladata npc:tä", "luotavaNPC = null", JOptionPane.ERROR_MESSAGE);
        }
        //luotavaNPC.teleport(sijX, sijY);
        return luotavaNPC;
    }

    NPC(int sijX, int sijY) {
        this.id = TarkistettavatArvot.npcId;
        TarkistettavatArvot.npcId++;
        this.sijX = sijX;
        this.sijY = sijY;
        this.alkuSijX = sijX;
        this.alkuSijY = sijY;
        this.onLadattuPelissä = false;
        this.hitbox.setLocation(sijX, sijY);
        if (Peli.npcLista != null) {
            Peli.lisääNPC(this);
        }
    }
}
