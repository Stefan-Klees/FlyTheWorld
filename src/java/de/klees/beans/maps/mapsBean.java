
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
package de.klees.beans.maps;

import de.klees.beans.assignement.AgentJobList;
import de.klees.beans.system.CONF;
import de.klees.beans.system.SystemFacade;
import de.klees.data.Airport;
import de.klees.data.Assignement;
import de.klees.data.Feinabstimmung;
import de.klees.data.Fluggesellschaft;
import de.klees.data.FlughafenKlassen;
import de.klees.data.Flugrouten;
import de.klees.data.Yaacarskopf;
import de.klees.data.Yaacarspositionen;
import de.klees.data.views.ViewAirportAnflugZiele;
import de.klees.data.views.ViewFlugzeugeMietKauf;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Stefan Klees
 */
public class mapsBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<Assignement> mapAssignment;
  private List<Assignement> assignmentsAusAuftragsplanung;

  private List<AgentJobList> mapAssignmentAgent;
  private List<AgentJobList> assignmentsAusAuftragsplanungAgent;

  private List<Flugrouten> routen;

  private Airport currentAirport;

  private String FlughaefenVisuelleAnflugliste;

  // YAACARS
  private int zoom;
  private String currentPiolot;
  private Yaacarskopf PilotKopfdaten;
  private Yaacarspositionen aktuellerPilotPosdaten;
  private Airport fhVon;
  private Airport fhNach;
  private ViewFlugzeugeMietKauf aktuellesFlugzeug;

  private String AcarsMapData;
  private String tmpData;
  private String pilotKoordinaten;
  private int userInFlight;

  //FlughafenUebersicht
  private String selectedLand;
  private int selectedKlasse;
  private boolean isLoaded;
  private boolean tmpFlughaefenGeladen;

  private int frmVonKlasse;
  private int frmBisKlasse;

  private String Meldung;

  private String fgIcao;
  private int idFG;

  private final DecimalFormat df = new DecimalFormat("#,##0");

  public mapsBean() {

    zoom = 3;
    currentPiolot = "";
    AcarsMapData = "";
    tmpData = "";
    selectedKlasse = 0;
    selectedLand = "";
    Meldung = "Class 9 max. 2000 entries";
    fgIcao = "";
    pilotKoordinaten = "49, 49";

  }

  @EJB
  SystemFacade facadeSystem;

  public List<marker> getTestfunktion() {
    List<marker> mk = new ArrayList<>();

    //System.out.println("de.klees.beans.maps.mapsBean.getRouten( ID FG ) " + idFG);
    if (idFG > 0) {
      routen = facadeSystem.getFlugroutenGroupBy(idFG);

      marker mk1 = new marker();
      mk1.setJsCode("<script type='text/javascript'>");
      mk.add(mk1);

      for (Flugrouten fg : routen) {

        Airport airport = facadeSystem.getAirportByIcao(fg.getVonIcao());

        mk1 = new marker();

        mk1.setJsCode("L.marker([#{" + String.valueOf(airport.getLatitude()) + " }, #{" + String.valueOf(airport.getLongitude()) + " }], {icon: airportIcon}).addTo(mymap).bindPopup('" + "(" + fg.getVonIcao() + ") " + fg.getName() + "');\n");

//        mk1.setLatitude(String.valueOf(airport.getLatitude()));
//        mk1.setLongitude(String.valueOf(airport.getLongitude()));
//        mk1.setTooltip("(" + fg.getVonIcao() + ") " + fg.getName());
        mk.add(mk1);

      }

      mk1 = new marker();
      mk1.setJsCode("</script>");
      mk.add(mk1);

    }

    return mk;
  }

  public String getRouten() {
    String ergebnis = "";

    if (idFG > 0) {

    } else {
      idFG = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftID");
    }

    if (idFG > 0) {
      routen = facadeSystem.getFlugroutenGroupBy(idFG);

      for (Flugrouten fg : routen) {

        Airport airport = facadeSystem.getAirportByIcao(fg.getVonIcao());

        ergebnis = ergebnis + "L.marker([" + String.valueOf(airport.getLatitude()) + ", "
                + String.valueOf(airport.getLongitude()) + "], {icon: airportIcon}).addTo(mymap).bindPopup('" + "("
                + fg.getVonIcao() + ") " + fg.getName() + "');\n";

      }
    }

    return ergebnis;
  }

  public String getRoutenPolylines() {
    String ergebnis = "";
    String vonIcao = "";
    String nachIcao = "";

    if (idFG > 0) {
      routen = facadeSystem.getFlugroutenGroupBy(idFG);

      for (Flugrouten fg : routen) {

        Airport airport = facadeSystem.getAirportByIcao(fg.getVonIcao());
        vonIcao = String.valueOf(airport.getLatitude()) + ", " + String.valueOf(airport.getLongitude());

        airport = facadeSystem.getAirportByIcao(fg.getNachicao());
        nachIcao = String.valueOf(airport.getLatitude()) + ", " + String.valueOf(airport.getLongitude());

        ergebnis = ergebnis + "[[" + vonIcao + "], [" + nachIcao + "]],\n";

      }
    }

    ergebnis = "var multiCoords1 = [\n"
            + ergebnis
            + "    ];\n"
            + "    var plArray = [];\n"
            + "    for(var i=0; i<multiCoords1.length; i++) {\n"
            + "        plArray.push(L.polyline(multiCoords1[i]).addTo(mymap));\n"
            + "    }\n"
            + "    L.polylineDecorator(multiCoords1, {\n"
            + "        patterns: [\n"
            + "            {offset: 25, repeat: 50, symbol: L.Symbol.arrowHead({pixelSize: 15, pathOptions: {fillOpacity: 1, weight: 0}})}\n"
            + "        ]\n"
            + "    }).addTo(mymap);";

    return ergebnis;

  }

  /*
  ****************** Visuelle Auftragsplanung BEGINN
   */
  public String getFlughafenAnflugziele() {
    String ergebnis = "";
    String home = "";
    String icao = "";
    String ToolTip = "";

    try {
      icao = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Assignment_ICAO");
      icao = icao.toUpperCase();
    } catch (NullPointerException e) {
      icao = "";
    }

    if (!icao.equals("")) {
      Airport fh = facadeSystem.getAirportByIcao(icao);

      if (fh != null) {

        ToolTip = "<h3>" + fh.getIcao() + " " + fh.getName() + "</h3>"
                + KlassenName(fh.getKlasse()) + "<br>"
                + "Location : " + fh.getStadt() + " " + fh.getLand() + " " + fh.getBundesland()
                + "<hr>"
                + "Elevation : " + String.valueOf(fh.getHoehe()) + "<br>"
                + "Longest runway : " + String.valueOf(fh.getLaengsteLandeBahn()) + "<br>"
                + "Max. Pax : " + String.valueOf(fh.getMaxpassagiereprotag()) + "<br>"
                + "Max. Cargo : " + String.valueOf(fh.getMaxCargo()) + "<br>"
                + "<hr>";

        home = "L.marker([" + String.valueOf(fh.getLatitude()) + ", " + String.valueOf(fh.getLongitude()) + "], {icon: airportIcon}).addTo(mymap).bindPopup('" + ToolTip.replaceAll("'", "") + "');\n";

        if (assignmentsAusAuftragsplanung != null) {
          mapAssignment = assignmentsAusAuftragsplanung;
        } else {
          mapAssignment = facadeSystem.findByICAOLocationAndtoIcaoMap(icao);
        }

        if (mapAssignment != null) {
          for (Assignement ziele : mapAssignment) {

            Airport airport = facadeSystem.getAirportByIcao(ziele.getToIcao());

            @SuppressWarnings("unchecked")
            List<Object[]> mengen = facadeSystem.getRoutenMengen(icao, ziele.getToIcao());

            boolean pax = false;
            boolean cargo = false;
            boolean bc = false;
            boolean addModell = true;
            String symbol = "";

            try {
              if (Integer.valueOf(String.valueOf(mengen.get(0)[0])) > 0) {
                pax = true;
              } else if (Integer.valueOf(String.valueOf(mengen.get(0)[1])) > 0) {
                cargo = true;
              } else if (Integer.valueOf(String.valueOf(mengen.get(0)[2])) > 0) {
                bc = true;
              }

              if (pax || bc && !cargo) {
                symbol = "paxIcon";
              } else {
                symbol = "cargoIcon";
              }

            } catch (NumberFormatException e) {

              addModell = false;

            }

            if (addModell) {

              //System.out.println("de.klees.beans.maps.mapsBean.getFlughafenAnflugziele() + " + ziele.getToIcao());
              ToolTip = "<h3>" + ziele.getToIcao() + " " + airport.getName() + " - " + String.valueOf(mengen.get(0)[4]) + " Meilen</h3>"
                      + KlassenName(airport.getKlasse()) + "<br>"
                      + "Location : " + airport.getStadt() + " " + airport.getLand() + " " + airport.getBundesland()
                      + "<hr>"
                      + "Elevation : " + String.valueOf(airport.getHoehe()) + "<br>"
                      + "Longest runway : " + String.valueOf(airport.getLaengsteLandeBahn()) + "<br>"
                      + "Max. Pax : " + String.valueOf(airport.getMaxpassagiereprotag()) + "<br>"
                      + "Max. Cargo : " + String.valueOf(airport.getMaxCargo()) + "<br>"
                      + "<hr>"
                      + String.valueOf(mengen.get(0)[0]) + " PAX <br>"
                      + String.valueOf(mengen.get(0)[2]) + " Business-Class  <br>"
                      + String.valueOf(mengen.get(0)[1]) + " kg Cargo<br><br>"
                      + "<h3>Auftragswert: " + df.format(mengen.get(0)[3]) + " &euro;<h3>";

              ergebnis = ergebnis + "L.marker([" + String.valueOf(airport.getLatitude())
                      + ", " + String.valueOf(airport.getLongitude())
                      + "], {icon: " + symbol + "}).addTo(mymap).bindPopup('"
                      + ToolTip.replaceAll("'", "") + "');\n";

            }

          }
        }
      }
    }

    return ergebnis + home;

  }

  public String getFlughafenAnflugzieleAgent() {
    String ergebnis = "";
    String home = "";
    String icao = "";
    String ToolTip = "";

    try {
      icao = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Assignment_ICAO");
      icao = icao.toUpperCase();
    } catch (NullPointerException e) {
      icao = "";
    }

    if (!icao.equals("") && assignmentsAusAuftragsplanungAgent != null) {
      Airport fh = facadeSystem.getAirportByIcao(icao);

      if (fh != null) {

        ToolTip = "<h3>" + fh.getIcao() + " " + fh.getName() + "</h3>"
                + KlassenName(fh.getKlasse()) + "<br>"
                + "Location : " + fh.getStadt() + " " + fh.getLand() + " " + fh.getBundesland()
                + "<hr>"
                + "Elevation : " + String.valueOf(fh.getHoehe()) + "<br>"
                + "Longest runway : " + String.valueOf(fh.getLaengsteLandeBahn()) + "<br>"
                + "Max. Pax : " + String.valueOf(fh.getMaxpassagiereprotag()) + "<br>"
                + "Max. Cargo : " + String.valueOf(fh.getMaxCargo()) + "<br>"
                + "<hr>";

        home = "L.marker([" + String.valueOf(fh.getLatitude()) + ", " + String.valueOf(fh.getLongitude()) + "], {icon: airportIcon}).addTo(mymapAgent).bindPopup('" + ToolTip.replaceAll("'", "") + "');\n";

        if (assignmentsAusAuftragsplanungAgent != null) {
          mapAssignmentAgent = assignmentsAusAuftragsplanungAgent;
        }

        if (mapAssignmentAgent != null) {

          for (AgentJobList ziele : mapAssignmentAgent) {

            Airport airport = facadeSystem.getAirportByIcao(ziele.getNachICAO());

            boolean pax = false;
            boolean cargo = false;
            boolean bc = false;
            String symbol = "";

            if (ziele.getPax() > 0) {
              pax = true;
            } else if (ziele.getCargo() > 0) {
              cargo = true;
            }

            if (pax || bc && !cargo) {
              symbol = "paxIcon";
            } else {
              symbol = "cargoIcon";
            }

            ToolTip = "<h3>" + ziele.getNachICAO() + " " + airport.getName() + " - " + ziele.getEntfernung() + " Meilen</h3>"
                    + KlassenName(airport.getKlasse()) + "<br>"
                    + "Location : " + airport.getStadt() + " " + airport.getLand() + " " + airport.getBundesland()
                    + "<hr>"
                    + "Elevation : " + String.valueOf(airport.getHoehe()) + "<br>"
                    + "Longest runway : " + String.valueOf(airport.getLaengsteLandeBahn()) + "<br>"
                    + "Max. Pax : " + String.valueOf(airport.getMaxpassagiereprotag()) + "<br>"
                    + "Max. Cargo : " + String.valueOf(airport.getMaxCargo()) + "<br>"
                    + "<hr>"
                    + ziele.getPax() + " PAX <br>"
                    + ziele.getCargo() + " kg Cargo<br><br>"
                    + "<h3>Auftragswert: " + df.format(ziele.getVerguetung()) + " &euro;<h3>";

            ergebnis = ergebnis + "L.marker([" + String.valueOf(airport.getLatitude())
                    + ", " + String.valueOf(airport.getLongitude())
                    + "], {icon: " + symbol + "}).addTo(mymapAgent).bindPopup('"
                    + ToolTip.replaceAll("'", "") + "');\n";

          }
        }
      }
    }

    return ergebnis + home;

  }

  /*
  ****************** Visuelle Auftragsplanung ENDE
   */

 /*
  ****************** LiveACARS Beginn
   */
  public void onLiveArcas() {
    String ergebnis = "";
    aktuellerPilotPosdaten = null;
    aktuellesFlugzeug = null;
    PilotKopfdaten = null;

    List<Yaacarskopf> liveAcars = facadeSystem.getYAACARSFluege();

    if (liveAcars != null) {
      setUserInFlight(liveAcars.size());

      for (Yaacarskopf akt : liveAcars) {
        if (akt.getUsername().equals(currentPiolot)) {
          //Flugdaten finden und auslesen
          PilotKopfdaten = akt;
        }
      }

      // Kein Pliot ausgewählt
      if (currentPiolot.equals("")) {

        tmpData = "";

        double longi = 0.0;
        double lati = 0.0;

        //Karte füllen und versuche zu zentrieren
        for (Yaacarskopf next : liveAcars) {
          ergebnis = ergebnis + MapFuellen(next);
          longi = longi + next.getLetztepositionlongitude();
          lati = lati + next.getLetztepositionlatitude();
        }

        pilotKoordinaten = (lati / liveAcars.size()) + ", " + (longi / liveAcars.size());

        ergebnis = "var multiCoords1 = [\n"
                + ergebnis
                + "    ];\n"
                + "    var plArray = [];\n"
                + "    for(var i=0; i<multiCoords1.length; i++) {\n"
                + "        plArray.push(L.polyline(multiCoords1[i]).addTo(mymap));\n"
                + "    }\n"
                + "    L.polylineDecorator(multiCoords1, {\n"
                + "        patterns: [\n"
                + "            {offset: 25, repeat: 50, symbol: L.Symbol.arrowHead({pixelSize: 8, pathOptions: {fillOpacity: 1, weight: 0.5}})}\n"
                + "        ]\n"
                + "    }).addTo(mymap);\n\n";

      } else {
        // Es wurde ein Pilot aus der Liste ausgewählt
        tmpData = "";

        for (Yaacarskopf next : liveAcars) {
          ergebnis = ergebnis + MapFuellenFocus(next);
        }
        ergebnis = "var multiCoords1 = [\n"
                + ergebnis
                + "    ];\n"
                + "    var plArray = [];\n"
                + "    for(var i=0; i<multiCoords1.length; i++) {\n"
                + "        plArray.push(L.polyline(multiCoords1[i]).addTo(mymap));\n"
                + "    }\n"
                + "    L.polylineDecorator(multiCoords1, {\n"
                + "        patterns: [\n"
                + "            {offset: 25, repeat: 50, symbol: L.Symbol.arrowHead({pixelSize: 8, pathOptions: {fillOpacity: 1, weight: 1}})}\n"
                + "        ]\n"
                + "    }).addTo(mymap);\n\n";
      }

      setAcarsMapData(ergebnis + tmpData);
    } else {
      setUserInFlight(0);
    }

  }

  public String MapFuellen(Yaacarskopf next) {

    String vonLat;
    String vonLong;
    String nachLat;
    String nachLong;

    vonLat = String.valueOf(next.getDeparturelatitude());
    vonLong = String.valueOf(next.getDeparturelongitude());

    nachLat = String.valueOf(next.getArrivallatitude());
    nachLong = String.valueOf(next.getArrivallongitude());

    String von = vonLat + ", " + vonLong;
    String nach = nachLat + ", " + nachLong;

    String Position = next.getLetztepositionlatitude() + ", " + next.getLetztepositionlongitude();

    tmpData = tmpData + "L.marker([" + von + "], {icon: airportIcon}).addTo(mymap);\n";
    tmpData = tmpData + "L.marker([" + nach + "], {icon: airportIcon}).addTo(mymap);\n";

    String lizenz = "";
    String art = "";

    ViewFlugzeugeMietKauf flugzeug = getFlugzeugLizenz(next.getFlugzeugid());
    if (flugzeug != null) {
      lizenz = flugzeug.getLizenz();
      art = flugzeug.getFlugzeugArt();
    }

    //Kursermittlung
    Yaacarspositionen position = facadeSystem.readYAACARSLetztePosition(next.getIdyaacarskopf());

    String Icon = " " + CONF.getDomainURL() + "/images/FTW/planes_monochrom/plane_" + 1 + ".png";
    if (position != null) {
      Icon = " " + CONF.getDomainURL() + "/images/FTW/planes_monochrom/plane_" + position.getHeading() + ".png";
    }

    if (lizenz.equals("MPL")) {
      Icon = " " + CONF.getDomainURL() + "/images/FTW/jets/plane_" + 1 + ".png";
      if (position != null) {
        Icon = " " + CONF.getDomainURL() + "/images/FTW/jets/plane_" + position.getHeading() + ".png";
      }
    }

    if (lizenz.equals("ATPL")) {
      Icon = " " + CONF.getDomainURL() + "/images/FTW/heavy/plane_" + 1 + ".png";
      if (position != null) {
        Icon = " " + CONF.getDomainURL() + "/images/FTW/heavy/plane_" + position.getHeading() + ".png";
      }
    }

    if (lizenz.equals("CPL") || lizenz.equals("PPL-A")) {
      Icon = " " + CONF.getDomainURL() + "/images/FTW/kleinflugzeug/plane_" + 1 + ".png";
      if (position != null) {
        Icon = " " + CONF.getDomainURL() + "/images/FTW/kleinflugzeug/plane_" + position.getHeading() + ".png";
      }
    }

    if (art.equals("Hubschrauber")) {
      Icon = " " + CONF.getDomainURL() + "/images/FTW/helis/plane_" + 1 + ".png";
      if (position != null) {
        Icon = " " + CONF.getDomainURL() + "/images/FTW/helis/plane_" + position.getHeading() + ".png";
      }
    }

    if (art.equals("Geschäftsflugzeug")) {
      Icon = " " + CONF.getDomainURL() + "/images/FTW/bc/plane_" + 1 + ".png";
      if (position != null) {
        Icon = " " + CONF.getDomainURL() + "/images/FTW/bc/plane_" + position.getHeading() + ".png";
      }
    }

    String info = "<div style=\"width: 250px\" align=\"center\">";
    info = info + "<h1>" + next.getUsername() + "<h1>";

    String BildAirURL = getAirlineBild(next.getFlugzeugid());

    if (!BildAirURL.equals("")) {
      info = info + "<img src=\"" + BildAirURL + "\" width=\"120\">";
    }

    info = info + "<img src=\"" + getFlugzeugBild(next.getFlugzeugid()) + "\" width=\"220\">";
    info = info + "<h2>" + next.getFlugzeugtype() + "<h2>";
    info = info + "</div>";
    info = info + "<div style=\"width: 250px\" align=\"left\">";

    if (next.getFlugstatus() == 0) {
      info = info + "<h2 align=\"center\">Flight preparation</h2>";
    } else if (next.getFlugstatus() == 1) {
      info = info + "<h2 align=\"center\">In flight</h2>";
    } else if (next.getFlugstatus() == 2) {
      info = info + "<h2 align=\"center\">Arrived</h2>";
    }

    info = info + "Route: " + next.getDepartureicao() + " >>> " + next.getArrivalicao() + "<br>";
    if (position != null) {
      info = info + "IAS / TAS / GS:  " + position.getIas() + " / " + position.getTas() + " / " + position.getGs() + "<br>";
      info = info + "Heading: " + position.getHeading() + "<br>";
      info = info + "ASL: " + df.format(position.getAsl()) + "<br>";
      info = info + "AGL: " + df.format(position.getAgl()) + "<br>";
    }
    info = info + "</div>";

    tmpData = tmpData + "L.marker([" + Position + "], {icon: L.icon({iconUrl:'" + Icon + "', iconSize: [64, 64], iconAnchor: [1,1]})}).addTo(mymap).bindPopup('" + info + "');\n";

    return "[[" + von + "], [" + nach + "]],\n";

  }

  public String MapFuellenFocus(Yaacarskopf next) {

    String vonLat;
    String vonLong;
    String nachLat;
    String nachLong;
    String von = "";
    String nach = "";
    String ergebnis = "";

    if (next.getUsername().equals(currentPiolot)) {

      //Aktuelles Flugzeug holen
      aktuellesFlugzeug = facadeSystem.readFlugzeugMietKauf(next.getFlugzeugid());

      //Aktuelle Kursdaten holen
      Yaacarspositionen position = facadeSystem.readYAACARSLetztePosition(next.getIdyaacarskopf());
      aktuellerPilotPosdaten = position;

      fhVon = facadeSystem.getAirportByIcao(next.getDepartureicao());

      //"missionsart"
      //1 = Normaler FTW-Flug
      //2 = Charter
      //3 = Rettung
      //4 = Missionen
      if (next.getMissionsart() <= 2) {
        vonLat = String.valueOf(fhVon.getLatitude());
        vonLong = String.valueOf(fhVon.getLongitude());
      } else {
        vonLat = String.valueOf(next.getDeparturelatitude());
        vonLong = String.valueOf(next.getDeparturelongitude());
      }

      fhNach = facadeSystem.getAirportByIcao(next.getArrivalicao());

      // Nullpointer lat long abfangen
      if (fhNach != null) {

        if (next.getMissionsart() <= 2) {
          nachLat = String.valueOf(fhNach.getLatitude());
          nachLong = String.valueOf(fhNach.getLongitude());
        } else {
          nachLat = String.valueOf(next.getArrivallatitude());
          nachLong = String.valueOf(next.getArrivallongitude());
        }
        von = vonLat + ", " + vonLong;
        nach = nachLat + ", " + nachLong;

        tmpData = tmpData + "L.marker([" + von + "], {icon: airportIcon}).addTo(mymap);\n";
        tmpData = tmpData + "L.marker([" + nach + "], {icon: airportIcon}).addTo(mymap);\n";

        ergebnis = "[[" + von + "], [" + nach + "]],\n";

        String Position = next.getLetztepositionlatitude() + ", " + next.getLetztepositionlongitude();

        String lizenz = "";
        String art = "";

        ViewFlugzeugeMietKauf flugzeug = getFlugzeugLizenz(next.getFlugzeugid());
        if (flugzeug != null) {
          lizenz = flugzeug.getLizenz();
          art = flugzeug.getFlugzeugArt();
        }

        String Icon = " " + CONF.getDomainURL() + "/images/FTW/planes_monochrom/plane_" + 1 + ".png";
        if (position != null) {
          Icon = " " + CONF.getDomainURL() + "/images/FTW/planes_monochrom/plane_" + position.getHeading() + ".png";
        }

        if (lizenz.equals("MPL")) {
          Icon = " " + CONF.getDomainURL() + "/images/FTW/jets/plane_" + 1 + ".png";
          if (position != null) {
            Icon = " " + CONF.getDomainURL() + "/images/FTW/jets/plane_" + position.getHeading() + ".png";
          }
        }

        if (lizenz.equals("ATPL")) {
          Icon = " " + CONF.getDomainURL() + "/images/FTW/heavy/plane_" + 1 + ".png";
          if (position != null) {
            Icon = " " + CONF.getDomainURL() + "/images/FTW/heavy/plane_" + position.getHeading() + ".png";
          }
        }

        if (lizenz.equals("CPL") || lizenz.equals("PPL-A")) {
          Icon = " " + CONF.getDomainURL() + "/images/FTW/kleinflugzeug/plane_" + 1 + ".png";
          if (position != null) {
            Icon = " " + CONF.getDomainURL() + "/images/FTW/kleinflugzeug/plane_" + position.getHeading() + ".png";
          }
        }

        if (art.equals("Hubschrauber")) {
          Icon = " " + CONF.getDomainURL() + "/images/FTW/helis/plane_" + 1 + ".png";
          if (position != null) {
            Icon = " " + CONF.getDomainURL() + "/images/FTW/helis/plane_" + position.getHeading() + ".png";
          }
        }

        if (art.equals("Geschäftsflugzeug")) {
          Icon = " " + CONF.getDomainURL() + "/images/FTW/bc/plane_" + 1 + ".png";
          if (position != null) {
            Icon = " " + CONF.getDomainURL() + "/images/FTW/bc/plane_" + position.getHeading() + ".png";
          }
        }

        // Für Rettungsmission anders zusammenbauen
        if (next.getMissionsart() == 3) {
          vonLat = String.valueOf(next.getArrivallatitude());
          vonLong = String.valueOf(next.getArrivallongitude());
          von = vonLat + ", " + vonLong;

          nachLat = String.valueOf(next.getAlternatelatitude());
          nachLong = String.valueOf(next.getAlternatelongitude());
          nach = nachLat + ", " + nachLong;

          tmpData = tmpData + "L.marker([" + von + "], {icon: airportIcon}).addTo(mymap);\n";
          tmpData = tmpData + "L.marker([" + nach + "], {icon: airportIcon}).addTo(mymap);\n";

          ergebnis = ergebnis + "[[" + von + "], [" + nach + "]],\n";

          vonLat = String.valueOf(next.getAlternatelatitude());
          vonLong = String.valueOf(next.getAlternatelongitude());
          von = vonLat + ", " + vonLong;

          nachLat = String.valueOf(next.getDeparturelatitude());
          nachLong = String.valueOf(next.getDeparturelongitude());
          nach = nachLat + ", " + nachLong;

          tmpData = tmpData + "L.marker([" + von + "], {icon: airportIcon}).addTo(mymap);\n";
          tmpData = tmpData + "L.marker([" + nach + "], {icon: airportIcon}).addTo(mymap);\n";

          ergebnis = ergebnis + "[[" + von + "], [" + nach + "]],\n";

        }

        tmpData = tmpData + "L.marker([" + Position + "], {icon: L.icon({iconUrl:'" + Icon + "', iconSize: [64, 64], iconAnchor: [1,1]})}).addTo(mymap);\n";

        pilotKoordinaten = Position;

      }
    }

    return ergebnis;

  }

  public void onLiveAcarsZoomAendern() {

  }

  public int getWindBlown() {

    int direction = 180;

    if (aktuellerPilotPosdaten != null) {

      direction = aktuellerPilotPosdaten.getWinddirection();

      if (direction <= 180) {
        direction = direction + 180;
      } else {
        direction = direction - 180;
      }

      if (direction == 0) {
        direction = 180;
      }

    }

    return direction;
  }

  public List getFliegendeUser() {
    return facadeSystem.getAcarsPiloten();
  }

  /*
  ****************** LiveACARS ENDE
   */
 /*
  ******************** Flughafen Uebersicht BEGINN
   */
  public List<Object[]> getLaender() {
    return facadeSystem.readLaenderFromAirports();
  }

  public List<FlughafenKlassen> getAirportKlassen() {
    return facadeSystem.findAllKlassen();
  }

  public String getFlughaefen() {
    String ergebnis = "";
    double summeLati = 0.0;
    double summeLong = 0.0;
    int zaehler = 0;

    List<Airport> fh = null;

    if (selectedKlasse > 0) {

      fh = facadeSystem.readAirportsByKlasse(selectedKlasse, selectedLand);

      for (Airport airport : fh) {

        ergebnis = ergebnis + "L.marker([" + String.valueOf(airport.getLatitude()) + ", " + String.valueOf(airport.getLongitude()) + "], {icon: airportIcon}).addTo(mymap).bindPopup('"
                + airport.getIcao() + " " + airport.getName().replaceAll("'", "") + "<br>"
                + KlassenName(airport.getKlasse()) + "<br><br>"
                + "Location : " + airport.getStadt().replaceAll("'", "") + " " + airport.getLand().replaceAll("'", "") + " " + airport.getBundesland().replaceAll("'", "") + "<br><br>"
                + "Elevation (ft): " + String.valueOf(airport.getHoehe()) + "<br>"
                + "Longest runway : " + String.valueOf(airport.getLaengsteLandeBahn()) + "<br>"
                + "Max. Pax : " + String.valueOf(airport.getMaxpassagiereprotag()) + "<br>"
                + "Max. Cargo : " + String.valueOf(airport.getMaxCargo()) + "<br>"
                + "');\n";

        summeLati = summeLati + airport.getLatitude();
        summeLong = summeLong + airport.getLongitude();

        zaehler = zaehler + 1;

        if (selectedKlasse == 9 && zaehler > 2000) {
          break;
        }
      }

      if (fh.size() > 0) {
        pilotKoordinaten = String.valueOf(summeLati / fh.size()) + ", " + String.valueOf(summeLong / fh.size());
      } else {
        pilotKoordinaten = "49,49";
      }

    }

    return ergebnis;
  }

  /*
  ******************** Flughafen Uebersicht ENDE
   */
 /*
  ******************* grafische Anflugliste BEGINN
   */
  public String getFlughaefenVisuelleAnflugliste(Airport aktairport) {

    List<Airport> fh = null;
    boolean gefunden = false;
    String Tooltip = "";
    String ergebnis = "";
    int zaehler = 0;

    currentAirport = aktairport;

    if (currentAirport != null) {
      // Flughäfen über Umkreissuche holen
      fh = getTmpFlughaefen();
      tmpFlughaefenGeladen = true;

      // current Flughafen eintragen
      ergebnis = ergebnis + "var marker = new L.marker([" + String.valueOf(currentAirport.getLatitude()) + ", " + String.valueOf(currentAirport.getLongitude()) + "], {icon: homeIcon}).addTo(anflugMap).bindPopup('"
              + currentAirport.getIcao() + " " + currentAirport.getName().replaceAll("'", "") + "<br>"
              + KlassenName(currentAirport.getKlasse()) + "<br><br>"
              + "Location : " + currentAirport.getStadt() + " " + currentAirport.getLand() + " " + currentAirport.getBundesland() + "<br><br>"
              + "Elevation (ft): " + String.valueOf(currentAirport.getHoehe()) + "<br>"
              + "Longest runway : " + String.valueOf(currentAirport.getLaengsteLandeBahn()) + "<br>"
              + "Max. Pax : " + String.valueOf(currentAirport.getMaxpassagiereprotag()) + "<br>"
              + "Max. Cargo : " + String.valueOf(currentAirport.getMaxCargo()) + "<br>"
              + "');\n";

      List<ViewAirportAnflugZiele> fhAnflug = facadeSystem.AnflugZiele(currentAirport.getIdairport());

      // Flughäfen aus der Umkreissuche abarbeiten
      for (Airport airport : fh) {

        int Ergebnis[] = CONF.DistanzBerechnung(currentAirport.getLongitude(), currentAirport.getLatitude(), airport.getLongitude(), airport.getLatitude());

        String Location = airport.getStadt().replaceAll("'", "") + " " + airport.getLand().replaceAll("'", "") + " " + airport.getBundesland().replaceAll("'", "");

        Tooltip = airport.getIcao() + " " + airport.getName().replaceAll("'", "") + " Entfernung : " + Ergebnis[0] + " Meilen<br>"
                + KlassenName(airport.getKlasse()) + "<br><br>"
                + "Location:  " + Location + "<br><br>"
                + "Elevation (ft): " + String.valueOf(airport.getHoehe()) + "<br>"
                + "Longest runway : " + String.valueOf(airport.getLaengsteLandeBahn()) + "<br>"
                + "Max. Pax : " + String.valueOf(airport.getMaxpassagiereprotag()) + "<br>"
                + "Max. Cargo : " + String.valueOf(airport.getMaxCargo()) + "<br><br>";

        gefunden = false;

        // Flughäfen aus der tmpListe (automatische Umkreissuche) ohne Anflugziele
        // Anflugziele werden weiter unten ausgelesen, incl. der Flughäfen die sich 
        // außerhalb der tmpListe befinden
        if (!findeFlughafenInAnflugListe(fhAnflug, airport)) {
          String tmpErgebnis = ergebnis + "var marker = new L.marker([" + String.valueOf(airport.getLatitude()) + ", " + String.valueOf(airport.getLongitude()) + "], {icon: fahne}).addTo(anflugMap).bindPopup('"
                  + Tooltip + " <button type=\"button\" onclick=\"markerOnClick();\" value=\"Edit\">Edit</button>'); marker.on('click', function test() {setVars('" + airport.getIcao() + "');});\n";
          ergebnis = tmpErgebnis;
          zaehler = zaehler + 1;
        }
      }
      //
      //

      ergebnis = ergebnis + findeFlughaefenDerAnflugliste(fhAnflug);

      return ergebnis;

    }

    return "";
  }

  public String getFlughaefenVisuelleAnfluglisteFuerInfo() {

    List<Airport> fh = null;
    boolean gefunden = false;
    String Tooltip = "";
    String ergebnis = "";
    int zaehler = 0;

    if (currentAirport != null) {

      //System.out.println("de.klees.beans.maps.mapsBean.getFlughaefenVisuelleAnfluglisteFuerInfo() ICAO: " + currentAirport.getIcao() + " " + currentAirport.getLand());
      // Flughäfen über Umkreissuche holen
      fh = getTmpFlughaefen();
      tmpFlughaefenGeladen = true;

      // current Flughafen eintragen
      ergebnis = ergebnis + "var marker = new L.marker([" + String.valueOf(currentAirport.getLatitude()) + ", " + String.valueOf(currentAirport.getLongitude()) + "], {icon: homeIcon}).addTo(anflugMap).bindPopup('"
              + currentAirport.getIcao() + " " + currentAirport.getName().replaceAll("'", "") + "<br>"
              + KlassenName(currentAirport.getKlasse()) + "<br><br>"
              + "Location : " + currentAirport.getStadt() + " " + currentAirport.getLand() + " " + currentAirport.getBundesland() + "<br><br>"
              + "Elevation (ft): " + String.valueOf(currentAirport.getHoehe()) + "<br>"
              + "Longest runway : " + String.valueOf(currentAirport.getLaengsteLandeBahn()) + "<br>"
              + "Max. Pax : " + String.valueOf(currentAirport.getMaxpassagiereprotag()) + "<br>"
              + "Max. Cargo : " + String.valueOf(currentAirport.getMaxCargo()) + "<br>"
              + "');\n";

      List<ViewAirportAnflugZiele> fhAnflug = facadeSystem.AnflugZiele(currentAirport.getIdairport());

      // Flughäfen aus der Umkreissuche abarbeiten
      for (Airport airport : fh) {

        int Ergebnis[] = CONF.DistanzBerechnung(currentAirport.getLongitude(), currentAirport.getLatitude(), airport.getLongitude(), airport.getLatitude());

        String Location = airport.getStadt().replaceAll("'", "") + " " + airport.getLand().replaceAll("'", "") + " " + airport.getBundesland().replaceAll("'", "");

        Tooltip = airport.getIcao() + " " + airport.getName().replaceAll("'", "") + " Entfernung : " + Ergebnis[0] + " Meilen<br>"
                + KlassenName(airport.getKlasse()) + "<br><br>"
                + "Location:  " + Location + "<br><br>"
                + "Elevation (ft): " + String.valueOf(airport.getHoehe()) + "<br>"
                + "Longest runway : " + String.valueOf(airport.getLaengsteLandeBahn()) + "<br>"
                + "Max. Pax : " + String.valueOf(airport.getMaxpassagiereprotag()) + "<br>"
                + "Max. Cargo : " + String.valueOf(airport.getMaxCargo()) + "<br><br>";

        gefunden = false;

        // Flughäfen aus der tmpListe (automatische Umkreissuche) ohne Anflugziele
        // Anflugziele werden weiter unten ausgelesen, incl. der Flughäfen die sich 
        // außerhalb der tmpListe befinden
        if (!findeFlughafenInAnflugListe(fhAnflug, airport)) {
          String tmpErgebnis = ergebnis + "var marker = new L.marker([" + String.valueOf(airport.getLatitude()) + ", " + String.valueOf(airport.getLongitude()) + "], {icon: fahne}).addTo(anflugMap).bindPopup('"
                  + Tooltip + "');\n";
          ergebnis = tmpErgebnis;
          zaehler = zaehler + 1;
        }
      }
      //
      //

      ergebnis = ergebnis + findeFlughaefenDerAnfluglisteFuerInfo(fhAnflug);

      return ergebnis;
    }

    return "";
  }

  public void onTest() {
    setFlughaefenVisuelleAnflugliste("");
  }

  private boolean findeFlughafenInAnflugListe(List<ViewAirportAnflugZiele> anflugziele, Airport suche) {

    for (ViewAirportAnflugZiele tmpsuche : anflugziele) {
      if (tmpsuche.getIcao().equals(suche.getIcao())) {
        return true;
      }
    }
    return false;
  }

  // Flughäfen aus der Anflugliste auslesen
  private String findeFlughaefenDerAnflugliste(List<ViewAirportAnflugZiele> anflugziele) {

    String ergebnis = "\n";

    for (ViewAirportAnflugZiele ziel : anflugziele) {

      String Tooltip;
      int Ergebnis[] = CONF.DistanzBerechnung(currentAirport.getLongitude(), currentAirport.getLatitude(), ziel.getLongitude(), ziel.getLatitude());

      String Location = ziel.getStadt().replaceAll("'", "") + " " + ziel.getLand().replaceAll("'", "") + " " + ziel.getBundesland().replaceAll("'", "");

      Tooltip = ziel.getIcao() + " " + ziel.getName().replaceAll("'", "") + " Entfernung : " + Ergebnis[0] + " Meilen<br>"
              + KlassenName(ziel.getKlasse()) + "<br><br>"
              + "Location:  " + Location + "<br><br>"
              + "Elevation (ft): " + String.valueOf(ziel.getHoehe()) + "<br>"
              + "Longest runway : " + String.valueOf(ziel.getLaengsteLandeBahn()) + "<br>"
              + "Max. Pax : " + String.valueOf(ziel.getMaxpassagiereprotag()) + "<br>"
              + "Max. Cargo : " + String.valueOf(ziel.getMaxCargo()) + "<br><br>";

      String tmpErgebnis = "var marker = new L.marker([" + String.valueOf(ziel.getLatitude()) + ", " + String.valueOf(ziel.getLongitude()) + "], {icon: airportIcon}).addTo(anflugMap).bindPopup('"
              + Tooltip + " <button type=\"button\" onclick=\"markerOnClick();\" value=\"Edit\">Edit</button>'); marker.on('click', function extDaten() {setVars('" + ziel.getIcao() + "');});\n";
      ergebnis = ergebnis + tmpErgebnis;

    }

    return ergebnis;
  }

  private String findeFlughaefenDerAnfluglisteFuerInfo(List<ViewAirportAnflugZiele> anflugziele) {

    String ergebnis = "\n";

    for (ViewAirportAnflugZiele ziel : anflugziele) {

      String Tooltip;
      int Ergebnis[] = CONF.DistanzBerechnung(currentAirport.getLongitude(), currentAirport.getLatitude(), ziel.getLongitude(), ziel.getLatitude());

      String Location = ziel.getStadt().replaceAll("'", "") + " " + ziel.getLand().replaceAll("'", "") + " " + ziel.getBundesland().replaceAll("'", "");

      Tooltip = ziel.getIcao() + " " + ziel.getName().replaceAll("'", "") + " Entfernung : " + Ergebnis[0] + " Meilen<br>"
              + KlassenName(ziel.getKlasse()) + "<br><br>"
              + "Location:  " + Location + "<br><br>"
              + "Elevation (ft): " + String.valueOf(ziel.getHoehe()) + "<br>"
              + "Longest runway : " + String.valueOf(ziel.getLaengsteLandeBahn()) + "<br>"
              + "Max. Pax : " + String.valueOf(ziel.getMaxpassagiereprotag()) + "<br>"
              + "Max. Cargo : " + String.valueOf(ziel.getMaxCargo()) + "<br><br>";

      String tmpErgebnis = "var marker = new L.marker([" + String.valueOf(ziel.getLatitude()) + ", " + String.valueOf(ziel.getLongitude()) + "], {icon: airportIcon}).addTo(anflugMap).bindPopup('"
              + Tooltip + "');\n";
      ergebnis = ergebnis + tmpErgebnis;

    }

    return ergebnis;
  }

  public List<Airport> getTmpFlughaefen() {

    isLoaded = false;

    double Lati = currentAirport.getLatitude();
    double Longi = currentAirport.getLongitude();

    double bgVon = 0.0;
    double bgBis = 0.0;
    double lgVon = 0.0;
    double lgBis = 0.0;

    int klasse = currentAirport.getKlasse();
    String vonIcao = currentAirport.getIcao();

    Feinabstimmung fa = facadeSystem.readConfig();
    List<Airport> flughaefen = null;

    if (currentAirport.getBgBis() + currentAirport.getBgVon() + currentAirport.getLgVon() + currentAirport.getLgBis() != 0.0) {
      bgVon = Lati + currentAirport.getBgVon();
      bgBis = Lati - currentAirport.getBgBis();
      lgVon = Longi + currentAirport.getLgVon();
      lgBis = Longi - currentAirport.getLgBis();
    }

    double lg = 0; // Umkreis in LaengenGrad
    double bg = 0; // Umkreis in BreitenGrad

    // Von Flughafen Klasse
    switch (klasse) {
      case 1:
        bg = fa.getUkBGClass1();
        lg = fa.getUkLGClass1();

        if (bgBis + bgVon + lgVon + lgBis == 0.0) {
          bgVon = Lati + bg;
          bgBis = Lati - bg;
          lgVon = Longi + lg;
          lgBis = Longi - lg;
        }
        if (frmVonKlasse > 0 && frmBisKlasse > 0) {
          flughaefen = facadeSystem.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, frmVonKlasse, frmBisKlasse);
        } else {
          flughaefen = facadeSystem.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 1, 3);
        }
        break;
      case 2:
        bg = fa.getUkBGClass2();
        lg = fa.getUkLGClass2();
        if (bgBis + bgVon + lgVon + lgBis == 0.0) {
          bgVon = Lati + bg;
          bgBis = Lati - bg;
          lgVon = Longi + lg;
          lgBis = Longi - lg;
        }
        if (frmVonKlasse > 0 && frmBisKlasse > 0) {
          flughaefen = facadeSystem.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, frmVonKlasse, frmBisKlasse);
        } else {
          flughaefen = facadeSystem.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 1, 4);
        }
        break;
      case 3:
        bg = fa.getUkBGClass3();
        lg = fa.getUkLGClass3();
        if (bgBis + bgVon + lgVon + lgBis == 0.0) {
          bgVon = Lati + bg;
          bgBis = Lati - bg;
          lgVon = Longi + lg;
          lgBis = Longi - lg;
        }
        if (frmVonKlasse > 0 && frmBisKlasse > 0) {
          flughaefen = facadeSystem.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, frmVonKlasse, frmBisKlasse);
        } else {
          flughaefen = facadeSystem.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 1, 5);
        }
        break;
      case 4:
        bg = fa.getUkBGClass4();
        lg = fa.getUkLGClass4();
        if (bgBis + bgVon + lgVon + lgBis == 0.0) {
          bgVon = Lati + bg;
          bgBis = Lati - bg;
          lgVon = Longi + lg;
          lgBis = Longi - lg;
        }
        if (frmVonKlasse > 0 && frmBisKlasse > 0) {
          flughaefen = facadeSystem.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, frmVonKlasse, frmBisKlasse);
        } else {
          flughaefen = facadeSystem.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 2, 6);
        }
        break;
      case 5:
        bg = fa.getUkBGClass5();
        lg = fa.getUkLGClass5();
        if ((bgBis + bgVon + lgVon + lgBis) == 0.0) {
          bgVon = Lati + bg;
          bgBis = Lati - bg;
          lgVon = Longi + lg;
          lgBis = Longi - lg;
        }
        if (frmVonKlasse > 0 && frmBisKlasse > 0) {
          flughaefen = facadeSystem.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, frmVonKlasse, frmBisKlasse);
        } else {
          flughaefen = facadeSystem.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 3, 6);
        }
        break;
      case 6:
        bg = fa.getUkBGClass6();
        lg = fa.getUkLGClass6();
        if (bgBis + bgVon + lgVon + lgBis == 0.0) {
          bgVon = Lati + bg;
          bgBis = Lati - bg;
          lgVon = Longi + lg;
          lgBis = Longi - lg;
        }
        if (frmVonKlasse > 0 && frmBisKlasse > 0) {
          flughaefen = facadeSystem.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, frmVonKlasse, frmBisKlasse);
        } else {
          flughaefen = facadeSystem.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 4, 8);
        }
        break;
      case 7:
        bg = fa.getUkBGClass7();
        lg = fa.getUkLGClass7();
        if (bgBis + bgVon + lgVon + lgBis == 0.0) {
          bgVon = Lati + bg;
          bgBis = Lati - bg;
          lgVon = Longi + lg;
          lgBis = Longi - lg;
        }
        if (frmVonKlasse > 0 && frmBisKlasse > 0) {
          flughaefen = facadeSystem.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, frmVonKlasse, frmBisKlasse);
        } else {
          flughaefen = facadeSystem.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 5, 8);
        }
        break;
      case 8:
        bg = fa.getUkBGClass8();
        lg = fa.getUkLGClass8();
        if (bgBis + bgVon + lgVon + lgBis == 0.0) {
          bgVon = Lati + bg;
          bgBis = Lati - bg;
          lgVon = Longi + lg;
          lgBis = Longi - lg;
        }
        if (frmVonKlasse > 0 && frmBisKlasse > 0) {
          flughaefen = facadeSystem.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, frmVonKlasse, frmBisKlasse);
        } else {
          flughaefen = facadeSystem.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 6, 9);
        }
        break;
      case 9:
        bg = fa.getUkBGClass9();
        lg = fa.getUkLGClass9();
        if (bgBis + bgVon + lgVon + lgBis == 0.0) {
          bgVon = Lati + bg;
          bgBis = Lati - bg;
          lgVon = Longi + lg;
          lgBis = Longi - lg;
        }
        if (frmVonKlasse > 0 && frmBisKlasse > 0) {
          flughaefen = facadeSystem.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, frmVonKlasse, frmBisKlasse);
        } else {
          flughaefen = facadeSystem.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 7, 9);
        }
        break;
      case 10:
        bg = fa.getUkBGClass10();
        lg = fa.getUkLGClass10();
        if (bgBis + bgVon + lgVon + lgBis == 0.0) {
          bgVon = Lati + bg;
          bgBis = Lati - bg;
          lgVon = Longi + lg;
          lgBis = Longi - lg;
        }
        if (frmVonKlasse > 0 && frmBisKlasse > 0) {
          flughaefen = facadeSystem.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, frmVonKlasse, frmBisKlasse);
        } else {
          flughaefen = facadeSystem.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 10, 11);
        }
        break;
      case 11:
        bg = fa.getUkBGClass11();
        lg = fa.getUkLGClass11();
        if (bgBis + bgVon + lgVon + lgBis == 0.0) {
          bgVon = Lati + bg;
          bgBis = Lati - bg;
          lgVon = Longi + lg;
          lgBis = Longi - lg;
        }
        if (frmVonKlasse > 0 && frmBisKlasse > 0) {
          flughaefen = facadeSystem.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, frmVonKlasse, frmBisKlasse);
        } else {
          flughaefen = facadeSystem.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 10, 11);
        }
        break;

      default:
        bg = fa.getUkBGClass9();
        lg = fa.getUkLGClass9();
        if (bgBis + bgVon + lgVon + lgBis == 0.0) {
          bgVon = Lati + bg;
          bgBis = Lati - bg;
          lgVon = Longi + lg;
          lgBis = Longi - lg;
        }
        if (frmVonKlasse > 0 && frmBisKlasse > 0) {
          flughaefen = facadeSystem.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, frmVonKlasse, frmBisKlasse);
        } else {
          flughaefen = facadeSystem.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 6, 14);
        }
        break;
    }

    return flughaefen;
  }

  /*
  ******************* grafische Anflugliste ENDE
   */
