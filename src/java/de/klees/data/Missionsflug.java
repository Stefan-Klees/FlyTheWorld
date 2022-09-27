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
@Table(name = "missionsflug")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Missionsflug.findAll", query = "SELECT m FROM Missionsflug m")})
public class Missionsflug implements Serializable {

  @Column(name = "mengepaxausladen")
  private Integer mengepaxausladen;
  @Column(name = "mengecargoausladen")
  private Integer mengecargoausladen;

  @Column(name = "tankenmoeglich")
  private Boolean tankenmoeglich;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idmissionsflug")
  private Integer idmissionsflug;
  @Column(name = "idmissionenwaypoints")
  private Integer idmissionenwaypoints;
  @Column(name = "idmission")
  private Integer idmission;
  @Column(name = "iduser")
  private Integer iduser;
  @Column(name = "idflugzeug")
  private Integer idflugzeug;
  @Column(name = "idyaacarskopf")
  private Integer idyaacarskopf;
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
  @Column(name = "wegpunkterreicht")
  private Boolean wegpunkterreicht;
  @Column(name = "mengecargo")
  private Integer mengecargo;
  @Column(name = "verguetung")
  private Double verguetung;
  @Column(name = "umkreisingrad")
  private Double umkreisingrad;

  public Missionsflug() {
  }

  public Missionsflug(Integer idmissionsflug) {
    this.idmissionsflug = idmissionsflug;
  }

  public Integer getIdmissionsflug() {
    return idmissionsflug;
  }

  public void setIdmissionsflug(Integer idmissionsflug) {
    this.idmissionsflug = idmissionsflug;
  }

  public Integer getIdmissionenwaypoints() {
    return idmissionenwaypoints;
  }

  public void setIdmissionenwaypoints(Integer idmissionenwaypoints) {
    this.idmissionenwaypoints = idmissionenwaypoints;
  }

  public Integer getIdmission() {
    return idmission;
  }

  public void setIdmission(Integer idmission) {
    this.idmission = idmission;
  }

  public Integer getIduser() {
    return iduser;
  }

  public void setIduser(Integer iduser) {
    this.iduser = iduser;
  }

  public Integer getIdflugzeug() {
    return idflugzeug;
  }

  public void setIdflugzeug(Integer idflugzeug) {
    this.idflugzeug = idflugzeug;
  }

  public Integer getIdyaacarskopf() {
    return idyaacarskopf;
  }

  public void setIdyaacarskopf(Integer idyaacarskopf) {
    this.idyaacarskopf = idyaacarskopf;
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

  public Boolean getWegpunkterreicht() {
    return wegpunkterreicht;
  }

  public void setWegpunkterreicht(Boolean wegpunkterreicht) {
    this.wegpunkterreicht = wegpunkterreicht;
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

  public Double getUmkreisingrad() {
    return umkreisingrad;
  }

  public void setUmkreisingrad(Double umkreisingrad) {
    this.umkreisingrad = umkreisingrad;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idmissionsflug != null ? idmissionsflug.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Missionsflug)) {
      return false;
    }
    Missionsflug other = (Missionsflug) object;
    if ((this.idmissionsflug == null && other.idmissionsflug != null) || (this.idmissionsflug != null && !this.idmissionsflug.equals(other.idmissionsflug))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Missionsflug[ idmissionsflug=" + idmissionsflug + " ]";
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
