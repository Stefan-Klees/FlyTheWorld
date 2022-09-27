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

package de.klees.beans.luftrettung;

import de.klees.data.RettungsZiele;
import de.klees.data.Rettungseinsaetze;
import de.klees.data.Rettungsstationen;
import java.io.Serializable;
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
public class LuftrettungBean implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Creates a new instance of LuftrettungBean
   */
  private List<Rettungsstationen> stationenList;
  private Rettungsstationen selectedStation;

  private List<Rettungseinsaetze> einsatzList;
  private Rettungseinsaetze selectedEinsatz;

  private RettungsZiele selectedFavorit;

  private String frmStationsName;
  private double frmLaengenGradStation;
  private double frmBreitenGradStation;

  private double frmLaengenGradUnfall;
  private double frmBreitenGradUnfall;

  private double frmLaengenGradZiel;
  private double frmBreitenGradZiel;

  private String frmBezeichnungZiel;

  private int StationsID;

  //Maps Variablen
  private String extPosdaten;
  private String extPosdatenUnfall;
  private String extPosdatenZiel;

  private boolean isLoadedStationen;
  private boolean isLoadedEinsatz;

  @EJB
  RettungsstationenFacade rettungFacade;

  public LuftrettungBean() {
    isLoadedStationen = false;
    isLoadedEinsatz = false;
    frmStationsName = "";
    extPosdaten = "";
    extPosdatenUnfall = "";
    extPosdatenZiel = "";
  }

  public List<Rettungsstationen> getStationenVerwaltung() {
    if (!isLoadedStationen) {
      stationenList = rettungFacade.getAlleStationVerwaltung();
      isLoadedStationen = true;
    }

    return stationenList;
  }

  public List<Rettungsstationen> getStationen() {
    if (!isLoadedStationen) {
      stationenList = rettungFacade.getAlleStationen();
      isLoadedStationen = true;
    }

    return stationenList;
  }

  public List<Rettungseinsaetze> getEinsaetze() {
    if (StationsID > 0 && !isLoadedEinsatz) {
      einsatzList = rettungFacade.getAlleEinSaetze(StationsID);
      isLoadedEinsatz = true;
    }
    return einsatzList;
  }

  public List<RettungsZiele> getFavoriten() {
    return rettungFacade.getFavoriten();
  }

  public void onSpeichernFavorit() {
    if (!rettungFacade.ifExistFavorit(frmBezeichnungZiel)) {
      RettungsZiele newFav = new RettungsZiele();
      newFav.setBreitengrad(frmBreitenGradZiel);
      newFav.setLaengengrad(frmLaengenGradZiel);
      newFav.setZielbezeichnung(frmBezeichnungZiel);

      rettungFacade.createFavorit(newFav);

      NewMessage("Zielfavorit gespeichert");
    } else {
      NewMessage("Zielfavorit existiert bereits");
    }
  }

  public void onChangeFavorit() {
    RettungsZiele fav = rettungFacade.getFavorit(frmBezeichnungZiel);
    if (fav != null) {
      if (selectedEinsatz != null) {
        selectedEinsatz.setBreitengradZiel(fav.getBreitengrad());;
        selectedEinsatz.setLaengengradZiel(fav.getLaengengrad());
        selectedEinsatz.setBezeichnungziel(fav.getZielbezeichnung());

        onPositionFestlegenEinsatzZiel();
      }
    }
  }

  public void onDeleteFavorit() {
    rettungFacade.removeFavorit(selectedFavorit);
    NewMessage("Favorit gelöscht");
  }

  public void onNeueRettungsstation() {
    Rettungsstationen neueStation = new Rettungsstationen();
    neueStation.setBundesland("");
    neueStation.setKuerzel("");
    neueStation.setBreitengrad(0.0);
    neueStation.setLaengengrad(0.0);
    neueStation.setStadt("");
    neueStation.setLand("");
    neueStation.setNameRettungsstatione("Neue Station bitte bearbeiten");
    neueStation.setMaxAnzahlSpieler(0);
    neueStation.setSprache("");
    neueStation.setIdFlugzeugMietKauf(-1);
    neueStation.setSceneryURL("");

    rettungFacade.create(neueStation);
    setSelectedStation(neueStation);

    frmBreitenGradStation = selectedStation.getBreitengrad();
    frmLaengenGradStation = selectedStation.getLaengengrad();

    isLoadedStationen = false;

    NewMessage("Rettungsstation angelegt");

  }

  public void onRettungsstationBearbeiten() {
    if (getSelectedStation() != null) {
      StationsID = selectedStation.getIdRettungsstationen();
      frmLaengenGradStation = selectedStation.getLaengengrad();
      frmBreitenGradStation = selectedStation.getBreitengrad();
    }

  }

  public void onSpeichernRettungsstation() {
    if (!extPosdaten.equals("")) {
      selectedStation.setBreitengrad(Double.valueOf(getMoveMarkerStation(extPosdaten)[0]));
      selectedStation.setLaengengrad(Double.valueOf(getMoveMarkerStation(extPosdaten)[1]));
    }
    rettungFacade.edit(selectedStation);
    isLoadedStationen = false;
    NewMessage("Rettungsstation gespeichert");
  }

  public void onSpeichernKoordinatenRettungsstation() {
    if (selectedStation != null) {
      selectedStation.setBreitengrad(frmBreitenGradStation);
      selectedStation.setLaengengrad(frmLaengenGradStation);
      rettungFacade.edit(selectedStation);
      NewMessage("Koordinaten gespeichert");
    }
  }

  public void onLoescheRettungsstation() {
    rettungFacade.remove(selectedStation);
    isLoadedStationen = false;
    setSelectedStation(null);
    NewMessage("Rettungsstation gelöscht");
  }

  public void onKopieEinsatz() {

    if (selectedEinsatz != null) {
      Rettungseinsaetze newEinsatz = new Rettungseinsaetze();
      newEinsatz = selectedEinsatz;
      newEinsatz.setKurzbezeichnung("Kopie, bitte ausfüllen");
      newEinsatz.setEinsatzmeldung("");
      rettungFacade.createEinsatz(newEinsatz);
      NewMessage("Einsatz kopiert");
    }
  }

  public void onNeuerEinsatz() {
    Rettungseinsaetze neuerEinsazt = new Rettungseinsaetze();
    neuerEinsazt.setBasisEinsatzPreis(0.0);
    neuerEinsazt.setPreisfaktor(1.0);
    neuerEinsazt.setEinsatzmeldung("");
    neuerEinsazt.setIdRettungsstation(StationsID);
    neuerEinsazt.setKurzbezeichnung("Neuer Einsatz bitte bearbeiten");
    neuerEinsazt.setBreitengradUnfall(frmBreitenGradStation + 0.05);
    neuerEinsazt.setLaengengradUnfall(frmLaengenGradStation + 0.05);
    neuerEinsazt.setBreitengradZiel(frmBreitenGradStation + 0.08);
    neuerEinsazt.setLaengengradZiel(frmLaengenGradStation + 0.08);
    neuerEinsazt.setBezeichnungziel("");
    neuerEinsazt.setBezeichnungunfall("");

    rettungFacade.createEinsatz(neuerEinsazt);
    setSelectedEinsatz(neuerEinsazt);

    isLoadedEinsatz = false;

    NewMessage("Neuer Einsatz angelegt");
  }

  public void onSpeichernEinsatz() {

    if (selectedEinsatz != null) {
      if (!extPosdatenUnfall.equals("")) {
        selectedEinsatz.setBreitengradUnfall(Double.valueOf(getMoveMarkerStation(extPosdatenUnfall)[0]));
        selectedEinsatz.setLaengengradUnfall(Double.valueOf(getMoveMarkerStation(extPosdatenUnfall)[1]));
      }

      if (!extPosdatenZiel.equals("")) {
        selectedEinsatz.setBreitengradZiel(Double.valueOf(getMoveMarkerStation(extPosdatenZiel)[0]));
        selectedEinsatz.setLaengengradZiel(Double.valueOf(getMoveMarkerStation(extPosdatenZiel)[1]));
      }

      selectedEinsatz.setBezeichnungziel(frmBezeichnungZiel);

      rettungFacade.editEinsatz(selectedEinsatz);

      if (selectedEinsatz != null) {
        frmBreitenGradUnfall = selectedEinsatz.getBreitengradUnfall();
        frmLaengenGradUnfall = selectedEinsatz.getLaengengradUnfall();
        frmBreitenGradZiel = selectedEinsatz.getBreitengradZiel();
        frmLaengenGradZiel = selectedEinsatz.getLaengengradZiel();
      }

      isLoadedEinsatz = false;
      NewMessage("Einsatz gespeichert");
    } else {
      NewMessage("Fehlerhafter Datensatz, speichern nicht möglich");
    }

  }

  public void onLoescheEinsatz() {
    rettungFacade.removeEinsatz(selectedEinsatz);
    isLoadedEinsatz = false;
    onRefreshEinsatz();
    NewMessage("Einsatz wurde gelöscht");
  }

  public void onRefreh() {
    isLoadedStationen = false;
    setSelectedStation(null);

  }

  public void onRefreshEinsatz() {
    isLoadedEinsatz = false;
    setSelectedEinsatz(null);
  }

  /*
  **************** Map-Darstellung BEGINN
   */
  /**
   * Liefert den Breitengrad und Laengengrad zurueck
   *
   * @param LeaPosDaten
   * @return index 0 = Breitengrad, Index 1 = Laengengrad
   */
  public String[] getMoveMarkerStation(String LeaPosDaten) {

    String tmp = String.valueOf(LeaPosDaten).replaceAll("LatLng\\(", "");
    String exLatLong = tmp.replaceAll("\\)", "");
    String[] koordinaten = exLatLong.split(",");
    return koordinaten;
  }

  /*
  **************** Map-Darstellung ENDE
   */
  public void onPositionenFestlegenEinsatzUnfall() {
    frmBreitenGradUnfall = selectedEinsatz.getBreitengradUnfall();
    frmLaengenGradUnfall = selectedEinsatz.getLaengengradUnfall();

  }

  public void onPositionFestlegenEinsatzZiel() {
    frmBreitenGradZiel = selectedEinsatz.getBreitengradZiel();
    frmLaengenGradZiel = selectedEinsatz.getLaengengradZiel();

  }

  public void onRowSelect(SelectEvent event) {
    if (getSelectedStation() != null) {
      StationsID = selectedStation.getIdRettungsstationen();
      frmStationsName = selectedStation.getNameRettungsstatione();
      frmBreitenGradStation = selectedStation.getBreitengrad();
      frmLaengenGradStation = selectedStation.getLaengengrad();
      isLoadedEinsatz = false;
    }
  }

  public void onRowUnselect(UnselectEvent event) {

  }

  public void onRowSelectEinsatz(SelectEvent event) {
    if (selectedEinsatz != null) {
      frmBreitenGradUnfall = selectedEinsatz.getBreitengradUnfall();
      frmLaengenGradUnfall = selectedEinsatz.getLaengengradUnfall();
      frmBreitenGradZiel = selectedEinsatz.getBreitengradZiel();
      frmLaengenGradZiel = selectedEinsatz.getLaengengradZiel();
      frmBezeichnungZiel = selectedEinsatz.getBezeichnungziel();
    }
  }

  public void onRowUnselectEinsatz(UnselectEvent event) {

  }

  private void NewMessage(String neueNachricht) {
    FacesMessage msg = new FacesMessage(neueNachricht);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  /*
  ************** Setter and Getter
   */
  public double getFrmLaengenGradStation() {
    return frmLaengenGradStation;
  }

  public void setFrmLaengenGradStation(double frmLaengenGradStation) {
    this.frmLaengenGradStation = frmLaengenGradStation;
  }

  public double getFrmBreitenGradStation() {
    return frmBreitenGradStation;
  }

  public void setFrmBreitenGradStation(double frmBreitenGradStation) {
    this.frmBreitenGradStation = frmBreitenGradStation;
  }

  public Rettungsstationen getSelectedStation() {
    return selectedStation;
  }

  public void setSelectedStation(Rettungsstationen selectedStation) {
    this.selectedStation = selectedStation;
  }

  public Rettungseinsaetze getSelectedEinsatz() {
    return selectedEinsatz;
  }

  public void setSelectedEinsatz(Rettungseinsaetze selectedEinsatz) {
    this.selectedEinsatz = selectedEinsatz;
  }

  public String getFrmStationsName() {
    return frmStationsName;
  }

  public void setFrmStationsName(String frmStationsName) {
    this.frmStationsName = frmStationsName;
  }

  public double getFrmLaengenGradUnfall() {
    return frmLaengenGradUnfall;
  }

  public void setFrmLaengenGradUnfall(double frmLaengenGradUnfall) {
    this.frmLaengenGradUnfall = frmLaengenGradUnfall;
  }

  public double getFrmBreitenGradUnfall() {
    return frmBreitenGradUnfall;
  }

  public void setFrmBreitenGradUnfall(double frmBreitenGradUnfall) {
    this.frmBreitenGradUnfall = frmBreitenGradUnfall;
  }

  public double getFrmLaengenGradZiel() {
    return frmLaengenGradZiel;
  }

  public void setFrmLaengenGradZiel(double frmLaengenGradZiel) {
    this.frmLaengenGradZiel = frmLaengenGradZiel;
  }

  public double getFrmBreitenGradZiel() {
    return frmBreitenGradZiel;
  }

  public void setFrmBreitenGradZiel(double frmBreitenGradZiel) {
    this.frmBreitenGradZiel = frmBreitenGradZiel;
  }

  public String getExtPosdaten() {
    return extPosdaten;
  }

  public void setExtPosdaten(String extPosdaten) {
    this.extPosdaten = extPosdaten;
  }

  public String getExtPosdatenUnfall() {
    return extPosdatenUnfall;
  }

  public void setExtPosdatenUnfall(String extPosdatenUnfall) {
    this.extPosdatenUnfall = extPosdatenUnfall;
  }

  public String getExtPosdatenZiel() {
    return extPosdatenZiel;
  }

  public void setExtPosdatenZiel(String extPosdatenZiel) {
    this.extPosdatenZiel = extPosdatenZiel;
  }

  public String getFrmBezeichnungZiel() {
    return frmBezeichnungZiel;
  }

  public void setFrmBezeichnungZiel(String frmBezeichnungZiel) {
    this.frmBezeichnungZiel = frmBezeichnungZiel;
  }

  public RettungsZiele getSelectedFavorit() {
    return selectedFavorit;
  }

  public void setSelectedFavorit(RettungsZiele selectedFavorit) {
    this.selectedFavorit = selectedFavorit;
  }

}
