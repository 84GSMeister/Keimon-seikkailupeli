package keimo.Kenttäkohteet;

import javax.swing.ImageIcon;

public final class Vesiämpäri extends Esine {

    @Override
    public String käytä() {
        super.poista = true;
        return "Vihollinen ämpäröitiin ja on nyt harmiton.";
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi": return "Vesiämpäri";
            case "genetiivi": return "Vesiämpärin";
            case "esiivi": return "Vesiämpärinä";
            case "partitiivi": return "Vesiämpäriä";
            case "translatiivi": return "Vesiämpäriksi";
            case "inessiivi": return "Vesiämpärissä";
            case "elatiivi": return "Vesiämpäristä";
            case "illatiivi": return "Vesiämpäriin";
            case "adessiivi": return "Vesiämpärillä";
            case "ablatiivi": return "Vesiämpäriltä";
            case "allatiivi": return "Vesiämpärille";
            default: return "Vesiämpäri";
        }
    }

    public Vesiämpäri(boolean määritettySijainti, int sijX, int sijY){
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Vesiämpäri";
        super.kenttäkäyttö = true;
        super.katsomisTeksti = "Näyttää juuri oikean kokoiselta vihollisen päähän.";
        super.sopiiKäytettäväksi.add("Nuotio");
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/vesiämpäri.png");
        super.tiedostonNimi = "vesiämpäri.png";
        super.vahinko = 2;
        super.asetaTiedot();
    }
}
