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


package de.klees.beans.takeoff;

import de.klees.beans.system.CONF;
import de.klees.beans.system.ReportList;
import de.klees.beans.system.loginMB;
import de.klees.data.Airport;
import de.klees.data.AirportTransfers;
import de.klees.data.ArcasFlights;
import de.klees.data.Benutzer;
import de.klees.data.Assignement;
import de.klees.data.Bank;
import de.klees.data.FboObjekte;
import de.klees.data.FboUserObjekte;
import de.klees.data.Feinabstimmung;
import de.klees.data.FlugesellschaftPiloten;
import de.klees.data.Fluggesellschaft;
import de.klees.data.FluggesellschaftBonusSystem;
import de.klees.data.Fluggesellschaftmanager;
import de.klees.data.Fluglogbuch;
import de.klees.data.Flugrouten;
import de.klees.data.Flugzeuge;
import de.klees.data.Flugzeugemietkauf;
import de.klees.data.Mail;
import de.klees.data.Sitzkonfiguration;
import de.klees.data.UmbauSitzplatz;
import de.klees.data.UserTyperatings;
import de.klees.data.Yaacarskopf;
import de.klees.data.views.ViewAbrechnungZieleSumme;
import de.klees.data.views.ViewAssiErweiterteLizenzPruefung;
import de.klees.data.views.ViewFBOUserObjekte;
import de.klees.data.views.ViewFlugProvisionen;
import de.klees.data.views.ViewFlugzeugeMietKauf;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
public class newTakeoffBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<ViewFlugzeugeMietKauf> aircraftsAvailable;
  private ViewFlugzeugeMietKauf rentedAircraft;
  private ViewFlugzeugeMietKauf currentAircraft;
  private Flugzeugemietkauf MeinFlugzeug;

  private Feinabstimmung config;

  private int UserID;

  private List<Assignement> assignmentList;
  private Airport airportData;
  private Fluggesellschaft airline;
  private FlugesellschaftPiloten airlinePilot;

  //****************** Acars Daten *************************************************************
  Random FlightNumber = new Random();
  Random GewichtGepaeck = new Random();
  private boolean PirepOK;
  private boolean isAcasFlightExist;

  private ArcasFlights acarsflightinfo;
  private String PirepFlugNummer;
  private String PirepFromICAO;
  private String PirepToICAO;
  private int PirepMiles;
  private String ZielFlughafenICAO;
  private String DestinationICAO;
  private double dgeschw;
  private boolean acarsFlugInUse;
  //********************************************************************************************
  private String Abflugflughafen;

  private List<Benutzer> userList;
  private Benutzer currentUser;

  private boolean showAircraftTable;
  private final Calendar c = Calendar.getInstance();

  private double SummeTakeOff;
  private int SummePassengersTakeOff;

  private String AirportName;
  private String DepartedFrom;
  private int currentFlightMiles;
  private int FlightTime;

  private int TransportPaxe;
  private int AnzahlPax;
  private int AirlineLicense;

  private boolean isLoaded;

  private boolean FlugGestartet;
  private Date FlugzeugRueckgabe;

  private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private final DecimalFormat flight = new DecimalFormat("####0000");
  private final DecimalFormat dfWaehrung = new DecimalFormat("#,##0.00");

  /*
  Abrechnung
   */
  private double sicherheitsgebuehr;
  private double landegebuehr;
  private double startgebuehr;
  private double buchungsgebuehr;
  private double treibstoffkosten;
  private double mietgebuehrflugzeug;
  private double crewgebuehren;
  private double bodenpersonalgebuehren;
  private double summekosten;
  private double summeabrechnung;
  private double summetakeoff_display;
  private double bonusProzent;
  private double bonus;
  private double WertStandardJob;
  private double WertSonstigerJob;
  private double WertProvision;
  private double GewinnPilot;
  private double GewinnAirline;
  private boolean AbrechnungsFehler;
  private String AbrechnungsFehlerText;
  private int AirlineID;
  private double BoniBonusSystem;
  private String BoniBezeichnung;
  private double BoniBonusSystemWert;
  private double FlugzeugFixkosten;

  private int ErwarteteMeilen;
  private int ErwarteteFlugzeit;

  private int SummeCargo;
  private int SummePassengers;
  private int SummeGewicht;

  private int SummeBuisnessPassengers;

  private boolean AbrechnungLaeuft;

  private double SitzKonfigFaktor;

  //*************************************************** Flugzeug
  private int IDFlugzeugbesitzer;
  private int IDFlughafenbetreiber;
  private int IDBank;
  private int IDFluggesellschaft;
  private String NameFluggesellschaft;
  private Sitzkonfiguration sitzKonfiguration;
  private String sitzKonfigBezeichnung;
  private int SitzeBusiness;
  private int SitzeEconomy;
  private int Leergewicht;
  private int SitzeCrew;
  private int SitzeFlugbegleiter;
  private int CargoVerfuegbar;
  private int maxPayload;
  private int DryOperatingWeight;
  private int ZeroFullWeight;
  private int Payload;
  private int TakeOffWeight;
  private int TreibstoffVerbleibendFuerTransportdaten;
  private String zustandText;
  private int ZustandProzent;
  private int maxTankkapazitaet;
  private int maxBelegbareSitze;

