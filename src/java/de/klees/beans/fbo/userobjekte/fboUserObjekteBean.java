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

package de.klees.beans.fbo.userobjekte;

import de.klees.beans.system.CONF;
import de.klees.beans.system.loginMB;
import de.klees.data.Airport;
import de.klees.data.Assignement;
import de.klees.data.Bank;
import de.klees.data.Bestellungen;
import de.klees.data.FboObjekte;
import de.klees.data.FboUserObjekte;
import de.klees.data.Feinabstimmung;
import de.klees.data.Fluggesellschaft;
import de.klees.data.Fluggesellschaftmanager;
import de.klees.data.Flugrouten;
import de.klees.data.Flugzeuge;
import de.klees.data.Hangarbelegung;
import de.klees.data.Kostenstellen;
import de.klees.data.Lagerbestellungservicehangar;
import de.klees.data.Lagerservicehangar;
import de.klees.data.Lagerservicehangarumsatz;
import de.klees.data.Servicehangartermine;
import de.klees.data.Benutzer;
import de.klees.data.views.ViewBestellungenDetail;
import de.klees.data.views.ViewFBOUserObjekte;
import de.klees.data.views.ViewTankstellenInFTW;
import de.klees.data.views.ViewuebersichtServiceHangar;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author Stefan Klees
 */
