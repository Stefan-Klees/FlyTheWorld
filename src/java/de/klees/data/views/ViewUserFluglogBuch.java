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
@Table(name = "viewUserFluglogBuch")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ViewUserFluglogBuch.findAll", query = "SELECT v FROM ViewUserFluglogBuch v")})
public class ViewUserFluglogBuch implements Serializable {

  private static final long serialVersionUID = 1L;
  
  @Column(name = "missionsart")
  private Integer missionsart;
  @Column(name = "iduser")
  private Integer iduser;
  @Basic(optional = false)
  @NotNull
  @Column(name = "idfluglogbuch")
  @Id
  private int idfluglogbuch;
  @Column(name = "landedatum")
  @Temporal(TemporalType.TIMESTAMP)
  private Date landedatum;
  @Size(max = 45)
  @Column(name = "flugnummer")
  private String flugnummer;
  @Size(max = 45)
  @Column(name = "registrierung")
  private String registrierung;
  @Size(max = 250)
  @Column(name = "type")
  private String type;
  @Column(name = "minuten")
  private Integer minuten;
  @Column(name = "miles")
  private Integer miles;
  @Column(name = "pax")
  private Integer pax;
  @Column(name = "cargo")
  private Integer cargo;
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
  @Column(name = "verbrauch")
  private Integer verbrauch;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "mietgebuehr")
  private Double mietgebuehr;

  public ViewUserFluglogBuch() {
  }

  public Integer getIduser() {
    return iduser;
  }

  public void setIduser(Integer iduser) {
    this.iduser = iduser;
  }

  public int getIdfluglogbuch() {
    return idfluglogbuch;
  }

  public void setIdfluglogbuch(int idfluglogbuch) {
    this.idfluglogbuch = idfluglogbuch;
  }

  public Date getLandedatum() {
    return landedatum;
  }

  public void setLandedatum(Date landedatum) {
    this.landedatum = landedatum;
  }

  public String getFlugnummer() {
    return flugnummer;
  }

  public void setFlugnummer(String flugnummer) {
    this.flugnummer = flugnummer;
  }

  public String getRegistrierung() {
    return registrierung;
  }

  public void setRegistrierung(String registrierung) {
    this.registrierung = registrierung;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Integer getMinuten() {
    return minuten;
  }

  public void setMinuten(Integer minuten) {
    this.minuten = minuten;
  }

  public Integer getMiles() {
    return miles;
  }

  public void setMiles(Integer miles) {
    this.miles = miles;
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

  public Integer getVerbrauch() {
    return verbrauch;
  }

  public void setVerbrauch(Integer verbrauch) {
    this.verbrauch = verbrauch;
  }

  public Double getMietgebuehr() {
    return mietgebuehr;
  }

  public void setMietgebuehr(Double mietgebuehr) {
    this.mietgebuehr = mietgebuehr;
  }

  public Integer getMissionsart() {
    return missionsart;
  }

  public void setMissionsart(Integer missionsart) {
    this.missionsart = missionsart;
  }
  
}
