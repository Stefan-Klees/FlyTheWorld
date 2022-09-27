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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Stefan Klees
 */
@Entity
@Table(name = "wartungsTabelle")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "WartungsTabelle.findAll", query = "SELECT w FROM WartungsTabelle w")})
public class WartungsTabelle implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idwartungsTabelle")
  private Integer idwartungsTabelle;
  @Column(name = "datumUhrzeit")
  @Temporal(TemporalType.TIMESTAMP)
  private Date datumUhrzeit;
  @Size(max = 250)
  @Column(name = "meldung")
  private String meldung;

  public WartungsTabelle() {
  }

  public WartungsTabelle(Integer idwartungsTabelle) {
    this.idwartungsTabelle = idwartungsTabelle;
  }

  public Integer getIdwartungsTabelle() {
    return idwartungsTabelle;
  }

  public void setIdwartungsTabelle(Integer idwartungsTabelle) {
    this.idwartungsTabelle = idwartungsTabelle;
  }

  public Date getDatumUhrzeit() {
    return datumUhrzeit;
  }

  public void setDatumUhrzeit(Date datumUhrzeit) {
    this.datumUhrzeit = datumUhrzeit;
  }

  public String getMeldung() {
    return meldung;
  }

  public void setMeldung(String meldung) {
    this.meldung = meldung;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idwartungsTabelle != null ? idwartungsTabelle.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof WartungsTabelle)) {
      return false;
    }
    WartungsTabelle other = (WartungsTabelle) object;
    if ((this.idwartungsTabelle == null && other.idwartungsTabelle != null) || (this.idwartungsTabelle != null && !this.idwartungsTabelle.equals(other.idwartungsTabelle))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.WartungsTabelle[ idwartungsTabelle=" + idwartungsTabelle + " ]";
  }
  
}
