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
@Table(name = "arcas_flights")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ArcasFlights.findAll", query = "SELECT a FROM ArcasFlights a")
  , @NamedQuery(name = "ArcasFlights.findById", query = "SELECT a FROM ArcasFlights a WHERE a.id = :id")
  , @NamedQuery(name = "ArcasFlights.findByFtwUserID", query = "SELECT a FROM ArcasFlights a WHERE a.ftwUserID = :ftwUserID")
  , @NamedQuery(name = "ArcasFlights.findByAircraft", query = "SELECT a FROM ArcasFlights a WHERE a.aircraft = :aircraft")
  , @NamedQuery(name = "ArcasFlights.findByDeparture", query = "SELECT a FROM ArcasFlights a WHERE a.departure = :departure")
  , @NamedQuery(name = "ArcasFlights.findByDestination", query = "SELECT a FROM ArcasFlights a WHERE a.destination = :destination")
  , @NamedQuery(name = "ArcasFlights.findByAlternate", query = "SELECT a FROM ArcasFlights a WHERE a.alternate = :alternate")
  , @NamedQuery(name = "ArcasFlights.findByRoute", query = "SELECT a FROM ArcasFlights a WHERE a.route = :route")
  , @NamedQuery(name = "ArcasFlights.findByAltitude", query = "SELECT a FROM ArcasFlights a WHERE a.altitude = :altitude")
  , @NamedQuery(name = "ArcasFlights.findByPax", query = "SELECT a FROM ArcasFlights a WHERE a.pax = :pax")
  , @NamedQuery(name = "ArcasFlights.findByCargo", query = "SELECT a FROM ArcasFlights a WHERE a.cargo = :cargo")
  , @NamedQuery(name = "ArcasFlights.findByFlightnumber", query = "SELECT a FROM ArcasFlights a WHERE a.flightnumber = :flightnumber")
  , @NamedQuery(name = "ArcasFlights.findByRules", query = "SELECT a FROM ArcasFlights a WHERE a.rules = :rules")})
public class ArcasFlights implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "id")
  private Integer id;
  @Column(name = "ftwUserID")
  private Integer ftwUserID;
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
  @Size(max = 7)
  @Column(name = "flightnumber")
  private String flightnumber;
  @Size(max = 3)
  @Column(name = "rules")
  private String rules;

  public ArcasFlights() {
  }

  public ArcasFlights(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getFtwUserID() {
    return ftwUserID;
  }

  public void setFtwUserID(Integer ftwUserID) {
    this.ftwUserID = ftwUserID;
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

  public String getFlightnumber() {
    return flightnumber;
  }

  public void setFlightnumber(String flightnumber) {
    this.flightnumber = flightnumber;
  }

  public String getRules() {
    return rules;
  }

  public void setRules(String rules) {
    this.rules = rules;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof ArcasFlights)) {
      return false;
    }
    ArcasFlights other = (ArcasFlights) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.ArcasFlights[ id=" + id + " ]";
  }
  
}
