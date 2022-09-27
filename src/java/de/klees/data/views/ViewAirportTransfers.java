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
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "view_AirportTransfers")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ViewAirportTransfers.findAll", query = "SELECT v FROM ViewAirportTransfers v")
  , @NamedQuery(name = "ViewAirportTransfers.findByPassagiere", query = "SELECT v FROM ViewAirportTransfers v WHERE v.passagiere = :passagiere")
  , @NamedQuery(name = "ViewAirportTransfers.findByCargo", query = "SELECT v FROM ViewAirportTransfers v WHERE v.cargo = :cargo")
  , @NamedQuery(name = "ViewAirportTransfers.findByDatum", query = "SELECT v FROM ViewAirportTransfers v WHERE v.datum = :datum")
  , @NamedQuery(name = "ViewAirportTransfers.findByIcao", query = "SELECT v FROM ViewAirportTransfers v WHERE v.icao = :icao")})
public class ViewAirportTransfers implements Serializable {

  private static final long serialVersionUID = 1L;
  @Column(name = "passagiere")
  private BigInteger passagiere;
  @Column(name = "cargo")
  private BigInteger cargo;
  @Column(name = "Datum")
  @Temporal(TemporalType.DATE)
  @Id
  private Date datum;
  @Size(max = 15)
  @Column(name = "ICAO")
  private String icao;

  public ViewAirportTransfers() {
  }

  public BigInteger getPassagiere() {
    return passagiere;
  }

  public void setPassagiere(BigInteger passagiere) {
    this.passagiere = passagiere;
  }

  public BigInteger getCargo() {
    return cargo;
  }

  public void setCargo(BigInteger cargo) {
    this.cargo = cargo;
  }

  public Date getDatum() {
    return datum;
  }

  public void setDatum(Date datum) {
    this.datum = datum;
  }

  public String getIcao() {
    return icao;
  }

  public void setIcao(String icao) {
    this.icao = icao;
  }
  
}
