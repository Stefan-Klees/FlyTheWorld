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
import javax.persistence.Lob;
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
@Table(name = "Flugrouten")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Flugrouten.findAll", query = "SELECT f FROM Flugrouten f")})
public class Flugrouten implements Serializable {

  @Column(name = "idPilot")
  private Integer idPilot;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idFlugrouten")
  private Integer idFlugrouten;
  @Column(name = "idFbo")
  private Integer idFbo;
  @Column(name = "idUserFBO")
  private Integer idUserFBO;
  @Column(name = "idFluggesellschaft")
  private Integer idFluggesellschaft;
  @Column(name = "idTerminalDep")
  private Integer idTerminalDep;
  @Column(name = "idTerminalArr")
  private Integer idTerminalArr;
  @Size(max = 250)
  @Column(name = "name")
  private String name;
  @Size(max = 45)
  @Column(name = "vonIcao")
  private String vonIcao;
  @Size(max = 45)
  @Column(name = "nachicao")
  private String nachicao;
  @Lob
  @Size(max = 65535)
  @Column(name = "bemerkung")
  private String bemerkung;
  @Column(name = "maxPaxgroesse")
  private Integer maxPaxgroesse;
  @Column(name = "oeffentlich")
  private Boolean oeffentlich;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "BonusFuerAirlinePiloten")
  private Double bonusFuerAirlinePiloten;
  @Column(name = "BonusFuerPiloten")
  private Double bonusFuerPiloten;
  @Lob
  @Size(max = 65535)
  @Column(name = "passengersTitle")
  private String passengersTitle;
  @Column(name = "direct")
  private Integer direct;
  @Column(name = "distance")
  private Integer distance;
  @Size(max = 250)
  @Column(name = "fromName")
  private String fromName;
  @Size(max = 250)
  @Column(name = "toName")
  private String toName;
  @Size(max = 250)
  @Column(name = "fromAirportLandCity")
  private String fromAirportLandCity;
  @Size(max = 250)
  @Column(name = "toAirportLandCity")
  private String toAirportLandCity;
  @Column(name = "routenArt")
  private Integer routenArt;
  @Column(name = "provision")
  private Double provision;
  @Column(name = "UseBusinessLounge")
  private Boolean useBusinessLounge;
  @Column(name = "letzterFlug")
  @Temporal(TemporalType.DATE)
  private Date letzterFlug;
  @Column(name = "aktiv")
  private Boolean aktiv;
  @Column(name = "umsatz")
  private Double umsatz;
  @Column(name = "umsatzmenge")
  private Integer umsatzmenge;
  @Column(name = "erzeugteMenge")
  private Integer erzeugteMenge;
  @Column(name = "ecoAktiv")
  private Boolean ecoAktiv;
  @Column(name = "langstrecke")
  private Boolean langstrecke;
  @Column(name = "mo")
  private Boolean mo;
  @Column(name = "di")
  private Boolean di;
  @Column(name = "mi")
  private Boolean mi;
  @Column(name = "don")
  private Boolean don;
  @Column(name = "fr")
  private Boolean fr;
  @Column(name = "sa")
  private Boolean sa;
  @Column(name = "so")
  private Boolean so;
  @Column(name = "ausfuehrungPerDatum")
  private Boolean ausfuehrungPerDatum;
  @Column(name = "ausfuehrungAm")
  @Temporal(TemporalType.TIMESTAMP)
  private Date ausfuehrungAm;
  @Column(name = "letzteAusfuehrungAm")
  @Temporal(TemporalType.TIMESTAMP)
  private Date letzteAusfuehrungAm;
  @Size(max = 10)
  @Column(name = "flugzeugLizenz")
  private String flugzeugLizenz;
  @Column(name = "routenType")
  private Integer routenType;
  @Column(name = "naechsteAusfuehrungAm")
  @Temporal(TemporalType.TIMESTAMP)
  private Date naechsteAusfuehrungAm;
  @Column(name = "wiederholungen")
  private Integer wiederholungen;
  @Column(name = "ausfuehrungsZaehler")
  private Integer ausfuehrungsZaehler;
  @Column(name = "maxPax")
  private Integer maxPax;
  @Column(name = "maxBusiness")
  private Integer maxBusiness;
  @Column(name = "maxCargo")
  private Integer maxCargo;
  @Column(name = "idFlugzeugMietKauf")
  private Integer idFlugzeugMietKauf;

  public Flugrouten() {
  }

  public Flugrouten(Integer idFlugrouten) {
    this.idFlugrouten = idFlugrouten;
  }

  public Integer getIdFlugrouten() {
    return idFlugrouten;
  }

  public void setIdFlugrouten(Integer idFlugrouten) {
    this.idFlugrouten = idFlugrouten;
  }

  public Integer getIdFbo() {
    return idFbo;
  }

  public void setIdFbo(Integer idFbo) {
    this.idFbo = idFbo;
  }

  public Integer getIdUserFBO() {
    return idUserFBO;
  }

  public void setIdUserFBO(Integer idUserFBO) {
    this.idUserFBO = idUserFBO;
  }

  public Integer getIdFluggesellschaft() {
    return idFluggesellschaft;
  }

  public void setIdFluggesellschaft(Integer idFluggesellschaft) {
    this.idFluggesellschaft = idFluggesellschaft;
  }

  public Integer getIdTerminalDep() {
    return idTerminalDep;
  }

  public void setIdTerminalDep(Integer idTerminalDep) {
    this.idTerminalDep = idTerminalDep;
  }

  public Integer getIdTerminalArr() {
    return idTerminalArr;
  }

  public void setIdTerminalArr(Integer idTerminalArr) {
    this.idTerminalArr = idTerminalArr;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getVonIcao() {
    return vonIcao;
  }

  public void setVonIcao(String vonIcao) {
    this.vonIcao = vonIcao;
  }

  public String getNachicao() {
    return nachicao;
  }

  public void setNachicao(String nachicao) {
    this.nachicao = nachicao;
  }

  public String getBemerkung() {
    return bemerkung;
  }

  public void setBemerkung(String bemerkung) {
    this.bemerkung = bemerkung;
  }

  public Integer getMaxPaxgroesse() {
    return maxPaxgroesse;
  }

  public void setMaxPaxgroesse(Integer maxPaxgroesse) {
    this.maxPaxgroesse = maxPaxgroesse;
  }

  public Boolean getOeffentlich() {
    return oeffentlich;
  }

  public void setOeffentlich(Boolean oeffentlich) {
    this.oeffentlich = oeffentlich;
  }

  public Double getBonusFuerAirlinePiloten() {
    return bonusFuerAirlinePiloten;
  }

  public void setBonusFuerAirlinePiloten(Double bonusFuerAirlinePiloten) {
    this.bonusFuerAirlinePiloten = bonusFuerAirlinePiloten;
  }

  public Double getBonusFuerPiloten() {
    return bonusFuerPiloten;
  }

  public void setBonusFuerPiloten(Double bonusFuerPiloten) {
    this.bonusFuerPiloten = bonusFuerPiloten;
  }

  public String getPassengersTitle() {
    return passengersTitle;
  }

  public void setPassengersTitle(String passengersTitle) {
    this.passengersTitle = passengersTitle;
  }

  public Integer getDirect() {
    return direct;
  }

  public void setDirect(Integer direct) {
    this.direct = direct;
  }

  public Integer getDistance() {
    return distance;
  }

  public void setDistance(Integer distance) {
    this.distance = distance;
  }

  public String getFromName() {
    return fromName;
  }

  public void setFromName(String fromName) {
    this.fromName = fromName;
  }

  public String getToName() {
    return toName;
  }

  public void setToName(String toName) {
    this.toName = toName;
  }

  public String getFromAirportLandCity() {
    return fromAirportLandCity;
  }

  public void setFromAirportLandCity(String fromAirportLandCity) {
    this.fromAirportLandCity = fromAirportLandCity;
  }

  public String getToAirportLandCity() {
    return toAirportLandCity;
  }

  public void setToAirportLandCity(String toAirportLandCity) {
    this.toAirportLandCity = toAirportLandCity;
  }

  public Integer getRoutenArt() {
    return routenArt;
  }

  public void setRoutenArt(Integer routenArt) {
    this.routenArt = routenArt;
  }

  public Double getProvision() {
    return provision;
  }

  public void setProvision(Double provision) {
    this.provision = provision;
  }

  public Boolean getUseBusinessLounge() {
    return useBusinessLounge;
  }

  public void setUseBusinessLounge(Boolean useBusinessLounge) {
    this.useBusinessLounge = useBusinessLounge;
  }

  public Date getLetzterFlug() {
    return letzterFlug;
  }

  public void setLetzterFlug(Date letzterFlug) {
    this.letzterFlug = letzterFlug;
  }

  public Boolean getAktiv() {
    return aktiv;
  }

  public void setAktiv(Boolean aktiv) {
    this.aktiv = aktiv;
  }

  public Double getUmsatz() {
    return umsatz;
  }

  public void setUmsatz(Double umsatz) {
    this.umsatz = umsatz;
  }

  public Integer getUmsatzmenge() {
    return umsatzmenge;
  }

  public void setUmsatzmenge(Integer umsatzmenge) {
    this.umsatzmenge = umsatzmenge;
  }

  public Integer getErzeugteMenge() {
    return erzeugteMenge;
  }

  public void setErzeugteMenge(Integer erzeugteMenge) {
    this.erzeugteMenge = erzeugteMenge;
  }

  public Boolean getEcoAktiv() {
    return ecoAktiv;
  }

  public void setEcoAktiv(Boolean ecoAktiv) {
    this.ecoAktiv = ecoAktiv;
  }

  public Boolean getLangstrecke() {
    return langstrecke;
  }

  public void setLangstrecke(Boolean langstrecke) {
    this.langstrecke = langstrecke;
  }

  public Boolean getMo() {
    return mo;
  }

  public void setMo(Boolean mo) {
    this.mo = mo;
  }

  public Boolean getDi() {
    return di;
  }

  public void setDi(Boolean di) {
    this.di = di;
  }

  public Boolean getMi() {
    return mi;
  }

  public void setMi(Boolean mi) {
    this.mi = mi;
  }

  public Boolean getDon() {
    return don;
  }

  public void setDon(Boolean don) {
    this.don = don;
  }

  public Boolean getFr() {
    return fr;
  }

  public void setFr(Boolean fr) {
    this.fr = fr;
  }

  public Boolean getSa() {
    return sa;
  }

  public void setSa(Boolean sa) {
    this.sa = sa;
  }

  public Boolean getSo() {
    return so;
  }

  public void setSo(Boolean so) {
    this.so = so;
  }

  public Boolean getAusfuehrungPerDatum() {
    return ausfuehrungPerDatum;
  }

  public void setAusfuehrungPerDatum(Boolean ausfuehrungPerDatum) {
    this.ausfuehrungPerDatum = ausfuehrungPerDatum;
  }

  public Date getAusfuehrungAm() {
    return ausfuehrungAm;
  }

  public void setAusfuehrungAm(Date ausfuehrungAm) {
    this.ausfuehrungAm = ausfuehrungAm;
  }

  public Date getLetzteAusfuehrungAm() {
    return letzteAusfuehrungAm;
  }

  public void setLetzteAusfuehrungAm(Date letzteAusfuehrungAm) {
    this.letzteAusfuehrungAm = letzteAusfuehrungAm;
  }

  public String getFlugzeugLizenz() {
    return flugzeugLizenz;
  }

  public void setFlugzeugLizenz(String flugzeugLizenz) {
    this.flugzeugLizenz = flugzeugLizenz;
  }

  public Integer getRoutenType() {
    return routenType;
  }

  public void setRoutenType(Integer routenType) {
    this.routenType = routenType;
  }

  public Date getNaechsteAusfuehrungAm() {
    return naechsteAusfuehrungAm;
  }

  public void setNaechsteAusfuehrungAm(Date naechsteAusfuehrungAm) {
    this.naechsteAusfuehrungAm = naechsteAusfuehrungAm;
  }

  public Integer getWiederholungen() {
    return wiederholungen;
  }

  public void setWiederholungen(Integer wiederholungen) {
    this.wiederholungen = wiederholungen;
  }

  public Integer getAusfuehrungsZaehler() {
    return ausfuehrungsZaehler;
  }

  public void setAusfuehrungsZaehler(Integer ausfuehrungsZaehler) {
    this.ausfuehrungsZaehler = ausfuehrungsZaehler;
  }

  public Integer getMaxPax() {
    return maxPax;
  }

  public void setMaxPax(Integer maxPax) {
    this.maxPax = maxPax;
  }

  public Integer getMaxBusiness() {
    return maxBusiness;
  }

  public void setMaxBusiness(Integer maxBusiness) {
    this.maxBusiness = maxBusiness;
  }

  public Integer getMaxCargo() {
    return maxCargo;
  }

  public void setMaxCargo(Integer maxCargo) {
    this.maxCargo = maxCargo;
  }

  public Integer getIdFlugzeugMietKauf() {
    return idFlugzeugMietKauf;
  }

  public void setIdFlugzeugMietKauf(Integer idFlugzeugMietKauf) {
    this.idFlugzeugMietKauf = idFlugzeugMietKauf;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idFlugrouten != null ? idFlugrouten.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Flugrouten)) {
      return false;
    }
    Flugrouten other = (Flugrouten) object;
    if ((this.idFlugrouten == null && other.idFlugrouten != null) || (this.idFlugrouten != null && !this.idFlugrouten.equals(other.idFlugrouten))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Flugrouten[ idFlugrouten=" + idFlugrouten + " ]";
  }

  public Integer getIdPilot() {
    return idPilot;
  }

  public void setIdPilot(Integer idPilot) {
    this.idPilot = idPilot;
  }
  
}
