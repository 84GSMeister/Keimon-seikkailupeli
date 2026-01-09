package keimo.seikkailupeli.io;

import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.SyöteLaitteet;
import keimo.seikkailupeli.Peli.SyötteenTila;
import keimo.seikkailupeli.gui.toimintoIkkunat.DialogiValintaIkkuna;
import keimo.seikkailupeli.gui.toimintoIkkunat.HuijauskoodiValikko;
import keimo.seikkailupeli.gui.toimintoIkkunat.KarttaIkkuna;
import keimo.seikkailupeli.gui.toimintoIkkunat.OhjeIkkuna;
import keimo.seikkailupeli.gui.toimintoIkkunat.PullonPalautusIkkuna;
import keimo.seikkailupeli.gui.toimintoIkkunat.ÄmpäriJonoIkkuna;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.MinipeliIkkuna3D;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.MinipeliIkkunaPokeri;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.MinipeliIkkunaPong;
import keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat.MinipeliIkkunaTetris;
import keimo.seikkailupeli.kenttä.Maailma3D;
import keimo.seikkailupeli.menu.KehittäjäRuutu;
import keimo.seikkailupeli.menu.LoppuRuutu;
import keimo.seikkailupeli.menu.TarinaRuutu;
import keimo.seikkailupeli.menu.ValikkoRuutu;
import keimo.seikkailupeli.menu.asetusRuudut.AsetusRuutu;
import keimo.seikkailupeli.menu.asetusRuudut.GrafiikkaAsetusRuutu;
import keimo.seikkailupeli.menu.asetusRuudut.OhjainAsetusRuutu;
import keimo.seikkailupeli.menu.asetusRuudut.PeliAsetusRuutu;
import keimo.seikkailupeli.menu.asetusRuudut.ÄäniAsetusRuutu;
import keimo.seikkailupeli.menu.asetusRuudut.äänitestiRuudut.ÄäniTestiMidi;
import keimo.seikkailupeli.menu.asetusRuudut.äänitestiRuudut.ÄäniTestiRuutu;
import keimo.seikkailupeli.menu.asetusRuudut.äänitestiRuudut.ÄäniTestiValikko;
import keimo.seikkailupeli.menu.asetusRuudut.äänitestiRuudut.ÄäniTestiWoof;
import keimo.seikkailupeli.menu.editori.EditoriRuutuVarmistus;
import keimo.seikkailupeli.objektit.Käännettävä.Suunta;
import keimo.seikkailupeli.objektit.Käännettävä.SuuntaVasenOikea;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.objektit.Pelaaja.KeimonState;
import keimo.seikkailupeli.toiminnot.Dialogit;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;
import net.java.games.input.Component.Identifier;

public class OhjainKomennot {

    public static boolean lAnalogVasenPainettu = false;
    public static boolean lAnalogOikeaPainettu = false;
    public static boolean lAnalogYlösPainettu = false;
    public static boolean lAnalogAlasPainettu = false;
    private static boolean lAnalogVapautettu = true;

    private static float value = 0;
    private static float analogValue = 0;
    private static float analogDeadzone = 0.25f;

    private static boolean rAnalogVasenPainettu = false;
    private static boolean rAnalogOikeaPainettu = false;
    private static boolean rAnalogYlösPainettu = false;
    private static boolean rAnalogAlasPainettu = false;
    private static boolean rAnalogVapautettu = true;

    public static void tarkistaSyöte() {
        tarkistaOhjainKomennot();
        tarkistaPainetutAnalogit();
        tarkistaPelaajanTila();
    }

