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
@Table(name = "viewFlugProvisionen")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ViewFlugProvisionen.findAll", query = "SELECT v FROM ViewFlugProvisionen v")})
public class ViewFlugProvisionen implements Serializable {

  private static final long serialVersionUID = 1L;
  @Size(max = 250)
  @Column(name = "KontoName")
  private String kontoName;
  @Size(max = 80)
  @Column(name = "BankKonto")
  private String bankKonto;
  @Basic(optional = false)
  @NotNull
  @Column(name = "idAirline")
  @Id
  private int idAirline;
  @Column(name = "kostenstelle")
  private Integer kostenstelle;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "GesamtBetrag")
  private Double gesamtBetrag;
  @Column(name = "Provision")
  private Double provision;
  @Column(name = "idowner")
  private Integer idowner;
  @Size(max = 45)
  @Column(name = "icao")
  private String icao;

  public ViewFlugProvisionen() {
  }

  public String getKontoName() {
    return kontoName;
  }

  public void setKontoName(String kontoName) {
    this.kontoName = kontoName;
  }

  public String getBankKonto() {
    return bankKonto;
  }

  public void setBankKonto(String bankKonto) {
    this.bankKonto = bankKonto;
  }

  public int getIdAirline() {
    return idAirline;
  }

  public void setIdAirline(int idAirline) {
    this.idAirline = idAirline;
  }

  public Integer getKostenstelle() {
    return kostenstelle;
  }

  public void setKostenstelle(Integer kostenstelle) {
    this.kostenstelle = kostenstelle;
  }

  public Double getGesamtBetrag() {
    return gesamtBetrag;
  }

  public void setGesamtBetrag(Double gesamtBetrag) {
    this.gesamtBetrag = gesamtBetrag;
  }

  public Double getProvision() {
    return provision;
  }

  public void setProvision(Double provision) {
    this.provision = provision;
  }

  public Integer getIdowner() {
    return idowner;
  }

  public void setIdowner(Integer idowner) {
    this.idowner = idowner;
  }

  public String getIcao() {
    return icao;
  }

  public void setIcao(String icao) {
    this.icao = icao;
  }
  
}
