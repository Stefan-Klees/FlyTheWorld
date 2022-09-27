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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Stefan Klees
 */
@Entity
@Table(name = "fluggesellschaft_flugzeuge")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "FluggesellschaftFlugzeuge.findAll", query = "SELECT f FROM FluggesellschaftFlugzeuge f"),
  @NamedQuery(name = "FluggesellschaftFlugzeuge.findByIdfluggesellschaftFlugzeuge", query = "SELECT f FROM FluggesellschaftFlugzeuge f WHERE f.idfluggesellschaftFlugzeuge = :idfluggesellschaftFlugzeuge"),
  @NamedQuery(name = "FluggesellschaftFlugzeuge.findByIdaircraft", query = "SELECT f FROM FluggesellschaftFlugzeuge f WHERE f.idaircraft = :idaircraft"),
  @NamedQuery(name = "FluggesellschaftFlugzeuge.findByUmsatz", query = "SELECT f FROM FluggesellschaftFlugzeuge f WHERE f.umsatz = :umsatz"),
  @NamedQuery(name = "FluggesellschaftFlugzeuge.findByKilometer", query = "SELECT f FROM FluggesellschaftFlugzeuge f WHERE f.kilometer = :kilometer"),
  @NamedQuery(name = "FluggesellschaftFlugzeuge.findByPassagiere", query = "SELECT f FROM FluggesellschaftFlugzeuge f WHERE f.passagiere = :passagiere"),
  @NamedQuery(name = "FluggesellschaftFlugzeuge.findByWaren", query = "SELECT f FROM FluggesellschaftFlugzeuge f WHERE f.waren = :waren"),
  @NamedQuery(name = "FluggesellschaftFlugzeuge.findByZeit", query = "SELECT f FROM FluggesellschaftFlugzeuge f WHERE f.zeit = :zeit"),
  @NamedQuery(name = "FluggesellschaftFlugzeuge.findByIdowner", query = "SELECT f FROM FluggesellschaftFlugzeuge f WHERE f.idowner = :idowner")})
public class FluggesellschaftFlugzeuge implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idfluggesellschaft_flugzeuge")
  private Integer idfluggesellschaftFlugzeuge;
  @Column(name = "idaircraft")
  private Integer idaircraft;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "umsatz")
  private Double umsatz;
  @Column(name = "kilometer")
  private Integer kilometer;
  @Column(name = "passagiere")
  private Integer passagiere;
  @Column(name = "waren")
  private Integer waren;
  @Column(name = "zeit")
  private Integer zeit;
  @Column(name = "idowner")
  private Integer idowner;

  public FluggesellschaftFlugzeuge() {
  }

  public FluggesellschaftFlugzeuge(Integer idfluggesellschaftFlugzeuge) {
    this.idfluggesellschaftFlugzeuge = idfluggesellschaftFlugzeuge;
  }

  public Integer getIdfluggesellschaftFlugzeuge() {
    return idfluggesellschaftFlugzeuge;
  }

  public void setIdfluggesellschaftFlugzeuge(Integer idfluggesellschaftFlugzeuge) {
    this.idfluggesellschaftFlugzeuge = idfluggesellschaftFlugzeuge;
  }

  public Integer getIdaircraft() {
    return idaircraft;
  }

  public void setIdaircraft(Integer idaircraft) {
    this.idaircraft = idaircraft;
  }

  public Double getUmsatz() {
    return umsatz;
  }

  public void setUmsatz(Double umsatz) {
    this.umsatz = umsatz;
  }

  public Integer getKilometer() {
    return kilometer;
  }

  public void setKilometer(Integer kilometer) {
    this.kilometer = kilometer;
  }

  public Integer getPassagiere() {
    return passagiere;
  }

  public void setPassagiere(Integer passagiere) {
    this.passagiere = passagiere;
  }

  public Integer getWaren() {
    return waren;
  }

  public void setWaren(Integer waren) {
    this.waren = waren;
  }

  public Integer getZeit() {
    return zeit;
  }

  public void setZeit(Integer zeit) {
    this.zeit = zeit;
  }

  public Integer getIdowner() {
    return idowner;
  }

  public void setIdowner(Integer idowner) {
    this.idowner = idowner;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idfluggesellschaftFlugzeuge != null ? idfluggesellschaftFlugzeuge.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof FluggesellschaftFlugzeuge)) {
      return false;
    }
    FluggesellschaftFlugzeuge other = (FluggesellschaftFlugzeuge) object;
    if ((this.idfluggesellschaftFlugzeuge == null && other.idfluggesellschaftFlugzeuge != null) || (this.idfluggesellschaftFlugzeuge != null && !this.idfluggesellschaftFlugzeuge.equals(other.idfluggesellschaftFlugzeuge))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.FluggesellschaftFlugzeuge[ idfluggesellschaftFlugzeuge=" + idfluggesellschaftFlugzeuge + " ]";
  }
  
}