// *************************************************** Weitere Kosten 
  private double Personalschluessel;
  private int ArrivalTerminalAbrechnung;
  private int DepartureTerminalAbrechnung;
  private double ArriavalGebuehren;
  private int ArrivalKapazitaet;
  private double DepartureGebuehren;
  private int DepartureKapazitaet;
  private boolean TerminalFreigeben;

  // ************** Tankstelle
  private List<ViewFBOUserObjekte> Tankstellen;
  private String TankstellenName;
  private String TankstellenGrafik;
  private int TankstelleTreibstoffArt;
  private int TankstelleSpritMenge;
  private int TankstelleMaxSpritMenge;
  private double TankstelleSumme;
  private int TankstelleMengeLbs;
  private boolean TankenBezahlt;
  private boolean TankenFreigeben;

  private double TankMengeProzent;
  private int TankMengeKilo;
  private int TankBisFuellstandKG;

  private int TankRestMengeSprit;
  private int Spritverbrauch;
  private double SpritPreis;
  private boolean istPrivateTankstelleVorhanden;
  private int TreibstoffArt;

  private boolean EnttankenLaeuft;
  private boolean TankstelleOffen;

  private int objektID;

  private boolean isLoadedAircrafts;

  private int TimeAdd;

  private boolean RandomJob;
  private boolean AirlineJob;

  private boolean istEinFlugzeugGemietet;
  private boolean istEinAuftragVorhanden;
  private String StartICAO;
  private String FlughafenName;
  private int GewichtDerCrew;
  private String Notam;
  private boolean pinGesendet;
  private boolean pinSpritGesendet;
  private boolean flugFortsetzung;
  private boolean wirdUmgebaut;
  private Date umbauZeit;

  private String simbriefDaten;

  private boolean AbbruchWegenDoppel;
  private boolean Langstrecke;
  private boolean LangstreckenFlugzeug;
  private boolean StartButtonDisabled;

  private Yaacarskopf SupportFreigabeFlug;

  ArrayList<ReportList> report = new ArrayList<>();

  public newTakeoffBean() {
    isLoaded = false;
    showAircraftTable = true;
    IDBank = -200;
    IDFluggesellschaft = -200;
    IDFlughafenbetreiber = -200;
    ArrivalTerminalAbrechnung = -300;
    DepartureTerminalAbrechnung = -300;
    AirlineLicense = 1;
    isLoadedAircrafts = false;
    Abflugflughafen = "";
    TimeAdd = 0;
    RandomJob = false;
    PirepOK = false;
    flugFortsetzung = false;
    AbbruchWegenDoppel = false;
    EnttankenLaeuft = false;
    GewinnAirline = 0.0;
    /*
    Bonussystem
     */
    UserID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
    ReportSchreiben("User id = " + UserID);
  }

  @EJB
  TakeoffFacade facadeTakeOff;

  public void onRefreshSite() {
    // Konfiguration einlesen  
    config = facadeTakeOff.readConfig();
    objektID = 0;
    acarsFlugInUse = false;
    TankstelleOffen = true;

    FlughafenName = "";
    istEinAuftragVorhanden = false;
    istEinFlugzeugGemietet = false;
    StartICAO = "";

    // ist ein Flugzeug gemietet
    try {
      rentedAircraft = getMeinGemietetesFlugzeug();

      if (rentedAircraft != null) {
        StartICAO = rentedAircraft.getAktuellePositionICAO().trim();
        FlugzeugRueckgabe = rentedAircraft.getIstGesperrtBis();
      }
    } catch (Exception e) {
      ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.onRefreshSite() Kein Flugzeug gemietet.....");
      System.out.println("de.klees.beans.takeoff.newTakeoffBean.onRefreshSite() Kein Flugzeug gemietet.....");
    }

    isLoaded = false;
    isLoadedAircrafts = false;

    // sind Aufträge vorhanden
    // Flag dient nur zum aktivieren von Buttons
    try {
      if (!facadeTakeOff.findByMyFlight(UserID,StartICAO).isEmpty()) {
        StartICAO = facadeTakeOff.findByMyFlight(UserID,StartICAO).get(0).getLocationIcao();
        airportData = readAirportData(StartICAO);
        AirportName = airportData.getName();
        istEinAuftragVorhanden = true;
      } else {
        istEinAuftragVorhanden = false;

      }
    } catch (Exception e) {
    }

    // Abflug-Flughafen auslesen
    if (!StartICAO.equals("")) {
      Airport flughafen = facadeTakeOff.getAirportByIcao(StartICAO);
      if (flughafen != null) {
        FlughafenName = flughafen.getIcao() + " - " + flughafen.getName() + " " + flughafen.getLand() + " " + flughafen.getBundesland();
      } else {
        FlughafenName = "";
      }

    }

    // Laufenden YAACARS-Flug ermitteln
    //"missionsart"
    //1 = Normaler FTW-Flug
    //2 = Charter
    //3 = Rettung
    //4 = Missionen
    //
    //"flugstatus"
    //0 = angelegt
    //1 = gestartet
    //2 = beendet
    //
    Yaacarskopf yaacarsFlight = facadeTakeOff.getYAACARSFlight(UserID);

    if (yaacarsFlight != null) {
      if (yaacarsFlight.getMissionsart() == 1 || yaacarsFlight.getMissionsart() == 2) {
        assignmentList = facadeTakeOff.findByMyFlight(UserID,StartICAO);
        flugFortsetzung = true;
        isAcasFlightExist = true;
        acarsFlugInUse = false;
        boolean isFlight = true;
        setFlugGestartet(true);
        setSummetakeoff_display(getWertTakeOff());

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Flight-ON", "true");

        try {
          FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FlightLogo", getFlugLogo());
          FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FlugzeugType", rentedAircraft.getType());
        } catch (NullPointerException e) {

        }

        if (yaacarsFlight.getFlugstatus() == 0) {
          TankstelleOffen = true;
        } else {
          TankstelleOffen = false;
        }
      } else {
        acarsFlugInUse = true;
      }
    } else {

    }

  }

  public void onErrorText() {
    AbrechnungsFehlerText = "";
    AbrechnungsFehler = false;
  }

  // Abrechnungsziele Summe
  public List<ViewAbrechnungZieleSumme> getAbrechnungsziele() {
    if (rentedAircraft != null) {
      return facadeTakeOff.getAbrechnungsziele(UserID, Abflugflughafen);
    } else {
      return null;
    }

  }

  /*
      *******************************************************  Flugzeuge BEGINN
   */
  public ViewFlugzeugeMietKauf getMeinGemietetesFlugzeug() {
    try {

      rentedAircraft = facadeTakeOff.findMyRentedAircraft(UserID);
      Abflugflughafen = rentedAircraft.getAktuellePositionICAO();
      TankstelleTreibstoffArt = rentedAircraft.getTreibstoffArt();
      TreibstoffArt = TankstelleTreibstoffArt;
      IDFlugzeugbesitzer = rentedAircraft.getIdflugzeugBesitzer();
      TankRestMengeSprit = rentedAircraft.getAktuelleTankfuellung();
      TankstelleMaxSpritMenge = rentedAircraft.getTreibstoffkapazitaet() - TankRestMengeSprit;
      maxTankkapazitaet = rentedAircraft.getTreibstoffkapazitaet();
      setSelectedAircraft(rentedAircraft);

      if (rentedAircraft.getZustand() >= 99.00) {
        zustandText = loginMB.onSprache("Terminal_txt_zustandTextSehrGut");
      } else if (rentedAircraft.getZustand() < 99.00) {
        zustandText = loginMB.onSprache("Terminal_txt_zustandTextGut");
      } else if (rentedAircraft.getZustand() < 98.00) {
        zustandText = loginMB.onSprache("Terminal_txt_zustandTextLeichteMaengel");
      } else if (rentedAircraft.getZustand() < 97.00) {
        zustandText = loginMB.onSprache("Terminal_txt_zustandTextNochFlugtauglich");
      } else if (rentedAircraft.getZustand() <= 95.00) {
        zustandText = loginMB.onSprache("Terminal_txt_zustandTextKeinTransport");
      } else if (rentedAircraft.getZustand() <= 90.00) {
        zustandText = loginMB.onSprache("Terminal_txt_zustandTextGesperrt");
      }

      if (CCheckFaellig()) {
        zustandText = loginMB.onSprache("Terminal_txt_zustandTextCCheck");
      }

      if (rentedAircraft.getAktuelleTankfuellung() > 0) {
        // Button Flug starten aktivieren da Sprit in der Maschine ist
        TankenBezahlt = true;
      }

      UmbauSitzplatz umbauDaten = facadeTakeOff.UmbauDaten(rentedAircraft.getIdMietKauf());

      if (umbauDaten != null) {

        wirdUmgebaut = true;
        umbauZeit = umbauDaten.getSperrzeit();
        onUmbauAktiv();
      } else {
        wirdUmgebaut = false;
      }

      if (rentedAircraft.getIdSitzKonfiguration() > 0) {
        sitzKonfiguration = facadeTakeOff.getKonfiguration(rentedAircraft.getIdSitzKonfiguration());

        SitzeBusiness = sitzKonfiguration.getSitzeBusiness();
        SitzeEconomy = sitzKonfiguration.getSitzeEconomy();
        SitzeCrew = sitzKonfiguration.getCrew();
        SitzeFlugbegleiter = sitzKonfiguration.getFlugbegleiter();
        SitzKonfigFaktor = sitzKonfiguration.getFaktor();

        // Gewicht der Crew wird auf 0 gesetzt
        setGewichtDerCrew(0);
        //setGewichtDerCrew((SitzeCrew + SitzeFlugbegleiter + 1) * 75);

        // ist kein DOW in der Sitzkonfiguration hinterlegt, dann das Leergewicht nehmen.
        // Sitzkonfiguration DOW = Leergewicht
        // Tankkapazität mit in Sitzkonfiguration
        if (sitzKonfiguration.getDow() > 0) {
          Leergewicht = sitzKonfiguration.getDow();
        } else {
          Leergewicht = rentedAircraft.getLeergewicht();
        }

        if (sitzKonfiguration.getTankkapazitaet() > 0) {
          maxTankkapazitaet = sitzKonfiguration.getTankkapazitaet();
          TankstelleMaxSpritMenge = maxTankkapazitaet - TankRestMengeSprit;
        } else {
          TankstelleMaxSpritMenge = rentedAircraft.getTreibstoffkapazitaet() - TankRestMengeSprit;
        }

        CargoVerfuegbar = sitzKonfiguration.getCargo();
        maxPayload = sitzKonfiguration.getMaxPayload();

      } else {
        SitzeBusiness = rentedAircraft.getSitzeBusinessClass();
        SitzeEconomy = rentedAircraft.getSitzeEconomy();
        SitzeCrew = rentedAircraft.getBesatzung();
        SitzeFlugbegleiter = rentedAircraft.getFlugbegleiter();
        Leergewicht = rentedAircraft.getLeergewicht();
        CargoVerfuegbar = rentedAircraft.getCargo();
        maxPayload = rentedAircraft.getPayload();
        SitzKonfigFaktor = 1.0;
        // Gewicht der Crew wird auf 0 gesetzt
        setGewichtDerCrew(0);
        //setGewichtDerCrew((rentedAircraft.getBesatzung() + rentedAircraft.getFlugbegleiter() + 1) * 75);
      }

      if (rentedAircraft.getAntriebsart() == 1) {
        // evtl. Cargo = MaxGewicht - Leergewicht
      }

      maxBelegbareSitze = SitzeEconomy + SitzeBusiness;
      FlugzeugFixkosten = rentedAircraft.getFixkosten();

      istEinFlugzeugGemietet = true;
      Abflugflughafen = rentedAircraft.getAktuellePositionICAO().trim();
      StartICAO = rentedAircraft.getAktuellePositionICAO().trim();

      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("AircraftID", rentedAircraft.getIdMietKauf());

      //LangstreckenFlugzeug = rentedAircraft.getLangstreckenflugzeug();
      LangstreckenFlugzeug = false;

      return rentedAircraft;

    } catch (NullPointerException e) {
      //System.out.println("Take-Off Nullpointer : Kein Flugzeug gemietet.");
      istEinFlugzeugGemietet = false;
      return null;
    }

  }

  private void aircraftUpdate(String Art) {

    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.aircraftUpdate() BEGINN");
    ReportSchreiben("Updateart " + Art);

    if (rentedAircraft != null) {
      MeinFlugzeug = facadeTakeOff.findeMeinFlugzeug(rentedAircraft.getIdMietKauf());

      if (Art.equals("Abrechnung")) {
        int FlightMiles = MeinFlugzeug.getGesamtEntfernung();
        int Flights = MeinFlugzeug.getGesamtFluege();
        int FlightTimeCur = MeinFlugzeug.getGesamtFlugzeit();
        int FlightPaxes = MeinFlugzeug.getGesamtTransportiertePassagiere();
        int FlightCargo = MeinFlugzeug.getGesamtTransportiertesCargo();
        int TotalEngineTime = MeinFlugzeug.getMaschinenLaufzeitMinuten();
        int LastCheck = MeinFlugzeug.getLetzterCheckMinuten();
        int AirFrame = MeinFlugzeug.getGesamtMaschinenLaufzeitMinuten();
        int GesamtAlter = MeinFlugzeug.getGesamtAlterDesFlugzeugsMinuten();
        double Zustand = MeinFlugzeug.getZustand();

        FlightMiles = FlightMiles + currentFlightMiles;
        Flights = Flights + 1;
        FlightTimeCur = FlightTimeCur + getFlightTime();
        FlightPaxes = FlightPaxes + AnzahlTransportierterPassgiereBerechnen();
        FlightCargo = FlightCargo + SummeCargo;
        TotalEngineTime = TotalEngineTime + getFlightTime();
        AirFrame = AirFrame + getFlightTime();
        LastCheck = LastCheck + getFlightTime();
        GesamtAlter = GesamtAlter + getFlightTime();
        Zustand = Zustand - onVerschleiss();

        // bei Job aus Jobmanagement wird das Flugzeug entsperrt
        //
        if (onJobAusJobboerse()) {
          long aktZeit = new Date().getTime();
          MeinFlugzeug.setIstGesperrtBis(new Date(aktZeit));
        }

        MeinFlugzeug.setAktuellePositionICAO(ZielFlughafenICAO.toUpperCase());
        MeinFlugzeug.setGesamtEntfernung(FlightMiles);
        MeinFlugzeug.setGesamtFluege(Flights);
        MeinFlugzeug.setGesamtFlugzeit(FlightTimeCur);
        MeinFlugzeug.setGesamtTransportiertePassagiere(FlightPaxes);
        MeinFlugzeug.setGesamtTransportiertesCargo(FlightCargo);
        MeinFlugzeug.setMaschinenLaufzeitMinuten(TotalEngineTime);
        MeinFlugzeug.setGesamtMaschinenLaufzeitMinuten(AirFrame);
        MeinFlugzeug.setGesamtAlterDesFlugzeugsMinuten(GesamtAlter);
        MeinFlugzeug.setLetzterCheckMinuten(LastCheck);
        //System.out.println("Spritverbrauch KG : " + Spritverbrauch);
        MeinFlugzeug.setAktuelleTankfuellung(MeinFlugzeug.getAktuelleTankfuellung() - (int) Spritverbrauch);
        MeinFlugzeug.setIstInDerLuft(false);
        MeinFlugzeug.setZustand(Zustand);

      }

      if (Art.equals("Tanken")) {
        MeinFlugzeug.setAktuelleTankfuellung(TankMengeKilo + TankRestMengeSprit);
        MeinFlugzeug.setLetzterSpritPreis(SpritPreis);
      }

      if (Art.equals("Storno")) {
        MeinFlugzeug.setAktuelleTankfuellung(0);
        MeinFlugzeug.setLetzterSpritPreis(0.0);
      }

      if (Art.equals("StartFlug")) {
        MeinFlugzeug.setIstInDerLuft(true);
        MeinFlugzeug.setAbgeflogenVonICAO(Abflugflughafen);
      }

      if (Art.equals("FlugAbbruch")) {
        MeinFlugzeug.setIstInDerLuft(false);
      }

      facadeTakeOff.editMeinFlugzeug(MeinFlugzeug);
    } else {
      ReportSchreiben("Kein gemietetes Flugzeug vorhanden ");
    }

    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.aircraftUpdate() ENDE");
  }

  public double onVerschleiss() {

    if (rentedAircraft != null) {

      double KolbenmotorNutzungsdauer = 24000;
      double TurboPropNutzungsdauer = 32000;
      double TurbineNutzungsdauer = 40000;
      double VerschleissFaktor = 0;
      double Nutzungsdauer = 0;

      switch (rentedAircraft.getAntriebsart()) {
        case 1:
          Nutzungsdauer = KolbenmotorNutzungsdauer;
          break;
        case 2:
          Nutzungsdauer = TurboPropNutzungsdauer;
          break;
        case 3:
          Nutzungsdauer = TurbineNutzungsdauer;
        default:
          break;
      }

      int AlterInJahren = Calendar.getInstance().get(Calendar.YEAR) - rentedAircraft.getBaujahr();
      if (AlterInJahren > 100) {
        AlterInJahren = 100;
      }

      double gesamtStundenFlugzeug = rentedAircraft.getGesamtAlterDesFlugzeugsMinuten() / 60;

      ReportSchreiben("Gesamtstunden vor Prüfung : " + gesamtStundenFlugzeug);

      if (gesamtStundenFlugzeug < 2000.0 && rentedAircraft.getAntriebsart() == 3) {
        gesamtStundenFlugzeug = 5000.0;
      }

      if (gesamtStundenFlugzeug < 1000.0 && rentedAircraft.getAntriebsart() == 2) {
        gesamtStundenFlugzeug = 2500.0;
      }

      if (gesamtStundenFlugzeug < 1000.0 && rentedAircraft.getAntriebsart() == 1) {
        gesamtStundenFlugzeug = 1000.0;
      }

      double flugZeit = FlightTime / 60.0;

      ReportSchreiben("Flugzeug : " + rentedAircraft.getType());
      ReportSchreiben("Flugzeit Std. : " + flugZeit);
      ReportSchreiben("gesamtstunden : " + gesamtStundenFlugzeug);

      double NutzungsFaktor = gesamtStundenFlugzeug / Nutzungsdauer;

      ReportSchreiben("Max. Nutzung Std. : " + Nutzungsdauer);

      double prozent = (NutzungsFaktor * flugZeit) / 10.0;

      ReportSchreiben("Nutzungsfaktor % : " + NutzungsFaktor);
      ReportSchreiben("Verschleiß % : " + prozent);

      //Altersfaktor beginn
      //double AltersFaktor = (100.0 - (100.0 - AlterInJahren)) / 100.0;
      double AltersFaktor = (100.0 - (100.0 - AlterInJahren)) / 50.0;
      prozent = prozent + (prozent * AltersFaktor);
      //Altersfaktor ende

      ReportSchreiben("Flugzeugalter % : " + AlterInJahren);
      ReportSchreiben("Altersfaktor % : " + AltersFaktor);
      ReportSchreiben("Verschleiß neu (Round) % : " + (double) Math.round(prozent * 1000) / 1000);

      return (double) Math.round(prozent * 1000) / 1000;

    }

    return 0.0;
  }

  public void onUmbauAktiv() {
    if (wirdUmgebaut) {
      if (umbauZeit.getTime() < new Date().getTime()) {
        wirdUmgebaut = false;
        UmbauSitzplatz umbauDaten = facadeTakeOff.UmbauDaten(rentedAircraft.getIdMietKauf());
        facadeTakeOff.removeUmbau(umbauDaten);
      }
    }

  }

  private boolean isLizenzOK() {

    String Lizenz = rentedAircraft.getLizenz();
    String UserLizenz = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Lizenz");
    Flugzeuge flz = facadeTakeOff.readFlugzeug(rentedAircraft.getIdFlugzeug());
    int Flugzeugklasse = flz.getFlugzeugklasse();

    int RentedFlugzeugKlasse = 0;

    try {
      RentedFlugzeugKlasse = rentedAircraft.getFlugzeugklasse();
    } catch (NullPointerException e) {
      RentedFlugzeugKlasse = 0;
    }

    //Prüfen auf Leerflug
    if (assignmentList.isEmpty()) {
      return true;
    }

    //Prüfen auf gemichte Jobs bei einem Jobbörsenjob
    if (facadeTakeOff.onIfGemicht(UserID).size() > 1) {
      if (!LizenzCheck(UserLizenz, Lizenz)) {
        AbrechnungsFehler = true;
        AbrechnungsFehlerText = "Sie haben nicht die erforderliche Lizenz um dieses Flugzeug zu fliegen.";
        return false;
      }
    }

    //Prüfen auf Freelancer Job
    //IDAirline = -1
    if (facadeTakeOff.onIfFreelancerJob(UserID).size() == 1) {
      if (!LizenzCheck(UserLizenz, Lizenz)) {
        AbrechnungsFehler = true;
        AbrechnungsFehlerText = "Sie haben nicht die erforderliche Lizenz um dieses Flugzeug zu fliegen.";
        return false;
      }
    }

    List<ViewAssiErweiterteLizenzPruefung> assiKlassen = facadeTakeOff.onMehrereKlassen(UserID);

    //Prüfen auf mehrere Flugzeugklassen
    if (assiKlassen.size() > 1) {
      AbrechnungsFehler = true;
      AbrechnungsFehlerText = "Unterschiedliche Flugzeugklassen festgestellt.";
      return false;

    }

    if (assiKlassen.size() == 1) {

      if (RentedFlugzeugKlasse == assiKlassen.get(0).getFlugzeugklasse()) {
        return true;
      } else {
        AbrechnungsFehler = true;
        AbrechnungsFehlerText = "Falsche Flugzeugklasse";
        return false;
      }
    }

    // ************** Ende Klassenprüfung
    if (Lizenz.equals("PPL-A")) {

      return true;
    }

    List<Assignement> assilist = facadeTakeOff.getGroupOfLizenz(rentedAircraft.getAktuellePositionICAO(), UserID);

    if (assilist != null) {

      if (assilist.isEmpty()) {
        //keine Lizenzen vorhanden
        AbrechnungsFehler = true;
        AbrechnungsFehlerText = "Flugzeuglizenz passt nicht zu den Aufträgen, Flug wird nicht gestartet";
        return true;
      }

      // Mehr als 2 Lizenzen geht nicht!
      if (assilist.size() > 2) {
        AbrechnungsFehler = true;
        AbrechnungsFehlerText = "Flugzeuglizenz passt nicht zu den Aufträgen, Flug wird nicht gestartet";
        return false;
      }

      if (assilist.size() == 1) {
        if (assilist.get(0).getLizenz().equals("Standard-Job")) {
          return true;
        } else if (assilist.get(0).getLizenz().equals(Lizenz)) {
          return true;
        }
      }

      if (assilist.size() == 2) {

        int zaehler = 0;

        for (Assignement assi : assilist) {

          if (assi.getLizenz().equals(Lizenz)) {
            zaehler = zaehler + 1;
          }

          if (assi.getLizenz().equals("Standard-Job")) {
            zaehler = zaehler + 1;
          }

        }

        if (zaehler == 2) {
          return true;
        }
      }

    } else {
      // assilist ist null, evtl Leerflug
      return true;
    }

    AbrechnungsFehler = true;
    AbrechnungsFehlerText = "Flugzeuglizenz passt nicht zu den Aufträgen, Flug wird nicht gestartet";

    return false;
  }

  private boolean LizenzCheck(String UserLizenz, String FlugzeugLizenz) {

    if (UserLizenz.equals(FlugzeugLizenz) || UserLizenz.equals("ATPL")) {
      return true;
    }

    if (UserLizenz.equals("PPL-A") && FlugzeugLizenz.equals("PPL-A")) {
      return true;
    }

    if (UserLizenz.equals("CPL") && (FlugzeugLizenz.equals("PPL-A") || FlugzeugLizenz.equals("CPL"))) {
      return true;
    }

    if (UserLizenz.equals("MPL") && (FlugzeugLizenz.equals("PPL-A") || FlugzeugLizenz.equals("MPL") || FlugzeugLizenz.equals("CPL") )) {
      return true;
    }

    return false;
  }

  /*
  *******************************************************  Flugzeuge ENDE
   */
 /*
  *******************************************************  Typerating Update Start
   */
  public void updateTyperating() {
    UserTyperatings typerating = facadeTakeOff.getTyperating(UserID, rentedAircraft.getTypeRating());

    double betrag = 0;

    if (typerating != null) {

      int gesamtGeflogenMinuten = typerating.getMinutenGeflogen() + getFlightTime();
      int gesamtGeflogenStd = gesamtGeflogenMinuten / 60;

//      System.out.println("de.klees.beans.takeoff.takeoffBean.updateTyperating() gesamtGeflogenMinuten = " + gesamtGeflogenMinuten);
//      System.out.println("de.klees.beans.takeoff.takeoffBean.updateTyperating() gesamtGeflogenStd = " + gesamtGeflogenStd);
      typerating.setMinutenGeflogen(gesamtGeflogenMinuten);

      if (!typerating.getErfuellt()) {
        // evtl. müssen nur die Restminuten berechnet werden
        if (((rentedAircraft.getTypeRatingMinStd() * 60) - typerating.getMinutenGeflogen()) > 0) {

          int MinutenRest = ((rentedAircraft.getTypeRatingMinStd() * 60) - typerating.getMinutenGeflogen());

          if (MinutenRest > getFlightTime()) {
            betrag = rentedAircraft.getTypeRatingKostenStd() / 60.0 * (double) getFlightTime();
          } else {
            betrag = rentedAircraft.getTypeRatingKostenStd() / 60.0 * (double) MinutenRest;
          }
        }
      }

      if (gesamtGeflogenStd >= rentedAircraft.getTypeRatingMinStd()) {
        typerating.setErfuellt(true);
      }

      facadeTakeOff.saveTypeRating(typerating);

    } else {
      UserTyperatings newType = new UserTyperatings();
      newType.setUserID(UserID);
      newType.setTypeRating(rentedAircraft.getTypeRating());
      newType.setMinutenGeflogen(getFlightTime());
      newType.setErfuellt(false);
      facadeTakeOff.createTypeRating(newType);
      betrag = rentedAircraft.getTypeRatingKostenStd() / 60.0 * (double) getFlightTime();
    }

//    System.out.println("de.klees.beans.takeoff.takeoffBean.updateTyperating() Schulungsbetrag abbuchen " + (betrag - (betrag * 2)));
    if (betrag > 0.0) {
      String VerwendungsZweck = loginMB.onSprache("Terminal_msg_Bankbuchung_AusbildungskostenTyperating") + " " + rentedAircraft.getTypeRating();
      String UserName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName");
      String UserKonto = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("BankKonto");
      SaveBankbuchung(UserKonto, UserName, UserKonto, UserName,
              new Date(), betrag - (betrag * 2), "500-1000002", "**** FTW Aircraft Stock ****",
              new Date(), VerwendungsZweck, UserID, -1, -1, -1, -1, -1, -1, Abflugflughafen, -1, rentedAircraft.getIdMietKauf(), -1, -1);
    }

  }

  /*
  *******************************************************  Typerating Update ENDE
   */
 /*
  *******************************************************  Terminal Beginn
   */
  public List<ViewFBOUserObjekte> getDepartureTerminals() {
    return facadeTakeOff.findAllTerminals(Abflugflughafen);
  }

  public List<ViewFBOUserObjekte> getArrivalTerminals() {
    return facadeTakeOff.findAllTerminals(ZielFlughafenICAO);
  }

  /*
  *******************************************************  Terminal ENDE
   */

 /*
  *******************************************************  Auftraege BEGINN
   */
  public List<Assignement> getMyAssignments() {
    if (!isLoaded) {
      config = facadeTakeOff.readConfig();
      isLoaded = true;
      assignmentList = facadeTakeOff.findByMyFlight(UserID, Abflugflughafen);
    }
    return assignmentList;
  }

  public void onAssignmentOfHold() {
    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.onAssignmentOfHold() BEGINN");
    for (Assignement agm : assignmentList) {
      if (agm.getLocationIcao().equals(Abflugflughafen)) {

        // Charter Auftraege einmalig um 8 Stunden verlaengern
        if (!agm.getVerlaengert() && agm.getType() == 3) {
          long newTime = agm.getExpires().getTime() + (8 * 60 * 60 * 1000);
          agm.setExpires(new Date(newTime));
          agm.setVerlaengert(true);
        }

        // Assignment einmalig um 48 Stunden verlaengern
        if (!agm.getVerlaengert()) {
          long newTime = agm.getExpires().getTime() + (48 * 60 * 60 * 1000);
          agm.setExpires(new Date(newTime));
          agm.setVerlaengert(true);
        }
        agm.setUserlock(1);
        facadeTakeOff.saveAssignment(agm);
      }
    }
    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.onAssignmentOfHold() ENDE");
  }

  public void onAssignmentFromHold() {
    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.onAssignmentFromHold() BEGINN");
    for (Assignement assi : assignmentList) {
      if (assi.getLocationIcao().equals(Abflugflughafen)) {
        facadeTakeOff.onAssignmentFromHold(assi.getIdassignement());
      }
    }
    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.onAssignmentFromHold() ENDE");
  }

  public void deleteFinishAssigments(String AbflugFlughafen) {
    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.deleteFinishAssigments() BEGINN");
    // Routentraffic speichern, erledigte Assignments löschen, Rest entsprechen aendern
    int Distanz[];

    for (Assignement ag : assignmentList) {

      if (ag.getToIcao().equals(ZielFlughafenICAO) && ag.getUserlock() == 1) {

        // Routentraffic speichern BEGINN
        Flugrouten fg = facadeTakeOff.getFlugroute(ag.getIdRoute());
        if (fg != null) {
          fg.setLetzterFlug(new Date());
          fg.setUmsatz(fg.getUmsatz() + ag.getPay());
          fg.setUmsatzmenge(fg.getUmsatzmenge() + ag.getAmmount());
          facadeTakeOff.saveFlugroute(fg);
        }
        // Routentraffic speichern ENDE

        // Type 5 Spritfässer nicht löschen,
        // wurden vorher schon verarbeitet und verbleiben am Zielflughafen da evtl. Spritlager voll
        if (!ag.getRoutenArt().equals(5)) {
          facadeTakeOff.deleteFinishAssignment(ag);
        }

      } else {
        // verschieben der übriggebliebenen Assignments    
        // neue Location eintragen nur wenn es gleicher Abflugflughafen war 
        // und die Aufträge auch auf "Fliegt gerade" gestanden haben
        if (ag.getLocationIcao().equals(AbflugFlughafen) && ag.getUserlock() == 1) {
          // ****** Neuberechnung der Distanze und des Kurses
          Distanz = CONF.DistanzBerechnung(
                  facadeTakeOff.getAirportByIcao(ZielFlughafenICAO).getLongitude(),
                  facadeTakeOff.getAirportByIcao(ZielFlughafenICAO).getLatitude(),
                  facadeTakeOff.getAirportByIcao(ag.getToIcao()).getLongitude(),
                  facadeTakeOff.getAirportByIcao(ag.getToIcao()).getLatitude()
          );
          // verbleibende Assignments auf Take-Off setzen und Daten anpassen
          ag.setLocationIcao(ZielFlughafenICAO);
          ag.setDistance(Distanz[0]);
          ag.setDirect(Distanz[1]);
          ag.setUserlock(0);
          facadeTakeOff.saveAssignment(ag);

        }
      }
    }
    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.deleteFinishAssigments() ENDE");
  }

  public void TreibstoffFaesserVerbuchen() {
    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.TreibstoffFaesserVerbuchen() BEGINN");
    boolean verbuchenOK = false;
    FboUserObjekte fb = null;
    FboObjekte fbo = null;
    int bestandJETA = 0;
    int bestandAVGAS = 0;
    int maxBestand = 0;

    // TreibstoffFaesser die der Benutzer an Board hat auslesen
    List<Assignement> listAssignments = facadeTakeOff.getAllSpritFaesser(ZielFlughafenICAO, UserID);

    if (!listAssignments.isEmpty()) {
      fb = facadeTakeOff.getUserFBOObjekt(listAssignments.get(0).getIdFBO());

      if (fb != null) {
        verbuchenOK = true;
      } else {
        ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.TreibstoffFaesserVerbuchen() FBO Objekt nicht gefunden " + listAssignments.get(0).getIdFBO());
      }

      if (verbuchenOK) {
        fbo = facadeTakeOff.readFBO(fb.getIdfboObjekt());
        bestandAVGAS = fb.getBestandAVGASkg();
        bestandJETA = fb.getBestandJETAkg();
        maxBestand = fbo.getTankstelleMaxFuelKG();
      }

    }

    if (verbuchenOK) {
      for (Assignement ag : listAssignments) {
        if ((bestandAVGAS + bestandJETA + ag.getAmmount()) <= maxBestand) {
          if (ag.getCommodity().equals("AVGAS")) {
            bestandAVGAS = bestandAVGAS + ag.getAmmount();
          } else if (ag.getCommodity().equals("JETA")) {
            bestandJETA = bestandJETA + ag.getAmmount();
          }
          facadeTakeOff.deleteFinishAssignment(ag);
        }

      }

      if (fb != null) {
        fb.setBestandAVGASkg(bestandAVGAS);
        fb.setBestandJETAkg(bestandJETA);
        facadeTakeOff.editFBOObjekt(fb);
      }
    }

    if (!verbuchenOK) {
      //Spritfässer aus der Luft holen
      if (!listAssignments.isEmpty()) {
        for (Assignement ag : listAssignments) {
          ag.setUserlock(0);
          facadeTakeOff.saveAssignment(ag);
        }
      }
    }

    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.TreibstoffFaesserVerbuchen() ENDE");
  }

  public void onAuftragsHinweisClose() {
    istEinAuftragVorhanden = true;
  }

  public boolean isLangstreckenFlugzeug() {
    return rentedAircraft.getLangstreckenflugzeug();
  }

  public boolean isLangstrecke() {
    return false;
//    assignmentList = facadeTakeOff.findByMyFlight(UserID);
//    boolean gefunden = false;
//
//    for (Assignement assi : assignmentList) {
//      if (assi.getLangstrecke()) {
//        gefunden = true;
//        break;
//      }
//    }
//
//    if (gefunden) {
//      for (Assignement assi : assignmentList) {
//        if (!assi.getLangstrecke()) {
//          gefunden = false;
//        }
//      }
//    }
//
//    return gefunden;
  }

  public boolean isStartButtonDisabled() {

    if (isFlugGestartet()) {
      return true;
    }

    if (assignmentList != null) {
      if (assignmentList.isEmpty()) {
        return false;
      }
    }

//    if (isLangstrecke() && isLangstreckenFlugzeug()) {
//      return false;
//    }
//
//    if (!isLangstrecke() && !isLangstreckenFlugzeug()) {
//      return false;
//    }
//
//    if (isLangstrecke() && !isLangstreckenFlugzeug()) {
//      return false;
//    }
//
//    if (!isLangstrecke() && isLangstreckenFlugzeug()) {
//      return true;
//    }
    return false;
  }

  /*
  *******************************************************  Auftraege ENDE
   */
 /*
  *******************************************************  Flughaefen BEGINN
   */
  private Airport readAirportData(String ICAO) {
    return facadeTakeOff.getAirportByIcao(ICAO);
  }

  public void icaoFuerFlughafenInfoSetzen(String icao) {
    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Assignment_ICAO", icao);
  }

  /*
  *******************************************************  Flughaefen Ende
   */
 /*
  *******************************************************  Tanken BEGINN
   */
  private void YAACARSFlugAnpassen() {
    // YAACARS Flug Anpassen, wenn gestartet
    // Nach dem Tanken muss auch die Tankmenge im YAACARSKopf angepasst werden.
    Yaacarskopf yaacarsflight = facadeTakeOff.YAACARSFlightInfo(UserID);
    if (yaacarsflight != null) {
      rentedAircraft = getMeinGemietetesFlugzeug();
      yaacarsflight.setTankmengekg(rentedAircraft.getAktuelleTankfuellung());
      yaacarsflight.setResttankmengekg(rentedAircraft.getAktuelleTankfuellung());
      facadeTakeOff.saveYAACARSKopf(yaacarsflight);
    }
  }

  public void onResetTankstelle() {
    TankMengeProzent = 0;
    TankMengeKilo = 0;
    TankBisFuellstandKG = 0;
    berechneTankfuellung();
  }

  public void onTankenBezahlen() {

    setTankenBezahlt(true);
    FboUserObjekte fboobjekt = null;
    Benutzer user = null;

    AbrechnungsFehler = false;

    String EmpfaegerName = "**** FTW OIL *****";
    String EmpfaengerKonto = "500-1000003";
    String FlugzeugReg = rentedAircraft.getRegistrierung();
    int objektid = -1;
    String icao = "";
    int Kostenstelle = 0;
    String VerwendungsZweck = "";

    if (!EnttankenLaeuft) {

      EnttankenLaeuft = true;

      if (TankstelleSumme > 0) {

        onTankenBerechneKilo();

        if (objektID > 0) {
          fboobjekt = facadeTakeOff.getUserFBOObjekt(objektID);
          user = facadeTakeOff.findByUserName(fboobjekt.getIdUser());

          EmpfaegerName = fboobjekt.getKontoName();
          EmpfaengerKonto = fboobjekt.getBankkonto();
        }

        String UserName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName");
        String UserKonto = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("BankKonto");

        if (getTankstelleTreibstoffArt() == 1) {
          VerwendungsZweck = "*** FTW OIL *** AVGAS : " + dfWaehrung.format(config.getPreisAVGASkg()) + " € - " + TankMengeKilo + " (kg) - Reg: " + FlugzeugReg;
          SpritPreis = config.getPreisAVGASkg();
          if (objektID > 0) {
            // es wurde nicht bei FTW-Oil getankt
            SpritPreis = fboobjekt.getPreisAVGAS();
            VerwendungsZweck = fboobjekt.getName() + " *** AVGAS *** " + TankMengeKilo + " (kg) " + dfWaehrung.format(SpritPreis) + " €  - Reg: " + FlugzeugReg;
            // Spritmengen verbuchen
            fboobjekt.setBestandAVGASkg(fboobjekt.getBestandAVGASkg() - TankMengeKilo);
            facadeTakeOff.FboBestandVerbuchen(fboobjekt);
            objektid = fboobjekt.getIdfboObjekt();
            icao = fboobjekt.getIcao();
            Kostenstelle = fboobjekt.getKostenstelle();
          }

        } else {
          VerwendungsZweck = "*** FTW OIL *** JETA : " + dfWaehrung.format(config.getPreisJETAkg()) + " € - " + TankMengeKilo + " (kg) - Reg: " + FlugzeugReg;
          SpritPreis = config.getPreisJETAkg();
          if (objektID > 0) {
            // es wurde nicht bei FTW-Oil getankt
            SpritPreis = fboobjekt.getPreisJETA();
            VerwendungsZweck = fboobjekt.getName() + " *** JETA *** " + TankMengeKilo + " (kg) - " + dfWaehrung.format(SpritPreis) + " € - Reg: " + FlugzeugReg;
            // Spritmengen verbuchen
            fboobjekt.setBestandJETAkg(fboobjekt.getBestandJETAkg() - TankMengeKilo);
            facadeTakeOff.FboBestandVerbuchen(fboobjekt);
            objektid = fboobjekt.getIdfboObjekt();
            icao = fboobjekt.getIcao();
            Kostenstelle = fboobjekt.getKostenstelle();
          }
        }

        //Tankstelle Kundenzahlung
        SaveBankbuchung(UserKonto, UserName, UserKonto, UserName, new Date(), TankstelleSumme - (TankstelleSumme * 2), EmpfaengerKonto, EmpfaegerName,
                new Date(), VerwendungsZweck, UserID, -1, -1, -1, -1, -1, -1, icao, objektid, -1, Kostenstelle, -1);

        //Gegenbuchung beim Tankstellenbesitzer
        if (objektID > 0) {
          SaveBankbuchung(EmpfaengerKonto, EmpfaegerName, UserKonto, UserName, new Date(), TankstelleSumme, EmpfaengerKonto, EmpfaegerName,
                  new Date(), VerwendungsZweck, fboobjekt.getIdUser(), -1, -1, -1, -1, -1, -1, icao, objektid, -1, Kostenstelle, -1);
        } else {
          SaveBankbuchung(EmpfaengerKonto, EmpfaegerName, UserKonto, UserName, new Date(), TankstelleSumme, EmpfaengerKonto, EmpfaegerName,
                  new Date(), VerwendungsZweck, UserID, -1, -1, -1, -1, -1, -1, "", -1, -1, -1, -1);

        }

        aircraftUpdate("Tanken");

        YAACARSFlugAnpassen();

        onResetTankstelle();

      } else {
        boolean enttankt = false;

        onTankenBerechneKilo();

        // Wenn die Tanksumme im minus ist 
        // Flugzeug soll entankt werden
        if (rentedAircraft.getIdflugzeugBesitzer() == UserID) {

          if (TankstelleSumme < 0) {

            double PreisAVGAS = config.getPreisAVGASkg();
            double PreisJetA = config.getPreisJETAkg();;

            onTankLeeren();

            if (getTankstelleTreibstoffArt() == 1) {
              // 0.80 Euro pro Kilo für das Entleeren
              SpritPreis = (PreisAVGAS - 0.80);
              VerwendungsZweck = "*** FTW OIL *** AVGAS : (" + dfWaehrung.format(SpritPreis) + " €) " + TankMengeKilo + " (kg) - Reg: " + FlugzeugReg;
            } else {
              // 0.80 Euro pro Kilo für das Entleeren
              SpritPreis = (PreisJetA - 0.80);
              VerwendungsZweck = "*** FTW OIL *** JETA : (" + dfWaehrung.format(SpritPreis) + " €) " + TankMengeKilo + " (kg) - Reg: " + FlugzeugReg;
            }

            TankMengeKilo = TankMengeKilo - (TankMengeKilo * 2);

            Double Betrag = SpritPreis * TankMengeKilo;

            onTankenRueckerstattung(Betrag, VerwendungsZweck);

            enttankt = true;
          }
        }

        // Managern erlauben das Flugzeug zu enttanken, wenn Flugzeug im Besitz der FG ist
        // Gutschrift erhält Tankstellenbesitzer
        // ManagerID Auslesen
        if (!enttankt) {
          user = facadeTakeOff.findByUserName(UserID);
          if (user != null) {
            if (user.getFluggesellschaftManagerID() > 0) {
              //Fluggesellschaft auslesen
              Fluggesellschaft fg = facadeTakeOff.readFluggesellschaft(user.getFluggesellschaftManagerID());
              if (fg != null) {
                //Managerberechtigung auslesen
                Fluggesellschaftmanager fgm = facadeTakeOff.readManager(UserID, fg.getIdFluggesellschaft());
                if (fgm != null) {
                  if (fgm.getAllowFlugzeugeBearbeiten()) {

                    if (fg.getIdFluggesellschaft() == rentedAircraft.getFluggesellschaftID()) {
                      double PreisAVGAS = config.getPreisAVGASkg();
                      double PreisJetA = config.getPreisJETAkg();;

                      // Tank leeren
                      onTankLeeren();

                      if (getTankstelleTreibstoffArt() == 1) {
                        // 0.80 Euro pro Kilo für das Entleeren
                        SpritPreis = (PreisAVGAS - 0.80);
                        VerwendungsZweck = "*** FTW OIL *** AVGAS : (" + dfWaehrung.format(SpritPreis) + " €) " + TankMengeKilo + " (kg) - Reg: " + FlugzeugReg;
                      } else {
                        // 0.80 Euro pro Kilo für das Entleeren
                        SpritPreis = (PreisJetA - 0.80);
                        VerwendungsZweck = "*** FTW OIL *** JETA : (" + dfWaehrung.format(SpritPreis) + " €) " + TankMengeKilo + " (kg) - Reg: " + FlugzeugReg;
                      }

                      EmpfaegerName = facadeTakeOff.getKontoName(rentedAircraft.getBankkontoBesitzer());
                      EmpfaengerKonto = rentedAircraft.getBankkontoBesitzer();

                      TankMengeKilo = TankMengeKilo - (TankMengeKilo * 2);

                      Double Betrag = (SpritPreis * TankMengeKilo);

                      SaveBankbuchung(EmpfaengerKonto, EmpfaegerName, "500-1000003", "**** FTW OIL *****", new Date(), Betrag, EmpfaengerKonto, EmpfaegerName,
                              new Date(), VerwendungsZweck, -1, -1, -1, -1, -1, -1, -1, "", -1, -1, 0, -1);

                      enttankt = true;
                    }
                  }
                }
              }
            }
          }

        }
      }
    }

  }

  public void onTankLeeren() {
    MeinFlugzeug = facadeTakeOff.findeMeinFlugzeug(rentedAircraft.getIdMietKauf());

    if (MeinFlugzeug.getAktuelleTankfuellung() > 0) {

      // TankMengeKilo ist ein negativer Wert, deshalb +
      if (rentedAircraft.getTreibstoffArt() == 1) {
        MeinFlugzeug.setAktuelleTankfuellung(MeinFlugzeug.getAktuelleTankfuellung() + TankMengeKilo);
        setTankenBezahlt(false);
        setTankstelleSumme(0);
        setTankstelleSpritMenge(0);
        setTankstelleMengeLbs(0);
        setTankRestMengeSprit(0);
      } else {
        MeinFlugzeug.setAktuelleTankfuellung(MeinFlugzeug.getAktuelleTankfuellung() + TankMengeKilo);
        setTankenBezahlt(false);
        setTankstelleSumme(0);
        setTankstelleSpritMenge(0);
        setTankstelleMengeLbs(0);
        setTankRestMengeSprit(0);
      }
      facadeTakeOff.editMeinFlugzeug(MeinFlugzeug);
      NewMessage(loginMB.onSprache("Terminal_msg_FlugzeugWurdeEnttankt"));

      YAACARSFlugAnpassen();

    }

  }

  public void onTankenRueckerstattung(double Betrag, String VerwendungsZweck) {
    AbrechnungsFehler = false;
    String UserName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName");
    String UserKonto = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("BankKonto");

    SaveBankbuchung(UserKonto, UserName, "500-1000003", "**** FTW OIL *****", new Date(), Betrag, UserKonto, UserName,
            new Date(), VerwendungsZweck, UserID, -1, -1, -1, -1, -1, -1, rentedAircraft.getAktuellePositionICAO().trim(), -1, -1, -1, -1);
  }

  public void berechneTankfuellung() {

    AbrechnungsFehler = false;

    double PreisAVGAS = config.getPreisAVGASkg();
    double PreisJETA = config.getPreisJETAkg();
    int BestandAVGAS = 0;
    int BestandJETA = 0;

    TankstellenName = "";
    TankstellenGrafik = "http://www.ftw-sim.de/images/FTW/images/tankstelle.png";
    TankstelleSumme = 0.0;

    TankenFreigeben = false;

    if (objektID > 0) {

      TankenFreigeben = true;
      setIstPrivateTankstelleVorhanden(true);

      FboUserObjekte fboobjekt = facadeTakeOff.getUserFBOObjekt(objektID);

      BestandAVGAS = fboobjekt.getBestandAVGASkg();
      BestandJETA = fboobjekt.getBestandJETAkg();

      //1 = AVGAS, 2 = JETA
      if (TankstelleTreibstoffArt == 1 && BestandAVGAS <= 0) {
        TankenFreigeben = false;
      } else if (TankstelleTreibstoffArt == 2 && BestandJETA <= 0) {
        TankenFreigeben = false;
      }

      PreisAVGAS = fboobjekt.getPreisAVGAS();
      PreisJETA = fboobjekt.getPreisJETA();

      TankstellenName = fboobjekt.getName();
      TankstellenGrafik = fboobjekt.getGrafikLink();

      if (fboobjekt.getGrafikLink() != null) {
        if (TankstellenGrafik.equals("")) {
          TankstellenGrafik = "http://www.ftw-sim.de/images/FTW/images/tankstelle.png";
        }
      } else {
        TankstellenGrafik = "http://www.ftw-sim.de/images/FTW/images/tankstelle.png";
      }

      if (getTankstelleTreibstoffArt() == 1) {
        if (TankMengeKilo > fboobjekt.getBestandAVGASkg()) {
          setTankMengeKilo(fboobjekt.getBestandAVGASkg());
        }
      }

      if (getTankstelleTreibstoffArt() == 2) {
        if (TankMengeKilo > fboobjekt.getBestandJETAkg()) {
          setTankMengeKilo(fboobjekt.getBestandJETAkg());
        }
      }
      if (getTankstelleTreibstoffArt() == 1) {
        setTankstelleSumme((double) TankMengeKilo * PreisAVGAS);
        setTankstelleMengeLbs((int) getKG2Libs(TankMengeKilo));
      } else {
        setTankstelleSumme((double) TankMengeKilo * PreisJETA);
        setTankstelleMengeLbs((int) getKG2Libs(TankMengeKilo));
      }
    }

    // FTW - Tankstelle
    if (objektID == -1) {
      TankenFreigeben = true;
      if (getTankstelleTreibstoffArt() == 1) {
        setTankstelleSumme((double) TankMengeKilo * PreisAVGAS);
        setTankstelleMengeLbs((int) getKG2Libs(TankMengeKilo));
      } else {
        setTankstelleSumme((double) TankMengeKilo * PreisJETA);
        setTankstelleMengeLbs((int) getKG2Libs(TankMengeKilo));
      }

    }

  }

  public void ontankenStart() {
    EnttankenLaeuft = false;
    onResetTankstelle();
    berechneTankfuellung();
  }

  public List<ViewFBOUserObjekte> getTankstellen() {
    Tankstellen = facadeTakeOff.getTankstellen(Abflugflughafen);
    if (Tankstellen.isEmpty()) {
      setIstPrivateTankstelleVorhanden(false);
    } else {
      setIstPrivateTankstelleVorhanden(onTankMengenPruefen());
    }

    try {
      if (facadeTakeOff.getAirportByIcao(Abflugflughafen).getLuftversorgung()) {
        setIstPrivateTankstelleVorhanden(true);
      }
    } catch (NullPointerException e) {
    }

    return Tankstellen;
  }

  public void onTankenBerechneProzentTankfüllung() {
    if (rentedAircraft != null) {
      int inhalt = rentedAircraft.getAktuelleTankfuellung();
      TankBisFuellstandKG = (int) (maxTankkapazitaet / 100.0 * TankMengeProzent);

      TankMengeKilo = TankBisFuellstandKG - inhalt;

      onTankenBerechneKilo();
    }
  }

  public void onTankenBerechneKilo() {
    if (rentedAircraft != null) {
      int inhalt = rentedAircraft.getAktuelleTankfuellung();

      inhalt = inhalt + TankMengeKilo;

      if (inhalt > maxTankkapazitaet) {
        inhalt = maxTankkapazitaet;
      }

      if (inhalt < 0) {
        inhalt = 0;
      }

      TankBisFuellstandKG = inhalt;

      onTankenBerechneBisFüllstandKg();
    }
  }

  public void onTankenBerechneBisFüllstandKg() {
    if (rentedAircraft != null) {
      int inhalt = rentedAircraft.getAktuelleTankfuellung();
      boolean tanken = true;

      if (TankBisFuellstandKG < 0) {
        tanken = false;
        NewMessage("Füllstand muss größer 0 sein");
        onResetTankstelle();

      }

      if (tanken) {
        if (TankBisFuellstandKG >= inhalt) {
          TankMengeKilo = TankBisFuellstandKG - inhalt;
        } else {
          TankMengeKilo = inhalt - TankBisFuellstandKG;
          TankMengeKilo = TankMengeKilo - (TankMengeKilo * 2);

          if (TankMengeKilo + inhalt < 0) {
            TankMengeKilo = TankMengeKilo - (inhalt * 2);
          }

        }

        if (inhalt + TankMengeKilo > maxTankkapazitaet) {
          TankMengeKilo = maxTankkapazitaet - inhalt;
          TankBisFuellstandKG = maxTankkapazitaet;
        }

        TankMengeProzent = 100.0 / maxTankkapazitaet * TankBisFuellstandKG;

        //******************************* Summenbildung
        berechneTankfuellung();
      }
    }
  }

  /**
   * Liefert true zurueck wenn private Tankstellen von beiden Sorten Sprit haben <br>
   * False entweder keine Tankstelle vorhanden oder eine Spritsorte ist 0
   *
   * @param keiner
   */
  private boolean onTankMengenPruefen() {
    @SuppressWarnings("unchecked")
    List<Object[]> Bestaende = facadeTakeOff.getTankstellenBestaende(Abflugflughafen);

    if (Bestaende != null) {
      try {
        if ((double) Bestaende.get(0)[0] > 0 && (double) Bestaende.get(0)[1] > 0) {
          return true;
        }
      } catch (NullPointerException e) {
        return false;
      }
    }

    return false;
  }

  /*
  *******************************************************  Tanken Ende
   */
 /*
  ****************************************************** Flug starten und beenden ******** Beginn
   */
  public void onStartTheFlight() {
    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.onStartTheFlight() BEGINN");
    /*
    @todo 
     */
    boolean isFlight = true;
    int idAirline = 0;

    AbrechnungsFehlerText = "";
    AbrechnungsFehler = false;
    isAcasFlightExist = false;
    ArrivalTerminalAbrechnung = -300;
    DepartureTerminalAbrechnung = -300;
    pinGesendet = false;
    flugFortsetzung = false;

    AbrechnungLaeuft = false;

    // gibt es Aufträge zum Fliegen dann nimm den ersten Eintrag fuer Destination in ACARS
    if (!assignmentList.isEmpty()) {
      DestinationICAO = readAirportData(assignmentList.get(0).getToIcao()).getIcao().trim();
    } else {
      DestinationICAO = "";
    }

    ZielFlughafenICAO = DestinationICAO;

    // ist ein Flugzeug gemietet
    // unbedingt nochmal das Flugzeug neu Laden
    rentedAircraft = getMeinGemietetesFlugzeug();

    if (rentedAircraft != null) {
      isFlight = true;
    } else {
      AbrechnungsFehler = true;
      AbrechnungsFehlerText = loginMB.onSprache("Terminal_msg_duHastKeinFlugzeugGemietet");
      isFlight = false;
    }

    // Ist ein Charter-Auftrag vorhanden
    if (istEsEinCharterAuftrag()) {
      if (PruefeObCharterAuftragOK()) {
        isFlight = true;
      } else {
        isFlight = false;
        AbrechnungsFehler = true;
        AbrechnungsFehlerText = "Die Bedingungen für einen Charter-Auftrag sind nicht erfüllt.";
      }

    }

    if (!isLizenzOK()) {
      isFlight = false;
    }

    // Nur Aufträge für die Fluggesellschaft erlaubt
    if (isFlight) {
      if (rentedAircraft.getNurFuerAuftraegeDerFluggesellschaft()) {
        if (!onNurJobsFuerFG()) {
          isFlight = false;
          AbrechnungsFehler = true;
          AbrechnungsFehlerText = loginMB.onSprache("Terminal_msg_MitDiesemFlugzeugNurFGJob");
        }
      }
    }

    // ist das Flugzeug überladen
    if (isFlight) {
      if ((getGewichtCargoTakeOff() + getGewichtGepaeckTakeOff() + getGewichtPassagiereTakeOff()) > getMaxPayload()) {
        isFlight = false;
        AbrechnungsFehler = true;
        AbrechnungsFehlerText = loginMB.onSprache("Terminal_msg_duHastDeinFlugzeugUeberladenPayload");
      }

    }

    if (isFlight) {
      if (getTakeOffWeight() <= rentedAircraft.getHoechstAbfluggewicht()) {
        isFlight = true;
      } else {
        isFlight = false;
        AbrechnungsFehler = true;
        AbrechnungsFehlerText = loginMB.onSprache("Terminal_msg_duHastDeinFlugzeugUeberladenMaximalGewicht");
      }
    }

    // Wurde das Cargo Ueberschritten
    if (isFlight) {
      if (getGewichtCargoTakeOff() + getGewichtGepaeckTakeOff() > CargoVerfuegbar) {
        isFlight = false;
        AbrechnungsFehler = true;
        AbrechnungsFehlerText = loginMB.onSprache("Terminal_msg_duHastDeinFlugzeugUeberladenCargo");
      }
    }

    // hat das Flugzeug Sprit im Tank
    if (isFlight) {
      //if (TankstelleSpritMenge <= 0 && rentedAircraft.getAktuelleTankfuellung() <= 0) {
      if (rentedAircraft.getAktuelleTankfuellung() <= 0) {
        AbrechnungsFehler = true;
        AbrechnungsFehlerText = loginMB.onSprache("Terminal_msg_duHastNochKeinSpritImFlugzeug");
        isFlight = false;
      }
    }

    // Ist der Zustand des Flugzeuges OK
    // Darf losfliegen jedoch ohne Pax und Cargo
    // Flugzeug muss entleert werden.
    if (isFlight) {
      if (rentedAircraft.getZustand() <= 95.00) {
        if (assignmentList.size() > 0) {
          AbrechnungsFehler = true;
          AbrechnungsFehlerText = loginMB.onSprache("Terminal_msg_Zustand95Prozent");
          isFlight = false;
        }
      }

      if (rentedAircraft.getZustand() <= 90.00) {
        if (assignmentList.size() > 0) {
          AbrechnungsFehler = true;
          AbrechnungsFehlerText = loginMB.onSprache("Terminal_msg_Zustand90Prozent");
          isFlight = false;
        }
      }

      //********************************** C-Check prüfen
      if (CCheckFaellig()) {
        isFlight = false;
        AbrechnungsFehler = true;
        AbrechnungsFehlerText = loginMB.onSprache("Terminal_msg_CCheckIstFaellig") + rentedAircraft.getType();
      }

      // Passen alle Passagiere in das Flugzeug für Charter-Aufträge deaktivieren
      if (isFlight && !istEsEinCharterAuftrag()) {
        //if (getAnzahlPassengersEconomyTakeOff() <= getSitzeEconomy() && getAnzahlPassengersBuisnessTakeOff() <= getSitzeBusiness()) {

        // Neue Funktion, BC-Sitze mit ECO's besetzen
        if (getAnzahlPassengersEconomyTakeOff() <= (maxBelegbareSitze - getAnzahlPassengersBuisnessTakeOff()) && getAnzahlPassengersBuisnessTakeOff() <= getSitzeBusiness()) {

        } else {
          isFlight = false;
          AbrechnungsFehler = true;
          AbrechnungsFehlerText = loginMB.onSprache("Terminal_msg_duHastZuWenigSitzeInDeinemFlugzeug") + rentedAircraft.getType();
        }
      }

      if (isFlight) {
        /*
            gibt es einen aktiven Flug in YAACARS
        Prüfung ob es evtl. auch eine Mission- oer Rettungsflug gibt
         */
        if (isFlight) {
          if (facadeTakeOff.isYAACARSFlightActive(UserID)) {
            flugFortsetzung = true;
            isAcasFlightExist = true;
          }
        }

        // ist Zielflughafen = Abflughafen
        if (isFlight && !flugFortsetzung) {
          if (rentedAircraft.getAktuellePositionICAO().equals(DestinationICAO.trim())) {
            setAbrechnungsFehler(true);
            setAbrechnungsFehlerText(loginMB.onSprache("Terminal_msg_ZielUndAbflugSindGleich"));
            isFlight = false;
          }
        }

        // Flughafen Gewicht pruefen
//        if (isFlight) {
//          if (!DestinationICAO.equals("")) {
//            if (rentedAircraft.getMaxLandegewicht() > readAirportData(DestinationICAO).getMaxlandegewicht()) {
//              ReportSchreiben("Airport Gewicht: " + readAirportData(DestinationICAO).getMaxlandegewicht());
//              ReportSchreiben("Flugzeug MTOW: " + rentedAircraft.getMaxLandegewicht());
//              setAbrechnungsFehler(true);
//              setAbrechnungsFehlerText("INFO, Mit diesem Flugzeug darfst du in Zukunft nicht an diesem Flughafen landen!");
//            }
//          }
//        }
        if (isFlight) {

          IDBank = -200;
          IDFluggesellschaft = -200;
          IDFlughafenbetreiber = -200;
          TerminalFreigeben = false;

          FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Flight-ON", "true");

          try {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FlightLogo", getFlugLogo());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FlugzeugType", rentedAircraft.getType());
          } catch (NullPointerException e) {
          }

          setFlightTime(0);

          // Transport paxe = getAnzahlPassengersEconomyTakeOff + getAnzahlPassengersBuisnessTakeOff
          TransportPaxe = AnzahlTransportierterPassgiereBerechnen();
          setTransportPaxe(TransportPaxe);

          CargoAndPassagiereMenge();
          onAssignmentOfHold();

          setSicherheitsgebuehr(0.0);
          setLandegebuehr(0.0);
          setStartgebuehr(0.0);
          setBuchungsgebuehr(0.0);
          setTreibstoffkosten(0.0);
          setMietgebuehrflugzeug(0.0);
          setCrewgebuehren(0.0);
          setBodenpersonalgebuehren(0.0);
          setSummekosten(0.0);
          setSummeabrechnung(0.0);
          setBonus(0.0);
          setBonusProzent(0.0);
          setSummetakeoff_display(0.0);

          setGewinnPilot(0.0);
          setGewinnAirline(0.0);
          setWertProvision(0.0);

          setArriavalGebuehren(0.0);
          setDepartureGebuehren(0.0);


          /*
            YAACARS Flugdaten eintragen
           */
          // Flugnummer erzeugen
          int fgmax = 9999;
          int fgnr;

          Yaacarskopf yaacarsflight = new Yaacarskopf();

          Airport airport = facadeTakeOff.getAirportByIcao(rentedAircraft.getAktuellePositionICAO());

          yaacarsflight.setDepartureicao(rentedAircraft.getAktuellePositionICAO());

          yaacarsflight.setDeparturelatitude(airport.getLatitude());
          yaacarsflight.setDeparturelongitude(airport.getLongitude());

          yaacarsflight.setLetztepositionlatitude(airport.getLatitude());
          yaacarsflight.setLetztepositionlongitude(airport.getLongitude());

          airport = facadeTakeOff.getAirportByIcao(DestinationICAO);

          // Bei Leerflug gibt es keine Destination
          try {

            yaacarsflight.setAlternateicao(DestinationICAO);

            yaacarsflight.setAlternatelatitude(airport.getLatitude());
            yaacarsflight.setAlternatelongitude(airport.getLongitude());

            yaacarsflight.setArrivalicao(DestinationICAO);

            yaacarsflight.setArrivallatitude(airport.getLatitude());
            yaacarsflight.setArrivallongitude(airport.getLongitude());

          } catch (NullPointerException e) {

            yaacarsflight.setAlternatelatitude(0.0);
            yaacarsflight.setAlternatelongitude(0.0);

            yaacarsflight.setArrivalicao(DestinationICAO);

            yaacarsflight.setArrivallatitude(0.0);
            yaacarsflight.setArrivallongitude(0.0);

          }

          yaacarsflight.setFlugerstelltam(new Date());

          // Airlinjob, IcaoCode der Fluggesellschaft in der Flugnummer verarbeiten
          if (!onGemischteJobs() && !assignmentList.isEmpty()) {
            do {
              fgnr = FlightNumber.nextInt(fgmax);
            } while (facadeTakeOff.isFlightNummerExist(assignmentList.get(0).getIcaoCodeFluggesellschaft() + flight.format(fgnr)));

            yaacarsflight.setFlugnummer(assignmentList.get(0).getIcaoCodeFluggesellschaft().toUpperCase() + flight.format(fgnr));
            ReportSchreiben("Airline Flug Nr.: " + yaacarsflight.getFlugnummer());
          } else {
            do {
              fgnr = FlightNumber.nextInt(fgmax);
            } while (facadeTakeOff.isFlightNummerExist("FTW" + flight.format(fgnr)));
            yaacarsflight.setFlugnummer("FTW" + flight.format(fgnr));
            ReportSchreiben("FTW Flug Nr.: " + yaacarsflight.getFlugnummer());
          }

          yaacarsflight.setMissionsart(1);

          // Charter Auftrag, C-A in der Flugnummer verarbeiten          
          if (istEsEinCharterAuftrag()) {
            do {
              fgnr = FlightNumber.nextInt(fgmax);
            } while (facadeTakeOff.isFlightNummerExist("CA-" + flight.format(fgnr)));
            yaacarsflight.setFlugnummer("CA-" + flight.format(fgnr));
            ReportSchreiben("Charter Auftrag Flug Nr.: " + yaacarsflight.getFlugnummer());
            yaacarsflight.setMissionsart(2);
          }

          yaacarsflight.setFlugroute(rentedAircraft.getAktuellePositionICAO() + " " + DestinationICAO);
          ReportSchreiben("Flugroute: " + yaacarsflight.getFlugroute());

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
          yaacarsflight.setFlugstatus(0);

          yaacarsflight.setMissionsid(-1);

          yaacarsflight.setFlugzeugid(rentedAircraft.getIdMietKauf());
          yaacarsflight.setFlugzeugkennung(rentedAircraft.getRegistrierung());
          yaacarsflight.setFlugzeugtype(rentedAircraft.getType());
          yaacarsflight.setGeflogenemeilen(0);
          yaacarsflight.setGeflogenezeit(0);
          yaacarsflight.setBlockzeit(0);

          yaacarsflight.setCargogewicht(getGewichtCargoTakeOff() + getGewichtGepaeckTakeOff());

          yaacarsflight.setPaxanzahl(getAnzahlPassengersBuisnessTakeOff() + getAnzahlPassengersEconomyTakeOff());
          yaacarsflight.setPaxgewicht(getGewichtPassagiereTakeOff());

          yaacarsflight.setProtokollversion("1");
          yaacarsflight.setResttankmengekg(0);
          yaacarsflight.setTankmengekg(rentedAircraft.getAktuelleTankfuellung());
          yaacarsflight.setResttankmengekg(rentedAircraft.getAktuelleTankfuellung());

          Benutzer user = facadeTakeOff.readUser(UserID);
          yaacarsflight.setUserid(UserID);
          yaacarsflight.setUsername(user.getName1());
          yaacarsflight.setUserhash(user.getPasswort());
          yaacarsflight.setUsermessage("");

          yaacarsflight.setVerbrauchtetankmengekg(0);
          yaacarsflight.setFreitext("");

          yaacarsflight.setFlugOK(false);

          ReportSchreiben("Username: " + yaacarsflight.getUsername());

          if (!isAcasFlightExist) {
            facadeTakeOff.createYaacarskopf(yaacarsflight);
            ReportSchreiben("YAACARS Flug erfolgreich in Datenbank geschrieben");
          }

          aircraftUpdate("StartFlug");

          CONF.RandomJob = false;
          isAcasFlightExist = true;
          setFlugGestartet(true);
          setSummetakeoff_display(getWertTakeOff());

        }

      }

    }
    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.onStartTheFlight() ENDE");
    ReportAusgeben();
  }

  public void onEndTheFlight() {
    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.onEndTheFlight() BEGINN");
    isLoaded = false;
    isLoadedAircrafts = false;
    TerminalFreigeben = false;
    PirepOK = false;

    // Prüfung Flugstatus
    //"flugstatus"
    //0 = angelegt
    //1 = gestartet
    //2 = beendet
    Yaacarskopf yaacarsFlight = facadeTakeOff.getYAACARSFlight(UserID);
    if (yaacarsFlight != null) {
      if (yaacarsFlight.getFlugstatus() == 2) {
        //        
        // Prüfung auf gemietetes Flugzeug
        // unbedingt nochmal das Flugzeug neu Laden
        rentedAircraft = getMeinGemietetesFlugzeug();

        if (rentedAircraft != null) {

          onZielFlughafenSuchen();

          if (!ZielFlughafenICAO.equals("")) {
            TerminalFreigeben = true;
            PirepOK = true;
            FlugKostenBerechnung();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Flight-ON", "false");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FlightLogo", "");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FlugzeugType", "");

          } else {
            TerminalFreigeben = false;
            PirepOK = false;
            setAbrechnungsFehler(true);
            setAbrechnungsFehlerText(loginMB.onSprache("Terminal_msg_zielFlughafenNichtGefundenFlugAbbrechen"));
            ReportSchreiben(AbrechnungsFehlerText);
          }
        } else {
          PirepOK = false;
          TerminalFreigeben = false;
          setAbrechnungsFehler(true);
          setAbrechnungsFehlerText("Kein Flugzeug gemietet. No rented aircraft");
          ReportSchreiben(AbrechnungsFehlerText);
        }
      } else {
        PirepOK = false;
        TerminalFreigeben = false;
        setAbrechnungsFehler(true);
        setAbrechnungsFehlerText("Flug noch nicht in YAACARS beendet");
        ReportSchreiben(AbrechnungsFehlerText);
      }
    } else {
      PirepOK = false;
      setAbrechnungsFehler(true);
      TerminalFreigeben = false;
      setAbrechnungsFehlerText("No YAACARS Flight found");
      ReportSchreiben(AbrechnungsFehlerText);
    }
    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.onEndTheFlight() ENDE");
  }

  public void onAbrechnungErstellen() {

    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.onAbrechnungErstellen() BEGINN");

    if (PirepOK && !AbrechnungLaeuft) {

      if (getAcarsflightinfo() != null) {
        AbrechnungLaeuft = true;
        Yaacarskopf yaacarsFlight = facadeTakeOff.getYAACARSFlight(UserID);
        if (yaacarsFlight != null) {
          ReportSchreiben("UserID: " + yaacarsFlight.getUserid());
          ReportSchreiben("Username: " + yaacarsFlight.getUsername());
          ReportSchreiben("Flugnummer: " + yaacarsFlight.getFlugnummer());
          ReportSchreiben("Route: " + yaacarsFlight.getFlugroute());
          ReportSchreiben("Flugzeug: " + yaacarsFlight.getFlugzeugtype());
          ReportSchreiben("Kennung: " + yaacarsFlight.getFlugzeugkennung());
        }

        createBankbuchung();
        createLogbuchEintrag();
        aircraftUpdate("Abrechnung");
        setSummetakeoff_display(getWertTakeOff());
        onAirportTransfer();
        updateFluggesellschaft();
        updateTyperating();
        userUpdate();
        TreibstoffFaesserVerbuchen();
        deleteFinishAssigments(PirepFromICAO);

        if (yaacarsFlight != null) {
          deleteYAAcarsDaten(yaacarsFlight.getIdyaacarskopf());
          ReportSchreiben("YAACARS Flug wurde gelöscht");
        } else {
          ReportSchreiben("Kein oder mehrere YAACARS-Flüge mit der UserID gefunden");

        }

        setFlugGestartet(false);
        setTankenBezahlt(false);
        setTankstelleSumme(0);
        setTankstelleSpritMenge(0);
        setTankstelleMengeLbs(0);
        Abflugflughafen = ZielFlughafenICAO.trim();
        PirepOK = false;
        TerminalFreigeben = false;
        setAbrechnungsFehler(false);
      } else {
        ReportSchreiben("<<< getAcarsflightinfo() fehlgeschlagen >>> ");
      }
    }
    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.onAbrechnungErstellen() ENDE");
    ReportAusgeben();
  }

  public void onFlugAbbrechenWegenDoppel() {

    if (!AbbruchWegenDoppel) {
      NewMessage("Cancel of the flight running");
      AbbruchWegenDoppel = true;
      assignmentList = facadeTakeOff.findByMyFlight(UserID,StartICAO);
      onRefreshSite();
      onStartTheFlight();
      onCancelFlight();
      AbbruchWegenDoppel = false;
    }
  }

  public void onCancelFlight() {
    ReportSchreiben("------------------------------------ onCancelFlight BEGINN");
    isLoaded = false;
    setFlightTime(0);
    setFlugGestartet(false);
    AbrechnungsFehler = false;
    onAssignmentFromHold();

    Yaacarskopf yaacarsFlight = facadeTakeOff.getYAACARSFlight(UserID);

    if (yaacarsFlight != null) {
      ReportSchreiben("Username: " + yaacarsFlight.getUsername());
      ReportSchreiben("Flugnummer: " + yaacarsFlight.getFlugnummer());
      ReportSchreiben("Route: " + yaacarsFlight.getFlugroute());
      deleteYAAcarsDaten(yaacarsFlight.getIdyaacarskopf());
      ReportSchreiben("YAACARS Flugdaten gelöscht");
    } else {
      ReportSchreiben("Kein oder mehrere YAACARS-Flüge mit der UserID gefunden");
    }
    aircraftUpdate("FlugAbbruch");
    try {
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Flight-ON", "false");
    } catch (NullPointerException e) {
      ReportSchreiben("de.klees.beans.takeoff.takeoffBean.onCancelFlight() Flight-ON false konnte nicht gesetzt werden.");
    }
    ReportSchreiben("------------------------------------ onCancelFlight ENDE");
    ReportAusgeben();
  }

  public void FlugKostenBerechnung() {

    setFlightTime(0);
    Spritverbrauch = 0;
    PirepOK = false;
    boolean leerFlug = false;
    AbrechnungsFehlerText = "";
    AbrechnungsFehler = false;
    int TASDurchschnitt;
    double MaxGeschwindigkeit = 0;
    int FlugzeugSpritPerStunde = 0;
    double GeflogeneMeilen = 0;

    Flugzeuge flugzeug = facadeTakeOff.readFlugzeug(rentedAircraft.getIdFlugzeug());

    if (flugzeug != null) {
      MaxGeschwindigkeit = flugzeug.getHoechstgeschwindigkeitTAS();
      FlugzeugSpritPerStunde = flugzeug.getVerbrauchStunde();
    }

    Airport arrair = getAirportData(ZielFlughafenICAO.trim());
    Airport depair = getAirportData(Abflugflughafen);

    Yaacarskopf acf = facadeTakeOff.YAACARSFlightInfo(UserID);
    String fgNr = "";
    GeflogeneMeilen = acf.getGeflogenemeilen();

    //ermitteln der Abrechnungspositionen
    List<Assignement> abrechnung = facadeTakeOff.getAllAssignmentByZielFlughafen(ZielFlughafenICAO.trim(), UserID);

    if (acf != null) {
      fgNr = acf.getFlugnummer();
      PirepOK = true;

      ErwarteteFlugzeit = 0;

      dgeschw = Double.valueOf(facadeTakeOff.YAACARSDurchschnittsgeschwindigkeit(acf.getIdyaacarskopf()));

      TASDurchschnitt = facadeTakeOff.TASDurchschnittsgeschwindigkeit(acf.getIdyaacarskopf());

      //*************************** Bankdaten
      String UserName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName");
      String UserKonto = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("BankKonto");
      String VerwendungsZweck = "";

      //*************** Pirepdaten auslesen
      PirepFlugNummer = acf.getFlugnummer();
      PirepFromICAO = acf.getDepartureicao();
      PirepToICAO = acf.getArrivalicao();

      setFlightTime(acf.getBlockzeit());

      // ********************************************************** Spritverbrauch prüfen
      // Spritverbrauch ist in KG
      Spritverbrauch = acf.getVerbrauchtetankmengekg();

      if (Spritverbrauch <= 0) {
        PirepOK = false;
        System.out.println("Spritmenge ist <= 0  ");
        //NewMessage(loginMB.onSprache("Terminal_msg_spritmengeIstKleinerGleichNull"));
        AbrechnungsFehler = true;
        AbrechnungsFehlerText = loginMB.onSprache("Terminal_msg_spritmengeIstKleinerGleichNull");
        ReportSchreiben(AbrechnungsFehlerText);

      }

      if ((rentedAircraft.getAktuelleTankfuellung() - Spritverbrauch) <= 0) {
        PirepOK = false;
        AbrechnungsFehler = true;
        AbrechnungsFehlerText = loginMB.onSprache("Terminal_msg_uHastDeinenTankLeergeflogen");
        ReportSchreiben(AbrechnungsFehlerText);
      }

      double VerbrauchProStundeAufFlug = 0.0;

      if (!acf.getFlugOK()) {
        try {
          // Mindestspritverbrauch prüfen
          VerbrauchProStundeAufFlug = (double) Spritverbrauch / (double) (acf.getGeflogenezeit() / 60);

          //weniger verbraucht
          //erst bei Verbrauch von über 50 KG / Stunde prüfen
          if (FlugzeugSpritPerStunde > 50) {

            if ((VerbrauchProStundeAufFlug * 1.15) < FlugzeugSpritPerStunde) {
//            if (!pinGesendet) {
//              saveMail("FTW-System", "Stefan.Klees", "Stefan.Klees", "Spritverbrauch ist nicht stimmig: ", MailNachrichtSprit("", FlugzeugSpritPerStunde, (int) VerbrauchProStundeAufFlug, acf.getPaxanzahl(), acf.getCargogewicht()));
//              saveMail("FTW-System", "Toffi", "Toffi", "Spritverbrauch ist nicht stimmig: ", MailNachrichtSprit("", FlugzeugSpritPerStunde, (int) VerbrauchProStundeAufFlug, acf.getPaxanzahl(), acf.getCargogewicht()));
//
//              saveMail("FTW-System", "chris86", "chris86", "Spritverbrauch ist nicht stimmig: ", MailNachrichtSprit("", FlugzeugSpritPerStunde, (int) VerbrauchProStundeAufFlug, acf.getPaxanzahl(), acf.getCargogewicht()));
//              saveMail("FTW-System", "DerGonzo", "DerGonzo", "Spritverbrauch ist nicht stimmig: ", MailNachrichtSprit("", FlugzeugSpritPerStunde, (int) VerbrauchProStundeAufFlug, acf.getPaxanzahl(), acf.getCargogewicht()));
//              saveMail("FTW-System", "old_Crashpilot", "old_Crashpilot", "Spritverbrauch ist nicht stimmig: ", MailNachrichtSprit("", FlugzeugSpritPerStunde, (int) VerbrauchProStundeAufFlug, acf.getPaxanzahl(), acf.getCargogewicht()));
//              saveMail("FTW-System", "HoroX", "HoroX", "Spritverbrauch ist nicht stimmig: ", MailNachrichtSprit("", FlugzeugSpritPerStunde, (int) VerbrauchProStundeAufFlug, acf.getPaxanzahl(), acf.getCargogewicht()));
//              pinGesendet = true;
//            }

//            AbrechnungsFehler = true;
//            AbrechnungsFehlerText = "Unstimmigkeiten beim Flug festgestellt, Flug kann nicht abgerechnet werden, wende dich an den Support oder im Forum unter: Abrechnung gesperrt.";
              PirepOK = false;

              //********** Automatische Freigabe
              onFlugFreigebenWegenSprit();

            }
          }

        } catch (Exception e) {
          if (!pinGesendet) {
            saveMail("FTW-System", "Stefan.Klees", "Stefan.Klees", "Exception bei Spritverbrauch ist nicht stimmig, evtl. kein Spritverbrauch beim Flugzeug: ", MailNachrichtSprit("", FlugzeugSpritPerStunde, (int) VerbrauchProStundeAufFlug, acf.getPaxanzahl(), acf.getCargogewicht()));
            saveMail("FTW-System", "Toffi", "Toffi", "Exception bei Spritverbrauch ist nicht stimmig, evtl. kein Spritverbrauch beim Flugzeug: ", MailNachrichtSprit("", FlugzeugSpritPerStunde, (int) VerbrauchProStundeAufFlug, acf.getPaxanzahl(), acf.getCargogewicht()));

            saveMail("FTW-System", "chris86", "chris86", "Spritverbrauch ist nicht stimmig: ", MailNachrichtSprit("", FlugzeugSpritPerStunde, (int) VerbrauchProStundeAufFlug, acf.getPaxanzahl(), acf.getCargogewicht()));
            saveMail("FTW-System", "DerGonzo", "DerGonzo", "Spritverbrauch ist nicht stimmig: ", MailNachrichtSprit("", FlugzeugSpritPerStunde, (int) VerbrauchProStundeAufFlug, acf.getPaxanzahl(), acf.getCargogewicht()));
            saveMail("FTW-System", "old_Crashpilot", "old_Crashpilot", "Spritverbrauch ist nicht stimmig: ", MailNachrichtSprit("", FlugzeugSpritPerStunde, (int) VerbrauchProStundeAufFlug, acf.getPaxanzahl(), acf.getCargogewicht()));
            saveMail("FTW-System", "HoroX", "HoroX", "Spritverbrauch ist nicht stimmig: ", MailNachrichtSprit("", FlugzeugSpritPerStunde, (int) VerbrauchProStundeAufFlug, acf.getPaxanzahl(), acf.getCargogewicht()));
            pinGesendet = true;
          }

          AbrechnungsFehler = true;
          AbrechnungsFehlerText = "Unstimmigkeiten beim Flug festgestellt, Flug kann nicht abgerechnet werden, wende dich an den Support oder im Forum unter: Abrechnung gesperrt.";

          PirepOK = false;
        }


        /*
        ********************* Flug für Abrechnung Freigeben
         */
        if (PirepOK) {
          acf.setFlugOK(true);
          facadeTakeOff.saveYAACARSKopf(acf);
        }

      }

      // ********************************************************** Spritverbrauch prüfen ENDE
      //
      if (PirepOK) {

        // ********************************************************** Geflogene Meilen prüfen
        int mil = 0; // direkte Strecke von a nach b in Meilen 
        int direct = 0;

        if (!abrechnung.isEmpty()) {

          for (Assignement pfr : abrechnung) {
            if (pfr.getLocationIcao().equals(Abflugflughafen)) {
              direct = pfr.getDistance();
              // 20 % Abweichung ist zulässig, evtl. später auf 10 % senken
              mil = pfr.getDistance() / 100 * 80;
              ErwarteteMeilen = pfr.getDistance();
            }
          }

          if (currentFlightMiles < mil) {
            PirepOK = false;
            AbrechnungsFehler = true;
            AbrechnungsFehlerText = loginMB.onSprache("Terminal_msg_zuWenigMeilenGeflogen");

            if (!pinGesendet) {
              saveMail("FTW-System", "Stefan.Klees", "Stefan.Klees", "Zu wenig Meilen geflogen (Flug wird nicht gewertet): ", MailNachrichtZuWenigMeilen(""));
              saveMail("FTW-System", "Teddii", "Teddii", "Zu wenig Meilen geflogen (Flug wird nicht gewertet): ", MailNachrichtZuWenigMeilen(""));
              saveMail("FTW-System", "Toffi", "Toffi", "Zu wenig Meilen geflogen (Flug wird nicht gewertet): ", MailNachrichtZuWenigMeilen(""));

              saveMail("FTW-System", "chris86", "chris86", "Zu wenig Meilen geflogen (Flug wird nicht gewertet): ", MailNachrichtZuWenigMeilen(""));
              saveMail("FTW-System", "DerGonzo", "DerGonzo", "Zu wenig Meilen geflogen (Flug wird nicht gewertet): ", MailNachrichtZuWenigMeilen(""));
              saveMail("FTW-System", "old_Crashpilot", "old_Crashpilot", "Zu wenig Meilen geflogen (Flug wird nicht gewertet): ", MailNachrichtZuWenigMeilen(""));

              saveMail("Posteingang", "**** FTW-Support ****", UserName, "Zu wenig Meilen geflogen (Flug wird nicht gewertet): ", MailNachrichtZuWenigMeilen(""));
            }

            ReportSchreiben(AbrechnungsFehlerText);
            ReportSchreiben("Benutzer: " + UserName);
            pinGesendet = true;

          }

          // sporadisch auftretender Bug in ACARS berichtigen, ACARS liefert viel zuviel Meilen.
          //
//          if (currentFlightMiles > (direct * 1.5)) {
//            currentFlightMiles = direct;
//          }
        } else {
          //********************
          // ******************* Entfernung prüfen bei Leerflug
          //********************
          int Distanz[] = CONF.DistanzBerechnung(depair.getLongitude(), depair.getLatitude(), arrair.getLongitude(), arrair.getLatitude());

          mil = Distanz[0] / 100 * 80;
          ErwarteteMeilen = Distanz[0];

          if (currentFlightMiles < mil) {
            PirepOK = false;
            AbrechnungsFehler = true;
            AbrechnungsFehlerText = loginMB.onSprache("Terminal_msg_zuWenigMeilenGeflogen");

            if (!pinGesendet) {
              saveMail("FTW-System", "Stefan.Klees", "Stefan.Klees", "Zu wenig Meilen geflogen (Flug wird nicht gewertet): ", MailNachrichtZuWenigMeilen(""));
              saveMail("FTW-System", "Teddii", "Teddii", "Zu wenig Meilen geflogen (Flug wird nicht gewertet): ", MailNachrichtZuWenigMeilen(""));
              saveMail("FTW-System", "Toffi", "Toffi", "Zu wenig Meilen geflogen (Flug wird nicht gewertet): ", MailNachrichtZuWenigMeilen(""));

              saveMail("FTW-System", "chris86", "chris86", "Zu wenig Meilen geflogen (Flug wird nicht gewertet): ", MailNachrichtZuWenigMeilen(""));
              saveMail("FTW-System", "DerGonzo", "DerGonzo", "Zu wenig Meilen geflogen (Flug wird nicht gewertet): ", MailNachrichtZuWenigMeilen(""));
              saveMail("FTW-System", "old_Crashpilot", "old_Crashpilot", "Zu wenig Meilen geflogen (Flug wird nicht gewertet): ", MailNachrichtZuWenigMeilen(""));

              saveMail("Posteingang", "**** FTW-Support ****", UserName, "Zu wenig Meilen geflogen (Flug wird nicht gewertet): ", MailNachrichtZuWenigMeilen(""));
            }

            ReportSchreiben(AbrechnungsFehlerText);
            ReportSchreiben("Benutzer: " + UserName);
            pinGesendet = true;
          }
        }
        // ********************************************************** Geflogene Meilen prüfen ENDE
        //
        if (PirepOK) {

          //
          //
          // ********************************************************** Geschwindigkeit prüfen BEGINN
          // int dgeschw = facadeTakeOff.AcarsDurchschnittsgeschwindigkeit();
          double ewm = new Double(ErwarteteMeilen);
          double ewt = (ewm / dgeschw) * 60;
          int differenz = 0;

          if (ewt < FlightTime) {
            differenz = FlightTime - (int) ewt;
          }

          ErwarteteFlugzeit = (int) ewt;

          // System.out.println("de.klees.beans.takeoff.takeoffBean.FlugKostenBerechnung() erwartete meilen " + ewm);
          // System.out.println("de.klees.beans.takeoff.takeoffBean.FlugKostenBerechnung() erwartete Flugzeit " + ErwarteteFlugzeit);
          if (differenz > 0) {
            if (differenz > 5) {
              ErwarteteFlugzeit = FlightTime - 5;
            } else {
              ErwarteteFlugzeit = CONF.zufallszahl((int) ewt, FlightTime);
            }
          }

          //************ Prüfung Time-Compression - Fluggeschwindigkeit BEGINN
          if (MaxGeschwindigkeit > 0) {
            double FlugzeitStunden = (double) FlightTime / 60.0;

            //Toleranz hinzugefügt
            if (TASDurchschnitt > MaxGeschwindigkeit) {
              if (!pinGesendet) {
                saveMail("FTW-System", "Stefan.Klees", "Stefan.Klees", "Fluggeschwindigkeit (Time-Compression)", MailNachrichtGeschwindigkeit("", MaxGeschwindigkeit, TASDurchschnitt));
                saveMail("FTW-System", "Toffi", "Toffi", "Fluggeschwindigkeit (Time-Compression)", MailNachrichtGeschwindigkeit("", MaxGeschwindigkeit, TASDurchschnitt));
                saveMail("FTW-System", "Teddii", "Teddii", "Fluggeschwindigkeit (Time-Compression)", MailNachrichtGeschwindigkeit("", MaxGeschwindigkeit, TASDurchschnitt));

                saveMail("FTW-System", "chris86", "chris86", "Fluggeschwindigkeit (Time-Compression)", MailNachrichtGeschwindigkeit("", MaxGeschwindigkeit, TASDurchschnitt));
                saveMail("FTW-System", "DerGonzo", "DerGonzo", "Fluggeschwindigkeit (Time-Compression)", MailNachrichtGeschwindigkeit("", MaxGeschwindigkeit, TASDurchschnitt));
                saveMail("FTW-System", "old_Crashpilot", "old_Crashpilot", "Fluggeschwindigkeit (Time-Compression)", MailNachrichtGeschwindigkeit("", MaxGeschwindigkeit, TASDurchschnitt));

                saveMail("Posteingang", "**** FTW-Support ****", UserName, "Fluggeschwindigkeit (Time-Compression)", MailNachrichtGeschwindigkeit("", MaxGeschwindigkeit, TASDurchschnitt));

                pinGesendet = true;
              }
              PirepOK = false;
              AbrechnungsFehler = true;
              AbrechnungsFehlerText = "Es ist verboten Time-Kompression zu benutzen! - It is forbidden to use time compression!";

              ReportSchreiben(AbrechnungsFehlerText);
              ReportSchreiben("Benutzer: " + UserName);
            }

          }

          //************ Prüfung Time-Compression - Fluggeschwindigkeit ENDE
        }

        // ********************************************************** Geschwindigkeit und  Meilen prüfen ENDE        
      }
      //
      //
      // ********************** Landebahnlänge Prüfen
      // ********************** Landebahnlänge Prüfen ENDE
      // ********************** Landegewicht Prüfen
      // ********************** Landegewicht Prüfen ENDE
    } else {
      PirepOK = false;
      //System.out.println("Kein gültiges Pirep gefunden ");
      AbrechnungsFehler = true;
      AbrechnungsFehlerText = loginMB.onSprache("Terminal_msg_keinPirepGefunden");
      ReportSchreiben(AbrechnungsFehlerText);
      NewMessage(loginMB.onSprache("Terminal_msg_keinPirepGefunden"));
    }

    // Terminalkapazitaet prüfen
    if (PirepOK) {

      if (DepartureTerminalAbrechnung == -300) {
        DepartureKapazitaet = 10000;

      } else {
        //Privates Terminal benutzen
        ViewFBOUserObjekte fboUser = facadeTakeOff.getViewUserFBOObjekt(DepartureTerminalAbrechnung);
        DepartureKapazitaet = fboUser.getTerminalMaxPax();
      }
      if (AnzahlPaxFuerTerminal() > DepartureKapazitaet) {
        AbrechnungsFehler = true;
        AbrechnungsFehlerText = loginMB.onSprache("Terminal_msg_zuKleinesTerminalDeparture");
        PirepOK = false;
      }

      if (ArrivalTerminalAbrechnung == -300) {
        ArrivalKapazitaet = 10000;

      } else {

        //System.out.println("ArrivalID = " + ArrivalTerminalAbrechnung);
        ViewFBOUserObjekte fboUser = facadeTakeOff.getViewUserFBOObjekt(ArrivalTerminalAbrechnung);
        ArrivalKapazitaet = fboUser.getTerminalMaxPax();
      }
      if (AnzahlPaxFuerTerminal() > ArrivalKapazitaet) {
        AbrechnungsFehler = true;
        AbrechnungsFehlerText = loginMB.onSprache("Terminal_msg_zuKleinesTerminalArrival");
        PirepOK = false;
      }
    }
    // Terminalkapazitaet prüfen *********************** ENDE

    // ****************** Auftrag Summen ermitteln
    if (PirepOK) {

      double minuten = (double) getFlightTime();
      double stunden = minuten / 60;
      double TransportWert = 0.0;

      SummeGewicht = getSummeGewichtNachLandung();

      if (!abrechnung.isEmpty()) {

        SummePassengers = 0;
        SummeBuisnessPassengers = 0;
        summeabrechnung = 0.0;
        WertStandardJob = 0.0;
        bonus = 0.0;
        WertProvision = 0.0;
        GewinnPilot = 0.0;
        GewinnAirline = 0.0;
        AirlineJob = false;
        BoniBonusSystem = 0.0;
        BoniBonusSystemWert = 0.0;

        TransportWert = 0.0;

        boolean gemischterJob = onGemischteJobs();

        if (!gemischterJob) {
          // Fluggesellschaft und Piloten ermitteln
          airlinePilot = facadeTakeOff.readFluggesellschaftPilot(abrechnung.get(0).getIdAirline(), UserID);
          airline = facadeTakeOff.readFluggesellschaft(abrechnung.get(0).getIdAirline());

          if (airlinePilot != null) {
            BoniBonusSystem = BonusUeberBonussystemDerFluggesellschaft(airlinePilot.getIdflugesellschaft());
          } else {
            // Hinzugefügt am 19.05.2019
            // http://www.ftw-sim.de/forum/viewtopic.php?p=12755#p12755
            // Wenn kein Pilot der Airline dann ist es wieder ein gemischterJob
            gemischterJob = true;
          }

        }

        //
        // Pseudo ArlineJob, Job aus der Jobboerse
        //
        if (onJobAusJobboerse()) {
          gemischterJob = false;
        }

        for (int i = 0; i < abrechnung.size(); i++) {

          // Routenart 1 = Passagiere
          if (abrechnung.get(i).getRoutenArt() == 1) {
            SummePassengers = SummePassengers + abrechnung.get(i).getAmmount();
            if (abrechnung.get(i).getFlugrouteName().equals("Standard-Job") || gemischterJob) {
              WertStandardJob = WertStandardJob + abrechnung.get(i).getPay();
            } else {
              TransportWert = TransportWert + abrechnung.get(i).getPay();

              // evtl. Bonus ausrechnen
              if (abrechnung.get(i).getBonusoeffentlich() > 0) {
                bonus = bonus + (abrechnung.get(i).getPay() / 100) * abrechnung.get(i).getBonusoeffentlich();
              }
              // Bonus der Fluggesellschaft (Gewinnbeteiligung)
              // Bonus aus den Jobeinstellungen
              if (airlinePilot != null || onJobAusJobboerse()) {
                AirlineJob = true;
                if (abrechnung.get(i).getBonusclosed() > 0) {
                  bonus = bonus + (abrechnung.get(i).getPay() / 100) * abrechnung.get(i).getBonusclosed();
                }
                if (airlinePilot != null) {
                  if (airlinePilot.getBonus() > 0) {
                    bonus = bonus + (abrechnung.get(i).getPay() / 100) * airlinePilot.getBonus();
                  }
                }
                // Bonus nach neuem Bonussystem
                if (BoniBonusSystem > 0.0) {
                  BoniBonusSystemWert = BoniBonusSystemWert + (abrechnung.get(i).getPay() / 100) * BoniBonusSystem;
                }
              }
            }
          }

          // Routenart 4 = Business Passagiere
          if (abrechnung.get(i).getRoutenArt() == 4) {
            SummeBuisnessPassengers = SummeBuisnessPassengers + abrechnung.get(i).getAmmount();
            if (abrechnung.get(i).getFlugrouteName().equals("Standard-Job") || gemischterJob) {
              WertStandardJob = WertStandardJob + abrechnung.get(i).getPay();
            } else {
              TransportWert = TransportWert + abrechnung.get(i).getPay();
              // evtl. Bonus ausrechnen
              if (abrechnung.get(i).getBonusoeffentlich() > 0) {
                bonus = bonus + (abrechnung.get(i).getPay() / 100) * abrechnung.get(i).getBonusoeffentlich();
              }
              // Bonus der Fluggesellschaft (Gewinnbeteiligung)
              // Bonus aus den Jobeinstellungen
              if (airlinePilot != null || onJobAusJobboerse()) {
                AirlineJob = true;
                if (abrechnung.get(i).getBonusclosed() > 0) {
                  bonus = bonus + (abrechnung.get(i).getPay() / 100) * abrechnung.get(i).getBonusclosed();
                }
                if (airlinePilot != null) {
                  if (airlinePilot.getBonus() > 0) {
                    bonus = bonus + (abrechnung.get(i).getPay() / 100) * airlinePilot.getBonus();
                  }
                }
                // Bonus nach neuem Bonussystem
                if (BoniBonusSystem > 0.0) {
                  BoniBonusSystemWert = BoniBonusSystemWert + (abrechnung.get(i).getPay() / 100) * BoniBonusSystem;
                }
              }
            }
          }

          // Routenart 2 = Cargo
          if (abrechnung.get(i).getRoutenArt() == 2) {

            if (abrechnung.get(i).getFlugrouteName().equals("Standard-Job") || gemischterJob) {
              WertStandardJob = WertStandardJob + abrechnung.get(i).getPay();
            } else {
              TransportWert = TransportWert + abrechnung.get(i).getPay();
              // evtl. Bonus ausrechnen
              if (abrechnung.get(i).getBonusoeffentlich() > 0) {
                bonus = bonus + (abrechnung.get(i).getPay() / 100) * abrechnung.get(i).getBonusoeffentlich();
              }
              // Bonus der Fluggesellschaft (Gewinnbeteiligung)
              // Bonus aus den Jobeinstellungen
              if (airlinePilot != null || onJobAusJobboerse()) {
                AirlineJob = true;
                if (abrechnung.get(i).getBonusclosed() > 0) {
                  bonus = bonus + (abrechnung.get(i).getPay() / 100) * abrechnung.get(i).getBonusclosed();
                }
                if (airlinePilot != null) {
                  if (airlinePilot.getBonus() > 0) {
                    bonus = bonus + (abrechnung.get(i).getPay() / 100) * airlinePilot.getBonus();
                  }
                }
                // Bonus nach neuem Bonussystem
                if (BoniBonusSystem > 0.0) {
                  BoniBonusSystemWert = BoniBonusSystemWert + (abrechnung.get(i).getPay() / 100) * BoniBonusSystem;
                }
              }
            }
          }
        }

        setLandegebuehr(SummeGewicht * config.getAbrLandegebuehr());

        //
        // ********* Wert aus Sitzkonfiguration Faktor berücksichtigen
        //
        bonus = (bonus + BoniBonusSystemWert) * SitzKonfigFaktor;
        TransportWert = TransportWert * SitzKonfigFaktor;
        WertStandardJob = WertStandardJob * SitzKonfigFaktor;

        setBuchungsgebuehr(TransportWert + WertStandardJob);
        setMietgebuehrflugzeug(rentedAircraft.getMietpreisTrocken() * stunden);


        /* 
          **************************************************************
          ******************** Provision auslesen *********************
          **************************************************************
         */
        List<ViewFlugProvisionen> prov = facadeTakeOff.getProvisionen(UserID, ZielFlughafenICAO.trim());

        for (ViewFlugProvisionen provision : prov) {
          WertProvision = WertProvision + provision.getProvision();
        }

//        //
//        // ********* Wert aus Sitzkonfiguration Faktor berücksichtigen
//        //
//        WertProvision = WertProvision * SitzKonfigFaktor;
        if (rentedAircraft.getBesatzung() > 0) {
          setCrewgebuehren((SitzeCrew * config.getAbrCrewgebuehren() * stunden) + (SitzeFlugbegleiter * config.getAbrFlugbegleiter() * stunden));
        } else {
          setCrewgebuehren(0.0);
        }

        setSummekosten(getLandegebuehr() + getStartgebuehr() + getMietgebuehrflugzeug() + getCrewgebuehren() + getBodenpersonalgebuehren());

        setSummeabrechnung(TransportWert + WertStandardJob);

        // **************************** Neu Hinzugefügt 15.04.2017
        if (WertStandardJob == 0) {
          AirlineJob = true;
          // **************************** Neu Hinzugefügt 16.02.2019
          // Wenn mehrere Airlines dann gemischter Job, 
          // es gibt ein Problem wenn für 2 oder mehr Airlines geflogen wird
          // muss hier wieder auf gemischter Job umgeschaltet werden.
          if (onGemischteJobs()) {
            AirlineJob = false;
          }
        }
        
        System.out.println("de.klees.beans.takeoff.newTakeoffBean.FlugKostenBerechnung() " + WertStandardJob);
        System.out.println("de.klees.beans.takeoff.newTakeoffBean.FlugKostenBerechnung() "  + AirlineJob);        
        
        
        // *******************************************************
        if (AirlineJob) {
          setWertProvision(0.0);
          setGewinnAirline(getSummeabrechnung());
          setGewinnPilot(bonus);

          // Treibstoffkosten Erstattung an Piloten berechnen *** Neu Hinzugefuegt am 19/01/2019
          SpritPreis = rentedAircraft.getLetzterSpritPreis();
          setTreibstoffkosten(SpritPreis * Spritverbrauch);

        } else {
          setGewinnPilot(getSummeabrechnung());

        }

      } else {

        /*
          ************************* Keine Aufträge zum Abrechnen gefunden, evtl. nur Flugzeugüberführung
          ************************* nur Kosten werden ermittelt
         */
        leerFlug = true;

        setLandegebuehr(SummeGewicht * config.getAbrLandegebuehr());
        setMietgebuehrflugzeug(rentedAircraft.getMietpreisTrocken() * stunden);

        if (rentedAircraft.getBesatzung() > 0) {
          setCrewgebuehren(rentedAircraft.getBesatzung() * config.getAbrCrewgebuehren() * stunden);
        } else {
          setCrewgebuehren(0.0);
        }

        setSummekosten(getLandegebuehr() + getStartgebuehr() + getMietgebuehrflugzeug() + getCrewgebuehren() + getBodenpersonalgebuehren());
        setSummeabrechnung((0 - getSummekosten()));

      }

      double faktor = 1;

      faktor = LandeStartFaktor(depair.getKlasse());
      setStartgebuehr(((SummeGewicht + Spritverbrauch) * config.getAbrLandegebuehr()) * faktor);

      faktor = LandeStartFaktor(arrair.getKlasse());
      setLandegebuehr((SummeGewicht * config.getAbrLandegebuehr()) * faktor);

      faktor = LandeStartFaktor(arrair.getKlasse());
      setBodenpersonalgebuehren(((TransportWert + WertStandardJob) * config.getAbrBodenpersonalgebuehr()) * faktor);

      double betrag = 0.0;
      int Klasse = 0;

      if (DepartureTerminalAbrechnung == -300) {
        setDepartureGebuehren((TransportWert + WertStandardJob) / 100 * 5.0);

        // Sonderregelung bei Klasse 7 und 8 Flughäfen
        Klasse = getAirportData(Abflugflughafen).getKlasse();
        if (Klasse == 7) {
          setDepartureGebuehren((TransportWert + WertStandardJob) / 100 * 1.0);
        } else if (Klasse == 8) {
          setDepartureGebuehren((TransportWert + WertStandardJob) / 100 * 0.5);
        }

      } else {
        FboUserObjekte fboUser = facadeTakeOff.getUserFBOObjekt(DepartureTerminalAbrechnung);
        if (fboUser != null) {
          betrag = (TransportWert + WertStandardJob) / 100 * fboUser.getTerminalGebuehrInProzent();
          setDepartureGebuehren(Math.rint(betrag * 100.0) / 100.0);
        } else {
          // Terminal evtl. aus den Fbo Objekten gelöscht
          setDepartureGebuehren((TransportWert + WertStandardJob) / 100 * 5.0);
        }
      }

      if (ArrivalTerminalAbrechnung == -300) {
        setArriavalGebuehren((TransportWert + WertStandardJob) / 100 * 5.0);

        // Sonderregelung bei Klasse 7 und 8 Flughäfen
        Klasse = getAirportData(ZielFlughafenICAO).getKlasse();
        if (Klasse == 7) {
          setArriavalGebuehren((TransportWert + WertStandardJob) / 100 * 1.0);
        } else if (Klasse == 8) {
          setArriavalGebuehren((TransportWert + WertStandardJob) / 100 * 0.5);
        }

      } else {
        FboUserObjekte fboUser = facadeTakeOff.getUserFBOObjekt(ArrivalTerminalAbrechnung);
        if (fboUser != null) {
          betrag = (TransportWert + WertStandardJob) / 100 * fboUser.getTerminalGebuehrInProzent();
          setArriavalGebuehren(Math.rint(betrag * 100.0) / 100.0);
        } else {
          // Terminal evtl. aus den Fbo Objekten gelöscht
          // Standard-Terminal benutzen
          setArriavalGebuehren((TransportWert + WertStandardJob) / 100 * 5.0);
        }
      }

      if (leerFlug) {
        setDepartureGebuehren(0.0);
        setArriavalGebuehren(0.0);
      }

      setBodenpersonalgebuehren(getDepartureGebuehren() + getArriavalGebuehren());

      setSummekosten(getLandegebuehr() + getStartgebuehr() + getMietgebuehrflugzeug() + getCrewgebuehren() + getBodenpersonalgebuehren());

    }
  }

  public String getFlugLogo() {

    try {
      if (rentedAircraft.getFlugzeugArt().equals("Hubschrauber")) {
        return " http://www.ftw-sim.de/images/FTW/helis/plane_" + 1 + ".png";
      }

      if (rentedAircraft.getFlugzeugArt().equals("Geschäftsflugzeug")) {
        return " http://www.ftw-sim.de/images/FTW/bc/plane_" + 1 + ".png";
      }

      if (rentedAircraft.getLizenz().equals("MPL")) {
        return " http://www.ftw-sim.de/images/FTW/jets/plane_" + 1 + ".png";
      }

      if (rentedAircraft.getLizenz().equals("ATPL")) {
        return " http://www.ftw-sim.de/images/FTW/heavy/plane_" + 1 + ".png";
      }

      if (rentedAircraft.getLizenz().equals("CPL") || rentedAircraft.getLizenz().equals("PPL-A")) {
        return " http://www.ftw-sim.de/images/FTW/kleinflugzeug/plane_" + 1 + ".png";
      }

    } catch (NullPointerException e) {
      return " http://www.ftw-sim.de/images/FTW/planes/plane_" + 1 + ".png";
    }

    return "";
  }

  private double LandeStartFaktor(int Flughafenklasse) {

    double faktor = 0.0;

    switch (Flughafenklasse) {
      case 1:
        faktor = 1.3;
        break;
      case 2:
        faktor = 1.2;
        break;
      case 3:
        faktor = 1.1;
        break;
      case 4:
        faktor = 1;
        break;
      case 5:
        faktor = 0.8;
        break;
      case 6:
        faktor = 0.7;
        break;
      case 7:
        faktor = 0.15;
        break;
      case 8:
        faktor = 0.10;
        break;
      case 9:
        faktor = 1.10;
        break;
      case 10:
        faktor = 0.5;
        break;
      case 11:
        faktor = 0.5;
        break;
      default:
        break;
    }

    return faktor;
  }

  private void createLogbuchEintrag() {
    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.createLogbuchEintrag() BEGINN");
    SummeCargo = GewichtTransportiertesCargo();
    TransportPaxe = AnzahlTransportierterPassgiereBerechnen();

    Yaacarskopf yaacarsFlight = getAcarsflightinfo();

    Fluglogbuch newLog = new Fluglogbuch();
    newLog.setAcarsFlugNummer(yaacarsFlight.getFlugnummer());
    newLog.setBodenpersonalgebuehr(getBodenpersonalgebuehren());
    newLog.setBonus1(getBonus());
    newLog.setBonus2(0.0);
    newLog.setBuchungsgebuehr(getBuchungsgebuehr());
    newLog.setCargo(SummeCargo);
    newLog.setCrewgebuehren(getCrewgebuehren());
    newLog.setFlugDatum(new Date());
    newLog.setFromicao(Abflugflughafen.toUpperCase());
    newLog.setToicao(ZielFlughafenICAO.toUpperCase().trim());
    newLog.setIdAirline(AirlineID);
    newLog.setIdaircraft(rentedAircraft.getIdMietKauf());
    newLog.setIduser(UserID);
    newLog.setLandegebuehr(getLandegebuehr());
    newLog.setStartgebuehr(getStartgebuehr());
    newLog.setMietgebuehr(getMietgebuehrflugzeug());
    newLog.setMiles(currentFlightMiles);
    newLog.setFlugzeitMinuten(getFlightTime());
    newLog.setPax((TransportPaxe));
    newLog.setSicherheitsgebuehr(getSicherheitsgebuehr());
    newLog.setSummeabrechnung(getSummeabrechnung());
    newLog.setSummekosten(getSummekosten());
    newLog.setTreibstoffkosten(getTreibstoffkosten());
    newLog.setTreibstoffverbrauchkg((int) Spritverbrauch);
    newLog.setIdArrivalTerminal(ArrivalTerminalAbrechnung);
    newLog.setBetragAriivalTerminal(ArriavalGebuehren);
    newLog.setIdDepartureTerminal(DepartureTerminalAbrechnung);
    newLog.setBetragDepartureTerminal(DepartureGebuehren);
    newLog.setTocaoFlughafenName(readAirportData(ZielFlughafenICAO.trim()).getName());
    newLog.setFromIcaoFlughafenName(readAirportData(Abflugflughafen.trim()).getName());
    newLog.setProvision(WertProvision);
    newLog.setMissionsart(yaacarsFlight.getMissionsart());
    newLog.setFixkosten(FlugzeugFixkosten);

    facadeTakeOff.createLogbuchEintrag(newLog);
    ReportSchreiben("Abflughafen: " + Abflugflughafen);
    ReportSchreiben("Zielflughafen: " + ZielFlughafenICAO);
    ReportSchreiben("Gebuchte Passagiere: " + newLog.getPax());
    ReportSchreiben("Gebuchtes Cargo: " + newLog.getCargo());
    ReportSchreiben("Gebuchte Flugzeit: " + newLog.getFlugzeitMinuten());
    ReportSchreiben("Gebuchte Meilen: " + newLog.getMiles());
    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.createLogbuchEintrag() ENDE");
  }

  private void createBankbuchung() {
    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.createBankbuchung() BEGINN");
    String UserName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName");
    String UserKonto = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("BankKonto");

    AirlineID = -1;

    double betrag = 0.0;
    String VerwendungsZweck = "";

    String icao = "";
    int objektid = 0;
    int flugzeugid = 0;
    int kostenstelle = 0;
    int pilotid = 0;

    /*
    ***************  Abfertigungsgebühren 
     */
//    System.out.println("DepartureTerminal für die Abrechnung ID " + DepartureTerminalAbrechnung);
//    System.out.println("ArrivalTerminal für die Abrechnung ID " + ArrivalTerminalAbrechnung);
    if (DepartureTerminalAbrechnung == -300) {
      //Keine Ertrags-Buchung da System
    } else {
      FboUserObjekte fboUser = facadeTakeOff.getUserFBOObjekt(DepartureTerminalAbrechnung);

      betrag = getDepartureGebuehren();
      VerwendungsZweck = loginMB.onSprache("Terminal_msg_Bankbuchung_gutschriftAbfertigungsgebuehrDaparture") + " - " + Abflugflughafen + " - " + PirepFlugNummer;

      SaveBankbuchung(fboUser.getBankkonto(), fboUser.getKontoName(), "300-1000001", "*** FTW Buchungssystem ***",
              new Date(), betrag, fboUser.getBankkonto(), fboUser.getKontoName(),
              new Date(), VerwendungsZweck, fboUser.getIdUser(), -1, -1, -1, -1, -1, -1, Abflugflughafen, fboUser.getIdfboObjekt(), rentedAircraft.getIdMietKauf(), fboUser.getKostenstelle(), pilotid);
    }

    if (ArrivalTerminalAbrechnung == -300) {
      //Keine Ertrags-Buchung da System
    } else {
      FboUserObjekte fboUser = facadeTakeOff.getUserFBOObjekt(ArrivalTerminalAbrechnung);

      betrag = getArriavalGebuehren();
      VerwendungsZweck = loginMB.onSprache("Terminal_msg_Bankbuchung_gutschriftAbfertigungsgebuehrArrival") + " - " + ZielFlughafenICAO + " - " + PirepFlugNummer;

      SaveBankbuchung(fboUser.getBankkonto(), fboUser.getKontoName(), "300-1000001", "*** FTW Buchungssystem ***",
              new Date(), betrag, fboUser.getBankkonto(), fboUser.getKontoName(),
              new Date(), VerwendungsZweck, fboUser.getIdUser(), -1, -1, -1, -1, -1, -1, Abflugflughafen, fboUser.getIdfboObjekt(), rentedAircraft.getIdMietKauf(), fboUser.getKostenstelle(), pilotid);
    }

    if (AirlineJob) {

      /*
          * Ertrag für Fluggesellschaft
       */
      ReportSchreiben("de.klees.beans.takeoff.takeoffBean.createBankbuchung() " + airline.getName());

      betrag = GewinnAirline;
      VerwendungsZweck = airline.getName() + " - " + loginMB.onSprache("Terminal_msg_Bankbuchung_BuchungsgebuehrFluggesellschaft") + "  - " + ZielFlughafenICAO.trim() + " - " + PirepFlugNummer;

      AirlineID = airline.getIdFluggesellschaft();
//
//      System.out.println("de.klees.beans.takeoff.newTakeoffBean.createBankbuchung() " +airline.getBankKontoName());
//      System.out.println("de.klees.beans.takeoff.newTakeoffBean.createBankbuchung() " +airline.getBankKonto());
//      System.out.println("de.klees.beans.takeoff.newTakeoffBean.createBankbuchung() " +airline.getKstBuchungsgebuehr());      
//

      //**** Buchungsgebühr
      SaveBankbuchung(airline.getBankKonto(), airline.getBankKontoName(), "300-1000001", "*** FTW Buchungssystem ***", new Date(), betrag, airline.getBankKonto(), airline.getBankKontoName(), new Date(), VerwendungsZweck, -1, -1, airline.getIdFluggesellschaft(), -1, -1, -1, -1, ZielFlughafenICAO.trim(), -1, rentedAircraft.getIdMietKauf(), airline.getKstBuchungsgebuehr(), -1);

      // Treibstoffkosten Erstattung an Piloten
      MeinFlugzeug = facadeTakeOff.findeMeinFlugzeug(rentedAircraft.getIdMietKauf());
      SpritPreis = MeinFlugzeug.getLetzterSpritPreis();

      betrag = SpritPreis * Spritverbrauch;

      setTreibstoffkosten(betrag);

      VerwendungsZweck = airline.getName() + " - " + loginMB.onSprache("Terminal_msg_Bankbuchung_treibstoffkostenerstattung") + " - " + ZielFlughafenICAO.trim() + " - " + PirepFlugNummer;

      //**** Treibstoffkosten Buchung Pilot
      SaveBankbuchung(UserKonto, UserName, airline.getBankKonto(), airline.getBankKontoName(),
              new Date(), betrag, UserKonto, UserName,
              new Date(), VerwendungsZweck, UserID, -1, -1, -1, -1, -1, -1, ZielFlughafenICAO.trim(), -1, -1, airline.getKstTreibstoffkostenErstattung(), -1);

      //**** Treibstoffkosten Gegenbuchung
      betrag = (SpritPreis * Spritverbrauch) - ((SpritPreis * Spritverbrauch) * 2);

      SaveBankbuchung(airline.getBankKonto(), airline.getBankKontoName(), airline.getBankKonto(), airline.getBankKontoName(),
              new Date(), betrag, UserKonto, UserName,
              new Date(), VerwendungsZweck, -1, -1, airline.getIdFluggesellschaft(), -1, -1, -1, -1, ZielFlughafenICAO.trim(), -1, -1, airline.getKstTreibstoffkostenErstattung(), -1);

      if (crewgebuehren > 0) {

        // ************ Kosten Besatzung (Wird noch geänder sobald die Personalsituation geklärt ist)
        betrag = crewgebuehren - (crewgebuehren * 2);
        VerwendungsZweck = airline.getName() + " - " + loginMB.onSprache("Terminal_msg_Bankbuchung_gehaltCrew") + " - " + ZielFlughafenICAO.trim() + " - " + PirepFlugNummer;

        SaveBankbuchung(airline.getBankKonto(), airline.getBankKontoName(), airline.getBankKonto(), airline.getBankKontoName(),
                new Date(), betrag, "300-1000001", "*** FTW Buchungssystem ***",
                new Date(), VerwendungsZweck, -1, -1, airline.getIdFluggesellschaft(), -1, -1, -1, -1, ZielFlughafenICAO.trim(), -1, -1, airline.getKstGehaltCrew(), -1);
      }


      /*
         * Bonuszahlung von der Airline an den Piloten
       */
      if (bonus > 0.0) {
        betrag = bonus;
        VerwendungsZweck = airline.getName() + ": " + loginMB.onSprache("Terminal_msg_Bankbuchung_bonuszahlungAnPilot") + " - " + ZielFlughafenICAO.trim() + " - " + PirepFlugNummer;

        SaveBankbuchung(UserKonto, UserName, airline.getBankKonto(), airline.getBankKontoName(),
                new Date(), betrag, UserKonto, UserName,
                new Date(), VerwendungsZweck, UserID, -1, -1, -1, -1, -1, -1, ZielFlughafenICAO.trim(), -1, -1, airline.getKstBonusPilot(), -1);
        //
        // ****** Gegenbuchung, bei der Airline in Abzug bringen
        //
        betrag = bonus - (bonus * 2);
        SaveBankbuchung(airline.getBankKonto(), airline.getBankKontoName(), airline.getBankKonto(), airline.getBankKontoName(),
                new Date(), betrag, UserKonto, UserName,
                new Date(), VerwendungsZweck, -1, -1, airline.getIdFluggesellschaft(), -1, -1, -1, -1, ZielFlughafenICAO.trim(), -1, -1, airline.getKstBonusPilot(), -1);

      }

      /*
          ************ Kosten Flugzeugbesitzer
       */
      betrag = getMietgebuehrflugzeug();

      VerwendungsZweck = loginMB.onSprache("Terminal_msg_Bankbuchung_ertragFlugzeugbesitzer") + " - " + rentedAircraft.getType() + " - Reg.- " + rentedAircraft.getRegistrierung() + " - " + PirepFlugNummer;

      String MietKonto = rentedAircraft.getBankkontoBesitzer();
      String MietUserName = facadeTakeOff.getKontoName(MietKonto);

      SaveBankbuchung(MietKonto, MietUserName, airline.getBankKonto(), airline.getBankKontoName(),
              new Date(), betrag, MietKonto, MietUserName,
              new Date(), VerwendungsZweck, -1, -1, -1, rentedAircraft.getIdflugzeugBesitzer(), -1, -1, -1, ZielFlughafenICAO.trim(), -1, rentedAircraft.getIdMietKauf(), rentedAircraft.getKostenstelle(), -1);

      // ************ Kosten  in Abzug bei der Fluggesellschaft bringen
      VerwendungsZweck = loginMB.onSprache("Terminal_msg_Bankbuchung_mietzahlungFlugzeug") + " - " + rentedAircraft.getType() + " - Reg.- " + rentedAircraft.getRegistrierung() + " - " + PirepFlugNummer;
      betrag = betrag - (betrag * 2);
      SaveBankbuchung(airline.getBankKonto(), airline.getBankKontoName(), airline.getBankKonto(), airline.getBankKontoName(),
              new Date(), betrag, MietKonto, MietUserName,
              new Date(), VerwendungsZweck, -1, -1, airline.getIdFluggesellschaft(), -1, -1, -1, -1, ZielFlughafenICAO.trim(), -1, -1, rentedAircraft.getKostenstelle(), -1);

      // ************ Kosten  Landegebühr
      betrag = landegebuehr - (landegebuehr * 2);
      VerwendungsZweck = loginMB.onSprache("Terminal_msg_Bankbuchung_kostenLandungsgebuehr") + " - " + ZielFlughafenICAO.trim() + " - " + PirepFlugNummer;

      SaveBankbuchung(airline.getBankKonto(), airline.getBankKontoName(), airline.getBankKonto(), airline.getBankKontoName(),
              new Date(), betrag, "300-1000001", "*** FTW Buchungssystem ***",
              new Date(), VerwendungsZweck, -1, -1, airline.getIdFluggesellschaft(), -1, -1, -1, -1, ZielFlughafenICAO.trim(), -1, -1, airline.getKstLandegebuehr(), -1);

      // ************ Kosten  Bodenpersonal
      betrag = bodenpersonalgebuehren - (bodenpersonalgebuehren * 2);
      VerwendungsZweck = loginMB.onSprache("Terminal_msg_Bankbuchung_kostenAbfertigung") + " - " + ZielFlughafenICAO.trim() + " - " + PirepFlugNummer;

      SaveBankbuchung(airline.getBankKonto(), airline.getBankKontoName(), airline.getBankKonto(), airline.getBankKontoName(),
              new Date(), betrag, "300-1000001", "*** FTW Buchungssystem ***",
              new Date(), VerwendungsZweck, -1, -1, airline.getIdFluggesellschaft(), -1, -1, -1, -1, ZielFlughafenICAO.trim(), -1, -1, airline.getKstAbfertigung(), -1);

      if (airlinePilot != null) {
        updatePilotFluggesellschaft(airlinePilot.getIdflugesellschaftPiloten(), AnzahlTransportierterPassgiereBerechnen(),
                GewichtTransportiertesCargo(), summeabrechnung, airline.getIdFluggesellschaft());
      }

    }

    /*
    ************** Private Job --- Alle Kosten trägt der Piloten
     */
    if (!AirlineJob) {

      /*
          * Ertrag für Pilot
       */
      betrag = GewinnPilot;
      VerwendungsZweck = loginMB.onSprache("Terminal_msg_Bankbuchung_BuchungsgebuehrPilot") + "  - " + ZielFlughafenICAO.trim() + " - " + PirepFlugNummer;

      if (betrag != 0) {
        //**** Buchungsgebühr
        SaveBankbuchung(UserKonto, UserName, "300-1000001", "*** FTW Buchungssystem ***",
                new Date(), betrag, UserKonto, UserName,
                new Date(), VerwendungsZweck, UserID, -1, -1, -1, -1, -1, -1, ZielFlughafenICAO.trim(), -1, -1, -1, -1);
      }

      if (rentedAircraft.getBesatzung() > 0) {

        //****** Bezahlung Besatzung
        betrag = crewgebuehren - (crewgebuehren * 2);
        VerwendungsZweck = loginMB.onSprache("Terminal_msg_Bankbuchung_gehaltCrew") + " - " + ZielFlughafenICAO.trim() + " - " + PirepFlugNummer;

        SaveBankbuchung(UserKonto, UserName, UserKonto, UserName,
                new Date(), betrag, "300-1000001", "*** FTW Buchungssystem ***",
                new Date(), VerwendungsZweck, UserID, -1, -1, -1, -1, -1, -1, ZielFlughafenICAO.trim(), -1, -1, -1, -1);

      }

      //****** Bezahlung Landegebühr
      betrag = landegebuehr - (landegebuehr * 2);
      VerwendungsZweck = loginMB.onSprache("Terminal_msg_Bankbuchung_kostenLandungsgebuehr") + " - " + ZielFlughafenICAO.trim() + " - " + PirepFlugNummer;

      SaveBankbuchung(UserKonto, UserName, UserKonto, UserName,
              new Date(), betrag, "300-1000001", "*** FTW Buchungssystem ***",
              new Date(), VerwendungsZweck, UserID, -1, -1, -1, -1, -1, -1, ZielFlughafenICAO.trim(), -1, -1, -1, -1);

      //****** Bezahlung Bodenpersonal
      betrag = bodenpersonalgebuehren - (bodenpersonalgebuehren * 2);
      VerwendungsZweck = loginMB.onSprache("Terminal_msg_Bankbuchung_kostenAbfertigung") + " - " + ZielFlughafenICAO.trim() + " - " + PirepFlugNummer;

      SaveBankbuchung(UserKonto, UserName, UserKonto, UserName,
              new Date(), betrag, "300-1000001", "*** FTW Buchungssystem ***",
              new Date(), VerwendungsZweck, UserID, -1, -1, -1, -1, -1, -1, ZielFlughafenICAO.trim(), -1, -1, -1, -1);

      /*
          ************ Ertrag für Flugzeugbesitzer
       */
      betrag = getMietgebuehrflugzeug();

      VerwendungsZweck = loginMB.onSprache("Terminal_msg_Bankbuchung_ertragFlugzeugbesitzer") + " - " + rentedAircraft.getType() + " - Reg.- " + rentedAircraft.getRegistrierung() + " - " + PirepFlugNummer;

      String MietKonto = rentedAircraft.getBankkontoBesitzer();
      String MietUserName = facadeTakeOff.getKontoName(MietKonto);

//  private void SaveBankbuchung(String Bankkonto, String KontoName, String AbsenderKontoNr, String AbsenderKontoName, Date AusfuehrungsDatum, double Betrag, String EmpfaengerKontoNr,
//          String EmpfaengerKontoName, Date UeberweisungsDatum, String VerwendungsZweck,
//          int userid, int AirportID, int FluggesellschaftID, int FlugzeugBesitzerID, int IndustrieID, int LeasinggesellschaftID, int TransportID, 
//          String icao, int objektID, int FlugzeugID, int Kostenstelle, int pilotID) {
//      
      SaveBankbuchung(MietKonto, MietUserName, UserKonto, UserName, new Date(), betrag, MietKonto, MietUserName,
              new Date(), VerwendungsZweck, -1, -1, -1, rentedAircraft.getIdflugzeugBesitzer(), -1, -1, -1, ZielFlughafenICAO.trim(), -1, rentedAircraft.getIdMietKauf(), rentedAircraft.getKostenstelle(), -1);

      // Kosten beim Piloten in Abzug  bringen 
      VerwendungsZweck = loginMB.onSprache("Terminal_msg_Bankbuchung_mietzahlungFlugzeug") + " - " + rentedAircraft.getType() + " - Reg.- " + rentedAircraft.getRegistrierung() + " - " + PirepFlugNummer;
      betrag = betrag - (betrag * 2);
      SaveBankbuchung(UserKonto, UserName, UserKonto, UserName, new Date(), betrag, MietKonto, MietUserName,
              new Date(), VerwendungsZweck, UserID, -1, -1, -1, -1, -1, -1, ZielFlughafenICAO.trim(), -1, -1, -1, -1);

      /* 
          **************************************************************
          ******************** Provision Überweisen *********************
          **************************************************************
       */
      List<ViewFlugProvisionen> abrechnung = facadeTakeOff.getProvisionen(UserID, ZielFlughafenICAO.trim());

      for (ViewFlugProvisionen provision : abrechnung) {

        if (provision.getProvision() > 0) {
          // Beim Piloten die Provision abziehen
          betrag = provision.getProvision() - (provision.getProvision() * 2);
          VerwendungsZweck = loginMB.onSprache("Terminal_msg_Bankbuchung_provision") + " - " + ZielFlughafenICAO.trim() + " - " + provision.getKontoName() + " - " + PirepFlugNummer;

          Fluggesellschaft ProvAirline = facadeTakeOff.readFluggesellschaft(provision.getIdAirline());
          int KostenStelle = 0;

          if (ProvAirline != null) {
            KostenStelle = ProvAirline.getKstProvisionFluggesellschaft();
          }

          SaveBankbuchung(UserKonto, UserName, UserKonto, UserName, new Date(), betrag, provision.getBankKonto(), provision.getKontoName(),
                  new Date(), VerwendungsZweck, UserID, -1, -1, -1, -1, -1, -1, ZielFlughafenICAO.trim(), -1, -1, KostenStelle, -1);

          // Bei der Fluggesellschaft die Provision als Guthaben buchen
          betrag = provision.getProvision();
          VerwendungsZweck = loginMB.onSprache("Terminal_msg_Bankbuchung_provision") + " - " + ZielFlughafenICAO.trim() + "  -  " + UserName + " - " + PirepFlugNummer;

          SaveBankbuchung(provision.getBankKonto(), provision.getKontoName(), UserKonto, UserName, new Date(), betrag, provision.getBankKonto(), provision.getKontoName(),
                  new Date(), VerwendungsZweck, -1, -1, provision.getIdAirline(), -1, -1, -1, -1, ZielFlughafenICAO.trim(), -1, -1, KostenStelle, -1);
        }
      }

    }
    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.createBankbuchung() ENDE");
  }

  private void userUpdate() {
    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.userUpdate() BEGINN");
    currentUser = facadeTakeOff.findByUserID(UserID);
    SummeCargo = GewichtTransportiertesCargo();
    TransportPaxe = AnzahlTransportierterPassgiereBerechnen();

    int FlightMiles = currentUser.getFlightmiles();
    int Flights = currentUser.getFlights();
    int FlightTimeCur = currentUser.getFlighttime();
    int FlightPaxes = currentUser.getFlightpaxes();
    int FlightCargo = currentUser.getFlightcargo();
    int FlightforAirline = currentUser.getFlugzeitenFG();

    FlightMiles = FlightMiles + currentFlightMiles;
    Flights = Flights + 1;
    FlightTimeCur = FlightTimeCur + (getFlightTime());
    FlightPaxes = FlightPaxes + TransportPaxe;
    FlightCargo = FlightCargo + SummeCargo;

    if (AirlineJob) {
      FlightforAirline = FlightforAirline + getFlightTime();
    }

    currentUser.setFlightmiles(FlightMiles);
    currentUser.setFlights(Flights);
    currentUser.setFlighttime(FlightTimeCur);
    currentUser.setFlightpaxes(FlightPaxes);
    currentUser.setFlightcargo(FlightCargo);
    currentUser.setFlugzeitenFG(FlightforAirline);

    ReportSchreiben("Gebuchte Passagiere: " + TransportPaxe);
    ReportSchreiben("Gebuchtes Cargo: " + SummeCargo);
    ReportSchreiben("Gebuchte Flugzeit: " + getFlightTime());
    ReportSchreiben("Gebuchte Meilen: " + currentFlightMiles);

    facadeTakeOff.userEdit(currentUser);

    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.userUpdate() ENDE");
  }

  private void updatePilotFluggesellschaft(int idPilotFluggesellschaft, int Passagiere, int Cargo, double Umsatz, int idAirline) {

    if (idPilotFluggesellschaft > 0) {
      ReportSchreiben("de.klees.beans.takeoff.takeoffBean.updatePilotFluggesellschaft() BEGINN");

      FlugesellschaftPiloten fgp = facadeTakeOff.readFluggesellschaftPilot(idAirline, UserID);

      int meilen = fgp.getKilometer() + currentFlightMiles;
      int flugzeit = fgp.getZeit() + getFlightTime();
      int pax = fgp.getPassagiere() + Passagiere;
      int cargo = fgp.getWaren() + Cargo;
      double gesUmsatz = fgp.getUmsatz() + Umsatz;

      fgp.setKilometer(meilen);
      fgp.setZeit(flugzeit);
      fgp.setPassagiere(pax);
      fgp.setWaren(cargo);
      fgp.setUmsatz(gesUmsatz);
      fgp.setLetzterFlug(new Date());

      facadeTakeOff.editFluggesellschaftPilot(fgp);

      ReportSchreiben("de.klees.beans.takeoff.takeoffBean.updatePilotFluggesellschaft() ENDE");

    }
  }

  private void updateFluggesellschaft() {

    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.updateFluggesellschaft() BEGINN");

    for (Assignement ag : assignmentList) {

      int mengePax = 0;
      int mengeCargo = 0;

      if (ag.getToIcao().equals(ZielFlughafenICAO.trim()) && ag.getUserlock() == 1) {

        // Fluggesellschaft ID auslesen
        if (ag.getIdAirline() > 0) {
          airline = facadeTakeOff.readFluggesellschaft(ag.getIdAirline());

          if (ag.getRoutenArt() == 1 || ag.getRoutenArt() == 4) {
            mengePax = airline.getGeflogeneJobs();
            airline.setGeflogeneJobs((mengePax + ag.getAmmount()));
            facadeTakeOff.editFluggesellschaft(airline);
          }

          if (ag.getRoutenArt() == 2) {
            mengeCargo = airline.getGeflogenesCargo();
            airline.setGeflogenesCargo((mengeCargo + ag.getAmmount()));
            facadeTakeOff.editFluggesellschaft(airline);
          }

        }

      }
    }
    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.updateFluggesellschaft() ENDE");
  }

  public void onZielFlughafenSuchen() {
    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.onZielFlughafenSuchen() BEGINN");
    ZielFlughafenICAO = "";
    boolean gefunden = false;
    // Flugdaten einlesen
    // *** Flug einlesen um Positionsdaten zu ermitteln

    Yaacarskopf acarsflight = facadeTakeOff.YAACARSFlightInfo(UserID);

    if (acarsflight != null) {
      PirepOK = true;
    } else {
      PirepOK = false;
      ReportSchreiben("onZielFlughafenSuchen : keine ACARS Flight ID gefunden");
      AbrechnungsFehler = true;
      setAbrechnungsFehlerText(loginMB.onSprache("Terminal_msg_keinAcarsFlightidGefunden"));
    }

//    if (PirepOK) {
//      if (acarsflight != null) {
//        position = facadeTakeOff.readAcarsPosition(acarsflight.getCurPositionID());
//      } else {
//        setAbrechnungsFehlerText(loginMB.onSprache("Terminal_msg_acarsFlightNummerEvtlFalsch"));
//        PirepOK = false;
//      }
//    }
    if (PirepOK) {

      double Long = acarsflight.getLetztepositionlongitude();
      double Lati = acarsflight.getLetztepositionlatitude();

      List<Airport> flughafen = facadeTakeOff.readZielFlughaefen(Lati - 0.15, Lati + 0.15, Long - 0.15, Long + 0.15);

      if (!flughafen.isEmpty()) {

        // Finde Flughafen über fliegende Aufträge
        // Änderung am 15.01.2018
        for (ViewAbrechnungZieleSumme auftragsziele : getAbrechnungsziele()) {
          if (findeFlughafenUeberIcao(auftragsziele.getToIcao(), flughafen) && !gefunden) {
            ZielFlughafenICAO = auftragsziele.getToIcao();
            ReportSchreiben("onZielFlughafenSuchen : Flughafen über fliegende Aufträge gefunden : " + ZielFlughafenICAO.trim());
            gefunden = true;
          }
        }

        if (!gefunden) {
          //Suche Flughafen über den ACARS-ICAO
          if (findeFlughafenUeberIcao(acarsflight.getArrivalicao(), flughafen)) {
            ZielFlughafenICAO = acarsflight.getArrivalicao();
            gefunden = true;
            ReportSchreiben("onZielFlughafenSuchen : Flughafen über ACARS-Destination gefunden : " + ZielFlughafenICAO);
          } else {
            gefunden = false;
            PirepOK = false;
            ReportSchreiben("onZielFlughafenSuchen : Keine Flughafen über ACARS-Destination gefunden, versuche den am nächsten Liegenden Flughafen zu finden");
          }
        }

        //Flughafen über ACARS-ICAO wurde nicht gefunden, es wird mit der nächst gelegene Flughafen gesucht
        if (!gefunden) {

          if (findeFlughafenUeberUmkreis(Lati, Long, 0.05)) {
            gefunden = true;
          }

          if (!gefunden) {
            if (findeFlughafenUeberUmkreis(Lati, Long, 0.04)) {
              gefunden = true;
            }
          }

          if (!gefunden) {
            if (findeFlughafenUeberUmkreis(Lati, Long, 0.03)) {
              gefunden = true;
            }
          }

          if (!gefunden) {
            if (findeFlughafenUeberUmkreis(Lati, Long, 0.02)) {
              gefunden = true;
            }
          }

          if (!gefunden) {
            if (findeFlughafenUeberUmkreis(Lati, Long, 0.01)) {
              gefunden = true;
            }
          }

          if (!gefunden) {
            if (findeFlughafenUeberUmkreis(Lati, Long, 0.005)) {
              gefunden = true;
            }
          }

          if (!gefunden) {
            if (findeFlughafenUeberUmkreis(Lati, Long, 0.001)) {
              gefunden = true;
            }
          }

        }

        // Geflogene Meilen auslesen
        if (gefunden) {
          currentFlightMiles = acarsflight.getGeflogenemeilen();
          ReportSchreiben("onZielFlughafenSuchen : geflogene Meilen......: " + currentFlightMiles);
        }
      } else {
        // Flughafenliste ist leer
        AbrechnungsFehler = true;
        PirepOK = false;
        setAbrechnungsFehlerText(loginMB.onSprache("Terminal_msg_esWurdeKeinZielflughafenGefunden"));

      }
    }
    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.onZielFlughafenSuchen() ENDE");
  }

  private boolean findeFlughafenUeberIcao(String icao, List<Airport> Flughaefen) {

    ReportSchreiben("findeFlughafenUeberIcao : Suche Flughafen mit dem ICAO : " + icao);

    for (Airport airport : Flughaefen) {
      if (airport.getIcao().equals(icao)) {
        ReportSchreiben("findeFlughafenUeberIcao : Flughafen gefunden : " + icao);
        return true;
      }
    }
    ReportSchreiben("findeFlughafenUeberIcao : Flughafen wurde nicht gefunden : " + icao);
    return false;
  }

  private boolean findeFlughafenUeberUmkreis(double Lati, double Longi, double wert) {

    double bgVon = Lati - wert;
    double bgBis = Lati + wert;
    double lgVon = Longi - wert;
    double lgBis = Longi + wert;

    List<Airport> flughaefen = facadeTakeOff.readZielFlughaefen(bgVon, bgBis, lgVon, lgBis);
    if (flughaefen.size() == 1) {
      ZielFlughafenICAO = flughaefen.get(0).getIcao();
      ReportSchreiben("findeFlughafenUeberUmkreis : Flughafen wurde gefunden : " + ZielFlughafenICAO.trim());
      return true;
    } else {
      ReportSchreiben("findeFlughafenUeberUmkreis : Flughafen wurde nicht gefunden : ");
    }
    return false;
  }

  private boolean onNurJobsFuerFG() {
    int zaehler = 0;

    for (Assignement assi : assignmentList) {
      if (assi.getLocationIcao().equals(rentedAircraft.getAktuellePositionICAO().trim())) {
        zaehler++;
        if (!assi.getIdAirline().equals(rentedAircraft.getFluggesellschaftID())) {
          return false;
        }
      }
    }

    //System.out.println("de.klees.beans.takeoff.takeoffBean.onNurJobsFuerFG() zaehler " + zaehler);
    if (zaehler == 0) {
      return false;
    }

    return true;
  }

  private boolean onGemischteJobs() {

    for (int i = 0; i < assignmentList.size(); i++) {
      //13.03.2019 Prüfung auf SystemAirline, wenn -1 = FTW-System
      //Aenderung wegen Bonus1 und Bonus2, jetzt sind wieder 0 Werte erlaubt
      if (assignmentList.get(i).getIdAirline() == -1) {
        return true;
      }
    }

    //Prüfung ob für mehrere Airlines geflogen wird
    for (int i = 0; i < assignmentList.size() - 1; i++) {

      if (i + 1 > assignmentList.size()) {
        return false;
      }
      if (assignmentList.get(i).getUserlock() == 1) {
        /*
       Umgestellt auf Fluggesellschaft ID 19.02.2018
         */
        if (!assignmentList.get(i).getIdAirline().equals(assignmentList.get(i + 1).getIdAirline())) {
          return true;
        }
      }
    }

    return false;
  }

  private boolean onJobAusJobboerse() {
    for (Assignement assi : assignmentList) {
      if (assi.getUserlock() == 1) {
        if (assi.getIdjob() > 0) {
          return true;
        }
      }
    }
    return false;
  }

  //Flughafen Transfers zaehlen
  private void onAirportTransfer() {

    ReportSchreiben("de.klees.beans.takeoff.takeoffBean.onAirportTransfer() BEGINN");

    // Arrival Airport
    AirportTransfers transfer = new AirportTransfers();
    transfer.setDatum(new Date());
    transfer.setIcao(ZielFlughafenICAO.trim());
    transfer.setCargo(SummeCargo);
    transfer.setPassengers(SummePassengers + SummeBuisnessPassengers);
    facadeTakeOff.newTransfer(transfer);

    // Departure Airport
    transfer = new AirportTransfers();
    transfer.setDatum(new Date());
    transfer.setIcao(PirepFromICAO);
    transfer.setCargo(SummeCargo);
    transfer.setPassengers(SummePassengers + SummeBuisnessPassengers);
    facadeTakeOff.newTransfer(transfer);

    ReportSchreiben("Passagier und Cargo Zahlen für " + ZielFlughafenICAO + " und " + PirepFromICAO + " erfolgreich übertragen");

    ReportSchreiben("de.klees.beans.takeoff.takeoffBean.onAirportTransfer() ENDE");

  }

  private boolean istEsEinCharterAuftrag() {
    for (Assignement assi : assignmentList) {
      if (assi.getType() == 3) {
        return true;
      }
    }

    return false;
  }

  private boolean PruefeObCharterAuftragOK() {
    //gibt es mehr als einenen Auftrag
    if (assignmentList.size() > 1) {
      return false;
    }
    return true;
  }

  public boolean isAcarsFlugInUse() {
    return acarsFlugInUse;
  }

  public boolean CCheckFaellig() {
    // C-Check prüfen
    // aktuelles Datum
    Calendar ccheck = Calendar.getInstance();
    Date naechsterCCheck = rentedAircraft.getNaechsterTerminCcheck();

    int AktMonat = ccheck.get(ccheck.MONTH) + 1; // Monate beginnen bei 0 = Januar
    int AktJahr = ccheck.get(ccheck.YEAR);

    int CCheckMonat = Integer.valueOf(new SimpleDateFormat("MM").format(naechsterCCheck)) + 1;
    int CCheckJahr = Integer.valueOf(new SimpleDateFormat("yyyy").format(naechsterCCheck));

    if (CCheckMonat > 12) {
      CCheckJahr = CCheckJahr + 1;
      CCheckMonat = 1;
    }

    if (CCheckJahr > AktJahr) {
      return false;
    }

    if (CCheckJahr < AktJahr) {
      return true;
    }

    if (CCheckJahr == AktJahr) {
      if (CCheckMonat > AktMonat) {
        return false;
      }
      if (CCheckMonat <= AktMonat) {
        return true;
      }
    }

    return false;
  }

  private double BonusUeberBonussystemDerFluggesellschaft(int FGID) {

    double boniProzent = 0.0;
    BoniBezeichnung = "";

    FlugesellschaftPiloten pilot = facadeTakeOff.readFluggesellschaftPilot(FGID, UserID);

    if (pilot != null) {

      List<FluggesellschaftBonusSystem> bonuslist = facadeTakeOff.getBoniList(FGID);

      for (FluggesellschaftBonusSystem boni : bonuslist) {
        if (pilot.getZeit() >= boni.getZeit()) {
          boniProzent = boni.getBonus();
          BoniBezeichnung = boni.getBonusname();
        }
      }

    }

    return boniProzent;
  }

  public String getSimbriefDaten() {

    /*
    http://www.simbrief.com/system/dispatch.php?
        fltnum=1234 	// Zahl der Flugnummer 
        &airline=FTW 	// ersten drei Ziffern der Flugnummer
        &type=B738		// Plane-ICAO

        &orig=DEP-Airport-ICAO
        &dest=ARR-Airport-ICAO
	&altn=ALT-Airport-ICAO
		
        &reg=FTW4711 	// Tail Number
        &pax=42			// Pax Count
        &cargo=7		// Cargo (in Tonnen! mit . als Trenner or gleich als int)
     */
    Yaacarskopf yaacarsFlight = facadeTakeOff.getYAACARSFlight(UserID);

    if (yaacarsFlight != null) {
      if (rentedAircraft != null) {

        try {
          String Url = "http://www.simbrief.com/system/dispatch.php?";

          String FlugNr = "";
          String Airline = "";

          int maxlengt = yaacarsFlight.getFlugnummer().length();

          FlugNr = "&fltnum=" + yaacarsFlight.getFlugnummer().substring(3, maxlengt);
          Airline = "&airline=" + yaacarsFlight.getFlugnummer().substring(0, 3);

          String FlugzeugTyp = "&type=N/A";

          FlugzeugTyp = "&type=" + rentedAircraft.getIcaoFlugzeugcode().replaceAll(" ", "%20");

          String FlugzeugReg = "&reg=" + rentedAircraft.getRegistrierung();
          String Departure = "&orig=" + yaacarsFlight.getDepartureicao();
          String Arrival = "&dest=" + yaacarsFlight.getArrivalicao();
          String Alternate = "&altn=AUTO";

          double cargo = (getGewichtCargoTakeOff() + getGewichtGepaeckTakeOff()) / 1000;
          int pax = getAnzahlPassengersBuisnessTakeOff() + getAnzahlPassengersEconomyTakeOff();

          String Pax = "&pax=" + pax;
          String Cargo = "&cargo=" + cargo;

          Url = Url + FlugNr + Airline + FlugzeugTyp + FlugzeugReg + Departure + Arrival + Alternate + Pax + Cargo;

//      ReportSchreiben(Url);
//      ReportAusgeben();
          return Url;
        } catch (NullPointerException e) {
          return "";
        }

      } else {
        return "";
      }
    } else {
      return "";
    }

  }

  public boolean isTankstelleOffen() {
    TankstelleOffen = true;
    //"flugstatus"
    //0 = angelegt
    //1 = gestartet
    //2 = beendet
    //
    Yaacarskopf yaacarsFlight = facadeTakeOff.getYAACARSFlight(UserID);
    if (yaacarsFlight != null) {
      if (yaacarsFlight.getFlugstatus() == 0) {
        TankstelleOffen = true;
      } else {
        TankstelleOffen = false;
      }
    }
    return TankstelleOffen;
  }

  /*
  ****************************************************** Flug starten und beenden ********* ENDE
   */
 /*
  ****************************************************** Wert des Transportes berechnen
   */
  public double getWertTakeOff() {
    SummeTakeOff = 0;
    if (!assignmentList.isEmpty()) {
      for (Assignement assignment : assignmentList) {
        if (assignment.getLocationIcao().equals(Abflugflughafen)) {
          SummeTakeOff = SummeTakeOff + assignment.getPay();
        }
      }
      //SummeTakeOff = SummeTakeOff * SitzKonfigFaktor;
    }
    return SummeTakeOff;
  }

  private int AnzahlPaxFuerTerminal() {
    int terminalPax = 0;

//    for (Assignement assi : assignmentList) {
//      if (assi.getLocationIcao().equals(Abflugflughafen) && assi.getUserlock() == 1) {
//        if (assi.getRoutenArt() == 1 || assi.getRoutenArt() == 4) {
//          terminalPax = terminalPax + assi.getAmmount();
//        }
//      }
//    }
    for (Assignement assi : assignmentList) {
      if (assi.getToIcao().equals(ZielFlughafenICAO) && assi.getUserlock() == 1) {
        if (assi.getRoutenArt() == 1 || assi.getRoutenArt() == 4) {
          terminalPax = terminalPax + assi.getAmmount();
        }
      }
    }

    return terminalPax;

  }

  private int AnzahlTransportierterPassgiereBerechnen() {
    TransportPaxe = 0;

    for (Assignement assi : assignmentList) {
      if (assi.getLocationIcao().equals(Abflugflughafen) && assi.getUserlock() == 1) {
        if (assi.getRoutenArt() == 1 | assi.getRoutenArt() == 4) {
          TransportPaxe = TransportPaxe + assi.getAmmount();
        }
      }
    }
    return TransportPaxe;
  }

  private int GewichtTransportiertesCargo() {
    int Cargo = 0;

    for (Assignement assi : assignmentList) {
      if (assi.getLocationIcao().equals(Abflugflughafen) && assi.getUserlock() == 1) {
        if (assi.getRoutenArt() == 2 || assi.getRoutenArt() == 5) {
          Cargo = Cargo + assi.getAmmount();
        }
      }
    }
    return Cargo;
  }

  public int getAnzahlPassengersEconomyTakeOff() {
    int Passengers = 0;
    if (!assignmentList.isEmpty()) {
      for (int i = 0; i < assignmentList.size(); i++) {
        if (assignmentList.get(i).getRoutenArt() == 1 && assignmentList.get(i).getLocationIcao().equals(Abflugflughafen)) {
          Passengers = Passengers + assignmentList.get(i).getAmmount();
        }
      }
    }
    return Passengers;
  }

  public int getAnzahlPassengersBuisnessTakeOff() {
    int Passengers = 0;
    if (!assignmentList.isEmpty()) {
      for (int i = 0; i < assignmentList.size(); i++) {
        if (assignmentList.get(i).getRoutenArt() == 4 && assignmentList.get(i).getLocationIcao().equals(Abflugflughafen)) {
          Passengers = Passengers + assignmentList.get(i).getAmmount();
        }
      }
    }
    return Passengers;
  }

  public int getGewichtCargoTakeOff() {
    int Cargo = 0;
    if (!assignmentList.isEmpty()) {
      for (int i = 0; i < assignmentList.size(); i++) {
        if ((assignmentList.get(i).getRoutenArt() == 2 || assignmentList.get(i).getRoutenArt() == 5) && assignmentList.get(i).getLocationIcao().equals(Abflugflughafen)) {
          Cargo = Cargo + assignmentList.get(i).getAmmount();
        }
      }
    }
    return Cargo;
  }

  public int getGewichtGepaeckTakeOff() {
    int gewicht = 0;
    if (!assignmentList.isEmpty()) {
      for (int i = 0; i < assignmentList.size(); i++) {
        //if (assignmentList.get(i).getRoutenArt() == 1 || assignmentList.get(i).getRoutenArt() == 4 && assignmentList.get(i).getLocationIcao().equals(Abflugflughafen)) {
        if (assignmentList.get(i).getLocationIcao().equals(Abflugflughafen)) {
          gewicht = gewicht + assignmentList.get(i).getGepaeck();
        }
      }
    }
    return gewicht;
  }

  public int getGewichtPassagiereTakeOff() {

    int gewicht = 0;
    if (!assignmentList.isEmpty()) {
      for (int i = 0; i < assignmentList.size(); i++) {
        if (assignmentList.get(i).getLocationIcao().equals(Abflugflughafen)) {
          if (assignmentList.get(i).getRoutenArt() == 1 || assignmentList.get(i).getRoutenArt() == 4) {
            gewicht = gewicht + assignmentList.get(i).getGewichtPax();
          }
        }
      }
    }
    return gewicht;

  }

  public int getSummeGewichtTotal() {
    return getGewichtCargoTakeOff() + getGewichtPassagiereTakeOff() + getGewichtGepaeckTakeOff() + getSummeGewichtTreibstoff() + getGewichtDerCrew();
  }

  public int getSummeGewichtNachLandung() {
    return getGewichtCargoTakeOff() + getGewichtPassagiereTakeOff() + getGewichtGepaeckTakeOff() + getLeergewicht() + getGewichtDerCrew() + getSummeGewichtTreibstoff() - getSpritverbrauch();
  }

  public int getSummeGewichtTreibstoff() {

    try {
      return rentedAircraft.getAktuelleTankfuellung();
    } catch (NullPointerException e) {
      return 0;
    }

  }

  private double WertTransport() {
    double wert = 0.0;
    WertSonstigerJob = 0.0;
    WertStandardJob = 0.0;
    if (!assignmentList.isEmpty()) {
      for (int i = 0; i < assignmentList.size(); i++) {
        // ********** Zielflughafen beachten
        if (assignmentList.get(i).getLocationIcao().equals(Abflugflughafen)) {
          if (assignmentList.get(i).getFlugrouteName().equals("Standard-Job")) {
            WertStandardJob = WertStandardJob + assignmentList.get(i).getPay();
          } else {
            WertSonstigerJob = WertSonstigerJob + assignmentList.get(i).getPay();
          }
          wert = WertSonstigerJob + WertStandardJob;
        }
      }
    }
    return wert;
  }

  private void CargoAndPassagiereMenge() {
    SummeCargo = 0;
    SummePassengers = 0;
    SummeBuisnessPassengers = 0;
    if (!assignmentList.isEmpty()) {
      for (int i = 0; i < assignmentList.size(); i++) {
        // ********** Zielflughafen beachten
        if (assignmentList.get(i).getLocationIcao().equals(Abflugflughafen)) {

          switch (assignmentList.get(i).getRoutenArt()) {
            case 1:
              SummePassengers = SummePassengers + assignmentList.get(i).getAmmount();
              break;
            case 2:
              SummeCargo = SummeCargo + assignmentList.get(i).getAmmount();
              break;
            case 4:
              SummeBuisnessPassengers = SummeBuisnessPassengers + assignmentList.get(i).getAmmount();
              break;
            default:
              break;
          }

        }

      }
    }
  }

  /*
  ****************************************************** Wert der Abrechnungspositionen
   */
 /*
  ****************************************************** Wert des Transportes berechnen ENDE
   */
 /*
  ***************************************************** ACARS Berechnungen Beginn
   */
  private void deleteYAAcarsDaten(int yaacarsID) {
    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.deleteYAAcarsDaten() BEGINN");

    facadeTakeOff.YAACARSDatenLoeschen(yaacarsID);

    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.deleteYAAcarsDaten() ENDE");
  }

  public List<Yaacarskopf> getListYaacarsFluege() {
    return facadeTakeOff.listYaacarsFluefe();
  }

  public int getSollTankMenge(int FlugzeugID) {
    ViewFlugzeugeMietKauf mietFlugzeug = facadeTakeOff.findAircraftForSupport(FlugzeugID);

    if (mietFlugzeug != null) {
      return mietFlugzeug.getVerbrauchStunde();
    } else {
      return 0;
    }

  }

  public void onFlugFreigebenWegenSprit() {

    Yaacarskopf yk = facadeTakeOff.YAACARSFlightInfo(UserID);

    double istVerbrauchStunde = rentedAircraft.getVerbrauchStunde();
    double Flugzeit = (double) yk.getGeflogenezeit() / 60.0;

    double SollVerbrauch = istVerbrauchStunde * Flugzeit;

    yk.setVerbrauchtetankmengekg((int) SollVerbrauch);
    yk.setFlugOK(true);

    facadeTakeOff.saveYAACARSKopf(yk);

    NewMessage("Flug wurde freigegeben");
  }

  public void onFlugFreigebenSupport() {

    ViewFlugzeugeMietKauf mietFlugzeug = facadeTakeOff.findAircraftForSupport(SupportFreigabeFlug.getFlugzeugid());

    double istVerbrauchStunde = mietFlugzeug.getVerbrauchStunde();
    double Flugzeit = (double) SupportFreigabeFlug.getGeflogenezeit() / 60.0;

    double SollVerbrauch = istVerbrauchStunde * Flugzeit;

    SupportFreigabeFlug.setVerbrauchtetankmengekg((int) SollVerbrauch);
    SupportFreigabeFlug.setFlugOK(true);

    facadeTakeOff.saveYAACARSKopf(SupportFreigabeFlug);

    NewMessage("Flug wurde freigegeben");
  }

  public void onFlugLoeschenSupport() {

    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.onFlugLoeschenSupport() BEGINN");

    /*
    *************** Aufträge zurücksetzen 
     */
    List<Assignement> assilist = facadeTakeOff.findByMyFlight(SupportFreigabeFlug.getUserid(),StartICAO);
    for (Assignement assi : assilist) {
      facadeTakeOff.onAssignmentFromHold(assi.getIdassignement());
    }

    /*
    ************** YAACARS Flugdaten löschen
     */
    facadeTakeOff.YAACARSDatenLoeschen(SupportFreigabeFlug.getIdyaacarskopf());

    ReportSchreiben("de.klees.beans.takeoff.newTakeoffBean.onFlugLoeschenSupport() ENDE");
  }

  /*
  ***************************************************** ACARS Berechnungen Ende
   */
  private void NewMessage(String neueNachricht) {
    FacesMessage msg = new FacesMessage(neueNachricht);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  public void timeForward() {
    TimeAdd = TimeAdd + 10;
  }

  public void onRandomJob() {
    try {
      ViewFlugzeugeMietKauf tmpAircraft = facadeTakeOff.findMyRentedAircraft(UserID);
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("LetztesZiel", tmpAircraft.getAktuellePositionICAO().trim());
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("AircraftID", tmpAircraft.getIdMietKauf());
      CONF.RandomJob = true;

    } catch (NullPointerException e) {
      CONF.RandomJob = false;
      RandomJob = false;
    }

  }

  public void onCancelRandomJob() {
    RandomJob = false;
    CONF.RandomJob = false;
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

    facadeTakeOff.createBankbuchung(newBuchung);

  }

  public void onRowSelect(SelectEvent event) {
  }

  public void onRowUnselect(UnselectEvent event) {
  }

  private void ReportSchreiben(String text) {
    ReportList rep = new ReportList();
    rep.setZeit(new Date());
    rep.setMeldung(text);
    report.add(rep);
  }

  private void ReportAusgeben() {
    String ausgabe = "---------------------------------- FTW Reporting Terminal BEGINN --------------------------------------------------------------" + "\n";
    for (ReportList lst : report) {
      ausgabe = ausgabe + format.format(lst.getZeit()) + " - " + lst.getMeldung() + "\n";
    }
    ausgabe = ausgabe + "------------------------------- FTW Reporting Terminal ENDE -----------------------------------------------------------------" + "\n";
    System.out.println(ausgabe);
    report.clear();
  }

  /*
  ************ Getter and Setter
   */
  public ViewFlugzeugeMietKauf getSelectedAircraft() {
    return currentAircraft;
  }

  public void setSelectedAircraft(ViewFlugzeugeMietKauf selectedAircraft) {
    this.currentAircraft = selectedAircraft;
  }

  public boolean isShowAircraftTable() {
    return showAircraftTable;
  }

  public String getAirportName() {
    return AirportName;
  }

  public String getDestinationICAO() {
    return DestinationICAO;
  }

  public void setDestinationICAO(String DestinationICAO) {
    this.DestinationICAO = DestinationICAO;
  }

  public double getSicherheitsgebuehr() {
    return sicherheitsgebuehr;
  }

  public void setSicherheitsgebuehr(double sicherheitsgebuehr) {
    this.sicherheitsgebuehr = sicherheitsgebuehr;
  }

  public double getLandegebuehr() {
    return landegebuehr;
  }

  public void setLandegebuehr(double landegebuehr) {
    this.landegebuehr = landegebuehr;
  }

  public double getBuchungsgebuehr() {
    return buchungsgebuehr;
  }

  public void setBuchungsgebuehr(double buchungsgebuehr) {
    this.buchungsgebuehr = buchungsgebuehr;
  }

  public double getTreibstoffkosten() {
    return treibstoffkosten;
  }

  public void setTreibstoffkosten(double treibstoffkosten) {
    this.treibstoffkosten = treibstoffkosten;
  }

  public double getMietgebuehrflugzeug() {
    return mietgebuehrflugzeug;
  }

  public void setMietgebuehrflugzeug(double mietgebuehrflugzeug) {
    this.mietgebuehrflugzeug = mietgebuehrflugzeug;
  }

  public double getCrewgebuehren() {
    return crewgebuehren;
  }

  public void setCrewgebuehren(double crewgebuehren) {
    this.crewgebuehren = crewgebuehren;
  }

  public double getBodenpersonalgebuehren() {
    return bodenpersonalgebuehren;
  }

  public void setBodenpersonalgebuehren(double bodenpersonalgebuehren) {
    this.bodenpersonalgebuehren = bodenpersonalgebuehren;
  }

  public int getTransportPaxe() {
    return TransportPaxe;
  }

  public void setTransportPaxe(int TransportPaxe) {
    this.TransportPaxe = TransportPaxe;
  }

  public double getSummeabrechnung() {
    return summeabrechnung;
  }

  public void setSummeabrechnung(double summeabrechnung) {
    this.summeabrechnung = summeabrechnung;
  }

  public double getSummekosten() {
    return summekosten;
  }

  public void setSummekosten(double summekosten) {
    this.summekosten = summekosten;
  }

  public double getSummetakeoff_display() {
    return summetakeoff_display;
  }

  public void setSummetakeoff_display(double summetakeoff_display) {
    this.summetakeoff_display = summetakeoff_display;
  }

  public double getBonusProzent() {
    return bonusProzent;
  }

  public void setBonusProzent(double bonusProzent) {
    this.bonusProzent = bonusProzent;
  }

  public double getBonus() {
    return bonus;
  }

  public void setBonus(double bonus) {
    this.bonus = bonus;
  }

  public String getTimeZone() {
    return Calendar.getInstance().getTimeZone().getID();
  }

  public boolean isIsLoaded() {
    return isLoaded;
  }

  public void setIsLoaded(boolean isLoaded) {
    this.isLoaded = isLoaded;
  }

  public String getAudioFile() {
    return "<audio src='https://docs.google.com/uc?export=open&id=0B3RGufMJTtW9ZXpWY1RPaWxBQjg' controls='' loop='' type='audio/mp3' style='width: 190px;'></audio>";
  }

  public boolean isRandomJob() {
    return RandomJob;
  }

  public boolean isIsAcasFlightExist() {
    return isAcasFlightExist;
  }

  public void setIsAcasFlightExist(boolean isAcasFlightExist) {
    this.isAcasFlightExist = isAcasFlightExist;
  }

  public Yaacarskopf getAcarsflightinfo() {
    return facadeTakeOff.YAACARSFlightInfo(UserID);
  }

  public double getKG2Libs(int Gewicht) {
    return (double) Gewicht * 2.204622915;
  }

  public double getLibs2KG(int Gewicht) {
    return (double) Gewicht * 0.453592309685;
  }

  /*
  Flugzeit in Minuten
   */
  public int getFlightTime() {
    return FlightTime;
  }

  public void setFlightTime(int FlightTime) {
    this.FlightTime = FlightTime;
  }

  public boolean isFlugGestartet() {
    return FlugGestartet;
  }

  public void setFlugGestartet(boolean FlugGestartet) {
    this.FlugGestartet = FlugGestartet;
  }

  public boolean isIstEinFlugzeugGemietet() {
    return istEinFlugzeugGemietet;
  }

  public boolean isIstEinAuftragVorhanden() {
    return istEinAuftragVorhanden;
  }

  public String getFlughafenName() {
    return FlughafenName;
  }

  public double getWertProvision() {
    return WertProvision;
  }

  public void setWertProvision(double WertProvision) {
    this.WertProvision = WertProvision;
  }

  public double getGewinnPilot() {
    return GewinnPilot;
  }

  public void setGewinnPilot(double GewinnPilot) {
    this.GewinnPilot = GewinnPilot;
  }

  public double getGewinnAirline() {
    return GewinnAirline;
  }

  public void setGewinnAirline(double GewinnAirline) {
    this.GewinnAirline = GewinnAirline;
  }

  public boolean isAbrechnungsFehler() {
    return AbrechnungsFehler;
  }

  public void setAbrechnungsFehler(boolean AbrechnungsFehler) {
    this.AbrechnungsFehler = AbrechnungsFehler;
  }

  public String getAbrechnungsFehlerText() {
    return AbrechnungsFehlerText;
  }

  public void setAbrechnungsFehlerText(String AbrechnungsFehlerText) {
    this.AbrechnungsFehlerText = getAbrechnungsFehlerText() + " | " + AbrechnungsFehlerText;
  }

  public int getObjektID() {
    return objektID;
  }

  public void setObjektID(int objektID) {
    this.objektID = objektID;
  }

  public String getAbfertigungsTerminal(int ID) {
    if (ID == -300) {
      return "FTW Terminal";
    }
    return facadeTakeOff.getFBOBezeichnung(ID);
  }

  public boolean isPirepOK() {
    return PirepOK;
  }

  public void setPirepOK(boolean PirepOK) {
    this.PirepOK = PirepOK;
  }

  public Date getFlugzeugRueckgabe() {
    return FlugzeugRueckgabe;
  }

  public void setFlugzeugRueckgabe(Date FlugzeugRueckgabe) {
    this.FlugzeugRueckgabe = FlugzeugRueckgabe;
  }

  public int getGewichtDerCrew() {
    return GewichtDerCrew;
  }

  public void setGewichtDerCrew(int GewichtDerCrew) {
    this.GewichtDerCrew = GewichtDerCrew;
  }

  public String getSitzKonfigBezeichnung(int IDKonfig) {
    if (IDKonfig > 0) {
      sitzKonfigBezeichnung = facadeTakeOff.getKonfig(IDKonfig).getBezeichnung();
    } else {
      sitzKonfigBezeichnung = loginMB.onSprache("Terminal_txt_KeineSitzKonfigurationVorgenommen");
    }

    return sitzKonfigBezeichnung;
  }

  public void setSitzKonfigBezeichnung(String sitzKonfigBezeichnung) {
    this.sitzKonfigBezeichnung = sitzKonfigBezeichnung;
  }

  public int getSitzeBusiness() {
    return SitzeBusiness;
  }

  public void setSitzeBusiness(int SitzeBusiness) {
    this.SitzeBusiness = SitzeBusiness;
  }

  public int getSitzeEconomy() {
    return SitzeEconomy;
  }

  public void setSitzeEconomy(int SitzeEconomy) {
    this.SitzeEconomy = SitzeEconomy;
  }

  public int getLeergewicht() {
    return Leergewicht;
  }

  public void setLeergewicht(int Leergewicht) {
    this.Leergewicht = Leergewicht;
  }

  public int getSitzeCrew() {
    return SitzeCrew;
  }

  public void setSitzeCrew(int SitzeCrew) {
    this.SitzeCrew = SitzeCrew;
  }

  public int getSitzeFlugbegleiter() {
    return SitzeFlugbegleiter;
  }

  public void setSitzeFlugbegleiter(int SitzeFlugbegleiter) {
    this.SitzeFlugbegleiter = SitzeFlugbegleiter;
  }

  public int getCargoVerfuegbar() {
    return CargoVerfuegbar;
  }

  public void setCargoVerfuegbar(int CargoVerfuegbar) {
    this.CargoVerfuegbar = CargoVerfuegbar;
  }

  public int getSpritverbrauch() {
    return Spritverbrauch;
  }

  public void setSpritverbrauch(int Spritverbrauch) {
    this.Spritverbrauch = Spritverbrauch;
  }

  public int getMaxPayload() {
    return maxPayload;
  }

  public void setMaxPayload(int maxPayload) {
    this.maxPayload = maxPayload;
  }

  public int getArrivalTerminalAbrechnung() {
    return ArrivalTerminalAbrechnung;
  }

  public void setArrivalTerminalAbrechnung(int ArrivalTerminalAbrechnung) {
    this.ArrivalTerminalAbrechnung = ArrivalTerminalAbrechnung;
  }

  public int getDepartureTerminalAbrechnung() {
    return DepartureTerminalAbrechnung;
  }

  public void setDepartureTerminalAbrechnung(int DepartureTerminalAbrechnung) {
    this.DepartureTerminalAbrechnung = DepartureTerminalAbrechnung;
  }

  public double getArriavalGebuehren() {
    return ArriavalGebuehren;
  }

  public void setArriavalGebuehren(double ArriavalGebuehren) {
    this.ArriavalGebuehren = ArriavalGebuehren;
  }

  public double getDepartureGebuehren() {
    return DepartureGebuehren;
  }

  public void setDepartureGebuehren(double DepartureGebuehren) {
    this.DepartureGebuehren = DepartureGebuehren;
  }

  public boolean isTerminalFreigeben() {
    return TerminalFreigeben;
  }

  public void setTerminalFreigeben(boolean TerminalFreigeben) {
    this.TerminalFreigeben = TerminalFreigeben;
  }

  public int getCurrentFlightMiles() {
    return currentFlightMiles;
  }

  public void setCurrentFlightMiles(int currentFlightMiles) {
    this.currentFlightMiles = currentFlightMiles;
  }

  public int getDryOperatingWeight() {
    if (rentedAircraft != null) {
      return this.DryOperatingWeight = GewichtDerCrew + Leergewicht;
    }
    return 0;
  }

  public int getZeroFullWeight() {
    return this.ZeroFullWeight = DryOperatingWeight + Payload;
  }

  public int getPayload() {
    return this.Payload = getGewichtGepaeckTakeOff() + getGewichtPassagiereTakeOff() + getGewichtCargoTakeOff();
  }

  public int getTakeOffWeight() {
    return this.TakeOffWeight = ZeroFullWeight + getSummeGewichtTreibstoff();
  }

  public int getTreibstoffVerbleibendFuerTransportdaten() {
    int zuladungVerbleibend = rentedAircraft.getHoechstAbfluggewicht() - getTakeOffWeight();
    int treibstoffVerbleibend = maxTankkapazitaet - getSummeGewichtTreibstoff();

    if (treibstoffVerbleibend < 0) {
      treibstoffVerbleibend = 0;
    }

    if (treibstoffVerbleibend > zuladungVerbleibend) {
      return zuladungVerbleibend;
    } else {
      return treibstoffVerbleibend;
    }
  }

  public int getErwarteteMeilen() {
    return ErwarteteMeilen;
  }

  public void setErwarteteMeilen(int ErwarteteMeilen) {
    this.ErwarteteMeilen = ErwarteteMeilen;
  }

  private Airport getAirportData(String icao) {
    return facadeTakeOff.getAirportByIcao(icao);
  }

  public String getZustandText() {
    return zustandText;
  }

  public void setZustandText(String zustandText) {
    this.zustandText = zustandText;
  }

  public int getErwarteteFlugzeit() {
    return ErwarteteFlugzeit;
  }

  public void setErwarteteFlugzeit(int ErwarteteFlugzeit) {
    this.ErwarteteFlugzeit = ErwarteteFlugzeit;
  }

  public double getSitzKonfigFaktor() {
    return SitzKonfigFaktor;
  }

  public void setSitzKonfigFaktor(double SitzKonfigFaktor) {
    this.SitzKonfigFaktor = SitzKonfigFaktor;
  }

  public int getTankMengeKilo() {
    return TankMengeKilo;
  }

  public void setTankMengeKilo(int TankMengeKilo) {
    this.TankMengeKilo = TankMengeKilo;
  }

  public int getTankBisFuellstandKG() {
    return TankBisFuellstandKG;
  }

  public void setTankBisFuellstandKG(int TankBisFuellstandKG) {
    this.TankBisFuellstandKG = TankBisFuellstandKG;
  }

  public int getTankstelleTreibstoffArt() {
    return TankstelleTreibstoffArt;
  }

  public void setTankstelleTreibstoffArt(int TankstelleTreibstoffArt) {
    this.TankstelleTreibstoffArt = TankstelleTreibstoffArt;
  }

  public int getTankstelleSpritMenge() {
    return TankstelleSpritMenge;
  }

  public void setTankstelleSpritMenge(int TankstelleSpritMenge) {
    this.TankstelleSpritMenge = TankstelleSpritMenge;
  }

  public int getTankstelleMaxSpritMenge() {
    return TankstelleMaxSpritMenge;
  }

  public void setTankstelleMaxSpritMenge(int TankstelleMaxSpritMenge) {
    this.TankstelleMaxSpritMenge = TankstelleMaxSpritMenge;
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

  public boolean isTankenBezahlt() {
    return TankenBezahlt;
  }

  public void setTankenBezahlt(boolean TankenBezahlt) {
    this.TankenBezahlt = TankenBezahlt;
  }

  public double getTankMengeProzent() {
    return TankMengeProzent;
  }

  public void setTankMengeProzent(double TankMengeProzent) {
    this.TankMengeProzent = TankMengeProzent;
  }

  public int getTankRestMengeSprit() {
    return TankRestMengeSprit;
  }

  public void setTankRestMengeSprit(int TankRestMengeSprit) {
    this.TankRestMengeSprit = TankRestMengeSprit;
  }

  public String getTankstellenName() {
    return TankstellenName;
  }

  public void setTankstellenName(String TankstellenName) {
    this.TankstellenName = TankstellenName;
  }

  public String getTankstellenGrafik() {
    return TankstellenGrafik;
  }

  public void setTankstellenGrafik(String TankstellenGrafik) {
    this.TankstellenGrafik = TankstellenGrafik;
  }

  public int getMaxTankkapazitaet() {
    return maxTankkapazitaet;
  }

  public void setMaxTankkapazitaet(int maxTankkapazitaet) {
    this.maxTankkapazitaet = maxTankkapazitaet;
  }

  public boolean isTankenFreigeben() {
    return TankenFreigeben;
  }

  public void setTankenFreigeben(boolean TankenFreigeben) {
    this.TankenFreigeben = TankenFreigeben;
  }

  public boolean isIstPrivateTankstelleVorhanden() {
    return istPrivateTankstelleVorhanden;
  }

  public void setIstPrivateTankstelleVorhanden(boolean istPrivateTankstelleVorhanden) {
    this.istPrivateTankstelleVorhanden = istPrivateTankstelleVorhanden;
  }

  public int getTreibstoffArt() {
    return TreibstoffArt;
  }

  public void setTreibstoffArt(int TreibstoffArt) {
    this.TreibstoffArt = TreibstoffArt;
  }

  public boolean isWirdUmgebaut() {
    return wirdUmgebaut;
  }

  public void setWirdUmgebaut(boolean wirdUmgebaut) {
    this.wirdUmgebaut = wirdUmgebaut;
  }

  public Date getUmbauZeit() {
    return umbauZeit;
  }

  public void setUmbauZeit(Date umbauZeit) {
    this.umbauZeit = umbauZeit;
  }

  public double getBoniBonusSystem() {
    return BoniBonusSystem;
  }

  public void setBoniBonusSystem(double BoniBonusSystem) {
    this.BoniBonusSystem = BoniBonusSystem;
  }

  public String getBoniBezeichnung() {
    return BoniBezeichnung;
  }

  public void setBoniBezeichnung(String BoniBezeichnung) {
    this.BoniBezeichnung = BoniBezeichnung;
  }

  public double getBoniBonusSystemWert() {
    return BoniBonusSystemWert;
  }

  public void setBoniBonusSystemWert(double BoniBonusSystemWert) {
    this.BoniBonusSystemWert = BoniBonusSystemWert;
  }

  public double getStartgebuehr() {
    return startgebuehr;
  }

  public void setStartgebuehr(double startgebuehr) {
    this.startgebuehr = startgebuehr;
  }

  public double getFlugzeugFixkosten() {
    return FlugzeugFixkosten;
  }

  public void setFlugzeugFixkosten(double FlugzeugFixkosten) {
    this.FlugzeugFixkosten = FlugzeugFixkosten;
  }

  public void saveMail(String Kategorie, String Von, String An, String Betreff, String Nachricht) {

    //Absendermail speichern
    Benutzer absender = getUserDaten(Von);
    Benutzer empfaenger = getUserDaten(An);

    Mail nm = new Mail();

    nm.setUserID(absender.getIdUser());
    nm.setAnID(empfaenger.getIdUser());
    nm.setAnUser(empfaenger.getName1());
    nm.setBetreff(Betreff);
    nm.setDatumZeit(new Date());
    nm.setGelesen(false);
    nm.setKategorie(Kategorie);
    nm.setVonID(absender.getIdUser());
    nm.setVonUser(absender.getName1());
    nm.setNachrichtText(CONF.NoScript(Nachricht));
    facadeTakeOff.saveUserMail(nm);

    //Empfaengermail speichern
    if (!An.equals(Von)) {
      nm = new Mail();
      nm.setUserID(empfaenger.getIdUser());
      nm.setAnID(empfaenger.getIdUser());
      nm.setAnUser(empfaenger.getName1());
      nm.setBetreff(Betreff);
      nm.setDatumZeit(new Date());
      nm.setGelesen(false);
      nm.setKategorie(Kategorie);
      nm.setVonID(absender.getIdUser());
      nm.setVonUser(absender.getName1());
      nm.setNachrichtText(CONF.NoScript(Nachricht));
      facadeTakeOff.saveUserMail(nm);
    }

  }

  private Benutzer getUserDaten(String BenutzerName) {
    return facadeTakeOff.findUser(BenutzerName);
  }

  private String MailNachrichtTimeCompression(String Meldung) {
    String nachricht = Meldung + "<b>Flug Nr: " + PirepFlugNummer
            + "</b><br><br> User :  " + (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName")
            + "<br> Flugzeug " + rentedAircraft.getType()
            + "<br> Registrierung " + rentedAircraft.getRegistrierung()
            + "<br> Von - Nach: " + PirepFromICAO + " > " + PirepToICAO
            + "<br> Erwartete Zeit: " + ErwarteteFlugzeit
            + "<br> geflogene Zeit: " + FlightTime
            + "<br> geflogene Meilen: " + currentFlightMiles
            + "<br> erwartete Meilen: " + ErwarteteMeilen
            + "<br> ACARS Durchschnittsgeschwindigkeit: " + dgeschw
            + "<br><br><b>Das MOD wurde darüber Informiert! "
            + "<br><br>Es ist verboten Time-Kompression zu benutzen! "
            + "<br>It is forbidden to use time compression! ";
    return nachricht;
  }

  private String MailNachrichtGeschwindigkeit(String Meldung, double MaxGeschwindigkeit, double GeflogeneGeschwindigkeit) {
    String nachricht = Meldung + "<b>Flug Nr: " + PirepFlugNummer
            + "</b><br><br> User :  " + (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName")
            + "<br> Von - Nach: " + PirepFromICAO + " > " + PirepToICAO
            + "<br> Flugzeug " + rentedAircraft.getType()
            + "<br> Registrierung " + rentedAircraft.getRegistrierung()
            + "<br> Flugzeug ID " + rentedAircraft.getIdMietKauf()
            + "<br> Höchstgeschwindigkeit Flugzeug: " + dfWaehrung.format(MaxGeschwindigkeit)
            + "<br> Geflogene Geschwindigkeit: " + dfWaehrung.format(GeflogeneGeschwindigkeit)
            + "<br> geflogene Meilen: " + currentFlightMiles;

    return nachricht;
  }

  private String MailNachrichtZuWenigMeilen(String Meldung) {
    String nachricht = Meldung + "<b>Flug Nr: " + PirepFlugNummer
            + "</b><br><br> User :  " + (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName")
            + "<br> Flugzeug " + rentedAircraft.getType()
            + "<br> Registrierung " + rentedAircraft.getRegistrierung()
            + "<br> Von - Nach: " + PirepFromICAO + " > " + PirepToICAO
            + "<br> Erwartete Zeit: " + ErwarteteFlugzeit
            + "<br> geflogene Zeit: " + FlightTime
            + "<br> geflogene Meilen: " + currentFlightMiles
            + "<br> erwartete Meilen: " + ErwarteteMeilen
            + "<br> ACARS Durchschnittsgeschwindigkeit: " + dgeschw
            + "<br><br><b>Das MOD wurde darüber Informiert! ";
    return nachricht;
  }

  private String MailNachrichtSprit(String Meldung, int istSprit, int Flugsprit, int Pax, int Cargo) {
    String nachricht = Meldung + "<b>Flug Nr: " + PirepFlugNummer
            + "</b><br><br> User :  " + (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName")
            + "<br> Flugzeug " + rentedAircraft.getType()
            + "<br> Registrierung " + rentedAircraft.getRegistrierung()
            + "<br> Von - Nach: " + PirepFromICAO + " > " + PirepToICAO
            + "<br> Spritverbrauch Stammdaten / Std.: " + istSprit
            + "<br> Spritverbrauch Flug / Std.: " + Flugsprit
            + "<br> geflogene Zeit: " + FlightTime
            + "<br> geflogene Meilen: " + currentFlightMiles
            + "<br> Durchschnittsgeschwindigkeit: " + dgeschw
            + "<br> Transport Cargo: " + Cargo
            + "<br> Transport Pax: " + Pax
            + "<br><br><b>Das MOD wurde darüber Informiert! ";
    return nachricht;
  }

  public Yaacarskopf getSupportFreigabeFlug() {
    return SupportFreigabeFlug;
  }

  public void setSupportFreigabeFlug(Yaacarskopf SupportFreigabeFlug) {
    this.SupportFreigabeFlug = SupportFreigabeFlug;
  }

}
