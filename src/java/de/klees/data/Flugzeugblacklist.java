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
@Table(name = "flugzeugblacklist")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Flugzeugblacklist.findAll", query = "SELECT f FROM Flugzeugblacklist f")})
public class Flugzeugblacklist implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idblacklist")
  private Integer idblacklist;
  @Column(name = "userid")
  private Integer userid;
  @Column(name = "flugzeugmietkaufid")
  private Integer flugzeugmietkaufid;
  @Size(max = 200)
  @Column(name = "username")
  private String username;

  public Flugzeugblacklist() {
  }

  public Flugzeugblacklist(Integer idblacklist) {
    this.idblacklist = idblacklist;
  }

  public Integer getIdblacklist() {
    return idblacklist;
  }

  public void setIdblacklist(Integer idblacklist) {
    this.idblacklist = idblacklist;
  }

  public Integer getUserid() {
    return userid;
  }

  public void setUserid(Integer userid) {
    this.userid = userid;
  }

  public Integer getFlugzeugmietkaufid() {
    return flugzeugmietkaufid;
  }

  public void setFlugzeugmietkaufid(Integer flugzeugmietkaufid) {
    this.flugzeugmietkaufid = flugzeugmietkaufid;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idblacklist != null ? idblacklist.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Flugzeugblacklist)) {
      return false;
    }
    Flugzeugblacklist other = (Flugzeugblacklist) object;
    if ((this.idblacklist == null && other.idblacklist != null) || (this.idblacklist != null && !this.idblacklist.equals(other.idblacklist))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Flugzeugblacklist[ idblacklist=" + idblacklist + " ]";
  }
  
}
