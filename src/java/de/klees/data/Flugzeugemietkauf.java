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
@Table(name = "Flugzeuge_miet_kauf")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Flugzeugemietkauf.findAll", query = "SELECT f FROM Flugzeugemietkauf f")})
public class Flugzeugemietkauf implements Serializable {

  @Column(name = "flugzeugUmgebaut")
  private Boolean flugzeugUmgebaut;

  @Column(name = "naechsterTerminCcheck")
  @Temporal(TemporalType.DATE)
  private Date naechsterTerminCcheck;

  @Column(name = "positionLaengenGrad")
  private Double positionLaengenGrad;
  @Column(name = "positiionBreitenGrad")
  private Double positiionBreitenGrad;

  @Column(name = "oldMiles")
  private Integer oldMiles;

  @Size(max = 250)
  @Column(name = "eigenesBildURL")
  private String eigenesBildURL;

  @Column(name = "nurFuerAuftraegeDerFluggesellschaft")
  private Boolean nurFuerAuftraegeDerFluggesellschaft;

  @Column(name = "baujahr")
  private Integer baujahr;
  @Column(name = "zustand")
  private Double zustand;

  @Column(name = "kostenstelle")
  private Integer kostenstelle;

  @Column(name = "istGesperrtBis")
  @Temporal(TemporalType.TIMESTAMP)
  private Date istGesperrtBis;

