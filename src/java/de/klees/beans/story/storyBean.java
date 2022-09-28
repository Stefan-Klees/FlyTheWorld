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
package de.klees.beans.story;

import de.klees.beans.system.CONF;
import de.klees.data.Airport;
import de.klees.data.Story;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author Stefan Klees
 */
public class storyBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<Story> storyList;
  private Story currentStory;
  private Boolean isLoaded;

  private String storyText;
  private String storyVerfasser;
  private Date storyDatum;
  private String storySprache;
  private boolean storyAktiviert;
  private int storyKlasse;
  private int storyAblaufzeit;
  private int storyRoutenArt;
  private String storyFlugzeugLizenz;
  private String storyFlugzeugType;
  private int storyMindestEnfernungInMeilen;
  private String storyKurzbezeichnung;
  private double storyVerguetungsFaktor;
  private String storyLand;
  private String storyBundesland;
  private double storyVerguetung;
  private int storyMaxEnfernungInMeilen;

  private String storyTestText;
  private Map<String, String> routenArt;

  private SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");

  @EJB
  StoryFacade facadeStory;

  public storyBean() {
    isLoaded = false;

    routenArt = new HashMap<>();
    routenArt.put("Passagiere", "1");
    routenArt.put("Cargo", "2");

    setStoryVerfasser((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName"));
    setStorySprache("DE");

    setStoryDatum(new Date());
    setStoryKlasse(1);

    df.setTimeZone(TimeZone.getTimeZone((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ZeitZone")));

  }

  public List<Story> getStoryList() {

    if (!isLoaded) {
      storyList = facadeStory.findAll();
      isLoaded = true;
    }

    return storyList;
  }

  public void onFilterBezeichnung() {

    if (storyKurzbezeichnung.equals("")) {
      storyList = facadeStory.findAll();
    } else {
      storyList = facadeStory.findAllFiltered(storyKurzbezeichnung);
    }

  }

  public List<Story> getAllDistinct() {
    return facadeStory.findAlldistinct();
  }

  public void onCreate() {
    Story newStory = new Story();
    newStory.setDatumzeit(storyDatum);
    newStory.setSprache(storySprache);
    newStory.setStoryText(storyText);
    newStory.setVerfasser(storyVerfasser);
    newStory.setBezeichnung(storyKurzbezeichnung);
    newStory.setArtdestransports(storyRoutenArt);
    newStory.setFlugzeuglizenz(storyFlugzeugLizenz);
    newStory.setFlugzeugKlasse(storyFlugzeugType);
    newStory.setMindestEnfernung(storyMindestEnfernungInMeilen);
    newStory.setVerguetungsFaktor(storyVerguetungsFaktor);
    newStory.setMaxEntferung(storyMaxEnfernungInMeilen);
    newStory.setVerguetung(storyVerguetung);

    facadeStory.create(newStory);
    NewMessage("Story gespeichert");
    isLoaded = false;
  }

  public void onDelete() {
    facadeStory.remove(currentStory);
    NewMessage("Story gelöscht");
    setSelectedStory(null);
    onFilterBezeichnung();
  }

  public void onEdit() {
    facadeStory.edit(currentStory);
    NewMessage("Story gespeichert");
    onFilterBezeichnung();
  }

  public void onCopy() {
    if (currentStory != null) {
      Story copStory = new Story();
      copStory = currentStory;
      copStory.setBezeichnung(currentStory.getBezeichnung());
      copStory.setAktiv(false);
      facadeStory.create(copStory);
      NewMessage("Story kopiert");
      onFilterBezeichnung();
    }

  }

  public void onTest() {

  }

  public void onRefresh() {
    onFilterBezeichnung();
    isLoaded = false;
  }

  public void onInsertFussEintrag(String Art) {
    if (Art.equals("Create")) {
      storyText = storyText + getFussEintrag();
    } else if (Art.equals("Edit")) {
      currentStory.setStoryText(currentStory.getStoryText() + getFussEintrag());
    }
  }

  public void onInsertTemplate(String Art) {
    if (Art.equals("Create")) {
      storyText = storyText + getTemplate();
    } else if (Art.equals("Edit")) {
      currentStory.setStoryText(currentStory.getStoryText() + getTemplate());
    }
  }

  public void onRowSelect(SelectEvent event) {

    Date jetzt = new Date();
    Date jobTime;
    long neueZeit;

    neueZeit = jetzt.getTime() + (long) (30 * 60 * 1000);
    jobTime = new Date(neueZeit);

    String sound = "0";
    if ((boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Sound")) {
      sound = "1";
    }

    setStoryTestText(currentStory.getStoryText()
            .replaceAll("#flughafen_city#", "Saarbrücken")
            .replaceAll("#flughafen_icao#", "EDDR")
            .replaceAll("#standort_city#", "Luxembug")
            .replaceAll("#standort_icao#", "ELLX")
            .replaceAll("#meilen#", "43")
            .replaceAll("#summe#", "8.952,45")
            .replaceAll("#anzahl#", "3")
            .replaceAll("#minuten#", "30")
            .replaceAll("#sound#", sound)
            .replaceAll("#datum#", df.format(jobTime))
            .replaceAll("#geschichte_von#", currentStory.getVerfasser()));
  }

  public void onRowUnselect(UnselectEvent event) {

  }

  private void NewMessage(String neueNachricht) {
    FacesMessage msg = new FacesMessage(neueNachricht);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  /*
  Setter and Getter
   */
  public List<Airport> getBundesLaender() {
    try {
      if (!storyLand.equals("")) {
        return facadeStory.getBundesLaender(storyLand);
      }

    } catch (NullPointerException e) {
      return null;
    }
    return null;
  }

  public String getFussEintrag() {
    return "<br><br><strong>Auftrag:</strong> <strong>Von</strong>: #standort_icao# #standort_city#  <strong>Nach</strong>: #flughafen_icao# #flughafen_city# <br>\n"
            + "<strong>Entfernung</strong>: #meilen# Meilen, <strong>Abflug bis spätestens: </strong>#datum# Uhr<br>\n"
            + "<strong>Vergütung</strong>: #summe# &euro; (abzüglich Lande-, Abfertigungs- und Chartergebühren)<br>\n"
            + "<strong>Gesamtgewicht (kg)</strong>: #gewicht#<br><br>\n"
            + "Job Idee: #geschichte_von#";
  }

  public String getTemplate() {
    return "<table cellpadding='0' cellspacing='0' align='left'>\n"
            + "<tr>\n"
            + "<td><img src='" + CONF.getDomainURL() + "/images/FTW/golden-job.png'></td>\n"
            + "<td>Hier der Text</td>\n"
            + "</tr>\n"
            + "</table>\n"
            + "<br><br><br><br><br><br><br><br><br><br>\n"
            + "<strong>Auftrag:</strong> <strong>Von</strong>: #standort_icao# #standort_city#  <strong>Nach</strong>: #flughafen_icao# #flughafen_city# <br>\n"
            + "<strong>Entfernung</strong>: #meilen# Meilen, <strong>Abflug bis spätestens: </strong>#datum# Uhr.<br>\n"
            + "<strong>Vergütung</strong>: #summe# &euro; (abzüglich Lande-, Abfertigungs- und Chartergebühren)<br>\n"
            + "<strong>Gesamtgewicht (kg)</strong>: #gewicht#<br><br>\n"
            + "Job Idee: #geschichte_von#\n";
  }

  public Story getSelectedStory() {
    return currentStory;
  }

  public void setSelectedStory(Story selectedStory) {
    this.currentStory = selectedStory;
  }

  public String getStoryText() {
    return storyText;
  }

  public void setStoryText(String storyText) {
    this.storyText = storyText;
  }

  public String getStoryVerfasser() {
    return storyVerfasser;
  }

  public void setStoryVerfasser(String storyVerfasser) {
    this.storyVerfasser = storyVerfasser;
  }

  public Date getStoryDatum() {
    return storyDatum;
  }

  public void setStoryDatum(Date storyDatum) {
    this.storyDatum = storyDatum;
  }

  public String getStorySprache() {
    return storySprache;
  }

  public void setStorySprache(String storySprache) {
    this.storySprache = storySprache;
  }

  public int getStoryKlasse() {
    return storyKlasse;
  }

  public void setStoryKlasse(int storyKlasse) {
    this.storyKlasse = storyKlasse;
  }

  public int getStoryAblaufzeit() {
    return storyAblaufzeit;
  }

  public void setStoryAblaufzeit(int storyAblaufzeit) {
    this.storyAblaufzeit = storyAblaufzeit;
  }

  public String getStoryTestText() {
    return storyTestText;
  }

  public void setStoryTestText(String storyTestText) {
    this.storyTestText = storyTestText;
  }

  public int getStoryRoutenArt() {
    return storyRoutenArt;
  }

  public void setStoryRoutenArt(int storyRoutenArt) {
    this.storyRoutenArt = storyRoutenArt;
  }

  public String getStoryFlugzeugLizenz() {
    return storyFlugzeugLizenz;
  }

  public void setStoryFlugzeugLizenz(String storyFlugzeugLizenz) {
    this.storyFlugzeugLizenz = storyFlugzeugLizenz;
  }

  public Map<String, String> getRoutenArt() {
    return routenArt;
  }

  public void setRoutenArt(Map<String, String> routenArt) {
    this.routenArt = routenArt;
  }

  public int getStoryMindestEnfernungInMeilen() {
    return storyMindestEnfernungInMeilen;
  }

  public void setStoryMindestEnfernungInMeilen(int storyMindestEnfernungInMeilen) {
    this.storyMindestEnfernungInMeilen = storyMindestEnfernungInMeilen;
  }

  public String getStoryKurzbezeichnung() {
    return storyKurzbezeichnung;
  }

  public void setStoryKurzbezeichnung(String storyKurzbezeichnung) {
    this.storyKurzbezeichnung = storyKurzbezeichnung;
  }

  public double getStoryVerguetungsFaktor() {
    return storyVerguetungsFaktor;
  }

  public void setStoryVerguetungsFaktor(double storyVerguetungsFaktor) {
    this.storyVerguetungsFaktor = storyVerguetungsFaktor;
  }

  public String getStoryLand() {
    return storyLand;
  }

  public void setStoryLand(String storyLand) {
    this.storyLand = storyLand;
  }

  public String getStoryBundesland() {
    return storyBundesland;
  }

  public void setStoryBundesland(String storyBundesland) {
    this.storyBundesland = storyBundesland;
  }

  public String getStoryFlugzeugType() {
    return storyFlugzeugType;
  }

  public void setStoryFlugzeugType(String storyFlugzeugType) {
    this.storyFlugzeugType = storyFlugzeugType;
  }

  public boolean isStoryAktiviert() {
    return storyAktiviert;
  }

  public void setStoryAktiviert(boolean storyAktiviert) {
    this.storyAktiviert = storyAktiviert;
  }

  public double getStoryVerguetung() {
    return storyVerguetung;
  }

  public void setStoryVerguetung(double storyVerguetung) {
    this.storyVerguetung = storyVerguetung;
  }

  public int getStoryMaxEnfernungInMeilen() {
    return storyMaxEnfernungInMeilen;
  }

  public void setStoryMaxEnfernungInMeilen(int storyMaxEnfernungInMeilen) {
    this.storyMaxEnfernungInMeilen = storyMaxEnfernungInMeilen;
  }

}
