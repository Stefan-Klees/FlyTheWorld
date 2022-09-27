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

import de.klees.data.FlughafenKlassen;
import de.klees.data.JobBeschreibungen;
import java.io.Serializable;
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
public class jobBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<JobBeschreibungen> jobList;
  private JobBeschreibungen currentJob;

  private boolean isLoaded;

  //***** Eingabefelder
  private String frmAufgabe;
  private String frmBezeichnung;
  private double frmfaktor;
  private int frmZufall;
  private String frmSprache;

  @EJB
  AssignementFacade jobFacade;

  public List<JobBeschreibungen> getJobs() {
    if (!isLoaded) {
      isLoaded = true;
      jobList = jobFacade.findAllJobs();
    }
    return jobList;
  }

  public List<FlughafenKlassen> getAirportKlassen() {
    return jobFacade.findAllKlassen();

  }

  public void onNeuerJob() {
    frmSprache = "EN";
    frmAufgabe = "P";
  }

  public void onCreate() {
    JobBeschreibungen newJob = new JobBeschreibungen();
    newJob.setAufgabe(frmAufgabe);
    newJob.setFaktor(frmfaktor);
    newJob.setZz(frmZufall);
    newJob.setBezeichnung(frmBezeichnung);
    newJob.setSprache(frmSprache);

    jobFacade.createJob(newJob);
    isLoaded = false;
    NewMessage("Daten gespeichert");

  }

  public void onEdit() {
    jobFacade.editJob(currentJob);
    isLoaded = false;
    NewMessage("Daten gespeichert");
  }

  public void onDelete() {
    jobFacade.removeJob(currentJob);
    isLoaded = false;
    NewMessage("Daten geloescht");
  }

  public void onRowSelect(SelectEvent event) {

  }

  public void onRowUnselect(UnselectEvent event) {

  }

  private void NewMessage(String neueNachricht) {
    FacesMessage msg = new FacesMessage(neueNachricht);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  /*
  ************************ Setter and Getter
   */
  public JobBeschreibungen getSelectedJob() {
    return currentJob;
  }

  public void setSelectedJob(JobBeschreibungen selectedJob) {
    this.currentJob = selectedJob;
  }

  public String getFrmAufgabe() {
    return frmAufgabe;
  }

  public void setFrmAufgabe(String frmAufgabe) {
    this.frmAufgabe = frmAufgabe;
  }

  public String getFrmBezeichnung() {
    return frmBezeichnung;
  }

  public void setFrmBezeichnung(String frmBezeichnung) {
    this.frmBezeichnung = frmBezeichnung;
  }

  public double getFrmfaktor() {
    return frmfaktor;
  }

  public void setFrmfaktor(double frmfaktor) {
    this.frmfaktor = frmfaktor;
  }

  public int getFrmZufall() {
    return frmZufall;
  }

  public void setFrmZufall(int frmZufall) {
    this.frmZufall = frmZufall;
  }


  public String getFrmSprache() {
    return frmSprache;
  }

  public void setFrmSprache(String frmSprache) {
    this.frmSprache = frmSprache;
  }

}
