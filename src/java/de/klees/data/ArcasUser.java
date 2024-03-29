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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Stefan Klees
 */
@Entity
@Table(name = "arcas_user")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ArcasUser.findAll", query = "SELECT a FROM ArcasUser a")
  , @NamedQuery(name = "ArcasUser.findById", query = "SELECT a FROM ArcasUser a WHERE a.id = :id")
  , @NamedQuery(name = "ArcasUser.findByUsername", query = "SELECT a FROM ArcasUser a WHERE a.username = :username")
  , @NamedQuery(name = "ArcasUser.findByPassword", query = "SELECT a FROM ArcasUser a WHERE a.password = :password")
  , @NamedQuery(name = "ArcasUser.findByIsactive", query = "SELECT a FROM ArcasUser a WHERE a.isactive = :isactive")})
public class ArcasUser implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @Column(name = "id")
  private Integer id;
  @Size(max = 255)
  @Column(name = "username")
  private String username;
  @Size(max = 255)
  @Column(name = "password")
  private String password;
  @Column(name = "isactive")
  private Integer isactive;

  public ArcasUser() {
  }

  public ArcasUser(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Integer getIsactive() {
    return isactive;
  }

  public void setIsactive(Integer isactive) {
    this.isactive = isactive;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof ArcasUser)) {
      return false;
    }
    ArcasUser other = (ArcasUser) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.ArcasUser[ id=" + id + " ]";
  }
  
}
