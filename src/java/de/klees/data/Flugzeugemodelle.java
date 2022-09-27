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
@Table(name = "Flugzeuge_modelle")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Flugzeugemodelle.findAll", query = "SELECT f FROM Flugzeugemodelle f ORDER BY f.modellName")
  , @NamedQuery(name = "Flugzeugemodelle.findByIdFlugzeugeModelle", query = "SELECT f FROM Flugzeugemodelle f WHERE f.idFlugzeugeModelle = :idFlugzeugeModelle")
  , @NamedQuery(name = "Flugzeugemodelle.findByModellName", query = "SELECT f FROM Flugzeugemodelle f WHERE f.modellName = :modellName")
  , @NamedQuery(name = "Flugzeugemodelle.findByIsAktive", query = "SELECT f FROM Flugzeugemodelle f WHERE f.isAktive = :isAktive")})
public class Flugzeugemodelle implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idFlugzeugeModelle")
  private Integer idFlugzeugeModelle;
  @Size(max = 250)
  @Column(name = "modellName")
  private String modellName;
  @Column(name = "isAktive")
  private Boolean isAktive;

  public Flugzeugemodelle() {
  }

  public Flugzeugemodelle(Integer idFlugzeugeModelle) {
    this.idFlugzeugeModelle = idFlugzeugeModelle;
  }

  public Integer getIdFlugzeugeModelle() {
    return idFlugzeugeModelle;
  }

  public void setIdFlugzeugeModelle(Integer idFlugzeugeModelle) {
    this.idFlugzeugeModelle = idFlugzeugeModelle;
  }

  public String getModellName() {
    return modellName;
  }

  public void setModellName(String modellName) {
    this.modellName = modellName;
  }

  public Boolean getIsAktive() {
    return isAktive;
  }

  public void setIsAktive(Boolean isAktive) {
    this.isAktive = isAktive;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idFlugzeugeModelle != null ? idFlugzeugeModelle.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Flugzeugemodelle)) {
      return false;
    }
    Flugzeugemodelle other = (Flugzeugemodelle) object;
    if ((this.idFlugzeugeModelle == null && other.idFlugzeugeModelle != null) || (this.idFlugzeugeModelle != null && !this.idFlugzeugeModelle.equals(other.idFlugzeugeModelle))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Flugzeugemodelle[ idFlugzeugeModelle=" + idFlugzeugeModelle + " ]";
  }
  
}
