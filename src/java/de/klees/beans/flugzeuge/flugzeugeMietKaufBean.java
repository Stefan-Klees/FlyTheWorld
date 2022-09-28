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

package de.klees.beans.flugzeuge;

import de.klees.beans.system.CONF;
import de.klees.beans.system.loginMB;
import de.klees.data.Airport;
import de.klees.data.Bank;
import de.klees.data.FboUserObjekte;
import de.klees.data.Feinabstimmung;
import de.klees.data.Fluggesellschaft;
import de.klees.data.Fluggesellschaftmanager;
import de.klees.data.Flugzeugblacklist;
import de.klees.data.Flugzeuge;
import de.klees.data.Flugzeugefluggesellschaft;
import de.klees.data.Flugzeugemietkauf;
import de.klees.data.Flugzeugsymbole;
import de.klees.data.Flugzeugtransfer;
import de.klees.data.Hangarbelegung;
import de.klees.data.Kostenstellen;
import de.klees.data.Lagerservicehangar;
import de.klees.data.Lagerservicehangarumsatz;
import de.klees.data.Mail;
import de.klees.data.Servicehangartermine;
import de.klees.data.Sitzkonfiguration;
import de.klees.data.UmbauSitzplatz;
import de.klees.data.Benutzer;
import de.klees.data.UserTyperatings;
import de.klees.data.Yaacarskopf;
import de.klees.data.views.ViewFBOUserObjekte;
import de.klees.data.views.ViewFlugzeugeMietKauf;
import de.klees.data.views.ViewFlugzeuglog;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Stefan Klees
 */
