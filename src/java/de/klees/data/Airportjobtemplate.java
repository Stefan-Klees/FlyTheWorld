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
@Table(name = "airportjobtemplate")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Airportjobtemplate.findAll", query = "SELECT a FROM Airportjobtemplate a")})
public class Airportjobtemplate implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idtemplate")
  private Integer idtemplate;
  @Size(max = 45)
  @Column(name = "sprache")
  private String sprache;
  @Size(max = 80)
  @Column(name = "templatename")
  private String templatename;
  @Lob
  @Size(max = 65535)
  @Column(name = "beschreibung")
  private String beschreibung;
  @Column(name = "jobart")
  private Integer jobart;
  @Column(name = "minwert")
  private Integer minwert;
  @Column(name = "maxwert")
  private Integer maxwert;
  @Size(max = 45)
  @Column(name = "preisklasse")
  private String preisklasse;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "faktor")
  private Double faktor;
  @Column(name = "zz")
  private Integer zz;

  public Airportjobtemplate() {
  }

  public Airportjobtemplate(Integer idtemplate) {
    this.idtemplate = idtemplate;
  }

  public Integer getIdtemplate() {
    return idtemplate;
  }

  public void setIdtemplate(Integer idtemplate) {
    this.idtemplate = idtemplate;
  }

  public String getSprache() {
    return sprache;
  }

  public void setSprache(String sprache) {
    this.sprache = sprache;
  }

  public String getTemplatename() {
    return templatename;
  }

  public void setTemplatename(String templatename) {
    this.templatename = templatename;
  }

  public String getBeschreibung() {
    return beschreibung;
  }

  public void setBeschreibung(String beschreibung) {
    this.beschreibung = beschreibung;
  }

  public Integer getJobart() {
    return jobart;
  }

  public void setJobart(Integer jobart) {
    this.jobart = jobart;
  }

  public Integer getMinwert() {
    return minwert;
  }

  public void setMinwert(Integer minwert) {
    this.minwert = minwert;
  }

  public Integer getMaxwert() {
    return maxwert;
  }

  public void setMaxwert(Integer maxwert) {
    this.maxwert = maxwert;
  }

  public String getPreisklasse() {
    return preisklasse;
  }

  public void setPreisklasse(String preisklasse) {
    this.preisklasse = preisklasse;
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

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idtemplate != null ? idtemplate.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Airportjobtemplate)) {
      return false;
    }
    Airportjobtemplate other = (Airportjobtemplate) object;
    if ((this.idtemplate == null && other.idtemplate != null) || (this.idtemplate != null && !this.idtemplate.equals(other.idtemplate))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Airportjobtemplate[ idtemplate=" + idtemplate + " ]";
  }
  
}
