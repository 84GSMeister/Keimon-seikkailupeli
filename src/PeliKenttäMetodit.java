import javax.swing.Timer;

public class PeliKenttäMetodit {
    
    static KenttäKohde[][] pelikenttä;

    static Timer päivitysTiheys = new Timer(300, e -> {
        if (!Main.pause) {
            kopioiPelikenttä();
            nollaaVihollistenTila();
            liikutaVihollisiaJatkuvaSilmukka();
            liikutaVihollisiaEsteeseenAsti();
            asetaPelikenttä();
        }
    });

    static void nollaaVihollistenTila() {
        for (int i = 0; i < Main.kentänKoko; i++) {
            for (int j = 0; j < Main.kentänKoko; j++) {
                if (pelikenttä[j][i] instanceof Vihollinen) {
                    Vihollinen vihollinen = (Vihollinen)pelikenttä[j][i];
                    vihollinen.onJoLiikutettu = false;
                }
            }
        }
    }

    static boolean liikutaVihollisiaJatkuvaSilmukka() {
        boolean vihollinenLiikutettiin = false;
        for (int i = 0; i < Main.kentänKoko; i++) {
            for (int j = 0; j < Main.kentänKoko; j++) {
                if (pelikenttä[j][i] instanceof Vihollinen) {
                    if (pelikenttä[j][i] instanceof PikkuVihu) {
                        Vihollinen vihollinen = (Vihollinen)pelikenttä[j][i];
                        if (!vihollinen.onJoLiikutettu) {
                            String suunta = vihollinen.liikeSuuntaLoop[vihollinen.seuraavaLiikesuunta % vihollinen.liikeSuuntaLoop.length];
                            if (liikuta(j, i, suunta)) {
                                vihollinen.onJoLiikutettu = true;
                                if ((Main.pelaajanSijX == j ) && (Main.pelaajanSijY == i) && (!vihollinen.onkoKukistettu())) {
                                    Pelaaja.vahingoita(vihollinen.vahinko);
                                }
                            }
                            vihollinen.seuraavaLiikesuunta++;
                        }
                    }
                }
            }
        }
        return vihollinenLiikutettiin;
    }

    static boolean liikutaVihollisiaEsteeseenAsti() {
        boolean vihollinenLiikutettiin = false;
        for (int i = 0; i < Main.kentänKoko; i++) {
            for (int j = 0; j < Main.kentänKoko; j++) {
                if (pelikenttä[j][i] instanceof Vihollinen) {
                    if (pelikenttä[j][i] instanceof PahaVihu) {
                        Vihollinen vihollinen = (Vihollinen)pelikenttä[j][i];
                        if (!vihollinen.onJoLiikutettu) {
                            String suunta = vihollinen.liikeSuuntaLoop[vihollinen.seuraavaLiikesuunta % vihollinen.liikeSuuntaLoop.length];
                            if (liikuta(j, i, suunta)) {
                                vihollinen.onJoLiikutettu = true;
                                if ((Main.pelaajanSijX == j ) && (Main.pelaajanSijY == i)) {
                                    Pelaaja.vahingoita(vihollinen.vahinko);
                                }
                                return true;
                            }
                            vihollinen.seuraavaLiikesuunta++;
                        }
                    }
                }
            }
        }
        return vihollinenLiikutettiin;
    }

    static boolean liikuta(int objSijX, int objSijY, String suunta) {
        boolean kohdeLiikutettiin = false;
        if (pelikenttä[objSijX][objSijY] == null) {
            return false;
        }
        else {
            switch (suunta) {
                case "vasen":
                    if (objSijX > 0) {
                        if (pelikenttä[objSijX-1][objSijY] == null) {
                            KenttäKohde k = pelikenttä[objSijX][objSijY];
                            pelikenttä[objSijX-1][objSijY] = k;
                            pelikenttä[objSijX][objSijY] = null;
                            return true;
                        }
                    }
                    break;
                case "oikea":
                    if (objSijX < Main.kentänKoko -1) {
                        if (pelikenttä[objSijX+1][objSijY] == null) {
                            KenttäKohde k = pelikenttä[objSijX][objSijY];
                            pelikenttä[objSijX+1][objSijY] = k;
                            pelikenttä[objSijX][objSijY] = null;
                            return true;
                        }
                    }
                    break;
                case "alas":
                    if (objSijY < Main.kentänKoko -1) {
                        if (pelikenttä[objSijX][objSijY+1] == null) {
                            KenttäKohde k = pelikenttä[objSijX][objSijY];
                            pelikenttä[objSijX][objSijY+1] = k;
                            pelikenttä[objSijX][objSijY] = null;
                            return true;
                        }
                    }
                    break;
                case "ylös":
                    if (objSijY > 0) {
                        if (pelikenttä[objSijX][objSijY-1] == null) {
                            KenttäKohde k = pelikenttä[objSijX][objSijY];
                            pelikenttä[objSijX][objSijY-1] = k;
                            pelikenttä[objSijX][objSijY] = null;
                            return true;
                        }
                    }
                    break;
                default:
                    return false;
            }
        }
        return kohdeLiikutettiin;
    }

    static void kopioiPelikenttä() {
        pelikenttä = Main.pelikenttä;
    }

    static void asetaPelikenttä() {
        Main.pelikenttä = pelikenttä;
    }
}
