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
@Table(name = "acars_rettungseinsatz")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "AcarsRettungseinsatz.findAll", query = "SELECT a FROM AcarsRettungseinsatz a")})
public class AcarsRettungseinsatz implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idrettungseinsatz")
  private Integer idrettungseinsatz;
  @Column(name = "idftwUser")
  private Integer idftwUser;
  @Column(name = "idselectedauftrag")
  private Integer idselectedauftrag;
  @Column(name = "idselectedstation")
  private Integer idselectedstation;
  @Column(name = "idselectedheli")
  private Integer idselectedheli;
  @Size(max = 7)
  @Column(name = "flightnumber")
  private String flightnumber;
  @Size(max = 80)
  @Column(name = "aircraft")
  private String aircraft;
  @Size(max = 4)
  @Column(name = "departure")
  private String departure;
  @Size(max = 4)
  @Column(name = "destination")
  private String destination;
  @Size(max = 4)
  @Column(name = "alternate")
  private String alternate;
  @Size(max = 255)
  @Column(name = "route")
  private String route;
  @Size(max = 5)
  @Column(name = "altitude")
  private String altitude;
  @Column(name = "pax")
  private Integer pax;
  @Column(name = "cargo")
  private Integer cargo;
  @Size(max = 3)
  @Column(name = "rules")
  private String rules;
  @Column(name = "unfallorterreicht")
  private Boolean unfallorterreicht;
  @Column(name = "klinikerreicht")
  private Boolean klinikerreicht;
  @Column(name = "standorterreicht")
  private Boolean standorterreicht;

  public AcarsRettungseinsatz() {
  }

  public AcarsRettungseinsatz(Integer idrettungseinsatz) {
    this.idrettungseinsatz = idrettungseinsatz;
  }

  public Integer getIdrettungseinsatz() {
    return idrettungseinsatz;
  }

  public void setIdrettungseinsatz(Integer idrettungseinsatz) {
    this.idrettungseinsatz = idrettungseinsatz;
  }

  public Integer getIdftwUser() {
    return idftwUser;
  }

  public void setIdftwUser(Integer idftwUser) {
    this.idftwUser = idftwUser;
  }

  public Integer getIdselectedauftrag() {
    return idselectedauftrag;
  }

  public void setIdselectedauftrag(Integer idselectedauftrag) {
    this.idselectedauftrag = idselectedauftrag;
  }

  public Integer getIdselectedstation() {
    return idselectedstation;
  }

  public void setIdselectedstation(Integer idselectedstation) {
    this.idselectedstation = idselectedstation;
  }

  public Integer getIdselectedheli() {
    return idselectedheli;
  }

  public void setIdselectedheli(Integer idselectedheli) {
    this.idselectedheli = idselectedheli;
  }

  public String getFlightnumber() {
    return flightnumber;
  }

  public void setFlightnumber(String flightnumber) {
    this.flightnumber = flightnumber;
  }

  public String getAircraft() {
    return aircraft;
  }

  public void setAircraft(String aircraft) {
    this.aircraft = aircraft;
  }

  public String getDeparture() {
    return departure;
  }

  public void setDeparture(String departure) {
    this.departure = departure;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public String getAlternate() {
    return alternate;
  }

  public void setAlternate(String alternate) {
    this.alternate = alternate;
  }

  public String getRoute() {
    return route;
  }

  public void setRoute(String route) {
    this.route = route;
  }

  public String getAltitude() {
    return altitude;
  }

  public void setAltitude(String altitude) {
    this.altitude = altitude;
  }

  public Integer getPax() {
    return pax;
  }

  public void setPax(Integer pax) {
    this.pax = pax;
  }

  public Integer getCargo() {
    return cargo;
  }

  public void setCargo(Integer cargo) {
    this.cargo = cargo;
  }

  public String getRules() {
    return rules;
  }

  public void setRules(String rules) {
    this.rules = rules;
  }

  public Boolean getUnfallorterreicht() {
    return unfallorterreicht;
  }

  public void setUnfallorterreicht(Boolean unfallorterreicht) {
    this.unfallorterreicht = unfallorterreicht;
  }

  public Boolean getKlinikerreicht() {
    return klinikerreicht;
  }

  public void setKlinikerreicht(Boolean klinikerreicht) {
    this.klinikerreicht = klinikerreicht;
  }

  public Boolean getStandorterreicht() {
    return standorterreicht;
  }

  public void setStandorterreicht(Boolean standorterreicht) {
    this.standorterreicht = standorterreicht;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idrettungseinsatz != null ? idrettungseinsatz.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof AcarsRettungseinsatz)) {
      return false;
    }
    AcarsRettungseinsatz other = (AcarsRettungseinsatz) object;
    if ((this.idrettungseinsatz == null && other.idrettungseinsatz != null) || (this.idrettungseinsatz != null && !this.idrettungseinsatz.equals(other.idrettungseinsatz))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.AcarsRettungseinsatz[ idrettungseinsatz=" + idrettungseinsatz + " ]";
  }
  
}