    private static void tarkistaOhjainKomennot() {
        /* Get the available controllers */
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
        if (controllers.length == 0) {
            System.out.println("Found no controllers.");
            System.exit(0);
        }

        for (int i = 0; i < controllers.length; i++) {
            /* Remember to poll each one */
            controllers[i].poll();
            int ohjainNumero = -1;
            Type type = controllers[i].getType();
            if (type.equals(Type.GAMEPAD) || type.equals(Type.STICK)) {
                ohjainNumero++;
            }

            EventQueue queue = controllers[i].getEventQueue();
            Event event = new Event();

            while (queue.getNextEvent(event)) {
                Component comp = event.getComponent();
                Identifier id = comp.getIdentifier();
                if (comp.isAnalog()) {
                    analogValue = event.getValue();
                }
                else {
                    value = event.getValue();
                }

                if (ohjainNumero > -1) {
                    Peli.viimeisinSyöteLaite = SyöteLaitteet.PELIOHJAIN;
                    switch (Peli.aktiivinenRuutu) {
                        case PELIRUUTU -> {
                            switch (Peli.syötteenTila) {
                                case PELI -> {
                                    analogPohjassa(id, false);
                                    if (id.equals(Identifier.Axis.Z)) {
                                        if (analogValue < -analogDeadzone) { // Oikea takatriggeri (R2/RT)
                                            Peli.painaQ(Pelaaja.sijX, Pelaaja.sijY);
                                        }
                                        else if (analogValue > analogDeadzone) { // Vasen takatriggeri (L2/LT)
                                            System.out.println("LZ");
                                        }
                                    }
                                    if (id.equals(Identifier.Axis.RZ)) {
                                        if (analogValue < -analogDeadzone) System.out.println("RZ Painettu");
                                        else if (analogValue > analogDeadzone) System.out.println("RZ Vapautettu");
                                    }

                                    if (id.equals(Identifier.Axis.POV)) {
                                        if (event.getValue() == 0.25) { // DPAD Ylös
                                            tavarapaikanVaihto(-3);
                                        }
                                        else if (event.getValue() == 0.75) { // DPAD Alas
                                            tavarapaikanVaihto(3);
                                        }
                                        else if (event.getValue() == 1.0) { // DPAD Vasen
                                            tavarapaikanVaihto(-1);
                                        }
                                        else if (event.getValue() == 0.5) { // DPAD Oikea
                                            tavarapaikanVaihto(1);
                                        }
                                        else if (event.getValue() == 0.0) { // DPAD Neutraali
                                            
                                        }
                                    }
                                    
                                    if (id.equals(Identifier.Button.A) || id.equals(Identifier.Button._0)) {
                                        if (value == 1f) Peli.painaE(Pelaaja.sijX, Pelaaja.sijY);
                                    }
                                    if (id.equals(Identifier.Button.B) || id.equals(Identifier.Button._1)) {
                                        if (value == 1f) Peli.käyttö(Peli.esineValInt);
                                    }
                                    if (id.equals(Identifier.Button.X) || id.equals(Identifier.Button._2)) {
                                        if (value == 1f) Peli.katsoKenttää();
                                    }
                                    if (id.equals(Identifier.Button.Y) || id.equals(Identifier.Button._3)) {
                                        // Y / Ylänäppäin
                                    }
                                    if (id.equals(Identifier.Button.LEFT_THUMB) || id.equals(Identifier.Button._4)) {
                                        // L
                                    }
                                    if (id.equals(Identifier.Button.RIGHT_THUMB) || id.equals(Identifier.Button._5)) {
                                        if (value == 1f) Peli.katsoEsinettä();
                                    }
                                    
                                    if (id.equals(Identifier.Button.START) || id.equals(Identifier.Button._7)) {
                                        if (value == 1f) {
                                            Peli.pausetaPeli(true);
                                            DialogiValintaIkkuna.avaaToimintoIkkuna("pause");
                                        }
                                    }
                                    if (id.equals(Identifier.Button.SELECT) || id.equals(Identifier.Button._6)) {
                                        if (value == 1f) {
                                            Peli.painaZ();
                                        }
                                    }
                                    if (id.equals(Identifier.Button.LEFT_THUMB3) || id.equals(Identifier.Button._8)) {
                                        if (value == 1f) System.out.println("L stick pressed");
                                        else System.out.println("L stick released");
                                    }
                                    if (id.equals(Identifier.Button.RIGHT_THUMB3) || id.equals(Identifier.Button._9)) {
                                        if (value == 1f) System.out.println("R stick pressed");
                                        else System.out.println("R stick released");
                                    }
                                }
                                case DIALOGI -> {
                                    if (id.equals(Identifier.Button.A) || id.equals(Identifier.Button._0) || id.equals(Identifier.Button.X) || id.equals(Identifier.Button._2) || id.equals(Identifier.Button.START) || id.equals(Identifier.Button._7)) {
                                        if (value == 1f) Dialogit.kelaaDialogi();
                                    }
                                    vapautaAnalogit();
                                    Pelaaja.keimonState = KeimonState.IDLE;
                                }
                                case TOIMINTO -> {
                                    switch (Peli.toimintoIkkuna) {
                                        case VALINTADIALOGI -> {
                                            analog1Paino(id, false);
                                            if (id.equals(Identifier.Axis.POV)) {
                                                if (event.getValue() == 0.25) { // DPAD Ylös
                                                    DialogiValintaIkkuna.pienennäValintaa();
                                                }
                                                else if (event.getValue() == 0.75) { // DPAD Alas
                                                    DialogiValintaIkkuna.kasvataValintaa();
                                                }
                                            }
                                            if (id.equals(Identifier.Button.A) || id.equals(Identifier.Button._0)) {
                                                if (value == 1f) DialogiValintaIkkuna.hyväksyValinta();
                                            }
                                            else if (id.equals(Identifier.Button.START) || id.equals(Identifier.Button._7)) {
                                                if (value == 1f) DialogiValintaIkkuna.peruValinta();
                                            }
                                        }
                                        case ÄMPÄRIJONO -> {
                                            if (id.equals(Identifier.Button.A) || id.equals(Identifier.Button._0) || id.equals(Identifier.Button.START) || id.equals(Identifier.Button._7)) {
                                                if (value == 1f) ÄmpäriJonoIkkuna.keskeytetty = true;
                                            }
                                        }
                                        case PULLONPALAUTUS -> {
                                            if (id.equals(Identifier.Button.START) || id.equals(Identifier.Button._7)) {
                                                Peli.syötteenTila = SyötteenTila.PELI;
                                            }
                                            else if (PullonPalautusIkkuna.virheenTyyppi == PullonPalautusIkkuna.VirheenTyyppi.PAKKAUS) {
                                                if (id.equals(Identifier.Button.A) || id.equals(Identifier.Button._0)) {
                                                    if (value == 1f) PullonPalautusIkkuna.jatkoSyöteAnnettu = true;
                                                }
                                            }
                                            else if (PullonPalautusIkkuna.virheenTyyppi == PullonPalautusIkkuna.VirheenTyyppi.KÄSI) {
                                                if (id.equals(Identifier.Button.X) || id.equals(Identifier.Button._2)) {
                                                    if (value == 1f) PullonPalautusIkkuna.jatkoSyöteAnnettu = true;
                                                }
                                            }
                                            else if (PullonPalautusIkkuna.virheenTyyppi == PullonPalautusIkkuna.VirheenTyyppi.MUOTO) {
                                                if (id.equals(Identifier.Button.Y) || id.equals(Identifier.Button._3)) {
                                                    if (value == 1f) PullonPalautusIkkuna.jatkoSyöteAnnettu = true;
                                                }
                                            }
                                            else if (PullonPalautusIkkuna.virheenTyyppi == PullonPalautusIkkuna.VirheenTyyppi.MERKKI) {
                                                if (id.equals(Identifier.Button.B) || id.equals(Identifier.Button._1)) {
                                                    if (value == 1f) PullonPalautusIkkuna.jatkoSyöteAnnettu = true;
                                                }
                                            }
                                        }
                                        case KARTTA -> {
                                            if (id.equals(Identifier.Button.A) || id.equals(Identifier.Button._0) || id.equals(Identifier.Button.START) || id.equals(Identifier.Button._7)) {
                                                if (value == 1f) KarttaIkkuna.suljeToimintoIkkuna();
                                            }
                                        }
                                        case OHJEET -> {
                                            if (id.equals(Identifier.Button.A) || id.equals(Identifier.Button._0) || id.equals(Identifier.Button.START) || id.equals(Identifier.Button._7)) {
                                                if (value == 1f) OhjeIkkuna.suljeToimintoIkkuna();
                                            }
                                        }
                                        case HUIJAUSKOODIT -> {
                                            analog1Paino(id, false);
                                            if (id.equals(Identifier.Axis.POV)) {
                                                if (event.getValue() == 0.25) { // DPAD Ylös
                                                    HuijauskoodiValikko.pienennäValintaa();
                                                }
                                                else if (event.getValue() == 0.75) { // DPAD Alas
                                                    HuijauskoodiValikko.kasvataValintaa();
                                                }
                                            }
                                            if (id.equals(Identifier.Button.A) || id.equals(Identifier.Button._0) || id.equals(Identifier.Button.START) || id.equals(Identifier.Button._7)) {
                                                if (value == 1f) HuijauskoodiValikko.suljeValikko();
                                            }
                                        }
                                        case MINIPELI_PONG -> {
                                            analogPohjassa(id, false);
                                            if (id.equals(Identifier.Button.START) || id.equals(Identifier.Button._7)) {
                                                if (value == 1f) MinipeliIkkunaPong.suljeToimintoIkkuna();
                                            }
                                            if (id.equals(Identifier.Button.A) || id.equals(Identifier.Button._0)) {
                                                if (value == 1f) MinipeliIkkunaPong.ohitaValikko();
                                            }
                                        }
                                        case MINIPELI_POKERI -> {
                                            if (id.equals(Identifier.Button.START) || id.equals(Identifier.Button._7)) {
                                                if (value == 1f) MinipeliIkkunaPokeri.suljeToimintoIkkuna();
                                            }
                                            if (id.equals(Identifier.Button.A) || id.equals(Identifier.Button._0)) {
                                                if (value == 1f) {
                                                    MinipeliIkkunaPokeri.pelaaValitut();
                                                    MinipeliIkkunaPokeri.ohitaValikko();
                                                }
                                            }
                                        }
                                        case MINIPELI_TETRIS -> {
                                            analog1Paino(id, lAnalogOikeaPainettu);
                                            if (id.equals(Identifier.Button.START) || id.equals(Identifier.Button._7)) {
                                                if (value == 1f) MinipeliIkkunaTetris.suljeToimintoIkkuna();
                                            }
                                            if (id.equals(Identifier.Button.A) || id.equals(Identifier.Button._0)) {
                                                if (value == 1f) {
                                                    MinipeliIkkunaTetris.ohitaValikko();
                                                    MinipeliIkkunaTetris.pudotaPalikka();
                                                }
                                            }
                                            if (id.equals(Identifier.Button.X) || id.equals(Identifier.Button._2)) {
                                                if (value == 1f) {
                                                    MinipeliIkkunaTetris.käännäPalikkaa();
                                                }
                                            }
                                        }
                                        default -> {

                                        }
                                    }
                                }
                            }
                        }
                        case TARINARUUTU -> {
                            if (id.equals(Identifier.Button.START) || id.equals(Identifier.Button._7)) {
                                if (value == 1f) {
                                    TarinaRuutu.jatka();
                                }
                            }
                            else if (id.equals(Identifier.Button.A) || id.equals(Identifier.Button._0)) {
                                if (value == 1f) {
                                    TarinaRuutu.jatka();
                                }
                            }
                        }
                        case VALIKKORUUTU -> {
                            analog1Paino(id, false);
                            if (id.equals(Identifier.Axis.POV)) {
                                if (event.getValue() == 0.25) { // DPAD Ylös
                                    ValikkoRuutu.painaNäppäintä("ylös");
                                }
                                else if (event.getValue() == 0.75) { // DPAD Alas
                                    ValikkoRuutu.painaNäppäintä("alas");
                                }
                            }
                            if (id.equals(Identifier.Button.START) || id.equals(Identifier.Button._7) || id.equals(Identifier.Button.A) || id.equals(Identifier.Button._0)) {
                                if (value == 1f) {
                                    ValikkoRuutu.painaNäppäintä("enter");
                                }
                            }
                        }
                        case ASETUSRUUTU -> {
                            analog1Paino(id, false);
                            if (id.equals(Identifier.Axis.POV)) {
                                if (event.getValue() == 0.25) { // DPAD Ylös
                                    AsetusRuutu.painaNäppäintä("ylös");
                                }
                                else if (event.getValue() == 0.75) { // DPAD Alas
                                    AsetusRuutu.painaNäppäintä("alas");
                                }
                            }
                            if (id.equals(Identifier.Button.START) || id.equals(Identifier.Button._7) || id.equals(Identifier.Button.A) || id.equals(Identifier.Button._0)) {
                                if (value == 1f) {
                                    AsetusRuutu.painaNäppäintä("enter");
                                }
                            }
                            else if (id.equals(Identifier.Button.B) || id.equals(Identifier.Button._1)) {
                                if (value == 1f) {
                                    AsetusRuutu.painaNäppäintä("esc");
                                }
                            }
                        }
                        case ASETUSRUUTU_GRAFIIKKA -> {
                            analog1Paino(id, false);
                            if (id.equals(Identifier.Axis.POV)) {
                                if (event.getValue() == 0.25) { // DPAD Ylös
                                    GrafiikkaAsetusRuutu.painaNäppäintä("ylös");
                                }
                                else if (event.getValue() == 0.75) { // DPAD Alas
                                    GrafiikkaAsetusRuutu.painaNäppäintä("alas");
                                }
                                else if (event.getValue() == 1.0) { // DPAD Vasen
                                    GrafiikkaAsetusRuutu.painaNäppäintä("vasen");
                                }
                                else if (event.getValue() == 0.5) { // DPAD Oikea
                                    GrafiikkaAsetusRuutu.painaNäppäintä("oikea");
                                }
                            }
                            if (id.equals(Identifier.Button.START) || id.equals(Identifier.Button._7) || id.equals(Identifier.Button.A) || id.equals(Identifier.Button._0)) {
                                if (value == 1f) {
                                    GrafiikkaAsetusRuutu.painaNäppäintä("enter");
                                }
                            }
                            else if (id.equals(Identifier.Button.B) || id.equals(Identifier.Button._1)) {
                                if (value == 1f) {
                                    GrafiikkaAsetusRuutu.painaNäppäintä("esc");
                                }
                            }
                        }
                        case ASETUSRUUTU_ÄÄNI -> {
                            analog1Paino(id, false);
                            if (id.equals(Identifier.Axis.POV)) {
                                if (event.getValue() == 0.25) { // DPAD Ylös
                                    ÄäniAsetusRuutu.painaNäppäintä("ylös");
                                }
                                else if (event.getValue() == 0.75) { // DPAD Alas
                                    ÄäniAsetusRuutu.painaNäppäintä("alas");
                                }
                                else if (event.getValue() == 1.0) { // DPAD Vasen
                                    ÄäniAsetusRuutu.painaNäppäintä("vasen");
                                }
                                else if (event.getValue() == 0.5) { // DPAD Oikea
                                    ÄäniAsetusRuutu.painaNäppäintä("oikea");
                                }
                            }
                            if (id.equals(Identifier.Button.START) || id.equals(Identifier.Button._7) || id.equals(Identifier.Button.A) || id.equals(Identifier.Button._0)) {
                                if (value == 1f) {
                                    ÄäniAsetusRuutu.painaNäppäintä("enter");
                                }
                            }
                            else if (id.equals(Identifier.Button.B) || id.equals(Identifier.Button._1)) {
                                if (value == 1f) {
                                    ÄäniAsetusRuutu.painaNäppäintä("esc");
                                }
                            }
                        }
                        case ASETUSRUUTU_PELI -> {
                            analog1Paino(id, false);
                            if (id.equals(Identifier.Axis.POV)) {
                                if (event.getValue() == 0.25) { // DPAD Ylös
                                    PeliAsetusRuutu.painaNäppäintä("ylös");
                                }
                                else if (event.getValue() == 0.75) { // DPAD Alas
                                    PeliAsetusRuutu.painaNäppäintä("alas");
                                }
                                else if (event.getValue() == 1.0) { // DPAD Vasen
                                    PeliAsetusRuutu.painaNäppäintä("vasen");
                                }
                                else if (event.getValue() == 0.5) { // DPAD Oikea
                                    PeliAsetusRuutu.painaNäppäintä("oikea");
                                }
                            }
                            if (id.equals(Identifier.Button.START) || id.equals(Identifier.Button._7) || id.equals(Identifier.Button.A) || id.equals(Identifier.Button._0)) {
                                if (value == 1f) {
                                    PeliAsetusRuutu.painaNäppäintä("enter");
                                }
                            }
                            else if (id.equals(Identifier.Button.B) || id.equals(Identifier.Button._1)) {
                                if (value == 1f) {
                                    PeliAsetusRuutu.painaNäppäintä("esc");
                                }
                            }
                        }
                        case ASETUSRUUTU_OHJAIMET -> {
                            analog1Paino(id, false);
                            if (id.equals(Identifier.Axis.POV)) {
                                if (event.getValue() == 0.25) { // DPAD Ylös
                                    OhjainAsetusRuutu.painaNäppäintä("ylös");
                                }
                                else if (event.getValue() == 0.75) { // DPAD Alas
                                    OhjainAsetusRuutu.painaNäppäintä("alas");
                                }
                                else if (event.getValue() == 1.0) { // DPAD Vasen
                                    OhjainAsetusRuutu.painaNäppäintä("vasen");
                                }
                                else if (event.getValue() == 0.5) { // DPAD Oikea
                                    OhjainAsetusRuutu.painaNäppäintä("oikea");
                                }
                            }
                            if (id.equals(Identifier.Button.START) || id.equals(Identifier.Button._7) || id.equals(Identifier.Button.A) || id.equals(Identifier.Button._0)) {
                                if (value == 1f) {
                                    OhjainAsetusRuutu.painaNäppäintä("enter");
                                }
                            }
                            else if (id.equals(Identifier.Button.B) || id.equals(Identifier.Button._1)) {
                                if (value == 1f) {
                                    OhjainAsetusRuutu.painaNäppäintä("esc");
                                }
                            }
                        }
                        case ASETUSRUUTU_ÄÄNITESTI_VALIKKO -> {
                            analog1Paino(id, false);
                            if (id.equals(Identifier.Axis.POV)) {
                                if (event.getValue() == 0.25) { // DPAD Ylös
                                    ÄäniTestiValikko.painaNäppäintä("ylös");
                                }
                                else if (event.getValue() == 0.75) { // DPAD Alas
                                    ÄäniTestiValikko.painaNäppäintä("alas");
                                }
                            }
                            if (id.equals(Identifier.Button.START) || id.equals(Identifier.Button._7) || id.equals(Identifier.Button.A) || id.equals(Identifier.Button._0)) {
                                if (value == 1f) {
                                    ÄäniTestiValikko.painaNäppäintä("enter");
                                }
                            }
                            else if (id.equals(Identifier.Button.B) || id.equals(Identifier.Button._1)) {
                                if (value == 1f) {
                                    ÄäniTestiValikko.painaNäppäintä("esc");
                                }
                            }
                        }
                        case ASETUSRUUTU_ÄÄNITESTI_PELIÄÄNET -> {
                            analog1Paino(id, false);
                            if (id.equals(Identifier.Axis.POV)) {
                                if (event.getValue() == 0.25) { // DPAD Ylös
                                    ÄäniTestiRuutu.painaNäppäintä("ylös");
                                }
                                else if (event.getValue() == 0.75) { // DPAD Alas
                                    ÄäniTestiRuutu.painaNäppäintä("alas");
                                }
                                else if (event.getValue() == 1.0) { // DPAD Vasen
                                    ÄäniTestiRuutu.painaNäppäintä("vasen");
                                }
                                else if (event.getValue() == 0.5) { // DPAD Oikea
                                    ÄäniTestiRuutu.painaNäppäintä("oikea");
                                }
                            }
                            if (id.equals(Identifier.Button.START) || id.equals(Identifier.Button._7) || id.equals(Identifier.Button.A) || id.equals(Identifier.Button._0)) {
                                if (value == 1f) {
                                    ÄäniTestiRuutu.painaNäppäintä("enter");
                                }
                            }
                            else if (id.equals(Identifier.Button.B) || id.equals(Identifier.Button._1)) {
                                if (value == 1f) {
                                    ÄäniTestiRuutu.painaNäppäintä("esc");
                                }
                            }
                        }
                        case ASETUSRUUTU_ÄÄNITESTI_MIDI -> {
                            analog1Paino(id, false);
                            if (id.equals(Identifier.Axis.POV)) {
                                if (event.getValue() == 0.25) { // DPAD Ylös
                                    ÄäniTestiMidi.painaNäppäintä("ylös");
                                }
                                else if (event.getValue() == 0.75) { // DPAD Alas
                                    ÄäniTestiMidi.painaNäppäintä("alas");
                                }
                                else if (event.getValue() == 1.0) { // DPAD Vasen
                                    ÄäniTestiMidi.painaNäppäintä("vasen");
                                }
                                else if (event.getValue() == 0.5) { // DPAD Oikea
                                    ÄäniTestiMidi.painaNäppäintä("oikea");
                                }
                            }
                            if (id.equals(Identifier.Button.START) || id.equals(Identifier.Button._7) || id.equals(Identifier.Button.A) || id.equals(Identifier.Button._0)) {
                                if (value == 1f) {
                                    ÄäniTestiMidi.painaNäppäintä("enter");
                                }
                            }
                            else if (id.equals(Identifier.Button.B) || id.equals(Identifier.Button._1)) {
                                if (value == 1f) {
                                    ÄäniTestiMidi.painaNäppäintä("esc");
                                }
                            }
                        }
                        case ASETUSRUUTU_ÄÄNITESTI_WOOF -> {
                            analog1Paino(id, false);
                            if (id.equals(Identifier.Axis.POV)) {
                                if (event.getValue() == 0.25) { // DPAD Ylös
                                    ÄäniTestiWoof.painaNäppäintä("ylös");
                                }
                                else if (event.getValue() == 0.75) { // DPAD Alas
                                    ÄäniTestiWoof.painaNäppäintä("alas");
                                }
                                else if (event.getValue() == 1.0) { // DPAD Vasen
                                    ÄäniTestiWoof.painaNäppäintä("vasen");
                                }
                                else if (event.getValue() == 0.5) { // DPAD Oikea
                                    ÄäniTestiWoof.painaNäppäintä("oikea");
                                }
                            }
                            if (id.equals(Identifier.Button.START) || id.equals(Identifier.Button._7) || id.equals(Identifier.Button.A) || id.equals(Identifier.Button._0)) {
                                if (value == 1f) {
                                    ÄäniTestiWoof.painaNäppäintä("enter");
                                }
                            }
                            else if (id.equals(Identifier.Button.B) || id.equals(Identifier.Button._1)) {
                                if (value == 1f) {
                                    ÄäniTestiWoof.painaNäppäintä("esc");
                                }
                            }
                        }
                        case MINIPELIRUUTU -> {
                            analogPohjassa(id, false);
                            analogPohjassa(id, true);
                            if (id.equals(Identifier.Button.START) || id.equals(Identifier.Button._7)) {
                                if (value == 1f) MinipeliIkkuna3D.suljeToimintoIkkuna();
                            }
                            if (id.equals(Identifier.Button.A) || id.equals(Identifier.Button._0)) {
                                if (value == 1f) Maailma3D.liiku(Maailma3D.Liike.HYPPY);
                            }
                        }
                        case LOPPURUUTU -> {
                            analog1Paino(id, false);
                            if (id.equals(Identifier.Axis.POV)) {
                                if (event.getValue() == 0.25) { // DPAD Ylös
                                    LoppuRuutu.painaNäppäintä("ylös");
                                }
                                else if (event.getValue() == 0.75) { // DPAD Alas
                                    LoppuRuutu.painaNäppäintä("alas");
                                }
                            }
                            if (id.equals(Identifier.Button.START) || id.equals(Identifier.Button._7) || id.equals(Identifier.Button.A) || id.equals(Identifier.Button._0)) {
                                if (value == 1f) {
                                    LoppuRuutu.painaNäppäintä("enter");
                                }
                            }
                        }
                        case KEHITTÄJÄRUUTU -> {
                            if (id.equals(Identifier.Button.START) || id.equals(Identifier.Button._7) || id.equals(Identifier.Button.A) || id.equals(Identifier.Button._0)) {
                                if (value == 1f) {
                                    KehittäjäRuutu.takaisin();
                                }
                            }
                        }
                        case EDITORIRUUTU_VARMISTUS -> {
                            analog1Paino(id, false);
                            if (id.equals(Identifier.Axis.POV)) {
                                if (event.getValue() == 0.1) { // DPAD Vasen
                                    EditoriRuutuVarmistus.painaNäppäintä("ylös");
                                }
                                else if (event.getValue() == 0.5) { // DPAD Oikea
                                    EditoriRuutuVarmistus.painaNäppäintä("alas");
                                }
                            }
                            if (id.equals(Identifier.Button.START) || id.equals(Identifier.Button._7) || id.equals(Identifier.Button.A) || id.equals(Identifier.Button._0)) {
                                if (value == 1f) {
                                    EditoriRuutuVarmistus.painaNäppäintä("enter");
                                }
                            }
                        }
                        default -> {

                        }
                    }
                }
            }
        }
	}

