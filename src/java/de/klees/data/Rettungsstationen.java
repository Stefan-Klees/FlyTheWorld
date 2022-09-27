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
@Table(name = "Rettungsstationen")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Rettungsstationen.findAll", query = "SELECT r FROM Rettungsstationen r")})
public class Rettungsstationen implements Serializable {

  @Size(max = 80)
  @Column(name = "stadt")
  private String stadt;
  @Size(max = 250)
  @Column(name = "sceneryURL")
  private String sceneryURL;

  @Column(name = "idFlugzeugMietKauf")
  private Integer idFlugzeugMietKauf;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idRettungsstationen")
  private Integer idRettungsstationen;
  @Size(max = 240)
  @Column(name = "nameRettungsstatione")
  private String nameRettungsstatione;
  @Size(max = 45)
  @Column(name = "kuerzel")
  private String kuerzel;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "laengengrad")
  private Double laengengrad;
  @Column(name = "breitengrad")
  private Double breitengrad;
  @Size(max = 80)
  @Column(name = "land")
  private String land;
  @Size(max = 80)
  @Column(name = "bundesland")
  private String bundesland;
  @Size(max = 80)
  @Column(name = "sprache")
  private String sprache;
  @Column(name = "maxAnzahlSpieler")
  private Integer maxAnzahlSpieler;

  public Rettungsstationen() {
  }

  public Rettungsstationen(Integer idRettungsstationen) {
    this.idRettungsstationen = idRettungsstationen;
  }

  public Integer getIdRettungsstationen() {
    return idRettungsstationen;
  }

  public void setIdRettungsstationen(Integer idRettungsstationen) {
    this.idRettungsstationen = idRettungsstationen;
  }

  public String getNameRettungsstatione() {
    return nameRettungsstatione;
  }

  public void setNameRettungsstatione(String nameRettungsstatione) {
    this.nameRettungsstatione = nameRettungsstatione;
  }

  public String getKuerzel() {
    return kuerzel;
  }

  public void setKuerzel(String kuerzel) {
    this.kuerzel = kuerzel;
  }

  public Double getLaengengrad() {
    return laengengrad;
  }

  public void setLaengengrad(Double laengengrad) {
    this.laengengrad = laengengrad;
  }

  public Double getBreitengrad() {
    return breitengrad;
  }

  public void setBreitengrad(Double breitengrad) {
    this.breitengrad = breitengrad;
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

  public String getSprache() {
    return sprache;
  }

  public void setSprache(String sprache) {
    this.sprache = sprache;
  }

  public Integer getMaxAnzahlSpieler() {
    return maxAnzahlSpieler;
  }

  public void setMaxAnzahlSpieler(Integer maxAnzahlSpieler) {
    this.maxAnzahlSpieler = maxAnzahlSpieler;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idRettungsstationen != null ? idRettungsstationen.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Rettungsstationen)) {
      return false;
    }
    Rettungsstationen other = (Rettungsstationen) object;
    if ((this.idRettungsstationen == null && other.idRettungsstationen != null) || (this.idRettungsstationen != null && !this.idRettungsstationen.equals(other.idRettungsstationen))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Rettungsstationen[ idRettungsstationen=" + idRettungsstationen + " ]";
  }

  public Integer getIdFlugzeugMietKauf() {
    return idFlugzeugMietKauf;
  }

  public void setIdFlugzeugMietKauf(Integer idFlugzeugMietKauf) {
    this.idFlugzeugMietKauf = idFlugzeugMietKauf;
  }

  public String getStadt() {
    return stadt;
  }

  public void setStadt(String stadt) {
    this.stadt = stadt;
  }

  public String getSceneryURL() {
    return sceneryURL;
  }

  public void setSceneryURL(String sceneryURL) {
    this.sceneryURL = sceneryURL;
  }
  
}
