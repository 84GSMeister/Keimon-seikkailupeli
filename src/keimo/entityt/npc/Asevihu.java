package keimo.entityt.npc;

import keimo.keimoEngine.grafiikat.*;

public class Asevihu extends Vihollinen {

    private Kuva vakioTekstuuri = new Animaatio(12, "tiedostot/kuvat/npc/asevihu.gif");
    private Kuva ämpäröityTekstuuri = new Tekstuuri("tiedostot/kuvat/npc/asevihu_ämpäröity.png");
    private Kuva lyötyTekstuuri = new Tekstuuri("tiedostot/kuvat/npc/asevihu_lyöty.png");

    @Override
    public void kukista(String kukistusTapa) {
        super.kukista(kukistusTapa);
        switch (kukistusTapa) {
            case "Ämpäri", "Vesiämpäri" -> {
                this.tekstuuri = ämpäröityTekstuuri;
            }
            case "Pesäpallomaila" -> {
                this.tekstuuri = lyötyTekstuuri;
            }
        }
    }

    @Override
    public void vahingoita(int määrä) {
        super.vahingoita(määrä);
    }

    public String katso() {
        if (!onkoKukistettu()) {
            return "Voi ei! Se on ilkeä vihollinen";
        }
        else {
            return "Vihollinen on kukistettu ja nyt täysin harmiton.";
        }
    }

    public void päivitäLisäOminaisuudet(LiikeTapa liikeTapa) {
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet = new String[1];
        this.lisäOminaisuudet[0] = "liiketapa=" + liikeTapa;
    }

    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi": return "Asevihu";
            case "genetiivi": return "Asevihun";
            case "esiivi": return "Asevihuna";
            case "partitiivi": return "Asevihua";
            case "translatiivi": return "Asevihuksi";
            case "inessiivi": return "Asevihussa";
            case "elatiivi": return "Asevihusta";
            case "illatiivi": return "Asevihuun";
            case "adessiivi": return "Asevihulla";
            case "ablatiivi": return "Asevihulta";
            case "allatiivi": return "Asevihulle";
            default: return "Asevihu";
        }
    }

    public Asevihu(int sijX, int sijY, String[] ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.hp = 2;
        super.vahinko = 1;
        super.nopeus = 3;
        super.tekeeVahinkoa = true;
        super.ampuu = true;
        super.ammusVahinko = 2;
        super.kuvaTiedosto = "tiedostot/kuvat/npc/asevihu.gif";
        super.tekstuuri = vakioTekstuuri;
        super.kilpiTehoaa = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.hitbox.setLocation(sijX * 64, sijY * 64);
        super.nimi = "Asevihu";
        super.lisäOminaisuuksia = true;
        super.lisäOminaisuudet = ominaisuusLista;
        super.tehoavatAseet.add("Vesiämpäri");
        super.tehoavatAseet.add("Pesäpallomaila");
        super.asetaTiedot();
    }
}
