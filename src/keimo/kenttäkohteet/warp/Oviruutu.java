package keimo.kenttäkohteet.warp;

import keimo.keimoEngine.grafiikat.Tekstuuri;

public class Oviruutu extends Warp {

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":  return "Oviruutu";
            case "genetiivi":    return "Oviruudun";
            case "esiivi":       return "Oviruutuna";
            case "partitiivi":   return "Oviruutua";
            case "translatiivi": return "Oviruuduksi";
            case "inessiivi":    return "Oviruudussa";
            case "elatiivi":     return "Oviruudusta";
            case "illatiivi":    return "Oviruutuun";
            case "adessiivi":    return "Oviruudulla";
            case "ablatiivi":    return "Oviruudulta";
            case "allatiivi":    return "Oviruudulle";
            default:             return "Oviruutu";
        }
    }
    
    @Override
    public void asetaSuunta(Suunta suunta) {
        super.asetaSuunta(suunta);
        switch (suunta) {
            case YLÖS:
                this.suunta = Suunta.YLÖS;
                break;
            case ALAS:
                this.suunta = Suunta.ALAS;
                break;
            case VASEN:
                this.suunta = Suunta.VASEN;
                break;
            case OIKEA:
                this.suunta = Suunta.OIKEA;
                break;
            default:
                this.suunta = Suunta.YLÖS;
                break;
        }
    }

    public Oviruutu(int sijX, int sijY, String[] ominaisuusLista) {
        super(true, sijX, sijY);
        this.nimi = "Oviruutu";

        this.suunta = Suunta.VASEN;

        if (ominaisuusLista != null) {
            for (String ominaisuus : ominaisuusLista) {
                if (ominaisuus.startsWith("kohdehuone=")) {
                    this.kohdeHuone = Integer.parseInt("" + ominaisuus.substring(ominaisuus.indexOf("=") +1));
                }
                else if (ominaisuus.startsWith("kohderuutuX=")) {
                    this.kohdeRuutuX = Integer.parseInt("" + ominaisuus.substring(ominaisuus.indexOf("=") +1));
                }
                else if (ominaisuus.startsWith("kohderuutuY=")) {
                    this.kohdeRuutuY = Integer.parseInt("" + ominaisuus.substring(ominaisuus.indexOf("=") +1));
                }
                else if (ominaisuus.startsWith("suunta=")) {
                    String suuntaString = ominaisuus.substring(7);
                    switch (suuntaString) {
                        case "vasen", "VASEN", "Vasen": this.suunta = Suunta.VASEN; break;
                        case "oikea", "OIKEA", "Oikea": this.suunta = Suunta.OIKEA; break;
                        case "ylös", "YLÖS", "Ylös": this.suunta = Suunta.YLÖS; break;
                        case "alas", "ALAS", "Alas": this.suunta = Suunta.ALAS; break;
                        default: this.suunta = Suunta.VASEN; break;
                    }
                }
            }
        }

        super.tiedostonNimi = "reunawarppi.png";
        super.tekstuuri = new Tekstuuri("tiedostot/kuvat/kenttäkohteet/" + tiedostonNimi);        
        asetaSuunta(suunta);
        this.lisäOminaisuuksia = true;
        this.lisäOminaisuudet = new String[4];
        this.lisäOminaisuudet[0] = "kohdehuone=" + kohdeHuone;
        this.lisäOminaisuudet[1] = "kohderuutuX=" + kohdeRuutuX;
        this.lisäOminaisuudet[2] = "kohderuutuY=" + kohdeRuutuY;
        this.lisäOminaisuudet[3] = "suunta=" + annaSuunta();
        super.asetaTiedot();
    }
}
