package keimo.Utility;

import java.awt.*;
import javax.swing.*;

public class KäännettäväKuvake implements Icon {
	
    public enum Peilaus {
        NORMAALI,
        PEILAA_X,
        PEILAA_Y,
		PEILAA_MOLEMMAT;
    }

	public enum KääntöValinta {
        MYÖTÄPÄIVÄÄN,
        VASTAPÄIVÄÄN;
    }

	public enum PeilausValinta {
        PEILAA_VAAKA,
        PEILAA_PYSTY;
    }

	private Icon icon;
	private Peilaus peilaus = Peilaus.NORMAALI;
	private double skaalaus = 64;
	private float läpinäkyvyys = 1f;
	private int xSiirto = 0;
	private int ySiirto = 0;
	private double kääntöAsteet = 0;
	private boolean perusMaastoKuvake = true;

	/**
	 *  Helppo konstruktori peruskenttäkuvakkeille, jos halutaan vain kääntää. <br><br>
	 *  Kääntöasteet tulee olla 90:llä jaollinen luku. Muuten kuvake ei todennäköisesti piirry oikein.
	 *
	 *  @param kuvake kuvake, jota käännetään
	 *  @param kääntöAsteet kuvakkeen kääntö asteina (vain 90:llä jaolliset luvut kelpaavat)
	 */
	public KäännettäväKuvake(Icon kuvake, double kääntöAsteet) {
		this.icon = kuvake;
		this.kääntöAsteet = kääntöAsteet;
	}

	/**
	 * <p>
	 * Luo käännettävän kuvakkeen.
	 * Aseta <code> perusMaastoKuvake = false </code> kaikissa tapauksissa, joissa tehdään jokin muu,
	 * kuin tavallisen 64 pikselin kenttäobjektin/maaston/npc:n kuvake.
	 * Kenttäkuvakkeet käyttävät hardkoodattuja arvoja kuvan sijainnin korjaamiselle pelikentässä käännön jälkeen.
	 * <p>
	 * Jos <code> perusMaastoKuvake </code> on <code> true </code>, <code> kääntöasteet </code> tulee olla 90:lla jaollinen.
	 * Muille kuin perusmaastokuvakkeille <code> kääntöasteet </code> voi olla mikä tahansa liukuluku.
	 * 
	 * @param kuvake kuvake, jota käännetään
	 * @param kääntöAsteet kuvakkeen kääntö asteina
	 * @param perusMaastoKuvake onko kyseessä tavallinen 64-pikselin kenttäkuvake
	 */
	public KäännettäväKuvake(Icon kuvake, double kääntöAsteet, boolean perusMaastoKuvake) {
		this(kuvake, kääntöAsteet);
		this.perusMaastoKuvake = perusMaastoKuvake;
	}

	/**
	 *  Konstruktori peruskenttäkuvakkeille, jolla voi säätää käännön ja peilauksen. <br><br>
	 *  Kääntöasteet tulee olla 90:llä jaollinen luku. Muuten kuvake ei todennäköisesti piirry oikein.
	 *
	 *  @param kuvake kuvake, jota käännetään/peilataan
	 *  @param kääntöAsteet kuvakkeen kääntö asteina (vain 90:llä jaolliset luvut kelpaavat)
	 *  @param xPeilaus peilaa kuvake vaakasuunnassa
	 *  @param yPeilaus peilaa kuvake pystysuunnassa
	 */
	public KäännettäväKuvake(Icon kuvake, double kääntöAsteet, boolean xPeilaus, boolean yPeilaus) {
		this(kuvake, kääntöAsteet);
		if (xPeilaus && yPeilaus) {
			this.peilaus = Peilaus.PEILAA_MOLEMMAT;
		}
		else if (xPeilaus) {
			this.peilaus = Peilaus.PEILAA_X;
		}
		else if (yPeilaus) {
			this.peilaus = Peilaus.PEILAA_Y;
		}
		else {
			this.peilaus = Peilaus.NORMAALI;
		}
	}

