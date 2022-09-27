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
package de.klees.beans.airport;

import de.klees.beans.system.CONF;
import de.klees.data.Airport;
import de.klees.data.AirportDispatchLog;
import de.klees.data.AirportZiele;
import de.klees.data.views.ViewAirportAnflugZiele;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author Stefan Klees
 */
public class airportZieleBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<ViewAirportAnflugZiele> anflugZiele;
  private ViewAirportAnflugZiele currentZiel;
  private ViewAirportAnflugZiele selectedAirportZiele;
  private List<ViewAirportAnflugZiele> selectedAnfluege;
  private List<Airport> airportList;
  private Airport currentAirport;

  private int zieleAnzahl;
  private String frmICAO;

  private int UserID;
  private String UserName;

  private boolean isLoaded;

  public airportZieleBean() {
    isLoaded = false;
    zieleAnzahl = 0;

    UserID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
    UserName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName");

  }

  @EJB
  AirportFacade facadeZiele;

  public List<ViewAirportAnflugZiele> getAnflugziele() {
    if (!isLoaded) {
      int id = 0;
      if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("AirportID") != null) {
        id = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("AirportID");
        isLoaded = true;
      }
      anflugZiele = facadeZiele.AnflugZiele(id);
    }
    return anflugZiele;
  }

  public void onDeleteAnflugZiel() {
    isLoaded = false;
    for (ViewAirportAnflugZiele ziele : selectedAnfluege) {
      facadeZiele.onDeleteAnflugZiel(ziele.getIdListe());
    }
  }

  public void onDeleteAnflugZielSchnell() {
    if (selectedAirportZiele != null) {
      facadeZiele.onDeleteAnflugZiel(selectedAirportZiele.getIdListe());
      onRefresh();
    }
  }

  public void onDeletListe() {

    if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("AirportID") != null) {
      isLoaded = false;
      facadeZiele.deleteListZielAirports((int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("AirportID"));
    }
  }

  public void onAutoDispatch() {

    facadeZiele.deleteListZielAirports(currentAirport.getIdairport());

    int maxAirports = airportList.size() - 1;

    int Zaehler = 0;

    do {
      Airport zielAirport = airportList.get(CONF.zufallszahl(0, maxAirports));

      if (!facadeZiele.ifExistAnflugziel(currentAirport.getIdairport(), zielAirport.getIcao())) {
        Zaehler++;
        onFlughafenHinzufuegenAutoDispatch(currentAirport, zielAirport);
      }

    } while (Zaehler < 10);

    currentAirport.setDispatch(true);
    facadeZiele.edit(currentAirport);

  }

  private void onFlughafenHinzufuegenAutoDispatch(Airport quelle, Airport ziel) {

    double Lati = quelle.getLatitude();
    double Longi = quelle.getLongitude();

    int Ergebnis[] = CONF.DistanzBerechnung(Longi, Lati, ziel.getLongitude(), ziel.getLatitude());

    AirportZiele newListAirport = new AirportZiele();
    newListAirport.setIdAirport(quelle.getIdairport());
    newListAirport.setIdZielAirport(ziel.getIdairport());
    newListAirport.setICAOZiel(ziel.getIcao());
    newListAirport.setKurs(Ergebnis[1]);
    newListAirport.setEntfernung(Ergebnis[0]);

    facadeZiele.createAirportZiel(newListAirport);
  }

  public void onFlughafenHinzufuegen() {

    Airport airport = null;
    int AirportID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("AirportID");
    boolean eintragen = false;

    // gibt es den Flughafen
    if (!facadeZiele.ifExistAirport(frmICAO)) {
      NewMessage("Airport mit ICAO - Code : " + frmICAO + " existiert nicht!");
    } else {
      eintragen = true;
    }

    // ist der Flughafen schon in der Liste
    if (eintragen) {
      if (facadeZiele.ifExistFlughafenInListe(frmICAO, AirportID)) {
        eintragen = false;
        NewMessage("Airport mit ICAO - Code : " + frmICAO + " existiert bereits in Liste!");
      } else {
        airport = facadeZiele.readAirportDaten(frmICAO);
      }
    }

    if (eintragen) {

      Airport QuellFlughafen = facadeZiele.readAirportDatenUeberID(AirportID);

      double Lati = QuellFlughafen.getLatitude();
      double Longi = QuellFlughafen.getLongitude();

      int Ergebnis[] = CONF.DistanzBerechnung(Longi, Lati, airport.getLongitude(), airport.getLatitude());

      AirportZiele newListAirport = new AirportZiele();
      newListAirport.setIdAirport(AirportID);
      newListAirport.setIdZielAirport(airport.getIdairport());
      newListAirport.setICAOZiel(frmICAO);
      newListAirport.setKurs(Ergebnis[1]);
      newListAirport.setEntfernung(Ergebnis[0]);

      facadeZiele.createAirportZiel(newListAirport);
    }

  }

  public void onRefresh() {
    isLoaded = false;
  }

  public void onNotamEintragAnflugziele() {

    int id = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("AirportID");
    anflugZiele = facadeZiele.AnflugZiele(id);

    String tabelle = "<br>Update: " + UserName;
    tabelle = tabelle + "<br>Date: " + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date().getTime());

    tabelle = tabelle
            + " <table cellpadding='5' border='1' align='left' width='100%'>\n"
            + " <caption><span style='font-size: x-large;'>List of approaches</span></caption>\n"
            + "	<tbody><tr><td style='font-weight: bolder; ' bgcolor='#DADADA'>Destination ICAO</td>\n"
            + "	<td style='font-weight: bolder; ' bgcolor='#DADADA'>Airport - Stadt (City)</td>\n"
            + "	<td style='font-weight: bolder; ' bgcolor='#DADADA'>Land (Country)</td>\n"
            + "	<td style='font-weight: bolder; ' bgcolor='#DADADA'>Klasse (Class)</td>\n"
            + "	<td style='font-weight: bolder; ' bgcolor='#DADADA'>Entfernung (Distance)</td>\n"
            + "	<td style='font-weight: bolder; ' bgcolor='#DADADA'>Kurs (Heading)</td>\n"
            + "	<td style='font-weight: bolder; ' bgcolor='#DADADA'>Runway</td>\n"
            + "</tr>\n";

    for (ViewAirportAnflugZiele ap : anflugZiele) {

      tabelle = tabelle + "<tr>\n"
              + "	<td>" + ap.getIcao() + "</td>\n"
              + "	<td>" + ap.getName() + " - " + ap.getStadt() + "</td>\n"
              + "	<td>" + ap.getLand() + "</td>\n"
              + "	<td>" + ap.getKlasse() + "</td>\n"
              + "	<td>" + ap.getEntfernung() + "</td>\n"
              + "	<td>" + ap.getKurs() + "</td>\n"
              + "	<td>" + ap.getLaengsteLandeBahn() + " (m)</td>\n"
              + "</tr>\n";

    }

    tabelle = tabelle + "</tbody></table>\n";

    Airport airport = facadeZiele.readAirportDatenUeberID(id);

    String notem = airport.getNotem() + tabelle;

    airport.setNotem(notem);

    facadeZiele.editAirport(airport);

    isLoaded = false;

  }

  private void DispatchLog(String Icao, int AirportID, String LogBezeichnung, String AenderungsArt) {

    AirportDispatchLog newLog = new AirportDispatchLog();

    newLog.setAirportBezeichnung(LogBezeichnung);
    newLog.setArtDerAenderung(AenderungsArt);
    newLog.setDatum(new Date());
    newLog.setIDAirport(AirportID);
    newLog.setIcao(Icao);
    newLog.setUserID(UserID);
    newLog.setUserName(UserName);

    facadeZiele.createDispatchLog(newLog);

  }

  public void onRowSelect(SelectEvent event) {
  }

  public void onRowUnselect(UnselectEvent event) {
  }

  private void NewMessage(String neueNachricht) {
    FacesMessage msg = new FacesMessage(neueNachricht);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  /*
  Setter and Getter
   */
  public ViewAirportAnflugZiele getSelectedAnflug() {
    return currentZiel;
  }

  public void setSelectedAnflug(ViewAirportAnflugZiele selectedAnflug) {
    this.currentZiel = selectedAnflug;
  }

  public int getZieleAnzahl() {
    return zieleAnzahl;
  }

  public String getFrmICAO() {
    return frmICAO;
  }

  public void setFrmICAO(String frmICAO) {
    this.frmICAO = frmICAO;
  }

  public ViewAirportAnflugZiele getSelectedAirportZiele() {
    return selectedAirportZiele;
  }

  public void setSelectedAirportZiele(ViewAirportAnflugZiele selectedAirportZiele) {
    this.selectedAirportZiele = selectedAirportZiele;
  }

  public List<ViewAirportAnflugZiele> getSelectedAnfluege() {
    return selectedAnfluege;
  }

  public void setSelectedAnfluege(List<ViewAirportAnflugZiele> selectedAnfluege) {
    this.selectedAnfluege = selectedAnfluege;
  }

  public List<Airport> getAirportList() {
    return airportList;
  }

  public void setAirportList(List<Airport> airportList) {
    this.airportList = airportList;
  }

  public Airport getCurrentAirport() {
    return currentAirport;
  }

  public void setCurrentAirport(Airport currentAirport) {
    this.currentAirport = currentAirport;
  }

}
