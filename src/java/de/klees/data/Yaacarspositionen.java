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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Stefan Klees
 */
@Entity
@Table(name = "yaacarspositionen")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Yaacarspositionen.findAll", query = "SELECT y FROM Yaacarspositionen y")})
public class Yaacarspositionen implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idyaacarspositionen")
  private Integer idyaacarspositionen;
  @Column(name = "idyaacarskopf")
  private Integer idyaacarskopf;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "latitude")
  private Double latitude;
  @Column(name = "longitude")
  private Double longitude;
  @Column(name = "fuelonboard")
  private Integer fuelonboard;
  @Column(name = "ias")
  private Integer ias;
  @Column(name = "tas")
  private Integer tas;
  @Column(name = "gs")
  private Integer gs;
  @Column(name = "oat")
  private Double oat;
  @Column(name = "heading")
  private Integer heading;
  @Column(name = "winddirection")
  private Integer winddirection;
  @Column(name = "windspeed")
  private Integer windspeed;
  @Column(name = "asl")
  private Integer asl;
  @Column(name = "agl")
  private Integer agl;
  @Column(name = "geflogeneentfernung")
  private Integer geflogeneentfernung;
  @Column(name = "geflogenezeitminuten")
  private Integer geflogenezeitminuten;
  @Column(name = "steigrate")
  private Integer steigrate;
  @Column(name = "sendezeit")
  @Temporal(TemporalType.TIMESTAMP)
  private Date sendezeit;

  public Yaacarspositionen() {
  }

  public Yaacarspositionen(Integer idyaacarspositionen) {
    this.idyaacarspositionen = idyaacarspositionen;
  }

  public Integer getIdyaacarspositionen() {
    return idyaacarspositionen;
  }

  public void setIdyaacarspositionen(Integer idyaacarspositionen) {
    this.idyaacarspositionen = idyaacarspositionen;
  }

  public Integer getIdyaacarskopf() {
    return idyaacarskopf;
  }

  public void setIdyaacarskopf(Integer idyaacarskopf) {
    this.idyaacarskopf = idyaacarskopf;
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

  public Integer getFuelonboard() {
    return fuelonboard;
  }

  public void setFuelonboard(Integer fuelonboard) {
    this.fuelonboard = fuelonboard;
  }

  public Integer getIas() {
    return ias;
  }

  public void setIas(Integer ias) {
    this.ias = ias;
  }

  public Integer getTas() {
    return tas;
  }

  public void setTas(Integer tas) {
    this.tas = tas;
  }

  public Integer getGs() {
    return gs;
  }

  public void setGs(Integer gs) {
    this.gs = gs;
  }

  public Double getOat() {
    return oat;
  }

  public void setOat(Double oat) {
    this.oat = oat;
  }

  public Integer getHeading() {
    return heading;
  }

  public void setHeading(Integer heading) {
    this.heading = heading;
  }

  public Integer getWinddirection() {
    return winddirection;
  }

  public void setWinddirection(Integer winddirection) {
    this.winddirection = winddirection;
  }

  public Integer getWindspeed() {
    return windspeed;
  }

  public void setWindspeed(Integer windspeed) {
    this.windspeed = windspeed;
  }

  public Integer getAsl() {
    return asl;
  }

  public void setAsl(Integer asl) {
    this.asl = asl;
  }

  public Integer getAgl() {
    return agl;
  }

  public void setAgl(Integer agl) {
    this.agl = agl;
  }

  public Integer getGeflogeneentfernung() {
    return geflogeneentfernung;
  }

  public void setGeflogeneentfernung(Integer geflogeneentfernung) {
    this.geflogeneentfernung = geflogeneentfernung;
  }

  public Integer getGeflogenezeitminuten() {
    return geflogenezeitminuten;
  }

  public void setGeflogenezeitminuten(Integer geflogenezeitminuten) {
    this.geflogenezeitminuten = geflogenezeitminuten;
  }

  public Integer getSteigrate() {
    return steigrate;
  }

  public void setSteigrate(Integer steigrate) {
    this.steigrate = steigrate;
  }

  public Date getSendezeit() {
    return sendezeit;
  }

  public void setSendezeit(Date sendezeit) {
    this.sendezeit = sendezeit;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idyaacarspositionen != null ? idyaacarspositionen.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Yaacarspositionen)) {
      return false;
    }
    Yaacarspositionen other = (Yaacarspositionen) object;
    if ((this.idyaacarspositionen == null && other.idyaacarspositionen != null) || (this.idyaacarspositionen != null && !this.idyaacarspositionen.equals(other.idyaacarspositionen))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Yaacarspositionen[ idyaacarspositionen=" + idyaacarspositionen + " ]";
  }
  
}
