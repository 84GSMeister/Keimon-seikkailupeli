package keimo.entityt.npc;

import keimo.keimoEngine.grafiikat.*;


public class Pikkuvihu extends Vihollinen {

    private Kuva vakioTekstuuri = new Animaatio(15, "tiedostot/kuvat/npc/pikkuvihu.gif");
    private Kuva ämpäröityTekstuuri = new Tekstuuri("tiedostot/kuvat/npc/pikkuvihu_suutari.png");
    private Kuva lyötyTekstuuri = new Tekstuuri("tiedostot/kuvat/npc/pikkuvihu_lyöty.png");

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
            case "nominatiivi": return "Pikkuvihu";
            case "genetiivi": return "Pikkuvihun";
            case "esiivi": return "Pikkuvihuna";
            case "partitiivi": return "Pikkuvihua";
            case "translatiivi": return "Pikkuvihuksi";
            case "inessiivi": return "Pikkuvihussa";
            case "elatiivi": return "Pikkuvihusta";
            case "illatiivi": return "Pikkuvihuun";
            case "adessiivi": return "Pikkuvihulla";
            case "ablatiivi": return "Pikkuvihulta";
            case "allatiivi": return "Pikkuvihulle";
            default: return "Pikkuvihu";
        }
    }

    public Pikkuvihu(int sijX, int sijY, String[] ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.hp = 2;
        super.maxHp = super.hp;
        super.vahinko = 1;
        super.nopeus = 3;
        super.tekeeVahinkoa = true;
        super.kuvaTiedosto = "tiedostot/kuvat/npc/pikkuvihu.gif";
        super.tekstuuri = vakioTekstuuri;
        super.kilpiTehoaa = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.hitbox.setLocation(sijX * 64, sijY * 64);
        super.nimi = "Pikkuvihu";
        super.lisäOminaisuuksia = true;
        super.lisäOminaisuudet = ominaisuusLista;
        super.tehoavatAseet.add("Vesiämpäri");
        super.tehoavatAseet.add("Pesäpallomaila");
        super.asetaTiedot();
    }
}
