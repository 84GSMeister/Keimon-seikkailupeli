package keimo.seikkailupeli.objektit.entityt;

import keimo.TarkistettavatArvot;
import keimo.seikkailupeli.assets.KuvaObjekti;
import keimo.seikkailupeli.objektit.PeliObjekti;
import keimo.seikkailupeli.objektit.entityt.npc.Asevihu;
import keimo.seikkailupeli.objektit.entityt.npc.Boss;
import keimo.seikkailupeli.objektit.entityt.npc.NPC;
import keimo.seikkailupeli.objektit.entityt.npc.Pahavihu;
import keimo.seikkailupeli.objektit.entityt.npc.Pikkuvihu;
import keimo.seikkailupeli.objektit.entityt.npc.Vartija;
import keimo.seikkailupeli.objektit.entityt.npc.Vihollinen;

import java.util.ArrayList;

public abstract class Entity extends PeliObjekti {

    public static String[] entityLista = {"Asevihu", "IsoLaatikko", "Laatikko", "Pahavihu", "Pikkuvihu", "Pomo", "TestiEntity", "Vartija"};

    protected int alkuSijX;
    protected int alkuSijY;
    public int id = 0;
    public int tilenKoko = 64;
    public int leveys;
    public int korkeus;
    boolean määritettySijainti = true;
    public SuuntaVasenOikea suuntaVasenOikea = SuuntaVasenOikea.VASEN;

    protected Entity(int sijX, int sijY) {
        this.sijX = sijX;
        this.sijY = sijY;
        this.alkuSijX = sijX;
        this.alkuSijY = sijY;
        this.id = TarkistettavatArvot.luoNpcId();
        this.tekstuuriObjekti = new KuvaObjekti(this.tekstuuri);
        asetaTiedot();
    }

    public boolean onkoMääritettySijainti() {
        return määritettySijainti;
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        return katsomisTeksti;
    }

    String tiedot = "";
    protected void asetaTiedot() {
        tiedot = "";
        tiedot += "Entityn ID: " + this.id + "\n";
        tiedot += "Nimi: " + this.annaNimi() + "\n";
        if (this instanceof NPC) {
            tiedot += "Tyyppi: NPC";
            if (this instanceof Vihollinen) {
                tiedot += ", Vihollinen" + "\n";
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
            else {
                tiedot += "\n";
            }
        }
        else if (this instanceof LiikkuvaObjekti) {
            tiedot += "Tyyppi: Liikkuva objekti" + "\n";
            LiikkuvaObjekti lo = (LiikkuvaObjekti)this;
            tiedot += "Keimo voi työntää: " + (lo.voiTyöntää ? "Kyllä" : "Ei") + "\n";
        }
    }

    public String annaTiedot() {
        return tiedot;
    }
    
    public int annaAlkuSijX() {
        return alkuSijX;
    }

    public int annaAlkuSijY() {
        return alkuSijY;
    }

    public static Entity luoEntityTiedoilla(String entitynNimi, int sijX, int sijY, ArrayList<String> ominaisuusLista) {

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
}
