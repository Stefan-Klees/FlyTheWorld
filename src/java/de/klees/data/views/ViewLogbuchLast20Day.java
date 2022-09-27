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
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Stefan Klees
 */
@Entity
@Table(name = "viewLogbuchLast20Day")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ViewLogbuchLast20Day.findAll", query = "SELECT v FROM ViewLogbuchLast20Day v")})
public class ViewLogbuchLast20Day implements Serializable {

  private static final long serialVersionUID = 1L;
  @Basic(optional = false)
  @NotNull
  @Column(name = "idfluglogbuch")
  @Id
  private int idfluglogbuch;
  @Column(name = "datum")
  @Temporal(TemporalType.TIMESTAMP)
  private Date datum;
  @Column(name = "minuten")
  private BigInteger minuten;
  @Column(name = "treibstoff")
  private BigInteger treibstoff;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "buchungsgebuehr")
  private Double buchungsgebuehr;
  @Column(name = "treibstoffkosten")
  private Double treibstoffkosten;
  @Column(name = "pax")
  private BigInteger pax;
  @Column(name = "cargo")
  private BigInteger cargo;

  public ViewLogbuchLast20Day() {
  }

  public int getIdfluglogbuch() {
    return idfluglogbuch;
  }

  public void setIdfluglogbuch(int idfluglogbuch) {
    this.idfluglogbuch = idfluglogbuch;
  }

  public Date getDatum() {
    return datum;
  }

  public void setDatum(Date datum) {
    this.datum = datum;
  }

  public BigInteger getMinuten() {
    return minuten;
  }

  public void setMinuten(BigInteger minuten) {
    this.minuten = minuten;
  }

  public BigInteger getTreibstoff() {
    return treibstoff;
  }

  public void setTreibstoff(BigInteger treibstoff) {
    this.treibstoff = treibstoff;
  }

  public Double getBuchungsgebuehr() {
    return buchungsgebuehr;
  }

  public void setBuchungsgebuehr(Double buchungsgebuehr) {
    this.buchungsgebuehr = buchungsgebuehr;
  }

  public Double getTreibstoffkosten() {
    return treibstoffkosten;
  }

  public void setTreibstoffkosten(Double treibstoffkosten) {
    this.treibstoffkosten = treibstoffkosten;
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
