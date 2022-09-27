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
package de.klees.data.views;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Stefan Klees
 */
@Entity
@Table(name = "viewAssiErweiterteLizenzPruefung")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ViewAssiErweiterteLizenzPruefung.findAll", query = "SELECT v FROM ViewAssiErweiterteLizenzPruefung v")})
public class ViewAssiErweiterteLizenzPruefung implements Serializable {

  private static final long serialVersionUID = 1L;
  @Basic(optional = false)
  @NotNull
  @Column(name = "idassignement")
  @Id
  private int idassignement;
  @Column(name = "idaircraft")
  private Integer idaircraft;
  @Column(name = "active")
  private Integer active;
  @Column(name = "idowner")
  private Integer idowner;
  @Column(name = "userlock")
  private Integer userlock;
  @Column(name = "idAirline")
  private Integer idAirline;
  @Column(name = "idRoute")
  private Integer idRoute;
  @Size(max = 15)
  @Column(name = "lizenz")
  private String lizenz;
  @Column(name = "idjob")
  private Integer idjob;
  @Column(name = "flugzeugklasse")
  private Integer flugzeugklasse;
  @Size(max = 80)
  @Column(name = "FlugzeugLizenz")
  private String flugzeugLizenz;

  public ViewAssiErweiterteLizenzPruefung() {
  }

  public int getIdassignement() {
    return idassignement;
  }

  public void setIdassignement(int idassignement) {
    this.idassignement = idassignement;
  }

  public Integer getIdaircraft() {
    return idaircraft;
  }

  public void setIdaircraft(Integer idaircraft) {
    this.idaircraft = idaircraft;
  }

  public Integer getActive() {
    return active;
  }

  public void setActive(Integer active) {
    this.active = active;
  }

  public Integer getIdowner() {
    return idowner;
  }

  public void setIdowner(Integer idowner) {
    this.idowner = idowner;
  }

  public Integer getUserlock() {
    return userlock;
  }

  public void setUserlock(Integer userlock) {
    this.userlock = userlock;
  }

  public Integer getIdAirline() {
    return idAirline;
  }

  public void setIdAirline(Integer idAirline) {
    this.idAirline = idAirline;
  }

  public Integer getIdRoute() {
    return idRoute;
  }

  public void setIdRoute(Integer idRoute) {
    this.idRoute = idRoute;
  }

  public String getLizenz() {
    return lizenz;
  }

  public void setLizenz(String lizenz) {
    this.lizenz = lizenz;
  }

  public Integer getIdjob() {
    return idjob;
  }

  public void setIdjob(Integer idjob) {
    this.idjob = idjob;
  }

  public Integer getFlugzeugklasse() {
    return flugzeugklasse;
  }

  public void setFlugzeugklasse(Integer flugzeugklasse) {
    this.flugzeugklasse = flugzeugklasse;
  }

  public String getFlugzeugLizenz() {
    return flugzeugLizenz;
  }

  public void setFlugzeugLizenz(String flugzeugLizenz) {
    this.flugzeugLizenz = flugzeugLizenz;
  }
  
}
