
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

package de.klees.beans.flugzeuge;

import de.klees.data.Airport;
import de.klees.data.Bank;
import de.klees.data.FboUserObjekte;
import de.klees.data.Feinabstimmung;
import de.klees.data.FlugesellschaftPiloten;
import de.klees.data.Fluggesellschaft;
import de.klees.data.Fluggesellschaftmanager;
import de.klees.data.Flugzeugblacklist;
import de.klees.data.Flugzeugboerse;
import de.klees.data.Flugzeugboersetemplates;
import de.klees.data.Flugzeuge;
import de.klees.data.FlugzeugeErlaubteUser;
import de.klees.data.Flugzeugemietkauf;
import de.klees.data.Flugzeugemodelle;
import de.klees.data.Flugzeugsymbole;
import de.klees.data.Flugzeugtransfer;
import de.klees.data.Hangarbelegung;
import de.klees.data.Kostenstellen;
import de.klees.data.Lagerservicehangar;
import de.klees.data.Lagerservicehangarumsatz;
import de.klees.data.Mail;
import de.klees.data.Servicehangartermine;
import de.klees.data.Sitzkonfiguration;
import de.klees.data.UmbauSitzplatz;
import de.klees.data.Benutzer;
import de.klees.data.UserTyperatings;
import de.klees.data.Yaacarskopf;
import de.klees.data.Yaacarspositionen;
import de.klees.data.views.ViewFBOUserObjekte;
import de.klees.data.views.ViewFlugzeugVerbrauch;
import de.klees.data.views.ViewFlugzeugeMietKauf;
import de.klees.data.views.ViewFlugzeuglog;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Stefan Klees
 */
@Stateless
public class FlugzeugFacade {

  private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @PersistenceContext(unitName = "FlyTheWorldPU")
  private EntityManager em;

  protected EntityManager getEntityManager() {
    return em;
  }

  /*
  ***************** CRUD Bereich Start
   */
  public void createFlugzeug(Flugzeuge entity) {
    getEntityManager().persist(entity);
  }

  public void createFlugzeugMietKauf(Flugzeugemietkauf entity) {
    getEntityManager().persist(entity);
  }

  public void createUmbau(UmbauSitzplatz entity) {
    getEntityManager().persist(entity);
  }

  public void editFlugzeug(Flugzeuge entity) {
    getEntityManager().merge(entity);
  }

  public void editFlugzeugMietKauf(Flugzeugemietkauf entity) {
    getEntityManager().merge(entity);
  }

