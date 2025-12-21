package keimo.seikkailupeli.objektit.kenttäkohteet.kenttäNPC;

import java.util.ArrayList;

public final class JumalYoda extends NPC_KenttäKohde {

    public JumalYoda(int sijX, int sijY, ArrayList<String> ominaisuusLista) {
        super(sijX, sijY, ominaisuusLista);
        super.nimi = "Jumal Yoda";
        super.tiedostonNimi = "goblin.png";
        super.katsomisTeksti = "Polku pimeälle puolelle?";
        super.dialogit.add("metsä");
        super.dialogit.add("kuu");
        if (ominaisuusLista == null) super.valitseVakioDialogi();
        super.asetaTiedot();
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        //Vaikee kuvitella, että näitäkään ikinä tarvis, mutta laitetaan ihan vaan perfektionismin tähden.
        switch (sijamuoto) {
            case "nominatiivi":  return "Jumal Yoda";
            case "genetiivi":    return "Jumal Yodan";
            case "essiivi":      return "Jumal Yodana";
            case "partitiivi":   return "Jumal Yodaa";
            case "translatiivi": return "Jumal Yodaksi";
            case "inessiivi":    return "Jumal Yodassa";
            case "elatiivi":     return "Jumal Yodasta";
            case "illatiivi":    return "Jumal Yodaan";
            case "adessiivi":    return "Jumal Yodalla";
            case "ablatiivi":    return "Jumal Yodalta";
            case "allatiivi":    return "Jumal Yodalle";
            default:             return "Jumal Velho";
        }
    }

    @Override
    public void juttele() {

    }
}
