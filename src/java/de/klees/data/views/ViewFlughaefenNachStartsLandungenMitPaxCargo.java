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
import java.math.BigInteger;
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
@Table(name = "view_FlughaefenNachStartsLandungenMitPaxCargo")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ViewFlughaefenNachStartsLandungenMitPaxCargo.findAll", query = "SELECT v FROM ViewFlughaefenNachStartsLandungenMitPaxCargo v")})
public class ViewFlughaefenNachStartsLandungenMitPaxCargo implements Serializable {

  private static final long serialVersionUID = 1L;
  @Basic(optional = false)
  @NotNull
  @Column(name = "idairport")
  @Id
  private int idairport;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 20)
  @Column(name = "icao")
  private String icao;
  @Size(max = 250)
  @Column(name = "name")
  private String name;
  @Size(max = 250)
  @Column(name = "land")
  private String land;
  @Basic(optional = false)
  @NotNull
  @Column(name = "StartsLandungen")
  private long startsLandungen;
  @Column(name = "pax")
  private BigInteger pax;
  @Column(name = "cargo")
  private BigInteger cargo;

  public ViewFlughaefenNachStartsLandungenMitPaxCargo() {
  }

  public int getIdairport() {
    return idairport;
  }

  public void setIdairport(int idairport) {
    this.idairport = idairport;
  }

  public String getIcao() {
    return icao;
  }

  public void setIcao(String icao) {
    this.icao = icao;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLand() {
    return land;
  }

  public void setLand(String land) {
    this.land = land;
  }

  public long getStartsLandungen() {
    return startsLandungen;
  }

  public void setStartsLandungen(long startsLandungen) {
    this.startsLandungen = startsLandungen;
  }

  public BigInteger getPax() {
    return pax;
  }

  public void setPax(BigInteger pax) {
    this.pax = pax;
  }

  public BigInteger getCargo() {
    return cargo;
  }

  public void setCargo(BigInteger cargo) {
    this.cargo = cargo;
  }
  
}
