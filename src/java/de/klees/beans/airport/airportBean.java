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

import de.klees.beans.maps.mapsBean;
import de.klees.beans.system.CONF;
import de.klees.beans.system.loginMB;
import de.klees.beans.user.UserFacade;
import de.klees.data.Airport;
import de.klees.data.AirportDispatchLog;
import de.klees.data.AirportZiele;
import de.klees.data.Airportjobtemplate;
import de.klees.data.Feinabstimmung;
import de.klees.data.Fluggesellschaft;
import de.klees.data.FlughafenKlassen;
import de.klees.data.Benutzer;
import de.klees.data.views.ViewAirportAnflugZiele;
import de.klees.data.views.ViewAirportTransfers;
import de.klees.data.views.ViewFBOUserObjekte;
import de.klees.data.views.ViewFlugzeuglogFlughafen;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author Stefan Klees
 */
public class airportBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private String SuchText;
  private Airport airports;
  private List<Airport> airportsList;
  private List<Airport> airportsUeberSuche;
  private List<AirportDispatchLog> DispatchLog;
  private List<FlughafenKlassen> klassen;
  private Airport currentAirport;
  private Airport newAirport;
  private ViewAirportAnflugZiele selectedAnflug;
  private Benutzer owner;
  private boolean isLoaded;
  private boolean isLoadedSuche;
  private int frmKlasse;
  private String frmLand;
  private String frmBundesLand;
  private double frmLatitude;
  private double frmLongitude;

  private final Calendar c = Calendar.getInstance();
  private final Map<String, String> Landebahn;
  private final Map<String, String> AirportSize;

  private List<String> imageList;
  private String AirportICAO;

  // OpenStreeMap
  private String exLatLong;
  private String exPopupText;
  private String flughafenKoordinaten;

  private double AcarsLatitude;
  private double AcarsLongitute;
  private boolean VisuellerDialogFlughafen;
  private int Zoom;
  boolean tmpFlughaefenGeladen;
  private int UserID;
  private String UserName;

  public airportBean() {

    SuchText = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Assignment_ICAO");

    if (SuchText == null) {
      SuchText = "";
    }

    isLoaded = false;
    isLoadedSuche=false;
    tmpFlughaefenGeladen = false;

    AirportSize = new HashMap<>();
    AirportSize.put(loginMB.onSprache("StartbahnGrassOderErde"), "1");
    AirportSize.put(loginMB.onSprache("StartbahnKleinAsphalt"), "2");
    AirportSize.put(loginMB.onSprache("StartbahnAsphalt"), "3");
    AirportSize.put(loginMB.onSprache("StartbahnZweiAsphalt"), "4");
    AirportSize.put(loginMB.onSprache("StartbahnMehrere"), "5");

    Landebahn = new HashMap<>();
    Landebahn.put("Asphalt", "1");
    Landebahn.put("Concrete", "2");
    Landebahn.put("Coral", "3");
    Landebahn.put("Dirt", "4");
    Landebahn.put("Grass", "5");
    Landebahn.put("Gravel", "6");
    Landebahn.put("Helipad", "7");
    Landebahn.put("Oil Treated", "8");
    Landebahn.put("Snow", "9");
    Landebahn.put("Steel Mats", "10");
    Landebahn.put("Wasser", "11");

    VisuellerDialogFlughafen = false;
    Zoom = 6;
    flughafenKoordinaten = "49, 49";
    exLatLong = flughafenKoordinaten;

    UserID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
    UserName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName");
  }

  @EJB
  AirportFacade airportFacade;
  UserFacade userFacade;

  public List<Airport> getAirports() {

    SuchText = SuchText.toUpperCase();

    try {
      String oldSuchText = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("AirportSuchText");

      if (SuchText != null) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("AirportSuchText", SuchText);

      } else if (SuchText != null && !SuchText.equals(oldSuchText)) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("AirportSuchText", SuchText);

      } else if (SuchText == null && !oldSuchText.equals("")) {
        SuchText = oldSuchText;
      }

    } catch (NullPointerException e) {
    }

    if (!isLoaded) {

      tmpFlughaefenGeladen = false;

      if (SuchText.length() > 2) {
        if (SuchText.substring(0, 2).equals("-C")) {
          SuchText = SuchText.substring(2, SuchText.length());
          isLoaded = true;
          airportsList = airportFacade.findAllbyCountry(SuchText);
        } else if (SuchText.substring(0, 2).equals("-N")) {
          SuchText = SuchText.substring(2, SuchText.length());
          isLoaded = true;
          airportsList = airportFacade.findAllbyAirportName(SuchText);
        } else if (SuchText.substring(0, 2).equals("-S")) {
          SuchText = SuchText.substring(2, SuchText.length());
          isLoaded = true;
          airportsList = airportFacade.findAllbyAirportCity(SuchText);
        } else if (SuchText.substring(0, 2).equals("-I")) {
          SuchText = SuchText.substring(2, SuchText.length());
          isLoaded = true;
          airportsList = airportFacade.findAllbyICAOKuerzell(SuchText);
        } else if (SuchText.substring(0, 2).equals("-K")) {
          SuchText = SuchText.substring(2, SuchText.length());
          isLoaded = true;
          airportsList = airportFacade.findAllbyKlasse(Integer.valueOf(SuchText));
        } else {
          airportsList = airportFacade.findAllbyIcao(SuchText);
          isLoaded = true;
        }

      } else {
        airportsList = airportFacade.findAllbyIcao(SuchText);
        isLoaded = true;
      }

    }

    return airportsList;

  }

  public List<Airport> getAirportsFuerListe() {

    SuchText = SuchText.toUpperCase();

    try {
      String oldSuchText = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("AirportSuchText");

      if (SuchText != null) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("AirportSuchText", SuchText);

      } else if (SuchText != null && !SuchText.equals(oldSuchText)) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("AirportSuchText", SuchText);

      } else if (SuchText == null && !oldSuchText.equals("")) {
        SuchText = oldSuchText;
      }

    } catch (NullPointerException e) {
    }

    if (!isLoadedSuche) {

      tmpFlughaefenGeladen = false;

      if (SuchText.length() > 2) {
        if (SuchText.substring(0, 2).equals("-C")) {
          SuchText = SuchText.substring(2, SuchText.length());
          isLoadedSuche = true;
          airportsUeberSuche = airportFacade.findAllbyCountry(SuchText);
        } else if (SuchText.substring(0, 2).equals("-N")) {
          SuchText = SuchText.substring(2, SuchText.length());
          isLoadedSuche = true;
          airportsUeberSuche = airportFacade.findAllbyAirportName(SuchText);
        } else if (SuchText.substring(0, 2).equals("-S")) {
          SuchText = SuchText.substring(2, SuchText.length());
          isLoadedSuche = true;
          airportsUeberSuche = airportFacade.findAllbyAirportCity(SuchText);
        } else if (SuchText.substring(0, 2).equals("-I")) {
          SuchText = SuchText.substring(2, SuchText.length());
          isLoadedSuche = true;
          airportsUeberSuche = airportFacade.findAllbyICAOKuerzell(SuchText);
        } else if (SuchText.substring(0, 2).equals("-K")) {
          SuchText = SuchText.substring(2, SuchText.length());
          isLoadedSuche = true;
          airportsUeberSuche = airportFacade.findAllbyKlasse(Integer.valueOf(SuchText));
        } else {
          airportsUeberSuche = airportFacade.findAllbyIcao(SuchText);
          isLoadedSuche = true;
        }

      } else {
        airportsUeberSuche = airportFacade.findAllbyIcao(SuchText);
        isLoadedSuche = true;
      }

    }

    return airportsUeberSuche;

  }

  public List<FlughafenKlassen> getAirportKlassen() {
    klassen = airportFacade.findAllKlassen();
    return klassen;
  }

  public List<ViewFBOUserObjekte> getTerminals() {
    return airportFacade.getTerminals(SuchText);
  }

  public List<ViewFBOUserObjekte> getTankstellen() {
    return airportFacade.findTankstellenByICAO(SuchText);
  }

  public List<ViewFBOUserObjekte> getFBOs() {
    return airportFacade.findFBOsByICAO(SuchText);
  }

  public List<Airport> getLaender() {
    return airportFacade.getLaender();
  }

  public List<Airport> getBundesLaender() {
    try {
      if (!frmLand.equals("")) {
        return airportFacade.getBundesLaender(frmLand);
      }

    } catch (NullPointerException e) {
      return null;
    }
    return null;
  }

  public List<Airportjobtemplate> getTemplates() {
    return airportFacade.getTemplates();
  }

  public List<ViewFlugzeuglogFlughafen> getFlughafenLog() {

    if (currentAirport != null) {
      return airportFacade.getFlugzeugLog(currentAirport.getIcao());
    } else {
      return null;
    }

  }

  public List<AirportDispatchLog> getDispatchLog() {
    if (currentAirport != null) {
      DispatchLog = airportFacade.getDispatchLog(currentAirport.getIcao());
      return DispatchLog;
    }
    return null;
  }

  public List<Fluggesellschaft> getFluggesellschaften() {
    List<Fluggesellschaft> fgs = airportFacade.getFluggesellschaften(SuchText);
    return fgs;
  }

  public int getAuslastung() {
    if (currentAirport != null) {
      //currentAirport = airportsList.get(0);
      int maxPax = currentAirport.getMaxpassagiereprotag() + getTransferPax(currentAirport.getIcao());
      int aktMengePax = airportFacade.getMengeRoutenPaxAmFlughafen(currentAirport.getIcao());
      return (int) ((100.0 / (double) maxPax) * (double) aktMengePax);
    }
    return 0;
  }

  public void onRefreshLog() {
    if (currentAirport != null) {
      DispatchLog = airportFacade.getDispatchLog(currentAirport.getIcao());
    }
  }

  public void createAirport() {

    if (airportFacade.ifExistAirport(getAirportICAO())) {
      NewMessage("Airport mit ICAO - Code : " + getAirportICAO() + " existiert bereits!");
      SuchText = getAirportICAO();
      isLoaded = false;
    } else {

      isLoaded = false;

      newAirport = new Airport();

      newAirport.setIsActiv(true);
      newAirport.setZustand(0);
      newAirport.setStadt("");
      newAirport.setBundesland("");
      newAirport.setLand("");
      newAirport.setHoehe(0);
      newAirport.setIcao(getAirportICAO());
      newAirport.setLatitude(frmLatitude);
      newAirport.setLongitude(frmLongitude);
      newAirport.setName("Neuer Airport " + getAirportICAO() + " keine Daten vorhanden");
      newAirport.setKlasse(50);
      newAirport.setLaengsteLandeBahn(1111);
      newAirport.setMaxpassagiereprotag(50);
      newAirport.setPreisklasse(10);
      newAirport.setZustand(1);
      newAirport.setNotem("");
      newAirport.setGepflegt(false);
      newAirport.setJobTemplate("");

      newAirport.setBilderUrls("");
      newAirport.setFreeFSXSceneryUrl("");
      newAirport.setFreeFS9SceneryUrl("");
      newAirport.setFreeP3DSceneryUrl("");
      newAirport.setFreeXPlaneSceneryUrl("");
      newAirport.setPaywareFSXSceneryUrl("");
      newAirport.setPaywareFS9SceneryUrl("");
      newAirport.setPaywareP3DSceneryUrl("");
      newAirport.setPaywareXPlaneSceneryUrl("");
      newAirport.setBgBis(0.0);
      newAirport.setBgVon(0.0);
      newAirport.setLgBis(0.0);
      newAirport.setLgVon(0.0);

      newAirport.setLuftversorgung(false);
      newAirport.setMaxCargo(0);
      newAirport.setTransportierteCargo(0);
      newAirport.setTransportiertePax(0);
      newAirport.setFluege(0);
      newAirport.setFaktor(0.0);
      newAirport.setMaxlandegewicht(0);

      airportFacade.create(newAirport);
      setSelectedAirport(newAirport);
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("AirportID", getSelectedAirport().getIdairport());
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("AirportICAO", getSelectedAirport().getIcao());
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("AirportSuchText", currentAirport.getIcao());
      SuchText = getSelectedAirport().getIcao();

    }
  }

  public void onSuche() {
    isLoaded = false;
    currentAirport = null;
  }

  public void findeFlughafen() {
    currentAirport = airportFacade.readAirportDaten(SuchText);

    if (currentAirport != null) {
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Assignment_ICAO", SuchText);
    }
  }

  public void onSucheDetails() {
    if (!frmLand.equals("")) {
      airportsList = airportFacade.getAiportsDetailsuche(frmLand, frmBundesLand, frmKlasse);
      airportsUeberSuche = airportFacade.getAiportsDetailsuche(frmLand, frmBundesLand, frmKlasse);
    }
  }

  public void onEditAirport() {
    exLatLong = String.valueOf(currentAirport.getLatitude()) + ", " + String.valueOf(currentAirport.getLongitude());
    isLoaded = false;

  }

  public void saveAirport() {

    String tmp = exLatLong.replaceAll("LatLng\\(", "");
    exLatLong = tmp.replaceAll("\\)", "");

    if (!exLatLong.equals("") & exPopupText.equals(currentAirport.getIcao())) {

      String[] koordinaten = exLatLong.split(",");
      currentAirport.setLatitude(Double.valueOf(koordinaten[0]));
      currentAirport.setLongitude(Double.valueOf(koordinaten[1]));
    }

    airportFacade.edit(currentAirport);
    //isLoaded = false;
    NewMessage("Datensatz gespeichert");
  }

  public void onRefresh() {
    isLoaded = false;
  }

  public void deleteAirport() {
    isLoaded = false;
    NewMessage("Datensatz gelöscht");
    airportFacade.remove(currentAirport);
  }

  public void HoleBilder() throws NullPointerException, IndexOutOfBoundsException {
    try {
      if (!currentAirport.getBilderUrls().equals("")) {
        String[] tmpList = currentAirport.getBilderUrls().split(";");
        imageList = new ArrayList<String>();
        for (int i = 0; i < tmpList.length; i++) {
          imageList.add(tmpList[i]);
        }
      }
    } catch (IndexOutOfBoundsException | NullPointerException e) {
    }
  }

  /*
  ************** Visuelle Darstellung Flughafen BEGINN
   */
  @SuppressWarnings("null")
  public void onFlughafenHinzufuegen() {

    String toIcao = exPopupText;

    Airport airport = null;
    int AirportID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("AirportID");
    boolean eintragen = false;

    // ist der Flughafen schon in der Liste
    if (airportFacade.ifExistFlughafenInListe(toIcao, AirportID)) {
      eintragen = false;
      NewMessage("Airport mit ICAO - Code : " + toIcao + " existiert bereits in der Anflugliste!");
      VisuellerDialogFlughafen = false;
    } else {
      airport = airportFacade.readAirportDaten(toIcao);
      eintragen = true;
      flughafenKoordinaten = String.valueOf(airport.getLatitude()) + ", " + String.valueOf(airport.getLongitude());
    }

    if (eintragen) {

      Airport QuellFlughafen = airportFacade.readAirportDatenUeberID(AirportID);

      double Lati = QuellFlughafen.getLatitude();
      double Longi = QuellFlughafen.getLongitude();

      int Ergebnis[] = CONF.DistanzBerechnung(Longi, Lati, airport.getLongitude(), airport.getLatitude());

      AirportZiele newListAirport = new AirportZiele();
      newListAirport.setIdAirport(AirportID);
      newListAirport.setIdZielAirport(airport.getIdairport());
      newListAirport.setICAOZiel(toIcao);
      newListAirport.setKurs(Ergebnis[1]);
      newListAirport.setEntfernung(Ergebnis[0]);

      airportFacade.createAirportZiel(newListAirport);
      VisuellerDialogFlughafen = false;

      setAcarsLatitude(airport.getLatitude());
      setAcarsLongitute(airport.getLongitude());

      //************* Dispatch Log schreiben
      DispatchLog(currentAirport.getIcao(), currentAirport.getIdairport(), airport.getIcao() + " " + airport.getName() + "-" + airport.getLand(), "added");
    }

  }

  public void onflughafenEntfernen() {
    String toIcao = exPopupText;

    int AirportID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("AirportID");

    Airport airport = airportFacade.readAirportDaten(toIcao);

    if (airportFacade.ifExistFlughafenInListe(toIcao, AirportID)) {

      if (airportFacade.LoescheFlughafenAusListe(toIcao, AirportID) > 0) {
        NewMessage("Airport mit ICAO - Code : " + toIcao + " wurde aus der Anflugliste gelöscht!");
      }
    }
    flughafenKoordinaten = String.valueOf(airport.getLatitude()) + ", " + String.valueOf(airport.getLongitude());

    DispatchLog(currentAirport.getIcao(), currentAirport.getIdairport(), airport.getIcao() + " " + airport.getName() + "-" + airport.getLand(), "deleted");

    VisuellerDialogFlughafen = false;
  }

  public void onFlughafenWechseln() {
    SuchText = exPopupText;
    Airport airport = airportFacade.readAirportDaten(SuchText);

    if (airport != null) {
      setSelectedAirport(airport);
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("AirportID", airport.getIdairport());
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("AirportICAO", airport.getIcao());
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("AirportSuchText", airport.getIcao());
      isLoaded = false;
      VisuellerDialogFlughafen = false;

      flughafenKoordinaten = String.valueOf(currentAirport.getLatitude()) + ", " + String.valueOf(currentAirport.getLongitude());

    } else {
      NewMessage("Flughafen nicht gefunden");
    }

  }

  /*
  ************** Visuelle Darstellung Flughafen ENDE
   */
  private void NewMessage(String neueNachricht) {
    FacesMessage msg = new FacesMessage(neueNachricht);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  private List<Airport> getTmpFlughaefen() {

    isLoaded = false;

    double Lati = currentAirport.getLatitude();
    double Longi = currentAirport.getLongitude();

    double bgVon = 0.0;
    double bgBis = 0.0;
    double lgVon = 0.0;
    double lgBis = 0.0;

    int klasse = currentAirport.getKlasse();
    String vonIcao = currentAirport.getIcao();

    Feinabstimmung fa = airportFacade.readConfig();
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

        flughaefen = airportFacade.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 1, 5);
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
        flughaefen = airportFacade.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 1, 5);
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
        flughaefen = airportFacade.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 1, 6);
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
        flughaefen = airportFacade.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 1, 7);
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
        flughaefen = airportFacade.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 1, 8);
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
        flughaefen = airportFacade.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 3, 8);
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
        flughaefen = airportFacade.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 3, 8);
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
        flughaefen = airportFacade.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 4, 9);
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
        flughaefen = airportFacade.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 4, 9);
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
        flughaefen = airportFacade.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 10, 11);
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
        flughaefen = airportFacade.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 10, 11);
        break;
      case 12:
        bg = fa.getUkBGClass12();
        lg = fa.getUkLGClass12();
        if (bgBis + bgVon + lgVon + lgBis == 0.0) {
          bgVon = Lati + bg;
          bgBis = Lati - bg;
          lgVon = Longi + lg;
          lgBis = Longi - lg;
        }
        flughaefen = airportFacade.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 7, 8);
        break;
      default:
        break;
    }

    return flughaefen;
  }

  public void createZielListe() {

    isLoaded = false;

    double Lati = currentAirport.getLatitude();
    double Longi = currentAirport.getLongitude();

    double bgVon = 0.0;
    double bgBis = 0.0;
    double lgVon = 0.0;
    double lgBis = 0.0;

    int klasse = currentAirport.getKlasse();
    String vonIcao = currentAirport.getIcao();

    Feinabstimmung fa = airportFacade.readConfig();
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

        flughaefen = airportFacade.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 1, 5);
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
        flughaefen = airportFacade.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 1, 5);
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
        flughaefen = airportFacade.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 1, 6);
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
        flughaefen = airportFacade.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 1, 7);
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
        flughaefen = airportFacade.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 1, 8);
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
        flughaefen = airportFacade.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 3, 8);
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
        flughaefen = airportFacade.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 3, 8);
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
        flughaefen = airportFacade.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 4, 9);
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
        flughaefen = airportFacade.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 4, 9);
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
        flughaefen = airportFacade.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 10, 11);
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
        flughaefen = airportFacade.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 10, 11);
        break;
      case 12:
        bg = fa.getUkBGClass12();
        lg = fa.getUkLGClass12();
        if (bgBis + bgVon + lgVon + lgBis == 0.0) {
          bgVon = Lati + bg;
          bgBis = Lati - bg;
          lgVon = Longi + lg;
          lgBis = Longi - lg;
        }
        flughaefen = airportFacade.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 7, 8);
        break;
      default:
        break;
    }

    airportFacade.deleteListZielAirports(currentAirport.getIdairport());

    for (Airport next : flughaefen) {
      AirportZiele newListAirport = new AirportZiele();

      int Ergebnis[] = CONF.DistanzBerechnung(Longi, Lati, next.getLongitude(), next.getLatitude());

      newListAirport.setIdAirport(currentAirport.getIdairport());
      newListAirport.setIdZielAirport(next.getIdairport());
      newListAirport.setICAOZiel(next.getIcao());
      newListAirport.setKurs(Ergebnis[1]);
      newListAirport.setEntfernung(Ergebnis[0]);
      airportFacade.createAirportZiel(newListAirport);
    }

  }

  public int getTransferPax(String icao) {
    // Auslesen des Traffics am Flughafen für 10 Tage
    int paxe = 0;
    int trafficTage = 21;
    long start = new Date().getTime() - (trafficTage * 24 * 60 * 60 * 1000);
    List<ViewAirportTransfers> trans = airportFacade.getTransfer(icao, new Date(start), new Date());

    for (ViewAirportTransfers tran : trans) {
      //System.out.println(tran.getPassagiere().intValue());
      paxe = paxe + tran.getPassagiere().intValue();
    }
    //System.out.println(paxe);
    return paxe / trafficTage;
  }

  public double getTransferPaxProzent(String icao) {
    try {
      // Auslesen des Traffics am Flughafen für 21 Tage
      int trafficTage = 21;
      long start = new Date().getTime() - (trafficTage * 24 * 60 * 60 * 1000);

      double maxPax = airportFacade.readAirportDaten(icao).getMaxpassagiereprotag();

      List<ViewAirportTransfers> trans = airportFacade.getTransfer(icao, new Date(start), new Date());
      int paxe = 0;

      for (ViewAirportTransfers tran : trans) {
        paxe += tran.getPassagiere().intValue();
      }

      if (paxe <= 0) {
        return 0;
      }

      double pax = paxe / 10;
      return 100.0 / maxPax * pax;

    } catch (Exception e) {
      return 0.0;
    }

  }

  public double getFBOAufschlag() {
    if (currentAirport != null) {
      //double prozente = CONF.getFlughafenAufschlag(currentAirport.getKlasse());
      double prozente = 10.0;
      long exFbo = airportFacade.wievieleFBOAmFlughafen(currentAirport.getIcao());

      if (exFbo > 0) {
        exFbo = exFbo - 1;
      }

      return prozente * exFbo;
    }
    return 0;
  }

  public double onFBOAufschlag(String icao) {

    if (icao != null) {
      //double prozente = CONF.getFlughafenAufschlag(currentAirport.getKlasse());
      double prozente = 10.0;
      long exFbo = airportFacade.wievieleFBOAmFlughafen(icao);
      if (exFbo > 0) {
        exFbo = exFbo - 1;
      }

      return prozente * exFbo;
    }
    return 0.0;
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

    airportFacade.createDispatchLog(newLog);

  }

  public void onRowSelect(SelectEvent event) {
    if (currentAirport != null) {

      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("AirportID", getSelectedAirport().getIdairport());
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("AirportICAO", getSelectedAirport().getIcao());
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("AirportSuchText", currentAirport.getIcao());
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Assignment_ICAO", currentAirport.getIcao());

      flughafenKoordinaten = String.valueOf(currentAirport.getLatitude()) + ", " + String.valueOf(currentAirport.getLongitude());

      SuchText = currentAirport.getIcao();
      HoleBilder();
    }
  }

  public void onRowUnselect(UnselectEvent event) {
  }

  /*
  Setter and Getter
   */
  public String getSuchText() {
    return SuchText;
  }

  public void setSuchText(String SuchText) {
    this.SuchText = SuchText;
  }

  public Airport getSelectedAirport() {
    return currentAirport;
  }

  public void setSelectedAirport(Airport currentAirport) {
    this.currentAirport = currentAirport;
  }

  public String getTimeZone() {
    return Calendar.getInstance().getTimeZone().getID();
  }

  public Map<String, String> getLandebahn() {
    return Landebahn;
  }

  public Map<String, String> getAirportSize() {
    return AirportSize;
  }

  public List<String> getImageList() {
    return imageList;
  }

  public String getAirportICAO() {
    return AirportICAO;
  }

  public void setAirportICAO(String AirportICAO) {
    this.AirportICAO = AirportICAO;
  }

  public int getFrmKlasse() {
    return frmKlasse;
  }

  public void setFrmKlasse(int frmKlasse) {
    this.frmKlasse = frmKlasse;
  }

  public String getFrmLand() {
    return frmLand;
  }

  public void setFrmLand(String frmLand) {
    this.frmLand = frmLand;
  }

  public String getFrmBundesLand() {
    return frmBundesLand;
  }

  public void setFrmBundesLand(String frmBundesLand) {
    this.frmBundesLand = frmBundesLand;
  }

  public double getAcarsLatitude() {
    return AcarsLatitude;
  }

  public void setAcarsLatitude(double AcarsLatitude) {
    this.AcarsLatitude = AcarsLatitude;
  }

  public double getAcarsLongitute() {
    return AcarsLongitute;
  }

  public void setAcarsLongitute(double AcarsLongitute) {
    this.AcarsLongitute = AcarsLongitute;
  }

  public String KlassenName(int klassenNr) {
    return CONF.KlassenName(klassenNr);
  }

  public boolean isVisuellerDialogFlughafen() {
    return VisuellerDialogFlughafen;
  }

  public void setVisuellerDialogFlughafen(boolean VisuellerDialogFlughafen) {
    this.VisuellerDialogFlughafen = VisuellerDialogFlughafen;
  }

  public int getZoom() {
    return Zoom;
  }

  public void setZoom(int Zoom) {
    this.Zoom = Zoom;
  }

  public String getExLatLong() {
    return exLatLong;
  }

  public void setExLatLong(String exLatLong) {
    this.exLatLong = exLatLong;
  }

  public String getExPopupText() {
    return exPopupText;
  }

  public void setExPopupText(String exPopupText) {
    this.exPopupText = exPopupText;
  }

  public String getFlughafenKoordinaten() {
    return flughafenKoordinaten;
  }

  public void setFlughafenKoordinaten(String flughafenKoordinaten) {
    this.flughafenKoordinaten = flughafenKoordinaten;
  }

  public double getFrmLatitude() {
    return frmLatitude;
  }

  public void setFrmLatitude(double frmLatitude) {
    this.frmLatitude = frmLatitude;
  }

  public double getFrmLongitude() {
    return frmLongitude;
  }

  public void setFrmLongitude(double frmLongitude) {
    this.frmLongitude = frmLongitude;
  }

  public String getBelag(int belag) {
    return CONF.getBelag(belag);
  }

  public int getMinCargo(int Klasse) {
    return CONF.getMinCargo(Klasse);
  }

  public int getMaxCargo(int Klasse) {
    return CONF.getMaxCargo(Klasse);
  }

  public int getMinPax(int Klasse) {
    return CONF.getMinPax(Klasse);
  }

  public int getMaxPax(int Klasse) {
    return CONF.getMaxPax(Klasse);
  }

  public int getMaxLandegewichtAirport(int Klasse) {
    return CONF.getMaxLandegewichtAirport(Klasse);
  }

  public void MTOW() {

    currentAirport.setMaxlandegewicht(getMaxLandegewichtAirport(currentAirport.getKlasse()));
    currentAirport.setMaxpassagiereprotag(CONF.getMaxKapazitaetPaxAirport(currentAirport.getKlasse()));
    currentAirport.setMaxCargo(CONF.getMaxKapazitaetCargoAirport(currentAirport.getKlasse()));
  }

  public boolean isIsLoaded() {
    return isLoaded;
  }

  public void setIsLoaded(boolean isLoaded) {
    this.isLoaded = isLoaded;
  }

}