  public void removeFlugzeug(Flugzeuge entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public void createModell(Flugzeugemodelle entity) {
    getEntityManager().persist(entity);
  }

  public void editModell(Flugzeugemodelle entity) {
    getEntityManager().merge(entity);
  }

  public void removeModell(Flugzeugemodelle entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public void createSymbol(Flugzeugsymbole entity) {
    getEntityManager().persist(entity);
  }

  public void editSymbol(Flugzeugsymbole entity) {
    getEntityManager().merge(entity);
  }

  public void removeSymbol(Flugzeugsymbole entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public void createBankbuchung(Bank entity) {
    getEntityManager().persist(entity);
  }

  /*
  ***************** CRUD Bereich Ende
   */
  public Feinabstimmung readConfig() {
    return getEntityManager().createQuery("SELECT c from Feinabstimmung c", Feinabstimmung.class).setMaxResults(1).getSingleResult();
  }

  public List<Fluggesellschaft> getFGKonten(int userID, int fgid) {
    return getEntityManager().createQuery("SELECT f FROM Fluggesellschaft f WHERE f.userid = :userid or f.idFluggesellschaft = :fgid", Fluggesellschaft.class)
            .setParameter("userid", userID)
            .setParameter("fgid", fgid)
            .getResultList();
  }

  /*
  ***************** Flugzeuge Stock Bereich FTW-Hangar BEGINN
   */
  public List<Flugzeuge> findAll() {
    return getEntityManager().createQuery("SELECT f FROM Flugzeuge f WHERE f.nurFuerUmbau = FALSE", Flugzeuge.class).getResultList();
  }

  public List<Flugzeuge> findAllOrdered() {
    return getEntityManager().createQuery("SELECT f FROM Flugzeuge f WHERE f.nurFuerUmbau = FALSE ORDER BY f.hersteller", Flugzeuge.class).getResultList();
  }

  public List<Flugzeuge> findAllUmbau() {
    return getEntityManager().createQuery("SELECT f FROM Flugzeuge f WHERE f.nurFuerUmbau = TRUE", Flugzeuge.class).getResultList();
  }

  public List<Flugzeuge> findAllFuerUserbearbeitung() {
    return getEntityManager().createQuery("SELECT f from Flugzeuge f where f.isUserEdit=1", Flugzeuge.class).getResultList();
  }

  public List<Flugzeugemodelle> findAllFlugzeugModelle() {
    return getEntityManager().createNamedQuery("Flugzeugemodelle.findAll", Flugzeugemodelle.class).getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<String> findFlugzeugModelleForUserHangar(String Art) {
    Art = "%" + Art + "%";
    return getEntityManager().createQuery("SELECT DISTINCT f.type FROM ViewFlugzeugeMietKauf AS f where f.flugzeugArt like :art ORDER BY f.type ")
            .setParameter("art", Art)
            .getResultList();
  }

  public List<Flugzeugsymbole> findAllFlugzeugSymbole() {
    return getEntityManager().createQuery("SELECT f FROM Flugzeugsymbole f ORDER BY f.flugzeugModell", Flugzeugsymbole.class).getResultList();
  }

  public boolean ifExistFlugzeug(String FlugzeugTyp) {
    if (getEntityManager().createNamedQuery("Flugzeuge.findByType", Flugzeuge.class).setParameter("type", FlugzeugTyp).getResultList().size() > 0) {
      return true;
    } else {
      return false;
    }
  }

  public boolean ifExistMietKaufFlugzeugAdmin(int fgzID) {
    if (getEntityManager().createQuery("SELECT f FROM ViewFlugzeugeMietKauf f where f.idFlugzeug = :fgzid ", ViewFlugzeugeMietKauf.class)
            .setParameter("fgzid", fgzID).getResultList().size() > 0) {
      return true;
    } else {
      return false;
    }
  }

  public Flugzeuge findFlugzeugbyID(int FlugzeugID) {
    return (Flugzeuge) getEntityManager().createNamedQuery("Flugzeuge.findByIdFlugzeug", Flugzeuge.class).setParameter("idFlugzeug", FlugzeugID).getSingleResult();
  }

  public boolean onFlugzeugKauf(int FlugzeugID, int UserID, String BankKonto) {
    int updateCount = getEntityManager().createQuery("UPDATE Flugzeugemietkauf f SET f.istZuVerkaufen=0, f.bankkontoBesitzer= :bankKonto, f.istMietbar=0, f.idFluggesellschaft=-1, f.idIndustrie=-1, f.idflugzeugBesitzer = " + UserID + " "
            + "  WHERE f.idMietKauf = :idMietKauf", Flugzeugemietkauf.class)
            .setParameter("idMietKauf", FlugzeugID)
            .setParameter("bankKonto", BankKonto)
            .executeUpdate();
    if (updateCount > 0) {
      return true;
    } else {
      return false;
    }

  }

  public boolean ifUmbauLaeuft(int FgzID) {

    try {
      if (getEntityManager().createQuery("SELECT u from UmbauSitzplatz u WHERE u.idFlugzeugMietKauf = :fgzid", UmbauSitzplatz.class)
              .setParameter("fgzid", FgzID).getSingleResult() != null) {
        return true;
      } else {
        return false;
      }

    } catch (Exception e) {
      return false;
    }

  }

  public Flugzeugemietkauf getFlugzeugMietKauf(int FlugzeugID) {
    return getEntityManager().createQuery("select f from Flugzeugemietkauf f where f.idMietKauf = :flugzeugID", Flugzeugemietkauf.class)
            .setParameter("flugzeugID", FlugzeugID).getSingleResult();
  }

  public ViewFlugzeugVerbrauch getVerbrauch(int FlugzeugID) {
    try {
      return getEntityManager().createQuery("SELECT v from ViewFlugzeugVerbrauch v where v.idFlugzeug = :id", ViewFlugzeugVerbrauch.class)
              .setParameter("id", FlugzeugID)
              .getSingleResult();

    } catch (NoResultException e) {
      return null;
    }
  }

  /*
  ***************** Flugzeug Stock Bereich FTW-Hangar ENDE ***************************************************************
   */
 /*
  ***************** Flugzeuge zur Miete und Verkauf vom  FTW - Aircraft Stock BEGINN
   */
 /*
  ***************** MOD Tools
   */
  public int onFlugzeugUmsetzen(String Icao, int FlugzeugID) {
    return getEntityManager().createNativeQuery("update Flugzeuge_miet_kauf set aktuellePositionICAO = '" + Icao + "' where idMietKauf = " + FlugzeugID).executeUpdate();
  }

  public int onFlugzeugTankfuellung(int Tankfuellung, int FlugzeugID) {
    return getEntityManager().createNativeQuery("update Flugzeuge_miet_kauf set aktuelleTankfuellung = " + Tankfuellung + " where idMietKauf = " + FlugzeugID).executeUpdate();
  }

  public int onFlugzeugEinemUserZuordnen(int userid, int FlugzeugID) {
    return getEntityManager().createNativeQuery("update Flugzeuge_miet_kauf set idflugzeugBesitzer = " + userid + " where idMietKauf = " + FlugzeugID).executeUpdate();
  }

  public int onFlugzeugAirframeSetzen(int Airframe, int FlugzeugID) {
    return getEntityManager().createNativeQuery("update Flugzeuge_miet_kauf set gesamtAlterDesFlugzeugsMinuten = " + Airframe + " where idMietKauf = " + FlugzeugID).executeUpdate();
  }

  public int onFlugzeugAusDerLuftHolen(int FlugzeugID) {
    String Abfrage = "UPDATE Flugzeuge_miet_kauf  "
            + "SET  "
            + "    istGesperrt = FALSE, "
            + "    istInDerLuft = FALSE, "
            + "    idUserDerFlugzeugGesperrtHat = - 1, "
            + "    istGesperrtBis = NULL, "
            + "    istGesperrtSeit = NULL "
            + "WHERE "
            + "    idMietKauf = " + FlugzeugID;

    return getEntityManager().createNativeQuery(Abfrage).executeUpdate();
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
  ***************** MOD Tools ENDE
   */
 /*
  ***************** Lizenz und Typrating BEGINN
   */
  public UserTyperatings findTypeRating(int userid, String typerating) {
    try {
      return getEntityManager().createQuery("SELECT t FROM UserTyperatings t WHERE t.userID = :userid AND t.typeRating = :typerating", UserTyperatings.class)
              .setParameter("userid", userid)
              .setParameter("typerating", typerating)
              .setMaxResults(1)
              .getSingleResult();

    } catch (NoResultException e) {
      return null;
    }
  }

  public UserTyperatings findOffeneTypeRating(int userid) {
    try {
      return getEntityManager().createQuery("SELECT t FROM UserTyperatings t WHERE t.userID = :userid AND t.erfuellt = false", UserTyperatings.class)
              .setParameter("userid", userid)
              .setMaxResults(1)
              .getSingleResult();
    } catch (NoResultException e) {
      System.out.println("de.klees.beans.flugzeuge.FlugzeugFacade.findOffeneTypeRating()");
      return null;
    }
  }

  /*
  ***************** Lizenz und Typrating ENDE
   */
  public long CountAllMietkaufByID(int FlugzeugID) {
    return (long) getEntityManager().createQuery("Select count(a.idFlugzeug) FROM ViewFlugzeugeMietKauf a WHERE a.idFlugzeug = :FlugzeugID").setParameter("FlugzeugID", FlugzeugID).getSingleResult();
  }

  public List<ViewFlugzeugeMietKauf> findAllMietKauf() {
    return getEntityManager().createQuery("SELECT v from ViewFlugzeugeMietKauf v", ViewFlugzeugeMietKauf.class).getResultList();
  }

  /**
   *
   * @param FlugzeugTyp
   * @param icao
   * @return
   */
  public List<ViewFlugzeugeMietKauf> findAllMietKaufView(String FlugzeugTyp, String icao, String lizenz) {
    return getEntityManager().createQuery("SELECT v from ViewFlugzeugeMietKauf v where v.type LIKE :type and v.aktuellePositionICAO LIKE :icao and v.lizenz LIKE :lizenz", ViewFlugzeugeMietKauf.class)
            .setParameter("type", FlugzeugTyp)
            .setParameter("icao", icao)
            .setParameter("lizenz", lizenz)
            .getResultList();
  }

  public List<ViewFlugzeugeMietKauf> findMietbareFlugzeugeNachType(String FlugzeugTyp) {
    return getEntityManager().createQuery("SELECT v from ViewFlugzeugeMietKauf v where v.type LIKE :type AND v.istGesperrt = false and v.istMietbar = true and v.nurFuerAuftraegeDerFluggesellschaft = false and v.zustand > 95 and v.naechsterTerminCcheck >= current_date ", ViewFlugzeugeMietKauf.class)
            .setParameter("type", FlugzeugTyp)
            .getResultList();
  }

  public List<ViewFlugzeugeMietKauf> findAllMietKaufInDerLuft() {
    return getEntityManager().createQuery("SELECT v from ViewFlugzeugeMietKauf v where v.istInDerLuft = true ORDER BY v.istGesperrtBis ASC", ViewFlugzeugeMietKauf.class)
            .getResultList();
  }

  public List<ViewFlugzeugeMietKauf> findAlleMietKaufAmFlughafen(String icao) {
    return getEntityManager().createQuery("SELECT v FROM ViewFlugzeugeMietKauf v WHERE v.aktuellePositionICAO =:icao", ViewFlugzeugeMietKauf.class)
            .setParameter("icao", icao)
            .getResultList();
  }

  public List<ViewFlugzeugeMietKauf> findAlleMietKaufDerArt(String Art, String Type) {
    Type = "%" + Type + "%";
    return getEntityManager().createQuery("SELECT v FROM ViewFlugzeugeMietKauf v WHERE v.flugzeugArt = :art and v.type like :type ", ViewFlugzeugeMietKauf.class)
            .setParameter("art", Art)
            .setParameter("type", Type)
            .getResultList();
  }

  public List<ViewFlugzeugeMietKauf> findAllMietKaufViewFluggesellschaft(String FlugzeugTyp, int FluggesellschaftID, boolean Mieten, boolean Kaufen) {
    if (FluggesellschaftID == -300) {
      return getEntityManager().createQuery("SELECT f from ViewFlugzeugeMietKauf f where f.type like :fgtype AND f.idflugzeugBesitzer =:fgID ",
              ViewFlugzeugeMietKauf.class)
              .setParameter("fgtype", FlugzeugTyp)
              .setParameter("fgID", FluggesellschaftID)
              .getResultList();

    }

    return getEntityManager().createQuery("SELECT f from ViewFlugzeugeMietKauf f where f.type like :fgtype AND f.fluggesellschaftID =:fgID ",
            ViewFlugzeugeMietKauf.class)
            .setParameter("fgtype", FlugzeugTyp)
            .setParameter("fgID", FluggesellschaftID)
            .getResultList();
  }

  public List<ViewFlugzeuglog> getFlugzeugLog(int idMietkauf) {
    return getEntityManager().createQuery("SELECT f FROM ViewFlugzeuglog f WHERE f.idaircraft = :idmietkauf ORDER BY f.flugDatum DESC", ViewFlugzeuglog.class)
            .setParameter("idmietkauf", idMietkauf)
            .getResultList();
  }

  /**
   *
   * @param FlugzeugTyp
   * @return
   */
  public List<ViewFlugzeugeMietKauf> findByFlugzeugTyp(String FlugzeugTyp) {
    return getEntityManager().createQuery("SELECT v FROM ViewFlugzeugeMietKauf v WHERE v.type = :type ", ViewFlugzeugeMietKauf.class)
            .setParameter("type", FlugzeugTyp)
            .getResultList();
  }

  public List<ViewFlugzeugeMietKauf> findInProduktion(int flugzeugID) {
    return getEntityManager().createQuery("SELECT i from ViewFlugzeugeMietKauf i WHERE i.idFlugzeug = :flugzeugID ORDER BY i.registrierung", ViewFlugzeugeMietKauf.class)
            .setParameter("flugzeugID", flugzeugID)
            .getResultList();
  }

  public void onDeleteFlugeugInBetrieb(int FlugzeugID) {
    getEntityManager().createNativeQuery("DELETE FROM Flugzeuge_miet_kauf where idMietKauf = " + FlugzeugID).executeUpdate();
  }

  public boolean ifUserFlugzeugMeilenGroesserNull(int fgzID) {
    if (getEntityManager().createQuery("SELECT k from ViewFlugzeugeMietKauf k where k.idMietKauf = :fgzid", ViewFlugzeugeMietKauf.class)
            .setParameter("fgzid", fgzID)
            .getSingleResult().getGesamtEntfernung() > 0) {
      return true;
    } else {
      return false;
    }
  }

  public boolean ifExistKennung(String Kennung) {
    if (getEntityManager().createQuery("SELECT k.registrierung from ViewFlugzeugeMietKauf k where k.registrierung = :kennung", ViewFlugzeugeMietKauf.class)
            .setParameter("kennung", Kennung)
            .getResultList().size() > 0) {
      return true;
    } else {
      return false;
    }
  }

  public void onDeletePilotenZuweisungen(int flzid) {
    getEntityManager().createQuery("DELETE FROM FlugzeugeErlaubteUser f where f.idFlugzeugeMietKauf = :flzid", FlugzeugeErlaubteUser.class)
            .setParameter("flzid", flzid)
            .executeUpdate();
  }

  /*
  ***************** Flugzeuge zur Miete und Verkauf vom  FTW - Aircraft Stock ENDE
   */
 /*
  ***************** Flugzeuge Mieten BEGINN
   */
  public void onFlugzeugMieteSpeichern(Flugzeugemietkauf entity) {
    getEntityManager().merge(entity);
  }

  public boolean onFlugzeugRueckgabe(int FlugzeugID) {

    int updateCount = getEntityManager().createQuery("UPDATE Flugzeugemietkauf f SET f.istGesperrt = 0, f.idUserDerFlugzeugGesperrtHat = -1 "
            + "WHERE f.idMietKauf = :idMietKauf", Flugzeugemietkauf.class).setParameter("idMietKauf", FlugzeugID).executeUpdate();
    if (updateCount > 0) {
      return true;
    } else {
      return false;
    }

  }

  public List<ViewFlugzeugeMietKauf> findeMeinGemietetesFlugzeug(int UserID) {
    return getEntityManager().createQuery("SELECT v FROM ViewFlugzeugeMietKauf v where v.idUserDerFlugzeugGesperrtHat = :idUserDerFlugzeugGesperrtHat", ViewFlugzeugeMietKauf.class)
            .setParameter("idUserDerFlugzeugGesperrtHat", UserID)
            .getResultList();
  }

  public boolean hatBenutzerEinFlugzeugGemietet(int UserID) {
    try {
      if (getEntityManager().createQuery("SELECT v FROM ViewFlugzeugeMietKauf v WHERE v.idUserDerFlugzeugGesperrtHat = :userID", ViewFlugzeugeMietKauf.class)
              .setParameter("userID", UserID)
              .getResultList().size() > 0) {
        return true;
      }
    } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
      return false;
    }
    return false;
  }

  public boolean istBenutzerBesitzerDesFlugzeuges(int FlugZeugID, int UserID) {
    try {
      List<Flugzeugemietkauf> flugzeuge = getEntityManager().createQuery("SELECT f FROM Flugzeugemietkauf f WHERE f.idMietKauf= :idMietKauf and f.idflugzeugBesitzer = :idflugzeugBesitzer", Flugzeugemietkauf.class)
              .setParameter("idMietKauf", FlugZeugID)
              .setParameter("idflugzeugBesitzer", UserID)
              .getResultList();

      if (flugzeuge.size() > 0) {
        return true;
      } else {
        return false;
      }
    } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
      //System.out.println(e.getMessage());
      return false;
    }
  }

  public boolean ifExistErlaubtListe(int fzid) {

    try {
      if (getEntityManager().createQuery("SELECT l FROM FlugzeugeErlaubteUser l WHERE l.idFlugzeugeMietKauf = :fzid", FlugzeugeErlaubteUser.class)
              .setParameter("fzid", fzid)
              .setMaxResults(1)
              .getSingleResult().getIdFlugzeugeMietKauf() == fzid) {

        return true;
      }

    } catch (NoResultException e) {
      return false;
    }

    return false;
  }

  public boolean ifUserInErlaubtListe(int userid, int fzid) {

    try {
      if (getEntityManager().createQuery("SELECT l FROM FlugzeugeErlaubteUser l WHERE l.idFlugzeugeMietKauf = :fzid and l.idUser = :userid", FlugzeugeErlaubteUser.class)
              .setParameter("fzid", fzid)
              .setParameter("userid", userid)
              .setMaxResults(1)
              .getSingleResult().getIdUser() == userid) {
        return true;
      }

    } catch (NoResultException e) {
      return false;
    }

    return false;
  }

  /*
  ***************** Flugzeuge Mieten ENDE 
   */
 /*
  ***************** Flugzeuge Blacklist BEGINN
   */
  public void createBlacklistEintrag(Flugzeugblacklist entity) {
    getEntityManager().persist(entity);
  }

  public void removeBlacklistEintrag(Flugzeugblacklist entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public List<Flugzeugblacklist> getBlackList(int MietKaufID) {
    return getEntityManager().createQuery("SELECT b FROM Flugzeugblacklist b WHERE b.flugzeugmietkaufid =:mietkaufid ORDER BY b.username", Flugzeugblacklist.class)
            .setParameter("mietkaufid", MietKaufID)
            .getResultList();
  }

  public boolean ifUserInBlacklist(int userid, int MietKaufID) {

    try {
      if (getEntityManager().createQuery("SELECT b FROM Flugzeugblacklist b WHERE b.userid =:userid AND b.flugzeugmietkaufid = :mietkaufid", Flugzeugblacklist.class)
              .setParameter("userid", userid)
              .setParameter("mietkaufid", MietKaufID)
              .setMaxResults(1)
              .getSingleResult().getUserid() == userid) {
        return true;
      }

    } catch (NoResultException e) {
      return false;
    }

    return false;
  }

  public void blackListAlleEintraegeEntfernen(int MietkaufID) {
    getEntityManager().createQuery("DELETE FROM Flugzeugblacklist b WHERE b.flugzeugmietkaufid =:mietkaufid", Flugzeugblacklist.class)
            .setParameter("mietkaufid", MietkaufID)
            .executeUpdate();
  }

  /*
  ***************** Flugzeuge Blacklist ENDE
   */
 /*
  ***************** Flugzeuge Kauf / Verkauf Begin 
   */
  public Flugzeuge readFlugzeug(int id) {
    try {
      return getEntityManager().createQuery("SELECT f FROM Flugzeuge f where f.idFlugzeug = :id", Flugzeuge.class)
              .setParameter("id", id)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public boolean onFlugzeugKaufen(int FlugzeugID, int UserID) {

    int updateCount = getEntityManager().createQuery("UPDATE Flugzeugemietkauf f SET f.istZuVerkaufen=0, f.istMietbar=0, f.idflugzeugBesitzer = " + UserID
            + "  WHERE f.idMietKauf = :idMietKauf", Flugzeugemietkauf.class).setParameter("idMietKauf", FlugzeugID).executeUpdate();
    if (updateCount > 0) {
      return true;
    } else {
      return false;
    }
  }

  public List<ViewFlugzeugeMietKauf> findeMeineGekaufteFlugzeuge(int UserID) {
    return getEntityManager().createQuery("SELECT v FROM ViewFlugzeugeMietKauf v WHERE v.userID = :userID or v.leasingAnUserID =:userID", ViewFlugzeugeMietKauf.class)
            .setParameter("userID", UserID).getResultList();
  }

  public String getBenutzerNameMieter(int userID) {
    try {
      return getEntityManager().createQuery("SELECT u from Benutzer u where u.idUser =:userID", Benutzer.class).setParameter("userID", userID).getSingleResult().getName1();
    } catch (NoResultException e) {
      return "";
    }

  }

  public boolean istMieterInDerFluggesellschaft(int fgid, int userid) {

    try {
      getEntityManager().createQuery("SELECT f FROM FlugesellschaftPiloten f WHERE f.idflugesellschaft = :fgid AND f.iduser = :userid", FlugesellschaftPiloten.class)
              .setParameter("fgid", fgid)
              .setParameter("userid", userid)
              .getSingleResult();
      return true;
    } catch (NoResultException e) {
      return false;
    }
  }

  /*
  ***************** Flugzeuge Kauf / Verkauf Ende 
   */

 /*
  ***************** Flugzeugtransfer Fluggesellschaften Beginn 
   */
//  @SuppressWarnings("unchecked")
//  public List<String> MeineFluggesellschaften(int userID) {
//    return getEntityManager().createQuery("SELECT DISTINCT f.idFluggesellschaft, f.name from Fluggesellschaft f where f.userid = :userID")
//            .setParameter("userID", userID)
//            .getResultList();
//  }
  @SuppressWarnings("unchecked")
  public List<String> MeineFluggesellschaften(int userID, int fgid) {
    return getEntityManager().createQuery("SELECT DISTINCT f.idFluggesellschaft, f.name from Fluggesellschaft f where f.userid = :userID OR f.idFluggesellschaft = :fgid")
            .setParameter("userID", userID)
            .setParameter("fgid", fgid)
            .getResultList();
  }

  public Fluggesellschaft datenFluggesellschaft(int idFluggesellschaft) {
    try {
      return getEntityManager().createQuery("SELECT f from Fluggesellschaft f where f.idFluggesellschaft = :fluggesellschaftID", Fluggesellschaft.class)
              .setParameter("fluggesellschaftID", idFluggesellschaft)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public Flugzeugemietkauf meinFlugzeug(int FlugzeugID) {
    return getEntityManager().createQuery("SELECT f from Flugzeugemietkauf f where f.idMietKauf = :flugzeugID", Flugzeugemietkauf.class)
            .setParameter("flugzeugID", FlugzeugID)
            .getSingleResult();
  }

  @SuppressWarnings("unchecked")
  public List<String> findFlugzeugModelleForFG(int FluggesellschaftID) {
    return getEntityManager().createQuery("SELECT DISTINCT f.type FROM ViewFlugzeugeMietKauf f WHERE f.fluggesellschaftID =:fgid ORDER BY f.type ")
            .setParameter("fgid", FluggesellschaftID)
            .getResultList();
  }

  /*
  ***************** Flugzeugtransfer Fluggesellschaften Ende
   */
 /*
  ***************** Flugzeug-Sitzkonfiguration Beginn 
   */
  public List<Sitzkonfiguration> getSitzKonfiguration(int FlugzeugID) {
    return getEntityManager().createQuery("SELECT k from Sitzkonfiguration k WHERE k.idFlugzeug = :flugzeugid", Sitzkonfiguration.class)
            .setParameter("flugzeugid", FlugzeugID)
            .getResultList();
  }

  public void SitzkonfigurationSpeichern(Sitzkonfiguration entity) {
    getEntityManager().merge(entity);
  }

  public void SitzkonfigurationNeu(Sitzkonfiguration entity) {
    getEntityManager().persist(entity);
  }

  public void SitzkonfigurationLoeschen(Sitzkonfiguration entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public Sitzkonfiguration getKonfig(int id) {
    try {
      return getEntityManager().createQuery("SELECT k FROM Sitzkonfiguration k WHERE k.idsitzKonfiguration =:id", Sitzkonfiguration.class)
              .setParameter("id", id)
              .getSingleResult();

    } catch (NoResultException e) {
      return null;
    }
  }


  /*
  ***************** Flugzeug-Sitzkonfiguration ENDE
   */
  public Airport getAirportInfo(String icao) {
    try {
      return getEntityManager().createQuery("SELECT a FROM Airport a WHERE a.icao = :icao", Airport.class)
              .setParameter("icao", icao)
              .getSingleResult();

    } catch (NoResultException e) {
      return null;
    }
  }

  public void onFlugzeugTransfer(Flugzeugtransfer entity) {
    getEntityManager().persist(entity);
  }

  /*
  Service Hangar BEGINN
   */
  public List<ViewFBOUserObjekte> getServiceHangars(String icao) {
    return getEntityManager().createQuery("SELECT s FROM ViewFBOUserObjekte s WHERE s.icao = :icao and s.servicehangar=true", ViewFBOUserObjekte.class)
            .setParameter("icao", icao)
            .getResultList();
  }

  public List<Lagerservicehangar> getPakete(String type, int hangarID) {
    return getEntityManager().createQuery("SELECT p FROM Lagerservicehangar p WHERE p.paketname = :type AND p.idfbouserobjekt = :id AND p.menge > 0", Lagerservicehangar.class)
            .setParameter("type", type)
            .setParameter("id", hangarID)
            .getResultList();
  }

  public Lagerservicehangar readPaket(String type, int hangarID, int Art) {

    if (Art == -1) {
      try {
        return getEntityManager().createQuery("SELECT p FROM Lagerservicehangar p WHERE p.paketname = :type AND p.idlagerservicehangar = :id ", Lagerservicehangar.class)
                .setParameter("type", type)
                .setParameter("id", hangarID)
                .getSingleResult();
      } catch (NoResultException e) {
      }
    } else {
      try {
        return getEntityManager().createQuery("SELECT p FROM Lagerservicehangar p WHERE p.paketname = :type AND p.idfbouserobjekt = :id AND p.paketart = :art", Lagerservicehangar.class)
                .setParameter("type", type)
                .setParameter("id", hangarID)
                .setParameter("art", Art)
                .getSingleResult();
      } catch (NoResultException e) {
        return null;
      }
    }
    return null;
  }

  public boolean checkAufMaterial(String icao, String type) {
    try {
      if (getEntityManager().createNativeQuery("Select lagerservicehangar.paketname from lagerservicehangar, fboUserObjekte where "
              + "lagerservicehangar.idfbouserobjekt = fboUserObjekte.iduserfbo and fboUserObjekte.icao = '" + icao + "' "
              + "and lagerservicehangar.paketname = '" + type + "' and lagerservicehangar.menge > 0 ").getResultList().size() > 0) {
        return true;
      }
    } catch (NoResultException e) {
      return false;
    }
    return false;
  }

  public FboUserObjekte readServiceHangar(int HangarID) {
    try {
      return getEntityManager().createQuery("SELECT f FROM FboUserObjekte f WHERE f.iduserfbo = :HangarID", FboUserObjekte.class)
              .setParameter("HangarID", HangarID)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public ViewFBOUserObjekte readViewServiceHangar(int HangarID) {
    try {
      return getEntityManager().createQuery("SELECT f FROM ViewFBOUserObjekte f WHERE f.iduserfbo = :HangarID", ViewFBOUserObjekte.class)
              .setParameter("HangarID", HangarID)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }

  }

  public List<Hangarbelegung> getHangarbelegung(int shid) {
    return getEntityManager().createQuery("SELECT h from Hangarbelegung h WHERE h.iduserfboid = :shid", Hangarbelegung.class)
            .setParameter("shid", shid)
            .getResultList();
  }

  public void newHangarbelegung(Hangarbelegung entity) {
    getEntityManager().persist(entity);
  }

  public void neuerUmsatzVerbuchen(Lagerservicehangarumsatz entity) {
    getEntityManager().persist(entity);
  }

  public List<Lagerservicehangarumsatz> getArtikelUmsatz(int id) {
    return getEntityManager().createQuery("SELECT a FROM Lagerservicehangarumsatz a WHERE a.idlagerservicehangar = :id", Lagerservicehangarumsatz.class)
            .setParameter("id", id)
            .getResultList();
  }

  public void editHangarLagerBestandVerbuchen(Lagerservicehangar entity) {
    getEntityManager().merge(entity);
  }

  public void neuerHangarTermin(Servicehangartermine entity) {
    getEntityManager().persist(entity);
  }

  /*
  Service Hangar ENDE
   */
 /*
  Flugzeugboerse BEGINN
   */
  public List<Flugzeugboerse> getAnzeigen(int userID) {
    return getEntityManager().createQuery("SELECT a FROM Flugzeugboerse a WHERE a.iduser = :userid", Flugzeugboerse.class)
            .setParameter("userid", userID)
            .getResultList();
  }

  public List<Flugzeugboerse> getAnzeigenFilter(int userID, boolean aktiv) {
    return getEntityManager().createQuery("SELECT a FROM Flugzeugboerse a WHERE a.iduser = :userid and a.freigabe = :aktiv", Flugzeugboerse.class)
            .setParameter("userid", userID)
            .setParameter("aktiv", aktiv)
            .getResultList();
  }

  public List<Flugzeugboerse> getAngebote(int AngebotArt, String type) {

    type = "%" + type + "%";

    switch (AngebotArt) {
      case -1:
        return getEntityManager().createQuery("SELECT a FROM Flugzeugboerse a WHERE a.freigabe=true and a.flugzeugtyp like :type ", Flugzeugboerse.class)
                .setParameter("type", type)
                .getResultList();
      case 1:
        return getEntityManager().createQuery("SELECT a FROM Flugzeugboerse a WHERE a.verkauf=TRUE AND a.freigabe=true and a.flugzeugtyp like :type ", Flugzeugboerse.class)
                .setParameter("type", type)
                .getResultList();
      case 2:
        return getEntityManager().createQuery("SELECT a FROM Flugzeugboerse a WHERE a.leasing=TRUE AND a.freigabe=true and a.flugzeugtyp like :type ", Flugzeugboerse.class)
                .setParameter("type", type)
                .getResultList();
      case 3:
        return getEntityManager().createQuery("SELECT a FROM Flugzeugboerse a WHERE a.finanzierung=TRUE AND a.freigabe=true and a.flugzeugtyp like :type ", Flugzeugboerse.class)
                .setParameter("type", type)
                .getResultList();
      default:
        break;
    }
    return null;
  }

  public List<Flugzeugboersetemplates> getVorlagenUser(int userid) {
    return getEntityManager().createQuery("SELECT v FROM Flugzeugboersetemplates v WHERE v.iduser =:iduser", Flugzeugboersetemplates.class)
            .setParameter("iduser", userid)
            .getResultList();
  }

  public List<Flugzeugboersetemplates> getVorlagenGeteilt() {
    return getEntityManager().createQuery("SELECT v FROM Flugzeugboersetemplates v WHERE v.tausch = true", Flugzeugboersetemplates.class)
            .getResultList();
  }

  public List<Flugzeugboerse> getAnzeigenFlugzeuge() {
    return getEntityManager().createQuery("SELECT a FROM Flugzeugboerse a WHERE a.freigabe=true GROUP BY a.flugzeugtyp ORDER BY a.flugzeugtyp", Flugzeugboerse.class)
            .getResultList();
  }

  public void neueAnzeige(Flugzeugboerse entity) {
    getEntityManager().persist(entity);
  }

  public void neueVorlage(Flugzeugboersetemplates entity) {
    getEntityManager().persist(entity);
  }

  public void editAnzeige(Flugzeugboerse entity) {
    getEntityManager().merge(entity);
  }

  public void editVorlage(Flugzeugboersetemplates entity) {
    getEntityManager().merge(entity);
  }

  public void removeAnzeige(Flugzeugboerse entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public void removeVorlage(Flugzeugboersetemplates entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public List<ViewFlugzeugeMietKauf> getUserFlugzeuge(int userid) {
    return getEntityManager().createQuery("SELECT f from ViewFlugzeugeMietKauf f where f.idflugzeugBesitzer = :userid", ViewFlugzeugeMietKauf.class)
            .setParameter("userid", userid)
            .getResultList();
  }

  public ViewFlugzeugeMietKauf readFlugzeugMietKauf(int fgzid) {
    try {
      return getEntityManager().createQuery("SELECT f FROM ViewFlugzeugeMietKauf f WHERE f.idMietKauf = :fgzid", ViewFlugzeugeMietKauf.class)
              .setParameter("fgzid", fgzid)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }

  }

  /*
  Flugzeugboerse ENDE
   */
 /*
  Detail Suche BEGINN
   */
  @SuppressWarnings("unchecked")
  public List<String> getUser() {
    return getEntityManager().createQuery("SELECT u.idUser, u.name1 FROM Benutzer u where u.isActive=true AND u.idUser > 0  ORDER BY u.name1").getResultList();
  }

  public List<ViewFlugzeugeMietKauf> getFlugzeugeUeberKennung(String Kennung) {
    String Suche = "%" + Kennung + "%";
    return getEntityManager().createQuery("SELECT f FROM ViewFlugzeugeMietKauf f WHERE f.registrierung LIKE :kennung ORDER BY f.registrierung", ViewFlugzeugeMietKauf.class)
            .setParameter("kennung", Suche)
            .getResultList();
  }

  public List<ViewFlugzeugeMietKauf> getFlugzeugeUeberID(int id) {
    return getEntityManager().createQuery("SELECT f FROM ViewFlugzeugeMietKauf f WHERE f.idMietKauf = :id", ViewFlugzeugeMietKauf.class)
            .setParameter("id", id)
            .getResultList();
  }

  public List<ViewFlugzeugeMietKauf> getFlugzeugeUeberKlasse(int id) {
    return getEntityManager().createQuery("SELECT f FROM ViewFlugzeugeMietKauf f WHERE f.flugzeugklasse = :id GROUP BY f.type", ViewFlugzeugeMietKauf.class)
            .setParameter("id", id)
            .getResultList();
  }

  
  public List<ViewFlugzeugeMietKauf> getFlugzeugeUeberUserID(int id) {
    return getEntityManager().createQuery("SELECT f FROM ViewFlugzeugeMietKauf f WHERE f.idflugzeugBesitzer = :id", ViewFlugzeugeMietKauf.class)
            .setParameter("id", id)
            .getResultList();
  }

  /*
  Detail Suche ENDE
   */
 /*
  ***************** Banktransaktionen Beginn
   */
  public double getBankSaldo(String BankKonto) {
    return (double) getEntityManager().createNamedQuery("Bank.Banksaldo").setParameter("bankKonto", BankKonto).getSingleResult();
  }

  public double getErweiterterBankSaldo(int UserID) {

    double Ergebnis = 0.0;

    // Fluggesellschaft abfragen
    String SqlAbfrage = "SELECT SUM(betrag) AS tmp_betrag FROM Fluggesellschaft AS Fluggesellschaft, benutzer AS benutzer, bank AS bank WHERE `Fluggesellschaft`.`userid` = `benutzer`.`idUser` AND `bank`.`bankKonto` = `Fluggesellschaft`.`bankKonto` "
            + " AND benutzer.idUser = " + UserID + " GROUP BY `benutzer`.`name1`";

    try {
      Ergebnis = (double) getEntityManager().createNativeQuery(SqlAbfrage).getSingleResult();
    } catch (NoResultException e) {
      Ergebnis = 0.0;
    }

    // Benutzer abfragen
    SqlAbfrage = "SELECT SUM(`bank`.`betrag`) AS tmp_betrag1 FROM `bank` AS `bank`, `benutzer` AS `benutzer` WHERE `bank`.`bankKonto` = `benutzer`.`bankKonto` "
            + "AND benutzer.idUser = " + UserID + " GROUP BY `benutzer`.`name1`";

    try {
      Ergebnis = Ergebnis + (double) getEntityManager().createNativeQuery(SqlAbfrage).getSingleResult();
    } catch (NoResultException e) {
      Ergebnis = Ergebnis + 0.0;
    }

    return Ergebnis;
  }

  public double getBankKonto(int KontoID) {
    return (double) getEntityManager().createNamedQuery("Bank.Banksaldo").setParameter("userID", KontoID).getSingleResult();
  }

  public String getBankKontoName(String BankKonto) {
    try {
      return getEntityManager().createQuery("SELECT b from Bank b where b.bankKonto = :bankKonto", Bank.class)
              .setParameter("bankKonto", BankKonto)
              .setMaxResults(1)
              .getSingleResult().getKontoName();
    } catch (NoResultException e) {
      return "";
    }
  }

  /*
  ***************** Banktransaktionen Ende
   */
  public List<Kostenstellen> getKostenstellen(int UserID, int besitzerid) {
    return getEntityManager().createQuery("SELECT k from Kostenstellen k where k.userid = :userid or k.userid = :besitzerid ORDER BY k.kostenstelle", Kostenstellen.class)
            .setParameter("userid", UserID)
            .setParameter("besitzerid", besitzerid)
            .getResultList();
  }

  public String getUserName(int id) {

    try {
      return getEntityManager().createQuery("SELECT u FROM Benutzer u WHERE u.idUser = :id", Benutzer.class)
              .setParameter("id", id)
              .getSingleResult().getName1();

    } catch (NoResultException e) {
      return "";
    }
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

  public Fluggesellschaft readFG(int fgid) {
    try {
      return getEntityManager().createQuery("SELECT f from Fluggesellschaft f WHERE f.idFluggesellschaft = :fgid", Fluggesellschaft.class)
              .setParameter("fgid", fgid)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public List<Fluggesellschaft> getAllFGKonten(int userID, int fguserID) {
    return getEntityManager().createQuery("SELECT f FROM Fluggesellschaft f WHERE f.userid = :userid OR f.userid = :fguserid ", Fluggesellschaft.class)
            .setParameter("userid", userID)
            .setParameter("fguserid", fguserID)
            .getResultList();
  }

  public Yaacarskopf readYAACARSFlight(int UserID) {
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

  public void saveUserMail(Mail entity) {
    getEntityManager().persist(entity);
  }

  public Benutzer findUser(String Suchtext) {
    return getEntityManager().createQuery("SELECT u FROM Benutzer u where u.name1 = :UserName", Benutzer.class)
            .setParameter("UserName", Suchtext)
            .getSingleResult();
  }

  public List<Benutzer> getUserList() {
    return getEntityManager().createQuery("SELECT u FROM Benutzer u where u.idUser > 0 ORDER BY u.name1", Benutzer.class).getResultList();
  }

}
