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
package de.klees.beans.airport;

import com.sun.xml.rpc.wsdl.document.schema.BuiltInTypes;
import de.klees.data.Airport;
import de.klees.data.AirportDispatchLog;
import de.klees.data.AirportZiele;
import de.klees.data.Airportjobtemplate;
import de.klees.data.Feinabstimmung;
import de.klees.data.Fluggesellschaft;
import de.klees.data.FlughafenKlassen;
import de.klees.data.views.ViewAirportAnflugZiele;
import de.klees.data.views.ViewAirportTransfers;
import de.klees.data.views.ViewFBOUserObjekte;
import de.klees.data.views.ViewFlugzeuglogFlughafen;
import java.math.BigDecimal;
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

  public void editAirport(Airport entity) {
    getEntityManager().merge(entity);
  }

  public void remove(T entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public void createAirportZiel(AirportZiele entity) {
    getEntityManager().persist(entity);
  }

  public T find(Object id) {
    return getEntityManager().find(entityClass, id);
  }

  public void createDispatchLog(AirportDispatchLog entity) {
    getEntityManager().persist(entity);
  }

  @SuppressWarnings("unchecked")
  public List<T> findAll() {
    javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
    cq.select(cq.from(entityClass));
    return getEntityManager().createQuery(cq).getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<T> findAllbyIcao(String icao) {
    return getEntityManager().createNamedQuery("Airport.findByIcao").setParameter("icao", icao).setMaxResults(5000).getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<T> findAllbyCountry(String Land) {
    return getEntityManager().createQuery("SELECT a FROM Airport a WHERE a.land like :land ").setParameter("land", Land).setMaxResults(5000).getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<T> findAllbyAirportName(String Name) {
    return getEntityManager().createQuery("SELECT a FROM Airport a WHERE a.name like :name ").setParameter("name", Name).setMaxResults(5000).getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<T> findAllbyAirportCity(String Stadt) {
    return getEntityManager().createQuery("SELECT a FROM Airport a WHERE a.stadt like :stadt ").setParameter("stadt", Stadt).setMaxResults(5000).getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<T> findAllbyICAOKuerzell(String icao) {
    return getEntityManager().createQuery("SELECT a FROM Airport a WHERE a.icao like :icao ").setParameter("icao", icao).setMaxResults(5000).getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<T> findAllbyKlasse(int Klasse) {
    return getEntityManager().createQuery("SELECT a FROM Airport a WHERE a.klasse = :klasse ").setParameter("klasse", Klasse).setMaxResults(5000).getResultList();
  }

  public List<Airport> getLaender() {
    return getEntityManager().createQuery("SELECT a from Airport a GROUP BY a.land", Airport.class).getResultList();
  }

  public List<Airportjobtemplate> getTemplates() {
    return getEntityManager().createQuery("SELECT t FROM Airportjobtemplate t GROUP BY t.templatename ORDER BY t.templatename ", Airportjobtemplate.class)
            .getResultList();
  }

  public List<Airport> getBundesLaender(String laender) {
    return getEntityManager().createQuery("SELECT a FROM Airport a where a.land = :land GROUP BY a.bundesland", Airport.class)
            .setParameter("land", laender)
            .getResultList();

  }

  public List<Airport> getAiportsDetailsuche(String Land, String Bundesland, int Klasse) {

    if (Klasse == -1) {

      if (Bundesland.equals("")) {
        return getEntityManager().createQuery("SELECT a FROM Airport a WHERE a.land = :land", Airport.class)
                .setParameter("land", Land)
                .getResultList();
      }

      return getEntityManager().createQuery("SELECT a FROM Airport a WHERE a.land = :land AND a.bundesland =:bundesland", Airport.class)
              .setParameter("land", Land)
              .setParameter("bundesland", Bundesland)
              .getResultList();

    }

    if (Bundesland.equals("")) {
      return getEntityManager().createQuery("SELECT a FROM Airport a WHERE a.land = :land AND a.klasse = :klasse", Airport.class)
              .setParameter("land", Land)
              .setParameter("klasse", Klasse)
              .getResultList();
    } else {
      return getEntityManager().createQuery("SELECT a FROM Airport a WHERE a.land = :land AND a.bundesland = :bundesland AND a.klasse = :klasse", Airport.class)
              .setParameter("land", Land)
              .setParameter("bundesland", Bundesland)
              .setParameter("klasse", Klasse)
              .getResultList();
    }

  }

  public int getMengeRoutenPaxAmFlughafen(String vonIcao) {
    try {
      BigDecimal wert = (BigDecimal) getEntityManager().createNativeQuery("select sum(ammount) as Menge from assignement "
              + "where routenArt=1 and NOT createdbyuser = 'FTW-System' and location_icao = '" + vonIcao + "'")
              .getSingleResult();
      return wert.intValueExact();

    } catch (Exception e) {
      return 0;
    }
  }

  public List<FlughafenKlassen> findAllKlassen() {
    return getEntityManager().createQuery("SELECT f FROM FlughafenKlassen f ORDER BY f.klasseNummer", FlughafenKlassen.class).getResultList();
  }

  public Feinabstimmung readConfig() {
    return getEntityManager().createQuery("SELECT c from Feinabstimmung c", Feinabstimmung.class).getSingleResult();
  }

  @SuppressWarnings("unchecked")
  public List<Airport> getJobFlughaefen(double vonLat, double bisLat, double vonLong, double bisLong, String icao, int vonKlasse, int bisKlasse) {
    String Abfrage = "SELECT * FROM Airport where ("
            + "latitude between " + vonLat + " and " + +bisLat + " and "
            + "longitude between " + vonLong + " and " + bisLong + ") and "
            + "not icao='" + icao + "' and (klasse >= " + vonKlasse + " and klasse <= " + bisKlasse + ")";

    //System.out.println(Abfrage);
    return getEntityManager().createNativeQuery(Abfrage, Airport.class).getResultList();
  }

  public void deleteListZielAirports(int id) {
    getEntityManager().createNativeQuery("delete FROM Airport_Ziele where idairport = " + id).executeUpdate();
  }

  public boolean ifExistAnflugziel(int idairport, String icaoziel) {
    try {
      getEntityManager().createQuery("SELECT z FROM AirportZiele z WHERE z.idAirport = :idairport and z.iCAOZiel = :icaoziel", AirportZiele.class)
              .setParameter("idairport", idairport)
              .setParameter("icaoziel", icaoziel)
              .getSingleResult();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public List<ViewAirportAnflugZiele> AnflugZiele(int id) {
    return getEntityManager().createQuery("SELECT v from ViewAirportAnflugZiele v WHERE v.quellidAirport =:idquelle", ViewAirportAnflugZiele.class)
            .setParameter("idquelle", id)
            .getResultList();
  }

  public void onDeleteAnflugZiel(int ID) {
    getEntityManager().createQuery("DELETE FROM AirportZiele z where z.idListe = :id", AirportZiele.class)
            .setParameter("id", ID).executeUpdate();
  }

  public Airport readAirportDaten(String icao) {
    try {
      return getEntityManager().createQuery("SELECT a FROM Airport a WHERE a.icao =:icao", Airport.class)
              .setParameter("icao", icao)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public Airport readAirportDatenUeberID(int id) {
    return getEntityManager().createQuery("SELECT a FROM Airport a WHERE a.idairport =:id", Airport.class)
            .setParameter("id", id)
            .getSingleResult();
  }

  public List<ViewAirportTransfers> getTransfer(String icao, Date startDatum, Date endDatum) {
    try {
      return getEntityManager().createQuery("SELECT v from ViewAirportTransfers v where v.datum BETWEEN :startDatum and :endDatum and v.icao = :icao", ViewAirportTransfers.class)
              .setParameter("startDatum", startDatum)
              .setParameter("endDatum", endDatum)
              .setParameter("icao", icao)
              .getResultList();

    } catch (NoResultException e) {
      return null;
    }
  }

  public List<Fluggesellschaft> getFluggesellschaften(String icao) {
    return getEntityManager().createQuery("SELECT f FROM Fluggesellschaft f WHERE f.icao =:icao", Fluggesellschaft.class)
            .setParameter("icao", icao)
            .getResultList();
  }

  public long wievieleFBOAmFlughafen(String icao) {
    return (long) getEntityManager().createQuery("SELECT COUNT (f) from ViewFBOUserObjekte f WHERE f.icao = :icao AND f.fbo = TRUE")
            .setParameter("icao", icao)
            .getSingleResult();
  }

  @SuppressWarnings("unchecked")
  public boolean ifExistAirport(String icao) {
    try {
      if (getEntityManager().createNamedQuery("Airport.findByIcao", Airport.class).setParameter("icao", icao).getResultList().size() > 0) {
        return true;
      } else {
        return false;
      }
    } catch (Exception e) {
      return false;
    }
  }

  @SuppressWarnings("unchecked")
  public boolean ifExistFlughafenInListe(String icao, int AirportID) {
    try {
      if (getEntityManager().createQuery("SELECT a from AirportZiele a WHERE a.iCAOZiel =:icao AND a.idAirport =:airportID", AirportZiele.class)
              .setParameter("icao", icao)
              .setParameter("airportID", AirportID)
              .getResultList().size() > 0) {
        return true;
      } else {
        return false;
      }
    } catch (Exception e) {
      return false;
    }
  }

  public int LoescheFlughafenAusListe(String icao, int AirportID) {
    return getEntityManager().createQuery("DELETE FROM AirportZiele a WHERE a.iCAOZiel = :icao and a.idAirport =:airportID")
            .setParameter("icao", icao)
            .setParameter("airportID", AirportID)
            .executeUpdate();
  }

  public List<ViewFlugzeuglogFlughafen> getFlugzeugLog(String icao) {
    return getEntityManager().createQuery("SELECT f FROM ViewFlugzeuglogFlughafen f WHERE f.fromicao = :icao OR f.toicao =:icao  ORDER BY f.flugDatum DESC", ViewFlugzeuglogFlughafen.class)
            .setParameter("icao", icao)
            .getResultList();
  }

  public List<ViewFBOUserObjekte> getTerminals(String icao) {
    return getEntityManager().createQuery("SELECT f FROM ViewFBOUserObjekte f WHERE f.icao = :icao and f.abfertigungsTerminal = true", ViewFBOUserObjekte.class)
            .setParameter("icao", icao)
            .getResultList();
  }

  public List<ViewFBOUserObjekte> findTankstellenByICAO(String icao) {
    return getEntityManager().createQuery("SELECT t FROM ViewFBOUserObjekte t WHERE (t.tankstelle=TRUE OR t.spritlager = TRUE) AND t.icao = :icao", ViewFBOUserObjekte.class)
            .setParameter("icao", icao)
            .getResultList();
  }

  public List<ViewFBOUserObjekte> findFBOsByICAO(String icao) {
    return getEntityManager().createQuery("SELECT t FROM ViewFBOUserObjekte t WHERE t.fbo=true AND t.icao = :icao", ViewFBOUserObjekte.class)
            .setParameter("icao", icao)
            .getResultList();
  }

  public List<AirportDispatchLog> getDispatchLog(String icao) {
    return getEntityManager().createQuery("SELECT a FROM AirportDispatchLog a WHERE a.icao = :icao ORDER BY a.datum DESC", AirportDispatchLog.class)
            .setParameter("icao", icao)
            .setMaxResults(250)
            .getResultList();
  }

}
