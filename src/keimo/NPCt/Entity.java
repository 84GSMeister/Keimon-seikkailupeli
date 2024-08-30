package keimo.NPCt;

import keimo.PääIkkuna;
import keimo.TarkistettavatArvot;
import keimo.Ruudut.PeliRuutu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JLabel;

public abstract class Entity extends JLabel {

    protected String nimi = "";
    public int sijX;
    public int sijY;
    protected int alkuSijX;
    protected int alkuSijY;
    public Rectangle hitbox;
    protected Icon kuvake;
    public int id = 0;
    protected int tilenKoko = PeliRuutu.pelaajanKokoPx;
    protected int leveys;
    protected int korkeus;
    boolean määritettySijainti = true;
    boolean lisäOminaisuuksia = false;
    String[] lisäOminaisuudet;

    public String annaNimi() {
        return nimi;
    }

    public Icon annaKuvake() {
        return kuvake;
    }

    public boolean onkoMääritettySijainti() {
        return määritettySijainti;
    }

    String tiedot = "";
    void asetaTiedot() {
        tiedot = "";
        tiedot += "Entityn ID: " + this.id + "\n";
        tiedot += "Nimi: " + this.annaNimi() + "\n";
        //tiedot += "Satunnainen sijainti: " + (!this.määritettySijainti ? "Kyllä" : "Ei") + "\n";
        if (this instanceof NPC) {
            if (this instanceof Vihollinen) {
                Vihollinen v = (Vihollinen)this;
                tiedot += "HP: " + v.hp + "\n";
                tiedot += "Vahinko: " + v.vahinko + "\n";
                if (v.ampuu) {
                    tiedot += "Ammusvahinko: " + v.ammusVahinko + "\n";
                }
                tiedot += "Nopeus: " + v.nopeus + "\n";
                if (v.tehoavatAseet != null) {
                    tiedot += "Tehoavat aseet: ";
                    for (String tehoavaAse : v.tehoavatAseet) {
                        tiedot += tehoavaAse + ", ";
                    }
                    tiedot = tiedot.substring(0, tiedot.length()-2);
                    tiedot += "\n";
                }
                tiedot += "Kilpi tehoaa: " + (v.kilpiTehoaa ? "Kyllä" : "Ei") + "\n";
                if (v.ominaisHuuto != null && v.ominaisHuuto != "") {
                    tiedot += "Ominaishuuto: " + v.ominaisHuuto + "\n";
                }
            }
        }
        else if (this instanceof LiikkuvaObjekti) {
            LiikkuvaObjekti lo = (LiikkuvaObjekti)this;
            tiedot += "Keimo voi työntää: " + (lo.voiTyöntää ? "Kyllä" : "Ei") + "\n";
        }
    }

    public String annaTiedot() {
        return tiedot;
    }

    /**
     * NPC:n tilen X-koordinaatti
     * @return X-sijainti (Tile)
     */
    public int annaSijX() {
        return sijX;
    }
    /**
     * NPC:n tilen Y-koordinaatti
     * @return Y-sijainti (Tile)
     */
    public int annaSijY() {
        return sijY;
    }
    /**
     * NPC:n sijainti pelikentällä pikseleinä.
     * Ei NPC:n sijainti näytöllä vaan scrollattavalla pelikentällä.
     * @return NPC:n sijaintia vastaava piste (java.awt.Point)
     */
    
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

    public static Entity luoEntityTiedoilla(String entitynNimi, boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista) {

        Entity luotavaEntity;

        switch (entitynNimi) {

            case "Asevihu":
                luotavaEntity = new Asevihu(sijX, sijY, ominaisuusLista);
            break;

            case "IsoLaatikko":
                luotavaEntity = new IsoLaatikko(sijX, sijY);
            break;

            case "Laatikko":
                luotavaEntity = new TyönnettäväLaatikko(sijX, sijY);
            break;

            case "Pahavihu":
                luotavaEntity = new Pahavihu(sijX, sijY, ominaisuusLista);
            break;

            case "Pikkuvihu":
                luotavaEntity = new Pikkuvihu(sijX, sijY, ominaisuusLista);
            break;

            case "Pomo":
                luotavaEntity = new Boss(sijX, sijY, ominaisuusLista);
            break;

            case "TestiEntity":
                luotavaEntity = new TestiEntity(sijX, sijY);
            break;

            case "Vartija":
                luotavaEntity = new Vartija(sijX, sijY, ominaisuusLista);
            break;

            default:
                luotavaEntity = null;
            break;
        }

        return luotavaEntity;
    }
    
    public SuuntaVasenOikea suuntaVasenOikea = SuuntaVasenOikea.OIKEA;
    public enum SuuntaVasenOikea {
        VASEN,
        OIKEA;
    }

    public SuuntaDiagonaali suuntaDiagonaali = SuuntaDiagonaali.OIKEA;
    public enum SuuntaDiagonaali {
        VASEN,
        OIKEA,
        ALAS,
        YLÖS,
        YLÄVASEN,
        ALAVASEN,
        YLÄOIKEA,
        ALAOIKEA;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (PääIkkuna.reunatNäkyvissä) {
            g.drawRect(0, 0, (int)hitbox.getWidth(), (int)hitbox.getHeight());
            g.setColor(Color.GREEN);
            g.fillRect(0, 0, (int)hitbox.getWidth(), (int)hitbox.getHeight());
            if (this instanceof LiikkuvaObjekti) {
                LiikkuvaObjekti obj = (LiikkuvaObjekti)this;
                g.drawRect(obj.sisäHitboxOffset, obj.sisäHitboxOffset, (int)obj.sisäHitbox.getWidth(), (int)obj.sisäHitbox.getHeight());
                g.setColor(Color.RED);
                g.fillRect(obj.sisäHitboxOffset, obj.sisäHitboxOffset, (int)obj.sisäHitbox.getWidth(), (int)obj.sisäHitbox.getHeight());
            }
        }
    }
    Entity(int sijX, int sijY) {
        this.sijX = sijX;
        this.sijY = sijY;
        this.alkuSijX = sijX;
        this.alkuSijY = sijY;
        this.id = TarkistettavatArvot.luoNpcId();
    }
    Entity(int sijX, int sijY, String[] ominaisuusLista) {
        this(sijX, sijY);
        this.lisäOminaisuudet = ominaisuusLista;
    }
}
