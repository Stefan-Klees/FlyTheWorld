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

package de.klees.data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Stefan Klees
 */
@Entity
@Table(name = "fluglogbuch")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Fluglogbuch.findAll", query = "SELECT f FROM Fluglogbuch f")})
public class Fluglogbuch implements Serializable {

  @Column(name = "startgebuehr")
  private Double startgebuehr;

  @Column(name = "fixkosten")
  private Double fixkosten;

  private static final long serialVersionUID = 1L;
  @Column(name = "idTerminal")
  private Integer idTerminal;
  @Size(max = 45)
  @Column(name = "registrierung")
  private String registrierung;
  @Column(name = "missionsart")
  private Integer missionsart;

  @Column(name = "betragAriivalTerminal")
  private Double betragAriivalTerminal;
  @Column(name = "betragDepartureTerminal")
  private Double betragDepartureTerminal;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idfluglogbuch")
  private Integer idfluglogbuch;
  @Column(name = "iduser")
  private Integer iduser;
  @Column(name = "idaircraft")
  private Integer idaircraft;
  @Column(name = "idAirline")
  private Integer idAirline;
  @Column(name = "idArrivalTerminal")
  private Integer idArrivalTerminal;
  @Column(name = "idDepartureTerminal")
  private Integer idDepartureTerminal;
  @Size(max = 45)
  @Column(name = "acarsFlugNummer")
  private String acarsFlugNummer;
  @Column(name = "flugDatum")
  @Temporal(TemporalType.TIMESTAMP)
  private Date flugDatum;
  @Size(max = 45)
  @Column(name = "fromicao")
  private String fromicao;
  @Size(max = 45)
  @Column(name = "toicao")
  private String toicao;
  @Column(name = "pax")
  private Integer pax;
  @Column(name = "cargo")
  private Integer cargo;
  @Column(name = "miles")
  private Integer miles;
  @Column(name = "flugzeitMinuten")
  private Integer flugzeitMinuten;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "sicherheitsgebuehr")
  private Double sicherheitsgebuehr;
  @Column(name = "landegebuehr")
  private Double landegebuehr;
  @Column(name = "buchungsgebuehr")
  private Double buchungsgebuehr;
  @Column(name = "treibstoffkosten")
  private Double treibstoffkosten;
  @Column(name = "mietgebuehr")
  private Double mietgebuehr;
  @Column(name = "crewgebuehren")
  private Double crewgebuehren;
  @Column(name = "bodenpersonalgebuehr")
  private Double bodenpersonalgebuehr;
  @Column(name = "summekosten")
  private Double summekosten;
  @Column(name = "summeabrechnung")
  private Double summeabrechnung;
  @Column(name = "bonus1")
  private Double bonus1;
  @Column(name = "bonus2")
  private Double bonus2;
  @Column(name = "treibstoffverbrauchkg")
  private Integer treibstoffverbrauchkg;
  @Size(max = 250)
  @Column(name = "fromIcaoFlughafenName")
  private String fromIcaoFlughafenName;
  @Size(max = 250)
  @Column(name = "tocaoFlughafenName")
  private String tocaoFlughafenName;
  @Column(name = "provision")
  private Double provision;

  public Fluglogbuch() {
  }

  public Fluglogbuch(Integer idfluglogbuch) {
    this.idfluglogbuch = idfluglogbuch;
  }

  public Integer getIdfluglogbuch() {
    return idfluglogbuch;
  }

  public void setIdfluglogbuch(Integer idfluglogbuch) {
    this.idfluglogbuch = idfluglogbuch;
  }

  public Integer getIduser() {
    return iduser;
  }

  public void setIduser(Integer iduser) {
    this.iduser = iduser;
  }

  public Integer getIdaircraft() {
    return idaircraft;
  }

  public void setIdaircraft(Integer idaircraft) {
    this.idaircraft = idaircraft;
  }

  public Integer getIdAirline() {
    return idAirline;
  }

  public void setIdAirline(Integer idAirline) {
    this.idAirline = idAirline;
  }

  public Integer getIdArrivalTerminal() {
    return idArrivalTerminal;
  }

  public void setIdArrivalTerminal(Integer idArrivalTerminal) {
    this.idArrivalTerminal = idArrivalTerminal;
  }

  public Integer getIdDepartureTerminal() {
    return idDepartureTerminal;
  }

  public void setIdDepartureTerminal(Integer idDepartureTerminal) {
    this.idDepartureTerminal = idDepartureTerminal;
  }

  public String getAcarsFlugNummer() {
    return acarsFlugNummer;
  }

  public void setAcarsFlugNummer(String acarsFlugNummer) {
    this.acarsFlugNummer = acarsFlugNummer;
  }

  public Date getFlugDatum() {
    return flugDatum;
  }

  public void setFlugDatum(Date flugDatum) {
    this.flugDatum = flugDatum;
  }

  public String getFromicao() {
    return fromicao;
  }

  public void setFromicao(String fromicao) {
    this.fromicao = fromicao;
  }

  public String getToicao() {
    return toicao;
  }

  public void setToicao(String toicao) {
    this.toicao = toicao;
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

  public Double getSicherheitsgebuehr() {
    return sicherheitsgebuehr;
  }

  public void setSicherheitsgebuehr(Double sicherheitsgebuehr) {
    this.sicherheitsgebuehr = sicherheitsgebuehr;
  }

  public Double getLandegebuehr() {
    return landegebuehr;
  }

  public void setLandegebuehr(Double landegebuehr) {
    this.landegebuehr = landegebuehr;
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

  public Double getMietgebuehr() {
    return mietgebuehr;
  }

  public void setMietgebuehr(Double mietgebuehr) {
    this.mietgebuehr = mietgebuehr;
  }

  public Double getCrewgebuehren() {
    return crewgebuehren;
  }

  public void setCrewgebuehren(Double crewgebuehren) {
    this.crewgebuehren = crewgebuehren;
  }

  public Double getBodenpersonalgebuehr() {
    return bodenpersonalgebuehr;
  }

  public void setBodenpersonalgebuehr(Double bodenpersonalgebuehr) {
    this.bodenpersonalgebuehr = bodenpersonalgebuehr;
  }

  public Double getSummekosten() {
    return summekosten;
  }

  public void setSummekosten(Double summekosten) {
    this.summekosten = summekosten;
  }

  public Double getSummeabrechnung() {
    return summeabrechnung;
  }

  public void setSummeabrechnung(Double summeabrechnung) {
    this.summeabrechnung = summeabrechnung;
  }

  public Double getBonus1() {
    return bonus1;
  }

  public void setBonus1(Double bonus1) {
    this.bonus1 = bonus1;
  }

  public Double getBonus2() {
    return bonus2;
  }

  public void setBonus2(Double bonus2) {
    this.bonus2 = bonus2;
  }

  public Integer getTreibstoffverbrauchkg() {
    return treibstoffverbrauchkg;
  }

  public void setTreibstoffverbrauchkg(Integer treibstoffverbrauchkg) {
    this.treibstoffverbrauchkg = treibstoffverbrauchkg;
  }

  public String getFromIcaoFlughafenName() {
    return fromIcaoFlughafenName;
  }

  public void setFromIcaoFlughafenName(String fromIcaoFlughafenName) {
    this.fromIcaoFlughafenName = fromIcaoFlughafenName;
  }

  public String getTocaoFlughafenName() {
    return tocaoFlughafenName;
  }

  public void setTocaoFlughafenName(String tocaoFlughafenName) {
    this.tocaoFlughafenName = tocaoFlughafenName;
  }

  public Double getProvision() {
    return provision;
  }

  public void setProvision(Double provision) {
    this.provision = provision;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idfluglogbuch != null ? idfluglogbuch.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Fluglogbuch)) {
      return false;
    }
    Fluglogbuch other = (Fluglogbuch) object;
    if ((this.idfluglogbuch == null && other.idfluglogbuch != null) || (this.idfluglogbuch != null && !this.idfluglogbuch.equals(other.idfluglogbuch))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Fluglogbuch[ idfluglogbuch=" + idfluglogbuch + " ]";
  }

  public Double getBetragAriivalTerminal() {
    return betragAriivalTerminal;
  }

  public void setBetragAriivalTerminal(Double betragAriivalTerminal) {
    this.betragAriivalTerminal = betragAriivalTerminal;
  }

  public Double getBetragDepartureTerminal() {
    return betragDepartureTerminal;
  }

  public void setBetragDepartureTerminal(Double betragDepartureTerminal) {
    this.betragDepartureTerminal = betragDepartureTerminal;
  }

  public Integer getIdTerminal() {
    return idTerminal;
  }

  public void setIdTerminal(Integer idTerminal) {
    this.idTerminal = idTerminal;
  }

  public String getRegistrierung() {
    return registrierung;
  }

  public void setRegistrierung(String registrierung) {
    this.registrierung = registrierung;
  }

  public Integer getMissionsart() {
    return missionsart;
  }

  public void setMissionsart(Integer missionsart) {
    this.missionsart = missionsart;
  }

  public Double getFixkosten() {
    return fixkosten;
  }

  public void setFixkosten(Double fixkosten) {
    this.fixkosten = fixkosten;
  }

  public Double getStartgebuehr() {
    return startgebuehr;
  }

  public void setStartgebuehr(Double startgebuehr) {
    this.startgebuehr = startgebuehr;
  }

}
