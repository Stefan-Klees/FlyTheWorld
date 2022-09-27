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

import de.klees.data.Airport;
import de.klees.data.Bank;
import de.klees.data.FboObjekte;
import de.klees.data.FboUserObjekte;
import de.klees.data.Fluggesellschaft;
import de.klees.data.Fluggesellschaftmanager;
import de.klees.data.Benutzer;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 *
 * @author Stefan Klees
 */
public abstract class AbstractFacade<T> {

  private Class<T> entityClass;

  public AbstractFacade(Class<T> entityClass) {
    this.entityClass = entityClass;
  }

  protected abstract EntityManager getEntityManager();

  @SuppressWarnings("unchecked")
  public void create(T entity) {
    getEntityManager().persist(entity);
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

  public List<FboObjekte> findAll() {
    return getEntityManager().createQuery("SELECT f from FboObjekte f ORDER BY f.objektName", FboObjekte.class)
            .getResultList();
  }

  public List<FboObjekte> findAllFboByKlasse(int klasse) {
    return getEntityManager().createQuery("SELECT f FROM FboObjekte f WHERE f.klasse = :klasse ORDER BY f.objektName", FboObjekte.class)
            .setParameter("klasse", klasse)
            .getResultList();
  }

  public List<FboObjekte> findAllObjekteDerArt(int Art, int klasse) {
    String sqlQuery = "";

    // 1 FBO, 2 Abfertigungsterminal, 3 Tankstelle, 4 Servicehangar
    switch (Art) {
      case 3:
        sqlQuery = "SELECT o FROM FboObjekte o WHERE o.tankstelle = TRUE and o.";
        break;
      case 2:
        sqlQuery = "SELECT o FROM FboObjekte o WHERE o.abfertigungsTerminal = TRUE";
        break;
      case 1:
        sqlQuery = "SELECT o FROM FboObjekte o WHERE o.fbo = TRUE";
        break;
      case 4:
        sqlQuery = "SELECT o FROM FboObjekte o WHERE o.servicehangar = TRUE";
        break;
      default:
        break;
    }

    return getEntityManager().createQuery("SELECT o FROM FboObjekte o WHERE o.servicehangar = TRUE AND o.klasse = :klasse ", FboObjekte.class)
            .setParameter("klasse", klasse)
            .getResultList();
  }

  public Benutzer readUser(int UserID) {
    try {
      return (Benutzer) getEntityManager().createQuery("SELECT u from Benutzer u WHERE u.idUser = :userid", Benutzer.class)
              .setParameter("userid", UserID)
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

  public Fluggesellschaft readMyFg(int userID, int fgid) {
    try {
      return getEntityManager().createQuery("SELECT f FROM Fluggesellschaft f WHERE f.userid = :userid AND f.idFluggesellschaft = :fgid", Fluggesellschaft.class)
              .setParameter("userid", userID)
              .setParameter("fgid", fgid)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public void onMieten(FboUserObjekte entity) {
    getEntityManager().persist(entity);
  }

  public void createBankbuchung(Bank entity) {
    getEntityManager().persist(entity);
  }

  public Fluggesellschaft readFG(int fgid) {
    try {
      return getEntityManager().createQuery("SELECT f FROM Fluggesellschaft f WHERE f.idFluggesellschaft = :fgid", Fluggesellschaft.class)
              .setParameter("fgid", fgid)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public double BankSaldo(int UserID) {
    String Abfrage = "SELECT SUM( betrag ) FROM bank AS bank WHERE userID = " + String.valueOf(UserID);
    return (double) getEntityManager().createNativeQuery(Abfrage).getSingleResult();
  }

  public double getBankSaldoUeberKonto(String BankKonto) {
    return (double) getEntityManager().createNamedQuery("Bank.Banksaldo").setParameter("bankKonto", BankKonto).getSingleResult();
  }

  public String getBankkontoName(String Kontonummer) {
    return getEntityManager().createQuery("SELECT b FROM Bank b WHERE b.bankKonto = :kontonummer", Bank.class)
            .setParameter("kontonummer", Kontonummer)
            .setMaxResults(1)
            .getSingleResult().getKontoName();
  }

  public boolean hatUserFBOAmFlughafen(int userID, String icao) {
    try {
      if (getEntityManager().createQuery("SELECT f from ViewFBOUserObjekte f WHERE f.idUser = :userID AND f.icao = :icao AND f.fbo = TRUE")
              .setParameter("userID", userID)
              .setParameter("icao", icao)
              .getResultList().size() > 0) {
        return true;
      }

    } catch (Exception e) {
      return false;
    }
    return false;
  }

  public long wievieleFBOHatUserAmFlughafen(int userID, String icao) {
    return (long) getEntityManager().createQuery("SELECT COUNT (f) from ViewFBOUserObjekte f WHERE f.idUser = :userID AND f.icao = :icao AND f.fbo = TRUE")
            .setParameter("userID", userID)
            .setParameter("icao", icao)
            .getSingleResult();
  }

  public long wievieleFBOAmFlughafen(String icao) {
    return (long) getEntityManager().createQuery("SELECT COUNT (f) from ViewFBOUserObjekte f WHERE f.icao = :icao AND f.fbo = TRUE")
            .setParameter("icao", icao)
            .getSingleResult();
  }

  public long wievieleTankstellenHatUserAmFlughafen(int userID, String icao) {
    return (long) getEntityManager().createQuery("SELECT COUNT (f) from ViewFBOUserObjekte f WHERE f.idUser = :userID AND f.icao = :icao AND (f.tankstelle = TRUE OR f.spritlager = TRUE)")
            .setParameter("userID", userID)
            .setParameter("icao", icao)
            .getSingleResult();
  }

  public long wievieleTerminalsHatUserAmFlughafen(int userID, String icao) {
    return (long) getEntityManager().createQuery("SELECT COUNT (f) from ViewFBOUserObjekte f WHERE f.idUser = :userID AND f.icao = :icao AND f.abfertigungsTerminal = TRUE")
            .setParameter("userID", userID)
            .setParameter("icao", icao)
            .getSingleResult();
  }

  public boolean ifExistFlughafen(String icao) {
    try {
      if (getEntityManager().createQuery("SELECT a FROM Airport a WHERE a.icao = :icao", Airport.class)
              .setParameter("icao", icao).getSingleResult().getIcao().equals(icao)) {
        return true;
      }
    } catch (NoResultException e) {
      return false;
    }
    return false;
  }

  public boolean ifExistFBOUserObjekt(int objectID) {
    if (getEntityManager().createQuery("SELECT o FROM FboUserObjekte o WHERE o.idfboObjekt = :objectid", FboUserObjekte.class)
            .setParameter("objectid", objectID).getResultList().size() > 0) {
      return true;
    }
    return false;
  }

  public Airport getAirportInfo(String icao) {
    try {
      return getEntityManager().createQuery("SELECT a from Airport a where a.icao = :icao", Airport.class)
              .setParameter("icao", icao)
              .getSingleResult();

    } catch (NoResultException e) {
      return null;
    }

  }

}