// Koordinaten Home der Fluggesellschaft
  public String getFGKoordinaten() {

    //System.out.println("de.klees.beans.maps.mapsBean.getFGKoordinaten() " + fgIcao);
    if (!fgIcao.equals("")) {
      Airport airport = facadeSystem.getAirportByIcao(fgIcao);
      if (airport != null) {
        return String.valueOf(airport.getLatitude()) + " , " + String.valueOf(airport.getLongitude());
      }
      return "0, 0";
    } else {
      return "0, 0";
    }
  }

  //Kordinaten Flughafen
  public String getFlughafenKoordinaten() {
    String icao = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Assignment_ICAO");

    try {
      Airport airport = facadeSystem.getAirportByIcao(icao);
      return String.valueOf(airport.getLatitude()) + ", " + String.valueOf(airport.getLongitude());

    } catch (NullPointerException e) {
      return "0, 0";
    }
  }

  //Standort Flugzeug Hangar
  public String getFlugzeugStandort(String icao) {

    try {
      Airport airport = facadeSystem.getAirportByIcao(icao);
      return String.valueOf(airport.getLatitude()) + " , " + String.valueOf(airport.getLongitude());

    } catch (NullPointerException e) {
      return "0, 0";
    }
  }

  //Standort Flugzeuge Fluggesellschaft
  public String getFlugzeugStandorteFluggesellschaft() {

    int FluggesellschaftID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftID");
    String Ergebnis = "";
    String PopUp = "";
    String Header = "";
    boolean ausgabe = true;
    double summeLati = 0.0;
    double summeLong = 0.0;

    List<ViewFlugzeugeMietKauf> flugzeuge = facadeSystem.getFluggesellschaftFlugzeugeDetails(FluggesellschaftID);

    for (int i = 0; i < flugzeuge.size(); i++) {

      Airport airport = facadeSystem.getAirportByIcao(flugzeuge.get(i).getAktuellePositionICAO());

      if (!(i + 1 >= flugzeuge.size())) {
        if (!flugzeuge.get(i + 1).getAktuellePositionICAO().equals(airport.getIcao())) {
          ausgabe = true;
          PopUp = PopUp + flugzeuge.get(i).getType() + " - " + flugzeuge.get(i).getRegistrierung() + "<br>";
        } else {
          ausgabe = false;
          PopUp = PopUp + flugzeuge.get(i).getType() + " - " + flugzeuge.get(i).getRegistrierung() + "<br>";
        }
      } else {
        PopUp = PopUp + flugzeuge.get(i).getType() + " - " + flugzeuge.get(i).getRegistrierung() + "<br>";
        ausgabe = true;
      }

      if (ausgabe) {

        Header = "<strong>(" + airport.getIcao() + ") " + airport.getName() + " - " + airport.getStadt() + "<br></strong>";

        Ergebnis = Ergebnis + "var marker = new L.marker([" + String.valueOf(airport.getLatitude()) + " , " + String.valueOf(airport.getLongitude()) + "],{icon: airportIcon})"
                + ".addTo(MapHangar).bindPopup('" + Header + PopUp + "')\n";
        PopUp = "";
      }

      summeLati = summeLati + airport.getLatitude();
      summeLong = summeLong + airport.getLongitude();

    }

    pilotKoordinaten = String.valueOf(summeLati / flugzeuge.size()) + ", " + String.valueOf(summeLong / flugzeuge.size());

    return Ergebnis;
  }

  public void onAirlineID() {
    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FluggesellschaftID", idFG);
  }

  /*
  **************** MOD Tools
   */
  public void onUserFlugZuruecksetzen() {

    if (facadeSystem.onUserFlugZuruecksetzen(getPilotID())) {
      NewMessage("Flug wurde erfolgreich zurückgesetzt.");
    } else {
      NewMessage("Flug konnte nicht zurückgesetzt werden.");
    }

  }

  private void NewMessage(String neueNachricht) {
    FacesMessage msg = new FacesMessage(neueNachricht);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  private String clearURL(String inText) {
    String ClearText = inText.replaceAll("'", "&#x27;");
    ClearText = ClearText.replaceAll("\"", "&#x22;");
    return ClearText;
  }

  // Setter and Getter
  public int getIdFG() {
    return idFG;
  }

  public void setIdFG(int idFG) {
    this.idFG = idFG;
  }

  public int getPilotID() {
    if (!currentPiolot.equals("")) {
      return facadeSystem.findUserByName(currentPiolot).getIdUser();
    } else {
      return 0;
    }

  }

  public List<Assignement> getMapAssignment() {
    return mapAssignment;
  }

  public void setMapAssignment(List<Assignement> mapAssignment) {
    this.mapAssignment = mapAssignment;
  }

  public String KlassenName(int klassenNr) {

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

  public int getZoom() {
    return zoom;
  }

  public void setZoom(int zoom) {
    this.zoom = zoom;
  }

  public String getCurrentPiolot() {
    return currentPiolot;
  }

  public void setCurrentPiolot(String currentPiolot) {
    this.currentPiolot = currentPiolot;
  }

  public String getAcarsMapData() {
    return AcarsMapData;
  }

  public void setAcarsMapData(String AcarsMapData) {
    this.AcarsMapData = AcarsMapData;
  }

  public String getPilotKoordinaten() {
    return pilotKoordinaten;
  }

  public int getUserInFlight() {
    return userInFlight;
  }

  public void setUserInFlight(int userInFlight) {
    this.userInFlight = userInFlight;
  }

  public String getSelectedLand() {
    return selectedLand;
  }

  public void setSelectedLand(String selectedLand) {
    this.selectedLand = selectedLand;
  }

  public int getSelectedKlasse() {
    return selectedKlasse;
  }

  public void setSelectedKlasse(int selectedKlasse) {
    this.selectedKlasse = selectedKlasse;
  }

  public String getMeldung() {
    return Meldung;
  }

  public Airport getCurrentAirport() {
    return currentAirport;
  }

  public void setCurrentAirport(Airport currentAirport) {
    this.currentAirport = currentAirport;
  }

  public int getFrmVonKlasse() {
    return frmVonKlasse;
  }

  public void setFrmVonKlasse(int frmVonKlasse) {
    this.frmVonKlasse = frmVonKlasse;
  }

  public int getFrmBisKlasse() {
    return frmBisKlasse;
  }

  public void setFrmBisKlasse(int frmBisKlasse) {
    this.frmBisKlasse = frmBisKlasse;
  }

  public void setFlughaefenVisuelleAnflugliste(String FlughaefenVisuelleAnflugliste) {
    this.FlughaefenVisuelleAnflugliste = FlughaefenVisuelleAnflugliste;
  }

  public String getFgIcao() {
    return fgIcao;
  }

  public void setFgIcao(String fgIcao) {
    this.fgIcao = fgIcao;
  }

  public List<Assignement> getAssignmentsAusAuftragsplanung() {
    return assignmentsAusAuftragsplanung;
  }

  public void setAssignmentsAusAuftragsplanung(List<Assignement> assignmentsAusAuftragsplanung) {
    this.assignmentsAusAuftragsplanung = assignmentsAusAuftragsplanung;
  }

  public Yaacarskopf getPilotKopfdaten() {
    return PilotKopfdaten;
  }

  public Yaacarspositionen getAktuellerPilotPosdaten() {
    return aktuellerPilotPosdaten;
  }

  public void setAktuellerPilotPosdaten(Yaacarspositionen aktuellerPilotPosdaten) {
    this.aktuellerPilotPosdaten = aktuellerPilotPosdaten;
  }

  public Airport getFhVon() {
    return fhVon;
  }

  public void setFhVon(Airport fhVon) {
    this.fhVon = fhVon;
  }

  public Airport getFhNach() {
    return fhNach;
  }

  public void setFhNach(Airport fhNach) {
    this.fhNach = fhNach;
  }

  public ViewFlugzeugeMietKauf getAktuellesFlugzeug() {
    return aktuellesFlugzeug;
  }

  public void setAktuellesFlugzeug(ViewFlugzeugeMietKauf aktuellesFlugzeug) {
    this.aktuellesFlugzeug = aktuellesFlugzeug;
  }

  private String getFlugzeugBild(int idMietkauf) {

    ViewFlugzeugeMietKauf flugzeug = facadeSystem.readFlugzeugMietKauf(idMietkauf);

    if (flugzeug != null) {
      if (!flugzeug.getEigenesBildURL().equals("")) {
        return clearURL(flugzeug.getEigenesBildURL());
      } else {
        return clearURL(flugzeug.getSymbolUrl());
      }
    }

    return "";

  }

  public String getAirlineBild(int idMietkauf) {

    ViewFlugzeugeMietKauf flugzeug = facadeSystem.readFlugzeugMietKauf(idMietkauf);
    Fluggesellschaft fg = null;

    if (flugzeug != null) {
      if (flugzeug.getFluggesellschaftID() != null) {
        if (flugzeug.getFluggesellschaftID() > 0) {
          fg = facadeSystem.readAirline(flugzeug.getFluggesellschaftID());
          if (fg != null) {
            if (fg.getLogoURL() != null || !fg.getLogoURL().equals("")) {
              return clearURL(fg.getLogoURL());
            }
          }
        }
      }
    }

    return "";

  }

  private ViewFlugzeugeMietKauf getFlugzeugLizenz(int idMietkauf) {

    ViewFlugzeugeMietKauf flugzeug = facadeSystem.readFlugzeugMietKauf(idMietkauf);

    if (flugzeug != null) {
      return flugzeug;
    }

    return null;

  }

  public List<AgentJobList> getAssignmentsAusAuftragsplanungAgent() {
    return assignmentsAusAuftragsplanungAgent;
  }

  public void setAssignmentsAusAuftragsplanungAgent(List<AgentJobList> assignmentsAusAuftragsplanungAgent) {
    this.assignmentsAusAuftragsplanungAgent = assignmentsAusAuftragsplanungAgent;
  }

}
