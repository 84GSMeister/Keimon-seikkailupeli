import javax.swing.ImageIcon;

public class GrafiikanPäivitysSäie extends Thread {

    long alkuAika = 0;
    long loppuAika = 0;
    ImageIcon pelaajanKuvake = new ImageIcon("tiedostot/kuvat/pelaaja.png");

    void valitsePelaajanKuvake() {
        int pelaajanKuvakeInt = Main.pelaajanKylläisyys;
        switch (pelaajanKuvakeInt) {
            case 0:
                pelaajanKuvake = new ImageIcon("tiedostot/kuvat/pelaaja.png");
                break;
            case 1:
                pelaajanKuvake = new ImageIcon("tiedostot/kuvat/pelaaja_1.png");
                break;
            case 2:
                pelaajanKuvake = new ImageIcon("tiedostot/kuvat/pelaaja_2.png");
                break;
            case 3:
                pelaajanKuvake = new ImageIcon("tiedostot/kuvat/pelaaja_3.png");
                break;
            case 4:
                pelaajanKuvake = new ImageIcon("tiedostot/kuvat/pelaaja_4.png");
                break;
            default:
                pelaajanKuvake = new ImageIcon("tiedostot/kuvat/pelaaja.png");
                break;
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        //super.run();

        while (true) {
            alkuAika = System.nanoTime();
            try {
                this.sleep(16);
                PääIkkuna.vaatiiPäivityksen = true;
            }
            catch (InterruptedException e){

            }
            valitsePelaajanKuvake();
            PääIkkuna.päivitäIkkuna(Main.pelaajanSijX, Main.pelaajanSijY, pelaajanKuvake);
            loppuAika = System.nanoTime();
            long aikaErotusUs = (loppuAika - alkuAika)/1000;
            long aikaErotusMs = aikaErotusUs/1000;
            int fps = 1000000/ (int)aikaErotusUs;
            PääIkkuna.yläteksti4.setText("Päivitysaika: " + aikaErotusMs + " ms");
            PääIkkuna.yläteksti5.setText("FPS: " + fps);
        }
    }
}