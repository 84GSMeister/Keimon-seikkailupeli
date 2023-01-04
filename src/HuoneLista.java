import java.util.ArrayList;
import java.util.Random;

public class HuoneLista {
    
    static KenttäKohde[][] pelikenttä = new KenttäKohde[Main.kentänKoko][Main.kentänKoko];
    static Maasto[][] maastokenttä = new Maasto[Main.kentänKoko][Main.kentänKoko];
    static int esineitäKentällä = 0;
    static Random r = new Random();

    static ArrayList<KenttäKohde> huone0Kenttä = new ArrayList<KenttäKohde>();
    static ArrayList<Maasto> huone0Maasto = new ArrayList<Maasto>();
    static ArrayList<KenttäKohde> huone1Kenttä = new ArrayList<KenttäKohde>();
    static ArrayList<Maasto> huone1Maasto = new ArrayList<Maasto>();
    static ArrayList<KenttäKohde> huone2Kenttä = new ArrayList<KenttäKohde>();
    static ArrayList<Maasto> huone2Maasto = new ArrayList<Maasto>();
    static ArrayList<KenttäKohde> huone3Kenttä = new ArrayList<KenttäKohde>();
    static ArrayList<Maasto> huone3Maasto = new ArrayList<Maasto>();
    static ArrayList<KenttäKohde> huone4Kenttä = new ArrayList<KenttäKohde>();
    static ArrayList<Maasto> huone4Maasto = new ArrayList<Maasto>();
    static ArrayList<KenttäKohde> huone5Kenttä = new ArrayList<KenttäKohde>();
    static ArrayList<Maasto> huone5Maasto = new ArrayList<Maasto>();

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

    static KenttäKohde[][] luoVakioKenttä(int huoneenId) {

        pelikenttä = new KenttäKohde[Main.kentänKoko][Main.kentänKoko];
        switch (huoneenId) {
            default:
                huone0Kenttä.removeAll(huone0Kenttä);
                huone0Kenttä.add(new ReunaWarppi(3, 0, 1, 3, 9, ReunaWarppi.Suunta.YLÖS));
                huone0Kenttä.add(new ReunaWarppi(5 , 9, 2, 5, 0, ReunaWarppi.Suunta.ALAS));
                huone0Kenttä.add(new ReunaWarppi(9, 3, 3, 0, 3, ReunaWarppi.Suunta.OIKEA));
                huone0Kenttä.add(new ReunaWarppi(0, 7, 4, 9, 7, ReunaWarppi.Suunta.VASEN));
                huone0Kenttä.add(new Suklaalevy());
                huone0Kenttä.add(new Vesiämpäri());
                break;
            case 1:
                huone1Kenttä.removeAll(huone1Kenttä);
                huone1Kenttä.add(new ReunaWarppi(3, 9, 0, 3, 0, ReunaWarppi.Suunta.ALAS));
                huone1Kenttä.add(new Hiili());
                huone1Kenttä.add(new Kaasusytytin("tyhjä"));
                huone1Kenttä.add(new Makkara());
                huone1Kenttä.add(new Suklaalevy());
                huone1Kenttä.add(new Kilpi());
                huone1Kenttä.add(new Makkara());
                huone1Kenttä.add(new Makkara());
                huone1Kenttä.add(new Makkara());
                break;
            case 2:
                huone2Kenttä.removeAll(huone2Kenttä);
                huone2Kenttä.add(new ReunaWarppi(5, 0, 0, 5, 9, ReunaWarppi.Suunta.YLÖS));
                huone2Kenttä.add(new Suklaalevy());
                huone2Kenttä.add(new Kaasupullo());
                huone2Kenttä.add(new Makkara());
                huone2Kenttä.add(new Makkara());
                huone2Kenttä.add(new PikkuVihu());
                huone2Kenttä.add(new PikkuVihu());
                huone2Kenttä.add(new PikkuVihu());
                huone2Kenttä.add(new PikkuVihu());
                huone2Kenttä.add(new PikkuVihu());
                break;
            case 3:
                huone3Kenttä.removeAll(huone3Kenttä);
                huone3Kenttä.add(new ReunaWarppi(0, 3, 0, 9, 3, ReunaWarppi.Suunta.VASEN));
                huone3Kenttä.add(new Vesiämpäri());
                huone3Kenttä.add(new Avain());
                huone3Kenttä.add(new Paperi());
                huone3Kenttä.add(new Makkara());
                huone3Kenttä.add(new Makkara());
                huone3Kenttä.add(new Makkara());
                huone3Kenttä.add(new PikkuVihu());
                huone3Kenttä.add(new PikkuVihu());
                huone3Kenttä.add(new PikkuVihu());
                break;
            case 4:
                huone4Kenttä.removeAll(huone4Kenttä);
                huone4Kenttä.add(new ReunaWarppi(9, 7, 0, 0, 7, ReunaWarppi.Suunta.OIKEA));
                huone4Kenttä.add(new ReunaWarppi(0, 2, 5, 9, 2, ReunaWarppi.Suunta.VASEN));
                huone4Kenttä.add(new Vesiämpäri());
                huone4Kenttä.add(new PikkuVihu(1,1));
                huone4Kenttä.add(new PikkuVihu(3,4));
                huone4Kenttä.add(new PikkuVihu(5,7));
                break;
            case 5:
                huone5Kenttä.removeAll(huone5Kenttä);
                huone5Kenttä.add(new ReunaWarppi(9, 2, 4, 0, 2, ReunaWarppi.Suunta.OIKEA));
                huone5Kenttä.add(new Nuotio(2,2));
                huone5Kenttä.add(new Kirstu(4,2));
                huone5Kenttä.add(new Ämpärikone());
                huone5Kenttä.add(new PikkuVihu());
                break;
        }
        return pelikenttä;
    }