    private static void analogPohjassa(Identifier id, boolean oikea) {
        if (oikea) {
            if (id.equals(Identifier.Axis.RX)) {
                if (analogValue < -analogDeadzone) {
                    rAnalogVasenPainettu = true;
                }
                else if (analogValue > analogDeadzone) {
                    rAnalogOikeaPainettu = true;
                }
                else {
                    rAnalogVasenPainettu = false;
                    rAnalogOikeaPainettu = false;
                }
            }
            if (id.equals(Identifier.Axis.RY)) {
                if (analogValue < -analogDeadzone) {
                    rAnalogYlösPainettu = true;
                }
                else if (analogValue > analogDeadzone) {
                    rAnalogAlasPainettu = true;
                }
                else {
                    rAnalogYlösPainettu = false;
                    rAnalogAlasPainettu = false;
                }
            }
        }
        else {
            if (id.equals(Identifier.Axis.X)) {
                if (analogValue < -analogDeadzone) {
                    lAnalogVasenPainettu = true;
                }
                else if (analogValue > analogDeadzone) {
                    lAnalogOikeaPainettu = true;
                }
                else {
                    lAnalogVasenPainettu = false;
                    lAnalogOikeaPainettu = false;
                }
            }
            if (id.equals(Identifier.Axis.Y)) {
                if (analogValue < -analogDeadzone) {
                    lAnalogYlösPainettu = true;
                }
                else if (analogValue > analogDeadzone) {
                    lAnalogAlasPainettu = true;
                }
                else {
                    lAnalogYlösPainettu = false;
                    lAnalogAlasPainettu = false;
                }
            }
        }
    }

