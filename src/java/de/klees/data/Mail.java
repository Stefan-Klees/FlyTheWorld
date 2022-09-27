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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Stefan Klees
 */
@Entity
@Table(name = "mail")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Mail.findAll", query = "SELECT m FROM Mail m")})
public class Mail implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idmail")
  private Integer idmail;
  @Column(name = "userID")
  private Integer userID;
  @Column(name = "vonID")
  private Integer vonID;
  @Size(max = 250)
  @Column(name = "vonUser")
  private String vonUser;
  @Column(name = "anID")
  private Integer anID;
  @Size(max = 250)
  @Column(name = "anUser")
  private String anUser;
  @Size(max = 512)
  @Column(name = "kategorie")
  private String kategorie;
  @Column(name = "datumZeit")
  @Temporal(TemporalType.TIMESTAMP)
  private Date datumZeit;
  @Lob
  @Size(max = 65535)
  @Column(name = "nachrichtText")
  private String nachrichtText;
  @Size(max = 250)
  @Column(name = "betreff")
  private String betreff;
  @Column(name = "gelesen")
  private Boolean gelesen;

  public Mail() {
  }

  public Mail(Integer idmail) {
    this.idmail = idmail;
  }

  public Integer getIdmail() {
    return idmail;
  }

  public void setIdmail(Integer idmail) {
    this.idmail = idmail;
  }

  public Integer getUserID() {
    return userID;
  }

  public void setUserID(Integer userID) {
    this.userID = userID;
  }

  public Integer getVonID() {
    return vonID;
  }

  public void setVonID(Integer vonID) {
    this.vonID = vonID;
  }

  public String getVonUser() {
    return vonUser;
  }

  public void setVonUser(String vonUser) {
    this.vonUser = vonUser;
  }

  public Integer getAnID() {
    return anID;
  }

  public void setAnID(Integer anID) {
    this.anID = anID;
  }

  public String getAnUser() {
    return anUser;
  }

  public void setAnUser(String anUser) {
    this.anUser = anUser;
  }

  public String getKategorie() {
    return kategorie;
  }

  public void setKategorie(String kategorie) {
    this.kategorie = kategorie;
  }

  public Date getDatumZeit() {
    return datumZeit;
  }

  public void setDatumZeit(Date datumZeit) {
    this.datumZeit = datumZeit;
  }

  public String getNachrichtText() {
    return nachrichtText;
  }

  public void setNachrichtText(String nachrichtText) {
    this.nachrichtText = nachrichtText;
  }

  public String getBetreff() {
    return betreff;
  }

  public void setBetreff(String betreff) {
    this.betreff = betreff;
  }

  public Boolean getGelesen() {
    return gelesen;
  }

  public void setGelesen(Boolean gelesen) {
    this.gelesen = gelesen;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idmail != null ? idmail.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Mail)) {
      return false;
    }
    Mail other = (Mail) object;
    if ((this.idmail == null && other.idmail != null) || (this.idmail != null && !this.idmail.equals(other.idmail))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Mail[ idmail=" + idmail + " ]";
  }
  
}
