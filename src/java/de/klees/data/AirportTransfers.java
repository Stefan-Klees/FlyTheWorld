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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Stefan Klees
 */
@Entity
@Table(name = "AirportTransfers")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "AirportTransfers.findAll", query = "SELECT a FROM AirportTransfers a")})
public class AirportTransfers implements Serializable {

  @Size(max = 15)
  @Column(name = "icao")
  private String icao;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idAirportTransfers")
  private Integer idAirportTransfers;
  @Column(name = "datum")
  @Temporal(TemporalType.DATE)
  private Date datum;
  @Column(name = "passengers")
  private Integer passengers;
  @Column(name = "cargo")
  private Integer cargo;

  public AirportTransfers() {
  }

  public AirportTransfers(Integer idAirportTransfers) {
    this.idAirportTransfers = idAirportTransfers;
  }

  public Integer getIdAirportTransfers() {
    return idAirportTransfers;
  }

  public void setIdAirportTransfers(Integer idAirportTransfers) {
    this.idAirportTransfers = idAirportTransfers;
  }

  public Date getDatum() {
    return datum;
  }

  public void setDatum(Date datum) {
    this.datum = datum;
  }

  public Integer getPassengers() {
    return passengers;
  }

  public void setPassengers(Integer passengers) {
    this.passengers = passengers;
  }

  public Integer getCargo() {
    return cargo;
  }

  public void setCargo(Integer cargo) {
    this.cargo = cargo;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idAirportTransfers != null ? idAirportTransfers.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof AirportTransfers)) {
      return false;
    }
    AirportTransfers other = (AirportTransfers) object;
    if ((this.idAirportTransfers == null && other.idAirportTransfers != null) || (this.idAirportTransfers != null && !this.idAirportTransfers.equals(other.idAirportTransfers))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.AirportTransfers[ idAirportTransfers=" + idAirportTransfers + " ]";
  }

  public String getIcao() {
    return icao;
  }

  public void setIcao(String icao) {
    this.icao = icao;
  }
  
}
