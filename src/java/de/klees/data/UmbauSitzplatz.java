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
@Table(name = "umbauSitzplatz")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "UmbauSitzplatz.findAll", query = "SELECT u FROM UmbauSitzplatz u")})
public class UmbauSitzplatz implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idumbauSitzplatz")
  private Integer idumbauSitzplatz;
  @Column(name = "idSitzplatzKonfiguration")
  private Integer idSitzplatzKonfiguration;
  @Column(name = "idFlugzeugMietKauf")
  private Integer idFlugzeugMietKauf;
  @Column(name = "betrag")
  private Integer betrag;
  @Column(name = "sperrzeit")
  @Temporal(TemporalType.TIMESTAMP)
  private Date sperrzeit;
  @Column(name = "dauerMinuten")
  private Integer dauerMinuten;

  public UmbauSitzplatz() {
  }

  public UmbauSitzplatz(Integer idumbauSitzplatz) {
    this.idumbauSitzplatz = idumbauSitzplatz;
  }

  public Integer getIdumbauSitzplatz() {
    return idumbauSitzplatz;
  }

  public void setIdumbauSitzplatz(Integer idumbauSitzplatz) {
    this.idumbauSitzplatz = idumbauSitzplatz;
  }

  public Integer getIdSitzplatzKonfiguration() {
    return idSitzplatzKonfiguration;
  }

  public void setIdSitzplatzKonfiguration(Integer idSitzplatzKonfiguration) {
    this.idSitzplatzKonfiguration = idSitzplatzKonfiguration;
  }

  public Integer getIdFlugzeugMietKauf() {
    return idFlugzeugMietKauf;
  }

  public void setIdFlugzeugMietKauf(Integer idFlugzeugMietKauf) {
    this.idFlugzeugMietKauf = idFlugzeugMietKauf;
  }

  public Integer getBetrag() {
    return betrag;
  }

  public void setBetrag(Integer betrag) {
    this.betrag = betrag;
  }

  public Date getSperrzeit() {
    return sperrzeit;
  }

  public void setSperrzeit(Date sperrzeit) {
    this.sperrzeit = sperrzeit;
  }

  public Integer getDauerMinuten() {
    return dauerMinuten;
  }

  public void setDauerMinuten(Integer dauerMinuten) {
    this.dauerMinuten = dauerMinuten;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idumbauSitzplatz != null ? idumbauSitzplatz.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof UmbauSitzplatz)) {
      return false;
    }
    UmbauSitzplatz other = (UmbauSitzplatz) object;
    if ((this.idumbauSitzplatz == null && other.idumbauSitzplatz != null) || (this.idumbauSitzplatz != null && !this.idumbauSitzplatz.equals(other.idumbauSitzplatz))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.UmbauSitzplatz[ idumbauSitzplatz=" + idumbauSitzplatz + " ]";
  }
  
}
