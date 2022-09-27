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
@Table(name = "Airport_Ziele")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "AirportZiele.findAll", query = "SELECT a FROM AirportZiele a")})
public class AirportZiele implements Serializable {

  @Column(name = "kurs")
  private Integer kurs;
  @Column(name = "entfernung")
  private Integer entfernung;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idListe")
  private Integer idListe;
  @Column(name = "idAirport")
  private Integer idAirport;
  @Column(name = "idZielAirport")
  private Integer idZielAirport;
  @Size(max = 45)
  @Column(name = "ICAOZiel")
  private String iCAOZiel;

  public AirportZiele() {
  }

  public AirportZiele(Integer idListe) {
    this.idListe = idListe;
  }

  public Integer getIdListe() {
    return idListe;
  }

  public void setIdListe(Integer idListe) {
    this.idListe = idListe;
  }

  public Integer getIdAirport() {
    return idAirport;
  }

  public void setIdAirport(Integer idAirport) {
    this.idAirport = idAirport;
  }

  public Integer getIdZielAirport() {
    return idZielAirport;
  }

  public void setIdZielAirport(Integer idZielAirport) {
    this.idZielAirport = idZielAirport;
  }

  public String getICAOZiel() {
    return iCAOZiel;
  }

  public void setICAOZiel(String iCAOZiel) {
    this.iCAOZiel = iCAOZiel;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idListe != null ? idListe.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof AirportZiele)) {
      return false;
    }
    AirportZiele other = (AirportZiele) object;
    if ((this.idListe == null && other.idListe != null) || (this.idListe != null && !this.idListe.equals(other.idListe))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.AirportZiele[ idListe=" + idListe + " ]";
  }

  public Integer getKurs() {
    return kurs;
  }

  public void setKurs(Integer kurs) {
    this.kurs = kurs;
  }

  public Integer getEntfernung() {
    return entfernung;
  }

  public void setEntfernung(Integer entfernung) {
    this.entfernung = entfernung;
  }
  
}
