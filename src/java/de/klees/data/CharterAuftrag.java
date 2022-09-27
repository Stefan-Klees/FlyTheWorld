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
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Stefan Klees
 */
@Entity
@Table(name = "charterAuftrag")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "CharterAuftrag.findAll", query = "SELECT c FROM CharterAuftrag c")})
public class CharterAuftrag implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "charterID")
  private Integer charterID;
  @Column(name = "userID")
  private Integer userID;
  @Column(name = "ablaufzeit")
  @Temporal(TemporalType.TIMESTAMP)
  private Date ablaufzeit;

  public CharterAuftrag() {
  }

  public CharterAuftrag(Integer charterID) {
    this.charterID = charterID;
  }

  public Integer getCharterID() {
    return charterID;
  }

  public void setCharterID(Integer charterID) {
    this.charterID = charterID;
  }

  public Integer getUserID() {
    return userID;
  }

  public void setUserID(Integer userID) {
    this.userID = userID;
  }

  public Date getAblaufzeit() {
    return ablaufzeit;
  }

  public void setAblaufzeit(Date ablaufzeit) {
    this.ablaufzeit = ablaufzeit;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (charterID != null ? charterID.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof CharterAuftrag)) {
      return false;
    }
    CharterAuftrag other = (CharterAuftrag) object;
    if ((this.charterID == null && other.charterID != null) || (this.charterID != null && !this.charterID.equals(other.charterID))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.CharterAuftrag[ charterID=" + charterID + " ]";
  }
  
}
