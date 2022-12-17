import java.util.Random;

public class HuoneLista {
    
    static KenttäKohde[][] pelikenttä = new KenttäKohde[Main.kentänKoko][Main.kentänKoko];
    static int esineitäKentällä = 0;
    static Random r = new Random();

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
                sijoitaSatunnaiseenRuutuun(new Nuotio());
                sijoitaSatunnaiseenRuutuun(new Kirstu());
                sijoitaSatunnaiseenRuutuun(new Makkara());
                sijoitaSatunnaiseenRuutuun(new Ämpärikone());
                break;
            case 1:
                sijoitaSatunnaiseenRuutuun(new Hiili());
                sijoitaSatunnaiseenRuutuun(new Kaasusytytin("tyhjä"));
                sijoitaSatunnaiseenRuutuun(new Makkara());
                sijoitaSatunnaiseenRuutuun(new Suklaalevy());
                sijoitaSatunnaiseenRuutuun(new PikkuVihu());
                sijoitaSatunnaiseenRuutuun(new Kilpi());
                sijoitaSatunnaiseenRuutuun(new ReunaWarppi(2, 0, 0, ReunaWarppi.Suunta.OIKEA));
                break;
            case 2:
                sijoitaSatunnaiseenRuutuun(new Suklaalevy());
                sijoitaSatunnaiseenRuutuun(new Kaasupullo());
                sijoitaSatunnaiseenRuutuun(new Makkara());
                sijoitaSatunnaiseenRuutuun(new Makkara());
                sijoitaSatunnaiseenRuutuun(new PikkuVihu());
                sijoitaSatunnaiseenRuutuun(new PikkuVihu());
                sijoitaSatunnaiseenRuutuun(new PikkuVihu());
                sijoitaSatunnaiseenRuutuun(new PikkuVihu());
                sijoitaSatunnaiseenRuutuun(new PikkuVihu());
                sijoitaSatunnaiseenRuutuun(new ReunaWarppi(3, 0, 0, ReunaWarppi.Suunta.OIKEA));
                break;
            case 3:
                sijoitaSatunnaiseenRuutuun(new Vesiämpäri());
                sijoitaSatunnaiseenRuutuun(new Avain());
                sijoitaSatunnaiseenRuutuun(new Paperi());
                sijoitaSatunnaiseenRuutuun(new Makkara());
                sijoitaSatunnaiseenRuutuun(new Makkara());
                sijoitaSatunnaiseenRuutuun(new Makkara());
                sijoitaSatunnaiseenRuutuun(new PikkuVihu());
                sijoitaSatunnaiseenRuutuun(new PikkuVihu());
                sijoitaSatunnaiseenRuutuun(new PikkuVihu());
                sijoitaSatunnaiseenRuutuun(new ReunaWarppi(1, 0, 0, ReunaWarppi.Suunta.OIKEA));
                break;
        }
        return pelikenttä;
    }
}
