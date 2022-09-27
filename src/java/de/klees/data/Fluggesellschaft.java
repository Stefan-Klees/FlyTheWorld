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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Stefan Klees
 */
@Entity
@Table(name = "Fluggesellschaft")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Fluggesellschaft.findAll", query = "SELECT f FROM Fluggesellschaft f")
  , @NamedQuery(name = "Fluggesellschaft.findByIdFluggesellschaft", query = "SELECT f FROM Fluggesellschaft f WHERE f.idFluggesellschaft = :idFluggesellschaft")
  , @NamedQuery(name = "Fluggesellschaft.findByUserid", query = "SELECT f FROM Fluggesellschaft f WHERE f.userid = :userid")
  , @NamedQuery(name = "Fluggesellschaft.findByName", query = "SELECT f FROM Fluggesellschaft f WHERE f.name = :name")
  , @NamedQuery(name = "Fluggesellschaft.findByIcao", query = "SELECT f FROM Fluggesellschaft f WHERE f.icao = :icao")
  , @NamedQuery(name = "Fluggesellschaft.findByIcaoCode", query = "SELECT f FROM Fluggesellschaft f WHERE f.icaoCode = :icaoCode")
  , @NamedQuery(name = "Fluggesellschaft.findByStadt", query = "SELECT f FROM Fluggesellschaft f WHERE f.stadt = :stadt")
  , @NamedQuery(name = "Fluggesellschaft.findByLand", query = "SELECT f FROM Fluggesellschaft f WHERE f.land = :land")
  , @NamedQuery(name = "Fluggesellschaft.findByBundesstaat", query = "SELECT f FROM Fluggesellschaft f WHERE f.bundesstaat = :bundesstaat")
  , @NamedQuery(name = "Fluggesellschaft.findByLogoURL", query = "SELECT f FROM Fluggesellschaft f WHERE f.logoURL = :logoURL")
  , @NamedQuery(name = "Fluggesellschaft.findByBesitzerName", query = "SELECT f FROM Fluggesellschaft f WHERE f.besitzerName = :besitzerName")
  , @NamedQuery(name = "Fluggesellschaft.findByBankKontoName", query = "SELECT f FROM Fluggesellschaft f WHERE f.bankKontoName = :bankKontoName")
  , @NamedQuery(name = "Fluggesellschaft.findByBankKonto", query = "SELECT f FROM Fluggesellschaft f WHERE f.bankKonto = :bankKonto")})
public class Fluggesellschaft implements Serializable {

