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


package de.klees.data.views;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Stefan Klees
 */
@Entity
@Table(name = "viewJobsAssignments")
@XmlRootElement
@NamedQueries({@NamedQuery(name = "ViewJobsAssignments.findAll", query = "SELECT v FROM ViewJobsAssignments v")})
public class ViewJobsAssignments implements Serializable {

  @Column(name = "nochfrei")
  private Boolean nochfrei;
  @Size(max = 80)
  @Column(name = "usernamePilot")
  private String usernamePilot;

  private static final long serialVersionUID = 1L;
  @Basic(optional = false)
  @NotNull
  @Column(name = "idjobs")
  @Id
  private int idjobs;
  @Size(max = 250)
  @Column(name = "type")
  private String type;
  @Column(name = "userid")
  private Integer userid;
  @Column(name = "flugzeugid")
  private Integer flugzeugid;
  @Column(name = "fluggesellschaftid")
  private Integer fluggesellschaftid;
  @Column(name = "erstelltam")
  @Temporal(TemporalType.TIMESTAMP)
  private Date erstelltam;
  @Column(name = "ablaufdatum")
  @Temporal(TemporalType.TIMESTAMP)
  private Date ablaufdatum;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "konventionalstrafe")
  private Double konventionalstrafe;
  @Size(max = 250)
  @Column(name = "erzeugtvonuser")
  private String erzeugtvonuser;
  @Lob
  @Size(max = 65535)
  @Column(name = "beschreibung")
  private String beschreibung;
  @Size(max = 250)
  @Column(name = "extlink")
  private String extlink;
  @Lob
  @Size(max = 65535)
  @Column(name = "kommentar")
  private String kommentar;
  @Column(name = "freigabe")
  private Boolean freigabe;
  @Size(max = 45)
  @Column(name = "location_icao")
  private String locationIcao;
  @Size(max = 45)
  @Column(name = "to_icao")
  private String toIcao;
  @Column(name = "distance")
  private Integer distance;
  @Column(name = "idowner")
  private Integer idowner;
  @Column(name = "userlock")
  private Integer userlock;

  public ViewJobsAssignments() {
  }

  public int getIdjobs() {
    return idjobs;
  }

  public void setIdjobs(int idjobs) {
    this.idjobs = idjobs;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Integer getUserid() {
    return userid;
  }

  public void setUserid(Integer userid) {
    this.userid = userid;
  }

  public Integer getFlugzeugid() {
    return flugzeugid;
  }

  public void setFlugzeugid(Integer flugzeugid) {
    this.flugzeugid = flugzeugid;
  }

  public Integer getFluggesellschaftid() {
    return fluggesellschaftid;
  }

  public void setFluggesellschaftid(Integer fluggesellschaftid) {
    this.fluggesellschaftid = fluggesellschaftid;
  }

  public Date getErstelltam() {
    return erstelltam;
  }

  public void setErstelltam(Date erstelltam) {
    this.erstelltam = erstelltam;
  }

  public Date getAblaufdatum() {
    return ablaufdatum;
  }

  public void setAblaufdatum(Date ablaufdatum) {
    this.ablaufdatum = ablaufdatum;
  }

  public Double getKonventionalstrafe() {
    return konventionalstrafe;
  }

  public void setKonventionalstrafe(Double konventionalstrafe) {
    this.konventionalstrafe = konventionalstrafe;
  }

  public String getErzeugtvonuser() {
    return erzeugtvonuser;
  }

  public void setErzeugtvonuser(String erzeugtvonuser) {
    this.erzeugtvonuser = erzeugtvonuser;
  }

  public String getBeschreibung() {
    return beschreibung;
  }

  public void setBeschreibung(String beschreibung) {
    this.beschreibung = beschreibung;
  }

  public String getExtlink() {
    return extlink;
  }

  public void setExtlink(String extlink) {
    this.extlink = extlink;
  }

  public String getKommentar() {
    return kommentar;
  }

  public void setKommentar(String kommentar) {
    this.kommentar = kommentar;
  }

  public Boolean getFreigabe() {
    return freigabe;
  }

  public void setFreigabe(Boolean freigabe) {
    this.freigabe = freigabe;
  }

  public String getLocationIcao() {
    return locationIcao;
  }

  public void setLocationIcao(String locationIcao) {
    this.locationIcao = locationIcao;
  }

  public String getToIcao() {
    return toIcao;
  }

  public void setToIcao(String toIcao) {
    this.toIcao = toIcao;
  }

  public Integer getDistance() {
    return distance;
  }

  public void setDistance(Integer distance) {
    this.distance = distance;
  }

  public Integer getIdowner() {
    return idowner;
  }

  public void setIdowner(Integer idowner) {
    this.idowner = idowner;
  }

  public Integer getUserlock() {
    return userlock;
  }

  public void setUserlock(Integer userlock) {
    this.userlock = userlock;
  }

  public Boolean getNochfrei() {
    return nochfrei;
  }

  public void setNochfrei(Boolean nochfrei) {
    this.nochfrei = nochfrei;
  }

  public String getUsernamePilot() {
    return usernamePilot;
  }

  public void setUsernamePilot(String usernamePilot) {
    this.usernamePilot = usernamePilot;
  }
  
}
