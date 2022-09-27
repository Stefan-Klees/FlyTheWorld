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
package de.klees.beans.airline.manager;

import de.klees.beans.system.loginMB;
import de.klees.data.Fluggesellschaft;
import de.klees.data.Fluggesellschaftmanager;
import de.klees.data.Benutzer;
import de.klees.data.views.ViewFluggesellschaftManager;
import java.io.Serializable;
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
public class ManagerBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private ViewFluggesellschaftManager selectedManager;
  private List<ViewFluggesellschaftManager> ManagerList;
  private Fluggesellschaft FlugGesellschaft;

  private final int FluggesellschaftID;
  private String FluggesellschaftName;
  private final int UserID;
  private final int ManagerID;

  private String frmFTWBenutzername;

  private boolean isLoaded;

  private boolean allowManager;
  private boolean allowManagerBearbeiten;
  private boolean allowManagerEinstellen;
  private boolean allowManagerLoeschen;

  public ManagerBean() {
    isLoaded = false;
    FluggesellschaftID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftID");
    UserID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
    ManagerID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ManagerID");
  }

  @EJB
  ManagerFacade facadeManager;

  public List<ViewFluggesellschaftManager> getManager() {
    if (!isLoaded) {
      ManagerList = facadeManager.getAllManager(FluggesellschaftID);
      isLoaded = true;
    }
    return ManagerList;
  }

  public void onCreateManager() {

    Benutzer userdaten = facadeManager.getUserDaten(frmFTWBenutzername);
    FlugGesellschaft = facadeManager.readFG(FluggesellschaftID);

    if (userdaten == null) {
      NewMessage(loginMB.onSprache("Fluggesellschaften_Manager_msg_FTWBenutzerNichtGefunden"));
    }

    if (userdaten != null && userdaten.getFluggesellschaftManagerID() > 0) {
      NewMessage(userdaten.getName1() + " " + loginMB.onSprache("Fluggesellschaften_Manager_msg_UserIstSchonManager"));
      userdaten = null;
      isLoaded = false;
    }

    if (userdaten != null && FlugGesellschaft != null) {
      if (userdaten.getIdUser().equals(FlugGesellschaft.getUserid())) {
        userdaten = null;
        isLoaded = false;
        NewMessage(loginMB.onSprache("Fluggesellschaften_Manager_msg_BesitzerDarfKeinManagerSein"));
      }
    }

    if (userdaten != null) {
      //System.out.println("de.klees.beans.airline.manager.ManagerBean.onCreateManager()");
      Fluggesellschaftmanager mg = new Fluggesellschaftmanager();
      mg.setUserid(userdaten.getIdUser());
      mg.setGeschaeftsfuehrer(false);
      mg.setBezeichnung("");
      mg.setFluggesellschaftid(FluggesellschaftID);
      mg.setAllowBank(false);
      mg.setAllowBankBuchhaltung(false);
      mg.setAllowBankTransfer(false);
      mg.setAllowFluggesellschaftBearbeiten(false);
      mg.setAllowFluggesellschaftLoeschen(false);
      mg.setAllowFlugzeuge(false);
      mg.setAllowFlugzeugeBearbeiten(false);
      mg.setAllowFlugzeugeKaufen(false);
      mg.setAllowFlugzeugeTransfer(false);
      mg.setAllowFlugzeugeVerkaufen(false);
      mg.setAllowManager(false);
      mg.setAllowManagerBearbeiten(false);
      mg.setAllowManagerEinstellen(false);
      mg.setAllowManagerLoeschen(false);
      mg.setAllowPiloten(false);
      mg.setAllowPilotenBearbeiten(false);
      mg.setAllowPilotenEinstellen(false);
      mg.setAllowPilotenEntlassen(false);
      mg.setAllowRouten(false);
      mg.setAllowRoutenLoeschen(false);
      mg.setAllowRoutenBearbeiten(false);
      mg.setAllowRoutenErstellen(false);
      mg.setAllowFBO(false);
      mg.setAllowFBOBearbeiten(false);
      mg.setAllowFBOHinzufuegen(false);
      mg.setAllowFBOLoeschen(false);
      mg.setAllowFBOTankstelleVerwalten(false);

      facadeManager.createManager(mg);
      userdaten.setFluggesellschaftManagerID(FluggesellschaftID);
      facadeManager.saveUser(userdaten);
      isLoaded = false;
    }
  }

  public void onDeleteManager() {

    if (selectedManager != null) {
      String UserName = selectedManager.getBenutzerName();
      int id = selectedManager.getIdfluggesellschaftmanager();

      if (facadeManager.deleteManager(id)) {

        Benutzer userdaten = facadeManager.getUserDaten(selectedManager.getBenutzerName());
        if (userdaten != null) {
          userdaten.setFluggesellschaftManagerID(-1);
          facadeManager.saveUser(userdaten);
        }

        NewMessage(loginMB.onSprache("Fluggesellschaften_Manager_msg_ManagerWurdeEntlassen"));
        setSelectedManager(null);
        isLoaded = false;
      }

    } else {
      NewMessage(loginMB.onSprache("Fluggesellschaften_Manager_msg_KeinManagerAusgewaehlt"));
    }

  }

  public void onEditManager() {
    Fluggesellschaftmanager manager = facadeManager.findManager(selectedManager.getIdfluggesellschaftmanager());

    if (manager != null) {
      manager.setBezeichnung(selectedManager.getBezeichnung());
      manager.setGeschaeftsfuehrer(selectedManager.getGeschaeftsfuehrer());
      manager.setAllowBank(selectedManager.getAllowBank());
      manager.setAllowBankBuchhaltung(selectedManager.getAllowBankBuchhaltung());
      manager.setAllowBankTransfer(selectedManager.getAllowBankTransfer());
      manager.setAllowFluggesellschaftBearbeiten(selectedManager.getAllowFluggesellschaftBearbeiten());
      manager.setAllowFluggesellschaftLoeschen(selectedManager.getAllowFluggesellschaftLoeschen());
      manager.setAllowFlugzeuge(selectedManager.getAllowFlugzeuge());
      manager.setAllowFlugzeugeBearbeiten(selectedManager.getAllowFlugzeugeBearbeiten());
      manager.setAllowFlugzeugeKaufen(selectedManager.getAllowFlugzeugeKaufen());
      manager.setAllowFlugzeugeTransfer(selectedManager.getAllowFlugzeugeTransfer());
      manager.setAllowFlugzeugeVerkaufen(selectedManager.getAllowFlugzeugeVerkaufen());
      manager.setAllowManager(selectedManager.getAllowManager());
      manager.setAllowManagerBearbeiten(selectedManager.getAllowManagerBearbeiten());
      manager.setAllowManagerEinstellen(selectedManager.getAllowManagerEinstellen());
      manager.setAllowManagerLoeschen(selectedManager.getAllowManagerLoeschen());
      manager.setAllowPiloten(selectedManager.getAllowPiloten());
      manager.setAllowPilotenBearbeiten(selectedManager.getAllowPilotenBearbeiten());
      manager.setAllowPilotenEinstellen(selectedManager.getAllowPilotenEinstellen());
      manager.setAllowPilotenEntlassen(selectedManager.getAllowPilotenEntlassen());
      manager.setAllowRouten(selectedManager.getAllowRouten());
      manager.setAllowRoutenBearbeiten(selectedManager.getAllowRoutenBearbeiten());
      manager.setAllowRoutenErstellen(selectedManager.getAllowRoutenErstellen());
      manager.setAllowRoutenLoeschen(selectedManager.getAllowRoutenLoeschen());
      manager.setAllowFBO(selectedManager.getAllowFBO());
      manager.setAllowFBOBearbeiten(selectedManager.getAllowFBOBearbeiten());
      manager.setAllowFBOHinzufuegen(selectedManager.getAllowFBOHinzufuegen());
      manager.setAllowFBOLoeschen(selectedManager.getAllowFBOLoeschen());
      manager.setAllowFBOTankstelleVerwalten(selectedManager.getAllowFBOTankstelleVerwalten());

      facadeManager.saveManager(manager);
      NewMessage(loginMB.onSprache("Fluggesellschaften_Manager_msg_AenderungenGespeichert"));
      isLoaded = false;
    }

  }

  public void onRefresh() {

    try {
      FlugGesellschaft = facadeManager.readFG(FluggesellschaftID);

      //System.out.println("de.klees.beans.airline.airlineBean.onRowSelect() FG ID " + FlugGesellschaft.getIdFluggesellschaft());
      if (FlugGesellschaft.getIdFluggesellschaft().equals(ManagerID)) {
        //System.out.println("de.klees.beans.airline.airlineBean.onRowSelect() Berechtigungen auslesen");

        Fluggesellschaftmanager mg = facadeManager.readManager(UserID, FlugGesellschaft.getIdFluggesellschaft());

        setAllowManager(mg.getAllowManager());
        setAllowManagerBearbeiten(mg.getAllowManagerBearbeiten());
        setAllowManagerEinstellen(mg.getAllowManagerEinstellen());
        setAllowManagerLoeschen(mg.getAllowManagerLoeschen());

      } else {
        //System.out.println("de.klees.beans.airline.airlineBean.onRowSelect() default Berechtigung");
        setAllowManager(true);
        setAllowManagerBearbeiten(true);
        setAllowManagerEinstellen(true);
        setAllowManagerLoeschen(true);
      }

    } catch (NullPointerException e) {

    }

  }

  public void onChangeGeschaeftsfuehrer() {

    if (selectedManager.getGeschaeftsfuehrer()) {
      selectedManager.setBezeichnung(selectedManager.getBezeichnung());
      selectedManager.setAllowBank(true);
      selectedManager.setAllowBankBuchhaltung(true);
      selectedManager.setAllowBankTransfer(true);
      selectedManager.setAllowFluggesellschaftBearbeiten(true);
      selectedManager.setAllowFluggesellschaftLoeschen(true);
      selectedManager.setAllowFluggesellschaftLoeschen(true);
      selectedManager.setAllowFlugzeuge(true);
      selectedManager.setAllowFlugzeugeBearbeiten(true);
      selectedManager.setAllowFlugzeugeKaufen(true);
      selectedManager.setAllowFlugzeugeTransfer(true);
      selectedManager.setAllowFlugzeugeVerkaufen(true);
      selectedManager.setAllowManager(true);
      selectedManager.setAllowManagerBearbeiten(true);
      selectedManager.setAllowManagerEinstellen(true);
      selectedManager.setAllowManagerLoeschen(true);
      selectedManager.setAllowPiloten(true);
      selectedManager.setAllowPilotenBearbeiten(true);
      selectedManager.setAllowPilotenEinstellen(true);
      selectedManager.setAllowPilotenEntlassen(true);
      selectedManager.setAllowRouten(true);
      selectedManager.setAllowRoutenBearbeiten(true);
      selectedManager.setAllowRoutenErstellen(true);
      selectedManager.setAllowRoutenLoeschen(true);
      selectedManager.setAllowFBO(true);
      selectedManager.setAllowFBOBearbeiten(true);
      selectedManager.setAllowFBOHinzufuegen(true);
      selectedManager.setAllowFBOLoeschen(true);
      selectedManager.setAllowFBOTankstelleVerwalten(true);

    } else {
      selectedManager.setBezeichnung(selectedManager.getBezeichnung());
      selectedManager.setAllowBank(false);
      selectedManager.setAllowBankBuchhaltung(false);
      selectedManager.setAllowBankTransfer(false);
      selectedManager.setAllowFluggesellschaftBearbeiten(false);
      selectedManager.setAllowFluggesellschaftLoeschen(false);
      selectedManager.setAllowFluggesellschaftLoeschen(false);
      selectedManager.setAllowFlugzeuge(false);
      selectedManager.setAllowFlugzeugeBearbeiten(false);
      selectedManager.setAllowFlugzeugeKaufen(false);
      selectedManager.setAllowFlugzeugeTransfer(false);
      selectedManager.setAllowFlugzeugeVerkaufen(false);
      selectedManager.setAllowManager(false);
      selectedManager.setAllowManagerBearbeiten(false);
      selectedManager.setAllowManagerEinstellen(false);
      selectedManager.setAllowManagerLoeschen(false);
      selectedManager.setAllowPiloten(false);
      selectedManager.setAllowPilotenBearbeiten(false);
      selectedManager.setAllowPilotenEinstellen(false);
      selectedManager.setAllowPilotenEntlassen(false);
      selectedManager.setAllowRouten(false);
      selectedManager.setAllowRoutenBearbeiten(false);
      selectedManager.setAllowRoutenErstellen(false);
      selectedManager.setAllowRoutenLoeschen(false);
      selectedManager.setAllowFBO(false);
      selectedManager.setAllowFBOBearbeiten(false);
      selectedManager.setAllowFBOHinzufuegen(false);
      selectedManager.setAllowFBOLoeschen(false);
      selectedManager.setAllowFBOTankstelleVerwalten(false);

    }

  }

  public void onRowSelect(SelectEvent event) {
  }

  public void onRowUnselect(UnselectEvent event) {
  }

  private void NewMessage(String neueNachricht) {
    FacesMessage msg = new FacesMessage(neueNachricht);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  // Getter and Setter
  public ViewFluggesellschaftManager getSelectedManager() {
    return selectedManager;
  }

  public void setSelectedManager(ViewFluggesellschaftManager selectedManager) {
    this.selectedManager = selectedManager;
  }

  public String getFrmFTWBenutzername() {
    return frmFTWBenutzername;
  }

  public void setFrmFTWBenutzername(String frmFTWBenutzername) {
    this.frmFTWBenutzername = frmFTWBenutzername;
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

  public String getFluggesellschaftName() {
    return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftName");
  }

}
