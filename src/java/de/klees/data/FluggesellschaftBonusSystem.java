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
@Table(name = "FluggesellschaftBonusSystem")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "FluggesellschaftBonusSystem.findAll", query = "SELECT f FROM FluggesellschaftBonusSystem f")})
public class FluggesellschaftBonusSystem implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idFluggesellschaftBonusSystem")
  private Integer idFluggesellschaftBonusSystem;
  @Column(name = "idfluggesellschaft")
  private Integer idfluggesellschaft;
  @Size(max = 100)
  @Column(name = "bonusname")
  private String bonusname;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "bonus")
  private Double bonus;
  @Size(max = 250)
  @Column(name = "urlabzeichen")
  private String urlabzeichen;
  @Column(name = "zeit")
  private Integer zeit;

  public FluggesellschaftBonusSystem() {
  }

  public FluggesellschaftBonusSystem(Integer idFluggesellschaftBonusSystem) {
    this.idFluggesellschaftBonusSystem = idFluggesellschaftBonusSystem;
  }

  public Integer getIdFluggesellschaftBonusSystem() {
    return idFluggesellschaftBonusSystem;
  }

  public void setIdFluggesellschaftBonusSystem(Integer idFluggesellschaftBonusSystem) {
    this.idFluggesellschaftBonusSystem = idFluggesellschaftBonusSystem;
  }

  public Integer getIdfluggesellschaft() {
    return idfluggesellschaft;
  }

  public void setIdfluggesellschaft(Integer idfluggesellschaft) {
    this.idfluggesellschaft = idfluggesellschaft;
  }

  public String getBonusname() {
    return bonusname;
  }

  public void setBonusname(String bonusname) {
    this.bonusname = bonusname;
  }

  public Double getBonus() {
    return bonus;
  }

  public void setBonus(Double bonus) {
    this.bonus = bonus;
  }

  public String getUrlabzeichen() {
    return urlabzeichen;
  }

  public void setUrlabzeichen(String urlabzeichen) {
    this.urlabzeichen = urlabzeichen;
  }

  public Integer getZeit() {
    return zeit;
  }

  public void setZeit(Integer zeit) {
    this.zeit = zeit;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idFluggesellschaftBonusSystem != null ? idFluggesellschaftBonusSystem.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof FluggesellschaftBonusSystem)) {
      return false;
    }
    FluggesellschaftBonusSystem other = (FluggesellschaftBonusSystem) object;
    if ((this.idFluggesellschaftBonusSystem == null && other.idFluggesellschaftBonusSystem != null) || (this.idFluggesellschaftBonusSystem != null && !this.idFluggesellschaftBonusSystem.equals(other.idFluggesellschaftBonusSystem))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.FluggesellschaftBonusSystem[ idFluggesellschaftBonusSystem=" + idFluggesellschaftBonusSystem + " ]";
  }
  
}
