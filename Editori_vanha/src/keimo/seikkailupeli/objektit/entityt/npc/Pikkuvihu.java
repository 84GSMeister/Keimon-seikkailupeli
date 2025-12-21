package keimo.seikkailupeli.objektit.entityt.npc;

import java.util.ArrayList;

public class Pikkuvihu extends Vihollinen {

    @Override
    public void kukista(String kukistusTapa) {
        super.kukista(kukistusTapa);
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

    @Override
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

    public void päivitäLisäOminaisuudet() {
        if (this.lisäOminaisuudet != null) {
            this.lisäOminaisuuksia = true;
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("liiketapa="));
            this.lisäOminaisuudet.add("liiketapa=" + liikeTapa);
            this.lisäOminaisuudet.removeIf(ominaisuus -> ominaisuus.startsWith("suunta="));
            this.lisäOminaisuudet.add("suunta=" + suunta);
        }
    }

    public Pikkuvihu(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.hp = 2;
        super.maxHp = super.hp;
        super.vahinko = 1;
        super.nopeus = 3;
        super.tekeeVahinkoa = true;
        super.tiedostonNimi = "tiedostot/kuvat/npc/pikkuvihu.gif";
        super.kilpiTehoaa = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.hitbox.setLocation(sijX * 64, sijY * 64);
        super.nimi = "Pikkuvihu";
        super.tehoavatAseet.add("Vesiämpäri");
        super.tehoavatAseet.add("Pesäpallomaila");
        super.asetaTiedot();
    }
}
