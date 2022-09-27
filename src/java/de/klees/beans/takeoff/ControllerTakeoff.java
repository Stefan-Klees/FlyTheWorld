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

package de.klees.beans.takeoff;

import de.klees.data.Airport;
import de.klees.data.Aircraft;
import de.klees.data.AirportTransfers;
import de.klees.data.Assignement;
import de.klees.data.Bank;
import de.klees.data.FboObjekte;
import de.klees.data.FboUserObjekte;
import de.klees.data.Feinabstimmung;
import de.klees.data.FlugesellschaftPiloten;
import de.klees.data.Fluggesellschaft;
import de.klees.data.FluggesellschaftBonusSystem;
import de.klees.data.Fluggesellschaftmanager;
import de.klees.data.Fluglogbuch;
import de.klees.data.Flugrouten;
import de.klees.data.Flugzeuge;
import de.klees.data.Flugzeugemietkauf;
import de.klees.data.Mail;
import de.klees.data.Sitzkonfiguration;
import de.klees.data.Story;
import de.klees.data.UmbauSitzplatz;
import de.klees.data.Benutzer;
import de.klees.data.UserTyperatings;
import de.klees.data.Yaacarskopf;
import de.klees.data.views.ViewAbrechnungZieleSumme;
import de.klees.data.views.ViewAssiErweiterteLizenzPruefung;
import de.klees.data.views.ViewFBOUserObjekte;
import de.klees.data.views.ViewFlugProvisionen;
import de.klees.data.views.ViewFlughafenAuftragsZiele;
import de.klees.data.views.ViewFlugzeugeMietKauf;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 *
 * @author Stefan Klees
 * @param <T>
 */
public abstract class ControllerTakeoff<T> {

  private final Class<T> entityClass;

  /**
   *
   * @param entityClass
   */
  public ControllerTakeoff(Class<T> entityClass) {
    this.entityClass = entityClass;
  }

  /**
   *
   * @return
   */
  protected abstract EntityManager getEntityManager();

  /**
   *
   * @param entity
   */
  public void create(T entity) {
    getEntityManager().persist(entity);
  }

  /**
   *
   * @param entity
   */
  public void createBankbuchung(Bank entity) {
    getEntityManager().persist(entity);
  }

  /**
   *
   * @param entity
   */
  public void edit(T entity) {
    getEntityManager().merge(entity);
  }

  public void editFluggesellschaft(Fluggesellschaft entity) {
    getEntityManager().merge(entity);
  }

  public void editMeinFlugzeug(Flugzeugemietkauf entity) {
    getEntityManager().merge(entity);
  }

  public void editFluggesellschaftPilot(FlugesellschaftPiloten entity) {
    getEntityManager().merge(entity);
  }

  public void editFBOObjekt(FboUserObjekte entity) {
    getEntityManager().merge(entity);
  }

