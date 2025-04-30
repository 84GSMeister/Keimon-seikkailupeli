package keimo.HuoneEditori;

import keimo.*;

import javax.swing.JOptionPane;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.text.DecimalFormat;

public class CustomViestiIkkunat {

    public static class SuljeVirheenJälkeen {

        static String viesti = "Käsittämätön poikkeus sovelluksessa. \n\nSanokaa sille jontulle että vetää käteen ja korjaa paskan softansa.";
        static String otsikko = "Fataali häire!";
        static int valitaTyyppi = JOptionPane.OK_CANCEL_OPTION;
        static int viestiTyyppi = JOptionPane.ERROR_MESSAGE;
        static Icon kuvake = null;
        static String[] vaihtoehdot = {"Yritä uudestaan (ihan turhaa mut laitoin tän kuitenkin)", "Sulje sovellus"};

        public static int näytäDialogi(String virheViesti) {
            return JOptionPane.showOptionDialog(null, viesti + "\n\n" + virheViesti, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[1]);
        }
    }

    public static class MuutoksetEditorissa {

        static String viesti = "Nykyiseen kenttään on tehty muutoksia. Haluatko silti avata uuden?";
        static String otsikko = "Muutoksia ei tallenneta";
        static int valitaTyyppi = JOptionPane.OK_CANCEL_OPTION;
        static int viestiTyyppi = JOptionPane.QUESTION_MESSAGE;
        static Icon kuvake = null;
        static String[] vaihtoehdot = {"OK","Peruuta"};

