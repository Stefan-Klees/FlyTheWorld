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

package de.klees.beans.user.mail;

import de.klees.beans.system.CONF;
import de.klees.beans.user.UserFacade;
import de.klees.data.Mail;
import de.klees.data.Benutzer;
import java.io.Serializable;
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
public class mailBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<Mail> listMails;
  private Mail selectedMail;
  private List<Mail> selectedMails;
  private List<String> userList;
  private int UserID;
  private String UserName;
  private String frmKategorie;
  private String frmAnUser;
  private Date frmDatum;
  private String frmNachrichtenText;
  private String frmBetreff;

  @EJB
  UserFacade facadeMail;

  public mailBean() {
    UserID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
    UserName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName");
    frmKategorie = "Posteingang";
  }

  public List<Mail> getListMails() {
    return facadeMail.getUserMails(UserID, frmKategorie);
  }

  public List<Mail> getMailKategorien() {
    return facadeMail.getMailKategorien(UserID);
  }

  public List<String> getUserList() {
    return facadeMail.getUserList();
  }

  public void saveMail() {

    frmKategorie = "Posteingang";
    String Gesendet = "Sent";

    //Absendermail speichern
    Mail nm = new Mail();
    nm.setUserID(UserID);
    nm.setAnID(getUserDaten(frmAnUser).getIdUser());
    nm.setAnUser(frmAnUser);
    nm.setBetreff(frmBetreff);
    nm.setDatumZeit(new Date());
    nm.setGelesen(false);
    nm.setKategorie(Gesendet);
    nm.setVonID(UserID);
    nm.setVonUser(UserName);
    nm.setNachrichtText(CONF.NoScript(frmNachrichtenText));
    facadeMail.saveUserMail(nm);

    if (!getUserDaten(frmAnUser).getIdUser().equals(UserID)) {
      //Empfaengermail speichern
      nm = new Mail();
      nm.setUserID(getUserDaten(frmAnUser).getIdUser());
      nm.setAnID(getUserDaten(frmAnUser).getIdUser());
      nm.setAnUser(frmAnUser);
      nm.setBetreff(frmBetreff);
      nm.setDatumZeit(new Date());
      nm.setGelesen(false);
      nm.setKategorie(frmKategorie);
      nm.setVonID(UserID);
      nm.setVonUser(UserName);
      nm.setNachrichtText(CONF.NoScript(frmNachrichtenText));

      facadeMail.saveUserMail(nm);
    }

    info("Mail wurde gespeichert!");
  }

  public void deleteMail() {

    for (Mail m : selectedMails) {
      facadeMail.deleteUserMail(m);
    }

    selectedMail = null;
    selectedMails = null;

    info("Löschen wurde durchgeführt");
  }

  public void kategorieAendern() {

    for (Mail m : selectedMails) {
      m.setKategorie(frmKategorie);
      facadeMail.editUserMail(m);
      selectedMail = null;
    }
    info("Neue Kategorie zugeordnet");
  }

  public void clearMail() {
    frmAnUser = "";
    frmBetreff = "";
    frmNachrichtenText = "";
    frmKategorie = "";
  }

  public void onRowSelect(SelectEvent event) {
    selectedMail = selectedMails.get(0);

    if (selectedMail != null) {
      frmAnUser = selectedMail.getVonUser();
      frmBetreff = "Re: " + selectedMail.getBetreff();
      frmNachrichtenText = selectedMail.getNachrichtText();
      if (!selectedMail.getGelesen()) {
        selectedMail.setGelesen(true);
        facadeMail.editUserMail(selectedMail);
      }
    } else {
      clearMail();
    }

  }

  public void onRowUnselect(UnselectEvent event) {
  }

  private void info(String neueNachricht) {
    FacesMessage msg = new FacesMessage(neueNachricht);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  public int getAnzahlUngeleseneMails() {
    return (int) facadeMail.getAnzahlUngeleseneMails(UserID);
  }

  public void onDeselectAll() {
    selectedMail = null;
    selectedMails = null;
  }

  /*
  Setter and Getter
   */
  private Benutzer getUserDaten(String BenutzerName) {
    return facadeMail.findUser(BenutzerName);
  }

  public Mail getSelectedMail() {
    return selectedMail;
  }

  public void setSelectedMail(Mail selectedMail) {
    this.selectedMail = selectedMail;
  }

  public String getFrmKategorie() {
    return frmKategorie;
  }

  public void setFrmKategorie(String frmKategorie) {
    this.frmKategorie = frmKategorie;
  }

  public List<Mail> getSelectedMails() {
    return selectedMails;
  }

  public void setSelectedMails(List<Mail> selectedMails) {
    this.selectedMails = selectedMails;
  }

  public String getFrmAnUser() {
    return frmAnUser;
  }

  public void setFrmAnUser(String frmAnUser) {
    this.frmAnUser = frmAnUser;
  }

  public Date getFrmDatum() {
    return frmDatum;
  }

  public void setFrmDatum(Date frmDatum) {
    this.frmDatum = frmDatum;
  }

  public String getFrmNachrichtenText() {
    return frmNachrichtenText;
  }

  public void setFrmNachrichtenText(String frmNachrichtenText) {
    this.frmNachrichtenText = frmNachrichtenText;
  }

  public String getFrmBetreff() {
    return frmBetreff;
  }

  public void setFrmBetreff(String frmBetreff) {
    this.frmBetreff = frmBetreff;
  }

}
