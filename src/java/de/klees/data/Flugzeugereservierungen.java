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
@Table(name = "Flugzeuge_reservierungen")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Flugzeugereservierungen.findAll", query = "SELECT f FROM Flugzeugereservierungen f")})
public class Flugzeugereservierungen implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idreservierung")
  private Integer idreservierung;
  @Column(name = "idflugzeugmietkauf")
  private Integer idflugzeugmietkauf;
  @Column(name = "iduser")
  private Integer iduser;
  @Column(name = "idfluggesellschaft")
  private Integer idfluggesellschaft;
  @Column(name = "vondatum")
  @Temporal(TemporalType.TIMESTAMP)
  private Date vondatum;
  @Column(name = "bisdatum")
  @Temporal(TemporalType.TIMESTAMP)
  private Date bisdatum;
  @Lob
  @Size(max = 65535)
  @Column(name = "bemerkung")
  private String bemerkung;

  public Flugzeugereservierungen() {
  }

  public Flugzeugereservierungen(Integer idreservierung) {
    this.idreservierung = idreservierung;
  }

  public Integer getIdreservierung() {
    return idreservierung;
  }

  public void setIdreservierung(Integer idreservierung) {
    this.idreservierung = idreservierung;
  }

  public Integer getIdflugzeugmietkauf() {
    return idflugzeugmietkauf;
  }

  public void setIdflugzeugmietkauf(Integer idflugzeugmietkauf) {
    this.idflugzeugmietkauf = idflugzeugmietkauf;
  }

  public Integer getIduser() {
    return iduser;
  }

  public void setIduser(Integer iduser) {
    this.iduser = iduser;
  }

  public Integer getIdfluggesellschaft() {
    return idfluggesellschaft;
  }

  public void setIdfluggesellschaft(Integer idfluggesellschaft) {
    this.idfluggesellschaft = idfluggesellschaft;
  }

  public Date getVondatum() {
    return vondatum;
  }

  public void setVondatum(Date vondatum) {
    this.vondatum = vondatum;
  }

  public Date getBisdatum() {
    return bisdatum;
  }

  public void setBisdatum(Date bisdatum) {
    this.bisdatum = bisdatum;
  }

  public String getBemerkung() {
    return bemerkung;
  }

  public void setBemerkung(String bemerkung) {
    this.bemerkung = bemerkung;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idreservierung != null ? idreservierung.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Flugzeugereservierungen)) {
      return false;
    }
    Flugzeugereservierungen other = (Flugzeugereservierungen) object;
    if ((this.idreservierung == null && other.idreservierung != null) || (this.idreservierung != null && !this.idreservierung.equals(other.idreservierung))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Flugzeugereservierungen[ idreservierung=" + idreservierung + " ]";
  }
  
}
