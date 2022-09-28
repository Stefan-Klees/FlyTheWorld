
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
import de.klees.data.Bank;
import de.klees.data.Feinabstimmung;
import de.klees.data.Fluglogbuch;
import de.klees.data.Flugzeugemietkauf;
import de.klees.data.Missionen;
import de.klees.data.Missionenwaypoints;
import de.klees.data.Missionsflug;
import de.klees.data.Benutzer;
import de.klees.data.Yaacarskopf;
import de.klees.data.Yaacarspositionen;
import de.klees.data.views.ViewFlugzeugeMietKauf;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author Stefan Klees
 */
public class TerminalMissionenBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<Missionen> listMissionen;
  private Missionen selectedMission;
  private List<Missionenwaypoints> listWayPoints;

  private Missionenwaypoints selectedWayPoint;

  private List<Missionsflug> listMissionFlug;
  private List<Missionsflug> mapWayPoints;
  private Missionsflug selectedFlugWayPoint;
  private Missionsflug nextFlugWayPoint;

  private Benutzer currentUser;
  private ViewFlugzeugeMietKauf missionsFlugzeug;

  //************** YAACARS
  private Yaacarskopf yaacarsFlight;
  private boolean yaacarsFlugInUse;
  private Yaacarspositionen letzteYaacarsPosition;
  double letztePositionLat;
  double letztePositionLong;
  int PaxeOnBoard;
  int CargoOnBoard;

  // Tankstelle
  private Feinabstimmung Config;
  private String TankstellenGrafik;
  private double TankstelleSumme;
  private int TankstelleMengeLbs;
  private int TankMengeKilo;
  private double TankMengeProzent;
  private int TankBisFuellstandKG;
  private int maxTankkapazitaet;
  private int aktuelleTankfuellung;
  private boolean tankstelleGesperrt;

  //*************
  private boolean isLoaded;
  private int UserID;
  private int Zoom;
  private String aktuellerStandort;
  private String IconFuerAircraft;
  private int FlugKurs;
  private String txtKurs;
  private String txtEntfernung;
  private String txtRestzeit;
  private String txtAktuellerKurs;
  private String txtHoehe;
  private String txtHoeheAGL;
  private int Geschwindigkeit;
  private int timerIntervall;

  private boolean AlleWegpunkteErreicht;

  private final DecimalFormat dfkurs = new DecimalFormat("###000");
  private final DecimalFormat dfmiles = new DecimalFormat("#,###0");

  public TerminalMissionenBean() {
    UserID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
    Zoom = 12;
    isLoaded = false;
    yaacarsFlugInUse = false;
    FlugKurs = 0;
    PaxeOnBoard = 0;
    CargoOnBoard = 0;
    timerIntervall = 10;
    IconFuerAircraft = CONF.getDomainURL() + "/images/FTW/helis/plane_" + FlugKurs + ".png";
  }

  @EJB
  MissionenFacade terminalFacade;

  public void refresh() {

  }

  public List<Missionen> getMissionen() {
    if (!isLoaded) {
      listMissionen = terminalFacade.getMissionen();
    }
    return listMissionen;
  }

  /*
  **************************** Mission starten/ende BEGINN
   */
  public void onMissionStarten() {

    if (selectedMission != null) {
      currentUser = terminalFacade.readUser(UserID);

      // Initiale Tankfuellung bei Flugzeug speichern
      Flugzeugemietkauf fg = terminalFacade.readFlugzeugMietKauf(selectedMission.getIdflugzeug());
      fg.setAktuelleTankfuellung(selectedMission.getInitialetankfuellung());
      terminalFacade.editMeinFlugzeug(fg);

      MissionsFlugzeug(selectedMission.getIdflugzeug());

      Missionsflug mission = new Missionsflug();

      int fgnr;
      int fgmax = 9999;

      Random FlightNumber = new Random();
      DecimalFormat flight = new DecimalFormat("####0000");

      Yaacarskopf acarsFlight = new Yaacarskopf();

      acarsFlight.setAlternateicao("MISS");
      acarsFlight.setAlternatelatitude(0.0);
      acarsFlight.setAlternatelongitude(0.0);

      acarsFlight.setArrivalicao("MISS");
      acarsFlight.setArrivallatitude(0.0);
      acarsFlight.setArrivallongitude(0.0);

      acarsFlight.setDepartureicao("MISS");
      acarsFlight.setDeparturelatitude(0.0);
      acarsFlight.setDeparturelongitude(0.0);

      acarsFlight.setBlockzeit(0);
      acarsFlight.setCargogewicht(0);
      acarsFlight.setFlugerstelltam(new Date());

      do {
        fgnr = FlightNumber.nextInt(fgmax);
      } while (terminalFacade.isFlightNummerExist("MIS" + flight.format(fgnr)));

      acarsFlight.setFlugnummer("MIS" + flight.format(fgnr));
      acarsFlight.setFlugroute("");
      //"flugstatus"
      //0 = angelegt
      //1 = gestartet
      //2 = beendet
      //
      //"missionsart"
      //1 = Normaler FTW-Flug
      //2 = Charter
      //3 = Rettung
      //4 = Missionen
      acarsFlight.setFlugstatus(0);
      acarsFlight.setMissionsart(4);
      acarsFlight.setMissionsid(selectedMission.getIdmissionen());

      acarsFlight.setFlugzeugid(missionsFlugzeug.getIdMietKauf());
      acarsFlight.setFlugzeugkennung(missionsFlugzeug.getRegistrierung());
      acarsFlight.setFlugzeugtype(missionsFlugzeug.getType());
      acarsFlight.setGeflogenemeilen(0);
      acarsFlight.setGeflogenezeit(0);
      acarsFlight.setLetztepositionlatitude(0.0);
      acarsFlight.setLetztepositionlongitude(0.0);

      acarsFlight.setPaxanzahl(0);
      acarsFlight.setPaxgewicht(0);

      acarsFlight.setProtokollversion("");
      acarsFlight.setResttankmengekg(missionsFlugzeug.getAktuelleTankfuellung());
      acarsFlight.setTankmengekg(missionsFlugzeug.getAktuelleTankfuellung());
      acarsFlight.setUserhash(currentUser.getPasswort());
      acarsFlight.setUserid(currentUser.getIdUser());
      acarsFlight.setUsermessage("Missionsflug");
      acarsFlight.setUsername(currentUser.getName1());
      acarsFlight.setVerbrauchtetankmengekg(0);
      acarsFlight.setFreitext("");

      terminalFacade.createYAACARSFlight(acarsFlight);

      yaacarsFlight = acarsFlight;

      missionWegpunkteErstellen(yaacarsFlight.getIdyaacarskopf());

      yaacarsFlugInUse = true;

      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Flight-ON", "true");
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FlightLogo", getFlugLogo());
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FlugzeugType", "Mission Flight \n" + missionsFlugzeug.getType());

    }

  }

  public String getFlugLogo() {

    if (missionsFlugzeug.getFlugzeugArt().equals("Hubschrauber")) {
      return " " + CONF.getDomainURL() + "/images/FTW/helis/plane_" + 1 + ".png";
    }

    if (missionsFlugzeug.getFlugzeugArt().equals("Geschäftsflugzeug")) {
      return " " + CONF.getDomainURL() + "/images/FTW/bc/plane_" + 1 + ".png";
    }

    if (missionsFlugzeug.getLizenz().equals("MPL")) {
      return " " + CONF.getDomainURL() + "/images/FTW/jets/plane_" + 1 + ".png";
    }

    if (missionsFlugzeug.getLizenz().equals("ATPL")) {
      return " " + CONF.getDomainURL() + "/images/FTW/heavy/plane_" + 1 + ".png";
    }

    if (missionsFlugzeug.getLizenz().equals("CPL") || missionsFlugzeug.getLizenz().equals("PPL-A")) {
      return " " + CONF.getDomainURL() + "/images/FTW/kleinflugzeug/plane_" + 1 + ".png";
    }

    return "";
  }

  private void missionWegpunkteErstellen(int yaacarsID) {

    List<Missionenwaypoints> wayPoints = terminalFacade.getWayPoints(selectedMission.getIdmissionen());
    Missionsflug letzterWegPunkt = null;
    for (Missionenwaypoints wp : wayPoints) {

      Missionsflug mf = new Missionsflug();

      mf.setAltitude(wp.getAltitude());
      mf.setAusladen(wp.getAusladen());
      mf.setEinladen(wp.getEinladen());
      mf.setIdflugzeug(missionsFlugzeug.getIdMietKauf());
      mf.setIdmission(selectedMission.getIdmissionen());
      mf.setIdmissionenwaypoints(wp.getIdmissionenwaypoints());
      mf.setIduser(UserID);
      mf.setIdyaacarskopf(yaacarsID);
      mf.setLanden(wp.getLanden());
      mf.setLatitude(wp.getLatitude());
      mf.setLongitude(wp.getLongitude());
      mf.setMengecargo(wp.getMengecargo());
      mf.setMengepax(wp.getMengepax());
      mf.setMengecargoausladen(wp.getMengecargoausladen());
      mf.setMengepaxausladen(wp.getMengepaxausladen());
      mf.setReihenfolge(wp.getReihenfolge());
      mf.setUmkreisingrad(wp.getUmkreisingrad());
      mf.setVerguetung(wp.getVerguetung());
      mf.setWaypointtext(wp.getWaypointtext());
      mf.setWegpunkterreicht(false);
      mf.setTankenmoeglich(wp.getTankenmoeglich());

      terminalFacade.createMissionsflug(mf);
      letzterWegPunkt = mf;

    }

    setSelectedWayPoint(wayPoints.get(0));

    aktuellerStandort = selectedWayPoint.getLatitude() + ", " + selectedWayPoint.getLongitude();

    /*
    YAACARSFlug anpassen, Positionen eintragen, Pax und Cargo evtl. laden.
     */
    Yaacarskopf acarsFlight = terminalFacade.readYAACARSFlight(UserID);

    acarsFlight.setAlternateicao("MISS");
    acarsFlight.setAlternatelatitude(letzterWegPunkt.getLatitude());
    acarsFlight.setAlternatelongitude(letzterWegPunkt.getLongitude());

    acarsFlight.setArrivalicao("MISS");
    acarsFlight.setArrivallatitude(letzterWegPunkt.getLatitude());
    acarsFlight.setArrivallongitude(letzterWegPunkt.getLongitude());

    acarsFlight.setDepartureicao("MISS");
    acarsFlight.setDeparturelatitude(selectedWayPoint.getLatitude());
    acarsFlight.setDeparturelongitude(selectedWayPoint.getLongitude());

    acarsFlight.setLetztepositionlatitude(selectedWayPoint.getLatitude());
    acarsFlight.setLetztepositionlongitude(selectedWayPoint.getLongitude());

    terminalFacade.editYAACARSFlight(acarsFlight);

  }

  public void onMissionAbbrechen() {

    terminalFacade.removeYAACARSFlight(yaacarsFlight.getIdyaacarskopf());
    terminalFacade.removeYAACARSPositionen(yaacarsFlight.getIdyaacarskopf());

    terminalFacade.deleteMisionsflug(yaacarsFlight.getIdyaacarskopf());

    listMissionFlug = null;
    selectedMission = null;
    selectedFlugWayPoint = null;
    yaacarsFlugInUse = false;

    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Flight-ON", "false");

    NewMessage("Mission abgebrochen");
  }

  public void onMissionWiederAufnehmen() {

    yaacarsFlight = terminalFacade.readYAACARSFlight(UserID);

    if (yaacarsFlight != null) {
      listMissionFlug = terminalFacade.getMissionsFlug(yaacarsFlight.getIdyaacarskopf());
      mapWayPoints = terminalFacade.getAllWaypointMissionsFlug(yaacarsFlight.getIdyaacarskopf());

      PaxeOnBoard = yaacarsFlight.getPaxanzahl();
      CargoOnBoard = yaacarsFlight.getCargogewicht();

      try {
        selectedMission = terminalFacade.getMission(listMissionFlug.get(0).getIdmission());
        selectedFlugWayPoint = listMissionFlug.get(0);
      } catch (IndexOutOfBoundsException e) {
        selectedMission = terminalFacade.getMission(mapWayPoints.get(0).getIdmission());
        selectedFlugWayPoint = mapWayPoints.get(0);
      }

      MissionsFlugzeug(selectedMission.getIdflugzeug());
      yaacarsFlugInUse = true;
      aktuellerStandort = yaacarsFlight.getLetztepositionlatitude() + ", " + yaacarsFlight.getLetztepositionlongitude();
      onAktuellePositionErmitteln();

      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Flight-ON", "true");
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FlightLogo", getFlugLogo());
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FlugzeugType", "Mission Flight \n" + missionsFlugzeug.getType());

    }
  }

  public boolean isLaufendeMission() {
    if (terminalFacade.isLaufendeMission(UserID)) {
      onMissionWiederAufnehmen();
      return true;
    }
    return false;
  }

  public void onMissionAbrechnen() {

    onMissionBeenden();

  }

  public void onMissionBeenden() {

    if (isAlleWegpunkteErreicht()) {

      //************* Fluglog schreiben
      createLogbuchEintrag(getMissionsBetrag());

      //************* Flugzeuglog schreiben
      aircraftUpdate();

      //************* Userlog schreiben
      userUpdate();

      //*********************** Bankbuchung
      String UserName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName");
      String UserKonto = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("BankKonto");

      String VerwendungsZweck = "Bezahlung für Mission: " + selectedMission.getKurzbezeichnung();

      SaveBankbuchung(UserKonto, UserName, "300-1000001", "*** FTW Buchungssystem ***",
              new Date(), getMissionsBetrag(), UserKonto, UserName,
              new Date(), VerwendungsZweck, UserID, -1, -1, -1, -1, -1, -1, "", -1, missionsFlugzeug.getIdMietKauf(), 0, UserID);

      onMissionAbbrechen();
    } else {
      NewMessage("Es wurden nicht alle Wegpunkte erreicht, Mission kann nicht abgerechnet werden.");
    }
  }

  public double getMissionsBetrag() {
    Double Betrag = 0.0;

    if (mapWayPoints != null) {
      for (Missionsflug mf : mapWayPoints) {
        Betrag = Betrag + mf.getVerguetung();
      }
    }

    return Betrag;
  }

  public int getMissionsPaxe() {
    int Menge = 0;
    if (mapWayPoints != null) {
      for (Missionsflug mf : mapWayPoints) {
        if (mf.getEinladen()) {
          Menge = Menge + mf.getMengepax();
        }
      }
    } else {
      Menge = 0;
    }
    return Menge;
  }

  public int getMissionsCargo() {
    int Menge = 0;
    if (mapWayPoints != null) {
      for (Missionsflug mf : mapWayPoints) {
        if (mf.getEinladen()) {
          Menge = Menge + mf.getMengecargo();
        }
      }
    } else {
      Menge = 0;
    }
    return Menge;
  }

  public boolean isAlleWegpunkteErreicht() {
    try {
      listMissionFlug = terminalFacade.getMissionsFlug(yaacarsFlight.getIdyaacarskopf());
      for (Missionsflug ms : listMissionFlug) {
        if (!ms.getWegpunkterreicht()) {
          return false;
        }
      }
      return true;
    } catch (NullPointerException e) {
      return false;
    }

  }

  /*
  **************************** Mission starten/ende ENDE
   */

 /*
  **************************** Statistik BEGINN
   */
  private void userUpdate() {

    Benutzer currentUser = terminalFacade.readUser(UserID);

    int FlightMiles = currentUser.getFlightmiles();
    int Flights = currentUser.getFlights();
    int FlightTimeCur = currentUser.getFlighttime();
    int FlightPaxes = currentUser.getFlightpaxes();
    int FlightCargo = currentUser.getFlightcargo();
    int FlightRettung = currentUser.getFlightrettung();

    FlightMiles = FlightMiles + yaacarsFlight.getGeflogenemeilen();
    Flights = Flights + 1;
    FlightTimeCur = FlightTimeCur + yaacarsFlight.getGeflogenezeit();
    FlightPaxes = FlightPaxes + getMissionsPaxe();
    FlightCargo = FlightCargo + getMissionsCargo();

    currentUser.setFlightmiles(FlightMiles);
    currentUser.setFlights(Flights);
    currentUser.setFlighttime(FlightTimeCur);
    currentUser.setFlightpaxes(FlightPaxes);
    currentUser.setFlightcargo(FlightCargo);

    terminalFacade.editUser(currentUser);
  }

  private void aircraftUpdate() {

    Flugzeugemietkauf MeinFlugzeug = terminalFacade.readFlugzeugMietKauf(missionsFlugzeug.getIdMietKauf());

    int FlightMiles = missionsFlugzeug.getGesamtEntfernung();
    int Flights = missionsFlugzeug.getGesamtFluege();
    int FlightTimeCur = missionsFlugzeug.getGesamtFlugzeit();
    int FlightPaxes = missionsFlugzeug.getGesamtTransportiertePassagiere();
    int FlightCargo = missionsFlugzeug.getGesamtTransportiertesCargo();
    int TotalEngineTime = missionsFlugzeug.getMaschinenLaufzeitMinuten();
    int LastCheck = missionsFlugzeug.getLetzterCheckMinuten();
    int AirFrame = missionsFlugzeug.getGesamtMaschinenLaufzeitMinuten();
    int GesamtAlter = missionsFlugzeug.getGesamtAlterDesFlugzeugsMinuten();
    int Tankinhalt = missionsFlugzeug.getAktuelleTankfuellung() - Math.round(yaacarsFlight.getVerbrauchtetankmengekg());

    FlightMiles = FlightMiles + yaacarsFlight.getGeflogenemeilen();
    Flights = Flights + 1;
    FlightTimeCur = FlightTimeCur + yaacarsFlight.getGeflogenezeit();
    FlightPaxes = FlightPaxes + 3;
    FlightCargo = FlightCargo + 0;
    TotalEngineTime = TotalEngineTime + yaacarsFlight.getGeflogenezeit();
    AirFrame = AirFrame + yaacarsFlight.getGeflogenezeit();
    LastCheck = LastCheck + yaacarsFlight.getGeflogenezeit();
    GesamtAlter = GesamtAlter + yaacarsFlight.getGeflogenezeit();

    MeinFlugzeug.setGesamtEntfernung(FlightMiles);
    MeinFlugzeug.setGesamtFluege(Flights);
    MeinFlugzeug.setGesamtFlugzeit(FlightTimeCur);
    MeinFlugzeug.setGesamtTransportiertePassagiere(FlightPaxes);
    MeinFlugzeug.setGesamtTransportiertesCargo(FlightCargo);
    MeinFlugzeug.setMaschinenLaufzeitMinuten(TotalEngineTime);
    MeinFlugzeug.setGesamtMaschinenLaufzeitMinuten(AirFrame);
    MeinFlugzeug.setGesamtAlterDesFlugzeugsMinuten(GesamtAlter);
    MeinFlugzeug.setLetzterCheckMinuten(LastCheck);
    MeinFlugzeug.setAktuelleTankfuellung(Tankinhalt);
    MeinFlugzeug.setIstInDerLuft(false);

    terminalFacade.editMeinFlugzeug(MeinFlugzeug);

  }

  private void createLogbuchEintrag(double Betrag) {

    Fluglogbuch newLog = new Fluglogbuch();

    //"missionsart"
    //1 = Normaler FTW-Flug
    //2 = Charter
    //3 = Rettung
    //4 = Missionen
    newLog.setAcarsFlugNummer(yaacarsFlight.getFlugnummer());
    newLog.setMissionsart(3);
    newLog.setBodenpersonalgebuehr(0.0);
    newLog.setBonus1(0.0);
    newLog.setBonus2(0.0);
    newLog.setBuchungsgebuehr(Betrag);
    newLog.setCargo(getMissionsCargo());
    newLog.setCrewgebuehren(0.0);
    newLog.setFlugDatum(new Date());
    newLog.setFromicao(yaacarsFlight.getDepartureicao().toUpperCase());
    newLog.setToicao(yaacarsFlight.getArrivalicao().toUpperCase());
    newLog.setIdAirline(-1);
    newLog.setIdaircraft(missionsFlugzeug.getIdMietKauf());
    newLog.setIduser(UserID);
    newLog.setLandegebuehr(0.0);
    newLog.setMietgebuehr(0.0);
    newLog.setMiles(yaacarsFlight.getGeflogenemeilen());
    newLog.setFlugzeitMinuten(yaacarsFlight.getGeflogenezeit());
    newLog.setPax(getMissionsPaxe());
    newLog.setSicherheitsgebuehr(0.0);
    newLog.setSummeabrechnung(Betrag);
    newLog.setSummekosten(0.0);
    newLog.setTreibstoffkosten(0.0);
    newLog.setTreibstoffverbrauchkg(Math.round(yaacarsFlight.getVerbrauchtetankmengekg()));
    newLog.setIdArrivalTerminal(-1);
    newLog.setBetragAriivalTerminal(0.0);
    newLog.setIdDepartureTerminal(-1);
    newLog.setBetragDepartureTerminal(0.0);
    newLog.setTocaoFlughafenName("Missionziel");
    newLog.setFromIcaoFlughafenName("Missionsabflug");
    newLog.setProvision(0.0);
    newLog.setFixkosten(0.0);

    terminalFacade.createLogbuchEintrag(newLog);
  }

  /*
  **************************** Statistik ENDE
   */
 /*
  *************************** YAACARS BEGINN
   */
  public boolean isOtherYaacarsFlightInUse() {

    //Prüfen ob es eine andere Art von Flug gibt
    Yaacarskopf yaacarskopf = terminalFacade.readYAACARSFlight(UserID);

    if (yaacarskopf != null) {
      // Laufenden YAACARS-Flug ermitteln
      //"missionsart"
      //1 = Normaler FTW-Flug
      //2 = Charter
      //3 = Rettung
      //4 = Missionen
      if (yaacarskopf.getMissionsart() == 4) {
        return false;
      } else {
        return true;
      }
    } else {
      return false;
    }
  }

  public boolean isYaacarsFlugBeendet() {
    Yaacarskopf yaacarskopf = terminalFacade.readYAACARSFlight(UserID);

    if (yaacarskopf != null) {
      //"flugstatus"
      //0 = angelegt
      //1 = gestartet
      //2 = beendet
      if (yaacarskopf.getFlugstatus() == 2) {
        if (isAlleWegpunkteErreicht()) {
          return true;
        }
      }
    }

    return false;
  }

  public void onAktuellePositionErmitteln() {
    letzteYaacarsPosition = terminalFacade.readLetzteYAACARSPosition(yaacarsFlight.getIdyaacarskopf());

    if (nextFlugWayPoint == null) {
      naechstenWegpunktErmitteln();
    }

    if (letzteYaacarsPosition != null) {
      FlugKurs = letzteYaacarsPosition.getHeading();
      aktuellerStandort = letzteYaacarsPosition.getLatitude() + ", " + letzteYaacarsPosition.getLongitude();

      if (missionsFlugzeug.getFlugzeugArt().equals("Hubschrauber")) {
        IconFuerAircraft = CONF.getDomainURL() + "/images/FTW/helis/plane_" + FlugKurs + ".png";
      } else {
        IconFuerAircraft = CONF.getDomainURL() + "/images/FTW/kleinflugzeug/plane_" + FlugKurs + ".png";
      }

      Geschwindigkeit = letzteYaacarsPosition.getTas();
      txtHoehe = dfmiles.format(letzteYaacarsPosition.getAsl());
      txtHoeheAGL = dfmiles.format(letzteYaacarsPosition.getAgl());
      txtAktuellerKurs = dfkurs.format(FlugKurs);

      if (nextFlugWayPoint != null) {
        int ergebnis[] = CONF.DistanzBerechnung(letzteYaacarsPosition.getLongitude(), letzteYaacarsPosition.getLatitude(), selectedFlugWayPoint.getLongitude(), selectedFlugWayPoint.getLatitude());
        txtEntfernung = dfmiles.format(ergebnis[0]) + " Mile(s)";
        txtKurs = dfkurs.format(ergebnis[1]);
        txtRestzeit = RestZeit(ergebnis[0]);
      }

      if (OrtOK(letzteYaacarsPosition.getLongitude(), letzteYaacarsPosition.getLatitude(), selectedFlugWayPoint.getLongitude(), selectedFlugWayPoint.getLatitude(), selectedFlugWayPoint.getUmkreisingrad())) {

        /*
        ******************* Wegpunkt erreicht, Wegpunkt Abarbeiten
         */
        if (!selectedFlugWayPoint.getLanden()) {
          if (selectedFlugWayPoint.getAltitude() <= 0) {

            if (!selectedFlugWayPoint.getWegpunkterreicht()) {
              selectedFlugWayPoint.setWegpunkterreicht(true);
              terminalFacade.saveMissionsflug(selectedFlugWayPoint);
              naechstenWegpunktErmitteln();
            }
          } else {
            /*
            ***************** Angegeben Höhe auswerten
             */
            if (selectedFlugWayPoint.getAltitude() == letzteYaacarsPosition.getAsl()) {
              selectedFlugWayPoint.setWegpunkterreicht(true);
              terminalFacade.saveMissionsflug(selectedFlugWayPoint);
              naechstenWegpunktErmitteln();
            } else if (selectedFlugWayPoint.getAltitude() == letzteYaacarsPosition.getAgl()) {
              selectedFlugWayPoint.setWegpunkterreicht(true);
              terminalFacade.saveMissionsflug(selectedFlugWayPoint);
              naechstenWegpunktErmitteln();
            }
          }
        }

        /*
        Wegpunktbedingungen abarbeiten BEGINN
         */
        if (selectedFlugWayPoint.getLanden()) {
          YAACARSKopfNeuEinlesen();
          if (letzteYaacarsPosition.getAgl() <= 10) {
            if (selectedFlugWayPoint.getAusladen()) {
              PaxeOnBoard = 0;
              CargoOnBoard = 0;
              yaacarsFlight.setCargogewicht(CargoOnBoard);
              yaacarsFlight.setPaxanzahl(PaxeOnBoard);
              int paxgewicht = 0;
              yaacarsFlight.setPaxgewicht(paxgewicht);
              terminalFacade.editYAACARSFlight(yaacarsFlight);
            }
            if (selectedFlugWayPoint.getEinladen()) {
              PaxeOnBoard = PaxeOnBoard + selectedFlugWayPoint.getMengepax();
              CargoOnBoard = CargoOnBoard + selectedFlugWayPoint.getMengecargo();
              yaacarsFlight.setCargogewicht(selectedFlugWayPoint.getMengecargo());
              yaacarsFlight.setPaxanzahl(selectedFlugWayPoint.getMengepax());
              int paxgewicht = CONF.zufallszahl(65, 80) * selectedFlugWayPoint.getMengepax();
              yaacarsFlight.setPaxgewicht(paxgewicht);

              terminalFacade.editYAACARSFlight(yaacarsFlight);

            }
            selectedFlugWayPoint.setWegpunkterreicht(true);
            terminalFacade.saveMissionsflug(selectedFlugWayPoint);
            naechstenWegpunktErmitteln();

          }

        }
        /*
        Wegpunktbedingungen abarbeiten ENDE
         */
      }

    } else {

    }

  }

  public void naechstenWegpunktErmitteln() {
    boolean gefunden = false;
    boolean gefundenNext = false;

    listMissionFlug = terminalFacade.getMissionsFlug(yaacarsFlight.getIdyaacarskopf());

    for (Iterator<Missionsflug> ms = listMissionFlug.iterator(); ms.hasNext();) {

      Missionsflug next = ms.next();

      if (!next.getWegpunkterreicht()) {
        selectedFlugWayPoint = next;

        if (ms.hasNext()) {
          next = ms.next();
          nextFlugWayPoint = next;
        }

        break;
      }

    }

  }

  private boolean OrtOK(double laengAktuell, double breitAktuell, double wayPointLaeng, double wayPointBreit, double radius) {

    double pruefWertPositiv = radius;
    double pruefWertNegativ = radius - (radius * 2);

    boolean BreitenGradOk = false;
    boolean LaengenGradOk = false;

    double difLaengengrad = wayPointLaeng - laengAktuell;
    double difBreitengrad = wayPointBreit - breitAktuell;

//    System.out.println("de.klees.beans.missionen.TerminalMissionenBean.OrtOK() selectedWay " + selectedFlugWayPoint.getWaypointtext());
//    System.out.println("de.klees.beans.missionen.TerminalMissionenBean.OrtOK() Akt. Laenge " + laengAktuell);
//    System.out.println("de.klees.beans.missionen.TerminalMissionenBean.OrtOK() Akt. Breite " + breitAktuell);
//
//    System.out.println("de.klees.beans.missionen.TerminalMissionenBean.OrtOK() Waypoint. Laenge " + wayPointLaeng);
//    System.out.println("de.klees.beans.missionen.TerminalMissionenBean.OrtOK() Waypoint Breite " + wayPointBreit);
//
//    System.out.println("de.klees.beans.missionen.TerminalMissionenBean.OrtOK() difLaenge " + difLaengengrad);
//    System.out.println("de.klees.beans.missionen.TerminalMissionenBean.OrtOK() difBreite " + difBreitengrad);
//    System.out.println("de.klees.beans.missionen.TerminalMissionenBean.OrtOK() Radius " + radius);
    if (difLaengengrad == 0.0 && difBreitengrad == 0.0) {
      return true;
    }

    if (difLaengengrad > 0) {
      if (difLaengengrad < pruefWertPositiv) {
        LaengenGradOk = true;
      }
    }

    if (difBreitengrad > 0) {
      if (difBreitengrad < pruefWertPositiv) {
        BreitenGradOk = true;
      }
    }

    if (difLaengengrad < 0) {
      if (difLaengengrad > pruefWertNegativ) {
        LaengenGradOk = true;
      }
    }

    if (difBreitengrad < 0) {
      if (difBreitengrad > pruefWertNegativ) {
        BreitenGradOk = true;
      }
    }

    if (BreitenGradOk && LaengenGradOk) {
      return true;
    }

    return false;

  }

  private void YAACARSKopfNeuEinlesen() {
    yaacarsFlight = terminalFacade.readYAACARSFlight(UserID);
  }

  /*
  *************************** YAACARS ENDE
   */

 /*
  *************************** Flugzeug BEGINN
   */
  private void MissionsFlugzeug(int id) {
    if (id > 0) {
      missionsFlugzeug = terminalFacade.readFlugzeug(id);

      try {
        aktuelleTankfuellung = missionsFlugzeug.getAktuelleTankfuellung();
        maxTankkapazitaet = missionsFlugzeug.getTreibstoffkapazitaet();
      } catch (NullPointerException e) {

      }

      if (missionsFlugzeug.getFlugzeugArt().equals("Hubschrauber")) {
        IconFuerAircraft = CONF.getDomainURL() + "/images/FTW/helis/plane_" + FlugKurs + ".png";
      } else {
        IconFuerAircraft = CONF.getDomainURL() + "/images/FTW/kleinflugzeug/plane_" + FlugKurs + ".png";
      }

    } else {
      missionsFlugzeug = null;
    }
  }

  /*
  *************************** Flugzeug ENDE
   */
 /*
   *************************** Map BEGINN
   */
  public String getWayPointMap() {

    int radius = 0;
    String ergebnis = "";
    String multicords = "";
    String retString = "";
    String von = "";
    String bis = "";
    String Icon = "";

    if (yaacarsFlight != null) {

      if (selectedMission != null) {
        mapWayPoints = terminalFacade.getAllWaypointMissionsFlug(yaacarsFlight.getIdyaacarskopf());
      }

      if (mapWayPoints != null) {

        if (mapWayPoints.size() > 1) {
          for (Missionsflug way : mapWayPoints) {

            Icon = "FahneIcon";

            //radius = (int) (110000 * way.getUmkreisingrad());
            if (way.getWegpunkterreicht()) {
              Icon = "grueneFahne";
              ergebnis = ergebnis + "L.marker([" + String.valueOf(way.getLatitude()) + ", " + String.valueOf(way.getLongitude()) + "], {icon: " + Icon + "}).addTo(MapWayPoint).bindPopup(' Lat. " + String.valueOf(way.getLatitude()) + ", Long. " + String.valueOf(way.getLongitude()) + "   ');\n";
            } else {
              ergebnis = ergebnis + "L.marker([" + String.valueOf(way.getLatitude()) + ", " + String.valueOf(way.getLongitude()) + "], {icon: " + Icon + "}).addTo(MapWayPoint).bindPopup(' Lat. " + String.valueOf(way.getLatitude()) + ", Long. " + String.valueOf(way.getLongitude()) + "   ');\n";
            }

            if (letzteYaacarsPosition != null) {
              ergebnis = ergebnis + "L.marker([" + String.valueOf(letzteYaacarsPosition.getLatitude()) + ", " + String.valueOf(letzteYaacarsPosition.getLongitude()) + "],{icon: heli}).addTo(MapWayPoint).bindPopup('" + String.valueOf(letzteYaacarsPosition.getLatitude()) + ", " + String.valueOf(letzteYaacarsPosition.getLongitude()) + "');";
            } else {
              ergebnis = ergebnis + "L.marker([" + String.valueOf(yaacarsFlight.getLetztepositionlatitude()) + ", " + String.valueOf(yaacarsFlight.getLetztepositionlongitude()) + "],{icon: heli}).addTo(MapWayPoint).bindPopup('" + String.valueOf(yaacarsFlight.getLetztepositionlatitude()) + ", " + String.valueOf(yaacarsFlight.getLetztepositionlongitude()) + "');";
            }

//          ergebnis = ergebnis + "L.circle([" + String.valueOf(way.getLatitude()) + ", " + String.valueOf(way.getLongitude()) + "], {radius: " + radius + "}).addTo(MapWayPoint);\n";
          }

          int Zaehler = 0;
          for (Iterator<Missionsflug> way = mapWayPoints.iterator(); way.hasNext();) {
            Missionsflug waypnt = way.next();
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
    }
    return retString;
  }

  /*
  *************************** Map ENDE
   */
 /*
  ************************ Tankstelle BEGINN
   */
  private void YAACARSFlugAnpassen(int Tankfuellung) {
    // YAACARS Flug Anpassen, wenn gestartet
    // Nach dem Tanken muss auch die Tankmenge im YAACARSKopf angepasst werden.
    if (yaacarsFlight != null) {
      YAACARSKopfNeuEinlesen();
      yaacarsFlight.setTankmengekg(Tankfuellung);
      yaacarsFlight.setResttankmengekg(Tankfuellung);
      terminalFacade.editYAACARSFlight(yaacarsFlight);
    }
  }

  public void onResetTankstelle() {
    TankMengeProzent = 0.0;
    TankMengeKilo = 0;
    TankBisFuellstandKG = 0;
    aktuelleTankfuellung = missionsFlugzeug.getAktuelleTankfuellung();
    maxTankkapazitaet = missionsFlugzeug.getTreibstoffkapazitaet();
    TankstellenGrafik = CONF.getDomainURL() + "/images/FTW/images/tankstelle.png";
    berechneTankfuellung();

  }

  public void berechneTankfuellung() {
    Config = terminalFacade.getConfig();
    double PreisAVGAS = Config.getPreisAVGASkg();
    double PreisJETA = Config.getPreisJETAkg();

    // FTW - Tankstelle
    if (missionsFlugzeug.getTreibstoffArt() == 1) {
      setTankstelleSumme((double) TankMengeKilo * PreisAVGAS);
      setTankstelleMengeLbs((int) getKG2Libs(TankMengeKilo));
    } else {
      setTankstelleSumme((double) TankMengeKilo * PreisJETA);
      setTankstelleMengeLbs((int) getKG2Libs(TankMengeKilo));
    }

  }

  public void ontankenStart() {
    onResetTankstelle();
  }

  public void onTankenBerechneProzentTankfüllung() {

    TankBisFuellstandKG = (int) (maxTankkapazitaet / 100.0 * TankMengeProzent);

    TankMengeKilo = TankBisFuellstandKG - aktuelleTankfuellung;

    onTankenBerechneKilo();
  }

  public void onTankenBerechneKilo() {

    int inhalt = aktuelleTankfuellung + TankMengeKilo;

    if (inhalt > maxTankkapazitaet) {
      inhalt = maxTankkapazitaet;
    }

    if (inhalt < 0) {
      inhalt = 0;
    }

    TankBisFuellstandKG = inhalt;

    onTankenBerechneBisFüllstandKg();
  }

  public void onTankenBerechneBisFüllstandKg() {

    boolean tanken = true;

    if (TankBisFuellstandKG < 0) {
      tanken = false;
      NewMessage("Füllstand muss größer 0 sein");
      onResetTankstelle();
    }

    if (tanken) {
      if (TankBisFuellstandKG >= aktuelleTankfuellung) {
        TankMengeKilo = TankBisFuellstandKG - aktuelleTankfuellung;
      } else {
        TankMengeKilo = aktuelleTankfuellung - TankBisFuellstandKG;
        TankMengeKilo = TankMengeKilo - (TankMengeKilo * 2);

        if (TankMengeKilo + aktuelleTankfuellung < 0) {
          TankMengeKilo = TankMengeKilo - (aktuelleTankfuellung * 2);
        }
      }

      if (aktuelleTankfuellung + TankMengeKilo > maxTankkapazitaet) {
        TankMengeKilo = maxTankkapazitaet - aktuelleTankfuellung;
        TankBisFuellstandKG = maxTankkapazitaet;
      }

      TankMengeProzent = 100.0 / maxTankkapazitaet * TankBisFuellstandKG;

      //******************************* Summenbildung
      berechneTankfuellung();
    }
  }

  public void onTankenBezahlen() {
    onTankenBerechneKilo();
    Flugzeugemietkauf fg = terminalFacade.readFlugzeugMietKauf(missionsFlugzeug.getIdMietKauf());
    fg.setAktuelleTankfuellung(TankMengeKilo + aktuelleTankfuellung);
    terminalFacade.editMeinFlugzeug(fg);
    YAACARSFlugAnpassen(fg.getAktuelleTankfuellung());
    MissionsFlugzeug(fg.getIdMietKauf());
    onResetTankstelle();
  }

  public boolean isTankstelleGesperrt() {

    tankstelleGesperrt = false;

    if (selectedFlugWayPoint != null) {
      if (selectedFlugWayPoint.getTankenmoeglich()) {
        tankstelleGesperrt = false;
      } else {
        tankstelleGesperrt = true;
      }
    }
    return tankstelleGesperrt;
  }

  /*
  ************************ Tankstelle ENDE
   */
  public void onRowSelect(SelectEvent event) {
  }

  public void onRowUnselect(UnselectEvent event) {

  }

  private void NewMessage(String neueNachricht) {
    FacesMessage msg = new FacesMessage(neueNachricht);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  /*
   ********************************** Setter and Getter
   */
  public Missionen getSelectedMission() {
    return selectedMission;
  }

  public void setSelectedMission(Missionen selectedMission) {
    this.selectedMission = selectedMission;
  }

  public Missionenwaypoints getSelectedWayPoint() {
    return selectedWayPoint;
  }

  public void setSelectedWayPoint(Missionenwaypoints selectedWayPoint) {
    this.selectedWayPoint = selectedWayPoint;
  }

  public boolean isYaacarsFlugInUse() {
    return yaacarsFlugInUse;
  }

  public void setYaacarsFlugInUse(boolean yaacarsFlugInUse) {
    this.yaacarsFlugInUse = yaacarsFlugInUse;
  }

  public Yaacarskopf getYaacarsFlight() {
    return yaacarsFlight;
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

  public Missionsflug getSelectedFlugWayPoint() {
    return selectedFlugWayPoint;
  }

  public void setSelectedFlugWayPoint(Missionsflug selectedFlugWayPoint) {
    this.selectedFlugWayPoint = selectedFlugWayPoint;
  }

  public List<Missionsflug> getListMissionFlug() {
    return listMissionFlug;
  }

  public List<Missionsflug> getMapWayPoints() {
    return mapWayPoints;
  }

  public String getIconFuerAircraft() {
    return IconFuerAircraft;
  }

  public void setIconFuerAircraft(String IconFuerAircraft) {
    this.IconFuerAircraft = IconFuerAircraft;
  }

  public int getFlugKurs() {
    return FlugKurs;
  }

  public void setFlugKurs(int FlugKurs) {
    this.FlugKurs = FlugKurs;
  }

  public String getTxtKurs() {
    return txtKurs;
  }

  public void setTxtKurs(String txtKurs) {
    this.txtKurs = txtKurs;
  }

  public String getTxtEntfernung() {
    return txtEntfernung;
  }

  public void setTxtEntfernung(String txtEntfernung) {
    this.txtEntfernung = txtEntfernung;
  }

  public String getTxtRestzeit() {
    return txtRestzeit;
  }

  public void setTxtRestzeit(String txtRestzeit) {
    this.txtRestzeit = txtRestzeit;
  }

  public String getTxtAktuellerKurs() {
    return txtAktuellerKurs;
  }

  public void setTxtAktuellerKurs(String txtAktuellerKurs) {
    this.txtAktuellerKurs = txtAktuellerKurs;
  }

  public String getTxtHoehe() {
    return txtHoehe;
  }

  public void setTxtHoehe(String txtHoehe) {
    this.txtHoehe = txtHoehe;
  }

  public String getTxtHoeheAGL() {
    return txtHoeheAGL;
  }

  public void setTxtHoeheAGL(String txtHoeheAGL) {
    this.txtHoeheAGL = txtHoeheAGL;
  }

  public int getGeschwindigkeit() {
    return Geschwindigkeit;
  }

  public void setGeschwindigkeit(int Geschwindigkeit) {
    this.Geschwindigkeit = Geschwindigkeit;
  }

  public Missionsflug getNextFlugWayPoint() {
    return nextFlugWayPoint;
  }

  public void setNextFlugWayPoint(Missionsflug nextFlugWayPoint) {
    this.nextFlugWayPoint = nextFlugWayPoint;
  }

  private String RestZeit(int Entfernung) {
    try {
      return dfmiles.format(Entfernung / (Geschwindigkeit / 60)) + " minutes";
    } catch (Exception ex) {
      return "---";
    }
  }

  /**
   *
   * @param Bankonto
   * @param Kontoname
   * @param AbsenderKontoNr
   * @param AbsenderKontoName
   * @param AusfuehrungsDatum
   * @param Betrag
   * @param EmpfaengerKontoNr
   * @param EmpfaengerKontoName
   * @param Ueberweisungsdatum
   * @param VerwendungsZweck
   * @param UserID
   * @param AirportID
   * @param FluggesellschaftID
   * @param FlugzeugBesitzerID
   * @param IndustrieID
   * @param LeasinggesellschaftID
   * @param TransportID
   * @param icao
   * @param objektID
   * @param FlugzeugID
   * @param kostenstelle
   * @param pilotID
   */
  private void SaveBankbuchung(String Bankkonto, String KontoName, String AbsenderKontoNr, String AbsenderKontoName, Date AusfuehrungsDatum, double Betrag, String EmpfaengerKontoNr,
          String EmpfaengerKontoName, Date UeberweisungsDatum, String VerwendungsZweck,
          int userid, int AirportID, int FluggesellschaftID, int FlugzeugBesitzerID, int IndustrieID, int LeasinggesellschaftID, int TransportID,
          String icao, int objektID, int FlugzeugID, int Kostenstelle, int pilotID) {

    Bank newBuchung = new Bank();

    newBuchung.setBankKonto(Bankkonto);
    newBuchung.setKontoName(KontoName);
    newBuchung.setAbsenderKonto(AbsenderKontoNr);
    newBuchung.setAbsenderName(AbsenderKontoName);
    newBuchung.setEmpfaengerKonto(EmpfaengerKontoNr);
    newBuchung.setEmpfaengerName(EmpfaengerKontoName);
    newBuchung.setAusfuehrungsDatum(AusfuehrungsDatum);
    newBuchung.setUeberweisungsDatum(UeberweisungsDatum);
    newBuchung.setVerwendungsZweck(VerwendungsZweck);
    newBuchung.setBetrag(Betrag);

    newBuchung.setAirportID(AirportID);
    newBuchung.setFluggesellschaftID(FluggesellschaftID);
    newBuchung.setFlugzeugBesitzerID(FlugzeugBesitzerID);
    newBuchung.setIndustrieID(IndustrieID);
    newBuchung.setLeasinggesellschaftID(LeasinggesellschaftID);
    newBuchung.setTransportID(TransportID);
    newBuchung.setUserID(userid);
    newBuchung.setIcao(icao);
    newBuchung.setObjektID(objektID);
    newBuchung.setFlugzeugID(FlugzeugID);
    newBuchung.setKostenstelle(Kostenstelle);
    newBuchung.setPilotID(pilotID);

    terminalFacade.createBankbuchung(newBuchung);

  }

  public int getPaxeOnBoard() {
    return PaxeOnBoard;
  }

  public void setPaxeOnBoard(int PaxeOnBoard) {
    this.PaxeOnBoard = PaxeOnBoard;
  }

  public int getCargoOnBoard() {
    return CargoOnBoard;
  }

  public void setCargoOnBoard(int CargoOnBoard) {
    this.CargoOnBoard = CargoOnBoard;
  }

  public int getTimerIntervall() {
    return timerIntervall;
  }

  public void setTimerIntervall(int timerIntervall) {
    this.timerIntervall = timerIntervall;
  }

  /*
  Setter and Getter Tankstelle
   */
  public double getKG2Libs(int Gewicht) {
    return (double) Gewicht * 2.204622915;
  }

  public double getLibs2KG(int Gewicht) {
    return (double) Gewicht * 0.453592309685;
  }

  public String getTankstellenGrafik() {
    if (missionsFlugzeug != null) {
      return missionsFlugzeug.getSymbolUrl();
    } else {
      return "";
    }

  }

  public void setTankstellenGrafik(String TankstellenGrafik) {
    this.TankstellenGrafik = TankstellenGrafik;
  }

  public double getTankstelleSumme() {
    return TankstelleSumme;
  }

  public void setTankstelleSumme(double TankstelleSumme) {
    this.TankstelleSumme = TankstelleSumme;
  }

  public int getTankstelleMengeLbs() {
    return TankstelleMengeLbs;
  }

  public void setTankstelleMengeLbs(int TankstelleMengeLbs) {
    this.TankstelleMengeLbs = TankstelleMengeLbs;
  }

  public int getTankMengeKilo() {
    return TankMengeKilo;
  }

  public void setTankMengeKilo(int TankMengeKilo) {
    this.TankMengeKilo = TankMengeKilo;
  }

  public double getTankMengeProzent() {
    return TankMengeProzent;
  }

  public void setTankMengeProzent(double TankMengeProzent) {
    this.TankMengeProzent = TankMengeProzent;
  }

  public int getTankBisFuellstandKG() {
    return TankBisFuellstandKG;
  }

  public void setTankBisFuellstandKG(int TankBisFuellstandKG) {
    this.TankBisFuellstandKG = TankBisFuellstandKG;
  }

  public int getMaxTankkapazitaet() {
    return maxTankkapazitaet;
  }

  public void setMaxTankkapazitaet(int maxTankkapazitaet) {
    this.maxTankkapazitaet = maxTankkapazitaet;
  }

  public int getAktuelleTankfuellung() {
    return aktuelleTankfuellung;
  }

  public void setAktuelleTankfuellung(int aktuelleTankfuellung) {
    this.aktuelleTankfuellung = aktuelleTankfuellung;
  }

}