    static Maasto[][] luoVakioMaasto(int huoneenId) {

        maastokenttä = new Maasto[Main.kentänKoko][Main.kentänKoko];
        switch (huoneenId) {
            default:
                huone0Maasto.removeAll(huone0Maasto);
                huone0Maasto.add(new Hiekka(1,3));
                huone0Maasto.add(new Seinä(8,8));
                huone0Maasto.add(new Seinä(8,9));
                huone0Maasto.add(new Seinä(9,8));
                huone0Maasto.add(new Seinä(9,9));
                huone0Maasto.add(new Hiekka());
                huone0Maasto.add(new Hiekka());
                huone0Maasto.add(new Hiekka());
                break;
            case 1:
                huone1Maasto.removeAll(huone1Maasto);
                huone1Maasto.add(new Vesi(2,1));
                huone1Maasto.add(new Hiekka(1,3));
                huone1Maasto.add(new Hiekka());
                huone1Maasto.add(new Hiekka());
                huone1Maasto.add(new Hiekka());
                break;
            case 2:
                huone2Maasto.removeAll(huone2Maasto);
                huone2Maasto.add(new Hiekka(1,3));
                huone2Maasto.add(new Vesi(2,1));
                huone2Maasto.add(new Hiekka());
                huone2Maasto.add(new Hiekka());
                huone2Maasto.add(new Hiekka());
                huone2Maasto.add(new Vesi());
                huone2Maasto.add(new Vesi());
                huone2Maasto.add(new Vesi());
                break;
            case 3:
                huone3Maasto.removeAll(huone3Maasto);
                huone3Maasto.add(new Vesi(7,1));
                huone3Maasto.add(new Vesi(7,2));
                huone3Maasto.add(new Vesi(7,3));
                huone3Maasto.add(new Vesi(7,4));
                huone3Maasto.add(new Vesi(8,1));
                huone3Maasto.add(new Vesi(8,2));
                huone3Maasto.add(new Vesi(8,3));
                huone3Maasto.add(new Vesi(8,4));
                huone3Maasto.add(new Vesi(8,5));
                huone3Maasto.add(new Hiekka());
                huone3Maasto.add(new Hiekka());
                huone3Maasto.add(new Hiekka());
                break;
        }
        return maastokenttä;
    }
}
