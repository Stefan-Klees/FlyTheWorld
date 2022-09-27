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
@Table(name = "viewAbrechnungZieleSumme")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ViewAbrechnungZieleSumme.findAll", query = "SELECT v FROM ViewAbrechnungZieleSumme v")})
public class ViewAbrechnungZieleSumme implements Serializable {

  @Basic(optional = false)
  @NotNull
  @Column(name = "idassignement")
  @Id
  private int idassignement;

  private static final long serialVersionUID = 1L;
  @Size(max = 45)
  @Column(name = "location_icao")
  private String locationIcao;
  @Size(max = 45)
  @Column(name = "to_icao")
  private String toIcao;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "Betrag")
  private Double betrag;
  @Column(name = "idowner")
  private Integer idowner;
  @Column(name = "userlock")
  private Integer userlock;

  public ViewAbrechnungZieleSumme() {
  }

  public String getLocationIcao() {
    return locationIcao;
  }

  public void setLocationIcao(String locationIcao) {
    this.locationIcao = locationIcao;
  }

  public String getToIcao() {
    return toIcao;
  }

  public void setToIcao(String toIcao) {
    this.toIcao = toIcao;
  }

  public Double getBetrag() {
    return betrag;
  }

  public void setBetrag(Double betrag) {
    this.betrag = betrag;
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

  public int getIdassignement() {
    return idassignement;
  }

  public void setIdassignement(int idassignement) {
    this.idassignement = idassignement;
  }
  
}