        public static int näytäDialogi() {
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[1]);
        }
    }
    
    public static class Loppuonnittelu {
        
        static String viesti = "Voitit pelin!";
        static String otsikko = "Voitit pelin!";
        static int valitaTyyppi = JOptionPane.OK_OPTION;
        static int viestiTyyppi = JOptionPane.INFORMATION_MESSAGE;
        static Icon kuvake = new ImageIcon("tiedostot/kuvat/kaksoispistedee.png");
        static String[] vaihtoehdot = {"Jee!"};
        static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");

        public static int näytäDialogi(double aika) {
            int minuutit = (int)aika / 60;
            double sekunnit = (aika % 60);
            String sekunnit2Desimaalia = kaksiDesimaalia.format(sekunnit);
            viesti += "\n\nAikasi: " + minuutit + ":" + sekunnit2Desimaalia;
            viesti += "\n\nLyödyt viholliset: " + TarkistettavatArvot.annaLyödytVihut();
            viesti += "\nÄmpäröidyt viholliset: " + TarkistettavatArvot.annaÄmpäröidytVihut();
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class LoppuHäviö {
        
        static String viesti = "\n\nHävisit pelin!";
        static String otsikko = "Hävisit pelin!";
        static int valitaTyyppi = JOptionPane.OK_CANCEL_OPTION;
        static int viestiTyyppi = JOptionPane.INFORMATION_MESSAGE;
        static Icon kuvake = new ImageIcon("tiedostot/kuvat/surunaama.png");
        static String[] vaihtoehdot = {"Uusi peli", "Lopeta"};

        public static int näytäDialogi(String hävionSyy) {
            viesti = hävionSyy + "\n\nHävisit pelin!";
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class ToteutusPuuttuu {
        
        static String viesti = "Koodaan tän joskus syssymmällä\n\nOdotathan kärsivällisesti :)";
        static String otsikko = "Toteutus puuttuu";
        static int valitaTyyppi = JOptionPane.YES_NO_CANCEL_OPTION;
        static int viestiTyyppi = JOptionPane.INFORMATION_MESSAGE;
        static Icon kuvake = new ImageIcon("tiedostot/kuvat/kaksoispistedee.png");
        static String[] vaihtoehdot = {"Oke", "En usko", "Onks tää vaihtoehto? :D"};

        public static int näytäDialogi() {
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class ToteutusPuuttuu2 {
        
        static String viesti = "Koodaan tän joskus syssymmällä\n\nOdotathan kärsivällisesti :)";
        static String otsikko = "Toteutus puuttuu";
        static int valitaTyyppi = JOptionPane.YES_NO_CANCEL_OPTION;
        static int viestiTyyppi = JOptionPane.INFORMATION_MESSAGE;
        static Icon kuvake = new ImageIcon("tiedostot/kuvat/kaksoispistedee.png");
        static String[] vaihtoehdot = {"Oke"};

        public static int näytäDialogi() {
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class Credits {
        
        //static String viesti = "Jonttu:\n-ohjelmointi\n\nUdo:\n-grafiikat\n-vuh-ääni\n\nKrisy:\n-kenttäsuunnittelu\n-tarina";
        static String viesti = "Tuottaja:\n    Joonatan\n\n" +
        "Pelisuunnittelu:\n    Joonatan\n\n" +
        "Ohjelmointi:\n    Joonatan\n\n" +
        "Grafiikat ja animaatiot:\n    Unto\n\n" +
        "Äänisuunnittelu:\n    Joonatan\n\n" +
        "Musat:\n    Joonatan\n    Unto\n\n" +
        "Kenttäsuunnittelu:\n    Kristian\n    Unto\n    Joonatan\n\n" +
        "Tarina:\n    Kristian\n\n";
        static String otsikko = "Kehittäjät";
        static int valitaTyyppi = JOptionPane.OK_OPTION;
        static int viestiTyyppi = JOptionPane.INFORMATION_MESSAGE;
        static Icon kuvake = null;
        static String[] vaihtoehdot = {"Ok"};

        public static int näytäDialogi() {
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class Ohjeet {
        
        static String viesti = "Kontrollit:\n\nNuolet / WASD: Liiku\nSpace: käytä esinettä\n1-6: Vaihda tavarapaikkaa\nE: Poimi / Vuorovaikutus\nQ: Pudota\nZ: Yhdistä\nX: Katso esinettä\n\nLisäkontrollit (näitä ei tarvitse pelissä):\n\nR: Järjestä tavaraluettelo\nF: Vuorovaikutus (kuten E, mutta ei poimi esineitä)\nC: Katso kentän kohdetta (Kuten E/F, mutta ei käytä esinettä)\n\n\nSeuraava Tavoite:\n\n";
        static String otsikko = "Ohjeet";
        static int valitaTyyppi = JOptionPane.OK_OPTION;
        static int viestiTyyppi = JOptionPane.INFORMATION_MESSAGE;
        static Icon kuvake = null;
        static String[] vaihtoehdot = {"Ok"};

        public static int näytäDialogi(String tavoite) {
            return JOptionPane.showOptionDialog(null, viesti + "" + tavoite, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class Kaksoispistedee {
        
        static String viesti = ":D";
        static String otsikko = ":D";
        static int valitaTyyppi = JOptionPane.YES_NO_CANCEL_OPTION;
        static int viestiTyyppi = JOptionPane.INFORMATION_MESSAGE;
        static Icon kuvake = new ImageIcon("tiedostot/kuvat/kaksoispistedee_sivuttain.png");
        static String[] vaihtoehdot = {":D",":D",":D"};

        public static int näytäDialogi() {
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class IsoKenttäVaroitus {
        
        static String viesti;
        static String otsikko = "Melko iso kenttä";
        static int valitaTyyppi = JOptionPane.OK_CANCEL_OPTION;
        static int viestiTyyppi = JOptionPane.WARNING_MESSAGE;
        static Icon kuvake = null;
        static String[] vaihtoehdot = {"Tee silti :D", "Kankel"};

        public static int näytäDialogi(int kentänKoko) {
            viesti = "Kentän kooksi on valittu " + kentänKoko + ".\n\nPeli on alun perin suunniteltu 10x10-kokoiseksi.\nYli 10-kokoiset kentät ovat tällä hetkellä kokeellinen ominaisuus.";
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class NollaKenttäVaroitus {
        
        static String viesti;
        static String otsikko = "Kentän koko = 0";
        static int valitaTyyppi = JOptionPane.OK_CANCEL_OPTION;
        static int viestiTyyppi = JOptionPane.WARNING_MESSAGE;
        static Icon kuvake = null;
        static String[] vaihtoehdot = {"<html><p>Ymmärrän asian.</p><p>Olen testaaja ja haluan kokeilla ominaisuutta.</p><p>Tulipas muuten iso nappi.</p></html>", "Kankel"};

        public static int näytäDialogi(int kentänKoko) {
            viesti = "Kentän koon muuttaminen on tällä hetkellä kokeellinen ominaisuus.\n\nNollakenttä voi aiheuttaa ei-toivottuja tuloksia.";
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class NegatiivinenKenttäVirhe {
        
        static String viesti;
        static String otsikko = "Negatiivinen kenttä";
        static int valitaTyyppi = JOptionPane.OK_CANCEL_OPTION;
        static int viestiTyyppi = JOptionPane.ERROR_MESSAGE;
        static Icon kuvake = null;
        static String[] vaihtoehdot = {"En tiedä itsekään."};

        public static int näytäDialogi(int kentänKoko) {
            viesti = "Huoneen koko: " + kentänKoko + "\n\nMitä helvettiä se edes tarkoittaa? :DD";
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class SulkumerkkiVaroitus {
        
        static String viesti = "Sulkumerkit nimessä eivät ole sallittuja.";
        static String otsikko = "Virheellinen merkki";
        static int valitaTyyppi = JOptionPane.OK_OPTION;
        static int viestiTyyppi = JOptionPane.ERROR_MESSAGE;
        static Icon kuvake = null;
        static String[] vaihtoehdot = {"Aasia kunnossa"};

        public static int näytäDialogi() {
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class HuoneenVaihtoDialogi {
        
        //static String viesti = "Tämä teksti tulee kun vaihtaa huonetta.";
        static String otsikko = "Otsikko";
        static int valitaTyyppi = JOptionPane.OK_OPTION;
        static int viestiTyyppi = JOptionPane.INFORMATION_MESSAGE;
        static Icon kuvake = new ImageIcon("tiedostot/kuvat/kaksoispistedee_sivuttain.png");
        static String[] vaihtoehdot = {"OK"};

        public static int näytäDialogi(String dialogiTeksti) {
            return JOptionPane.showOptionDialog(null, dialogiTeksti, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class FataaliVirhe {
        
        static int valitaTyyppi = JOptionPane.YES_NO_CANCEL_OPTION;
        static int viestiTyyppi = JOptionPane.ERROR_MESSAGE;
        //static Icon kuvake = new ImageIcon("tiedostot/kuvat/kaksoispistedee.png");
        static String[] vaihtoehdot = {"Jatka", "Sulje sovellus", "Takaisin peliin (sulje editori)"};

        public static int näytäDialogi(String viesti, String otsikko) {
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, null, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class GrafiikkäSäieVirhe {
        
        static int valitaTyyppi = JOptionPane.YES_NO_OPTION;
        static int viestiTyyppi = JOptionPane.ERROR_MESSAGE;
        //static Icon kuvake = new ImageIcon("tiedostot/kuvat/kaksoispistedee.png");
        static String[] vaihtoehdot = {"Uudelleenkäynnistä säie", "Sulje sovellus"};

        public static int näytäDialogi(String viesti, String otsikko) {
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, null, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class EditorinTyhjennys {
        
        static int valitaTyyppi = JOptionPane.YES_NO_OPTION;
        static int viestiTyyppi = JOptionPane.QUESTION_MESSAGE;;
        static String[] vaihtoehdot = {"Kyllä", "Ei"};

        public static int näytäDialogi(String viesti, String otsikko) {
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, null, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class TiedostonKorvaus {
        
        static int valitaTyyppi = JOptionPane.YES_NO_OPTION;
        static int viestiTyyppi = JOptionPane.WARNING_MESSAGE;
        static String[] vaihtoehdot = {"Kyllä", "Ei"};

        public static int näytäDialogi(String viesti, String otsikko) {
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, null, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class WarpinMuokkausVirhe {
        
        static int valitaTyyppi = JOptionPane.OK_OPTION;
        static int viestiTyyppi = JOptionPane.ERROR_MESSAGE;
        static String[] vaihtoehdot = {"OK"};

        public static int näytäDialogi(String viesti, String otsikko) {
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, null, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class TiedostonTallennusVaroitus {
        
        static String viesti = "Huom! Kenttä pyyhkiytyy aina uuden pelin jälkeen, jollei sitä ole tallennettu tiedostoon.";
        static String otsikko = "Kokeile kenttää";
        static int valitaTyyppi = JOptionPane.YES_NO_CANCEL_OPTION;
        static int viestiTyyppi = JOptionPane.WARNING_MESSAGE;
        static Icon kuvake = null;
        static String[] vaihtoehdot = {"OK","Älä näytä uudestaan","Peruuta"};

        public static int näytäDialogi() {
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class UudelleenkäynnistäAsetukset {
        
        static String viesti = "Jotkin asetukset eivät tallennu ennen uudelleenkäynnistystä";
        static String otsikko = "Hyväksy asetukset";
        static int valitaTyyppi = JOptionPane.YES_NO_OPTION;
        static int viestiTyyppi = JOptionPane.WARNING_MESSAGE;
        static Icon kuvake = null;
        static String[] vaihtoehdot = {"OK","Peruuta"};

        public static int näytäDialogi() {
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class PoistaDialogiPätkä {
        
        static String viesti = "Haluatko varmasti poistaa dialogipätkän?";
        static String otsikko = "Poista dialogipätkä";
        static int valitaTyyppi = JOptionPane.YES_NO_OPTION;
        static int viestiTyyppi = JOptionPane.QUESTION_MESSAGE;
        static Icon kuvake = null;
        static String[] vaihtoehdot = {"Kyllä","Ei"};

        public static int näytäDialogi() {
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class DialogiKuvanKokoHuomautus {
        
        static String otsikko = "Epäoptimaalinen kuvan koko";
        static int valitaTyyppi = JOptionPane.YES_NO_OPTION;
        static int viestiTyyppi = JOptionPane.QUESTION_MESSAGE;
        static Icon kuvake = null;
        static String[] vaihtoehdot = {"Joo","En"};

        public static int näytäDialogi(String viesti) {
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class HuonettaEiLöytynytHuomautus {

        static String otsikko = "Huonetta ei löytynyt";
        static int valitaTyyppi = JOptionPane.YES_NO_OPTION;
        static int viestiTyyppi = JOptionPane.QUESTION_MESSAGE;
        static Icon kuvake = null;
        static String[] vaihtoehdot = {"Kyllä","Ei"};

        public static int näytäDialogi(String viesti) {
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class EiWarppia {

        static String otsikko = "Ei warppia";
        static int valitaTyyppi = JOptionPane.YES_NO_CANCEL_OPTION;
        static int viestiTyyppi = JOptionPane.QUESTION_MESSAGE;
        static Icon kuvake = null;
        static String[] vaihtoehdot = {"Aseta warp","Luo uusi huone","Peruuta"};

        public static int näytäDialogi(String viesti) {
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[2]);
        }
    }
}
