package keimo.HuoneEditori.TarinaEditori;

import java.util.HashMap;
import javax.swing.ImageIcon;

public class TarinaDialogiLista {

    public static HashMap<String, TarinaPätkä> tarinaKartta = new HashMap<>();

    public static void luoVakioTarinaPätkät() {
        if (tarinaKartta.size() == 0) {
            luoTarina("alku");
            //luoTarina("koti");
        }
    }
    
    public static TarinaPätkä luoTarina(String tarinanTunniste) {
        int tarinanPituusRuutuina = 1;
        String[] tarinaTeksti = {"tarinan teksti"};
        String[] tarinanKuvatiedosto = {"tarinan kuva"};
        ImageIcon[] tarinanKuva = {new ImageIcon()};
        switch (tarinanTunniste) {
            case "alku":
                tarinanPituusRuutuina = 3;
                tarinaTeksti = new String[tarinanPituusRuutuina];
                tarinanKuva = new ImageIcon[tarinanPituusRuutuina];
                tarinanKuvatiedosto = new String[tarinanPituusRuutuina];
            
                tarinanKuvatiedosto[0] = "tiedostot/kuvat/tarina/alku/tarina_alku_1.png";
                tarinanKuvatiedosto[1] = "tiedostot/kuvat/tarina/alku/tarina_alku_2.gif";
                tarinanKuvatiedosto[2] = "tiedostot/kuvat/tarina/alku/tarina_alku_3.png";
                tarinanKuva[0] = new ImageIcon(tarinanKuvatiedosto[0]);
                tarinanKuva[1] = new ImageIcon(tarinanKuvatiedosto[1]);
                tarinanKuva[2] = new ImageIcon(tarinanKuvatiedosto[2]);

                tarinaTeksti[0] = "<html><p>" +
                "Keimo herää auringon säteiden aiheuttamaan kipuun. " +
                "Nämä normaalisti harmittomat luonnonilmiöt olivat päättäneet, että juuri Keimon tulisi kärsiä seuraamuksia eilispäivän nautinnosta. " +
                "Yksikään kuolevainen ei ole kokenut yhtä murhaavaa krapulaa, kuin sitä, joka yritti paraikaa pitää Keimon maassa. " +
                "Keimo ei kuitenkaan ollut tavallinen kuolevainen, ja hän kykeni yli-inhimillisellä tahdonvoimallaan avaamaan silmänsä ja katsomaan ympärilleen. " +
                "</p></html>";

                tarinaTeksti[1] = "<html><p>" +
                "Valonsäteet jatkoivat Keimon kiusaamista myös sen jälkeen, kun hän oli saanut silmänsä auki. " +
                "Säteet eivät tuoneet hänen verkkokalvoilleen kuvia, joita hän olisi tunnistanut. " +
                "Sen sijaan Keimon verkkokalvot täyttyivät kuvista, jotka olisivat tuhonneet heikomman mielen." +
                "</p></html>";

                tarinaTeksti[2] = "<html><p>" +
                "Puisto on autioitunut tavallisista ihmisistä.<br>" +
                "Niiden sijaan puistot ja kadut ovat tulvillaan ilkeän näköisiä, aseistautuneita hyypiöitä.<br>" +
                "Ne ryntäilevät nopeasti pensaiden välillä, kuin etsien vankikarkuria.<br>" +
                "Tunnelma on uhkaava, ja huolestuttavat ajatukset vaivaavat sankariamme.<br>" +
                "<br>" +
                "Etsivätkö ne minua? Ovatko ne hävittäneet jo kaikki muut?<br>" +
                "Mitä hirveyksiä täällä tapahtuu? Minun on heti päästävä kotiin!" +
                "</p></html>";

            break;
            case "koti":
                tarinanPituusRuutuina = 1;
                tarinaTeksti = new String[tarinanPituusRuutuina];
                tarinanKuva = new ImageIcon[tarinanPituusRuutuina];
                tarinanKuvatiedosto = new String[tarinanPituusRuutuina];
            
                tarinanKuvatiedosto[0] = "tiedostot/kuvat/tarina/koti/tarina_koti.png";
                tarinanKuva[0] = new ImageIcon(tarinanKuvatiedosto[0]);

                tarinaTeksti[0] = "<html><p>" +
                "Keimo saapuu kotiin ja huomaa kaikkien tavaroiden olevan levällään.<br>" +
                "Koti on täydellisessä kaaoksessa.<br>" +
                "Näyttää siltä kuin joku olisi murtautunut Keimon kotiin ja penkonut tavaroita.<br><br>" +
                "Kuka täällä on käynyt ja mitä hän haluaa?<br>" +
                "Parasta napata jotain kättä pidempää ja lähteä selvittämään asiaa.<br>" +
                "</p></html>";

            break;
        }
        TarinaPätkä tp = new TarinaPätkä(tarinanTunniste, tarinanPituusRuutuina, tarinanKuvatiedosto, tarinanKuva, tarinaTeksti);
        tarinaKartta.put(tarinanTunniste, tp);
        return tp;
    }
}