    private static void analog1Paino(Identifier id, boolean oikea) {
        if (oikea) {
            if (id.equals(Identifier.Axis.RX)) {
                if (analogValue <= -analogDeadzone) {
                    if (rAnalogVapautettu) {
                        rAnalogVasenPainettu = true;
                        rAnalogVapautettu = false;
                    }
                }
                else if (analogValue > analogDeadzone) {
                    if (rAnalogVapautettu) {
                        rAnalogOikeaPainettu = true;
                        rAnalogVapautettu = false;
                    }
                }
                else {
                    rAnalogVapautettu = true;
                    rAnalogVasenPainettu = false;
                    rAnalogOikeaPainettu = false;
                }
            }
            if (id.equals(Identifier.Axis.RY)) {
                if (analogValue <= -analogDeadzone) {
                    if (rAnalogVapautettu) {
                        rAnalogYlösPainettu = true;
                        rAnalogVapautettu = false;
                    }
                }
                else if (analogValue > analogDeadzone) {
                    if (rAnalogVapautettu) {
                        rAnalogAlasPainettu = true;
                        rAnalogVapautettu = false;
                    }
                }
                else {
                    rAnalogVapautettu = true;
                    rAnalogYlösPainettu = false;
                    rAnalogAlasPainettu = false;
                }
            }
        }
        else {
            if (id.equals(Identifier.Axis.X)) {
                if (analogValue <= -analogDeadzone) {
                    if (lAnalogVapautettu) {
                        lAnalogVasenPainettu = true;
                        lAnalogVapautettu = false;
                    }
                }
                else if (analogValue > analogDeadzone) {
                    if (lAnalogVapautettu) {
                        lAnalogOikeaPainettu = true;
                        lAnalogVapautettu = false;
                    }
                }
                else {
                    lAnalogVapautettu = true;
                    lAnalogVasenPainettu = false;
                    lAnalogOikeaPainettu = false;
                }
            }
            if (id.equals(Identifier.Axis.Y)) {
                if (analogValue <= -analogDeadzone) {
                    if (lAnalogVapautettu) {
                        lAnalogYlösPainettu = true;
                        lAnalogVapautettu = false;
                    }
                }
                else if (analogValue > analogDeadzone) {
                    if (lAnalogVapautettu) {
                        lAnalogAlasPainettu = true;
                        lAnalogVapautettu = false;
                    }
                }
                else {
                    lAnalogVapautettu = true;
                    lAnalogYlösPainettu = false;
                    lAnalogAlasPainettu = false;
                }
            }
        }
    }

