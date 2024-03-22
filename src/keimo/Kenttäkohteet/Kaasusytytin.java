package keimo.Kenttäkohteet;

import javax.swing.ImageIcon;

public final class Kaasusytytin extends Esine {
    
    public boolean toimiva;

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        //Sittenhän tää homma vasta meneekin hauskaksi, kun tulee esineitä, joiden nimi voi muuttua.
        if (toimiva){
            switch (sijamuoto) {
                case "nominatiivi": return "Toimiva kaasusytytin";
                case "genetiivi": return "Toimivan kaasusytyttimen";    
                case "esiivi": return "Toimivana kaasusytytimenä";
                case "partitiivi": return "Toimivaa kaasusytytintä";
                case "translatiivi": return "Toimivaksi kaasusytyttimeksi";
                case "inessiivi": return "Toimivassa kaasusytyttimessä";
                case "elatiivi": return "Toimivasta kaasusytyttimestä";
                case "illatiivi": return "Toimivaan kaasusytyttimeen";
                case "adessiivi": return "Toimivalla kaasusytyttimellä";
                case "ablatiivi": return "Toimivalta kaasusytyttimeltä";
                case "allatiivi": return "Toimivalle kaasusytyttimelle";
                default: return "Toimiva kaasusytytin";
            }
        }
        else {
            switch (sijamuoto) {
                case "nominatiivi": return "Tyhjä kaasusytytin";
                case "genetiivi": return "Tyhjän kaasusytyttimen";    
                case "esiivi": return "Tyhjänä kaasusytytimenä";
                case "partitiivi": return "Tyhjää kaasusytytintä";
                case "translatiivi": return "Tyhjäksi kaasusytyttimeksi";
                case "inessiivi": return "Tyhjässä kaasusytyttimessä";
                case "elatiivi": return "Tyhjästä kaasusytyttimestä";
                case "illatiivi": return "Tyhjään kaasusytyttimeen";
                case "adessiivi": return "Tyhjällä kaasusytyttimellä";
                case "ablatiivi": return "Tyhjältä kaasusytyttimeltä";
                case "allatiivi": return "Tyhjälle kaasusytyttimelle";
                default: return "Tyhjä kaasusytytin";
            }
        }
    }

    public void asetaToimivuus(String toimivuus) {
        this.kelvollisetYhdistettävät.clear();
        this.sopiiKäytettäväksi.clear();
        switch (toimivuus) {
            case "tyhjä":
                this.toimiva = false;
                this.kenttäkäyttö = false;
                this.yhdistettävä = true;
                this.kelvollisetYhdistettävät.add("Kaasupullo");
                this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/tyhjäkaasusytytin.png");
                this.katsomisTeksti = "Tästä puuttuu kaasupullo. Löytyisiköhän sellainen kentältä?";
                this.käyttöTeksti = "Kaasusytytin ei toimi ilman kaasupulloa.";
                this.lisäOminaisuudet = new String[1];
                this.lisäOminaisuudet[0] = "toimivuus=tyhjä";
                break;
            case "toimiva":
                this.toimiva = true;
                this.kenttäkäyttö = true;
                this.yhdistettävä = false;
                this.sopiiKäytettäväksi.add("Nuotio");
                this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/kaasusytytin.png");
                this.katsomisTeksti = "Tätä voisi käyttää nuotion sytyttämiseen.";
                this.käyttöTeksti = "Leimahti!";
                this.lisäOminaisuudet = new String[1];
                this.lisäOminaisuudet[0] = "toimivuus=toimiva";
                break;
            default:
                break;
        }
    }

    public Kaasusytytin(boolean määritettySijainti, int sijX, int sijY, String[] ominaisuusLista){
        super(määritettySijainti, sijX, sijY);
        this.nimi = "Kaasusytytin";
        String toimivuus = "tyhjä";
        if (ominaisuusLista != null) {
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("toimivuus=")) {
                    toimivuus = ominaisuus.substring(10);
                }
            }
        }
        switch (toimivuus) {
            case "toimiva":
                this.toimiva = true;
                this.kenttäkäyttö = true;
                this.sopiiKäytettäväksi.add("Nuotio");
                this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/kaasusytytin.png");
                this.katsomisTeksti = "Tätä voisi käyttää nuotion sytyttämiseen.";
                this.käyttöTeksti = "Leimahti!";
                break;
            case "tyhjä":
                this.toimiva = false;
                this.yhdistettävä = true;
                this.kelvollisetYhdistettävät.add("Kaasupullo");
                this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/tyhjäkaasusytytin.png");
                this.katsomisTeksti = "Tästä puuttuu kaasupullo. Löytyisiköhän sellainen kentältä?";
                this.käyttöTeksti = "Kaasusytytin ei toimi ilman kaasupulloa.";
                break;
            default:
                this.toimiva = true;
                this.kenttäkäyttö = true;
                this.sopiiKäytettäväksi.add("Nuotio");
                this.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/kaasusytytin.png");
                this.katsomisTeksti = "Tätä voisi käyttää nuotion sytyttämiseen.";
                this.käyttöTeksti = "Leimahti!";
                break;
        }
        super.hinta = 12.9;
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet = new String[1];
        this.lisäOminaisuudet[0] = "toimivuus=" + toimivuus;
        super.asetaTiedot();
    }
}
