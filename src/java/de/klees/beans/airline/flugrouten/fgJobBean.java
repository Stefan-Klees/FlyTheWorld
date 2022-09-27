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

import de.klees.beans.airline.FluggesellschaftFacade;
import de.klees.beans.system.CONF;
import de.klees.beans.system.loginMB;
import de.klees.data.Assignement;
import de.klees.data.Fluggesellschaft;
import de.klees.data.Flugzeugemietkauf;
import de.klees.data.Jobs;
import de.klees.data.Jobtemplate;
import de.klees.data.Mail;
import de.klees.data.Sitzkonfiguration;
import de.klees.data.Benutzer;
import de.klees.data.views.ViewFlugzeugeMietKauf;
import de.klees.data.views.ViewJobsAssignments;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author Stefan Klees
 */
public class fgJobBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<Jobs> jobList;
  private List<ViewJobsAssignments> JobBoerseJobList;
  private List<ViewFlugzeugeMietKauf> FlugzeugListe;
  private List<Assignement> verfuegbareAuftraege;
  private List<Assignement> selectierteAuftraege;
  private Assignement selectedVerfuegbarerAuftrag;
  private Assignement selectedSelectierterAuftrag;
  private Jobs selectedJob;
  private ViewJobsAssignments selectedUserJob;

  private Jobtemplate selectedTemplate;
  private Jobtemplate importTemplate;
  private int UserJobID;
  private Fluggesellschaft aktuelleFluggesellschaft;
  private int FgID;
  private int UserID;
  private String UserName;

  private String FlugzeugTyp;
  private Sitzkonfiguration sitzKonfiguration;
  private int SitzeBusiness;
  private int SitzeEconomy;
  private int CargoVerfuegbar;
  private int maxPayload;

  private double BonusAllePiloten;
  private double BonusFGPilot;
  private double Provision;

  private double JobBoerseAuftragswert;
  private double JobBoerseProvision;
  private double JobBoerseBoni1;
  private double JobBoerseBoni2;

  private String JobFlugzeuge;
  private String JobFluggesellschaften;
  private String JobArrival;
  private boolean listLoaded;
  private boolean Start;

  private String ArrivalFilter;
  private int LadungFilter;

  public fgJobBean() {
    FgID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftID");
    UserName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName");
    UserID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
    BonusAllePiloten = 0.0;
    BonusFGPilot = 0.0;
    Provision = 0.0;
    JobFlugzeuge = "";
    JobFluggesellschaften = "-1";
    ArrivalFilter = "";
    Start = true;

  }

  @EJB
  FluggesellschaftFacade jobFacade;

  public void onStart() {
    readFG();
    listLoaded = false;
  }

  public List<Jobs> getAktiveJobs() {
    if (!listLoaded) {
      jobList = jobFacade.getJobs(FgID);
      listLoaded = true;
    }
    return jobList;
  }

  public List<Jobtemplate> getTemplates() {
    return jobFacade.getTemplates(FgID);
  }

  public List<Jobtemplate> getTemplatesTausch() {
    return jobFacade.getTemplatesTausch(FgID);
  }

  public List<ViewJobsAssignments> getPublicJobs() {

    //Beim Starten nichts Anzeigen
    if (Start) {
      Start = false;
      listLoaded = true;
      return JobBoerseJobList;
    }

    if (!listLoaded) {
      if (JobFlugzeuge.equals("") && JobFluggesellschaften.equals("-1")) {
        JobBoerseJobList = jobFacade.getPublicJobs();
      } else {
        JobBoerseJobList = jobFacade.getPublicJobsGefiltert(JobFlugzeuge, Integer.valueOf(JobFluggesellschaften));
      }
    }

    return JobBoerseJobList;
  }

  public void onRefreshPublicJobs() {
    listLoaded = false;
  }

  public List<ViewFlugzeugeMietKauf> getFlugzeugeFG() {
    return jobFacade.getVerfuegbareFlugzeugeFG(FgID);
  }

  public List<Assignement> getVerfuegbareAuftraege() {
    if (selectedJob != null) {
      try {
        if (selectedJob.getFlugzeugid() > 0) {
          return jobFacade.getVerfuegbareAuftraege(getFlugzeug(selectedJob.getFlugzeugid()).getAktuellePositionICAO(), FgID, ArrivalFilter, LadungFilter);
        } else {
          return null;
        }
      } catch (NullPointerException e) {
        NewMessage("No Aircraft available");
        return null;
      }
    } else {
      return null;
    }
  }

  public List<Assignement> getSelectierteAuftraege() {
    if (selectedJob != null) {
      return jobFacade.getJobAuftraege(selectedJob.getIdjobs());
    } else {
      return null;
    }

  }

  public List<Object> getFlugzeugeInJobs() {
    return jobFacade.FlugzeugeInJobs();
  }

  public List<Object> getFluggesellschaftenInJobs() {
    return jobFacade.FluggesellschaftenInJobs();
  }

  public List<Object> getArrivalInJobs() {
    return jobFacade.FluggesellschaftenInJobs();
  }

  public List<Assignement> getFilteredArrivals() {
    if (selectedJob != null) {
      try {
        if (selectedJob.getFlugzeugid() > 0) {
          return jobFacade.getFilteredArrivals(getFlugzeug(selectedJob.getFlugzeugid()).getAktuellePositionICAO(), FgID);
        } else {
          return null;
        }
      } catch (NullPointerException e) {
        return null;
      }
    } else {
      return null;
    }
  }

  public void onFilteredAuftraege() {

  }

  public void onAuftragZurBeladung() {
    if (selectedVerfuegbarerAuftrag != null) {
      selectedVerfuegbarerAuftrag.setIdjob(selectedJob.getIdjobs());
      jobFacade.editAssignment(selectedVerfuegbarerAuftrag);
    }
  }

  public void onAuftragRausAusBeladung() {
    if (selectedSelectierterAuftrag != null) {
      selectedSelectierterAuftrag.setIdjob(-1);
      jobFacade.editAssignment(selectedSelectierterAuftrag);
    }
  }

  public void onUserJobZuweisen() {
    boolean jobOK = false;

    if (selectedUserJob != null) {
      UserJobID = selectedUserJob.getIdjobs();
      jobOK = true;
    } else {
      NewMessage("No job selected");
    }

    //Blacklist Abfrage
    if (jobFacade.ifUserInBlacklist(UserID, selectedUserJob.getFlugzeugid())) {
      jobOK = false;
      NewMessage("Du kannst den Job nicht annehmen, du stehst bei dem reservierten Flugzeug auf der Blacklist");
    }

    //hat Benutzer ein Flugzeug gemietet
    if (jobOK) {
      if (!jobFacade.hatUserEinFlugzeugGemietet(UserID)) {
        jobOK = true;
      } else {
        jobOK = false;
        NewMessage(loginMB.onSprache("JobBoerse_msg_onUserJobZuweisen"));
      }
    }

    //jobOK = false;
    if (jobOK) {
      //Prüfen ob Job noch vorhanden ist.

      Jobs jobdaten = jobFacade.getJobDaten(UserJobID);

      if (jobdaten != null) {

        saveMail(UserName, jobFacade.getJobDaten(UserJobID).getErzeugtvonuser(), loginMB.onSprache("JobBoerse_msg_onUserJobZuweisen2"), jobFacade.getJobDaten(UserJobID).getBeschreibung());

        //FlugzeugID auslesen
        int flugzeugID = jobFacade.getJobDaten(UserJobID).getFlugzeugid();
        Flugzeugemietkauf flz = jobFacade.getFlugzeugDaten(flugzeugID);
        flz.setIdUserDerFlugzeugGesperrtHat(UserID);
        flz.setIstGesperrt(true);
        jobFacade.saveMietKaufFlugzeug(flz);

        // Zuweisung der Auftraege an den Benutzer
        jobFacade.JobsUserZuweisen(UserID, UserJobID);

//          Job markieren, nur die Job Stammdaten nicht die Auftraege
//          jobdaten.setNochfrei(false);
//          jobdaten.setUsernamePilot(UserName);
//
//          jobFacade.saveJob(jobdaten);
        jobFacade.deleteJob(jobdaten);
        NewMessage(loginMB.onSprache("JobBoerse_msg_onUserJobZuweisen3"));

        listLoaded = false;
      } else {
        jobOK = false;
        NewMessage(loginMB.onSprache("JobBoerse_msg_onUserJobZuweisen4"));
      }
    }
  }

  public void saveJob() {

    if (selectedJob != null) {
      if (!selectedJob.getFreigabe()) {

        selectedJob.setBeschreibung(clearText(selectedJob.getBeschreibung()));

        jobFacade.saveJob(selectedJob);
        listLoaded = false;
        NewMessage(loginMB.onSprache("JobBoerse_msg_saveJob"));
      } else {
        NewMessage(loginMB.onSprache("JobBoerse_msg_saveJob1"));
      }
    }

  }

  public void createTemplate() {
    Jobtemplate tmpl = new Jobtemplate();
    tmpl.setBeschreibung("");
    tmpl.setBezeichnung(loginMB.onSprache("JobBoerse_msg_createTemplate"));
    tmpl.setIdfluggesellschaft(FgID);
    tmpl.setTausch(false);
    jobFacade.createTemplate(tmpl);
    NewMessage(loginMB.onSprache("JobBoerse_msg_createTemplate1"));
  }

  public void saveTemplate() {
    selectedTemplate.setBeschreibung(clearText(selectedTemplate.getBeschreibung()));
    jobFacade.saveTemplate(selectedTemplate);
    NewMessage(loginMB.onSprache("JobBoerse_msg_createTemplate1"));
  }

  public void deleteTemplate() {
    jobFacade.deleteTemplate(selectedTemplate);
    setSelectedTemplate(null);
    NewMessage(loginMB.onSprache("JobBoerse_msg_deleteTemplate"));
  }

  public void loadTemplate() {

    List<Assignement> auftraege = getSelectierteAuftraege();

    JobBoerseAuftragswert = 0.0;
    JobBoerseBoni1 = 0.0;
    JobBoerseBoni2 = 0.0;
    JobBoerseProvision = 0.0;
    int PayLoad = 0;
    int PaxECO = 0;
    int PaxBC = 0;
    int Cargo = 0;
    int Meilen = 0;

    for (Assignement assi : auftraege) {
      JobBoerseAuftragswert = JobBoerseAuftragswert + assi.getPay();
      JobBoerseBoni1 = JobBoerseBoni1 + assi.getPay() / 100.0 * assi.getBonusoeffentlich();
      JobBoerseBoni2 = JobBoerseBoni2 + assi.getPay() / 100.0 * assi.getBonusclosed();
      JobBoerseProvision = JobBoerseProvision + ((assi.getPay() / 100.0) * assi.getProvision());
      PayLoad = PayLoad + assi.getGepaeck() + assi.getGewichtPax();
      Meilen = assi.getDistance();
      // RoutenArt 1=PAX, 2=CARGO, 3=TRF, 4=Business-PAX
      switch (assi.getRoutenArt()) {
        case 1:
          PaxECO = PaxECO + assi.getAmmount();
          break;
        case 2:
          Cargo = Cargo + assi.getAmmount();
          PayLoad = PayLoad + assi.getAmmount();
          break;
        case 4:
          PaxBC = PaxBC + assi.getAmmount();
          break;
        default:
          break;
      }

    }

    if (selectedJob != null) {
      ViewFlugzeugeMietKauf fgz = jobFacade.readFlugzeugMietKauf(selectedJob.getFlugzeugid());

      Fluggesellschaft fg = jobFacade.readFG(FgID);

      DecimalFormat df = new DecimalFormat("#,##0");
      SimpleDateFormat dfdate = new SimpleDateFormat("dd.MM.yyyy HH:mm");
      DecimalFormat dfkomma = new DecimalFormat("#,##0.00");

      try {
        String departure = auftraege.get(0).getFromIcao() + " - " + auftraege.get(0).getFromAirportLandCity();
        String arrival = auftraege.get(0).getToIcao() + " - " + auftraege.get(0).getToAirportLandCity();
        String arrivalICAO = auftraege.get(0).getToIcao();
        String departureICAO = auftraege.get(0).getFromIcao();
        String FlugzeugBild = "";
        String FlugzeugName = "";
        String FlugzeugTank = df.format(fgz.getAktuelleTankfuellung());
        String FluggesellschaftName = "";
        String Fluggesellschaftbild = "";
        double kmpProzent = 0.0;
        double kmpPay = 0.0;
        double kmpPay1 = 0.0;
        double kmpPay2 = 0.0;
        double Bonus1AndkmpPay1 = 0.0;
        double Bonus2AndkmpPay2 = 0.0;

        FlugzeugName = fgz.getType();

        if (!fgz.getEigenesBildURL().equals("")) {
          FlugzeugBild = fgz.getEigenesBildURL();
        } else {
          FlugzeugBild = fgz.getSymbolUrl();
        }

        FluggesellschaftName = fg.getName();
        Fluggesellschaftbild = fg.getLogoURL();

        if (fgz.getIdSitzKonfiguration() > 0) {
          Sitzkonfiguration sitzkfg = jobFacade.getSitzKonfiguration(fgz.getIdSitzKonfiguration());
          kmpProzent = (sitzkfg.getFaktor() * 100) - 100;
          kmpPay = ((JobBoerseBoni1 + JobBoerseBoni2) / 100.0) * kmpProzent;
          // Boni1 = Oeffentlicher Bonus für jeden Piloten
          // Boni2 = Bonus für Piloten der Fluggesellschaft
          kmpPay1 = (JobBoerseBoni1 / 100.0) * kmpProzent;
          kmpPay2 = (JobBoerseBoni2 / 100.0) * kmpProzent;
          Bonus1AndkmpPay1 = JobBoerseBoni1 + kmpPay1;
          Bonus2AndkmpPay2 = JobBoerseBoni2 + kmpPay2;

        }

        String rendered = importTemplate.getBeschreibung()
                .replaceAll("#contact#", selectedJob.getErzeugtvonuser())
                .replaceAll("#expirydate#", dfdate.format(selectedJob.getAblaufdatum()))
                .replaceAll("#arrival#", arrival)
                .replaceAll("#arrivalICAO#", arrivalICAO)
                .replaceAll("#departure#", departure)
                .replaceAll("#departureICAO#", departureICAO)
                .replaceAll("#miles#", df.format(Meilen))
                .replaceAll("#airline#", FluggesellschaftName)
                .replaceAll("#airlinelogo#", Fluggesellschaftbild)
                .replaceAll("#bonusPublic#", dfkomma.format(JobBoerseBoni1))
                .replaceAll("#bonusAirline#", dfkomma.format(JobBoerseBoni2))
                .replaceAll("#bonusPublicInclKMP1#", dfkomma.format(Bonus1AndkmpPay1))
                .replaceAll("#bonusAirlineInclKMP2#", dfkomma.format(Bonus2AndkmpPay2))
                .replaceAll("#bonusTotal#", dfkomma.format(JobBoerseBoni1 + JobBoerseBoni2))
                .replaceAll("#bonusTotalInclKMP#", dfkomma.format(JobBoerseBoni1 + JobBoerseBoni2 + kmpPay))
                .replaceAll("#aircraft#", FlugzeugName)
                .replaceAll("#aircraftImage#", FlugzeugBild)
                .replaceAll("#fuellOB#", FlugzeugTank)
                .replaceAll("#kmp%#", dfkomma.format(kmpProzent))
                .replaceAll("#kmppay#", dfkomma.format(kmpPay))
                .replaceAll("#kmppay1#", dfkomma.format(kmpPay1))
                .replaceAll("#kmppay2#", dfkomma.format(kmpPay2))
                .replaceAll("#paxECO#", df.format(PaxECO))
                .replaceAll("#paxBC#", df.format(PaxBC))
                .replaceAll("#totalPax#", df.format(PaxBC + PaxECO))
                .replaceAll("#payloadKG#", df.format(PayLoad))
                .replaceAll("#provision#", dfkomma.format(JobBoerseProvision))
                .replaceAll("#cargo#", df.format(Cargo))
                .replaceAll("#pay#", dfkomma.format(JobBoerseBoni1 + JobBoerseBoni2));

        selectedJob.setBeschreibung(clearText(rendered));
      } catch (IndexOutOfBoundsException e) {
        NewMessage("Erst das Flugzeug beladen!");
      }
    } else {
      NewMessage("Job wurde noch nicht gespeichert!");
    }

  }

  public void jobFreigeben() {
    if (selectedJob != null) {
      if (!selectedJob.getFreigabe()) {
        //Bezahlung der Gebuehren fuer Bereitstellung
        //ToDo kommt später
        //
        //
        //Sperren des Flugzeugs
        Flugzeugemietkauf flz = jobFacade.getFlugzeugDaten(selectedJob.getFlugzeugid());
        if (flz != null) {
          if (flz.getIstGesperrt()) {
            selectedJob.setFreigabe(false);
            NewMessage(loginMB.onSprache("JobBoerse_msg_jobFreigeben"));
          } else {
            flz.setIstGesperrt(true);
            flz.setIdUserDerFlugzeugGesperrtHat(-500);
            flz.setIstGesperrtBis(selectedJob.getAblaufdatum());
            flz.setIstGesperrtSeit(new Date());
            jobFacade.saveMietKaufFlugzeug(flz);
            //
            //
            selectedJob.setBeschreibung(clearText(selectedJob.getBeschreibung()));
            selectedJob.setFreigabe(true);
            jobFacade.saveJob(selectedJob);
            listLoaded = false;

            setSelectedJob(null);
            NewMessage(loginMB.onSprache("JobBoerse_msg_jobFreigeben1"));
          }
        }
      } else {
        NewMessage(loginMB.onSprache("JobBoerse_msg_jobFreigeben2"));
      }
    }
  }

  public void createJob() {
    selectedJob = null;
    Jobs newJob = new Jobs();
    newJob.setAblaufdatum(new Date());
    newJob.setBeschreibung("");
    newJob.setErstelltam(new Date());
    newJob.setErzeugtvonuser(UserName);
    newJob.setExtlink("");
    newJob.setFluggesellschaftid(FgID);
    newJob.setFlugzeugid(-1);
    newJob.setKommentar("");
    newJob.setKonventionalstrafe(0.0);
    newJob.setUserid(UserID);
    newJob.setFreigabe(false);
    newJob.setUsernamePilot("");
    newJob.setNochfrei(true);

    jobFacade.newJob(newJob);
    selectedJob = newJob;
    listLoaded = false;
    NewMessage("Job angelegt");

  }

  public void deleteJob() {

    boolean deleteOK = false;

    if (selectedJob != null) {

      //Prüfen ob Jobs in der Luft ist.
      if (jobFacade.SindJobsAmBoden(selectedJob.getIdjobs())) {
        deleteOK = true;
      } else {
        NewMessage(loginMB.onSprache("JobBoerse_msg_deleteJob"));
      }

      if (deleteOK) {
        //Aufträge JobID wieder auf -1 setzen
        //Aufträge ownerID auf -1 setzen
        //
        jobFacade.JobsOwnerZuruecksetzen(selectedJob.getIdjobs());
        //        
        // Flugzeug Freigeben
        //
        try {
          if (selectedJob.getFlugzeugid() > 0) {
            Flugzeugemietkauf flz = jobFacade.getFlugzeugDaten(selectedJob.getFlugzeugid());
            flz.setIstGesperrt(false);
            flz.setIdUserDerFlugzeugGesperrtHat(-1);
            flz.setIstGesperrtBis(null);
            flz.setIstGesperrtSeit(null);
            jobFacade.saveMietKaufFlugzeug(flz);
          }
        } catch (NullPointerException e) {

        }

        //
        //Job löschen
        //
        jobFacade.deleteJob(selectedJob);
        selectedJob = null;
        NewMessage(loginMB.onSprache("JobBoerse_msg_deleteJob1"));
        listLoaded = false;
      }
    }
  }

  private void readFG() {
    aktuelleFluggesellschaft = jobFacade.readFG(FgID);
  }

  public void onFlugzeugDaten() {
    try {
      ViewFlugzeugeMietKauf BenutztesFlugzeug = jobFacade.readFlugzeugMietKauf(selectedJob.getFlugzeugid());
      FlugzeugTyp = BenutztesFlugzeug.getType();
      if (BenutztesFlugzeug.getIdSitzKonfiguration() > 0) {
        sitzKonfiguration = jobFacade.getSitzKonfiguration(BenutztesFlugzeug.getIdSitzKonfiguration());
        SitzeBusiness = sitzKonfiguration.getSitzeBusiness();
        SitzeEconomy = sitzKonfiguration.getSitzeEconomy();
        CargoVerfuegbar = sitzKonfiguration.getCargo();
        maxPayload = sitzKonfiguration.getMaxPayload();
      } else {
        SitzeBusiness = BenutztesFlugzeug.getSitzeBusinessClass();
        SitzeEconomy = BenutztesFlugzeug.getSitzeEconomy();
        CargoVerfuegbar = BenutztesFlugzeug.getCargo();
        maxPayload = BenutztesFlugzeug.getPayload();
      }

    } catch (NullPointerException e) {
      FlugzeugTyp = loginMB.onSprache("JobBoerse_msg_onFlugzeugDaten");
      SitzeBusiness = 0;
      SitzeEconomy = 0;
      CargoVerfuegbar = 0;
      maxPayload = 0;
    }
  }

  public void onChangeBoniProvision() {

    if (getSelectierteAuftraege() != null) {
      for (Assignement assi : getSelectierteAuftraege()) {
        assi.setProvision(Provision);
        assi.setBonusoeffentlich(BonusAllePiloten);
        assi.setBonusclosed(BonusFGPilot);
        jobFacade.editAssignment(assi);
      }
    }

  }

  public void onAllesEinladen() {
    try {
      jobFacade.alleJobsEinladen(getFlugzeug(selectedJob.getFlugzeugid()).getAktuellePositionICAO(), FgID, selectedJob.getIdjobs(), ArrivalFilter, LadungFilter);
    } catch (NullPointerException e) {
      NewMessage("No Jobs available");
    }

  }

  public void onAllesAusladen() {
    jobFacade.alleJobsAusladen(getFlugzeug(selectedJob.getFlugzeugid()).getAktuellePositionICAO(), FgID, selectedJob.getIdjobs());
  }

  public void onRefresh() {
    setSelectedUserJob(null);
    listLoaded = false;
  }

  public void onRowSelect(SelectEvent event) {

  }

  public void onRowUnselect(UnselectEvent event) {

  }

  public void onRowSelectUserJob(SelectEvent event) {

  }

  public void onRowUnselectUserJob(UnselectEvent event) {

  }

  private void NewMessage(String neueNachricht) {
    FacesMessage msg = new FacesMessage(neueNachricht);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  /*
  Getter and Setter
   */
  private String clearText(String inText) {
    String ClearText = inText.replaceAll("<script", "");
    ClearText = ClearText.replaceAll("</script>", "");
    ClearText = ClearText.replaceAll("<css", "");
    ClearText = ClearText.replaceAll("</css>", "");
    ClearText = ClearText.replaceAll("<refresh", "");
    ClearText = ClearText.replaceAll("<meta", "");
    ClearText = ClearText.replaceAll("<iframe", "");
    ClearText = ClearText.replaceAll("</iframe>", "");

    return ClearText;
  }

  public String getFlugzeugBild(int id) {
    if (id > 0) {
      ViewFlugzeugeMietKauf fg = jobFacade.readFlugzeugMietKauf(id);
      if (fg.getEigenesBildURL().equals("")) {
        return fg.getSymbolUrl();
      } else {
        return fg.getEigenesBildURL();
      }
    } else {
      return "";
    }
  }

  public String getFlugzeugKennung(int id) {
    if (id > 0) {
      ViewFlugzeugeMietKauf fg = jobFacade.readFlugzeugMietKauf(id);
      if (!fg.getRegistrierung().equals("")) {
        return fg.getRegistrierung();
      } else {
        return "N/A";
      }
    } else {
      return "N/A";
    }
  }

  private ViewFlugzeugeMietKauf getFlugzeug(int id) {
    return jobFacade.readFlugzeugMietKauf(id);
  }

  public Jobs getSelectedJob() {
    return selectedJob;
  }

  public void setSelectedJob(Jobs selectedJob) {
    this.selectedJob = selectedJob;
  }

  public Fluggesellschaft getAktuelleFluggesellschaft() {
    return aktuelleFluggesellschaft;
  }

  public void setAktuelleFluggesellschaft(Fluggesellschaft aktuelleFluggesellschaft) {
    this.aktuelleFluggesellschaft = aktuelleFluggesellschaft;
  }

  public boolean isListLoaded() {
    return listLoaded;
  }

  public void setListLoaded(boolean listLoaded) {
    this.listLoaded = listLoaded;
  }

  public Assignement getSelectedVerfuegbarerAuftrag() {
    return selectedVerfuegbarerAuftrag;
  }

  public void setSelectedVerfuegbarerAuftrag(Assignement selectedVerfuegbarerAuftrag) {
    this.selectedVerfuegbarerAuftrag = selectedVerfuegbarerAuftrag;
  }

  public Assignement getSelectedSelectierterAuftrag() {
    return selectedSelectierterAuftrag;
  }

  public void setSelectedSelectierterAuftrag(Assignement selectedSelectierterAuftrag) {
    this.selectedSelectierterAuftrag = selectedSelectierterAuftrag;
  }

  public String getFlugzeugTyp() {
    return FlugzeugTyp;
  }

  public void setFlugzeugTyp(String FlugzeugTyp) {
    this.FlugzeugTyp = FlugzeugTyp;
  }

  public int getSitzeBusiness() {
    return SitzeBusiness;
  }

  public void setSitzeBusiness(int SitzeBusiness) {
    this.SitzeBusiness = SitzeBusiness;
  }

  public int getSitzeEconomy() {
    return SitzeEconomy;
  }

  public void setSitzeEconomy(int SitzeEconomy) {
    this.SitzeEconomy = SitzeEconomy;
  }

  public int getCargoVerfuegbar() {
    return CargoVerfuegbar;
  }

  public void setCargoVerfuegbar(int CargoVerfuegbar) {
    this.CargoVerfuegbar = CargoVerfuegbar;
  }

  public int getMaxPayload() {
    return maxPayload;
  }

  public void setMaxPayload(int maxPayload) {
    this.maxPayload = maxPayload;
  }

  public int getMengeECOSelect() {
    int summe = 0;
    if (getSelectierteAuftraege() != null) {
      for (Assignement assi : getSelectierteAuftraege()) {
        if (assi.getRoutenArt().equals(1)) {
          summe = summe + assi.getAmmount();
        }
      }
    }
    return summe;
  }

  public int getMengeBCSelected() {
    int summe = 0;
    if (getSelectierteAuftraege() != null) {
      for (Assignement assi : getSelectierteAuftraege()) {
        if (assi.getRoutenArt().equals(4)) {
          summe = summe + assi.getAmmount();
        }
      }
    }
    return summe;
  }

  public int getMengeCargoSelected() {
    int summe = 0;
    if (getSelectierteAuftraege() != null) {
      for (Assignement assi : getSelectierteAuftraege()) {
        if (assi.getRoutenArt().equals(2)) {
          summe = summe + assi.getAmmount();
        }
      }
    }
    return summe;
  }

  public int getGewichtGepaeckSelected() {
    int summe = 0;
    if (getSelectierteAuftraege() != null) {
      for (Assignement assi : getSelectierteAuftraege()) {
        if (assi.getRoutenArt() == 1 || assi.getRoutenArt() == 4) {
          summe = summe + assi.getGepaeck();
        }
      }
    }
    return summe;
  }

  public int getGewichtPassagiereEcoSelect() {
    int summe = 0;
    if (getSelectierteAuftraege() != null) {
      for (Assignement assi : getSelectierteAuftraege()) {
        if (assi.getRoutenArt().equals(1)) {
          summe = summe + assi.getGewichtPax();
        }
      }
    }
    return summe;
  }

  public int getGewichtPassagiereBCSelected() {
    int summe = 0;
    if (getSelectierteAuftraege() != null) {
      for (Assignement assi : getSelectierteAuftraege()) {
        if (assi.getRoutenArt().equals(4)) {
          summe = summe + assi.getGewichtPax();
        }
      }
    }
    return summe;
  }

  public double getAuftragswertSelected() {
    double summe = 0;
    if (getSelectierteAuftraege() != null) {
      for (Assignement assi : getSelectierteAuftraege()) {
        summe = summe + assi.getPay();
      }
    }
    return summe;
  }

  public double getPilotenVerguetungNichtFGPilot() {
    double summe = 0;
    if (getSelectierteAuftraege() != null) {
      for (Assignement assi : getSelectierteAuftraege()) {
        summe = summe + assi.getPay();
      }
    }
    return summe;
  }

  public double getBonusAllePiloten() {
    return BonusAllePiloten;
  }

  public void setBonusAllePiloten(double BonusAllePiloten) {
    this.BonusAllePiloten = BonusAllePiloten;
  }

  public double getBonusFGPilot() {
    return BonusFGPilot;
  }

  public void setBonusFGPilot(double BonusFGPilot) {
    this.BonusFGPilot = BonusFGPilot;
  }

  public double getProvision() {
    return Provision;
  }

  public void setProvision(double Provision) {
    this.Provision = Provision;
  }

  public int getUserJobID() {
    return UserJobID;
  }

  public void setUserJobID(int UserJobID) {
    this.UserJobID = UserJobID;
  }

  public double getJobBoerseAuftragswert() {
    return JobBoerseAuftragswert;
  }

  public void setJobBoerseAuftragswert(double JobBoerseAuftragswert) {
    this.JobBoerseAuftragswert = JobBoerseAuftragswert;
  }

  public double getJobBoerseProvision() {
    return JobBoerseProvision;
  }

  public void setJobBoerseProvision(double JobBoerseProvision) {
    this.JobBoerseProvision = JobBoerseProvision;
  }

  public double getJobBoerseBoni1() {
    return JobBoerseBoni1;
  }

  public void setJobBoerseBoni1(double JobBoerseBoni1) {
    this.JobBoerseBoni1 = JobBoerseBoni1;
  }

  public double getJobBoerseBoni2() {
    return JobBoerseBoni2;
  }

  public void setJobBoerseBoni2(double JobBoerseBoni2) {
    this.JobBoerseBoni2 = JobBoerseBoni2;
  }

  public void getJobBoerseAuftragswert(int JobID) {
    JobBoerseAuftragswert = 0.0;
    JobBoerseBoni1 = 0.0;
    JobBoerseBoni2 = 0.0;
    JobBoerseProvision = 0.0;

    for (Assignement assi : jobFacade.getJobAuftraege(JobID)) {
      JobBoerseAuftragswert = JobBoerseAuftragswert + assi.getPay();
      JobBoerseBoni1 = JobBoerseBoni1 + assi.getPay() / 100.0 * assi.getBonusoeffentlich();
      JobBoerseBoni2 = JobBoerseBoni2 + assi.getPay() / 100.0 * assi.getBonusclosed();
      JobBoerseProvision = JobBoerseProvision + assi.getPay() / 100.0 * assi.getProvision();
    }

  }

  public String getJobFlugzeuge() {
    return JobFlugzeuge;
  }

  public void setJobFlugzeuge(String JobFlugzeuge) {
    this.JobFlugzeuge = JobFlugzeuge;
  }

  public String getJobFluggesellschaften() {
    return JobFluggesellschaften;
  }

  public void setJobFluggesellschaften(String JobFluggesellschaften) {
    this.JobFluggesellschaften = JobFluggesellschaften;
  }

  public String getJobArrival() {
    return JobArrival;
  }

  public void setJobArrival(String JobArrival) {
    this.JobArrival = JobArrival;
  }

  public Jobtemplate getSelectedTemplate() {
    return selectedTemplate;
  }

  public void setSelectedTemplate(Jobtemplate selectedTemplate) {
    this.selectedTemplate = selectedTemplate;
  }

  public Jobtemplate getImportTemplate() {
    return importTemplate;
  }

  public void setImportTemplate(Jobtemplate importTemplate) {
    this.importTemplate = importTemplate;
  }

  public ViewJobsAssignments getSelectedUserJob() {
    return selectedUserJob;
  }

  public void setSelectedUserJob(ViewJobsAssignments selectedUserJob) {
    this.selectedUserJob = selectedUserJob;
  }

  public String getArrivalFilter() {
    return ArrivalFilter;
  }

  public void setArrivalFilter(String ArrivalFilter) {
    this.ArrivalFilter = ArrivalFilter;
  }

  public int getLadungFilter() {
    return LadungFilter;
  }

  public void setLadungFilter(int LadungFilter) {
    this.LadungFilter = LadungFilter;
  }

  public int getAnzahlJobs() {
    
    return jobFacade.getJobs();
    
    
//    
//    try {
//      return getPublicJobs().size();
//    } catch (NullPointerException e) {
//      return 0;
//    }

  }

  public void saveMail(String Von, String An, String Betreff, String Nachricht) {

    //Absendermail speichern
    Benutzer absender = getUserDaten(Von);
    Benutzer empfaenger = getUserDaten(An);

    Mail nm = new Mail();
    nm.setUserID(absender.getIdUser());
    nm.setAnID(empfaenger.getIdUser());
    nm.setAnUser(empfaenger.getName1());
    nm.setBetreff(Betreff);
    nm.setDatumZeit(new Date());
    nm.setGelesen(false);
    nm.setKategorie("Posteingang");
    nm.setVonID(absender.getIdUser());
    nm.setVonUser(absender.getName1());
    nm.setNachrichtText(CONF.NoScript(Nachricht));
    jobFacade.saveUserMail(nm);

    //
    //Empfaengermail speichern, wenn nicht gleich absender
    //
    if (!absender.getName1().equals(empfaenger.getName1())) {
      nm = new Mail();
      nm.setUserID(empfaenger.getIdUser());
      nm.setAnID(empfaenger.getIdUser());
      nm.setAnUser(empfaenger.getName1());
      nm.setBetreff(Betreff);
      nm.setDatumZeit(new Date());
      nm.setGelesen(false);
      nm.setKategorie("Posteingang");
      nm.setVonID(absender.getIdUser());
      nm.setVonUser(absender.getName1());
      nm.setNachrichtText(CONF.NoScript(Nachricht));
      jobFacade.saveUserMail(nm);
    }

  }

  private Benutzer getUserDaten(String BenutzerName) {
    return jobFacade.findUser(BenutzerName);
  }

}
