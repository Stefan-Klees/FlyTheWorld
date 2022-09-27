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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Stefan Klees
 */
@Entity
@Table(name = "missionenwaypoints")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Missionenwaypoints.findAll", query = "SELECT m FROM Missionenwaypoints m")})
public class Missionenwaypoints implements Serializable {

  @Column(name = "mengepaxausladen")
  private Integer mengepaxausladen;
  @Column(name = "mengecargoausladen")
  private Integer mengecargoausladen;

  @Column(name = "tankenmoeglich")
  private Boolean tankenmoeglich;

  @Column(name = "entfernungnaechsterwegpunkt")
  private Integer entfernungnaechsterwegpunkt;

  @Column(name = "umkreisingrad")
  private Double umkreisingrad;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idmissionenwaypoints")
  private Integer idmissionenwaypoints;
  @Column(name = "idmissionen")
  private Integer idmissionen;
  @Lob
  @Size(max = 65535)
  @Column(name = "waypointtext")
  private String waypointtext;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "latitude")
  private Double latitude;
  @Column(name = "longitude")
  private Double longitude;
  @Column(name = "altitude")
  private Integer altitude;
  @Column(name = "reihenfolge")
  private Integer reihenfolge;
  @Column(name = "landen")
  private Boolean landen;
  @Column(name = "ausladen")
  private Boolean ausladen;
  @Column(name = "einladen")
  private Boolean einladen;
  @Column(name = "mengepax")
  private Integer mengepax;
  @Column(name = "mengecargo")
  private Integer mengecargo;
  @Column(name = "verguetung")
  private Double verguetung;

  public Missionenwaypoints() {
  }

  public Missionenwaypoints(Integer idmissionenwaypoints) {
    this.idmissionenwaypoints = idmissionenwaypoints;
  }

  public Integer getIdmissionenwaypoints() {
    return idmissionenwaypoints;
  }

  public void setIdmissionenwaypoints(Integer idmissionenwaypoints) {
    this.idmissionenwaypoints = idmissionenwaypoints;
  }

  public Integer getIdmissionen() {
    return idmissionen;
  }

  public void setIdmissionen(Integer idmissionen) {
    this.idmissionen = idmissionen;
  }

  public String getWaypointtext() {
    return waypointtext;
  }

  public void setWaypointtext(String waypointtext) {
    this.waypointtext = waypointtext;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public Integer getAltitude() {
    return altitude;
  }

  public void setAltitude(Integer altitude) {
    this.altitude = altitude;
  }

  public Integer getReihenfolge() {
    return reihenfolge;
  }

  public void setReihenfolge(Integer reihenfolge) {
    this.reihenfolge = reihenfolge;
  }

  public Boolean getLanden() {
    return landen;
  }

  public void setLanden(Boolean landen) {
    this.landen = landen;
  }

  public Boolean getAusladen() {
    return ausladen;
  }

  public void setAusladen(Boolean ausladen) {
    this.ausladen = ausladen;
  }

  public Boolean getEinladen() {
    return einladen;
  }

  public void setEinladen(Boolean einladen) {
    this.einladen = einladen;
  }

  public Integer getMengepax() {
    return mengepax;
  }

  public void setMengepax(Integer mengepax) {
    this.mengepax = mengepax;
  }

  public Integer getMengecargo() {
    return mengecargo;
  }

  public void setMengecargo(Integer mengecargo) {
    this.mengecargo = mengecargo;
  }

  public Double getVerguetung() {
    return verguetung;
  }

  public void setVerguetung(Double verguetung) {
    this.verguetung = verguetung;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idmissionenwaypoints != null ? idmissionenwaypoints.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Missionenwaypoints)) {
      return false;
    }
    Missionenwaypoints other = (Missionenwaypoints) object;
    if ((this.idmissionenwaypoints == null && other.idmissionenwaypoints != null) || (this.idmissionenwaypoints != null && !this.idmissionenwaypoints.equals(other.idmissionenwaypoints))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Missionenwaypoints[ idmissionenwaypoints=" + idmissionenwaypoints + " ]";
  }

  public Double getUmkreisingrad() {
    return umkreisingrad;
  }

  public void setUmkreisingrad(Double umkreisingrad) {
    this.umkreisingrad = umkreisingrad;
  }

  public Integer getEntfernungnaechsterwegpunkt() {
    return entfernungnaechsterwegpunkt;
  }

  public void setEntfernungnaechsterwegpunkt(Integer entfernungnaechsterwegpunkt) {
    this.entfernungnaechsterwegpunkt = entfernungnaechsterwegpunkt;
  }

  public Boolean getTankenmoeglich() {
    return tankenmoeglich;
  }

  public void setTankenmoeglich(Boolean tankenmoeglich) {
    this.tankenmoeglich = tankenmoeglich;
  }

  public Integer getMengepaxausladen() {
    return mengepaxausladen;
  }

  public void setMengepaxausladen(Integer mengepaxausladen) {
    this.mengepaxausladen = mengepaxausladen;
  }

  public Integer getMengecargoausladen() {
    return mengecargoausladen;
  }

  public void setMengecargoausladen(Integer mengecargoausladen) {
    this.mengecargoausladen = mengecargoausladen;
  }
  
}
