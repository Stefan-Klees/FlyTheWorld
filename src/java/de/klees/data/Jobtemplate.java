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
@Table(name = "jobtemplate")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Jobtemplate.findAll", query = "SELECT j FROM Jobtemplate j")})
public class Jobtemplate implements Serializable {

  @Column(name = "tausch")
  private Boolean tausch;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idjobtemplate")
  private Integer idjobtemplate;
  @Column(name = "idfluggesellschaft")
  private Integer idfluggesellschaft;
  @Size(max = 250)
  @Column(name = "bezeichnung")
  private String bezeichnung;
  @Lob
  @Size(max = 65535)
  @Column(name = "beschreibung")
  private String beschreibung;

  public Jobtemplate() {
  }

  public Jobtemplate(Integer idjobtemplate) {
    this.idjobtemplate = idjobtemplate;
  }

  public Integer getIdjobtemplate() {
    return idjobtemplate;
  }

  public void setIdjobtemplate(Integer idjobtemplate) {
    this.idjobtemplate = idjobtemplate;
  }

  public Integer getIdfluggesellschaft() {
    return idfluggesellschaft;
  }

  public void setIdfluggesellschaft(Integer idfluggesellschaft) {
    this.idfluggesellschaft = idfluggesellschaft;
  }

  public String getBezeichnung() {
    return bezeichnung;
  }

  public void setBezeichnung(String bezeichnung) {
    this.bezeichnung = bezeichnung;
  }

  public String getBeschreibung() {
    return beschreibung;
  }

  public void setBeschreibung(String beschreibung) {
    this.beschreibung = beschreibung;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idjobtemplate != null ? idjobtemplate.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Jobtemplate)) {
      return false;
    }
    Jobtemplate other = (Jobtemplate) object;
    if ((this.idjobtemplate == null && other.idjobtemplate != null) || (this.idjobtemplate != null && !this.idjobtemplate.equals(other.idjobtemplate))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Jobtemplate[ idjobtemplate=" + idjobtemplate + " ]";
  }

  public Boolean getTausch() {
    return tausch;
  }

  public void setTausch(Boolean tausch) {
    this.tausch = tausch;
  }
  
}