    private static void vapautaAnalogit() {
        if (lAnalogVasenPainettu ||
            lAnalogOikeaPainettu ||
            lAnalogYlösPainettu ||
            lAnalogAlasPainettu ||
            rAnalogVasenPainettu ||
            rAnalogOikeaPainettu ||
            rAnalogYlösPainettu ||
            rAnalogAlasPainettu
        ) {
            lAnalogVasenPainettu = false;
            lAnalogOikeaPainettu = false;
            lAnalogYlösPainettu = false;
            lAnalogAlasPainettu = false;
            rAnalogVasenPainettu = false;
            rAnalogOikeaPainettu = false;
            rAnalogYlösPainettu = false;
            rAnalogAlasPainettu = false;
        }
    }

    private static void tarkistaPainetutAnalogit() {
        switch (Peli.aktiivinenRuutu) {
            case PELIRUUTU -> {
                switch (Peli.syötteenTila) {
                    case PELI -> {
                        if (lAnalogVasenPainettu && lAnalogYlösPainettu) Pelaaja.kokeileLiikkumista(Suunta.YLÄVASEN);
                        else if (lAnalogVasenPainettu && lAnalogAlasPainettu) Pelaaja.kokeileLiikkumista(Suunta.ALAVASEN);
                        else if (lAnalogOikeaPainettu && lAnalogYlösPainettu) Pelaaja.kokeileLiikkumista(Suunta.YLÄOIKEA);
                        else if (lAnalogOikeaPainettu && lAnalogAlasPainettu) Pelaaja.kokeileLiikkumista(Suunta.ALAOIKEA);
                        else if (lAnalogVasenPainettu) Pelaaja.kokeileLiikkumista(Suunta.VASEN);
                        else if (lAnalogOikeaPainettu) Pelaaja.kokeileLiikkumista(Suunta.OIKEA);
                        else if (lAnalogYlösPainettu) Pelaaja.kokeileLiikkumista(Suunta.YLÖS);
                        else if (lAnalogAlasPainettu) Pelaaja.kokeileLiikkumista(Suunta.ALAS);
                    }
                    case TOIMINTO -> {
                        switch (Peli.toimintoIkkuna) {
                            case VALINTADIALOGI -> {
                                if (lAnalogYlösPainettu) {
                                    DialogiValintaIkkuna.pienennäValintaa();
                                    lAnalogYlösPainettu = false;
                                }
                                else if (lAnalogAlasPainettu) {
                                    DialogiValintaIkkuna.kasvataValintaa();
                                    lAnalogAlasPainettu = false;
                                }
                            }
                            case HUIJAUSKOODIT -> {
                                if (lAnalogYlösPainettu) {
                                    HuijauskoodiValikko.pienennäValintaa();
                                    lAnalogYlösPainettu = false;
                                }
                                else if (lAnalogAlasPainettu) {
                                    HuijauskoodiValikko.kasvataValintaa();
                                    lAnalogAlasPainettu = false;
                                }
                                if (lAnalogVasenPainettu) {
                                    HuijauskoodiValikko.muutaValintaa();
                                    lAnalogVasenPainettu = false;
                                }
                                else if (lAnalogOikeaPainettu) {
                                    HuijauskoodiValikko.muutaValintaa();
                                    lAnalogOikeaPainettu = false;
                                }
                            }
                            case MINIPELI_PONG -> {
                                if (lAnalogYlösPainettu) {
                                    MinipeliIkkunaPong.liikutaYlös();
                                }
                                else if (lAnalogAlasPainettu) {
                                    MinipeliIkkunaPong.liikutaAlas();
                                }
                            }
                            case MINIPELI_POKERI -> {
                                // Keksin tähän jotain myöhemmin.
                            }
                            case MINIPELI_TETRIS -> {
                                if (lAnalogVasenPainettu) {
                                    MinipeliIkkunaTetris.siirräPalikkaa(false);
                                    lAnalogVasenPainettu = false;
                                }
                                else if (lAnalogOikeaPainettu) {
                                    MinipeliIkkunaTetris.siirräPalikkaa(true);
                                    lAnalogOikeaPainettu = false;
                                }
                            }
                            default -> {

                            }
                        }
                    }
                    case DIALOGI -> {

                    }
                }
            }
            case VALIKKORUUTU -> {
                if (lAnalogYlösPainettu) {
                    ValikkoRuutu.painaNäppäintä("ylös");
                    lAnalogYlösPainettu = false;
                }
                else if (lAnalogAlasPainettu) {
                    ValikkoRuutu.painaNäppäintä("alas");
                    lAnalogAlasPainettu = false;
                }
            }
            case ASETUSRUUTU -> {
                if (lAnalogYlösPainettu) {
                    AsetusRuutu.painaNäppäintä("ylös");
                    lAnalogYlösPainettu = false;
                }
                else if (lAnalogAlasPainettu) {
                    AsetusRuutu.painaNäppäintä("alas");
                    lAnalogAlasPainettu = false;
                }
            }
            case ASETUSRUUTU_GRAFIIKKA -> {
                if (lAnalogYlösPainettu) {
                    GrafiikkaAsetusRuutu.painaNäppäintä("ylös");
                    lAnalogYlösPainettu = false;
                }
                else if (lAnalogAlasPainettu) {
                    GrafiikkaAsetusRuutu.painaNäppäintä("alas");
                    lAnalogAlasPainettu = false;
                }
                else if (lAnalogVasenPainettu) {
                    GrafiikkaAsetusRuutu.painaNäppäintä("vasen");
                    lAnalogVasenPainettu = false;
                }
                else if (lAnalogOikeaPainettu) {
                    GrafiikkaAsetusRuutu.painaNäppäintä("oikea");
                    lAnalogOikeaPainettu = false;
                }
            }
            case ASETUSRUUTU_ÄÄNI -> {
                if (lAnalogYlösPainettu) {
                    ÄäniAsetusRuutu.painaNäppäintä("ylös");
                    lAnalogYlösPainettu = false;
                }
                else if (lAnalogAlasPainettu) {
                    ÄäniAsetusRuutu.painaNäppäintä("alas");
                    lAnalogAlasPainettu = false;
                }
                else if (lAnalogVasenPainettu) {
                    ÄäniAsetusRuutu.painaNäppäintä("vasen");
                    lAnalogVasenPainettu = false;
                }
                else if (lAnalogOikeaPainettu) {
                    ÄäniAsetusRuutu.painaNäppäintä("oikea");
                    lAnalogOikeaPainettu = false;
                }
            }
            case ASETUSRUUTU_PELI -> {
                if (lAnalogYlösPainettu) {
                    PeliAsetusRuutu.painaNäppäintä("ylös");
                    lAnalogYlösPainettu = false;
                }
                else if (lAnalogAlasPainettu) {
                    PeliAsetusRuutu.painaNäppäintä("alas");
                    lAnalogAlasPainettu = false;
                }
                else if (lAnalogVasenPainettu) {
                    PeliAsetusRuutu.painaNäppäintä("vasen");
                    lAnalogVasenPainettu = false;
                }
                else if (lAnalogOikeaPainettu) {
                    PeliAsetusRuutu.painaNäppäintä("oikea");
                    lAnalogOikeaPainettu = false;
                }
            }
            case ASETUSRUUTU_OHJAIMET -> {
                if (lAnalogYlösPainettu) {
                    OhjainAsetusRuutu.painaNäppäintä("ylös");
                    lAnalogYlösPainettu = false;
                }
                else if (lAnalogAlasPainettu) {
                    OhjainAsetusRuutu.painaNäppäintä("alas");
                    lAnalogAlasPainettu = false;
                }
                else if (lAnalogVasenPainettu) {
                    OhjainAsetusRuutu.painaNäppäintä("vasen");
                    lAnalogVasenPainettu = false;
                }
                else if (lAnalogOikeaPainettu) {
                    OhjainAsetusRuutu.painaNäppäintä("oikea");
                    lAnalogOikeaPainettu = false;
                }
            }
            case ASETUSRUUTU_ÄÄNITESTI_VALIKKO -> {
                if (lAnalogYlösPainettu) {
                    ÄäniTestiValikko.painaNäppäintä("ylös");
                    lAnalogYlösPainettu = false;
                }
                else if (lAnalogAlasPainettu) {
                    ÄäniTestiValikko.painaNäppäintä("alas");
                    lAnalogAlasPainettu = false;
                }
            }
            case ASETUSRUUTU_ÄÄNITESTI_PELIÄÄNET -> {
                if (lAnalogYlösPainettu) {
                    ÄäniTestiRuutu.painaNäppäintä("ylös");
                    lAnalogYlösPainettu = false;
                }
                else if (lAnalogAlasPainettu) {
                    ÄäniTestiRuutu.painaNäppäintä("alas");
                    lAnalogAlasPainettu = false;
                }
                else if (lAnalogVasenPainettu) {
                    ÄäniTestiRuutu.painaNäppäintä("vasen");
                    lAnalogVasenPainettu = false;
                }
                else if (lAnalogOikeaPainettu) {
                    ÄäniTestiRuutu.painaNäppäintä("oikea");
                    lAnalogOikeaPainettu = false;
                }
            }
            case ASETUSRUUTU_ÄÄNITESTI_MIDI -> {
                if (lAnalogYlösPainettu) {
                    ÄäniTestiMidi.painaNäppäintä("ylös");
                    lAnalogYlösPainettu = false;
                }
                else if (lAnalogAlasPainettu) {
                    ÄäniTestiMidi.painaNäppäintä("alas");
                    lAnalogAlasPainettu = false;
                }
                else if (lAnalogVasenPainettu) {
                    ÄäniTestiMidi.painaNäppäintä("vasen");
                    lAnalogVasenPainettu = false;
                }
                else if (lAnalogOikeaPainettu) {
                    ÄäniTestiMidi.painaNäppäintä("oikea");
                    lAnalogOikeaPainettu = false;
                }
            }
            case ASETUSRUUTU_ÄÄNITESTI_WOOF -> {
                if (lAnalogYlösPainettu) {
                    ÄäniTestiWoof.painaNäppäintä("ylös");
                    lAnalogYlösPainettu = false;
                }
                else if (lAnalogAlasPainettu) {
                    ÄäniTestiWoof.painaNäppäintä("alas");
                    lAnalogAlasPainettu = false;
                }
                else if (lAnalogVasenPainettu) {
                    ÄäniTestiWoof.painaNäppäintä("vasen");
                    lAnalogVasenPainettu = false;
                }
                else if (lAnalogOikeaPainettu) {
                    ÄäniTestiWoof.painaNäppäintä("oikea");
                    lAnalogOikeaPainettu = false;
                }
            }
            case MINIPELIRUUTU -> {
                if (lAnalogVasenPainettu && lAnalogYlösPainettu) {
                    Maailma3D.liiku(Maailma3D.Liike.VASEN);
                    Maailma3D.liiku(Maailma3D.Liike.ETEENPÄIN);
                }
                else if (lAnalogVasenPainettu && lAnalogAlasPainettu) {
                    Maailma3D.liiku(Maailma3D.Liike.VASEN);
                    Maailma3D.liiku(Maailma3D.Liike.TAAKSEPÄIN);
                }
                else if (lAnalogOikeaPainettu && lAnalogYlösPainettu) {
                    Maailma3D.liiku(Maailma3D.Liike.OIKEA);
                    Maailma3D.liiku(Maailma3D.Liike.ETEENPÄIN);
                }
                else if (lAnalogOikeaPainettu && lAnalogAlasPainettu) {
                    Maailma3D.liiku(Maailma3D.Liike.OIKEA);
                    Maailma3D.liiku(Maailma3D.Liike.TAAKSEPÄIN);
                }
                else if (lAnalogVasenPainettu) Maailma3D.liiku(Maailma3D.Liike.VASEN);
                else if (lAnalogOikeaPainettu) Maailma3D.liiku(Maailma3D.Liike.OIKEA);
                else if (lAnalogYlösPainettu) Maailma3D.liiku(Maailma3D.Liike.ETEENPÄIN);
                else if (lAnalogAlasPainettu) Maailma3D.liiku(Maailma3D.Liike.TAAKSEPÄIN);

                if (rAnalogVasenPainettu) Maailma3D.käännä(Maailma3D.KameranLiike.VASEN);
                else if (rAnalogOikeaPainettu) Maailma3D.käännä(Maailma3D.KameranLiike.OIKEA);
                if (rAnalogYlösPainettu) Maailma3D.käännä(Maailma3D.KameranLiike.YLÖS);
                else if (rAnalogAlasPainettu) Maailma3D.käännä(Maailma3D.KameranLiike.ALAS);
            }
            case LOPPURUUTU -> {
                if (lAnalogYlösPainettu) {
                    LoppuRuutu.painaNäppäintä("ylös");
                    lAnalogYlösPainettu = false;
                }
                else if (lAnalogAlasPainettu) {
                    LoppuRuutu.painaNäppäintä("alas");
                    lAnalogAlasPainettu = false;
                }
            }
            case EDITORIRUUTU_VARMISTUS -> {
                if (lAnalogVasenPainettu) {
                    EditoriRuutuVarmistus.painaNäppäintä("ylös");
                    lAnalogVasenPainettu = false;
                }
                else if (lAnalogOikeaPainettu) {
                    EditoriRuutuVarmistus.painaNäppäintä("alas");
                    lAnalogOikeaPainettu = false;
                }
            }
            default -> {
            
            }
        }
    }

