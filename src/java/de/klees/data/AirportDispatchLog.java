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
@Table(name = "AirportDispatchLog")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "AirportDispatchLog.findAll", query = "SELECT a FROM AirportDispatchLog a")})
public class AirportDispatchLog implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idAirportDispatchLog")
  private Integer idAirportDispatchLog;
  @Column(name = "IDAirport")
  private Integer iDAirport;
  @Size(max = 45)
  @Column(name = "ICAO")
  private String icao;
  @Size(max = 250)
  @Column(name = "AirportBezeichnung")
  private String airportBezeichnung;
  @Column(name = "datum")
  @Temporal(TemporalType.TIMESTAMP)
  private Date datum;
  @Column(name = "userID")
  private Integer userID;
  @Size(max = 250)
  @Column(name = "UserName")
  private String userName;
  @Size(max = 45)
  @Column(name = "ArtDerAenderung")
  private String artDerAenderung;

  public AirportDispatchLog() {
  }

  public AirportDispatchLog(Integer idAirportDispatchLog) {
    this.idAirportDispatchLog = idAirportDispatchLog;
  }

  public Integer getIdAirportDispatchLog() {
    return idAirportDispatchLog;
  }

  public void setIdAirportDispatchLog(Integer idAirportDispatchLog) {
    this.idAirportDispatchLog = idAirportDispatchLog;
  }

  public Integer getIDAirport() {
    return iDAirport;
  }

  public void setIDAirport(Integer iDAirport) {
    this.iDAirport = iDAirport;
  }

  public String getIcao() {
    return icao;
  }

  public void setIcao(String icao) {
    this.icao = icao;
  }

  public String getAirportBezeichnung() {
    return airportBezeichnung;
  }

  public void setAirportBezeichnung(String airportBezeichnung) {
    this.airportBezeichnung = airportBezeichnung;
  }

  public Date getDatum() {
    return datum;
  }

  public void setDatum(Date datum) {
    this.datum = datum;
  }

  public Integer getUserID() {
    return userID;
  }

  public void setUserID(Integer userID) {
    this.userID = userID;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getArtDerAenderung() {
    return artDerAenderung;
  }

  public void setArtDerAenderung(String artDerAenderung) {
    this.artDerAenderung = artDerAenderung;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idAirportDispatchLog != null ? idAirportDispatchLog.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof AirportDispatchLog)) {
      return false;
    }
    AirportDispatchLog other = (AirportDispatchLog) object;
    if ((this.idAirportDispatchLog == null && other.idAirportDispatchLog != null) || (this.idAirportDispatchLog != null && !this.idAirportDispatchLog.equals(other.idAirportDispatchLog))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.AirportDispatchLog[ idAirportDispatchLog=" + idAirportDispatchLog + " ]";
  }
  
}
