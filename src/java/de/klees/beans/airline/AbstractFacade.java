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
package de.klees.beans.airline;

import de.klees.data.Airport;
import de.klees.data.Assignement;
import de.klees.data.Bank;
import de.klees.data.FlugesellschaftPiloten;
import de.klees.data.Fluggesellschaft;
import de.klees.data.FluggesellschaftBonusSystem;
import de.klees.data.Fluggesellschaftmanager;
import de.klees.data.Flugzeugblacklist;
import de.klees.data.Flugzeuge;
import de.klees.data.Flugzeugemietkauf;
import de.klees.data.Jobs;
import de.klees.data.Jobtemplate;
import de.klees.data.Mail;
import de.klees.data.Sitzkonfiguration;
import de.klees.data.Benutzer;
import de.klees.data.views.ViewFlugzeugeMietKauf;
import de.klees.data.views.ViewJobsAssignments;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

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

  public void createPilot(FlugesellschaftPiloten entity) {
    getEntityManager().persist(entity);
  }

  public void createBonus(FluggesellschaftBonusSystem entity) {
    getEntityManager().persist(entity);
  }

  public void removeBonus(FluggesellschaftBonusSystem entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public void editBonus(FluggesellschaftBonusSystem entity) {
    getEntityManager().merge(entity);
  }

  public void edit(T entity) {
    getEntityManager().merge(entity);
  }

  public void remove(T entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
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
   * @param Bankkonto
   */
  public void createBankkonto(Bank Bankkonto) {
    getEntityManager().persist(Bankkonto);
  }

  @SuppressWarnings("unchecked")
  public List<Airport> findAllAirportbyIcaoSingle(String icao) {
    return getEntityManager().createNamedQuery("Airport.findByIcaoSingle").setParameter("icao", icao).getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<String> findAllCountrys() {
    return getEntityManager().createQuery("SELECT DISTINCT a.land FROM Airport a ORDER BY a.land").getResultList();
  }

  public List<FluggesellschaftBonusSystem> getBoni(int fgid) {
    return getEntityManager().createQuery("SELECT b FROM FluggesellschaftBonusSystem b WHERE b.idfluggesellschaft = :fgid ORDER BY b.zeit", FluggesellschaftBonusSystem.class)
            .setParameter("fgid", fgid)
            .getResultList();
  }


  /*
  ****************************************** Bank Beginn
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

  public boolean ifExistBankkonto(String Bankkonto) {

    try {
      Bank konto = getEntityManager().createQuery("SELECT b FROM Bank b WHERE b.bankKonto = :konto", Bank.class)
              .setParameter("konto", Bankkonto)
              .setMaxResults(1)
              .getSingleResult();

      String KontoNummer = konto.getBankKonto();

      if (KontoNummer.equals("")) {
        return false;
      }

      return true;

    } catch (Exception e) {
      return false;
    }

  }

  /**
   *
   * @param Kontonummer
   * @return
   */
  public boolean ifExistBankKonto(String Kontonummer) {
    try {
      if (getEntityManager().createNamedQuery("Bank.findByBankKonto", Bank.class).setParameter("bankKonto", Kontonummer).getFirstResult() > 0) {
        return true;
      } else {
        return false;
      }
    } catch (NoResultException e) {
      return false;
    }
  }

  /*
  ****************************************** Bank Ende
   */
  //************************* Flugzeuge BEGINN
  public List<ViewFlugzeugeMietKauf> getFlugzeugeFluggesellscahft(int fgid) {
    return getEntityManager().createQuery("SELECT f from ViewFlugzeugeMietKauf f WHERE f.fluggesellschaftID = :fgid", ViewFlugzeugeMietKauf.class)
            .setParameter("fgid", fgid)
            .getResultList();
  }

  public List<ViewFlugzeugeMietKauf> getVerfuegbareFlugzeugeFG(int fgid) {
    return getEntityManager().createQuery("SELECT f FROM ViewFlugzeugeMietKauf f WHERE f.fluggesellschaftID = :fgid AND f.istGesperrt = FALSE", ViewFlugzeugeMietKauf.class)
            .setParameter("fgid", fgid)
            .getResultList();
  }

  public ViewFlugzeugeMietKauf readFlugzeugMietKauf(int id) {
    try {
      return getEntityManager().createQuery("SELECT f FROM ViewFlugzeugeMietKauf f WHERE f.idMietKauf =:id", ViewFlugzeugeMietKauf.class)
              .setParameter("id", id)
              .getSingleResult();
    } catch (NullPointerException e) {
      return null;
    }
  }

  public Flugzeuge readFlugzeugbyID(int fgzid) {
    return getEntityManager().createQuery("SELECT f FROM Flugzeuge f WHERE f.idFlugzeug = :fgzid", Flugzeuge.class)
            .setParameter("fgzid", fgzid)
            .getSingleResult();
  }

  public boolean hatUserEinFlugzeugGemietet(int userid) {

    try {
      if (getEntityManager().createQuery("SELECT v from ViewFlugzeugeMietKauf v where v.idUserDerFlugzeugGesperrtHat =:userid", ViewFlugzeugeMietKauf.class)
              .setParameter("userid", userid)
              .getSingleResult() != null) {
        return true;
      }

    } catch (NoResultException e) {
      return false;
    }
    return false;
  }

  public Sitzkonfiguration getSitzKonfiguration(int id) {
    try {
      return getEntityManager().createQuery("SELECT k FROM Sitzkonfiguration k WHERE k.idsitzKonfiguration =:id", Sitzkonfiguration.class)
              .setParameter("id", id)
              .getSingleResult();

    } catch (NoResultException e) {
      return null;
    }
  }

  //************************* Flugzeuge ENDE
  public int onDeleteFluggesellschaft(int idFG) {
    String abfrage = "DELETE Fluggesellschaft, Flugrouten, assignement FROM assignement AS assignement, Flugrouten AS Flugrouten, Fluggesellschaft AS Fluggesellschaft WHERE assignement.idRoute = Flugrouten.idFlugrouten AND Flugrouten.idFluggesellschaft = Fluggesellschaft.idFluggesellschaft AND Fluggesellschaft.idFluggesellschaft = " + idFG;
    return getEntityManager().createNativeQuery(abfrage).executeUpdate();
  }

  public boolean ifExistAirline(String name) {
    try {
      if (getEntityManager().createQuery("SELECT a FROM Fluggesellschaft a WHERE a.name = :name", Fluggesellschaft.class)
              .setParameter("name", name).getSingleResult().getName().toLowerCase().equals(name.toLowerCase())) {
        return true;
      }
    } catch (Exception e) {
      return false;
    }
    return false;
  }

  public List<Fluggesellschaft> findAll() {
    return getEntityManager().createQuery("SELECT f FROM Fluggesellschaft f where f.besitzerName NOT LIKE '**** FTW Airlines ****' ORDER BY f.name", Fluggesellschaft.class).getResultList();
  }

  public List<Fluggesellschaft> findAllAirlinesByUser(int UserID, int mgid) {
    return getEntityManager().createQuery("SELECT f from Fluggesellschaft f WHERE f.idFluggesellschaft = :mgid OR f.userid = :uid", Fluggesellschaft.class)
            .setParameter("uid", UserID)
            .setParameter("mgid", mgid)
            .getResultList();
  }

  public Fluggesellschaft readFG(int fgid) {
    try {
      return getEntityManager().createQuery("SELECT f FROM Fluggesellschaft f WHERE f.idFluggesellschaft =:fgid", Fluggesellschaft.class)
              .setParameter("fgid", fgid)
              .getSingleResult();
    } catch (NullPointerException e) {
      return null;
    }
  }

  public Flugzeugemietkauf getFlugzeugDaten(int flzid) {
    try {
      return getEntityManager().createQuery("SELECT f FROM Flugzeugemietkauf f WHERE f.idMietKauf =:flzid", Flugzeugemietkauf.class)
              .setParameter("flzid", flzid)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public void saveMietKaufFlugzeug(Flugzeugemietkauf entity) {
    getEntityManager().merge(entity);
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

  public Benutzer readUser(int UserID) {
    try {
      return (Benutzer) getEntityManager().createQuery("SELECT u from Benutzer u WHERE u.idUser = :userid", Benutzer.class)
              .setParameter("userid", UserID)
              .getSingleResult();

    } catch (NoResultException e) {
      return null;
    }

  }

  public int executeAbfrage(String Abfrage) {
    return getEntityManager().createNativeQuery(Abfrage).executeUpdate();
  }

  public boolean findIcaoCode(String icaocode) {

    try {
      if (getEntityManager().createQuery("SELECT f FROM Fluggesellschaft f WHERE f.icaoCode =:icaocode", Fluggesellschaft.class)
              .setParameter("icaocode", icaocode)
              .getSingleResult().getIcaoCode().equals(icaocode)) {
        return true;
      }
    } catch (NoResultException e) {
      return false;
    } catch (NonUniqueResultException e) {
      return true;
    }

    return true;
  }

  public List<Jobs> getJobs(int fgid) {
    return getEntityManager().createQuery("SELECT j FROM Jobs j WHERE j.fluggesellschaftid =:fgid", Jobs.class)
            .setParameter("fgid", fgid)
            .getResultList();
  }

  public List<ViewJobsAssignments> getPublicJobs() {
    return getEntityManager().createQuery("SELECT j FROM ViewJobsAssignments j WHERE j.freigabe = TRUE GROUP BY j.idjobs ORDER BY j.ablaufdatum", ViewJobsAssignments.class)
            .getResultList();
  }

  public int getJobs(){
    long counter = (long)getEntityManager().createNativeQuery("SELECT COUNT(*) from jobs WHERE freigabe = TRUE ").getSingleResult();
    return (int)counter;
  }
  
  public List<ViewJobsAssignments> getPublicJobsGefiltert(String flgz, int fgid) {
    flgz = "%" + flgz + "%";

    if (fgid < 0) {
      return getEntityManager().createQuery("SELECT j FROM ViewJobsAssignments j WHERE j.freigabe = TRUE AND j.type LIKE :flgz GROUP BY j.idjobs ORDER BY j.ablaufdatum", ViewJobsAssignments.class)
              .setParameter("flgz", flgz)
              .getResultList();
    } else {
      return getEntityManager().createQuery("SELECT j FROM ViewJobsAssignments j WHERE j.freigabe = TRUE AND j.type LIKE :flgz AND j.fluggesellschaftid =:fgid GROUP BY j.idjobs ORDER BY j.ablaufdatum", ViewJobsAssignments.class)
              .setParameter("flgz", flgz)
              .setParameter("fgid", fgid)
              .getResultList();
    }

  }

  public void newJob(Jobs entity) {
    getEntityManager().persist(entity);
  }

  public void saveJob(Jobs entity) {
    getEntityManager().merge(entity);
  }

  public void deleteJob(Jobs entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public List<Assignement> getVerfuegbareAuftraege(String icao, int fgid, String Arrival, int Ladung) {

    if (Arrival.equals("")) {

      if (Ladung == 1) {
        return getEntityManager().createQuery("SELECT a FROM Assignement a WHERE a.locationIcao =:icao AND (a.routenArt=1 OR a.routenArt=4) AND a.idAirline = :fgid AND a.idowner = -1 AND a.idjob = -1 ORDER BY a.toIcao, a.expires ASC", Assignement.class)
                .setParameter("icao", icao)
                .setParameter("fgid", fgid)
                .getResultList();
      }

      if (Ladung == 2) {
        return getEntityManager().createQuery("SELECT a FROM Assignement a WHERE a.locationIcao =:icao AND (a.routenArt=2 OR a.routenArt=5) AND a.idAirline = :fgid AND a.idowner = -1 AND a.idjob = -1 ORDER BY a.toIcao, a.expires ASC", Assignement.class)
                .setParameter("icao", icao)
                .setParameter("fgid", fgid)
                .getResultList();
      }

      return getEntityManager().createQuery("SELECT a FROM Assignement a WHERE a.locationIcao =:icao AND a.idAirline = :fgid AND a.idowner = -1 AND a.idjob = -1 ORDER BY a.toIcao, a.expires ASC", Assignement.class)
              .setParameter("icao", icao)
              .setParameter("fgid", fgid)
              .getResultList();

    } else {
      if (Ladung == 1) {
        return getEntityManager().createQuery("SELECT a FROM Assignement a WHERE a.locationIcao =:icao AND (a.routenArt=1 OR a.routenArt=4) AND a.toIcao =:arrival AND a.idAirline = :fgid AND a.idowner = -1 AND a.idjob = -1 ORDER BY a.toIcao, a.expires ASC", Assignement.class)
                .setParameter("icao", icao)
                .setParameter("fgid", fgid)
                .setParameter("arrival", Arrival)
                .getResultList();
      }

      if (Ladung == 2) {
        return getEntityManager().createQuery("SELECT a FROM Assignement a WHERE a.locationIcao =:icao AND (a.routenArt=2 OR a.routenArt=5) AND a.toIcao =:arrival AND a.idAirline = :fgid AND a.idowner = -1 AND a.idjob = -1 ORDER BY a.toIcao, a.expires ASC", Assignement.class)
                .setParameter("icao", icao)
                .setParameter("fgid", fgid)
                .setParameter("arrival", Arrival)
                .getResultList();
      }

      return getEntityManager().createQuery("SELECT a FROM Assignement a WHERE a.locationIcao =:icao AND a.toIcao =:arrival AND a.idAirline = :fgid AND a.idowner = -1 AND a.idjob = -1 ORDER BY a.toIcao, a.expires ASC", Assignement.class)
              .setParameter("icao", icao)
              .setParameter("fgid", fgid)
              .setParameter("arrival", Arrival)
              .getResultList();
    }

  }

  public List<Assignement> getFilteredArrivals(String icao, int fgid) {
    return getEntityManager().createQuery("SELECT a FROM Assignement a WHERE a.locationIcao =:icao AND a.idAirline = :fgid AND a.idowner = -1 AND a.idjob = -1 GROUP BY a.toIcao ORDER BY a.toIcao, a.expires ASC", Assignement.class)
            .setParameter("icao", icao)
            .setParameter("fgid", fgid)
            .getResultList();
  }

  public List<Assignement> getJobAuftraege(int jobid) {
    return getEntityManager().createQuery("SELECT a FROM Assignement a WHERE a.idjob =:jobid ORDER BY a.toIcao, a.expires ASC ", Assignement.class)
            .setParameter("jobid", jobid)
            .getResultList();
  }

  public List<Jobtemplate> getTemplates(int fgid) {
    return getEntityManager().createQuery("SELECT t from Jobtemplate t WHERE t.idfluggesellschaft = :fgid", Jobtemplate.class)
            .setParameter("fgid", fgid)
            .getResultList();
  }

  public List<Jobtemplate> getTemplatesTausch(int fgid) {
    return getEntityManager().createQuery("SELECT t from Jobtemplate t WHERE t.tausch = true", Jobtemplate.class)
            .getResultList();
  }

  public void createTemplate(Jobtemplate entity) {
    getEntityManager().persist(entity);
  }

  public void saveTemplate(Jobtemplate entity) {
    getEntityManager().merge(entity);
  }

  public void deleteTemplate(Jobtemplate entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public void JobsUserZuweisen(int userid, int jobid) {
    getEntityManager().createQuery("UPDATE Assignement a SET a.idowner = :userid, a.userlock = 0, a.active = 0 WHERE a.idjob = :jobid", Assignement.class)
            .setParameter("jobid", jobid)
            .setParameter("userid", userid)
            .executeUpdate();
  }

  public void JobsOwnerZuruecksetzen(int jobid) {
    getEntityManager().createQuery("UPDATE Assignement a SET a.idowner = -1, a.idjob = -1, a.userlock = 0, a.active = 0 WHERE a.idjob = :jobid", Assignement.class)
            .setParameter("jobid", jobid)
            .executeUpdate();
  }

  public void alleJobsEinladen(String icao, int fgid, int jobid, String Arrival, int Ladung) {

    if (Arrival.equals("")) {

      if (Ladung == 1) {
        getEntityManager().createQuery("UPDATE Assignement a SET a.idjob =:jobid WHERE a.locationIcao =:icao AND (a.routenArt=1 OR a.routenArt=4) AND a.idjob = -1  AND a.idAirline = :fgid AND a.idowner = -1 ", Assignement.class)
                .setParameter("icao", icao)
                .setParameter("fgid", fgid)
                .setParameter("jobid", jobid)
                .executeUpdate();
      }

      if (Ladung == 2) {
        getEntityManager().createQuery("UPDATE Assignement a SET a.idjob =:jobid WHERE a.locationIcao =:icao AND (a.routenArt=2 OR a.routenArt=5) AND a.idjob = -1  AND a.idAirline = :fgid AND a.idowner = -1 ", Assignement.class)
                .setParameter("icao", icao)
                .setParameter("fgid", fgid)
                .setParameter("jobid", jobid)
                .executeUpdate();
      }

      if (Ladung == -1) {
        getEntityManager().createQuery("UPDATE Assignement a SET a.idjob =:jobid WHERE a.locationIcao =:icao AND a.idjob = -1  AND a.idAirline = :fgid AND a.idowner = -1 ", Assignement.class)
                .setParameter("icao", icao)
                .setParameter("fgid", fgid)
                .setParameter("jobid", jobid)
                .executeUpdate();

      }
    } else {

      if (Ladung == 1) {
        getEntityManager().createQuery("UPDATE Assignement a SET a.idjob =:jobid WHERE a.locationIcao =:icao AND (a.routenArt=1 OR a.routenArt=4) AND a.toIcao =:arrival AND a.idjob = -1  AND a.idAirline = :fgid AND a.idowner = -1 ", Assignement.class)
                .setParameter("icao", icao)
                .setParameter("fgid", fgid)
                .setParameter("jobid", jobid)
                .setParameter("arrival", Arrival)
                .executeUpdate();
      }

      if (Ladung == 2) {
        getEntityManager().createQuery("UPDATE Assignement a SET a.idjob =:jobid WHERE a.locationIcao =:icao AND (a.routenArt=2 OR a.routenArt=5) AND a.toIcao =:arrival AND a.idjob = -1  AND a.idAirline = :fgid AND a.idowner = -1 ", Assignement.class)
                .setParameter("icao", icao)
                .setParameter("fgid", fgid)
                .setParameter("jobid", jobid)
                .setParameter("arrival", Arrival)
                .executeUpdate();
      }

      if (Ladung == -1) {
        getEntityManager().createQuery("UPDATE Assignement a SET a.idjob =:jobid WHERE a.locationIcao =:icao AND a.toIcao =:arrival AND a.idjob = -1  AND a.idAirline = :fgid AND a.idowner = -1 ", Assignement.class)
                .setParameter("icao", icao)
                .setParameter("fgid", fgid)
                .setParameter("jobid", jobid)
                .setParameter("arrival", Arrival)
                .executeUpdate();
      }
    }

  }

  public void alleJobsAusladen(String icao, int fgid, int jobid) {
    getEntityManager().createQuery("UPDATE Assignement a SET a.idjob = -1 WHERE a.locationIcao =:icao AND a.idAirline = :fgid AND a.idowner = -1 AND a.idjob =:jobid ", Assignement.class)
            .setParameter("icao", icao)
            .setParameter("fgid", fgid)
            .setParameter("jobid", jobid)
            .executeUpdate();
  }

  public Jobs getJobDaten(int jobid) {
    try {
      return getEntityManager().createQuery("SELECT j FROM Jobs j WHERE j.idjobs = :jobid", Jobs.class)
              .setParameter("jobid", jobid)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  public List<Object> FlugzeugeInJobs() {
    String Abfrage = "SELECT jobs.idjobs, jobs.flugzeugid, Flugzeuge.type "
            + "FROM "
            + "    Flugzeuge_miet_kauf AS Flugzeuge_miet_kauf, "
            + "    jobs AS jobs, "
            + "    Flugzeuge AS Flugzeuge "
            + "WHERE "
            + "    jobs.freigabe = TRUE "
            + "    AND jobs.nochfrei = TRUE "
            + "        AND Flugzeuge_miet_kauf.idMietKauf = jobs.flugzeugid "
            + "        AND Flugzeuge.idFlugzeug = Flugzeuge_miet_kauf.idFlugzeug "
            + "group by Flugzeuge.type "
            + "order by Flugzeuge.type";

    return getEntityManager().createNativeQuery(Abfrage).getResultList();

  }

  @SuppressWarnings("unchecked")
  public List<Object> FluggesellschaftenInJobs() {
    String Abfrage = "SELECT "
            + "    jobs.idjobs, "
            + "    Fluggesellschaft.idFluggesellschaft, "
            + "    Fluggesellschaft.name "
            + "FROM "
            + "    Fluggesellschaft AS Fluggesellschaft, "
            + "    jobs AS jobs "
            + "WHERE "
            + "    jobs.freigabe = TRUE "
            + "    AND jobs.nochfrei = TRUE "
            + "        AND Fluggesellschaft.idFluggesellschaft = jobs.fluggesellschaftid "
            + "GROUP BY Fluggesellschaft.name "
            + "ORDER BY Fluggesellschaft.name";

    return getEntityManager().createNativeQuery(Abfrage).getResultList();
  }

  public boolean SindJobsAmBoden(int jobid) {
    try {
      int lock = getEntityManager().createQuery("SELECT a from Assignement a WHERE a.idjob = :jobid ", Assignement.class)
              .setParameter("jobid", jobid)
              .getResultList().get(0).getUserlock();

      // Userlock = 0 Am Boden
      // Userlock = 1 In der Luft
      if (lock == 1) {
        return false;
      } else {
        return true;
      }

    } catch (Exception e) {
      return true;
    }

  }

  public void editAssignment(Assignement entity) {
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

}
