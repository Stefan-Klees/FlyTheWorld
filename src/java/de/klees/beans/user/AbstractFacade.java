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

package de.klees.beans.user;

import de.klees.data.ArcasUser;
import de.klees.data.Bank;
import de.klees.data.FboUserObjekte;
import de.klees.data.Feinabstimmung;
import de.klees.data.Fluggesellschaft;
import de.klees.data.Mail;
import de.klees.data.Systemmeldung;
import de.klees.data.Benutzer;
import de.klees.data.UserFavoriten;
import de.klees.data.UserTyperatings;
import de.klees.data.WartungsTabelle;
import de.klees.data.Yaacarskopf;
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
  private Benutzer selectedUser;

  public AbstractFacade(Class<T> entityClass) {
    this.entityClass = entityClass;
  }

  protected abstract EntityManager getEntityManager();

  public void create(T entity) {
    getEntityManager().persist(entity);
  }

  public void createTyperating(UserTyperatings entity) {
    getEntityManager().persist(entity);
  }

  public void removeTyperating(UserTyperatings entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public void removeUserFavorit(UserFavoriten entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public void edit(T entity) {
    getEntityManager().merge(entity);
  }

  public void remove(T entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public int deleteUser(int id) {
    return getEntityManager().createQuery("DELETE FROM User u WHERE u.idUser = :id")
            .setParameter("id", id)
            .executeUpdate();
  }

  public List<UserTyperatings> findAllTypratingByUserID(int userid) {
    return getEntityManager().createQuery("SELECT t FROM UserTyperatings t WHERE t.userID = :userid", UserTyperatings.class)
            .setParameter("userid", userid)
            .getResultList();
  }

  public List<UserFavoriten> UserFavoriten(int userid) {
    return getEntityManager().createQuery("SELECT f FROM UserFavoriten f WHERE f.iduser = :userid ORDER BY f.icao", UserFavoriten.class)
            .setParameter("userid", userid)
            .getResultList();
  }

  
  public List<Benutzer> findAll(String suchText) {
    System.out.println("de.klees.beans.user.AbstractFacade.findAll() " + suchText);
    return getEntityManager().createQuery("SELECT u FROM Benutzer u where u.name1 like :name",Benutzer.class)
            .setParameter("name", suchText)
            .getResultList();
  }

  public List<Benutzer> getAllUsers() {
    return getEntityManager().createQuery("SELECT u FROM Benutzer u", Benutzer.class).getResultList();
  }

 public List<Benutzer> getCreateAndLastloginGleich() {
    return getEntityManager().createQuery("SELECT u FROM Benutzer u where u.lastlogon = u.created and u.idUser > 0 ORDER BY u.created", Benutzer.class).getResultList();
  }

 @SuppressWarnings("unchecked")
 public List<Benutzer> getLastloginPruefung2() {
    return getEntityManager().createNativeQuery("SELECT * FROM User  where datediff(lastlogon, created) <= 14 and idUser > 0 ORDER BY created", Benutzer.class).getResultList();
  }
  
  
  public boolean ifExistReadKey(String Key) {
    try {
      if (getEntityManager().createQuery("SELECT u from Benutzer u where u.readaccesskey = :key", Benutzer.class).setParameter("key", Key).getSingleResult().getReadaccesskey().length() > 0) {
        return true;
      }
    } catch (NoResultException e) {
      return false;
    }
    return false;
  }

  public boolean ifExistWriteKey(String Key) {
    try {
      if (getEntityManager().createQuery("SELECT u from Benutzer u where u.writeaccesskey = :key", Benutzer.class).setParameter("key", Key).getSingleResult().getWriteaccesskey().length() > 0) {
        return true;
      }
    } catch (NoResultException e) {
      return false;
    }
    return false;
  }

  public Benutzer findUser(String Suchtext) {
    return getEntityManager().createQuery("SELECT u FROM Benutzer u where u.name1 = :UserName", Benutzer.class)
            .setParameter("UserName", Suchtext)
            .getSingleResult();
  }

  public Benutzer findUserMail(String Suchtext) {

    return getEntityManager().createQuery("SELECT u FROM Benutzer u where u.name1 = :UserName", Benutzer.class)
            .setParameter("UserName", Suchtext)
            .getSingleResult();
  }

  public boolean ifExistUser(String name) {

    try {
      Benutzer user = getEntityManager().createQuery("SELECT u FROM Benutzer u WHERE u.name1 = :name", Benutzer.class)
              .setParameter("name", name)
              .getSingleResult();

      if (user != null) {
        return true;
      }

    } catch (NoResultException e) {
      return false;
    }

    return false;
  }

  public Benutzer findUserById(Integer ID) {
    return getEntityManager().createQuery("SELECT u FROM Benutzer u WHERE u.idUser = :idUser", Benutzer.class)
            .setParameter("idUser", ID)
            .getSingleResult();
  }

  public ArcasUser findAcarsUserByName(String UserName) {

    try {
      return getEntityManager().createNamedQuery("ArcasUser.findByUsername", ArcasUser.class).setParameter("username", UserName).getSingleResult();
    } catch (Exception e) {
      return null;
    }

  }

  /*
  ************** Bank Beginn
   */
  /**
   *
   * @param Bankkonto
   * @return
   */
  public double BankSaldo(String Bankkonto) {
    try {
      return (double) getEntityManager().createNamedQuery("Bank.Banksaldo").setParameter("bankKonto", Bankkonto).getSingleResult();
    } catch (Exception e) {
      return 0.0;
    }

  }

  /**
   *
   * @param Bankkonto
   */
  public void createBankkonto(Bank Bankkonto) {
    getEntityManager().persist(Bankkonto);
  }

  /**
   *
   * @param Kontonummer
   * @return
   */
  public boolean ifExistBankKonto(String Kontonummer) {
    try {
      if (getEntityManager().createQuery("SELECT b from Bank b where b.bankKonto = :konto", Bank.class)
              .setParameter("konto", Kontonummer)
              .getResultList().get(0).getBankKonto().equals(Kontonummer))
              {
                System.out.println("de.klees.beans.user.AbstractFacade.ifExistBankKonto() Konto Existiert schon : " + Kontonummer);
        return true;
      } else {
        return false;
      }
    } catch (Exception e) {
      return false;
    }
  }

  public String getFluggesellschaftName(int fgid) {
    try {
      return getEntityManager().createQuery("SELECT f from Fluggesellschaft f WHERE f.idFluggesellschaft = :fgid", Fluggesellschaft.class)
              .setParameter("fgid", fgid)
              .getSingleResult().getName();
    } catch (NoResultException e) {
      return "Keine Managerfunktion";
    }
  }

  public List<Fluggesellschaft> getFluggesellschaften(int userid) {
    return getEntityManager().createQuery("SELECT f from Fluggesellschaft f where f.userid = :userid", Fluggesellschaft.class)
            .setParameter("userid", userid)
            .getResultList();
  }

  public List<FboUserObjekte> getUserFBOs(int userid) {
    return getEntityManager().createQuery("SELECT f from FboUserObjekte f where f.idUser = :userid", FboUserObjekte.class)
            .setParameter("userid", userid)
            .getResultList();
  }

  public int onManagerKuendigen(int fgid, int userid) {
    return getEntityManager().createQuery("DELETE FROM Fluggesellschaftmanager AS f WHERE f.fluggesellschaftid = :fgid and f.userid = :userid")
            .setParameter("fgid", fgid)
            .setParameter("userid", userid)
            .executeUpdate();
  }

  public Integer BankKontoLoeschen(String Kontonummer) {
    return getEntityManager().createNativeQuery("delete from bank where bankKonto = '" + Kontonummer + "'").executeUpdate();
  }

  public Integer RoutenLoeschen(int id) {
    return getEntityManager().createNativeQuery("delete FROM WorldOfFly.Flugrouten where idFbo = " + id).executeUpdate();
  }

  public Integer FGPilotenLoeschen(int fgid) {
    return getEntityManager().createNativeQuery("delete FROM flugesellschaft_piloten where idflugesellschaft = " + fgid).executeUpdate();
  }

  public Integer ZugeordnetePilotenAusFlugzeugenLoeschen(int fgid) {
    return getEntityManager().createNativeQuery("delete FROM Flugzeuge_ErlaubteUser where idFluggesellschaft = " + fgid).executeUpdate();
  }

  public Integer FluggesellschaftLoeschen(int userid) {
    return getEntityManager().createNativeQuery("delete from Fluggesellschaft where userid = " + userid).executeUpdate();
  }

  public Integer FlugzeugeZurueckAnSystem(int userid) {
    return getEntityManager().createNativeQuery("update Flugzeuge_miet_kauf set idflugzeugBesitzer=-300,istMietbar=false,istZuVerkaufen=false,"
            + "aktuellePositionICAO='LFSF',bankkontoBesitzer='500-1000002',kostenstelle=-1,nurFuerAuftraegeDerFluggesellschaft=false"
            + ",idSitzKonfiguration=-1,eigenesBildURL='',istCheckDurchUserErlaubt=false where idflugzeugBesitzer = " + userid).executeUpdate();
  }

  public Integer mailsLoeschen(int userid) {
    return getEntityManager().createNativeQuery("delete from mail where userID =" + userid).executeUpdate();
  }

  public Integer fboLoeschen(int userid) {
    return getEntityManager().createNativeQuery("delete from fboUserObjekte where idUser =" + userid).executeUpdate();
  }

  public Integer TypeRatingsLoeschen(int userid) {
    return getEntityManager().createNativeQuery("delete from UserTyperatings where userID =" + userid).executeUpdate();
  }

  public Integer ManagerEntlassen(int fgid) {
    return getEntityManager().createNativeQuery("update benutzer set fluggesellschaftManagerID = -1 where fluggesellschaftManagerID = " + fgid).executeUpdate();
  }

  public Integer FluglogbuchAnpassen(int userid) {
    return getEntityManager().createNativeQuery("update fluglogbuch set iduser=-150 where iduser = " + userid).executeUpdate();
  }

  // 0
  /*
  ************** Bank Ende
   */
  public Systemmeldung getSystemMeldung() {
    return getEntityManager().createQuery("SELECT s FROM Systemmeldung s", Systemmeldung.class).setMaxResults(1).getSingleResult();
  }

  public List<WartungsTabelle> readWartungsTabelle() {
    return getEntityManager().createQuery("SELECT w FROM WartungsTabelle w ORDER BY w.datumUhrzeit DESC", WartungsTabelle.class)
            .setMaxResults(60)
            .getResultList();
  }

  public Feinabstimmung readConfig() {
    return getEntityManager().createQuery("SELECT c FROM Feinabstimmung c", Feinabstimmung.class).setMaxResults(1).getSingleResult();
  }

  /*
  ***************** Lizenz und Typrating BEGINN
   */
  public void saveTyperating(UserTyperatings entity) {
    getEntityManager().persist(entity);
  }

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
      return null;
    }
  }

  /*
  ***************** Lizenz und Typrating ENDE
   */
 /*
  ***************** YAACARS Beginn
   */
  public Yaacarskopf getYAACARSFlight(int id) {
    try {
      return getEntityManager().createQuery("SELECT y FROM Yaacarskopf y WHERE y.userid = :id", Yaacarskopf.class)
              .setParameter("id", id)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public void saveYAACARSKopf(Yaacarskopf entity) {
    getEntityManager().merge(entity);
  }

  /*
  ***************** YAACARS ENDE
   */

 /*
  ************************ DOB Banking BEGINN
   */
  public double getErweiterterBankSaldo(int UserID) {

    double Ergebnis = 0.0;

    // Fluggesellschaft abfragen
    String SqlAbfrage = "SELECT SUM(betrag) AS tmp_betrag FROM Fluggesellschaft AS Fluggesellschaft, User AS User, bank AS bank WHERE `Fluggesellschaft`.`userid` = `benutzer`.`idUser` AND `bank`.`bankKonto` = `Fluggesellschaft`.`bankKonto` "
            + " AND benutzer.idUser = " + UserID + " GROUP BY `benutzer`.`name`";

    try {
      Ergebnis = (double) getEntityManager().createNativeQuery(SqlAbfrage).getSingleResult();
    } catch (NoResultException e) {
      Ergebnis = 0.0;
    }

    // Benutzer abfragen
    SqlAbfrage = "SELECT SUM(`bank`.`betrag`) AS tmp_betrag1 FROM `bank` AS `bank`, `benutzer` AS `benutzer` WHERE `bank`.`bankKonto` = `benutzer`.`bankKonto` "
            + "AND benutzer.idUser = " + UserID + " GROUP BY `benutzer`.`name`";

    try {
      Ergebnis = Ergebnis + (double) getEntityManager().createNativeQuery(SqlAbfrage).getSingleResult();
    } catch (NoResultException e) {
      Ergebnis = Ergebnis + 0.0;
    }

    return Ergebnis;

  }

  public List<Bank> getBankbuchungen(String KtoNummer) {
    return getEntityManager().createQuery("SELECT b FROM Bank b WHERE b.bankKonto = :kto ORDER BY b.ueberweisungsDatum DESC", Bank.class)
            .setParameter("kto", KtoNummer)
            .getResultList();
  }

  public void deleteBuchung(Bank entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  /*
  ************************ DOB Banking ENDE
   */
 /*
  *********************** Mail BEGINN
   */
  public List<Mail> getUserMails(int userid, String Kategorie) {

    Kategorie = "%" + Kategorie + "%";

    return getEntityManager().createQuery("SELECT m FROM Mail m WHERE m.userID = :userid and m.kategorie like :kategorie ORDER BY m.datumZeit DESC", Mail.class)
            .setParameter("userid", userid)
            .setParameter("kategorie", Kategorie)
            .getResultList();
  }

  public List<Mail> getUserMailsKategorie(int userid, String Kategorie) {
    return getEntityManager().createQuery("SELECT m FROM Mail m WHERE m.userID = :userid AND m.kategorie = :kategorie ORDER BY m.datumZeit DESC", Mail.class)
            .setParameter("userid", userid)
            .setParameter("kategorie", Kategorie)
            .getResultList();
  }

  public List<Mail> getMailKategorien(int userid) {
    return getEntityManager().createQuery("SELECT m FROM Mail m WHERE m.userID = :userid AND (m.kategorie <> 'Posteingang' and m.kategorie <> 'Sent' ) GROUP BY m.kategorie ORDER BY m.kategorie", Mail.class)
            .setParameter("userid", userid)
            .getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<String> getUserList() {
    return getEntityManager().createQuery("SELECT f.name1 FROM Benutzer f WHERE f.idUser > 0 ORDER BY f.name1").getResultList();
  }

  public long getAnzahlUngeleseneMails(int id) {

    long Anzahl = (long) getEntityManager().createNativeQuery("SELECT count(*) FROM mail where userID = " + id + " and gelesen = false").getSingleResult();

    return Anzahl;

  }

  public void deleteUserMail(Mail entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public void editUserMail(Mail entity) {
    getEntityManager().merge(entity);
  }

  public void saveUserMail(Mail entity) {
    getEntityManager().persist(entity);
  }

  /*
  *********************** Mail ENDE
   */
}
