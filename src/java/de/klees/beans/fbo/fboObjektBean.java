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

package de.klees.beans.fbo;

import de.klees.beans.system.loginMB;
import de.klees.data.Airport;
import de.klees.data.Bank;
import de.klees.data.FboObjekte;
import de.klees.data.FboUserObjekte;
import de.klees.data.Fluggesellschaftmanager;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
public class fboObjektBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private FboObjekte currentFboObjekt;
  private List<FboObjekte> fboList;

  private boolean isLoaded;
  private String frmFboObjektName;
  private int UserID;

  private String frmICAO;
  private final Calendar c = Calendar.getInstance();
  private SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");

  private int frmMietPreis;
  private int frmAnzahlRouten;
  private int frmAnzahlStellplaetze;
  private int frmAnzahlPersonal;
  private boolean frmTankstelle;
  private boolean frmSpritlager;
  private boolean frmFBO;
  private boolean frmAbfertigungsTerminal;
  private int frmTerminalMaxPax;
  private boolean frmServiceHangar;
  private boolean frmBusinessLounge;
  private int frmTankstelleMaxFuelKG;
  private int frmKlasse;
  private int frmServiceHangarQM;
  private double frmServiceHangarPreisQM;
  private int frmFluggesellschaftID;

  private String UserName;
  private String UserKonto;
  private String frmBankkonto;

  private boolean mietProzessLaeuft;

  @EJB
  FboObjekteFacade fbofacade;

  public fboObjektBean() {
    UserName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName");
    UserKonto = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("BankKonto");

    UserID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
    isLoaded = false;
    frmKlasse = 0;
    mietProzessLaeuft = false;
  }

  public List<FboObjekte> getFBOObjekte() {
    if (!isLoaded) {
      fboList = fbofacade.findAllFboByKlasse(frmKlasse);
      isLoaded = true;
    }
    return fboList;
  }

  public void onChangeKlasse() {
    isLoaded = false;
    fboList = fbofacade.findAllFboByKlasse(frmKlasse);
  }

  public void fboObjekteSuchen() {
    Airport airport = fbofacade.getAirportInfo(frmICAO);
    if (airport != null) {
      frmKlasse = airport.getKlasse();
    } else {
      NewMessage(loginMB.onSprache("FBOManagement_msg_FlughafenNichtGefunden"));
      frmKlasse = 0;
    }
    fboList = fbofacade.findAllFboByKlasse(frmKlasse);
  }

  public void onCreate() {

    if (frmServiceHangar) {
      frmMietPreis = frmServiceHangarQM * (int) frmServiceHangarPreisQM;
    }

    FboObjekte newObjekt = new FboObjekte();
    newObjekt.setAnzahlPersonal(frmAnzahlPersonal);
    newObjekt.setAnzahlRouten(frmAnzahlRouten);
    newObjekt.setAnzahlStellplaetze(frmAnzahlStellplaetze);
    newObjekt.setFbo(frmFBO);
    newObjekt.setMietPreis(frmMietPreis);
    newObjekt.setObjektName(frmFboObjektName);
    newObjekt.setTankstelle(frmTankstelle);
    newObjekt.setSpritlager(frmSpritlager);
    newObjekt.setServicehangar(frmServiceHangar);
    newObjekt.setBusinessLounge(frmBusinessLounge);
    newObjekt.setTankstelleMaxFuelKG(frmTankstelleMaxFuelKG);
    newObjekt.setAbfertigungsTerminal(frmAbfertigungsTerminal);
    newObjekt.setTerminalMaxPax(frmTerminalMaxPax);
    newObjekt.setKlasse(frmKlasse);
    newObjekt.setServicehangarQM(frmServiceHangarQM);
    newObjekt.setServicehangarQMPreis(frmServiceHangarPreisQM);

    fbofacade.create(newObjekt);

    NewMessage(loginMB.onSprache("FBOManagement_msg_FBOObjektGespeichert"));

    isLoaded = false;

  }

  public void onKopie() {
    if (currentFboObjekt != null) {
      currentFboObjekt.setKlasse(frmKlasse);
      fbofacade.create(currentFboObjekt);
      isLoaded = false;
    } else {
      NewMessage(loginMB.onSprache("FBOManagement_msg_KeinObjektZumKopierenGewaehlt"));
    }

  }

  public void onSave() {

    if (currentFboObjekt.getServicehangar()) {
      currentFboObjekt.setMietPreis(currentFboObjekt.getServicehangarQM() * currentFboObjekt.getServicehangarQMPreis().intValue());
    }

    fbofacade.edit(currentFboObjekt);
    NewMessage(loginMB.onSprache("FBOManagement_msg_FBOObjektGespeichert"));
    isLoaded = false;
  }

  public void onDelete() {

    if (!fbofacade.ifExistFBOUserObjekt(currentFboObjekt.getIdObjekt())) {
      fbofacade.remove(currentFboObjekt);
      NewMessage(loginMB.onSprache("FBOManagement_msg_FBOObjektGeloescht"));
      isLoaded = false;
    } else {
      NewMessage("FBO-Objekt in der FTW-Welt schon verbaut");
    }

  }

  public void onMieten() {

    boolean kauf = false;

    frmICAO = frmICAO.trim();
    frmICAO = frmICAO.toUpperCase();
    
    Airport airport = null;
    double aufschlag = 0.0;

    if (currentFboObjekt != null && !mietProzessLaeuft) {
      kauf = true;
      mietProzessLaeuft = true;
    }

    // Ohne Fluggesellschaft keine FBO Objekte
    if (kauf) {
      if (frmFluggesellschaftID < 1) {
        kauf = false;
        NewMessage(loginMB.onSprache("FBOManagement_msg_EsFehltEineFluggesellschaft"));
      } else {
        // ************* Benutzer ID Switchen damit die Objekte dem richtigen Benutzer zugeordnet werden
        UserID = fbofacade.readFG(frmFluggesellschaftID).getUserid();
      }
    }

    if (kauf) {
      //System.out.println("de.klees.beans.fbo.fboObjektBean.onMieten() Konto " + getFrmBankkonto());
      if (fbofacade.getBankSaldoUeberKonto(getFrmBankkonto()) >= currentFboObjekt.getMietPreis()) {
        kauf = true;
      } else {
        NewMessage(loginMB.onSprache("FBOManagement_msg_nichtGenugGeld"));
        kauf = false;
      }
    }

    if (kauf) {
      if (fbofacade.ifExistFlughafen(frmICAO.toUpperCase())) {
        kauf = true;
      } else {
        kauf = false;
        NewMessage(loginMB.onSprache("FBOManagement_msg_FlughafenNichtGefunden") + " : " + frmICAO);
      }
    }

    // Bei Klasse 9, 12,13,14 keine FBO erlaubt.
    if (kauf) {
      airport = fbofacade.getAirportInfo(frmICAO);
      if (airport.getKlasse() == 12 | airport.getKlasse() == 9 | airport.getKlasse() == 13 | airport.getKlasse() == 14) {
        kauf = false;
        NewMessage(loginMB.onSprache("FBOManagement_msg_FBONichtErlaubtAnDiesemFlughafen") + " : " + frmICAO);
      }

    }

    // Hat der Benutzer eine FBO an dem Flughafen an dem das Objekt gemietet werden soll.
    // evtl. Außnahmen kommen noch
    if (kauf) {
      
      if (fbofacade.hatUserFBOAmFlughafen(UserID, frmICAO)) {
        kauf = true;
        // Der Benutzer darf nur eine FBO pro Flughafen mieten
        if (fbofacade.wievieleFBOHatUserAmFlughafen(UserID, frmICAO) > 0 && currentFboObjekt.getFbo()) {
          NewMessage(loginMB.onSprache("FBOManagement_msg_DuHastSchonEineFBOAnDiesemFlughafen"));
          kauf = false;
        }
      } else {
        kauf = false;
        if (currentFboObjekt.getFbo()) {
          kauf = true;
        } else {
          NewMessage(loginMB.onSprache("FBOManagement_msg_EsFehltEineFBO"));
        }
      }
    }


    /*
    *********** Tankstelle Pruefungen
     */
    if (kauf) {
      if (currentFboObjekt.getTankstelle()) {

        if (fbofacade.wievieleTankstellenHatUserAmFlughafen(UserID, frmICAO) > 0) {
          NewMessage(loginMB.onSprache("FBOManagement_msg_TankstelleSchonVorhanden"));
          kauf = false;
        }
      }
    }

    /*
    *********** Spritlager Pruefungen
     */
    if (kauf) {
      if (currentFboObjekt.getSpritlager()) {

        if (fbofacade.wievieleTankstellenHatUserAmFlughafen(UserID, frmICAO) > 0) {
          NewMessage(loginMB.onSprache("FBOManagement_msg_TankstelleSchonVorhanden"));
          kauf = false;
        }

        if (kauf) {
          if (!fbofacade.getAirportInfo(frmICAO).getLuftversorgung()) {
            NewMessage(loginMB.onSprache("FBOManagement_msg_KeinSpritlagerMoeglich"));
            kauf = false;
          }
        }

      }

    }

    /*
    *********** Terminal Pruefungen
     */
    if (kauf) {
      if (currentFboObjekt.getAbfertigungsTerminal()) {
        if (fbofacade.wievieleTerminalsHatUserAmFlughafen(UserID, frmICAO) > 0) {
          NewMessage(loginMB.onSprache("FBOManagement_msg_TerminalSchonVorhanden"));
          kauf = false;
        }
      }
    }

    // Preiserhöhungen ermitteln
    if (kauf) {
      if (airport != null) {
        //double prozente = CONF.getFlughafenAufschlag(airport.getKlasse());
        double prozente = 10.0;
        long exFbo = fbofacade.wievieleFBOAmFlughafen(frmICAO);

//        if (exFbo > 0) {
//          exFbo = exFbo - 1;
//        }
        //aufschlag = prozente * exFbo;
        aufschlag = 0;
      } else {
        kauf = false;
      }
    }

    if (kauf) {
      if (!MietBerechtigung()) {
        NewMessage(loginMB.onSprache("FBOManagement_msg_KeineBerechtigung"));
        kauf = false;
      }
    }

    if (kauf) {

      double betrag = currentFboObjekt.getMietPreis() - (currentFboObjekt.getMietPreis() * 2);

      betrag = (betrag * ((100 + aufschlag) / 100));

      String VerwendungsZweck = loginMB.onSprache("FBOManagement_msg_Bank_MietZahlung") + " Icao: " + frmICAO + "   " + currentFboObjekt.getObjektName() + loginMB.onSprache("FBOManagement_msg_Bank_Mietaufschlag") + Math.floor(aufschlag) + " %";

      UserKonto = getFrmBankkonto();
      UserName = fbofacade.getBankkontoName(UserKonto);

      SaveBankbuchung(UserKonto, UserName, UserKonto, UserName, new Date(), betrag, "500-1000001", "**** FTW BANK *****",
              new Date(), VerwendungsZweck, UserID, -1, -1, -1, -1, -1, -1, "", -1, -1, -1, -1);

      Calendar naechsteMiete = Calendar.getInstance();
      naechsteMiete.add(Calendar.DATE, 30);

      FboUserObjekte newMiete = new FboUserObjekte();
      newMiete.setFaelligkeitNaechsteMiete(naechsteMiete.getTime());
      newMiete.setBestandAVGASkg(0);
      newMiete.setBestandJETAkg(0);
      newMiete.setEinkaufsPreisAVGAS(0.0);
      newMiete.setEinkaufsPreisJETA(0.0);
      newMiete.setPreisAVGAS(0.0);
      newMiete.setPreisJETA(0.0);
      newMiete.setIcao(frmICAO.toUpperCase());
      newMiete.setIdUser(UserID);
      newMiete.setIdfboObjekt(currentFboObjekt.getIdObjekt());
      newMiete.setMietbeginn(c.getTime());
      newMiete.setLetzteMietzahlung(c.getTime());
      newMiete.setName(currentFboObjekt.getObjektName());
      newMiete.setPreisArbeitseinheit(0.0);
      newMiete.setTerminalGebuehrInProzent(3.5);
      newMiete.setBankkonto(UserKonto);
      newMiete.setKontoName(UserName);
      newMiete.setKostenstelle(0);
      newMiete.setIdfluggesellschaft(frmFluggesellschaftID);
      newMiete.setMahnStufe(0);

      if (currentFboObjekt.getTankstelle()) {
        newMiete.setGrafikLink("http://www.ftw-sim.de/images/FTW/images/tankstelle.png");
      } else {
        newMiete.setGrafikLink("");
      }

      fbofacade.onMieten(newMiete);
      NewMessage(loginMB.onSprache("FBOManagement_msg_FBOObjektGespeichert"));

      // UserID wieder auf Benutzer ID setzen
      UserID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");

    }
    mietProzessLaeuft = false;
  }

  private boolean MietBerechtigung() {

    int idUser = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
    int idfg = fbofacade.readUser(idUser).getFluggesellschaftManagerID();

    // ******  Besitzer der Fluggesellschaft
    if (idfg == -1) {
      return true;
    }

    int OwnerFG = fbofacade.readFG(idfg).getUserid();

    // ist Besitzer der Fluggesellschaft = Benutzer
    if (OwnerFG == idUser) {
      return true;
    }

    if (idfg == frmFluggesellschaftID) {
      // ****************  Benutzer ist Besitzer
      return true;
    }

    if (frmFluggesellschaftID != idfg) {
      // ist user der Besiter der frmFluggesellschaft
      if (fbofacade.readMyFg(idUser, frmFluggesellschaftID).getIdFluggesellschaft() == frmFluggesellschaftID) {
        return true;
      } else {
        // user ist Manager Berechtigungen auslesen
        Fluggesellschaftmanager mg = fbofacade.readManager(idUser, idfg);
        if (mg.getAllowFBOHinzufuegen()) {
          return true;
        }

      }
    }

    return false;
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
  public FboObjekte getSelectedFboObjekt() {
    return currentFboObjekt;
  }

  public void setSelectedFboObjekt(FboObjekte selectedFboObjekt) {
    this.currentFboObjekt = selectedFboObjekt;
  }

  public String getFrmFboObjektName() {
    return frmFboObjektName;
  }

  public void setFrmFboObjektName(String frmFboObjektName) {
    this.frmFboObjektName = frmFboObjektName;
  }

  public int getFrmMietPreis() {
    return frmMietPreis;
  }

  public void setFrmMietPreis(int frmMietPreis) {
    this.frmMietPreis = frmMietPreis;
  }

  public int getFrmAnzahlRouten() {
    return frmAnzahlRouten;
  }

  public void setFrmAnzahlRouten(int frmAnzahlRouten) {
    this.frmAnzahlRouten = frmAnzahlRouten;
  }

  public int getFrmAnzahlStellplaetze() {
    return frmAnzahlStellplaetze;
  }

  public void setFrmAnzahlStellplaetze(int frmAnzahlStellplaetze) {
    this.frmAnzahlStellplaetze = frmAnzahlStellplaetze;
  }

  public int getFrmAnzahlPersonal() {
    return frmAnzahlPersonal;
  }

  public void setFrmAnzahlPersonal(int frmAnzahlPersonal) {
    this.frmAnzahlPersonal = frmAnzahlPersonal;
  }

  public boolean isFrmTankstelle() {
    return frmTankstelle;
  }

  public void setFrmTankstelle(boolean frmTankstelle) {
    this.frmTankstelle = frmTankstelle;
  }

  public boolean isFrmFBO() {
    return frmFBO;
  }

  public void setFrmFBO(boolean frmFBO) {
    this.frmFBO = frmFBO;
  }

  public boolean isFrmServiceHangar() {
    return frmServiceHangar;
  }

  public void setFrmServiceHangar(boolean frmServiceHangar) {
    this.frmServiceHangar = frmServiceHangar;
  }

  public boolean isFrmBusinessLounge() {
    return frmBusinessLounge;
  }

  public void setFrmBusinessLounge(boolean frmBusinessLounge) {
    this.frmBusinessLounge = frmBusinessLounge;
  }

  public String getFrmICAO() {
    return frmICAO;
  }

  public void setFrmICAO(String frmICAO) {
    this.frmICAO = frmICAO;
  }

  public int getFrmTankstelleMaxFuelKG() {
    return frmTankstelleMaxFuelKG;
  }

  public void setFrmTankstelleMaxFuelKG(int frmTankstelleMaxFuelKG) {
    this.frmTankstelleMaxFuelKG = frmTankstelleMaxFuelKG;
  }

  public boolean isFrmAbfertigungsTerminal() {
    return frmAbfertigungsTerminal;
  }

  public void setFrmAbfertigungsTerminal(boolean frmAbfertigungsTerminal) {
    this.frmAbfertigungsTerminal = frmAbfertigungsTerminal;
  }

  public int getFrmTerminalMaxPax() {
    return frmTerminalMaxPax;
  }

  public void setFrmTerminalMaxPax(int frmTerminalMaxPax) {
    this.frmTerminalMaxPax = frmTerminalMaxPax;
  }

  public String getUserName() {
    return UserName;
  }

  public void setUserName(String UserName) {
    this.UserName = UserName;
  }

  public String getUserKonto() {
    return UserKonto;
  }

  public void setUserKonto(String UserKonto) {
    this.UserKonto = UserKonto;
  }

  public int getFrmKlasse() {
    return frmKlasse;
  }

  public void setFrmKlasse(int frmKlasse) {
    this.frmKlasse = frmKlasse;
  }

  public String getFrmBankkonto() {
    return frmBankkonto;
  }

  public void setFrmBankkonto(String frmBankkonto) {
    this.frmBankkonto = frmBankkonto;
  }

  public int getFrmFluggesellschaftID() {
    return frmFluggesellschaftID;
  }

  public void setFrmFluggesellschaftID(int frmFluggesellschaftID) {
    this.frmFluggesellschaftID = frmFluggesellschaftID;
  }

  public boolean isFrmSpritlager() {
    return frmSpritlager;
  }

  public void setFrmSpritlager(boolean frmSpritlager) {
    this.frmSpritlager = frmSpritlager;
  }

  public int getFrmServiceHangarQM() {
    return frmServiceHangarQM;
  }

  public void setFrmServiceHangarQM(int frmServiceHangarQM) {
    this.frmServiceHangarQM = frmServiceHangarQM;
  }

  public double getFrmServiceHangarPreisQM() {
    return frmServiceHangarPreisQM;
  }

  public void setFrmServiceHangarPreisQM(double frmServiceHangarPreisQM) {
    this.frmServiceHangarPreisQM = frmServiceHangarPreisQM;
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

    fbofacade.createBankbuchung(newBuchung);

  }

}
