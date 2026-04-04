package keimo.seikkailupeli.gui.toimintoIkkunat;

import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.Latauspalkki;
import keimo.keimoengine.grafiikat.guikomponentit.StaattinenKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.SyöteLaitteet;
import keimo.seikkailupeli.Peli.SyötteenTila;
import keimo.seikkailupeli.Peli.ToimintoIkkunanTyyppi;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.objektit.kenttäkohteet.kiintopiste.Pulloautomaatti;
import keimo.seikkailupeli.objektit.kenttäkohteet.kiintopiste.Pulloautomaatti.PulloautomaatinKuvake;
import keimo.seikkailupeli.toiminnot.Dialogit;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Random;

public class PullonPalautusIkkuna {
    private static Renderöitävä kehysTekstuuri = Assets.annaTekstuuri("ikkuna_kehys_musta");
    private static Renderöitävä taustaTekstuuri = Assets.annaTekstuuri("toimintoikkuna_pullonpalautus");
    private static Teksti tölkkiMääräTeksti;
    private static Teksti pullonPalautusTeksti;
    private static StaattinenKomponentti kehysKomponentti = new StaattinenKomponentti(0.5f, 0.5f, 0, 0, kehysTekstuuri);
    private static StaattinenKomponentti taustaLabel = new StaattinenKomponentti(0.4f, 0.4f, 0, 0, taustaTekstuuri);
    private static StaattinenKomponentti tölkkiMääräLabel;
    private static Latauspalkki edistymispalkki = new Latauspalkki(1f/3f, 0.05f, 0.0f, -0.2f);
    private static StaattinenKomponentti virheLabel;

    private static Random r = new Random();
    private static String statusTeksti1 = "0/0";
    private static String statusTeksti2 = "Palautetaan";
    private static int pullonPalautuksenPituus;
    public static boolean jatkoSyöteAnnettu = false;
    private static boolean valmis = false;
    public static Pulloautomaatti pulloautomaatti;
    public static VirheenTyyppi virheenTyyppi = VirheenTyyppi.PAKKAUS;
    private static float siirräY;
    private static float venytäX;
    private static int toistot = 0;
    private static int virhe;

    public static void alustaGrafiikat() {
        tölkkiMääräTeksti = new Teksti("0 / 0", Color.black, 350, 48);
        pullonPalautusTeksti = new Teksti("Palautetaan", Color.black, 600, 192);
        tölkkiMääräLabel = new StaattinenKomponentti(0.15f, 0.1f, 0.275f, 0.2f, tölkkiMääräTeksti);
        virheLabel = new StaattinenKomponentti(0.125f, 0.1f, 0.15f, 0, pullonPalautusTeksti);
    }
    
    public static void renderöiIkkuna(Shader peliShader, Ikkuna window) {
        peliShader.bind();
        peliShader.nollaaShaderEfektit();
        if (siirräY > 0) siirräY -= 0.05f;
        if (venytäX < 1) venytäX += 0.05f;

        kehysKomponentti.muutaKokoa(0.5f * venytäX, 0.5f, 0, 0);
        kehysKomponentti.renderöi(peliShader, window);
        taustaLabel.muutaKokoa(0.4f * venytäX, 0.4f, 0, 0);
        taustaLabel.renderöi(peliShader, window);

        if (venytäX >= 1) {
            tölkkiMääräTeksti.päivitäTeksti(statusTeksti1);
            tölkkiMääräLabel.renderöi(peliShader, window);
            edistymispalkki.päivitäLatausProsentti(100f * (((float)Pelaaja.kuparit-pullonPalautuksenPituus) / (float)Pelaaja.kuparit));
            edistymispalkki.renderöi(peliShader, window);
            pullonPalautusTeksti.päivitäTeksti(statusTeksti2, 2);
            virheLabel.renderöi(peliShader, window);
        }
    }

    public static void avaaToimintoIkkuna() {
        Peli.syötteenTila = SyötteenTila.TOIMINTO;
        Peli.toimintoIkkuna = ToimintoIkkunanTyyppi.PULLONPALAUTUS;
        siirräY = 1.5f;
        venytäX = 0f;
        if (Peli.annaObjektiKenttä()[Pelaaja.sijX][Pelaaja.sijY] instanceof Pulloautomaatti) {
            pulloautomaatti = (Pulloautomaatti)Peli.annaObjektiKenttä()[Pelaaja.sijX][Pelaaja.sijY];
            if (Pelaaja.kuparit > 0) {
                pulloautomaatti.valitseTila(PulloautomaatinKuvake.AKTIIVINEN);
                valmis = false;
                pullonPalautuksenPituus = Pelaaja.kuparit;
                toistot = 0;
                virhe = r.nextInt(0, 10);
            }
            else {
                suljeToimintoIkkuna();
                Dialogit.avaaDialogi(pulloautomaatti.annaDialogiTekstuuri(), "Sinulla ei ole yhtään tölkkiä.", "Pulloautomaatti");
            }
        }
        else suljeToimintoIkkuna();
    }

