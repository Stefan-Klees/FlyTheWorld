/* Fly the World
 * Wirtschaftsymulation für Flugsimulatoren
 * Copyright (C) 2016 Stefan Klees
 * stefan.klees@gmail.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.klees.beans.system;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import java.util.Random;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Stefan Klees
 */
public class CONF {

  public static double Basis_Gallon_nach_KG_AVGAS = 2.74583597;      // Multiplikator
  public static double Basis_Gallon_nach_KG_JETA = 3.0661835;      // Multiplikator

  public static boolean RandomJob = false;                  // 

  private static boolean resBundleLoaded = false;

  private static String originUser;

  private static ResourceBundle messages;

  /**
   *
   * @param vonLongitude
   * @param vonLatitude
   * @param nachLongitude
   * @param nachLatitude
   * @return Ergebnis 0 = Entfernung, Ergebnis 1 = kurs
   *
   */
  public static int[] DistanzBerechnung(double vonLongitude, double vonLatitude, double nachLongitude, double nachLatitude) {

    double vonLati = Math.toRadians(vonLatitude);
    double vonLong = Math.toRadians(vonLongitude);
    double nachLati = Math.toRadians(nachLatitude);
    double nachLong = Math.toRadians(nachLongitude);

    double sin_vonLati = Math.sin(vonLati);
    double sin_nachLati = Math.sin(nachLati);
    double cos_vonLati = Math.cos(vonLati);
    double cos_nachLati = Math.cos(nachLati);

    double Entfernung_Radian = Math.acos(sin_vonLati * sin_nachLati + cos_vonLati * cos_nachLati * Math.cos(nachLong - vonLong));

    Double ZielKurs = Math.acos((sin_nachLati - sin_vonLati * Math.cos(Entfernung_Radian)) / (cos_vonLati * Math.sin(Entfernung_Radian)));
    ZielKurs = Math.toDegrees(ZielKurs);

    double dist = 6378.137 * acos(sin(vonLati) * sin(nachLati) + cos(vonLati) * cos(nachLati) * cos(nachLong - vonLong));

    if (Math.sin(nachLong - vonLong) < 0.0) {
      ZielKurs = 360 - ZielKurs;
    }

    int Ergebnis[] = new int[2];
    Ergebnis[0] = (int) Math.round(dist * 0.53995680345);
    Ergebnis[1] = (int) Math.round(ZielKurs);

    return Ergebnis;
  }

  /**
   * Zufallszahl von "min"(einschliesslich) bis "max"(einschliesslich) Beispiel:zufallszahl(4,10); Moegliche
   * Zufallszahlen 4,5,6,7,8,9,10
   *
   * @param min
   * @param max
   * @return
   */
  public static int zufallszahl(int min, int max) {
    Random random = new Random();
    return random.nextInt(max - min + 1) + min;
  }

  public static double zufallszahlDouble(double min, double max) {
    Random r = new Random();
    return (double) min + (max - min) * r.nextDouble();
  }

  public static String NoScript(String Inputtext) {

    String Clear = Inputtext.replaceAll("script", "");
    Clear = Clear.replaceAll("css", "");
    Clear = Clear.replaceAll("stylesheet", "");
    Clear = Clear.replaceAll("refresh", "");
    Clear = Clear.replaceAll("meta", "");

    return Clear;
  }

  public static void setCookie(String name, String value, int expiry) {

    FacesContext facesContext = FacesContext.getCurrentInstance();

    HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
    Cookie cookie = null;

    Cookie[] userCookies = request.getCookies();
    if (userCookies != null && userCookies.length > 0) {
      for (int i = 0; i < userCookies.length; i++) {
        if (userCookies[i].getName().equals(name)) {
          cookie = userCookies[i];
          break;
        }
      }
    }

    if (cookie != null) {
      cookie.setValue(value);
    } else {
      cookie = new Cookie(name, value);
      cookie.setPath(request.getContextPath());
    }

    cookie.setMaxAge(expiry);

    HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
    response.addCookie(cookie);
  }

  public static Cookie getCookie(String name) {

    FacesContext facesContext = FacesContext.getCurrentInstance();

    HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
    Cookie cookie = null;

    Cookie[] userCookies = request.getCookies();
    if (userCookies != null && userCookies.length > 0) {
      for (int i = 0; i < userCookies.length; i++) {
        if (userCookies[i].getName().equals(name)) {
          cookie = userCookies[i];
          return cookie;
        }
      }
    }
    return null;
  }

  public static boolean isResBundleLoaded() {
    return resBundleLoaded;
  }

  public static void setResBundleLoaded(boolean resBundleLoaded) {
    CONF.resBundleLoaded = resBundleLoaded;
  }

  public static String getOriginUser() {
    return originUser;
  }

  public static void setOriginUser(String originUser) {
    CONF.originUser = originUser;
  }

  public static String KlassenName(int klassenNr) {

    switch (klassenNr) {
      case 1:
        return "Class 1 - Primary Hub";
      case 2:
        return "Class 2 - Secondary Hub";
      case 3:
        return "Class 3 - Major International Airport";
      case 4:
        return "Class 4 - Minor International Airport";
      case 5:
        return "Class 5 - Regional Airport";
      case 6:
        return "Class 6 - Large GA-Airport";
      case 7:
        return "Class 7 - Small GA-Airport";
      case 8:
        return "Class 8 - Airstrip";
      case 9:
        return "Class 9 - Unclassified";
      case 10:
        return "Class M1 - Major Military Airport";
      case 11:
        return "Class M2 - Minor Military Airport";
      case 12:
        return "Class 12 - Drop-Off";
      case 13:
        return "Class 13 - Closed ((partially) intact structure)";
      case 14:
        return "Class 14 - Closed (tore off, demolished";
      default:
        return "Unbekannte Klasse";
    }
  }