  @Column(name = "pfand")
  private Integer pfand;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idMietKauf")
  private Integer idMietKauf;
  @Column(name = "idFlugzeug")
  private Integer idFlugzeug;
  @Column(name = "idflugzeugBesitzer")
  private Integer idflugzeugBesitzer;
  @Column(name = "idUserDerFlugzeugGesperrtHat")
  private Integer idUserDerFlugzeugGesperrtHat;
  @Column(name = "idFluggesellschaft")
  private Integer idFluggesellschaft;
  @Column(name = "idIndustrie")
  private Integer idIndustrie;
  @Size(max = 45)
  @Column(name = "registrierung")
  private String registrierung;
  @Size(max = 25)
  @Column(name = "heimatICAO")
  private String heimatICAO;
  @Size(max = 25)
  @Column(name = "aktuellePositionICAO")
  private String aktuellePositionICAO;
  @Column(name = "istGesperrt")
  private Boolean istGesperrt;
  @Column(name = "istGesperrtSeit")
  @Temporal(TemporalType.TIMESTAMP)
  private Date istGesperrtSeit;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "mietpreisTrocken")
  private Double mietpreisTrocken;
  @Column(name = "mietpreisNass")
  private Double mietpreisNass;
  @Column(name = "aktuelleTankfuellung")
  private Integer aktuelleTankfuellung;
  @Column(name = "verkaufsPreis")
  private Double verkaufsPreis;
  @Column(name = "istPrivatverkauf")
  private Boolean istPrivatverkauf;
  @Column(name = "LeasingAnUserID")
  private Integer leasingAnUserID;
  @Column(name = "maxMietzeitMinuten")
  private Integer maxMietzeitMinuten;
  @Size(max = 250)
  @Column(name = "abgeflogenVonICAO")
  private String abgeflogenVonICAO;
  @Column(name = "bonusFuerRueckfuehrung")
  private Double bonusFuerRueckfuehrung;
  @Column(name = "leasinggeberUserID")
  private Integer leasinggeberUserID;
  @Column(name = "letzterCheckMinuten")
  private Integer letzterCheckMinuten;
  @Column(name = "gesamtMaschinenLaufzeitMinuten")
  private Integer gesamtMaschinenLaufzeitMinuten;
  @Column(name = "gesamtAlterDesFlugzeugsMinuten")
  private Integer gesamtAlterDesFlugzeugsMinuten;
  @Column(name = "zustandDesFlugzeugs")
  private Integer zustandDesFlugzeugs;
  @Column(name = "istCheckDurchUserErlaubt")
  private Boolean istCheckDurchUserErlaubt;
  @Column(name = "istMietbar")
  private Boolean istMietbar;
  @Column(name = "istZuVerkaufen")
  private Boolean istZuVerkaufen;
  @Column(name = "gesamtFlugzeit")
  private Integer gesamtFlugzeit;
  @Column(name = "gesamtEntfernung")
  private Integer gesamtEntfernung;
  @Column(name = "gesamtFluege")
  private Integer gesamtFluege;
  @Column(name = "gesamtTransportiertePassagiere")
  private Integer gesamtTransportiertePassagiere;
  @Column(name = "gesamtTransportiertesCargo")
  private Integer gesamtTransportiertesCargo;
  @Size(max = 80)
  @Column(name = "bankkontoBesitzer")
  private String bankkontoBesitzer;
  @Column(name = "isAktive")
  private Boolean isAktive;
  @Column(name = "maschinenLaufzeitMinuten")
  private Integer maschinenLaufzeitMinuten;
  @Column(name = "letzterSpritPreis")
  private Double letzterSpritPreis;
  @Column(name = "idSitzKonfiguration")
  private Integer idSitzKonfiguration;
  @Column(name = "istInDerLuft")
  private Boolean istInDerLuft;

  public Flugzeugemietkauf() {
  }

  public Flugzeugemietkauf(Integer idMietKauf) {
    this.idMietKauf = idMietKauf;
  }

  public Integer getIdMietKauf() {
    return idMietKauf;
  }

  public void setIdMietKauf(Integer idMietKauf) {
    this.idMietKauf = idMietKauf;
  }

  public Integer getIdFlugzeug() {
    return idFlugzeug;
  }

  public void setIdFlugzeug(Integer idFlugzeug) {
    this.idFlugzeug = idFlugzeug;
  }

  public Integer getIdflugzeugBesitzer() {
    return idflugzeugBesitzer;
  }

  public void setIdflugzeugBesitzer(Integer idflugzeugBesitzer) {
    this.idflugzeugBesitzer = idflugzeugBesitzer;
  }

  public Integer getIdUserDerFlugzeugGesperrtHat() {
    return idUserDerFlugzeugGesperrtHat;
  }

  public void setIdUserDerFlugzeugGesperrtHat(Integer idUserDerFlugzeugGesperrtHat) {
    this.idUserDerFlugzeugGesperrtHat = idUserDerFlugzeugGesperrtHat;
  }

  public Integer getIdFluggesellschaft() {
    return idFluggesellschaft;
  }

  public void setIdFluggesellschaft(Integer idFluggesellschaft) {
    this.idFluggesellschaft = idFluggesellschaft;
  }

  public Integer getIdIndustrie() {
    return idIndustrie;
  }

  public void setIdIndustrie(Integer idIndustrie) {
    this.idIndustrie = idIndustrie;
  }

  public String getRegistrierung() {
    return registrierung;
  }

  public void setRegistrierung(String registrierung) {
    this.registrierung = registrierung;
  }

  public String getHeimatICAO() {
    return heimatICAO;
  }

  public void setHeimatICAO(String heimatICAO) {
    this.heimatICAO = heimatICAO;
  }

  public String getAktuellePositionICAO() {
    return aktuellePositionICAO;
  }

  public void setAktuellePositionICAO(String aktuellePositionICAO) {
    this.aktuellePositionICAO = aktuellePositionICAO;
  }

  public Boolean getIstGesperrt() {
    return istGesperrt;
  }

  public void setIstGesperrt(Boolean istGesperrt) {
    this.istGesperrt = istGesperrt;
  }

  public Date getIstGesperrtSeit() {
    return istGesperrtSeit;
  }

  public void setIstGesperrtSeit(Date istGesperrtSeit) {
    this.istGesperrtSeit = istGesperrtSeit;
  }

  public Double getMietpreisTrocken() {
    return mietpreisTrocken;
  }

  public void setMietpreisTrocken(Double mietpreisTrocken) {
    this.mietpreisTrocken = mietpreisTrocken;
  }

  public Double getMietpreisNass() {
    return mietpreisNass;
  }

  public void setMietpreisNass(Double mietpreisNass) {
    this.mietpreisNass = mietpreisNass;
  }

  public Integer getAktuelleTankfuellung() {
    return aktuelleTankfuellung;
  }

  public void setAktuelleTankfuellung(Integer aktuelleTankfuellung) {
    this.aktuelleTankfuellung = aktuelleTankfuellung;
  }

  public Double getVerkaufsPreis() {
    return verkaufsPreis;
  }

  public void setVerkaufsPreis(Double verkaufsPreis) {
    this.verkaufsPreis = verkaufsPreis;
  }

  public Boolean getIstPrivatverkauf() {
    return istPrivatverkauf;
  }

  public void setIstPrivatverkauf(Boolean istPrivatverkauf) {
    this.istPrivatverkauf = istPrivatverkauf;
  }

  public Integer getLeasingAnUserID() {
    return leasingAnUserID;
  }

  public void setLeasingAnUserID(Integer leasingAnUserID) {
    this.leasingAnUserID = leasingAnUserID;
  }

  public Integer getMaxMietzeitMinuten() {
    return maxMietzeitMinuten;
  }

  public void setMaxMietzeitMinuten(Integer maxMietzeitMinuten) {
    this.maxMietzeitMinuten = maxMietzeitMinuten;
  }

  public String getAbgeflogenVonICAO() {
    return abgeflogenVonICAO;
  }

  public void setAbgeflogenVonICAO(String abgeflogenVonICAO) {
    this.abgeflogenVonICAO = abgeflogenVonICAO;
  }

  public Double getBonusFuerRueckfuehrung() {
    return bonusFuerRueckfuehrung;
  }

  public void setBonusFuerRueckfuehrung(Double bonusFuerRueckfuehrung) {
    this.bonusFuerRueckfuehrung = bonusFuerRueckfuehrung;
  }

  public Integer getLeasinggeberUserID() {
    return leasinggeberUserID;
  }

  public void setLeasinggeberUserID(Integer leasinggeberUserID) {
    this.leasinggeberUserID = leasinggeberUserID;
  }

  public Integer getLetzterCheckMinuten() {
    return letzterCheckMinuten;
  }

  public void setLetzterCheckMinuten(Integer letzterCheckMinuten) {
    this.letzterCheckMinuten = letzterCheckMinuten;
  }

  public Integer getGesamtMaschinenLaufzeitMinuten() {
    return gesamtMaschinenLaufzeitMinuten;
  }

  public void setGesamtMaschinenLaufzeitMinuten(Integer gesamtMaschinenLaufzeitMinuten) {
    this.gesamtMaschinenLaufzeitMinuten = gesamtMaschinenLaufzeitMinuten;
  }

  public Integer getGesamtAlterDesFlugzeugsMinuten() {
    return gesamtAlterDesFlugzeugsMinuten;
  }

  public void setGesamtAlterDesFlugzeugsMinuten(Integer gesamtAlterDesFlugzeugsMinuten) {
    this.gesamtAlterDesFlugzeugsMinuten = gesamtAlterDesFlugzeugsMinuten;
  }

  public Integer getZustandDesFlugzeugs() {
    return zustandDesFlugzeugs;
  }

  public void setZustandDesFlugzeugs(Integer zustandDesFlugzeugs) {
    this.zustandDesFlugzeugs = zustandDesFlugzeugs;
  }

  public Boolean getIstCheckDurchUserErlaubt() {
    return istCheckDurchUserErlaubt;
  }

  public void setIstCheckDurchUserErlaubt(Boolean istCheckDurchUserErlaubt) {
    this.istCheckDurchUserErlaubt = istCheckDurchUserErlaubt;
  }

  public Boolean getIstMietbar() {
    return istMietbar;
  }

  public void setIstMietbar(Boolean istMietbar) {
    this.istMietbar = istMietbar;
  }

  public Boolean getIstZuVerkaufen() {
    return istZuVerkaufen;
  }

  public void setIstZuVerkaufen(Boolean istZuVerkaufen) {
    this.istZuVerkaufen = istZuVerkaufen;
  }

  public Integer getGesamtFlugzeit() {
    return gesamtFlugzeit;
  }

  public void setGesamtFlugzeit(Integer gesamtFlugzeit) {
    this.gesamtFlugzeit = gesamtFlugzeit;
  }

  public Integer getGesamtEntfernung() {
    return gesamtEntfernung;
  }

  public void setGesamtEntfernung(Integer gesamtEntfernung) {
    this.gesamtEntfernung = gesamtEntfernung;
  }

  public Integer getGesamtFluege() {
    return gesamtFluege;
  }

  public void setGesamtFluege(Integer gesamtFluege) {
    this.gesamtFluege = gesamtFluege;
  }

  public Integer getGesamtTransportiertePassagiere() {
    return gesamtTransportiertePassagiere;
  }

  public void setGesamtTransportiertePassagiere(Integer gesamtTransportiertePassagiere) {
    this.gesamtTransportiertePassagiere = gesamtTransportiertePassagiere;
  }

  public Integer getGesamtTransportiertesCargo() {
    return gesamtTransportiertesCargo;
  }

  public void setGesamtTransportiertesCargo(Integer gesamtTransportiertesCargo) {
    this.gesamtTransportiertesCargo = gesamtTransportiertesCargo;
  }

  public String getBankkontoBesitzer() {
    return bankkontoBesitzer;
  }

  public void setBankkontoBesitzer(String bankkontoBesitzer) {
    this.bankkontoBesitzer = bankkontoBesitzer;
  }

  public Boolean getIsAktive() {
    return isAktive;
  }

  public void setIsAktive(Boolean isAktive) {
    this.isAktive = isAktive;
  }

  public Integer getMaschinenLaufzeitMinuten() {
    return maschinenLaufzeitMinuten;
  }

  public void setMaschinenLaufzeitMinuten(Integer maschinenLaufzeitMinuten) {
    this.maschinenLaufzeitMinuten = maschinenLaufzeitMinuten;
  }

  public Double getLetzterSpritPreis() {
    return letzterSpritPreis;
  }

  public void setLetzterSpritPreis(Double letzterSpritPreis) {
    this.letzterSpritPreis = letzterSpritPreis;
  }

  public Integer getIdSitzKonfiguration() {
    return idSitzKonfiguration;
  }

  public void setIdSitzKonfiguration(Integer idSitzKonfiguration) {
    this.idSitzKonfiguration = idSitzKonfiguration;
  }

  public Boolean getIstInDerLuft() {
    return istInDerLuft;
  }

  public void setIstInDerLuft(Boolean istInDerLuft) {
    this.istInDerLuft = istInDerLuft;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idMietKauf != null ? idMietKauf.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Flugzeugemietkauf)) {
      return false;
    }
    Flugzeugemietkauf other = (Flugzeugemietkauf) object;
    if ((this.idMietKauf == null && other.idMietKauf != null) || (this.idMietKauf != null && !this.idMietKauf.equals(other.idMietKauf))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Flugzeugemietkauf[ idMietKauf=" + idMietKauf + " ]";
  }

  public Integer getPfand() {
    return pfand;
  }

  public void setPfand(Integer pfand) {
    this.pfand = pfand;
  }

  public Date getIstGesperrtBis() {
    return istGesperrtBis;
  }

  public void setIstGesperrtBis(Date istGesperrtBis) {
    this.istGesperrtBis = istGesperrtBis;
  }

  public Integer getKostenstelle() {
    return kostenstelle;
  }

  public void setKostenstelle(Integer kostenstelle) {
    this.kostenstelle = kostenstelle;
  }

  public Integer getBaujahr() {
    return baujahr;
  }

  public void setBaujahr(Integer baujahr) {
    this.baujahr = baujahr;
  }

  public Double getZustand() {
    return zustand;
  }

  public void setZustand(Double zustand) {
    this.zustand = zustand;
  }

  public Boolean getNurFuerAuftraegeDerFluggesellschaft() {
    return nurFuerAuftraegeDerFluggesellschaft;
  }

  public void setNurFuerAuftraegeDerFluggesellschaft(Boolean nurFuerAuftraegeDerFluggesellschaft) {
    this.nurFuerAuftraegeDerFluggesellschaft = nurFuerAuftraegeDerFluggesellschaft;
  }

  public String getEigenesBildURL() {
    return eigenesBildURL;
  }

  public void setEigenesBildURL(String eigenesBildURL) {
    this.eigenesBildURL = eigenesBildURL;
  }

  public Integer getOldMiles() {
    return oldMiles;
  }

  public void setOldMiles(Integer oldMiles) {
    this.oldMiles = oldMiles;
  }

  public Double getPositionLaengenGrad() {
    return positionLaengenGrad;
  }

  public void setPositionLaengenGrad(Double positionLaengenGrad) {
    this.positionLaengenGrad = positionLaengenGrad;
  }

  public Double getPositiionBreitenGrad() {
    return positiionBreitenGrad;
  }

  public void setPositiionBreitenGrad(Double positiionBreitenGrad) {
    this.positiionBreitenGrad = positiionBreitenGrad;
  }

  public Date getNaechsterTerminCcheck() {
    return naechsterTerminCcheck;
  }

  public void setNaechsterTerminCcheck(Date naechsterTerminCcheck) {
    this.naechsterTerminCcheck = naechsterTerminCcheck;
  }

  public Boolean getFlugzeugUmgebaut() {
    return flugzeugUmgebaut;
  }

  public void setFlugzeugUmgebaut(Boolean flugzeugUmgebaut) {
    this.flugzeugUmgebaut = flugzeugUmgebaut;
  }
  
}
