package keimo;

import java.util.ArrayList;

public class TavoiteLista {
    
    public static ArrayList<Tavoite> tavoiteLista = new ArrayList<Tavoite>();
    public static int tavoitteitaSuoritettu = 0;

    static class Tavoite {
        String tavoite = "";
        boolean suoritettu = false;

        Tavoite(String tavoite) {
            this.tavoite = tavoite;
            this.suoritettu = false;
        }
    }

    static void suoritaTavoite() {
        tavoiteLista.get(tavoitteitaSuoritettu).suoritettu = true;
        tavoitteitaSuoritettu++;
    }

    static void luoTavoiteLista() {
        tavoitteitaSuoritettu = 0;
        tavoiteLista.add(new Tavoite("Löydä takaisin kotiin"));
        tavoiteLista.add(new Tavoite("Etsi pesäpallomaila"));
    }

}