  /**
   *
   * @param entity
   */
  public void remove(T entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public void createLogbuchEintrag(Fluglogbuch entity) {
    getEntityManager().persist(entity);
  }

  public void userEdit(Benutzer entity) {
    getEntityManager().merge(entity);
  }

  /*
  ********************************************************************************
  *********************** ACARS Daten BEGINN *************************************
  ********************************************************************************
   */
  public boolean isYAACARSFlightActive(int UserID) {
    try {
      Yaacarskopf YaacarsFlights = getEntityManager().createQuery("SELECT y from Yaacarskopf y where y.userid = :userid", Yaacarskopf.class)
              .setParameter("userid", UserID)
              .setMaxResults(1)
              .getSingleResult();
      if (YaacarsFlights != null) {
        return true;
      }
    } catch (NoResultException e) {
      return false;
    }
    return false;
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

  public Yaacarskopf YAACARSFlightInfo(int UserID) {
    try {
      return getEntityManager().createQuery("SELECT y FROM Yaacarskopf y WHERE y.userid = :userid", Yaacarskopf.class)
              .setParameter("userid", UserID)
              .setMaxResults(1)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }

  }

  public void saveYAACARSKopf(Yaacarskopf entity) {
    getEntityManager().merge(entity);
  }

  public Yaacarskopf getYAACARSFlight(int userid) {
    try {
      return getEntityManager().createQuery("SELECT y FROM Yaacarskopf y WHERE y.userid = :userid", Yaacarskopf.class)
              .setParameter("userid", userid)
              .getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  public String YAACARSDurchschnittsgeschwindigkeit(int FlightID) {
    try {
      String Abfrage = "SELECT MAX(tas) as TAS FROM yaacarspositionen where idyaacarskopf = " + FlightID;
      return getEntityManager().createNativeQuery(Abfrage).getSingleResult().toString();
    } catch (NoResultException e) {
      return "-1";
    }
  }

  public int TASDurchschnittsgeschwindigkeit(int FlightID) {
    try {
      String Abfrage = "SELECT AVG(tas) as TAS FROM yaacarspositionen where idyaacarskopf = " + FlightID;
      BigDecimal tas = (BigDecimal) getEntityManager().createNativeQuery(Abfrage).getSingleResult();
      return tas.intValue();
    } catch (NoResultException e) {
      return 0;
    }
  }

  public void YAACARSDatenLoeschen(int YAACARSID) {
    getEntityManager().createNativeQuery("delete FROM yaacarskopf where idyaacarskopf = " + YAACARSID).executeUpdate();
    getEntityManager().createNativeQuery("delete FROM yaacarspositionen where idyaacarskopf = " + YAACARSID).executeUpdate();
  }

  public void createYaacarskopf(Yaacarskopf entity) {
    getEntityManager().persist(entity);
  }

  public List<Yaacarskopf> listYaacarsFluefe() {
    return getEntityManager().createQuery("SELECT y FROM Yaacarskopf y ORDER BY y.flugerstelltam DESC", Yaacarskopf.class).getResultList();
  }

  /*
  ********************************************************************************
  *********************** ACARS Daten ENDE ***************************************
  ********************************************************************************
   */
 /*
  *********************** Config Auslesen Beginn***************************************
   */
  public Feinabstimmung readConfig() {
    return getEntityManager().createQuery("SELECT c from Feinabstimmung c", Feinabstimmung.class).setMaxResults(1).getSingleResult();
  }

  public Benutzer readUser(int userid) {
    try {
      return getEntityManager().createQuery("SELECT u FROM Benutzer u WHERE u.idUser = :userid", Benutzer.class)
              .setParameter("userid", userid)
              .getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  /*
  *********************** Config Auslesen ENDE ***************************************
   */

 /*
  *********************** Sitzkonfiguration  Beginn***************************************
   */
  public Sitzkonfiguration getKonfig(int id) {
    return getEntityManager().createQuery("SELECT k FROM Sitzkonfiguration k WHERE k.idsitzKonfiguration =:id", Sitzkonfiguration.class)
            .setParameter("id", id)
            .getSingleResult();
  }

  public Sitzkonfiguration getKonfiguration(int id) {
    return getEntityManager().createQuery("SELECT k FROM Sitzkonfiguration k WHERE k.idsitzKonfiguration =:id", Sitzkonfiguration.class)
            .setParameter("id", id)
            .getSingleResult();
  }


  /*
  *********************** Sitzkonfiguration  ENDE***************************************
   */
 /*
  *********************** Live ACARS  Beginn***************************************
   */
  public List<Airport> readAirportsByKlasse(int klasse, String land) {

    if (klasse == 0) {
      return getEntityManager().createQuery("SELECT a from Airport a where a.land = :land", Airport.class
      )
              .setParameter("land", land)
              .getResultList();

    } else {
      return getEntityManager().createQuery("SELECT a from Airport a where a.klasse = :klasse and a.land = :land", Airport.class
      )
              .setParameter("land", land)
              .setParameter("klasse", klasse)
              .getResultList();

    }

  }

  @SuppressWarnings("unchecked")
  public List<String> readLaenderFromAirports() {
    return getEntityManager().createQuery("SELECT DISTINCT l.land FROM Airport l ORDER BY l.land").getResultList();
  }

  /*
  *********************** Live ACARS ENDE ***************************************
   */
 /*
  ********************* Fluggesellschaft BEGINN
   */
  /**
   *
   * @param UserID
   * @param AirlineID
   * @return
   */
  public List<FlugesellschaftPiloten> findUserIDInAirlinePiloten(int UserID, int AirlineID) {
    return getEntityManager().createNamedQuery("FlugesellschaftPiloten.findByIduserAndIDFluggesellschaft", FlugesellschaftPiloten.class)
            .setParameter("iduser", UserID)
            .setParameter("idflugesellschaft", AirlineID)
            .getResultList();
  }

  public Fluggesellschaft readFluggesellschaft(int idAirline) {
    try {
      return getEntityManager().createQuery("SELECT f FROM Fluggesellschaft f WHERE f.idFluggesellschaft =:idAirline", Fluggesellschaft.class)
              .setParameter("idAirline", idAirline)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  // Manager der Fluggesellschaft auslesen
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

  // Pilot der Fluggesellschaft auslesen
  public FlugesellschaftPiloten readFluggesellschaftPilot(int idAirline, int UserID) {
    try {
      return getEntityManager().createQuery("SELECT f FROM FlugesellschaftPiloten f WHERE f.idflugesellschaft =:idAirline and f.iduser =:userID", FlugesellschaftPiloten.class)
              .setParameter("userID", UserID)
              .setParameter("idAirline", idAirline)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  public List<Fluggesellschaft> findFluggessellschaft(int UserID, int AirlineID) {
    return getEntityManager().createNamedQuery("Fluggesellschaft.findByIdFluggesellschaft")
            .setParameter("idFluggesellschaft", AirlineID)
            .getResultList();
  }

  public List<FluggesellschaftBonusSystem> getBoniList(int fgid) {
    return getEntityManager().createQuery("SELECT b FROM FluggesellschaftBonusSystem b WHERE b.idfluggesellschaft = :fgid ORDER BY b.zeit", FluggesellschaftBonusSystem.class)
            .setParameter("fgid", fgid)
            .getResultList();
  }

  /*
  ********************* Fluggesellschaft ENDE
   */
 /*
  ********************* Typerating BEGINN
   */
  public UserTyperatings getTyperating(int userid, String typerating) {
    try {
      return getEntityManager().createQuery("SELECT t FROM UserTyperatings t WHERE t.userID = :userid and t.typeRating = :typerating", UserTyperatings.class)
              .setParameter("userid", userid)
              .setParameter("typerating", typerating)
              .getSingleResult();

    } catch (NoResultException e) {
      return null;
    }
  }

  public void saveTypeRating(UserTyperatings entity) {
    getEntityManager().merge(entity);
  }

  public void createTypeRating(UserTyperatings entity) {
    getEntityManager().persist(entity);
  }

  /*
  ********************* Typerating ENDE
   */
  /**
   *
   * @param id
   * @return
   */
  @SuppressWarnings("unchecked")
  public T find(Object id) {
    return getEntityManager().find(entityClass, id);
  }

  public List<Abrechnungspositionen> getAbrechnungsPositionen(int UserID) {
    return getEntityManager().createNamedQuery("Abrechnungspositionen.findByIdowner", Abrechnungspositionen.class
    )
            .setParameter("idowner", UserID)
            .getResultList();
  }

  public List<Assignement> getAllAssignmentByZielFlughafen(String ICAO, int UserID) {
    return getEntityManager().createQuery("SELECT a FROM Assignement a WHERE a.toIcao =:icao and a.idowner =:userid and a.userlock = 1 ", Assignement.class)
            .setParameter("icao", ICAO)
            .setParameter("userid", UserID)
            .getResultList();
  }

  public List<Assignement> getAllSpritFaesser(String ICAO, int UserID) {
    return getEntityManager().createQuery("SELECT a FROM Assignement a WHERE a.toIcao =:icao and a.idowner =:userid and a.userlock = 1 and a.routenArt = 5 ", Assignement.class)
            .setParameter("icao", ICAO)
            .setParameter("userid", UserID)
            .getResultList();
  }

  public List<ViewFlugProvisionen> getProvisionen(int userID, String icao) {
    return getEntityManager().createQuery("SELECT p FROM ViewFlugProvisionen p WHERE p.idowner = :userid AND p.icao = :icao", ViewFlugProvisionen.class)
            .setParameter("icao", icao)
            .setParameter("userid", userID)
            .getResultList();
  }

  public List<Assignement> findAssignmentByToIcaoInTakeOffList(String Suchtext, int UserID) {
    return getEntityManager().createNamedQuery("Assignement.findByIdownerReadyForTakeoffnachIcao", Assignement.class)
            .setParameter("idowner", UserID)
            .setParameter("toIcao", Suchtext)
            .setParameter("active", 0)
            .getResultList();
  }

  public List<Assignement> onIfGemicht(int userid) {
    return getEntityManager().createQuery("SELECT a FROM Assignement a where a.userlock = 0 and a.active=0 and a.idowner = :owner GROUP BY a.idAirline ", Assignement.class)
            .setParameter("owner", userid)
            .getResultList();
  }

  public List<Assignement> onIfFreelancerJob(int userid) {
    return getEntityManager().createQuery("SELECT a FROM Assignement a where a.idAirline = -1 and a.userlock = 0 and a.active=0 and a.idowner = :owner GROUP BY a.idAirline ", Assignement.class)
            .setParameter("owner", userid)
            .getResultList();
  }

  public List<ViewAssiErweiterteLizenzPruefung> onMehrereKlassen(int userid) {
    return getEntityManager().createQuery("SELECT a FROM ViewAssiErweiterteLizenzPruefung a where a.flugzeugklasse > 0 and a.userlock = 0 and a.active=0 and a.idowner = :owner GROUP BY a.flugzeugklasse", ViewAssiErweiterteLizenzPruefung.class)
            .setParameter("owner", userid)
            .getResultList();
  }

  public List<Assignement> getGroupOfLizenz(String Suchtext, int UserID) {
    return getEntityManager().createQuery("SELECT a FROM Assignement a WHERE a.locationIcao =:Suchtext and a.idowner = :userid and a.active = 0 GROUP BY a.lizenz", Assignement.class)
            .setParameter("Suchtext", Suchtext)
            .setParameter("userid", UserID)
            .getResultList();
  }

  public List<ViewAbrechnungZieleSumme> getAbrechnungsziele(int userid, String icao) {
    return getEntityManager().createQuery("SELECT a FROM ViewAbrechnungZieleSumme a WHERE a.idowner = :userid and a.locationIcao = :icao ORDER BY a.toIcao", ViewAbrechnungZieleSumme.class)
            .setParameter("userid", userid)
            .setParameter("icao", icao)
            .getResultList();
  }

  /**
   *
   * @param Suchtext
   * @param UserID
   * @return
   */
  public List<Aircraft> findAllUserAircrafts(String Suchtext, int UserID) {
    return getEntityManager().createNamedQuery("Aircraft.findByOwnerofaircraft", Aircraft.class
    )
            .setParameter("ownerofaircraft", UserID)
            .setParameter("model", Suchtext)
            .getResultList();
  }

  /**
   *
   * @param UserID
   * @param location
   * @return
   */
  public List<ViewFlugzeugeMietKauf> findAllMyRentedAircrafts(int UserID, String location) {
    return getEntityManager().createQuery("SELECT f FROM ViewFlugzeugeMietKauf f where f.idUserDerFlugzeugGesperrtHat = :userid AND f.istGesperrt = :gesperrt", ViewFlugzeugeMietKauf.class
    )
            .setParameter("userid", UserID)
            .setParameter("gesperrt", true)
            .getResultList();
  }

  public ViewFlugzeugeMietKauf findMyRentedAircraft(int UserID) {

    try {
      return getEntityManager().createQuery("SELECT f FROM ViewFlugzeugeMietKauf f WHERE f.idUserDerFlugzeugGesperrtHat =:userID AND f.istGesperrt =:gesperrt ", ViewFlugzeugeMietKauf.class)
              .setParameter("userID", UserID)
              .setParameter("gesperrt", true)
              .getSingleResult();

    } catch (NoResultException e) {
      return null;
    }
  }

  public ViewFlugzeugeMietKauf findAircraftForSupport(int ID) {
    try {
      return getEntityManager().createQuery("SELECT f FROM ViewFlugzeugeMietKauf f WHERE f.idMietKauf =:id", ViewFlugzeugeMietKauf.class)
              .setParameter("id", ID)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public Flugzeugemietkauf findeMeinFlugzeug(int FlugzeugID) {
    return getEntityManager().createQuery("SELECT f FROM Flugzeugemietkauf f WHERE f.idMietKauf=:flugzeugID", Flugzeugemietkauf.class
    )
            .setParameter("flugzeugID", FlugzeugID)
            .getSingleResult();
  }

  public Flugzeuge readFlugzeug(int id) {
    return getEntityManager().createQuery("SELECT f FROM Flugzeuge f WHERE f.idFlugzeug =:id", Flugzeuge.class)
            .setParameter("id", id)
            .getSingleResult();
  }

  /**
   *
   * @param Icao
   * @return
   */
  public List<ViewFlugzeugeMietKauf> findAllRentableAircraftsInIcao(String Icao) {
    return getEntityManager().createQuery("SELECT f FROM ViewFlugzeugeMietKauf f where f.aktuellePositionICAO = :standort AND f.istGesperrt =:gesperrt AND f.istMietbar= :mietbar ",
            ViewFlugzeugeMietKauf.class
    )
            .setParameter("standort", Icao)
            .setParameter("gesperrt", false)
            .setParameter("mietbar", true)
            .getResultList();
  }

  /**
   *
   * @param OwnerID
   * @return
   */
  public List<Assignement> findByMyFlight(int OwnerID, String ICAO) {
    return getEntityManager().createQuery("SELECT a FROM Assignement a WHERE a.idowner = :idowner and a.active = 0 and a.locationIcao = :icao  order by a.toIcao", Assignement.class)
            .setParameter("idowner", OwnerID)
            .setParameter("icao", ICAO)
            .getResultList();
  }

  public UmbauSitzplatz UmbauDaten(int FgzID) {
    try {
      return getEntityManager().createQuery("SELECT u from UmbauSitzplatz u WHERE u.idFlugzeugMietKauf = :fgzid", UmbauSitzplatz.class)
              .setParameter("fgzid", FgzID).getSingleResult();

    } catch (NoResultException e) {
      return null;
    }
  }

  public void removeUmbau(UmbauSitzplatz entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  /*
  ************************* Benutzer Beginn
   */
  /**
   *
   * @param UserID
   * @return
   */
  public Benutzer findByUserID(int UserID) {
    return getEntityManager().createQuery("SELECT u from Benutzer u WHERE u.idUser = :userid", Benutzer.class
    )
            .setParameter("userid", UserID)
            .getSingleResult();
  }

  /**
   *
   * @param UserID
   * @return
   */
  public Benutzer findByUserName(int UserID) {
    return (Benutzer) getEntityManager().createQuery("SELECT u FROM Benutzer u WHERE u.idUser = :idUser", Benutzer.class)
            .setParameter("idUser", UserID)
            .getSingleResult();
  }

  /*
  ************************* Benutzer Ende
   */
 /*
  ************************* FBO - Verwaltung
   */
  public void FboBestandVerbuchen(FboUserObjekte entity) {
    getEntityManager().merge(entity);
  }

  public List<ViewFBOUserObjekte> getTankstellen(String icao) {
    return getEntityManager().createQuery("SELECT t from ViewFBOUserObjekte t WHERE t.icao = :icao AND (t.tankstelle = true or t.spritlager = true)", ViewFBOUserObjekte.class
    )
            .setParameter("icao", icao)
            .getResultList();
  }

  public FboObjekte readFBO(int fboid) {
    try {
      return getEntityManager().createQuery("SELECT b from FboObjekte b where b.idObjekt =:fboid", FboObjekte.class)
              .setParameter("fboid", fboid)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public FboUserObjekte getUserFBOObjekt(int objektID) {

    try {
      return getEntityManager().createQuery("SELECT o from FboUserObjekte o WHERE o.iduserfbo = :objektID", FboUserObjekte.class)
              .setParameter("objektID", objektID)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }

  }

  public ViewFBOUserObjekte getViewUserFBOObjekt(int objektID) {
    try {
      return getEntityManager().createQuery("SELECT v from ViewFBOUserObjekte v WHERE v.iduserfbo = :objektID", ViewFBOUserObjekte.class)
              .setParameter("objektID", objektID)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public List<ViewFBOUserObjekte> findAllTerminals(String icao) {
    return getEntityManager().createQuery("SELECT t FROM ViewFBOUserObjekte t WHERE t.icao = :icao AND t.abfertigungsTerminal = TRUE ", ViewFBOUserObjekte.class)
            .setParameter("icao", icao)
            .getResultList();
  }

  public String getFBOBezeichnung(int fboid) {
    try {
      return getEntityManager().createQuery("SELECT f FROM FboUserObjekte f WHERE f.iduserfbo = :fboid", FboUserObjekte.class
      )
              .setParameter("fboid", fboid)
              .getSingleResult().getName();

    } catch (NoResultException e) {
      return "";
    }
  }

  public List getTankstellenBestaende(String icao) {
    String Abfrage = "SELECT sum(bestandAVGASkg*preisAVGAS) as AVGAS, sum(bestandJETAkg*preisJETA) as JETA FROM fboUserObjekte where icao = '" + icao + "'";
    try {
      return getEntityManager().createNativeQuery(Abfrage).getResultList();
    } catch (NoResultException e) {
      return null;
    }

  }

  /*
  ************************* FBO - Verwaltung ENDE
   */
  /**
   *
   * @param RouteID
   * @return
   */
  public List<Flugrouten> findByIdRoute(int RouteID) {
    return getEntityManager().createNamedQuery("Flugrouten.findByIdFlugrouten", Flugrouten.class
    )
            .setParameter("idFlugrouten", RouteID)
            .getResultList();
  }

  public Flugrouten getFlugroute(int routeid) {
    try {
      return getEntityManager().createQuery("SELECT f FROM Flugrouten f WHERE f.idFlugrouten = :routeid", Flugrouten.class)
              .setParameter("routeid", routeid)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public void saveFlugroute(Flugrouten entity) {
    getEntityManager().merge(entity);
  }

  @SuppressWarnings("Unchecked")
  public List getRoutenMengen(String locationIcao, String toIcao) {

    String Abfrage = "SELECT  "
            + "    SUM(tmp1.tmp_pax) AS pax, "
            + "    SUM(tmp1.tmp_cargo) AS cargo, "
            + "    SUM(tmp1.tmp_bc) AS bc, "
            + "    SUM(tmp1.tmp_Betrag) as Auftragswert, "
            + "    tmp1.tmp_distance as Entfernung "
            + "    FROM "
            + "    (SELECT  "
            + "        IF(routenArt = 1, SUM(ammount), 0) AS tmp_pax, "
            + "            IF(routenArt = 2, SUM(ammount), 0) AS tmp_cargo, "
            + "            IF(routenArt = 4, SUM(ammount), 0) AS tmp_bc, "
            + "            sum(assignement.pay) as tmp_Betrag, "
            + "            distance as tmp_distance, "
            + "            from_icao, "
            + "            to_icao "
            + "    FROM "
            + "        assignement "
            + "    WHERE "
            + "        location_icao = '" + locationIcao + "' "
            + "            AND to_icao = '" + toIcao + "' AND idowner <= 0 "
            + "    GROUP BY routenArt) AS tmp1";

    return getEntityManager().createNativeQuery(Abfrage).getResultList();
  }

  public List<ViewFlughafenAuftragsZiele> getFlughafenAuftragsZiele(String icao) {
    return getEntityManager().createQuery("SELECT f FROM ViewFlughafenAuftragsZiele f WHERE f.locationIcao = :icao", ViewFlughafenAuftragsZiele.class)
            .setParameter("icao", icao)
            .getResultList();
  }

  public List<ViewFlughafenAuftragsZiele> getFlughafenAuftragsZieleNachICAO(String icao) {
    return getEntityManager().createQuery("SELECT f FROM ViewFlughafenAuftragsZiele f WHERE f.toIcao = :icao", ViewFlughafenAuftragsZiele.class)
            .setParameter("icao", icao)
            .getResultList();
  }

  public List<Flugrouten> getFlugrouten(int fgid) {
    return getEntityManager().createQuery("SELECT r FROM Flugrouten r WHERE r.idFluggesellschaft = :fgid GROUP BY r.vonIcao , r.nachicao", Flugrouten.class)
            .setParameter("fgid", fgid)
            .getResultList();
  }

  /**
   *
   * @param icao
   * @return
   */
  public Airport getAirportByIcao(String icao) {
    try {
      return getEntityManager().createQuery("SELECT a FROM Airport a WHERE a.icao = :icao", Airport.class)
              .setParameter("icao", icao)
              .getSingleResult();

    } catch (NoResultException e) {
      return null;
    }
  }

  public void newTransfer(AirportTransfers entity) {
    getEntityManager().persist(entity);
  }

  /**
   *
   * @param icao
   * @return
   */
  public List<Airport> findAirportByIcao(String icao) {
    return getEntityManager().createNamedQuery("Airport.findByIcao", Airport.class
    ).setParameter("icao", icao).getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<Airport> readZielFlughaefen(double vonLat, double bisLat, double vonLong, double bisLong) {
    String Abfrage = "SELECT * FROM Airport where ("
            + "latitude between " + vonLat + " and " + +bisLat + " and "
            + "longitude between " + vonLong + " and " + bisLong + ")";

    //System.out.println(Abfrage);
    return getEntityManager().createNativeQuery(Abfrage, Airport.class
    ).getResultList();
  }

  /**
   *
   * @param AssID
   */
  @SuppressWarnings("Unchecked")
  public void onAssignmentOfHold(int AssID) {
    getEntityManager().createNativeQuery("UPDATE assignement set userlock=1 WHERE idassignement=" + AssID).executeUpdate();
  }

  /**
   *
   * @param AssID
   */
  @SuppressWarnings("Unchecked")
  public void onAssignmentFromHold(int AssID) {
    getEntityManager().createNativeQuery("UPDATE assignement set userlock=0 WHERE idassignement=" + AssID).executeUpdate();
  }

  public void deleteFinishAssignment(Assignement entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public void saveAssignment(Assignement entity) {
    getEntityManager().merge(entity);
  }

  /**
   *
   * @param Abfrage
   * @return
   */
  @SuppressWarnings("Unchecked")
  public int testTable(String Abfrage) {
    return getEntityManager().createNativeQuery(Abfrage).executeUpdate();

  }

  /**
   *
   * @param Abfrage
   * @return
   */
  @SuppressWarnings("Unchecked")
  public int updateTable(String Abfrage) {
    return getEntityManager().createNativeQuery(Abfrage).executeUpdate();

  }

  /**
   *
   * @param UserID
   * @return
   */
  @SuppressWarnings("Unchecked")
  public double BankSaldo(int UserID) {
    String Abfrage = "SELECT SUM( `betrag` ) FROM `WorldOfFly`.`bank` AS `bank` WHERE `userID` = " + String.valueOf(UserID);
    return (double) getEntityManager().createNativeQuery(Abfrage).getSingleResult();
  }

  public String getKontoName(String KontoNummer) {
    try {
      return getEntityManager().createQuery("SELECT b FROM Bank b WHERE b.bankKonto = :kontoNummer", Bank.class)
              .setParameter("kontoNummer", KontoNummer)
              .getResultList().get(0).getKontoName();

    } catch (Exception e) {
      return "";
    }
  }

  public List<Story> findAllStorysByLizenz(int Lizenz) {
    return getEntityManager().createNamedQuery("Story.findAllByLizenz", Story.class
    ).setParameter("flugzeuglizenz", Lizenz)
            .getResultList();
  }

  public void saveUserMail(Mail entity) {
    getEntityManager().persist(entity);
  }

  public Benutzer findUser(String Suchtext) {
    return getEntityManager().createQuery("SELECT u FROM Benutzer u where u.name1 = :UserName", Benutzer.class)
            .setParameter("UserName", Suchtext)
            .getSingleResult();
  }
}
