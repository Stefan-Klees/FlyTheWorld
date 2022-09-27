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


package de.klees.beans.system;

import de.klees.data.Airport;
import de.klees.data.Assignement;
import de.klees.data.Feinabstimmung;
import de.klees.data.FlugesellschaftPiloten;
import de.klees.data.Fluggesellschaft;
import de.klees.data.FluggesellschaftBonusSystem;
import de.klees.data.Fluggesellschaftmanager;
import de.klees.data.FlughafenKlassen;
import de.klees.data.Flugrouten;
import de.klees.data.Flugzeuge;
import de.klees.data.Kostenstellen;
import de.klees.data.Mail;
import de.klees.data.Modsql;
import de.klees.data.Systemmeldung;
import de.klees.data.Benutzer;
import de.klees.data.WartungsTabelle;
import de.klees.data.Yaacarskopf;
import de.klees.data.Yaacarspositionen;
import de.klees.data.views.ViewAirportAnflugZiele;
import de.klees.data.views.ViewFBOUserObjekte;
import de.klees.data.views.ViewFluggesellschaftInfoFlugzeuge;
import de.klees.data.views.ViewFluggesellschaftManager;
import de.klees.data.views.ViewFluggesellschaftUmsatzPiloten;
import de.klees.data.views.ViewFlughaefenNachStartsLandungenMitPaxCargo;
import de.klees.data.views.ViewFlugzeugeMietKauf;
import de.klees.data.views.ViewFlugzeuglog;
import de.klees.data.views.ViewKostenstellenAuswertung;
import de.klees.data.views.ViewLetzteFluege;
import de.klees.data.views.ViewLogbuchLast20Day;
import de.klees.data.views.ViewMeineFluggesellschaften;
import de.klees.data.views.ViewStatFluggesellschaftVermoegen;
import de.klees.data.views.ViewStatUserFirmenvermoegen;
import de.klees.data.views.ViewStatUsersFlugzeuge;
import de.klees.data.views.ViewUserFluglogBuch;
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

  public void createWartungsMeldung(WartungsTabelle entity) {
    getEntityManager().persist(entity);
  }

  public void edit(T entity) {
    getEntityManager().merge(entity);
  }

  public void remove(T entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public Feinabstimmung readConfig() {
    return getEntityManager().createQuery("SELECT c FROM Feinabstimmung c", Feinabstimmung.class).setMaxResults(1).getSingleResult();
  }

  public Fluggesellschaft readAirline(int fgID) {
    try {
      return getEntityManager().createQuery("SELECT a FROM Fluggesellschaft a where a.idFluggesellschaft = :fgid", Fluggesellschaft.class)
              .setParameter("fgid", fgID)
              .getSingleResult();

    } catch (NoResultException e) {
      return null;
    }
  }

  public Systemmeldung getSystemMeldung() {
    return getEntityManager().createQuery("SELECT s FROM Systemmeldung s", Systemmeldung.class).setMaxResults(1).getSingleResult();
  }

  public void feinabstimmungEdit(Feinabstimmung entity) {
    getEntityManager().merge(entity);
  }

  public void UpdateModusEdit(Systemmeldung entity) {
    getEntityManager().merge(entity);
  }

  public Feinabstimmung getDaten() {
    return getEntityManager().createQuery("SELECT f from Feinabstimmung f", Feinabstimmung.class).setMaxResults(1).getSingleResult();
  }

  public Systemmeldung getUpdateStatus() {
    return getEntityManager().createQuery("SELECT m from Systemmeldung m", Systemmeldung.class).setMaxResults(1).getSingleResult();
  }

  public List<Airport> getAirports(String query) {
    return getEntityManager().createQuery("SELECT a FROM Airport a WHERE a.icao like :query ORDER BY a.name", Airport.class)
            .setParameter("query", query)
            .setMaxResults(200).getResultList();
  }

  public Benutzer findUserByName(String UserName) {
    return getEntityManager().createQuery("SELECT u FROM Benutzer u WHERE u.name1 =:name", Benutzer.class)
            .setParameter("name", UserName)
            .getSingleResult();
  }

  public Benutzer findUserByID(int UserID) {
    return getEntityManager().createQuery("SELECT u FROM Benutzer u WHERE u.idUser = :userid", Benutzer.class)
            .setParameter("userid", UserID)
            .getSingleResult();
  }

  public String getUserName(int UserID) {
    try {
      return getEntityManager().createQuery("SELECT u FROM Benutzer u WHERE u.idUser =:userid", Benutzer.class)
              .setParameter("userid", UserID)
              .getSingleResult().getName1();

    } catch (Exception e) {
      return "not found";
    }
  }

  public Fluggesellschaftmanager readManagerDaten(int userid, int fgid) {
    try {
      return getEntityManager().createQuery("Select m FROM Fluggesellschaftmanager m where m.userid = :userid and m.fluggesellschaftid = :fgid ", Fluggesellschaftmanager.class)
              .setParameter("userid", userid)
              .setParameter("fgid", fgid)
              .setMaxResults(1)
              .getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  //************************* Statistik Beginn
  public List<Fluggesellschaft> getFluggesellschaften(int userid) {
    return getEntityManager().createQuery("SELECT f from Fluggesellschaft f WHERE f.userid = :userid", Fluggesellschaft.class)
            .setParameter("userid", userid)
            .getResultList();
  }

  public List<Fluggesellschaft> getFluggesellschaftenKostenstellenAuswertung(int userid) {
    return getEntityManager().createQuery("SELECT f from Fluggesellschaft f WHERE f.userid = :userid", Fluggesellschaft.class)
            .setParameter("userid", userid)
            .getResultList();
  }

  public Fluggesellschaft getFluggesellschaft(int fgid) {
    try {
      return getEntityManager().createQuery("SELECT f from Fluggesellschaft f WHERE f.idFluggesellschaft = :fgid", Fluggesellschaft.class)
              .setParameter("fgid", fgid)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public Fluggesellschaft getFluggesellschaftUeberBankkonto(String Konto) {
    try {
      return getEntityManager().createQuery("SELECT f from Fluggesellschaft f WHERE f.bankKonto = :konto", Fluggesellschaft.class)
              .setParameter("konto", Konto)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public List<FluggesellschaftBonusSystem> getBoniList(int fgid) {
    return getEntityManager().createQuery("SELECT b FROM FluggesellschaftBonusSystem b WHERE b.idfluggesellschaft = :fgid ORDER BY b.zeit", FluggesellschaftBonusSystem.class)
            .setParameter("fgid", fgid)
            .getResultList();
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

  public List getUserFluegeTop20(String vonDatum, String bisDatum) {
    String Abfrage = "SELECT "
            + "    `benutzer`.`name1` AS `Pilot`,"
            + "    COUNT(`fluglogbuch`.`iduser`) AS `Anzahl`,"
            + "    `fluglogbuch`.`flugDatum` AS `Datum`,"
            + "    `benutzer`.`idUser`"
            + "FROM"
            + "    `benutzer` AS `benutzer`,"
            + "    `fluglogbuch` AS `fluglogbuch`"
            + "WHERE "
            + "    `benutzer`.`idUser` = `fluglogbuch`.`iduser`"
            + "        AND `fluglogbuch`.`flugDatum` BETWEEN { d '" + vonDatum + "' } AND { d '" + bisDatum + "' }"
            + "        AND `benutzer`.`gesperrt` = FALSE "
            + "GROUP BY `benutzer`.`name1` "
            + "ORDER BY `Anzahl` DESC";

    return getEntityManager().createNativeQuery(Abfrage).setMaxResults(20).getResultList();
  }

  public List getUserFluegeNachZeitTop20(String vonDatum, String bisDatum) {
    String Abfrage = "SELECT  "
            + "    `benutzer`.`name1` AS `Pilot`, "
            + "    `fluglogbuch`.`flugDatum` AS `Datum`, "
            + "    `benutzer`.`idUser`, "
            + "    SUM(`fluglogbuch`.`flugzeitMinuten`) "
            + "FROM "
            + "    `benutzer` AS `benutzer`, "
            + "    `fluglogbuch` AS `fluglogbuch` "
            + "WHERE "
            + "    `benutzer`.`idUser` = `fluglogbuch`.`iduser` "
            + "        AND `fluglogbuch`.`flugDatum` BETWEEN { d '" + vonDatum + "' } AND { d '" + bisDatum + "' } "
            + "        AND `benutzer`.`gesperrt` = FALSE "
            + "GROUP BY `benutzer`.`name1` "
            + "ORDER BY SUM(`fluglogbuch`.`flugzeitMinuten`) DESC";

    return getEntityManager().createNativeQuery(Abfrage).setMaxResults(20).getResultList();

  }

  public List getFluggesellschaftenNachPaxTop100(String vonDatum, String bisDatum) {

    String Abfrage = "SELECT  "
            + "    fluglogbuch.idAirline, "
            + "    Fluggesellschaft.name, "
            + "    SUM(fluglogbuch.pax) AS pax, "
            + "    SUM(fluglogbuch.cargo) AS cargo "
            + "FROM "
            + "    Fluggesellschaft AS Fluggesellschaft, "
            + "    fluglogbuch AS fluglogbuch "
            + "WHERE "
            + "    Fluggesellschaft.idFluggesellschaft = fluglogbuch.idAirline "
            + "        AND fluglogbuch.flugDatum BETWEEN { d '" + vonDatum + "' } AND { d '" + bisDatum + "' } "
            + "GROUP BY fluglogbuch.idAirline "
            + "HAVING ((fluglogbuch.idAirline > 0)) "
            + "ORDER BY pax DESC limit 100";

    return getEntityManager().createNativeQuery(Abfrage).getResultList();

  }

  public List getFluggesellschaftenNachCargoTop100(String vonDatum, String bisDatum) {

    String Abfrage = "SELECT  "
            + "    fluglogbuch.idAirline, "
            + "    Fluggesellschaft.name, "
            + "    SUM(fluglogbuch.pax) AS pax, "
            + "    SUM(fluglogbuch.cargo) AS cargo "
            + "FROM "
            + "    Fluggesellschaft AS Fluggesellschaft, "
            + "    fluglogbuch AS fluglogbuch "
            + "WHERE "
            + "    Fluggesellschaft.idFluggesellschaft = fluglogbuch.idAirline "
            + "        AND fluglogbuch.flugDatum BETWEEN { d '" + vonDatum + "' } AND { d '" + bisDatum + "' } "
            + "GROUP BY fluglogbuch.idAirline "
            + "HAVING ( (fluglogbuch.idAirline > 0 and cargo > 0)) "
            + "ORDER BY cargo DESC";

    return getEntityManager().createNativeQuery(Abfrage).setMaxResults(100).getResultList();

  }

  public List<Fluggesellschaft> getAllFluggesellschaften() {
    return getEntityManager().createQuery("SELECT f FROM Fluggesellschaft f WHERE f.besitzerName <> '**** FTW Airlines ****' ORDER BY f.name", Fluggesellschaft.class).getResultList();
  }

  public List<ViewFlugzeugeMietKauf> getFluggesellschaftFlugzeuge(int idFG, int ownerID) {
    return getEntityManager().createQuery("SELECT f from ViewFlugzeugeMietKauf f where f.fluggesellschaftID = :idfg  ORDER BY f.type", ViewFlugzeugeMietKauf.class)
            .setParameter("idfg", idFG)
            .getResultList();
  }

  public List<ViewFlugzeugeMietKauf> getFluggesellschaftFlugzeugeDetails(int idFG) {
    return getEntityManager().createQuery("SELECT f FROM ViewFlugzeugeMietKauf f where f.fluggesellschaftID = :idfg ORDER BY f.aktuellePositionICAO, f.type", ViewFlugzeugeMietKauf.class)
            .setParameter("idfg", idFG)
            .getResultList();
  }

//  public List<ViewFluggesellschaftPiloten> getFluggesellschaftPiloten(int idFG) {
//    return getEntityManager().createQuery("SELECT p from ViewFluggesellschaftPiloten p WHERE p.idFluggesellschaft = :idfg", ViewFluggesellschaftPiloten.class)
//            .setParameter("idfg", idFG)
//            .getResultList();
//  }
  public List<FlugesellschaftPiloten> getFluggesellschaftPiloten(int idFG) {
    return getEntityManager().createQuery("SELECT p from FlugesellschaftPiloten p WHERE p.idflugesellschaft = :idfg ORDER BY p.zeit DESC", FlugesellschaftPiloten.class)
            .setParameter("idfg", idFG)
            .getResultList();
  }

  public List<ViewFluggesellschaftManager> getFluggesellschaftManager(int idFG) {
    return getEntityManager().createQuery("SELECT m from ViewFluggesellschaftManager m WHERE m.fluggesellschaftid = :idfg", ViewFluggesellschaftManager.class)
            .setParameter("idfg", idFG)
            .getResultList();
  }

  public List<Flugrouten> getFluggesellschaftRouten(int idFG) {
    return getEntityManager().createQuery("SELECT r from Flugrouten r WHERE r.idFluggesellschaft = :idfg ORDER BY r.name, r.vonIcao", Flugrouten.class)
            .setParameter("idfg", idFG)
            .getResultList();
  }

  public List<Flugrouten> getFlugroutenGroupBy(int fgid) {
    return getEntityManager().createQuery("SELECT r FROM Flugrouten r WHERE r.idFluggesellschaft = :fgid GROUP BY r.vonIcao , r.nachicao", Flugrouten.class)
            .setParameter("fgid", fgid)
            .getResultList();
  }

  public Airport getAirportByIcao(String icao) {
    try {
      return getEntityManager().createQuery("SELECT a FROM Airport a WHERE a.icao = :icao", Airport.class)
              .setParameter("icao", icao)
              .getSingleResult();

    } catch (NoResultException e) {
      return null;
    }
  }

  public List<ViewStatFluggesellschaftVermoegen> getFluggesellschaftVermoegen() {
    return getEntityManager().createQuery("SELECT v from ViewStatFluggesellschaftVermoegen v where v.userid > 0", ViewStatFluggesellschaftVermoegen.class)
            .setMaxResults(20)
            .getResultList();
  }

  public List<ViewStatUsersFlugzeuge> getUsersFlugzeuge() {
    return getEntityManager().createQuery("SELECT v from ViewStatUsersFlugzeuge v where v.idUser > 0", ViewStatUsersFlugzeuge.class)
            .setMaxResults(20)
            .getResultList();
  }

  public List<ViewStatUserFirmenvermoegen> getUsersFirmenVermoegen() {
    return getEntityManager().createQuery("SELECT v from ViewStatUserFirmenvermoegen v where v.idUser > 0", ViewStatUserFirmenvermoegen.class)
            .setMaxResults(20)
            .getResultList();
  }

  public List<Benutzer> getBestNachMeilen() {
    return getEntityManager().createQuery("SELECT u FROM Benutzer u where u.idUser > 0 AND u.gesperrt = FALSE ORDER BY u.flightmiles DESC ", Benutzer.class)
            .setMaxResults(20)
            .getResultList();
  }

  public List<Benutzer> getBestNachPax() {
    return getEntityManager().createQuery("SELECT u FROM Benutzer u where u.idUser > 0 AND u.gesperrt = FALSE ORDER BY u.flightpaxes DESC ", Benutzer.class)
            .setMaxResults(20)
            .getResultList();
  }

  public List<Benutzer> getBestNachCargo() {
    return getEntityManager().createQuery("SELECT u FROM Benutzer u where u.idUser > 0 AND u.gesperrt = FALSE ORDER BY u.flightcargo DESC ", Benutzer.class)
            .setMaxResults(20)
            .getResultList();
  }

  public List<Benutzer> getBestNachZeit() {
    return getEntityManager().createQuery("SELECT u FROM Benutzer u where u.idUser > 0 AND u.gesperrt = FALSE ORDER BY u.flighttime DESC ", Benutzer.class)
            .setMaxResults(20)
            .getResultList();
  }

  public List<Benutzer> getBestNachFluegen() {
    return getEntityManager().createQuery("SELECT u FROM Benutzer u where u.idUser > 0 AND u.gesperrt = FALSE ORDER BY u.flights DESC ", Benutzer.class)
            .setMaxResults(20)
            .getResultList();
  }

  public List<ViewLetzteFluege> getLetzteFluege() {
    return getEntityManager().createQuery("SELECT l from ViewLetzteFluege l ORDER BY l.flugDatum DESC ", ViewLetzteFluege.class).setMaxResults(100).getResultList();
  }

  public List<Yaacarskopf> getAktuelleYAACARSFluege() {
    return getEntityManager().createQuery("SELECT y FROM Yaacarskopf y ORDER BY y.flugerstelltam DESC", Yaacarskopf.class).setMaxResults(100).getResultList();
  }

  public List<ViewFlughaefenNachStartsLandungenMitPaxCargo> getFlughaefenNachStartsLandungen() {
    return getEntityManager().createQuery("SELECT f FROM ViewFlughaefenNachStartsLandungenMitPaxCargo f", ViewFlughaefenNachStartsLandungenMitPaxCargo.class)
            .setMaxResults(100)
            .getResultList();
  }

  public List<ViewFlughaefenNachStartsLandungenMitPaxCargo> getFlughaefenNachStartsLandungenNachPax() {
    return getEntityManager().createQuery("SELECT f FROM ViewFlughaefenNachStartsLandungenMitPaxCargo f ORDER BY f.pax DESC", ViewFlughaefenNachStartsLandungenMitPaxCargo.class)
            .setMaxResults(100)
            .getResultList();
  }

  public List<ViewLogbuchLast20Day> getLast20Logbuch(Date vonDatum, Date bisDatum) {
    return getEntityManager().createQuery("SELECT l from ViewLogbuchLast20Day l WHERE l.datum BETWEEN :vonDatum and :bisDatum", ViewLogbuchLast20Day.class)
            .setParameter("vonDatum", vonDatum)
            .setParameter("bisDatum", bisDatum)
            .getResultList();
  }

  public List<ViewUserFluglogBuch> getUserFlugLogBuch(int userid) {
    return getEntityManager().createQuery("SELECT l from ViewUserFluglogBuch l WHERE l.iduser = :userid", ViewUserFluglogBuch.class)
            .setParameter("userid", userid)
            .getResultList();
  }

  public List<ViewFlugzeuglog> getFlugzeugLog(int iduser) {
    return getEntityManager().createQuery("SELECT f FROM ViewFlugzeuglog f WHERE f.iduser = :iduser ORDER BY f.flugDatum DESC", ViewFlugzeuglog.class)
            .setParameter("iduser", iduser)
            .getResultList();
  }

  @SuppressWarnings("unchecked")
  //public List<ViewKostenstellenAuswertung> getKostenstellenAuswertung(int fgid, int vonkst, int biskst, Date vondat, Date bisdat, int auswahlModus) {
  public List<ViewKostenstellenAuswertung> getKostenstellenAuswertung(String KontoNr, int vonkst, int biskst, Date vondat, Date bisdat, int auswahlModus) {

    String querry = "";

    switch (auswahlModus) {
      case 1:
        querry = "SELECT kostenstelle,Sum(betrag) as betrag,bezeichnung,fluggesellschaftID,bankKonto,datumFormatiert,primanota,ueberweisungsDatum from viewKostenstellenAuswertung "
                + "where bankKonto = #kontoNr and kostenstelle BETWEEN #vonkst and #biskst and "
                + "ueberweisungsDatum BETWEEN #vondat and #bisdat GROUP BY DATE_FORMAT(ueberweisungsDatum, '%Y-%m-%d') ,kostenstelle "
                + "ORDER BY  DATE_FORMAT(ueberweisungsDatum, '%Y-%m-%d'),kostenstelle";
        break;

      case 2:
        querry = "SELECT kostenstelle,Sum(betrag) as betrag,bezeichnung,fluggesellschaftID,bankKonto, DATE_FORMAT(ueberweisungsDatum, '%Y-%m') as monatlich,primanota "
                + "from viewKostenstellenAuswertung where bankKonto = #kontoNr and kostenstelle BETWEEN #vonkst and #biskst and "
                + "ueberweisungsDatum BETWEEN #vondat and #bisdat GROUP BY kostenstelle, monatlich ORDER BY  monatlich,kostenstelle";
        break;

      case 3:
        querry = "SELECT kostenstelle,Sum(betrag) as betrag,bezeichnung,fluggesellschaftID,bankKonto,primanota,"
                + "CONCAT(Year(ueberweisungsDatum),' ',QUARTER(datumFormatiert),'.Quartal') as quartal from viewKostenstellenAuswertung where bankKonto = #kontoNr "
                + "and kostenstelle BETWEEN #vonkst and #biskst and  ueberweisungsDatum BETWEEN #vondat and #bisdat GROUP BY kostenstelle, quartal ORDER BY quartal ,kostenstelle";
        break;

      default:
        return null;
    }

    return getEntityManager().createNativeQuery(querry, ViewKostenstellenAuswertung.class)
            .setParameter("kontoNr", KontoNr)
            .setParameter("vonkst", vonkst)
            .setParameter("biskst", biskst)
            .setParameter("vondat", vondat)
            .setParameter("bisdat", bisdat)
            .getResultList();


//    return getEntityManager().createNativeQuery(querry, ViewKostenstellenAuswertung.class)
//            .setParameter("fgid", fgid)
//            .setParameter("vonkst", vonkst)
//            .setParameter("biskst", biskst)
//            .setParameter("vondat", vondat)
//            .setParameter("bisdat", bisdat)
//            .getResultList();


//    switch (auswahlModus) {
//      case 1:
//        querry = "SELECT kostenstelle,Sum(betrag) as betrag,bezeichnung,fluggesellschaftID,datumFormatiert,primanota,ueberweisungsDatum from viewKostenstellenAuswertung "
//                + "where fluggesellschaftID = #fgid and kostenstelle BETWEEN #vonkst and #biskst and "
//                + "ueberweisungsDatum BETWEEN #vondat and #bisdat GROUP BY DATE_FORMAT(ueberweisungsDatum, '%Y-%m-%d') ,kostenstelle "
//                + "ORDER BY  DATE_FORMAT(ueberweisungsDatum, '%Y-%m-%d'),kostenstelle";
//        break;
//
//      case 2:
//        querry = "SELECT kostenstelle,Sum(betrag) as betrag,bezeichnung,fluggesellschaftID, DATE_FORMAT(ueberweisungsDatum, '%Y-%m') as monatlich,primanota "
//                + "from viewKostenstellenAuswertung where fluggesellschaftID = #fgid and kostenstelle BETWEEN #vonkst and #biskst and "
//                + "ueberweisungsDatum BETWEEN #vondat and #bisdat GROUP BY kostenstelle, monatlich ORDER BY  monatlich,kostenstelle";
//        break;
//
//      case 3:
//        querry = "SELECT kostenstelle,Sum(betrag) as betrag,bezeichnung,fluggesellschaftID,primanota,"
//                + "CONCAT(Year(ueberweisungsDatum),' ',QUARTER(datumFormatiert),'.Quartal') as quartal from viewKostenstellenAuswertung where fluggesellschaftID = #fgid "
//                + "and kostenstelle BETWEEN #vonkst and #biskst and  ueberweisungsDatum BETWEEN #vondat and #bisdat GROUP BY kostenstelle, quartal ORDER BY quartal ,kostenstelle";
//        break;
//
//      default:
//        return null;
//    }


  }

  public List<ViewMeineFluggesellschaften> getMeineFluggesellschaften(int userid) {
    return getEntityManager().createQuery("SELECT m FROM ViewMeineFluggesellschaften m WHERE m.iduser = :userid ORDER BY m.name", ViewMeineFluggesellschaften.class)
            .setParameter("userid", userid)
            .getResultList();
  }

  public List<ViewFBOUserObjekte> getFluggesellschaftFBOs(int userid, int fgid, String art) {

    if (art.equals("FBO")) {
      return getEntityManager().createQuery("SELECT o FROM ViewFBOUserObjekte o WHERE o.idUser = :userid AND o.idfluggesellschaft = :fgid AND o.fbo = true", ViewFBOUserObjekte.class)
              .setParameter("userid", userid)
              .setParameter("fgid", fgid)
              .getResultList();
    }

    if (art.equals("Terminal")) {
      return getEntityManager().createQuery("SELECT o FROM ViewFBOUserObjekte o WHERE o.idUser = :userid AND o.idfluggesellschaft = :fgid AND o.abfertigungsTerminal = true", ViewFBOUserObjekte.class)
              .setParameter("userid", userid)
              .setParameter("fgid", fgid)
              .getResultList();
    }

    if (art.equals("Tankstelle")) {
      return getEntityManager().createQuery("SELECT o FROM ViewFBOUserObjekte o WHERE o.idUser = :userid AND o.idfluggesellschaft = :fgid AND o.tankstelle = true", ViewFBOUserObjekte.class)
              .setParameter("userid", userid)
              .setParameter("fgid", fgid)
              .getResultList();
    }

    return getEntityManager().createQuery("SELECT o FROM ViewFBOUserObjekte o WHERE o.idUser = -1", ViewFBOUserObjekte.class)
            .getResultList();

  }

  //************************* Flugzeuge BEGINN
  public List<ViewFlugzeugeMietKauf> getFlugzeugeFluggesellscahft(int fgid) {
    return getEntityManager().createQuery("SELECT f from ViewFlugzeugeMietKauf f WHERE f.fluggesellschaftID = :fgid ", ViewFlugzeugeMietKauf.class)
            .setParameter("fgid", fgid)
            .getResultList();
  }

  public Flugzeuge readFlugzeugbyID(int fgzid) {
    return getEntityManager().createQuery("SELECT f FROM Flugzeuge f WHERE f.idFlugzeug = :fgzid", Flugzeuge.class)
            .setParameter("fgzid", fgzid)
            .getSingleResult();
  }

  //************************* Flugzeuge ENDE
  //************************* Remove Pilot von der Fluggesellschaft Beginn
  public void onRemovePilotVonDerFluggesellschaft(int pilotID) {

    getEntityManager().remove(getEntityManager().merge(
            getEntityManager().createQuery("SELECT p from FlugesellschaftPiloten p WHERE p.idflugesellschaftPiloten = :pilotID", FlugesellschaftPiloten.class)
                    .setParameter("pilotID", pilotID)
                    .getSingleResult())
    );

  }

  public void onRemoveFromErlaubteFlugzeuge(int pilotID, int fgid) {
    getEntityManager().createQuery("DELETE FROM FlugzeugeErlaubteUser f where f.idUser = :pilotid and f.idFluggesellschaft = :fgid")
            .setParameter("pilotid", pilotID)
            .setParameter("fgid", fgid)
            .executeUpdate();

  }

  //************************* Remove Pilot von der Fluggesellschaft ENDE
  //************************* Statistik ENDE
  //************************* Kostenstellen Beginn
  public List<Kostenstellen> getKostenstellen(int UserID) {
    return getEntityManager().createQuery("SELECT k from Kostenstellen k where k.userid = :userid ORDER BY k.kostenstelle", Kostenstellen.class)
            .setParameter("userid", UserID)
            .getResultList();
  }

  public void createKostenStelle(Kostenstellen entity) {
    getEntityManager().persist(entity);
  }

  public void deleteKostenStelle(Kostenstellen entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  //************************* Kostenstellen ENDE
  //Umsatzzahlen der Piloten der Fluggesellschaft
  public ViewFluggesellschaftUmsatzPiloten getUmsatzPiloten(int fgID) {
    try {
      return getEntityManager().createQuery("SELECT u FROM ViewFluggesellschaftUmsatzPiloten u WHERE u.idflugesellschaft = :fgid", ViewFluggesellschaftUmsatzPiloten.class)
              .setParameter("fgid", fgID)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

// umsatz über Bankkonto
  public double getBankSaldo(String BankKonto) {
    return (double) getEntityManager().createQuery("SELECT SUM(b.betrag) FROM Bank b where b.bankKonto = :bankKonto")
            .setParameter("bankKonto", BankKonto)
            .getSingleResult();
  }

  public List<Assignement> findByICAOLocationAndtoIcaoMap(String icao) {
    return getEntityManager().createQuery("SELECT a FROM Assignement a WHERE a.oeffentlich = 1 AND a.locationIcao = :icao and a.idjob = -1 group by a.toIcao ", Assignement.class)
            .setParameter("icao", icao)
            .getResultList();
  }

  @SuppressWarnings("Unchecked")
  public List getRoutenMengen(String locationIcao, String toIcao) {

    String Abfrage = "";

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
            + "        location_icao = '" + locationIcao + "' AND to_icao = '" + toIcao + "' AND idowner <= 0"
            + "            "
            + "    GROUP BY routenArt) AS tmp1";

    return getEntityManager().createNativeQuery(Abfrage).getResultList();
  }


  /*
  *******************************************
  *************************** MapsBean BEGINN
  *******************************************
   */

 /*
  ****************** LiveACARS Beginn
   */
  public List<Yaacarskopf> getYAACARSFluege() {
    return getEntityManager().createQuery("SELECT y FROM Yaacarskopf y", Yaacarskopf.class).getResultList();
  }

  public Yaacarspositionen readYAACARSLetztePosition(int KopfID) {
    try {
      return getEntityManager().createQuery("SELECT y FROM Yaacarspositionen y WHERE y.idyaacarskopf = :id ORDER BY y.idyaacarspositionen DESC", Yaacarspositionen.class)
              .setParameter("id", KopfID)
              .setMaxResults(1)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public ViewFlugzeugeMietKauf readFlugzeugMietKauf(int MietKaufID) {
    try {
      return getEntityManager().createQuery("SELECT f FROM ViewFlugzeugeMietKauf f WHERE f.idMietKauf = :id", ViewFlugzeugeMietKauf.class)
              .setParameter("id", MietKaufID)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public List getAcarsPiloten() {
    return getEntityManager().createNativeQuery("SELECT username FROM yaacarskopf; ").getResultList();
  }


  /*
  ****************** LiveACARS ENDE
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> readLaenderFromAirports() {
    return getEntityManager().createQuery("SELECT DISTINCT l.land FROM Airport l ORDER BY l.land").getResultList();
  }

  public List<FlughafenKlassen> findAllKlassen() {
    return getEntityManager().createQuery("SELECT f FROM FlughafenKlassen f ORDER BY f.klasseNummer", FlughafenKlassen.class).getResultList();
  }

  public List<Airport> readAirportsByKlasse(int klasse, String land) {

    if (klasse == 0) {
      return getEntityManager().createQuery("SELECT a from Airport a where a.land = :land", Airport.class)
              .setParameter("land", land)
              .getResultList();

    } else {
      return getEntityManager().createQuery("SELECT a from Airport a where a.klasse = :klasse and a.land = :land", Airport.class)
              .setParameter("land", land)
              .setParameter("klasse", klasse)
              .getResultList();

    }

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

  public List<ViewAirportAnflugZiele> AnflugZiele(int id) {
    return getEntityManager().createQuery("SELECT v from ViewAirportAnflugZiele v WHERE v.quellidAirport =:idquelle", ViewAirportAnflugZiele.class)
            .setParameter("idquelle", id)
            .getResultList();
  }

  public boolean onUserFlugZuruecksetzen(int userid) {

    Yaacarskopf kopf = getEntityManager().createQuery("SELECT y FROM Yaacarskopf y WHERE y.userid = :id", Yaacarskopf.class)
            .setParameter("id", userid)
            .getSingleResult();

    try {

      int KopfID = kopf.getIdyaacarskopf();

      getEntityManager().createQuery("DELETE FROM Yaacarskopf y WHERE y.idyaacarskopf = :id", Yaacarskopf.class)
              .setParameter("id", KopfID)
              .executeUpdate();

      getEntityManager().createQuery("DELETE FROM Yaacarspositionen y WHERE y.idyaacarskopf = :id", Yaacarspositionen.class)
              .setParameter("id", KopfID)
              .executeUpdate();

    } catch (Exception e) {
      System.out.println("de.klees.beans.flugzeuge.FlugzeugFacade.onUserFlugZuruecksetzen()");;
    }

    String Abfrage = "Update assignement set userlock = 0 where userlock = 1 and idowner = " + userid;
    getEntityManager().createNativeQuery(Abfrage).executeUpdate();

    Abfrage = "UPDATE Flugzeuge_miet_kauf  "
            + "SET  "
            + "    istGesperrt = FALSE, "
            + "    istInDerLuft = FALSE, "
            + "    idUserDerFlugzeugGesperrtHat = - 1, "
            + "    istGesperrtBis = NULL, "
            + "    istGesperrtSeit = NULL "
            + "WHERE "
            + "    idUserDerFlugzeugGesperrtHat = " + userid;

    getEntityManager().createNativeQuery(Abfrage).executeUpdate();

    return true;
  }

  /*
  *****************************************
  *************************** MapsBean ENDE
  *****************************************
   */
 /*
  ************ MOD Tools
   */
  public int MODNativSQLExecute(String sqlabfrage) {
    return getEntityManager().createNativeQuery(sqlabfrage).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<Object> MODSelectSql(String sqlabfrage) {
    return getEntityManager().createQuery(sqlabfrage).getResultList();
  }

  public List<Modsql> getSqlAbfragen() {
    return getEntityManager().createQuery("SELECT m FROM Modsql m ORDER BY m.bezeichnung", Modsql.class).getResultList();
  }

  public void saveUserMail(Mail entity) {
    getEntityManager().persist(entity);
  }

}
