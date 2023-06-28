package keimo.Säikeet;

import keimo.PelinAsetukset;
import keimo.PääIkkuna;
import keimo.Ruudut.PeliRuutu;

public class TekstiAjastinSäie extends Thread {

    public static String dialogiTeksti = "";
    
    protected void scrollaaDialogiTeksti() {
        if (PääIkkuna.tekstiAuki) {
            if (PääIkkuna.tekstiäJäljellä > 0) {
                String tulostettavaTeksti = dialogiTeksti.substring(0, dialogiTeksti.length()-PääIkkuna.tekstiäJäljellä +1);
                PeliRuutu.vuoropuheTeksti.setText(tulostettavaTeksti);
                PääIkkuna.tekstiäJäljellä--;
                //System.out.println(PääIkkuna.tekstiAuki);
            }
            pakotaOdotus(2_000_000/PelinAsetukset.tavoiteTickrate);
        }
        else {
            try {
                Thread.sleep(10);
            }
            catch (InterruptedException e) {
                System.out.println("Säie keskeytettiin.");
                e.printStackTrace();
            }
        }
    }

    public static void pakotaOdotus(long mikrosekunnit){
        long waitUntil = System.nanoTime() + (mikrosekunnit * 1_000);
        while(waitUntil > System.nanoTime()){
            
        }
    }

    @Override
    public void run() {
        while (true) {
            scrollaaDialogiTeksti();
        }
    }
}
