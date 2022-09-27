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
@Table(name = "Story")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Story.findAll", query = "SELECT s FROM Story s")})
public class Story implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idStory")
  private Integer idStory;
  @Lob
  @Size(max = 2147483647)
  @Column(name = "storyText")
  private String storyText;
  @Size(max = 80)
  @Column(name = "verfasser")
  private String verfasser;
  @Size(max = 80)
  @Column(name = "bezeichnung")
  private String bezeichnung;
  @Size(max = 80)
  @Column(name = "land")
  private String land;
  @Size(max = 80)
  @Column(name = "bundesland")
  private String bundesland;
  @Column(name = "datumzeit")
  @Temporal(TemporalType.TIMESTAMP)
  private Date datumzeit;
  @Size(max = 45)
  @Column(name = "sprache")
  private String sprache;
  @Column(name = "artdestransports")
  private Integer artdestransports;
  @Size(max = 80)
  @Column(name = "flugzeuglizenz")
  private String flugzeuglizenz;
  @Column(name = "mindestEnfernung")
  private Integer mindestEnfernung;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "verguetungsFaktor")
  private Double verguetungsFaktor;
  @Size(max = 100)
  @Column(name = "FlugzeugKlasse")
  private String flugzeugKlasse;
  @Column(name = "aktiv")
  private Boolean aktiv;
  @Column(name = "maxEntferung")
  private Integer maxEntferung;
  @Column(name = "verguetung")
  private Double verguetung;

  public Story() {
  }

  public Story(Integer idStory) {
    this.idStory = idStory;
  }

  public Integer getIdStory() {
    return idStory;
  }

  public void setIdStory(Integer idStory) {
    this.idStory = idStory;
  }

  public String getStoryText() {
    return storyText;
  }

  public void setStoryText(String storyText) {
    this.storyText = storyText;
  }

  public String getVerfasser() {
    return verfasser;
  }

  public void setVerfasser(String verfasser) {
    this.verfasser = verfasser;
  }

  public String getBezeichnung() {
    return bezeichnung;
  }

  public void setBezeichnung(String bezeichnung) {
    this.bezeichnung = bezeichnung;
  }

  public String getLand() {
    return land;
  }

  public void setLand(String land) {
    this.land = land;
  }

  public String getBundesland() {
    return bundesland;
  }

  public void setBundesland(String bundesland) {
    this.bundesland = bundesland;
  }

  public Date getDatumzeit() {
    return datumzeit;
  }

  public void setDatumzeit(Date datumzeit) {
    this.datumzeit = datumzeit;
  }

  public String getSprache() {
    return sprache;
  }

  public void setSprache(String sprache) {
    this.sprache = sprache;
  }

  public Integer getArtdestransports() {
    return artdestransports;
  }

  public void setArtdestransports(Integer artdestransports) {
    this.artdestransports = artdestransports;
  }

  public String getFlugzeuglizenz() {
    return flugzeuglizenz;
  }

  public void setFlugzeuglizenz(String flugzeuglizenz) {
    this.flugzeuglizenz = flugzeuglizenz;
  }

  public Integer getMindestEnfernung() {
    return mindestEnfernung;
  }

  public void setMindestEnfernung(Integer mindestEnfernung) {
    this.mindestEnfernung = mindestEnfernung;
  }

  public Double getVerguetungsFaktor() {
    return verguetungsFaktor;
  }

  public void setVerguetungsFaktor(Double verguetungsFaktor) {
    this.verguetungsFaktor = verguetungsFaktor;
  }

  public String getFlugzeugKlasse() {
    return flugzeugKlasse;
  }

  public void setFlugzeugKlasse(String flugzeugKlasse) {
    this.flugzeugKlasse = flugzeugKlasse;
  }

  public Boolean getAktiv() {
    return aktiv;
  }

  public void setAktiv(Boolean aktiv) {
    this.aktiv = aktiv;
  }

  public Integer getMaxEntferung() {
    return maxEntferung;
  }

  public void setMaxEntferung(Integer maxEntferung) {
    this.maxEntferung = maxEntferung;
  }

  public Double getVerguetung() {
    return verguetung;
  }

  public void setVerguetung(Double verguetung) {
    this.verguetung = verguetung;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idStory != null ? idStory.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Story)) {
      return false;
    }
    Story other = (Story) object;
    if ((this.idStory == null && other.idStory != null) || (this.idStory != null && !this.idStory.equals(other.idStory))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Story[ idStory=" + idStory + " ]";
  }
  
}
