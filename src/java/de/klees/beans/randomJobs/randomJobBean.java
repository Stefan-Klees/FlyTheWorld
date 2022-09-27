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

package de.klees.beans.randomJobs;

import de.klees.beans.assignement.AgentJobList;
import de.klees.beans.assignement.AssignementFacade;
import de.klees.beans.system.CONF;
import de.klees.data.Airport;
import de.klees.data.Assignement;
import de.klees.data.CharterAuftrag;
import de.klees.data.Feinabstimmung;
import de.klees.data.Flugzeuge;
import de.klees.data.Sitzkonfiguration;
import de.klees.data.Story;
import de.klees.data.views.ViewFlugzeugeMietKauf;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Stefan Klees
 */
public class randomJobBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private Assignement RD;

  private Story story;
  private List<Story> storyList;
  private List<Airport> flughaefenListe;
  private Airport flughafen;
  private Feinabstimmung config;
  private ViewFlugzeugeMietKauf fg;

  private int rdMenge;
  private String rdComment;
  private String rdCommodity;
  private int rdKurs;
  private int rdEntfernung;
  private Date rdAblauf;
  private String rdVonIcao;
  private String rdVonName;
  private String rdVonFlughafenName;
  private String rdVonFlughafenLandStad;
  private String rdNachIcao;
  private String rdNachName;
  private String rdNachFlughafenName;
  private String rdNachFlughafenLandStad;
  private int rdGepaeck;
  private int rdGewichtPax;
  private String rdLocationIcao;
  private double rdBezahlung;
  // Type 1=PAX, 2=CARGO, 3=TRF, 4=Business-PAX
  private int rdRoutenArt;
  // Type 1=Routen Job, 2=Standard-Job, 3=Golden-Job, 4=Linien-Job
  private int rdType;
  private int rdUserLock;
  private int rdGesamtGewicht;

  private boolean storyGefunden;
  private int UserID;
  private String StandOrt;
  private boolean IstAllesKlarFuerJob;
  private int AnzahlEcoSitzplaetze;
  private int MaxCargo;

  private boolean okButton;
  private boolean geschichteFertig;

  private final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
  private final SimpleDateFormat dfSql = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
  private final DecimalFormat numf = new DecimalFormat("#,##0");

  private Story testGeschichte;

  private ArrayList<AgentJobList> Joblist = new ArrayList<>();
  private AgentJobList selectedAgentJob;
  private String AgentICAO;
  private String flugzeugTyp;
  private String StoryBezeichnung;

  @EJB
  AssignementFacade facadeRD;

  public randomJobBean() {
    storyGefunden = false;
    geschichteFertig = false;
    okButton = false;
    UserID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
    df.setTimeZone(TimeZone.getTimeZone((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ZeitZone")));
    dfSql.setTimeZone(TimeZone.getTimeZone((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ZeitZone")));
  }

  public void onGoldenJobErstellen() {

    fg = facadeRD.getRentedAircraft(UserID);
    // prüfen ob ein Flugzeug gemietet ist
    if (fg != null) {
      // gibt es schon eine Charter Auftrag
      // wenn ja muss nicht weiter geprüft werden

      if (!facadeRD.hatUserEinenCharterAuftrag(UserID)) {

        RD = new Assignement();

        // Active = 0 Aktiver Auftrag, 1 = Auftrag ist in der Wartehalle
        RD.setActive(0);
        RD.setAirlineLogo("http://www.ftw-sim.de/images/FTW/golden-job.png");
        RD.setAmmount(rdMenge);
        RD.setBonusclosed(0.0);
        RD.setBonusoeffentlich(0.0);
        RD.setCeoAirline("FTW-System");
        RD.setComment(rdComment);
        RD.setCommodity(rdCommodity);
        RD.setCreatedbyuser("FTW-System");
        RD.setDirect(rdKurs);
        RD.setDistance(rdEntfernung);
        RD.setExpires(rdAblauf);
        RD.setFlugrouteName("Charter Auftrag");
        RD.setFromAirportLandCity(rdVonFlughafenLandStad);
        RD.setFromIcao(rdVonIcao);
        RD.setFromName(rdVonFlughafenName);
        RD.setGepaeck(rdGepaeck);
        RD.setGewichtPax(rdGewichtPax);
        RD.setIcaoCodeFluggesellschaft("CA");
        RD.setIdAirline(-1);
        RD.setIdFBO(-1);
        RD.setIdRoute(-1);
        RD.setIdTerminal(-1);
        RD.setIdaircraft(-1);
        RD.setIdowner(rdUserLock);
        RD.setIsBusinessClass(0);
        RD.setLocationIcao(rdLocationIcao);
        RD.setNameairline("Charter Auftrag");
        RD.setOeffentlich(0);
        RD.setPay(rdBezahlung);
        RD.setProvision(0.0);
        // Type 1=PAX, 2=CARGO, 3=TRF, 4=Business-PAX, 5 Spritfaesser
        RD.setRoutenArt(rdRoutenArt);
        RD.setToAirportLandCity(rdNachFlughafenLandStad);
        RD.setToIcao(rdNachIcao);
        RD.setToName(rdNachFlughafenName);

        // Type 1=Routen Job, 2=Standard-Job, 3=Random-Job, 4=Linien-Job, 5=Treibstofftransport
        RD.setType(rdType);

        // UserLock 0 = Bereit zum Abflug, 1 = Fliegt gerade
        RD.setUserlock(0);

        RD.setVerlaengert(false);

        // Diese Felder werden zur Zeit nicht gebraucht
        RD.setBearing(0);
        RD.setCreation(new Date());
        RD.setDaysclaimedactive(0);
        RD.setGruppe("");
        RD.setIdcommodity(-1);
        RD.setIdgroup(-1);
        RD.setLizenz(fg.getLizenz());
        RD.setMpttax(0);
        RD.setNoext(0);
        RD.setPilotfee(0);
        RD.setPtassigment("");
        RD.setUnits("");
        RD.setIdjob(-1);

        //*********** Langstrecke eintragen, Bei Charter alles Kurzstrecke
        RD.setLangstrecke(false);

        facadeRD.createRD(RD);

        setIstAllesKlarFuerJob(false);

      }
    } else {
      okButton = false;
    }

  }

  /*
  ************** Airport Agent BEGINN
   */
  public boolean isIstAllesKlarFuerJob() {

    AgentICAO = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Assignment_ICAO");

    // Prüfen ob ein Charter-Aufrag läuft
    CharterAuftrag ch = facadeRD.getCharterSperre(UserID);

    if (ch != null) {
      //Charter Auftrag vorhanden, ein weiterer Charter-Auftrag ist nicht erlaubt.
      return false;
    }

    // gibt es schon eine Charter Auftrag
    // wenn ja muss nicht weiter geprüft werden
    if (facadeRD.hatUserEinenCharterAuftrag(UserID)) {
      return false;
    }

    ViewFlugzeugeMietKauf fg = facadeRD.getRentedAircraft(UserID);

    // ist ein Flugzeug gemietet
    if (fg != null && AgentICAO != null) {

      int VerfuegbareSitze = fg.getSitzeEconomy();

      if (VerfuegbareSitze <= 0) {
        VerfuegbareSitze = fg.getSitzeBusinessClass();
      }

//      if (VerfuegbareSitze <= 0) {
//        return false;
//      }
      MaxCargo = fg.getCargo();

      //Steht das Flugzeug am richtigen Flughafen
      if (!fg.getAktuellePositionICAO().equals(AgentICAO.toUpperCase())) {
        return false;
      }

      //*** Eventuelle sitzkonfiguration auslesen  
      if (fg.getIdSitzKonfiguration() > 0) {
        Sitzkonfiguration konf = facadeRD.getSitzKonfiguration(fg.getIdSitzKonfiguration());
        if (konf != null) {

          VerfuegbareSitze = konf.getSitzeEconomy();

          if (VerfuegbareSitze <= 0) {
            VerfuegbareSitze = konf.getSitzeBusiness();
          }

          MaxCargo = konf.getCargo();
        }
      }

      if ((fg.getFlugzeugArt().equals("Passagierflugzeug") || fg.getFlugzeugArt().equals("Hubschrauber")) && VerfuegbareSitze > 16) {
        return false;
      }

      if (VerfuegbareSitze > 24) {
        return false;
      }

      AnzahlEcoSitzplaetze = VerfuegbareSitze;

    } else {
      return false;
    }
    return true;
  }

  public void onAuftragSperreSetzen() {

    CharterAuftrag ch = new CharterAuftrag();
    ch.setUserID(UserID);
    Date ablauf = new Date(new Date().getTime() + ((long) (60 * 60 * 1000)));

    try {
      ch.setAblaufzeit(dfSql.parse(dfSql.format(ablauf)));
    } catch (ParseException ex) {
      Logger.getLogger(randomJobBean.class.getName()).log(Level.SEVERE, null, ex);
    }

    facadeRD.createCharterAuftrag(ch);
    setIstAllesKlarFuerJob(false);

  }

  public void onCharterAgentStart() {
    onAuftragSperreSetzen();
    onCharterAgent();
  }

  public void onCharterAgent() {

    Joblist.clear();
    boolean TypeCargo = false;

    ViewFlugzeugeMietKauf meinFlugzeug = facadeRD.getRentedAircraft(UserID);
    String Sprache = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Sprache");

    if (meinFlugzeug != null) {
      AgentICAO = meinFlugzeug.getAktuellePositionICAO();
    } else {
      AgentICAO = "";
    }

    if (meinFlugzeug != null && !AgentICAO.equals("") && meinFlugzeug.getAktuellePositionICAO().equals(AgentICAO)) {
      if (meinFlugzeug.getMaxAnzahlZuBelgenderSitze() >= 1 || meinFlugzeug.getCargo() > 0) {

        config = facadeRD.readConfig();

        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Assignment_ICAO", AgentICAO);
        Flugzeuge Stammflugzeug = facadeRD.readStammflugzeug(meinFlugzeug.getIdFlugzeug());

        Airport abflug = facadeRD.getAirportByIcao(AgentICAO);

        int maxPax = meinFlugzeug.getSitzeBusinessClass() + meinFlugzeug.getSitzeEconomy();
        int maxCargo = meinFlugzeug.getCargo();
        int Flugbegleiter = meinFlugzeug.getFlugbegleiter();
        String Lizenz = Stammflugzeug.getLizenz();
        String FlzArt = Stammflugzeug.getFlugzeugArt();
        int idFlugzeug = meinFlugzeug.getIdMietKauf();
        

        // Fuer Hubschrauber gibt es noch keine extra Storys
        if (FlzArt.equals("Hubschrauber")) {
          FlzArt = "Passagierflugzeug";
        }

        flugzeugTyp = Stammflugzeug.getType();

        if (meinFlugzeug.getIdSitzKonfiguration() > 0) {
          Sitzkonfiguration sk = facadeRD.getSitzKonfiguration(meinFlugzeug.getIdSitzKonfiguration());
          maxPax = sk.getSitzeBusiness() + sk.getSitzeEconomy();
          maxCargo = sk.getCargo();
          Flugbegleiter = sk.getFlugbegleiter();
        }

        int maxSpeed = meinFlugzeug.getReisegeschwindigkeitTAS();
        double Taxiing = 0.25;

        if (!Sprache.equals("de")) {
          Sprache = "en";
        }

        List<Story> story = facadeRD.StorysForAircraft(Sprache, FlzArt, Lizenz);

        if (story != null) {
          int max = story.size();
          if (max > 5) {
            max = 5;
          }

          for (int ik = 0; ik < max; ik++) {

            Story st = null;

            if (max < 5) {
              st = story.get(ik);
            } else {
              st = story.get(CONF.zufallszahl(0, story.size() - 1));
            }

            // 1 = Pax, 2 = Cargo
            if (st.getArtdestransports() == 2) {
              TypeCargo = true;
            } else {
              TypeCargo = false;
            }

            int minEntfernung = st.getMindestEnfernung();
            int maxEnfernung = st.getMaxEntferung();

            if (maxEnfernung <= 0) {
              maxEnfernung = Stammflugzeug.getMaxReichweite();
            }

            int minKlasse = 1;
            int maxKlasse = abflug.getKlasse() + 2;

            if (maxKlasse > 8) {
              maxKlasse = 8;
            }

            if (minKlasse == maxKlasse) {
              maxKlasse = 4;
            }

            if (TypeCargo) {
              if (maxCargo < 2000) {
                Taxiing = 0.5;
                maxKlasse = 8;
              } else if (maxCargo >= 2000 && maxCargo < 4000) {
                Taxiing = 0.5;
                maxKlasse = 7;
              } else if (maxCargo >= 4000 && maxCargo < 8000) {
                Taxiing = 0.5;
                maxKlasse = 7;
              } else if (maxCargo >= 8000 && maxCargo < 10000) {
                Taxiing = 0.5;
                maxKlasse = 6;
              } else if (maxCargo >= 10000 && maxCargo < 20000) {
                Taxiing = 0.5;
                maxKlasse = 6;
              } else if (maxCargo >= 20000 && maxCargo < 40000) {
                maxKlasse = 6;
                Taxiing = 0.5;
              } else if (maxCargo >= 40000 && maxCargo < 60000) {
                maxKlasse = 4;
                Taxiing = 0.5;
              } else if (maxCargo >= 60000 && maxCargo < 100000) {
                maxKlasse = 4;
                Taxiing = 0.5;
              } else if (maxCargo >= 100000) {
                maxKlasse = 4;
                Taxiing = 0.5;
              }
            } else {
              if (maxPax < 5) {
                maxKlasse = 8;
              } else if (maxPax >= 5 && maxPax < 10) {
                maxKlasse = 8;
              } else if (maxPax >= 10 && maxPax < 20) {
                maxKlasse = 7;
                if (meinFlugzeug.getAntriebsart() > 1) {
                }
              } else if (maxPax >= 20 && maxPax < 50) {
                maxKlasse = 6;
                if (meinFlugzeug.getAntriebsart() > 1) {
                  maxEnfernung = 550;
                }
              } else if (maxPax >= 50 && maxPax < 100) {
                maxKlasse = 6;
              } else if (maxPax >= 100 && maxPax < 150) {
                maxKlasse = 6;
              } else if (maxPax >= 100 && maxPax < 150) {
                Taxiing = 0.5;
                maxKlasse = 6;
              } else if (maxPax >= 150 && maxPax < 200) {
                maxKlasse = 5;
                Taxiing = 0.5;
              } else if (maxPax >= 200 && maxPax < 250) {
                maxKlasse = 5;
                Taxiing = 0.5;
              } else if (maxPax >= 250 && maxPax < 300) {
                Taxiing = 0.5;
                maxKlasse = 4;
              } else if (maxPax >= 300) {
                Taxiing = 0.5;
                maxKlasse = 4;
              }
            }

            if (abflug != null) {

              double bgVon = 0.0;
              double bgBis = 0.0;
              double lgVon = 0.0;
              double lgBis = 0.0;
              double Umkreis = 180.0;

              if (abflug.getLatitude() < 0.0) {
                bgVon = abflug.getLatitude() - Umkreis;
                bgBis = abflug.getLatitude() + Umkreis;
              } else {
                bgVon = abflug.getLatitude() - Umkreis;
                bgBis = abflug.getLatitude() + Umkreis;
              }

              if (abflug.getLongitude() < 0.0) {
                lgVon = abflug.getLongitude() - Umkreis;
                lgBis = abflug.getLongitude() + Umkreis;
              } else {
                lgVon = abflug.getLongitude() - Umkreis;
                lgBis = abflug.getLongitude() + Umkreis;
              }

              boolean gefunden = false;
              int Entfernung = 0;
              int Kurs = 0;

              Airport airp = null;
              List<Airport> flughaefen = facadeRD.getJobFlughaefen(bgVon, bgBis, lgVon, lgBis, abflug.getIcao(), minKlasse, maxKlasse);

              for (int i = 0; i < flughaefen.size(); i++) {
                airp = flughaefen.get(CONF.zufallszahl(1, flughaefen.size() - 1));

                int Ergebnis[] = CONF.DistanzBerechnung(abflug.getLongitude(), abflug.getLatitude(), airp.getLongitude(), airp.getLatitude());
                if (Ergebnis[0] >= minEntfernung && Ergebnis[0] <= maxEnfernung) {
                  Entfernung = Ergebnis[0];
                  Kurs = Ergebnis[1];

                  gefunden = true;
                  break;
                }

              }

              if (gefunden) {

                AgentJobList AgentJobEintrag = new AgentJobList();
                AgentJobEintrag.setEntfernung(Entfernung);
                AgentJobEintrag.setKurs(Kurs);
                AgentJobEintrag.setNachBezeichnung(airp.getName());
                AgentJobEintrag.setNachLand(airp.getLand() + " " + airp.getStadt());
                AgentJobEintrag.setNachICAO(airp.getIcao());
                AgentJobEintrag.setKlasse(airp.getKlasse());
                AgentJobEintrag.setCargo(0);
                AgentJobEintrag.setCharterKurzbezeichnung(st.getBezeichnung());
                
                //Eintraege für das Klassensystem bei den Flugzeugen
                //09.02.2021
                AgentJobEintrag.setIdFlugzeug(idFlugzeug);
                AgentJobEintrag.setLizenz(Lizenz);
                //Ende

                if (TypeCargo) {
                  AgentJobEintrag.setPax(0);
                  AgentJobEintrag.setCargo(CONF.zufallszahl(maxCargo / 4, maxCargo));
                } else {
                  if (maxPax <= 3) {
                    AgentJobEintrag.setPax(CONF.zufallszahl(1, maxPax));
                  } else {
                    AgentJobEintrag.setPax(CONF.zufallszahl(maxPax / 3, maxPax));
                  }

                }

                AgentJobEintrag.setVonICAO(abflug.getIcao());

                //************ Auftragswert
                int Verguetung = 0;

                double FlugzeitStd = (double) Entfernung / meinFlugzeug.getReisegeschwindigkeitTAS();
                // Abweichende Geschwindigkeit bei Start und Landung berücksichtigen
                FlugzeitStd = FlugzeitStd + Taxiing;

                double Spritkosten = 0.0;

                if (meinFlugzeug.getTreibstoffArt() == 1) {
                  Spritkosten = FlugzeitStd * meinFlugzeug.getVerbrauchStunde() * config.getPreisAVGASkg();
                } else {
                  Spritkosten = FlugzeitStd * meinFlugzeug.getVerbrauchStunde() * config.getPreisJETAkg();
                }

                double PilotGehalt = FlugzeitStd * st.getVerguetung();
                double KalkulatorischerStundensatz = Stammflugzeug.getKalkulatorischerStundensatz() * FlugzeitStd;
                double Zwischensumme = Spritkosten + PilotGehalt + KalkulatorischerStundensatz;
                double Terminal = Zwischensumme * 0.10;
                double Flughafen = Zwischensumme * 0.02;
                double FlughafenPersonal = Zwischensumme * 0.01;
                double GehaltFlugbegleiter = Flugbegleiter * (config.getAbrFlugbegleiter() * FlugzeitStd);

                double Summe = Zwischensumme + Terminal + Flughafen + FlughafenPersonal + GehaltFlugbegleiter;

                AgentJobEintrag.setVerguetung((int) Summe);

                // Gepaeck für jeden Passagier seperat ausrechnen
                int GesamtGepaeck = 0;
                int GewichtPax = 0;

                for (int i = 0; i < AgentJobEintrag.getPax(); i++) {
                  GesamtGepaeck = GesamtGepaeck + CONF.zufallszahl(1, config.getBasisGewichtGepaeck().intValue());
                  GewichtPax = GewichtPax + CONF.zufallszahl(60, config.getBasisGewichtPassagier().intValue());
                }

                int SummeGewicht = GesamtGepaeck + GewichtPax + AgentJobEintrag.getCargo();

                AgentJobEintrag.setGesamtGepaeck(GesamtGepaeck);
                AgentJobEintrag.setGewichtPax(GewichtPax);

                Date jetzt = new Date();
                Date jobTime;
                long neueZeit;

                // Minuten * 60 * 1000
                neueZeit = jetzt.getTime() + (long) 30 * 60 * 1000;
                jobTime = new Date(neueZeit);

                String rdCommodity = st.getStoryText()
                        .replaceAll("#flughafen_city#", airp.getStadt() + " " + airp.getLand())
                        .replaceAll("#flughafen_icao#", airp.getIcao())
                        .replaceAll("#standort_city#", abflug.getStadt() + " " + abflug.getLand())
                        .replaceAll("#standort_icao#", abflug.getIcao())
                        .replaceAll("#meilen#", numf.format(Entfernung))
                        .replaceAll("#summe#", numf.format(Summe))
                        .replaceAll("#gewicht#", numf.format(SummeGewicht))
                        .replaceAll("#anzahl#", numf.format(AgentJobEintrag.getPax()))
                        .replaceAll("#minuten#", numf.format(30))
                        .replaceAll("#datum#", df.format(jobTime))
                        .replaceAll("#geschichte_von#", st.getVerfasser());

                AgentJobEintrag.setBezeichnung(rdCommodity);

                Joblist.add(AgentJobEintrag);
              }

            }
          }
        }
      }

    }
  }

  public void onCharterAgentTest() {

    boolean TypeCargo = false;
    Joblist.clear();

    ViewFlugzeugeMietKauf meinFlugzeug = facadeRD.getRentedAircraft(UserID);
    String Sprache = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Sprache");

    if (meinFlugzeug != null) {
      AgentICAO = meinFlugzeug.getAktuellePositionICAO();
    } else {
      AgentICAO = "";
    }

    if (meinFlugzeug != null && !AgentICAO.equals("") && meinFlugzeug.getAktuellePositionICAO().equals(AgentICAO)) {
      if (meinFlugzeug.getMaxAnzahlZuBelgenderSitze() >= 1 || meinFlugzeug.getCargo() > 0) {

        config = facadeRD.readConfig();

        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Assignment_ICAO", AgentICAO);
        Flugzeuge Stammflugzeug = facadeRD.readStammflugzeug(meinFlugzeug.getIdFlugzeug());

        Airport abflug = facadeRD.getAirportByIcao(AgentICAO);

        int maxPax = meinFlugzeug.getSitzeBusinessClass() + meinFlugzeug.getSitzeEconomy();
        int maxCargo = meinFlugzeug.getCargo();
        int Flugbegleiter = meinFlugzeug.getFlugbegleiter();
        String Lizenz = Stammflugzeug.getLizenz();
        String FlzArt = Stammflugzeug.getFlugzeugArt();
        flugzeugTyp = Stammflugzeug.getType();

        if (meinFlugzeug.getIdSitzKonfiguration() > 0) {
          Sitzkonfiguration sk = facadeRD.getSitzKonfiguration(meinFlugzeug.getIdSitzKonfiguration());
          maxPax = sk.getSitzeBusiness() + sk.getSitzeEconomy();
          maxCargo = sk.getCargo();
          Flugbegleiter = sk.getFlugbegleiter();
        }

        int maxSpeed = meinFlugzeug.getReisegeschwindigkeitTAS();
        double Taxiing = 0.25;

        if (!Sprache.equals("de")) {
          Sprache = "en";
        }

        List<Story> story = facadeRD.StorysForAircraftTest(StoryBezeichnung);

        if (story != null) {
          int max = story.size();

          for (int ik = 0; ik < max; ik++) {

            Story st = story.get(ik);

            // 1 = Pax, 2 = Cargo
            if (st.getArtdestransports() == 2) {
              TypeCargo = true;
              maxPax = 0;
            } else {
              TypeCargo = false;
            }

            int minEntfernung = st.getMindestEnfernung();
            int maxEnfernung = st.getMaxEntferung();

            if (maxEnfernung <= 0) {
              maxEnfernung = Stammflugzeug.getMaxReichweite();
            }

            int minKlasse = 1;
            int maxKlasse = abflug.getKlasse() + 2;

            if (maxKlasse > 8) {
              maxKlasse = 8;
            }

            if (minKlasse == maxKlasse) {
              maxKlasse = 4;
            }

            if (TypeCargo) {
              if (maxCargo < 2000) {
                Taxiing = 0.5;
                maxKlasse = 8;
              } else if (maxCargo >= 2000 && maxCargo < 4000) {
                Taxiing = 0.5;
                maxKlasse = 7;
              } else if (maxCargo >= 4000 && maxCargo < 8000) {
                Taxiing = 0.5;
                maxKlasse = 7;
              } else if (maxCargo >= 8000 && maxCargo < 10000) {
                Taxiing = 0.5;
                maxKlasse = 6;
              } else if (maxCargo >= 10000 && maxCargo < 20000) {
                Taxiing = 0.5;
                maxKlasse = 6;
              } else if (maxCargo >= 20000 && maxCargo < 40000) {
                maxKlasse = 6;
                Taxiing = 0.5;
              } else if (maxCargo >= 40000 && maxCargo < 60000) {
                maxKlasse = 4;
                Taxiing = 0.5;
              } else if (maxCargo >= 60000 && maxCargo < 100000) {
                maxKlasse = 4;
                Taxiing = 0.5;
              } else if (maxCargo >= 100000) {
                maxKlasse = 4;
                Taxiing = 0.5;
              }
            } else {
              if (maxPax < 5) {
                maxKlasse = 8;
              } else if (maxPax >= 5 && maxPax < 10) {
                maxKlasse = 8;
              } else if (maxPax >= 10 && maxPax < 20) {
                maxKlasse = 7;
                if (meinFlugzeug.getAntriebsart() > 1) {

                }
              } else if (maxPax >= 20 && maxPax < 50) {
                maxKlasse = 6;
                if (meinFlugzeug.getAntriebsart() > 1) {
                  maxEnfernung = 550;
                }
              } else if (maxPax >= 50 && maxPax < 100) {
                maxKlasse = 6;
              } else if (maxPax >= 100 && maxPax < 150) {
                maxKlasse = 6;
              } else if (maxPax >= 100 && maxPax < 150) {
                Taxiing = 0.5;
                maxKlasse = 6;
              } else if (maxPax >= 150 && maxPax < 200) {
                maxKlasse = 5;
                Taxiing = 0.5;
              } else if (maxPax >= 200 && maxPax < 250) {
                maxKlasse = 5;
                Taxiing = 0.5;
              } else if (maxPax >= 250 && maxPax < 300) {
                Taxiing = 0.5;
                maxKlasse = 4;
              } else if (maxPax >= 300) {
                Taxiing = 0.5;
                maxKlasse = 4;
              }
            }

            if (abflug != null) {

              double bgVon = 0.0;
              double bgBis = 0.0;
              double lgVon = 0.0;
              double lgBis = 0.0;
              double Umkreis = 180.0;

              if (abflug.getLatitude() < 0.0) {
                bgVon = abflug.getLatitude() - Umkreis;
                bgBis = abflug.getLatitude() + Umkreis;
              } else {
                bgVon = abflug.getLatitude() - Umkreis;
                bgBis = abflug.getLatitude() + Umkreis;
              }

              if (abflug.getLongitude() < 0.0) {
                lgVon = abflug.getLongitude() - Umkreis;
                lgBis = abflug.getLongitude() + Umkreis;
              } else {
                lgVon = abflug.getLongitude() - Umkreis;
                lgBis = abflug.getLongitude() + Umkreis;
              }

              boolean gefunden = false;
              int Entfernung = 0;
              int Kurs = 0;

              Airport airp = null;
              List<Airport> flughaefen = facadeRD.getJobFlughaefen(bgVon, bgBis, lgVon, lgBis, abflug.getIcao(), minKlasse, maxKlasse);

              for (int i = 0; i < flughaefen.size(); i++) {
                airp = flughaefen.get(CONF.zufallszahl(1, flughaefen.size() - 1));

                int Ergebnis[] = CONF.DistanzBerechnung(abflug.getLongitude(), abflug.getLatitude(), airp.getLongitude(), airp.getLatitude());
                if (Ergebnis[0] >= minEntfernung && Ergebnis[0] <= maxEnfernung) {
                  Entfernung = Ergebnis[0];
                  Kurs = Ergebnis[1];

                  gefunden = true;
                  break;
                }

              }

              if (gefunden) {

                AgentJobList AgentJobEintrag = new AgentJobList();
                AgentJobEintrag.setEntfernung(Entfernung);
                AgentJobEintrag.setKurs(Kurs);
                AgentJobEintrag.setNachBezeichnung(airp.getName());
                AgentJobEintrag.setNachLand(airp.getLand() + " " + airp.getStadt());
                AgentJobEintrag.setNachICAO(airp.getIcao());
                AgentJobEintrag.setKlasse(airp.getKlasse());
                AgentJobEintrag.setCargo(0);
                AgentJobEintrag.setCharterKurzbezeichnung(st.getBezeichnung());
                AgentJobEintrag.setLizenz(st.getFlugzeuglizenz());
                AgentJobEintrag.setSprache(st.getSprache());
                AgentJobEintrag.setFlugzeugArt(st.getFlugzeugKlasse());

                if (TypeCargo) {
                  AgentJobEintrag.setPax(0);
                  AgentJobEintrag.setCargo(CONF.zufallszahl(maxCargo / 4, maxCargo));
                } else {
                  AgentJobEintrag.setPax(CONF.zufallszahl(maxPax / 3, maxPax));
                }

                AgentJobEintrag.setVonICAO(abflug.getIcao());

                //************ Auftragswert
                int Verguetung = 0;

                double FlugzeitStd = (double) Entfernung / meinFlugzeug.getReisegeschwindigkeitTAS();
                // Abweichende Geschwindigkeit bei Start und Landung berücksichtigen
                FlugzeitStd = FlugzeitStd + Taxiing;

                double Spritkosten = 0.0;

                if (meinFlugzeug.getTreibstoffArt() == 1) {
                  Spritkosten = FlugzeitStd * meinFlugzeug.getVerbrauchStunde() * config.getPreisAVGASkg();
                } else {
                  Spritkosten = FlugzeitStd * meinFlugzeug.getVerbrauchStunde() * config.getPreisJETAkg();
                }

                double PilotGehalt = FlugzeitStd * st.getVerguetung();
                double KalkulatorischerStundensatz = Stammflugzeug.getKalkulatorischerStundensatz() * FlugzeitStd;
                double Zwischensumme = Spritkosten + PilotGehalt + KalkulatorischerStundensatz;
                double Terminal = Zwischensumme * 0.10;
                double Flughafen = Zwischensumme * 0.02;
                double FlughafenPersonal = Zwischensumme * 0.01;
                double GehaltFlugbegleiter = Flugbegleiter * (config.getAbrFlugbegleiter() * FlugzeitStd);

                double Summe = Zwischensumme + Terminal + Flughafen + FlughafenPersonal + GehaltFlugbegleiter;

                AgentJobEintrag.setVerguetung((int) Summe);

                // Gepaeck für jeden Passagier seperat ausrechnen
                int GesamtGepaeck = 0;
                int GewichtPax = 0;

                for (int i = 0; i < AgentJobEintrag.getPax(); i++) {
                  GesamtGepaeck = GesamtGepaeck + CONF.zufallszahl(1, config.getBasisGewichtGepaeck().intValue());
                  GewichtPax = GewichtPax + CONF.zufallszahl(60, config.getBasisGewichtPassagier().intValue());
                }

                AgentJobEintrag.setGesamtGepaeck(GesamtGepaeck);
                AgentJobEintrag.setGewichtPax(GewichtPax);

                int SummeGewicht = GesamtGepaeck + GewichtPax + AgentJobEintrag.getCargo();

                Date jetzt = new Date();
                Date jobTime;
                long neueZeit;

                // Minuten * 60 * 1000
                neueZeit = jetzt.getTime() + (long) 30 * 60 * 1000;
                jobTime = new Date(neueZeit);

                String rdCommodity = st.getStoryText()
                        .replaceAll("#flughafen_city#", airp.getStadt() + " " + airp.getLand())
                        .replaceAll("#flughafen_icao#", airp.getIcao())
                        .replaceAll("#standort_city#", abflug.getStadt() + " " + abflug.getLand())
                        .replaceAll("#standort_icao#", abflug.getIcao())
                        .replaceAll("#meilen#", numf.format(Entfernung))
                        .replaceAll("#summe#", numf.format(Summe))
                        .replaceAll("#gewicht#", numf.format(SummeGewicht))
                        .replaceAll("#anzahl#", numf.format(AgentJobEintrag.getPax()))
                        .replaceAll("#minuten#", numf.format(30))
                        .replaceAll("#datum#", df.format(jobTime))
                        .replaceAll("#geschichte_von#", st.getVerfasser());

                AgentJobEintrag.setBezeichnung(rdCommodity);

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

    // ***** Chartersperre raus, erst wieder rein, wenn die Erstellung erfolgreich war
    facadeRD.onCharterSperreEntfernen(UserID);
    //**********************************************************************************

    if (!facadeRD.hatUserEinenCharterAuftrag(UserID)) {

//      System.out.println("de.klees.beans.assignement.assignementBean.onAgentJobErstellen()");
      Date jetzt = new Date();
      Date jobTime;
      long neueZeit;

      config = facadeRD.readConfig();

      ViewFlugzeugeMietKauf meinFlugzeug = facadeRD.getRentedAircraft(UserID);

      // Gepaeck für jeden Passagier seperat ausrechnen
      int GesamtGepaeck = 0;
      int GewichtPax = 0;

      Airport vonFlugHafen = facadeRD.getAirportByIcao(AgentICAO);
      Airport nachFlugHafen = facadeRD.getAirportByIcao(selectedAgentJob.getNachICAO());

      Assignement newAssignment = new Assignement();
      // Active = 0 Aktiver Auftrag, 1 = Auftrag ist in der Wartehalle
      newAssignment.setActive(0);
      newAssignment.setAirlineLogo("http://www.ftw-sim.de/images/FTW/golden-job.png");

      // Pax oder Cargo Menge eintragen
      if (selectedAgentJob.getPax() == 0) {
        newAssignment.setAmmount(selectedAgentJob.getCargo());
      } else {
        newAssignment.setAmmount(selectedAgentJob.getPax());
      }

      newAssignment.setBonusclosed(0.0);
      newAssignment.setBonusoeffentlich(0.0);
      newAssignment.setCeoAirline("FTW-System");
      newAssignment.setComment("Charter-Job");
      newAssignment.setCommodity(selectedAgentJob.getBezeichnung());
      newAssignment.setCreatedbyuser("FTW-System");
      newAssignment.setCreation(jetzt);
      newAssignment.setDaysclaimedactive(0);
      newAssignment.setDirect(selectedAgentJob.getKurs());
      newAssignment.setDistance(selectedAgentJob.getEntfernung());

      // FtwJobsAblauf ist in Sekungen
      long milli = CONF.zufallszahl(config.getFtwJobsAblauf() / 2, config.getFtwJobsAblauf());

      // Minuten * 60 * 1000
      neueZeit = jetzt.getTime() + (long) 30 * 60 * 1000;
      jobTime = new Date(neueZeit);
      newAssignment.setExpires(jobTime);
      newAssignment.setFlugrouteName("Charter Auftrag");
      newAssignment.setFromAirportLandCity(vonFlugHafen.getStadt() + " " + vonFlugHafen.getLand());
      newAssignment.setFromIcao(vonFlugHafen.getIcao());
      newAssignment.setLocationIcao(vonFlugHafen.getIcao());
      newAssignment.setFromName(vonFlugHafen.getName());
      newAssignment.setGepaeck(selectedAgentJob.getGesamtGepaeck());
      newAssignment.setGewichtPax(selectedAgentJob.getGewichtPax());
      newAssignment.setIdAirline(-1);
      newAssignment.setIdRoute(-1);
      newAssignment.setLizenz(selectedAgentJob.getLizenz());
      newAssignment.setIdaircraft(selectedAgentJob.getIdFlugzeug());
      

      newAssignment.setIdowner(UserID);
      newAssignment.setIsBusinessClass(0);

      newAssignment.setNameairline("Charter Auftrag");
      newAssignment.setOeffentlich(0);
      // ************************************************** Passagiere 
      newAssignment.setPay((double) selectedAgentJob.getVerguetung());

      // Type 1=PAX, 2=CARGO, 3=TRF, 4=Business-PAX, 5 Spritfaesser
      if (selectedAgentJob.getPax() == 0) {
        newAssignment.setRoutenArt(2);
      } else {
        newAssignment.setRoutenArt(1);
      }

      newAssignment.setToAirportLandCity(nachFlugHafen.getLand() + " " + nachFlugHafen.getStadt());
      newAssignment.setToIcao(nachFlugHafen.getIcao());
      newAssignment.setToName(nachFlugHafen.getName());
      // Type 1=Routen Job, 2=Standard-Job, 3=Random-Job, 4=Linien-Job, 5=Airport Agent
      newAssignment.setType(3);
      newAssignment.setUserlock(0);
      newAssignment.setProvision(0.0);
      newAssignment.setIdFBO(-1);
      newAssignment.setIdTerminal(-1);
      newAssignment.setIcaoCodeFluggesellschaft("CA");
      newAssignment.setVerlaengert(false);
      newAssignment.setGesplittet(false);
      newAssignment.setIdjob(-1);
      //*********** Langstrecke eintragen
      newAssignment.setLangstrecke(true);

      // Diese Felder werden zur Zeit nicht gebraucht
      newAssignment.setBearing(0);
      newAssignment.setCreation(new Date());
      newAssignment.setDaysclaimedactive(0);
      newAssignment.setGruppe("");
      newAssignment.setIdcommodity(-1);
      newAssignment.setIdgroup(-1);
      newAssignment.setLizenz(meinFlugzeug.getLizenz());
      newAssignment.setMpttax(0);
      newAssignment.setNoext(0);
      newAssignment.setPilotfee(0);
      newAssignment.setPtassigment("");
      newAssignment.setUnits("");

      facadeRD.create(newAssignment);

      onAuftragSperreSetzen();

    } else {
      NewMessage("Du hast schon einen Charter-Auftrag angenommen, Auftrag wird nicht angenommen!");
    }

  }

  /*
  ************** Airport Agent ENDE
   */
  private void NewMessage(String neueNachricht) {
    FacesMessage msg = new FacesMessage(neueNachricht);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  // Setter and Getter
  public void setIstAllesKlarFuerJob(boolean IstAllesKlarFuerJob) {
    this.IstAllesKlarFuerJob = IstAllesKlarFuerJob;
  }

  public boolean isOkButton() {
    return okButton;
  }

  public void setOkButton(boolean okButton) {
    this.okButton = okButton;
  }

  public boolean isGeschichteFertig() {
    return geschichteFertig;
  }

  public void setGeschichteFertig(boolean geschichteFertig) {
    this.geschichteFertig = geschichteFertig;
  }

  public Story getTestGeschichte() {
    return testGeschichte;
  }

  public void setTestGeschichte(Story testGeschichte) {
    this.testGeschichte = testGeschichte;
  }

  public AgentJobList getSelectedAgentJob() {
    return selectedAgentJob;
  }

  public void setSelectedAgentJob(AgentJobList selectedAgentJob) {
    this.selectedAgentJob = selectedAgentJob;
  }

  public String getFlugzeugTyp() {
    return flugzeugTyp;
  }

  public void setFlugzeugTyp(String flugzeugTyp) {
    this.flugzeugTyp = flugzeugTyp;
  }

  public String getAgentICAO() {
    return AgentICAO;
  }

  public void setAgentICAO(String AgentICAO) {
    this.AgentICAO = AgentICAO;
  }

  public String getStoryBezeichnung() {
    return StoryBezeichnung;
  }

  public void setStoryBezeichnung(String StoryBezeichnung) {
    this.StoryBezeichnung = StoryBezeichnung;
  }

}
