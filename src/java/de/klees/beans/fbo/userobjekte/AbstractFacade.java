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

package de.klees.beans.fbo.userobjekte;

import de.klees.data.Airport;
import de.klees.data.Assignement;
import de.klees.data.Bank;
import de.klees.data.Bestellungen;
import de.klees.data.FboObjekte;
import de.klees.data.FboUserObjekte;
import de.klees.data.Feinabstimmung;
import de.klees.data.Fluggesellschaft;
import de.klees.data.Fluggesellschaftmanager;
import de.klees.data.Flugrouten;
import de.klees.data.Flugzeuge;
import de.klees.data.Hangarbelegung;
import de.klees.data.Kostenstellen;
import de.klees.data.Lagerbestellungservicehangar;
import de.klees.data.Lagerservicehangar;
import de.klees.data.Lagerservicehangarumsatz;
import de.klees.data.Servicehangartermine;
import de.klees.data.Benutzer;
import de.klees.data.views.ViewBestellungenDetail;
import de.klees.data.views.ViewFBOUserObjekte;
import de.klees.data.views.ViewFlugzeugeMietKauf;
import de.klees.data.views.ViewTankstellenInFTW;
import de.klees.data.views.ViewuebersichtServiceHangar;
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

  public void onEditUserFBO(FboUserObjekte entity) {
    getEntityManager().merge(entity);
  }

  @SuppressWarnings("unchecked")
  public void remove(T entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
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

  public Fluggesellschaft readMyFg(int userID) {
    try {
      return getEntityManager().createQuery("SELECT f from Fluggesellschaft f WHERE f.userid = :userid", Fluggesellschaft.class)
              .setParameter("userid", userID)
              .getSingleResult();
    } catch (Exception e) {
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

  public Airport readAirport(String icao) {

    try {
      return getEntityManager().createQuery("SELECT a FROM Airport a WHERE a.icao = :icao", Airport.class)
              .setParameter("icao", icao)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public Flugzeuge readFlugzeug(int flzID) {
    try {
      return getEntityManager().createQuery("SELECT f FROM Flugzeuge f WHERE f.idFlugzeug = :flzid", Flugzeuge.class)
              .setParameter("flzid", flzID)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public ViewFlugzeugeMietKauf readFlugzeugByType(String type) {
    try {
      return getEntityManager().createQuery("SELECT f FROM ViewFlugzeugeMietKauf f WHERE f.type = :type", ViewFlugzeugeMietKauf.class)
              .setParameter("type", type)
              .setMaxResults(1)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public void onBestellung(Bestellungen entity) {
    getEntityManager().persist(entity);
  }

  public void onKuendigung(int fboID) {
    getEntityManager().createQuery("DELETE FROM FboUserObjekte f where f.iduserfbo = :fboid", FboUserObjekte.class)
            .setParameter("fboid", fboID)
            .executeUpdate();

    getEntityManager().createQuery("DELETE FROM Bestellungen b WHERE b.objektID = :fboid", Bestellungen.class)
            .setParameter("fboid", fboID)
            .executeUpdate();
//    getEntityManager().createNativeQuery("Delete from fboUserObjekte where iduserfbo = " + fboID).executeUpdate();
//    getEntityManager().createNativeQuery("Delete from Bestellungen where objektID = " + fboID).executeUpdate();
  }

  public void onUmbauSpeichern(FboUserObjekte entity) {
    getEntityManager().persist(entity);
  }

  public List<Flugrouten> getFlugroutenUberTerminalID(int id) {
    return getEntityManager().createQuery("SELECT f FROM Flugrouten f WHERE f.idUserFBO = :id", Flugrouten.class)
            .setParameter("id", id)
            .getResultList();
  }

  public List<Flugrouten> getFlugroutenUeberID(int fboID, int fgid, int anzahl) {
    return getEntityManager().createQuery("SELECT f FROM Flugrouten f WHERE f. = :icao AND f.idFluggesellschaft = :fgid", Flugrouten.class)
            .setMaxResults(anzahl)
            .setParameter("fboid", fboID)
            .setParameter("fgid", fgid)
            .getResultList();
  }

  public List<ViewFBOUserObjekte> findFBOByUser(int userID) {
    return getEntityManager().createQuery("SELECT v FROM ViewFBOUserObjekte v WHERE v.idUser = :userID ORDER BY v.icao ", ViewFBOUserObjekte.class)
            .setParameter("userID", userID)
            .getResultList();
  }

  public ViewFBOUserObjekte readFBOObjektByID(int id) {
    try {
      return getEntityManager().createQuery("SELECT f FROM ViewFBOUserObjekte f WHERE f.iduserfbo = :id", ViewFBOUserObjekte.class)
              .setParameter("id", id)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }

  }

  public List<ViewFBOUserObjekte> findAllFBOByUser(int userID, int managerID, String fltIcao, String fltObjekt) {
    return getEntityManager().createQuery("SELECT v FROM ViewFBOUserObjekte v WHERE (v.idUser = :userID OR v.idUser = :managerID) and (v.icao like :flticao and v.objektName like :fltobjekt)  ORDER BY v.icao ", ViewFBOUserObjekte.class)
            .setParameter("userID", userID)
            .setParameter("managerID", managerID)
            .setParameter("flticao", fltIcao)
            .setParameter("fltobjekt", fltObjekt)
            .getResultList();
  }

  public List<ViewFBOUserObjekte> findAllSpritlager(int fgid) {
    return getEntityManager().createQuery("SELECT v FROM ViewFBOUserObjekte v WHERE (v.idfluggesellschaft =:fgid and v.spritlager = true) ORDER BY v.icao ", ViewFBOUserObjekte.class)
            .setParameter("fgid", fgid)
            .getResultList();
  }

  public List<ViewFBOUserObjekte> findAllFBOByUserGroupByIcao(int userID, int managerID) {
    return getEntityManager().createQuery("SELECT v FROM ViewFBOUserObjekte v WHERE v.idUser = :userID OR v.idUser = :managerID GROUP BY v.icao  ORDER BY v.icao", ViewFBOUserObjekte.class)
            .setParameter("userID", userID)
            .setParameter("managerID", managerID)
            .getResultList();
  }

  public List<ViewFBOUserObjekte> findAllFBOByUserGroupByObjekt(int userID, int managerID) {
    return getEntityManager().createQuery("SELECT v FROM ViewFBOUserObjekte v WHERE v.idUser = :userID OR v.idUser = :managerID GROUP BY v.objektName  ORDER BY v.objektName", ViewFBOUserObjekte.class)
            .setParameter("userID", userID)
            .setParameter("managerID", managerID)
            .getResultList();
  }

  public List<ViewFBOUserObjekte> findAllFBOBFuerZahlungen(int userID, int fgid) {
    return getEntityManager().createQuery("SELECT v FROM ViewFBOUserObjekte v WHERE v.idfluggesellschaft = :fgid ORDER BY v.faelligkeitNaechsteMiete ASC ", ViewFBOUserObjekte.class)
            .setParameter("fgid", fgid)
            .getResultList();
  }

  public List<FboObjekte> findAllObjekteDerArt(int Art, int klasse) {
    String sqlQuery = "";

    // 1 FBO, 2 Abfertigungsterminal, 3 Tankstelle, 4 Servicehangar
    switch (Art) {
      case 3:
        return getEntityManager().createQuery("SELECT o FROM FboObjekte o WHERE o.tankstelle = TRUE AND o.klasse = :klasse", FboObjekte.class)
                .setParameter("klasse", klasse)
                .getResultList();
      case 2:
        return getEntityManager().createQuery("SELECT o FROM FboObjekte o WHERE o.abfertigungsTerminal = TRUE AND o.klasse = :klasse", FboObjekte.class)
                .setParameter("klasse", klasse)
                .getResultList();
      case 1:
        return getEntityManager().createQuery("SELECT o FROM FboObjekte o WHERE o.fbo = TRUE AND o.klasse = :klasse", FboObjekte.class)
                .setParameter("klasse", klasse)
                .getResultList();
      case 4:
        return getEntityManager().createQuery("SELECT o FROM FboObjekte o WHERE o.servicehangar = TRUE AND o.klasse = :klasse", FboObjekte.class)
                .setParameter("klasse", klasse)
                .getResultList();
      default:
        return getEntityManager().createQuery("SELECT o FROM FboObjekte o WHERE o.idObjekt = -500 AND o.klasse = :klasse", FboObjekte.class)
                .setParameter("klasse", klasse)
                .getResultList();
    }
  }

  public int istFBOLeer(int userid, String icao) {

    int objAnzahl = getEntityManager().createQuery("SELECT o FROM FboUserObjekte o WHERE o.icao = :icao AND o.idUser = :userid", FboUserObjekte.class)
            .setParameter("userid", userid)
            .setParameter("icao", icao)
            .getResultList().size();

    //System.out.println("de.klees.beans.fbo.userobjekte.AbstractFacade.istFBOLeer() Anzahl Objekte : " + objAnzahl);
    return objAnzahl;

  }

  public long wievieleFBOAmFlughafen(String icao) {
    return (long) getEntityManager().createQuery("SELECT COUNT (f) from ViewFBOUserObjekte f WHERE f.icao = :icao AND f.fbo = TRUE")
            .setParameter("icao", icao)
            .getSingleResult();
  }

  public long getAnzahlFBO(String icao) {
    return (long) getEntityManager().createQuery("SELECT COUNT (o) FROM ViewFBOUserObjekte o WHERE o.icao = :icao and o.fbo = TRUE")
            .setParameter("icao", icao)
            .getSingleResult();

  }

  public List<ViewBestellungenDetail> findBestellungenByUserID(int userID, int fgUserid) {
    return getEntityManager().createQuery("SELECT b from ViewBestellungenDetail b WHERE (b.userID = :userID or b.userID =:fgUserid) ORDER BY b.lieferdatum ASC", ViewBestellungenDetail.class)
            .setParameter("userID", userID)
            .setParameter("fgUserid", fgUserid)
            .getResultList();
  }

  public List<ViewBestellungenDetail> findBestellungenByIcao(String icao, int userid) {
    return getEntityManager().createQuery("SELECT b from ViewBestellungenDetail b WHERE b.icao = :icao and b.userID = :userid ORDER BY b.lieferdatum ASC", ViewBestellungenDetail.class)
            .setParameter("icao", icao)
            .setParameter("userid", userid)
            .getResultList();
  }

  public FboUserObjekte findByFboID(int fboID) {
    return getEntityManager().createQuery("SELECT f from FboUserObjekte f WHERE f.iduserfbo = :fboID", FboUserObjekte.class)
            .setParameter("fboID", fboID)
            .getSingleResult();
  }

  public List<ViewFBOUserObjekte> findTankstellenByICAO(String icao) {
    return getEntityManager().createQuery("SELECT t FROM ViewFBOUserObjekte t WHERE t.tankstelle=true AND t.icao = :icao", ViewFBOUserObjekte.class)
            .setParameter("icao", icao)
            .getResultList();
  }

  public List<ViewFBOUserObjekte> getAlleServicehangars() {
    return getEntityManager().createQuery("SELECT s FROM ViewFBOUserObjekte s WHERE s.servicehangar = true", ViewFBOUserObjekte.class).getResultList();
  }

  public List<ViewFBOUserObjekte> findAllTankstellen(int userID, int fgid) {
    return getEntityManager().createQuery("SELECT t FROM ViewFBOUserObjekte t WHERE t.tankstelle = true AND (t.idUser = :userid or t.idUser = :fgid)", ViewFBOUserObjekte.class)
            .setParameter("userid", userID)
            .setParameter("fgid", fgid)
            .getResultList();
  }

  public int getAnzahlMaxRouten(int userid, String icao) {
    String Abfrage = "SELECT CAST(SUM( fboObjekte.anzahlRouten ) AS UNSIGNED) AS `anzahlRouten` FROM fboObjekte AS fboObjekte, fboUserObjekte AS fboUserObjekte "
            + " WHERE fboObjekte.idObjekt = fboUserObjekte.idfboObjekt AND fboUserObjekte.idUser = " + userid + " AND fboUserObjekte.icao = '" + icao + "'";
    return Integer.valueOf(getEntityManager().createNativeQuery(Abfrage).getSingleResult().toString());
  }

  public int getAnzahlRouten(int fboid, String icao) {
    String Abfrage = "SELECT count(*) FROM Flugrouten where idFbo = " + fboid;
    return Integer.valueOf(getEntityManager().createNativeQuery(Abfrage).getSingleResult().toString());
  }

  public int getAnzahlRoutenObjekte(int userid, String icao) {
    String Abfrage = "SELECT sum(anzahlRouten) FROM viewFBOUserObjekte where idUser = " + userid + " and icao = '" + icao + "'";
    return Integer.valueOf(getEntityManager().createNativeQuery(Abfrage).getSingleResult().toString());
  }

  public int changeAssignmentFBO(int alteID, int neueID) {

    String Abfrage = "update assignement set idFBO = " + neueID + " where idFBO = " + alteID;
    int z = getEntityManager().createNativeQuery(Abfrage).executeUpdate();

    Abfrage = "update assignement set idTerminal = " + neueID + " where idTerminal = " + alteID;
    z = z + getEntityManager().createNativeQuery(Abfrage).executeUpdate();

    return z;
  }

  public int changeBestellungenIDTankstelle(int alteID, int neueID) {
    String Abfrage = "update Bestellungen set objektID = " + neueID + " where objektID = " + alteID;
    return getEntityManager().createNativeQuery(Abfrage).executeUpdate();
  }

  public int changeRoutenFBO(int alteID, int neueID) {

    int z = getEntityManager().createQuery("UPDATE Flugrouten f SET f.idUserFBO = :neueID WHERE f.idUserFBO = :alteID")
            .setParameter("alteID", alteID)
            .setParameter("neueID", neueID)
            .executeUpdate();

    z = getEntityManager().createQuery("UPDATE Flugrouten f SET f.idTerminalDep = :neueID WHERE f.idTerminalDep = :alteID")
            .setParameter("alteID", alteID)
            .setParameter("neueID", neueID)
            .executeUpdate();

    z = getEntityManager().createQuery("UPDATE Flugrouten f SET f.idTerminalArr = :neueID WHERE f.idTerminalArr = :alteID")
            .setParameter("alteID", alteID)
            .setParameter("neueID", neueID)
            .executeUpdate();

    return z;
  }

  public void deleteRoutenUeberTerminalID(int id) {
    int z = getEntityManager().createQuery("DELETE FROM Flugrouten f WHERE f.idUserFBO = :id")
            .setParameter("id", id)
            .executeUpdate();
  }

  //
  public void createBankbuchung(Bank entity) {
    getEntityManager().persist(entity);
  }

  public double BankSaldo(String KontoNummer) {
    try {
      String Abfrage = "SELECT SUM( betrag ) as Betrag FROM bank WHERE bankKonto ='" + KontoNummer + "'";
      return (double) getEntityManager().createNativeQuery(Abfrage).getSingleResult();
    } catch (NullPointerException | NoResultException e) {
      return 0.0;
    }
  }

  public String getBankKontoName(String BankKonto) {
    return getEntityManager().createQuery("SELECT b FROM Bank b WHERE b.bankKonto = :bankkonto", Bank.class)
            .setParameter("bankkonto", BankKonto)
            .setMaxResults(1).getSingleResult().getKontoName();
  }

  public List<Fluggesellschaft> getFGKonten(int userID) {
    return getEntityManager().createQuery("SELECT f FROM Fluggesellschaft f WHERE f.userid = :userid", Fluggesellschaft.class)
            .setParameter("userid", userID)
            .getResultList();
  }

  public List<Fluggesellschaft> getAllFGKonten(int userID, int fgID) {
    return getEntityManager().createQuery("SELECT f FROM Fluggesellschaft f WHERE f.userid = :userid OR f.idFluggesellschaft = :fgid ", Fluggesellschaft.class)
            .setParameter("userid", userID)
            .setParameter("fgid", fgID)
            .getResultList();
  }

  public List<ViewTankstellenInFTW> getFTWTankstellen() {
    return getEntityManager().createQuery("SELECT t from ViewTankstellenInFTW t", ViewTankstellenInFTW.class).getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<String> MeineFluggesellschaften(int userID, int fgid) {
    return getEntityManager().createQuery("SELECT DISTINCT f.idFluggesellschaft, f.name from Fluggesellschaft f where f.userid = :userID OR f.idFluggesellschaft = :fgid")
            .setParameter("userID", userID)
            .setParameter("fgid", fgid)
            .getResultList();
  }

  public List<Kostenstellen> getKostenstellen(int UserID) {
    return getEntityManager().createQuery("SELECT k from Kostenstellen k where k.userid = :userid ORDER BY k.kostenstelle", Kostenstellen.class)
            .setParameter("userid", UserID)
            .getResultList();
  }

  public Airport getAirportInfo(String icao) {

    return getEntityManager().createQuery("SELECT a from Airport a where a.icao = :icao", Airport.class)
            .setParameter("icao", icao)
            .getSingleResult();

  }

  public List<Assignement> getAusstehendeSpritFaesser(int FboID) {
    return getEntityManager().createQuery("SELECT a FROM Assignement a WHERE a.idFBO =:fboid", Assignement.class)
            .setParameter("fboid", FboID)
            .getResultList();
  }

  /*
  *********************** Config Auslesen Beginn***************************************
   */
  public Feinabstimmung readConfig() {
    return getEntityManager().createQuery("SELECT c from Feinabstimmung c", Feinabstimmung.class).setMaxResults(1).getSingleResult();
  }

  /*
  *********************** Config Auslesen ENDE ***************************************
   */
 /*
  Service Hangar BEGINN
   */
  public List<Flugzeuge> getListeServicePacks() {
    return getEntityManager().createQuery("SELECT p FROM Flugzeuge p ORDER BY p.type", Flugzeuge.class).getResultList();
  }

  public void neueServiceHangarBestellung(Lagerbestellungservicehangar entity) {
    getEntityManager().persist(entity);
  }

  public void editServiceHangarBestellung(Lagerbestellungservicehangar entity) {
    getEntityManager().merge(entity);

  }

  public void editLagerServiceHangarSave(Lagerservicehangar entity) {
    getEntityManager().merge(entity);
  }

  public void neuerUmsatzVerbuchen(Lagerservicehangarumsatz entity) {
    getEntityManager().persist(entity);
  }

  public void neuerHangarTermin(Servicehangartermine entity) {
    getEntityManager().persist(entity);
  }

  public void editHangarTermin(Servicehangartermine entity) {
    getEntityManager().merge(entity);
  }

  public void removeHangarTermin(Servicehangartermine entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public void alleTermineLoeschen(int HangarID) {
    getEntityManager().createQuery("DELETE FROM Servicehangartermine s WHERE s.idservicehangar = :hangarid")
            .setParameter("hangarid", HangarID)
            .executeUpdate();
  }

  public Lagerservicehangar getLagerArtikel(int id) {
    try {
      return getEntityManager().createQuery("SELECT l FROM Lagerservicehangar l WHERE l.idlagerservicehangar = :id", Lagerservicehangar.class)
              .setParameter("id", id)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public List<Lagerservicehangar> getLagerBestaende(int fbouserid) {
    return getEntityManager().createQuery("SELECT l FROM Lagerservicehangar l WHERE l.idfbouserobjekt = :id ORDER BY l.paketname, l.paketart", Lagerservicehangar.class)
            .setParameter("id", fbouserid)
            .getResultList();
  }

  public List<Lagerbestellungservicehangar> getServicHangarBestellungen(int fbouserid) {
    return getEntityManager().createQuery("SELECT l FROM Lagerbestellungservicehangar l WHERE l.idfbouserobjekt = :id ORDER BY l.zustellungam", Lagerbestellungservicehangar.class)
            .setParameter("id", fbouserid)
            .getResultList();
  }

  public void deleteServicHangarBestellung(int id) {
    getEntityManager().createQuery("DELETE FROM Lagerbestellungservicehangar s WHERE s.idlagerbestellungservicehangar =:id")
            .setParameter("id", id)
            .executeUpdate();
  }

  public List<Hangarbelegung> getHangarBelegung(int fbouserid) {
    return getEntityManager().createQuery("SELECT b FROM Hangarbelegung b WHERE b.iduserfboid = :fboid ORDER BY b.ablaufzeit DESC", Hangarbelegung.class)
            .setParameter("fboid", fbouserid)
            .getResultList();
  }

  public List<ViewuebersichtServiceHangar> getListeServiceHangars() {
    return getEntityManager().createQuery("SELECT s FROM ViewuebersichtServiceHangar s ORDER BY s.land", ViewuebersichtServiceHangar.class).getResultList();
  }

  public List<Lagerservicehangarumsatz> getPaketUmsatz(int id) {
    return getEntityManager().createQuery("SELECT p FROM Lagerservicehangarumsatz p WHERE p.idlagerservicehangar = :id ORDER BY p.verkauftam DESC", Lagerservicehangarumsatz.class)
            .setParameter("id", id)
            .getResultList();
  }

  public List<Servicehangartermine> getHangarTermine(int HangarID) {
    return getEntityManager().createQuery("SELECT t FROM Servicehangartermine t WHERE t.idservicehangar = :hangarid", Servicehangartermine.class)
            .setParameter("hangarid", HangarID)
            .getResultList();
  }

  public boolean istHangarBelegt(int id) {
    try {
      Hangarbelegung hgb = getEntityManager().createQuery("SELECT h FROM Hangarbelegung h WHERE h.iduserfboid = :id", Hangarbelegung.class).setParameter("id", id).setMaxResults(1).getSingleResult();
      return true;
    } catch (NoResultException e) {
      return false;
    }
  }

  public void hangarUmziehen(int alteHangarID, int neueHangarID) {
    getEntityManager().createNativeQuery("Update lagerbestellungservicehangar set idfbouserobjekt = " + neueHangarID + " where idfbouserobjekt = " + alteHangarID).executeUpdate();
    getEntityManager().createNativeQuery("update lagerservicehangar set idfbouserobjekt = " + neueHangarID + " where idfbouserobjekt = " + alteHangarID).executeUpdate();
    getEntityManager().createNativeQuery("update lagerservicehangarumsatz set idfbouserobjekt = " + neueHangarID + " where idfbouserobjekt = " + alteHangarID).executeUpdate();
    getEntityManager().createNativeQuery("update servicehangartermine set idservicehangar = " + neueHangarID + " where idservicehangar = " + alteHangarID).executeUpdate();

  }

  public void deleteServiceHangar(int id) {
    int result = getEntityManager().createNativeQuery("DELETE FROM lagerbestellungservicehangar WHERE idfbouserobjekt = " + id).executeUpdate();
    result = getEntityManager().createNativeQuery("DELETE FROM lagerservicehangar WHERE idfbouserobjekt = " + id).executeUpdate();
    result = getEntityManager().createNativeQuery("DELETE FROM servicehangartermine WHERE  idservicehangar = " + id).executeUpdate();
    result = getEntityManager().createNativeQuery("DELETE FROM lagerservicehangarumsatz WHERE  idfbouserobjekt = " + id).executeUpdate();
  }

  public void removeLagerPaket(Lagerservicehangar entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  /*
  Service Hangar ENDE
   */

 /*
  ************************ Assignments BEGINN
   */
  public void createAssignment(Assignement entity) {
    getEntityManager().persist(entity);
  }

  public List<Assignement> listAuftraegeUeberTerminalID(int id) {
    return getEntityManager().createQuery("SELECT a FROM Assignement a where a.idFBO = :id", Assignement.class)
            .setParameter("id", id)
            .getResultList();
  }

  public void deleteAssignmentsUeberTerminalID(int id) {
    int z = getEntityManager().createQuery("DELETE FROM Assignement a WHERE a.idFBO = :id")
            .setParameter("id", id)
            .executeUpdate();
  }
  
  
  /*
  ************************ Assignments ENDE
   */
}
