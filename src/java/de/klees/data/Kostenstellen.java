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
@Table(name = "kostenstellen")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Kostenstellen.findAll", query = "SELECT k FROM Kostenstellen k")})
public class Kostenstellen implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idkostenstellen")
  private Integer idkostenstellen;
  @Column(name = "userid")
  private Integer userid;
  @Column(name = "kostenstelle")
  private Integer kostenstelle;
  @Size(max = 250)
  @Column(name = "bezeichnung")
  private String bezeichnung;

  public Kostenstellen() {
  }

  public Kostenstellen(Integer idkostenstellen) {
    this.idkostenstellen = idkostenstellen;
  }

  public Integer getIdkostenstellen() {
    return idkostenstellen;
  }

  public void setIdkostenstellen(Integer idkostenstellen) {
    this.idkostenstellen = idkostenstellen;
  }

  public Integer getUserid() {
    return userid;
  }

  public void setUserid(Integer userid) {
    this.userid = userid;
  }

  public Integer getKostenstelle() {
    return kostenstelle;
  }

  public void setKostenstelle(Integer kostenstelle) {
    this.kostenstelle = kostenstelle;
  }

  public String getBezeichnung() {
    return bezeichnung;
  }

  public void setBezeichnung(String bezeichnung) {
    this.bezeichnung = bezeichnung;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idkostenstellen != null ? idkostenstellen.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Kostenstellen)) {
      return false;
    }
    Kostenstellen other = (Kostenstellen) object;
    if ((this.idkostenstellen == null && other.idkostenstellen != null) || (this.idkostenstellen != null && !this.idkostenstellen.equals(other.idkostenstellen))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Kostenstellen[ idkostenstellen=" + idkostenstellen + " ]";
  }
  
}
