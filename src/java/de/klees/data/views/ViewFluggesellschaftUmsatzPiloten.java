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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Stefan Klees
 */
@Entity
@Table(name = "viewFluggesellschaftUmsatzPiloten")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ViewFluggesellschaftUmsatzPiloten.findAll", query = "SELECT v FROM ViewFluggesellschaftUmsatzPiloten v")})
public class ViewFluggesellschaftUmsatzPiloten implements Serializable {

  private static final long serialVersionUID = 1L;
  @Column(name = "idflugesellschaft")
  @Id
  private Integer idflugesellschaft;
  @Column(name = "Cargo")
  private BigInteger cargo;
  @Column(name = "Pax")
  private BigInteger pax;
  @Column(name = "Meilen")
  private BigInteger meilen;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "Umsatz")
  private Double umsatz;

  public ViewFluggesellschaftUmsatzPiloten() {
  }

  public Integer getIdflugesellschaft() {
    return idflugesellschaft;
  }

  public void setIdflugesellschaft(Integer idflugesellschaft) {
    this.idflugesellschaft = idflugesellschaft;
  }

  public BigInteger getCargo() {
    return cargo;
  }

  public void setCargo(BigInteger cargo) {
    this.cargo = cargo;
  }

  public BigInteger getPax() {
    return pax;
  }

  public void setPax(BigInteger pax) {
    this.pax = pax;
  }

  public BigInteger getMeilen() {
    return meilen;
  }

  public void setMeilen(BigInteger meilen) {
    this.meilen = meilen;
  }

  public Double getUmsatz() {
    return umsatz;
  }

  public void setUmsatz(Double umsatz) {
    this.umsatz = umsatz;
  }
  
}
