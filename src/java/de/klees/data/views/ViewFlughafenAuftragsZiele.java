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
@Table(name = "viewFlughafenAuftragsZiele")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ViewFlughafenAuftragsZiele.findAll", query = "SELECT v FROM ViewFlughafenAuftragsZiele v")})
public class ViewFlughafenAuftragsZiele implements Serializable {

  private static final long serialVersionUID = 1L;
  @Basic(optional = false)
  @NotNull
  @Column(name = "idauftrag")
  @Id
  private int idauftrag;
  @Size(max = 45)
  @Column(name = "location_icao")
  private String locationIcao;
  @Size(max = 45)
  @Column(name = "to_icao")
  private String toIcao;
  @Size(max = 250)
  @Column(name = "name")
  private String name;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "latitude")
  private Double latitude;
  @Column(name = "longitude")
  private Double longitude;

  public ViewFlughafenAuftragsZiele() {
  }

  public int getIdauftrag() {
    return idauftrag;
  }

  public void setIdauftrag(int idauftrag) {
    this.idauftrag = idauftrag;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }
  
}