  @Column(name = "kstBuchungsgebuehr")
  private Integer kstBuchungsgebuehr;
  @Column(name = "kstBonusPilot")
  private Integer kstBonusPilot;
  @Column(name = "kstGehaltCrew")
  private Integer kstGehaltCrew;
  @Column(name = "kstAbfertigung")
  private Integer kstAbfertigung;
  @Column(name = "kstLandegebuehr")
  private Integer kstLandegebuehr;
  @Column(name = "kstProvisionFluggesellschaft")
  private Integer kstProvisionFluggesellschaft;
  @Column(name = "kstTreibstoffkostenErstattung")
  private Integer kstTreibstoffkostenErstattung;

  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "stdBonus1")
  private Double stdBonus1;
  @Column(name = "stdBonus2")
  private Double stdBonus2;
  @Column(name = "stdProvision")
  private Double stdProvision;

  @Column(name = "erzeugtesCargo")
  private Integer erzeugtesCargo;
  @Column(name = "geflogenesCargo")
  private Integer geflogenesCargo;

  @Column(name = "erzeugteJobs")
  private Integer erzeugteJobs;
  @Column(name = "geflogeneJobs")
  private Integer geflogeneJobs;

  @Column(name = "kostenstelle")
  private Integer kostenstelle;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idFluggesellschaft")
  private Integer idFluggesellschaft;
  @Column(name = "userid")
  private Integer userid;
  @Size(max = 250)
  @Column(name = "name")
  private String name;
  @Size(max = 45)
  @Column(name = "icao")
  private String icao;
  @Size(max = 10)
  @Column(name = "icaoCode")
  private String icaoCode;
  @Size(max = 80)
  @Column(name = "stadt")
  private String stadt;
  @Size(max = 80)
  @Column(name = "land")
  private String land;
  @Size(max = 80)
  @Column(name = "bundesstaat")
  private String bundesstaat;
  @Size(max = 250)
  @Column(name = "logoURL")
  private String logoURL;
  @Size(max = 250)
  @Column(name = "besitzerName")
  private String besitzerName;
  @Size(max = 250)
  @Column(name = "bankKontoName")
  private String bankKontoName;
  @Size(max = 80)
  @Column(name = "bankKonto")
  private String bankKonto;

  public Fluggesellschaft() {
  }

  public Fluggesellschaft(Integer idFluggesellschaft) {
    this.idFluggesellschaft = idFluggesellschaft;
  }

  public Integer getIdFluggesellschaft() {
    return idFluggesellschaft;
  }

  public void setIdFluggesellschaft(Integer idFluggesellschaft) {
    this.idFluggesellschaft = idFluggesellschaft;
  }

  public Integer getUserid() {
    return userid;
  }

  public void setUserid(Integer userid) {
    this.userid = userid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIcao() {
    return icao;
  }

  public void setIcao(String icao) {
    this.icao = icao;
  }

  public String getIcaoCode() {
    return icaoCode;
  }

  public void setIcaoCode(String icaoCode) {
    this.icaoCode = icaoCode;
  }

  public String getStadt() {
    return stadt;
  }

  public void setStadt(String stadt) {
    this.stadt = stadt;
  }

  public String getLand() {
    return land;
  }

  public void setLand(String land) {
    this.land = land;
  }

  public String getBundesstaat() {
    return bundesstaat;
  }

  public void setBundesstaat(String bundesstaat) {
    this.bundesstaat = bundesstaat;
  }

  public String getLogoURL() {
    return logoURL;
  }

  public void setLogoURL(String logoURL) {
    this.logoURL = logoURL;
  }

  public String getBesitzerName() {
    return besitzerName;
  }

  public void setBesitzerName(String besitzerName) {
    this.besitzerName = besitzerName;
  }

  public String getBankKontoName() {
    return bankKontoName;
  }

  public void setBankKontoName(String bankKontoName) {
    this.bankKontoName = bankKontoName;
  }

  public String getBankKonto() {
    return bankKonto;
  }

  public void setBankKonto(String bankKonto) {
    this.bankKonto = bankKonto;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idFluggesellschaft != null ? idFluggesellschaft.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Fluggesellschaft)) {
      return false;
    }
    Fluggesellschaft other = (Fluggesellschaft) object;
    if ((this.idFluggesellschaft == null && other.idFluggesellschaft != null) || (this.idFluggesellschaft != null && !this.idFluggesellschaft.equals(other.idFluggesellschaft))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Fluggesellschaft[ idFluggesellschaft=" + idFluggesellschaft + " ]";
  }

  public Integer getKostenstelle() {
    return kostenstelle;
  }

  public void setKostenstelle(Integer kostenstelle) {
    this.kostenstelle = kostenstelle;
  }

  public Integer getErzeugteJobs() {
    return erzeugteJobs;
  }

  public void setErzeugteJobs(Integer erzeugteJobs) {
    this.erzeugteJobs = erzeugteJobs;
  }

  public Integer getGeflogeneJobs() {
    return geflogeneJobs;
  }

  public void setGeflogeneJobs(Integer geflogeneJobs) {
    this.geflogeneJobs = geflogeneJobs;
  }

  public Integer getErzeugtesCargo() {
    return erzeugtesCargo;
  }

  public void setErzeugtesCargo(Integer erzeugtesCargo) {
    this.erzeugtesCargo = erzeugtesCargo;
  }

  public Integer getGeflogenesCargo() {
    return geflogenesCargo;
  }

  public void setGeflogenesCargo(Integer geflogenesCargo) {
    this.geflogenesCargo = geflogenesCargo;
  }

  public Double getStdBonus1() {
    return stdBonus1;
  }

  public void setStdBonus1(Double stdBonus1) {
    this.stdBonus1 = stdBonus1;
  }

  public Double getStdBonus2() {
    return stdBonus2;
  }

  public void setStdBonus2(Double stdBonus2) {
    this.stdBonus2 = stdBonus2;
  }

  public Double getStdProvision() {
    return stdProvision;
  }

  public void setStdProvision(Double stdProvision) {
    this.stdProvision = stdProvision;
  }

  public Integer getKstBuchungsgebuehr() {
    return kstBuchungsgebuehr;
  }

  public void setKstBuchungsgebuehr(Integer kstBuchungsgebuehr) {
    this.kstBuchungsgebuehr = kstBuchungsgebuehr;
  }

  public Integer getKstBonusPilot() {
    return kstBonusPilot;
  }

  public void setKstBonusPilot(Integer kstBonusPilot) {
    this.kstBonusPilot = kstBonusPilot;
  }

  public Integer getKstGehaltCrew() {
    return kstGehaltCrew;
  }

  public void setKstGehaltCrew(Integer kstGehaltCrew) {
    this.kstGehaltCrew = kstGehaltCrew;
  }

  public Integer getKstAbfertigung() {
    return kstAbfertigung;
  }

  public void setKstAbfertigung(Integer kstAbfertigung) {
    this.kstAbfertigung = kstAbfertigung;
  }

  public Integer getKstLandegebuehr() {
    return kstLandegebuehr;
  }

  public void setKstLandegebuehr(Integer kstLandegebuehr) {
    this.kstLandegebuehr = kstLandegebuehr;
  }

  public Integer getKstProvisionFluggesellschaft() {
    return kstProvisionFluggesellschaft;
  }

  public void setKstProvisionFluggesellschaft(Integer kstProvisionFluggesellschaft) {
    this.kstProvisionFluggesellschaft = kstProvisionFluggesellschaft;
  }

  public Integer getKstTreibstoffkostenErstattung() {
    return kstTreibstoffkostenErstattung;
  }

  public void setKstTreibstoffkostenErstattung(Integer kstTreibstoffkostenErstattung) {
    this.kstTreibstoffkostenErstattung = kstTreibstoffkostenErstattung;
  }
  
}
