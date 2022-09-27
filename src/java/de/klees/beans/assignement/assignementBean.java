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

package de.klees.beans.assignement;

import de.klees.beans.system.CONF;
import de.klees.beans.system.loginMB;
import static de.klees.beans.system.loginMB.onSprache;
import de.klees.data.Airport;
import de.klees.data.Airportjobtemplate;
import de.klees.data.Assignement;
import de.klees.data.Feinabstimmung;
import de.klees.data.FlugesellschaftPiloten;
import de.klees.data.Fluggesellschaft;
import de.klees.data.Fluggesellschaftmanager;
import de.klees.data.Flugzeuge;
import de.klees.data.JobBeschreibungen;
import de.klees.data.Sitzkonfiguration;
import de.klees.data.Benutzer;
import de.klees.data.UserFavoriten;
import de.klees.data.views.ViewAirportAnflugZiele;
import de.klees.data.views.ViewAssiErweiterteLizenzPruefung;
import de.klees.data.views.ViewFluggesellschaftPiloten;
import de.klees.data.views.ViewFlugzeugeMietKauf;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
public class assignementBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private String SuchText;
  private Assignement assignment;

  private Assignement currentAssignment;
  private Benutzer owner;
  private ViewFlugzeugeMietKauf MeinGemietetesFlugzeug;
  private Sitzkonfiguration sitzKonfiguration;
  private Feinabstimmung config;
  private List<Assignement> Assignments;
  private List<Assignement> AssignmentsHold;
  private List<Assignement> selectedAssignments;
  private List<Assignement> selectedHoldAssignments;
  private List<Assignement> filteredAssignmenst;
  private Map<String, String> zielFlughaefen;
  private Map<String, String> abFlughaefen;
  private String zielFlughafenSelected;
  private int OwnerID;
  private boolean ViewMyFlight;
  private boolean ViewHoldArea;
  private boolean ViewAssignments;

  private boolean isLoaded;
  private boolean isLoadedHold;

  private String headerText;
  private final int Norden;
  private final int Osten;
  private final int Sueden;
  private final int Westen;
  private final int NullPunkt;
  private int TableHight;
  private String ICAOS;
  private String FlughafenName;
  private int Gepaeck;
  private int PassagiereGewicht;
  private String reverseSuch;
  private boolean reverse;
  private int UserID;
  private int FluggesellschaftID;
  private boolean hatUserEineFluggesellschaft;
  private boolean auftragIstOeffentlich;
  private double frmBonusOeffentlich;
  private double frmBonusFGPiloten;
  private double frmProvision;
  private boolean Auftragslauf;
  private boolean darfKonvertieren;

  private String FlugzeugTyp;
  private int SitzeBusiness;
  private int SitzeEconomy;
  private int CargoVerfuegbar;
  private int maxPayload;

  private boolean IsAirportAgent;
  private AgentJobList selectedAgentJob;
  private ArrayList<AgentJobList> Joblist = new ArrayList<>();

  List<JobBeschreibungen> p1Jobs;
  List<JobBeschreibungen> p2Jobs;
  List<JobBeschreibungen> p3Jobs;
  List<JobBeschreibungen> p4Jobs;
  List<JobBeschreibungen> p5Jobs;
  List<JobBeschreibungen> p6Jobs;
  List<JobBeschreibungen> p7Jobs;
  List<JobBeschreibungen> c1Jobs;
  List<JobBeschreibungen> c2Jobs;
  List<JobBeschreibungen> c3Jobs;
  List<JobBeschreibungen> c4Jobs;
  List<JobBeschreibungen> c5Jobs;
  List<JobBeschreibungen> c6Jobs;

  //******************* {Min, Anzahl}
  // P1 nur 1 Pax
  int[] p2Max = new int[]{1, 3};  // zwischen 1 und 3
  int[] p3Max = new int[]{2, 5}; // zwischen 2 und 5
  int[] p4Max = new int[]{5, 10}; // zwischen 5 und 10
  int[] p5Max = new int[]{10, 19}; // zwischen 10 und 19
  int[] p6Max = new int[]{20, 30}; // zwischen 20 und 30

  int[] p7Max = new int[]{1, 3}; // zwischen 1 und 3

  int[] c2Max = new int[]{1, 100};
  int[] c3Max = new int[]{50, 500};
  int[] c4Max = new int[]{250, 1000};
  int[] c5Max = new int[]{500, 5000};
  int[] c6Max = new int[]{50, 500};

  // Jobzuweisung je nach Flughafenklasse : VonPJob bisPJob
  int[] Classe1P = new int[]{4, 6};
  int[] Classe2P = new int[]{3, 6};
  int[] Classe3P = new int[]{2, 6};
  int[] Classe4P = new int[]{1, 5};
  int[] Classe5P = new int[]{1, 4};
  int[] Classe6P = new int[]{1, 3};
  int[] Classe7P = new int[]{1, 2};
  int[] Classe8P = new int[]{1, 1};
  int[] Classe9P = new int[]{1, 2};

  int zaehlerPax;
  int zaehlerCargo;

  int minAuftraege;
  int minCargo;

  int ErzeugteJobsImDurchlauf;

  String MaxZeilen;

  Feinabstimmung fa;

  //Variablen Aktivierung Zeitverzögerung
  private long oldTime = new Date().getTime();
  private boolean firstRun;

  // Mapdarstellung
  private List<Assignement> mapAssignment;
  private double AcarsLatitude;
  private double AcarsLongitute;
  private boolean VisuellAuftragDialog;
  private final DecimalFormat df = new DecimalFormat("#,##0");

  private int idFuerUserAuftraege;

  @EJB
  AssignementFacade facadeAssignment;

  public assignementBean() {
    this.firstRun = true;

    Norden = 360;
    Sueden = 180;
    Westen = 270;
    Osten = 90;
    NullPunkt = 0;
    TableHight = 10;
    reverseSuch = "vonICAO";
    reverse = false;
    Auftragslauf = false;
    auftragIstOeffentlich = true;
    FluggesellschaftID = -1;
    darfKonvertieren = false;

    OwnerID = -1;
    ViewAssignments = true;
    ViewMyFlight = false;
    ViewHoldArea = false;
    headerText = "";

    zielFlughafenSelected = "";
    isLoaded = false;
    isLoadedHold = false;
    setHatUserEineFluggesellschaft(false);
    UserID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
    SuchText = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Assignment_ICAO");
    //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Assignment_ICAO", "");

    if (SuchText == null) {
      SuchText = "";
    }

    IsAirportAgent = false;
  }

  public List<Assignement> getAssignments() {

    SuchText = SuchText.toUpperCase();

    if (!isLoaded) {
      config = facadeAssignment.readConfig();
      isLoaded = true;

      if (ViewMyFlight) {
        Assignments = facadeAssignment.findByMyFlight(OwnerID);
        mapAssignment = facadeAssignment.findByMyFlightMap(OwnerID);

      } else if (ViewAssignments) {
        Airport airport = facadeAssignment.getAirportData(SuchText);
        if (!"".equals(zielFlughafenSelected)) {
          Assignments = facadeAssignment.findByICAOLocationAndtoIcao(SuchText, zielFlughafenSelected, -1);
          mapAssignment = facadeAssignment.findByICAOLocationAndtoIcaoMap(SuchText, zielFlughafenSelected, -1);
          if (Assignments.size() > 0) {
            if (airport != null) {
              setFlughafenName(airport.getName());
            } else {
              setFlughafenName("not found");
            }
          }
        } else {

          if (reverseSuch.equals("vonICAO")) {
            Assignments = facadeAssignment.findByICAOLocation(SuchText, -1);
            mapAssignment = facadeAssignment.findByICAOLocationMap(SuchText, -1);
          }

          if (reverseSuch.equals("nachICAO")) {
            Assignments = facadeAssignment.findNachIcao(SuchText, -1);
          }

          if (Assignments.size() > 0) {
            if (airport != null) {
              setFlughafenName(airport.getName());
            } else {
              setFlughafenName("not found");
            }
          }

        }
      }
    }
    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Assignment_ICAO", SuchText);

    return Assignments;

  }

  public List<UserFavoriten> getFavoriten() {
    return facadeAssignment.getFavoriten(UserID);
  }

  public List<Assignement> getHoldAssignements() {
    if (!isLoadedHold) {
      isLoadedHold = true;
      OwnerID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
      AssignmentsHold = facadeAssignment.findByMyFlightHold(OwnerID);
      filteredAssignmenst = AssignmentsHold;
    }
    return AssignmentsHold;
  }

  public List<ViewFluggesellschaftPiloten> getPilotFluggesellschaften() {
    return facadeAssignment.getFluggesellschaften(UserID);
  }

  public List<Fluggesellschaft> getFluggesellschaften() {

    int fgid = facadeAssignment.readUser(UserID).getFluggesellschaftManagerID();

    try {
      if (fgid > 0) {
        Fluggesellschaftmanager mg = facadeAssignment.readManager(UserID, fgid);
        if (mg.getAllowRoutenBearbeiten()) {
          hatUserEineFluggesellschaft = true;
          return facadeAssignment.getFGsFuerManager(UserID, fgid);
        }
      }
    } catch (Exception e) {
    }

    return facadeAssignment.getFGs(UserID);

  }

  public List<ViewAssiErweiterteLizenzPruefung> getUserAuftraege() {
    return facadeAssignment.getUserAuftraegeFuerSupport(idFuerUserAuftraege);
  }

  public void onChangeFluggesellschaft() {

    // den Zielflughafen mit in die Suche einbeziehen
    if (SuchText.equals("")) {
      NewMessage(loginMB.onSprache("Auftragsplanung_msg_keinIcaoAngegeben"));
    } else {
      if (FluggesellschaftID == 0) {
        Assignments = facadeAssignment.findByICAOLocation(SuchText, -1);
        mapAssignment = facadeAssignment.findByICAOLocationMap(SuchText, -1);
      } else {
        Assignments = facadeAssignment.findByFluggesellschaft(FluggesellschaftID, SuchText, zielFlughafenSelected);
        mapAssignment = facadeAssignment.findByFluggesellschaftMap(FluggesellschaftID, SuchText, zielFlughafenSelected);
      }
    }
  }

  public void onViewMyFlight() {
    OwnerID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
    headerText = onSprache("Auftragsbestand_pnl_header_wartehalle");
    filteredAssignmenst = null;
//    selectedAssignments = null;
//    selectedHoldAssignments = null;
    isLoadedHold = false;
    isLoaded = false;
    ViewMyFlight = true;
    ViewHoldArea = true;
  }

  public void onViewAssignments() {
    headerText = onSprache("Auftragsplanung_msg_AuftraegeAmFlughafen");
    isLoaded = false;
    OwnerID = -1;
    filteredAssignmenst = null;
//    selectedAssignments = null;
//    selectedHoldAssignments = null;
    ViewAssignments = true;
    ViewMyFlight = false;
    ViewHoldArea = false;
  }

  public void onSuchText() {

    if (SuchText.contains(",")) {
      SuchText = SuchText.substring(0, SuchText.indexOf(","));
    }

    if (reverseSuch.equals("nachICAO")) {
      String OldSuche = SuchText;
      SuchText = zielFlughafenSelected;
      zielFlughafenSelected = OldSuche;

      reverseSuch = "vonICAO";
      reverse = false;
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Assignment_ICAO", SuchText.toUpperCase());

    }

    if (FluggesellschaftID > 0) {
      onChangeFluggesellschaft();
    }

    ViewMyFlight = false;
    ViewHoldArea = false;
    ViewAssignments = true;
    isLoaded = false;
  }

  public void onSucheFluggesellschaft() {
    onChangeFluggesellschaft();
  }

  public void onChangeIcao() {
    reverse = reverseSuch.equals("nachICAO");
    isLoaded = false;
  }

  /*
  *************** Konvertieren Beginn 
   */
  public void onStandardWerteFuerConvertSetzen() {

    if (FluggesellschaftID == -1) {
      if (getFluggesellschaften() != null) {
        if (getFluggesellschaften().size() > 0) {
          FluggesellschaftID = getFluggesellschaften().get(0).getIdFluggesellschaft();
        }
      }
    }

    // Daten der Fluggesellschaft auslesen
    if (FluggesellschaftID > 0) {

      Fluggesellschaft fg = facadeAssignment.readFg(FluggesellschaftID);

      frmBonusFGPiloten = fg.getStdBonus2();
      frmBonusOeffentlich = fg.getStdBonus1();
      frmProvision = fg.getStdProvision();
    }

  }

  public void onConvertAuftraege() {

    String fgNameAirline = "";
    String fgIcaoCode = "";
    String fgLogoUrl = "";
    String fgCreatedByUser = "";
    boolean KonvertOK = false;
    int idFlugzeug = -1;
    String Lizenz = "";

    long Verfallszeit = 96 * 60 * 60 * 1000;
    long neueZeit = 0;

    String CeoAirline = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName");

    try {
      ViewFlugzeugeMietKauf meinFlugzeug = facadeAssignment.getRentedAircraft(UserID);
      idFlugzeug = meinFlugzeug.getIdMietKauf();
      Lizenz = meinFlugzeug.getLizenz();
      KonvertOK = true;
    } catch (NullPointerException e) {
      NewMessage("Kein Flugzeug gemietet");
    }

    if (KonvertOK) {

      //Airline-Job konvertieren
      if (FluggesellschaftID > 0) {
        //System.out.println(" ID " + FluggesellschaftID);

        // Daten der Fluggesellschaft auslesen
        Fluggesellschaft fg = facadeAssignment.readFg(FluggesellschaftID);

        fgNameAirline = fg.getName();
        CeoAirline = fg.getBesitzerName();
        fgIcaoCode = fg.getIcaoCode();
        fgLogoUrl = fg.getLogoURL();
        fgCreatedByUser = fg.getName();

        for (Assignement sel : selectedAssignments) {
          // Type 1=Routen Job, 2=Standard-Job, 3=Random-Job, 4=Linien-Job, 5=Airport Agent
          if (sel.getUserlock() == 0 && !(sel.getType().equals(3) || sel.getType().equals(5))) {
            // Bei System-Jobs keine Mengenveraenderung bei der Fluggesellschaft
            if (sel.getIdAirline() > 0) {

              FlugesellschaftPiloten pilot = facadeAssignment.readPilot(UserID, sel.getIdAirline());

              if (pilot != null) {
                if (pilot.getDarfkonvertieren()) {

                  // Type 1=PAX, 2=CARGO, 3=TRF, 4=Business-PAX, 5 Spritfaesser
                  //Paxe, eco und bc
                  if (sel.getRoutenArt().equals(1) || sel.getRoutenArt().equals(4)) {
                    fg.setErzeugteJobs(fg.getErzeugteJobs() + sel.getAmmount());
                    facadeAssignment.savaFluggesellschaft(fg);
                    Fluggesellschaft quellfg = facadeAssignment.readFg(sel.getIdAirline());
                    quellfg.setErzeugteJobs(quellfg.getErzeugteJobs() - sel.getAmmount());
                    facadeAssignment.savaFluggesellschaft(quellfg);
                  }
                  //Cargo
                  if (sel.getRoutenArt().equals(2)) {
                    fg.setErzeugtesCargo(fg.getErzeugtesCargo() + sel.getAmmount());
                    facadeAssignment.savaFluggesellschaft(fg);
                    Fluggesellschaft quellfg = facadeAssignment.readFg(sel.getIdAirline());
                    quellfg.setErzeugtesCargo(quellfg.getErzeugtesCargo() - sel.getAmmount());
                    facadeAssignment.savaFluggesellschaft(quellfg);
                  }
                  sel.setCreatedbyuser(fgCreatedByUser);
                  sel.setAirlineLogo(fgLogoUrl);
                  sel.setCeoAirline(CeoAirline);
                  sel.setIcaoCodeFluggesellschaft(fgIcaoCode);
                  sel.setNameairline(fgNameAirline);
                  sel.setIdAirline(FluggesellschaftID);
                  sel.setFlugrouteName(fgNameAirline);

                  //Lizenz und Flugzeugklasse hinzugefügt
                  //Lizenz und Klasse wird nur beim ersten Konvertierung ergänzt.
                  if (sel.getIdaircraft().equals(-1)) {
                    sel.setIdaircraft(idFlugzeug);
                    sel.setLizenz(Lizenz);
                  }

                  sel.setBonusclosed(frmBonusFGPiloten);

                  if (frmBonusFGPiloten < 0.0) {
                    sel.setBonusclosed(0.0);
                  }
                  if (frmBonusFGPiloten > 100.0) {
                    sel.setBonusclosed(100.0);
                  }

                  sel.setBonusoeffentlich(frmBonusOeffentlich);

                  if (frmBonusOeffentlich < 0.0) {
                    sel.setBonusoeffentlich(0.0);
                  }

                  if (frmBonusOeffentlich > 100.0) {
                    sel.setBonusoeffentlich(100.0);
                  }

                  sel.setProvision(frmProvision);

                  if (frmProvision > 100.0) {
                    sel.setProvision(100.0);
                  }
                  if (frmProvision < 0.0) {
                    sel.setProvision(0.0);
                  }

                  sel.setIdowner(sel.getIdowner());
                  sel.setKonvertiert(true);
                  if (auftragIstOeffentlich) {
                    sel.setOeffentlich(1);
                  } else {
                    sel.setOeffentlich(0);
                  }

                  if (!sel.getVerlaengert()) {
                    neueZeit = new Date().getTime();
                    sel.setExpires(new Date(neueZeit + Verfallszeit));
                    sel.setVerlaengert(true);
                  }

                  facadeAssignment.saveAuftrag(sel);
                }
              }
            }

            // Systemjob Konvertieren
            if (sel.getIdAirline() < 0) {
              sel.setCreatedbyuser(fgCreatedByUser);
              sel.setAirlineLogo(fgLogoUrl);
              sel.setCeoAirline(CeoAirline);
              sel.setIcaoCodeFluggesellschaft(fgIcaoCode);
              sel.setNameairline(fgNameAirline);
              sel.setIdAirline(FluggesellschaftID);
              sel.setFlugrouteName(fgNameAirline);

              //Lizenz und Flugzeugklasse hinzugefügt
              //Lizenz und Klasse wird nur beim ersten Konvertierung ergänzt.
              if (sel.getIdaircraft().equals(-1)) {
                sel.setIdaircraft(idFlugzeug);
                sel.setLizenz(Lizenz);
              }

              sel.setBonusclosed(frmBonusFGPiloten);

              if (frmBonusFGPiloten < 0.0) {
                sel.setBonusclosed(0.0);
              }
              if (frmBonusFGPiloten > 100.0) {
                sel.setBonusclosed(100.0);
              }

              sel.setBonusoeffentlich(frmBonusOeffentlich);

              if (frmBonusOeffentlich < 0.0) {
                sel.setBonusoeffentlich(0.0);
              }

              if (frmBonusOeffentlich > 100.0) {
                sel.setBonusoeffentlich(100.0);
              }

              sel.setProvision(frmProvision);

              if (frmProvision > 100.0) {
                sel.setProvision(100.0);
              }
              if (frmProvision < 0.0) {
                sel.setProvision(0.0);
              }

//            sel.setBonusclosed(frmBonusFGPiloten);
//            sel.setBonusoeffentlich(frmBonusOeffentlich);
//            sel.setProvision(frmProvision);
              sel.setIdowner(sel.getIdowner());
              sel.setKonvertiert(true);
              if (auftragIstOeffentlich) {
                sel.setOeffentlich(1);
              } else {
                sel.setOeffentlich(0);
              }

              if (!sel.getVerlaengert()) {
                neueZeit = new Date().getTime();
                sel.setExpires(new Date(neueZeit + Verfallszeit));
                sel.setVerlaengert(true);
              }

              facadeAssignment.saveAuftrag(sel);
            }

          }
        }

        if (ViewMyFlight) {
          onViewMyFlight();
        } else {
          onViewAssignments();
        }

      }
    } else {
      NewMessage("Kein Flugzeug gemietet");
    }
  }

  public boolean isDarfKonvertiern() {

    FlugesellschaftPiloten fp = facadeAssignment.readPilot(UserID, FluggesellschaftID);

    if (fp != null) {

      if (fp.getDarfkonvertieren() != null) {
        return fp.getDarfkonvertieren();
      } else {
        System.out.println("de.klees.beans.assignement.assignementBean.isDarfKonvertiern() ist null ");
        return false;
      }
    } else {
      return false;
    }
  }

  private boolean isAirlinePilotForSplitting(int AirlineID) {
    FlugesellschaftPiloten fp = facadeAssignment.readPilot(UserID, FluggesellschaftID);
    if (fp != null) {
      return true;
    }
    return false;
  }

  /*
  *************** Konvertieren ENDE
   */
 /*
  ************** Airport Agent BEGINN
   */
  public int getRestLadekapazitaet(int maxPayload, int maxCargo, int maxAbfluggewicht, int GewichtPax, int GewichtGepaeck, int GewichtSprit) {

    int Beladung = GewichtPax + GewichtGepaeck + GewichtSprit;
    int Reserve = (int) (maxCargo * 0.10);

    if (Beladung <= maxAbfluggewicht) {

      if (maxPayload == maxCargo) {
        int Rest = maxPayload - Beladung;
        if (Rest - Reserve > 0) {
          return Rest;
        } else {
          return 0;
        }
      } else {
        int Rest = maxCargo - (GewichtGepaeck + Reserve);
        int GesGewicht = Rest + GewichtPax + GewichtGepaeck;
        int Abzug = maxPayload - GesGewicht;

        if (GesGewicht > maxPayload) {
          Rest = Rest + Abzug;
        }

        if (Rest > 0) {
          return Rest;
        } else {
          return 0;
        }
      }
    }

    return 0;
  }

  public void onAirportAgent() {
    IsAirportAgent = true;
    Joblist.clear();

    ViewFlugzeugeMietKauf meinFlugzeug = facadeAssignment.getRentedAircraft(UserID);

    if (meinFlugzeug != null && !SuchText.equals("") && meinFlugzeug.getAktuellePositionICAO().equals(SuchText)) {
      if (meinFlugzeug.getMaxAnzahlZuBelgenderSitze() > 0 || meinFlugzeug.getCargo() > 0) {

        config = facadeAssignment.readConfig();

        Flugzeuge Stammflugzeug = facadeAssignment.readStammflugzeug(meinFlugzeug.getIdFlugzeug());

        Airport abflug = facadeAssignment.getAirportByIcao(SuchText);

        //int maxPax = meinFlugzeug.getMaxAnzahlZuBelgenderSitze();
        int maxPax = meinFlugzeug.getSitzeBusinessClass() + meinFlugzeug.getSitzeEconomy();
        int maxCargo = meinFlugzeug.getCargo();
        int maxSpeed = meinFlugzeug.getReisegeschwindigkeitTAS();
        int maxReichweite = Stammflugzeug.getMaxReichweite();
        int idFlugzeug = meinFlugzeug.getIdMietKauf();
        int CrewAnzahl = meinFlugzeug.getBesatzung() + meinFlugzeug.getFlugbegleiter();
        int Leergewicht = meinFlugzeug.getLeergewicht();
        int MaxMTOW = meinFlugzeug.getHoechstAbfluggewicht();
        int PayLoad = meinFlugzeug.getPayload();
        int RestTankMenge = meinFlugzeug.getAktuelleTankfuellung();
        int GewichtSprit = 0;

        if (meinFlugzeug.getIdSitzKonfiguration() > 0) {
          Sitzkonfiguration sk = facadeAssignment.getSitzKonfiguration(meinFlugzeug.getIdSitzKonfiguration());
          if (sk != null) {
            maxPax = sk.getSitzeBusiness() + sk.getSitzeEconomy();
            maxCargo = sk.getCargo();
            CrewAnzahl = sk.getCrew();
            PayLoad = sk.getMaxPayload();
          }
        }

        int BezahlungPilot = 0;
        double Taxiing = 25;

        int minEntfernung = 0;
        int maxEnfernung = 0;
        int minKlasse = 1;
        int maxKlasse = abflug.getKlasse() + 2;

        if (maxKlasse > 8) {
          maxKlasse = 8;
        }

        if (minKlasse == maxKlasse) {
          maxKlasse = 4;
        }

        if (maxPax < 5) {
          minEntfernung = 30;
          maxEnfernung = maxReichweite;
          BezahlungPilot = 7000;
          maxKlasse = 8;
        } else if (maxPax >= 5 && maxPax < 10) {
          minEntfernung = 30;
          maxEnfernung = maxReichweite;
          BezahlungPilot = 7000;
          maxKlasse = 8;
        } else if (maxPax >= 10 && maxPax < 20) {
          minEntfernung = 50;
          maxEnfernung = maxReichweite;
          BezahlungPilot = 7000;
          maxKlasse = 7;
        } else if (maxPax >= 20 && maxPax < 50) {
          minEntfernung = 70;
          maxEnfernung = maxReichweite;
          BezahlungPilot = 7000;
          maxKlasse = 6;
        } else if (maxPax >= 50 && maxPax < 100) {
          minEntfernung = 200;
          maxEnfernung = maxReichweite;
          BezahlungPilot = 7000;
          Taxiing = 30;
          maxKlasse = 6;
        } else if (maxPax >= 100 && maxPax < 150) {
          minEntfernung = 200;
          maxEnfernung = maxReichweite;
          BezahlungPilot = 7000;
          Taxiing = 30;
          maxKlasse = 5;
        } else if (maxPax >= 150 && maxPax < 200) {
          minEntfernung = 350;
          maxEnfernung = maxReichweite;
          BezahlungPilot = 6000;
          maxKlasse = 5;
          Taxiing = 30;
        } else if (maxPax >= 200 && maxPax < 250) {
          minEntfernung = 500;
          maxEnfernung = maxReichweite;
          BezahlungPilot = 5500;
          maxKlasse = 5;
          Taxiing = 30;
        } else if (maxPax >= 250 && maxPax < 300) {
          minEntfernung = 500;
          maxEnfernung = maxReichweite;
          BezahlungPilot = 5500;
          Taxiing = 45;
          maxKlasse = 4;
        } else if (maxPax >= 300) {
          minEntfernung = 1000;
          maxEnfernung = maxReichweite;
          BezahlungPilot = 5500;
          Taxiing = 45;
          maxKlasse = 4;
        }

        if (maxPax == 0) {
          if (maxCargo < 2000) {
            minEntfernung = 100;
            maxEnfernung = maxReichweite;
            BezahlungPilot = 5000;
            Taxiing = 30;
            maxKlasse = 8;
          } else if (maxCargo >= 2000 && maxCargo < 4000) {
            minEntfernung = 150;
            maxEnfernung = maxReichweite;
            BezahlungPilot = 5000;
            Taxiing = 30;
            maxKlasse = 7;
          } else if (maxCargo >= 4000 && maxCargo < 8000) {
            minEntfernung = 350;
            maxEnfernung = maxReichweite;
            BezahlungPilot = 3000;
            Taxiing = 30;
            maxKlasse = 6;
          } else if (maxCargo >= 8000 && maxCargo < 10000) {
            minEntfernung = 350;
            maxEnfernung = maxReichweite;
            BezahlungPilot = 3000;
            Taxiing = 30;
            maxKlasse = 5;
          } else if (maxCargo >= 10000 && maxCargo < 20000) {
            minEntfernung = 400;
            maxEnfernung = maxReichweite;
            BezahlungPilot = 3500;
            Taxiing = 30;
            maxKlasse = 5;
          } else if (maxCargo >= 20000 && maxCargo < 40000) {
            minEntfernung = 400;
            maxEnfernung = maxReichweite;
            BezahlungPilot = 3500;
            maxKlasse = 5;
            Taxiing = 30;
          } else if (maxCargo >= 40000 && maxCargo < 60000) {
            minEntfernung = 500;
            maxEnfernung = maxReichweite;
            BezahlungPilot = 3800;
            maxKlasse = 4;
            Taxiing = 30;
          } else if (maxCargo >= 60000 && maxCargo < 100000) {
            minEntfernung = 500;
            maxEnfernung = maxReichweite;
            BezahlungPilot = 4000;
            maxKlasse = 4;
            Taxiing = 45;
          } else if (maxCargo >= 100000) {
            minEntfernung = 1000;
            maxEnfernung = maxReichweite;
            BezahlungPilot = 5000;
            maxKlasse = 4;
            Taxiing = 45;
          }
        }

        if (abflug != null) {

          double bgVon = 0.0;
          double bgBis = 0.0;
          double lgVon = 0.0;
          double lgBis = 0.0;
          double Umkreis = 180.0;

          if (abflug.getLatitude() < 0.0) {
            //System.out.println("de.klees.beans.assignement.assignementBean.onAirportAgent() Lati <0");
            bgVon = abflug.getLatitude() - Umkreis;
            bgBis = abflug.getLatitude() + Umkreis;
          } else {
            bgVon = abflug.getLatitude() - Umkreis;
            bgBis = abflug.getLatitude() + Umkreis;
          }

          if (abflug.getLongitude() < 0.0) {
            //System.out.println("de.klees.beans.assignement.assignementBean.onAirportAgent() Long <0");
            lgVon = abflug.getLongitude() - Umkreis;
            lgBis = abflug.getLongitude() + Umkreis;
          } else {
            lgVon = abflug.getLongitude() - Umkreis;
            lgBis = abflug.getLongitude() + Umkreis;
          }

          List<Airport> flughaefen = facadeAssignment.getJobFlughaefen(bgVon, bgBis, lgVon, lgBis, abflug.getIcao(), minKlasse, maxKlasse);

          int MaxZufall = 30;

          if (flughaefen.size() < 20) {
            MaxZufall = 10;
          } else if (flughaefen.size() > 20) {
            MaxZufall = 15;
          } else if (flughaefen.size() > 30) {
            MaxZufall = 20;
          }

          for (Airport airp : flughaefen) {

            if (CONF.zufallszahl(1, MaxZufall) == 1) {

              int Ergebnis[] = CONF.DistanzBerechnung(abflug.getLongitude(), abflug.getLatitude(), airp.getLongitude(), airp.getLatitude());

              if (Ergebnis[0] >= minEntfernung && Ergebnis[0] <= maxEnfernung) {
                AgentJobList AgentJobEintrag = new AgentJobList();
                AgentJobEintrag.setBezeichnung("Agent-Job");
                AgentJobEintrag.setEntfernung(Ergebnis[0]);
                AgentJobEintrag.setKurs(Ergebnis[1]);
                AgentJobEintrag.setNachBezeichnung(airp.getName());
                AgentJobEintrag.setNachLand(airp.getLand() + " " + airp.getStadt());
                AgentJobEintrag.setNachICAO(airp.getIcao());
                AgentJobEintrag.setKlasse(airp.getKlasse());
                AgentJobEintrag.setLizenz(meinFlugzeug.getLizenz());
                AgentJobEintrag.setCargo(0);
                AgentJobEintrag.setIdFlugzeug(idFlugzeug);

                if (maxPax > 0) {
                  if (maxPax <= 3) {
                    AgentJobEintrag.setPax(CONF.zufallszahl(1, maxPax));
                  } else {
                    AgentJobEintrag.setPax(CONF.zufallszahl(maxPax / 3, maxPax));
                  }

                } else {
                  AgentJobEintrag.setPax(0);
                  AgentJobEintrag.setCargo(CONF.zufallszahl(maxCargo / 4, maxCargo));
                }

                int GesamtGepaeck = 0;
                int GewichtPax = 0;

                for (int i = 0; i < AgentJobEintrag.getPax(); i++) {
                  GesamtGepaeck = GesamtGepaeck + CONF.zufallszahl(1, config.getBasisGewichtGepaeck().intValue());
                  GewichtPax = GewichtPax + CONF.zufallszahl(60, config.getBasisGewichtPassagier().intValue());
                }

                AgentJobEintrag.setGesamtGepaeck(GesamtGepaeck);
                AgentJobEintrag.setGewichtPax(GewichtPax);

                AgentJobEintrag.setVonICAO(abflug.getIcao());

                //************ Auftragswert
                int Verguetung = 0;

                double FlugzeitStd = (double) Ergebnis[0] / meinFlugzeug.getReisegeschwindigkeitTAS();
                // Abweichende Geschwindigkeit bei Start und Landung berücksichtigen
                FlugzeitStd = FlugzeitStd + (Taxiing / 60.0);

                double Spritkosten = 0.0;
                if (meinFlugzeug.getTreibstoffArt() == 1) {
                  Spritkosten = FlugzeitStd * meinFlugzeug.getVerbrauchStunde() * config.getPreisAVGASkg();
                  GewichtSprit = (int) (FlugzeitStd * meinFlugzeug.getVerbrauchStunde());
                } else {
                  Spritkosten = FlugzeitStd * meinFlugzeug.getVerbrauchStunde() * config.getPreisJETAkg();
                  GewichtSprit = (int) (FlugzeitStd * meinFlugzeug.getVerbrauchStunde());
                }

                double PilotGehalt = FlugzeitStd * BezahlungPilot;
                double CrewGehalt = (FlugzeitStd * config.getAbrCrewgebuehren()) * CrewAnzahl;
                double KalkulatorischerStundensatz = Stammflugzeug.getKalkulatorischerStundensatz() * FlugzeitStd;
                double Startgebuehr = (Leergewicht + GewichtPax + GesamtGepaeck + GewichtSprit + RestTankMenge) * config.getAbrLandegebuehr();
                double Landegebuehr = (Leergewicht + GewichtPax + GesamtGepaeck + RestTankMenge) * config.getAbrLandegebuehr();

                double Zwischensumme = Spritkosten + (PilotGehalt * 3.0) + KalkulatorischerStundensatz + CrewGehalt + Startgebuehr + Landegebuehr;

                double Terminal = Zwischensumme * 0.10;
                double Flughafen = Zwischensumme * 0.02;
                double BodenPersonal = Zwischensumme * 0.05;

                double Summe = Zwischensumme + Terminal + Flughafen + BodenPersonal;

                AgentJobEintrag.setVerguetung((int) Summe);

                AgentJobEintrag.setFlugzeit(FlugzeitStd);
                AgentJobEintrag.setMaxAbfluggewicht(MaxMTOW);
                AgentJobEintrag.setSpritKG(GewichtSprit);
                AgentJobEintrag.setMaxCargo(maxCargo);
                AgentJobEintrag.setMaxPayload(PayLoad);

                Joblist.add(AgentJobEintrag);
              }

            }

          }

        }

      }

    }
  }

  public List<AgentJobList> getJoblist() {
    return Joblist;
  }

  public void onAgentJobErstellen() {

    if (!facadeAssignment.hatUserSchonEinenAgentJob(UserID)) {

      Date jetzt = new Date();
      Date jobTime;
      long neueZeit;

      config = facadeAssignment.readConfig();

      Airport vonFlugHafen = facadeAssignment.getAirportByIcao(SuchText);
      Airport nachFlugHafen = facadeAssignment.getAirportByIcao(selectedAgentJob.getNachICAO());

      Assignement newAssignment = new Assignement();
      newAssignment.setActive(0);
      newAssignment.setAirlineLogo("http://www.ftw-sim.de/images/FTW/agent-job.png");
      // Pax oder Cargo Menge eintragen
      if (selectedAgentJob.getPax() == 0) {
        newAssignment.setAmmount(selectedAgentJob.getCargo());
      } else {
        newAssignment.setAmmount(selectedAgentJob.getPax());
      }

      newAssignment.setBonusclosed(0.0);
      newAssignment.setBonusoeffentlich(0.0);
      newAssignment.setCeoAirline("");
      newAssignment.setComment(selectedAgentJob.getBezeichnung());
      newAssignment.setCommodity(selectedAgentJob.getBezeichnung());
      newAssignment.setCreatedbyuser("FTW-System");
      newAssignment.setCreation(jetzt);
      newAssignment.setDaysclaimedactive(0);
      newAssignment.setDirect(selectedAgentJob.getKurs());
      newAssignment.setDistance(selectedAgentJob.getEntfernung());

      // FtwJobsAblauf ist in Sekungen
      long milli = CONF.zufallszahl(config.getFtwJobsAblauf() / 2, config.getFtwJobsAblauf());

      // Minuten * 60 * 1000
      neueZeit = jetzt.getTime() + (long) 900000;
      jobTime = new Date(neueZeit);
      newAssignment.setExpires(jobTime);
      newAssignment.setFlugrouteName("Standard-Job");
      newAssignment.setFromAirportLandCity(vonFlugHafen.getStadt() + " " + vonFlugHafen.getLand());
      newAssignment.setFromIcao(vonFlugHafen.getIcao());
      newAssignment.setLocationIcao(vonFlugHafen.getIcao());
      newAssignment.setFromName(vonFlugHafen.getName());
      newAssignment.setGepaeck(selectedAgentJob.getGesamtGepaeck());
      newAssignment.setGewichtPax(selectedAgentJob.getGewichtPax());
      newAssignment.setGruppe("");
      newAssignment.setIdAirline(-1);
      newAssignment.setIdRoute(-1);
      newAssignment.setIdaircraft(selectedAgentJob.getIdFlugzeug());
      newAssignment.setIdcommodity(-1);
      newAssignment.setIdgroup(-1);
      newAssignment.setIdowner(UserID);
      newAssignment.setIsBusinessClass(0);
      newAssignment.setLizenz(selectedAgentJob.getLizenz());
      newAssignment.setMpttax(0);
      newAssignment.setNameairline("Standard-Job");
      newAssignment.setNoext(0);
      newAssignment.setOeffentlich(1);
      // ************************************************** Passagiere 
      newAssignment.setPay((double) selectedAgentJob.getVerguetung());
      newAssignment.setPilotfee(0);
      newAssignment.setPtassigment("");
      // Type 1=PAX, 2=CARGO, 3=TRF, 4=Business-PAX, 5 Spritfaesser
      if (selectedAgentJob.getPax() == 0) {
        newAssignment.setRoutenArt(2);
      } else {
        newAssignment.setRoutenArt(1);
      }

      newAssignment.setToAirportLandCity(nachFlugHafen.getStadt() + " " + nachFlugHafen.getLand());
      newAssignment.setToIcao(nachFlugHafen.getIcao());
      newAssignment.setToName(nachFlugHafen.getName());
      // Type 1=Routen Job, 2=Standard-Job, 3=Random-Job, 4=Linien-Job, 5=Airport Agent
      newAssignment.setType(5);
      newAssignment.setUnits("");
      newAssignment.setUserlock(0);
      newAssignment.setProvision(0.0);
      newAssignment.setIdFBO(-300);
      newAssignment.setIdTerminal(-300);
      newAssignment.setIcaoCodeFluggesellschaft("FTW");
      newAssignment.setVerlaengert(false);
      newAssignment.setGesplittet(false);
      newAssignment.setIdjob(-1);
      //*********** Langstrecke eintragen
      newAssignment.setLangstrecke(true);
      facadeAssignment.create(newAssignment);

      // Bei Paxauftraegen noch einen Cargo-Auftrag dranhaengen
      if (selectedAgentJob.getPax() > 0) {
        int RestFuerCargo = getRestLadekapazitaet(selectedAgentJob.getMaxPayload(), selectedAgentJob.getMaxCargo(), selectedAgentJob.getMaxAbfluggewicht(),
                selectedAgentJob.getGewichtPax() + selectedAgentJob.getGesamtGepaeck(), selectedAgentJob.getGesamtGepaeck(), selectedAgentJob.getSpritKG());
        if (RestFuerCargo > 0) {
          AgentJobRestCargoErstellen(RestFuerCargo);
        }
      }

    } else {
      NewMessage("Du hast schon einen Agent Job angenommen, Job wird nicht angenommen!");
    }

    Joblist.clear();
    selectedAgentJob = null;
  }

  public void AgentJobRestCargoErstellen(int Menge) {
    Date jetzt = new Date();
    Date jobTime;
    long neueZeit;

    config = facadeAssignment.readConfig();

    Airport vonFlugHafen = facadeAssignment.getAirportByIcao(SuchText);
    Airport nachFlugHafen = facadeAssignment.getAirportByIcao(selectedAgentJob.getNachICAO());

    Assignement newAssignment = new Assignement();
    newAssignment.setActive(0);
    newAssignment.setAirlineLogo("http://www.ftw-sim.de/images/FTW/agent-job-cargo.png");

    //Von der ausgerechneten Menge per Zufall mehr als 50% auswählen
    if (Menge / 2 > 1) {
      Menge = CONF.zufallszahl(Menge / 2, Menge);
    } else {
      Menge = 1;
    }

    newAssignment.setAmmount(Menge);

    newAssignment.setBonusclosed(0.0);
    newAssignment.setBonusoeffentlich(0.0);
    newAssignment.setCeoAirline("");
    newAssignment.setComment(selectedAgentJob.getBezeichnung());
    newAssignment.setCommodity(selectedAgentJob.getBezeichnung());
    newAssignment.setCreatedbyuser("FTW-System");
    newAssignment.setCreation(jetzt);
    newAssignment.setDaysclaimedactive(0);
    newAssignment.setDirect(selectedAgentJob.getKurs());
    newAssignment.setDistance(selectedAgentJob.getEntfernung());

    // FtwJobsAblauf ist in Sekungen
    long milli = CONF.zufallszahl(config.getFtwJobsAblauf() / 2, config.getFtwJobsAblauf());

    // Minuten * 60 * 1000
    neueZeit = jetzt.getTime() + (long) 900000;
    jobTime = new Date(neueZeit);
    newAssignment.setExpires(jobTime);
    newAssignment.setFlugrouteName("Standard-Job");
    newAssignment.setFromAirportLandCity(vonFlugHafen.getStadt() + " " + vonFlugHafen.getLand());
    newAssignment.setFromIcao(vonFlugHafen.getIcao());
    newAssignment.setLocationIcao(vonFlugHafen.getIcao());
    newAssignment.setFromName(vonFlugHafen.getName());
    newAssignment.setGepaeck(0);
    newAssignment.setGewichtPax(0);
    newAssignment.setGruppe("");
    newAssignment.setIdAirline(-1);
    newAssignment.setIdRoute(-1);
    newAssignment.setIdaircraft(selectedAgentJob.getIdFlugzeug());
    newAssignment.setIdcommodity(-1);
    newAssignment.setIdgroup(-1);
    newAssignment.setIdowner(UserID);
    newAssignment.setIsBusinessClass(0);
    newAssignment.setLizenz(selectedAgentJob.getLizenz());
    newAssignment.setMpttax(0);
    newAssignment.setNameairline("Standard-Job");
    newAssignment.setNoext(0);
    newAssignment.setOeffentlich(1);
    // ************************************************** Passagiere 
    newAssignment.setPay(Menge * config.getPreisFuerCargokg());

    //********* PPL-A oder kurze Entfernungen besser bezahlen.
    if (selectedAgentJob.getLizenz().equals("PPL-A") || selectedAgentJob.getEntfernung() <= 100) {
      newAssignment.setPay(Menge * (config.getPreisFuerCargokg() * 2));
    }

    newAssignment.setPilotfee(0);
    newAssignment.setPtassigment("");
    // Type 1=PAX, 2=CARGO, 3=TRF, 4=Business-PAX, 5 Spritfaesser
    newAssignment.setRoutenArt(2);

    newAssignment.setToAirportLandCity(nachFlugHafen.getStadt() + " " + nachFlugHafen.getLand());
    newAssignment.setToIcao(nachFlugHafen.getIcao());
    newAssignment.setToName(nachFlugHafen.getName());
    // Type 1=Routen Job, 2=Standard-Job, 3=Random-Job, 4=Linien-Job, 5=Airport Agent
    newAssignment.setType(5);
    newAssignment.setUnits("");
    newAssignment.setUserlock(0);
    newAssignment.setProvision(0.0);
    newAssignment.setIdFBO(-300);
    newAssignment.setIdTerminal(-300);
    newAssignment.setIcaoCodeFluggesellschaft("FTW");
    newAssignment.setVerlaengert(false);
    newAssignment.setGesplittet(false);
    newAssignment.setIdjob(-1);
    //*********** Langstrecke eintragen
    newAssignment.setLangstrecke(true);
    facadeAssignment.create(newAssignment);

  }

  public boolean getExistAgentjob() {

    if (facadeAssignment.hatUserSchonEinenAgentJob(UserID)) {
      return true;
    }
    return false;
  }

  /*
  ************** Airport Agent ENDE
   */
 /*
  *************** Favoriten BEGINN
   */
  public void onCreateFavorite() {

    if (!SuchText.equals("")) {

      if (!facadeAssignment.ifExistFavorit(UserID, SuchText)) {

        Airport airport = facadeAssignment.getAirportByIcao(SuchText);

        if (airport != null) {
          UserFavoriten fav = new UserFavoriten();

          fav.setIduser(UserID);
          fav.setIcao(SuchText);
          fav.setBezeichnung(airport.getName() + " " + airport.getLand());

          facadeAssignment.createFavorit(fav);
        }

      }

    }
  }

  /*
  *************** Favoriten ENDE
   */
 /*
  Visuelle Auftragsplanung BEGINN
   */
  public void onVisuellAuftragDlgClose() {
    VisuellAuftragDialog = false;
  }

  public void onAuftraegeHinzufuegen() {
    if (Assignments != null) {
      OwnerID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
      // unbedingt zuweisen
      String toIcao = "";

      for (Assignement assi : Assignments) {
        if (assi.getUserlock() == 0 && assi.getIdowner() == -1) {
          if (assi.getToIcao().equals(toIcao)) {
            assi.setIdowner(OwnerID);
            assi.setActive(0);
            facadeAssignment.edit(assi);
          }
        }
      }
      NewMessage(loginMB.onSprache("Auftragsplanung_msg_AuftraegeZuMeinenAuftraegenHinzugefuegt"));
      VisuellAuftragDialog = false;
      selectedAssignments = null;
      selectedHoldAssignments = null;
    }
  }

  /*
  Visuelle Auftragsplanung ENDE
   */
 /*
  **** Splitten von Auftraegen Beginn
   */
  public void onPaxAuftraegeSplitten() {

    boolean splitOK = true;

    if (selectedAssignments != null) {

      //nur Paxe splitten
      for (Assignement assi : selectedAssignments) {
        if (!assi.getRoutenArt().equals(1)) {
          splitOK = false;
        }
      }

      if (splitOK) {
        if (!selectedAssignments.isEmpty()) {
          if (selectedAssignments.size() == 1) {

            int Paxe = selectedAssignments.get(0).getAmmount();
            int GewichtPax = selectedAssignments.get(0).getGewichtPax() / Paxe;
            int GewichtGepaek = selectedAssignments.get(0).getGepaeck() / Paxe;
            double Preis = selectedAssignments.get(0).getPay() / Paxe;
            int id = selectedAssignments.get(0).getIdassignement();
            boolean splitted = false;

            if (Paxe > 1 && selectedAssignments.get(0).getRoutenArt().equals(1) && selectedAssignments.get(0).getIdAirline().equals(-1)) {
              Assignement neuAssi = new Assignement();
              neuAssi = selectedAssignments.get(0);
              neuAssi.setAmmount(1);
              neuAssi.setGewichtPax(GewichtPax);
              neuAssi.setGepaeck(GewichtGepaek);
              neuAssi.setPay(Preis);
              neuAssi.setGesplittet(true);

              for (int i = 0; i < Paxe; i++) {
                facadeAssignment.create(neuAssi);
              }

              selectedAssignments = null;
              isLoaded = false;
              if (facadeAssignment.removeKopie(id) > 0) {
                NewMessage("Splitting erfolgreich");
                splitted = true;
              }
            }

            // Jobs der Fluggesellschaft splitten
            if (!splitted) {
              if (Paxe > 1 && (selectedAssignments.get(0).getRoutenArt().equals(1) && selectedAssignments.get(0).getIdAirline() > 0)) {
                if (isAirlinePilotForSplitting(UserID)) {
                  Assignement neuAssi = new Assignement();
                  neuAssi = selectedAssignments.get(0);
                  neuAssi.setAmmount(1);
                  neuAssi.setGewichtPax(GewichtPax);
                  neuAssi.setGepaeck(GewichtGepaek);
                  neuAssi.setPay(Preis);
                  neuAssi.setGesplittet(true);

                  for (int i = 0; i < Paxe; i++) {
                    facadeAssignment.create(neuAssi);
                  }

                  selectedAssignments = null;
                  isLoaded = false;
                  if (facadeAssignment.removeKopie(id) > 0) {
                    NewMessage("Splitting erfolgreich");
                  }
                }
              }
            }
            selectedAssignments = null;
            selectedHoldAssignments = null;
          }
        }
      }
    }
  }

  /*
  **** Splitten von Auftraegen ENDE
   */

 /*
  From Assignments to Ready for Takeoff
   */
  public void onAddToMyFlight(int Ansicht) {

    if (selectedAssignments != null) {

      OwnerID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
      isLoaded = false;
      isLoadedHold = false;

      for (Assignement assi : selectedAssignments) {
        if (assi.getUserlock() == 0 && assi.getIdowner() < 0) {
          assi.setIdowner(OwnerID);
          assi.setActive(0);
          facadeAssignment.edit(assi);
        }
      }

      if (Ansicht == 1) {
        onViewAssignments();
      } else if (Ansicht == 2) {
        onViewMyFlight();
      }

      NewMessage(loginMB.onSprache("Auftragsplanung_msg_AuftraegeZuMeinenAuftraegenHinzugefuegt"));

      selectedAssignments = null;
      selectedHoldAssignments = null;

    }
  }


  /*
  From Assingments to DeapatureLounge
   */
  public void onAddToDepartureLonge(int Ansicht) {
    if (selectedAssignments != null) {
      isLoaded = false;
      isLoadedHold = false;
      OwnerID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
      for (Assignement assi : selectedAssignments) {
        if (assi.getUserlock() == 0) {
          if (assi.getType() != 3) {
            assi.setIdowner(OwnerID);
            assi.setActive(1);
            facadeAssignment.edit(assi);
          } else {
            NewMessage(loginMB.onSprache("Auftragsplanung_msg_CharterAuftraegeVerschiebenVerboten"));
          }
        } else {
          NewMessage(loginMB.onSprache("Auftragsplanung_msg_AuftraegeSindInDerLuft"));
        }
      }

      if (Ansicht == 1) {
        onViewAssignments();
      } else if (Ansicht == 2) {
        onViewMyFlight();
      }
      selectedAssignments = null;
      selectedHoldAssignments = null;
    }
  }

  /*
  Remove to Assignments
   */
  public void onRemoveFromMyFlight(int Ansicht) {
    if (selectedAssignments != null) {
      isLoaded = false;
      isLoadedHold = false;
      OwnerID = -1;
      for (Assignement assi : selectedAssignments) {
        if (assi.getUserlock() == 0) {
          if (assi.getType() != 3) {
            assi.setIdowner(OwnerID);
            assi.setActive(0);
            assi.setIdjob(-1);
            facadeAssignment.edit(assi);
          } else {
            NewMessage(loginMB.onSprache("Auftragsplanung_msg_CharterAuftraegeVerschiebenVerboten"));
          }
        } else {
          NewMessage(loginMB.onSprache("Auftragsplanung_msg_AuftraegeSindInDerLuft"));
        }
      }
      NewMessage(loginMB.onSprache("Auftragsplanung_msg_AuftraegeVonMeinenAuftraegenEntfernt"));
      if (Ansicht == 1) {
        onViewAssignments();
      } else if (Ansicht == 2) {
        onViewMyFlight();
      }
      selectedAssignments = null;
      selectedHoldAssignments = null;
    }
  }

  public void onRemoveFromMyFlightToAssignments(int Ansicht) {
    if (selectedHoldAssignments != null) {
      OwnerID = -1;
      isLoadedHold = false;
      for (Assignement assi : selectedHoldAssignments) {
        if (assi.getUserlock() == 0) {
          if (assi.getType() != 3) {
            assi.setIdowner(OwnerID);
            assi.setActive(0);
            facadeAssignment.edit(assi);
          } else {
            NewMessage(loginMB.onSprache("Auftragsplanung_msg_CharterAuftraegeVerschiebenVerboten"));
          }
        } else {
          NewMessage(loginMB.onSprache("Auftragsplanung_msg_AuftraegeSindInDerLuft"));
        }
      }
      NewMessage(loginMB.onSprache("Auftragsplanung_msg_AuftraegeVonMeinenAuftraegenEntfernt"));
      if (Ansicht == 1) {
        onViewAssignments();
      } else if (Ansicht == 2) {
        onViewMyFlight();
      }
      selectedAssignments = null;
      selectedHoldAssignments = null;
    }
  }

  /*
  From Departure Lounge to Ready for Take off
   */
  public void onRemoveToReadyForTakeoff(int Ansicht) {
    if (selectedHoldAssignments != null) {
      OwnerID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
      isLoadedHold = false;
      for (Assignement assi : selectedHoldAssignments) {
        if (assi.getUserlock() == 0) {
          assi.setIdowner(OwnerID);
          assi.setActive(0);
          facadeAssignment.edit(assi);
        } else {
          NewMessage(loginMB.onSprache("Auftragsplanung_msg_AuftraegeSindInDerLuft"));
        }
      }
      NewMessage(loginMB.onSprache("Auftragsplanung_msg_AuftraegeZuMeinenAuftraegenHinzugefuegt"));
      if (Ansicht == 1) {
        onViewAssignments();
      } else if (Ansicht == 2) {
        onViewMyFlight();
      }

      selectedAssignments = null;
      selectedHoldAssignments = null;
    }
  }

  private boolean onGemischteJobs() {

    for (int i = 0; i < Assignments.size() - 1; i++) {

      if (i + 1 > Assignments.size()) {
        return false;
      }

      if (!Assignments.get(i).getCreatedbyuser().equals(Assignments.get(i + 1).getCreatedbyuser())) {
        return true;
      }
    }

    return false;
  }

  public void onRowSelect(SelectEvent event) {

    try {
      if (selectedAssignments.size() == 1) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Assignment_ZIEL_ICAO", selectedAssignments.get(0).getToIcao());
      }
    } catch (NullPointerException e) {

    }

  }

  public void onRowUnselect(UnselectEvent event) {
  }

  private void NewMessage(String neueNachricht) {
    FacesMessage msg = new FacesMessage(neueNachricht);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  public void onStarteSchwarzeJobsErstellen() {

    long sekunden = (new Date().getTime() - oldTime) / 1000;

    isLoaded = true;

    if (firstRun) {
      sekunden = 5;
    }

    if (sekunden >= 5) {
      oldTime = new Date().getTime();
      firstRun = false;
      isLoaded = false;
      try {
        if (SuchText.contains(",")) {
          SuchText = SuchText.substring(0, SuchText.indexOf(","));
        }

        Airport airport = facadeAssignment.getAirportData(SuchText);

        if (airport != null) {
          if (airport.getJobTemplate().equals("")) {
            onStandardSchwarzeJobsErstellen();
          } else {
            SchwarzeJobsVonTemplateErstellen();
          }
        }

      } catch (IndexOutOfBoundsException e) {
      }
    } else {
      //System.out.println("Dauerklick de.klees.beans.assignement.assignementBean.onStarteSchwarzeJobsErstellen() UserID: " + UserID);
    }
  }

  private void onStandardSchwarzeJobsErstellen() {

    Airport airport = facadeAssignment.getAirportData(SuchText);

    if (airport != null) {
      zaehlerCargo = 0;
      zaehlerPax = 0;

      zaehlerPax = facadeAssignment.GetMengeAmFlughafen("select sum(ammount) as Menge from assignement where routenArt=1 and createdbyuser = 'FTW-System' and from_icao='" + SuchText + "'");
      zaehlerCargo = facadeAssignment.GetMengeAmFlughafen("select sum(ammount) as Menge from assignement where routenArt=2 and createdbyuser = 'FTW-System' and from_icao='" + SuchText + "'");

      if (zaehlerPax < airport.getMaxpassagiereprotag() || zaehlerCargo < airport.getMaxCargo()) {

        //Standardaten laden aus Feinabstimmung
        fa = facadeAssignment.getDaten();

        for (int i = 0; i < 3; i++) {
          SchwarzeJobsErstellen();
        }
      }

    }
    ViewMyFlight = false;
    ViewHoldArea = false;
    ViewAssignments = true;
    isLoaded = false;

  }

  private void SchwarzeJobsVonTemplateErstellen() {

    zaehlerCargo = 0;
    zaehlerPax = 0;

    Airport AbflughafenFuerTemplate = facadeAssignment.getAirportData(SuchText);

    zaehlerPax = facadeAssignment.GetMengeAmFlughafen("select sum(ammount) as Menge from assignement where routenArt=1 and createdbyuser = 'FTW-System' and from_icao='" + AbflughafenFuerTemplate.getIcao() + "'");
    zaehlerCargo = facadeAssignment.GetMengeAmFlughafen("select sum(ammount) as Menge from assignement where routenArt=2 and createdbyuser = 'FTW-System' and from_icao='" + AbflughafenFuerTemplate.getIcao() + "'");

    if (zaehlerPax < AbflughafenFuerTemplate.getMaxpassagiereprotag() || zaehlerCargo < AbflughafenFuerTemplate.getMaxCargo()) {

      //Standardaten laden aus Feinabstimmung
      fa = facadeAssignment.getDaten();

      if (AbflughafenFuerTemplate != null) {
        List<ViewAirportAnflugZiele> flughaefen = facadeAssignment.holeAnflugziele(AbflughafenFuerTemplate.getIdairport());
        List<Airportjobtemplate> templateJobs = facadeAssignment.getTemplateJobs(AbflughafenFuerTemplate.getJobTemplate());

        do {
          for (ViewAirportAnflugZiele ziel : flughaefen) {
            for (Airportjobtemplate job : templateJobs) {
              //PaxJobs
              if (job.getJobart() == 1) {
                if (zaehlerPax < AbflughafenFuerTemplate.getMaxpassagiereprotag()) {
                  if (CONF.zufallszahl(0, job.getZz()) == job.getZz()) {
                    createTemplateJob(AbflughafenFuerTemplate, facadeAssignment.getAirportData(ziel.getIcao()), job);
                  }
                }
                // CargoJobs
              } else if (job.getJobart() == 2) {
                if (zaehlerCargo < AbflughafenFuerTemplate.getMaxCargo()) {
                  if (CONF.zufallszahl(0, job.getZz()) == job.getZz()) {
                    createTemplateJob(AbflughafenFuerTemplate, facadeAssignment.getAirportData(ziel.getIcao()), job);
                  }
                }
              }
            }
          }

        } while ((zaehlerCargo < AbflughafenFuerTemplate.getMaxCargo() || zaehlerPax < AbflughafenFuerTemplate.getMaxpassagiereprotag()));

      }//Endif AbflughafenFuerTemplate !=null
    }
    ViewMyFlight = false;
    ViewHoldArea = false;
    ViewAssignments = true;
    isLoaded = false;

  }

  public void SchwarzeJobsErstellen() {

//    System.out.println("de.klees.beans.assignement.assignementBean.SchwarzeJobsErstellen() ***");
    ViewMyFlight = false;
    ViewHoldArea = false;
    ViewAssignments = true;
    isLoaded = false;

    double bgVon = 0.0;
    double bgBis = 0.0;
    double lgVon = 0.0;
    double lgBis = 0.0;
    boolean ListeVorhanden = false;

    ErzeugteJobsImDurchlauf = 0;

    if (facadeAssignment.ifExistFlughafen(SuchText) && !Auftragslauf) {
      Assignments = facadeAssignment.findByICAOLocationAndFTWJob(SuchText, "%", -1);

      Auftragslauf = true;

      Airport vonFlughafen = facadeAssignment.getAirportData(SuchText);

      //minAuftraege = MinAuftraege(vonFlughafen.getKlasse());
      minAuftraege = vonFlughafen.getMaxpassagiereprotag();
      minCargo = vonFlughafen.getMaxCargo();

      setFlughafenName(vonFlughafen.getStadt());

      if (zaehlerPax <= minAuftraege || zaehlerCargo <= minCargo) {

        p1Jobs = facadeAssignment.getJobs("P1");
        p2Jobs = facadeAssignment.getJobs("P2");
        p3Jobs = facadeAssignment.getJobs("P3");
        p4Jobs = facadeAssignment.getJobs("P4");
        p5Jobs = facadeAssignment.getJobs("P5");
        p6Jobs = facadeAssignment.getJobs("P6");
        p7Jobs = facadeAssignment.getJobs("P7");

        c1Jobs = facadeAssignment.getJobs("C1");
        c2Jobs = facadeAssignment.getJobs("C2");
        c3Jobs = facadeAssignment.getJobs("C3");
        c4Jobs = facadeAssignment.getJobs("C4");
        c5Jobs = facadeAssignment.getJobs("C5");
        c6Jobs = facadeAssignment.getJobs("C6");

        Airport nachFlughafen;

        String vonIcao = vonFlughafen.getIcao();
        String nachIcao = "";

        int klasse = vonFlughafen.getKlasse();
        int ZZ = 0;
        int AnzahlJobsGenerieren = 0;
        int PaxGroesse = 0;

        String JobBezeichnung = "";
        double JobFaktor = 0.0;
        int Basispreis = 0;

        if (facadeAssignment.holeAnflugziele(vonFlughafen.getIdairport()).size() > 0) {
          AnflugListeAbarbeiten(vonFlughafen);
          ListeVorhanden = true;
        } else {
          ListeVorhanden = false;
        }

        if (vonFlughafen.getKlasse() > 12) {
          ListeVorhanden = true;
        }

        List<Airport> flughaefen = null;

        if (!ListeVorhanden) {

          double Lati = vonFlughafen.getLatitude();
          double Longi = vonFlughafen.getLongitude();

          if (vonFlughafen.getBgBis() + vonFlughafen.getBgVon() + vonFlughafen.getLgVon() + vonFlughafen.getLgBis() != 0.0) {
            bgVon = Lati + vonFlughafen.getBgVon();
            bgBis = Lati - vonFlughafen.getBgBis();
            lgVon = Longi + vonFlughafen.getLgVon();
            lgBis = Longi - vonFlughafen.getLgBis();
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

              // Achtung Reihenfolge vertauscht damit es mit der Eingabmaske der Flughaefen uebereinstimmt
              flughaefen = facadeAssignment.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 1, 5);
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
              flughaefen = facadeAssignment.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 1, 5);
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
              flughaefen = facadeAssignment.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 1, 6);
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
              flughaefen = facadeAssignment.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 1, 7);
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
              flughaefen = facadeAssignment.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 1, 8);
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
              flughaefen = facadeAssignment.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 3, 8);
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
              flughaefen = facadeAssignment.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 3, 8);
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
              flughaefen = facadeAssignment.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 4, 9);
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
              flughaefen = facadeAssignment.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 4, 9);
              // Keine Flughäfen gefunden Umkreis erweitern

              if (flughaefen.size() < 1) {
                int doZaehler = 0;
                do {

                  if (bgVon < 0) {
                    bgVon = bgVon - -1;
                  } else {
                    bgVon = bgVon - 1;
                  }

                  if (bgBis < 0) {
                    bgBis = bgBis + -1;
                  } else {
                    bgBis = bgBis + 1;
                  }

                  if (lgVon < 0) {
                    lgVon = lgVon - 1;
                  } else {
                    lgVon = lgVon - -1;
                  }

                  if (lgBis < 0) {
                    lgBis = lgBis + 1;
                  } else {
                    lgBis = lgBis + -1;
                  }

                  if (lgVon > lgBis) {
                    flughaefen = facadeAssignment.getJobFlughaefen(bgVon, bgBis, lgBis, lgVon, vonIcao, 4, 9);
                  } else if (bgVon > bgBis) {
                    flughaefen = facadeAssignment.getJobFlughaefen(bgBis, bgVon, lgVon, lgBis, vonIcao, 4, 9);
                  } else if ((lgVon > lgBis) && (bgVon > bgBis)) {
                    flughaefen = facadeAssignment.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 4, 9);
                  } else {
                    flughaefen = facadeAssignment.getJobFlughaefen(bgVon, bgBis, lgVon, lgBis, vonIcao, 4, 9);
                  }
                  doZaehler = doZaehler + 1;
                } while ((flughaefen.size() < 8) && (doZaehler < 60));
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
              flughaefen = facadeAssignment.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 10, 11);
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
              flughaefen = facadeAssignment.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 10, 11);
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
              flughaefen = facadeAssignment.getJobFlughaefen(bgBis, bgVon, lgBis, lgVon, vonIcao, 7, 8);
              break;
            default:
              break;
          }

          if (flughaefen.size() > 0) {

            //AnzahlJobsGenerieren = 20;
            AnzahlJobsGenerieren = CONF.zufallszahl(10, 20);
            ErzeugteJobsImDurchlauf = 0;

            // ***************** Abflug-Flughafen
            int QVonBis[] = getPJobs(vonFlughafen);
            int Qvon = QVonBis[0];
            int Qbis = QVonBis[1];

            Airport airport;
            Airport CargoAirport;

            for (int i = 0; i < flughaefen.size(); i++) {

              // Zufall entscheidet ob Flughafen bedient wird
              if (ErzeugteJobsImDurchlauf <= vonFlughafen.getMaxpassagiereprotag()) {

                airport = flughaefen.get(CONF.zufallszahl(0, flughaefen.size() - 1));
                CargoAirport = flughaefen.get(CONF.zufallszahl(0, flughaefen.size() - 1));

                //System.out.println("Flughafen : " + airport.getIcao() + " " + airport.getName() + " Klasse : " + airport.getKlasse());
                //*************** Ziel-Flughafen
                int ZVonBis[];

                // Zurückgestellt da große Flughäfen nicht ordentlich bedient werden.               
//                if (vonFlughafen.getKlasse() > airport.getKlasse()) {
//                  ZVonBis = getPJobs(vonFlughafen);
//                } else {
//                  ZVonBis = getPJobs(airport);
//                }
                ZVonBis = getPJobs(airport);

                int Zvon = ZVonBis[0];
                int Zbis = ZVonBis[1];

                // Kleinster zu generierender Job am Abflug-Flughafen ist gleich groeßter Job am Zielflughafen
                // dann muss nur dieser Job generiert werden und der Zielflughafen ist abgearbeitet.
                if (Zbis == Qvon) {
                  //System.out.println("(Single) Flughafen : " + airport.getIcao() + " " + airport.getName() + " Klasse : " + airport.getKlasse() + "  PJob von : " + Qvon + "  PJob bis : " + Zbis);
                  // hier neu Zaehlen
                  createSingleJob(Zbis, vonFlughafen, airport, CargoAirport);

                }

                // Mehrere Jobs müssen abgearbeite werden.
                if (Zbis > Qvon) {
                  //System.out.println("(Multi) Flughafen : " + airport.getIcao() + " " + airport.getName() + " Klasse : " + airport.getKlasse() + "  PJob von : " + Qvon + "  PJob bis : " + Zbis);
                  // max = zBis
                  // min = qVon
                  // hier neu Zaehlen
                  createMultipleJobs(Qvon, Zbis, vonFlughafen, airport, CargoAirport);

                }

                // Militaer-Flughaefen
                if (vonFlughafen.getKlasse() >= 10 && vonFlughafen.getKlasse() <= 11) {
                  // hier neu Zaehlen
                  createSingleJob(7, vonFlughafen, airport, CargoAirport);

                }

              }//end if Zufall

            }

          }
        } // Liste vorhanden = false
      }

    } else {
      NewMessage(loginMB.onSprache("Auftragsplanung_msg_FlughafenNichtGefunden"));
      zielFlughafenSelected = "";
      isLoaded = false;
    }
    Auftragslauf = false;
  }

  private void AnflugListeAbarbeiten(Airport vonFlughafen) {

    List<ViewAirportAnflugZiele> flughaefen = facadeAssignment.holeAnflugziele(vonFlughafen.getIdairport());

    if (flughaefen.size() > 0) {

      ErzeugteJobsImDurchlauf = 0;

      // ***************** Abflug-Flughafen
      int QVonBis[] = getPJobs(vonFlughafen);
      int Qvon = QVonBis[0];
      int Qbis = QVonBis[1];

      Airport airport;
      Airport CargoAirport;

      for (int i = 0; i < flughaefen.size(); i++) {

        // Zufall entscheidet ob Flughafen bedient wird
        if (ErzeugteJobsImDurchlauf <= vonFlughafen.getMaxpassagiereprotag()) {

          airport = facadeAssignment.getAirportData(flughaefen.get(CONF.zufallszahl(0, flughaefen.size() - 1)).getIcao());
          CargoAirport = facadeAssignment.getAirportData(flughaefen.get(CONF.zufallszahl(0, flughaefen.size() - 1)).getIcao());

//          airport = flughaefen.get(CONF.zufallszahl(0, flughaefen.size() - 1));
//          CargoAirport = flughaefen.get(CONF.zufallszahl(0, flughaefen.size() - 1));
          //System.out.println("Anflugliste abarbeiten");
          //System.out.println("Flughafen : " + airport.getIcao() + " " + airport.getName() + " Klasse : " + airport.getKlasse());
          // *************** Ziel-Flughafen
          int ZVonBis[] = getPJobs(airport);
          int Zvon = ZVonBis[0];
          int Zbis = ZVonBis[1];

          // Kleinster zu generierender Job am Abflug-Flughafen ist gleich groeßter Job am Zielflughafen
          // dann muss nur dieser Job generiert werden und der Zielflughafen ist abgearbeitet.
          if (Zbis == Qvon) {
            //System.out.println("Flughafen : " + airport.getIcao() + " " + airport.getName() + " Klasse : " + airport.getKlasse() + "  PJob bis : " + Zbis);
            createSingleJob(Zbis, vonFlughafen, airport, CargoAirport);
          }

          // Mehrere Jobs müssen abgearbeite werden.
          if (Zbis > Qvon) {
            //System.out.println("Flughafen : " + airport.getIcao() + " " + airport.getName() + " Klasse : " + airport.getKlasse() + "  PJob bis : " + Zbis);
            // max = zBis
            // min = qVon
            createMultipleJobs(Qvon, Zbis, vonFlughafen, airport, CargoAirport);
          }

          // Militaer-Flughaefen
          if (vonFlughafen.getKlasse() >= 10 && vonFlughafen.getKlasse() <= 11) {

            createSingleJob(7, vonFlughafen, airport, CargoAirport);

          }

        }

      }

    }
  }

  private void createMultipleJobs(int Vonpjob, int Bispjob, Airport vonFlughafen, Airport zuFlughafen, Airport CargoAirport) {
    //System.out.println("de.klees.beans.assignement.assignementBean.createMultipleJobs()");
    //int zZ = CONF.zufallszahl(1, 6);
    int zZ = CONF.zufallszahl(1, 6);
    int maxJobs = 1;

    for (int i = 0; i < zZ; i++) {
      int szZ = CONF.zufallszahl(Vonpjob, Bispjob);

      switch (szZ) {

        case 7:
          onPaxP7ToP7(CONF.zufallszahl(1, 1), zuFlughafen, vonFlughafen);
          //onCargoC6ToC6(CONF.zufallszahl(1, maxJobs), CargoAirport, vonFlughafen);
          onCargoC6ToC6(CONF.zufallszahl(1, maxJobs), zuFlughafen, vonFlughafen);
          break;
        case 6:
          onPaxP6ToP6(CONF.zufallszahl(1, 1), zuFlughafen, vonFlughafen);
          onCargoC5ToC5(CONF.zufallszahl(1, maxJobs), zuFlughafen, vonFlughafen);
          break;
        case 5:
          onPaxP5ToP5(CONF.zufallszahl(1, 1), zuFlughafen, vonFlughafen);
          onCargoC4ToC4(CONF.zufallszahl(1, maxJobs), zuFlughafen, vonFlughafen);
          break;
        case 4:
          onPaxP4ToP4(CONF.zufallszahl(1, 1), zuFlughafen, vonFlughafen);
          onCargoC3ToC3(CONF.zufallszahl(1, maxJobs), zuFlughafen, vonFlughafen);
          break;
        case 3:
          onPaxP3ToP3(CONF.zufallszahl(1, 1), zuFlughafen, vonFlughafen);
          onCargoC2ToC2(CONF.zufallszahl(1, maxJobs), zuFlughafen, vonFlughafen);
          break;
        case 2:
          onPaxP2ToP2(CONF.zufallszahl(1, 1), zuFlughafen, vonFlughafen);
          onCargoC2ToC2(CONF.zufallszahl(1, maxJobs), zuFlughafen, vonFlughafen);
          break;
        case 1:
          onPaxP1ToP1(CONF.zufallszahl(1, 1), zuFlughafen, vonFlughafen);
          onCargoC1ToC1(CONF.zufallszahl(1, maxJobs), zuFlughafen, vonFlughafen);
          break;

      }

    }

  }

  private void createSingleJob(int pjob, Airport vonFlughafen, Airport zuFlughafen, Airport CargoAirport) {
    //System.out.println("de.klees.beans.assignement.assignementBean.createSingleJob()");

    int maxJobs = 1;

    switch (pjob) {

      case 7:
        onPaxP7ToP7(CONF.zufallszahl(1, 1), zuFlughafen, vonFlughafen);
        //onCargoC6ToC6(CONF.zufallszahl(1, maxJobs), CargoAirport, vonFlughafen);
        onCargoC6ToC6(CONF.zufallszahl(1, maxJobs), zuFlughafen, vonFlughafen);
        break;
      case 4:
        onPaxP4ToP4(CONF.zufallszahl(1, 1), zuFlughafen, vonFlughafen);
        onCargoC3ToC3(CONF.zufallszahl(1, maxJobs), zuFlughafen, vonFlughafen);
        break;
      case 3:
        onPaxP3ToP3(CONF.zufallszahl(1, 1), zuFlughafen, vonFlughafen);
        onCargoC2ToC2(CONF.zufallszahl(1, maxJobs), zuFlughafen, vonFlughafen);
        break;
      case 2:
        onPaxP2ToP2(CONF.zufallszahl(1, 1), zuFlughafen, vonFlughafen);
        onCargoC2ToC2(CONF.zufallszahl(1, maxJobs), zuFlughafen, vonFlughafen);
        break;
      case 1:
        onPaxP1ToP1(CONF.zufallszahl(1, 1), zuFlughafen, vonFlughafen);
        onCargoC1ToC1(CONF.zufallszahl(1, maxJobs), zuFlughafen, vonFlughafen);
        break;

    }

  }

  private void createTemplateJob(Airport vonFlughafen, Airport zuFlughafen, Airportjobtemplate job) {

    int Basispreis = BasisPreise(job.getPreisklasse());
    double JobFaktor = job.getFaktor();
    String JobBezeichnung = job.getBeschreibung();
    int RoutenArt = job.getJobart();
    int JobGroesse = CONF.zufallszahl(job.getMinwert(), job.getMaxwert());

    Airport nachFlughafen = zuFlughafen;

    onCreateAssignmentFromTemplate(Basispreis, JobFaktor, vonFlughafen, nachFlughafen, JobGroesse, JobBezeichnung, RoutenArt);

  }

  private int BasisPreise(String PreisKlasse) {

    int Basispreis = 0;

    // Basispreise für Paxe
    switch (PreisKlasse) {
      case "P1":
        Basispreis = fa.getPkP1().intValue();
        break;
      case "P2":
        Basispreis = fa.getPkP2().intValue();
        break;
      case "P3":
        Basispreis = fa.getPkp3().intValue();
        break;
      case "P4":
        Basispreis = fa.getPkP4().intValue();
        break;
      case "P5":
        Basispreis = fa.getPkP5().intValue();
        break;
      case "P6":
        Basispreis = fa.getPkP6().intValue();
        break;
      case "P7":
        Basispreis = fa.getPkP7().intValue();
        break;
      default:
        break;
    }

    // Basispreise für Cargo
    switch (PreisKlasse) {
      case "C1":
        Basispreis = fa.getPkC1().intValue();
        break;
      case "C2":
        Basispreis = fa.getPkC2().intValue();
        break;
      case "C3":
        Basispreis = fa.getPkC3().intValue();
        break;
      case "C4":
        Basispreis = fa.getPkC4().intValue();
        break;
      case "C5":
        Basispreis = fa.getPkC5().intValue();
        break;
      case "C6":
        Basispreis = fa.getPkC6().intValue();
        break;
      case "C7":
        Basispreis = fa.getPkC7().intValue();
        break;
      default:
        break;
    }

    return Basispreis;
  }

  private int[] getPJobs(Airport airport) {

    if (airport.getKlasse() == 1) {
      return Classe1P;
    }

    if (airport.getKlasse() == 2) {
      return Classe2P;
    }

    if (airport.getKlasse() == 3) {
      return Classe3P;
    }

    if (airport.getKlasse() == 4) {
      return Classe4P;
    }

    if (airport.getKlasse() == 5) {
      return Classe5P;
    }

    if (airport.getKlasse() == 6) {
      return Classe6P;
    }

    if (airport.getKlasse() == 7) {
      return Classe7P;
    }

    if (airport.getKlasse() == 8) {
      return Classe8P;
    }

    if (airport.getKlasse() == 9) {
      return Classe9P;
    }

    return new int[]{0, 0};
  }

  private void onPaxP1ToP1(int AnzahlJobsGenerieren, Airport zuFlughafen, Airport vonFlughafen) {

    for (int i = 0; i < AnzahlJobsGenerieren; i++) {
      if (!pZZ1(vonFlughafen, zuFlughafen)) {
        i--;
      }

    }
  }

  private void onPaxP2ToP2(int AnzahlJobsGenerieren, Airport zuFlughafen, Airport vonFlughafen) {

    for (int i = 0; i < AnzahlJobsGenerieren; i++) {
      if (!pZZ2(vonFlughafen, zuFlughafen)) {
        i--;
      }

    }
  }

  private void onPaxP3ToP3(int AnzahlJobsGenerieren, Airport zuFlughafen, Airport vonFlughafen) {

    for (int i = 0; i < AnzahlJobsGenerieren; i++) {
      if (!pZZ3(vonFlughafen, zuFlughafen)) {
        i--;
      }

    }
  }

  private void onPaxP4ToP4(int AnzahlJobsGenerieren, Airport zuFlughafen, Airport vonFlughafen) {

    for (int i = 0; i < AnzahlJobsGenerieren; i++) {
      if (!pZZ4(vonFlughafen, zuFlughafen)) {
        i--;
      }

    }
  }

  private void onPaxP5ToP5(int AnzahlJobsGenerieren, Airport zuFlughafen, Airport vonFlughafen) {

    for (int i = 0; i < AnzahlJobsGenerieren; i++) {
      if (!pZZ5(vonFlughafen, zuFlughafen)) {
        i--;
      }

    }
  }

  private void onPaxP6ToP6(int AnzahlJobsGenerieren, Airport zuFlughafen, Airport vonFlughafen) {

    for (int i = 0; i < AnzahlJobsGenerieren; i++) {
      if (!pZZ6(vonFlughafen, zuFlughafen)) {
        i--;
      }

    }
  }

  private void onPaxP7ToP7(int AnzahlJobsGenerieren, Airport zuFughafen, Airport vonFlughafen) {

    for (int i = 0; i < AnzahlJobsGenerieren; i++) {
      if (!pZZ7(vonFlughafen, zuFughafen)) {
        i--;
      }
    }
  }

  private void onCargoC1ToC1(int AnzahlJobsGenerieren, Airport zuFughafen, Airport vonFlughafen) {

    for (int i = 0; i < AnzahlJobsGenerieren; i++) {
      if (!cZZ1(vonFlughafen, zuFughafen)) {
        i--;
      }
    }
  }

  private void onCargoC2ToC2(int AnzahlJobsGenerieren, Airport zuFughafen, Airport vonFlughafen) {

    for (int i = 0; i < AnzahlJobsGenerieren; i++) {
      if (!cZZ2(vonFlughafen, zuFughafen)) {
        i--;
      }
    }
  }

  private void onCargoC3ToC3(int AnzahlJobsGenerieren, Airport zuFughafen, Airport vonFlughafen) {

    for (int i = 0; i < AnzahlJobsGenerieren; i++) {
      if (!cZZ3(vonFlughafen, zuFughafen)) {
        i--;
      }
    }
  }

  private void onCargoC4ToC4(int AnzahlJobsGenerieren, Airport zuFughafen, Airport vonFlughafen) {

    for (int i = 0; i < AnzahlJobsGenerieren; i++) {
      if (!cZZ4(vonFlughafen, zuFughafen)) {
        i--;
      }
    }
  }

  private void onCargoC5ToC5(int AnzahlJobsGenerieren, Airport zuFughafen, Airport vonFlughafen) {

    for (int i = 0; i < AnzahlJobsGenerieren; i++) {
      if (!cZZ5(vonFlughafen, zuFughafen)) {
        i--;
      }
    }
  }

  private void onCargoC6ToC6(int AnzahlJobsGenerieren, Airport zuFughafen, Airport vonFlughafen) {

    for (int i = 0; i < AnzahlJobsGenerieren; i++) {
      if (!cZZ6(vonFlughafen, zuFughafen)) {
        i--;
      }
    }
  }

  private boolean pZZ1(Airport vonFlughafen, Airport zuFlughafen) {
    int Basispreis = fa.getPkP1().intValue();
    int PaxGroesse = 1;
    Airport nachFlughafen = zuFlughafen;
    int zZJob = CONF.zufallszahl(0, p1Jobs.size() - 1);

    if (p1Jobs.get(zZJob).getZz() > 0) {
      if (CONF.zufallszahl(0, p1Jobs.get(zZJob).getZz()) != p1Jobs.get(zZJob).getZz()) {
        return false;
      }
    }

    String JobBezeichnung = p1Jobs.get(zZJob).getBezeichnung();
    double JobFaktor = p1Jobs.get(zZJob).getFaktor();

    onCreateAssignment(Basispreis, JobFaktor, vonFlughafen, nachFlughafen, PaxGroesse, JobBezeichnung, 1);
    return true;
  }

  private boolean pZZ2(Airport vonFlughafen, Airport zuFlughafen) {
    int Basispreis = fa.getPkP2().intValue();
    Airport nachFlughafen = zuFlughafen;
    int zZJob = CONF.zufallszahl(0, p2Jobs.size() - 1);

    if (p2Jobs.get(zZJob).getZz() > 0) {
      if (CONF.zufallszahl(0, p2Jobs.get(zZJob).getZz()) != p2Jobs.get(zZJob).getZz()) {
        return false;
      }
    }

    String JobBezeichnung = p2Jobs.get(zZJob).getBezeichnung();
    double JobFaktor = p2Jobs.get(zZJob).getFaktor();
    int PaxGroesse = CONF.zufallszahl(CONF.getMinPax(vonFlughafen.getKlasse()), CONF.getMaxPax(vonFlughafen.getKlasse()));
    onCreateAssignment(Basispreis, JobFaktor, vonFlughafen, nachFlughafen, PaxGroesse, JobBezeichnung, 1);
    return true;
  }

  private boolean pZZ3(Airport vonFlughafen, Airport zuFlughafen) {
    int Basispreis = fa.getPkp3().intValue();
    Airport nachFlughafen = zuFlughafen;
    int zZJob = CONF.zufallszahl(0, p3Jobs.size() - 1);

    if (p3Jobs.get(zZJob).getZz() > 0) {
      if (CONF.zufallszahl(0, p3Jobs.get(zZJob).getZz()) != p3Jobs.get(zZJob).getZz()) {
        return false;
      }
    }

    String JobBezeichnung = p3Jobs.get(zZJob).getBezeichnung();
    double JobFaktor = p3Jobs.get(zZJob).getFaktor();
    int PaxGroesse = CONF.zufallszahl(CONF.getMinPax(vonFlughafen.getKlasse()), CONF.getMaxPax(vonFlughafen.getKlasse()));
    onCreateAssignment(Basispreis, JobFaktor, vonFlughafen, nachFlughafen, PaxGroesse, JobBezeichnung, 1);
    return true;
  }

  private boolean pZZ4(Airport vonFlughafen, Airport zuFlughafen) {
    int Basispreis = fa.getPkP4().intValue();
    Airport nachFlughafen = zuFlughafen;
    int zZJob = CONF.zufallszahl(0, p4Jobs.size() - 1);

    if (p4Jobs.get(zZJob).getZz() > 0) {
      if (CONF.zufallszahl(0, p4Jobs.get(zZJob).getZz()) != p4Jobs.get(zZJob).getZz()) {
        return false;
      }
    }

    String JobBezeichnung = p4Jobs.get(zZJob).getBezeichnung();
    double JobFaktor = p4Jobs.get(zZJob).getFaktor();
    int PaxGroesse = CONF.zufallszahl(CONF.getMinPax(vonFlughafen.getKlasse()), CONF.getMaxPax(vonFlughafen.getKlasse()));
    onCreateAssignment(Basispreis, JobFaktor, vonFlughafen, nachFlughafen, PaxGroesse, JobBezeichnung, 1);
    return true;
  }

  private boolean pZZ5(Airport vonFlughafen, Airport zuFlughafen) {
    int Basispreis = fa.getPkP5().intValue();
    Airport nachFlughafen = zuFlughafen;
    int zZJob = CONF.zufallszahl(0, p5Jobs.size() - 1);

    if (p5Jobs.get(zZJob).getZz() > 0) {
      if (CONF.zufallszahl(0, p5Jobs.get(zZJob).getZz()) != p5Jobs.get(zZJob).getZz()) {
        return false;
      }
    }

    String JobBezeichnung = p5Jobs.get(zZJob).getBezeichnung();
    double JobFaktor = p5Jobs.get(zZJob).getFaktor();
    int PaxGroesse = CONF.zufallszahl(CONF.getMinPax(vonFlughafen.getKlasse()), CONF.getMaxPax(vonFlughafen.getKlasse()));

    onCreateAssignment(Basispreis, JobFaktor, vonFlughafen, nachFlughafen, PaxGroesse, JobBezeichnung, 1);
    return true;
  }

  private boolean pZZ6(Airport vonFlughafen, Airport zuFlughafen) {
    int Basispreis = fa.getPkP6().intValue();
    Airport nachFlughafen = zuFlughafen;
    int zZJob = CONF.zufallszahl(0, p6Jobs.size() - 1);

    if (p6Jobs.get(zZJob).getZz() > 0) {
      if (CONF.zufallszahl(0, p6Jobs.get(zZJob).getZz()) != p6Jobs.get(zZJob).getZz()) {
        return false;
      }
    }

    String JobBezeichnung = p6Jobs.get(zZJob).getBezeichnung();
    double JobFaktor = p6Jobs.get(zZJob).getFaktor();
    int PaxGroesse = CONF.zufallszahl(CONF.getMinPax(vonFlughafen.getKlasse()), CONF.getMaxPax(vonFlughafen.getKlasse()));
    onCreateAssignment(Basispreis, JobFaktor, vonFlughafen, nachFlughafen, PaxGroesse, JobBezeichnung, 1);
    return true;
  }

  private boolean pZZ7(Airport vonFlughafen, Airport zuFlughafen) {
    int Basispreis = fa.getPkP7().intValue();
    Airport nachFlughafen = zuFlughafen;
    int zZJob = CONF.zufallszahl(0, p7Jobs.size() - 1);

    if (p7Jobs.get(zZJob).getZz() > 0) {
      if (CONF.zufallszahl(0, p7Jobs.get(zZJob).getZz()) != p7Jobs.get(zZJob).getZz()) {
        return false;
      }
    }

    String JobBezeichnung = p7Jobs.get(zZJob).getBezeichnung();
    double JobFaktor = p7Jobs.get(zZJob).getFaktor();
    int PaxGroesse = CONF.zufallszahl(CONF.getMinPax(vonFlughafen.getKlasse()), CONF.getMaxPax(vonFlughafen.getKlasse()));
    onCreateAssignment(Basispreis, JobFaktor, vonFlughafen, nachFlughafen, PaxGroesse, JobBezeichnung, 1);
    return true;
  }

  private boolean cZZ1(Airport vonFlughafen, Airport zuFlughafen) {
    int Basispreis = fa.getPkC1().intValue();
    int PaxGroesse = CONF.zufallszahl(1, 20);
    Airport nachFlughafen = zuFlughafen;
    int zZJob = CONF.zufallszahl(0, c1Jobs.size() - 1);

    if (c1Jobs.get(zZJob).getZz() > 0) {
      if (CONF.zufallszahl(0, c1Jobs.get(zZJob).getZz()) != c1Jobs.get(zZJob).getZz()) {
        return false;
      }
    }

    String JobBezeichnung = c1Jobs.get(zZJob).getBezeichnung();
    double JobFaktor = c1Jobs.get(zZJob).getFaktor();

    onCreateAssignment(Basispreis, JobFaktor, vonFlughafen, nachFlughafen, PaxGroesse, JobBezeichnung, 2);
    return true;
  }

  private boolean cZZ2(Airport vonFlughafen, Airport zuFlughafen) {
    int Basispreis = fa.getPkC2().intValue();
    int PaxGroesse = CONF.zufallszahl(c2Max[0], c2Max[1]);

    Airport nachFlughafen = zuFlughafen;
    int zZJob = CONF.zufallszahl(0, c2Jobs.size() - 1);

    if (c2Jobs.get(zZJob).getZz() > 0) {
      if (CONF.zufallszahl(0, c2Jobs.get(zZJob).getZz()) != c2Jobs.get(zZJob).getZz()) {
        return false;
      }
    }

    String JobBezeichnung = c2Jobs.get(zZJob).getBezeichnung();
    double JobFaktor = c2Jobs.get(zZJob).getFaktor();

    onCreateAssignment(Basispreis, JobFaktor, vonFlughafen, nachFlughafen, PaxGroesse, JobBezeichnung, 2);
    return true;
  }

  private boolean cZZ3(Airport vonFlughafen, Airport zuFlughafen) {
    int Basispreis = fa.getPkC3().intValue();
    int PaxGroesse = CONF.zufallszahl(c3Max[0], c3Max[1]);

    Airport nachFlughafen = zuFlughafen;
    int zZJob = CONF.zufallszahl(0, c3Jobs.size() - 1);

    if (c3Jobs.get(zZJob).getZz() > 0) {
      if (CONF.zufallszahl(0, c3Jobs.get(zZJob).getZz()) != c3Jobs.get(zZJob).getZz()) {
        return false;
      }
    }

    String JobBezeichnung = c3Jobs.get(zZJob).getBezeichnung();
    double JobFaktor = c3Jobs.get(zZJob).getFaktor();

    onCreateAssignment(Basispreis, JobFaktor, vonFlughafen, nachFlughafen, PaxGroesse, JobBezeichnung, 2);
    return true;
  }

  private boolean cZZ4(Airport vonFlughafen, Airport zuFlughafen) {
    int Basispreis = fa.getPkC4().intValue();
    int PaxGroesse = CONF.zufallszahl(c4Max[0], c4Max[1]);

    Airport nachFlughafen = zuFlughafen;
    int zZJob = CONF.zufallszahl(0, c4Jobs.size() - 1);

    if (c4Jobs.get(zZJob).getZz() > 0) {
      if (CONF.zufallszahl(0, c4Jobs.get(zZJob).getZz()) != c4Jobs.get(zZJob).getZz()) {
        return false;
      }
    }

    String JobBezeichnung = c4Jobs.get(zZJob).getBezeichnung();
    double JobFaktor = c4Jobs.get(zZJob).getFaktor();

    onCreateAssignment(Basispreis, JobFaktor, vonFlughafen, nachFlughafen, PaxGroesse, JobBezeichnung, 2);
    return true;
  }

  private boolean cZZ5(Airport vonFlughafen, Airport zuFlughafen) {
    int Basispreis = fa.getPkC5().intValue();
    int PaxGroesse = CONF.zufallszahl(c5Max[0], c5Max[1]);

    Airport nachFlughafen = zuFlughafen;
    int zZJob = CONF.zufallszahl(0, c5Jobs.size() - 1);

    if (c5Jobs.get(zZJob).getZz() > 0) {
      if (CONF.zufallszahl(0, c5Jobs.get(zZJob).getZz()) != c5Jobs.get(zZJob).getZz()) {
        return false;
      }
    }

    String JobBezeichnung = c5Jobs.get(zZJob).getBezeichnung();
    double JobFaktor = c5Jobs.get(zZJob).getFaktor();

    onCreateAssignment(Basispreis, JobFaktor, vonFlughafen, nachFlughafen, PaxGroesse, JobBezeichnung, 2);
    return true;
  }

  private boolean cZZ6(Airport vonFlughafen, Airport zuFlughafen) {
    int Basispreis = fa.getPkC5().intValue();
    int PaxGroesse = CONF.zufallszahl(c6Max[0], c6Max[1]);

    Airport nachFlughafen = zuFlughafen;
    int zZJob = CONF.zufallszahl(0, c6Jobs.size() - 1);

    if (c6Jobs.get(zZJob).getZz() > 0) {
      if (CONF.zufallszahl(0, c6Jobs.get(zZJob).getZz()) != c6Jobs.get(zZJob).getZz()) {
        return false;
      }
    }

    String JobBezeichnung = c5Jobs.get(zZJob).getBezeichnung();
    double JobFaktor = c5Jobs.get(zZJob).getFaktor();

    onCreateAssignment(Basispreis, JobFaktor, vonFlughafen, nachFlughafen, PaxGroesse, JobBezeichnung, 2);
    return true;
  }

  private void onCreateAssignment(int Preis, double Faktor, Airport vonFlugHafen, Airport nachFlugHafen, int Menge, String Bezeichnung, int RoutenArt) {

    boolean assiOK = false;

    if (RoutenArt == 1 && zaehlerPax <= minAuftraege) {
      assiOK = true;
      //zaehlerPax = zaehlerPax + 1;
    }

    if (RoutenArt == 2 && zaehlerCargo <= minCargo) {
      assiOK = true;
      //zaehlerCargo = zaehlerCargo + 1;
    }

    if (assiOK) {

      double EntfernungsFaktor = 1;
      int Distanz[] = CONF.DistanzBerechnung(vonFlugHafen.getLongitude(), vonFlugHafen.getLatitude(), nachFlugHafen.getLongitude(), nachFlugHafen.getLatitude());

      Date jetzt = new Date();
      Date jobTime;
      long neueZeit;
      double TonnenPreis = 1500; // pro 100 Meilen

      // Gepaeck für jeden Passagier seperat ausrechnen
      int GesamtGepaeck = 0;
      int GewichtPax = 0;
      if (RoutenArt == 1) {
        for (int i = 0; i < Menge; i++) {
          GesamtGepaeck = GesamtGepaeck + CONF.zufallszahl(1, config.getBasisGewichtGepaeck().intValue());
          GewichtPax = GewichtPax + CONF.zufallszahl(60, config.getBasisGewichtPassagier().intValue());

        }
      }

      if (Distanz[0] < 50) {
        EntfernungsFaktor = 3;
      } else if (Distanz[0] > 49 & Distanz[0] < 76) {
        EntfernungsFaktor = 2;
      }

      Assignement newAssignment = new Assignement();

      newAssignment.setActive(1);
      newAssignment.setAirlineLogo("http://www.ftw-sim.de/images/FTW/ftw-job.png");

      if (RoutenArt == 1 && zaehlerPax <= minAuftraege) {
        zaehlerPax = zaehlerPax + Menge;
      }

      if (RoutenArt == 2 && zaehlerCargo <= minCargo) {
        zaehlerCargo = zaehlerCargo + Menge;

        if ((zaehlerCargo + Menge) > minCargo) {
          Menge = (zaehlerCargo + Menge) - minCargo;
        }

      }

      newAssignment.setAmmount(Menge);

      newAssignment.setBonusclosed(0.0);
      newAssignment.setBonusoeffentlich(0.0);
      newAssignment.setCeoAirline("");
      newAssignment.setComment(Bezeichnung);
      newAssignment.setCommodity(Bezeichnung);
      newAssignment.setCreatedbyuser("FTW-System");
      newAssignment.setCreation(jetzt);
      newAssignment.setDaysclaimedactive(0);
      newAssignment.setDirect(Distanz[1]);
      newAssignment.setDistance(Distanz[0]);

      // FtwJobsAblauf ist in Sekungen
      long milli = CONF.zufallszahl(fa.getFtwJobsAblauf() / 2, fa.getFtwJobsAblauf());

      // Minuten * 60 * 1000
      neueZeit = jetzt.getTime() + (long) (milli * 60 * 1000);

      //Cargojobs laufen 5 Tage länger als Pax Jobs
      if (RoutenArt == 2) {
        neueZeit = jetzt.getTime() + (long) ((milli + (5 * 24 * 60)) * 60 * 1000);
      }

      jobTime = new Date(neueZeit);
      newAssignment.setExpires(jobTime);
      newAssignment.setFlugrouteName("Standard-Job");
      newAssignment.setFromAirportLandCity(vonFlugHafen.getStadt() + " " + vonFlugHafen.getLand());
      newAssignment.setFromIcao(vonFlugHafen.getIcao());
      newAssignment.setLocationIcao(vonFlugHafen.getIcao());
      newAssignment.setFromName(vonFlugHafen.getName());
      newAssignment.setGepaeck(GesamtGepaeck);
      newAssignment.setGewichtPax(GewichtPax);
      newAssignment.setGruppe("");
      newAssignment.setIdAirline(-1);
      newAssignment.setIdRoute(-1);
      newAssignment.setIdaircraft(-1);
      newAssignment.setIdcommodity(-1);
      newAssignment.setIdgroup(-1);
      newAssignment.setIdowner(-1);
      newAssignment.setIsBusinessClass(0);
      newAssignment.setLizenz("Standard-Job");
      newAssignment.setMpttax(0);
      newAssignment.setNameairline("Standard-Job");
      newAssignment.setNoext(0);
      newAssignment.setOeffentlich(1);

      // ************************************************** Passagiere 
      if (RoutenArt == 1) {
        newAssignment.setPay(PreiskalkulationPax(Faktor * Preis, Menge, Distanz[0], vonFlugHafen));
      }

      // ************************************************** Cargo
      if (RoutenArt == 2) {
        newAssignment.setPay(PreiskalkulationCargo(Faktor * Preis, Menge, Distanz[0], vonFlugHafen));
      }

      newAssignment.setPilotfee(0);
      newAssignment.setPtassigment("");
      newAssignment.setRoutenArt(RoutenArt);
      newAssignment.setToAirportLandCity(nachFlugHafen.getStadt() + " " + nachFlugHafen.getLand());
      newAssignment.setToIcao(nachFlugHafen.getIcao());
      newAssignment.setToName(nachFlugHafen.getName());

// Type 1=Routen Job, 2=Standard-Job, 3=Random-Job, 4=Linien-Job, 5=Airport Agent
      newAssignment.setType(2);
      newAssignment.setUnits("");
      newAssignment.setUserlock(0);
      newAssignment.setProvision(0.0);
      newAssignment.setIdFBO(-300);
      newAssignment.setIdTerminal(-300);
      newAssignment.setIcaoCodeFluggesellschaft("FTW");
      newAssignment.setVerlaengert(false);
      newAssignment.setGesplittet(false);
      newAssignment.setIdjob(-1);

      //*********** Langstrecke eintragen
      if (Distanz[0] > 500) {
        newAssignment.setLangstrecke(true);
      } else {
        newAssignment.setLangstrecke(false);
      }

      facadeAssignment.create(newAssignment);
      if (RoutenArt == 1) {
        ErzeugteJobsImDurchlauf = ErzeugteJobsImDurchlauf + Menge;
      }
    }// if (zaehler <= minAuftraege)
  }

  private void onCreateAssignmentFromTemplate(int Preis, double Faktor, Airport vonFlugHafen, Airport nachFlugHafen, int Menge, String Bezeichnung, int RoutenArt) {

    boolean assiOK = true;

    if (assiOK) {

      double EntfernungsFaktor = 1;
      int Distanz[] = CONF.DistanzBerechnung(vonFlugHafen.getLongitude(), vonFlugHafen.getLatitude(), nachFlugHafen.getLongitude(), nachFlugHafen.getLatitude());

      Date jetzt = new Date();
      Date jobTime;
      long neueZeit;
      double TonnenPreis = 1500; // pro 100 Meilen

      // Gepaeck für jeden Passagier seperat ausrechnen
      int GesamtGepaeck = 0;
      int GewichtPax = 0;
      if (RoutenArt == 1) {
        for (int i = 0; i < Menge; i++) {
          GesamtGepaeck = GesamtGepaeck + CONF.zufallszahl(1, config.getBasisGewichtGepaeck().intValue());
          GewichtPax = GewichtPax + CONF.zufallszahl(60, config.getBasisGewichtPassagier().intValue());

        }
      }

      if (Distanz[0] < 50) {
        EntfernungsFaktor = 3;
      } else if (Distanz[0] > 49 & Distanz[0] < 76) {
        EntfernungsFaktor = 2;
      }

      Assignement newAssignment = new Assignement();

      newAssignment.setActive(1);
      newAssignment.setAirlineLogo("http://www.ftw-sim.de/images/FTW/ftw-job.png");

      if (RoutenArt == 1) {
        zaehlerPax = zaehlerPax + Menge;
      }

      if (RoutenArt == 2) {
        zaehlerCargo = zaehlerCargo + Menge;
      }
      newAssignment.setAmmount(Menge);

      newAssignment.setBonusclosed(0.0);
      newAssignment.setBonusoeffentlich(0.0);
      newAssignment.setCeoAirline("");
      newAssignment.setComment(Bezeichnung);
      newAssignment.setCommodity(Bezeichnung);
      newAssignment.setCreatedbyuser("FTW-System");
      newAssignment.setCreation(jetzt);
      newAssignment.setDaysclaimedactive(0);
      newAssignment.setDirect(Distanz[1]);
      newAssignment.setDistance(Distanz[0]);

      // FtwJobsAblauf ist in Sekungen
      long milli = (long) (CONF.zufallszahl(5, 12) * CONF.zufallszahl(1, 60) * 60);

      neueZeit = jetzt.getTime() + (long) (milli * 1000);

//      //Cargojobs laufen 5 Tage länger als Pax Jobs
//      if (RoutenArt == 2) {
//        neueZeit = jetzt.getTime() + (long) ((milli + (5 * 24 * 60)) * 60 * 1000);
//      }
      jobTime = new Date(neueZeit);
      newAssignment.setExpires(jobTime);
      newAssignment.setFlugrouteName("Standard-Job");
      newAssignment.setFromAirportLandCity(vonFlugHafen.getStadt() + " " + vonFlugHafen.getLand());
      newAssignment.setFromIcao(vonFlugHafen.getIcao());
      newAssignment.setLocationIcao(vonFlugHafen.getIcao());
      newAssignment.setFromName(vonFlugHafen.getName());
      newAssignment.setGepaeck(GesamtGepaeck);
      newAssignment.setGewichtPax(GewichtPax);
      newAssignment.setGruppe("");
      newAssignment.setIdAirline(-1);
      newAssignment.setIdRoute(-1);
      newAssignment.setIdaircraft(-1);
      newAssignment.setIdcommodity(-1);
      newAssignment.setIdgroup(-1);
      newAssignment.setIdowner(-1);
      newAssignment.setIsBusinessClass(0);
      newAssignment.setLizenz("Standard-Job");
      newAssignment.setMpttax(0);
      newAssignment.setNameairline("Standard-Job");
      newAssignment.setNoext(0);
      newAssignment.setOeffentlich(1);

      // ************************************************** Passagiere 
      if (RoutenArt == 1) {
        newAssignment.setPay(PreiskalkulationPax(Faktor * Preis, Menge, Distanz[0], vonFlugHafen));
      }

      // ************************************************** Cargo
      if (RoutenArt == 2) {
        newAssignment.setPay(PreiskalkulationCargo(Faktor * Preis, Menge, Distanz[0], vonFlugHafen));
      }

      newAssignment.setPilotfee(0);
      newAssignment.setPtassigment("");
      newAssignment.setRoutenArt(RoutenArt);
      newAssignment.setToAirportLandCity(nachFlugHafen.getStadt() + " " + nachFlugHafen.getLand());
      newAssignment.setToIcao(nachFlugHafen.getIcao());
      newAssignment.setToName(nachFlugHafen.getName());

      // Type 1=Routen Job, 2=Standard-Job, 3=Random-Job, 4=Linien-Job, 5=Airport Agent
      newAssignment.setType(2);
      newAssignment.setUnits("");
      newAssignment.setUserlock(0);
      newAssignment.setProvision(0.0);
      newAssignment.setIdFBO(-300);
      newAssignment.setIdTerminal(-300);
      newAssignment.setIcaoCodeFluggesellschaft("FTW");
      newAssignment.setVerlaengert(false);
      newAssignment.setGesplittet(false);
      newAssignment.setIdjob(-1);

      //*********** Langstrecke eintragen
      if (Distanz[0] > 500) {
        newAssignment.setLangstrecke(true);
      } else {
        newAssignment.setLangstrecke(false);
      }

      facadeAssignment.create(newAssignment);
//      if (RoutenArt == 1) {
//        ErzeugteJobsImDurchlauf = ErzeugteJobsImDurchlauf + Menge;
//      }
    }// if (zaehler <= minAuftraege)
  }

  private double PreiskalkulationPax(double betrag, int Menge, double entfernung, Airport Airport_FromICAO) {
    /*
            *********************** Preiskalkulation *********************************
     */
    double PriceForPassenger = betrag;
    double SubventionsFaktor = CONF.getSubvention(Airport_FromICAO.getKlasse());

    if (SubventionsFaktor <= 0) {
      SubventionsFaktor = 1;
    }

    double MeilenPreis = (betrag / 100.0);

    if (Menge == 0) {
      Menge = 1;
    }

    if (entfernung <= 25) {
      MeilenPreis = MeilenPreis * 6 * SubventionsFaktor;
    } else if (entfernung > 25 && entfernung <= 50) {
      MeilenPreis = MeilenPreis * 6 * SubventionsFaktor;
    } else if (entfernung > 50 && entfernung <= 75) {
      MeilenPreis = MeilenPreis * 6 * SubventionsFaktor;
    } else if (entfernung > 75 && entfernung <= 100) {
      MeilenPreis = MeilenPreis * 3.5;
    } else if (entfernung > 100 && entfernung <= 150) {
      MeilenPreis = MeilenPreis * 2;
    } else if (entfernung > 150 && entfernung <= 250) {
      MeilenPreis = MeilenPreis * 1.5;
    } else if (entfernung > 250 && entfernung <= 350) {
      MeilenPreis = MeilenPreis * 1;
    } else if (entfernung > 350 && entfernung <= 550) {
      MeilenPreis = MeilenPreis * 0.8;
    } else if (entfernung > 550 && entfernung <= 1000) {
      MeilenPreis = MeilenPreis * 0.65;
    } else if (entfernung > 1000 && entfernung <= 2000) {
      MeilenPreis = MeilenPreis * 0.6;
    } else if (entfernung > 2000 && entfernung <= 3000) {
      MeilenPreis = MeilenPreis * 0.55;
    } else if (entfernung > 3000 && entfernung <= 3500) {
      MeilenPreis = MeilenPreis * 0.5;
    } else if (entfernung > 3500 && entfernung <= 5000) {
      MeilenPreis = MeilenPreis * 0.30;
    } else if (entfernung > 5000) {
      MeilenPreis = MeilenPreis * 0.25;
    }

    betrag = MeilenPreis;

    PriceForPassenger = betrag;

    if (Menge > 1 && Menge <= 5) {
      PriceForPassenger = betrag * 0.95;
    } else if (Menge > 5 && Menge <= 10) {
      PriceForPassenger = betrag * 0.90;
    } else if (Menge > 10 && Menge <= 15) {
      PriceForPassenger = betrag * 0.85;
    } else if (Menge > 15 && Menge <= 20) {
      PriceForPassenger = betrag * 0.80;
    } else if (Menge > 20 && Menge <= 30) {
      PriceForPassenger = betrag * 0.78;
    } else if (Menge > 30 && Menge <= 50) {
      PriceForPassenger = betrag * 0.75;
    } else if (Menge > 50 && Menge <= 100) {
      PriceForPassenger = betrag * 0.70;
    }

    double Summe = (PriceForPassenger * Menge * entfernung) * CONF.zufallszahlDouble(0.98, 1.15);

    return (double) Math.round(Summe);

  }

  private double PreiskalkulationCargo(double betrag, int Menge, double entfernung, Airport Airport_FromICAO) {

    /*
            *********************** Preiskalkulation *********************************
     */
    double PriceForCargo = betrag;
    double SubventionsFaktor = CONF.getSubvention(Airport_FromICAO.getKlasse());

    if (SubventionsFaktor <= 0) {
      SubventionsFaktor = 1;
    }

    double MeilenPreis = (betrag / 100.0);

    if (Menge == 0) {
      Menge = 1;
    }

    if (entfernung <= 25) {
      MeilenPreis = MeilenPreis * 6 * SubventionsFaktor;
    } else if (entfernung > 25 && entfernung <= 50) {
      MeilenPreis = MeilenPreis * 6 * SubventionsFaktor;
    } else if (entfernung > 50 && entfernung <= 75) {
      MeilenPreis = MeilenPreis * 6 * SubventionsFaktor;
    } else if (entfernung > 75 && entfernung <= 100) {
      MeilenPreis = MeilenPreis * 3.5;
    } else if (entfernung > 100 && entfernung <= 150) {
      MeilenPreis = MeilenPreis * 3;
    } else if (entfernung > 150 && entfernung <= 250) {
      MeilenPreis = MeilenPreis * 2;
    } else if (entfernung > 150 && entfernung <= 250) {
      MeilenPreis = MeilenPreis * 1.5;
    } else if (entfernung > 250 && entfernung <= 350) {
      MeilenPreis = MeilenPreis * 1.85;
    } else if (entfernung > 350 && entfernung <= 550) {
      MeilenPreis = MeilenPreis * 1.80;
    } else if (entfernung > 550 && entfernung <= 1000) {
      MeilenPreis = MeilenPreis * 1.0;
    } else if (entfernung > 1000 && entfernung <= 2000) {
      MeilenPreis = MeilenPreis * 1.4;
    } else if (entfernung > 2000 && entfernung <= 3000) {
      MeilenPreis = MeilenPreis * 1.0;
    } else if (entfernung > 3000 && entfernung <= 3500) {
      MeilenPreis = MeilenPreis * 0.5;
    } else if (entfernung > 3000 && entfernung <= 3500) {
      MeilenPreis = MeilenPreis * 0.4;
    } else if (entfernung > 3500 && entfernung <= 5000) {
      MeilenPreis = MeilenPreis * 0.30;
    } else if (entfernung > 5000) {
      MeilenPreis = MeilenPreis * 0.25;
    }

    betrag = MeilenPreis;

    if (Menge > 0 && Menge <= 50) {
      PriceForCargo = betrag * 1.0;
    } else if (Menge > 50 && Menge <= 100) {
      PriceForCargo = betrag * 0.99;
    } else if (Menge > 100 && Menge <= 200) {
      PriceForCargo = betrag * 0.98;
    } else if (Menge > 200 && Menge <= 500) {
      PriceForCargo = betrag * 0.97;
    } else if (Menge > 500 && Menge <= 750) {
      PriceForCargo = betrag * 0.96;
    } else if (Menge > 750 && Menge <= 1000) {
      PriceForCargo = betrag * 0.90;
    } else if (Menge > 1000 && Menge <= 2000) {
      PriceForCargo = betrag * 0.80;
    } else if (Menge > 2000 && Menge <= 3000) {
      PriceForCargo = betrag * 0.75;
    } else if (Menge > 3000 && Menge <= 4000) {
      PriceForCargo = betrag * 0.70;
    } else if (Menge > 4000 && Menge <= 5000) {
      PriceForCargo = betrag * 0.65;
    } else if (Menge > 5000 && Menge <= 7500) {
      PriceForCargo = betrag * 0.60;
    } else if (Menge > 7500 && Menge <= 10000) {
      PriceForCargo = betrag * 0.55;
    } else if (Menge > 10000) {
      PriceForCargo = betrag * 0.50;
    }

    double Summe = (betrag * Menge * entfernung) * CONF.zufallszahlDouble(0.98, 1.05);

    return (double) Math.round(Summe);

  }

  /*
  ************************      Setter and Getter
   */
  public String getSuchText() {
    return SuchText;
  }

  public void setSuchText(String SuchText) {
    this.SuchText = SuchText;
  }

  public Assignement getSelectedAssignment() {
    return currentAssignment;
  }

  public void setSelectedAssignment(Assignement selectedAssignment) {
    currentAssignment = selectedAssignment;
  }

  public boolean isViewMyFlight() {
    return ViewMyFlight;
  }

  public void setViewMyFlight(boolean ViewMyFlight) {
    this.ViewMyFlight = ViewMyFlight;
  }

  public List<Assignement> getSelectedAssignments() {
    return selectedAssignments;
  }

  public void setSelectedAssignments(List<Assignement> selectedAssignments) {
    this.selectedAssignments = selectedAssignments;
  }

  public List<Assignement> getSelectedHoldAssignments() {
    return selectedHoldAssignments;
  }

  public void setSelectedHoldAssignments(List<Assignement> selectedHoldAssignments) {
    this.selectedHoldAssignments = selectedHoldAssignments;
  }

  public int getMengeEcoHold() {
    int summe = 0;
    for (Assignement assi : AssignmentsHold) {
      if (assi.getRoutenArt().equals(1)) {
        summe = summe + assi.getAmmount();
      }
    }
    return summe;
  }

  public int getMengeECOSelect() {
    int summe = 0;
    if (selectedAssignments != null) {
      for (Assignement assi : selectedAssignments) {
        if (assi.getRoutenArt().equals(1)) {
          summe = summe + assi.getAmmount();
        }
      }
    }
    return summe;
  }

  public int getMengeEcoFilter() {
    int summe = 0;
    if (filteredAssignmenst != null) {
      for (Assignement assi : filteredAssignmenst) {
        if (assi.getRoutenArt().equals(1)) {
          summe = summe + assi.getAmmount();
        }
      }
    }
    return summe;
  }

  public int getMengeBCHold() {
    int summe = 0;
    for (Assignement assi : AssignmentsHold) {
      if (assi.getRoutenArt().equals(4)) {
        summe = summe + assi.getAmmount();
      }
    }
    return summe;
  }

  public int getMengeBCSelected() {
    int summe = 0;
    if (selectedAssignments != null) {
      for (Assignement assi : selectedAssignments) {
        if (assi.getRoutenArt().equals(4)) {
          summe = summe + assi.getAmmount();
        }
      }
    }
    return summe;
  }

  public int getMengeBCFilter() {
    int summe = 0;
    if (filteredAssignmenst != null) {
      for (Assignement assi : filteredAssignmenst) {
        if (assi.getRoutenArt().equals(4)) {
          summe = summe + assi.getAmmount();
        }
      }
    }
    return summe;
  }

  public int getMengeCargoHold() {
    int summe = 0;
    for (Assignement assi : AssignmentsHold) {
      if (assi.getRoutenArt().equals(2)) {
        summe = summe + assi.getAmmount();
      }
    }
    return summe;
  }

  public int getMengeCargoSelected() {
    int summe = 0;
    if (selectedAssignments != null) {
      for (Assignement assi : selectedAssignments) {
        if (assi.getRoutenArt().equals(2)) {
          summe = summe + assi.getAmmount();
        }
      }
    }
    return summe;
  }

  public int getMengeCargoFilter() {
    int summe = 0;
    if (filteredAssignmenst != null) {
      for (Assignement assi : filteredAssignmenst) {
        if (assi.getRoutenArt().equals(2)) {
          summe = summe + assi.getAmmount();
        }
      }
    }
    return summe;
  }

  public double getSummeBC() {
    double summe = 0;
    if (Assignments != null) {
      for (Assignement assi : Assignments) {
        if (assi.getRoutenArt().equals(4)) {
          summe = summe + assi.getPay();
        }
      }
    }
    return summe;
  }

  public double getSummeBCHold() {
    double summe = 0;
    if (Assignments != null) {
      for (Assignement assi : AssignmentsHold) {
        if (assi.getRoutenArt().equals(4)) {
          summe = summe + assi.getPay();
        }
      }
    }
    return summe;
  }

  public double getSummeBCSelected() {
    double summe = 0;
    if (selectedAssignments != null) {
      for (Assignement assi : selectedAssignments) {
        if (assi.getRoutenArt().equals(4)) {
          summe = summe + assi.getPay();
        }
      }
    }
    return summe;
  }

  public double getSummeBCFilter() {
    double summe = 0;
    if (filteredAssignmenst != null) {
      for (Assignement assi : filteredAssignmenst) {
        if (assi.getRoutenArt().equals(4)) {
          summe = summe + assi.getPay();
        }
      }
    }
    return summe;
  }

  public double getSummeEco() {
    double summe = 0;
    if (Assignments != null) {
      for (Assignement assi : Assignments) {
        if (assi.getRoutenArt().equals(1)) {
          summe = summe + assi.getPay();
        }
      }
    }

    return summe;
  }

  public double getSummeEcoHold() {
    double summe = 0;
    for (Assignement assi : AssignmentsHold) {
      if (assi.getRoutenArt().equals(1)) {
        summe = summe + assi.getPay();
      }
    }
    return summe;
  }

  public double getSummeEcoSelect() {
    double summe = 0;
    if (selectedAssignments != null) {
      for (Assignement assi : selectedAssignments) {
        if (assi.getRoutenArt().equals(1)) {
          summe = summe + assi.getPay();
        }
      }
    }
    return summe;
  }

  public double getSummeEcoFilter() {
    double summe = 0;
    if (filteredAssignmenst != null) {
      for (Assignement assi : filteredAssignmenst) {
        if (assi.getRoutenArt().equals(1)) {
          summe = summe + assi.getPay();
        }
      }
    }
    return summe;
  }

  public double getSummeCargo() {
    double summe = 0;
    if (Assignments != null) {
      for (Assignement assi : Assignments) {
        if (assi.getRoutenArt().equals(2)) {
          summe = summe + assi.getPay();
        }
      }
    }
    return summe;
  }

  public double getSummeCargoHold() {
    double summe = 0;
    for (Assignement assi : AssignmentsHold) {
      if (assi.getRoutenArt().equals(2)) {
        summe = summe + assi.getPay();
      }
    }
    return summe;
  }

  public double getSummeCargoSelected() {
    double summe = 0;
    if (selectedAssignments != null) {
      for (Assignement assi : selectedAssignments) {
        if (assi.getRoutenArt().equals(2)) {
          summe = summe + assi.getPay();
        }
      }
    }
    return summe;
  }

  public double getSummeCargoFilter() {
    double summe = 0;
    if (filteredAssignmenst != null) {
      for (Assignement assi : filteredAssignmenst) {
        if (assi.getRoutenArt().equals(2)) {
          summe = summe + assi.getPay();
        }
      }
    }
    return summe;
  }

  public int getMengePassengersTakeOff() {
    int Passengers = 0;
    if (Assignments != null) {
      for (Assignement assi : Assignments) {
        if (assi.getRoutenArt().equals(1)) {
          Passengers = Passengers + assi.getAmmount();
        }
      }
    }
    return Passengers;
  }

  public int getMengeBuisnessPassengersTakeOff() {
    int Passengers = 0;
    if (Assignments != null) {
      for (Assignement assi : Assignments) {
        if (assi.getRoutenArt().equals(4)) {
          Passengers = Passengers + assi.getAmmount();
        }
      }
    }
    return Passengers;
  }

  public int getMengeCargoTakeOff() {
    int Cargo = 0;
    if (Assignments != null) {
      for (Assignement assi : Assignments) {
        if (assi.getRoutenArt().equals(2)) {
          Cargo = Cargo + assi.getAmmount();
        }
      }
    }
    return Cargo;
  }

  public double getSummeTakeOff() {
    double TakeOff = 0;
    if (Assignments != null) {
      for (Assignement assi : Assignments) {
        TakeOff = TakeOff + assi.getPay();
      }
    }
    return TakeOff;
  }

  public int getGewichtGepaeck() {
    int summe = 0;
    if (Assignments != null) {
      for (Assignement assi : Assignments) {
        if (assi.getRoutenArt().equals(1) || assi.getRoutenArt().equals(4)) {
          summe = summe + assi.getGepaeck();
        }
      }
    }
    return summe;
  }

  public int getGewichtGepaeckHold() {
    int summe = 0;
    if (Assignments != null) {
      for (Assignement assi : AssignmentsHold) {
        if (assi.getRoutenArt() == 1 || assi.getRoutenArt() == 4) {
          summe = summe + assi.getGepaeck();
        }
      }
    }
    return summe;
  }

  public int getGewichtGepaeckSelected() {
    int summe = 0;
    if (selectedAssignments != null) {
      for (Assignement assi : selectedAssignments) {
        if (assi.getRoutenArt() == 1 || assi.getRoutenArt() == 4) {
          summe = summe + assi.getGepaeck();
        }
      }
    }
    return summe;
  }

  public int getGewichtGepaeckFilter() {
    int summe = 0;
    if (filteredAssignmenst != null) {
      for (Assignement assi : filteredAssignmenst) {
        if (assi.getRoutenArt() == 1 || assi.getRoutenArt() == 4) {
          summe = summe + assi.getGepaeck();
        }
      }
    }
    return summe;
  }

  public int getGewichtPassagiereEco() {
    int summe = 0;
    if (Assignments != null) {
      for (Assignement assi : Assignments) {
        if (assi.getRoutenArt().equals(1)) {
          summe = summe + assi.getGewichtPax();
        }
      }
    }
    return summe;
  }

  public int getGewichtPassagiereEcoHold() {
    int summe = 0;
    for (Assignement assi : AssignmentsHold) {
      if (assi.getRoutenArt().equals(1)) {
        summe = summe + assi.getGewichtPax();
      }
    }
    return summe;
  }

  public int getGewichtPassagiereEcoSelect() {
    int summe = 0;
    if (selectedAssignments != null) {
      for (Assignement assi : selectedAssignments) {
        if (assi.getRoutenArt().equals(1)) {
          summe = summe + assi.getGewichtPax();
        }
      }
    }
    return summe;
  }

  public int getGewichtPassagiereEcoFilter() {
    int summe = 0;
    if (filteredAssignmenst != null) {
      for (Assignement assi : filteredAssignmenst) {
        if (assi.getRoutenArt().equals(1)) {
          summe = summe + assi.getGewichtPax();
        }
      }
    }
    return summe;
  }

  public int getGewichtPassagiereBC() {
    int summe = 0;
    if (Assignments != null) {
      for (Assignement assi : Assignments) {
        if (assi.getRoutenArt().equals(4)) {
          summe = summe + assi.getGewichtPax();
        }
      }
    }
    return summe;
  }

  public int getGewichtPassagiereBCHold() {
    int summe = 0;
    if (Assignments != null) {
      for (Assignement assi : AssignmentsHold) {
        if (assi.getRoutenArt().equals(4)) {
          summe = summe + assi.getGewichtPax();
        }
      }
    }
    return summe;
  }

  public int getGewichtPassagiereBCSelected() {
    int summe = 0;
    if (selectedAssignments != null) {
      for (Assignement assi : selectedAssignments) {
        if (assi.getRoutenArt().equals(4)) {
          summe = summe + assi.getGewichtPax();
        }
      }
    }
    return summe;
  }

  public int getGewichtPassagiereBCFilter() {
    int summe = 0;
    if (filteredAssignmenst != null) {
      for (Assignement assi : filteredAssignmenst) {
        if (assi.getRoutenArt().equals(4)) {
          summe = summe + assi.getGewichtPax();
        }
      }
    }
    return summe;
  }

  public String getHeaderText() {
    return headerText;
  }

  public void setHeaderText(String headerText) {
    this.headerText = headerText;
  }

  public String getTimeZone() {
    return Calendar.getInstance().getTimeZone().getID();
  }

  public int getNorden() {
    return Norden;
  }

  public int getOsten() {
    return Osten;
  }

  public int getSueden() {
    return Sueden;
  }

  public int getWesten() {
    return Westen;
  }

  public int getNullPunkt() {
    return NullPunkt;
  }

  public int getTableHight() {
    return TableHight;
  }

  public String getICAOS() {
    return ICAOS;
  }

  public List<String> getZielFlughaefen() {
    if (reverseSuch.equals("nachICAO")) {
      return facadeAssignment.DistinctByToICAO(SuchText, 0);
    } else {

      if (FluggesellschaftID > 0) {
        return facadeAssignment.DistinctByToICAOFGSelect(SuchText, FluggesellschaftID);
      }

      return facadeAssignment.DistinctByICAOLocation(SuchText, 0);
    }
  }

  public List<String> getAbFlughaefen() {

    if (reverseSuch.equals("nachICAO")) {
      return facadeAssignment.DistinctByFromICAO(SuchText, OwnerID);
    }

    return facadeAssignment.DistinctByICAOLocationAllAssignment();
  }

  public void setZielFlughaefen(Map<String, String> zielFlughaefen) {
    this.zielFlughaefen = zielFlughaefen;
  }

  public String getZielFlughafenSelected() {
    return zielFlughafenSelected;
  }

  public void setZielFlughafenSelected(String zielFlughafenSelected) {
    this.zielFlughafenSelected = zielFlughafenSelected;
  }

  public String getFlughafenName() {
    return FlughafenName;
  }

  public void setFlughafenName(String FlughafenName) {
    this.FlughafenName = FlughafenName;
  }

  public int getGepaeck() {
    return Gepaeck;
  }

  public void setGepaeck(int Gepaeck) {
    this.Gepaeck = Gepaeck;
  }

  public int getPassagiereGewicht() {
    return PassagiereGewicht;
  }

  public void setPassagiereGewicht(int PassagiereGewicht) {
    this.PassagiereGewicht = PassagiereGewicht;
  }

  public String getReverseSuch() {
    return reverseSuch;
  }

  public void setReverseSuch(String reverseSuch) {
    this.reverseSuch = reverseSuch;
  }

  public boolean isReverse() {
    return reverse;
  }

  public void setReverse(boolean reverse) {
    this.reverse = reverse;
  }

  public int getFluggesellschaftID() {
    return FluggesellschaftID;
  }

  public void setFluggesellschaftID(int FluggesellschaftID) {
    this.FluggesellschaftID = FluggesellschaftID;
  }

  private int MinAuftraege(int Klasse) {

    switch (Klasse) {
      case 1:
        return 120;
      case 2:
        return 100;
      case 3:
        return 90;
      case 4:
        return 80;
      case 5:
        return 70;
      case 6:
        return 50;
      case 7:
        return 30;
      case 8:
        return 20;
      case 9:
        return 20;
      case 10:
        return 40;
      case 11:
        return 60;
      case 12:
        return 5;
      case 13:
        return 0;
      case 14:
        return 0;
      default:
        return 0;

    }
  }

  private int MinCargo(int Klasse) {

    switch (Klasse) {
      case 1:
        return 120;
      case 2:
        return 100;
      case 3:
        return 90;
      case 4:
        return 80;
      case 5:
        return 70;
      case 6:
        return 50;
      case 7:
        return 30;
      case 8:
        return 20;
      case 9:
        return 20;
      case 10:
        return 40;
      case 11:
        return 60;
      case 12:
        return 5;
      case 13:
        return 0;
      case 14:
        return 0;
      default:
        return 0;

    }
  }

  public boolean isHatUserEineFluggesellschaft() {

    //********** Hat Benutzer eine Fluggesellschaft
    if (facadeAssignment.hatUserEineFluggesellschaft(UserID)) {
      return true;
    }

    if (getFluggesellschaften().size() > 0) {
      return true;
    }

    //Ist Benutzer Pilot einer FG und darf konvertieren
    try {
      if (getPilotFluggesellschaften() != null) {
        for (ViewFluggesellschaftPiloten pilot : getPilotFluggesellschaften()) {
          if (pilot.getDarfkonvertieren()) {
            return true;
          }
        }
      }

    } catch (NullPointerException e) {
      return false;
    }

    return false;
  }

  public void setHatUserEineFluggesellschaft(boolean hatUserEineFluggesellschaft) {
    this.hatUserEineFluggesellschaft = hatUserEineFluggesellschaft;
  }

  public boolean isAuftragIstOeffentlich() {
    return auftragIstOeffentlich;
  }

  public void setAuftragIstOeffentlich(boolean auftragIstOeffentlich) {
    this.auftragIstOeffentlich = auftragIstOeffentlich;
  }

  public double getFrmBonusOeffentlich() {
    return frmBonusOeffentlich;
  }

  public void setFrmBonusOeffentlich(double frmBonusOeffentlich) {
    this.frmBonusOeffentlich = frmBonusOeffentlich;
  }

  public double getFrmBonusFGPiloten() {
    return frmBonusFGPiloten;
  }

  public void setFrmBonusFGPiloten(double frmBonusFGPiloten) {
    this.frmBonusFGPiloten = frmBonusFGPiloten;
  }

  public double getFrmProvision() {
    return frmProvision;
  }

  public void setFrmProvision(double frmProvision) {
    this.frmProvision = frmProvision;
  }

  public List<Assignement> getFilteredAssignmenst() {
    return filteredAssignmenst;
  }

  public void setFilteredAssignmenst(List<Assignement> filteredAssignmenst) {
    this.filteredAssignmenst = filteredAssignmenst;
  }

  public boolean isVisuellAuftragDialog() {
    return VisuellAuftragDialog;
  }

  public void setVisuellAuftragDialog(boolean VisuellAuftragDialog) {
    this.VisuellAuftragDialog = VisuellAuftragDialog;
  }

  public String getMaxZeilen() {
    try {
      return CONF.getCookie("AuftragsplanungMaxZeilen").getValue();
    } catch (NullPointerException e) {
      return "20";
    }
  }

  public void setMaxZeilen(String MaxZeilen) {
    this.MaxZeilen = MaxZeilen;
  }

  private void keineFlughaefenKlasse9Gefunden() {

  }

  public ViewFlugzeugeMietKauf getMeinGemietetesFlugzeug() {
    try {

      MeinGemietetesFlugzeug = facadeAssignment.getRentedAircraft(UserID);

      FlugzeugTyp = MeinGemietetesFlugzeug.getType();

      if (MeinGemietetesFlugzeug.getIdSitzKonfiguration() > 0) {
        sitzKonfiguration = facadeAssignment.getSitzKonfiguration(MeinGemietetesFlugzeug.getIdSitzKonfiguration());

        SitzeBusiness = sitzKonfiguration.getSitzeBusiness();
        SitzeEconomy = sitzKonfiguration.getSitzeEconomy();
        CargoVerfuegbar = sitzKonfiguration.getCargo();
        maxPayload = sitzKonfiguration.getMaxPayload();

      } else {
        SitzeBusiness = MeinGemietetesFlugzeug.getSitzeBusinessClass();
        SitzeEconomy = MeinGemietetesFlugzeug.getSitzeEconomy();
        CargoVerfuegbar = MeinGemietetesFlugzeug.getCargo();
        maxPayload = MeinGemietetesFlugzeug.getPayload();
      }

      return MeinGemietetesFlugzeug;

    } catch (NullPointerException e) {
      FlugzeugTyp = "Kein Flugzeug gemietet";
      SitzeBusiness = 0;
      SitzeEconomy = 0;
      CargoVerfuegbar = 0;
      maxPayload = 0;

      //System.out.println("Assignments Nullpointer : Kein Flugzeug gemietet.");
      return null;
    }

  }

  public void setMeinGemietetesFlugzeug(ViewFlugzeugeMietKauf MeinGemietetesFlugzeug) {
    this.MeinGemietetesFlugzeug = MeinGemietetesFlugzeug;
  }

  public String getFlugzeugTyp() {
    return FlugzeugTyp;
  }

  public void setFlugzeugTyp(String FlugzeugTyp) {
    this.FlugzeugTyp = FlugzeugTyp;
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

  public int getCargoVerfuegbar() {
    return CargoVerfuegbar;
  }

  public void setCargoVerfuegbar(int CargoVerfuegbar) {
    this.CargoVerfuegbar = CargoVerfuegbar;
  }

  public int getMaxPayload() {
    return maxPayload;
  }

  public void setMaxPayload(int maxPayload) {
    this.maxPayload = maxPayload;
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

  private double KlassenFaktorFuerPreise(int KlasseNr) {

    switch (KlasseNr) {
      case 1:
        return 0.80;
      case 2:
        return 0.85;
      case 3:
        return 0.85;
      case 4:
        return 0.90;
      case 5:
        return 0.95;
      default:
        return 1.00;
    }

  }

  public AgentJobList getSelectedAgentJob() {
    return selectedAgentJob;
  }

  public void setSelectedAgentJob(AgentJobList selectedAgentJob) {
    this.selectedAgentJob = selectedAgentJob;
  }

  public int getIdFuerUserAuftraege() {
    return idFuerUserAuftraege;
  }

  public void setIdFuerUserAuftraege(int idFuerUserAuftraege) {
    this.idFuerUserAuftraege = idFuerUserAuftraege;
  }

}
