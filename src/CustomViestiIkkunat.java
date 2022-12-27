import javax.swing.JOptionPane;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.text.DecimalFormat;

public class CustomViestiIkkunat {
    
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
            return JOptionPane.showOptionDialog(null, viesti + "\n\nAikasi: " + minuutit + ":" + sekunnit2Desimaalia, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[0]);
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
        
        static String viesti = "Koodaan tän joskus syssymmällä";
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
        
        static String viesti = "Jonttu:\n-ohjelmointi\n\nUdo:\n-grafiikat\n-vuh-ääni";
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
        
        static String viesti = "Kontrollit\n\nNuolet / WASD: Liiku\nSpace: käytä esinettä\n1-5: Vaihda tavarapaikkaa\nE: Poimi\nQ: Pudota\nF: Erikoiskäyttö\nZ: Yhdistä\nX: Katso esinettä\nC: Katso kentän kohdetta\nR: Järjestä tavaraluettelo\n\n\nLisäkomennot \n\nF2: Pakota ruudunpäivitys\n\n\nTavoitteet\n\n1: Sytytä nuotio\n2: Avaa kirstu\n3: Syö";
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
        
        static String viesti = "Ei ehkä kannata tehdä noin isoa kenttää, sillä se voi mennä yli näytöstäsi\n(olettaen, että sinulla on 1920 x 1080 -näyttö).\n\nLisäksi se voi lagittaa aika paljon.";
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
        static String[] vaihtoehdot = {"Täytyyks tästä painaa joka helvetin kerta?"};

        static int showDialog(String dialogiTeksti) {
            return JOptionPane.showOptionDialog(null, dialogiTeksti, otsikko, valitaTyyppi, viestiTyyppi, kuvake, vaihtoehdot, vaihtoehdot[0]);
        }
    }
}
