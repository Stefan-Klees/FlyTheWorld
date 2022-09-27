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
@Table(name = "viewFlugzeugVerbrauch")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ViewFlugzeugVerbrauch.findAll", query = "SELECT v FROM ViewFlugzeugVerbrauch v")})
public class ViewFlugzeugVerbrauch implements Serializable {

  private static final long serialVersionUID = 1L;
  @Basic(optional = false)
  @NotNull
  @Column(name = "idFlugzeug")
  @Id
  private int idFlugzeug;
  @Size(max = 250)
  @Column(name = "type")
  private String type;
  @Column(name = "minuten")
  private BigInteger minuten;
  @Column(name = "verbrauch")
  private BigInteger verbrauch;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "miete")
  private Double miete;
  @Column(name = "treibstoffkosten")
  private Double treibstoffkosten;

  public ViewFlugzeugVerbrauch() {
  }

  public int getIdFlugzeug() {
    return idFlugzeug;
  }

  public void setIdFlugzeug(int idFlugzeug) {
    this.idFlugzeug = idFlugzeug;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public BigInteger getMinuten() {
    return minuten;
  }

  public void setMinuten(BigInteger minuten) {
    this.minuten = minuten;
  }

  public BigInteger getVerbrauch() {
    return verbrauch;
  }

  public void setVerbrauch(BigInteger verbrauch) {
    this.verbrauch = verbrauch;
  }

  public Double getMiete() {
    return miete;
  }

  public void setMiete(Double miete) {
    this.miete = miete;
  }

  public Double getTreibstoffkosten() {
    return treibstoffkosten;
  }

  public void setTreibstoffkosten(Double treibstoffkosten) {
    this.treibstoffkosten = treibstoffkosten;
  }
  
}
