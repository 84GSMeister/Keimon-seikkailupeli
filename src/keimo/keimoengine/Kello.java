package keimo.keimoengine;

import java.text.DecimalFormat;

public class Kello {
    private static double aikaReferenssi = System.nanoTime();
    private static boolean ajastinPysäytetty;
    private static double pauseAlkuAika = 0;
    private static double pauseLoppuAika = 0;
    private static DecimalFormat kaksiDesimaalia = new DecimalFormat("##.##");
    private static long globaaliTickit = 0;

    public static double annaAika() {
        return (double) System.nanoTime() / (double) 1_000_000_000;
    }

    public static String päivitäAika() {
		double kulunutAika = (System.nanoTime() - aikaReferenssi)/1_000_000;
        String aika = "";
		if (!ajastinPysäytetty) {
			double kulunutAikaSek = (double)kulunutAika/1000d;
			int kulunutAikaMin = (int)kulunutAikaSek / 60;
			int kulunutAikaH = (int)kulunutAikaMin / 60;
            kulunutAikaSek %= 60;
            kulunutAikaMin %= 60;
			aika = kulunutAikaH + ":" + kulunutAikaMin + ":" + kaksiDesimaalia.format(kulunutAikaSek);
		}
        else {
            kulunutAika = (pauseAlkuAika - aikaReferenssi)/1_000_000;
            double kulunutAikaSek = (double)kulunutAika/1000d;
			int kulunutAikaMin = (int)kulunutAikaSek / 60;
			int kulunutAikaH = (int)kulunutAikaMin / 60;
            kulunutAikaSek %= 60;
            kulunutAikaMin %= 60;
			aika = kulunutAikaH + ":" + kulunutAikaMin + ":" + kaksiDesimaalia.format(kulunutAikaSek);
        }
        return aika;
	}

    /** Anna globaalin ajastimen lukema */
    public static long globaaliTickit() {
        return globaaliTickit;
    }

    /** Kasvata globaalia ajastinta yhdellä */
    public static void tick() {
        globaaliTickit++;
    }

    public static void pysäytä(boolean pysäytä) {
        if (pysäytä) {
            pauseAlkuAika = System.nanoTime();
        }
        else {
            if (ajastinPysäytetty) {
                pauseLoppuAika = System.nanoTime();
                aikaReferenssi += (pauseLoppuAika - pauseAlkuAika);
            }
        }
        
        ajastinPysäytetty = pysäytä;
    }

    public static void nollaa() {
        globaaliTickit = 0;
        aikaReferenssi = System.nanoTime();
        ajastinPysäytetty = false;
    }
}


