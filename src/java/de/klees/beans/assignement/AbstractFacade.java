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

package de.klees.beans.assignement;

import de.klees.beans.system.CONF;
import de.klees.data.Airport;
import de.klees.data.Airportjobtemplate;
import de.klees.data.Fluggesellschaft;
import de.klees.data.FlughafenKlassen;
import de.klees.data.Flugrouten;
import de.klees.data.JobBeschreibungen;
import de.klees.data.Story;
import de.klees.data.views.ViewFlugzeugeMietKauf;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import de.klees.data.Assignement;
import de.klees.data.Bank;
import de.klees.data.Bestellungen;
import de.klees.data.CharterAuftrag;
import de.klees.data.FboObjekte;
import de.klees.data.FboUserObjekte;
import de.klees.data.Feinabstimmung;
import de.klees.data.FlugesellschaftPiloten;
import de.klees.data.Fluggesellschaftmanager;
import de.klees.data.Flugzeuge;
import de.klees.data.Flugzeugemietkauf;
import de.klees.data.Flugzeugtransfer;
import de.klees.data.Sitzkonfiguration;
import de.klees.data.Benutzer;
import de.klees.data.UserFavoriten;
import de.klees.data.views.ViewAirportAnflugZiele;
import de.klees.data.views.ViewAirportTransfers;
import de.klees.data.views.ViewAssiErweiterteLizenzPruefung;
import de.klees.data.views.ViewFBOUserObjekte;
import de.klees.data.views.ViewFluggesellschaftPiloten;
import de.klees.data.views.ViewFlughafenAuftragsZiele;
import java.util.Date;
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

  public void createRD(Assignement entity) {
    getEntityManager().persist(entity);
  }

  public void createCharterAuftrag(CharterAuftrag entity) {
    getEntityManager().persist(entity);
  }

  public void edit(T entity) {
    getEntityManager().merge(entity);
  }

  public void remove(T entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public int removeKopie(int id) {
    return getEntityManager().createNativeQuery("DELETE FROM assignement where idassignement = " + id).executeUpdate();
  }

  public T find(Object id) {
    return getEntityManager().find(entityClass, id);
  }

  public Feinabstimmung readConfig() {
    return getEntityManager().createQuery("SELECT c from Feinabstimmung c", Feinabstimmung.class).setMaxResults(1).getSingleResult();
  }

  public Sitzkonfiguration getSitzKonfiguration(int konfid) {
    try {
      return getEntityManager().createQuery("SELECT s FROM Sitzkonfiguration s WHERE s.idsitzKonfiguration = :konfid", Sitzkonfiguration.class)
              .setParameter("konfid", konfid)
              .getSingleResult();

    } catch (NoResultException e) {
      return null;
    }
  }

  /*
  Diese funktionen werden vom AssignmentCreate Modul benötigt
   */
  @SuppressWarnings("unchecked")
  public List<Fluggesellschaft> findAllAirlines() {
    return getEntityManager().createNamedQuery("Fluggesellschaft.findAll").getResultList();
  }

  public Airport findAllbyIcaoSingle(String icao) {

    try {
      return getEntityManager().createQuery("SELECT a FROM Airport a WHERE a.icao =:icao", Airport.class).setParameter("icao", icao).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }

  }

  public boolean hatUserEinenCharterAuftrag(int userid) {
    try {
      Assignement assi = getEntityManager().createQuery("SELECT a FROM Assignement a WHERE a.idowner = :userid and a.type = 3", Assignement.class)
              .setParameter("userid", userid)
              .setMaxResults(1)
              .getSingleResult();
      return true;
    } catch (NoResultException e) {
      return false;
    }
  }

  public CharterAuftrag getCharterSperre(int userid) {
    try {
      return getEntityManager().createQuery("SELECT c FROM CharterAuftrag c WHERE c.userID = :userid", CharterAuftrag.class)
              .setParameter("userid", userid)
              .setMaxResults(1)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public void onCharterSperreEntfernen(int userid) {
    getEntityManager().createQuery("DELETE FROM CharterAuftrag c WHERE c.userID = :userid", CharterAuftrag.class)
            .setParameter("userid", userid)
            .executeUpdate();
  }

  public ViewFlugzeugeMietKauf getRentedAircraft(int userid) {
    try {
      return getEntityManager().createQuery("SELECT f FROM ViewFlugzeugeMietKauf f WHERE f.idUserDerFlugzeugGesperrtHat = :userid", ViewFlugzeugeMietKauf.class)
              .setParameter("userid", userid)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public ViewFlugzeugeMietKauf findbyAircraftID(int FlugzeugID) {
    return getEntityManager().createNamedQuery("ViewFlugzeugeMietKauf.findByIdMietKauf", ViewFlugzeugeMietKauf.class).setParameter("idMietKauf", FlugzeugID).getSingleResult();
  }

  public List<ViewFluggesellschaftPiloten> getFluggesellschaften(int userID) {
    return getEntityManager().createQuery("SELECT a from ViewFluggesellschaftPiloten a WHERE a.idUser =:userid", ViewFluggesellschaftPiloten.class)
            .setParameter("userid", userID)
            .getResultList();
  }

  public List<Assignement> findByFluggesellschaft(int FluggesellschaftID, String vonicao, String nachicao) {
    if (vonicao.equals("")) {
      vonicao = "%";
    }

    if (nachicao.equals("")) {
      nachicao = "%";
    }

    return getEntityManager().createQuery("SELECT a from Assignement a WHERE a.idAirline =:id AND a.idowner=-1 AND a.idjob = -1 AND a.locationIcao LIKE :vonicao and a.toIcao LIKE :nachicao", Assignement.class)
            .setParameter("id", FluggesellschaftID)
            .setParameter("vonicao", vonicao)
            .setParameter("nachicao", nachicao)
            .getResultList();
  }

  public ViewAirportTransfers getTransfer(String icao, Date startDatum, Date endDatum) {

    try {
      return getEntityManager().createQuery("SELECT v from ViewAirportTransfers v where v.datum BETWEEN :startDatum and :endDatum and v.icao = :icao", ViewAirportTransfers.class)
              .setParameter("startDatum", startDatum)
              .setParameter("endDatum", endDatum)
              .setParameter("icao", icao)
              .getSingleResult();

    } catch (Exception e) {
      return null;
    }
  }

  /*
    ************************** Jobs  
   */
  public void createJob(JobBeschreibungen entity) {
    getEntityManager().persist(entity);
  }

  public void editJob(JobBeschreibungen entity) {
    getEntityManager().merge(entity);
  }

  public void removeJob(JobBeschreibungen entity) {
    getEntityManager().remove(getEntityManager().merge(entity));

  }

  public List<JobBeschreibungen> findAllJobs() {
    return getEntityManager().createQuery("SELECT j FROM JobBeschreibungen j", JobBeschreibungen.class).getResultList();

  }

  @SuppressWarnings("unchecked")
  public List<FlughafenKlassen> findAllKlassen() {
    return getEntityManager().createQuery("SELECT f FROM FlughafenKlassen f ORDER BY f.idFlughafenKlassen").getResultList();
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

  public List<JobBeschreibungen> getJobs(String Job) {
    return getEntityManager().createQuery("SELECT j from JobBeschreibungen j where j.aufgabe =:job", JobBeschreibungen.class)
            .setParameter("job", Job)
            .getResultList();
  }

  public List<Airportjobtemplate> getTemplateJobs(String TemplateName) {
    return getEntityManager().createQuery("SELECT j FROM Airportjobtemplate j WHERE j.templatename = :templatename", Airportjobtemplate.class)
            .setParameter("templatename", TemplateName)
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

  public Fluggesellschaft readFg(int fgid) {

    try {
      return getEntityManager().createQuery("SELECT f from Fluggesellschaft f WHERE f.idFluggesellschaft = :fgid", Fluggesellschaft.class)
              .setParameter("fgid", fgid)
              .getSingleResult();
    } catch (Exception e) {
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

  public FlugesellschaftPiloten readPilot(int userid, int fgid) {
    try {
      return getEntityManager().createQuery("SELECT p FROM FlugesellschaftPiloten p WHERE p.idflugesellschaft = :fgid and p.iduser = :userid", FlugesellschaftPiloten.class)
              .setParameter("userid", userid)
              .setParameter("fgid", fgid)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  /*
    ************************** Jobs  ENDE
   */
 /*
    ************************** FBO Objekte
   */
  public boolean existBusinessLounge(String icao) {

    String Abfrage = "SELECT businessLounge FROM fboObjekte AS fboObjekte, fboUserObjekte AS fboUserObjekte "
            + "WHERE fboObjekte.idObjekt = fboUserObjekte.idfboObjekt AND fboUserObjekte.icao = '" + icao + "' AND fboObjekte.businessLounge = TRUE";

    return getEntityManager().createNativeQuery(Abfrage).getResultList().size() > 0;
  }

  public long wievieleFBOAmFlughafen(String icao) {
    return (long) getEntityManager().createQuery("SELECT COUNT (f) from ViewFBOUserObjekte f WHERE f.icao = :icao AND f.fbo = TRUE")
            .setParameter("icao", icao)
            .getSingleResult();
  }

  /*
    ************************** FBO Objekte ENDE
   */
 /*
    ************************** Flughaefen
   */
  public boolean ifExistFlughafen(String icao) {
    if (getEntityManager().createQuery("SELECT f from Airport f WHERE f.icao = :icao", Airport.class)
            .setParameter("icao", icao).getResultList().size() > 0) {
      return true;
    } else {
      return false;
    }
  }

  public Airport getAirportData(String icao) {
    try {
      return getEntityManager().createQuery("SELECT a FROM Airport a where a.icao =:icao", Airport.class)
              .setParameter("icao", icao)
              .getSingleResult();

    } catch (NoResultException e) {
      return null;
    }
  }

  public List<Airport> findAllbyIcaoNach(String NachIcao) {
    return getEntityManager().createQuery("SELECT a FROM Airport a WHERE a.icao = :icao", Airport.class)
            .setParameter("icao", NachIcao)
            .getResultList();
  }

  public List<Flugrouten> findAllFlugroutenByIDFluggesellschaft(int idFluggesellschaft) {
    return getEntityManager().createQuery("SELECT f FROM Flugrouten f WHERE f.idFluggesellschaft = :fgid", Flugrouten.class)
            .setParameter("fgid", idFluggesellschaft)
            .getResultList();
  }

//  public void NativeQuerry(String Abrfrage) {
//    getEntityManager().createNativeQuery(Abrfrage).executeUpdate();
//  }
  public int GetMengeAmFlughafen(String Abfrage) {
    try {
      BigDecimal bd = (BigDecimal) getEntityManager().createNativeQuery(Abfrage).getSingleResult();
      return bd.intValue();

    } catch (Exception e) {
      return 0;
    }
  }

  @SuppressWarnings("unchecked")
  public List<Airport> findAllAirportsByLongLat(double vonlatitute, double bislatitute, double vonlongitude, double bislongitude, String icao) {

    return getEntityManager().
            createNativeQuery("SELECT * FROM Airport where (latitude between "
                    + vonlatitute + " and " + bislatitute
                    + " and " + "longitude between "
                    + vonlongitude + " and " + bislongitude + " )", Airport.class).getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<ViewAirportAnflugZiele> holeAnflugziele(int AirportID) {
    return getEntityManager().createQuery("SELECT a FROM ViewAirportAnflugZiele a WHERE a.quellidAirport =:idairport ORDER BY a.klasse DESC")
            .setParameter("idairport", AirportID)
            .getResultList();
  }


  /*
    ************************** Flughaefen
   */
 /*
  ***************************************************************************************************
   */
 /*
    ************************** Storys BEGINN
   */
  public List<Story> StorysForAircraft(String Sprache, String Type, String Lizenz) {

    String[] OrderBy = new String[]{"ORDER BY s.bezeichnung DESC", "ORDER BY s.bezeichnung ASC", "ORDER BY s.idStory ASC", "ORDER BY s.idStory DESC", "ORDER BY s.verfasser ASC", "ORDER BY s.verfasser DESC"};

    return getEntityManager().createQuery("SELECT s FROM Story s WHERE s.flugzeugKlasse = :type AND s.flugzeuglizenz = :lizenz  AND s.sprache = :sprache AND s.aktiv = TRUE " + OrderBy[CONF.zufallszahl(0, 5)], Story.class)
            .setParameter("sprache", Sprache)
            .setParameter("type", Type)
            .setParameter("lizenz", Lizenz)
            .getResultList();
  }

  public List<Story> StorysForAircraftTest(String Bezeichnung) {

    return getEntityManager().createQuery("SELECT s FROM Story s WHERE s.bezeichnung =:bezeichnung ORDER BY s.flugzeuglizenz", Story.class)
            .setParameter("bezeichnung", Bezeichnung)
            .getResultList();
  }

  public List<Story> findAllStorys(String Sprache) {
    return getEntityManager().createQuery("SELECT s FROM Story s WHERE NOT (s.flugzeugKlasse = 'Geschäftsflugzeug') AND s.sprache = :sprache AND s.aktiv = TRUE", Story.class)
            .setParameter("sprache", Sprache)
            .getResultList();
  }

  public List<Story> findAllStorysBC(String Sprache) {
    return getEntityManager().createQuery("SELECT s from Story s WHERE s.flugzeugKlasse = 'Geschäftsflugzeug' AND s.sprache = :sprache AND s.aktiv = TRUE", Story.class)
            .setParameter("sprache", Sprache)
            .getResultList();
  }

  public List<Story> findAllStorysLandBundesland(String land, String bundesland) {
    return getEntityManager().createQuery("SELECT s FROM Story s WHERE s.bundesland = :bundesland and s.land = :land", Story.class)
            .setParameter("bundesland", bundesland)
            .setParameter("land", land)
            .getResultList();
  }

  public List<Story> findAllStorysLand(String land) {
    return getEntityManager().createQuery("SELECT s FROM Story s WHERE s.land = :land", Story.class)
            .setParameter("land", land)
            .getResultList();
  }

  /*
    ************************** Storys ENDE
   */
  @SuppressWarnings("unchecked")
  public List<T> findAll() {
    javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
    cq.select(cq.from(entityClass));
    return getEntityManager().createQuery(cq).getResultList();
  }

  public List<Assignement> findByICAOLocation(String Suchtext, int OwnerID) {
    return getEntityManager().createQuery("SELECT a FROM Assignement a WHERE a.oeffentlich = 1 AND a.locationIcao = :location AND a.idjob = -1 AND a.idowner = :owner", Assignement.class)
            .setParameter("location", Suchtext)
            .setParameter("owner", OwnerID)
            .getResultList();
  }

  public List<Assignement> findNachIcao(String Suchtext, int OwnerID) {
    return getEntityManager().createQuery("SELECT a FROM Assignement a where a.oeffentlich = 1 AND a.toIcao = :icao AND a.idjob = -1", Assignement.class)
            .setParameter("icao", Suchtext)
            .getResultList();
  }

  public List<Assignement> findByICAOLocationAndtoIcao(String Suchtext, String Icao, int OwnerID) {
    return getEntityManager().createQuery("SELECT a FROM Assignement a WHERE a.oeffentlich = 1 AND a.locationIcao = :location AND a.idowner =:owner AND a.idjob = -1 AND a.toIcao like :icao ", Assignement.class)
            .setParameter("location", Suchtext)
            .setParameter("owner", OwnerID)
            .setParameter("icao", Icao)
            .getResultList();
  }

  public List<Assignement> findByICAOLocationAndFTWJob(String Suchtext, String Icao, int OwnerID) {
    return getEntityManager().createQuery("SELECT a FROM Assignement a WHERE a.locationIcao = :location and a.createdbyuser = 'FTW-System' AND a.idjob = -1 ", Assignement.class)
            .setParameter("location", Suchtext)
            .getResultList();
  }

  public List<UserFavoriten> getFavoriten(int userid) {
    return getEntityManager().createQuery("SELECT f FROM UserFavoriten f where f.iduser = :userid ORDER BY f.icao", UserFavoriten.class)
            .setParameter("userid", userid)
            .getResultList();
  }

  public boolean ifExistFavorit(int userid, String icao) {
    try {
      getEntityManager().createQuery("SELECT f FROM UserFavoriten f WHERE f.icao = :icao AND f.iduser = :userid", UserFavoriten.class)
              .setParameter("userid", userid)
              .setParameter("icao", icao)
              .getSingleResult();
      return true;
    } catch (NoResultException e) {
      return false;
    }
  }

  public void createFavorit(UserFavoriten entity) {
    getEntityManager().persist(entity);
  }

  @SuppressWarnings("unchecked")
  public List<T> findByMyFlight(int OwnerID) {
    return getEntityManager().createNamedQuery("Assignement.findByIdownerReadyForTakeoff", entityClass)
            .setParameter("idowner", OwnerID)
            .setParameter("active", 0)
            .getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<T> findByMyFlightHold(int OwnerID) {
    return getEntityManager().createNamedQuery("Assignement.findByIdownerAndHold", entityClass)
            .setParameter("idowner", OwnerID)
            .setParameter("active", 1)
            .getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<String> DistinctByICAOLocation(String Suchtext, int OwnerID) {
    return getEntityManager().createQuery("SELECT DISTINCT a.toIcao, a.toName, a.toAirportLandCity FROM Assignement a where a.locationIcao =:icao  order by a.toIcao")
            .setParameter("icao", Suchtext)
            .getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<String> DistinctByFromICAO(String Suchtext, int OwnerID) {
    return getEntityManager().createQuery("SELECT DISTINCT a.fromIcao, a.fromName, a.fromAirportLandCity FROM Assignement a where a.fromIcao =:icao  order by a.fromIcao")
            .setParameter("icao", Suchtext)
            .getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<String> DistinctByToICAO(String Suchtext, int OwnerID) {
    return getEntityManager().createQuery("SELECT DISTINCT a.fromIcao, a.fromName, a.fromAirportLandCity FROM Assignement a where a.toIcao =:icao and a.idjob = -1  order by a.toIcao")
            .setParameter("icao", Suchtext)
            .getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<String> DistinctByToICAOFGSelect(String Suchtext, int fgID) {

    return getEntityManager().createQuery("SELECT DISTINCT a.toIcao, a.toName, a.toAirportLandCity FROM Assignement a WHERE a.locationIcao =:icao AND a.idjob = -1 AND a.idAirline =:fgid order by a.toIcao")
            .setParameter("icao", Suchtext)
            .setParameter("fgid", fgID)
            .getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<String> DistinctByICAOLocationAllAssignment() {
    return getEntityManager().createQuery("SELECT DISTINCT a.fromIcao, a.fromName, a.fromAirportLandCity FROM Assignement a order by a.fromIcao")
            .getResultList();
  }

  public boolean hatUserSchonEinenAgentJob(int id) {
    try {
      getEntityManager().createQuery("SELECT a FROM Assignement a WHERE a.idowner  = :id AND a.type = 5", Assignement.class)
              .setParameter("id", id)
              .setMaxResults(1)
              .getSingleResult();
    } catch (NoResultException e) {
      return false;
    }
    return true;
  }

  
  public List<ViewAssiErweiterteLizenzPruefung> getUserAuftraegeFuerSupport(int ID){
    return getEntityManager().createQuery("SELECT v from ViewAssiErweiterteLizenzPruefung v WHERE v.idowner = :owner",ViewAssiErweiterteLizenzPruefung.class)
            .setParameter("owner", ID)
            .getResultList();
  }
  
  /*
    ************************** Daten Feinabstimmung Beginn
   */
  public Feinabstimmung getDaten() {
    return getEntityManager().createQuery("SELECT f from Feinabstimmung f", Feinabstimmung.class).setMaxResults(1).getSingleResult();
  }

  /*
    ************************** Daten Feinabstimmung Ende
   */
 /*
    ************************** Bestellungen
   */
  public void verbuchen(FboUserObjekte entity) {
    getEntityManager().merge(entity);
  }

  public List<Bestellungen> getBestellungen(Date datum) {
    return getEntityManager().createQuery("SELECT b FROM Bestellungen b where b.ausfuehrungsdatum <= :datum", Bestellungen.class)
            .setParameter("datum", datum)
            .getResultList();
  }

  public void loescheBestellposition(int bestellID) {
    getEntityManager().createNativeQuery("delete from Bestellungen where idBestellungen = " + bestellID).executeUpdate();
  }

  public FboUserObjekte getFboUserOjekt(int objektID) {
    return getEntityManager().createQuery("SELECT o from FboUserObjekte o WHERE o.iduserfbo = :objektID", FboUserObjekte.class)
            .setParameter("objektID", objektID)
            .getSingleResult();
  }

  public ViewFBOUserObjekte getViewFboUserOjekt(int objektID) {
    return getEntityManager().createQuery("SELECT o from ViewFBOUserObjekte o WHERE o.iduserfbo = :objektID", ViewFBOUserObjekte.class)
            .setParameter("objektID", objektID)
            .getSingleResult();
  }

  public List<ViewFBOUserObjekte> getFaelligeMietvertraege(Date Datum) {
    return getEntityManager().createQuery("SELECT m FROM ViewFBOUserObjekte m WHERE m.faelligkeitNaechsteMiete <= :datum", ViewFBOUserObjekte.class).
            setParameter("datum", Datum)
            .getResultList();
  }

  public FboObjekte getFboOjekt(int objektID) {
    return getEntityManager().createQuery("SELECT o from FboObjekte o WHERE o.idObjekt = :objektID", FboObjekte.class)
            .setParameter("objektID", objektID)
            .getSingleResult();
  }

  public FboUserObjekte getUserObjekt(int objektid) {
    return getEntityManager().createQuery("SELECT o FROM FboUserObjekte o WHERE o.iduserfbo = :objektid", FboUserObjekte.class)
            .setParameter("objektid", objektid)
            .getSingleResult();
  }

 
  public void createBankbuchung(Bank entity) {
    getEntityManager().persist(entity);
  }

  public void onMietZahlungBuchen(FboUserObjekte entity) {
    getEntityManager().merge(entity);
  }

  public double BankSaldo(int UserID) {
    String Abfrage = "SELECT SUM( betrag ) FROM bank AS bank WHERE userID = " + String.valueOf(UserID);
    return (double) getEntityManager().createNativeQuery(Abfrage).getSingleResult();
  }

  public double BankSaldoUeberBankKonto(String KontoNummer) {
    String Abfrage = "SELECT SUM( betrag ) FROM bank AS bank WHERE bankKonto = '" + KontoNummer + "'";
    return (double) getEntityManager().createNativeQuery(Abfrage).getSingleResult();
  }

  public String getBankkontoName(String Kontonummer) {
    return getEntityManager().createQuery("SELECT b FROM Bank b WHERE b.bankKonto = :kontonummer", Bank.class)
            .setParameter("kontonummer", Kontonummer)
            .setMaxResults(1)
            .getSingleResult().getKontoName();
  }

  /*
    ************************** Bestellungen ENDE
   */
 /*
    ************************** Flugzeuge Mietzeiten
   */
  public List<Flugzeugemietkauf> getFlugzeugeMietZeitAblauf(Date DatumZeit) {
    // f.istInDerLuft=false am 02.05.2017 hinzugefügt

    return getEntityManager().createQuery("SELECT f FROM Flugzeugemietkauf f WHERE f.istGesperrt=TRUE and f.istInDerLuft=false and f.istGesperrtBis < :zeit", Flugzeugemietkauf.class)
            .setParameter("zeit", DatumZeit)
            .getResultList();
  }

  public Flugzeugemietkauf getFlugzeug(int id) {
    return getEntityManager().createQuery("SELECT f from Flugzeugemietkauf f WHERE f.idMietKauf = :id", Flugzeugemietkauf.class)
            .setParameter("id", id)
            .getSingleResult();
  }

  public Flugzeuge readStammflugzeug(int id) {
    return getEntityManager().createQuery("SELECT f FROM Flugzeuge f WHERE f.idFlugzeug = :id", Flugzeuge.class)
            .setParameter("id", id)
            .getSingleResult();
  }

  public List<Flugzeugtransfer> getFlugzeugTransferListe(Date DatumZeit) {
    return getEntityManager().createQuery("SELECT t from Flugzeugtransfer t where t.enddatum < :zeit", Flugzeugtransfer.class)
            .setParameter("zeit", DatumZeit)
            .getResultList();
  }

  public void onFlugzeugMietKaufSpeichern(Flugzeugemietkauf entity) {
    getEntityManager().merge(entity);
  }

  public void onTransferLoeschen(Flugzeugtransfer entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  /*
    ************************** Flugzeuge Mietzeiten ENDE
   */

 /*
  **************************** Fluggesellschaften Umwandeln von Assignment
   */
  public boolean hatUserEineFluggesellschaft(int userid) {
    if (getEntityManager().createQuery("SELECT f FROM Fluggesellschaft f WHERE f.userid = :userid")
            .setParameter("userid", userid)
            .getResultList().size() > 0) {
      return true;
    }
    return false;
  }

  public List<Fluggesellschaft> getFGs(int userID) {
    return getEntityManager().createQuery("SELECT f from Fluggesellschaft f WHERE f.userid = :userid", Fluggesellschaft.class)
            .setParameter("userid", userID)
            .getResultList();
  }

  public List<Fluggesellschaft> getFGsFuerManager(int userID, int fgid) {
    return getEntityManager().createQuery("SELECT f from Fluggesellschaft f WHERE f.userid = :userid OR f.idFluggesellschaft = :fgid", Fluggesellschaft.class)
            .setParameter("userid", userID)
            .setParameter("fgid", fgid)
            .getResultList();
  }

  // Change Auftrag zur Fluggesellschaft
  public void saveAuftrag(Assignement entity) {
    getEntityManager().merge(entity);
  }

  public void savaFluggesellschaft(Fluggesellschaft entity) {
    getEntityManager().merge(entity);
  }

  /*
  **************************** Fluggesellschaften ENDE
   */

 /*
  *************************** Map view BEGINN
   */
  public List<ViewFlughafenAuftragsZiele> getFlughafenAuftragsZiele(String icao) {
    return getEntityManager().createQuery("SELECT f FROM ViewFlughafenAuftragsZiele f WHERE f.locationIcao = :icao", ViewFlughafenAuftragsZiele.class)
            .setParameter("icao", icao)
            .getResultList();
  }

  public Airport getAirportByIcao(String icao) {
    try {

      return getEntityManager().createQuery("SELECT a FROM Airport a WHERE a.icao = :icao", Airport.class)
              .setParameter("icao", icao)
              .getSingleResult();

    } catch (NoResultException e) {
      //System.out.println("de.klees.beans.takeoff.ControllerTakeoff.getAirportByIcao() ICAO : " + icao);
    }

    return null;
  }

  @SuppressWarnings("Unchecked")
  public List getRoutenMengen(String locationIcao, String toIcao, int fgID) {

    String Abfrage = "";

    if (fgID > 0) {
      Abfrage = "SELECT  "
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
              + "            AND to_icao = '" + toIcao + "' AND idowner <= 0 and idAirline = " + fgID
              + "    GROUP BY routenArt) AS tmp1";

    } else {
      Abfrage = "SELECT  "
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

    }

    return getEntityManager().createNativeQuery(Abfrage).getResultList();
  }

  public List<Assignement> findByMyFlightMap(int OwnerID) {
    return getEntityManager().createQuery("SELECT a FROM Assignement a WHERE a.idowner = :idowner AND a.active = :active group by a.toIcao ORDER BY a.toIcao", Assignement.class)
            .setParameter("idowner", OwnerID)
            .setParameter("active", 0)
            .getResultList();
  }

  public List<Assignement> findByICAOLocationAndtoIcaoMap(String Suchtext, String Icao, int OwnerID) {
    return getEntityManager().createQuery("SELECT a FROM Assignement a WHERE a.oeffentlich = 1 AND a.locationIcao = :location AND a.idowner =:owner AND a.idjob = -1 AND a.toIcao like :icao group by a.toIcao ", Assignement.class)
            .setParameter("location", Suchtext)
            .setParameter("owner", OwnerID)
            .setParameter("icao", Icao)
            .getResultList();
  }

  public List<Assignement> findByICAOLocationMap(String Suchtext, int OwnerID) {
    return getEntityManager().createQuery("SELECT a FROM Assignement a WHERE a.oeffentlich = 1 AND a.locationIcao = :location AND a.idowner = :owner AND a.idjob = -1 group by a.toIcao", Assignement.class)
            .setParameter("location", Suchtext)
            .setParameter("owner", OwnerID)
            .getResultList();
  }

  public List<Assignement> findByFluggesellschaftMap(int FluggesellschaftID, String vonicao, String nachicao) {
    if (vonicao.equals("")) {
      vonicao = "%";
    }

    if (nachicao.equals("")) {
      nachicao = "%";
    }

    return getEntityManager().createQuery("SELECT a from Assignement a WHERE a.idAirline =:id AND a.idowner=-1 AND a.locationIcao LIKE :vonicao and a.toIcao LIKE :nachicao group by a.toIcao", Assignement.class)
            .setParameter("id", FluggesellschaftID)
            .setParameter("vonicao", vonicao)
            .setParameter("nachicao", nachicao)
            .getResultList();
  }


  /*
  *************************** Map view Ende
   */
}
