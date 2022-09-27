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
import javax.persistence.Lob;
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
@Table(name = "Systemmeldung")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Systemmeldung.findAll", query = "SELECT s FROM Systemmeldung s")
  , @NamedQuery(name = "Systemmeldung.findByIdSystemmeldung", query = "SELECT s FROM Systemmeldung s WHERE s.idSystemmeldung = :idSystemmeldung")
  , @NamedQuery(name = "Systemmeldung.findByAktiv", query = "SELECT s FROM Systemmeldung s WHERE s.aktiv = :aktiv")})
public class Systemmeldung implements Serializable {

  @Size(max = 250)
  @Column(name = "heraldurl")
  private String heraldurl;
  @Column(name = "heraldaktiv")
  private Boolean heraldaktiv;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idSystemmeldung")
  private Integer idSystemmeldung;
  @Lob
  @Size(max = 16777215)
  @Column(name = "meldung")
  private String meldung;
  @Column(name = "aktiv")
  private Boolean aktiv;

  public Systemmeldung() {
  }

  public Systemmeldung(Integer idSystemmeldung) {
    this.idSystemmeldung = idSystemmeldung;
  }

  public Integer getIdSystemmeldung() {
    return idSystemmeldung;
  }

  public void setIdSystemmeldung(Integer idSystemmeldung) {
    this.idSystemmeldung = idSystemmeldung;
  }

  public String getMeldung() {
    return meldung;
  }

  public void setMeldung(String meldung) {
    this.meldung = meldung;
  }

  public Boolean getAktiv() {
    return aktiv;
  }

  public void setAktiv(Boolean aktiv) {
    this.aktiv = aktiv;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idSystemmeldung != null ? idSystemmeldung.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Systemmeldung)) {
      return false;
    }
    Systemmeldung other = (Systemmeldung) object;
    if ((this.idSystemmeldung == null && other.idSystemmeldung != null) || (this.idSystemmeldung != null && !this.idSystemmeldung.equals(other.idSystemmeldung))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Systemmeldung[ idSystemmeldung=" + idSystemmeldung + " ]";
  }

  public String getHeraldurl() {
    return heraldurl;
  }

  public void setHeraldurl(String heraldurl) {
    this.heraldurl = heraldurl;
  }

  public Boolean getHeraldaktiv() {
    return heraldaktiv;
  }

  public void setHeraldaktiv(Boolean heraldaktiv) {
    this.heraldaktiv = heraldaktiv;
  }
  
}
