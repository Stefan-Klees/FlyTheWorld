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

package de.klees.beans.missionen;

import de.klees.data.Bank;
import de.klees.data.Feinabstimmung;
import de.klees.data.Fluglogbuch;
import de.klees.data.Flugzeugemietkauf;
import de.klees.data.Missionen;
import de.klees.data.Missionenwaypoints;
import de.klees.data.Missionsflug;
import de.klees.data.Benutzer;
import de.klees.data.Yaacarskopf;
import de.klees.data.Yaacarspositionen;
import de.klees.data.views.ViewFlugzeugeMietKauf;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 *
 * @author Stefan Klees
 */
public abstract class AbstractFacade {

  protected abstract EntityManager getEntityManager();


  /*
  **************** Mission BEGINN
   */
  public void neueMission(Missionen entity) {
    getEntityManager().persist(entity);
  }

  public void deleteMission(Missionen entity) {

    getEntityManager().createQuery("DELETE FROM Missionenwaypoints w WHERE w.idmissionen = :id ", Missionenwaypoints.class)
            .setParameter("id", entity.getIdmissionen())
            .executeUpdate();

    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public void saveMission(Missionen entity) {
    getEntityManager().merge(entity);
  }

  public List<Missionen> getMissionen() {
    return getEntityManager().createQuery("SELECT m from Missionen m WHERE m.freigabe = true", Missionen.class).getResultList();
  }

  public List<Missionen> getMissionenVerwaltung() {
    return getEntityManager().createQuery("SELECT m FROM Missionen m ORDER BY m.idmissionen DESC", Missionen.class).getResultList();
  }

  public Missionen getMission(int id) {
    try {
      return getEntityManager().createQuery("SELECT m FROM Missionen m WHERE m.idmissionen = :id", Missionen.class)
              .setParameter("id", id)
              .getSingleResult();
    } catch (Exception e) {
      return null;
    }

  }

  /*
  **************** Mission ENDE
   */
 /*
  *********************** Waypoints BEGINN
   */
  public void createWayPoint(Missionenwaypoints entity) {
    getEntityManager().persist(entity);
  }

  public void saveWayPoint(Missionenwaypoints entity) {
    getEntityManager().merge(entity);
  }

  public void deleteWayPoint(Missionenwaypoints entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public List<Missionenwaypoints> getWayPoints(int MID) {
    return getEntityManager().createQuery("SELECT w FROM Missionenwaypoints w WHERE w.idmissionen = :id ORDER BY w.reihenfolge", Missionenwaypoints.class)
            .setParameter("id", MID)
            .getResultList();
  }

  public void reihenfolgeNeu(int id) {

  }

  /*
  *********************** Waypoints ENDE
   */
 /*
  **************** YAACARS BEGINN
   */
  public void createYAACARSFlight(Yaacarskopf entity) {
    getEntityManager().persist(entity);
  }

  public void editYAACARSFlight(Yaacarskopf entity) {
    getEntityManager().merge(entity);
  }

  public int removeYAACARSFlight(int FlightID) {
    return getEntityManager().createQuery("DELETE FROM Yaacarskopf y WHERE y.idyaacarskopf = :id")
            .setParameter("id", FlightID)
            .executeUpdate();
  }

  public int removeYAACARSPositionen(int FlightID) {
    return getEntityManager().createQuery("DELETE FROM Yaacarspositionen y WHERE y.idyaacarskopf = :id")
            .setParameter("id", FlightID)
            .executeUpdate();
  }

  public Yaacarskopf readYAACARSFlight(int UserID) {
    try {
      return getEntityManager().createQuery("SELECT y FROM Yaacarskopf y WHERE y.userid = :userid", Yaacarskopf.class)
              .setParameter("userid", UserID)
              .getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  public Yaacarspositionen readLetzteYAACARSPosition(int FlightID) {
    try {
      return getEntityManager().createQuery("SELECT y FROM Yaacarspositionen y WHERE y.idyaacarskopf = :id ORDER BY y.idyaacarspositionen DESC", Yaacarspositionen.class)
              .setParameter("id", FlightID)
              .setMaxResults(1)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public boolean isFlightNummerExist(String FlugNr) {
    try {
      getEntityManager().createQuery("SELECT y FROM Yaacarskopf y WHERE y.flugnummer = :flugnr", Yaacarskopf.class)
              .setParameter("flugnr", FlugNr)
              .getSingleResult();
      return true;
    } catch (NoResultException e) {
      return false;
    }
  }

  /*
  **************** YAACARS ENDE
   */
 /*
  ******************** Missionsflug BEGINN
   */
  public void createMissionsflug(Missionsflug entity) {
    getEntityManager().persist(entity);
  }

  public void saveMissionsflug(Missionsflug entity) {
    getEntityManager().merge(entity);
  }

  public void deleteMisionsflug(int id) {
    getEntityManager().createQuery("DELETE FROM Missionsflug m WHERE m.idyaacarskopf = :id", Missionsflug.class)
            .setParameter("id", id)
            .executeUpdate();
  }

  public List<Missionsflug> getMissionsFlug(int id) {
    return getEntityManager().createQuery("SELECT m FROM Missionsflug m WHERE m.idyaacarskopf = :id and m.wegpunkterreicht=false ORDER BY m.reihenfolge", Missionsflug.class)
            .setParameter("id", id)
            .getResultList();
  }

  public List<Missionsflug> getAllWaypointMissionsFlug(int id) {
    return getEntityManager().createQuery("SELECT m FROM Missionsflug m WHERE m.idyaacarskopf = :id ORDER BY m.reihenfolge", Missionsflug.class)
            .setParameter("id", id)
            .getResultList();
  }

  public boolean isLaufendeMission(int id) {
    try {
      Missionsflug mf = getEntityManager().createQuery("SELECT m FROM Missionsflug m WHERE m.iduser = :id", Missionsflug.class)
              .setParameter("id", id)
              .setMaxResults(1)
              .getSingleResult();
      return true;
    } catch (NoResultException e) {
      return false;
    }
  }

  public void createBankbuchung(Bank entity) {
    getEntityManager().persist(entity);
  }

  public void editMeinFlugzeug(Flugzeugemietkauf entity) {
    getEntityManager().merge(entity);
  }

  public void editUser(Benutzer entity) {
    getEntityManager().merge(entity);
  }

  public void createLogbuchEintrag(Fluglogbuch entity) {
    getEntityManager().persist(entity);
  }

  /*
  ******************** Missionsflug ENDE
   */
 /*
  ******************** Flugzeug BEGINN
   */
  public List<ViewFlugzeugeMietKauf> getFlugzeuge() {
    return getEntityManager().createQuery("SELECT f FROM ViewFlugzeugeMietKauf f WHERE f.idflugzeugBesitzer = -320", ViewFlugzeugeMietKauf.class).getResultList();
  }

  public ViewFlugzeugeMietKauf readFlugzeug(int id) {
    try {
      return getEntityManager().createQuery("SELECT f FROM ViewFlugzeugeMietKauf f WHERE f.idMietKauf = :id", ViewFlugzeugeMietKauf.class)
              .setParameter("id", id)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public Flugzeugemietkauf readFlugzeugMietKauf(int id) {
    try {
      return getEntityManager().createQuery("SELECT f FROM Flugzeugemietkauf f WHERE f.idMietKauf = :id", Flugzeugemietkauf.class)
              .setParameter("id", id)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  /*
  ******************** Flugzeug BEGINN
   */

 /*
  ****************** Config - Feinabstimmung
   */
  public Feinabstimmung getConfig() {
    return getEntityManager().createQuery("SELECT c from Feinabstimmung c", Feinabstimmung.class).setMaxResults(1).getSingleResult();
  }

  public Benutzer readUser(int UserID) {
    try {
      return getEntityManager().createQuery("SELECT u from Benutzer u WHERE u.idUser = :userid", Benutzer.class)
              .setParameter("userid", UserID)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

}
