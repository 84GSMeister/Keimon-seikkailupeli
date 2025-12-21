package keimo.seikkailupeli.objektit.entityt.npc;

import java.util.ArrayList;

public class Pahavihu extends Vihollinen {

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
        super.tiedostonNimi = "tiedostot/kuvat/npc/pahavihu.gif";
        super.kilpiTehoaa = true;
        super.sijX = sijX;
        super.sijY = sijY;
        super.hitbox.setLocation(sijX * 64, sijY * 64);
        super.nimi = "Pahavihu";
        super.tehoavatAseet.add("Pesäpallomaila");
        super.asetaTiedot();
    }
}