    public static void suljeToimintoIkkuna() {
        Peli.syötteenTila = SyötteenTila.PELI;
    }

    public static void tarkistaTila() {
        if (valmis) {
            valmis();
        }
    }

    public enum VirheenTyyppi {
        PAKKAUS,
        MUOTO,
        KÄSI,
        MERKKI;
    }
    
    public static void simuloiPullonpalautus() {
        if (pullonPalautuksenPituus > 0) {
            statusTeksti1 = "" + (Pelaaja.kuparit - pullonPalautuksenPituus) + "/" + Pelaaja.kuparit;
            statusTeksti2 = "Palautetaan...";
            switch (virhe) {
                case 0:
                    pulloautomaatti.valitseTila(PulloautomaatinKuvake.VIRHE);
                    virheenTyyppi = VirheenTyyppi.PAKKAUS;
                    statusTeksti2 = "Poista pakkaus ja yritä uudelleen tai hävitä se muuten.\n(Paina " + (Peli.viimeisinSyöteLaite == SyöteLaitteet.NÄPPÄIMISTÖ ? "Space)" : "A)");
                    if (!jatkoSyöteAnnettu) return;
                    else virhe = r.nextInt(0, 10);
                break;
                case 1:
                    pulloautomaatti.valitseTila(PulloautomaatinKuvake.VIRHE);
                    virheenTyyppi = VirheenTyyppi.MUOTO;
                    statusTeksti2 = "Palauta pakkaus alkuperäiseen muotoon ja yritä uudelleen.\n(Paina " + (Peli.viimeisinSyöteLaite == SyöteLaitteet.NÄPPÄIMISTÖ ? "X)" : "Y)");
                    if (!jatkoSyöteAnnettu) return;
                    else virhe = r.nextInt(0, 10);
                break;
                case 2:
                    pulloautomaatti.valitseTila(PulloautomaatinKuvake.VIRHE);
                    virheenTyyppi = VirheenTyyppi.KÄSI;
                    statusTeksti2 = "Älä työnnä kättä automaattiin!\n(Paina " + (Peli.viimeisinSyöteLaite == SyöteLaitteet.NÄPPÄIMISTÖ ? "C)" : "X)");
                    if (!jatkoSyöteAnnettu) return;
                    else virhe = r.nextInt(0, 10);
                break;
                case 3:
                    pulloautomaatti.valitseTila(PulloautomaatinKuvake.VIRHE);
                    virheenTyyppi = VirheenTyyppi.MERKKI;
                    statusTeksti2 = "Kauppa ei hyväksy tätä merkkiä!\n(Paina " + (Peli.viimeisinSyöteLaite == SyöteLaitteet.NÄPPÄIMISTÖ ? "Z)" : "B)");
                    if (!jatkoSyöteAnnettu) return;
                    else virhe = r.nextInt(0, 10);
                break;
                default:
                    if (toistot % 20 == 0) {
                        pulloautomaatti.valitseTila(PulloautomaatinKuvake.AKTIIVINEN);    
                        pullonPalautuksenPituus --;
                        jatkoSyöteAnnettu = false;
                        virhe = r.nextInt(0, 10);
                    }
                    toistot++;
                break;
            }
        }
        else valmis = true;
    }

    private static void valmis() {
        try {
            valmis = false;
            pulloautomaatti.valitseTila(PulloautomaatinKuvake.IDLE);
            Peli.pauseDialogi = false;
            Peli.valintaDialogi = false;
            suljeToimintoIkkuna();
            float saatavaRaha = 0.15f * Pelaaja.kuparit;
            if (saatavaRaha > 0) {
                Pelaaja.raha += saatavaRaha;
                Pelaaja.kuparit = 0;
                DecimalFormat df = new DecimalFormat("##.##");
                String saatavaRahaFormatoitu = df.format(saatavaRaha);
                Dialogit.avaaDialogi(pulloautomaatti.annaDialogiTekstuuri(), "Palautit tölkit kauppaan ja sait " + saatavaRahaFormatoitu + "€.", "Pulloautomaatti");
            }
            else {
                Dialogit.avaaDialogi(pulloautomaatti.annaDialogiTekstuuri(), "Sinulla ei ole yhtään tölkkiä.", "Pulloautomaatti");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
