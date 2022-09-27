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
@Table(name = "jobBeschreibungen")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "JobBeschreibungen.findAll", query = "SELECT j FROM JobBeschreibungen j")})
public class JobBeschreibungen implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idjobs")
  private Integer idjobs;
  @Size(max = 45)
  @Column(name = "aufgabe")
  private String aufgabe;
  @Size(max = 250)
  @Column(name = "bezeichnung")
  private String bezeichnung;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "faktor")
  private Double faktor;
  @Column(name = "zz")
  private Integer zz;
  @Size(max = 45)
  @Column(name = "sprache")
  private String sprache;

  public JobBeschreibungen() {
  }

  public JobBeschreibungen(Integer idjobs) {
    this.idjobs = idjobs;
  }

  public Integer getIdjobs() {
    return idjobs;
  }

  public void setIdjobs(Integer idjobs) {
    this.idjobs = idjobs;
  }

  public String getAufgabe() {
    return aufgabe;
  }

  public void setAufgabe(String aufgabe) {
    this.aufgabe = aufgabe;
  }

  public String getBezeichnung() {
    return bezeichnung;
  }

  public void setBezeichnung(String bezeichnung) {
    this.bezeichnung = bezeichnung;
  }

  public Double getFaktor() {
    return faktor;
  }

  public void setFaktor(Double faktor) {
    this.faktor = faktor;
  }

  public Integer getZz() {
    return zz;
  }

  public void setZz(Integer zz) {
    this.zz = zz;
  }

  public String getSprache() {
    return sprache;
  }

  public void setSprache(String sprache) {
    this.sprache = sprache;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idjobs != null ? idjobs.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof JobBeschreibungen)) {
      return false;
    }
    JobBeschreibungen other = (JobBeschreibungen) object;
    if ((this.idjobs == null && other.idjobs != null) || (this.idjobs != null && !this.idjobs.equals(other.idjobs))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.JobBeschreibungen[ idjobs=" + idjobs + " ]";
  }
  
}
