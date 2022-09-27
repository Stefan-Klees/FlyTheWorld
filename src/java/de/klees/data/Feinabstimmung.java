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
import javax.persistence.Lob;
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
@Table(name = "feinabstimmung")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Feinabstimmung.findAll", query = "SELECT f FROM Feinabstimmung f")})
public class Feinabstimmung implements Serializable {

  @Column(name = "faktorFuerZellenstunden")
  private Double faktorFuerZellenstunden;

  @Column(name = "abrFlugbegleiter")
  private Double abrFlugbegleiter;

  @Size(max = 250)
  @Column(name = "ftwVersion")
  private String ftwVersion;

  @Lob
  @Size(max = 16777215)
  @Column(name = "sicherheitslage")
  private String sicherheitslage;

  @Column(name = "Basis_GewichtPassagier")
  private Double basisGewichtPassagier;
  @Column(name = "Basis_GewichtGepaeck")
  private Double basisGewichtGepaeck;
  @Column(name = "PreisFuerPassagier")
  private Double preisFuerPassagier;
  @Column(name = "PreisFuerGepaeckkg")
  private Double preisFuerGepaeckkg;
  @Column(name = "PreisFuerCargokg")
  private Double preisFuerCargokg;

  @Column(name = "preisAVGASkg")
  private Double preisAVGASkg;
  @Column(name = "preisJETAkg")
  private Double preisJETAkg;
  @Column(name = "abrSicherheitsgebuehr")
  private Double abrSicherheitsgebuehr;
  @Column(name = "abrLandegebuehr")
  private Double abrLandegebuehr;
  @Column(name = "abrPrzBuchungsgebuehr")
  private Double abrPrzBuchungsgebuehr;
  @Column(name = "abrCrewgebuehren")
  private Double abrCrewgebuehren;
  @Column(name = "abrBodenpersonalgebuehr")
  private Double abrBodenpersonalgebuehr;
  @Column(name = "abrPersonalkostenFluggesellschaft")
  private Double abrPersonalkostenFluggesellschaft;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idfeinabstimmung")
  private Integer idfeinabstimmung;
  @Column(name = "ftwJobsAblauf")
  private Integer ftwJobsAblauf;
  @Column(name = "routenJobsAblauf")
  private Integer routenJobsAblauf;
  @Column(name = "linieJobsAblauf")
  private Integer linieJobsAblauf;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "ukLGClass1")
  private Double ukLGClass1;
  @Column(name = "ukLGClass2")
  private Double ukLGClass2;
  @Column(name = "ukLGClass3")
  private Double ukLGClass3;
  @Column(name = "ukLGClass4")
  private Double ukLGClass4;
  @Column(name = "ukLGClass5")
  private Double ukLGClass5;
  @Column(name = "ukLGClass6")
  private Double ukLGClass6;
  @Column(name = "ukLGClass7")
  private Double ukLGClass7;
  @Column(name = "ukLGClass8")
  private Double ukLGClass8;
  @Column(name = "ukLGClass9")
  private Double ukLGClass9;
  @Column(name = "ukLGClass10")
  private Double ukLGClass10;
  @Column(name = "ukLGClass11")
  private Double ukLGClass11;
  @Column(name = "ukLGClass12")
  private Double ukLGClass12;
  @Column(name = "ukLGClass13")
  private Double ukLGClass13;
  @Column(name = "ukLGClass14")
  private Double ukLGClass14;
  @Column(name = "ukLGClass15")
  private Double ukLGClass15;
  @Column(name = "ukLGClass16")
  private Double ukLGClass16;
  @Column(name = "ukLGClass17")
  private Double ukLGClass17;
  @Column(name = "ukLGClass18")
  private Double ukLGClass18;
  @Column(name = "ukLGClass19")
  private Double ukLGClass19;
  @Column(name = "ukLGClass20")
  private Double ukLGClass20;
  @Column(name = "ukBGClass1")
  private Double ukBGClass1;
  @Column(name = "ukBGClass2")
  private Double ukBGClass2;
  @Column(name = "ukBGClass3")
  private Double ukBGClass3;
  @Column(name = "ukBGClass4")
  private Double ukBGClass4;
  @Column(name = "ukBGClass5")
  private Double ukBGClass5;
  @Column(name = "ukBGClass6")
  private Double ukBGClass6;
  @Column(name = "ukBGClass7")
  private Double ukBGClass7;
  @Column(name = "ukBGClass8")
  private Double ukBGClass8;
  @Column(name = "ukBGClass9")
  private Double ukBGClass9;
  @Column(name = "ukBGClass10")
  private Double ukBGClass10;
  @Column(name = "ukBGClass11")
  private Double ukBGClass11;
  @Column(name = "ukBGClass12")
  private Double ukBGClass12;
  @Column(name = "ukBGClass13")
  private Double ukBGClass13;
  @Column(name = "ukBGClass14")
  private Double ukBGClass14;
  @Column(name = "ukBGClass15")
  private Double ukBGClass15;
  @Column(name = "ukBGClass16")
  private Double ukBGClass16;
  @Column(name = "ukBGClass17")
  private Double ukBGClass17;
  @Column(name = "ukBGClass18")
  private Double ukBGClass18;
  @Column(name = "ukBGClass19")
  private Double ukBGClass19;
  @Column(name = "ukBGClass20")
  private Double ukBGClass20;
  @Column(name = "pkP1")
  private Double pkP1;
  @Column(name = "pkP2")
  private Double pkP2;
  @Column(name = "pkp3")
  private Double pkp3;
  @Column(name = "pkP4")
  private Double pkP4;
  @Column(name = "pkP5")
  private Double pkP5;
  @Column(name = "pkP6")
  private Double pkP6;
  @Column(name = "pkP7")
  private Double pkP7;
  @Column(name = "pkC1")
  private Double pkC1;
  @Column(name = "pkC2")
  private Double pkC2;
  @Column(name = "pkC3")
  private Double pkC3;
  @Column(name = "pkC4")
  private Double pkC4;
  @Column(name = "pkC5")
  private Double pkC5;
  @Column(name = "pkC6")
  private Double pkC6;
  @Column(name = "pkC7")
  private Double pkC7;

  public Feinabstimmung() {
  }

  public Feinabstimmung(Integer idfeinabstimmung) {
    this.idfeinabstimmung = idfeinabstimmung;
  }

  public Integer getIdfeinabstimmung() {
    return idfeinabstimmung;
  }

  public void setIdfeinabstimmung(Integer idfeinabstimmung) {
    this.idfeinabstimmung = idfeinabstimmung;
  }

  public Integer getFtwJobsAblauf() {
    return ftwJobsAblauf;
  }

  public void setFtwJobsAblauf(Integer ftwJobsAblauf) {
    this.ftwJobsAblauf = ftwJobsAblauf;
  }

  public Integer getRoutenJobsAblauf() {
    return routenJobsAblauf;
  }

  public void setRoutenJobsAblauf(Integer routenJobsAblauf) {
    this.routenJobsAblauf = routenJobsAblauf;
  }

  public Integer getLinieJobsAblauf() {
    return linieJobsAblauf;
  }

  public void setLinieJobsAblauf(Integer linieJobsAblauf) {
    this.linieJobsAblauf = linieJobsAblauf;
  }

  public Double getUkLGClass1() {
    return ukLGClass1;
  }

  public void setUkLGClass1(Double ukLGClass1) {
    this.ukLGClass1 = ukLGClass1;
  }

  public Double getUkLGClass2() {
    return ukLGClass2;
  }

  public void setUkLGClass2(Double ukLGClass2) {
    this.ukLGClass2 = ukLGClass2;
  }

  public Double getUkLGClass3() {
    return ukLGClass3;
  }

  public void setUkLGClass3(Double ukLGClass3) {
    this.ukLGClass3 = ukLGClass3;
  }

  public Double getUkLGClass4() {
    return ukLGClass4;
  }

  public void setUkLGClass4(Double ukLGClass4) {
    this.ukLGClass4 = ukLGClass4;
  }

  public Double getUkLGClass5() {
    return ukLGClass5;
  }

  public void setUkLGClass5(Double ukLGClass5) {
    this.ukLGClass5 = ukLGClass5;
  }

  public Double getUkLGClass6() {
    return ukLGClass6;
  }

  public void setUkLGClass6(Double ukLGClass6) {
    this.ukLGClass6 = ukLGClass6;
  }

  public Double getUkLGClass7() {
    return ukLGClass7;
  }

  public void setUkLGClass7(Double ukLGClass7) {
    this.ukLGClass7 = ukLGClass7;
  }

  public Double getUkLGClass8() {
    return ukLGClass8;
  }

  public void setUkLGClass8(Double ukLGClass8) {
    this.ukLGClass8 = ukLGClass8;
  }

  public Double getUkLGClass9() {
    return ukLGClass9;
  }

  public void setUkLGClass9(Double ukLGClass9) {
    this.ukLGClass9 = ukLGClass9;
  }

  public Double getUkLGClass10() {
    return ukLGClass10;
  }

  public void setUkLGClass10(Double ukLGClass10) {
    this.ukLGClass10 = ukLGClass10;
  }

  public Double getUkLGClass11() {
    return ukLGClass11;
  }

  public void setUkLGClass11(Double ukLGClass11) {
    this.ukLGClass11 = ukLGClass11;
  }

  public Double getUkLGClass12() {
    return ukLGClass12;
  }

  public void setUkLGClass12(Double ukLGClass12) {
    this.ukLGClass12 = ukLGClass12;
  }

  public Double getUkLGClass13() {
    return ukLGClass13;
  }

  public void setUkLGClass13(Double ukLGClass13) {
    this.ukLGClass13 = ukLGClass13;
  }

  public Double getUkLGClass14() {
    return ukLGClass14;
  }

  public void setUkLGClass14(Double ukLGClass14) {
    this.ukLGClass14 = ukLGClass14;
  }

  public Double getUkLGClass15() {
    return ukLGClass15;
  }

  public void setUkLGClass15(Double ukLGClass15) {
    this.ukLGClass15 = ukLGClass15;
  }

  public Double getUkLGClass16() {
    return ukLGClass16;
  }

  public void setUkLGClass16(Double ukLGClass16) {
    this.ukLGClass16 = ukLGClass16;
  }

  public Double getUkLGClass17() {
    return ukLGClass17;
  }

  public void setUkLGClass17(Double ukLGClass17) {
    this.ukLGClass17 = ukLGClass17;
  }

  public Double getUkLGClass18() {
    return ukLGClass18;
  }

  public void setUkLGClass18(Double ukLGClass18) {
    this.ukLGClass18 = ukLGClass18;
  }

  public Double getUkLGClass19() {
    return ukLGClass19;
  }

  public void setUkLGClass19(Double ukLGClass19) {
    this.ukLGClass19 = ukLGClass19;
  }

  public Double getUkLGClass20() {
    return ukLGClass20;
  }

  public void setUkLGClass20(Double ukLGClass20) {
    this.ukLGClass20 = ukLGClass20;
  }

  public Double getUkBGClass1() {
    return ukBGClass1;
  }

  public void setUkBGClass1(Double ukBGClass1) {
    this.ukBGClass1 = ukBGClass1;
  }

  public Double getUkBGClass2() {
    return ukBGClass2;
  }

  public void setUkBGClass2(Double ukBGClass2) {
    this.ukBGClass2 = ukBGClass2;
  }

  public Double getUkBGClass3() {
    return ukBGClass3;
  }

  public void setUkBGClass3(Double ukBGClass3) {
    this.ukBGClass3 = ukBGClass3;
  }

  public Double getUkBGClass4() {
    return ukBGClass4;
  }

  public void setUkBGClass4(Double ukBGClass4) {
    this.ukBGClass4 = ukBGClass4;
  }

  public Double getUkBGClass5() {
    return ukBGClass5;
  }

  public void setUkBGClass5(Double ukBGClass5) {
    this.ukBGClass5 = ukBGClass5;
  }

  public Double getUkBGClass6() {
    return ukBGClass6;
  }

  public void setUkBGClass6(Double ukBGClass6) {
    this.ukBGClass6 = ukBGClass6;
  }

  public Double getUkBGClass7() {
    return ukBGClass7;
  }

  public void setUkBGClass7(Double ukBGClass7) {
    this.ukBGClass7 = ukBGClass7;
  }

  public Double getUkBGClass8() {
    return ukBGClass8;
  }

  public void setUkBGClass8(Double ukBGClass8) {
    this.ukBGClass8 = ukBGClass8;
  }

  public Double getUkBGClass9() {
    return ukBGClass9;
  }

  public void setUkBGClass9(Double ukBGClass9) {
    this.ukBGClass9 = ukBGClass9;
  }

  public Double getUkBGClass10() {
    return ukBGClass10;
  }

  public void setUkBGClass10(Double ukBGClass10) {
    this.ukBGClass10 = ukBGClass10;
  }

  public Double getUkBGClass11() {
    return ukBGClass11;
  }

  public void setUkBGClass11(Double ukBGClass11) {
    this.ukBGClass11 = ukBGClass11;
  }

  public Double getUkBGClass12() {
    return ukBGClass12;
  }

  public void setUkBGClass12(Double ukBGClass12) {
    this.ukBGClass12 = ukBGClass12;
  }

  public Double getUkBGClass13() {
    return ukBGClass13;
  }

  public void setUkBGClass13(Double ukBGClass13) {
    this.ukBGClass13 = ukBGClass13;
  }

  public Double getUkBGClass14() {
    return ukBGClass14;
  }

  public void setUkBGClass14(Double ukBGClass14) {
    this.ukBGClass14 = ukBGClass14;
  }

  public Double getUkBGClass15() {
    return ukBGClass15;
  }

  public void setUkBGClass15(Double ukBGClass15) {
    this.ukBGClass15 = ukBGClass15;
  }

  public Double getUkBGClass16() {
    return ukBGClass16;
  }

  public void setUkBGClass16(Double ukBGClass16) {
    this.ukBGClass16 = ukBGClass16;
  }

  public Double getUkBGClass17() {
    return ukBGClass17;
  }

  public void setUkBGClass17(Double ukBGClass17) {
    this.ukBGClass17 = ukBGClass17;
  }

  public Double getUkBGClass18() {
    return ukBGClass18;
  }

  public void setUkBGClass18(Double ukBGClass18) {
    this.ukBGClass18 = ukBGClass18;
  }

  public Double getUkBGClass19() {
    return ukBGClass19;
  }

  public void setUkBGClass19(Double ukBGClass19) {
    this.ukBGClass19 = ukBGClass19;
  }

  public Double getUkBGClass20() {
    return ukBGClass20;
  }

  public void setUkBGClass20(Double ukBGClass20) {
    this.ukBGClass20 = ukBGClass20;
  }

  public Double getPkP1() {
    return pkP1;
  }

  public void setPkP1(Double pkP1) {
    this.pkP1 = pkP1;
  }

  public Double getPkP2() {
    return pkP2;
  }

  public void setPkP2(Double pkP2) {
    this.pkP2 = pkP2;
  }

  public Double getPkp3() {
    return pkp3;
  }

  public void setPkp3(Double pkp3) {
    this.pkp3 = pkp3;
  }

  public Double getPkP4() {
    return pkP4;
  }

  public void setPkP4(Double pkP4) {
    this.pkP4 = pkP4;
  }

  public Double getPkP5() {
    return pkP5;
  }

  public void setPkP5(Double pkP5) {
    this.pkP5 = pkP5;
  }

  public Double getPkP6() {
    return pkP6;
  }

  public void setPkP6(Double pkP6) {
    this.pkP6 = pkP6;
  }

  public Double getPkP7() {
    return pkP7;
  }

  public void setPkP7(Double pkP7) {
    this.pkP7 = pkP7;
  }

  public Double getPkC1() {
    return pkC1;
  }

  public void setPkC1(Double pkC1) {
    this.pkC1 = pkC1;
  }

  public Double getPkC2() {
    return pkC2;
  }

  public void setPkC2(Double pkC2) {
    this.pkC2 = pkC2;
  }

  public Double getPkC3() {
    return pkC3;
  }

  public void setPkC3(Double pkC3) {
    this.pkC3 = pkC3;
  }

  public Double getPkC4() {
    return pkC4;
  }

  public void setPkC4(Double pkC4) {
    this.pkC4 = pkC4;
  }

  public Double getPkC5() {
    return pkC5;
  }

  public void setPkC5(Double pkC5) {
    this.pkC5 = pkC5;
  }

  public Double getPkC6() {
    return pkC6;
  }

  public void setPkC6(Double pkC6) {
    this.pkC6 = pkC6;
  }

  public Double getPkC7() {
    return pkC7;
  }

  public void setPkC7(Double pkC7) {
    this.pkC7 = pkC7;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idfeinabstimmung != null ? idfeinabstimmung.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Feinabstimmung)) {
      return false;
    }
    Feinabstimmung other = (Feinabstimmung) object;
    if ((this.idfeinabstimmung == null && other.idfeinabstimmung != null) || (this.idfeinabstimmung != null && !this.idfeinabstimmung.equals(other.idfeinabstimmung))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Feinabstimmung[ idfeinabstimmung=" + idfeinabstimmung + " ]";
  }

  public Double getPreisAVGASkg() {
    return preisAVGASkg;
  }

  public void setPreisAVGASkg(Double preisAVGASkg) {
    this.preisAVGASkg = preisAVGASkg;
  }

  public Double getPreisJETAkg() {
    return preisJETAkg;
  }

  public void setPreisJETAkg(Double preisJETAkg) {
    this.preisJETAkg = preisJETAkg;
  }

  public Double getAbrSicherheitsgebuehr() {
    return abrSicherheitsgebuehr;
  }

  public void setAbrSicherheitsgebuehr(Double abrSicherheitsgebuehr) {
    this.abrSicherheitsgebuehr = abrSicherheitsgebuehr;
  }

  public Double getAbrLandegebuehr() {
    return abrLandegebuehr;
  }

  public void setAbrLandegebuehr(Double abrLandegebuehr) {
    this.abrLandegebuehr = abrLandegebuehr;
  }

  public Double getAbrPrzBuchungsgebuehr() {
    return abrPrzBuchungsgebuehr;
  }

  public void setAbrPrzBuchungsgebuehr(Double abrPrzBuchungsgebuehr) {
    this.abrPrzBuchungsgebuehr = abrPrzBuchungsgebuehr;
  }

  public Double getAbrCrewgebuehren() {
    return abrCrewgebuehren;
  }

  public void setAbrCrewgebuehren(Double abrCrewgebuehren) {
    this.abrCrewgebuehren = abrCrewgebuehren;
  }

  public Double getAbrBodenpersonalgebuehr() {
    return abrBodenpersonalgebuehr;
  }

  public void setAbrBodenpersonalgebuehr(Double abrBodenpersonalgebuehr) {
    this.abrBodenpersonalgebuehr = abrBodenpersonalgebuehr;
  }

  public Double getAbrPersonalkostenFluggesellschaft() {
    return abrPersonalkostenFluggesellschaft;
  }

  public void setAbrPersonalkostenFluggesellschaft(Double abrPersonalkostenFluggesellschaft) {
    this.abrPersonalkostenFluggesellschaft = abrPersonalkostenFluggesellschaft;
  }

  public Double getBasisGewichtPassagier() {
    return basisGewichtPassagier;
  }

  public void setBasisGewichtPassagier(Double basisGewichtPassagier) {
    this.basisGewichtPassagier = basisGewichtPassagier;
  }

  public Double getBasisGewichtGepaeck() {
    return basisGewichtGepaeck;
  }

  public void setBasisGewichtGepaeck(Double basisGewichtGepaeck) {
    this.basisGewichtGepaeck = basisGewichtGepaeck;
  }

  public Double getPreisFuerPassagier() {
    return preisFuerPassagier;
  }

  public void setPreisFuerPassagier(Double preisFuerPassagier) {
    this.preisFuerPassagier = preisFuerPassagier;
  }

  public Double getPreisFuerGepaeckkg() {
    return preisFuerGepaeckkg;
  }

  public void setPreisFuerGepaeckkg(Double preisFuerGepaeckkg) {
    this.preisFuerGepaeckkg = preisFuerGepaeckkg;
  }

  public Double getPreisFuerCargokg() {
    return preisFuerCargokg;
  }

  public void setPreisFuerCargokg(Double preisFuerCargokg) {
    this.preisFuerCargokg = preisFuerCargokg;
  }

  public String getSicherheitslage() {
    return sicherheitslage;
  }

  public void setSicherheitslage(String sicherheitslage) {
    this.sicherheitslage = sicherheitslage;
  }

  public String getFtwVersion() {
    return ftwVersion;
  }

  public void setFtwVersion(String ftwVersion) {
    this.ftwVersion = ftwVersion;
  }

  public Double getAbrFlugbegleiter() {
    return abrFlugbegleiter;
  }

  public void setAbrFlugbegleiter(Double abrFlugbegleiter) {
    this.abrFlugbegleiter = abrFlugbegleiter;
  }

  public Double getFaktorFuerZellenstunden() {
    return faktorFuerZellenstunden;
  }

  public void setFaktorFuerZellenstunden(Double faktorFuerZellenstunden) {
    this.faktorFuerZellenstunden = faktorFuerZellenstunden;
  }

  
}
