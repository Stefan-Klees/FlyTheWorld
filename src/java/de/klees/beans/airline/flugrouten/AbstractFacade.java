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
package de.klees.beans.airline.flugrouten;

import de.klees.data.Airport;
import de.klees.data.Assignement;
import de.klees.data.Bank;
import de.klees.data.FboUserObjekte;
import de.klees.data.FlugesellschaftPiloten;
import de.klees.data.Fluggesellschaft;
import de.klees.data.Fluggesellschaftmanager;
import de.klees.data.Flugrouten;
import de.klees.data.Flugzeuge;
import de.klees.data.Sitzkonfiguration;
import de.klees.data.Benutzer;
import de.klees.data.views.ViewFBOUserObjekte;
import de.klees.data.views.ViewFlugzeugeMietKauf;
import de.klees.data.views.ViewRoutenAuftraege;
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

  public Airport readAirport(String ICAO) {
    try {
      return getEntityManager().createQuery("SELECT a FROM Airport a where a.icao = :icao", Airport.class)
              .setParameter("icao", ICAO)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public List<Flugrouten> findAllByAirlineID(int AirlineID) {
    return getEntityManager().createQuery("SELECT f from Flugrouten f WHERE f.idFluggesellschaft = :id ORDER BY f.name", Flugrouten.class)
            .setParameter("id", AirlineID)
            .getResultList();
  }

  public List<Flugrouten> findAllByPilotID(int ID,int FGID) {
    return getEntityManager().createQuery("SELECT f from Flugrouten f WHERE f.idPilot = :id and f.idFluggesellschaft =:fgid ORDER BY f.name", Flugrouten.class)
            .setParameter("id", ID)
            .setParameter("fgid", FGID)
            .getResultList();
  }

  
  public List<Flugrouten> findMeineRouten(int ID) {
    return getEntityManager().createQuery("SELECT f from Flugrouten f WHERE f.idPilot = :id ORDER BY f.name", Flugrouten.class)
            .setParameter("id", ID)
            .getResultList();
  }
  

  public List<Flugrouten> findAllByAirlineIDGroupRoute(int AirlineID) {
    return getEntityManager().createQuery("SELECT f from Flugrouten f WHERE f.idFluggesellschaft = :id GROUP BY f.vonIcao ORDER BY f.vonIcao", Flugrouten.class)
            .setParameter("id", AirlineID)
            .getResultList();
  }

  public List<ViewRoutenAuftraege> getAuftraege(String icao, int fgid, int pilot, String toIcao) {

    if (pilot == -1) {
      return getEntityManager().createQuery("SELECT a from ViewRoutenAuftraege a WHERE a.locationIcao like :icao and a.idAirline = :fgid and a.idowner = :pilot and a.toIcao like :toicao ORDER BY a.locationIcao, a.toIcao, a.expires ASC", ViewRoutenAuftraege.class)
              .setParameter("icao", icao)
              .setParameter("fgid", fgid)
              .setParameter("pilot", pilot)
              .setParameter("toicao", toIcao)
              .getResultList();
    }
    return getEntityManager().createQuery("SELECT a from ViewRoutenAuftraege a WHERE a.locationIcao like :icao and a.idAirline = :fgid and a.idowner = :pilot and a.toIcao like :toicao ORDER BY a.locationIcao, a.toIcao, a.expires ASC", ViewRoutenAuftraege.class)
            .setParameter("icao", icao)
            .setParameter("fgid", fgid)
            .setParameter("pilot", pilot)
            .setParameter("toicao", toIcao)
            .getResultList();
  }

  public List<ViewRoutenAuftraege> getAlleAuftraege(int fgid) {
    return getEntityManager().createQuery("SELECT a from ViewRoutenAuftraege a WHERE a.idAirline = :fgid ORDER BY a.locationIcao, a.toIcao, a.expires ASC", ViewRoutenAuftraege.class)
            .setParameter("fgid", fgid)
            .getResultList();
  }

  public List<ViewRoutenAuftraege> getPiloten(String icao, int fgid) {
    return getEntityManager().createQuery("SELECT a from ViewRoutenAuftraege a WHERE a.locationIcao = :icao and a.idAirline = :fgid and a.idowner > 0  GROUP BY a.username ORDER BY a.username", ViewRoutenAuftraege.class)
            .setParameter("icao", icao)
            .setParameter("fgid", fgid)
            .getResultList();
  }

  /*
  Liefert alle Piloten die einen Auftrag der Fluggesellschaft angenommen haben
   */
  public List<ViewRoutenAuftraege> getAllPiloten(int fgid) {
    return getEntityManager().createQuery("SELECT a from ViewRoutenAuftraege a WHERE a.idAirline = :fgid and a.idowner > 0  GROUP BY a.username ORDER BY a.username", ViewRoutenAuftraege.class)
            .setParameter("fgid", fgid)
            .getResultList();
  }

  public List<FlugesellschaftPiloten> getFluggesellschaftPiloten(int fgid) {
    return getEntityManager().createQuery("SELECT p from FlugesellschaftPiloten p WHERE p.idflugesellschaft = :fgid", FlugesellschaftPiloten.class)
            .setParameter("fgid", fgid)
            .getResultList();
  }

  public Benutzer readUser(int id) {
    try {
      return getEntityManager().createQuery("SELECT u FROM User u where u.idUser = :id", Benutzer.class)
              .setParameter("id", id)
              .getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  public List<ViewRoutenAuftraege> getZielflughaefen(String icao, int fgid) {
    return getEntityManager().createQuery("SELECT a from ViewRoutenAuftraege a WHERE a.locationIcao = :icao and a.idAirline = :fgid  GROUP BY a.toIcao ORDER BY a.toIcao", ViewRoutenAuftraege.class)
            .setParameter("icao", icao)
            .setParameter("fgid", fgid)
            .getResultList();
  }

  public int onDeleteFlugrouteUndAuftraege(Flugrouten entity) {

    int z = getEntityManager().createQuery("DELETE FROM Assignement a WHERE a.idRoute = :idRoute")
            .setParameter("idRoute", entity.getIdFlugrouten())
            .executeUpdate();

    if (z == 0) {
      z = 1;
    }

    getEntityManager().remove(getEntityManager().merge(entity));

    return z;

  }

  public int onDeleteFluggesellschaft(int idFG) {
    String abfrage = "DELETE Fluggesellschaft, Flugrouten, assignement FROM assignement AS assignement, Flugrouten AS Flugrouten, Fluggesellschaft AS Fluggesellschaft WHERE assignement.idRoute = Flugrouten.idFlugrouten AND Flugrouten.idFluggesellschaft = Fluggesellschaft.idFluggesellschaft AND Fluggesellschaft.idFluggesellschaft = " + idFG;
    return getEntityManager().createNativeQuery(abfrage).executeUpdate();
  }

  public void onAuftraegeZurueckholen(int assid) {
    getEntityManager().createQuery("UPDATE Assignement set idowner = -1, userlock = 0, idjob = -1 WHERE idassignement = :assid ", Assignement.class)
            .setParameter("assid", assid)
            .executeUpdate();
  }

  public void onAuftraegeZuweisen(int assid, int pilot) {
    getEntityManager().createQuery("UPDATE Assignement set idowner = :pilot WHERE idassignement = :assid", Assignement.class)
            .setParameter("pilot", pilot)
            .setParameter("assid", assid)
            .executeUpdate();
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

  @SuppressWarnings("Unchecked")
  public List RoutenAuslastung(String vonIcao, String nachIcao, int fgid) {

    String Abfrage = "SELECT  "
            + "    SUM(tmp1.tmp_Pax) AS Pax, "
            + "    SUM(tmp1.tmp_Cargo) AS Cargo, "
            + "    SUM(tmp1.tmp_bc) AS BC "
            + "FROM "
            + "    (SELECT  "
            + "        IF(routenArt = 1, SUM(ammount), 0) AS tmp_Pax, "
            + "            IF(routenArt = 2, SUM(ammount), 0) AS tmp_Cargo, "
            + "            IF(routenArt = 4, SUM(ammount), 0) AS tmp_bc "
            + "    FROM "
            + "        assignement AS assignement "
            + "    WHERE "
            + "        from_icao = '" + vonIcao + "' AND to_icao = '" + nachIcao + "' "
            + "            AND idAirline = " + fgid
            + "    GROUP BY routenArt) AS tmp1";

    return getEntityManager().createNativeQuery(Abfrage).getResultList();
  }

  public int getAnzahlMaxRouten(int userid, String icao) {

    try {
      String Abfrage = "SELECT CAST(SUM( fboObjekte.anzahlRouten ) AS UNSIGNED) AS `anzahlRouten` FROM fboObjekte AS fboObjekte, fboUserObjekte AS fboUserObjekte "
              + " WHERE fboObjekte.idObjekt = fboUserObjekte.idfboObjekt AND fboUserObjekte.idUser = " + userid + " AND fboUserObjekte.icao = '" + icao + "'";

      //System.out.println("de.klees.beans.airline.flugrouten.AbstractFacade.getAnzahlMaxRouten() " + Abfrage);
      return Integer.valueOf(getEntityManager().createNativeQuery(Abfrage).getSingleResult().toString());

    } catch (NullPointerException e) {
      return 0;
    }

  }

  public int getAnzahlRouten(int userid, String icao) {

    try {
      String Abfrage = "SELECT COUNT( `Flugrouten`.`vonIcao` ) AS `Anzahl` FROM `Fluggesellschaft` AS `Fluggesellschaft`, `User` AS `User`, `Flugrouten` AS `Flugrouten` "
              + "WHERE `Fluggesellschaft`.`userid` = `User`.`idUser` AND `Flugrouten`.`idFluggesellschaft` = `Fluggesellschaft`.`idFluggesellschaft` "
              + "AND `User`.`idUser` = " + userid + " AND `Flugrouten`.`vonIcao` = '" + icao + "' ORDER BY `Anzahl` ASC";
      return Integer.valueOf(getEntityManager().createNativeQuery(Abfrage).getSingleResult().toString());

    } catch (NullPointerException e) {
      return 0;
    }

  }

  public List<ViewFlugzeugeMietKauf> findAllAirlineFlugzeuge(int idAirline) {
    try {
      return getEntityManager().createQuery("SELECT f FROM ViewFlugzeugeMietKauf f WHERE f.fluggesellschaftID = :id", ViewFlugzeugeMietKauf.class)
              .setParameter("id", idAirline)
              .getResultList();
    } catch (Exception e) {
      return null;
    }
  }

  public Flugzeuge readFlugzeug(int FlugzeugID) {
    try {
      return getEntityManager().createQuery("SELECT f FROM Flugzeuge f where f.idFlugzeug =:id", Flugzeuge.class)
              .setParameter("id", FlugzeugID)
              .getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  public ViewFlugzeugeMietKauf readFlugzeugMietKauf(int FlugzeugID) {
    try {
      return getEntityManager().createQuery("SELECT f FROM ViewFlugzeugeMietKauf f where f.idMietKauf =:id", ViewFlugzeugeMietKauf.class)
              .setParameter("id", FlugzeugID)
              .getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  public Sitzkonfiguration readSitzkonfiguration(int KonfigID) {
    try {
      return getEntityManager().createQuery("SELECT k FROM Sitzkonfiguration k WHERE k.idsitzKonfiguration =:id", Sitzkonfiguration.class)
              .setParameter("id", KonfigID)
              .getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  public ViewFBOUserObjekte readFboObjectTerminal(String icao, int idfg) {

    try {
      return getEntityManager().createQuery("SELECT o FROM ViewFBOUserObjekte o WHERE  o.abfertigungsTerminal = TRUE AND o.idfluggesellschaft = :idfg AND o.icao = :icao", ViewFBOUserObjekte.class)
              .setParameter("icao", icao)
              .setParameter("idfg", idfg)
              .setMaxResults(1)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public List<ViewFBOUserObjekte> findAllTerminals(int userid) {
    return getEntityManager().createQuery("SELECT t FROM ViewFBOUserObjekte t WHERE t.idUser = :userid AND t.abfertigungsTerminal = TRUE ORDER BY t.icao ", ViewFBOUserObjekte.class)
            .setParameter("userid", userid)
            .getResultList();
  }

  public List<ViewFBOUserObjekte> findAllFBOs(int userid) {
    return getEntityManager().createQuery("SELECT t FROM ViewFBOUserObjekte t WHERE t.idUser = :userid AND t.fbo = TRUE ", ViewFBOUserObjekte.class)
            .setParameter("userid", userid)
            .getResultList();
  }

  public String getFBOIcao(int fboid) {
    try {
      return getEntityManager().createQuery("SELECT f FROM FboUserObjekte f WHERE f.iduserfbo = :fboid", FboUserObjekte.class)
              .setParameter("fboid", fboid)
              .getSingleResult().getIcao();

    } catch (NoResultException e) {
      return "";
    }
  }

  public String getFBOBezeichnung(int fboid) {
    try {
      return getEntityManager().createQuery("SELECT f FROM FboUserObjekte f WHERE f.iduserfbo = :fboid", FboUserObjekte.class)
              .setParameter("fboid", fboid)
              .getSingleResult().getName();

    } catch (NoResultException e) {
      return "";
    }
  }

  public void createBankbuchung(Bank entity) {
    getEntityManager().persist(entity);
  }

  public List<Assignement> listAuftraegeUeberRoutenID(int id) {
    return getEntityManager().createQuery("SELECT a FROM Assignement a where a.idRoute = :id", Assignement.class)
            .setParameter("id", id)
            .getResultList();
  }

}
