package keimo.seikkailupeli.objektit.entityt.npc;

import java.util.ArrayList;

public class Asevihu extends Vihollinen {

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

    public Asevihu(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.hp = 2;
        super.vahinko = 1;
        super.nopeus = 3;
        super.tekeeVahinkoa = true;
        super.ampuu = true;
        super.ammusVahinko = 2;
        super.tiedostonNimi = "tiedostot/kuvat/npc/asevihu.gif";
        super.kilpiTehoaa = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.hitbox.setLocation(sijX * 64, sijY * 64);
        super.nimi = "Asevihu";
        super.tehoavatAseet.add("Vesiämpäri");
        super.tehoavatAseet.add("Pesäpallomaila");
        super.asetaTiedot();
    }
}
