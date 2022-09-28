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
package de.klees.beans.airline;

import de.klees.beans.system.CONF;
import de.klees.beans.system.configBean;
import de.klees.beans.system.loginMB;
import de.klees.data.Bank;
import de.klees.data.FlugesellschaftPiloten;
import de.klees.data.Fluggesellschaft;
import de.klees.data.FluggesellschaftBonusSystem;
import de.klees.data.Fluggesellschaftmanager;
import de.klees.data.Flugzeuge;
import de.klees.data.Mail;
import de.klees.data.Benutzer;
import de.klees.data.views.ViewFlugzeugeMietKauf;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
public class airlineBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<Fluggesellschaft> airline;
  private Fluggesellschaft currentAirline;
  private Fluggesellschaft newAirline;
  private String SuchText = "%";
  private final Calendar c = Calendar.getInstance();
  private final int UserID;
  private String AirlineName;
  private String AirlineBankAccountName;
  private String HomeICAO;
  private String ICAOCode;
  private int AirlineAngestellte;
  private int AirlineSize;
  private String AirlineSizeText;
  private String AirlinePassengerTitles;
  private String AirlineLogo;
  private Double kaufPreis;
  private int MengePersonalEinkauf;
  private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private boolean isLoaded;
  private int ManagerID;

  private double traffikIndexPax;
  private double traffikIndexCargo;

  private double FGSummen[];

  private boolean allowBank;
  private boolean allowBankTransfer;
  private boolean allowBankBuchhaltung;
  private boolean allowFluggesellschaftBearbeiten;
  private boolean allowFluggesellschaftLoeschen;
  private boolean allowFlugzeuge;
  private boolean allowFlugzeugeBearbeiten;
  private boolean allowFlugzeugeKaufen;
  private boolean allowFlugzeugeTransfer;
  private boolean allowFlugzeugeVerkaufen;
  private boolean allowManager;
  private boolean allowManagerBearbeiten;
  private boolean allowManagerEinstellen;
  private boolean allowManagerLoeschen;
  private boolean allowPiloten;
  private boolean allowPilotenBearbeiten;
  private boolean allowPilotenEinstellen;
  private boolean allowPilotenEntlassen;
  private boolean allowRouten;
  private boolean allowRoutenBearbeiten;
  private boolean allowRoutenErstellen;
  private boolean allowRoutenLoeschen;

  private FluggesellschaftBonusSystem currentBonus;
  private String frmBonusName;
  private double frmBonusProzent;
  private int frmBonusZeit;
  private String frmBonusUrlAbzeichen;

  private UploadedFile GrafikFile;
  private String frmAirlineLogo;

  @EJB
  FluggesellschaftFacade airlineFacade;

  public airlineBean() {
    isLoaded = false;
    UserID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
    ManagerID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ManagerID");

    
    
    AirlineLogo = CONF.getDomainURL()+ "/images/FTW/fg-logos/logona.png";
  }

  public List<Fluggesellschaft> getAirlines() {
    if (!isLoaded) {
      ManagerID = airlineFacade.readUser(UserID).getFluggesellschaftManagerID();
      isLoaded = true;
      airline = airlineFacade.findAllAirlinesByUser(UserID, ManagerID);
    }
    return airline;
  }

  public List<String> getAllCountrys() {
    return airlineFacade.findAllCountrys();
  }

  public List<Fluggesellschaft> getAllAirlines() {
    if (!isLoaded) {
      isLoaded = true;
      airline = airlineFacade.findAll();
    }
    return airline;
  }

  public void createAirline() {

    if (!airlineFacade.findIcaoCode(ICAOCode)) {
      isLoaded = false;

      newAirline = new Fluggesellschaft();
      newAirline.setIcao(getHomeICAO());
      newAirline.setIcaoCode(ICAOCode);

      if (airlineFacade.ifExistAirline(getAirlineName())) {
        NewMessage(loginMB.onSprache("Fluggesellschaften_msg_FgExistierBereits"));
      } else {

        newAirline.setName(getAirlineName());
        newAirline.setBankKontoName(getAirlineBankAccountName());
        newAirline.setUserid(UserID);
        newAirline.setBesitzerName(airlineFacade.readUser(UserID).getName1());
        newAirline.setKostenstelle(0);
        newAirline.setBundesstaat("");
        newAirline.setLogoURL(AirlineLogo);
        newAirline.setErzeugteJobs(200);
        newAirline.setGeflogeneJobs(200);
        newAirline.setErzeugtesCargo(2000);
        newAirline.setGeflogenesCargo(2000);
        newAirline.setStdBonus1(0.0);
        newAirline.setStdBonus2(0.0);
        newAirline.setStdProvision(0.0);
        newAirline.setKstAbfertigung(0);
        newAirline.setKstBonusPilot(0);
        newAirline.setKstBuchungsgebuehr(0);
        newAirline.setKstGehaltCrew(0);
        newAirline.setKstLandegebuehr(0);
        newAirline.setKstProvisionFluggesellschaft(0);
        newAirline.setKstTreibstoffkostenErstattung(0);

        airlineFacade.create(newAirline);

        setSelectedAirline(newAirline);
        if (currentAirline != null) {
          FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FluggesellschaftID", getSelectedAirline().getIdFluggesellschaft());
          FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FluggesellschaftICAO", getSelectedAirline().getIcao());
          FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FluggesellschaftName", getSelectedAirline().getName());

          NewMessage(loginMB.onSprache("Fluggesellschaften_msg_FgAngelegt"));

          createBankKonto();

          onCreatePilot(airlineFacade.readUser(UserID).getName1());

//          String Betreff = "Neue Airline wurde angelegt: " + AirlineName;
//          String MailText = "Neue Airline wurde angelegt: " + AirlineName
//                  + "<br> Benutzer: " + airlineFacade.readUser(UserID).getName()
//                  + "<br> UserID: " + UserID;
//
//          saveMail("Stefan.Klees", "Stefan.Klees", Betreff, MailText);
//          saveMail("Toffi", "Toffi", Betreff, MailText);
        }
      }
    } else {
      NewMessage(loginMB.onSprache("Fluggesellschaften_msg_FgIcaoCodeExistiertBereits"));
    }

  }

  public void onCreatePilot(String FtwName) {

    if (!FtwName.equals("")) {
      FlugesellschaftPiloten newPilot = new FlugesellschaftPiloten();

      newPilot.setIdflugesellschaft(getSelectedAirline().getIdFluggesellschaft());
      newPilot.setIduser(UserID);
      newPilot.setCallname(FtwName);
      newPilot.setBonus(0.0);
      newPilot.setEmail("@");
      newPilot.setKilometer(0);
      newPilot.setPassagiere(0);
      newPilot.setStatus(1);
      newPilot.setUmsatz(0.0);
      newPilot.setWaren(0);
      newPilot.setZeit(0);
      newPilot.setKostenstelle(0);
      newPilot.setFlusi("");
      airlineFacade.createPilot(newPilot);

      NewMessage(loginMB.onSprache("Fluggesellschaften_Piloten_msg_PilotAngelegt"));

    }

  }

  public List<FluggesellschaftBonusSystem> getBoni() {
    if (currentAirline != null) {
      return airlineFacade.getBoni(currentAirline.getIdFluggesellschaft());
    }
    return null;
  }

  public void readBoni() {
    frmBonusName = currentBonus.getBonusname();
    frmBonusProzent = currentBonus.getBonus();
    frmBonusUrlAbzeichen = currentBonus.getUrlabzeichen();
    frmBonusZeit = currentBonus.getZeit();
  }

  public void onCreateBonus() {

    if (currentAirline != null) {

      FluggesellschaftBonusSystem fgb = new FluggesellschaftBonusSystem();

      fgb.setIdfluggesellschaft(currentAirline.getIdFluggesellschaft());
      fgb.setBonus(frmBonusProzent);
      fgb.setBonusname(frmBonusName);
      fgb.setUrlabzeichen(frmBonusUrlAbzeichen);
      fgb.setZeit(frmBonusZeit);

      airlineFacade.createBonus(fgb);

      NewMessage("Bonus angelegt");
    } else {
      NewMessage("Bonus konnte nicht angelegt werden, keine Fluggesellschaft gewählt.");
    }

  }

  public void onEditBonus() {
    if (currentBonus != null) {

      currentBonus.setIdfluggesellschaft(currentAirline.getIdFluggesellschaft());
      currentBonus.setBonus(frmBonusProzent);
      currentBonus.setBonusname(frmBonusName);
      currentBonus.setUrlabzeichen(frmBonusUrlAbzeichen);
      currentBonus.setZeit(frmBonusZeit);

      airlineFacade.editBonus(currentBonus);
      NewMessage("Bonus gespeichert");
    } else {
      NewMessage("Kein Bonus zum Speichern ausgewählt....");
    }
  }

  public void onDeletBonus() {
    if (currentBonus != null) {
      airlineFacade.removeBonus(currentBonus);
      NewMessage("Bonus gelöscht...");
    } else {
      NewMessage("Kein Bonus zum Löschen ausgewählt....");
    }
  }

  //************* Bankkonto erstellen
  public void createBankKonto() {

    String Kontonummer = getNeueKontoNummer();

    Bank newBankkonto = new Bank();
    newBankkonto.setBankKonto(Kontonummer);
    newBankkonto.setKontoName(currentAirline.getName());
    newBankkonto.setAbsenderKonto("500-1000001");
    newBankkonto.setAbsenderName("**** FTW BANK *****");
    newBankkonto.setEmpfaengerName(currentAirline.getName());
    newBankkonto.setEmpfaengerKonto(Kontonummer);
    newBankkonto.setVerwendungsZweck("Kontoeröffnung");
    newBankkonto.setUeberweisungsDatum(new Date());
    newBankkonto.setAusfuehrungsDatum(new Date());
    newBankkonto.setBetrag(0.01);

    //************ ID's Beginn
    newBankkonto.setUserID(UserID);
    newBankkonto.setAirportID(-1);
    newBankkonto.setFluggesellschaftID(currentAirline.getIdFluggesellschaft());
    newBankkonto.setFlugzeugBesitzerID(-1);
    newBankkonto.setIndustrieID(-1);
    newBankkonto.setLeasinggesellschaftID(-1);
    newBankkonto.setTransportID(-1);
    newBankkonto.setIcao("");
    newBankkonto.setFlugzeugID(-1);
    newBankkonto.setKostenstelle(-1);
    newBankkonto.setPilotID(-1);

    //************ ID's Ende
    airlineFacade.createBankkonto(newBankkonto);

    currentAirline.setBankKonto(Kontonummer);
    currentAirline.setBankKontoName(currentAirline.getName());
    airlineFacade.edit(currentAirline);

  }

  public void saveAirline() {

    if (!airlineFacade.findIcaoCode(ICAOCode)) {
      isLoaded = false;
      currentAirline.setLogoURL(frmAirlineLogo);
      airlineFacade.edit(currentAirline);
      NewMessage(loginMB.onSprache("Fluggesellschaften_msg_FgGespeichert"));

    } else {
      NewMessage(loginMB.onSprache("Fluggesellschaften_msg_FgIcaoCodeExistiertBereits"));
    }

  }

  public void deleteAirline() {
    isLoaded = false;

    // Umbuchung Banksaldo auf Benutzer-Konto
    //
    Benutzer besitzer = airlineFacade.readUser(currentAirline.getUserid());

    String BankKonto = "";
    String KontoName = "";

    if (besitzer != null) {
      BankKonto = besitzer.getBankKonto();
      KontoName = besitzer.getName1();
    } else {
      BankKonto = "500-1000001";
      KontoName = "**** FTW BANK *****";
    }

    if (airlineFacade.ifExistBankkonto(currentAirline.getBankKonto())) {
      double betrag = airlineFacade.BankSaldo(currentAirline.getBankKonto());

      SaveBankbuchung(BankKonto, KontoName, "500-1000001", "**** FTW BANK *****", new Date(), betrag, BankKonto, KontoName, new Date(),
              " Konto geschlossen : " + currentAirline.getName(), UserID, -1, -1, -1, -1, -1, -1, "", -1, -1, -1, -1);

      // *** Löschen der Bankbuchungen
      if (airlineFacade.executeAbfrage("DELETE FROM bank where bankKonto = '" + currentAirline.getBankKonto() + "'") > 0) {
        NewMessage("Bankaccount deleted");
      }
    } else {
      // Benachrichtigung an MOD, Bankkonto nicht gefunden
      String Betreff = "Bankkonto der Airline: " + currentAirline.getName() + " nicht gefunden";
      String text = Betreff;
      saveMail("Stefan.Klees", "Stefan.Klees", Betreff, text);

    }

    // *** Pilotenverknüpfungen Löschen 
    String Abfrage = "delete from flugesellschaft_piloten where idflugesellschaft = " + currentAirline.getIdFluggesellschaft() + ";";
    if (airlineFacade.executeAbfrage(Abfrage) > 0) {
      NewMessage("Pilots deleted");
    }

    // *** Pilotenverknüpfungen zu Flugzeugen (erlaubte Piloten) entfernen
    if (airlineFacade.executeAbfrage("delete from Flugzeuge_ErlaubteUser WHERE idFluggesellschaft = " + currentAirline.getIdFluggesellschaft()) > 0) {
      NewMessage("Pilots > Aircraft deleted");
    }

    // *** Assignments löschen
    int AnzahlKonvertierter = airlineFacade.executeAbfrage("update assignement set active = -1 where idAirline = " + currentAirline.getIdFluggesellschaft() + " and konvertiert=true;");

    if (airlineFacade.executeAbfrage("delete from assignement WHERE idAirline = " + currentAirline.getIdFluggesellschaft()) > 0) {
      NewMessage("Assignment deleted");
    }

    // *** Flugrouten löschen
    if (airlineFacade.executeAbfrage("delete from Flugrouten WHERE idFluggesellschaft = " + currentAirline.getIdFluggesellschaft()) > 0) {
      NewMessage("Routes deleted");
    }

    // *** Flugzeuge Bankkonto wechseln auf Benutzer
    if (airlineFacade.executeAbfrage("update Flugzeuge_miet_kauf set bankkontoBesitzer = '" + BankKonto + "' where idFluggesellschaft = " + currentAirline.getIdFluggesellschaft()) > 0) {
      NewMessage("Aircraft Bankaccount changed");
    }

    // *** Flugzeugverknüpfungen lösen
    if (airlineFacade.executeAbfrage("update Flugzeuge_miet_kauf set idFluggesellschaft = -1 where idFluggesellschaft = " + currentAirline.getIdFluggesellschaft()) > 0) {
      NewMessage("Aircraft links deletet");
    }

    // *** FBO-Bankkonto wechseln auf Benutzer
    if (airlineFacade.executeAbfrage("update fboUserObjekte set kontoName = '" + KontoName + "', bankkonto = '" + BankKonto + "' where idfluggesellschaft = " + currentAirline.getIdFluggesellschaft()) > 0) {
      NewMessage("FBO Bankaccount changed");
    }

    // *** FBO-Verknüpfungen lösen
    if (airlineFacade.executeAbfrage("update fboUserObjekte set idfluggesellschaft = -1 where idfluggesellschaft = " + currentAirline.getIdFluggesellschaft()) > 0) {
      NewMessage("FBO links deletet");
    }

    // *** Logbuch Verknüpfungen lösen
    if (airlineFacade.executeAbfrage("update fluglogbuch set idAirline = -1 where idAirline = " + currentAirline.getIdFluggesellschaft()) > 0) {
      NewMessage("Flightlog links deletet");
    }

    // *** Manager Verknüpfungen lösen
    if (airlineFacade.executeAbfrage("update User set fluggesellschaftManagerID = -1 where fluggesellschaftManagerID = " + currentAirline.getIdFluggesellschaft()) > 0) {
      NewMessage("Manager links deletet");
    }

    // *** Bestellungen löschen
    if (airlineFacade.executeAbfrage("delete from Bestellungen where bankkonto = '" + currentAirline.getBankKonto() + "' ") > 0) {
      NewMessage("Orders deleted");
    }

//    String Betreff = "Airline wurde gelöscht: " + currentAirline.getName();
//    String MailText = "Airline wurde gelöscht: " + currentAirline.getName()
//            + "<br> Benutzer: " + currentAirline.getBesitzerName()
//            + "<br> UserID: " + UserID
//            + "<br> Anzahl konvertierter Jobs: " + AnzahlKonvertierter;
//
//    saveMail("Stefan.Klees", "Stefan.Klees", Betreff, MailText);
//    saveMail("Toffi", "Toffi", Betreff, MailText);

    // Fluggesellschaft löschen
    if (airlineFacade.executeAbfrage("delete Fluggesellschaft FROM Fluggesellschaft where idFluggesellschaft = " + currentAirline.getIdFluggesellschaft()) > 0) {
      NewMessage("Airline deletet");
    }

    NewMessage(loginMB.onSprache("Fluggesellschaften_msg_FgFluggesellschaftGeloescht"));
  }

  public void deleteAirlineDurchMOD() {

    int UserID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
    Benutzer Absender = airlineFacade.readUser(UserID);

    if (Absender != null) {
      String Betreff = "Löschung der Airline: " + currentAirline.getName() + " durch MOD";
      String text = "Die o.g. Airline wurde wegen inaktivität oder Verschuldung gelöscht";
      saveMail(Absender.getName1(), currentAirline.getBesitzerName(), Betreff, text);
      deleteAirline();
    }
  }

  public void onPersonalKaufen() {
    isLoaded = false;

  }

  public void berechtigungenManager() {

  }

  public void onWertermittlung(int fgid) {

    FGSummen = new double[2];

    FGSummen[0] = 0.0;
    FGSummen[1] = 0.0;

    if (fgid > 0) {
      List<ViewFlugzeugeMietKauf> flugzeuge = airlineFacade.getFlugzeugeFluggesellscahft(fgid);

      int zaehler = 0;
      double gesPrz = 0.0;
      double Flottenwert = 0.0;

      for (ViewFlugzeugeMietKauf fgz : flugzeuge) {
        zaehler = zaehler + 1;
        gesPrz = gesPrz + fgz.getZustand();

        Flottenwert = Flottenwert + onFlugzeugWert(fgz);

        FGSummen[0] = gesPrz / zaehler;
        FGSummen[1] = Flottenwert;

      }
    }

  }

  public double onFlugzeugWert(ViewFlugzeugeMietKauf currentFlugzeug) {

    Flugzeuge stammfg = null;

    stammfg = airlineFacade.readFlugzeugbyID(currentFlugzeug.getIdFlugzeug());

    if (stammfg != null) {
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

      return Verkaufspreis;

    }
    return 0.0;
  }

  public void onTraffikIndex(int fgid) {
    double geflogen = 0.0;
    double erzeugt = 0.0;
    traffikIndexPax = 0.0;

    for (Fluggesellschaft fg : airline) {
      if (fg.getIdFluggesellschaft() == fgid) {
        geflogen = fg.getGeflogeneJobs();
        erzeugt = fg.getErzeugteJobs();
        traffikIndexPax = 100 / erzeugt * geflogen;
        geflogen = fg.getGeflogenesCargo();
        erzeugt = fg.getErzeugtesCargo();
        traffikIndexCargo = 100 / erzeugt * geflogen;
      }
    }

  }

  public void onRefresh() {
    isLoaded = false;
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
    String Endung = "";
    GrafikFile = event.getFile();


    
    if (GrafikFile != null) {

      if (GrafikFile.getContentType().equals("image/png") || GrafikFile.getContentType().equals("image/jpeg")) {

        
        
        if (GrafikFile.getContentType().equals("image/png")) {
          Endung = "png";
        } else if (GrafikFile.getContentType().equals("image/jpeg")) {
          Endung = "jpg";
        }

        try {
          BufferedImage image;
          image = ImageIO.read(event.getFile().getInputstream());
          String Datei = CONF.getLocalWWWDir() +  "/images/FTW/usergrafiken/" + currentAirline.getIdFluggesellschaft() + "-" + currentAirline.getName() + "." + Endung;
          ImageIO.write(image, Endung, new File(Datei));

          frmAirlineLogo = CONF.getDomainURL()+ "/images/FTW/usergrafiken/" + currentAirline.getIdFluggesellschaft() + "-" + currentAirline.getName() + "." + Endung;

          saveAirline();

          isLoaded=false;
          
        } catch (IOException e) {
          System.out.println("de.klees.beans.airline.airlineBean.onGrafikUpload()" + e.getMessage());
        }

      }
    }

  }

  /*
   *********** Grafikupload ENDE
   */
  public void onRowSelect(SelectEvent event) {
    if (getSelectedAirline() != null) {

      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FluggesellschaftID", getSelectedAirline().getIdFluggesellschaft());
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FluggesellschaftBankKonto", getSelectedAirline().getBankKonto());
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FluggesellschaftBankKontoName", getSelectedAirline().getBankKontoName());
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FluggesellschaftICAO", getSelectedAirline().getIcao());
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FluggesellschaftName", getSelectedAirline().getName());

      frmAirlineLogo = getSelectedAirline().getLogoURL();

      //System.out.println("de.klees.beans.airline.airlineBean.onRowSelect() FG ID " + getSelectedAirline().getIdFluggesellschaft());
      if (getSelectedAirline().getIdFluggesellschaft().equals(ManagerID)) {

        //System.out.println("de.klees.beans.airline.airlineBean.onRowSelect() Berechtigungen auslesen");
        Fluggesellschaftmanager mg = airlineFacade.readManager(UserID, getSelectedAirline().getIdFluggesellschaft());

        setAllowBank(mg.getAllowBank());
        setAllowBankBuchhaltung(mg.getAllowBankBuchhaltung());
        setAllowBankTransfer(mg.getAllowBankTransfer());
        setAllowFluggesellschaftBearbeiten(mg.getAllowFluggesellschaftBearbeiten());
        setAllowFluggesellschaftLoeschen(mg.getAllowFluggesellschaftLoeschen());
        setAllowFlugzeuge(mg.getAllowFlugzeuge());
        setAllowFlugzeugeBearbeiten(mg.getAllowFlugzeugeBearbeiten());
        setAllowFlugzeugeKaufen(mg.getAllowFlugzeugeKaufen());
        setAllowFlugzeugeTransfer(mg.getAllowFlugzeugeTransfer());
        setAllowFlugzeugeVerkaufen(mg.getAllowFlugzeugeVerkaufen());
        setAllowManager(mg.getAllowManager());
        setAllowManagerBearbeiten(mg.getAllowManagerBearbeiten());
        setAllowManagerEinstellen(mg.getAllowManagerEinstellen());
        setAllowManagerLoeschen(mg.getAllowManagerLoeschen());
        setAllowPiloten(mg.getAllowPiloten());
        setAllowPilotenBearbeiten(mg.getAllowPilotenBearbeiten());
        setAllowPilotenEinstellen(mg.getAllowPilotenEinstellen());
        setAllowPilotenEntlassen(mg.getAllowPilotenEntlassen());
        setAllowRouten(mg.getAllowRouten());
        setAllowRoutenBearbeiten(mg.getAllowRoutenBearbeiten());
        setAllowRoutenErstellen(mg.getAllowRoutenErstellen());
        setAllowRoutenLoeschen(mg.getAllowRoutenLoeschen());

      } else {

        //System.out.println("de.klees.beans.airline.airlineBean.onRowSelect() default Berechtigung");
        setAllowBank(true);
        setAllowBankBuchhaltung(true);
        setAllowBankTransfer(true);
        setAllowFluggesellschaftBearbeiten(true);
        setAllowFluggesellschaftLoeschen(true);
        setAllowFlugzeuge(true);
        setAllowFlugzeugeBearbeiten(true);
        setAllowFlugzeugeKaufen(true);
        setAllowFlugzeugeTransfer(true);
        setAllowFlugzeugeVerkaufen(true);
        setAllowManager(true);
        setAllowManagerBearbeiten(true);
        setAllowManagerEinstellen(true);
        setAllowManagerLoeschen(true);
        setAllowPiloten(true);
        setAllowPilotenBearbeiten(true);
        setAllowPilotenEinstellen(true);
        setAllowPilotenEntlassen(true);
        setAllowRouten(true);
        setAllowRoutenBearbeiten(true);
        setAllowRoutenErstellen(true);
        setAllowRoutenLoeschen(true);

      }

    }

  }

  public void onRowUnselect(UnselectEvent event) {

  }

  public String onBack() {
    return "fluggesellschaft.xhtml?faces-redirect=true";
  }

  private void NewMessage(String neueNachricht) {
    FacesMessage msg = new FacesMessage(neueNachricht);
    FacesContext.getCurrentInstance().addMessage(null, msg);
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

    airlineFacade.createBankbuchung(newBuchung);

  }

  /*
  ****************** Setter and Getter
   */
  private String getNeueKontoNummer() {
    String KontoNummer = "";
    int anzahl = 1000000;
    int Nummer;

    Nummer = (int) (Math.random() * anzahl) + (anzahl * 2);
    KontoNummer = "600-" + String.valueOf(Nummer);

    while (airlineFacade.ifExistBankKonto(KontoNummer)) {
      Nummer = (int) (Math.random() * anzahl) + (anzahl * 2);
      KontoNummer = "600-" + String.valueOf(Nummer);
    }
    return KontoNummer;
  }

  public Fluggesellschaft getSelectedAirline() {
    return currentAirline;
  }

  public void setSelectedAirline(Fluggesellschaft SelectedAirline) {
    this.currentAirline = SelectedAirline;
  }

  public String getSuchText() {
    return SuchText;
  }

  public void setSuchText(String SuchText) {
    this.SuchText = SuchText;
  }

  public Double getKaufPreis() {
    return kaufPreis;
  }

  public void setKaufPreis(Double kaufPreis) {
    this.kaufPreis = kaufPreis;
  }

  public int getAirlineSize() {
    return AirlineSize;
  }

  public void setAirlineSize(int AirlineSize) {
    this.AirlineSize = AirlineSize;
  }

  public String getAirlineName() {
    return AirlineName;
  }

  public void setAirlineName(String AirlineName) {
    this.AirlineName = AirlineName;
  }

  public String getAirlineBankAccountName() {
    return AirlineBankAccountName;
  }

  public void setAirlineBankAccountName(String AirlineBankAccountName) {
    this.AirlineBankAccountName = AirlineBankAccountName;
  }

  public String getHomeICAO() {
    return HomeICAO;
  }

  public void setHomeICAO(String HomeICAO) {
    this.HomeICAO = HomeICAO;
  }

  public int getAirlineAngestellte() {
    return AirlineAngestellte;
  }

  public void setAirlineAngestellte(int AirlineAngestellte) {
    this.AirlineAngestellte = AirlineAngestellte;
  }

  public String getAirlineSizeText() {
    return AirlineSizeText;
  }

  public String getAirlineLogo() {
    return AirlineLogo;
  }

  public void setAirlineLogo(String AirlineLogo) {
    this.AirlineLogo = AirlineLogo;
  }

  public int getMengePersonalEinkauf() {
    return MengePersonalEinkauf;
  }

  public void setMengePersonalEinkauf(int MengePersonalEinkauf) {
    this.MengePersonalEinkauf = MengePersonalEinkauf;
  }

  public String getICAOCode() {
    return ICAOCode;
  }

  public void setICAOCode(String ICAOCode) {
    this.ICAOCode = ICAOCode;
  }

  /// ******************* Setter and Getter Berechtigungen
  public boolean isAllowBank() {
    return allowBank;
  }

  public void setAllowBank(boolean allowBank) {
    this.allowBank = allowBank;
  }

  public boolean isAllowBankTransfer() {
    return allowBankTransfer;
  }

  public void setAllowBankTransfer(boolean allowBankTransfer) {
    this.allowBankTransfer = allowBankTransfer;
  }

  public boolean isAllowBankBuchhaltung() {
    return allowBankBuchhaltung;
  }

  public void setAllowBankBuchhaltung(boolean allowBankBuchhaltung) {
    this.allowBankBuchhaltung = allowBankBuchhaltung;
  }

  public boolean isAllowFluggesellschaftBearbeiten() {
    return allowFluggesellschaftBearbeiten;
  }

  public void setAllowFluggesellschaftBearbeiten(boolean allowFluggesellschaftBearbeiten) {
    this.allowFluggesellschaftBearbeiten = allowFluggesellschaftBearbeiten;
  }

  public boolean isAllowFluggesellschaftLoeschen() {
    return allowFluggesellschaftLoeschen;
  }

  public void setAllowFluggesellschaftLoeschen(boolean allowFluggesellschaftLoeschen) {
    this.allowFluggesellschaftLoeschen = allowFluggesellschaftLoeschen;
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

  public boolean isAllowManager() {
    return allowManager;
  }

  public void setAllowManager(boolean allowManager) {
    this.allowManager = allowManager;
  }

  public boolean isAllowManagerBearbeiten() {
    return allowManagerBearbeiten;
  }

  public void setAllowManagerBearbeiten(boolean allowManagerBearbeiten) {
    this.allowManagerBearbeiten = allowManagerBearbeiten;
  }

  public boolean isAllowManagerEinstellen() {
    return allowManagerEinstellen;
  }

  public void setAllowManagerEinstellen(boolean allowManagerEinstellen) {
    this.allowManagerEinstellen = allowManagerEinstellen;
  }

  public boolean isAllowManagerLoeschen() {
    return allowManagerLoeschen;
  }

  public void setAllowManagerLoeschen(boolean allowManagerLoeschen) {
    this.allowManagerLoeschen = allowManagerLoeschen;
  }

  public boolean isAllowPiloten() {
    return allowPiloten;
  }

  public void setAllowPiloten(boolean allowPiloten) {
    this.allowPiloten = allowPiloten;
  }

  public boolean isAllowPilotenBearbeiten() {
    return allowPilotenBearbeiten;
  }

  public void setAllowPilotenBearbeiten(boolean allowPilotenBearbeiten) {
    this.allowPilotenBearbeiten = allowPilotenBearbeiten;
  }

  public boolean isAllowPilotenEinstellen() {
    return allowPilotenEinstellen;
  }

  public void setAllowPilotenEinstellen(boolean allowPilotenEinstellen) {
    this.allowPilotenEinstellen = allowPilotenEinstellen;
  }

  public boolean isAllowPilotenEntlassen() {
    return allowPilotenEntlassen;
  }

  public void setAllowPilotenEntlassen(boolean allowPilotenEntlassen) {
    this.allowPilotenEntlassen = allowPilotenEntlassen;
  }

  public boolean isAllowRouten() {
    return allowRouten;
  }

  public void setAllowRouten(boolean allowRouten) {
    this.allowRouten = allowRouten;
  }

  public boolean isAllowRoutenBearbeiten() {
    return allowRoutenBearbeiten;
  }

  public void setAllowRoutenBearbeiten(boolean allowRoutenBearbeiten) {
    this.allowRoutenBearbeiten = allowRoutenBearbeiten;
  }

  public boolean isAllowRoutenErstellen() {
    return allowRoutenErstellen;
  }

  public void setAllowRoutenErstellen(boolean allowRoutenErstellen) {
    this.allowRoutenErstellen = allowRoutenErstellen;
  }

  public boolean isAllowRoutenLoeschen() {
    return allowRoutenLoeschen;
  }

  public void setAllowRoutenLoeschen(boolean allowRoutenLoeschen) {
    this.allowRoutenLoeschen = allowRoutenLoeschen;
  }

  public double[] getFGSummen() {
    return FGSummen;
  }

  public void setFGSummen(double[] FGSummen) {
    this.FGSummen = FGSummen;
  }

  public double getTraffikIndexPax() {
    return traffikIndexPax;
  }

  public void setTraffikIndexPax(double traffikIndexPax) {
    this.traffikIndexPax = traffikIndexPax;
  }

  public double getTraffikIndexCargo() {
    return traffikIndexCargo;
  }

  public void setTraffikIndexCargo(double traffikIndexCargo) {
    this.traffikIndexCargo = traffikIndexCargo;
  }

  public double getBankKontoStand(String Bankkonto) {
    return airlineFacade.BankSaldo(Bankkonto);
  }

  public String getFrmBonusName() {
    return frmBonusName;
  }

  public void setFrmBonusName(String frmBonusName) {
    this.frmBonusName = frmBonusName;
  }

  public double getFrmBonusProzent() {
    return frmBonusProzent;
  }

  public void setFrmBonusProzent(double frmBonusProzent) {
    this.frmBonusProzent = frmBonusProzent;
  }

  public int getFrmBonusZeit() {
    return frmBonusZeit;
  }

  public void setFrmBonusZeit(int frmBonusZeit) {
    this.frmBonusZeit = frmBonusZeit;
  }

  public String getFrmBonusUrlAbzeichen() {
    return frmBonusUrlAbzeichen;
  }

  public void setFrmBonusUrlAbzeichen(String frmBonusUrlAbzeichen) {
    this.frmBonusUrlAbzeichen = frmBonusUrlAbzeichen;
  }

  public FluggesellschaftBonusSystem getCurrentBonus() {
    return currentBonus;
  }

  public void setCurrentBonus(FluggesellschaftBonusSystem currentBonus) {
    this.currentBonus = currentBonus;
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
    airlineFacade.saveUserMail(nm);

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
      airlineFacade.saveUserMail(nm);
    }

  }

  private Benutzer getUserDaten(String BenutzerName) {
    return airlineFacade.findUser(BenutzerName);
  }

  public String getFrmAirlineLogo() {
    return frmAirlineLogo;
  }

  public void setFrmAirlineLogo(String frmAirlineLogo) {
    this.frmAirlineLogo = frmAirlineLogo;
  }

}
