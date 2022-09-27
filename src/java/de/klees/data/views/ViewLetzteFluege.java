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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Stefan Klees
 */
@Entity
@Table(name = "view_LetzteFluege")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ViewLetzteFluege.findAll", query = "SELECT v FROM ViewLetzteFluege v")})
public class ViewLetzteFluege implements Serializable {

  private static final long serialVersionUID = 1L;
  @Size(max = 250)
  @Column(name = "type")
  private String type;
  @Size(max = 45)
  @Column(name = "registrierung")
  private String registrierung;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 80)
  @Column(name = "name")
  private String name;
  @Column(name = "flugDatum")
  @Temporal(TemporalType.TIMESTAMP)
  private Date flugDatum;
  @Size(max = 45)
  @Column(name = "acarsFlugNummer")
  private String acarsFlugNummer;
  @Size(max = 45)
  @Column(name = "fromicao")
  private String fromicao;
  @Size(max = 250)
  @Column(name = "fromIcaoFlughafenName")
  private String fromIcaoFlughafenName;
  @Size(max = 45)
  @Column(name = "toicao")
  private String toicao;
  @Size(max = 250)
  @Column(name = "tocaoFlughafenName")
  private String tocaoFlughafenName;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "buchungsgebuehr")
  private Double buchungsgebuehr;
  @Column(name = "pax")
  private Integer pax;
  @Column(name = "cargo")
  private Integer cargo;
  @Column(name = "miles")
  private Integer miles;
  @Column(name = "flugzeitMinuten")
  private Integer flugzeitMinuten;
  @Basic(optional = false)
  @NotNull
  @Column(name = "idfluglogbuch")
  @Id
  private int idfluglogbuch;

  public ViewLetzteFluege() {
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getRegistrierung() {
    return registrierung;
  }

  public void setRegistrierung(String registrierung) {
    this.registrierung = registrierung;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getFlugDatum() {
    return flugDatum;
  }

  public void setFlugDatum(Date flugDatum) {
    this.flugDatum = flugDatum;
  }

  public String getAcarsFlugNummer() {
    return acarsFlugNummer;
  }

  public void setAcarsFlugNummer(String acarsFlugNummer) {
    this.acarsFlugNummer = acarsFlugNummer;
  }

  public String getFromicao() {
    return fromicao;
  }

  public void setFromicao(String fromicao) {
    this.fromicao = fromicao;
  }

  public String getFromIcaoFlughafenName() {
    return fromIcaoFlughafenName;
  }

  public void setFromIcaoFlughafenName(String fromIcaoFlughafenName) {
    this.fromIcaoFlughafenName = fromIcaoFlughafenName;
  }

  public String getToicao() {
    return toicao;
  }

  public void setToicao(String toicao) {
    this.toicao = toicao;
  }

  public String getTocaoFlughafenName() {
    return tocaoFlughafenName;
  }

  public void setTocaoFlughafenName(String tocaoFlughafenName) {
    this.tocaoFlughafenName = tocaoFlughafenName;
  }

  public Double getBuchungsgebuehr() {
    return buchungsgebuehr;
  }

  public void setBuchungsgebuehr(Double buchungsgebuehr) {
    this.buchungsgebuehr = buchungsgebuehr;
  }

  public Integer getPax() {
    return pax;
  }

  public void setPax(Integer pax) {
    this.pax = pax;
  }

  public Integer getCargo() {
    return cargo;
  }

  public void setCargo(Integer cargo) {
    this.cargo = cargo;
  }

  public Integer getMiles() {
    return miles;
  }

  public void setMiles(Integer miles) {
    this.miles = miles;
  }

  public Integer getFlugzeitMinuten() {
    return flugzeitMinuten;
  }

  public void setFlugzeitMinuten(Integer flugzeitMinuten) {
    this.flugzeitMinuten = flugzeitMinuten;
  }

  public int getIdfluglogbuch() {
    return idfluglogbuch;
  }

  public void setIdfluglogbuch(int idfluglogbuch) {
    this.idfluglogbuch = idfluglogbuch;
  }
  
}
