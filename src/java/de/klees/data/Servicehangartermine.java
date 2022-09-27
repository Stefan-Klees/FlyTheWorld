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
@Table(name = "servicehangartermine")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Servicehangartermine.findAll", query = "SELECT s FROM Servicehangartermine s")})
public class Servicehangartermine implements Serializable {

  @Column(name = "iduser")
  private Integer iduser;
  @Column(name = "idservicehangar")
  private Integer idservicehangar;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idservicehangartermine")
  private Integer idservicehangartermine;
  @Column(name = "startdatum")
  @Temporal(TemporalType.TIMESTAMP)
  private Date startdatum;
  @Column(name = "enddatum")
  @Temporal(TemporalType.TIMESTAMP)
  private Date enddatum;
  @Size(max = 500)
  @Column(name = "text")
  private String text;
  @Column(name = "dauerminuten")
  private Integer dauerminuten;

  public Servicehangartermine() {
  }

  public Servicehangartermine(Integer idservicehangartermine) {
    this.idservicehangartermine = idservicehangartermine;
  }

  public Integer getIdservicehangartermine() {
    return idservicehangartermine;
  }

  public void setIdservicehangartermine(Integer idservicehangartermine) {
    this.idservicehangartermine = idservicehangartermine;
  }

  public Date getStartdatum() {
    return startdatum;
  }

  public void setStartdatum(Date startdatum) {
    this.startdatum = startdatum;
  }

  public Date getEnddatum() {
    return enddatum;
  }

  public void setEnddatum(Date enddatum) {
    this.enddatum = enddatum;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Integer getDauerminuten() {
    return dauerminuten;
  }

  public void setDauerminuten(Integer dauerminuten) {
    this.dauerminuten = dauerminuten;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idservicehangartermine != null ? idservicehangartermine.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Servicehangartermine)) {
      return false;
    }
    Servicehangartermine other = (Servicehangartermine) object;
    if ((this.idservicehangartermine == null && other.idservicehangartermine != null) || (this.idservicehangartermine != null && !this.idservicehangartermine.equals(other.idservicehangartermine))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Servicehangartermine[ idservicehangartermine=" + idservicehangartermine + " ]";
  }

  public Integer getIduser() {
    return iduser;
  }

  public void setIduser(Integer iduser) {
    this.iduser = iduser;
  }

  public Integer getIdservicehangar() {
    return idservicehangar;
  }

  public void setIdservicehangar(Integer idservicehangar) {
    this.idservicehangar = idservicehangar;
  }
  
}