  public static String getBelag(int belag) {
    switch (belag) {
      case 1:
        return "Asphalt";
      case 2:
        return "Concrete";
      case 3:
        return "Coral";
      case 4:
        return "Dirt";
      case 5:
        return "Grass";
      case 6:
        return "Gravel";
      case 7:
        return "Helipad";
      case 8:
        return "Oil Treated";
      case 9:
        return "Snow";
      case 10:
        return "Steel Mats";
      case 11:
        return "Wasser";
      default:
        break;
    }
    return "N/A";
  }

  public static int getMinCargo(int Klasse) {

    switch (Klasse) {
      case 1:
        return 1000;
      case 2:
        return 750;
      case 3:
        return 500;
      case 4:
        return 100;
      case 5:
        return 50;
      case 6:
        return 10;
      case 7:
        return 5;
      case 8:
        return 1;
      case 9:
        // wie der Klasse 6
        return 10;
      default:
        break;
    }

    return 0;
  }

  public static int getMaxCargo(int Klasse) {
    switch (Klasse) {
      case 1:
        return 5000;
      case 2:
        return 3000;
      case 3:
        return 2000;
      case 4:
        return 1000;
      case 5:
        return 500;
      case 6:
        return 100;
      case 7:
        return 50;
      case 8:
        return 10;
      case 9:
        return 10;
      case 10:
        return 3000;
      case 11:
        return 1000;
      default:
        return 0;
    }
  }
  
  public static int getMaxKapazitaetCargoAirport(int Klasse) {
    switch (Klasse) {
      case 1:
        return 700000;
      case 2:
        return 360000;
      case 3:
        return 200000;
      case 4:
        return 100000;
      case 5:
        return 75000;
      case 6:
        return 50000;
      case 7:
        return 30000;
      case 8:
        return 20000;
      case 9:
        return 10000;
      case 10:
        return 300000;
      case 11:
        return 100000;
      default:
        return 0;
    }
  }
  
  public static int getMaxKapazitaetPaxAirport(int Klasse) {
    switch (Klasse) {
      case 1:
        return 1250;
      case 2:
        return 700;
      case 3:
        return 400;
      case 4:
        return 300;
      case 5:
        return 200;
      case 6:
        return 150;
      case 7:
        return 100;
      case 8:
        return 30;
      case 9:
        return 3;
      case 10:
        return 300;
      case 11:
        return 100;
      default:
        return 0;
    }
  }

  public static int getMinPax(int Klasse) {

    switch (Klasse) {
      case 1:
        return 30;
      case 2:
        return 20;
      case 3:
        return 15;
      case 4:
        return 5;
      case 5:
        return 3;
      case 6:
        return 2;
      case 7:
        return 1;
      case 8:
        return 1;
      case 9:
        return 1;
      case 10:
        return 1;
      case 11:
        return 1;
      default:
        return 1;
    }

  }

  public static int getMaxPax(int Klasse) {
    switch (Klasse) {
      case 1:
        return 50;
      case 2:
        return 35;
      case 3:
        return 25;
      case 4:
        return 15;
      case 5:
        return 10;
      case 6:
        return 5;
      case 7:
        return 2;
      case 8:
        return 1;
      case 9:
        return 3;
      case 10:
        return 10;
      case 11:
        return 7;
      default:
        return 0;
    }
  }

  public static int getMaxRoutenEnfernung(int Klasse) {
    switch (Klasse) {
      case 1:
        return 15000;
      case 2:
        return 15000;
      case 3:
        return 15000;
      case 4:
        return 7000;
      case 5:
        return 500;
      case 6:
        return 350;
      case 7:
        return 120;
      case 8:
        return 100;
      case 9:
        return 100;
      case 10:
        return 1000;
      case 11:
        return 1000;
      default:
        return 0;
    }

  }

  public static int getMinRoutenEnfernung(int Klasse) {
    switch (Klasse) {
      case 1:
        return 500;
      case 2:
        return 400;
      case 3:
        return 350;
      case 4:
        return 100;
      case 5:
        return 150;
      case 6:
        return 80;
      case 7:
        return 10;
      case 8:
        return 10;
      case 9:
        return 50;
      case 10:
        return 5;
      case 11:
        return 5;
      default:
        return 1;
    }

  }

  public static int getMaxLandegewichtAirport(int Klasse) {
    switch (Klasse) {
      case 1:
        return 590000;
      case 2:
        return 590000;
      case 3:
        return 500000;
      case 4:
        return 250000;
      case 5:
        return 65500;
      case 6:
        return 25000;
      case 7:
        return 9500;
      case 8:
        return 7500;
      case 9:
        return 50000;
      case 10:
        return 590000;
      case 11:
        return 590000;
      default:
        return 1;
    }

  }

  public static double getFlughafenAufschlag(int klasse) {
    switch (klasse) {
      case 1:
        return 30;
      case 2:
        return 25;
      case 3:
        return 20;
      case 4:
        return 15;
      case 5:
        return 10;
      case 6:
        return 10;
      case 7:
        return 5;
      case 8:
        return 2.5;
      case 10:
        return 10;
      case 11:
        return 10;
      default:
        break;
    }
    return 0;
  }

  public static double getSubvention(int klasse) {
    switch (klasse) {
      case 1:
        return 0.0;
      case 2:
        return 0.0;
      case 3:
        return 0.0;
      case 4:
        return 0.0;
      case 5:
        return 0.0;
      case 6:
        return 0.0;
      case 7:
        return 1.20;
      case 8:
        return 1.30;
      case 9:
        return 1.30;
      case 10:
        return 0.0;
      case 11:
        return 0.0;
      default:
        break;
    }
    return 0.0;
  }

}
