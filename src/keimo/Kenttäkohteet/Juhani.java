package keimo.Kenttäkohteet;

import keimo.Pelaaja;
import keimo.PääIkkuna;
import keimo.TarkistettavatArvot;

import javax.swing.ImageIcon;

public class Juhani extends NPC_KenttäKohde {

    @Override
    public String kokeileEsinettä(Esine e) {
        if (e instanceof Pesäpallomaila) {
            return "...";
        }
        else if (e instanceof Seteli) {
            return "<html><p>-Njuu. Kyllä tää fiitti nyt siltä näyttää, että sinä annat minulle yhden massin, ja minä annan sinulle yhden huumeen.</p></html>";
        }
        else {
            return super.kokeileEsinettä(e);
        }
    }

    @Override
    public Esine suoritaMuutoksetEsineelle(Esine e) {
        if (e instanceof Seteli) {
            e = annaHuume();
            PääIkkuna.hudTeksti.setText("Juhani: -Njuu. Kyllä tää fiitti nyt siltä näyttää, että sinä annat minulle yhden massin, ja minä annan sinulle yhden huumeen.");
            return e;
        }
        else if (e instanceof Pesäpallomaila) {
            TarkistettavatArvot.pelinLoppuSyy = TarkistettavatArvot.PelinLopetukset.KUOLEMA_JUHANI;
            Pelaaja.hp = 0;
            return e;
        }
        else {
            PääIkkuna.hudTeksti.setText(e.annaNimiSijamuodossa("partitiivi") + " ei voi käyttää " + this.annaNimiSijamuodossa("illatiivi"));
            return e;
        }
    }

    @Override
    public String annaNimiSijamuodossa(String sijamuoto) {
        switch (sijamuoto) {
            case "nominatiivi":
                return "Juhani";
            case "genetiivi":
                return "Juhanin";
            case "esiivi":
                return "Juhanina";
            case "partitiivi":
                return "Juhania";
            case "translatiivi":
                return "Juhaniksi";
            case "inessiivi":
                return "Juhanissa";
            case "elatiivi":
                return "Juhanista";
            case "illatiivi":
                return "Juhaniin";
            case "adessiivi":
                return "Juhanilla";
            case "ablatiivi":
                return "Juhanilta";
            case "allatiivi":
                return "Juhanille";
            default:
                return "Juhani";
        }
    }

    public Huume annaHuume() {
        return new Huume(false, 0, 0);
    }
    
    public Juhani(boolean määritettySijainti, int sijX, int sijY){
        super(määritettySijainti, sijX, sijY);
        super.nimi = "Juhani";
        super.kuvake = new ImageIcon("tiedostot/kuvat/kenttäkohteet/juhani.gif");
        super.katsomisTeksti = "Osta Juhanilta kahel kybäl yksi huume pois.";
        super.asetaTiedot();
    }
}
