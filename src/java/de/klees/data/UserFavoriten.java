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
@Table(name = "userFavoriten")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "UserFavoriten.findAll", query = "SELECT u FROM UserFavoriten u")})
public class UserFavoriten implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "iduserFavoriten")
  private Integer iduserFavoriten;
  @Column(name = "iduser")
  private Integer iduser;
  @Size(max = 45)
  @Column(name = "icao")
  private String icao;
  @Size(max = 200)
  @Column(name = "bezeichnung")
  private String bezeichnung;

  public UserFavoriten() {
  }

  public UserFavoriten(Integer iduserFavoriten) {
    this.iduserFavoriten = iduserFavoriten;
  }

  public Integer getIduserFavoriten() {
    return iduserFavoriten;
  }

  public void setIduserFavoriten(Integer iduserFavoriten) {
    this.iduserFavoriten = iduserFavoriten;
  }

  public Integer getIduser() {
    return iduser;
  }

  public void setIduser(Integer iduser) {
    this.iduser = iduser;
  }

  public String getIcao() {
    return icao;
  }

  public void setIcao(String icao) {
    this.icao = icao;
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
    hash += (iduserFavoriten != null ? iduserFavoriten.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof UserFavoriten)) {
      return false;
    }
    UserFavoriten other = (UserFavoriten) object;
    if ((this.iduserFavoriten == null && other.iduserFavoriten != null) || (this.iduserFavoriten != null && !this.iduserFavoriten.equals(other.iduserFavoriten))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.UserFavoriten[ iduserFavoriten=" + iduserFavoriten + " ]";
  }
  
}
