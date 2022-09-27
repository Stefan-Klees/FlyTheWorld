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


package de.klees.beans.missionen;

import de.klees.beans.system.CONF;
import de.klees.beans.system.configBean;
import de.klees.data.Flugzeugemietkauf;
import de.klees.data.Missionen;
import de.klees.data.Missionenwaypoints;
import de.klees.data.views.ViewFlugzeugeMietKauf;
import java.io.Serializable;
import java.time.temporal.TemporalQueries;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author Stefan Klees
 */
public class MissionenBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private Missionen selectedMission;
  private List<Missionen> listMissionen;

  private Missionenwaypoints selectedWayPoint;
  private List<Missionenwaypoints> listWayPoints;

  private boolean isLoaded;

  private String NewWaypoint;
  private int Zoom;
  private double aktuellerBreitengrad;
  private double aktuellerLaengengrad;
  private double markerBreitengrad;
  private double markerLaengengrad;
  private String aktuellerStandort;

  private String FlugzeugBild;
  private String FlugzeugType;

  private int WpMiles;

  public MissionenBean() {
    isLoaded = false;
    markerBreitengrad = 50.0;
    markerLaengengrad = 8.0;

    aktuellerStandort = "50.0, 8.0";

    listWayPoints = null;
    Zoom = 5;

  }

  @EJB
  MissionenFacade missFacade;

  public void refresh() {
    setSelectedWayPoint(null);
  }

  /*
  ********************** Missionen BEGINN
   */
  public List<Missionen> getMissionen() {

    if (!isLoaded) {
      listMissionen = missFacade.getMissionenVerwaltung();
      isLoaded = true;
    }

    return listMissionen;
  }

  public void createMission() {
    Missionen newMis = new Missionen();
    newMis.setKurzbezeichnung("Neue Mission, fertig zur Bearbeitung");
    newMis.setAnzahlwaypoints(0);
    newMis.setBezahlung(0.0);
    newMis.setFaktorfuerbezahlung(0.0);
    newMis.setIdflugzeug(-1);
    newMis.setMissionstext("");
    newMis.setLand("");
    newMis.setInitialetankfuellung(0);
    missFacade.neueMission(newMis);
    isLoaded = false;
    setSelectedWayPoint(null);
    setSelectedMission(newMis);
    aktuellerStandort = "50.0, 8.0";
    markerBreitengrad = 50.0;
    markerLaengengrad = 8.0;
    Meldung("Mission angelegt");
  }

  public void saveMission() {
    selectedMission.setMissionstext(clearText(selectedMission.getMissionstext()));
    missFacade.saveMission(selectedMission);
    isLoaded = false;
    Meldung("Mission gespeichert");
  }

  public void deleteMission() {
    missFacade.deleteMission(selectedMission);
    isLoaded = false;
    setSelectedMission(null);
    setSelectedWayPoint(null);
    Meldung("Mission gelöscht");
  }

  /*
  ********************** Missionen ENDE
   */
 /*
  ********************* Wegpunkte BEGINN 
   */
  public List<Missionenwaypoints> getWayPoints() {
    if (selectedMission != null) {
      listWayPoints = missFacade.getWayPoints(selectedMission.getIdmissionen());
    }
    return listWayPoints;
  }

  public void createWayPoint() {

    try {
      String tmp = String.valueOf(NewWaypoint).replaceAll("LatLng\\(", "");
      String exLatLong = tmp.replaceAll("\\)", "");
      String[] koordinaten = exLatLong.split(",");

      markerBreitengrad = Double.valueOf(koordinaten[0]);;
      markerLaengengrad = Double.valueOf(koordinaten[1]);

      aktuellerBreitengrad = Double.valueOf(koordinaten[0]);
      aktuellerLaengengrad = Double.valueOf(koordinaten[1]);

      Meldung("Wegpunkt: " + NewWaypoint);

      Missionenwaypoints WegPunkt = new Missionenwaypoints();

      WegPunkt.setAltitude(-1);
      WegPunkt.setAusladen(false);
      WegPunkt.setEinladen(false);
      WegPunkt.setIdmissionen(selectedMission.getIdmissionen());
      WegPunkt.setLanden(false);
      WegPunkt.setLongitude(aktuellerLaengengrad);
      WegPunkt.setLatitude(aktuellerBreitengrad);
      WegPunkt.setMengecargo(0);
      WegPunkt.setMengepax(0);
      WegPunkt.setVerguetung(0.0);
      WegPunkt.setWaypointtext("Neuer Wegpunkt, bearbeiten!");
      WegPunkt.setUmkreisingrad(0.0);
      WegPunkt.setReihenfolge(10000);
      WegPunkt.setTankenmoeglich(false);
      WegPunkt.setMengecargoausladen(0);
      WegPunkt.setMengepaxausladen(0);

      missFacade.createWayPoint(WegPunkt);
      NewWaypoint = "";
      aktuellerStandort = aktuellerBreitengrad + ", " + aktuellerLaengengrad;

      Meldung("Wegpunkt wurde gespeichert");

      incrementWayPoint();

    } catch (Exception e) {
      Meldung("Wegpunkt wurde nicht gespeichert, Marker positioniert?");
    }

  }

  public void saveWayPoint() {

    try {
      String tmp = String.valueOf(NewWaypoint).replaceAll("LatLng\\(", "");
      String exLatLong = tmp.replaceAll("\\)", "");
      String[] koordinaten = exLatLong.split(",");
      aktuellerBreitengrad = Double.valueOf(koordinaten[0]);
      aktuellerLaengengrad = Double.valueOf(koordinaten[1]);

      System.out.println("de.klees.beans.missionen.MissionenBean.saveWayPoint() " + aktuellerBreitengrad);
      System.out.println("de.klees.beans.missionen.MissionenBean.saveWayPoint() " + aktuellerLaengengrad);

      if (!selectedWayPoint.getLatitude().equals(aktuellerBreitengrad) || !selectedWayPoint.getLongitude().equals(aktuellerLaengengrad)) {
        selectedWayPoint.setLatitude(aktuellerBreitengrad);
        selectedWayPoint.setLongitude(aktuellerLaengengrad);
        NewWaypoint = "";
      }

    } catch (Exception e) {
    }

    selectedWayPoint.setWaypointtext(clearText(selectedWayPoint.getWaypointtext()));

    missFacade.saveWayPoint(selectedWayPoint);
    Meldung("Wegpunkt gespeichert.");
    incrementWayPoint();
  }

  public void copyWayPoint() {
    if (selectedWayPoint != null) {
      Missionenwaypoints newWaypoint = new Missionenwaypoints();
      newWaypoint = selectedWayPoint;
      newWaypoint.setReihenfolge(10000);
      newWaypoint.setWaypointtext("Kopie");
      missFacade.createWayPoint(newWaypoint);
      Meldung("Wegpunkt kopiert");
      incrementWayPoint();
    } else {
      Meldung("Kein Wegpunkt ausgewählt");
    }
  }

  public void insertWayPoint() {
    if (selectedWayPoint != null) {
      Missionenwaypoints newWaypoint = new Missionenwaypoints();
      newWaypoint = selectedWayPoint;
      newWaypoint.setReihenfolge(selectedWayPoint.getReihenfolge() - 2);
      newWaypoint.setWaypointtext("Eingefügte Kopie");
      missFacade.createWayPoint(newWaypoint);
      Meldung("Wegpunkt kopiert");
      incrementWayPoint();
    }
  }

  public void incrementWayPoint() {
    int Zaehler = 0;
    for (Missionenwaypoints wp : getWayPoints()) {
      Zaehler = Zaehler + 10;
      wp.setReihenfolge(Zaehler);
      wp.setWaypointtext(wp.getWaypointtext().replaceAll("\r\n|\n", "<br />"));

      missFacade.saveWayPoint(wp);
    }

    Missionenwaypoints ziel = null;
    Missionenwaypoints quelle = null;

    for (int i = 0; i < getWayPoints().size(); i++) {
      Missionenwaypoints wp = getWayPoints().get(i);
      quelle = wp;
      try {
        ziel = getWayPoints().get(i + 1);

        int[] ergebnis = CONF.DistanzBerechnung(quelle.getLongitude(), quelle.getLatitude(), ziel.getLongitude(), ziel.getLatitude());

        quelle.setEntfernungnaechsterwegpunkt(ergebnis[0]);
        missFacade.saveWayPoint(quelle);

      } catch (IndexOutOfBoundsException | NullPointerException e) {
        quelle.setEntfernungnaechsterwegpunkt(0);
        missFacade.saveWayPoint(quelle);
      }

    }

  }

  public void deleteWayPoint() {
    if (selectedWayPoint != null) {
      missFacade.deleteWayPoint(selectedWayPoint);
      setSelectedWayPoint(null);
      incrementWayPoint();
      Meldung("Wegpunkt gelöscht.");
    }
  }

  public int getWpEntfernung(int id) {

    listWayPoints = missFacade.getWayPoints(id);

    int miles = 0;

    try {
      for (Missionenwaypoints wp : listWayPoints) {
        miles = miles + wp.getEntfernungnaechsterwegpunkt();
      }
    } catch (Exception e) {
    }

    return miles;
  }

  /*
  ********************* Wegpunkte ENDE
   */
 /*
  ******************** Map-Operationen BEGINN
   */
  public String getWayPointMap() {

    int radius = 0;
    String ergebnis = "";
    String multicords = "";
    String retString = "";
    String von = "";
    String bis = "";
    String Icon = "";

    if (selectedMission != null) {
      listWayPoints = missFacade.getWayPoints(selectedMission.getIdmissionen());
    }

    if (listWayPoints != null) {

      if (listWayPoints.size() > 1) {
        for (Missionenwaypoints way : listWayPoints) {

          Icon = "FahneIcon";

          aktuellerBreitengrad = way.getLatitude();
          aktuellerLaengengrad = way.getLongitude();

          markerBreitengrad = way.getLatitude();
          markerLaengengrad = way.getLongitude();

          if (selectedWayPoint != null) {
            if (way.getLatitude().equals(selectedWayPoint.getLatitude()) && way.getLongitude().equals(selectedWayPoint.getLongitude())) {
              Icon = "grueneFahne";

              setAktuellerLaengengrad(selectedWayPoint.getLongitude());
              setAktuellerBreitengrad(selectedWayPoint.getLatitude());

              aktuellerStandort = selectedWayPoint.getLatitude() + ", " + selectedWayPoint.getLongitude();

              radius = (int) (110000 * selectedWayPoint.getUmkreisingrad());

              ergebnis = ergebnis + "var selectedMarker = new L.marker([" + aktuellerStandort + "], {draggable: true, icon: " + Icon + "}).addTo(MapWayPoint).bindPopup('" + way.getWaypointtext() + "');\n"
                      + "                  selectedMarker.on(\"dragend\", function (e) {setVarsWaypointPosition(e.target.getLatLng());});\n";
              ergebnis = ergebnis + "L.circle([" + String.valueOf(way.getLatitude()) + ", " + String.valueOf(way.getLongitude()) + "], {radius: " + radius + "}).addTo(MapWayPoint);\n";
            } else {
              radius = (int) (110000 * way.getUmkreisingrad());
              ergebnis = ergebnis + "L.marker([" + String.valueOf(way.getLatitude()) + ", " + String.valueOf(way.getLongitude()) + "], {icon: " + Icon + "}).addTo(MapWayPoint).bindPopup('  Popup   ');\n";
              ergebnis = ergebnis + "L.circle([" + String.valueOf(way.getLatitude()) + ", " + String.valueOf(way.getLongitude()) + "], {radius: " + radius + "}).addTo(MapWayPoint);\n";

            }
          } else {
            aktuellerStandort = way.getLatitude() + ", " + way.getLongitude();
            radius = (int) (110000 * way.getUmkreisingrad());

            ergebnis = ergebnis + "L.marker([" + String.valueOf(way.getLatitude()) + ", " + String.valueOf(way.getLongitude()) + "], {icon: " + Icon + "}).addTo(MapWayPoint).bindPopup('  Popup   ');\n";
            ergebnis = ergebnis + "L.circle([" + String.valueOf(way.getLatitude()) + ", " + String.valueOf(way.getLongitude()) + "], {radius: " + radius + "}).addTo(MapWayPoint);\n";

          }

        }

        int Zaehler = 0;
        for (Iterator<Missionenwaypoints> way = listWayPoints.iterator(); way.hasNext();) {
          Missionenwaypoints waypnt = way.next();
          Zaehler = Zaehler + 1;

          von = String.valueOf(waypnt.getLatitude()) + ", " + String.valueOf(waypnt.getLongitude());

          if (Zaehler > 1) {
            von = bis;
            bis = String.valueOf(waypnt.getLatitude()) + ", " + String.valueOf(waypnt.getLongitude());
          }

          if (way.hasNext() && Zaehler == 1) {
            waypnt = way.next();
            bis = String.valueOf(waypnt.getLatitude()) + ", " + String.valueOf(waypnt.getLongitude());
          }

          multicords = multicords + "[[" + von + "], [" + bis + "]],";

        }

        retString = ergebnis + "var multiCoords1 = [ " + multicords + " ];\n"
                + "                var plArray = [];\n"
                + "                for (var i = 0; i<multiCoords1.length; i++) {plArray.push(L.polyline(multiCoords1[i]).addTo(MapWayPoint));}\n"
                + "                L.polylineDecorator(multiCoords1, {patterns: [{offset: 25, repeat: 50, symbol: L.Symbol.arrowHead({pixelSize: 15, pathOptions: {fillOpacity: 1, weight: 0}})}]}).addTo(MapWayPoint);\n"
                + "                MapWayPoint.invalidateSize(true);\n";

      }

    }

    if (selectedWayPoint == null) {

      ergebnis = ergebnis + "                  var marker = new L.marker([ " + markerBreitengrad + ", " + markerLaengengrad + " ], {draggable: true, icon: WayIcon}).addTo(MapWayPoint).bindPopup('Neuer Wegpunkt');\n"
              + "                  marker.on(\"dragend\", function (e) {setVarsWaypointPosition(e.target.getLatLng());});\n";

      retString = retString + ergebnis;

    }

    return retString;
  }

  /*
  ******************** Map-Operationen ENDE
   */
 /*
  ******************** Flugzeug BEGINN
   */
  public List<ViewFlugzeugeMietKauf> getFlugzeugListe() {
    return missFacade.getFlugzeuge();
  }


  /*
  ******************** Flugzeug ENDE
   */
  public void onRowSelect(SelectEvent event) {
  }

  public void onRowUnselect(UnselectEvent event) {

  }

  public void onRowSelectWayPoint(SelectEvent event) {

  }

  public void onRowUnselectWayPoint(UnselectEvent event) {

  }

  private void Meldung(String neueNachricht) {
    FacesMessage msg = new FacesMessage(neueNachricht);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  private String clearText(String inText) {
    String ClearText = inText.replaceAll("'", "&#x27;");
    ClearText = ClearText.replaceAll("\"", "&#x22;");

    return ClearText;
  }

  /*
Setter and Getter
   */
  public Missionen getSelectedMission() {
    return selectedMission;
  }

  public void setSelectedMission(Missionen selectedMission) {
    this.selectedMission = selectedMission;
  }

  public String getNewWaypoint() {
    return NewWaypoint;
  }

  public void setNewWaypoint(String NewWaypoint) {
    this.NewWaypoint = NewWaypoint;
  }

  public double getAktuellerBreitengrad() {
    return aktuellerBreitengrad;
  }

  public void setAktuellerBreitengrad(double aktuellerBreitengrad) {
    this.aktuellerBreitengrad = aktuellerBreitengrad;
  }

  public double getAktuellerLaengengrad() {
    return aktuellerLaengengrad;
  }

  public void setAktuellerLaengengrad(double aktuellerLaengengrad) {
    this.aktuellerLaengengrad = aktuellerLaengengrad;
  }

  public double getMarkerBreitengrad() {
    return markerBreitengrad;
  }

  public void setMarkerBreitengrad(double markerBreitengrad) {
    this.markerBreitengrad = markerBreitengrad;
  }

  public double getMarkerLaengengrad() {
    return markerLaengengrad;
  }

  public void setMarkerLaengengrad(double markerLaengengrad) {
    this.markerLaengengrad = markerLaengengrad;
  }

  public Missionenwaypoints getSelectedWayPoint() {
    return selectedWayPoint;
  }

  public void setSelectedWayPoint(Missionenwaypoints selectedWayPoint) {
    this.selectedWayPoint = selectedWayPoint;
  }

  public int getZoom() {
    return Zoom;
  }

  public void setZoom(int Zoom) {
    this.Zoom = Zoom;
  }

  public String getAktuellerStandort() {
    return aktuellerStandort;
  }

  public void setAktuellerStandort(String aktuellerStandort) {
    this.aktuellerStandort = aktuellerStandort;
  }

  public String getFlugzeugBild(int id) {
    ViewFlugzeugeMietKauf fg = missFacade.readFlugzeug(id);
    if (fg != null) {
      FlugzeugType = fg.getType();
      return fg.getSymbolUrl();
    } else {
      return "";
    }
  }

  public String getFlugzeugType(int id) {
    ViewFlugzeugeMietKauf fg = missFacade.readFlugzeug(id);
    if (fg != null) {
      return fg.getType();
    } else {
      return "";
    }

  }

}
