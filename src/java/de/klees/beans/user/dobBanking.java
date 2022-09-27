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

package de.klees.beans.user;

import de.klees.data.Bank;
import de.klees.data.Benutzer;
import java.io.Serializable;
import java.util.ArrayList;
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
public class dobBanking implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<Bank> listBuchungen;
  private Bank selectedBuchung;
  private Benutzer selectedUser;

  private boolean isLoaded;
  private String KtoNummer;
  private String KtoName;
  private double[] BankSaldo;

  private ArrayList<bankkonto> BankSalden = new ArrayList<>();

  @EJB
  UserFacade facadeDobBanking;

  public dobBanking() {
    isLoaded = false;
    KtoNummer = "";
  }

  public void onRefresh() {
    isLoaded = false;
  }

  public void onBankkontoUser() {
    if (selectedUser != null) {
      KtoNummer = selectedUser.getBankKonto();
      KtoName = selectedUser.getName1();
      isLoaded = false;
    } else {
      KtoNummer = "";
    }
  }

  public void onBankkontoVonEmpfpfaenger() {
    if (selectedBuchung != null) {
      KtoNummer = selectedBuchung.getEmpfaengerKonto();
      KtoName = selectedBuchung.getEmpfaengerName();
      isLoaded = false;
    }
  }

  public void onBankkontoVonAuftraggeber() {
    if (selectedBuchung != null) {
      KtoNummer = selectedBuchung.getAbsenderKonto();
      KtoName = selectedBuchung.getAbsenderName();
      isLoaded = false;
    }
  }

  public List<Bank> getListBuchungen() {
    if (!isLoaded) {
      if (!KtoNummer.equals("")) {
        listBuchungen = facadeDobBanking.getBankbuchungen(KtoNummer);
        isLoaded = true;
      }

    }
    return listBuchungen;
  }

  public void onBankkontoAirline() {
    isLoaded = false;
  }

  public List<Bank> getListBuchungenAirline() {
    if (!isLoaded) {
      if (!KtoNummer.equals("")) {
        listBuchungen = facadeDobBanking.getBankbuchungen(KtoNummer);
        isLoaded = true;
      }

    }
    return listBuchungen;
  }

  public void onDeleteBuchung() {
    if (selectedBuchung != null) {
      facadeDobBanking.deleteBuchung(selectedBuchung);
      selectedBuchung = null;
      Meldung("Buchung gelöscht, Buchung wurde nicht automatisch auf dem Gegenkonto gelöscht");
    }
  }

  public void bankSaldenErmitteln() {
    BankSalden.clear();

    List<Benutzer> users = facadeDobBanking.getAllUsers();

    double saldo = 0.0;
    int Zaehler = 0;

    for (Benutzer us : users) {

      if (us.getIdUser() > 0) {

        try {
          Zaehler = Zaehler + 1;
          bankkonto bk = new bankkonto();
          bk.setSaldo(facadeDobBanking.getErweiterterBankSaldo(us.getIdUser()));
          bk.setUserID(us.getIdUser());
          bk.setUserName(us.getName1());
          BankSalden.add(bk);

        } catch (NullPointerException e) {

        }

      }

    }
  }

  public List<bankkonto> getBanksalden() {
    return BankSalden;
  }

  public void onRowSelect(SelectEvent event) {
  }

  public void onRowUnselect(UnselectEvent event) {
  }

  private void Meldung(String neueNachricht) {
    FacesMessage msg = new FacesMessage(neueNachricht);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  /*
  ************ Getter and Setter
   */
  public Bank getSelectedBuchung() {
    return selectedBuchung;
  }

  public void setSelectedBuchung(Bank selectedBuchung) {
    this.selectedBuchung = selectedBuchung;
  }

  public String getKtoNummer() {
    return KtoNummer;
  }

  public void setKtoNummer(String KtoNummer) {
    this.KtoNummer = KtoNummer;
  }

  public String getKtoName() {
    return KtoName;
  }

  public void setKtoName(String KtoName) {
    this.KtoName = KtoName;
  }

  public Benutzer getSelectedUser() {
    return selectedUser;
  }

  public void setSelectedUser(Benutzer selectedUser) {
    this.selectedUser = selectedUser;
  }

  public double[] getBankSaldo() {
    double banksaldo[] = new double[3];
    double summe = 0;
    double einnahmen = 0;
    double ausgaben = 0;

    if (listBuchungen != null) {
      for (Bank bk : listBuchungen) {
        summe = summe + bk.getBetrag();
        if (bk.getBetrag() < 0) {
          ausgaben = ausgaben + bk.getBetrag();
        } else {
          einnahmen = einnahmen + bk.getBetrag();
        }
      }
    }

    banksaldo[0] = summe;
    banksaldo[1] = ausgaben;
    banksaldo[2] = einnahmen;

    return banksaldo;
  }

}
