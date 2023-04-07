package keimo;
import javax.swing.JOptionPane;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.text.DecimalFormat;

public class CustomViestiIkkunat {

    public static class SuljeVirheenJälkeen {

        static String viesti = "\n\nKäsittämätön poikkeus sovelluksessa.";
        static String otsikko = "Fataali häire!";
        static int valitaTyyppi = JOptionPane.OK_CANCEL_OPTION;
        static int viestiTyyppi = JOptionPane.ERROR_MESSAGE;
        static Icon kuvake = null;
        static String[] vaihtoehdot = {"Yritä uudestaan (ihan turhaa mut laitoin tän kuitenkin)", "Sulje sovellus"};

        static int showDialog() {
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

        static int showDialog(double aika) {
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

        static int showDialog(String hävionSyy) {
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

        static int showDialog() {
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class Credits {
        
        static String viesti = "Jonttu:\n-ohjelmointi\n\nUdo:\n-grafiikat\n-vuh-ääni\n\nKrisy:\n-kenttäsuunnittelu\n-tarina";
        static String otsikko = "Kehittäjät";
        static int valitaTyyppi = JOptionPane.OK_OPTION;
        static int viestiTyyppi = JOptionPane.INFORMATION_MESSAGE;
        static Icon kuvake = null;
        static String[] vaihtoehdot = {"Ok"};

        static int showDialog() {
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class Ohjeet {
        
        static String viesti = "Kontrollit\n\nNuolet / WASD: Liiku\nSpace: käytä esinettä\n1-5: Vaihda tavarapaikkaa\nE: Poimi\nQ: Pudota\nF: Vuorovaikutus\nZ: Yhdistä\nX: Katso esinettä\nC: Katso kentän kohdetta\nR: Järjestä tavaraluettelo\n\n\nLisäkomennot \n\nF2: Pakota ruudunpäivitys\n\n\nTavoitteet\n\n1: Sytytä nuotio\n2: Avaa kirstu\n3: Syö";
        static String otsikko = "Ohjeet";
        static int valitaTyyppi = JOptionPane.OK_OPTION;
        static int viestiTyyppi = JOptionPane.INFORMATION_MESSAGE;
        static Icon kuvake = null;
        static String[] vaihtoehdot = {"Ok"};

        static int showDialog() {
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class Kaksoispistedee {
        
        static String viesti = ":D";
        static String otsikko = ":D";
        static int valitaTyyppi = JOptionPane.YES_NO_CANCEL_OPTION;
        static int viestiTyyppi = JOptionPane.INFORMATION_MESSAGE;
        static Icon kuvake = new ImageIcon("tiedostot/kuvat/kaksoispistedee_sivuttain.png");
        static String[] vaihtoehdot = {":D",":D",":D"};

        static int showDialog() {
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class IsoKenttäVaroitus {
        
        static String viesti = "Ei ehkä kannata tehdä noin isoa kenttää. Muutama syy: \n -UI:ta ei ole suuniteltu yli 10-koon kentälle, joten \nse voi mennä yli näytöstäsi(olettaen, että sinulla on 1920 x 1080 -näyttö)\n -se voi lagittaa aika paljon \n -todennäköisesti rikkoo koko pelin";
        static String otsikko = "Melko iso kenttä";
        static int valitaTyyppi = JOptionPane.OK_CANCEL_OPTION;
        static int viestiTyyppi = JOptionPane.WARNING_MESSAGE;
        static Icon kuvake = null;
        static String[] vaihtoehdot = {"Tee silti :D", "Kankel"};

        static int showDialog() {
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

        static int showDialog() {
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

        static int showDialog(String dialogiTeksti) {
            return JOptionPane.showOptionDialog(null, dialogiTeksti, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class FataaliVirhe {
        
        static int valitaTyyppi = JOptionPane.YES_NO_CANCEL_OPTION;
        static int viestiTyyppi = JOptionPane.ERROR_MESSAGE;
        //static Icon kuvake = new ImageIcon("tiedostot/kuvat/kaksoispistedee.png");
        static String[] vaihtoehdot = {"Jatka (ei suositella)", "Sulje sovellus", "Takaisin peliin (sulje editori)"};

        static int showDialog(String viesti, String otsikko) {
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, null, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class EditorinTyhjennys {
        
        static int valitaTyyppi = JOptionPane.YES_NO_OPTION;
        static int viestiTyyppi = JOptionPane.QUESTION_MESSAGE;
        //static Icon kuvake = new ImageIcon("tiedostot/kuvat/kaksoispistedee.png");
        static String[] vaihtoehdot = {"Kyllä", "Ei"};

        static int showDialog(String viesti, String otsikko) {
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, null, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class TiedostonKorvaus {
        
        static int valitaTyyppi = JOptionPane.YES_NO_OPTION;
        static int viestiTyyppi = JOptionPane.WARNING_MESSAGE;
        //static Icon kuvake = new ImageIcon("tiedostot/kuvat/kaksoispistedee.png");
        static String[] vaihtoehdot = {"Kyllä", "Ei"};

        static int showDialog(String viesti, String otsikko) {
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, null, vaihtoehdot, vaihtoehdot[0]);
        }
    }

    public static class WarpinMuokkausVirhe {
        
        static int valitaTyyppi = JOptionPane.OK_OPTION;
        static int viestiTyyppi = JOptionPane.ERROR_MESSAGE;
        //static Icon kuvake = new ImageIcon("tiedostot/kuvat/kaksoispistedee.png");
        static String[] vaihtoehdot = {"OK"};

        static int showDialog(String viesti, String otsikko) {
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

        static int showDialog() {
            return JOptionPane.showOptionDialog(null, viesti, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[0]);
        }
    }
}
