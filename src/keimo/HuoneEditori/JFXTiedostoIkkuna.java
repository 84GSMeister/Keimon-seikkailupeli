package keimo.HuoneEditori;

import keimo.HuoneEditori.TarinaEditori.TarinaDialogiLista;
import keimo.Utility.Käännettävä.Suunta;
import keimo.keimoEngine.toiminnot.Dialogit;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import javafx.application.*;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;

public class JFXTiedostoIkkuna {

    public enum TiedostoTyyppi {
        KOKO_TIEDOSTO,
        VAIN_HUONE;
    }

    protected static TiedostoTyyppi avausTyyppi = TiedostoTyyppi.KOKO_TIEDOSTO;
    protected static TiedostoTyyppi tallennusTyyppi = TiedostoTyyppi.KOKO_TIEDOSTO;

    public static void launchAvaaTiedosto(TiedostoTyyppi tiedostoTyyppi) {
        // This method is invoked on the EDT thread
        JFXPanel fxPanel = new JFXPanel();
        avausTyyppi = tiedostoTyyppi;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                fxPanel.setScene(luoTiedostonAvausIkkuna());
            }
       });
    }

    private static Scene luoTiedostonAvausIkkuna() {
        // This method is invoked on the JavaFX thread
        Group root = new Group();
        Scene scene = new Scene(root, 640, 360, javafx.scene.paint.Color.ALICEBLUE);
        Text text = new Text();

        switch (avausTyyppi) {
            case KOKO_TIEDOSTO:
                try {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setInitialDirectory(new File("tiedostot/pelitiedostot"));
                    ExtensionFilter tiedostoPäätteet = new ExtensionFilter("Keimon seikkailupelin tiedosto", "*.kst");
                    fileChooser.getExtensionFilters().add(tiedostoPäätteet);
                    HuoneEditoriIkkuna.jfxAvattuTiedosto = fileChooser.showOpenDialog(null);
                    if (HuoneEditoriIkkuna.jfxAvattuTiedosto != null) {
                        String[] huoneetMerkkijonoina;
                        int huoneidenMääräTiedostossa = 0;
                        String[] tarinaDialogitTiedostossa;
                        int tarinaDialogienMääräTiedostossa = 0;
                        Path path = FileSystems.getDefault().getPath(HuoneEditoriIkkuna.jfxAvattuTiedosto.getPath());
                        Charset charset = Charset.forName("UTF-8");
                        BufferedReader read = Files.newBufferedReader(path, charset);
                        String tarkastettavaRivi = null;
                        if ((tarkastettavaRivi = read.readLine()) != null) {
                            if (!tarkastettavaRivi.startsWith("<KEIMO>")) {
                                System.out.println(tarkastettavaRivi);
                                throw new FileNotFoundException();
                            }
                        }
                        while ((tarkastettavaRivi = read.readLine()) != null) {
                            if (tarkastettavaRivi.startsWith("Huone ")) {
                                huoneidenMääräTiedostossa++;
                            }
                            else if (tarkastettavaRivi.startsWith("Tarina ")) {
                                tarinaDialogienMääräTiedostossa++;
                            }
                        }
                        huoneetMerkkijonoina = new String[huoneidenMääräTiedostossa];
                        huoneidenMääräTiedostossa = 0;
                        tarinaDialogitTiedostossa = new String[tarinaDialogienMääräTiedostossa];
                        tarinaDialogienMääräTiedostossa = 0;
                        read.close();
                        read = Files.newBufferedReader(path, charset);
                        tarkastettavaRivi = read.readLine();
                        while ((tarkastettavaRivi != null)) {
                            if (tarkastettavaRivi.startsWith("Huone ")) {
                                huoneidenMääräTiedostossa++;
                                huoneetMerkkijonoina[huoneidenMääräTiedostossa-1] = "";
                                while (tarkastettavaRivi != null) {
                                    huoneetMerkkijonoina[huoneidenMääräTiedostossa-1] += tarkastettavaRivi + "\n";
                                    if (tarkastettavaRivi.startsWith("/Huone")) {
                                        break;
                                    }
                                    tarkastettavaRivi = read.readLine();
                                }
                            }
                            else if (tarkastettavaRivi.startsWith("Tarina ")) {
                                tarinaDialogienMääräTiedostossa++;
                                tarinaDialogitTiedostossa[tarinaDialogienMääräTiedostossa-1] = "";
                                while (tarkastettavaRivi != null) {
                                    tarinaDialogitTiedostossa[tarinaDialogienMääräTiedostossa-1] += tarkastettavaRivi + "\n";
                                    if (tarkastettavaRivi.startsWith("/Tarina")) {
                                        break;
                                    }
                                    tarkastettavaRivi = read.readLine();
                                }
                            }
                            else if (tarkastettavaRivi.startsWith("</KEIMO>")) {
                                break;
                            }
                            else {
                                tarkastettavaRivi = read.readLine();
                            }
                        }
                        HuoneEditoriIkkuna.huoneKartta = HuoneEditorinMetodit.luoHuoneKarttaMerkkijonosta(huoneetMerkkijonoina);
                        TarinaDialogiLista.tarinaKartta = HuoneEditorinMetodit.luoTarinaKarttaMerkkijonosta(tarinaDialogitTiedostossa);
                        HuoneEditoriIkkuna.muokattavaHuone = 0;
                        HuoneEditoriIkkuna.lataaHuoneKartasta(HuoneEditoriIkkuna.muokattavaHuone, false);
                        HuoneEditoriIkkuna.warpVasen = HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).annaReunaWarppiTiedot(Suunta.VASEN);
                        HuoneEditoriIkkuna.warpOikea = HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).annaReunaWarppiTiedot(Suunta.OIKEA);
                        HuoneEditoriIkkuna.warpAlas = HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).annaReunaWarppiTiedot(Suunta.ALAS);
                        HuoneEditoriIkkuna.warpYlös = HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).annaReunaWarppiTiedot(Suunta.YLÖS);
                        HuoneEditoriIkkuna.warpVasenHuoneId = HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).annaReunaWarpinKohdeId(Suunta.VASEN);
                        HuoneEditoriIkkuna.warpOikeaHuoneId = HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).annaReunaWarpinKohdeId(Suunta.OIKEA);
                        HuoneEditoriIkkuna.warpAlasHuoneId = HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).annaReunaWarpinKohdeId(Suunta.ALAS);
                        HuoneEditoriIkkuna.warpYlösHuoneId = HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).annaReunaWarpinKohdeId(Suunta.YLÖS);
                        read.close();
                    }
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }

                root.getChildren().add(text);
            break;
            case VAIN_HUONE:
                try {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setInitialDirectory(new File("tiedostot/pelitiedostot/huoneet"));
                    ExtensionFilter tiedostoPäätteet = new ExtensionFilter("Keimon seikkailupelin huone", "*.ksh");
                    fileChooser.getExtensionFilters().add(tiedostoPäätteet);
                    HuoneEditoriIkkuna.jfxAvattuTiedosto = fileChooser.showOpenDialog(null);
                    if (HuoneEditoriIkkuna.jfxAvattuTiedosto != null) {
                        String huoneMerkkijonoina = "";
                        Path path = FileSystems.getDefault().getPath(HuoneEditoriIkkuna.jfxAvattuTiedosto.getPath());
                        Charset charset = Charset.forName("UTF-8");
                        BufferedReader read = Files.newBufferedReader(path, charset);
                        String tarkastettavaRivi = null;
                        tarkastettavaRivi = read.readLine();
                        while ((tarkastettavaRivi != null)) {
                            if (tarkastettavaRivi.startsWith("Huone ")) {
                                huoneMerkkijonoina = "";
                                while (tarkastettavaRivi != null) {
                                    huoneMerkkijonoina += tarkastettavaRivi + "\n";
                                    if (tarkastettavaRivi.startsWith("/Huone")) {
                                        break;
                                    }
                                    tarkastettavaRivi = read.readLine();
                                }
                            }
                            else if (tarkastettavaRivi.startsWith("</KEIMO>")) {
                                break;
                            }
                            else {
                                tarkastettavaRivi = read.readLine();
                            }
                        }
                        HuoneEditoriIkkuna.huoneKartta.put(HuoneEditoriIkkuna.muokattavaHuone, HuoneEditorinMetodit.luoHuoneMerkkijonosta(huoneMerkkijonoina, HuoneEditoriIkkuna.muokattavaHuone));
                        HuoneEditoriIkkuna.lataaHuoneKartasta(HuoneEditoriIkkuna.muokattavaHuone, false);
                        HuoneEditoriIkkuna.warpVasen = HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).annaReunaWarppiTiedot(Suunta.VASEN);
                        HuoneEditoriIkkuna.warpOikea = HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).annaReunaWarppiTiedot(Suunta.OIKEA);
                        HuoneEditoriIkkuna.warpAlas = HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).annaReunaWarppiTiedot(Suunta.ALAS);
                        HuoneEditoriIkkuna.warpYlös = HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).annaReunaWarppiTiedot(Suunta.YLÖS);
                        HuoneEditoriIkkuna.warpVasenHuoneId = HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).annaReunaWarpinKohdeId(Suunta.VASEN);
                        HuoneEditoriIkkuna.warpOikeaHuoneId = HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).annaReunaWarpinKohdeId(Suunta.OIKEA);
                        HuoneEditoriIkkuna.warpAlasHuoneId = HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).annaReunaWarpinKohdeId(Suunta.ALAS);
                        HuoneEditoriIkkuna.warpYlösHuoneId = HuoneEditoriIkkuna.huoneKartta.get(HuoneEditoriIkkuna.muokattavaHuone).annaReunaWarpinKohdeId(Suunta.YLÖS);
                        read.close();
                    }
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }

                root.getChildren().add(text);
            break;
        }

        return (scene);
    }

    public static void launchTallennaTiedosto(TiedostoTyyppi tiedostoTyyppi) {
        // This method is invoked on the EDT thread
        JFXPanel fxPanel = new JFXPanel();
        tallennusTyyppi = tiedostoTyyppi;

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                fxPanel.setScene(luoTiedostonTallennusIkkuna());
            }
       });
    }

    private static Scene luoTiedostonTallennusIkkuna() {
        // This method is invoked on the JavaFX thread
        Group root = new Group();
        Scene scene = new Scene(root, 640, 360, javafx.scene.paint.Color.ALICEBLUE);
        Text text = new Text();

        switch (tallennusTyyppi) {
            case KOKO_TIEDOSTO:
                try {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setInitialDirectory(new File("tiedostot/pelitiedostot"));
                    ExtensionFilter tiedostoPäätteet = new ExtensionFilter("Keimon seikkailupelin tiedosto", "*.kst");
                    fileChooser.getExtensionFilters().add(tiedostoPäätteet);
                    File tiedosto = fileChooser.showSaveDialog(null);
                    if (tiedosto.isFile()) {
                        Writer fstream = new OutputStreamWriter(new FileOutputStream(tiedosto.getPath()), StandardCharsets.UTF_8);
                        HuoneEditoriIkkuna.jfxKokoTiedostoMerkkijonona = HuoneEditorinMetodit.luoMerkkijonotHuonekartasta(HuoneEditoriIkkuna.huoneKartta, TarinaDialogiLista.tarinaKartta, Dialogit.PitkätDialogit.vuoropuheDialogiKartta);
                        fstream.write(HuoneEditoriIkkuna.jfxKokoTiedostoMerkkijonona);
                        fstream.close();
                    }
                    else {
                        String tiedostonNimi = tiedosto.getName();
                        String tiedostonPolku = tiedosto.getPath();
                        if (!tiedostonNimi.endsWith(".kst")) {
                            tiedostonNimi += ".kst";
                        }
                        Writer fstream = new OutputStreamWriter(new FileOutputStream(tiedostonPolku), StandardCharsets.UTF_8);
                        HuoneEditoriIkkuna.jfxKokoTiedostoMerkkijonona = HuoneEditorinMetodit.luoMerkkijonotHuonekartasta(HuoneEditoriIkkuna.huoneKartta, TarinaDialogiLista.tarinaKartta, Dialogit.PitkätDialogit.vuoropuheDialogiKartta);
                        fstream.write(HuoneEditoriIkkuna.jfxKokoTiedostoMerkkijonona);
                        fstream.close();
                    }
                    HuoneEditoriIkkuna.muutoksiaTehty = false;
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                root.getChildren().add(text);
            break;
            case VAIN_HUONE:
                try {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setInitialDirectory(new File("tiedostot/pelitiedostot/huoneet"));
                    ExtensionFilter tiedostoPäätteet = new ExtensionFilter("Keimon seikkailupelin huone", "*.ksh");
                    fileChooser.getExtensionFilters().add(tiedostoPäätteet);
                    File tiedosto = fileChooser.showSaveDialog(null);
                    if (tiedosto.isFile()) {
                        Writer fstream = new OutputStreamWriter(new FileOutputStream(tiedosto.getPath()), StandardCharsets.UTF_8);
                        fstream.write(HuoneEditoriIkkuna.jfxKokoTiedostoMerkkijonona);
                        fstream.close();
                    }
                    else {
                        String tiedostonNimi = tiedosto.getName();
                        String tiedostonPolku = tiedosto.getPath();
                        if (!tiedostonNimi.endsWith(".ksh")) {
                            tiedostonNimi += ".ksh";
                        }
                        Writer fstream = new OutputStreamWriter(new FileOutputStream(tiedostonPolku), StandardCharsets.UTF_8);
                        fstream.write(HuoneEditoriIkkuna.jfxKokoTiedostoMerkkijonona);
                        fstream.close();
                    }
                    HuoneEditoriIkkuna.muutoksiaTehty = false;
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                root.getChildren().add(text);
            break;
        }
        return (scene);
    }
}
