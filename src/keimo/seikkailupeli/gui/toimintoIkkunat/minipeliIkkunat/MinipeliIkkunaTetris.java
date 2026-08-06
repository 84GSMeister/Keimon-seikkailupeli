package keimo.seikkailupeli.gui.toimintoIkkunat.minipeliIkkunat;

import keimo.keimoengine.fontit.Väri;
import keimo.keimoengine.grafiikat.Renderöitävä;
import keimo.keimoengine.grafiikat.Teksti;
import keimo.keimoengine.grafiikat.guikomponentit.LabelKomponentti;
import keimo.keimoengine.grafiikat.shaderit.Shader;
import keimo.keimoengine.ikkuna.Ikkuna;
import keimo.seikkailupeli.Peli;
import keimo.seikkailupeli.Peli.SyötteenTila;
import keimo.seikkailupeli.Peli.ToimintoIkkunanTyyppi;
import keimo.seikkailupeli.assets.Assets;
import keimo.seikkailupeli.objektit.Pelaaja;
import keimo.seikkailupeli.äänet.Musat;

import java.util.Random;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class MinipeliIkkunaTetris {
    private static Shader peliShader;

    private static Renderöitävä kehysTekstuuri = Assets.annaTekstuuri("minipeli_kehys");
    private static LabelKomponentti kehysKomponentti = new LabelKomponentti(2f/3f, 2f/2.4f, 0, -1f/6f, kehysTekstuuri);
    private static Renderöitävä alkuruutuTekstuuri = Assets.annaTekstuuri("minipeli_tetris_alkuruutu");
    private static Renderöitävä palikkaTekstuuri = Assets.annaTekstuuri("minipeli_tetris_palikka");
    private static Renderöitävä hudTekstuuri = Assets.annaTekstuuri("minipeli_tetris_hud");
    private static Teksti teksti;
    private static Teksti peliohiTeksti;
    private static Teksti ohjeTeksti;
    private static float siirtymä = 0;

    public static int pelaajanSijY = 0;
    public static int vihollisenSijY = 0;
    private static Random random = new Random();

    public static int minX = 0;
    public static int maxX = 10;
    public static int minY = 0;
    public static int maxY = 16;
    private static int iteraatio = 0;
    private static boolean odotaPalikanJälkeen = false;
    private static int[][] taulukko = new int[-minX + maxX][-minY + maxY];
    private static boolean peliOhi = false;
    private static boolean valikko = true;

    private static int palikanSijX = 0;
    private static int palikanSijY = maxY-1;
    private static int nopeus = 1;
    private static int kääntö = 0;

    private static int pelaajanPisteet = 0;

    private static class Palikka {
        public Palikkatyypit tyyppi;
        public int tyyppiInt;
        public PalikanVäri väri;
        public int leveys;
        public int korkeus;
        public boolean[][] osat;

        public Palikka(int tyyppi) {
            
            if (tyyppi > Palikkatyypit.values().length || tyyppi < 0) this.tyyppi = Palikkatyypit.values()[random.nextInt(Palikkatyypit.values().length)];
            else this.tyyppi = Palikkatyypit.values()[tyyppi];
            switch (this.tyyppi) {
                case NELIÖ -> {
                    this.tyyppiInt = 1;
                    this.leveys = 2;
                    this.korkeus = 2;
                    this.väri = PalikanVäri.KELTAINEN;
                    this.osat = new boolean[][]{{true,true},{true,true}};
                }
                case L_PALIKKA -> {
                    this.tyyppiInt = 2;
                    this.leveys = 3;
                    this.korkeus = 2;
                    this.väri = PalikanVäri.VIHREÄ;
                    this.osat = new boolean[][]{{true,false},{true,false},{true,true}};
                }
                case L_PALIKKA_2 -> {
                    this.tyyppiInt = 3;
                    this.leveys = 3;
                    this.korkeus = 2;
                    this.väri = PalikanVäri.PUNAINEN;
                    this.osat = new boolean[][]{{true,true},{true,false},{true,false}};
                }
                case T_PALIKKA -> {
                    this.tyyppiInt = 4;
                    this.leveys = 3;
                    this.korkeus = 2;
                    this.väri = PalikanVäri.TURKOOSI;
                    this.osat = new boolean[][]{{true,false},{true,true},{true,false}};
                }
                case Z_PALIKKA -> {
                    this.tyyppiInt = 5;
                    this.leveys = 3;
                    this.korkeus = 2;
                    this.väri = PalikanVäri.PINKKI;
                    this.osat = new boolean[][]{{true,false},{true,true},{false,true}};
                }
                case Z_PALIKKA_2 -> {
                    this.tyyppiInt = 6;
                    this.leveys = 3;
                    this.korkeus = 2;
                    this.väri = PalikanVäri.ORANSSI;
                    this.osat = new boolean[][]{{false,true},{true,true},{true,false}};
                }
                case SUORA -> {
                    this.tyyppiInt = 7;
                    this.leveys = 4;
                    this.korkeus = 1;
                    this.väri = PalikanVäri.SININEN;
                    this.osat = new boolean[][]{{true},{true},{true},{true}};
                }
            }
        }
    }

    private static enum Palikkatyypit {
        NELIÖ,
        L_PALIKKA,
        L_PALIKKA_2,
        T_PALIKKA,
        Z_PALIKKA,
        Z_PALIKKA_2,
        SUORA;
    }
    private static enum PalikanVäri {
        KELTAINEN,
        VIHREÄ,
        PUNAINEN,
        ORANSSI,
        SININEN,
        PINKKI,
        TURKOOSI;
    }
    private static Palikka palikka = new Palikka(-1);
    private static Palikka seuraavaPalikka = new Palikka(-1);

    private static void valitseRandomPalikka() {
        palikka = seuraavaPalikka;
        seuraavaPalikka = new Palikka(-1);
    }

    private static void alustaGrafiikat() {
        if (teksti == null) {
            teksti = new Teksti("Tähän tulee tetris", Väri.green, 200, 48);
            peliohiTeksti = new Teksti("Peli ohi!", Väri.green, 350, 48);
            ohjeTeksti = new Teksti("Ohjeet", Väri.green, 500, 140);
        }
    }

    public static void renderöiKehys(Ikkuna window, Shader peliShader1) {
        alustaGrafiikat();
        // if (peliShader == null) peliShader = peliShader1;
        // float ruudunLeveys = window.getWidth();
        // float ruudunKorkeus = window.getHeight();
        // float scaleX = ruudunLeveys/3f;
        // float scaleY = ruudunKorkeus/2.4f;
        // float offsetY = ruudunKorkeus/12f;
        if (siirtymä < 1) siirtymä += 0.05;
        peliShader1.bind();
        peliShader1.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));
        peliShader1.setUniform("addcolor", new Vector4f(0, 0, 0, 1));

        // Matrix4f matKehys = new Matrix4f();
        // window.getView().scale(1, matKehys);
        // matKehys.translate(0, - offsetY, 0);
        // matKehys.scale(scaleX * siirtymä, scaleY * siirtymä, 0);
        // peliShader.asetaSijainti(matKehys);
        // kehysTekstuuri.bind(0);
        // Assets.getModel().render();

        kehysKomponentti.muutaKokoa(2f/3f * siirtymä, 2f/2.4f * siirtymä, 0, -1f/6f);
        kehysKomponentti.renderöi(peliShader1, window);
    }
    
    public static void renderöiIkkuna(Ikkuna window, Shader peliShader1) {
        if (peliShader == null) peliShader = peliShader1;
        float ruudunLeveys = window.getWidth();
        float ruudunKorkeus = window.getHeight();
        if (siirtymä >= 1) {
            peliShader.bind();
            peliShader.setUniform("color", new Vector4f(1f, 1f, 1f, 1f));
            peliShader.setUniform("addcolor", new Vector4f(0, 0, 0, 1));

            if (valikko) {
                float scaleXValikkoKuvake = ruudunLeveys/4f;
                float scaleYValikkoKuvake = ruudunKorkeus/4f;
                Matrix4f matValikkoKuvake = new Matrix4f();
                window.getView().scale(1, matValikkoKuvake);
                matValikkoKuvake.scale(scaleXValikkoKuvake, scaleYValikkoKuvake, 0);
                peliShader.asetaSijainti(matValikkoKuvake);
                peliShader.setUniform("addcolor", new Vector4f(0, 0, 0, 1));
                alkuruutuTekstuuri.bind(0);
                Assets.getModel().render();
            }
            else if (!peliOhi) {
                float offsetX = ruudunLeveys/4f;
                float offsetY = ruudunKorkeus/4f;

                float scaleXPalikka = ruudunLeveys/80f;
                float scaleYPalikka = ruudunKorkeus/56f;
                float liikeSkaalaXPalikka = ruudunLeveys/80f;
                float liikeSkaalaYPalikka = ruudunKorkeus/56f;

                if (!odotaPalikanJälkeen) renderöiPalikka(palikka, window);

                for (int y = 0; y < -minY + maxY; y++) {
                    for (int x = 0; x < -minX + maxX; x++) {
                        if (taulukko[x][y] != 0) {
                            Matrix4f matTaulukko = new Matrix4f();
                            window.getView().scale(1, matTaulukko);
                            matTaulukko.translate(2 * liikeSkaalaXPalikka * (x+0.5f) - offsetX, 2 * liikeSkaalaYPalikka * (y+0.5f) - offsetY, 0);
                            matTaulukko.scale(scaleXPalikka, scaleYPalikka, 0);
                            peliShader.asetaSijainti(matTaulukko);
                            switch (taulukko[x][y]) {
                                case 1: peliShader.setUniform("addcolor", new Vector4f(1, 1, 0, 1)); break;
                                case 2: peliShader.setUniform("addcolor", new Vector4f(0, 1, 0, 1)); break;
                                case 3: peliShader.setUniform("addcolor", new Vector4f(1, 0, 0, 1)); break;
                                case 4: peliShader.setUniform("addcolor", new Vector4f(0, 1, 1, 1)); break;
                                case 5: peliShader.setUniform("addcolor", new Vector4f(1, 0, 1, 1)); break;
                                case 6: peliShader.setUniform("addcolor", new Vector4f(0.5f, 0.25f, 0, 1)); break;
                                case 7: peliShader.setUniform("addcolor", new Vector4f(0, 0, 1, 1)); break;
                                default: peliShader.setUniform("addcolor", new Vector4f(0, 0, 0, 1)); break;
                            }
                            palikkaTekstuuri.bind(0);
                            Assets.getModel().render();
                        }
                    }
                }

                float scaleXHUD = ruudunLeveys/8f;
                float scaleYHUD = ruudunKorkeus/4f;
                Matrix4f matHUD = new Matrix4f();
                window.getView().scale(1, matHUD);
                matHUD.translate(scaleXHUD, 0, 0);
                matHUD.scale(scaleXHUD, scaleYHUD, 0);
                peliShader.asetaSijainti(matHUD);
                peliShader.setUniform("addcolor", new Vector4f(0, 0, 0, 1));
                hudTekstuuri.bind(0);
                Assets.getModel().render();

                renderöiSeuraavaPalikka(seuraavaPalikka, window);

                float scaleXPisteet = ruudunLeveys/12f;
                float scaleYPisteet = ruudunKorkeus/32f;
                float keskitysXPisteet = ruudunLeveys/12f;
                float offsetXPisteet = ruudunLeveys/8f;
                float offsetYPisteet = ruudunKorkeus/5.75f;

                Matrix4f matOhjeTeksti = new Matrix4f();
                window.getView().scale(1, matOhjeTeksti);
                matOhjeTeksti.translate(-0*keskitysXPisteet + offsetXPisteet, - offsetYPisteet, 0);
                matOhjeTeksti.scale(scaleXPisteet, scaleYPisteet, 0);
                peliShader.asetaSijainti(matOhjeTeksti);
                ohjeTeksti.päivitäTeksti("A/D: Liikuta" + "\n" + "S: Aseta" + "\n" + "Space: Käännä" + "        ", 2);
                peliShader.setUniform("addcolor", new Vector4f(0, 0, 0, 1));
                ohjeTeksti.bind(0);
                Assets.getModel().render();

                Matrix4f matTekstiPelaajanPisteet = new Matrix4f();
                window.getView().scale(1, matTekstiPelaajanPisteet);
                matTekstiPelaajanPisteet.translate(-0*keskitysXPisteet + offsetXPisteet, - 0*offsetYPisteet, 0);
                matTekstiPelaajanPisteet.scale(scaleXPisteet, scaleYPisteet, 0);
                peliShader.asetaSijainti(matTekstiPelaajanPisteet);
                peliShader.setUniform("addcolor", new Vector4f(0, 0, 0, 1));
                teksti.päivitäTeksti("" + pelaajanPisteet);
                teksti.bind(0);
                Assets.getModel().render();
            }
            else {
                float scaleXPeliohiTeksti= ruudunLeveys/8f;
                float scaleYPeliohiTeksti = ruudunKorkeus/32f;
                float keskitysXPisteet = ruudunLeveys/12f;
                float offsetXPisteet = ruudunLeveys/32f;
                Matrix4f matTekstiPelaajanPisteet = new Matrix4f();
                window.getView().scale(1, matTekstiPelaajanPisteet);
                matTekstiPelaajanPisteet.translate(-keskitysXPisteet + offsetXPisteet, 0, 0);
                matTekstiPelaajanPisteet.scale(scaleXPeliohiTeksti, scaleYPeliohiTeksti, 0);
                peliShader.asetaSijainti(matTekstiPelaajanPisteet);
                peliShader.setUniform("addcolor", new Vector4f(0, 0, 0, 1));
                peliohiTeksti.bind(0);
                Assets.getModel().render();
            }
        }
    }

    private static void renderöiPalikka(Palikka palikka, Ikkuna window) {
        
        switch (kääntö) {
            default -> {
                for (int y = 0; y < palikka.korkeus; y++) {
                    for (int x = 0; x < palikka.leveys; x++) {
                        if (palikka.osat[x][y]) {
                            renderöiPalikanOsa(palikka, x, y, window, false);
                        }
                    }
                }
            }
            case 90 -> {
                for (int y = 0; y < palikka.leveys; y++) {
                    for (int x = 0; x < palikka.korkeus; x++) {
                        if (palikka.osat[y][x]) {
                            renderöiPalikanOsa(palikka, x, palikka.leveys-y, window, false);
                        }
                    }
                }
            }
            case 180 -> {
                for (int y = 0; y < palikka.korkeus; y++) {
                    for (int x = 0; x < palikka.leveys; x++) {
                        if (palikka.osat[x][y]) {
                            renderöiPalikanOsa(palikka, palikka.leveys-x-1, palikka.korkeus-y, window, false);
                        }
                    }
                }
            }
            case 270 -> {
                for (int y = 0; y < palikka.leveys; y++) {
                    for (int x = 0; x < palikka.korkeus; x++) {
                        if (palikka.osat[y][x]) {
                            renderöiPalikanOsa(palikka, palikka.korkeus-x-1, y, window, false);
                        }
                    }
                }
            }
        }
    }

    private static void renderöiPalikanOsa(Palikka palikka, float x, float y, Ikkuna window, boolean staattinen) {
        float ruudunLeveys = window.getWidth();
        float ruudunKorkeus = window.getHeight();
        float offsetX = ruudunLeveys/4f;
        float offsetY = ruudunKorkeus/4f;
        
        float scaleXPalikka = ruudunLeveys/80f;
        float scaleYPalikka = ruudunKorkeus/56f;
        float liikeSkaalaXPalikka = ruudunLeveys/80f;
        float liikeSkaalaYPalikka = ruudunKorkeus/56f;

        Matrix4f matPalikka = new Matrix4f();
        window.getView().scale(1, matPalikka);
        if (staattinen) {
            matPalikka.translate(2 * liikeSkaalaXPalikka * 15 - liikeSkaalaXPalikka * seuraavaPalikka.leveys/2f - offsetX + 2*x*liikeSkaalaXPalikka, 2 * liikeSkaalaYPalikka * 10.5f - offsetY + 2*y*liikeSkaalaYPalikka, 0);
        }
        else {
            matPalikka.translate(2 * liikeSkaalaXPalikka * (palikanSijX+0.5f) - offsetX + 2*x*liikeSkaalaXPalikka, 2 * liikeSkaalaYPalikka * (palikanSijY+0.5f) - offsetY + 2*y*liikeSkaalaYPalikka, 0);
        }
        switch (palikka.väri) {
            case KELTAINEN: peliShader.setUniform("addcolor", new Vector4f(1, 1, 0, 1)); break;
            case VIHREÄ: peliShader.setUniform("addcolor", new Vector4f(0, 1, 0, 1)); break;
            case PUNAINEN: peliShader.setUniform("addcolor", new Vector4f(1, 0, 0, 1)); break;
            case PINKKI: peliShader.setUniform("addcolor", new Vector4f(1, 0, 1, 1)); break;
            case SININEN: peliShader.setUniform("addcolor", new Vector4f(0, 0, 1, 1)); break;
            case ORANSSI: peliShader.setUniform("addcolor", new Vector4f(0.5f, 0.25f, 0, 1)); break;
            case TURKOOSI: peliShader.setUniform("addcolor", new Vector4f(0, 1, 1, 1)); break;
            default: peliShader.setUniform("addcolor", new Vector4f(0, 0, 0, 1)); break;
        }
        matPalikka.scale(scaleXPalikka, scaleYPalikka, 0);
        peliShader.asetaSijainti(matPalikka);
        
        palikkaTekstuuri.bind(0);
        Assets.getModel().render();
    }

    private static void renderöiSeuraavaPalikka(Palikka palikka, Ikkuna window) {
        switch (palikka.tyyppi) {
            case NELIÖ -> {
                for (int y = 0; y < 2; y++) {
                    for (int x = 0; x < 2; x++) {
                        renderöiPalikanOsa(palikka, x, y, window, true);
                    }
                }
            }
            case SUORA -> {
                for (int y = 0; y < 1; y++) {
                    for (int x = 0; x < 4; x++) {
                        renderöiPalikanOsa(palikka, x, y, window, true);
                    }
                }
            }
            case Z_PALIKKA -> {
                renderöiPalikanOsa(palikka, 0, 0, window, true);
                renderöiPalikanOsa(palikka, 1, 0, window, true);
                renderöiPalikanOsa(palikka, 1, 1, window, true);
                renderöiPalikanOsa(palikka, 2, 1, window, true);
            }
            case Z_PALIKKA_2 -> {
                renderöiPalikanOsa(palikka, 0, 1, window, true);
                renderöiPalikanOsa(palikka, 1, 1, window, true);
                renderöiPalikanOsa(palikka, 1, 0, window, true);
                renderöiPalikanOsa(palikka, 2, 0, window, true);
            }
            case L_PALIKKA -> {
                renderöiPalikanOsa(palikka, 0, 0, window, true);
                renderöiPalikanOsa(palikka, 1, 0, window, true);
                renderöiPalikanOsa(palikka, 2, 0, window, true);
                renderöiPalikanOsa(palikka, 2, 1, window, true);
            }
            case L_PALIKKA_2 -> {
                renderöiPalikanOsa(palikka, 0, 0, window, true);
                renderöiPalikanOsa(palikka, 1, 0, window, true);
                renderöiPalikanOsa(palikka, 2, 0, window, true);
                renderöiPalikanOsa(palikka, 0, 1, window, true);
            }
            case T_PALIKKA -> {
                renderöiPalikanOsa(palikka, 0, 0, window, true);
                renderöiPalikanOsa(palikka, 1, 0, window, true);
                renderöiPalikanOsa(palikka, 2, 0, window, true);
                renderöiPalikanOsa(palikka, 1, 1, window, true);
            }
        }
    }

    public static void siirräPalikkaa(boolean oikealle) {
        if (!oikealle) {
            if (palikanSijX > minX) {
                if (taulukko[palikanSijX-1][palikanSijY] == 0) {
                    palikanSijX--;
                }
            }
        }
        else {
            switch (kääntö) {
                default -> {
                    if (palikanSijX + palikka.leveys < maxX) {
                        if (taulukko[palikanSijX+1][palikanSijY] == 0) {
                            palikanSijX++;
                        }
                    }
                }
                case 90, 270 -> {
                    if (palikanSijX + palikka.korkeus < maxX) {
                        if (taulukko[palikanSijX+1][palikanSijY] == 0) {
                            palikanSijX++;
                        }
                    }
                }
            }
        }
    }

    public static void käännäPalikkaa() {
        kääntö += 90;
        kääntö %= 360;
        switch (kääntö) {
            default -> {
                while (palikanSijX + palikka.leveys-1 >= maxX) palikanSijX--;
            }
            case 90, 270 -> {
                while (palikanSijX + palikka.korkeus-1 >= maxX) palikanSijX--;
            }
        }
    }

    public static void pudotaPalikka() {
        if (!odotaPalikanJälkeen) {
            for (int i = maxY; i >= 0; i--) {
                if (i+1 < maxY) {
                    if (tarkistaPalikanKohde(palikanSijX, i)) {
                        asetaPalikka(palikanSijX, i);
                        break;
                    }
                }
                if (i == 0) {
                    asetaPalikka(palikanSijX, i);
                    break;
                }
            }
        }
    }

    private static boolean tarkistaPalikanSiirrettävyys(int sijX, int sijY, boolean oikealle) {
        if (!oikealle && sijX > 0) {
            switch (palikka.tyyppi) {
                case NELIÖ -> {
                    if (taulukko[sijX-1][sijY] != 0 || taulukko[sijX-1][sijY+1] != 0) {
                        return true;
                    }
                    else return false;
                }
                case L_PALIKKA -> {
                    if (taulukko[sijX-1][sijY] != 0 || taulukko[sijX+1][sijY+1] != 0) {
                        return true;
                    }
                    else return false;
                }
                case T_PALIKKA -> {
                    if (taulukko[sijX-1][sijY] != 0 || taulukko[sijX][sijY+1] != 0) {
                        return true;
                    }
                    else return false;
                }
                case Z_PALIKKA -> {
                    if (taulukko[sijX-1][sijY] != 0 || taulukko[sijX][sijY+1] != 0) {
                        return true;
                    }
                    else return false;
                }
                case SUORA -> {
                    if (taulukko[sijX-1][sijY] != 0) {
                        return true;
                    }
                    else return false;
                }
                default -> {return false;}
            }
        }
        else if (oikealle && sijX < maxX - palikka.leveys) {
            switch (palikka.tyyppi) {
                case NELIÖ -> {
                    if (taulukko[sijX+2][sijY] != 0 || taulukko[sijX+2][sijY+1] != 0) {
                        return true;
                    }
                    else return false;
                }
                case L_PALIKKA -> {
                    if (taulukko[sijX+3][sijY] != 0 || taulukko[sijX+3][sijY+1] != 0) {
                        return true;
                    }
                    else return false;
                }
                case T_PALIKKA -> {
                    if (taulukko[sijX+3][sijY] != 0 || taulukko[sijX+2][sijY+1] != 0) {
                        return true;
                    }
                    else return false;
                }
                case Z_PALIKKA -> {
                    if (taulukko[sijX+2][sijY] != 0 || taulukko[sijX+3][sijY+1] != 0) {
                        return true;
                    }
                    else return false;
                }
                case SUORA -> {
                    if (taulukko[sijX+4][sijY] != 0) {
                        return true;
                    }
                    else return false;
                }
                default -> {return false;}
            }
        }
        else return false;
    }

    private static boolean tarkistaPalikanKohde(int sijX, int sijY) {
        if (sijY > 0) {
            switch (palikka.tyyppi) {
                case NELIÖ -> {
                    if (taulukko[sijX][sijY-1] != 0 || taulukko[sijX+1][sijY-1] != 0) {
                        return true;
                    }
                    else return false;
                }
                case L_PALIKKA -> {
                    switch (kääntö) {
                        default -> {
                            if (taulukko[sijX][sijY-1] != 0 || taulukko[sijX+1][sijY-1] != 0 || taulukko[sijX+2][sijY-1] != 0) {
                                return true;
                            }
                            else return false;
                        }
                        case 90 -> {
                            if (taulukko[sijX][sijY-1] != 0 || taulukko[sijX+1][sijY-1] != 0) {
                                return true;
                            }
                            else return false;
                        }
                        case 180 -> {
                            if (taulukko[sijX][sijY-1] != 0 || taulukko[sijX+1][sijY] != 0 || taulukko[sijX+2][sijY] != 0) {
                                return true;
                            }
                            else return false;
                        }
                        case 270 -> {
                            if (sijY < maxY-1 && (taulukko[sijX][sijY+1] != 0 || taulukko[sijX+1][sijY-1] != 0)) {
                                return true;
                            }
                            else return false;
                        }
                    }
                }
                case L_PALIKKA_2 -> {
                    switch (kääntö) {
                        default -> {
                            if (taulukko[sijX][sijY-1] != 0 || taulukko[sijX+1][sijY-1] != 0 || taulukko[sijX+2][sijY-1] != 0) {
                                return true;
                            }
                            else return false;
                        }
                        case 90 -> {
                            if (sijY < maxY-1 && (taulukko[sijX][sijY-1] != 0 || taulukko[sijX+1][sijY+1] != 0)) {
                                return true;
                            }
                            else return false;
                        }
                        case 180 -> {
                            if (taulukko[sijX][sijY] != 0 || taulukko[sijX+1][sijY] != 0 || taulukko[sijX+2][sijY-1] != 0) {
                                return true;
                            }
                            else return false;
                        }
                        case 270 -> {
                            if (taulukko[sijX][sijY-1] != 0 || taulukko[sijX+1][sijY-1] != 0) {
                                return true;
                            }
                            else return false;
                        }
                    }
                }
                case T_PALIKKA -> {
                    switch (kääntö) {
                        default -> {
                            if (taulukko[sijX][sijY-1] != 0 || taulukko[sijX+1][sijY-1] != 0 || taulukko[sijX+2][sijY-1] != 0) {
                                return true;
                            }
                            else return false;
                        }
                        case 90 -> {
                            if (taulukko[sijX][sijY-1] != 0 || taulukko[sijX+1][sijY] != 0) {
                                return true;
                            }
                            else return false;
                        }
                        case 180 -> {
                            if (taulukko[sijX+1][sijY-1] != 0 || taulukko[sijX][sijY] != 0 || taulukko[sijX+2][sijY] != 0) {
                                return true;
                            }
                            else return false;
                        }
                        case 270 -> {
                            if (taulukko[sijX+1][sijY-1] != 0 || taulukko[sijX][sijY] != 0) {
                                return true;
                            }
                            else return false;
                        }
                    }
                }
                case Z_PALIKKA -> {
                    switch (kääntö) {
                        default -> {
                            if (taulukko[sijX][sijY-1] != 0 || taulukko[sijX+1][sijY-1] != 0 || taulukko[sijX+2][sijY] != 0) {
                                return true;
                            }
                            else return false;
                        }
                        case 90, 270 -> {
                            if (taulukko[sijX+1][sijY-1] != 0 || taulukko[sijX][sijY] != 0) {
                                return true;
                            }
                            else return false;
                        }
                    }
                }
                case Z_PALIKKA_2 -> {
                    switch (kääntö) {
                        default -> {
                            if (taulukko[sijX][sijY] != 0 || taulukko[sijX+1][sijY-1] != 0 || taulukko[sijX+2][sijY-1] != 0) {
                                return true;
                            }
                            else return false;
                        }
                        case 90, 270 -> {
                            if (taulukko[sijX][sijY-1] != 0 || taulukko[sijX+1][sijY] != 0) {
                                return true;
                            }
                            else return false;
                        }
                    }
                }
                case SUORA -> {
                    switch (kääntö) {
                        default -> {
                            if (taulukko[sijX][sijY-1] != 0 || taulukko[sijX+1][sijY-1] != 0 || taulukko[sijX+2][sijY-1] != 0 || taulukko[sijX+3][sijY-1] != 0) {
                                return true;
                            }
                            else return false;
                        }
                        case 90, 270 -> {
                            if (taulukko[sijX][sijY-1] != 0) {
                                return true;
                            }
                            else return false;
                        }
                    }
                }
                default -> {return false;}
            }
            // boolean osumia = false;
            // switch (kääntö) {
            //     default -> {
            //         for (int y = sijY; y < palikka.korkeus; y++) {
            //             for (int x = sijX; x < palikka.leveys; x++) {
            //                 if (palikka.osat[x][y]) {
            //                     osumia = true;
            //                 }
            //             }
            //         }
            //     }
            //     case 90 -> {
            //         for (int y = 0; y < palikka.leveys; y++) {
            //             for (int x = 0; x < palikka.korkeus; x++) {
            //                 if (palikka.osat[y][x]) {
            //                     osumia = true;
            //                 }
            //             }
            //         }
            //     }
            //     case 180 -> {
            //         for (int y = 0; y < palikka.korkeus; y++) {
            //             for (int x = 0; x < palikka.leveys; x++) {
            //                 if (palikka.osat[x][y]) {
            //                     osumia = true;
            //                 }
            //             }
            //         }
            //     }
            //     case 270 -> {
            //         for (int y = 0; y < palikka.leveys; y++) {
            //             for (int x = 0; x < palikka.korkeus; x++) {
            //                 if (palikka.osat[y][x]) {
            //                     osumia = true;
            //                 }
            //             }
            //         }
            //     }
            // }
            // if (osumia) return true;
            // else return false;
        }
        else return false;
    }

    private static void asetaPalikka(int sijX, int sijY) {
        try {
            switch (kääntö) {
                default -> {
                    for (int y = sijY; y < sijY + palikka.korkeus; y++) {
                        for (int x = sijX; x < sijX + palikka.leveys; x++) {
                            if (x < maxX && y < maxY) {
                                if (palikka.osat[x-sijX][y-sijY]) {
                                    taulukko[x][y] = palikka.tyyppiInt;
                                }
                            } 
                        }
                    }
                }
                case 90 -> {
                    for (int y = sijY; y < sijY + palikka.leveys; y++) {
                        for (int x = sijX; x < sijX + palikka.korkeus; x++) {
                            if (x < maxX && y < maxY) {
                                if (palikka.osat[palikka.leveys-1 - (y-sijY)][x-sijX]) {
                                    taulukko[x][y] = palikka.tyyppiInt;
                                }
                            } 
                        }
                    }
                }
                case 180 -> {
                    for (int y = sijY; y < sijY + palikka.korkeus; y++) {
                        for (int x = sijX; x < sijX + palikka.leveys; x++) {
                            if (x < maxX && y < maxY) {
                                if (palikka.osat[palikka.leveys-1 - (x-sijX)][palikka.korkeus-1 - (y-sijY)]) {
                                    taulukko[x][y] = palikka.tyyppiInt;
                                }
                            } 
                        }
                    }
                }
                case 270 -> {
                    for (int y = sijY; y < sijY + palikka.leveys; y++) {
                        for (int x = sijX; x < sijX + palikka.korkeus; x++) {
                            if (x < maxX && y < maxY) {
                                if (palikka.osat[y-sijY][palikka.korkeus-1 - (x-sijX)]) {
                                    taulukko[x][y] = palikka.tyyppiInt;
                                }
                            } 
                        }
                    }
                }
            }
        }
        catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        laskeVaakarivi();
        tarkistaPeliOhi();
        taulukko = siirräTaulukonAlkioitaAlas(taulukko);
        iteraatio = 0;
        odotaPalikanJälkeen = true;
    }

    private static void laskeVaakarivi() {
        for (int y = 0; y < maxY; y++) {
            int osumia = 0;
            for (int x = 0; x < maxX; x++) {
                if (taulukko[x][y] != 0) osumia++;
            }
            if (osumia == maxX) {
                for (int x = 0; x < maxX; x++) {
                    taulukko[x][y] = 0;
                }
                pelaajanPisteet++;
            }
        }
    }

    private static int[][] siirräTaulukonAlkioitaAlas(int[][] taulukko) {
        for (int i = 0; i < maxY; i++) {
            for (int y = 0; y < maxY; y++) {
                boolean osumiaRivilä = false;
                for (int x = 0; x < maxX; x++) {
                    if (taulukko[x][y] != 0) {
                        osumiaRivilä = true;
                    }
                }
                if (!osumiaRivilä) {
                    if (y+1 < maxY-1) {
                        for (int x = 0; x < maxX; x++) {
                            taulukko[x][y] = taulukko[x][y+1];
                            taulukko[x][y+1] = 0;
                        }
                    }
                }
            }
        }
        return taulukko;
    }

    private static void tarkistaPeliOhi() {
        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                if (taulukko[x][maxY-1] != 0) {
                    peliOhi = true;
                }
            }
        }
    }

    public static void pelaa() {
        if (!peliOhi && !valikko) {
            if (iteraatio % 10 == 0 && !odotaPalikanJälkeen) {
                if (palikanSijY > minY) {
                    if (tarkistaPalikanKohde(palikanSijX, palikanSijY)) {
                        asetaPalikka(palikanSijX, palikanSijY);
                    }
                    else {
                        palikanSijY -= nopeus;
                    }
                }
                else {
                    asetaPalikka(palikanSijX, palikanSijY);
                }
            }
            if (iteraatio % 100 == 99 && odotaPalikanJälkeen) {
                odotaPalikanJälkeen = false;
                palikanSijY = maxY-1;
                valitseRandomPalikka();
                while (palikanSijX + palikka.leveys >= maxX) palikanSijX--;
            }
            iteraatio++;
        }
    }

    private static void nollaa() {
        palikanSijX = 0;
        palikanSijY = maxY-1;
        pelaajanPisteet = 0;
        peliOhi = false;
        for (int y = 0; y < -minY + maxY; y++) {
            for (int x = 0; x < -minX + maxX; x++) {
                taulukko[x][y] = 0;
            }
        }
    }

    public static void ohitaValikko() {
        valikko = false;
    }

    public static void avaaToimintoIkkuna() {
        valikko = true;
        nollaa();
        Peli.syötteenTila = SyötteenTila.TOIMINTO;
        Peli.toimintoIkkuna = ToimintoIkkunanTyyppi.MINIPELI_TETRIS;
        Musat.suljeMusa();
        Musat.toistaPeliMusa("minipeli_tetris");
    }

    public static void suljeToimintoIkkuna() {
        Peli.syötteenTila = SyötteenTila.PELI;
        Pelaaja.käyttöViive = 50;
        siirtymä = 0;
        Musat.suljeMusa();
    }
}