	/**
	 *  Konstruktori peruskenttäkuvakkeiden skaalatulle instanssille, jolla voi säätää käännön ja peilauksen.
	 *  Skaalattua kuvaketta voi käyttää dialogiruudussa, sekä muissa paikoissa, joissa halutaan suurentaa
	 *  tavallista 64 pikselin kenttäkuvaketta. <br><br>
	 *  Kääntöasteet tulee olla 90:llä jaollinen luku. Muuten kuvake ei todennäköisesti piirry oikein.
	 *
	 *  @param kuvake kuvake, jota käännetään
	 *  @param kääntöAsteet kuvakkeen kääntö asteina (vain 90:llä jaolliset luvut kelpaavat)
	 *  @param xPeilaus peilaa kuvake vaakasuunnassa
	 *  @param yPeilaus peilaa kuvake pystysuunnassa
	 *  @param skaalaus kerroin, jolla kuvakkeen kokoa muutetaan alkuperäisestä
	 */
	public KäännettäväKuvake(Icon kuvake, double kääntöAsteet, boolean xPeilaus, boolean yPeilaus, double skaalaus) {
		this(kuvake, kääntöAsteet);
		if (xPeilaus && yPeilaus) {
			this.peilaus = Peilaus.PEILAA_MOLEMMAT;
		}
		else if (xPeilaus) {
			this.peilaus = Peilaus.PEILAA_X;
		}
		else if (yPeilaus) {
			this.peilaus = Peilaus.PEILAA_Y;
		}
		else {
			this.peilaus = Peilaus.NORMAALI;
		}
		this.skaalaus = skaalaus;
	}


	/**
	 * Luo muokattavan kuvakkeen.
	 * Edistyneempi konstruktori, joka mahdollistaa myös muita säätöjä kuvakkeelle.
	 * 
	 * @param kuvake kuvake
	 * @param kääntöAsteet kuvakkeen kääntö asteina
	 * @param xPeilaus peilaa kuvake vaakasuunnassa
	 * @param yPeilaus peilaa kuvake pystysuunnassa
	 * @param skaalaus kerroin, jolla kuvakkeen kokoa muutetaan alkuperäisestä
	 * @param läpinäkyvyys kuvakkeen läpinäkyvyys (alfa); 0 = täysin läpinäkyvä, 1 = täysin näkyvä
	 */
	public KäännettäväKuvake(Icon kuvake, double kääntöAsteet, boolean xPeilaus, boolean yPeilaus, double skaalaus, float läpinäkyvyys) {
		this(kuvake, kääntöAsteet);
		if (xPeilaus && yPeilaus) {
			this.peilaus = Peilaus.PEILAA_MOLEMMAT;
		}
		else if (xPeilaus) {
			this.peilaus = Peilaus.PEILAA_X;
		}
		else if (yPeilaus) {
			this.peilaus = Peilaus.PEILAA_Y;
		}
		else {
			this.peilaus = Peilaus.NORMAALI;
		}
		this.skaalaus = skaalaus;
		this.läpinäkyvyys = läpinäkyvyys;
	}

	/**
	 * Luo muokattavan kuvakkeen.
	 * Edistyneempi konstruktori, joka mahdollistaa myös muita säätöjä kuvakkeelle.
	 * 
	 * @param kuvake kuvake
	 * @param kääntöAsteet kuvakkeen kääntö asteina
	 * @param xPeilaus peilaa kuvake vaakasuunnassa
	 * @param yPeilaus peilaa kuvake pystysuunnassa
	 * @param skaalaus kerroin, jolla kuvakkeen kokoa muutetaan alkuperäisestä
	 * @param läpinäkyvyys kuvakkeen läpinäkyvyys (alfa); 0 = täysin läpinäkyvä, 1 = täysin näkyvä
	 * @param xSiirto kuvan skaalauksenjälkeisen sijaintivirheen korjaus vaakasuunnassa
	 * @param ySiirto kuvan skaalauksenjälkeisen sijaintivirheen korjaus pystysuunnassa
	 * @param perusMaastoKuvake onko kyseessä tavallinen 64-pikselin kenttäkuvake (erittäin todennäköisesti ei, jos tätä konstruktoria käytetään)
	 */
	public KäännettäväKuvake(Icon kuvake, double kääntöAsteet, boolean xPeilaus, boolean yPeilaus, double skaalaus, float läpinäkyvyys, int xSiirto, int ySiirto, boolean perusMaastoKuvake) {
		this(kuvake, kääntöAsteet);
		if (xPeilaus && yPeilaus) {
			this.peilaus = Peilaus.PEILAA_MOLEMMAT;
		}
		else if (xPeilaus) {
			this.peilaus = Peilaus.PEILAA_X;
		}
		else if (yPeilaus) {
			this.peilaus = Peilaus.PEILAA_Y;
		}
		else {
			this.peilaus = Peilaus.NORMAALI;
		}
		this.skaalaus = skaalaus;
		this.läpinäkyvyys = läpinäkyvyys;
		this.xSiirto = xSiirto;
		this.ySiirto = ySiirto;
		this.perusMaastoKuvake = perusMaastoKuvake;
	}

