package keimo.entityt.npc;

import keimo.keimoEngine.grafiikat.Animaatio;
import keimo.keimoEngine.grafiikat.Kuva;
import keimo.keimoEngine.grafiikat.Tekstuuri;
import keimo.keimoEngine.äänet.Äänet;

import java.util.ArrayList;

public class Pahavihu extends Vihollinen {

    private Kuva vakioTekstuuri = new Animaatio(15, "tiedostot/kuvat/npc/pahavihu.gif");
    private Kuva lyötyTekstuuri = new Tekstuuri("tiedostot/kuvat/npc/pahavihu_lyöty.png");

    @Override
    public void kukista(String kukistusTapa) {
        super.kukista(kukistusTapa);
        switch (kukistusTapa) {
            case "Pesäpallomaila" -> {
                this.tekstuuri = lyötyTekstuuri;
            }
        }
    }

    @Override
    public void vahingoita(int määrä) {
        super.vahingoita(määrä);
        Äänet.toistaSFX("pikkuvihu_damage");
    }

    public String katso() {
        if (!onkoKukistettu()) {
            return "Voi ei! Se on ilkeä vihollinen";
        }
        else {
            return "Vihollinen on kukistettu ja nyt täysin harmiton.";
        }
    }

    // public void päivitäLisäOminaisuudet(LiikeTapa liikeTapa) {
    //     this.lisäOminaisuuksia = true;
    //     //this.lisäOminaisuudet = new String[1];
    //     //this.lisäOminaisuudet[0] = "liiketapa=" + liikeTapa;
    //     this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("liiketapa="));
    //     this.lisäOminaisuudet.add("liiketapa=" + liikeTapa);
    // }

    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi": return "Paha vihu";
            case "genetiivi": return "Pahan vihun";
            case "esiivi": return "Pahana vihuna";
            case "partitiivi": return "Pahaa vihua";
            case "translatiivi": return "Pahaksi vihuksi";
            case "inessiivi": return "Pahassa vihussa";
            case "elatiivi": return "Pahasta vihusta";
            case "illatiivi": return "Pahaan vihuun";
            case "adessiivi": return "Pahalla vihulla";
            case "ablatiivi": return "Pahalta vihulta";
            case "allatiivi": return "Pahalle vihulle";
            default: return "Paha vihu";
        }
    }

    public Pahavihu(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.hp = 5;
        super.maxHp = super.hp;
        super.vahinko = 2;
        super.nopeus = 4;
        super.tekeeVahinkoa = true;
        super.kuvaTiedosto = "tiedostot/kuvat/npc/pahavihu.gif";
        super.tekstuuri = vakioTekstuuri;
        super.kilpiTehoaa = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.hitbox.setLocation(sijX * 64, sijY * 64);
        super.nimi = "Pahavihu";
        super.tehoavatAseet.add("Pesäpallomaila");
        super.asetaTiedot();
    }
}
