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

package de.klees.beans.banking;

import de.klees.beans.system.CONF;
import de.klees.beans.system.loginMB;
import de.klees.data.Bank;
import de.klees.data.Banktransfer;
import de.klees.data.Fluggesellschaft;
import de.klees.data.Fluggesellschaftmanager;
import de.klees.data.Mail;
import de.klees.data.Benutzer;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
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
public class bankingBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private Date SuchText;
  private List<Bank> Kontouebersicht;
  private List<Bank> selectedData;
  private List<Benutzer> userlist;
  private Bank Empfaenger;
  private Bank currentPosition;
  private Banktransfer selectedTermin;
  private Fluggesellschaft FlugGesellschaft;

  private final Calendar c = Calendar.getInstance();

  private double frmUeberweisungsBetrag;
  private String frmAuftraggeberKonto;
  private String frmAuftraggeber;
  private String frmEmpfaengerKonto;
  private String frmEmpfaenger;
  private String frmVerwendungsZweck;
  private int frmKostenstelle;

  private final int UserID;
  private final int FluggesellschaftID;
  private final int ManagerID;

  private boolean isLoaded;
  private final SimpleDateFormat formatWithTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private final SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
  private final DecimalFormat dfWaehrung = new DecimalFormat("#,##0.00");

  private boolean BankingUser;
  private boolean BankingFluggesellschaft;

  private boolean allowBank;
  private boolean allowBankTransfer;
  private boolean allowBankBuchhaltung;

  private boolean frmTerminUeberweisung;
  private Date frmAusfuehrungstermin;

  private String AuftraggeberKonto;

  @EJB
  BankFacade facadeBank;

  public bankingBean() {
    SuchText = new Date();
    FluggesellschaftID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftID");
    UserID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
    ManagerID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ManagerID");
    frmAusfuehrungstermin = new Date();

  }

  public List<Bank> getKontouebersicht() {

    if (!isLoaded) {
      isLoaded = true;

      if (BankingUser) {
        Kontouebersicht = facadeBank.findAllByKontoNummer((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("BankKonto"));
      } else if (BankingFluggesellschaft) {
        Kontouebersicht = facadeBank.findAllByKontoNummer((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftBankKonto"));
      }

    }

    return Kontouebersicht;
  }

  public List<Bank> getKontoUebersichtForAirline() {

    if (!isLoaded) {
      isLoaded = true;
      int AirlineID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftID");
      Kontouebersicht = facadeBank.findAllByAirline(AirlineID);
    }
    return Kontouebersicht;
  }

  public List<Bank> getEmpfaenger() {

    if (BankingUser) {
      return facadeBank.getEmpfaenger((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("BankKonto"));
    } else {
      return facadeBank.getEmpfaenger((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftBankKonto"));
    }

  }

  public List<Banktransfer> getTermine() {

    if (BankingFluggesellschaft) {
      AuftraggeberKonto = frmAuftraggeberKonto;
    } else {
      AuftraggeberKonto = facadeBank.readUserDaten(UserID).getBankKonto();
    }

    if (!AuftraggeberKonto.equals("")) {
      return facadeBank.getTermine(AuftraggeberKonto);
    } else {
      return null;
    }

  }

  public void deleteTerminUeberweisung() {
    facadeBank.removeTerminUeberweisung(selectedTermin);
  }

  public void onSucheStarten() throws ParseException {

    try {
      Date StartDatum = formatWithTime.parse(formatWithTime.format(SuchText));
      Date EndDatum = formatWithTime.parse(formatDate.format(SuchText) + " 23:59:59");

      if (BankingUser) {
        Kontouebersicht = facadeBank.findAllByDate(StartDatum, EndDatum, (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("BankKonto"));
      } else if (BankingFluggesellschaft) {
        Kontouebersicht = facadeBank.findAllByDate(StartDatum, EndDatum, (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftBankKonto"));
      }
      isLoaded = true;

    } catch (NullPointerException e) {
      NewMessage(loginMB.onSprache("Bank_msg_gueltigesDatumEingeben"));
    }
  }

  public void onSucheDetail() {

    if (frmVerwendungsZweck == null) {
      frmVerwendungsZweck = "";
    }

    isLoaded = true;

    if (BankingUser) {
      Kontouebersicht = facadeBank.findByDetail("%" + frmVerwendungsZweck + "%", frmKostenstelle, (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("BankKonto"));
    } else if (BankingFluggesellschaft) {
      Kontouebersicht = facadeBank.findByDetail("%" + frmVerwendungsZweck + "%", frmKostenstelle, (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftBankKonto"));
    }

  }

  public void onBenutzerBanking() {
    frmAuftraggeber = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName");
    frmAuftraggeberKonto = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("BankKonto");
    frmEmpfaenger = loginMB.onSprache("Bank_msg_wirdVonDerBankAutomatischEingetragen");
    setBankingUser(true);
  }

  public void onFluggesellschaftBanking() {
    frmAuftraggeber = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftBankKontoName");
    frmAuftraggeberKonto = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftBankKonto");
    frmEmpfaenger = loginMB.onSprache("Bank_msg_wirdVonDerBankAutomatischEingetragen");
    setBankingFluggesellschaft(true);
  }

  public void onIdsPruefen() {
    facadeBank.setFluggesellschaftID(
            (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftBankKonto"),
            (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftID")
    );
  }

  public void onViewAll() {
    isLoaded = false;
  }

  public void onTransfer() {
    boolean transfer = false;

    if (frmEmpfaengerKonto.contains(" ")) {
      frmEmpfaengerKonto = frmEmpfaengerKonto.substring(0, frmEmpfaengerKonto.indexOf(" "));
    }

    if (getFrmUeberweisungsBetrag() <= 0) {
      transfer = false;
      NewMessage(loginMB.onSprache("Bank_msg_minusUndNullNichtErlaubt"));
    } else {
      transfer = true;
    }

    if (transfer) {
      if (facadeBank.ifExistBankKonto(getFrmEmpfaengerKonto())) {
        Empfaenger = facadeBank.getBankDaten(getFrmEmpfaengerKonto());
        //System.out.println("Bankkonto gefunden");
        transfer = true;
      } else {
        transfer = false;
        NewMessage(loginMB.onSprache("Bank_msg_bankkontoNichtGefunden"));
      }

    }

    if (transfer) {
      //Kontostand prüfen
      String Konto = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("BankKonto");

      if (BankingFluggesellschaft) {
        Konto = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftBankKonto");
      }

      if (facadeBank.getBankSaldo(Konto) >= getFrmUeberweisungsBetrag()) {
        transfer = true;
      } else {
        transfer = false;
        if (frmTerminUeberweisung) {
          transfer = true;
        }
      }
    }

    if (transfer) {
      if (!frmTerminUeberweisung) {

        Bank newBuchung = new Bank();

        newBuchung.setUserID(UserID);
        newBuchung.setAbsenderKonto(getFrmAuftraggeberKonto());
        newBuchung.setAbsenderName(getFrmAuftraggeber());
        newBuchung.setKontoName(getFrmAuftraggeber());
        newBuchung.setAusfuehrungsDatum(new Date());
        newBuchung.setBankKonto(getFrmAuftraggeberKonto());
        newBuchung.setBetrag(getFrmUeberweisungsBetrag() - (getFrmUeberweisungsBetrag() * 2.0));
        newBuchung.setEmpfaengerKonto(Empfaenger.getEmpfaengerKonto());
        newBuchung.setEmpfaengerName(Empfaenger.getEmpfaengerName());
        newBuchung.setUeberweisungsDatum(new Date());
        newBuchung.setVerwendungsZweck(getFrmVerwendungsZweck());

        newBuchung.setTransportID(-1);
        newBuchung.setLeasinggesellschaftID(-1);
        newBuchung.setIndustrieID(-1);
        newBuchung.setFlugzeugBesitzerID(-1);

        newBuchung.setFluggesellschaftID(-1);
        newBuchung.setAirportID(-1);

        newBuchung.setIcao("");
        newBuchung.setObjektID(-1);
        newBuchung.setFlugzeugID(-1);
        newBuchung.setKostenstelle(-1);
        newBuchung.setPilotID(-1);

        facadeBank.create(newBuchung);

        //************** Gegenbuchung
        newBuchung = new Bank();
        newBuchung.setUserID(Empfaenger.getUserID());
        newBuchung.setBankKonto(Empfaenger.getEmpfaengerKonto());
        newBuchung.setKontoName(Empfaenger.getKontoName());
        newBuchung.setAbsenderKonto(getFrmAuftraggeberKonto());
        newBuchung.setAbsenderName(getFrmAuftraggeber());
        newBuchung.setAusfuehrungsDatum(new Date());
        newBuchung.setBetrag(getFrmUeberweisungsBetrag());
        newBuchung.setEmpfaengerKonto(Empfaenger.getEmpfaengerKonto());
        newBuchung.setEmpfaengerName(Empfaenger.getEmpfaengerName());
        newBuchung.setUeberweisungsDatum(new Date());
        newBuchung.setVerwendungsZweck(getFrmVerwendungsZweck());

        newBuchung.setTransportID(-1);
        newBuchung.setLeasinggesellschaftID(-1);
        newBuchung.setIndustrieID(-1);
        newBuchung.setFlugzeugBesitzerID(-1);
        newBuchung.setFluggesellschaftID(-1);
        newBuchung.setAirportID(-1);

        newBuchung.setIcao("");
        newBuchung.setObjektID(-1);
        newBuchung.setFlugzeugID(-1);
        newBuchung.setKostenstelle(-1);
        newBuchung.setPilotID(-1);

        facadeBank.create(newBuchung);

        //MOD Benachritigung, wenn mehr als 1.000.000€ überwiesen werden.
//        if (getFrmUeberweisungsBetrag() >= 1000000.0) {
//
//          String Nachricht = "Vom Konto: " + getFrmAuftraggeberKonto() + " " + frmAuftraggeber
//                  + "<br>wurden: " + dfWaehrung.format(frmUeberweisungsBetrag) + " € überwiesen"
//                  + "<br>Empfaenger: " + Empfaenger.getEmpfaengerKonto() + " " + Empfaenger.getEmpfaengerName()
//                  + "<br>Verwendungszweck: " + getFrmVerwendungsZweck()
//                  + "<br><br>"
//                  + "Aktueller Kontostand Auftraggeber: " + dfWaehrung.format(facadeBank.getBankSaldo((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("BankKonto")));
//
//          String Betreff = "Bankueberweisung über: " + dfWaehrung.format(frmUeberweisungsBetrag) + " €";
//
//          saveMail("Stefan.Klees", "Stefan.Klees", Betreff, Nachricht);
//          saveMail("Toffi", "Toffi", Betreff, Nachricht);
//        }

        NewMessage(loginMB.onSprache("Bank_msg_UeberweisungErfolgreich"));
        isLoaded = false;
      } else {

        /*
        ********************* Terminueberweisung BEGINN
         */
        Banktransfer nT = new Banktransfer();
        nT.setUserID(UserID);
        nT.setEmpfaengerUserID(Empfaenger.getUserID());
        nT.setAbsenderKonto(getFrmAuftraggeberKonto());
        nT.setAbsenderName(getFrmAuftraggeber());
        nT.setKontoName(getFrmAuftraggeber());
        nT.setAusfuehrungsDatum(frmAusfuehrungstermin);
        nT.setBankKonto(getFrmAuftraggeberKonto());
        nT.setBetrag(getFrmUeberweisungsBetrag() - (getFrmUeberweisungsBetrag() * 2.0));
        nT.setEmpfaengerKonto(Empfaenger.getEmpfaengerKonto());
        nT.setEmpfaengerName(Empfaenger.getEmpfaengerName());
        nT.setVerwendungsZweck(getFrmVerwendungsZweck());

        facadeBank.createTerminUeberweisung(nT);

        //MOD Benachritigung, wenn mehr als 1.000.000€ überwiesen werden.
//        if (getFrmUeberweisungsBetrag() >= 1000000.0) {
//
//          String Nachricht = "Vom Konto: " + getFrmAuftraggeberKonto() + " " + frmAuftraggeber
//                  + "<br>wurden: " + dfWaehrung.format(frmUeberweisungsBetrag) + " € überwiesen"
//                  + "<br>Empfaenger: " + Empfaenger.getEmpfaengerKonto() + " " + Empfaenger.getEmpfaengerName()
//                  + "<br>Verwendungszweck: " + getFrmVerwendungsZweck()
//                  + "<br><br>";
//
//          saveMail("Stefan.Klees", "Stefan.Klees", "Bankueberweisung >= 1.000.000 €", Nachricht);
//        }

        /*
        ********************* Terminueberweisung ENDE
         */
      }
    }
  }

  public void onFibuSpeichern() {

    for (Bank data : selectedData) {
      if (BankingFluggesellschaft) {
        data.setFluggesellschaftID((int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftID"));
      }
      data.setKostenstelle(frmKostenstelle);
      facadeBank.edit(data);
      NewMessage("Saved - Primanota: " + data.getPrimanota());
    }
    isLoaded = true;
  }

  public int getKostenstelle() {
    frmKostenstelle = 0;
    for (Bank data : selectedData) {
      frmKostenstelle = data.getKostenstelle();
    }
    return frmKostenstelle;
  }

  public void onRefresh() {

    FlugGesellschaft = facadeBank.readFG(FluggesellschaftID);

    //System.out.println("de.klees.beans.airline.airlineBean.onRowSelect() FG ID " + FlugGesellschaft.getIdFluggesellschaft());
    if (FlugGesellschaft.getIdFluggesellschaft().equals(ManagerID)) {
      //System.out.println("de.klees.beans.airline.airlineBean.onRowSelect() Berechtigungen auslesen");

      Fluggesellschaftmanager mg = facadeBank.readManager(UserID, FlugGesellschaft.getIdFluggesellschaft());

      setAllowBank(mg.getAllowBank());
      setAllowBankBuchhaltung(mg.getAllowBankBuchhaltung());
      setAllowBankTransfer(mg.getAllowBankTransfer());

    } else {

      //System.out.println("de.klees.beans.airline.airlineBean.onRowSelect() default Berechtigung");
      setAllowBank(true);
      setAllowBankBuchhaltung(true);
      setAllowBankTransfer(true);

    }

  }

  public void onRowSelect(SelectEvent event) {
    getKostenstelle();

  }

  public void onRowUnselect(UnselectEvent event) {
  }

  private void NewMessage(String neueNachricht) {
    FacesMessage msg = new FacesMessage(neueNachricht);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  /*
  *************** Setter and Getter
   */
  public double[] getBankSaldo() {
    double banksaldo[] = new double[3];
    double summe = 0;
    double einnahmen = 0;
    double ausgaben = 0;

    for (int i = 0; i < Kontouebersicht.size(); i++) {
      summe = summe + Kontouebersicht.get(i).getBetrag();
      if (Kontouebersicht.get(i).getBetrag() < 0) {
        ausgaben = ausgaben + Kontouebersicht.get(i).getBetrag();
      } else {
        einnahmen = einnahmen + Kontouebersicht.get(i).getBetrag();
      }
    }

    banksaldo[0] = summe;
    banksaldo[1] = ausgaben;
    banksaldo[2] = einnahmen;

    return banksaldo;
  }

  /*
  Setter and Getter
   */
  public Date getSuchText() {
    return SuchText;
  }

  public void setSuchText(Date SuchText) {
    this.SuchText = SuchText;
  }

  public Bank getSelectedPosition() {
    return currentPosition;
  }

  public void setSelectedPosition(Bank selectedPosition) {
    this.currentPosition = selectedPosition;
  }

  public List<Benutzer> getListUser() {
    userlist = facadeBank.findAllUsers();
    return userlist;
  }

  public double getFrmUeberweisungsBetrag() {
    return frmUeberweisungsBetrag;
  }

  public void setFrmUeberweisungsBetrag(double frmUeberweisungsBetrag) {
    this.frmUeberweisungsBetrag = frmUeberweisungsBetrag;
  }

  public String getFrmAuftraggeber() {
    return frmAuftraggeber;
  }

  public void setFrmAuftraggeber(String frmAuftraggeber) {
    this.frmAuftraggeber = frmAuftraggeber;
  }

  public String getFrmEmpfaenger() {
    return frmEmpfaenger;
  }

  public void setFrmEmpfaenger(String frmEmpfaenger) {
    this.frmEmpfaenger = frmEmpfaenger;
  }

  public String getFrmAuftraggeberKonto() {
    return frmAuftraggeberKonto;
  }

  public void setFrmAuftraggeberKonto(String frmAuftraggeberKonto) {
    this.frmAuftraggeberKonto = frmAuftraggeberKonto;
  }

  public String getFrmEmpfaengerKonto() {
    return frmEmpfaengerKonto;
  }

  public void setFrmEmpfaengerKonto(String frmEmpfaengerKonto) {
    this.frmEmpfaengerKonto = frmEmpfaengerKonto;
  }

  public String getFrmVerwendungsZweck() {
    return frmVerwendungsZweck;
  }

  public void setFrmVerwendungsZweck(String frmVerwendungsZweck) {
    this.frmVerwendungsZweck = frmVerwendungsZweck;
  }

  public boolean isBankingUser() {
    return BankingUser;
  }

  public void setBankingUser(boolean BankingUser) {
    this.BankingUser = BankingUser;
  }

  public boolean isBankingFluggesellschaft() {
    return BankingFluggesellschaft;
  }

  public void setBankingFluggesellschaft(boolean BankingFluggesellschaft) {
    this.BankingFluggesellschaft = BankingFluggesellschaft;
  }

  public int getFrmKostenstelle() {
    return frmKostenstelle;
  }

  public void setFrmKostenstelle(int frmKostenstelle) {
    this.frmKostenstelle = frmKostenstelle;
  }

  public List<Bank> getSelectedData() {
    return selectedData;
  }

  public void setSelectedData(List<Bank> selectedData) {
    this.selectedData = selectedData;
  }

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

  public boolean isFrmTerminUeberweisung() {
    return frmTerminUeberweisung;
  }

  public void setFrmTerminUeberweisung(boolean frmTerminUeberweisung) {
    this.frmTerminUeberweisung = frmTerminUeberweisung;
  }

  public Date getFrmAusfuehrungstermin() {
    return frmAusfuehrungstermin;
  }

  public void setFrmAusfuehrungstermin(Date frmAusfuehrungstermin) {
    this.frmAusfuehrungstermin = frmAusfuehrungstermin;
  }

  public String getAuftraggeberKonto() {
    return AuftraggeberKonto;
  }

  public void setAuftraggeberKonto(String AuftraggeberKonto) {
    this.AuftraggeberKonto = AuftraggeberKonto;
  }

  public Banktransfer getSelectedTermin() {
    return selectedTermin;
  }

  public void setSelectedTermin(Banktransfer selectedTermin) {
    this.selectedTermin = selectedTermin;
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
    facadeBank.saveUserMail(nm);

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
      facadeBank.saveUserMail(nm);
    }

  }

  private Benutzer getUserDaten(String name) {
    return facadeBank.findUserByName(name);
  }

}
