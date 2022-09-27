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
@Table(name = "RettungsZiele")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "RettungsZiele.findAll", query = "SELECT r FROM RettungsZiele r")})
public class RettungsZiele implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idRettungsZiele")
  private Integer idRettungsZiele;
  @Size(max = 100)
  @Column(name = "zielbezeichnung")
  private String zielbezeichnung;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "laengengrad")
  private Double laengengrad;
  @Column(name = "breitengrad")
  private Double breitengrad;

  public RettungsZiele() {
  }

  public RettungsZiele(Integer idRettungsZiele) {
    this.idRettungsZiele = idRettungsZiele;
  }

  public Integer getIdRettungsZiele() {
    return idRettungsZiele;
  }

  public void setIdRettungsZiele(Integer idRettungsZiele) {
    this.idRettungsZiele = idRettungsZiele;
  }

  public String getZielbezeichnung() {
    return zielbezeichnung;
  }

  public void setZielbezeichnung(String zielbezeichnung) {
    this.zielbezeichnung = zielbezeichnung;
  }

  public Double getLaengengrad() {
    return laengengrad;
  }

  public void setLaengengrad(Double laengengrad) {
    this.laengengrad = laengengrad;
  }

  public Double getBreitengrad() {
    return breitengrad;
  }

  public void setBreitengrad(Double breitengrad) {
    this.breitengrad = breitengrad;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idRettungsZiele != null ? idRettungsZiele.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof RettungsZiele)) {
      return false;
    }
    RettungsZiele other = (RettungsZiele) object;
    if ((this.idRettungsZiele == null && other.idRettungsZiele != null) || (this.idRettungsZiele != null && !this.idRettungsZiele.equals(other.idRettungsZiele))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.RettungsZiele[ idRettungsZiele=" + idRettungsZiele + " ]";
  }
  
}
