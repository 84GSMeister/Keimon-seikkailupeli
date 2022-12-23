import java.util.ArrayList;
import java.util.Random;

public class HuoneLista {
    
    static KenttäKohde[][] pelikenttä = new KenttäKohde[Main.kentänKoko][Main.kentänKoko];
    static int esineitäKentällä = 0;
    static Random r = new Random();

    static ArrayList<KenttäKohde> huone0 = new ArrayList<KenttäKohde>();
    static ArrayList<KenttäKohde> huone1 = new ArrayList<KenttäKohde>();
    static ArrayList<KenttäKohde> huone2 = new ArrayList<KenttäKohde>();
    static ArrayList<KenttäKohde> huone3 = new ArrayList<KenttäKohde>();

    /**
     * Arpoo satunnaisesti pelikentän x- ja y-koordinaatit.
     * Lisää arvottuun kohtaan syötteenä saadun KenttäKohde-tyyppisen olion
     * eli jonkin Esine-luokan tai Kiintopiste-luokan alaluokan olioista.
     * @.pre {
     * @param t instanceof KenttäKohde
     * }
     * @.post pelikenttä[randX][randY] != null
     */

    static void sijoitaSatunnaiseenRuutuun(KenttäKohde t){
        int randX = r.nextInt(Main.kentänKoko);
        int randY = r.nextInt(Main.kentänKoko);
        if (pelikenttä[randX][randY] == null) {
            pelikenttä[randX][randY] = t;
            esineitäKentällä++;
        }
        else {
            if (esineitäKentällä < Main.kentänKoko * Main.kentänKoko) {
                sijoitaSatunnaiseenRuutuun(t);
            }
            else {
                //JOptionPane.showMessageDialog(null, "Esineiden määrä yli kentän koon.\n\nViimeisimpänä spawnattu esine hylätään.", "Kenttä täynnä esineitä", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    static KenttäKohde[][] huoneet(int huoneenId) {

        pelikenttä = new KenttäKohde[Main.kentänKoko][Main.kentänKoko];
        switch (huoneenId) {
            default:
                huone0.removeAll(huone0);
                huone0.add(new Nuotio(2,2));
                huone0.add(new Kirstu(4,2));
                huone0.add(new Makkara());
                huone0.add(new Ämpärikone());
                huone0.add(new ReunaWarppi(1, 2, 2, ReunaWarppi.Suunta.OIKEA));
                break;
            case 1:
                huone1.removeAll(huone1);    
                huone1.add(new Hiili());
                huone1.add(new Kaasusytytin("tyhjä"));
                huone1.add(new Makkara());
                huone1.add(new Suklaalevy());
                huone1.add(new PikkuVihu());
                huone1.add(new Kilpi());
                huone1.add(new Makkara());
                huone1.add(new ReunaWarppi(2, 0, 0, ReunaWarppi.Suunta.VASEN));   
                break;
            case 2:
                huone2.removeAll(huone2);
                huone2.add(new Suklaalevy());
                huone2.add(new Kaasupullo());
                huone2.add(new Makkara());
                huone2.add(new Makkara());
                huone2.add(new PikkuVihu());
                huone2.add(new PikkuVihu());
                huone2.add(new PikkuVihu());
                huone2.add(new PikkuVihu());
                huone2.add(new PikkuVihu());
                huone2.add(new ReunaWarppi(3, 5, 5, ReunaWarppi.Suunta.YLÖS));
                break;
            case 3:
                huone3.removeAll(huone3);
                huone3.add(new Vesiämpäri());
                huone3.add(new Avain());
                huone3.add(new Paperi());
                huone3.add(new Makkara());
                huone3.add(new Makkara());
                huone3.add(new Makkara());
                huone3.add(new PikkuVihu());
                huone3.add(new PikkuVihu());
                huone3.add(new PikkuVihu());
                huone3.add(new ReunaWarppi(1, 0, 0, ReunaWarppi.Suunta.ALAS));
                break;
        }
        return pelikenttä;
    }
}