	public static Icon luoSkaalattuGif(Icon kuvake, int resoluutio) {
        ImageIcon skaalattuKuvake = (ImageIcon)kuvake;
        Image kuva64 = skaalattuKuvake.getImage();
        Image kuva96 = kuva64.getScaledInstance(resoluutio, resoluutio, Image.SCALE_DEFAULT);
        skaalattuKuvake = new ImageIcon(kuva96);
        return skaalattuKuvake;
    }

//
//  Implement the Icon Interface
//

	/**
	 *  Gets the width of this icon.
	 *
	 *  @return the width of the icon in pixels.
	 */
	@Override
	public int getIconWidth() {
		if (icon != null) {
			if (perusMaastoKuvake) {
				return icon.getIconWidth();
			}
			else {
				double radians = Math.toRadians(kääntöAsteet);
				double sin = Math.abs( Math.sin(radians));
				double cos = Math.abs( Math.cos(radians));
				int width = (int)Math.floor(icon.getIconWidth() * cos + icon.getIconHeight() * sin);
				return width;
			}
		}
		else {
			return 0;
		}
	}

	/**
	 *  Gets the height of this icon.
	 *
	 *  @return the height of the icon in pixels.
	 */
	@Override
	public int getIconHeight() {
		if (icon != null) {
			if (perusMaastoKuvake) {
				return icon.getIconHeight();
			}
			else {
				double radians = Math.toRadians(kääntöAsteet);
				double sin = Math.abs( Math.sin(radians));
				double cos = Math.abs( Math.cos(radians));
				int height = (int)Math.floor(icon.getIconHeight() * cos + icon.getIconWidth() * sin);
				return height;
			}
		}
		else {
			return 0;
		}
	}

   /**
	*  Paint the icons of this compound icon at the specified location
	*
	*  @param c The component on which the icon is painted
	*  @param g the graphics context
	*  @param x the X coordinate of the icon's top-left corner
	*  @param y the Y coordinate of the icon's top-left corner
	*/
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		if (icon != null) {
			Graphics2D g2 = (Graphics2D)g.create();

			int cWidth = icon.getIconWidth() / 2;
			int cHeight = icon.getIconHeight() / 2;
			//int xAdjustment = (icon.getIconWidth() % 2) == 0 ? 0 : -1;
			//int yAdjustment = (icon.getIconHeight() % 2) == 0 ? 0 : -1;

			if (läpinäkyvyys < 1) {
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, läpinäkyvyys));
			}

			if (xSiirto != 0) {
				g2.translate(xSiirto, 0);
			}
			if (ySiirto != 0) {
				g2.translate(0, ySiirto);
			}

			if (skaalaus != 64f) {
				if (perusMaastoKuvake) {
					g2.scale(skaalaus/64f, skaalaus/64f);
					if (peilaus == Peilaus.PEILAA_Y || peilaus == Peilaus.PEILAA_MOLEMMAT) {
						g2.translate(0, -(skaalaus - 64f));
						g2.scale(1, 2);
					}
					else {
						g2.translate(0, -(skaalaus - 64f)/2);
					}
				}
				else {
					g2.scale(skaalaus, skaalaus);
				}
			}
			else {
				if (perusMaastoKuvake) {
					if (kääntöAsteet % 360 == 0) {
						g2.translate(x, y);
						g2.rotate(Math.toRadians(0));
					}
					else if (kääntöAsteet % 360 == 90) {
						g2.translate(x + icon.getIconHeight(), y);
						g2.rotate(Math.toRadians(90));
					}
					else if (kääntöAsteet % 360 == 180) {
						g2.translate(x + getIconHeight(), y + getIconWidth());
						g2.rotate(Math.toRadians(180));
					}
					else if (kääntöAsteet % 360 == 270) {
						g2.translate(x, y + icon.getIconWidth());
						g2.rotate(Math.toRadians(-90));
					}
				}
				else {
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setClip(x, y, getIconWidth(), getIconHeight());
					g2.translate((getIconWidth() - icon.getIconWidth()) / 2, (getIconHeight() - icon.getIconHeight()) / 2);
					g2.rotate(Math.toRadians(kääntöAsteet), x + cWidth, y + cHeight);
				}

				switch (peilaus) {
					case NORMAALI:
					break;
					case PEILAA_X:
						g2.translate(getIconWidth(), 0);
						g2.scale(-1, 1);
					break;
					case PEILAA_Y:
						g2.translate(0, getIconHeight());
						g2.scale(1, -1);
					break;
					case PEILAA_MOLEMMAT:
						g2.translate(getIconWidth(), getIconHeight());
						g2.scale(-1, -1);
					break;
				}
			}

			icon.paintIcon(c, g2, x, y);
			g2.dispose();
		}
	}
}
