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

import de.klees.data.Feinabstimmung;
import de.klees.data.Modsql;
import de.klees.data.Systemmeldung;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Stefan Klees
 */
public class configBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private double PreisAVGAS_KG;
  private double PreisAVGAS_Gallon;
  private double PreisJETA_KG;
  private double PreisJETA_Gallon;
  private Systemmeldung systemmeldung;
  private boolean RandomJob;
  private int zoom = 4;

  private double dummy;

  private String sqlAbfrage;

  private int userid;

  public configBean() {
  }

  @EJB

  SystemFacade facadeSystem;

  public void onModAbfrageAusfuehren() {

    NewMessage("Es wurden " + facadeSystem.MODNativSQLExecute(sqlAbfrage) + " Datensätze verarbeitet");

//    if (sqlAbfrage.toLowerCase().contains("select")) {
//      NewMessage("Select noch nicht unterstützt");
//    } else {
//    }
  }

  private void NewMessage(String neueNachricht) {
    FacesMessage msg = new FacesMessage(neueNachricht);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  public void onSupportCharterResetten() {
    sqlAbfrage = "DELETE FROM charterAuftrag where userID = " + userid;
    NewMessage("Es wurde " + facadeSystem.MODNativSQLExecute(sqlAbfrage) + " Charter Sperre entfernt");

    sqlAbfrage = "DELETE FROM assignement where idowner = " + userid + " and type = 3";
    NewMessage("Es wurde " + facadeSystem.MODNativSQLExecute(sqlAbfrage) + " Charter Auftrag gelöscht");

  }

  public void onSupportUserAuftraegeLoeschen() {
    sqlAbfrage = "DELETE FROM assignement where idowner = " + userid;
    NewMessage("Es wurden " + facadeSystem.MODNativSQLExecute(sqlAbfrage) + " Aufträge gelöscht");
  }

  /*
  Setter and Getter
   */
  public Feinabstimmung getConfig() {
    return facadeSystem.readConfig();
  }

  public double getDummy() {
    return 0.0;
  }

  public int getZoom() {
    return zoom;
  }

  public void setZoom(int zoom) {
    this.zoom = zoom;
  }

  public String getPicHintergrund() {
    return getDomainURL() + "/images/ftw-background/background.png";
  }

  public String getBR() {
    return "</br>";
  }

  public String getBoldOn() {
    return "<strong>";
  }

  public String getBoldOff() {
    return "</strong>";
  }


  public String getForumLink() {
    return getDomainURL();
  }

  public String getDonationLink() {
     return getDomainURL();
  }

  public String getWeiterleitung() {
    return "<meta  http-equiv = 'refresh' content = '0; URL=" + getDomainURL() + "' >";
  }

  public String getUpdateText() {
     return getDomainURL();
  }

  public String getUpdateHotfixText() {
     return getDomainURL();
  }

  public String getWebsiteLink() {
    return getDomainURL();
  }

  public String getDomainURL() {
    return CONF.getDomainURL();
  }

  public String getWiKi(){
    return getDomainURL();
  }
  
  public String getWikiRouten() {
    //return "https://openair-alliance.eu/ftw/doc/doku.php?id=routensystem";
    return getDomainURL();
  }

  public String getWikiAirline() {
    //return "https://openair-alliance.eu/ftw/doc/doku.php?id=routensystem";
    return getDomainURL();
  }

  public String getWikiFBO() {
    //return "https://openair-alliance.eu/ftw/doc/doku.php?id=fbos";
    return getDomainURL();
  }

  public String getWikiBlacklist() {
    //return "https://openair-alliance.eu/ftw/doc/doku.php?id=blacklist";
    return getDomainURL();
  }

  public String getPilotenHandbuch(){
    return getDomainURL();
  }
  public String getPilotenHandbuchEN(){
    return getDomainURL();
  }
  
  public String getFluggesellschaftHandbuch(){
    return getDomainURL();
  }

  public String getFluggesellschaftHandbuchEN(){
    return getDomainURL();
  }
  
  
  
  
  public boolean isRandomJob() {
    return CONF.RandomJob;
  }

  public List<Modsql> getSqlAbfragen() {
    return facadeSystem.getSqlAbfragen();
  }

  public String getSqlAbfrage() {
    return sqlAbfrage;
  }

  public void setSqlAbfrage(String sqlAbfrage) {
    this.sqlAbfrage = sqlAbfrage;
  }

  public int getUserid() {
    return userid;
  }

  public void setUserid(int userid) {
    this.userid = userid;
  }

}
