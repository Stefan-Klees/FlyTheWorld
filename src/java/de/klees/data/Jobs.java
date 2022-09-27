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

package de.klees.data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Stefan Klees
 */
@Entity
@Table(name = "jobs")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Jobs.findAll", query = "SELECT j FROM Jobs j")})
public class Jobs implements Serializable {

  @Column(name = "nochfrei")
  private Boolean nochfrei;
  @Size(max = 80)
  @Column(name = "usernamePilot")
  private String usernamePilot;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idjobs")
  private Integer idjobs;
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

  public Jobs() {
  }

  public Jobs(Integer idjobs) {
    this.idjobs = idjobs;
  }

  public Integer getIdjobs() {
    return idjobs;
  }

  public void setIdjobs(Integer idjobs) {
    this.idjobs = idjobs;
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

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idjobs != null ? idjobs.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Jobs)) {
      return false;
    }
    Jobs other = (Jobs) object;
    if ((this.idjobs == null && other.idjobs != null) || (this.idjobs != null && !this.idjobs.equals(other.idjobs))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Jobs[ idjobs=" + idjobs + " ]";
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
