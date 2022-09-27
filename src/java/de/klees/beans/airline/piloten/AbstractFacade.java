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

import de.klees.data.FlugesellschaftPiloten;
import de.klees.data.Fluggesellschaft;
import de.klees.data.FluggesellschaftBonusSystem;
import de.klees.data.Fluggesellschaftmanager;
import de.klees.data.FlugzeugeErlaubteUser;
import de.klees.data.Mail;
import de.klees.data.Benutzer;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 *
 * @author Stefan Klees
 * @param <T>
 */
public abstract class AbstractFacade<T> {

  private final Class<T> entityClass;

  public AbstractFacade(Class<T> entityClass) {
    this.entityClass = entityClass;
  }

  protected abstract EntityManager getEntityManager();

  public void create(T entity) {
    getEntityManager().persist(entity);
  }

  public void edit(T entity) {
    getEntityManager().merge(entity);
  }

  public void remove(T entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public List<FlugesellschaftPiloten> findAllByCallName(String SuchText, int idAirline) {
    return getEntityManager().createNamedQuery("FlugesellschaftPiloten.findByCallnameOrderBy", FlugesellschaftPiloten.class)
            .setParameter("callname", SuchText)
            .setParameter("idflugesellschaft", idAirline)
            .getResultList();
  }

  public List<Fluggesellschaft> getFluggesellschaften(int userid) {
    return getEntityManager().createQuery("SELECT f FROM Fluggesellschaft f WHERE f.userid = :userid", Fluggesellschaft.class)
            .setParameter("userid", userid)
            .getResultList();
  }

  public List<Benutzer> findAllUserOrderByName() {
    return getEntityManager().createQuery("SELECT u FROM User u WHERE u.idUser > 0 ORDER BY u.name ", Benutzer.class).getResultList();
  }

  public Benutzer findUserByName(String UserName) {
    try {
      return getEntityManager().createNamedQuery("User.findByName", Benutzer.class)
              .setParameter("name", UserName)
              .getSingleResult();

    } catch (Exception e) {
      return null;
    }
  }

  public Benutzer findUserByID(int UserID) {
    return getEntityManager().createNamedQuery("User.findByIdUser", Benutzer.class)
            .setParameter("idUser", UserID)
            .getSingleResult();
  }

  public String getUserName(int UserID) {
    try {
      return getEntityManager().createQuery("SELECT u FROM User u WHERE u.idUser =:userid", Benutzer.class)
              .setParameter("userid", UserID)
              .getSingleResult().getName1();

    } catch (Exception e) {
      return "not found";
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

  public Fluggesellschaft readFG(int fgid) {
    try {
      return getEntityManager().createQuery("SELECT f from Fluggesellschaft f WHERE f.idFluggesellschaft = :fgid", Fluggesellschaft.class)
              .setParameter("fgid", fgid)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  // Pilot der Fluggesellschaft auslesen
  public FlugesellschaftPiloten readPilot(int idAirline, int UserID) {
    try {
      return getEntityManager().createQuery("SELECT f FROM FlugesellschaftPiloten f WHERE f.idflugesellschaft =:idAirline and f.iduser =:userID", FlugesellschaftPiloten.class)
              .setParameter("userID", UserID)
              .setParameter("idAirline", idAirline)
              .setMaxResults(1)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public boolean ifExistPilotInAirline(int idAirline, int userid) {
    try {
      FlugesellschaftPiloten piloten = getEntityManager().createQuery("SELECT f FROM FlugesellschaftPiloten f WHERE f.idflugesellschaft = :idAirline and f.iduser = :userID", FlugesellschaftPiloten.class)
              .setParameter("userID", userid)
              .setParameter("idAirline", idAirline)
              .getSingleResult();
      return true;
    } catch (Exception e) {
      //System.out.println("de.klees.beans.airline.piloten.AbstractFacade.ifExistPilotInAirline() " + e.getMessage());
      return false;
    }
  }

  public List<FluggesellschaftBonusSystem> getBoniList(int fgid) {
    return getEntityManager().createQuery("SELECT b FROM FluggesellschaftBonusSystem b WHERE b.idfluggesellschaft = :fgid ORDER BY b.zeit", FluggesellschaftBonusSystem.class)
            .setParameter("fgid", fgid)
            .getResultList();
  }

  public Benutzer readUser(int UserID) {
    try {
      return (Benutzer) getEntityManager().createQuery("SELECT u from User u WHERE u.idUser = :userid", Benutzer.class)
              .setParameter("userid", UserID)
              .getSingleResult();

    } catch (NoResultException e) {
      return null;
    }

  }

  public List getPilotenLog(int userid, int fgid) {

    String Abfrage = "    SELECT  "
            + "        `fluglogbuch`.`idAirline` AS `idAirline`, "
            + "        `fluglogbuch`.`flugDatum` AS `flugDatum`, "
            + "        `fluglogbuch`.`acarsFlugNummer` AS `acarsFlugNummer`, "
            + "        `Flugzeuge_miet_kauf`.`registrierung` AS `registrierung`, "
            + "        `Flugzeuge`.`type` AS `type`, "
            + "        `fluglogbuch`.`fromicao` AS `fromicao`, "
            + "        `fluglogbuch`.`fromIcaoFlughafenName` AS `fromIcaoFlughafenName`, "
            + "        `fluglogbuch`.`toicao` AS `toicao`, "
            + "        `fluglogbuch`.`tocaoFlughafenName` AS `tocaoFlughafenName`, "
            + "        `fluglogbuch`.`pax` AS `pax`, "
            + "        `fluglogbuch`.`cargo` AS `cargo`, "
            + "        `fluglogbuch`.`buchungsgebuehr` AS `Auftragswert`, "
            + "        `fluglogbuch`.`iduser` AS `iduser`, "
            + "        `fluglogbuch`.`miles` AS `miles`, "
            + "        `fluglogbuch`.`flugzeitMinuten` AS `flugzeitMinuten` "            
            + "    FROM "
            + "        ((`Flugzeuge` "
            + "        JOIN `Flugzeuge_miet_kauf`) "
            + "        JOIN `fluglogbuch`) "
            + "    WHERE "
            + "        ((`Flugzeuge`.`idFlugzeug` = `Flugzeuge_miet_kauf`.`idFlugzeug`) "
            + "            AND (`Flugzeuge_miet_kauf`.`idMietKauf` = `fluglogbuch`.`idaircraft`) "
            + "            AND (`fluglogbuch`.`idAirline` = " + fgid + " ) "
            + "            AND (`fluglogbuch`.`iduser` = " + userid + " )) order by `fluglogbuch`.`flugDatum` DESC";

    return getEntityManager().createNativeQuery(Abfrage).getResultList();
  }

  public List getPilotenLogDatum(int fgid, String vonDatum, String bisDatum) {

    String Abfrage = "    SELECT  "
            + "        `fluglogbuch`.`idAirline` AS `idAirline`, "
            + "        `fluglogbuch`.`flugDatum` AS `flugDatum`, "
            + "        `fluglogbuch`.`acarsFlugNummer` AS `acarsFlugNummer`, "
            + "        `Flugzeuge_miet_kauf`.`registrierung` AS `registrierung`, "
            + "        `Flugzeuge`.`type` AS `type`, "
            + "        `fluglogbuch`.`fromicao` AS `fromicao`, "
            + "        `fluglogbuch`.`fromIcaoFlughafenName` AS `fromIcaoFlughafenName`, "
            + "        `fluglogbuch`.`toicao` AS `toicao`, "
            + "        `fluglogbuch`.`tocaoFlughafenName` AS `tocaoFlughafenName`, "
            + "        `fluglogbuch`.`pax` AS `pax`, "
            + "        `fluglogbuch`.`cargo` AS `cargo`, "
            + "        `fluglogbuch`.`buchungsgebuehr` AS `Auftragswert`, "
            + "        `fluglogbuch`.`iduser` AS `iduser`, "
            + "        `fluglogbuch`.`miles` AS `miles`, "
            + "        `fluglogbuch`.`flugzeitMinuten` AS `flugzeitMinuten`, "            
            + "        `User`.`name` AS `name` "
            + "    FROM\n"
            + "        (((`Flugzeuge` "
            + "        JOIN `Flugzeuge_miet_kauf`) "
            + "        JOIN `fluglogbuch`) "
            + "        JOIN `User`) "
            + "    WHERE "
            + "        ((`Flugzeuge`.`idFlugzeug` = `Flugzeuge_miet_kauf`.`idFlugzeug`) "
            + "            AND (`Flugzeuge_miet_kauf`.`idMietKauf` = `fluglogbuch`.`idaircraft`) "
            + "            AND (`User`.`idUser` = `fluglogbuch`.`iduser`) "
            + "            AND (`fluglogbuch`.`idAirline` = " + fgid + " ) "
            + "            AND (`fluglogbuch`.`flugDatum` BETWEEN { d '" + vonDatum + "' } AND { d '" + bisDatum + "' } )) order by `fluglogbuch`.`flugDatum` DESC  ";

    return getEntityManager().createNativeQuery(Abfrage).getResultList();
  }

  public List<FlugzeugeErlaubteUser> getErlaubteUser(int fgid, int fzid) {
    // (10.03.2019) FGID entfernt, damit auch evtl. aeltere Eintraege von anderen FG's entfernt werden können
    return getEntityManager().createQuery("SELECT f FROM FlugzeugeErlaubteUser f WHERE f.idFlugzeugeMietKauf = :fzid", FlugzeugeErlaubteUser.class)
            .setParameter("fzid", fzid)
            .getResultList();
  }

  public boolean ifExistUserFromFlugzeug(int uid, int fzid) {

    try {
      FlugzeugeErlaubteUser test = getEntityManager().createQuery("SELECT f FROM FlugzeugeErlaubteUser f WHERE f.idUser = :uid and f.idFlugzeugeMietKauf = :fzid", FlugzeugeErlaubteUser.class)
              .setParameter("uid", uid)
              .setParameter("fzid", fzid)
              .getSingleResult();
      return true;
    } catch (NoResultException e) {
      return false;
    }
  }

  public void createUserFromFlugzeug(FlugzeugeErlaubteUser entity) {
    getEntityManager().persist(entity);
  }

  public void removeUserFromFlugzeug(FlugzeugeErlaubteUser entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public void saveUserMail(Mail entity) {
    getEntityManager().persist(entity);
  }

}
