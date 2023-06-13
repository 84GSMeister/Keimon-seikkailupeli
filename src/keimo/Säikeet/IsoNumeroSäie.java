package keimo.Säikeet;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

public class IsoNumeroSäie {
    
    private static Random r = new Random();

    private static String format(BigDecimal x) {
        NumberFormat formatter = new DecimalFormat("0.0E0");
        formatter.setRoundingMode(RoundingMode.HALF_UP);
        formatter.setMinimumFractionDigits((x.scale() > 0) ? x.precision() : x.scale());
        return formatter.format(x);
    }

    public static void luoIsoNumeroSäie() {
        Thread isoNumeroSäie = new Thread() {
            public void run() {
                System.out.println("Lasketaan päivän iso numero... ");
                BigDecimal bd = new BigDecimal(Math.pow(2.0f, 63.0f));
                int randomMäärä = r.nextInt(20);
                int randomMäärä2 = r.nextInt(20);
                for (int i = 0; i < randomMäärä; i++) {
                    bd = bd.multiply(bd).multiply(new BigDecimal(randomMäärä2));
                }
                System.out.println("Päivän iso numero: " + format(new BigDecimal("" + bd)));
            }
        };
        isoNumeroSäie.setPriority(Thread.MIN_PRIORITY);
        isoNumeroSäie.start();
    }
}