public class fboUserObjekteBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<ViewFBOUserObjekte> fboUserList;
  private List<ViewFBOUserObjekte> fboUserListByIcao;
  private List<ViewFBOUserObjekte> fboUserListByObjekt;
  private List<ViewuebersichtServiceHangar> listeServiceHangars;
  private List<ViewFBOUserObjekte> ListUserFbos;

  private ViewFBOUserObjekte currentFboUser;
  private AngebotsListeFuel currentBestellung;
  private ViewFBOUserObjekte selectedTankstelle;
  private Feinabstimmung config;
  private Lagerbestellungservicehangar selectedHangarBestellung;

  private boolean isLoaded;

  private final int UserID;
  private final Calendar c = Calendar.getInstance();

  private String frmBezeichnung;
  private double frmPreisArbeitseinheit;
  private double frmPreisAVGAS;
  private double frmPreisJETA;
  private boolean frmTankstelle;
  private boolean frmSpritlager;
  private String frmTankstelleGrafik;
  private boolean frmServiceHangar;
  private double frmTerminalAbfertigungsGebuehr;
  private int frmKostenStelle;
  private int frmKlasse;
  private int frmfluggesellschaftID;

  private int frmBestellmengeFuel;
  private int frmFuelArt;

  private int frmFassgroesseKG;
  private int frmFassanzahl;
  private double frmFassTransportPreis;
  private String frmFassZielflughafen;
  private String frmFassTreibstoffart;
  private int frmFassFboID;

  private String frmBankKonto;
  private String frmKontoName;
  private boolean zeigeBankkonto;

  // *** Filter Felder
  private String fltObjekt;
  private String fltIcao;

  private String UserName;
  private String UserKonto;

  private FboObjekte selectedUmbau;

  private boolean abrechnungsFehler;
  private String abrechnungsFehlerText;

  private double GesamtSumme;
  private double SummeTankstellen;
  private double SummeFBO;
  private double SummeBusinessLounge;
  private double SummeTerminals;
  private double SummeRoutenObjekte;

  private double SummeTankstellenAufschlag;
  private double SummeFBOAufschlag;
  private double SummeBusinessLoungeAufschlag;
  private double SummeTerminalsAufschlag;
  private double SummeRoutenObjekteAufschlag;

  private String AirportName;

  private int Zaehler;

  private int ManagerID;

  private boolean allowFBO;
  private boolean allowFBOBearbeiten;
  private boolean allowFBOHinzufuegen;
  private boolean allowFBOLoeschen;
  private boolean allowFBOTankstelleVerwalten;

  private boolean allowFluggesellschaftLoeschen;

  private boolean umbauGestartet;

  // Variablen für Service-Pack Bestellung
  private int bstPackFlugzeugID;
  private String bstPackStandortICAO;
  private String bstPackStandortName;
  private int bstPackEinkauf;
  private int bstPackMaxVerkauf;
  private Date bstPackLieferzeit;
  private int bstPackLieferkosten;
  private int bstPackEntfernung;
  private int bstPackFTWPreis;
  private int bstPackAnzahl;
  private double bstPackProzent;
  private int bstPackArt;
  private String bstPackPaketName;
  private String bstPackFlugzeugBild;
  private int bstPackAnlieferID;
  private String bstPackAnlieferBankKonto;
  private double bstPackAnlieferPreis;
  private int bstPackAnlieferUserID;
  private boolean bstPackHandelOK;
  private Lagerservicehangar currentLagerItem;

  // Variablen Scheduler Service Hangar
  private ScheduleModel eventModel = new DefaultScheduleModel();
  private ScheduleEvent event = new DefaultScheduleEvent();
  private boolean isLoadedScheduler;

  private final DecimalFormat nf = new DecimalFormat("#,##0.00");
  private final DecimalFormat nfohneKomma = new DecimalFormat("#,##0");
  private final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");

  @EJB
  ViewFBOUserObjekteFacade facadeFBOUser;

  public fboUserObjekteBean() {
    isLoaded = false;
    isLoadedScheduler = false;
    umbauGestartet = false;
    ManagerID = 0;
    UserID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
    UserName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName");
    UserKonto = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("BankKonto");
    fltIcao = "";
    fltObjekt = "";
    allowFBOHinzufuegen = true;
    bstPackAnzahl = 1;
    bstPackArt = 1;
    bstPackProzent = 2.5;

  }

  public List<ViewFBOUserObjekte> getFboUser() {
    if (!isLoaded) {
      int fgid = facadeFBOUser.readUser(UserID).getFluggesellschaftManagerID();

      int fgUserID = -1;

      if (fgid > 0) {
        Fluggesellschaftmanager mg = facadeFBOUser.readManager(UserID, fgid);
        if (mg.getAllowFBO()) {
          try {
            fgUserID = facadeFBOUser.readFg(fgid).getUserid();
          } catch (NullPointerException e) {
            fgUserID = -1;
          }

        }
      }

      fboUserList = facadeFBOUser.findAllFBOByUser(UserID, fgUserID, "%" + fltIcao + "%", "%" + fltObjekt + "%");

      isLoaded = true;
    }
    return fboUserList;
  }

  public List<ViewFBOUserObjekte> getSpritlager() {
    if (currentFboUser != null) {
      return facadeFBOUser.findAllSpritlager(currentFboUser.getIdfluggesellschaft());
    }
    return null;
  }

  public List<ViewFBOUserObjekte> getFboUserByIcao() {

    int fgid = facadeFBOUser.readUser(UserID).getFluggesellschaftManagerID();

    int fgUserID = -1;

    if (fgid > 0) {
      Fluggesellschaftmanager mg = facadeFBOUser.readManager(UserID, fgid);
      if (mg.getAllowFBO()) {
        try {
          fgUserID = facadeFBOUser.readFg(fgid).getUserid();
        } catch (NullPointerException e) {
          fgUserID = -1;
        }

      }
    }

    fboUserListByIcao = facadeFBOUser.findAllFBOByUserGroupByIcao(UserID, fgUserID);

    return fboUserListByIcao;
  }

  public List<ViewFBOUserObjekte> getFboUserByObjekt() {

    int fgid = facadeFBOUser.readUser(UserID).getFluggesellschaftManagerID();

    int fgUserID = -1;

    if (fgid > 0) {
      Fluggesellschaftmanager mg = facadeFBOUser.readManager(UserID, fgid);
      if (mg.getAllowFBO()) {
        try {
          fgUserID = facadeFBOUser.readFg(fgid).getUserid();
        } catch (NullPointerException e) {
          fgUserID = -1;
        }

      }
    }

    fboUserListByObjekt = facadeFBOUser.findAllFBOByUserGroupByObjekt(UserID, fgUserID);

    return fboUserListByObjekt;
  }

  public List<ViewFBOUserObjekte> getTankstellen() {
    return facadeFBOUser.findTankstellenByICAO((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Assignment_ICAO"));
  }

  public List<ViewFBOUserObjekte> getAlleTankstellen() {

    int fgid = facadeFBOUser.readUser(UserID).getFluggesellschaftManagerID();

    int fgUserID = -1;

    if (fgid > 0) {
      Fluggesellschaftmanager mg = facadeFBOUser.readManager(UserID, fgid);
      if (mg.getAllowFBOTankstelleVerwalten()) {
        try {
          fgUserID = facadeFBOUser.readFg(fgid).getUserid();
        } catch (NullPointerException e) {
          fgUserID = -1;
        }

      }
    }

    return facadeFBOUser.findAllTankstellen(UserID, fgUserID);
  }

  public List<ViewFBOUserObjekte> getTankstellenZiel() {
    return facadeFBOUser.findTankstellenByICAO((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Assignment_ZIEL_ICAO"));
  }

  public List<ViewFBOUserObjekte> getServiceHangars() {
    return facadeFBOUser.getAlleServicehangars();
  }

  public List<FboObjekte> getObjekteDerArt() {

    if (currentFboUser != null) {
      Airport airport = facadeFBOUser.getAirportInfo(currentFboUser.getIcao());
      if (airport != null) {
        frmKlasse = airport.getKlasse();
      } else {
        NewMessage(loginMB.onSprache("FBOManagement_msg_FlughafenNichtGefunden"));
        frmKlasse = 0;
      }
    }

    try {
      // 1 FBO, 2 Abfertigungsterminal, 3 Tankstelle, 4 Servicehangar
      if (currentFboUser.getTankstelle()) {
        return facadeFBOUser.findAllObjekteDerArt(3, frmKlasse);
      } else if (currentFboUser.getFbo()) {
        return facadeFBOUser.findAllObjekteDerArt(1, frmKlasse);
      } else if (currentFboUser.getAbfertigungsTerminal()) {
        return facadeFBOUser.findAllObjekteDerArt(2, frmKlasse);
      } else if (currentFboUser.getServicehangar()) {
        return facadeFBOUser.findAllObjekteDerArt(4, frmKlasse);
      }

    } catch (NullPointerException e) {
    }
    //Dummy liefert keine Datensaetze zurück
    return facadeFBOUser.findAllObjekteDerArt(10, 100);
  }

  public List<ViewBestellungenDetail> getBestellungen() {
    int fgid = facadeFBOUser.readUser(UserID).getFluggesellschaftManagerID();

    int fgUserID = -1;

    if (fgid > 0) {
      Fluggesellschaftmanager mg = facadeFBOUser.readManager(UserID, fgid);
      if (mg.getAllowFBOTankstelleVerwalten()) {
        try {
          fgUserID = facadeFBOUser.readFg(fgid).getUserid();
        } catch (NullPointerException e) {
          fgUserID = -1;
        }

      }
    }

    return facadeFBOUser.findBestellungenByUserID(UserID, fgUserID);
  }

  public List<ViewBestellungenDetail> getBestellungIcao() {
    if (currentFboUser != null) {
      return facadeFBOUser.findBestellungenByIcao(currentFboUser.getIcao(), currentFboUser.getIdUser());
    } else {
      return facadeFBOUser.findBestellungenByIcao("", -1);
    }

  }

  public List<Assignement> getFaesserBereitgestellt() {
    if (currentFboUser != null) {
      return facadeFBOUser.getAusstehendeSpritFaesser(currentFboUser.getIduserfbo());
    }
    return null;
  }

  public List<Fluggesellschaft> getKontenFluggesellschaft() {

    int fgid = facadeFBOUser.readUser(UserID).getFluggesellschaftManagerID();
    Fluggesellschaftmanager mg = null;

    if (fgid > 0) {
      mg = facadeFBOUser.readManager(UserID, fgid);
    }

    try {
      if (mg != null) {
        if (mg.getAllowFBO()) {
          //System.out.println("de.klees.beans.fbo.userobjekte.fboUserObjekteBean.getKontenFluggesellschaft() FGID " + fgid);
          return facadeFBOUser.getAllFGKonten(UserID, fgid);
        }
      }
      return facadeFBOUser.getAllFGKonten(UserID, -1);
    } catch (NullPointerException e) {
      return null;
    }
  }

  public List<ViewTankstellenInFTW> getFTWTankstellen() {
    return facadeFBOUser.getFTWTankstellen();
  }

  public List<String> getMeineFluggesellschaftenFuerManager() {
    int fgid = facadeFBOUser.readUser(UserID).getFluggesellschaftManagerID();

    Fluggesellschaftmanager mg = null;

    if (fgid > 0) {
      mg = facadeFBOUser.readManager(UserID, fgid);
    }

    try {
      if (mg != null) {
        if (mg.getAllowFBO()) {
          return facadeFBOUser.MeineFluggesellschaften(UserID, fgid);
        }
      }
      return facadeFBOUser.MeineFluggesellschaften(UserID, -1);
    } catch (NullPointerException e) {
      return null;
    }

  }

  public List<Kostenstellen> getKostenstellenFuerFluggesellschaft() {

    if (currentFboUser.getIdfluggesellschaft() > 0) {
      return facadeFBOUser.getKostenstellen(facadeFBOUser.readFg(currentFboUser.getIdfluggesellschaft()).getUserid());
    }
    return null;
  }

  public List<lqList> getLiquiditaetsplanung() {

    ArrayList<lqList> lqlist = new ArrayList<>();

    if (frmfluggesellschaftID <= 0 && getFboUser().size() > 0) {
      frmfluggesellschaftID = getFboUser().get(0).getIdfluggesellschaft();
    }

    ListUserFbos = facadeFBOUser.findAllFBOBFuerZahlungen(UserID, frmfluggesellschaftID);

    lqlist.clear();

    double Betrag = 0.0;

    for (int i = 0; i < ListUserFbos.size(); i++) {

      String Icao = ListUserFbos.get(i).getIcao();

      if (i + 1 < ListUserFbos.size()) {

        if (ListUserFbos.get(i).getFaelligkeitNaechsteMiete().equals(ListUserFbos.get(i + 1).getFaelligkeitNaechsteMiete())) {

          Betrag = Betrag + (ListUserFbos.get(i).getMietPreis() * MietaufschlagFaktor(Icao));

        } else {
          Betrag = Betrag + (ListUserFbos.get(i).getMietPreis() * MietaufschlagFaktor(Icao));
          lqList lq = new lqList();
          lq.setBetrag(Betrag);
          lq.setBezeichnung("Zu zahlen am :");
          lq.setDatum(ListUserFbos.get(i).getFaelligkeitNaechsteMiete());

          lqlist.add(lq);
          Betrag = 0.0;
        }

      } else {
        try {
          // Den letzten Eintrag verarbeiten
          if (ListUserFbos.get(i).getFaelligkeitNaechsteMiete().equals(ListUserFbos.get(i - 1).getFaelligkeitNaechsteMiete())) {
            Betrag = Betrag + (ListUserFbos.get(i).getMietPreis() * MietaufschlagFaktor(Icao));
            lqList lq = new lqList();
            lq.setBetrag(Betrag);
            lq.setBezeichnung("Zu zahlen am :");
            lq.setDatum(ListUserFbos.get(i).getFaelligkeitNaechsteMiete());
            lqlist.add(lq);
            Betrag = 0.0;
          } else {
            Betrag = 0.0;
            Betrag = Betrag + (ListUserFbos.get(i).getMietPreis() * MietaufschlagFaktor(Icao));
            lqList lq = new lqList();
            lq.setBetrag(Betrag);
            lq.setBezeichnung("Zu zahlen am :");
            lq.setDatum(ListUserFbos.get(i).getFaelligkeitNaechsteMiete());

            lqlist.add(lq);
          }
        } catch (ArrayIndexOutOfBoundsException e) {
        }

      }

    }

    return lqlist;
  }

  public List<Flugzeuge> getServicePackList() {
    return facadeFBOUser.getListeServicePacks();
  }

  public void onTankstelleZuweisenFuerBestellung() {

    setSelectedFboUser(selectedTankstelle);
    frmTankstelle = true;

  }

  public void onChangeIcaoOrObjekt() {
    isLoaded = false;
  }

  public void onKuendigung() {

    boolean Kuendigung = true;
    boolean belegung = true;

    Feinabstimmung cf = facadeFBOUser.readConfig();

    if (currentFboUser.getFbo()) {

      // gibt es noch Objekte die zur FBO gehören
      if (facadeFBOUser.istFBOLeer(currentFboUser.getIdUser(), currentFboUser.getIcao()) > 1) {
        NewMessage(loginMB.onSprache("FBOManagement_msg_FBORaeumen"));
        Kuendigung = false;
      } else if (facadeFBOUser.istFBOLeer(currentFboUser.getIdUser(), currentFboUser.getIcao()) == 1) {
        Kuendigung = true;
      }

    }

    /*
    *********** Terminal kann erst nach 31 Tagen gekündigt werden
     */
    if (currentFboUser.getAbfertigungsTerminal()) {
//      long gemietetam = currentFboUser.getMietbeginn().getTime();
//      MietTage = new Date().getTime() - gemietetam;
//      long tage = MietTage / 1000 / 60 / 60 / 24;

      if (!isKuendigungsZeitOK(currentFboUser.getMietbeginn(), 1)) {
        Kuendigung = false;
      } else {
        Kuendigung = true;
      }
    }

    // Pruefung Service Hangar
    if (currentFboUser.getServicehangar()) {
      //Belegung pruefen
      if (facadeFBOUser.istHangarBelegt(currentFboUser.getIduserfbo())) {
        NewMessage("Service Hangar kann nicht gelöscht werden, Hangar ist belegt.");
        belegung = true;
        Kuendigung = false;
      } else {
        belegung = false;
        Kuendigung = true;
      }

      if (Kuendigung) {
        if (!isKuendigungsZeitOK(currentFboUser.getMietbeginn(), 180)) {
          Kuendigung = false;
        }
      }
    }

    if (Kuendigung) {

      // Preiserhöhungen ermitteln
      Airport airport = null;
      double aufschlag = 0.0;

      airport = facadeFBOUser.getAirportInfo(currentFboUser.getIcao());

      if (airport != null) {
        //double prozente = CONF.getFlughafenAufschlag(airport.getKlasse());
        double prozente = 10.0;
        long exFbo = facadeFBOUser.wievieleFBOAmFlughafen(currentFboUser.getIcao());

        if (exFbo > 0) {
          exFbo = exFbo - 1;
        }

        aufschlag = prozente * exFbo;

      }

      double Miete = 0.0;

      // Abzurechnende Miettage berechnen
      long difMillisekunden = c.getTime().getTime() - currentFboUser.getLetzteMietzahlung().getTime();
      long MietTage = difMillisekunden / 1000 / 60 / 60 / 24;

      // Mietrückzahlung berechnen
      Miete = currentFboUser.getMietPreis() - ((currentFboUser.getMietPreis() / 30) * MietTage);

      //**** Bankbuchung Rückerstattung Miete
      UserName = currentFboUser.getKontoName();
      UserKonto = currentFboUser.getBankKonto();

      double betrag = Miete;

      //aktuellen Mietaufschlag und Mietzeit berücksichtigen
      if (getMietTage(currentFboUser.getMietbeginn().getTime()) > 30) {
        betrag = (betrag * ((100 + aufschlag) / 100));
      } else {
        aufschlag = 0;
      }

      String VerwendungsZweck = loginMB.onSprache("FBOManagement_msg_Bank_Rueckerstattung") + " - " + currentFboUser.getIcao() + " - " + currentFboUser.getName()
              + "</br> " + loginMB.onSprache("FBOManagement_msg_Bank_Resttage") + " : " + (30 - Math.round(MietTage))
              + "</br>" + loginMB.onSprache("FBOManagement_msg_Bank_Mietaufschlag") + Math.floor(aufschlag) + " %";

      SaveBankbuchung(UserKonto, UserName, "500-1000001", "**** FTW BANK *****", new Date(), betrag, UserKonto, UserName,
              new Date(), VerwendungsZweck, currentFboUser.getIdUser(), -1, -1, -1, -1, -1, -1, currentFboUser.getIcao(), currentFboUser.getIduserfbo(), -1, currentFboUser.getKostenstelle(), -1);

      //
      // Tankstelle Bestände abrechnen
      //
      if (currentFboUser.getTankstelle()) {
        //Gutschrift Resttreibstoff
        betrag = (currentFboUser.getEinkaufsPreisJETA() * currentFboUser.getBestandJETAkg()
                + (currentFboUser.getEinkaufsPreisAVGAS() * currentFboUser.getBestandAVGASkg()));

        if (betrag != 0) {
          //Entleerung berechnen 0,50 € pro KG
          double pumpe = (currentFboUser.getBestandJETAkg() + currentFboUser.getBestandAVGASkg()) * 0.50;

          VerwendungsZweck = loginMB.onSprache("FBOManagement_msg_Bank_RueckerstattungTreibstoff") + " - " + currentFboUser.getIcao() + " - " + currentFboUser.getName()
                  + "</br> JETA : " + currentFboUser.getBestandJETAkg() + " (kg)"
                  + "</br> AVGAS : " + currentFboUser.getBestandAVGASkg() + " (kg)"
                  + "</br> " + loginMB.onSprache("FBOManagement_msg_Bank_Pumpgebuehren") + " : " + nf.format(pumpe);

          SaveBankbuchung(UserKonto, UserName, "500-1000003", "**** FTW OIL *****", new Date(), betrag, UserKonto, UserName,
                  new Date(), VerwendungsZweck, currentFboUser.getIdUser(), -1, -1, -1, -1, -1, -1, "", -1, -1, -1, -1);
        }
      }

      //
      // Spritlager Bestände abrechnen
      //
      if (currentFboUser.getSpritlager()) {
        //Gutschrift Resttreibstoff
        betrag = (cf.getPreisJETAkg() * currentFboUser.getBestandJETAkg()
                + (cf.getPreisAVGASkg() * currentFboUser.getBestandAVGASkg()));

        if (betrag != 0) {
          //Entleerung berechnen 0,50 € pro KG
          double pumpe = (currentFboUser.getBestandJETAkg() + currentFboUser.getBestandAVGASkg()) * 0.50;

          VerwendungsZweck = loginMB.onSprache("FBOManagement_msg_Bank_RueckerstattungTreibstoff") + " - " + currentFboUser.getIcao() + " - " + currentFboUser.getName()
                  + "</br> JETA : " + currentFboUser.getBestandJETAkg() + " (kg)"
                  + "</br> AVGAS : " + currentFboUser.getBestandAVGASkg() + " (kg)"
                  + "</br> " + loginMB.onSprache("FBOManagement_msg_Bank_Pumpgebuehren") + " : " + nf.format(pumpe);

          SaveBankbuchung(UserKonto, UserName, "500-1000003", "**** FTW OIL *****", new Date(), betrag, UserKonto, UserName,
                  new Date(), VerwendungsZweck, currentFboUser.getIdUser(), -1, -1, -1, -1, -1, -1, "", -1, -1, -1, -1);
        }
        
        //Bereitstehende Spritfässer löschen
        facadeFBOUser.deleteAssignmentsUeberTerminalID(currentFboUser.getIduserfbo());
        
      }

      // Terminal Löschen, für evlt. bestehende Aufträge Regress nehmen
      if (currentFboUser.getAbfertigungsTerminal()) {

        // Ermitteln der zu löschenden Routen 
        List<Flugrouten> routen = facadeFBOUser.getFlugroutenUberTerminalID(currentFboUser.getIduserfbo());

        for (Flugrouten route : routen) {
          double Betrag = 0.0;
          //ermitteln der noch vorhandenen Aufträge für diese Route
          //Vorhandene Aufträge in Regress nehemn
          for (Assignement assi : facadeFBOUser.listAuftraegeUeberTerminalID(route.getIdUserFBO())) {
            Betrag = Betrag + assi.getPay();
          }
          //Buchung des Regress
          if (Betrag > 0) {
            Fluggesellschaft fg = facadeFBOUser.readFg(route.getIdFluggesellschaft());
            VerwendungsZweck = "Terminal has been deleted - claims for recourse: " + route.getName();
            String Auftraggeber = "**** FTW BANK *****";
            String AuftraggeberKonto = "500-1000001";
            String EmpfaengerKonto = fg.getBankKonto();
            String Empfaenger = fg.getBankKontoName();

            Betrag = (Betrag * 0.5) * -1;

            SaveBankbuchung(EmpfaengerKonto, Empfaenger, AuftraggeberKonto, Auftraggeber,
                    new Date(), Betrag, EmpfaengerKonto, Empfaenger, new Date(), VerwendungsZweck, fg.getUserid(), -1, -1, -1, -1, -1, -1, "", -1, -1, -1, -1);

          }

          //Löschen der Aufträge
          facadeFBOUser.deleteAssignmentsUeberTerminalID(route.getIdUserFBO());
          //Löschen der Route
          facadeFBOUser.deleteRoutenUeberTerminalID(route.getIdUserFBO());
        }

      }

      // Service Hangar und Lager löschen
      if (currentFboUser.getServicehangar()) {
        
        //Löschen der Abhängigkeiten zum Servicehangar
        facadeFBOUser.deleteServiceHangar(currentFboUser.getIduserfbo());
        
        //Löschen des Servicehangars
        facadeFBOUser.onKuendigung(currentFboUser.getIduserfbo());
        
        NewMessage("Service Hangar wurde gelöscht.");
        Kuendigung = false;
      }

      if (Kuendigung) {
        // Löschen des Objekts
        facadeFBOUser.onKuendigung(currentFboUser.getIduserfbo());
        NewMessage(loginMB.onSprache("FBOManagement_msg_FBOObjektGeloescht"));
      }

      setSelectedFboUser(null);
      isLoaded = false;
    }

  }

  public long getMietTage(long LongDatum) {
    long MietTage = 0;
    MietTage = new Date().getTime() - LongDatum;
    long tage = MietTage / 1000 / 60 / 60 / 24;

    return tage;
  }

  private boolean isKuendigungsZeitOK(Date beginn, long tage) {
    long mietbeginn = beginn.getTime();
    long aktdatum = new Date().getTime();
    long mietzeit = aktdatum - mietbeginn;
    long mindestlaufzeit = tage * 24 * 60 * 60 * 1000L;

    if (mietzeit >= mindestlaufzeit) {
      return true;
    }
    NewMessage("Minimum term of " + tage + " days not reached! Next possible appointment: " + df.format(new Date(mietbeginn + mindestlaufzeit)));
    return false;
  }

  public void onEdit() {
    config = facadeFBOUser.readConfig();
    boolean PreisOK = false;

    if (frmPreisAVGAS > (config.getPreisAVGASkg() + 1) | frmPreisJETA > (config.getPreisJETAkg() + 1)) {
      PreisOK = false;
      NewMessage(loginMB.onSprache("FBOManagement_msg_MaxSpritpreis"));
    } else {
      PreisOK = true;
    }

    if (PreisOK) {
      if (frmTerminalAbfertigungsGebuehr > 5.00 | frmTerminalAbfertigungsGebuehr < 0) {
        PreisOK = false;
        NewMessage(loginMB.onSprache("FBOManagement_msg_MaxTerminalGebuehr"));
      }
    }

    if (PreisOK) {
      FboUserObjekte editFbo = facadeFBOUser.findByFboID(currentFboUser.getIduserfbo());
      editFbo.setName(frmBezeichnung);

      editFbo.setPreisAVGAS(frmPreisAVGAS);
      editFbo.setPreisJETA(frmPreisJETA);

      editFbo.setPreisArbeitseinheit(frmPreisArbeitseinheit);
      editFbo.setBankkonto(frmBankKonto);
      editFbo.setKontoName(facadeFBOUser.getBankKontoName(frmBankKonto));
      editFbo.setTerminalGebuehrInProzent(frmTerminalAbfertigungsGebuehr);
      editFbo.setKostenstelle(frmKostenStelle);
      editFbo.setIdfluggesellschaft(frmfluggesellschaftID);
      editFbo.setGrafikLink(frmTankstelleGrafik);

      facadeFBOUser.onEditUserFBO(editFbo);
      isLoaded = false;
      NewMessage(loginMB.onSprache("FBOManagement_msg_FBOObjektGespeichert"));

    } else {
      NewMessage(loginMB.onSprache("FBOManagement_msg_nichtGespeichert"));
    }

  }

  public void onFuelBestellung() {

    UserName = currentFboUser.getKontoName();
    UserKonto = currentFboUser.getBankKonto();

    Bestellungen newOrder = new Bestellungen();

    if (currentBestellung.getFuelArt().equals("AVGAS 100LL")) {
      newOrder.setArt(1);
    } else if (currentBestellung.getFuelArt().equals("JETA")) {
      newOrder.setArt(2);
    } else {
      newOrder.setArt(0);
    }
    newOrder.setAusfuehrungsdatum(currentBestellung.getLieferDatum());
    newOrder.setBankkonto(UserKonto);
    newOrder.setBestellsumme(currentBestellung.getPreis() * currentBestellung.getMenge());
    newOrder.setMenge(currentBestellung.getMenge());
    newOrder.setName(UserName);
    newOrder.setObjektID(currentFboUser.getIduserfbo());
    newOrder.setTankstelle(true);

    // UserID Owner Fluggesellschaft
    int fgUserID = -1;

    if (currentFboUser.getIdfluggesellschaft() > 0) {
      fgUserID = facadeFBOUser.readFg(currentFboUser.getIdfluggesellschaft()).getUserid();
    } else {
      fgUserID = UserID;
    }

    newOrder.setUserID(fgUserID);

    facadeFBOUser.onBestellung(newOrder);

    NewMessage(loginMB.onSprache("FBOManagement_msg_BestellungGesendet"));
  }

  public List<AngebotsListeFuel> getAngebote() {

    List<AngebotsListeFuel> Angebote = new ArrayList<>();

    String fuelArt = "";
    int Volumen = 0;
    int Gesamt = 0;
    double Preis = 0.0;

    Calendar LieferDatum = Calendar.getInstance();

    if (frmTankstelle) {

      if (currentFboUser != null) {
        Volumen = currentFboUser.getTankstelleMaxFuelKG() - currentFboUser.getBestandAVGASkg() - currentFboUser.getBestandJETAkg();
        Gesamt = currentFboUser.getTankstelleMaxFuelKG();

        if (frmFuelArt > 0) {

          if (frmFuelArt == 1) {
            fuelArt = "AVGAS 100LL";
            Preis = config.getPreisAVGASkg();
          } else {
            fuelArt = "JETA";
            Preis = config.getPreisJETAkg();
          }

          Zaehler = 0;

          // Lieferdatum addiert sich auf
          LieferDatum.add(Calendar.DATE, 0);
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), 100, Preis + 0.12, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Volumen / 100 * 10, Preis + 0.10, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Volumen / 100 * 20, Preis + 0.08, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Volumen / 100 * 30, Preis + 0.08, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Volumen / 100 * 50, Preis + 0.07, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Volumen / 100 * 75, Preis + 0.07, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Volumen / 100 * 100, Preis + 0.05, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 100 * 10, Preis + 0.10, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 100 * 20, Preis + 0.08, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 100 * 30, Preis + 0.08, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 100 * 50, Preis + 0.07, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 100 * 75, Preis + 0.07, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 100 * 100, Preis + 0.05, fuelArt, currentFboUser.getIduserfbo()));

          LieferDatum.add(Calendar.DATE, 2);
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Volumen / 100 * 10, Preis - 0.10, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Volumen / 100 * 25, Preis - 0.15, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Volumen / 100 * 50, Preis - 0.20, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Volumen / 100 * 100, Preis - 0.25, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 100 * 10, Preis - 0.10, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 100 * 25, Preis - 0.15, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 100 * 50, Preis - 0.20, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 100 * 75, Preis - 0.222, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 100 * 100, Preis - 0.25, fuelArt, currentFboUser.getIduserfbo()));

          LieferDatum.add(Calendar.DATE, 6);
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Volumen / 30 * 10, Preis - 0.27, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Volumen / 60 * 10, Preis - 0.29, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Volumen / 100 * 10, Preis - 0.30, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 10 * 10, Preis - 0.27, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 20 * 10, Preis - 0.27, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 50 * 10, Preis - 0.29, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 30 * 10, Preis - 0.27, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 60 * 10, Preis - 0.29, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 100 * 10, Preis - 0.30, fuelArt, currentFboUser.getIduserfbo()));

          LieferDatum.add(Calendar.DATE, 4);
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Volumen / 100 * 30, Preis - 0.40, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Volumen / 100 * 60, Preis - 0.45, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Volumen / 100 * 100, Preis - 0.50, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 100 * 20, Preis - 0.40, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 100 * 25, Preis - 0.42, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 100 * 30, Preis - 0.40, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 100 * 60, Preis - 0.45, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 100 * 100, Preis - 0.50, fuelArt, currentFboUser.getIduserfbo()));

          LieferDatum.add(Calendar.DATE, 4);
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Volumen / 100 * 25, Preis - 0.50, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Volumen / 100 * 50, Preis - 0.60, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Volumen / 100 * 100, Preis - 0.65, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 100 * 25, Preis - 0.50, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 100 * 50, Preis - 0.60, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 100 * 100, Preis - 0.65, fuelArt, currentFboUser.getIduserfbo()));

          LieferDatum.add(Calendar.DATE, 8);
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Volumen / 100 * 10, Preis - 0.65, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Volumen / 100 * 25, Preis - 0.67, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Volumen / 100 * 50, Preis - 0.70, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Volumen / 100 * 100, Preis - 0.72, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 100 * 10, Preis - 0.65, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 100 * 25, Preis - 0.67, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 100 * 50, Preis - 0.70, fuelArt, currentFboUser.getIduserfbo()));
          Angebote.add(new AngebotsListeFuel(getZaehler(1), LieferDatum.getTime(), Gesamt / 100 * 100, Preis - 0.72, fuelArt, currentFboUser.getIduserfbo()));

        }
      }
    }

    return Angebote;
  }

  private int getZaehler(int zaehler) {
    Zaehler = Zaehler + zaehler;
    return Zaehler;
  }

  public void onAngeboteEinholen() {
    config = facadeFBOUser.readConfig();
  }

  public void onUmbau() {

    if (currentFboUser != null && !umbauGestartet) {
      umbauGestartet = true;
      boolean umbauOK = false;
      abrechnungsFehler = true;
      abrechnungsFehlerText = "";
      int AnzahlRouten = 0;
      int MaxRouten = 0;
      FboUserObjekte newObjekt = new FboUserObjekte();

      // Tankstelle prüfen ob leer oder die Restmenge in eine kleinere umgefüllt werden kann.
      if (currentFboUser.getTankstelle()) {
        if (currentFboUser.getBestandAVGASkg() + currentFboUser.getBestandJETAkg() > selectedUmbau.getTankstelleMaxFuelKG()) {
          NewMessage(loginMB.onSprache("FBOManagement_msg_TankstelleVoll"));
          umbauOK = false;
        } else {
          umbauOK = true;
        }
      }

      // FBO Routen prüfen, sind Routen zu viel müssen diese gelöscht werden
      if (currentFboUser.getFbo()) {
        umbauOK = true;
      }


      if (currentFboUser.getAbfertigungsTerminal()) {
//      long gemietetam = currentFboUser.getMietbeginn().getTime();
//      MietTage = new Date().getTime() - gemietetam;
//      long tage = MietTage / 1000 / 60 / 60 / 24;

        if (isKuendigungsZeitOK(currentFboUser.getMietbeginn(), 1)) {
          umbauOK = true;
        } else {
          umbauOK = false;
          //NewMessage(loginMB.onSprache("FBOManagement_msg_TerminalMinUmbauzeit"));
        }
      }

      if (currentFboUser.getBusinessLounge()) {
        umbauOK = true;
      }

      if (currentFboUser.getSpritlager()) {
        umbauOK = false;
        NewMessage("Umbau zur Zeit nicht möglich");
      }

      if (currentFboUser.getServicehangar()) {
        if (facadeFBOUser.istHangarBelegt(currentFboUser.getIduserfbo())) {
          umbauOK = false;
          NewMessage("Hangar ist belegt, Umbau zur Zeit nicht möglich");
        } else {
          umbauOK = true;
        }
      }

      if (umbauOK) {

        // ****************************** Tankstellenoptionen
        if (currentFboUser.getTankstelle()) {
          newObjekt.setBestandAVGASkg(currentFboUser.getBestandAVGASkg());
          newObjekt.setBestandJETAkg(currentFboUser.getBestandJETAkg());
          newObjekt.setEinkaufsPreisAVGAS(currentFboUser.getEinkaufsPreisAVGAS());
          newObjekt.setEinkaufsPreisJETA(currentFboUser.getEinkaufsPreisJETA());
          newObjekt.setPreisAVGAS(currentFboUser.getPreisAVGAS());
          newObjekt.setPreisJETA(currentFboUser.getPreisJETA());
          newObjekt.setGrafikLink(currentFboUser.getGrafikLink());
        }

        newObjekt.setBankkonto(currentFboUser.getBankKonto());
        newObjekt.setKontoName(currentFboUser.getKontoName());

        Calendar naechsteMiete = Calendar.getInstance();
        naechsteMiete.add(Calendar.DATE, 30);
        newObjekt.setFaelligkeitNaechsteMiete(naechsteMiete.getTime());
        newObjekt.setLetzteMietzahlung(new Date());

        newObjekt.setIcao(currentFboUser.getIcao());
        newObjekt.setIdUser(currentFboUser.getIdUser());
        newObjekt.setIdfboObjekt(selectedUmbau.getIdObjekt());
        newObjekt.setMahnStufe(currentFboUser.getMahnStufe());
        newObjekt.setMietbeginn(new Date());
        newObjekt.setName(currentFboUser.getName());
        newObjekt.setPreisArbeitseinheit(0.0);
        newObjekt.setKostenstelle(currentFboUser.getKostenstelle());
        newObjekt.setTerminalGebuehrInProzent(currentFboUser.getTerminalGebuehrInProzent());
        newObjekt.setIdfluggesellschaft(currentFboUser.getIdfluggesellschaft());

        facadeFBOUser.onUmbauSpeichern(newObjekt);

        if (currentFboUser.getAbfertigungsTerminal()) {
          // ID's in den Assignment auf neues Objekt ändern
          System.out.println("Assignments geaendert : " + facadeFBOUser.changeAssignmentFBO(currentFboUser.getIduserfbo(), newObjekt.getIduserfbo()));
          //ID's in den Routen auf neues Objekt ändern
          System.out.println("Routen geaendert : " + facadeFBOUser.changeRoutenFBO(currentFboUser.getIduserfbo(), newObjekt.getIduserfbo()));
        }

        if (currentFboUser.getTankstelle()) {
          //****************** Aendere ID in Bestellungen
          System.out.println("Bestellungen geaendert : " + facadeFBOUser.changeBestellungenIDTankstelle(currentFboUser.getIduserfbo(), newObjekt.getIduserfbo()));
        }

        // Anzurechnende Miettage berechnen
        long difMillisekunden = c.getTime().getTime() - currentFboUser.getLetzteMietzahlung().getTime();
        long MietTage = difMillisekunden / 1000 / 60 / 60 / 24;

        //**** Bankbuchung Rückerstattung Miete
        UserName = currentFboUser.getKontoName();
        UserKonto = currentFboUser.getBankKonto();

        double betrag = currentFboUser.getMietPreis() - ((currentFboUser.getMietPreis() / 30) * MietTage);
        String VerwendungsZweck = loginMB.onSprache("FBOManagement_msg_Bank_Rueckerstattung") + " - " + currentFboUser.getName()
                + "</br>" + loginMB.onSprache("FBOManagement_msg_Bank_Resttage") + " : " + (30 - Math.round(MietTage));

        SaveBankbuchung(UserKonto, UserName, "500-1000001", "**** FTW BANK *****", new Date(), betrag, UserKonto, UserName,
                new Date(), VerwendungsZweck, currentFboUser.getIdUser(), -1, -1, -1, -1, -1, -1, currentFboUser.getIcao(), currentFboUser.getIduserfbo(), -1, currentFboUser.getKostenstelle(), -1);

        //
        //**** Bankbuchung Miete (Miete immer im Voraus)
        //
        betrag = selectedUmbau.getMietPreis() - (selectedUmbau.getMietPreis() * 2);
        VerwendungsZweck = loginMB.onSprache("FBOManagement_msg_Bank_MietZahlung") + loginMB.onSprache("FBOManagement_msg_Bank_Umbau") + currentFboUser.getName();

        SaveBankbuchung(UserKonto, UserName, UserKonto, UserName, new Date(), betrag, "500-1000001", "**** FTW BANK *****",
                new Date(), VerwendungsZweck, currentFboUser.getIdUser(), -1, -1, -1, -1, -1, -1, currentFboUser.getIcao(), currentFboUser.getIduserfbo(), -1, currentFboUser.getKostenstelle(), -1);

        if (currentFboUser.getServicehangar()) {
          facadeFBOUser.hangarUmziehen(currentFboUser.getIduserfbo(), newObjekt.getIduserfbo());
        }

        //löschen des alten Objekts
        facadeFBOUser.onKuendigung(currentFboUser.getIduserfbo());
        abrechnungsFehler = false;

        isLoaded = false;
        setSelectedFboUser(null);

      }

      // *********************  FBO Umbauen
    } // end if Fbo Objekt != 0 
    umbauGestartet = false;
  }

  public void onFaesserErstellen() {
    onTreibstoffJobErstellen();
  }

  public void onTreibstoffJobErstellen() {

    boolean jobErstellen = false;

    if (frmFassTreibstoffart.equals("AVGAS")) {
      if (currentFboUser.getBestandAVGASkg() - (frmFassgroesseKG * frmFassanzahl) >= 0) {
        jobErstellen = true;
      }
    } else if (frmFassTreibstoffart.equals("JETA")) {
      if (currentFboUser.getBestandJETAkg() - (frmFassgroesseKG * frmFassanzahl) >= 0) {
        jobErstellen = true;
      }
    }

    if (frmFassFboID == -1) {
      jobErstellen = false;
      NewMessage(loginMB.onSprache("FBOManagement_msg_KeinSpritlagerAusgewaehlt"));
    }

    if (jobErstellen) {

      isLoaded = false;
      FboUserObjekte editFbo;

      Airport vonFlughafen = facadeFBOUser.getAirportInfo(currentFboUser.getIcao());
      Airport nachFlughafen = facadeFBOUser.getAirportInfo(frmFassZielflughafen);

      Fluggesellschaft fg = facadeFBOUser.readFg(currentFboUser.getIdfluggesellschaft());

      int Ergebnis[] = CONF.DistanzBerechnung(vonFlughafen.getLongitude(), vonFlughafen.getLatitude(), nachFlughafen.getLongitude(), nachFlughafen.getLatitude());

      int rdEntfernung = Ergebnis[0];
      int rdKurs = Ergebnis[1];

      for (int i = 0; i < frmFassanzahl; i++) {

        Assignement RD = new Assignement();

        // Active = 0 Aktiver Auftrag, 1 = Auftrag ist in der Wartehalle
        RD.setActive(0);
        RD.setAirlineLogo(fg.getLogoURL());
        RD.setAmmount(frmFassgroesseKG);
        RD.setBonusclosed(0.0);
        RD.setBonusoeffentlich(0.0);
        RD.setCeoAirline(fg.getBesitzerName());
        RD.setComment("Fuel " + frmFassTreibstoffart);
        RD.setCommodity(frmFassTreibstoffart);
        RD.setCreatedbyuser(fg.getName());
        RD.setDirect(rdKurs);
        RD.setDistance(rdEntfernung);

        long aktZeit = new Date().getTime();
        long endeZeit = aktZeit + ((long) 365 * 24 * 60 * 60 * 1000);

        RD.setExpires(new Date(endeZeit));
        RD.setFlugrouteName("Fuel");
        RD.setFromAirportLandCity(vonFlughafen.getBundesland() + " " + vonFlughafen.getStadt());
        RD.setFromIcao(vonFlughafen.getIcao());
        RD.setFromName(vonFlughafen.getName());
        RD.setGepaeck(0);
        RD.setGewichtPax(0);
        RD.setIcaoCodeFluggesellschaft(fg.getIcaoCode());
        RD.setIdAirline(fg.getIdFluggesellschaft());
        RD.setIdFBO(frmFassFboID);
        RD.setIdRoute(-1);
        RD.setIdTerminal(-1);
        RD.setIdaircraft(-1);
        RD.setIdowner(-1);
        RD.setIsBusinessClass(0);
        RD.setLocationIcao(vonFlughafen.getIcao());
        RD.setNameairline(fg.getName());
        RD.setOeffentlich(1);
        RD.setPay(frmFassTransportPreis);
        RD.setProvision(0.0);
        // Type 1=PAX, 2=CARGO, 3=TRF, 4=Business-PAX, 5 Spritfaesser
        RD.setRoutenArt(5);
        RD.setToAirportLandCity(nachFlughafen.getLand() + " " + nachFlughafen.getStadt());
        RD.setToIcao(nachFlughafen.getIcao());
        RD.setToName(nachFlughafen.getName());

        // Type 1=Routen Job, 2=Standard-Job, 3=Random-Job, 4=Linien-Job, 5=Airport Agent
        RD.setType(2);

        // UserLock 0 = Bereit zum Abflug, 1 = Fliegt gerade
        RD.setUserlock(0);

        RD.setVerlaengert(true);

        // Diese Felder werden zur Zeit nicht gebraucht
        RD.setBearing(0);
        RD.setCreation(new Date());
        RD.setDaysclaimedactive(0);
        RD.setGruppe("");
        RD.setIdcommodity(-1);
        RD.setIdgroup(-1);
        RD.setLizenz("Standard-Job");
        RD.setMpttax(0);
        RD.setNoext(0);
        RD.setPilotfee(0);
        RD.setPtassigment("");
        RD.setUnits("");
        RD.setIdjob(-1);
        RD.setGesplittet(false);
        RD.setKonvertiert(false);
        RD.setLangstrecke(true);

        facadeFBOUser.createAssignment(RD);

        editFbo = facadeFBOUser.findByFboID(currentFboUser.getIduserfbo());

        if (frmFassTreibstoffart.equals("AVGAS")) {
          editFbo.setBestandAVGASkg(editFbo.getBestandAVGASkg() - frmFassgroesseKG);
        } else if (frmFassTreibstoffart.equals("JETA")) {
          editFbo.setBestandJETAkg(editFbo.getBestandJETAkg() - frmFassgroesseKG);
        }

        facadeFBOUser.onEditUserFBO(editFbo);

      }//for Next

    }// Job erstellen ENDE
  }

  public void onFaesserZielflughafen() {
    frmFassZielflughafen = facadeFBOUser.findByFboID(frmFassFboID).getIcao();
  }

  /*
  ****************** Service Hangar Bestellung BEGINN
   */
  public List<Lagerservicehangar> getLagerBestaende() {

    if (currentFboUser != null) {
      return facadeFBOUser.getLagerBestaende(currentFboUser.getIduserfbo());
    } else {
      return null;
    }

  }

  public List<Lagerbestellungservicehangar> getServiceHangarBestellungen() {
    if (currentFboUser != null) {
      return facadeFBOUser.getServicHangarBestellungen(currentFboUser.getIduserfbo());
    } else {
      return null;
    }
  }

  public List<Hangarbelegung> getHangarBelegung() {
    if (currentFboUser != null) {
      return facadeFBOUser.getHangarBelegung(currentFboUser.getIduserfbo());
    } else {
      return null;
    }
  }

  public List<Lagerservicehangarumsatz> getLagerPaketUmsatz() {
    if (currentLagerItem != null) {
      return facadeFBOUser.getPaketUmsatz(currentLagerItem.getIdlagerservicehangar());
    } else {
      return null;
    }
  }

  public void onBestellungAufgeben() {

    String TerminText = "";

    Lagerbestellungservicehangar svh = new Lagerbestellungservicehangar();

    svh.setIdfbouserobjekt(currentFboUser.getIduserfbo());
    svh.setIduser(currentFboUser.getIdUser());
    // 1 = C-Check, 2 = Reparatur-Kit
    svh.setPaketart(bstPackArt);
    svh.setPaketname(bstPackPaketName);
    if (bstPackArt == 1) {
      double menge = bstPackAnzahl;
      TerminText = "Order C-Check: " + bstPackPaketName;
      svh.setMenge(menge);
    } else {
      TerminText = "Order Repairkit: " + bstPackPaketName;
      svh.setMenge(bstPackProzent);
    }
    svh.setPaketekpreis(new Double(bstPackEinkauf));
    svh.setLieferkosten(new Double(bstPackLieferkosten));
    svh.setZustellungam(bstPackLieferzeit);
    svh.setDirektverkauf(false);
    facadeFBOUser.neueServiceHangarBestellung(svh);
    bstPackFlugzeugID = -1;

    Servicehangartermine termin = new Servicehangartermine();
    long enddatum = bstPackLieferzeit.getTime() + (30 * 60 * 1000L);
    termin.setStartdatum(bstPackLieferzeit);
    termin.setEnddatum(new Date(enddatum));
    termin.setText(TerminText);
    termin.setIduser(UserID);
    termin.setIdservicehangar(currentFboUser.getIduserfbo());

    facadeFBOUser.neuerHangarTermin(termin);

    NewMessage("Bestellung wurde erfolgreich gesendet");
  }

  public void onBestellungServiceHangarLoeschen() {
    if (selectedHangarBestellung != null) {
      facadeFBOUser.deleteServicHangarBestellung(selectedHangarBestellung.getIdlagerbestellungservicehangar());
    }
  }

  public void onBestelldatenErmittelnUeberFlugzeug() {

    bstPackLieferkosten = 0;
    bstPackEinkauf = 0;

    Flugzeuge flz = facadeFBOUser.readFlugzeug(bstPackFlugzeugID);
    if (flz != null) {
      bstPackPaketName = flz.getType();
      bstPackFlugzeugBild = flz.getSymbolUrl();
      bstPackStandortICAO = flz.getHerstellerICAO();
    }

    Airport airport = facadeFBOUser.readAirport(bstPackStandortICAO);

    if (airport != null) {
      bstPackStandortName = airport.getName() + " - " + airport.getLand() + " " + airport.getStadt();
    } else {
      bstPackStandortName = "";
    }

    Airport ziel = facadeFBOUser.readAirport(currentFboUser.getIcao());

    //1=CCheck, 2=Servicepack
    if (flz != null) {
      if (bstPackArt == 1) {
        bstPackEinkauf = getCcheckPreis(flz);
        bstPackMaxVerkauf = (int) (bstPackEinkauf * 2.3);
      } else {
        bstPackEinkauf = (int) getRepairKit(flz);
        bstPackEinkauf = bstPackEinkauf * 1;
        bstPackMaxVerkauf = (int) (bstPackEinkauf * 2.3);
      }

      if (ziel != null && airport != null) {
        int[] ergebnis = CONF.DistanzBerechnung(airport.getLongitude(), airport.getLatitude(), ziel.getLongitude(), ziel.getLatitude());
        bstPackEntfernung = ergebnis[0];
        bstPackLieferkosten = (int) (bstPackEntfernung * 3.5);

        switch (flz.getAntriebsart()) {
          case 1:
            bstPackLieferkosten = (int) (bstPackLieferkosten * 0.05);
            break;
          case 2:
            bstPackLieferkosten = (int) (bstPackLieferkosten * 0.5);
            break;
          case 3:
            bstPackLieferkosten = (int) (bstPackLieferkosten * 1);
            break;
          default:

            break;
        }

        long lfzeit = (bstPackEntfernung / 250) * 60 * 60 * 1000;
        if (bstPackEntfernung < 500) {
          lfzeit = (500 / 250) * 60 * 60 * 1000;
        }

        bstPackLieferzeit = new Date(new Date().getTime() + lfzeit);

      } else {
        bstPackEntfernung = 0;
      }
    }
  }

  public void onBestelldatenErmittelnUeberIcao(String Lager, String Kunde) {

    bstPackLieferkosten = 0;
    bstPackEinkauf = 0;

    bstPackStandortICAO = Lager;

    Airport airport = facadeFBOUser.readAirport(bstPackStandortICAO);

    if (airport != null) {
      bstPackStandortName = airport.getName() + " - " + airport.getLand() + " " + airport.getStadt();
    } else {
      bstPackStandortName = "";
    }

    Airport ziel = facadeFBOUser.readAirport(Kunde);

    if (ziel != null && airport != null) {
      int[] ergebnis = CONF.DistanzBerechnung(airport.getLongitude(), airport.getLatitude(), ziel.getLongitude(), ziel.getLatitude());
      bstPackEntfernung = ergebnis[0];

      bstPackLieferkosten = (int) (bstPackEntfernung * 3.5);
      long lfzeit = (bstPackEntfernung / 250) * 60 * 60 * 1000;

      if (bstPackEntfernung < 500) {
        lfzeit = (500 / 250) * 60 * 60 * 1000;
      }

      bstPackLieferzeit = new Date(new Date().getTime() + lfzeit);

    } else {
      bstPackEntfernung = 0;
    }

  }

  private int getCcheckPreis(Flugzeuge flz) {

    double Einkauf = 0;
    switch (flz.getLizenz()) {
      case "PPL-A":
        Einkauf = flz.getVerkaufspreis() * 0.10;
        break;
      case "CPL":
        Einkauf = flz.getVerkaufspreis() * 0.10;
        break;
      case "MPL":
        Einkauf = flz.getVerkaufspreis() * 0.10;
        break;
      case "ATPL":
        Einkauf = flz.getVerkaufspreis() * 0.10;
        break;
      default:
        break;
    }

    Einkauf = (Einkauf * 0.60);
    return (int) Einkauf;
  }

  private double getRepairKit(Flugzeuge flz) {

    int AktuellesJahr = Calendar.getInstance().get(Calendar.YEAR);

    double KolbenmotorGewoehnlichNutzungsdauer = 12000;
    double TurboPropGewoehnlichNutzungsdauer = 16000;
    double TurbineGewoehnlichNutzungsdauer = 20000;
    double BasisPreis = 0.0;
    double TypeFaktor = 0.0;

    switch (flz.getAntriebsart()) {
      case 1:
        BasisPreis = flz.getVerkaufspreis() / KolbenmotorGewoehnlichNutzungsdauer;
        TypeFaktor = 35;
        break;
      case 2:
        BasisPreis = flz.getVerkaufspreis() / TurboPropGewoehnlichNutzungsdauer;
        TypeFaktor = 45;
        break;
      case 3:
        BasisPreis = flz.getVerkaufspreis() / TurbineGewoehnlichNutzungsdauer;
        TypeFaktor = 55;
        break;
      default:
        BasisPreis = 0.0;
        break;
    }

    BasisPreis = (BasisPreis * TypeFaktor) * bstPackProzent;
    return BasisPreis;

  }

  public void onLagerServiceHangarSpeichern() {
    facadeFBOUser.editLagerServiceHangarSave(currentLagerItem);
  }

  // Unternehmensinfo
  public List<ViewuebersichtServiceHangar> getInfoListServiceHangars() {

    if (!isLoaded) {
      listeServiceHangars = facadeFBOUser.getListeServiceHangars();
      isLoaded = true;
      return listeServiceHangars;
    }
    return listeServiceHangars;
  }

  // Handel
  public void onHandelCheck() {
    bstPackAnlieferBankKonto = "";
    bstPackLieferkosten = 0;
    bstPackLieferzeit = null;
    bstPackHandelOK = false;

    if (currentLagerItem != null) {

      bstPackArt = currentLagerItem.getPaketart();
      bstPackPaketName = currentLagerItem.getPaketname();

      if (bstPackArt == 1) {
        if (currentLagerItem.getMenge() - bstPackAnzahl < 0) {
          NewMessage("Bestand reicht nicht für den Handel aus");
        } else {
          bstPackHandelOK = true;
        }
      } else {
        if (currentLagerItem.getMenge() - bstPackProzent < 0.0) {
          NewMessage("Bestand reicht nicht für den Handel aus");
        } else {
          bstPackAnlieferPreis = (bstPackAnlieferPreis * bstPackProzent);
          bstPackHandelOK = true;
        }
      }

      if (bstPackHandelOK) {
        ViewFBOUserObjekte ob = facadeFBOUser.readFBOObjektByID(bstPackAnlieferID);
        if (ob != null) {
          if (ob.getServicehangar()) {
            try {
              bstPackAnlieferBankKonto = ob.getBankKonto();
              bstPackAnlieferUserID = ob.getIdUser();
              onBestelldatenErmittelnUeberIcao(currentFboUser.getIcao(), ob.getIcao());

              double Betrag = (bstPackLieferkosten + bstPackAnlieferPreis);
//              System.out.println("onHandelTransfer() Rechnungsbetrag: " + nf.format(Betrag));
//              System.out.println("Banksaldo " + nf.format(facadeFBOUser.BankSaldo(bstPackAnlieferBankKonto)));

//              if (!(facadeFBOUser.BankSaldo(bstPackAnlieferBankKonto) >= Betrag)) {
//                NewMessage("Bankguthaben reicht für Transaktion nicht aus");
////                System.out.println("onHandelTransfer() Bankguthaben reicht für Transaktion nicht aus ");
//                bstPackHandelOK = false;
//              }
            } catch (NullPointerException e) {
              NewMessage("Bankverbindung konnte nicht ermittelt werden!");
              bstPackHandelOK = false;
            }
          } else {
            NewMessage("Bei der angegebenen ID handelt es sich nicht um einen Service Hangar");
            bstPackHandelOK = false;
          }
        } else {
          bstPackAnlieferBankKonto = "Not found";
          bstPackHandelOK = false;
        }
      }

    }

  }

  public void onHandelTransfer() {

    onHandelCheck();

    if (bstPackHandelOK) {
      Lagerbestellungservicehangar svh = new Lagerbestellungservicehangar();
      svh.setIdfbouserobjekt(bstPackAnlieferID);
      svh.setIduser(bstPackAnlieferUserID);
      // 1 = C-Check, 2 = Reparatur-Kit
      svh.setPaketart(bstPackArt);
      svh.setPaketname(bstPackPaketName);
      if (bstPackArt == 1) {
        double menge = bstPackAnzahl;
        currentLagerItem.setMenge(currentLagerItem.getMenge() - bstPackAnzahl);
        svh.setMenge(menge);
      } else {
        svh.setMenge(bstPackProzent);
        currentLagerItem.setMenge(currentLagerItem.getMenge() - bstPackProzent);
      }
      svh.setPaketekpreis(bstPackAnlieferPreis);
      svh.setLieferkosten(new Double(bstPackLieferkosten));
      svh.setZustellungam(bstPackLieferzeit);
      svh.setDirektverkauf(true);
      facadeFBOUser.neueServiceHangarBestellung(svh);
      bstPackFlugzeugID = -1;

      //Transaktion im Lager speichern
      currentLagerItem.setVerkauftam(new Date());
      facadeFBOUser.editLagerServiceHangarSave(currentLagerItem);

      // Umsatz verbuchen
      ViewFBOUserObjekte ag = facadeFBOUser.readFBOObjektByID(bstPackAnlieferID);

      String Auftraggeber = ag.getKontoName();
      String AuftraggeberKonto = ag.getBankKonto();

      String Empfaenger = currentFboUser.getKontoName();
      String EmpfaengerKonto = currentFboUser.getBankKonto();

      String PaketArt = "";
      String PaketMenge = "";

      if (bstPackArt == 1) {
        PaketArt = "C-Check";
        PaketMenge = nf.format(bstPackAnzahl) + " Item(s)";
      } else {
        PaketMenge = nf.format(bstPackProzent) + " %";
        PaketArt = "Servicepunkte";
      }

      // Wegen Betrugsausnutzung wurde die automatische Abbuchung entfernt.
//      String Verwendungszweck = "Verkauf von: (" + PaketMenge + ") " + PaketArt + " - " + currentLagerItem.getPaketname();
      double Betrag = (bstPackLieferkosten + bstPackAnlieferPreis);

      if (bstPackHandelOK) {

//        //Verkaeuferbuchung
//        SaveBankbuchung(EmpfaengerKonto, Empfaenger, AuftraggeberKonto, Auftraggeber, new Date(), Betrag, EmpfaengerKonto, Empfaenger,
//                new Date(), Verwendungszweck, UserID, -1, currentFboUser.getIdfluggesellschaft(), -1, -1, -1, -1,
//                currentFboUser.getIcao(), currentFboUser.getIduserfbo(), -1, currentFboUser.getKostenstelle(), -1);
//
//        //Käuferbuchung
//        Verwendungszweck = "Einkauf von: (" + PaketMenge + ") " + PaketArt + " - " + currentLagerItem.getPaketname() + " - ";
//
//        Betrag = Betrag * -1;
//        SaveBankbuchung(AuftraggeberKonto, Auftraggeber, EmpfaengerKonto, Empfaenger, new Date(), Betrag, AuftraggeberKonto, Auftraggeber,
//                new Date(), Verwendungszweck, ag.getIdUser(), -1, ag.getIdfluggesellschaft(), -1, -1, -1, -1,
//                ag.getIcao(), ag.getIduserfbo(), -1, ag.getKostenstelle(), -1);
        onHangarLagerUmsatzVerbuchen(currentFboUser.getIduserfbo(), currentLagerItem.getIdlagerservicehangar(), UserID, ag.getIdUser(),
                bstPackArt, bstPackProzent, currentLagerItem.getPaketekpreis(), Betrag, PaketArt);

        NewMessage("Handel wurde erfolgreich durchgeführt");

      }

    }

  }

  private void onHangarLagerUmsatzVerbuchen(int HangarID, int LagerID, int OwnerID, int KaeuferID, int PaketArt, double Menge, double EKPreis, double VKPreis, String PaketName) {
    Lagerservicehangarumsatz lagerUmsatz = new Lagerservicehangarumsatz();

    //Umsatz verbuchen
    lagerUmsatz.setIdfbouserobjekt(HangarID);
    lagerUmsatz.setIdlagerservicehangar(LagerID);
    lagerUmsatz.setIduser(OwnerID);
    if (PaketArt == 1) {
      lagerUmsatz.setMenge(1.0);
    } else {
      lagerUmsatz.setMenge(Menge);
    }
    lagerUmsatz.setPaketart(PaketArt);
    lagerUmsatz.setPaketekpreis(EKPreis);
    lagerUmsatz.setPaketname(PaketName);
    lagerUmsatz.setPaketvkpreis(VKPreis);
    lagerUmsatz.setVerkauftanuserid(KaeuferID);
    lagerUmsatz.setVerkauftam(new Date());

    facadeFBOUser.neuerUmsatzVerbuchen(lagerUmsatz);

  }

  public String getPlatzImHangar() {

    if (currentFboUser != null && currentFboUser.getServicehangar()) {
      List<Hangarbelegung> belegung = facadeFBOUser.getHangarBelegung(currentFboUser.getIduserfbo());
      int summeBelegung = 0;

      for (Hangarbelegung item : belegung) {
        summeBelegung = summeBelegung + item.getQuadratmeter();
      }

      summeBelegung = currentFboUser.getServicehangarQM() - summeBelegung;

      return " Freier Platz im Hangar: " + nfohneKomma.format(summeBelegung) + " m²";

    } else {
      return "";
    }

  }

  /*
  ****************** Service Hangar Bestellung ENDE
   */
  public void onRowSelect(SelectEvent event) {

    frmTankstelle = false;

    try {
      frmBezeichnung = currentFboUser.getName();
      frmPreisAVGAS = currentFboUser.getPreisAVGAS();
      frmPreisJETA = currentFboUser.getPreisJETA();
      frmPreisArbeitseinheit = currentFboUser.getPreisArbeitseinheit();
      frmTankstelle = currentFboUser.getTankstelle();
      frmSpritlager = currentFboUser.getSpritlager();
      frmServiceHangar = currentFboUser.getServicehangar();
      zeigeBankkonto = currentFboUser.getAbfertigungsTerminal();
      frmTerminalAbfertigungsGebuehr = currentFboUser.getTerminalGebuehrInProzent();
      frmKostenStelle = currentFboUser.getKostenstelle();
      frmfluggesellschaftID = currentFboUser.getIdfluggesellschaft();
      frmTankstelleGrafik = currentFboUser.getGrafikLink();

      frmBankKonto = currentFboUser.getBankKonto();

    } catch (NullPointerException e) {
      AirportName = "";
    }

    onBerechtigungenEinlesen();

  }

  public void onBerechtigungenEinlesen() {

    boolean gelesen = false;

    setAllowFBO(false);
    setAllowFBOBearbeiten(false);
    setAllowFBOHinzufuegen(true);
    setAllowFBOLoeschen(false);
    setAllowFBOTankstelleVerwalten(false);

    int idUser = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
    int idfg = facadeFBOUser.readUser(idUser).getFluggesellschaftManagerID();

    // ****** nichts Ausgewählt und es kann evtl. keine Berechtigung ermittelt werden
    // ****** das hinzufügen von FBO Objekten erlauben.
    if (getSelectedFboUser() == null) {
      setAllowFBOHinzufuegen(true);
      setAllowFBO(true);
      setAllowFBOBearbeiten(true);
      setAllowFBOHinzufuegen(true);
      setAllowFBOLoeschen(true);
      setAllowFBOTankstelleVerwalten(true);
      gelesen = true;
    }

    // ****** Keine Managerfunktion
    if (idfg == -1) {
      setAllowFBO(true);
      setAllowFBOBearbeiten(true);
      setAllowFBOHinzufuegen(true);
      setAllowFBOLoeschen(true);
      setAllowFBOTankstelleVerwalten(true);
      gelesen = true;
    }

    // ****** Besitzer der Fluggesellschaft
    if (getSelectedFboUser() != null) {
      if (getSelectedFboUser().getIdfluggesellschaft() == -1) {
        setAllowFBO(true);
        setAllowFBOBearbeiten(true);
        setAllowFBOHinzufuegen(true);
        setAllowFBOLoeschen(true);
        setAllowFBOTankstelleVerwalten(true);
        gelesen = true;
      }
    }

    if (getSelectedFboUser() != null && !gelesen) {

      int OwnerFG = facadeFBOUser.readFg(getSelectedFboUser().getIdfluggesellschaft()).getUserid();

      if (!gelesen) {
        if (OwnerFG == idUser) {
          setAllowFBO(true);
          setAllowFBOBearbeiten(true);
          setAllowFBOHinzufuegen(true);
          setAllowFBOLoeschen(true);
          setAllowFBOTankstelleVerwalten(true);
          gelesen = true;
        }
      }

      if (!gelesen) {
        if (OwnerFG != idUser) {
          // user ist Manager Berechtigungen auslesen
          Fluggesellschaftmanager mg = facadeFBOUser.readManager(idUser, idfg);
          if (mg != null) {
            setAllowFBO(mg.getAllowFBO());
            setAllowFBOBearbeiten(mg.getAllowFBOBearbeiten());
            setAllowFBOHinzufuegen(mg.getAllowFBOHinzufuegen());
            setAllowFBOLoeschen(mg.getAllowFBOLoeschen());
            setAllowFBOTankstelleVerwalten(mg.getAllowFBOTankstelleVerwalten());
            gelesen = true;
          }
        }
      }
      if (!gelesen) {
        // Berechtigungen für Manager der Fluggesellschaft lesen
        if (idfg > 0) {
          Fluggesellschaftmanager mg = facadeFBOUser.readManager(idUser, idfg);
          if (mg != null) {
            setAllowFBO(mg.getAllowFBO());
            setAllowFBOBearbeiten(mg.getAllowFBOBearbeiten());
            setAllowFBOHinzufuegen(mg.getAllowFBOHinzufuegen());
            setAllowFBOLoeschen(mg.getAllowFBOLoeschen());
            setAllowFBOTankstelleVerwalten(mg.getAllowFBOTankstelleVerwalten());
            gelesen = true;
          }
        }
      }

    }

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
  public ViewFBOUserObjekte getSelectedFboUser() {
    return currentFboUser;
  }

  public void setSelectedFboUser(ViewFBOUserObjekte selectedFboUser) {
    this.currentFboUser = selectedFboUser;
  }

  public String getFrmBezeichnung() {
    return frmBezeichnung;
  }

  public void setFrmBezeichnung(String frmBezeichnung) {
    this.frmBezeichnung = frmBezeichnung;
  }

  public double getFrmPreisArbeitseinheit() {
    return frmPreisArbeitseinheit;
  }

  public void setFrmPreisArbeitseinheit(double frmPreisArbeitseinheit) {
    this.frmPreisArbeitseinheit = frmPreisArbeitseinheit;
  }

  public double getFrmPreisAVGAS() {
    return frmPreisAVGAS;
  }

  public void setFrmPreisAVGAS(double frmPreisAVGAS) {
    this.frmPreisAVGAS = frmPreisAVGAS;
  }

  public double getFrmPreisJETA() {
    return frmPreisJETA;
  }

  public void setFrmPreisJETA(double frmPreisJETA) {
    this.frmPreisJETA = frmPreisJETA;
  }

  public boolean isFrmTankstelle() {
    return frmTankstelle;
  }

  public void setFrmTankstelle(boolean frmTankstelle) {
    this.frmTankstelle = frmTankstelle;
  }

  public boolean isFrmServiceHangar() {
    return frmServiceHangar;
  }

  public void setFrmServiceHangar(boolean frmServiceHangar) {
    this.frmServiceHangar = frmServiceHangar;
  }

  public int getFrmBestellmengeFuel() {
    return frmBestellmengeFuel;
  }

  public void setFrmBestellmengeFuel(int frmBestellmengeFuel) {
    this.frmBestellmengeFuel = frmBestellmengeFuel;
  }

  public int getFrmFuelArt() {
    return frmFuelArt;
  }

  public void setFrmFuelArt(int frmFuelArt) {
    this.frmFuelArt = frmFuelArt;
  }

  public AngebotsListeFuel getSelectedBestellung() {
    return currentBestellung;
  }

  public void setSelectedBestellung(AngebotsListeFuel selectedBestellung) {
    this.currentBestellung = selectedBestellung;
  }

  public String getFrmBankKonto() {
    return frmBankKonto;
  }

  public void setFrmBankKonto(String frmBankKonto) {
    this.frmBankKonto = frmBankKonto;
  }

  public String getFrmKontoName() {
    return frmKontoName;
  }

  public void setFrmKontoName(String frmKontoName) {
    this.frmKontoName = frmKontoName;
  }

  public boolean isZeigeBankkonto() {
    return zeigeBankkonto;
  }

  public void setZeigeBankkonto(boolean zeigeBankkonto) {
    this.zeigeBankkonto = zeigeBankkonto;
  }

  public String getHangarLagerUmsatzAbnehmer(int id) {
    if (currentLagerItem != null) {
      Benutzer user = facadeFBOUser.readUser(id);

      if (user != null) {
        return user.getName1();
      } else {
        return "";
      }
    }
    return "";
  }

  public String getUserName() {
    return UserName;
  }

  public String getUserKonto() {
    return UserKonto;
  }

  public FboObjekte getSelectedUmbau() {
    return selectedUmbau;
  }

  public double getFrmTerminalAbfertigungsGebuehr() {
    return frmTerminalAbfertigungsGebuehr;
  }

  public void setFrmTerminalAbfertigungsGebuehr(double frmTerminalAbfertigungsGebuehr) {
    this.frmTerminalAbfertigungsGebuehr = frmTerminalAbfertigungsGebuehr;
  }

  public void setSelectedUmbau(FboObjekte selectedUmbau) {
    this.selectedUmbau = selectedUmbau;
  }

  public int getFrmKostenStelle() {
    return frmKostenStelle;
  }

  public void setFrmKostenStelle(int frmKostenStelle) {
    this.frmKostenStelle = frmKostenStelle;
  }

  public boolean isAbrechnungsFehler() {
    return abrechnungsFehler;
  }

  public void setAbrechnungsFehler(boolean abrechnungsFehler) {
    this.abrechnungsFehler = abrechnungsFehler;
  }

  public String getAbrechnungsFehlerText() {
    return abrechnungsFehlerText;
  }

  public void setAbrechnungsFehlerText(String abrechnungsFehlerText) {
    this.abrechnungsFehlerText = abrechnungsFehlerText;
  }

  public double getGesamtSumme() {
    if (isLoaded) {
      double faktor = 0.0;
      long anzahl = 0;

      SummeTankstellen = 0;
      SummeBusinessLounge = 0;
      SummeFBO = 0;
      SummeRoutenObjekte = 0;
      SummeTerminals = 0;
      SummeTankstellenAufschlag = 0;
      SummeBusinessLoungeAufschlag = 0;
      SummeFBOAufschlag = 0;
      SummeRoutenObjekteAufschlag = 0;
      SummeTerminalsAufschlag = 0;

      GesamtSumme = 0;

      for (ViewFBOUserObjekte next : fboUserList) {

        faktor = 0.0;
        anzahl = 0;

        // wieviele FBO's gibt es an dem Flughafen
        anzahl = facadeFBOUser.getAnzahlFBO(next.getIcao());

        if (anzahl > 1) {
          faktor = anzahl * 10.0 / 100.0;
        }

        GesamtSumme = GesamtSumme + next.getMietPreis() + (next.getMietPreis() * faktor);

        if (next.getTankstelle()) {
          SummeTankstellen = SummeTankstellen + next.getMietPreis();
          SummeTankstellenAufschlag = SummeTankstellenAufschlag + next.getMietPreis() * faktor;
        }
        if (next.getAbfertigungsTerminal()) {
          SummeTerminals = SummeTerminals + next.getMietPreis();
          SummeTerminalsAufschlag = SummeTerminalsAufschlag + next.getMietPreis() * faktor;
        }
        if (next.getBusinessLounge()) {
          SummeBusinessLounge = SummeBusinessLounge + next.getMietPreis();
          SummeBusinessLoungeAufschlag = SummeBusinessLoungeAufschlag + next.getMietPreis() * faktor;
        }
        if (next.getFbo()) {
          SummeFBO = SummeFBO + next.getMietPreis();
          SummeFBOAufschlag = SummeFBOAufschlag + next.getMietPreis() * faktor;
        }
        if (!next.getFbo() && next.getAnzahlRouten() > 0) {
          SummeRoutenObjekte = SummeRoutenObjekte + next.getMietPreis();
          SummeRoutenObjekteAufschlag = SummeRoutenObjekteAufschlag + next.getMietPreis() * faktor;
        }

      }
    }

    return GesamtSumme;
  }

  public void setGesamtSumme(double GesamtSumme) {
    this.GesamtSumme = GesamtSumme;
  }

  public double getSummeTankstellen() {
    return SummeTankstellen;
  }

  public void setSummeTankstellen(double SummeTankstellen) {
    this.SummeTankstellen = SummeTankstellen;
  }

  public double getSummeFBO() {
    return SummeFBO;
  }

  public void setSummeFBO(double SummeFBO) {
    this.SummeFBO = SummeFBO;
  }

  public double getSummeBusinessLounge() {
    return SummeBusinessLounge;
  }

  public void setSummeBusinessLounge(double SummeBusinessLounge) {
    this.SummeBusinessLounge = SummeBusinessLounge;
  }

  public double getSummeTerminals() {
    return SummeTerminals;
  }

  public void setSummeTerminals(double SummeTerminals) {
    this.SummeTerminals = SummeTerminals;
  }

  public double getSummeRoutenObjekte() {
    return SummeRoutenObjekte;
  }

  public void setSummeRoutenObjekte(double SummeRoutenObjekte) {
    this.SummeRoutenObjekte = SummeRoutenObjekte;
  }

  public double getSummeTankstellenAufschlag() {
    return SummeTankstellenAufschlag;
  }

  public void setSummeTankstellenAufschlag(double SummeTankstellenAufschlag) {
    this.SummeTankstellenAufschlag = SummeTankstellenAufschlag;
  }

  public double getSummeFBOAufschlag() {
    return SummeFBOAufschlag;
  }

  public void setSummeFBOAufschlag(double SummeFBOAufschlag) {
    this.SummeFBOAufschlag = SummeFBOAufschlag;
  }

  public double getSummeBusinessLoungeAufschlag() {
    return SummeBusinessLoungeAufschlag;
  }

  public void setSummeBusinessLoungeAufschlag(double SummeBusinessLoungeAufschlag) {
    this.SummeBusinessLoungeAufschlag = SummeBusinessLoungeAufschlag;
  }

  public double getSummeTerminalsAufschlag() {
    return SummeTerminalsAufschlag;
  }

  public void setSummeTerminalsAufschlag(double SummeTerminalsAufschlag) {
    this.SummeTerminalsAufschlag = SummeTerminalsAufschlag;
  }

  public double getSummeRoutenObjekteAufschlag() {
    return SummeRoutenObjekteAufschlag;
  }

  public void setSummeRoutenObjekteAufschlag(double SummeRoutenObjekteAufschlag) {
    this.SummeRoutenObjekteAufschlag = SummeRoutenObjekteAufschlag;
  }

  public int getFrmKlasse() {
    return frmKlasse;
  }

  public void setFrmKlasse(int frmKlasse) {
    this.frmKlasse = frmKlasse;
  }

  public String getAirportName() {
    return AirportName;
  }

  public void setAirportName(String AirportName) {
    this.AirportName = AirportName;
  }

  private Airport getAirport(String icao) {
    return facadeFBOUser.getAirportInfo(icao);
  }

  public int getFrmfluggesellschaftID() {
    return frmfluggesellschaftID;
  }

  public void setFrmfluggesellschaftID(int frmfluggesellschaftID) {
    this.frmfluggesellschaftID = frmfluggesellschaftID;
  }

  private double MietaufschlagFaktor(String icao) {
    Airport airport = getAirport(icao);
    double aufschlag = 0.0;
    double prozente = 0.0;

    if (airport != null) {
      prozente = 10.0;
      long exFbo = facadeFBOUser.wievieleFBOAmFlughafen(icao);
      if (exFbo > 0) {
        exFbo = exFbo - 1;
      }

      aufschlag = (100 + (prozente * exFbo)) / 100;

    }
    return aufschlag;
  }

  // diese Methode dient nur zur Darstellung in der Tabelle
  public String getMietaufschlag(String icao, double miete) {
    Airport airport = getAirport(icao);
    double aufschlag = 0.0;
    double aufschlagPRZ = 0.0;

    if (airport != null) {
      //double prozente = CONF.getFlughafenAufschlag(airport.getKlasse());
      double prozente = 10.0;
      long exFbo = facadeFBOUser.wievieleFBOAmFlughafen(icao);
      if (exFbo > 0) {
        exFbo = exFbo - 1;
      }
      aufschlag = prozente * exFbo / 100 * miete;
      aufschlagPRZ = prozente * exFbo;

    }
    return nf.format(aufschlagPRZ) + " % - " + nf.format(aufschlag) + " &euro;";
  }

  public boolean isAllowFluggesellschaftLoeschen() {
    return allowFluggesellschaftLoeschen;
  }

  public void setAllowFluggesellschaftLoeschen(boolean allowFluggesellschaftLoeschen) {
    this.allowFluggesellschaftLoeschen = allowFluggesellschaftLoeschen;
  }

  public String getFrmTankstelleGrafik() {
    return frmTankstelleGrafik;
  }

  public void setFrmTankstelleGrafik(String frmTankstelleGrafik) {
    this.frmTankstelleGrafik = frmTankstelleGrafik;
  }

  public String getFltObjekt() {
    return fltObjekt;
  }

  public void setFltObjekt(String fltObjekt) {
    this.fltObjekt = fltObjekt;
  }

  public String getFltIcao() {
    return fltIcao;
  }

  public void setFltIcao(String fltIcao) {
    this.fltIcao = fltIcao;
  }

  public ViewFBOUserObjekte getSelectedTankstelle() {
    return selectedTankstelle;
  }

  public void setSelectedTankstelle(ViewFBOUserObjekte selectedTankstelle) {
    this.selectedTankstelle = selectedTankstelle;
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

    facadeFBOUser.createBankbuchung(newBuchung);

  }

  public boolean isAllowFBO() {
    return allowFBO;
  }

  public void setAllowFBO(boolean allowFBO) {
    this.allowFBO = allowFBO;
  }

  public boolean isAllowFBOBearbeiten() {
    return allowFBOBearbeiten;
  }

  public void setAllowFBOBearbeiten(boolean allowFBOBearbeiten) {
    this.allowFBOBearbeiten = allowFBOBearbeiten;
  }

  public boolean isAllowFBOHinzufuegen() {
    return allowFBOHinzufuegen;
  }

  public void setAllowFBOHinzufuegen(boolean allowFBOHinzufuegen) {
    this.allowFBOHinzufuegen = allowFBOHinzufuegen;
  }

  public boolean isAllowFBOLoeschen() {
    return allowFBOLoeschen;
  }

  public void setAllowFBOLoeschen(boolean allowFBOLoeschen) {
    this.allowFBOLoeschen = allowFBOLoeschen;
  }

  public boolean isAllowFBOTankstelleVerwalten() {
    return allowFBOTankstelleVerwalten;
  }

  public void setAllowFBOTankstelleVerwalten(boolean allowFBOTankstelleVerwalten) {
    this.allowFBOTankstelleVerwalten = allowFBOTankstelleVerwalten;
  }

  public int getFrmFassgroesseKG() {
    return frmFassgroesseKG;
  }

  public void setFrmFassgroesseKG(int frmFassgroesseKG) {
    this.frmFassgroesseKG = frmFassgroesseKG;
  }

  public int getFrmFassanzahl() {
    return frmFassanzahl;
  }

  public void setFrmFassanzahl(int frmFassanzahl) {
    this.frmFassanzahl = frmFassanzahl;
  }

  public double getFrmFassTransportPreis() {
    return frmFassTransportPreis;
  }

  public void setFrmFassTransportPreis(double frmFassTransportPreis) {
    this.frmFassTransportPreis = frmFassTransportPreis;
  }

  public String getFrmFassZielflughafen() {
    return frmFassZielflughafen;
  }

  public void setFrmFassZielflughafen(String frmFassZielflughafen) {
    this.frmFassZielflughafen = frmFassZielflughafen;
  }

  public String getFrmFassTreibstoffart() {
    return frmFassTreibstoffart;
  }

  public void setFrmFassTreibstoffart(String frmFassTreibstoffart) {
    this.frmFassTreibstoffart = frmFassTreibstoffart;
  }

  public boolean isFrmSpritlager() {
    return frmSpritlager;
  }

  public void setFrmSpritlager(boolean frmSpritlager) {
    this.frmSpritlager = frmSpritlager;
  }

  public int getFrmFassFboID() {
    return frmFassFboID;
  }

  public void setFrmFassFboID(int frmFassFboID) {
    this.frmFassFboID = frmFassFboID;
  }

  public int getBstPackFlugzeugID() {
    return bstPackFlugzeugID;
  }

  public void setBstPackFlugzeugID(int bstPackFlugzeugID) {
    this.bstPackFlugzeugID = bstPackFlugzeugID;
  }

  public String getBstPackStandortICAO() {
    return bstPackStandortICAO;
  }

  public void setBstPackStandortICAO(String bstPackStandortICAO) {
    this.bstPackStandortICAO = bstPackStandortICAO;
  }

  public String getBstPackStandortName() {
    return bstPackStandortName;
  }

  public void setBstPackStandortName(String bstPackStandortName) {
    this.bstPackStandortName = bstPackStandortName;
  }

  public int getBstPackEinkauf() {
    return bstPackEinkauf;
  }

  public void setBstPackEinkauf(int bstPackEinkauf) {
    this.bstPackEinkauf = bstPackEinkauf;
  }

  public int getBstPackMaxVerkauf() {
    return bstPackMaxVerkauf;
  }

  public void setBstPackMaxVerkauf(int bstPackMaxVerkauf) {
    this.bstPackMaxVerkauf = bstPackMaxVerkauf;
  }

  public Date getBstPackLieferzeit() {
    return bstPackLieferzeit;
  }

  public void setBstPackLieferzeit(Date bstPackLieferzeit) {
    this.bstPackLieferzeit = bstPackLieferzeit;
  }

  public int getBstPackLieferkosten() {
    return bstPackLieferkosten;
  }

  public void setBstPackLieferkosten(int bstPackLieferkosten) {
    this.bstPackLieferkosten = bstPackLieferkosten;
  }

  public int getBstPackEntfernung() {
    return bstPackEntfernung;
  }

  public void setBstPackEntfernung(int bstPackEntfernung) {
    this.bstPackEntfernung = bstPackEntfernung;
  }

  public int getBstPackFTWPreis() {
    return bstPackFTWPreis;
  }

  public void setBstPackFTWPreis(int bstPackFTWPreis) {
    this.bstPackFTWPreis = bstPackFTWPreis;
  }

  public int getBstPackAnzahl() {
    return bstPackAnzahl;
  }

  public void setBstPackAnzahl(int bstPackAnzahl) {
    this.bstPackAnzahl = bstPackAnzahl;
  }

  public int getBstPackArt() {
    return bstPackArt;
  }

  public void setBstPackArt(int bstPackArt) {
    this.bstPackArt = bstPackArt;
  }

  public String getBstPackPaketName() {
    return bstPackPaketName;
  }

  public void setBstPackPaketName(String bstPackPaketName) {
    this.bstPackPaketName = bstPackPaketName;
  }

  public String getBstPackFlugzeugBild() {
    return bstPackFlugzeugBild;
  }

  public void setBstPackFlugzeugBild(String bstPackFlugzeugBild) {
    this.bstPackFlugzeugBild = bstPackFlugzeugBild;
  }

  public double getBstPackProzent() {
    return bstPackProzent;
  }

  public void setBstPackProzent(double bstPackProzent) {
    this.bstPackProzent = bstPackProzent;
  }

  public int getBstPackAnlieferID() {
    return bstPackAnlieferID;
  }

  public void setBstPackAnlieferID(int bstPackAnlieferID) {
    this.bstPackAnlieferID = bstPackAnlieferID;
  }

  public String getBstPackAnlieferBankKonto() {
    return bstPackAnlieferBankKonto;
  }

  public void setBstPackAnlieferBankKonto(String bstPackAnlieferBankKonto) {
    this.bstPackAnlieferBankKonto = bstPackAnlieferBankKonto;
  }

  public Lagerservicehangar getCurrentLagerItem() {
    return currentLagerItem;
  }

  public void setCurrentLagerItem(Lagerservicehangar currentLagerItem) {
    this.currentLagerItem = currentLagerItem;
  }

  public double getBstPackAnlieferPreis() {
    return bstPackAnlieferPreis;
  }

  public void setBstPackAnlieferPreis(double bstPackAnlieferPreis) {
    this.bstPackAnlieferPreis = bstPackAnlieferPreis;
  }

  public int getBstPackAnlieferUserID() {
    return bstPackAnlieferUserID;
  }

  public void setBstPackAnlieferUserID(int bstPackAnlieferUserID) {
    this.bstPackAnlieferUserID = bstPackAnlieferUserID;
  }

  public Lagerbestellungservicehangar getSelectedHangarBestellung() {
    return selectedHangarBestellung;
  }

  public void setSelectedHangarBestellung(Lagerbestellungservicehangar selectedHangarBestellung) {
    this.selectedHangarBestellung = selectedHangarBestellung;
  }

}