public class flugzeugeMietKaufBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<Flugzeuge> flugzeugListe;
  private List<ViewFlugzeugeMietKauf> modellListe;
  private List<ViewFlugzeugeMietKauf> inBetrieb;
  private List<Flugzeugefluggesellschaft> fluggesellschaftFlugzeuge;
  private List<Flugzeugsymbole> flugzeugSymbole;
  private List<Sitzkonfiguration> ListKonfiguration;
  private ViewFlugzeugeMietKauf currentFlugzeug;
  private Flugzeugemietkauf meinFlugzeug;
  private Feinabstimmung config;
  private Sitzkonfiguration selectedKonfig;
  private String sitzKonfigBezeichnung;

  private Fluggesellschaft FlugGesellschaft;
  private List<ViewFlugzeugeMietKauf> mietKaufFlugzeuge;

  private boolean isLoadedFlugzeugliste;
  private boolean isLoadedUserFlugzeuge;
  private boolean isLoadedFluggesellschaftFlugzeuge;
  private boolean isBenutzerBesitzer;
  private boolean isBenutzerEigentuemer;
  private boolean isBenutzerMieter;
  private boolean isZuVerkaufen;

  private Map<String, String> TreibstoffArt;
  private Map<String, String> AntriebsArt;
  private Map<String, String> TriebwerksType;

  private String SucheBezeichnung;
  private int fgID; // Fluggesellschaft ID
  private String frmFlugzeugArt;

  private String frmflugzeugTyp;
  private boolean frmCCheckDurchfuehren;
  private double frmCCheckWert;
  private boolean frmCCheckAktiv;
  private int frmCCheckZeit;

  //********* Variablen fuer Aenderungen durch Besitzer
  private String frmRegistrierung;
  private String frmHeimatICAO;
  private double frmVerkaufsPreis;
  private double frmMietPreisNass;
  private double frmMietPreisTrocken;
  private boolean frmMietbar;
  private boolean frmKaufbar;
  private int frmMaxMietZeit;
  private String frmBankKonto;
  private int frmPfand;
  private int frmKostenStelle;
  private boolean frmNurFGAuftraege;
  private String frmEigenesBild;
  private String frmLizenz;
  private int frmNeuerEigentuemer;
  private double frmBreitenGrad;
  private double frmLaengenGrad;

  //********* Variablen fuer Suche
  private String sucheKennung;
  private int sucheID;
  private int sucheUberKlasse;
  private int sucheBesitzer;

  //********* Variablen fuer MOD Tools
  private int dobFlugzeugID;
  private String dobNeuerFlugzeugStandort;
  private int dobTankfuellung;
  private int dobAirframe;
  private int dobUserID;

  //********* Variablen fuer Aenderungen durch Fluggesellschaft
  private String fgGemieteVon;
  private String fgGemietetSeit;
  private final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
  private final DecimalFormat dfkomma = new DecimalFormat("#,##0.00");
  private final DecimalFormat dfnormal = new DecimalFormat("#,##0");

  //********* Variablen fuer Flugzeugtransfer
  private String trsZielICAO;
  private String trsICAOName;
  private String trsBankkonto;
  private String trsKontoName;
  private int trsEntfernung;
  private double trsBetrag;
  private Date trsDatumEnde;
  private boolean trsIstKalkuliert;

  private boolean verleast;

  private boolean btnMietbar;
  private boolean btnKaufbar;

  private String StandortICAO;

  //******* Varialblen fuer Berechtigung Manager
  private boolean allowFlugzeuge;
  private boolean allowFlugzeugeBearbeiten;
  private boolean allowFlugzeugeKaufen;
  private boolean allowFlugzeugeTransfer;
  private boolean allowFlugzeugeVerkaufen;

  private int UserID;
  private int FluggesellschaftID;
  private int ManagerID;

  private double verkaufsPreis;
  private double CCheckFaktor;
  private double werkstattSumme;
  private int werkstattZeit;
  private double werkstattRepProzent;

  // Variablen privaterService Hangar 
  private int PSHangarID;
  private int PSHangarPaketID;
  private String PSHangarPaketArt;
  private int PSLagerItemID;

  private final double AnkaufWertverlust = 0.9;

  private String assignment_icao;
  private String AirlineName;

  private int BlackListUserID;
  private Flugzeugblacklist BlackListenEintrag;

  private UploadedFile GrafikFile;

  public flugzeugeMietKaufBean() {
    FluggesellschaftID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftID");
    UserID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
    ManagerID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ManagerID");

    isLoadedFlugzeugliste = true;
    SucheBezeichnung = "%";
    frmflugzeugTyp = "";
    StandortICAO = "";
    frmLizenz = "";
    frmFlugzeugArt = "";

    sucheBesitzer = -1;
    sucheID = -1;
    sucheKennung = "";

    isBenutzerMieter = false;

    TreibstoffArt = new HashMap<>();
    TreibstoffArt.put("AVGAS 100LL", "1");
    TreibstoffArt.put("JETA", "2");

    AntriebsArt = new HashMap<>();
    AntriebsArt.put("Kolbenmotor", "1");
    AntriebsArt.put("Turboprop", "2");
    AntriebsArt.put("Strahltriebwerk", "3");

    btnKaufbar = true;
    btnMietbar = true;

    frmCCheckAktiv = false;

    werkstattRepProzent = 0.0;
    AirlineName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftName");

    trsKontoName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName");
    trsBankkonto = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("BankKonto");

  }

  @EJB
  FlugzeugFacade facadeFlugzeug;

  public List<ViewFlugzeugeMietKauf> getFlugzeugeMietKauf() {
    if (!isLoadedFlugzeugliste) {
      config = facadeFlugzeug.readConfig();
      mietKaufFlugzeuge = facadeFlugzeug.findAllMietKaufView("%" + frmflugzeugTyp + "%", StandortICAO + "%", "%" + frmLizenz + "%");
      isLoadedFlugzeugliste = true;
    }
    return mietKaufFlugzeuge;
  }

  public List<ViewFlugzeugeMietKauf> getFlugzeugeMietKaufFluggesellschaft() {
    if (!isLoadedFlugzeugliste) {
      config = facadeFlugzeug.readConfig();
      mietKaufFlugzeuge = facadeFlugzeug.findAllMietKaufViewFluggesellschaft("%" + frmflugzeugTyp + "%", FluggesellschaftID, false, false);
      isLoadedFlugzeugliste = true;
    }
    return mietKaufFlugzeuge;
  }

  public List<ViewFlugzeuglog> getFlugzeuglog() {
    if (currentFlugzeug != null) {
      return facadeFlugzeug.getFlugzeugLog(currentFlugzeug.getIdMietKauf());
    }
    return null;
  }

  public List<ViewFlugzeugeMietKauf> getFlugzeugeMietKaufAmFlughafen() {
    if (!isLoadedFlugzeugliste) {
      config = facadeFlugzeug.readConfig();
      assignment_icao = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Assignment_ICAO");
      mietKaufFlugzeuge = facadeFlugzeug.findAlleMietKaufAmFlughafen(assignment_icao);
      isLoadedFlugzeugliste = true;
    }
    return mietKaufFlugzeuge;
  }

  /*
  ************** Blacklist BEGINN
   */
  public List<Flugzeugblacklist> getFlugzeugBlackList() {

    if (currentFlugzeug != null) {
      return facadeFlugzeug.getBlackList(currentFlugzeug.getIdMietKauf());
    }
    return null;
  }

  public void onBlackListErgaenzen() {
    if (currentFlugzeug != null) {

      if (!facadeFlugzeug.ifUserInBlacklist(BlackListUserID, currentFlugzeug.getIdMietKauf())) {
        Flugzeugblacklist newItem = new Flugzeugblacklist();
        newItem.setFlugzeugmietkaufid(currentFlugzeug.getIdMietKauf());
        newItem.setUserid(BlackListUserID);
        newItem.setUsername(getUserName(BlackListUserID));
        facadeFlugzeug.createBlacklistEintrag(newItem);
        NewMessage("Pilot wurde der Blacklist hinzugefügt");
      } else {
        NewMessage("Pilot ist schon auf der Blacklist");
      }

    }

  }

  public void onBlackListEntfernen() {
    facadeFlugzeug.removeBlacklistEintrag(BlackListenEintrag);
    NewMessage("Pilot wurde von der Blacklist gelöscht");
  }

  public List<Benutzer> getListUser() {
    return facadeFlugzeug.getUserList();
  }

  /*
  ************** Blacklist ENDE
   */
  public void resetFlugzeugeAmflughafen() {
    isLoadedFlugzeugliste = false;
  }

  public void onFlugzeugeMietKaufInDerLuft() {
    mietKaufFlugzeuge = facadeFlugzeug.findAllMietKaufInDerLuft();
  }

  public void onChangeModel() {
    isLoadedFlugzeugliste = false;
  }

  public void onChangeFluggesellschaft() {
    config = facadeFlugzeug.readConfig();
    mietKaufFlugzeuge = facadeFlugzeug.findAllMietKaufViewFluggesellschaft("%" + frmflugzeugTyp + "%", FluggesellschaftID, false, false);
    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FluggesellschaftID", fgID);
  }

  public void onFlugZeugeInBetrieb() {
    isLoadedFlugzeugliste = false;
  }

  public void onZeigeFlugzeugeDerArt() {

    setSelectedFlugzeug(null);

    if (!frmFlugzeugArt.equals("")) {
      mietKaufFlugzeuge = facadeFlugzeug.findAlleMietKaufDerArt(frmFlugzeugArt, frmflugzeugTyp);
      isLoadedFlugzeugliste = true;
    } else {
      mietKaufFlugzeuge = null;
      isLoadedFlugzeugliste = true;
    }

    //System.out.println("de.klees.beans.flugzeuge.flugzeugeMietKaufBean.onZeigeFlugzeugeDerArt() " + frmFlugzeugArt);
  }

  public List<ViewFlugzeugeMietKauf> getInBetrieb() {
    if (!isLoadedFlugzeugliste) {
      try {
        inBetrieb = facadeFlugzeug.findInProduktion((int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("inBetriebID"));
        isLoadedFlugzeugliste = true;
      } catch (NullPointerException e) {
        //System.out.println("Nullpointer FlugzeugeMietKauf - public List<ViewFlugzeugeMietKauf> getInBetrieb()");
      }

    }

    return inBetrieb;
  }

  public List<String> getFlugzeugTypen() {
    return facadeFlugzeug.findFlugzeugModelleForUserHangar(frmFlugzeugArt);
  }

  public List<String> getFlugzeugTypenFG() {
    return facadeFlugzeug.findFlugzeugModelleForFG(fgID);
  }

  public List<String> getMeineFluggesellschaftenFuerManager() {
    int fgid = facadeFlugzeug.readUser(UserID).getFluggesellschaftManagerID();
    Fluggesellschaftmanager mg = null;

    if (fgid > 0) {
      mg = facadeFlugzeug.readManager(UserID, fgid);
    }

    try {
      if (mg != null) {
        if (mg.getAllowFlugzeugeTransfer()) {
          return facadeFlugzeug.MeineFluggesellschaften(UserID, fgid);
        }
      }
      return facadeFlugzeug.MeineFluggesellschaften(UserID, -1);
    } catch (NullPointerException e) {
      return null;
    }

  }

  public List<Fluggesellschaft> getKontenFluggesellschaft() {

    int fgid = facadeFlugzeug.readUser(UserID).getFluggesellschaftManagerID();
    Fluggesellschaftmanager mg = null;

    if (fgid > 0) {
      mg = facadeFlugzeug.readManager(UserID, fgid);
    }

    try {
      if (mg != null) {
        if (mg.getAllowFlugzeugeKaufen()) {
          return facadeFlugzeug.getFGKonten(UserID, fgid);
        }
      }
      return facadeFlugzeug.getFGKonten(UserID, -1);
    } catch (NullPointerException e) {
      return null;
    }

  }

  public List<Kostenstellen> getKostenstellen() {
    int BesitzerID = -1;

    if (currentFlugzeug != null) {
      BesitzerID = currentFlugzeug.getIdflugzeugBesitzer();
    }

    // Kostenstellen vom Benutzer und dem Besitzer auswählen
    return facadeFlugzeug.getKostenstellen(UserID, BesitzerID);
  }

  public List<Sitzkonfiguration> getSitzKonfiguration() {

    try {
      if (currentFlugzeug.getIdFlugzeug() != null) {
        ListKonfiguration = facadeFlugzeug.getSitzKonfiguration(currentFlugzeug.getIdFlugzeug());
      }
    } catch (NullPointerException e) {

    }

    return ListKonfiguration;
  }

  /*
  Detail Suche BEGINN
   */
  public List<String> getUser() {
    return facadeFlugzeug.getUser();
  }

  public void onSucheUeberKennung() {
    mietKaufFlugzeuge = facadeFlugzeug.getFlugzeugeUeberKennung(sucheKennung);

  }

  public void onSucheUeberUserID() {
    mietKaufFlugzeuge = facadeFlugzeug.getFlugzeugeUeberUserID(sucheBesitzer);

  }

  public void onSucheUeberMietkaufID() {
    mietKaufFlugzeuge = facadeFlugzeug.getFlugzeugeUeberID(sucheID);
  }

  public void onSucheUeberKlasse() {
    mietKaufFlugzeuge = facadeFlugzeug.getFlugzeugeUeberKlasse(sucheUberKlasse);
  }

  /*
  Detail Suche ENDE
   */
  public void onSelectBoxChangeListener() {

  }

  public void onKaufen() {

    String Text = "";
    boolean Kauf = false;

    String BesitzerKonto = "";
    String BesitzerName = "";

    double Banksaldo = 0.0;
    try {
      Banksaldo = facadeFlugzeug.getErweiterterBankSaldo(UserID);
    } catch (NullPointerException e) {
      Banksaldo = 0.0;
    }

    if (Banksaldo >= getSelectedFlugzeug().getVerkaufsPreis()) {
      Kauf = true;
    } else {
      Text = "Hangar_msg_duHastNichtGenugGeldUmDiesesFlugzeugZuKaufen";
      Kauf = false;
    }

    if (Kauf) {
      if (!getSelectedFlugzeug().getIstZuVerkaufen()) {
        Kauf = false;
        Text = "Hangar_msg_flugzeugIstNichtZuKaufen";
      } else {
        Kauf = true;
      }
    }

    // Prüfen auf in der Luft oder gesperrt
    if (Kauf) {
      if (currentFlugzeug.getIstGesperrt()) {
        Kauf = false;
        Text = "Hangar_msg_flugzeugIstVermietetUndKannNichtGekauftWerden";
      } else {
        Kauf = true;
      }
    }

    // prüfen ob Benutzer das Flugzeug schon besitzt
    if (Kauf) {
      if (facadeFlugzeug.istBenutzerBesitzerDesFlugzeuges(currentFlugzeug.getIdMietKauf(), UserID)) {
        Kauf = false;
        Text = "Hangar_msg_duBistSchonBesitzerDiesesFlugzeugs";
      } else {
        Kauf = true;
      }
    }

    if (Kauf) {
      // Bankkonto des Besitzers pruefen
      meinFlugzeug = facadeFlugzeug.getFlugzeugMietKauf(currentFlugzeug.getIdMietKauf());
      // Besitzerdaten auslesen
      BesitzerKonto = meinFlugzeug.getBankkontoBesitzer();
      BesitzerName = facadeFlugzeug.getBankKontoName(BesitzerKonto);

      if (BesitzerName.equals("")) {
        Text = "Hangar_msg_KontoPruefungFehlgeschlagen";
        Kauf = false;
      }

    }

    if (Kauf) {

      int BesitzerID = meinFlugzeug.getIdflugzeugBesitzer();

      // wenn Bank der Verkäufer ist, dann Zustand auf 100% und frischen C-Check
      if (BesitzerID == -300) {
        meinFlugzeug.setZustand(100.0);
        meinFlugzeug.setZustandDesFlugzeugs(100);
        // C-Check + 6 Monate
        Date ccheckDatum = java.sql.Date.valueOf(LocalDate.now().plusMonths(6));
        meinFlugzeug.setNaechsterTerminCcheck(ccheckDatum);
      }

      // Manager kauf Flugzeug
      if (fgID > 0) {
        BesitzerID = facadeFlugzeug.readFG(fgID).getUserid();
      } else {
        BesitzerID = UserID;
      }

      // Neu Zuweisung des Besitzwechsels
      meinFlugzeug.setKostenstelle(frmKostenStelle);
      meinFlugzeug.setIdflugzeugBesitzer(BesitzerID);
      meinFlugzeug.setIdFluggesellschaft(fgID);
      meinFlugzeug.setBankkontoBesitzer(trsBankkonto);
      meinFlugzeug.setIstMietbar(false);
      meinFlugzeug.setIstZuVerkaufen(false);
      meinFlugzeug.setNurFuerAuftraegeDerFluggesellschaft(false);

      // Pilotenzuweisungen löschen
      facadeFlugzeug.onDeletePilotenZuweisungen(currentFlugzeug.getIdMietKauf());

      // Daten Speichern
      facadeFlugzeug.editFlugzeugMietKauf(meinFlugzeug);

      double betrag = currentFlugzeug.getVerkaufsPreis() - (currentFlugzeug.getVerkaufsPreis() * 2);

      String An = facadeFlugzeug.readUser(meinFlugzeug.getIdflugzeugBesitzer()).getName1();
      String Von = "**** FTW Aircraft Stock ****";
      String Betreff = "Flugzeugkauf: " + currentFlugzeug.getRegistrierung() + " " + currentFlugzeug.getType();
      String MailText = "Flugzeug ID: " + currentFlugzeug.getIdMietKauf() + "<br>" + currentFlugzeug.getType() + "<br>Betrag: " + dfkomma.format(currentFlugzeug.getVerkaufsPreis());

      saveMail(Von, An, Betreff, MailText);

      // Auftraggeber Bankdaten
      String BankKonto = trsBankkonto;
      String KontoName = facadeFlugzeug.getBankKontoName(trsBankkonto);

      // ************** Abbuchung beim Käufer
      SaveBankbuchung(BankKonto, KontoName, BankKonto, KontoName, new Date(), betrag,
              BesitzerKonto, BesitzerName,
              new Date(), loginMB.onSprache("Hangar_msg_bank_flugzeugGekauft") + currentFlugzeug.getType(), BesitzerID, -1, fgID, -1, -1, -1, -1, currentFlugzeug.getAktuellePositionICAO(), -1, currentFlugzeug.getIdMietKauf(), frmKostenStelle, -1);

      //*************** Zahlungseingang buchen beim Verkäufer
      SaveBankbuchung(BesitzerKonto, BesitzerName, BankKonto, KontoName, new Date(), currentFlugzeug.getVerkaufsPreis(),
              BesitzerKonto, BesitzerName,
              new Date(), loginMB.onSprache("Hangar_msg_bank_flugzeugVerkauft") + currentFlugzeug.getType(), BesitzerID, -1, -1, -1, -1, -1, -1, "", -1, currentFlugzeug.getIdMietKauf(), -1, -1);

      NewMessage(loginMB.onSprache("Hangar_msg_bankabbuchungWarErfolgreich"));
      Text = "Hangar_msg_bank_duHastErfolgreichDiesesFlugzeugGekauft";

    }

    onZeigeGekaufte();

    if (!Text.equals("")) {
      NewMessage(loginMB.onSprache(Text));
    }

  }

  public void onVerKaufen() {

    //**************** Verkauf an den FTW-Aircraft Stock
    if (!currentFlugzeug.getIstGesperrt()) {
      meinFlugzeug = facadeFlugzeug.getFlugzeugMietKauf(currentFlugzeug.getIdMietKauf());

      // Besitzerdaten auslesen
      String BesitzerKonto = meinFlugzeug.getBankkontoBesitzer();
      String BesitzerName = facadeFlugzeug.getBankKontoName(BesitzerKonto);

      String An = facadeFlugzeug.readUser(meinFlugzeug.getIdflugzeugBesitzer()).getName1();
      String Von = "**** FTW Aircraft Stock ****";
      String Betreff = "Flugzeugverkauf an Aircraft Stock: " + currentFlugzeug.getRegistrierung() + " " + currentFlugzeug.getType();
      String MailText = "Flugzeug ID: " + currentFlugzeug.getIdMietKauf() + "<br>" + currentFlugzeug.getType() + "<br> wurde an den Aircraft-Stock verkauft" + "<br>Betrag: " + dfkomma.format(verkaufsPreis);

      saveMail(Von, An, Betreff, MailText);

      //*************** Zahlungseingang buchen beim Verkäufer
      SaveBankbuchung(BesitzerKonto, BesitzerName, "500-1000002", "*** FTW Aircraft Stock ****", new Date(), verkaufsPreis,
              BesitzerKonto, BesitzerName, new Date(), loginMB.onSprache("Hangar_msg_bank_flugzeugVerkauftAnBank") + " " + currentFlugzeug.getType(),
              UserID, -1, -1, -1, -1, -1, -1, "", -1, currentFlugzeug.getIdMietKauf(), -1, -1);

      // Neu Zuweisung des Besitzwechsels
      meinFlugzeug.setKostenstelle(-1);
      meinFlugzeug.setIdflugzeugBesitzer(-300);
      meinFlugzeug.setIdFluggesellschaft(-1);
      meinFlugzeug.setBankkontoBesitzer("500-1000002");
      meinFlugzeug.setIstGesperrt(false);
      meinFlugzeug.setIstInDerLuft(false);
      meinFlugzeug.setIstMietbar(false);
      meinFlugzeug.setMaxMietzeitMinuten(1440);
      meinFlugzeug.setPfand(0);
      meinFlugzeug.setIstZuVerkaufen(true);
      meinFlugzeug.setIstPrivatverkauf(false);
      meinFlugzeug.setIdSitzKonfiguration(-1);
      meinFlugzeug.setEigenesBildURL("");
      meinFlugzeug.setZustand(100.0);
      meinFlugzeug.setLetzterCheckMinuten(0);
      meinFlugzeug.setIstCheckDurchUserErlaubt(false);
      meinFlugzeug.setLeasingAnUserID(-1);
      meinFlugzeug.setLeasinggeberUserID(-1);
      meinFlugzeug.setNurFuerAuftraegeDerFluggesellschaft(false);

      // Alte Registrierung löschen und durch neue ersetzen BEGINN
      int kmax = 9999;
      int kennnr;
      String Kennung;
      DecimalFormat nummer = new DecimalFormat("####0000");

      do {
        kennnr = CONF.zufallszahl(1, kmax);
        Kennung = "FTW-" + nummer.format(kennnr);

      } while (facadeFlugzeug.ifExistKennung(Kennung));

      meinFlugzeug.setRegistrierung(Kennung);
      // Alte Registrierung löschen und durch neue ersetzen ENDE

      Flugzeuge stammfg = facadeFlugzeug.findFlugzeugbyID(currentFlugzeug.getIdFlugzeug());

      meinFlugzeug.setMietpreisNass(stammfg.getStandardMietpreis());
      meinFlugzeug.setMietpreisTrocken(stammfg.getStandardMietpreis());

      verkaufsPreis = verkaufsPreis * (1.0 + (1 - AnkaufWertverlust));

      meinFlugzeug.setVerkaufsPreis(onFlugzeugWert(currentFlugzeug));

      // C-Check, von Ankaufsdatum + 6 Monate
      Date ccheckDatum = java.sql.Date.valueOf(LocalDate.now().plusMonths(6));
      meinFlugzeug.setNaechsterTerminCcheck(ccheckDatum);

      // Pilotenzuweisungen löschen
      facadeFlugzeug.onDeletePilotenZuweisungen(currentFlugzeug.getIdMietKauf());

      // Blacklisteintragungen löschen
      facadeFlugzeug.blackListAlleEintraegeEntfernen(currentFlugzeug.getIdMietKauf());

      // Daten Speichern
      facadeFlugzeug.editFlugzeugMietKauf(meinFlugzeug);

      NewMessage(loginMB.onSprache("Hangar_msg_bankabbuchungWarErfolgreich"));

    } else {
      NewMessage(loginMB.onSprache("Hangar_msg_FlugzeugKannNichtVerkauftWerdenFlugzeugIstGeperrt"));
    }

  }

  private double onFlugzeugWert(ViewFlugzeugeMietKauf flugzeug) {

    //Parameter fuer erweiterte Berechnungen laden
    config = facadeFlugzeug.readConfig();
    Flugzeuge stammfg = facadeFlugzeug.readFlugzeug(flugzeug.getIdFlugzeug());

    //********* Flugzeug aufrufen
    // um die Maximale Nutzungsdauer zu Berechnen wird die gewöhnliche Nutzungsdauer mit 2 mulitpliziert
    int KolbenmotorGewoehnlichNutzungsdauer = 12000;
    int TurboPropGewoehnlichNutzungsdauer = 16000;
    int TurbineGewoehnlichNutzungsdauer = 20000;
    int AktuellesJahr = Calendar.getInstance().get(Calendar.YEAR);

    double ZellenStunden = flugzeug.getAirframe();
    double Alter = AktuellesJahr - flugzeug.getBaujahr();
    double MaximaleNutzungsdauer = 0;
    double Wert = stammfg.getVerkaufspreis();
    double Zustand = 100.00;

    double MietFaktor = 0.0005;

    // Enfernt durch Toffi (Peter) am 01.02.2018, neues Mietpreismodell
    // double Mietpreis = Wert * MietFaktor;
    if (null != flugzeug.getAntriebsart()) {
      switch (flugzeug.getAntriebsart()) {
        case 1:
          MaximaleNutzungsdauer = KolbenmotorGewoehnlichNutzungsdauer * 2;
          break;
        case 2:
          MaximaleNutzungsdauer = TurboPropGewoehnlichNutzungsdauer * 2;
          break;
        case 3:
          MaximaleNutzungsdauer = TurbineGewoehnlichNutzungsdauer * 2;
          break;
        default:
          MaximaleNutzungsdauer = 0;
          break;
      }

    }

    ZellenStunden = (ZellenStunden / config.getFaktorFuerZellenstunden());

    // Kalkulierter Marktpreis(mit Zellenstunden) =  FTW-Neupreis - ((Zellenstunden/ Maximale Nutzungsdauer )  * Kalkulierter Neupreis)
    double KalkulierterMarktPreisMitZellenstunden = Wert - ((ZellenStunden / MaximaleNutzungsdauer) * Wert);

    // Marktpreis mit Alter = Kalkulierter Marktpreis mit Zellenstunden - (Kalkulierter Marktpreis mit Zellenstunden*(Alter/100))
    double MarktpreisMitAlter = KalkulierterMarktPreisMitZellenstunden - (KalkulierterMarktPreisMitZellenstunden * (Alter / 100.));

    // Verkaufspreis = Zustand  / 100 * Kalkulierter Marktpreis mit Alter
    double Verkaufspreis = (Zustand / 100.) * MarktpreisMitAlter;

    return Verkaufspreis;
  }

  public void onMieten() {
    boolean Miete = true;
    String Text = "";

    if (!facadeFlugzeug.hatBenutzerEinFlugzeugGemietet(UserID)) {

      // ist ein Flugzeug ausgewählt
      if (currentFlugzeug != null) {
        Miete = true;
      } else {
        Miete = false;
        Text = "Hangar_msg_KeinFlugzeugAusgewaehlt";
      }

      Miete = LizenzPruefen();

      if (!Miete) {
        NewMessage(loginMB.onSprache("Hangar_msg_LizenzpruefungWarNichtErfolgreich"));
      }

      if (Miete) {
        if (facadeFlugzeug.ifUserInBlacklist(UserID, currentFlugzeug.getIdMietKauf())) {
          Miete = false;
          NewMessage("Du stehst bei diesem Flugzeug auf der Blacklist, Flugzeug kann nicht gemietet werden.");
        }

      }

      if (Miete) {
        // Wenn flugzeug gesperrt ist geht gar nichts mehr
        if (getSelectedFlugzeug().getIstGesperrt()) {
          Miete = false;
          Text = "Hangar_msg_flugzeugIstBereitsVermietet";
        } else {
          Miete = true;
        }
      }

      if (Miete) {
        if (!getSelectedFlugzeug().getIstMietbar()) {
          Miete = false;
          Text = "Hangar_msg_dieVermietungDesFlugzeugsIstVomVermieterNichtGewuenscht";
          // 22.04.2019 Eigentuemer hinzugefuegt, darf sowieso Mieten
          if (isBenutzerBesitzer) {
            Miete = true;
          }

          //
          // gehört Flugzeug einer Fluggesellschaft
          if (currentFlugzeug.getFluggesellschaftID() != null) {
            if (currentFlugzeug.getFluggesellschaftID() > 0) {
              //s
              // Piloten der Fluggesellschaft können trotzdem mieten  
              if (facadeFlugzeug.istMieterInDerFluggesellschaft(currentFlugzeug.getFluggesellschaftID(), UserID)) {
                Miete = true;
              }
            }
          }

          //
        }
      }

      //Prüfen ob für das Flugzeuge eine ErlaubtListe vorhanden ist
      //und Benutzer mit der Liste abgleichen
      if (Miete) {
        if (facadeFlugzeug.ifExistErlaubtListe(currentFlugzeug.getIdMietKauf())) {
          if (facadeFlugzeug.ifUserInErlaubtListe(UserID, currentFlugzeug.getIdMietKauf())) {
            Miete = true;
          } else {
            Miete = false;
            Text = "Hangar_msg_dieVermietungDesFlugzeugsIstVomVermieterNichtGewuenscht";
            // 22.04.2019 Eigentuemer hinzugefuegt, darf sowieso Mieten
            if (isBenutzerBesitzer) {
              Miete = true;
            }
          }
        } else {
          Miete = true;
        }
      }

      //Wenn mieter der Besitzer des Flugzeuges ist, dann sowieso Mietbar
      if (Miete) {
        if (isBenutzerBesitzer) {
          Miete = true;

          double Banksaldo = 0.0;
          try {
            Banksaldo = facadeFlugzeug.getErweiterterBankSaldo(UserID);
          } catch (NullPointerException e) {
            Banksaldo = 0.0;
          }

          if (Banksaldo <= -150000.00) {
            Miete = false;
            // ************************ Zwangsversteigerung
            getVerkaufsPreis();
            onVerKaufen();
          }

        }
      }

      if (Miete) {
        // Hole Flugzeug
        meinFlugzeug = facadeFlugzeug.getFlugzeugMietKauf(currentFlugzeug.getIdMietKauf());

        //Mietzeit ausrechnen und eintragen
        long aktZeit = new Date().getTime();
        long endeZeit = aktZeit + ((long) meinFlugzeug.getMaxMietzeitMinuten() * 60 * 1000);
        meinFlugzeug.setIstGesperrtBis(new Date(endeZeit));
        meinFlugzeug.setIstGesperrtSeit(new Date(aktZeit));
        meinFlugzeug.setIstGesperrt(true);
        meinFlugzeug.setIstInDerLuft(false);
        meinFlugzeug.setIdUserDerFlugzeugGesperrtHat(UserID);
        meinFlugzeug.setOldMiles(currentFlugzeug.getGesamtEntfernung());

        facadeFlugzeug.onFlugzeugMieteSpeichern(meinFlugzeug);

//        int Pfand = currentFlugzeug.getPfand();
//
//        if (Pfand > 0) {
//
//          // Pfand belasten
//          //Pfand beim Benutzer Belasten
//          String UserName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName");
//          String UserKonto = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("BankKonto");
//
//          String AuftraggeberKonto = currentFlugzeug.getBankkontoBesitzer();
//          String AuftraggeberName = getKontoName(AuftraggeberKonto);
//
//          String VerwendungsZweck = "Pfand für : " + currentFlugzeug.getType();
//
//          //Pfand den Flugzeugbesitzer gutschreiben
//          SaveBankbuchung(AuftraggeberKonto, AuftraggeberName, UserKonto, UserName, new Date(), Pfand, AuftraggeberKonto, AuftraggeberName,
//                  new Date(), VerwendungsZweck, currentFlugzeug.getIdflugzeugBesitzer(), -1, -1, -1, -1, -1, -1,
//                  currentFlugzeug.getAktuellePositionICAO(), -1, currentFlugzeug.getIdMietKauf(), currentFlugzeug.getKostenstelle(), -1);
//
//          //Pfand beim Benutzer belasten
//          SaveBankbuchung(UserKonto, UserName, AuftraggeberName, UserKonto, new Date(), (Pfand - (Pfand * 2)), UserKonto, UserName,
//                  new Date(), VerwendungsZweck, UserID, -1, -1, -1, -1, -1, -1, currentFlugzeug.getAktuellePositionICAO(), -1, -1, 0, -1);
//        }
        Text = "Hangar_msg_flugzeugErfolgreichGemietet";
      }

    } else {
      NewMessage(loginMB.onSprache("Hangar_msg_DuHastSchonEinFlugzeugGemietetDeinFlugzeugWirdInDerTabelleAngezeigt"));
      Text = "";
    }

    if (!Text.equals("")) {
      NewMessage(loginMB.onSprache(Text));
    }

    onZeigeGemietete();
  }

  public void onRueckgabe() {
    boolean Rueck = false;
    String Text = "Hangar_msg_duHastDiesesFlugzeugNichtGemietet";

    if (currentFlugzeug != null) {
      if (currentFlugzeug.getIstGesperrt()) {
        Rueck = true;
      } else {
        Rueck = false;
      }
    } else {
      Rueck = false;
    }

    if (Rueck) {
      if (currentFlugzeug.getIstInDerLuft()) {
        Text = "Hangar_msg_FlugzeugIstInDerLuft";
        Rueck = false;
      }
    }

    if (Rueck) {
      if (currentFlugzeug.getIdUserDerFlugzeugGesperrtHat().equals(UserID)) {
        Rueck = true;
      } else {
        Rueck = false;
        Text = "Hangar_msg_duHastDiesesFlugzeugNichtGemietet";
      }
    }

    if (Rueck) {
      meinFlugzeug = facadeFlugzeug.getFlugzeugMietKauf(currentFlugzeug.getIdMietKauf());
      int Pfand = meinFlugzeug.getPfand();
      meinFlugzeug.setIstGesperrt(false);
      meinFlugzeug.setIstGesperrtBis(null);
      meinFlugzeug.setIstGesperrtSeit(null);
      meinFlugzeug.setIstInDerLuft(false);
      meinFlugzeug.setIdUserDerFlugzeugGesperrtHat(-1);

      // Pfand berechnen
//      if (Pfand > 0) {
//        if ((meinFlugzeug.getAktuellePositionICAO().equals(currentFlugzeug.getHeimatICAO())) || (currentFlugzeug.getOldMiles().equals(currentFlugzeug.getGesamtEntfernung()))) {
//          //Pfand dem Benutzer zurückerstatten
//          String UserName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName");
//          String UserKonto = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("BankKonto");
//
//          String AuftraggeberKonto = currentFlugzeug.getBankkontoBesitzer();
//          String AuftraggeberName = getKontoName(AuftraggeberKonto);
//
//          String VerwendungsZweck = "Pfand für : " + currentFlugzeug.getType() + " : " + currentFlugzeug.getRegistrierung() + " ICAO : " + currentFlugzeug.getAktuellePositionICAO();
//
//          SaveBankbuchung(UserKonto, UserName, AuftraggeberKonto, AuftraggeberName, new Date(), Pfand, UserKonto, UserName,
//                  new Date(), VerwendungsZweck, UserID, -1, -1, -1, -1, -1, -1, currentFlugzeug.getAktuellePositionICAO(), -1, -1, -1, -1);
//
//          //Pfand beim Flugzeugbesitzer abziehen
//          SaveBankbuchung(AuftraggeberKonto, AuftraggeberName, AuftraggeberKonto, AuftraggeberName, new Date(), Pfand - (Pfand * 2), UserKonto, UserName,
//                  new Date(), VerwendungsZweck, currentFlugzeug.getIdflugzeugBesitzer(), -1, -1, -1, -1, -1, -1, currentFlugzeug.getAktuellePositionICAO(), -1,
//                  currentFlugzeug.getIdMietKauf(), currentFlugzeug.getKostenstelle(), -1);
//
//        }
//      }
      meinFlugzeug.setOldMiles(currentFlugzeug.getGesamtEntfernung());
      facadeFlugzeug.editFlugzeugMietKauf(meinFlugzeug);

      Text = "Hangar_msg_flugzeugMieteBeendet";
      setSelectedFlugzeug(null);

      isBenutzerBesitzer = false;
      isBenutzerMieter = false;
    }

    NewMessage(loginMB.onSprache(Text));
    isLoadedFlugzeugliste = false;

    onZeigeGekaufte();

  }

  public void onSucheFlugzeug() {
    isBenutzerBesitzer = false;
    isLoadedFlugzeugliste = false;
    setSelectedFlugzeug(null);
  }

  public void onAlleFlugzeugeZeigen() {
    isLoadedFlugzeugliste = false;
    setSelectedFlugzeug(null);
  }

  public void onZeigeNurMietbareFlugzeuge() {
    mietKaufFlugzeuge = facadeFlugzeug.findMietbareFlugzeugeNachType("%" + frmflugzeugTyp + "%");
  }

  public void onZeigeGemietete() {
    mietKaufFlugzeuge = facadeFlugzeug.findeMeinGemietetesFlugzeug(UserID);

    isBenutzerBesitzer = false;
    isBenutzerMieter = false;

    setSelectedFlugzeug(null);

    isLoadedFlugzeugliste = true;
  }

  public void onZeigeGekaufte() {
    isBenutzerBesitzer = false;
    setSelectedFlugzeug(null);
    isLoadedFlugzeugliste = true;
    mietKaufFlugzeuge = facadeFlugzeug.findeMeineGekaufteFlugzeuge(UserID);
  }

  public void onEditFlugzeug() {
    if (currentFlugzeug != null) {
      meinFlugzeug = facadeFlugzeug.getFlugzeugMietKauf(currentFlugzeug.getIdMietKauf());
      setFrmRegistrierung(meinFlugzeug.getRegistrierung());
      setFrmBankKonto(meinFlugzeug.getBankkontoBesitzer());
      setFrmHeimatICAO(meinFlugzeug.getHeimatICAO());
      setFrmKaufbar(meinFlugzeug.getIstZuVerkaufen());
      setFrmMietbar(meinFlugzeug.getIstMietbar());
      setFrmNurFGAuftraege(meinFlugzeug.getNurFuerAuftraegeDerFluggesellschaft());
      setFrmMaxMietZeit(meinFlugzeug.getMaxMietzeitMinuten());
      //setFrmMietPreisNass(meinFlugzeug.getMietpreisNass());
      setFrmMietPreisTrocken(meinFlugzeug.getMietpreisTrocken());

      setFrmVerkaufsPreis(meinFlugzeug.getVerkaufsPreis());
      setFrmPfand(meinFlugzeug.getPfand());
      setFrmKostenStelle(meinFlugzeug.getKostenstelle());
      setFrmEigenesBild(meinFlugzeug.getEigenesBildURL());

      setFrmBreitenGrad(meinFlugzeug.getPositiionBreitenGrad());
      setFrmLaengenGrad(meinFlugzeug.getPositionLaengenGrad());

      //****** Fluggesellschaft Daten
      if (meinFlugzeug.getIdUserDerFlugzeugGesperrtHat() > 0) {
        setFgGemieteVon(facadeFlugzeug.getBenutzerNameMieter(UserID));
        setFgGemietetSeit(df.format(meinFlugzeug.getIstGesperrtSeit()));
      } else {
        setFgGemieteVon("N/A");
        setFgGemietetSeit(null);
      }
    }
  }

  public void onSaveFlugzeug() {
    meinFlugzeug.setBankkontoBesitzer(frmBankKonto);
    meinFlugzeug.setIdFluggesellschaft(fgID);
    meinFlugzeug.setHeimatICAO(frmHeimatICAO.toUpperCase());
    meinFlugzeug.setIstZuVerkaufen(frmKaufbar);
    meinFlugzeug.setIstMietbar(frmMietbar);
    meinFlugzeug.setNurFuerAuftraegeDerFluggesellschaft(frmNurFGAuftraege);

    if (frmMaxMietZeit > 43200) {
      frmMaxMietZeit = 43200;
    }

    meinFlugzeug.setMaxMietzeitMinuten(frmMaxMietZeit);
    //meinFlugzeug.setMietpreisNass(frmMietPreisNass);

    if (frmMietPreisTrocken > 250000.0) {
      frmMietPreisTrocken = 250000.0;
    }

    meinFlugzeug.setMietpreisTrocken(frmMietPreisTrocken);
    meinFlugzeug.setRegistrierung(frmRegistrierung);
    meinFlugzeug.setVerkaufsPreis(frmVerkaufsPreis);
    meinFlugzeug.setKostenstelle(frmKostenStelle);
    meinFlugzeug.setEigenesBildURL(frmEigenesBild);

    if (frmPfand < 0) {
      frmPfand = 0;
    }

    meinFlugzeug.setPfand(frmPfand);

    meinFlugzeug.setPositiionBreitenGrad(frmBreitenGrad);
    meinFlugzeug.setPositionLaengenGrad(frmLaengenGrad);

    facadeFlugzeug.editFlugzeugMietKauf(meinFlugzeug);

    NewMessage(loginMB.onSprache("Hangar_msg_flugzeugeinstellungenGespeichert"));

    isLoadedFluggesellschaftFlugzeuge = false;
    isLoadedFlugzeugliste = false;
    isLoadedUserFlugzeuge = false;

    onZeigeGekaufte();

  }

  public void onRegistrierungAendern() {
    if (!facadeFlugzeug.ifExistKennung(frmRegistrierung)) {
      if (!frmRegistrierung.equals("")) {
        meinFlugzeug.setRegistrierung(frmRegistrierung);
        facadeFlugzeug.editFlugzeugMietKauf(meinFlugzeug);
        NewMessage(loginMB.onSprache("Hangar_msg_RegistrierungWurdeGeaendert"));
        onZeigeGekaufte();
      }
    } else {
      NewMessage(loginMB.onSprache("Hangar_msg_RegistrierungWurdeNichtGeaendert"));
    }
  }

  public void onUmbau() {

    if (currentFlugzeug != null) {

      if (!currentFlugzeug.getIstInDerLuft()) {

        if (!facadeFlugzeug.ifUmbauLaeuft(currentFlugzeug.getIdMietKauf())) {

          meinFlugzeug = facadeFlugzeug.getFlugzeugMietKauf(currentFlugzeug.getIdMietKauf());
          meinFlugzeug.setIdSitzKonfiguration(selectedKonfig.getIdsitzKonfiguration());
          facadeFlugzeug.editFlugzeugMietKauf(meinFlugzeug);

          if (selectedKonfig.getUmbauzeitMinuten() > 0) {
            UmbauSitzplatz newUmbau = new UmbauSitzplatz();

            newUmbau.setBetrag(selectedKonfig.getPreis());
            newUmbau.setDauerMinuten(selectedKonfig.getUmbauzeitMinuten());
            newUmbau.setIdFlugzeugMietKauf(currentFlugzeug.getIdMietKauf());
            newUmbau.setIdSitzplatzKonfiguration(selectedKonfig.getIdsitzKonfiguration());

            long sperrzeit = new Date().getTime() + (long) (selectedKonfig.getUmbauzeitMinuten() * 60 * 1000);
            newUmbau.setSperrzeit(new Date(sperrzeit));

            meinFlugzeug.setIstGesperrtBis(new Date(sperrzeit));
            meinFlugzeug.setIstGesperrtSeit(new Date());
            meinFlugzeug.setIstGesperrt(true);
            meinFlugzeug.setIstInDerLuft(false);
            meinFlugzeug.setIdUserDerFlugzeugGesperrtHat(-300);

            facadeFlugzeug.editFlugzeugMietKauf(meinFlugzeug);

            facadeFlugzeug.createUmbau(newUmbau);
          }

          if (selectedKonfig.getPreis() > 0) {
            double Betrag = (double) selectedKonfig.getPreis();

            String AuftraggeberKonto = "500-1000002";
            String AuftraggeberName = "**** FTW Aircraft Stock ****";

            String UserName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName");
            String UserKonto = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("BankKonto");

            String VerwendungsZweck = "Umbaukosten für : " + currentFlugzeug.getType() + " : " + currentFlugzeug.getRegistrierung() + " ICAO : " + currentFlugzeug.getAktuellePositionICAO();

            SaveBankbuchung(UserKonto, UserName, AuftraggeberKonto, AuftraggeberName, new Date(), Betrag - (Betrag * 2), UserKonto, UserName,
                    new Date(), VerwendungsZweck, currentFlugzeug.getIdflugzeugBesitzer(), -1, -1, -1, -1, -1, -1, currentFlugzeug.getAktuellePositionICAO(), -1,
                    currentFlugzeug.getIdMietKauf(), 0, -1);
          }

          NewMessage(loginMB.onSprache("Hangar_msg_AuftragFuerUmbauErteilt"));

        } else {
          NewMessage(loginMB.onSprache("Hangar_msg_UmbauLaeuftBereits"));
        }

      } else {
        NewMessage(loginMB.onSprache("Hangar_msg_flugzeugeinstellungenGespeichertFehlerIstInDerLuft"));
      }
    }

  }

  public void onStandardKonfiguration() {

    if (currentFlugzeug != null) {
      if (!currentFlugzeug.getIstInDerLuft()) {

        if (!facadeFlugzeug.ifUmbauLaeuft(currentFlugzeug.getIdMietKauf())) {

          meinFlugzeug = facadeFlugzeug.getFlugzeugMietKauf(currentFlugzeug.getIdMietKauf());

          Sitzkonfiguration sitzkonfig = facadeFlugzeug.getKonfig(currentFlugzeug.getIdSitzKonfiguration());

          if (sitzkonfig != null) {
            if (sitzkonfig.getUmbauzeitMinuten() > 0) {
              UmbauSitzplatz newUmbau = new UmbauSitzplatz();

              newUmbau.setBetrag(sitzkonfig.getPreis());
              newUmbau.setDauerMinuten(sitzkonfig.getUmbauzeitMinuten());
              newUmbau.setIdFlugzeugMietKauf(currentFlugzeug.getIdMietKauf());
              newUmbau.setIdSitzplatzKonfiguration(sitzkonfig.getIdsitzKonfiguration());

              long sperrzeit = new Date().getTime() + (long) (sitzkonfig.getUmbauzeitMinuten() * 60 * 1000);
              newUmbau.setSperrzeit(new Date(sperrzeit));

              meinFlugzeug.setIstGesperrtBis(new Date(sperrzeit));
              meinFlugzeug.setIstGesperrtSeit(new Date());
              meinFlugzeug.setIstGesperrt(true);
              meinFlugzeug.setIstInDerLuft(false);
              meinFlugzeug.setIdUserDerFlugzeugGesperrtHat(-300);

              facadeFlugzeug.createUmbau(newUmbau);
            }

            if (sitzkonfig.getPreis() > 0) {
              double Betrag = (double) sitzkonfig.getPreis();

              String AuftraggeberKonto = "500-1000002";
              String AuftraggeberName = "**** FTW Aircraft Stock ****";

              String UserName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName");
              String UserKonto = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("BankKonto");

              String VerwendungsZweck = "Umbaukosten für : " + currentFlugzeug.getType() + " : " + currentFlugzeug.getRegistrierung() + " ICAO : " + currentFlugzeug.getAktuellePositionICAO();

              SaveBankbuchung(UserKonto, UserName, AuftraggeberKonto, AuftraggeberName, new Date(), Betrag - (Betrag * 2), UserKonto, UserName,
                      new Date(), VerwendungsZweck, currentFlugzeug.getIdflugzeugBesitzer(), -1, -1, -1, -1, -1, -1, currentFlugzeug.getAktuellePositionICAO(), -1,
                      currentFlugzeug.getIdMietKauf(), 0, -1);
            }
          }

          meinFlugzeug.setIdSitzKonfiguration(-1);
          facadeFlugzeug.editFlugzeugMietKauf(meinFlugzeug);

          NewMessage(loginMB.onSprache("Hangar_msg_AuftragFuerUmbauErteilt"));
          NewMessage(loginMB.onSprache("Hangar_msg_flugzeugeinstellungenGespeichert"));

        } else {
          NewMessage(loginMB.onSprache("Hangar_msg_UmbauLaeuftBereits"));
        }

      } else {
        NewMessage(loginMB.onSprache("Hangar_msg_flugzeugeinstellungenGespeichertFehlerIstInDerLuft"));
      }
    }

  }

  public void onTransferToFG() {

    boolean Transfer = true;

    // prüfen ob Benutzer das Flugzeug schon besitzt
    if (facadeFlugzeug.istBenutzerBesitzerDesFlugzeuges(currentFlugzeug.getIdMietKauf(), UserID)) {
      Transfer = true;
      if (currentFlugzeug.getIdUserDerFlugzeugGesperrtHat() < -1) {
        Transfer = false;
        NewMessage("Flugzeug durch System gesperrt, kein Transfer möglich");
      }

    } else {
      Transfer = false;
    }

    // ********* Evtl. Zwangsversteigerung
    if (Transfer) {
      double Banksaldo = 0.0;
      try {
        Banksaldo = facadeFlugzeug.getErweiterterBankSaldo(UserID);
      } catch (NullPointerException e) {
        Banksaldo = 0.0;
      }

      if (Banksaldo <= -150000) {
        getVerkaufsPreis();
        onVerKaufen();
        Transfer = false;
      }
    }

    if (Transfer) {
      if (fgID > 0) {
        Fluggesellschaft fgesellschaft;
        fgesellschaft = facadeFlugzeug.datenFluggesellschaft(fgID);
        String KontoNummer = fgesellschaft.getBankKonto();
        int FlugzeugID = currentFlugzeug.getIdMietKauf();
        Flugzeugemietkauf meinFlugzeug = facadeFlugzeug.meinFlugzeug(FlugzeugID);
        meinFlugzeug.setBankkontoBesitzer(KontoNummer);
        meinFlugzeug.setIdFluggesellschaft(fgID);
        meinFlugzeug.setIdflugzeugBesitzer(UserID);
        facadeFlugzeug.editFlugzeugMietKauf(meinFlugzeug);
        // Blacklisteintragungen löschen
        facadeFlugzeug.blackListAlleEintraegeEntfernen(currentFlugzeug.getIdMietKauf());

        NewMessage(loginMB.onSprache("Hangar_msg_uebertragungDesFlugzeugesErfolgreich"));
        onZeigeGekaufte();

      }

    } else {
      NewMessage(loginMB.onSprache("Hangar_msg_duBistNichtEigentuemerDiesesFlugzeugs"));
    }

  }

  public void onTransferToUser() {

    boolean Transfer = true;

    // prüfen ob Benutzer das Flugzeug schon besitzt
    if (currentFlugzeug.getIdflugzeugBesitzer().equals(UserID) && !currentFlugzeug.getIstGesperrt()) {
      Transfer = true;
      if (currentFlugzeug.getIdUserDerFlugzeugGesperrtHat() < -1) {
        Transfer = false;
        NewMessage("Flugzeug durch System gesperrt, kein Transfer möglich");
      }
    } else {
      Transfer = false;
      NewMessage(loginMB.onSprache("Hangar_msg_FlugzeugtransferZuUserFehlgeschlagen"));
    }

    // ********* Evtl. Zwangsversteigerung
    if (Transfer) {
      double Banksaldo = 0.0;
      try {
        Banksaldo = facadeFlugzeug.getErweiterterBankSaldo(UserID);
      } catch (NullPointerException e) {
        Banksaldo = 0.0;
      }

      if (Banksaldo <= -150000) {
        getVerkaufsPreis();
        onVerKaufen();
        Transfer = false;
      }
    }

    if (Transfer) {
      if (currentFlugzeug.getIdflugzeugBesitzer().equals(frmNeuerEigentuemer)) {
        NewMessage(loginMB.onSprache("Hangar_msg_FlugzeugtransferZuUserSelbst"));
        Transfer = false;
      }
    }

    // Bankverbindung von neuem Benutzer ermitteln
    Benutzer neuerUser = facadeFlugzeug.readUser(frmNeuerEigentuemer);

    if (Transfer) {
      if (neuerUser == null) {
        Transfer = false;
        NewMessage("Fehler beim ermitteln des Bankkontos");
      }
    }

    if (Transfer) {

      String UserName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName");
      String UserKonto = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("BankKonto");

      int FlugzeugID = currentFlugzeug.getIdMietKauf();

      // Blacklisteintragungen löschen
      facadeFlugzeug.blackListAlleEintraegeEntfernen(currentFlugzeug.getIdMietKauf());

      String An = facadeFlugzeug.readUser(meinFlugzeug.getIdflugzeugBesitzer()).getName1();
      String Von = "**** FTW Aircraft Stock ****";
      String Betreff = "Flugzeugtransfer: " + currentFlugzeug.getRegistrierung() + " " + currentFlugzeug.getType();
      String MailText = "Flugzeug ID: " + currentFlugzeug.getIdMietKauf() + "<br>" + currentFlugzeug.getType() + "<br>Flugzeug wurde an den User: " + neuerUser.getName1() + " übertragen";

      saveMail(Von, An, Betreff, MailText);

      meinFlugzeug = facadeFlugzeug.meinFlugzeug(FlugzeugID);
      meinFlugzeug.setBankkontoBesitzer(neuerUser.getBankKonto());
      meinFlugzeug.setKostenstelle(-1);
      meinFlugzeug.setEigenesBildURL("");
      meinFlugzeug.setBonusFuerRueckfuehrung(0.0);
      meinFlugzeug.setIdFluggesellschaft(-1);
      meinFlugzeug.setIdflugzeugBesitzer(frmNeuerEigentuemer);
      meinFlugzeug.setNurFuerAuftraegeDerFluggesellschaft(false);

      facadeFlugzeug.editFlugzeugMietKauf(meinFlugzeug);

      NewMessage(loginMB.onSprache("Hangar_msg_uebertragungDesFlugzeugesErfolgreich"));

      isLoadedFluggesellschaftFlugzeuge = false;
      isLoadedFlugzeugliste = false;
      isLoadedUserFlugzeuge = false;

      onZeigeGekaufte();

    }

  }

  public void onLeasingToUser() {

    boolean Transfer = true;

    // prüfen ob Benutzer das Flugzeug besitzt
    if (currentFlugzeug.getIdflugzeugBesitzer().equals(UserID)) {
      Transfer = true;
      if (currentFlugzeug.getIdUserDerFlugzeugGesperrtHat() < -1) {
        Transfer = false;
        NewMessage("Flugzeug durch System gesperrt, kein Transfer möglich");
      }
    } else {
      Transfer = false;
      NewMessage(loginMB.onSprache("Hangar_msg_FlugzeugtransferZuUserFehlgeschlagen"));
    }

    // ********* Evtl. Zwangsversteigerung
    if (Transfer) {
      double Banksaldo = 0.0;
      try {
        Banksaldo = facadeFlugzeug.getErweiterterBankSaldo(UserID);
      } catch (NullPointerException e) {
        Banksaldo = 0.0;
      }

      if (Banksaldo <= -150000) {
        getVerkaufsPreis();
        onVerKaufen();
        Transfer = false;
      }
    }

    if (Transfer) {
      if (currentFlugzeug.getIdflugzeugBesitzer().equals(frmNeuerEigentuemer)) {
        NewMessage(loginMB.onSprache("Hangar_msg_FlugzeugtransferZuUserSelbst"));
        Transfer = false;
      }
    }

    // Bankverbindung von neuem Benutzer ermitteln
    Benutzer neuerUser = facadeFlugzeug.readUser(frmNeuerEigentuemer);

    if (Transfer) {
      if (neuerUser == null) {
        Transfer = false;
        NewMessage("Fehler beim ermitteln des Bankkontos");
      }
    }

    if (Transfer) {

      String UserName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName");
      String UserKonto = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("BankKonto");

      int FlugzeugID = currentFlugzeug.getIdMietKauf();

      // Blacklisteintragungen löschen
      facadeFlugzeug.blackListAlleEintraegeEntfernen(currentFlugzeug.getIdMietKauf());

      String An = facadeFlugzeug.readUser(meinFlugzeug.getIdflugzeugBesitzer()).getName1();
      String Von = "**** FTW Aircraft Stock ****";
      String Betreff = "Flugzeugleasing / Finanzierung: " + currentFlugzeug.getRegistrierung() + " " + currentFlugzeug.getType();
      String MailText = "Flugzeug ID: " + currentFlugzeug.getIdMietKauf() + "<br>" + currentFlugzeug.getType() + "<br>Flugzeug wurde an den User: " + neuerUser.getName1() + " Verleast / Finanzierung.";

      saveMail(Von, An, Betreff, MailText);

      meinFlugzeug = facadeFlugzeug.meinFlugzeug(FlugzeugID);
      meinFlugzeug.setBankkontoBesitzer(neuerUser.getBankKonto());
      meinFlugzeug.setKostenstelle(-1);
      meinFlugzeug.setEigenesBildURL("");
      meinFlugzeug.setBonusFuerRueckfuehrung(0.0);
      meinFlugzeug.setIdFluggesellschaft(-1);
      meinFlugzeug.setLeasingAnUserID(frmNeuerEigentuemer);
      meinFlugzeug.setNurFuerAuftraegeDerFluggesellschaft(false);

      //***
      //************* Sperren entfernen
      //***
      meinFlugzeug.setIstGesperrt(false);
      meinFlugzeug.setIstGesperrtBis(null);
      meinFlugzeug.setIstGesperrtSeit(null);
      meinFlugzeug.setIstInDerLuft(false);
      meinFlugzeug.setIdUserDerFlugzeugGesperrtHat(-1);

      facadeFlugzeug.editFlugzeugMietKauf(meinFlugzeug);

      NewMessage(loginMB.onSprache("Hangar_msg_uebertragungDesFlugzeugesErfolgreich"));

      isLoadedFluggesellschaftFlugzeuge = false;
      isLoadedFlugzeugliste = false;
      isLoadedUserFlugzeuge = false;

      onZeigeGekaufte();

    }

  }

  public void onLeasingEnd() {

    if (currentFlugzeug.getIdUserDerFlugzeugGesperrtHat() >= -1) {

      String UserName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName");
      String UserKonto = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("BankKonto");

      int FlugzeugID = currentFlugzeug.getIdMietKauf();

      // Blacklisteintragungen löschen
      facadeFlugzeug.blackListAlleEintraegeEntfernen(currentFlugzeug.getIdMietKauf());

      String An = facadeFlugzeug.readUser(meinFlugzeug.getIdflugzeugBesitzer()).getName1();
      String Von = "**** FTW Aircraft Stock ****";
      String Betreff = "Flugzeugleasing / Finanzierung beendet: " + currentFlugzeug.getRegistrierung() + " " + currentFlugzeug.getType();
      String MailText = "Flugzeug ID: " + currentFlugzeug.getIdMietKauf() + "<br>" + currentFlugzeug.getType() + "<br>Flugzeugleasing / Finanzierung wurde beendet.";

      saveMail(Von, An, Betreff, MailText);

      meinFlugzeug = facadeFlugzeug.meinFlugzeug(FlugzeugID);
      meinFlugzeug.setBankkontoBesitzer(UserKonto);
      meinFlugzeug.setKostenstelle(-1);
      meinFlugzeug.setEigenesBildURL("");
      meinFlugzeug.setBonusFuerRueckfuehrung(0.0);
      meinFlugzeug.setIdFluggesellschaft(-1);
      meinFlugzeug.setLeasingAnUserID(-1);
      meinFlugzeug.setNurFuerAuftraegeDerFluggesellschaft(false);

      //***
      //************* Sperren entfernen
      //***
      meinFlugzeug.setIstGesperrt(false);
      meinFlugzeug.setIstGesperrtBis(null);
      meinFlugzeug.setIstGesperrtSeit(null);
      meinFlugzeug.setIstInDerLuft(false);
      meinFlugzeug.setIdUserDerFlugzeugGesperrtHat(-1);

      facadeFlugzeug.editFlugzeugMietKauf(meinFlugzeug);

      NewMessage("Flugzeugleasing / Finanzierung wurde beendet");

      isLoadedFluggesellschaftFlugzeuge = false;
      isLoadedFlugzeugliste = false;
      isLoadedUserFlugzeuge = false;

      onZeigeGekaufte();
    } else {
      NewMessage("Flugzeug durch System gesperrt, kein Transfer möglich");
    }

  }

  public String getPruefeUserID() {
    return facadeFlugzeug.getBenutzerNameMieter(frmNeuerEigentuemer);
  }

  public void onTransferFromFGToFG() {

    boolean Transfer = true;

    if (Transfer) {

      Fluggesellschaft fgesellschaft;

      int FlugzeugID = currentFlugzeug.getIdMietKauf();

      // Blacklisteintragungen löschen
      facadeFlugzeug.blackListAlleEintraegeEntfernen(currentFlugzeug.getIdMietKauf());

      meinFlugzeug = facadeFlugzeug.meinFlugzeug(FlugzeugID);

      meinFlugzeug.setKostenstelle(frmKostenStelle);
      meinFlugzeug.setIdFluggesellschaft(fgID);
      meinFlugzeug.setBankkontoBesitzer(trsBankkonto);

      facadeFlugzeug.editFlugzeugMietKauf(meinFlugzeug);

      NewMessage(loginMB.onSprache("Hangar_msg_uebertragungDesFlugzeugesErfolgreich"));

      isLoadedFlugzeugliste = false;

      // NewMessage(loginMB.onSprache("youAreNotOwnerOfThisAircraft"));
    }

  }

  public void onRepair() {

    int AktuellesJahr = Calendar.getInstance().get(Calendar.YEAR);

    double KolbenmotorGewoehnlichNutzungsdauer = 12000;
    double TurboPropGewoehnlichNutzungsdauer = 16000;
    double TurbineGewoehnlichNutzungsdauer = 20000;
    double ServiceFaktor = 0.0;
    int ZuschlagZustand = 0;
    boolean Operaror = false;
    double BasisPreis = 0.0;
    double ReparaturPunkte = 0.0;
    double ReparaturPreis = 0.0;
    double SP_FaktorProzent = 0.0;
    double MaxRepProzent = 100.0 - currentFlugzeug.getZustand();
    setWerkstattSumme(0.0);
    setWerkstattZeit(0);

    if (werkstattRepProzent > MaxRepProzent) {
      werkstattRepProzent = MaxRepProzent;
    } else if (werkstattRepProzent < 0) {
      werkstattRepProzent = MaxRepProzent;
    } else if (werkstattRepProzent == 0.0) {
      werkstattRepProzent = MaxRepProzent;
    }

    Flugzeuge stammfg = null;

    if (currentFlugzeug != null) {
      stammfg = facadeFlugzeug.findFlugzeugbyID(currentFlugzeug.getIdFlugzeug());

      switch (currentFlugzeug.getAntriebsart()) {
        case 1:
          SP_FaktorProzent = 100 / KolbenmotorGewoehnlichNutzungsdauer * (currentFlugzeug.getGesamtAlterDesFlugzeugsMinuten() / 60);
          BasisPreis = stammfg.getVerkaufspreis() / KolbenmotorGewoehnlichNutzungsdauer;
          break;
        case 2:
          SP_FaktorProzent = 100 / TurboPropGewoehnlichNutzungsdauer * (currentFlugzeug.getGesamtAlterDesFlugzeugsMinuten() / 60);
          BasisPreis = stammfg.getVerkaufspreis() / TurboPropGewoehnlichNutzungsdauer;
          break;
        case 3:
          SP_FaktorProzent = 100 / TurbineGewoehnlichNutzungsdauer * (currentFlugzeug.getGesamtAlterDesFlugzeugsMinuten() / 60);
          BasisPreis = stammfg.getVerkaufspreis() / TurbineGewoehnlichNutzungsdauer;
          break;
        default:
          SP_FaktorProzent = 0.0;
          BasisPreis = 0.0;
          break;
      }

      if (SP_FaktorProzent <= 25.99) {
        ServiceFaktor = 0.25;
      } else if (SP_FaktorProzent >= 26.0 && SP_FaktorProzent <= 75.99) {
        ServiceFaktor = 0.1;
      } else if (SP_FaktorProzent >= 76.0 && SP_FaktorProzent <= 125.99) {
        ServiceFaktor = 0.0;
      } else if (SP_FaktorProzent >= 126.0 && SP_FaktorProzent <= 150.99) {
        Operaror = true;
        ServiceFaktor = 0.1;
      } else if (SP_FaktorProzent >= 151.0 && SP_FaktorProzent <= 200.0) {
        ServiceFaktor = 0.25;
        Operaror = true;
      }
      //
      //
      //
      double zwReparaturPunkte = (10000 - (currentFlugzeug.getZustand() * 100));
      ReparaturPunkte = (zwReparaturPunkte / MaxRepProzent) * werkstattRepProzent;

//      System.out.println("de.klees.beans.flugzeuge.flugzeugeMietKaufBean.onRepair() zw " + zwReparaturPunkte);
//      System.out.println("de.klees.beans.flugzeuge.flugzeugeMietKaufBean.onRepair() Rep " + ReparaturPunkte);
      if (!Operaror) {
        ReparaturPreis = BasisPreis - (BasisPreis * ServiceFaktor);
      } else if (Operaror) {
        ReparaturPreis = BasisPreis + (BasisPreis * ServiceFaktor);
      }
      //
      //
      //
      // Zeitzuschlag für Reparatur in %, je schlechter der Zustand je mehr Zuschlag
      ZuschlagZustand = (int) ((1200 / 5) * werkstattRepProzent);
      if (werkstattRepProzent > 5.0) {
        ZuschlagZustand = 1200;
      }
      //
      //
      //
      //
      setWerkstattSumme(ReparaturPreis * ReparaturPunkte);
      double Arbeitszeit = ReparaturPunkte * 2.5;
      setWerkstattZeit((int) Arbeitszeit + ZuschlagZustand);

      if (currentFlugzeug.getLizenz().equals("PPL-A")) {
        frmCCheckWert = stammfg.getVerkaufspreis() * 0.10;
        frmCCheckZeit = (2 * 60 * 5);
      } else if (currentFlugzeug.getLizenz().equals("CPL")) {
        frmCCheckWert = stammfg.getVerkaufspreis() * 0.10;
        frmCCheckZeit = (4 * 60 * 5);
      } else if (currentFlugzeug.getLizenz().equals("MPL")) {
        frmCCheckWert = stammfg.getVerkaufspreis() * 0.10;
        frmCCheckZeit = (8 * 60 * 5);
      } else if (currentFlugzeug.getLizenz().equals("ATPL")) {
        frmCCheckWert = stammfg.getVerkaufspreis() * 0.10;
        frmCCheckZeit = (16 * 60 * 5);
      }

      if (frmCCheckDurchfuehren) {
        werkstattRepProzent = 0.0;
        setWerkstattSumme(frmCCheckWert);
        setWerkstattZeit(frmCCheckZeit);
      }

      if (Double.isNaN(werkstattSumme)) {
        werkstattSumme = 0.0;
      }

    }

    if (PSHangarID > 0) {
      onReparaturkostenPrivatHangar();
    }

  }

  public void onReparaturDurchfuehren() {

    double Betrag = werkstattSumme;
    boolean Reparatur = false;

    if (Betrag > 0) {

      if (currentFlugzeug.getIstGesperrt()) {
        NewMessage(loginMB.onSprache("Hangar_msg_FlugzeugIstGeperrtUndKannNichtRepariertWerden"));
        Reparatur = false;
      } else {
        Reparatur = true;
      }

      if (Reparatur) {
        double Banksaldo = facadeFlugzeug.getBankSaldo(currentFlugzeug.getBankkontoBesitzer());

        if (Banksaldo > werkstattSumme) {
          Reparatur = true;
        } else {
          NewMessage(loginMB.onSprache("Hangar_msg_KontostandReichtNichtAusUmReparaturZuBezahlen"));
          Reparatur = false;
        }
      }

      if (Reparatur) {

        if (PSHangarID > 0) {
          onPrivaterServiceHangarAbrechnung();
        } else {
          String AuftraggeberKonto = currentFlugzeug.getBankkontoBesitzer();
          String AuftraggeberName = getKontoName(AuftraggeberKonto);

          String VerwendungsZweck = "Reparaturkosten für : " + currentFlugzeug.getType() + " : " + currentFlugzeug.getRegistrierung() + " ICAO : " + currentFlugzeug.getAktuellePositionICAO();
          //Pfand beim Flugzeugbesitzer abziehen
          SaveBankbuchung(AuftraggeberKonto, AuftraggeberName, "500-1000002", "**** FTW Aircraft Stock ****", new Date(), Betrag - (Betrag * 2), AuftraggeberKonto, AuftraggeberName,
                  new Date(), VerwendungsZweck, currentFlugzeug.getIdflugzeugBesitzer(), -1, -1, -1, -1, -1, -1, currentFlugzeug.getAktuellePositionICAO(), -1,
                  currentFlugzeug.getIdMietKauf(), currentFlugzeug.getKostenstelle(), -1);

          // Hole Flugzeug
          meinFlugzeug = facadeFlugzeug.getFlugzeugMietKauf(currentFlugzeug.getIdMietKauf());

          double neuZustand = (double) Math.round((meinFlugzeug.getZustand() + werkstattRepProzent) * 100) / 100;

          meinFlugzeug.setZustand(neuZustand);

          if (frmCCheckDurchfuehren) {
            meinFlugzeug.setZustand(100.0);
          }

          //
          // Flugzeug Sperren für die Dauer der Reparaturzeit
          //
          //****************************************
          //Sperrzeit ausrechnen und eintragen
          //****************************************
          long aktZeit = new Date().getTime();
          long endeZeit = aktZeit + ((long) werkstattZeit * 60 * 1000);
          meinFlugzeug.setIstGesperrtBis(new Date(endeZeit));
          meinFlugzeug.setIstGesperrtSeit(new Date(aktZeit));
          meinFlugzeug.setIstGesperrt(true);
          meinFlugzeug.setIstInDerLuft(false);
          meinFlugzeug.setIdUserDerFlugzeugGesperrtHat(-300);
          meinFlugzeug.setLetzterCheckMinuten(0);

          //    
          // Fälligkeit nächster C-Check
          //
          if (frmCCheckDurchfuehren) {
            Date ccheckDatum = java.sql.Date.valueOf(LocalDate.now().plusMonths(6));
            meinFlugzeug.setNaechsterTerminCcheck(ccheckDatum);
          }

          facadeFlugzeug.onFlugzeugMieteSpeichern(meinFlugzeug);
        }// Ende Reparatur
      }

      werkstattRepProzent = 0.0;
      werkstattSumme = 0.0;
      werkstattZeit = 0;

    }

    onZeigeGekaufte();
  }

  public double getCcheckPreis(int MietkaufID) {

    for (ViewFlugzeugeMietKauf fg : mietKaufFlugzeuge) {

      if (fg.getIdMietKauf() == MietkaufID) {

        Flugzeuge stammfg = null;
        stammfg = facadeFlugzeug.findFlugzeugbyID(fg.getIdFlugzeug());

        switch (fg.getLizenz()) {
          case "PPL-A":
            frmCCheckWert = stammfg.getVerkaufspreis() * 0.10;
            frmCCheckZeit = (2 * 60);
            break;
          case "CPL":
            frmCCheckWert = stammfg.getVerkaufspreis() * 0.10;
            frmCCheckZeit = (4 * 60);
            break;
          case "MPL":
            frmCCheckWert = stammfg.getVerkaufspreis() * 0.10;
            frmCCheckZeit = (8 * 60);
            break;
          case "ATPL":
            frmCCheckWert = stammfg.getVerkaufspreis() * 0.10;
            frmCCheckZeit = (16 * 60);
            break;
          default:
            break;
        }
        return frmCCheckWert;

      }

    }

    return 0.0;
  }

  public void onFlugzeugWert() {

    Flugzeuge stammfg = null;

    if (currentFlugzeug != null) {
      stammfg = facadeFlugzeug.findFlugzeugbyID(currentFlugzeug.getIdFlugzeug());

    }

    if (stammfg != null) {

      //System.out.println("Berechnung");
      // um die Maximale Nutzungsdauer zu Berechnen wird die gewöhnliche Nutzungsdauer mit 2 mulitpliziert
      int KolbenmotorGewoehnlichNutzungsdauer = 12000;
      int TurboPropGewoehnlichNutzungsdauer = 16000;
      int TurbineGewoehnlichNutzungsdauer = 20000;
      int AktuellesJahr = Calendar.getInstance().get(Calendar.YEAR);

      double ZellenStunden = currentFlugzeug.getGesamtAlterDesFlugzeugsMinuten() / 60;
      double Alter = AktuellesJahr - currentFlugzeug.getBaujahr();
      double MaximaleNutzungsdauer = 0;
      double Wert = stammfg.getVerkaufspreis();
      double Zustand = currentFlugzeug.getZustand();
      double MietFaktor = 0.0005;

      switch (currentFlugzeug.getAntriebsart()) {
        case 1:
          MaximaleNutzungsdauer = KolbenmotorGewoehnlichNutzungsdauer * 2;
          break;
        case 2:
          MaximaleNutzungsdauer = TurboPropGewoehnlichNutzungsdauer * 2;
          break;
        case 3:
          MaximaleNutzungsdauer = TurbineGewoehnlichNutzungsdauer * 2;
          break;
        default:
          MaximaleNutzungsdauer = 0;
          break;
      }

      // Kalkulierter Marktpreis(mit Zellenstunden) =  FTW-Neupreis - ((Zellenstunden/ Maximale Nutzungsdauer )  * Kalkulierter Neupreis)
      double KalkulierterMarktPreisMitZellenstunden = Wert - ((ZellenStunden / MaximaleNutzungsdauer) * Wert);

      // Marktpreis mit Alter = Kalkulierter Marktpreis mit Zellenstunden - (Kalkulierter Marktpreis mit Zellenstunden*(Alter/100))
      double MarktpreisMitAlter = KalkulierterMarktPreisMitZellenstunden - (KalkulierterMarktPreisMitZellenstunden * (Alter / 100.));

      // Verkaufspreis = Zustand  / 100 * Kalkulierter Marktpreis mit Alter
      double Verkaufspreis = (Zustand / 100.) * MarktpreisMitAlter;

      if (CCheckFaellig()) {
        Verkaufspreis = (Verkaufspreis - getCcheckPreis(currentFlugzeug.getIdMietKauf()));
      } else {
        //Anteilige Checkkosten abziehen
        if (CCheckFaktor > 0) {
          Verkaufspreis = (Verkaufspreis - (getCcheckPreis(currentFlugzeug.getIdMietKauf()) * CCheckFaktor));
        }

      }

      setVerkaufsPreis(Verkaufspreis * 0.9);

    }

  }

  public boolean CCheckFaellig() {
    // C-Check prüfen
    // aktuelles Datum
    Calendar ccheck = Calendar.getInstance();
    Date naechsterCCheck = currentFlugzeug.getNaechsterTerminCcheck();

    int AktMonat = ccheck.get(ccheck.MONTH) + 1; // Monate beginnen bei 0 = Januar
    int AktJahr = ccheck.get(ccheck.YEAR);

    int CCheckMonat = Integer.valueOf(new SimpleDateFormat("MM").format(naechsterCCheck)) + 1;
    int CCheckJahr = Integer.valueOf(new SimpleDateFormat("yyyy").format(naechsterCCheck));

    int Monate = ((CCheckJahr * 12) + CCheckMonat - 1) - ((AktJahr * 12) + AktMonat - 1);

    if (Monate > 6) {
      Monate = 6;
    }

    CCheckFaktor = (100.0 - ((100.0 / 6.0) * Monate)) / 100.;

    if (Monate <= 0) {
      CCheckFaktor = 1;
    }

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

  public boolean isFlugzeugMietbar() {

    boolean mietbar = false;

    if (currentFlugzeug != null) {

      if (currentFlugzeug.getIstMietbar()) {
        mietbar = true;
      }

      //Prüfen ob für das Flugzeuge eine ErlaubtListe vorhanden ist
      //und Benutzer mit der Liste abgleichen
      if (!mietbar) {
        if (facadeFlugzeug.ifExistErlaubtListe(currentFlugzeug.getIdMietKauf())) {
          if (facadeFlugzeug.ifUserInErlaubtListe(UserID, currentFlugzeug.getIdMietKauf())) {
            mietbar = true;
          }
        }
      }

      if (!mietbar) {
        // gehört Flugzeug einer Fluggesellschaft
        if (currentFlugzeug.getFluggesellschaftID() != null) {
          if (currentFlugzeug.getFluggesellschaftID() > 0) {
            //s
            // Piloten der Fluggesellschaft können trotzdem mieten  
            if (facadeFlugzeug.istMieterInDerFluggesellschaft(currentFlugzeug.getFluggesellschaftID(), UserID)) {
              mietbar = true;
            }
          }
        }
      }

      if (mietbar) {
        if (CCheckFaellig()) {
          mietbar = false;
        }
      }

      if (currentFlugzeug.getIdflugzeugBesitzer() == UserID) {
        mietbar = true;
      }

      if (currentFlugzeug.getIstGesperrt()) {
        mietbar = false;
      }

    }

    return mietbar;
  }

//********************** Flugzeug Umbau Beginn
  public Flugzeuge getUmbauFlugzeug() {
    if (currentFlugzeug != null) {
      int UmbauID = currentFlugzeug.getIdUmbauModel();

      if (UmbauID > 0) {
        Flugzeuge fg = facadeFlugzeug.readFlugzeug(UmbauID);

        if (fg != null) {
          return fg;
        }
      }

    }
    return null;
  }

  public boolean isUmbauFaehig() {
    if (currentFlugzeug != null) {
      if (currentFlugzeug.getIdUmbauModel() > 0 && !currentFlugzeug.getFlugzeugUmgebaut()) {
        return true;
      }
    }

    return false;
  }

  public void onFlugzeugUmbauen() {
    if (currentFlugzeug != null) {
      int UmbauID = currentFlugzeug.getIdUmbauModel();
      boolean UmbauOK = false;

      Flugzeuge fg = facadeFlugzeug.readFlugzeug(UmbauID);

      if (fg != null) {

        double Banksaldo = facadeFlugzeug.getBankSaldo(currentFlugzeug.getBankkontoBesitzer());

        if (Banksaldo > fg.getUmbaukosten()) {
          String AuftraggeberKonto = currentFlugzeug.getBankkontoBesitzer();
          String AuftraggeberName = getKontoName(AuftraggeberKonto);

          Double Betrag = fg.getUmbaukosten();

          String VerwendungsZweck = "Umbaukosten für : " + currentFlugzeug.getType() + " : " + currentFlugzeug.getRegistrierung() + " ICAO : " + currentFlugzeug.getAktuellePositionICAO();
          SaveBankbuchung(AuftraggeberKonto, AuftraggeberName, "500-1000002", "**** FTW Aircraft Stock ****", new Date(), Betrag - (Betrag * 2), AuftraggeberKonto, AuftraggeberName,
                  new Date(), VerwendungsZweck, currentFlugzeug.getIdflugzeugBesitzer(), -1, -1, -1, -1, -1, -1, currentFlugzeug.getAktuellePositionICAO(), -1,
                  currentFlugzeug.getIdMietKauf(), currentFlugzeug.getKostenstelle(), -1);

          UmbauOK = true;

        } else {
          NewMessage(loginMB.onSprache("Hangar_msg_KontostandReichtNichtAusUmReparaturZuBezahlen"));
        }

        if (UmbauOK) {
          Flugzeugemietkauf flz = facadeFlugzeug.getFlugzeugMietKauf(currentFlugzeug.getIdMietKauf());
          flz.setIdFlugzeug(fg.getIdFlugzeug());
          flz.setGesamtMaschinenLaufzeitMinuten(CONF.zufallszahl(300, 600));
          flz.setGesamtAlterDesFlugzeugsMinuten(CONF.zufallszahl(300, 600));
          flz.setGesamtFlugzeit(0);
          flz.setGesamtEntfernung(0);
          flz.setGesamtFluege(0);
          flz.setGesamtTransportiertePassagiere(0);
          flz.setGesamtTransportiertesCargo(0);
          flz.setMaschinenLaufzeitMinuten(0);
          flz.setIdSitzKonfiguration(-1);
          flz.setZustand(100.0);

          //****************************************
          //Sperrzeit ausrechnen und eintragen
          //****************************************
          long aktZeit = new Date().getTime();
          long endeZeit = aktZeit + ((long) fg.getUmbauzeit() * 60 * 1000);
          flz.setIstGesperrtBis(new Date(endeZeit));
          flz.setIstGesperrtSeit(new Date(aktZeit));
          flz.setIstGesperrt(true);
          flz.setIstInDerLuft(false);
          flz.setIdUserDerFlugzeugGesperrtHat(-300);
          flz.setLetzterCheckMinuten(0);

          Date ccheckDatum = java.sql.Date.valueOf(LocalDate.now().plusMonths(6));
          flz.setNaechsterTerminCcheck(ccheckDatum);

          flz.setFlugzeugUmgebaut(true);

          facadeFlugzeug.editFlugzeugMietKauf(flz);

        }

      }

    }

    onZeigeGekaufte();
  }

  //********************** Flugzeug Umbau Ende
  // *************************************************** Lizenzprüfungen beginn
  public boolean LizenzPruefen() {
    boolean LizenzOK = false;

    UserTyperatings userTypes = null;

    String FlugzeugLizenz = "";
    String TypeRating = "";
    String UserLizenz = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Lizenz");

    if (currentFlugzeug != null) {
      FlugzeugLizenz = currentFlugzeug.getLizenz();
      TypeRating = currentFlugzeug.getTypeRating();

      // Bei ATPL muss keine weitere Prüfung erfolgen
      if (UserLizenz.equals("ATPL")) {
        return true;
      }
      // Bei gleicher Lizenz muss keine weiere Prüfung erfolgen
      if (UserLizenz.equals(currentFlugzeug.getLizenz())) {
        return true;
      }

    } else {
      //System.out.println("de.klees.beans.flugzeuge.flugzeugeMietKaufBean.LizenzPruefen() Kein Flugzeug ausgewählt");
      return false;
    }

    // Lizenzverarbeitung wenn für Fluggesellschaft geflogen wird
    if (currentFlugzeug.getFluggesellschaftID() != null) {
      if (currentFlugzeug.getFluggesellschaftID() > 0) {
        if (facadeFlugzeug.istMieterInDerFluggesellschaft(currentFlugzeug.getFluggesellschaftID(), UserID)) {
          //System.out.println("de.klees.beans.flugzeuge.flugzeugeMietKaufBean.LizenzPruefen() für Pilot der Fluggesellschaft");

          FlugzeugLizenz = currentFlugzeug.getLizenz();
          TypeRating = currentFlugzeug.getTypeRating();

          //System.out.println("de.klees.beans.flugzeuge.flugzeugeMietKaufBean.LizenzPruefen() Flugzeuglizenz :   " + FlugzeugLizenz);
          if (!FlugzeugLizenz.equals("")) {

            // Hat Benutzer schon das Typrating für dieses Flugzeug oder hat er mit dem Typerating begonnen,
            // wenn ja hat er auch die entsprechende Lizenz, weitere Prüfungen nicht notwendeig
//            userTypes = facadeFlugzeug.findTypeRating(UserID, TypeRating);
//            if (userTypes != null) {
//              if (userTypes.getMinutenGeflogen() > 0) {
//                return true;
//              }
//            }
            //System.out.println("de.klees.beans.flugzeuge.flugzeugeMietKaufBean.LizenzPruefen() Hat Benutzer schon das Typrating für dieses Flugzeug oder hat er mit dem Typerating begonnen, : False  ");
            // Mit MPL Lizenz darf auch in einer FG ATPL geflogen werden, sofern ein Flugzeug in der FG vorhanden ist
//            userTypes = facadeFlugzeug.findOffeneTypeRating(UserID);
            if (FlugzeugLizenz.equals("ATPL") && (UserLizenz.equals("MPL") || UserLizenz.equals("ATPL"))) {
//              if (userTypes != null) {
//                if (!userTypes.getTypeRating().equals(TypeRating)) {
//                  return true;
//                }
//              } else {
//                return true;
//              }
              return true;
            }

            // Mit PPL-A und CPL darf bis max. MPL geflogen werden
            // Hier muss noch ausgebaut werden, das funktioniert vermutlich nicht richtig 
            if ((FlugzeugLizenz.equals("PPL-A") || FlugzeugLizenz.equals("CPL") || FlugzeugLizenz.equals("MPL"))
                    && (UserLizenz.equals("PPL-A") || UserLizenz.equals("CPL") || UserLizenz.equals("MPL") || UserLizenz.equals("ATPL"))) {

              //System.out.println("de.klees.beans.flugzeuge.flugzeugeMietKaufBean.LizenzPruefen() Mit PPL-A und CPL darf bis max. MPL geflogen werden ");
//              if (userTypes != null) {
//                if (!userTypes.getTypeRating().equals(TypeRating)) {
//                  //System.out.println("de.klees.beans.flugzeuge.flugzeugeMietKaufBean.LizenzPruefen() Typrating stimmt mit Flugzeugtyperating überein");
//                  return true;
//                }
//              } else {
//                return true;
//              }
              return true;
            }
          }
        } else {
          // Pilot mietet Flugzeug von Fluggesellschaft und ist nicht Mietglied
          //System.out.println("de.klees.beans.flugzeuge.flugzeugeMietKaufBean.LizenzPruefen() Pilot mietet Flugzeug von Fluggesellschaft und ist nicht Mietglied");
          return LizenzPruefungFreierPilot(FlugzeugLizenz, TypeRating, UserLizenz);
        }
      }
    } else {
      /*
      ************************  Lizenzprüfung Freier Pilot - Freelancer
       */

      //System.out.println("de.klees.beans.flugzeuge.flugzeugeMietKaufBean.LizenzPruefen() für Freien Piloten");
      return LizenzPruefungFreierPilot(FlugzeugLizenz, TypeRating, UserLizenz);

    }

    //System.out.println("de.klees.beans.flugzeuge.flugzeugeMietKaufBean.LizenzPruefen() Lizenzprüfung erfolglos");
    return false;
  }

  private String offenesTyperating(int UserID) {
    UserTyperatings rating = facadeFlugzeug.findOffeneTypeRating(UserID);

    if (rating != null) {
      return rating.getTypeRating();
    }
    return "";

  }

  private boolean LizenzPruefungFreierPilot(String FlugzeugLizenz, String TypeRating, String UserLizenz) {

    //UserTyperatings userTypes = null;
    //System.out.println("de.klees.beans.flugzeuge.flugzeugeMietKaufBean.LizenzPruefen() (Modul) für Freien Piloten");
    if (!FlugzeugLizenz.equals("")) {

      // Flugzeuglizen mit Userlizenz vergleichen
      if (FlugzeugLizenz.equals(UserLizenz)) {

        return true;
      }

      if (UserLizenz.equals("CPL") && (FlugzeugLizenz.equals("CPL") || FlugzeugLizenz.equals("PPL-A"))) {
        //System.out.println("de.klees.beans.flugzeuge.flugzeugeMietKaufBean.LizenzPruefen() Userlizenz = " + UserLizenz);
        // Hat der Benutzer angefangenes Typerating 
        //System.out.println("de.klees.beans.flugzeuge.flugzeugeMietKaufBean.LizenzPruefen() hat der Benutzer ein offenes Typrating");

        return true;

      }

      if (UserLizenz.equals("MPL") && (FlugzeugLizenz.equals("CPL") || FlugzeugLizenz.equals("PPL-A") || FlugzeugLizenz.equals("MPL"))) {
        //System.out.println("de.klees.beans.flugzeuge.flugzeugeMietKaufBean.LizenzPruefen() Userlizenz = " + UserLizenz);
        // Hat der Benutzer angefangenes Typerating 
        //System.out.println("de.klees.beans.flugzeuge.flugzeugeMietKaufBean.LizenzPruefen() hat der Benutzer ein offenes Typrating");

        return true;

      }

      if (UserLizenz.equals("ATPL") && (FlugzeugLizenz.equals("CPL") || FlugzeugLizenz.equals("PPL-A") || FlugzeugLizenz.equals("MPL"))) {
        //System.out.println("de.klees.beans.flugzeuge.flugzeugeMietKaufBean.LizenzPruefen() Userlizenz = " + UserLizenz);
        // Hat der Benutzer angefangenes Typerating 
        //System.out.println("de.klees.beans.flugzeuge.flugzeugeMietKaufBean.LizenzPruefen() hat der Benutzer ein offenes Typrating");
        return true;
      }

    }

    return false;
  }

  // *************************************************** Lizenzprüfungen ENDE
  //
  //
  //
  // *************************************************** MOD Tools beginn
  public void onFlugzeugUmsetzen() {

    if (!dobNeuerFlugzeugStandort.equals("")) {
      setDobNeuerFlugzeugStandort(dobNeuerFlugzeugStandort.toUpperCase());
    }

    if (facadeFlugzeug.onFlugzeugUmsetzen(getDobNeuerFlugzeugStandort(), getDobFlugzeugID()) > 0) {
      NewMessage("Flugzeug wurde nach " + dobNeuerFlugzeugStandort + " Umgesetzt");
    } else {
      NewMessage("Beim Umsetzen hat was nicht geklappt");
    }

  }

  public void onFlugzeugAusDerLuftHolen() {
    if (facadeFlugzeug.onFlugzeugAusDerLuftHolen(getDobFlugzeugID()) > 0) {
      NewMessage("Flugzeug wurde erfolgreich aus der Luft geholt");
    } else {
      NewMessage("Flugzeug konnte nicht aus der Luft geholt werden.");
    }
  }

  public void onFlugzeugAusDerLuftHolenAusHangar() {
    if (facadeFlugzeug.onFlugzeugAusDerLuftHolen(currentFlugzeug.getIdMietKauf()) > 0) {
      onFlugzeugeMietKaufInDerLuft();
      NewMessage("Flugzeug wurde erfolgreich aus der Luft geholt");
      setSelectedFlugzeug(null);
    } else {
      NewMessage("Flugzeug konnte nicht aus der Luft geholt werden.");
    }
  }

  public void onUserFlugZuruecksetzen() {
    if (facadeFlugzeug.onUserFlugZuruecksetzen(dobUserID)) {
      NewMessage("Flug wurde erfolgreich zurückgesetzt.");
    } else {
      NewMessage("Flug konnte nicht zurückgesetzt werden.");
    }

  }

  public void onFlugzeugTankfuellung() {
    if (facadeFlugzeug.onFlugzeugTankfuellung(getDobTankfuellung(), getDobFlugzeugID()) > 0) {
      NewMessage("Flugzeugtankfüllung wurde geändert");
    } else {
      NewMessage("Beim Füllen hat was nicht geklappt");
    }
  }

  public void onFlugzeugEinemUserZuordnen() {
    if (facadeFlugzeug.onFlugzeugEinemUserZuordnen(getDobUserID(), getDobFlugzeugID()) > 0) {
      NewMessage("Flugzeug wurde dem User mit der ID : " + dobUserID + " zugeordnet");
    } else {
      NewMessage("Beim Zuweisen hat was nicht geklappt");
    }
  }

  public void onFlugzeugAirframeSetzen() {
    if (facadeFlugzeug.onFlugzeugAirframeSetzen(getDobAirframe(), getDobFlugzeugID()) > 0) {
      NewMessage("Flugzeugalter wurde auf : " + getDobAirframe() + " geändert");
    } else {
      NewMessage("Beim Zuweisen hat was nicht geklappt");
    }
  }

  public void onDeleteUserFlugzeug() {
    if (currentFlugzeug != null) {

      if (!facadeFlugzeug.ifUserFlugzeugMeilenGroesserNull(currentFlugzeug.getIdMietKauf())) {
        facadeFlugzeug.onDeleteFlugeugInBetrieb(currentFlugzeug.getIdMietKauf());
        NewMessage(loginMB.onSprache("Hangar_msg_flugzeugGeloescht"));
        isLoadedFlugzeugliste = false;
      } else {
        NewMessage("Flugzeug darf nicht gelöscht werden, da damit geflogen wurde.");
      }

    }
  }

  //
  //
  // *************************************************** MOD Tools ENDE
  /*
  ******************************************* Service - Hangar BEGINN
   */
  public List<ViewFBOUserObjekte> getServiceHangars() {
    if (currentFlugzeug != null) {
      return facadeFlugzeug.getServiceHangars(currentFlugzeug.getAktuellePositionICAO());
    } else {
      return null;
    }
  }

  public List<Object> getPakete() {
    if (PSHangarID > 0 && currentFlugzeug != null) {

      List<Lagerservicehangar> lager = facadeFlugzeug.getPakete(currentFlugzeug.getType(), PSHangarID);

      List<Object> obj = new ArrayList<>();

      for (Lagerservicehangar items : lager) {
        if (items.getPaketart() == 1) {
          obj.add("C-Check");
        } else if (items.getPaketart() == 2) {
          obj.add("Reparatur");
        }
      }
      return obj;
    } else {
      return null;
    }

  }

  public void onReparaturkostenPrivatHangar() {

    Lagerservicehangar lagerItem = null;

    boolean repairOK = false;

    //prüfen ob Hangarkapazitaet ueberschritten ist
    int qm = (int) (currentFlugzeug.getLaenge() / 100) * (currentFlugzeug.getBreite() / 100);

    System.out.println("de.klees.beans.flugzeuge.flugzeugeMietKaufBean.onReparaturkostenPrivatHangar() " + PSHangarPaketArt);

    if (PSHangarPaketArt != null) {

      if (isPlatzImHangar(PSHangarID, qm)) {
        repairOK = true;
      } else {
        NewMessage("Hangar ist voll belegt.");
      }

      if (PSHangarPaketArt.equals("C-Check") && repairOK) {
        lagerItem = facadeFlugzeug.readPaket(currentFlugzeug.getType(), PSHangarID, 1);
      } else if (PSHangarPaketArt.equals("Reparatur")) {
        lagerItem = facadeFlugzeug.readPaket(currentFlugzeug.getType(), PSHangarID, 2);
      }

      if (lagerItem != null) {
        PSLagerItemID = lagerItem.getIdlagerservicehangar();
        repairOK = true;
      }

      if (repairOK) {
        if (PSHangarPaketArt.equals("Reparatur")) {
          if (lagerItem.getMenge() >= werkstattRepProzent) {
            repairOK = true;
          } else {
            repairOK = false;
            NewMessage("Nicht genug Material am Lager");
          }
        } else {
          if (lagerItem.getMenge() > 0) {
            repairOK = true;
          } else {
            repairOK = false;
            NewMessage("Nicht genug Material am Lager");
          }
        }
      }
    }

    if (repairOK) {
      try {
        werkstattSumme = lagerItem.getPaketvkpreis();
        if (lagerItem.getPaketart() == 1) {
          if (currentFlugzeug.getLizenz().equals("PPL-A")) {
            werkstattZeit = (2 * 60 * 5);
          } else if (currentFlugzeug.getLizenz().equals("CPL")) {
            werkstattZeit = (4 * 60 * 5);
          } else if (currentFlugzeug.getLizenz().equals("MPL")) {
            werkstattZeit = (8 * 60 * 5);
          } else if (currentFlugzeug.getLizenz().equals("ATPL")) {
            werkstattZeit = (16 * 60 * 5);
          }

          werkstattSumme = lagerItem.getPaketvkpreis();
        } else if (lagerItem.getPaketart() == 2) {
          werkstattSumme = lagerItem.getPaketvkpreis() * werkstattRepProzent;
        }

        double Arbeitszeit = werkstattZeit;

        werkstattZeit = (int) (Arbeitszeit * 0.7);
      } catch (NullPointerException e) {
        NewMessage("Es wurde kein Paketpreis angegeben, benachrichtigen Sie den Hangar Betreiber.");
      }
    }

  }

  public void onPrivaterServiceHangarAbrechnung() {

    String EmpfaengerKonto = "";
    String EmpfaengerName = "";
    boolean AbrechnungOK = false;

    //FBO-Objekt für Abrechnung
    FboUserObjekte hangar = facadeFlugzeug.readServiceHangar(PSHangarID);
    ViewFBOUserObjekte viewHangar = facadeFlugzeug.readViewServiceHangar(PSHangarID);
    //ID vom ServicePaket
    Lagerservicehangar paket = facadeFlugzeug.readPaket(currentFlugzeug.getType(), PSLagerItemID, -1);

    //prüfen ob Hangarkapazitaet ueberschritten ist
    int qm = (int) (currentFlugzeug.getLaenge() / 100) * (currentFlugzeug.getBreite() / 100);

    if (hangar != null) {
      if (isPlatzImHangar(PSHangarID, qm)) {
        AbrechnungOK = true;
      } else {
        NewMessage("Hangar ist zur Zeit voll belegt.");
        AbrechnungOK = false;
      }
    }

    if (AbrechnungOK) {
      if (PSHangarPaketArt.equals("C-Check")) {
        if (paket.getMenge() > 0) {
          AbrechnungOK = true;
        } else {
          NewMessage("Nicht genug Material am Lager, bitte kleineren Prozentsatz wählen");
          AbrechnungOK = false;
        }
      } else if (paket.getMenge() >= werkstattRepProzent) {
        AbrechnungOK = true;
      } else {
        NewMessage("Nicht genug Material am Lager, bitte kleineren Prozentsatz wählen");
        AbrechnungOK = false;
      }
    }

    if (AbrechnungOK) {
      if (!(werkstattSumme > 0)) {
        AbrechnungOK = false;
        NewMessage("Betrag = 0, Service kann nicht ausgeführt werden");
      }
    }

    if (AbrechnungOK) {
      if (paket != null) {
        double aktuellerBestand = paket.getMenge();
        double neuerBestand = 0.0;
        AbrechnungOK = true;
        if (PSHangarPaketArt.equals("C-Check")) {
          neuerBestand = aktuellerBestand - 1.0;
        } else {
          neuerBestand = aktuellerBestand - werkstattRepProzent;
        }
        paket.setMenge(neuerBestand);
        paket.setVerkauftam(new Date());
        facadeFlugzeug.editHangarLagerBestandVerbuchen(paket);
      } else {
        AbrechnungOK = false;
      }

    }

    if (AbrechnungOK) {
      EmpfaengerKonto = hangar.getBankkonto();
      EmpfaengerName = getKontoName(EmpfaengerKonto);

      String AuftraggeberKonto = currentFlugzeug.getBankkontoBesitzer();
      String AuftraggeberName = getKontoName(AuftraggeberKonto);

      String VerwendungsZweck = PSHangarPaketArt + " für : " + currentFlugzeug.getType() + " : " + currentFlugzeug.getRegistrierung() + " ICAO : " + currentFlugzeug.getAktuellePositionICAO();

      //Servicekosten Auftraggeber
      double Betrag = (werkstattSumme * -1);

      SaveBankbuchung(AuftraggeberKonto, AuftraggeberName, EmpfaengerKonto, EmpfaengerName, new Date(), Betrag, AuftraggeberKonto, AuftraggeberName,
              new Date(), VerwendungsZweck, currentFlugzeug.getIdflugzeugBesitzer(), -1, -1, -1, -1, -1, -1, currentFlugzeug.getAktuellePositionICAO(), -1,
              currentFlugzeug.getIdMietKauf(), currentFlugzeug.getKostenstelle(), -1);

      //Servicekosten Umsatz Hangarbetreiber
      SaveBankbuchung(EmpfaengerKonto, EmpfaengerName, AuftraggeberKonto, AuftraggeberName, new Date(), werkstattSumme, EmpfaengerKonto, EmpfaengerName,
              new Date(), VerwendungsZweck, -1, -1, -1, -1, -1, -1, -1, currentFlugzeug.getAktuellePositionICAO(), -1,
              currentFlugzeug.getIdMietKauf(), hangar.getKostenstelle(), -1);

      //Hangarbelegung aktualisieren
      Hangarbelegung belegung = new Hangarbelegung();
      Lagerservicehangarumsatz lagerUmsatz = new Lagerservicehangarumsatz();

      //****************************************
      //Sperrzeit ausrechnen und eintragen
      //****************************************
      long aktZeit = new Date().getTime();
      long endeZeit = aktZeit + ((long) werkstattZeit * 60 * 1000);

      belegung.setAblaufzeit(new Date(endeZeit));
      belegung.setDauerminuten(werkstattZeit);
      belegung.setFlugzeugtyp(currentFlugzeug.getType());
      belegung.setKennung(currentFlugzeug.getRegistrierung());
      belegung.setIduserfboid(PSHangarID);
      belegung.setIdServicePaket(PSLagerItemID);

      belegung.setQuadratmeter(qm);

      if (PSHangarPaketArt.equals("C-Check")) {
        belegung.setServiceart(1);
      } else {
        belegung.setServiceart(2);
      }

      //Service-Pakete verbuchen
      belegung.setWert(werkstattSumme);
      facadeFlugzeug.newHangarbelegung(belegung);

      //Termin eintragen
      Servicehangartermine termin = new Servicehangartermine();
      termin.setIdservicehangar(PSHangarID);
      termin.setIduser(hangar.getIdUser());
      termin.setEnddatum(new Date(endeZeit));
      termin.setStartdatum(new Date(aktZeit));
      if (PSHangarPaketArt.equals("C-Check")) {
        termin.setText(PSHangarPaketArt + ": " + currentFlugzeug.getType());
      } else {
        termin.setText("Reparatur: " + currentFlugzeug.getType());
      }

      facadeFlugzeug.neuerHangarTermin(termin);

      //Umsatz verbuchen
      lagerUmsatz.setVerkauftanuserid(UserID);
      lagerUmsatz.setIdfbouserobjekt(PSHangarID);
      lagerUmsatz.setIdlagerservicehangar(paket.getIdlagerservicehangar());
      lagerUmsatz.setIduser(hangar.getIdUser());

      if (PSHangarPaketArt.equals("C-Check")) {
        lagerUmsatz.setMenge(1.0);
      } else {
        lagerUmsatz.setMenge(werkstattRepProzent);
      }

      lagerUmsatz.setPaketart(paket.getPaketart());
      lagerUmsatz.setPaketekpreis(paket.getPaketekpreis());
      lagerUmsatz.setPaketname(currentFlugzeug.getType());
      lagerUmsatz.setPaketvkpreis(werkstattSumme);
      lagerUmsatz.setVerkauftam(new Date());

      facadeFlugzeug.neuerUmsatzVerbuchen(lagerUmsatz);

      // Flugzeugdaten aktualisieren Flugzeug
      meinFlugzeug = facadeFlugzeug.getFlugzeugMietKauf(currentFlugzeug.getIdMietKauf());

      double neuerZustand = (double) Math.round((meinFlugzeug.getZustand() + werkstattRepProzent) * 100) / 100;

      meinFlugzeug.setZustand(neuerZustand);

      //
      // Flugzeug Sperren für die Dauer der Reparaturzeit
      //
      //****************************************
      //Sperrzeit ausrechnen und eintragen
      //****************************************
      meinFlugzeug.setIstGesperrtBis(new Date(endeZeit));
      meinFlugzeug.setIstGesperrtSeit(new Date(aktZeit));
      meinFlugzeug.setIstGesperrt(true);
      meinFlugzeug.setIstInDerLuft(false);
      meinFlugzeug.setIdUserDerFlugzeugGesperrtHat(-300);
      meinFlugzeug.setLetzterCheckMinuten(0);

      //    
      // Fälligkeit nächster C-Check
      //
      if (PSHangarPaketArt.equals("C-Check")) {
        meinFlugzeug.setZustand(100.0);
        Date ccheckDatum = java.sql.Date.valueOf(LocalDate.now().plusMonths(6));
        meinFlugzeug.setNaechsterTerminCcheck(ccheckDatum);
      }

      facadeFlugzeug.onFlugzeugMieteSpeichern(meinFlugzeug);
    }

  }

  public boolean isCheckAufMaterial() {
    if (currentFlugzeug != null) {
      return facadeFlugzeug.checkAufMaterial(currentFlugzeug.getAktuellePositionICAO(), currentFlugzeug.getType());
    } else {
      return false;
    }
  }

  public boolean isPlatzImHangar(int hangarID, int benoetigterPlatzqm) {

    int hangarGroesseqm = facadeFlugzeug.readViewServiceHangar(hangarID).getServicehangarQM();

    List<Hangarbelegung> belegung = facadeFlugzeug.getHangarbelegung(hangarID);

    int summeBelegung = 0;

    for (Hangarbelegung item : belegung) {
      summeBelegung = summeBelegung + item.getQuadratmeter();
    }

    summeBelegung = summeBelegung + benoetigterPlatzqm;

    if (summeBelegung > hangarGroesseqm) {
      return false;
    } else {
      return true;
    }
  }

  /*
  ******************************************* Service - Hangar ENDE
   */
  public void onFlugzeugUeberfuehren() {

    boolean transferOK = false;

    if (currentFlugzeug != null) {
      if (!currentFlugzeug.getIstGesperrt()) {

        onBerechnungTransfer();

        double Banksaldo = facadeFlugzeug.getBankSaldo(trsBankkonto);

        if (Banksaldo >= trsBetrag) {
          transferOK = true;
        } else {
          NewMessage(loginMB.onSprache("Hangar_msg_duHastNichtGenugGeldUmDenSystempilotenZuBezahlen"));
          transferOK = false;
        }

        if (transferOK) {
          // Hole Flugzeug
          meinFlugzeug = facadeFlugzeug.getFlugzeugMietKauf(currentFlugzeug.getIdMietKauf());
          //Mietzeit eintragen          
          meinFlugzeug.setIstGesperrtBis(trsDatumEnde);
          meinFlugzeug.setIstGesperrtSeit(new Date());
          meinFlugzeug.setIstGesperrt(true);
          meinFlugzeug.setIstInDerLuft(true);
          meinFlugzeug.setIdUserDerFlugzeugGesperrtHat(-300);
          facadeFlugzeug.onFlugzeugMieteSpeichern(meinFlugzeug);

          Flugzeugtransfer transfer = new Flugzeugtransfer();

          transfer.setBankkonto(trsBankkonto);
          transfer.setKontoname(facadeFlugzeug.getBankKontoName(trsBankkonto));
          transfer.setBeginndatum(new Date());
          transfer.setBetrag(trsBetrag);
          transfer.setEnddatum(trsDatumEnde);
          transfer.setEntfernung(trsEntfernung);
          transfer.setIdFlugzeugMietKauf(currentFlugzeug.getIdMietKauf());
          transfer.setIduser(UserID);
          transfer.setZielicao(trsZielICAO.toUpperCase().trim());

          facadeFlugzeug.onFlugzeugTransfer(transfer);

          NewMessage(loginMB.onSprache("Hangar_msg_derAuftragWurdeAnDenSystempilotenUebermittelt"));

          trsIstKalkuliert = false;
          FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("TransferGebucht", true);
          isLoadedFlugzeugliste = false;
        }
      } else {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("TransferNichtGebucht", true);
        NewMessage(loginMB.onSprache("Hangar_msg_TransferNichtGebucht"));
      }
    }

  }

  public void onTransferdialogClose() {
    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("TransferGebucht", false);
    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("TransferNichtGebucht", false);
  }

  public void onBerechnungTransfer() {

    setTrsBetrag(0);
    double PreisPilot = 0.0;
    double PreisGewicht = 0.0;
    double Spritpreis = 0.0;
    double ZeitbedarfMinuten = 0.0;
    double Spritverbrauch = 0.0;
    int Gewicht;
    trsIstKalkuliert = false;
    config = facadeFlugzeug.readConfig();

    Airport airportstart = facadeFlugzeug.getAirportInfo(currentFlugzeug.getAktuellePositionICAO());
    Airport airportziel = facadeFlugzeug.getAirportInfo(trsZielICAO);

    if (airportstart != null && airportziel != null) {
      int[] ergebnis = CONF.DistanzBerechnung(airportstart.getLongitude(), airportstart.getLatitude(), airportziel.getLongitude(), airportziel.getLatitude());
      setTrsEntfernung(ergebnis[0]);

      ZeitbedarfMinuten = (60.0 / currentFlugzeug.getReisegeschwindigkeitTAS() * getTrsEntfernung()) + 20;
      Spritverbrauch = (ZeitbedarfMinuten / 60) * currentFlugzeug.getVerbrauchStunde();
      Gewicht = currentFlugzeug.getLeergewicht();

      if (currentFlugzeug.getTreibstoffArt() == 1) {
        Spritpreis = config.getPreisAVGASkg() * Spritverbrauch;
      } else {
        Spritpreis = config.getPreisJETAkg() * Spritverbrauch;
      }

      switch (currentFlugzeug.getLizenz()) {
        case "PPL-A":
          PreisPilot = (ZeitbedarfMinuten * config.getAbrCrewgebuehren() / 60);
          PreisGewicht = (Gewicht * 0.08);
          break;
        case "CPL":
          PreisPilot = (ZeitbedarfMinuten * config.getAbrCrewgebuehren() / 60) * 1.10;
          PreisGewicht = (Gewicht * 0.08) * 1.10;
          break;
        case "MPL":
          PreisPilot = (ZeitbedarfMinuten * config.getAbrCrewgebuehren() / 60) * 1.50;
          PreisGewicht = (Gewicht * 0.08) * 1.50;

          // ***** Passagierflugzeuge
          if (currentFlugzeug.getExitlimit() > 150) {
            PreisPilot = PreisPilot * 65;
          } else if (currentFlugzeug.getExitlimit() > 200) {
            PreisPilot = PreisPilot * 80;
          } else if (currentFlugzeug.getExitlimit() > 250) {
            PreisPilot = PreisPilot * 90;
          }

          //***** Cargoflugzeuge
          if (currentFlugzeug.getExitlimit() == 0 && currentFlugzeug.getCargo() > 10000) {
            PreisPilot = PreisPilot * 30;
          } else if (currentFlugzeug.getExitlimit() == 0 && currentFlugzeug.getCargo() > 15000) {
            PreisPilot = PreisPilot * 40;
          } else if (currentFlugzeug.getExitlimit() == 0 && currentFlugzeug.getCargo() > 20000) {
            PreisPilot = PreisPilot * 65;
          } else if (currentFlugzeug.getExitlimit() == 0 && currentFlugzeug.getCargo() > 25000) {
            PreisPilot = PreisPilot * 80;
          } else if (currentFlugzeug.getExitlimit() == 0 && currentFlugzeug.getCargo() > 30000) {
            PreisPilot = PreisPilot * 90;
          } else if (currentFlugzeug.getExitlimit() == 0 && currentFlugzeug.getCargo() > 35000) {
            PreisPilot = PreisPilot * 95;
          }

          break;
        case "ATPL":
          PreisPilot = (ZeitbedarfMinuten * config.getAbrCrewgebuehren() / 60) * 2;
          // Otto verteuert damit private Unternehmen Flugzeuge ueberfuehren koennen
          // 24.04.2019
          PreisPilot = PreisPilot * 95;
          PreisGewicht = (Gewicht * 0.08) * 2;
          break;
        default:
          PreisPilot = (ZeitbedarfMinuten * config.getAbrCrewgebuehren() / 60);
          PreisGewicht = (Gewicht * 0.08);
          break;
      }

      setTrsDatumEnde(new Date((long) (Calendar.getInstance().getTimeInMillis() + (ZeitbedarfMinuten * 60.0 * 1000.0))));

      trsIstKalkuliert = true;
    }

    setTrsBetrag(PreisPilot + Spritpreis + PreisGewicht);
  }

  public void onFlugzeugAusDerLuftFluggesellschat(int CancelID) {

    ViewFlugzeugeMietKauf cafg = facadeFlugzeug.readFlugzeugMietKauf(CancelID);

    if (cafg != null) {
      if (!(cafg.getIdUserDerFlugzeugGesperrtHat() == -300)) {

        int iduser = cafg.getIdUserDerFlugzeugGesperrtHat();
        String username = facadeFlugzeug.getUserName(iduser);

        Fluggesellschaft fg = facadeFlugzeug.readFG(cafg.getFluggesellschaftID());

        Yaacarskopf yaacarsflight = facadeFlugzeug.readYAACARSFlight(iduser);

        if (yaacarsflight != null) {
          yaacarsflight.setFlugzeugid(-1);
          facadeFlugzeug.saveYAACARSKopf(yaacarsflight);
        }

        String betreff = "Flugzeug wurde durch " + fg.getName() + " aus der Luft geholt ";

        String meldung = "Fluggesellschaft: " + fg.getName()
                + "<br>Betroffener User :  " + username
                + "<br>Flugzeug: " + cafg.getType()
                + "<br>Flugzeug ID: " + cafg.getIdMietKauf()
                + "<br>Registrierung: " + cafg.getRegistrierung()
                + "<br>Aktueller Standort: " + cafg.getAktuellePositionICAO();

        if (facadeFlugzeug.onFlugzeugAusDerLuftHolen(CancelID) > 0) {

          NewMessage("Flugzeug wurde erfolgreich aus der Luft geholt");

          isLoadedFlugzeugliste = false;
        } else {
          NewMessage("Flugzeug konnte nicht aus der Luft geholt werden.");
        }

      } else {
        NewMessage("Flugzeug ist vom System gesperrt!");

      }

    }

  }

  public void onRefresh() {

    isLoadedFlugzeugliste = false;

    FlugGesellschaft = facadeFlugzeug.readFG(FluggesellschaftID);

    //System.out.println("de.klees.beans.airline.airlineBean.onRowSelect() FG ID " + FlugGesellschaft.getIdFluggesellschaft());
    if (FlugGesellschaft.getIdFluggesellschaft().equals(ManagerID)) {
      //System.out.println("de.klees.beans.airline.airlineBean.onRowSelect() Berechtigungen auslesen");

      Fluggesellschaftmanager mg = facadeFlugzeug.readManager(UserID, FlugGesellschaft.getIdFluggesellschaft());

      setAllowFlugzeuge(mg.getAllowFlugzeuge());
      setAllowFlugzeugeBearbeiten(mg.getAllowFlugzeugeBearbeiten());
      setAllowFlugzeugeKaufen(mg.getAllowFlugzeugeKaufen());
      setAllowFlugzeugeTransfer(mg.getAllowFlugzeugeTransfer());
      setAllowFlugzeugeVerkaufen(mg.getAllowFlugzeugeVerkaufen());

    } else {

      //System.out.println("de.klees.beans.airline.airlineBean.onRowSelect() default Berechtigung");
      setAllowFlugzeuge(true);
      setAllowFlugzeugeBearbeiten(true);
      setAllowFlugzeugeKaufen(true);
      setAllowFlugzeugeTransfer(true);
      setAllowFlugzeugeVerkaufen(true);

    }

    isIsBenutzerBesitzer();

  }


  /*
   *********** Grafikupload BEGINN
   */
  public UploadedFile getGrafikFile() {
    return GrafikFile;

  }

  public void setGrafikFile(UploadedFile GrafikFile) {
    this.GrafikFile = GrafikFile;
  }

  public void onGrafikUpload(FileUploadEvent event) {
    GrafikFile = event.getFile();
    if (GrafikFile != null) {
      try {
        BufferedImage image;
        image = ImageIO.read(event.getFile().getInputstream());

        String Datei = CONF.getLocalWWWDir() + "/images/FTW/usergrafiken/" + currentFlugzeug.getIdMietKauf() + "-" + currentFlugzeug.getType() + ".jpg";
        ImageIO.write(image, "jpg", new File(Datei));

        frmEigenesBild = CONF.getDomainURL()+ "/images/FTW/usergrafiken/" + currentFlugzeug.getIdMietKauf() + "-" + currentFlugzeug.getType() + ".jpg";
        onSaveFlugzeug();

      } catch (IOException e) {
        System.out.println("de.klees.beans.flugzeuge.flugzeugeMietKaufBean.onGrafikUpload() " + e.getMessage());
      }

    }
  }

  /*
   *********** Grafikupload ENDE
   */
  public void onRowSelect(SelectEvent event) {
    if (currentFlugzeug != null) {
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FlugzeugID", currentFlugzeug.getIdMietKauf());

      isBenutzerBesitzer = currentFlugzeug.getIdflugzeugBesitzer().equals(UserID);
      isBenutzerMieter = currentFlugzeug.getIdUserDerFlugzeugGesperrtHat().equals(UserID);
      frmKostenStelle = currentFlugzeug.getKostenstelle();

      if (isBenutzerBesitzer) {
        isZuVerkaufen = false;
      } else {
        isZuVerkaufen = true;
      }

      if (isZuVerkaufen) {
        if (!currentFlugzeug.getIstZuVerkaufen()) {
          isZuVerkaufen = false;
        }
      }

      if (currentFlugzeug.getFluggesellschaftID() != null) {
        fgID = currentFlugzeug.getFluggesellschaftID();
      } else {
        fgID = -1;
      }

      frmBankKonto = currentFlugzeug.getBankkontoBesitzer();

      PSHangarID = -1;

    }
  }

  public void onRowUnselect(UnselectEvent event) {
    isBenutzerBesitzer = false;
    isBenutzerMieter = false;
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
   * @param fgID
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
          int userid, int AirportID, int fgID, int FlugzeugBesitzerID, int IndustrieID, int LeasinggesellschaftID, int TransportID,
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
    newBuchung.setFluggesellschaftID(fgID);
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

    facadeFlugzeug.createBankbuchung(newBuchung);

  }


  /*
  ******************** Setter and Getter
   */
  public String getHangarZeit() {
    return df.format(new Date());
  }

  public double getVerkaufsPreis() {
    onFlugzeugWert();
    return verkaufsPreis;
  }

  public void setVerkaufsPreis(double verkaufsPreis) {
    this.verkaufsPreis = verkaufsPreis;
  }

  public ViewFlugzeugeMietKauf getSelectedFlugzeug() {
    return currentFlugzeug;
  }

  public void setSelectedFlugzeug(ViewFlugzeugeMietKauf selectedFlugzeug) {
    this.currentFlugzeug = selectedFlugzeug;
  }

  public String getFrmflugzeugTyp() {
    return frmflugzeugTyp;
  }

  public void setFrmflugzeugTyp(String frmflugzeugTyp) {
    this.frmflugzeugTyp = frmflugzeugTyp;
  }

  public String getSucheBezeichnung() {
    return SucheBezeichnung;
  }

  public void setSucheBezeichnung(String SucheBezeichnung) {
    this.SucheBezeichnung = SucheBezeichnung;
  }

  public Map<String, String> getTreibstoffArt() {
    return TreibstoffArt;
  }

  public void setTreibstoffArt(Map<String, String> TreibstoffArt) {
    this.TreibstoffArt = TreibstoffArt;
  }

  public Map<String, String> getAntriebsArt() {
    return AntriebsArt;
  }

  public void setAntriebsArt(Map<String, String> AntriebsArt) {
    this.AntriebsArt = AntriebsArt;
  }

  public Map<String, String> getTriebwerksType() {
    return TriebwerksType;
  }

  public void setTriebwerksType(Map<String, String> TriebwerksType) {
    this.TriebwerksType = TriebwerksType;
  }

  public boolean isBtnMietbar() {
    return btnMietbar;
  }

  public void setBtnMietbar(boolean btnMietbar) {
    this.btnMietbar = btnMietbar;
  }

  public boolean isBtnKaufbar() {
    return btnKaufbar;
  }

  public void setBtnKaufbar(boolean btnKaufbar) {
    this.btnKaufbar = btnKaufbar;
  }

  public boolean isVerleast() {
    if (currentFlugzeug != null) {
      if (currentFlugzeug.getIdflugzeugBesitzer() == UserID && currentFlugzeug.getLeasingAnUserID() > 0) {
        return true;
      }
    }
    return false;
  }

  public boolean isIsBenutzerBesitzer() {

    if (currentFlugzeug != null) {
      if (currentFlugzeug.getLeasingAnUserID() == UserID || currentFlugzeug.getIdflugzeugBesitzer() == UserID) {
        return true;
      }
    }

    return false;
  }

  public void setIsBenutzerBesitzer(boolean isBenutzerBesitzer) {
    this.isBenutzerBesitzer = isBenutzerBesitzer;
  }

  public String getFrmRegistrierung() {
    return frmRegistrierung;
  }

  public void setFrmRegistrierung(String frmRegistrierung) {
    this.frmRegistrierung = frmRegistrierung;
  }

  public String getFrmHeimatICAO() {
    return frmHeimatICAO;
  }

  public void setFrmHeimatICAO(String frmHeimatICAO) {
    this.frmHeimatICAO = frmHeimatICAO;
  }

  public double getFrmVerkaufsPreis() {
    return frmVerkaufsPreis;
  }

  public void setFrmVerkaufsPreis(double frmVerkaufsPreis) {
    this.frmVerkaufsPreis = frmVerkaufsPreis;
  }

  public double getFrmMietPreisNass() {
    return frmMietPreisNass;
  }

  public void setFrmMietPreisNass(double frmMietPreisNass) {
    this.frmMietPreisNass = frmMietPreisNass;
  }

  public double getFrmMietPreisTrocken() {
    return frmMietPreisTrocken;
  }

  public void setFrmMietPreisTrocken(double frmMietPreisTrocken) {
    this.frmMietPreisTrocken = frmMietPreisTrocken;
  }

  public boolean isFrmMietbar() {
    return frmMietbar;
  }

  public void setFrmMietbar(boolean frmMietbar) {
    this.frmMietbar = frmMietbar;
  }

  public boolean isFrmKaufbar() {
    return frmKaufbar;
  }

  public void setFrmKaufbar(boolean frmKaufbar) {
    this.frmKaufbar = frmKaufbar;
  }

  public int getFrmMaxMietZeit() {
    return frmMaxMietZeit;
  }

  public void setFrmMaxMietZeit(int frmMaxMietZeit) {
    this.frmMaxMietZeit = frmMaxMietZeit;
  }

  public String getFrmBankKonto() {
    return frmBankKonto;
  }

  public void setFrmBankKonto(String frmBankKonto) {
    this.frmBankKonto = frmBankKonto;
  }

  public int getFgID() {
    return fgID;
  }

  public void setFgID(int fgID) {
    this.fgID = fgID;
  }

  public String getFgGemieteVon() {
    return fgGemieteVon;
  }

  public void setFgGemieteVon(String fgGemieteVon) {
    this.fgGemieteVon = fgGemieteVon;
  }

  public String getFgGemietetSeit() {
    return fgGemietetSeit;
  }

  public void setFgGemietetSeit(String fgGemietetSeit) {
    this.fgGemietetSeit = fgGemietetSeit;
  }

  public String getAssignment_icao() {
    return assignment_icao;
  }

  public void setAssignment_icao(String assignment_icao) {
    this.assignment_icao = assignment_icao;
  }

  public int getFrmPfand() {
    return frmPfand;
  }

  public void setFrmPfand(int frmPfand) {
    this.frmPfand = frmPfand;
  }

  public Sitzkonfiguration getSelectedKonfig() {
    return selectedKonfig;
  }

  public void setSelectedKonfig(Sitzkonfiguration selectedKonfig) {
    this.selectedKonfig = selectedKonfig;
  }

  public boolean isIsBenutzerMieter() {
    return isBenutzerMieter;
  }

  public void setIsBenutzerMieter(boolean isBenutzerMieter) {
    this.isBenutzerMieter = isBenutzerMieter;
  }

  public String getSitzKonfigBezeichnung(int IDKonfig) {

    if (IDKonfig > 0) {
      Sitzkonfiguration sitzkonfig = facadeFlugzeug.getKonfig(IDKonfig);
      if (sitzkonfig != null) {
        sitzKonfigBezeichnung = sitzkonfig.getBezeichnung();
      } else {
        sitzKonfigBezeichnung = "Error in Sitzkonfiguration";
      }
    } else {
      sitzKonfigBezeichnung = loginMB.onSprache("Hangar_msg_keineKonfigurationVorgenommen");
    }

    return sitzKonfigBezeichnung;
  }

  public void setSitzKonfigBezeichnung(String sitzKonfigBezeichnung) {
    this.sitzKonfigBezeichnung = sitzKonfigBezeichnung;
  }

  public int getDobFlugzeugID() {
    return dobFlugzeugID;
  }

  public void setDobFlugzeugID(int dobFlugzeugID) {
    this.dobFlugzeugID = dobFlugzeugID;
  }

  public String getDobNeuerFlugzeugStandort() {
    return dobNeuerFlugzeugStandort;
  }

  public void setDobNeuerFlugzeugStandort(String dobNeuerFlugzeugStandort) {
    this.dobNeuerFlugzeugStandort = dobNeuerFlugzeugStandort;
  }

  public String getStandortICAO() {
    return StandortICAO;
  }

  public void setStandortICAO(String StandortICAO) {
    this.StandortICAO = StandortICAO;
  }

  public String getUserName(int UserID) {
    return facadeFlugzeug.getUserName(UserID);
  }

  public String getKontoName(String KontoName) {
    return facadeFlugzeug.getBankKontoName(KontoName);
  }

  public String getTrsZielICAO() {
    return trsZielICAO;
  }

  public void setTrsZielICAO(String trsZielICAO) {
    this.trsZielICAO = trsZielICAO;
  }

  public String getTrsICAOName() {
    return trsICAOName;
  }

  public void setTrsICAOName(String trsICAOName) {
    this.trsICAOName = trsICAOName;
  }

  public String getTrsBankkonto() {
    return trsBankkonto;
  }

  public void setTrsBankkonto(String trsBankkonto) {
    this.trsBankkonto = trsBankkonto;
  }

  public String getTrsKontoName() {
    return trsKontoName;
  }

  public void setTrsKontoName(String trsKontoName) {
    this.trsKontoName = trsKontoName;
  }

  public int getTrsEntfernung() {
    return trsEntfernung;
  }

  public void setTrsEntfernung(int trsEntfernung) {
    this.trsEntfernung = trsEntfernung;
  }

  public double getTrsBetrag() {
    return trsBetrag;
  }

  public void setTrsBetrag(double trsBetrag) {
    this.trsBetrag = trsBetrag;
  }

  public Date getTrsDatumEnde() {
    return trsDatumEnde;
  }

  public void setTrsDatumEnde(Date trsDatumEnde) {
    this.trsDatumEnde = trsDatumEnde;
  }

  public boolean isTrsIstKalkuliert() {
    return trsIstKalkuliert;
  }

  public void setTrsIstKalkuliert(boolean trsIstKalkuliert) {
    this.trsIstKalkuliert = trsIstKalkuliert;
  }

  public int getFrmKostenStelle() {
    return frmKostenStelle;
  }

  public void setFrmKostenStelle(int frmKostenStelle) {
    this.frmKostenStelle = frmKostenStelle;
  }

  public String getAirlineName() {
    return AirlineName;
  }

  public int getFluggesellschaftID() {
    return FluggesellschaftID;
  }

  public void setFluggesellschaftID(int FluggesellschaftID) {
    this.FluggesellschaftID = FluggesellschaftID;
  }

  public int getDobTankfuellung() {
    return dobTankfuellung;
  }

  public void setDobTankfuellung(int dobTankfuellung) {
    this.dobTankfuellung = dobTankfuellung;
  }

  public int getDobUserID() {
    return dobUserID;
  }

  public void setDobUserID(int dobUserID) {
    this.dobUserID = dobUserID;
  }

  public int getDobAirframe() {
    return dobAirframe;
  }

  public void setDobAirframe(int dobAirframe) {
    this.dobAirframe = dobAirframe;
  }

  public boolean isAllowFlugzeuge() {
    return allowFlugzeuge;
  }

  public void setAllowFlugzeuge(boolean allowFlugzeuge) {
    this.allowFlugzeuge = allowFlugzeuge;
  }

  public boolean isAllowFlugzeugeBearbeiten() {
    return allowFlugzeugeBearbeiten;
  }

  public void setAllowFlugzeugeBearbeiten(boolean allowFlugzeugeBearbeiten) {
    this.allowFlugzeugeBearbeiten = allowFlugzeugeBearbeiten;
  }

  public boolean isAllowFlugzeugeKaufen() {
    return allowFlugzeugeKaufen;
  }

  public void setAllowFlugzeugeKaufen(boolean allowFlugzeugeKaufen) {
    this.allowFlugzeugeKaufen = allowFlugzeugeKaufen;
  }

  public boolean isAllowFlugzeugeTransfer() {
    return allowFlugzeugeTransfer;
  }

  public void setAllowFlugzeugeTransfer(boolean allowFlugzeugeTransfer) {
    this.allowFlugzeugeTransfer = allowFlugzeugeTransfer;
  }

  public boolean isAllowFlugzeugeVerkaufen() {
    return allowFlugzeugeVerkaufen;
  }

  public void setAllowFlugzeugeVerkaufen(boolean allowFlugzeugeVerkaufen) {
    this.allowFlugzeugeVerkaufen = allowFlugzeugeVerkaufen;
  }

  public double getWerkstattSumme() {
    return werkstattSumme;
  }

  public void setWerkstattSumme(double werkstattSumme) {
    this.werkstattSumme = werkstattSumme;
  }

  public int getWerkstattZeit() {
    return werkstattZeit;
  }

  public void setWerkstattZeit(int werkstattZeit) {
    this.werkstattZeit = werkstattZeit;
  }

  public boolean isFrmNurFGAuftraege() {
    return frmNurFGAuftraege;
  }

  public void setFrmNurFGAuftraege(boolean frmNurFGAuftraege) {
    this.frmNurFGAuftraege = frmNurFGAuftraege;
  }

  public String getFrmEigenesBild() {
    return frmEigenesBild;
  }

  public void setFrmEigenesBild(String frmEigenesBild) {
    this.frmEigenesBild = frmEigenesBild;
  }

  public String getFrmLizenz() {
    return frmLizenz;
  }

  public void setFrmLizenz(String frmLizenz) {
    this.frmLizenz = frmLizenz;
  }

  public int getFrmNeuerEigentuemer() {
    return frmNeuerEigentuemer;
  }

  public void setFrmNeuerEigentuemer(int frmNeuerEigentuemer) {
    this.frmNeuerEigentuemer = frmNeuerEigentuemer;
  }

  public String getFrmFlugzeugArt() {
    return frmFlugzeugArt;
  }

  public void setFrmFlugzeugArt(String frmFlugzeugArt) {
    this.frmFlugzeugArt = frmFlugzeugArt;
  }

  public double getWerkstattRepProzent() {
    return werkstattRepProzent;
  }

  public void setWerkstattRepProzent(double werkstattRepProzent) {
    this.werkstattRepProzent = werkstattRepProzent;
  }

  public double getFrmBreitenGrad() {
    return frmBreitenGrad;
  }

  public void setFrmBreitenGrad(double frmBreitenGrad) {
    this.frmBreitenGrad = frmBreitenGrad;
  }

  public double getFrmLaengenGrad() {
    return frmLaengenGrad;
  }

  public void setFrmLaengenGrad(double frmLaengenGrad) {
    this.frmLaengenGrad = frmLaengenGrad;
  }

  public String getMietZeitText(int Mietzeit) {

    int Stunden = 0;
    int Minuten = 0;
    int Tage = Mietzeit / 1440;

    String Ergebnis = "";

    if (Tage > 0) {
      Stunden = (Mietzeit - (Tage * 1440)) / 60;
    }

    if (Tage <= 0) {
      Stunden = Mietzeit / 60;
    }

    if (Tage <= 0 && Stunden <= 0) {
      Minuten = Mietzeit;
      if (Minuten > 0) {
        return Minuten + " " + loginMB.onSprache("minuten");
      }
    }

    if (Tage == 0) {
      return Stunden + " " + loginMB.onSprache("stunden");
    }

    if (Tage > 0 && (Stunden == 0)) {
      if (Tage == 1) {
        return Tage + " " + loginMB.onSprache("tag");
      }
      return Tage + " " + loginMB.onSprache("tage");
    }

    if (Tage > 1 && (Stunden > 1)) {
      return Tage + " " + loginMB.onSprache("tage") + " " + Stunden + " " + loginMB.onSprache("stunden");
    }

    Ergebnis = Tage + " " + loginMB.onSprache("tag") + " " + Stunden + " " + loginMB.onSprache("stunden");

    return Ergebnis;
  }

  public boolean isFrmCCheckDurchfuehren() {
    return frmCCheckDurchfuehren;
  }

  public void setFrmCCheckDurchfuehren(boolean frmCCheckDurchfuehren) {
    this.frmCCheckDurchfuehren = frmCCheckDurchfuehren;
  }

  public double getFrmCCheckWert() {
    return frmCCheckWert;
  }

  public void setFrmCCheckWert(double frmCCheckWert) {
    this.frmCCheckWert = frmCCheckWert;
  }

  public boolean isFrmCCheckAktiv() {
    return frmCCheckAktiv;
  }

  public void setFrmCCheckAktiv(boolean frmCCheckAktiv) {
    this.frmCCheckAktiv = frmCCheckAktiv;
  }

  public boolean isIsZuVerkaufen() {
    return isZuVerkaufen;
  }

  public void setIsZuVerkaufen(boolean isZuVerkaufen) {
    this.isZuVerkaufen = isZuVerkaufen;
  }

  public int getFrmCCheckZeit() {
    return frmCCheckZeit;
  }

  public void setFrmCCheckZeit(int frmCCheckZeit) {
    this.frmCCheckZeit = frmCCheckZeit;
  }

  public int getPSHangarID() {
    return PSHangarID;
  }

  public void setPSHangarID(int PSHangarID) {
    this.PSHangarID = PSHangarID;
  }

  public int getPSHangarPaketID() {
    return PSHangarPaketID;
  }

  public void setPSHangarPaketID(int PSHangarPaketID) {
    this.PSHangarPaketID = PSHangarPaketID;
  }

  public String getPSHangarPaketArt() {
    return PSHangarPaketArt;
  }

  public void setPSHangarPaketArt(String PSHangarPaketArt) {
    this.PSHangarPaketArt = PSHangarPaketArt;
  }

  public String getSucheKennung() {
    return sucheKennung;
  }

  public void setSucheKennung(String sucheKennung) {
    this.sucheKennung = sucheKennung;
  }

  public int getSucheID() {
    return sucheID;
  }

  public void setSucheID(int sucheID) {
    this.sucheID = sucheID;
  }

  public int getSucheBesitzer() {
    return sucheBesitzer;
  }

  public void setSucheBesitzer(int sucheBesitzer) {
    this.sucheBesitzer = sucheBesitzer;
  }

  public String getFlugzeugBild(int id) {

    if (id > 0) {
      ViewFlugzeugeMietKauf fg = facadeFlugzeug.readFlugzeugMietKauf(id);

      if (fg.getEigenesBildURL().equals("")) {
        return clearURL(fg.getSymbolUrl());
      } else {
        return clearURL(fg.getEigenesBildURL());
      }
    } else {
      return "";
    }
  }

  public String getFlugzeugBezeichnung(int id) {

    if (id > 0) {
      ViewFlugzeugeMietKauf fg = facadeFlugzeug.readFlugzeugMietKauf(id);
      if (fg != null) {
        return fg.getRegistrierung() + " : " + fg.getType();
      }
    }
    return "Type nicht gefunden";
  }

  public String getFlugzeugBildFuerUmbau(int id) {

    if (id > 0) {
      Flugzeuge fg = facadeFlugzeug.readFlugzeug(id);
      if (fg != null) {
        return clearURL(fg.getSymbolUrl());
      } else {
        return "";
      }
    } else {
      return "";
    }

  }

  public boolean isIsBenutzerEigentuemer() {
    if (currentFlugzeug != null) {
      if (currentFlugzeug.getIdflugzeugBesitzer() == UserID) {
        return true;
      }
    }

    return false;
  }

  public void setIsBenutzerEigentuemer(boolean isBenutzerEigentuemer) {
    this.isBenutzerEigentuemer = isBenutzerEigentuemer;
  }

  public void saveMail(String Von, String An, String Betreff, String Nachricht) {

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
    nm.setKategorie("Posteingang");
    nm.setVonID(absender.getIdUser());
    nm.setVonUser(absender.getName1());
    nm.setNachrichtText(CONF.NoScript(Nachricht));
    facadeFlugzeug.saveUserMail(nm);

    //Empfaengermail speichern
    if (!An.equals(Von)) {
      nm = new Mail();
      nm.setUserID(empfaenger.getIdUser());
      nm.setAnID(empfaenger.getIdUser());
      nm.setAnUser(empfaenger.getName1());
      nm.setBetreff(Betreff);
      nm.setDatumZeit(new Date());
      nm.setGelesen(false);
      nm.setKategorie("Posteingang");
      nm.setVonID(absender.getIdUser());
      nm.setVonUser(absender.getName1());
      nm.setNachrichtText(CONF.NoScript(Nachricht));
      facadeFlugzeug.saveUserMail(nm);
    }

  }

  private Benutzer getUserDaten(String BenutzerName) {
    return facadeFlugzeug.findUser(BenutzerName);
  }

  public int getBlackListUserID() {
    return BlackListUserID;
  }

  public void setBlackListUserID(int BlackListUserID) {
    this.BlackListUserID = BlackListUserID;
  }

  public Flugzeugblacklist getBlackListenEintrag() {
    return BlackListenEintrag;
  }

  public void setBlackListenEintrag(Flugzeugblacklist BlackListenEintrag) {
    this.BlackListenEintrag = BlackListenEintrag;
  }

  public int getSucheUberKlasse() {
    return sucheUberKlasse;
  }

  public void setSucheUberKlasse(int sucheUberKlasse) {
    this.sucheUberKlasse = sucheUberKlasse;
  }

}
