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


package de.klees.beans.luftrettung;

import de.klees.data.AcarsRettungseinsatz;
import de.klees.data.Bank;
import de.klees.data.Feinabstimmung;
import de.klees.data.Fluglogbuch;
import de.klees.data.Flugzeugemietkauf;
import de.klees.data.RettungsZiele;
import de.klees.data.Rettungseinsaetze;
import de.klees.data.Rettungsstationen;
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
 * @param <T>
 */
public abstract class LuftrettungFacade<T> {

  private final Class<T> entityClass;

  public LuftrettungFacade(Class<T> entityClass) {
    this.entityClass = entityClass;
  }

  protected abstract EntityManager getEntityManager();

  public void create(Rettungsstationen entity) {
    getEntityManager().persist(entity);
  }

  public void createEinsatz(Rettungseinsaetze entity) {
    getEntityManager().persist(entity);
  }

  public void edit(Rettungsstationen entity) {
    getEntityManager().merge(entity);
  }

  public void editEinsatz(Rettungseinsaetze entity) {
    getEntityManager().merge(entity);
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

  public void remove(Rettungsstationen entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public void removeEinsatz(Rettungseinsaetze entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public void removeFavorit(RettungsZiele entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public void createFavorit(RettungsZiele entity) {
    getEntityManager().persist(entity);
  }

  public List<RettungsZiele> getFavoriten() {
    return getEntityManager().createQuery("SELECT f FROM RettungsZiele f ORDER BY f.zielbezeichnung", RettungsZiele.class).getResultList();
  }

  public boolean ifExistFavorit(String fav) {
    try {
      RettungsZiele ergebnis = getEntityManager().createQuery("SELECT f FROM RettungsZiele f WHERE f.zielbezeichnung = :fav", RettungsZiele.class)
              .setParameter("fav", fav)
              .getSingleResult();
      return true;
    } catch (NoResultException e) {
      return false;
    }
  }

  public RettungsZiele getFavorit(String fav) {
    try {
      return getEntityManager().createQuery("SELECT f FROM RettungsZiele f WHERE f.zielbezeichnung = :fav", RettungsZiele.class)
              .setParameter("fav", fav)
              .getSingleResult();

    } catch (NoResultException e) {
      return null;
    }
  }

  public List<Rettungsstationen> getAlleStationVerwaltung() {
    return getEntityManager().createQuery("SELECT r FROM Rettungsstationen r", Rettungsstationen.class).getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<Rettungsstationen> getAlleStationen() {

    return getEntityManager().createNativeQuery("SELECT Rettungsstationen.*  FROM Rettungsstationen, Rettungseinsaetze where Rettungsstationen.idRettungsstationen = Rettungseinsaetze.idRettungsstation group by Rettungsstationen.idRettungsstationen",
            Rettungsstationen.class)
            .getResultList();

  }

  public List<Rettungseinsaetze> getAlleEinSaetze(int statID) {
    return getEntityManager().createQuery("SELECT a FROM Rettungseinsaetze a WHERE a.idRettungsstation = :statid ", Rettungseinsaetze.class)
            .setParameter("statid", statID)
            .getResultList();
  }

  /*
  *************** Gemietetes Flugzeug
   */
  public ViewFlugzeugeMietKauf getGemietetesFlugzeug(int ID) {
    try {
      return getEntityManager().createQuery("SELECT f from ViewFlugzeugeMietKauf f WHERE f.idMietKauf = :id", ViewFlugzeugeMietKauf.class)
              .setParameter("id", ID)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public Flugzeugemietkauf getFlugzeug(int ID) {
    try {
      return getEntityManager().createQuery("SELECT f FROM Flugzeugemietkauf f WHERE f.idMietKauf = :id", Flugzeugemietkauf.class)
              .setParameter("id", ID)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

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

  /*
  ************************** ACARS Daten BEGINN
   */
  public void createAuftrag(AcarsRettungseinsatz entity) {
    getEntityManager().persist(entity);
  }

  public void editAuftrag(AcarsRettungseinsatz entity) {
    getEntityManager().merge(entity);
  }

  public void createYAACARSFlight(Yaacarskopf entity) {
    getEntityManager().persist(entity);
  }

  public void editYAACARSFlight(Yaacarskopf entity) {
    getEntityManager().merge(entity);
  }


  
  public int removeAktuellerEinsatz(int UserID) {
    return getEntityManager().createQuery("DELETE FROM AcarsRettungseinsatz a WHERE a.idftwUser = :userid")
            .setParameter("userid", UserID)
            .executeUpdate();
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

/// ************ Acars Daten löschen BEGINN
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

  /*
  ************************** ACARS Daten ENDE
   */
  public Rettungsstationen getRettungsstationEinsatz(int idstat) {
    try {
      return getEntityManager().createQuery("SELECT s FROM Rettungsstationen s WHERE s.idRettungsstationen = :idstat", Rettungsstationen.class)
              .setParameter("idstat", idstat)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public Rettungseinsaetze getRettungsEinsatz(int idret) {
    try {
      return getEntityManager().createQuery("SELECT r FROM Rettungseinsaetze r WHERE r.idRettungseinsaetze = :idret", Rettungseinsaetze.class)
              .setParameter("idret", idret)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public AcarsRettungseinsatz getLaufenderAuftrag(int UserID) {
    try {
      return getEntityManager().createQuery("SELECT a from AcarsRettungseinsatz a WHERE a.idftwUser = :userid", AcarsRettungseinsatz.class)
              .setParameter("userid", UserID)
              .getSingleResult();

    } catch (NoResultException e) {
      return null;
    }
  }

  /**
   *
   * @param entity
   */
  public void createBankbuchung(Bank entity) {
    getEntityManager().persist(entity);
  }

}
