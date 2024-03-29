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

import java.io.File;
import java.io.IOException;
import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Stefan Klees
 */
public class CONF {

  public static double Basis_Gallon_nach_KG_AVGAS = 2.74583597;      // Multiplikator
  public static double Basis_Gallon_nach_KG_JETA = 3.0661835;      // Multiplikator

  public static boolean RandomJob = false;                  // 

  private static boolean resBundleLoaded = false;
  private static boolean xmlLoaded = false;

  private static String originUser;

  private static String domainURL;
  private static String websiteLink;
  private static String localWWWDir;
  private static String urlPilotenHandbuch;
  private static String urlPilotenHandbuchEN;
  private static String urlFluggesellschaftHandbuch;
  private static String urlFluggesellschaftHandbuchEN;
  private static String urlWiKi;
  // Hilfe Links
  private static String urlWikiRouten;
  private static String urlWikiAirline;
  private static String urlWikiFBO;
  private static String urlWikiBlacklist;
  
  private static String urlForumLink;
  private static String urlDonationLink;
  private static String urlDiscordLink;
  private static String urlYaacars;

  private static ResourceBundle messages;

  private static final String xmlFile = "ftw-config.xml";

  public CONF() {
  }

  private static void configLesen() throws ParserConfigurationException {

    if (!xmlLoaded) {

      DocumentBuilderFactory docbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder dbuilder = docbf.newDocumentBuilder();
      try {

        Document doc = dbuilder.parse(new File(xmlFile));

        doc.getDocumentElement().normalize();

        NodeList nodelist = doc.getElementsByTagName("ftw-settings");

        for (int temp = 0; temp < nodelist.getLength(); temp++) {
          Node node = nodelist.item(temp);
          if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;

            domainURL = element.getElementsByTagName("DomainURL").item(0).getTextContent();
            localWWWDir = element.getElementsByTagName("LocalWWWDir").item(0).getTextContent();
            websiteLink = element.getElementsByTagName("WebsiteLink").item(0).getTextContent();
            urlPilotenHandbuch = element.getElementsByTagName("urlPilotenHandbuch").item(0).getTextContent();
            urlPilotenHandbuchEN = element.getElementsByTagName("urlPilotenHandbuchEN").item(0).getTextContent();
            urlFluggesellschaftHandbuch = element.getElementsByTagName("urlFluggesellschaftHandbuch").item(0).getTextContent();
            urlFluggesellschaftHandbuchEN = element.getElementsByTagName("urlFluggesellschaftHandbuchEN").item(0).getTextContent();
            urlWiKi = element.getElementsByTagName("urlWiKi").item(0).getTextContent();
            urlWikiRouten = element.getElementsByTagName("urlWikiRouten").item(0).getTextContent();
            urlWikiAirline = element.getElementsByTagName("urlWikiAirline").item(0).getTextContent();
            urlWikiFBO = element.getElementsByTagName("urlWikiFBO").item(0).getTextContent();
            urlWikiBlacklist = element.getElementsByTagName("urlWikiBlacklist").item(0).getTextContent();
            urlForumLink = element.getElementsByTagName("urlForumLink").item(0).getTextContent();
            urlDonationLink = element.getElementsByTagName("urlDonationLink").item(0).getTextContent();
            urlDiscordLink = element.getElementsByTagName("urlDiscordLink").item(0).getTextContent();
            urlYaacars = element.getElementsByTagName("urlYaacars").item(0).getTextContent();


            System.out.println("de.klees.beans.system.CONF.configLesen() - erfolgreich");

            //ftw-config.xml nach /opt/glassfish4/glassfish/domains/DomainName/config/ kopieren
          }
        }

      } catch (IOException | SAXException ex) {
        System.out.println("Fehler ftw-config.xml");
        System.out.println(ex.getMessage());
      }

    }
  }

  public static String getDomainURL() {
    try {
      configLesen();
      xmlLoaded = true;
    } catch (ParserConfigurationException ex) {
      Logger.getLogger(CONF.class.getName()).log(Level.SEVERE, null, ex);
    }

    return domainURL;
  }

  public static String getLocalWWWDir() {
    return localWWWDir;
  }

  public static String getWebsiteLink() {
    return websiteLink;
  }

  public static String getUrlPilotenHandbuch() {
    return urlPilotenHandbuch;
  }

  public static String getUrlPilotenHandbuchEN() {
    return urlPilotenHandbuchEN;
  }

  public static String getUrlFluggesellschaftHandbuch() {
    return urlFluggesellschaftHandbuch;
  }

  public static String getUrlFluggesellschaftHandbuchEN() {
    return urlFluggesellschaftHandbuchEN;
  }

  public static String getUrlWiKi() {
    return urlWiKi;
  }

  public static String getUrlWikiRouten() {
    return urlWikiRouten;
  }

  public static String getUrlWikiAirline() {
    return urlWikiAirline;
  }

  public static String getUrlWikiFBO() {
    return urlWikiFBO;
  }

  public static String getUrlWikiBlacklist() {
    return urlWikiBlacklist;
  }

  public static String getUrlForumLink() {
    return urlForumLink;
  }

  public static String getUrlDonationLink() {
    return urlDonationLink;
  }

  public static String getUrlDiscordLink() {
    return urlDiscordLink;
  }

  public static String getUrlYaacars() {
    return urlYaacars;
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

}
