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

import de.klees.data.Bank;
import de.klees.data.Banktransfer;
import de.klees.data.Fluggesellschaft;
import de.klees.data.Fluggesellschaftmanager;
import de.klees.data.Mail;
import de.klees.data.Benutzer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 *
 * @author Stefan Klees
 * @param <T>
 */
public abstract class AbstractFacade<T> {

  private SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-dd-MM");

  private final Class<T> entityClass;

  public AbstractFacade(Class<T> entityClass) {
    this.entityClass = entityClass;
  }

  protected abstract EntityManager getEntityManager();

  @SuppressWarnings("unchecked")
  public void create(T entity) {
    getEntityManager().persist(entity);
  }

  public void createTerminUeberweisung(Banktransfer entity) {
    getEntityManager().persist(entity);
  }

  public void editTerminUeberweisung(Banktransfer entity) {
    getEntityManager().merge(entity);
  }

  public void removeTerminUeberweisung(Banktransfer entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  @SuppressWarnings("unchecked")
  public void edit(T entity) {
    getEntityManager().merge(entity);
  }

  @SuppressWarnings("unchecked")
  public void remove(T entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  @SuppressWarnings("unchecked")
  public T find(Object id) {
    return getEntityManager().find(entityClass, id);
  }

  /**
   *
   * @param UserID
   * @return
   */
  public List<Bank> findAllByUser(int UserID) {
    return getEntityManager().createNamedQuery("Bank.findByUserID", Bank.class).setParameter("userID", UserID).getResultList();
  }

  public Benutzer readUserDaten(int userid){
    return getEntityManager().createQuery("SELECT u FROM Benutzer u WHERE u.idUser = :userid",Benutzer.class)
            .setParameter("userid", userid)
            .getSingleResult();
  }
  
  /**
   *
   * @param KontoNummer
   * @return
   */
  public List<Bank> findAllByKontoNummer(String KontoNummer) {
    return getEntityManager().createQuery("SELECT b from Bank b WHERE b.bankKonto = :bankKonto ORDER BY b.ausfuehrungsDatum DESC", Bank.class).setParameter("bankKonto", KontoNummer).getResultList();
  }

  public Bank findbyKontoNummer(String KontoNummer) {
    try {
      return getEntityManager().createQuery("SELECT b from Bank b WHERE b.bankKonto = :bankKonto ORDER BY b.ausfuehrungsDatum DESC", Bank.class).setParameter("bankKonto", KontoNummer).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public List<Banktransfer> getTermine(String KontoNummer) {
    return getEntityManager().createQuery("SELECT t FROM Banktransfer t WHERE t.absenderKonto = :KontoNummer ", Banktransfer.class)
            .setParameter("KontoNummer", KontoNummer)
            .getResultList();
  }

  public boolean ifExistBankKonto(String KontoNummer) {
    try {
      if (getEntityManager().createNamedQuery("Bank.findByBankKonto", Bank.class).setParameter("bankKonto", KontoNummer).getResultList().get(0).getBankKonto().equals(KontoNummer)) {
        return true;
      } else {
        return false;
      }
    } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
      return false;
    }
  }

  public Bank getBankDaten(String KontoNummer) {
    try {
      return getEntityManager().createNamedQuery("Bank.findByBankKonto", Bank.class)
              .setParameter("bankKonto", KontoNummer)
              .getResultList().get(0);
    } catch (NullPointerException e) {
      return null;
    }

  }

  public List<Bank> findAllByDate(Date StartDatum, Date EndDatum, String BankKonto) {

    return getEntityManager().createQuery("SELECT b FROM Bank b WHERE b.ueberweisungsDatum BETWEEN :StartDatum and :EndDatum and b.bankKonto =:bankKonto ORDER BY b.ueberweisungsDatum DESC", Bank.class)
            .setParameter("StartDatum", StartDatum)
            .setParameter("EndDatum", EndDatum)
            .setParameter("bankKonto", BankKonto)
            .getResultList();
  }

  public List<Bank> findByDetail(String vwz, int kst, String ktonr) {
    return getEntityManager().createQuery("SELECT b FROM Bank b WHERE b.verwendungsZweck like :vwz AND b.kostenstelle = :kst AND b.bankKonto = :ktonr ORDER BY b.ueberweisungsDatum DESC", Bank.class)
            .setParameter("vwz", vwz)
            .setParameter("kst", kst)
            .setParameter("ktonr", ktonr)
            .getResultList();
  }

  /*
  ****************************** Airline Bankingmodul Begin **************************************
   */
  @SuppressWarnings("unchecked")
  public List<T> findAllByAirline(int AirlineID) {
    return getEntityManager().createNamedQuery("Bank.findByAirlineID").setParameter("airlineID", AirlineID).getResultList();
  }

  public List<Bank> getEmpfaenger(String ktoNr) {
    return getEntityManager().createQuery("SELECT b FROM Bank b WHERE b.bankKonto = :ktoNr GROUP BY b.empfaengerKonto", Bank.class)
            .setParameter("ktoNr", ktoNr)
            .getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<Bank> findAllByDateForAirline(Date StartDatum, Date EndDatum, int AirlineID) {

    return getEntityManager().createNamedQuery("Bank.findByDatumForAirline")
            .setParameter("StartDatum", StartDatum)
            .setParameter("EndDatum", EndDatum)
            .setParameter("airlineID", AirlineID)
            .getResultList();
  }

  // Fluggesellschaft ID korrigieren
  public void setFluggesellschaftID(String Konto, int id) {
    getEntityManager().createNativeQuery("update bank set fluggesellschaftID = " + id + " where bankkonto = '" + Konto + "' and fluggesellschaftID = -1").executeUpdate();
  }

  public double getBankSaldo(String BankKonto) {
    return (double) getEntityManager().createNamedQuery("Bank.Banksaldo").setParameter("bankKonto", BankKonto).getSingleResult();
  }

  public Fluggesellschaft readFG(int fgid) {
    try {
      return getEntityManager().createQuery("SELECT f from Fluggesellschaft f WHERE f.idFluggesellschaft = :fgid", Fluggesellschaft.class)
              .setParameter("fgid", fgid)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public Fluggesellschaftmanager readManager(int uid, int fgid) {
    try {
      return getEntityManager().createQuery("SELECT m FROM Fluggesellschaftmanager m WHERE m.fluggesellschaftid = :fgid AND m.userid = :uid", Fluggesellschaftmanager.class)
              .setParameter("uid", uid)
              .setParameter("fgid", fgid)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }


/*
  **************** Mailfunktionen BEGINN
  */

  public Benutzer findUserByName(String UserName) {
    try {
      return getEntityManager().createQuery("SELECT u FROM Benutzer u WHERE u.name1 = :name", Benutzer.class)
              .setParameter("name", UserName)
              .getSingleResult();

    } catch (Exception e) {
      return null;
    }
  }

  public void saveUserMail(Mail entity) {
    getEntityManager().persist(entity);
  }

/*
  **************** Mailfunktionen ENDE
  */

  /*
  ****************************** Airline Bankingmodul ENDE **************************************
   */
  
  public List<Benutzer> findAllUsers() {
    return getEntityManager().createQuery("SELECT u FROM Benutzer",Benutzer.class).getResultList();
  }

  
  public List<Benutzer> findUserByUserID(int userID) {
    return getEntityManager().createQuery("SELECT u FROM Benutzer u WHERE u.idUser= :iduser", Benutzer.class)
            .setParameter("iduser", userID)
            .getResultList();
  }

}
