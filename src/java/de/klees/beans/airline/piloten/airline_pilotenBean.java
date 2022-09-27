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

package de.klees.beans.airline.piloten;

import de.klees.beans.system.CONF;
import de.klees.beans.system.loginMB;
import de.klees.data.FlugesellschaftPiloten;
import de.klees.data.Fluggesellschaft;
import de.klees.data.FluggesellschaftBonusSystem;
import de.klees.data.Fluggesellschaftmanager;
import de.klees.data.FlugzeugeErlaubteUser;
import de.klees.data.Mail;
import de.klees.data.Benutzer;
import de.klees.data.views.ViewFlugzeugeMietKauf;
import java.io.Serializable;
import java.text.SimpleDateFormat;
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
public class airline_pilotenBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<FlugesellschaftPiloten> listPiloten;
  private List<FlugesellschaftPiloten> selectedPiloten;

  private FlugesellschaftPiloten currentPilot;
  private FlugzeugeErlaubteUser selectedErlaubterUser;
  private Benutzer userDaten;
  private Fluggesellschaft FlugGesellschaft;
  private ViewFlugzeugeMietKauf currentFlugzeug;

  private String suchText;
  private String PilotUserName;
  private boolean isLoadet;

  private int iduser;
  private final Map<String, String> UserStatus;

  private final int UserID;
  private final int FluggesellschaftID;
  private final int ManagerID;

  private String callname;
  private String email;
  private double bonus;
  private int status;
  private int kilometer;
  private int zeit;
  private int passagiere;
  private int waren;
  private double umsatz;
  private Date letzterFlug;
  private String AirlineName;
  private String UserName;

  private boolean allowPiloten;
  private boolean allowPilotenBearbeiten;
  private boolean allowPilotenEinstellen;
  private boolean allowPilotenEntlassen;

  private String frmFTWUsername;
  private Date frmStatistikvonDatum;
  private Date frmStatistikbisDatum;

  private String frmPinBetreff;
  private String frmPinText;

  // Pilotenswitch
  private int switchFluggesellschaft;

  //******* Variablen für Pilotenzuweisung zu einem Flugzeug
  private int PilotenID;

  private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

  @EJB
  FlugesellschaftPilotenFacade facadePiloten;

  public airline_pilotenBean() {
    setSuchText("%");

    FluggesellschaftID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftID");
    switchFluggesellschaft = FluggesellschaftID;

    UserID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
    UserName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName");
    ManagerID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ManagerID");

    setAirlineName(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftName").toString());
    isLoadet = false;

    UserStatus = new HashMap<>();
    UserStatus.put("Flugkapitän", "1");
    UserStatus.put("Pilot", "2");
    UserStatus.put("Co-Pilot", "3");
    UserStatus.put("Saftschubse", "4");

    frmStatistikvonDatum = new Date();
    frmStatistikbisDatum = new Date();

  }

  public List<FlugesellschaftPiloten> getListPiloten() {

    if (!isLoadet) {
      isLoadet = false;
      listPiloten = facadePiloten.findAllByCallName(suchText, FluggesellschaftID);
    }
    return listPiloten;
  }

  public List<Fluggesellschaft> getFluggesellschaften() {
    return facadePiloten.getFluggesellschaften(facadePiloten.readFG(FluggesellschaftID).getUserid());
  }

  public List<Benutzer> getListUser() {
    return facadePiloten.findAllUserOrderByName();
  }

  public List<FlugzeugeErlaubteUser> getErlaubteUser() {
    if (currentFlugzeug != null) {
      int FlugesellschaftID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftID");
      return facadePiloten.getErlaubteUser(FlugesellschaftID, currentFlugzeug.getIdMietKauf());
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public List<Object> getPilotenLog() {
    //int FlugesellschaftID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftID");
    if (currentPilot != null) {
      return facadePiloten.getPilotenLog(currentPilot.getIduser(), currentPilot.getIdflugesellschaft());
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public List<Object> getPilotenLogDatum() {
    int FlugesellschaftID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftID");
    return facadePiloten.getPilotenLogDatum(FlugesellschaftID, df.format(frmStatistikvonDatum), df.format(frmStatistikbisDatum));
  }

  public void onCreatePilot() {

    if (!frmFTWUsername.equals("")) {

      Benutzer pilot = getUserDaten(frmFTWUsername);

      boolean pilotExistiert = facadePiloten.ifExistPilotInAirline(FluggesellschaftID, pilot.getIdUser());

      UserName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName");

      System.out.println("de.klees.beans.airline.piloten.airline_pilotenBean.onCreatePilot() " + pilotExistiert);

      if (!pilotExistiert) {
        FlugesellschaftPiloten newPilot = new FlugesellschaftPiloten();

        newPilot.setIdflugesellschaft(FluggesellschaftID);
        newPilot.setIduser(pilot.getIdUser());
        newPilot.setCallname(pilot.getName1());
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
        newPilot.setDarfkonvertieren(false);

        facadePiloten.create(newPilot);
        setSelectedPilot(newPilot);

        saveMail(UserName, frmFTWUsername, loginMB.onSprache("Fluggesellschaften_Piloten_msg_AufnahmeBetreff") + " " + AirlineName + " *** ", loginMB.onSprache("Fluggesellschaften_Piloten_msg_AufnahmeInFluggesellschaft1") + " " + AirlineName + " " + loginMB.onSprache("Fluggesellschaften_Piloten_msg_AufnahmeInFluggesellschaft2"));

        NewMessage(loginMB.onSprache("Fluggesellschaften_Piloten_msg_PilotAngelegt"));
      } else {
        NewMessage("Pilot ist schon in der Fluggesellschaft als Pilot eingetragen");
      }

    } else {
      NewMessage("Username wurde nicht gefunden");
    }

  }

  public void onSavePilot() {
    if (currentPilot != null) {
      if (!facadePiloten.ifExistPilotInAirline(currentPilot.getIduser(), switchFluggesellschaft)) {

        try {
          if (currentPilot.getBonus() == null) {
            currentPilot.setBonus(0.0);
          }
        } catch (NullPointerException e) {
          currentPilot.setBonus(0.0);
        }

        currentPilot.setIdflugesellschaft(switchFluggesellschaft);
      }
      facadePiloten.edit(currentPilot);
      NewMessage(loginMB.onSprache("Fluggesellschaften_Piloten_msg_PilotGespeichert"));
    }
  }

  public void onDeletePilot() {

    UserName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName");

    String PilotName = "";

    for (FlugesellschaftPiloten pilots : selectedPiloten) {

      try {

        Benutzer user = facadePiloten.readUser(pilots.getIduser());
        PilotName = user.getName1();
        saveMail(UserName, PilotName, loginMB.onSprache("Fluggesellschaften_Piloten_msg_EntlassungBetreff") + " " + AirlineName + " *** ", loginMB.onSprache("Fluggesellschaften_Piloten_msg_EntlassungAusFluggesellschaft1") + " " + AirlineName + " " + loginMB.onSprache("Fluggesellschaften_Piloten_msg_EntlassungAusluggesellschaft2"));

      } catch (NullPointerException e) {
        PilotName = "Kein bestehender User";
      }

      facadePiloten.remove(pilots);
      NewMessage(loginMB.onSprache("Fluggesellschaften_Piloten_msg_pilotLoeschen"));

    }

  }

  private Benutzer getUserDaten(String name) {
    userDaten = facadePiloten.findUserByName(name);
    return userDaten;
  }

  private Benutzer findUserByID(int userID) {
    userDaten = facadePiloten.findUserByID(userID);
    return userDaten;
  }

  public void onSwitchPilot() {
    if (facadePiloten.ifExistPilotInAirline(currentPilot.getIduser(), switchFluggesellschaft)) {
      switchFluggesellschaft = currentPilot.getIdflugesellschaft();
    }
  }

  public void onSuche() {
    isLoadet = false;
  }

  public void onRefresh() {

    FlugGesellschaft = facadePiloten.readFG(FluggesellschaftID);

    //System.out.println("de.klees.beans.airline.airlineBean.onRowSelect() FG ID " + FlugGesellschaft.getIdFluggesellschaft());
    if (FlugGesellschaft.getIdFluggesellschaft().equals(ManagerID)) {
      //System.out.println("de.klees.beans.airline.airlineBean.onRowSelect() Berechtigungen auslesen");

      Fluggesellschaftmanager mg = facadePiloten.readManager(UserID, FlugGesellschaft.getIdFluggesellschaft());
      setAllowPiloten(mg.getAllowPiloten());
      setAllowPilotenBearbeiten(mg.getAllowPilotenBearbeiten());
      setAllowPilotenEinstellen(mg.getAllowPilotenEinstellen());
      setAllowPilotenEntlassen(mg.getAllowPilotenEntlassen());
    } else {

      //System.out.println("de.klees.beans.airline.airlineBean.onRowSelect() default Berechtigung");
      setAllowPiloten(true);
      setAllowPilotenBearbeiten(true);
      setAllowPilotenEinstellen(true);
      setAllowPilotenEntlassen(true);
    }

  }

  public void onRundbrief() {

    for (FlugesellschaftPiloten pilots : selectedPiloten) {
      saveMail(findUserByID(UserID).getName1(), findUserByID(pilots.getIduser()).getName1(), frmPinBetreff, frmPinText);
    }

  }

  public void onUserZuFlugzeugHinzufuegen() {
    if (currentFlugzeug != null) {
      if (!facadePiloten.ifExistUserFromFlugzeug(PilotenID, currentFlugzeug.getIdMietKauf())) {

        FlugzeugeErlaubteUser newUser = new FlugzeugeErlaubteUser();

        newUser.setIdFluggesellschaft(FluggesellschaftID);
        newUser.setIdFlugzeugeMietKauf(currentFlugzeug.getIdMietKauf());
        newUser.setIdUser(PilotenID);

        facadePiloten.createUserFromFlugzeug(newUser);

      } else {
        NewMessage(loginMB.onSprache("Hangar_dlg_PilotenZuweisen_msg_PilotHatErlaubnis"));
      }

    }
  }

  public void onUserAusFlugzeugEntfernen() {

    if (currentFlugzeug != null) {
      if (facadePiloten.ifExistUserFromFlugzeug(selectedErlaubterUser.getIdUser(), selectedErlaubterUser.getIdFlugzeugeMietKauf())) {
        facadePiloten.removeUserFromFlugzeug(selectedErlaubterUser);
        NewMessage(loginMB.onSprache("Hangar_dlg_PilotenZuweisen_msg_PilotGeloescht"));
      } else {
        NewMessage(loginMB.onSprache("Hangar_dlg_PilotenZuweisen_msg_PilotLoeschungFehler"));
      }
    }
  }

  public String BonusBezeichnungStatus(int uid) {

    FlugesellschaftPiloten pilot = facadePiloten.readPilot(FluggesellschaftID, uid);

    String BoniBezeichnung = "";

    if (pilot != null) {

      List<FluggesellschaftBonusSystem> bonuslist = facadePiloten.getBoniList(FluggesellschaftID);

      for (FluggesellschaftBonusSystem boni : bonuslist) {
        if (pilot.getZeit() >= boni.getZeit()) {
          //bonusProzent = boni.getBonus();
          BoniBezeichnung = boni.getBonusname();
        }
      }

    }

    return BoniBezeichnung;
  }

  public void onRowSelect(SelectEvent event) {

    try {
      currentPilot = selectedPiloten.get(0);
      setPilotUserName(findUserByID(currentPilot.getIduser()).getName1());
      switchFluggesellschaft = currentPilot.getIdflugesellschaft();
    } catch (NullPointerException e) {
      setPilotUserName("");
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
  public String getSuchText() {
    return suchText;
  }

  public void setSuchText(String suchText) {
    this.suchText = suchText;
  }

  public FlugesellschaftPiloten getSelectedPilot() {
    return currentPilot;
  }

  public void setSelectedPilot(FlugesellschaftPiloten selectedPilot) {
    this.currentPilot = selectedPilot;
  }

  public String getTimeZone() {
    return Calendar.getInstance().getTimeZone().getID();
  }

  public String getPilotUserName() {
    return PilotUserName;
  }

  public void setPilotUserName(String PilotUserName) {
    this.PilotUserName = PilotUserName;
  }

  public int getIduser() {
    return iduser;
  }

  public void setIduser(int iduser) {
    this.iduser = iduser;
  }

  public String getCallname() {
    return callname;
  }

  public void setCallname(String callname) {
    this.callname = callname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public double getBonus() {
    return bonus;
  }

  public void setBonus(double bonus) {
    this.bonus = bonus;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getKilometer() {
    return kilometer;
  }

  public void setKilometer(int kilometer) {
    this.kilometer = kilometer;
  }

  public int getZeit() {
    return zeit;
  }

  public void setZeit(int zeit) {
    this.zeit = zeit;
  }

  public int getPassagiere() {
    return passagiere;
  }

  public void setPassagiere(int passagiere) {
    this.passagiere = passagiere;
  }

  public int getWaren() {
    return waren;
  }

  public void setWaren(int waren) {
    this.waren = waren;
  }

  public double getUmsatz() {
    return umsatz;
  }

  public void setUmsatz(double umsatz) {
    this.umsatz = umsatz;
  }

  public Date getLetzterFlug() {
    return letzterFlug;
  }

  public void setLetzterFlug(Date letzterFlug) {
    this.letzterFlug = letzterFlug;
  }

  public Map<String, String> getUserStatus() {
    return UserStatus;
  }

  public String getAirlineName() {
    return AirlineName;
  }

  public void setAirlineName(String AirlineName) {
    this.AirlineName = AirlineName;
  }

  public String getUserName(int userid) {
    return facadePiloten.getUserName(userid);
  }

  public String getFrmFTWUsername() {
    return frmFTWUsername;
  }

  public void setFrmFTWUsername(String frmFTWUsername) {
    this.frmFTWUsername = frmFTWUsername;
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

  public Date getFrmStatistikvonDatum() {
    return frmStatistikvonDatum;
  }

  public void setFrmStatistikvonDatum(Date frmStatistikvonDatum) {
    this.frmStatistikvonDatum = frmStatistikvonDatum;
  }

  public Date getFrmStatistikbisDatum() {
    return frmStatistikbisDatum;
  }

  public void setFrmStatistikbisDatum(Date frmStatistikbisDatum) {
    this.frmStatistikbisDatum = frmStatistikbisDatum;
  }

  public List<FlugesellschaftPiloten> getSelectedPiloten() {
    return selectedPiloten;
  }

  public void setSelectedPiloten(List<FlugesellschaftPiloten> selectedPiloten) {
    this.selectedPiloten = selectedPiloten;
  }

  public String getFrmPinBetreff() {
    return frmPinBetreff;
  }

  public void setFrmPinBetreff(String frmPinBetreff) {
    this.frmPinBetreff = frmPinBetreff;
  }

  public String getFrmPinText() {
    return frmPinText;
  }

  public void setFrmPinText(String frmPinText) {
    this.frmPinText = frmPinText;
  }

  public ViewFlugzeugeMietKauf getCurrentFlugzeug() {
    return currentFlugzeug;
  }

  public void setCurrentFlugzeug(ViewFlugzeugeMietKauf currentFlugzeug) {
    this.currentFlugzeug = currentFlugzeug;
  }

  public int getPilotenID() {
    return PilotenID;
  }

  public void setPilotenID(int PilotenID) {
    this.PilotenID = PilotenID;
  }

  public FlugzeugeErlaubteUser getSelectedErlaubterUser() {
    return selectedErlaubterUser;
  }

  public void setSelectedErlaubterUser(FlugzeugeErlaubteUser selectedErlaubterUser) {
    this.selectedErlaubterUser = selectedErlaubterUser;
  }

  public int getSwitchFluggesellschaft() {
    return switchFluggesellschaft;
  }

  public void setSwitchFluggesellschaft(int switchFluggesellschaft) {
    this.switchFluggesellschaft = switchFluggesellschaft;
  }

  public void saveMail(String Von, String An, String Betreff, String Nachricht) {

    //Absendermail speichern
    Benutzer absender = getUserDaten(Von);
    Benutzer empfaenger = getUserDaten(An);

    Mail nm = new Mail();
    if (!An.equals(Von)) {
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
      facadePiloten.saveUserMail(nm);
    }

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
      facadePiloten.saveUserMail(nm);
    }

  }

}
