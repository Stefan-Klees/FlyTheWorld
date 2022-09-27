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
@Table(name = "UserTyperatings")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "UserTyperatings.findAll", query = "SELECT u FROM UserTyperatings u")})
public class UserTyperatings implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idUserTyperatings")
  private Integer idUserTyperatings;
  @Column(name = "userID")
  private Integer userID;
  @Size(max = 80)
  @Column(name = "typeRating")
  private String typeRating;
  @Column(name = "minutenGeflogen")
  private Integer minutenGeflogen;
  @Column(name = "erfuellt")
  private Boolean erfuellt;

  public UserTyperatings() {
  }

  public UserTyperatings(Integer idUserTyperatings) {
    this.idUserTyperatings = idUserTyperatings;
  }

  public Integer getIdUserTyperatings() {
    return idUserTyperatings;
  }

  public void setIdUserTyperatings(Integer idUserTyperatings) {
    this.idUserTyperatings = idUserTyperatings;
  }

  public Integer getUserID() {
    return userID;
  }

  public void setUserID(Integer userID) {
    this.userID = userID;
  }

  public String getTypeRating() {
    return typeRating;
  }

  public void setTypeRating(String typeRating) {
    this.typeRating = typeRating;
  }

  public Integer getMinutenGeflogen() {
    return minutenGeflogen;
  }

  public void setMinutenGeflogen(Integer minutenGeflogen) {
    this.minutenGeflogen = minutenGeflogen;
  }

  public Boolean getErfuellt() {
    return erfuellt;
  }

  public void setErfuellt(Boolean erfuellt) {
    this.erfuellt = erfuellt;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idUserTyperatings != null ? idUserTyperatings.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof UserTyperatings)) {
      return false;
    }
    UserTyperatings other = (UserTyperatings) object;
    if ((this.idUserTyperatings == null && other.idUserTyperatings != null) || (this.idUserTyperatings != null && !this.idUserTyperatings.equals(other.idUserTyperatings))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.UserTyperatings[ idUserTyperatings=" + idUserTyperatings + " ]";
  }
  
}