    private static void tarkistaPelaajanTila() {
        if (Peli.viimeisinSyöteLaite == SyöteLaitteet.PELIOHJAIN) {
            if (Peli.syötteenTila == SyötteenTila.PELI) {
                if (lAnalogVasenPainettu ||
                    lAnalogOikeaPainettu ||
                    lAnalogYlösPainettu ||
                    lAnalogAlasPainettu
                ) {
                    Pelaaja.keimonState = KeimonState.JUOKSU;
                }
                else Pelaaja.keimonState = KeimonState.IDLE;

                if ((lAnalogVasenPainettu) && lAnalogYlösPainettu) {
                    Pelaaja.keimonSuunta = Suunta.YLÄVASEN;
                    Pelaaja.keimonSuuntaVasenOikea = SuuntaVasenOikea.VASEN;
                }
                else if ((lAnalogVasenPainettu) && (lAnalogAlasPainettu)) {
                    Pelaaja.keimonSuunta = Suunta.ALAVASEN;
                    Pelaaja.keimonSuuntaVasenOikea = SuuntaVasenOikea.VASEN;
                }
                else if ((lAnalogOikeaPainettu) && (lAnalogYlösPainettu)) {
                    Pelaaja.keimonSuunta = Suunta.YLÄOIKEA;
                    Pelaaja.keimonSuuntaVasenOikea = SuuntaVasenOikea.OIKEA;
                }
                else if ((lAnalogOikeaPainettu) && (lAnalogAlasPainettu)) {
                    Pelaaja.keimonSuunta = Suunta.ALAOIKEA;
                    Pelaaja.keimonSuuntaVasenOikea = SuuntaVasenOikea.OIKEA;
                }
                else if (lAnalogVasenPainettu) {
                    Pelaaja.keimonSuunta = Suunta.VASEN;
                    Pelaaja.keimonSuuntaVasenOikea = SuuntaVasenOikea.VASEN;
                }
                else if (lAnalogOikeaPainettu) {
                    Pelaaja.keimonSuunta = Suunta.OIKEA;
                    Pelaaja.keimonSuuntaVasenOikea = SuuntaVasenOikea.OIKEA;
                }
                else if (lAnalogYlösPainettu) {
                    Pelaaja.keimonSuunta = Suunta.YLÖS;
                }
                else if (lAnalogAlasPainettu) {
                    Pelaaja.keimonSuunta = Suunta.ALAS;
                }
            }
        }
    }

    private static void tavarapaikanVaihto(int määrä) {
        Peli.esineValInt += määrä;
        if (Peli.esineValInt < 0) Peli.esineValInt += Pelaaja.esineet.length;
        Peli.esineValInt %= Pelaaja.esineet.length;
        Peli.valittuEsine = Pelaaja.esineet[Peli.esineValInt];
    }
}
