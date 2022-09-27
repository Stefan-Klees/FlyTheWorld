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
@Table(name = "assignement")
@XmlRootElement



@NamedQueries({
    @NamedQuery(name = "Assignement.findAll", query = "SELECT a FROM Assignement a order by a.toIcao "),
  @NamedQuery(name = "Assignement.findByIdownerAndHold", query = "SELECT a FROM Assignement a WHERE a.idowner = :idowner and a.active = :active order by a.toIcao "),
  @NamedQuery(name = "Assignement.findByIdownerReadyForTakeoff", query = "SELECT a FROM Assignement a WHERE a.idowner = :idowner and a.active = :active order by a.toIcao "),
  @NamedQuery(name = "Assignement.findByIdownerReadyForTakeoffnachIcao", query = "SELECT a FROM Assignement a WHERE a.idowner = :idowner "
          + " and a.active = :active and a.toIcao = :toIcao order by a.toIcao "),

  @NamedQuery(name = "Assignement.findByLocationIcao", query = "SELECT a FROM Assignement a WHERE a.locationIcao = :locationIcao and a.idowner = :idowner order by a.toIcao "),
  @NamedQuery(name = "Assignement.findByLocationIcaoAndArrive", query = "SELECT a FROM Assignement a WHERE a.locationIcao = :locationIcao and a.idowner = :idowner and a.toIcao = :toIcao order by a.toIcao "),
  
  
  
  @NamedQuery(name = "Assignement.findByIdassignement", query = "SELECT a FROM Assignement a WHERE a.idassignement = :idassignement"),
  @NamedQuery(name = "Assignement.findByCreation", query = "SELECT a FROM Assignement a WHERE a.creation = :creation"),
  @NamedQuery(name = "Assignement.findByExpires", query = "SELECT a FROM Assignement a WHERE a.expires = :expires"),
  @NamedQuery(name = "Assignement.findByCommodity", query = "SELECT a FROM Assignement a WHERE a.commodity = :commodity"),
  @NamedQuery(name = "Assignement.findByIdaircraft", query = "SELECT a FROM Assignement a WHERE a.idaircraft = :idaircraft"),
  @NamedQuery(name = "Assignement.findByFromIcao", query = "SELECT a FROM Assignement a WHERE a.fromIcao = :fromIcao"),
  @NamedQuery(name = "Assignement.findByToIcao", query = "SELECT a FROM Assignement a WHERE a.toIcao = :toIcao"),
  
  @NamedQuery(name = "Assignement.findByPay", query = "SELECT a FROM Assignement a WHERE a.pay = :pay"),
  @NamedQuery(name = "Assignement.findByType", query = "SELECT a FROM Assignement a WHERE a.type = :type"),
  @NamedQuery(name = "Assignement.findByAmmount", query = "SELECT a FROM Assignement a WHERE a.ammount = :ammount"),
  @NamedQuery(name = "Assignement.findByDistance", query = "SELECT a FROM Assignement a WHERE a.distance = :distance"),
  @NamedQuery(name = "Assignement.findByActive", query = "SELECT a FROM Assignement a WHERE a.active = :active"),
  @NamedQuery(name = "Assignement.findByBearing", query = "SELECT a FROM Assignement a WHERE a.bearing = :bearing"),
  @NamedQuery(name = "Assignement.findByGruppe", query = "SELECT a FROM Assignement a WHERE a.gruppe = :gruppe"),
  @NamedQuery(name = "Assignement.findByIdgroup", query = "SELECT a FROM Assignement a WHERE a.idgroup = :idgroup"),
  @NamedQuery(name = "Assignement.findByPilotfee", query = "SELECT a FROM Assignement a WHERE a.pilotfee = :pilotfee"),
  @NamedQuery(name = "Assignement.findByUnits", query = "SELECT a FROM Assignement a WHERE a.units = :units"),
  @NamedQuery(name = "Assignement.findByDirect", query = "SELECT a FROM Assignement a WHERE a.direct = :direct"),
  @NamedQuery(name = "Assignement.findByNoext", query = "SELECT a FROM Assignement a WHERE a.noext = :noext"),
  @NamedQuery(name = "Assignement.findByComment", query = "SELECT a FROM Assignement a WHERE a.comment = :comment"),
  @NamedQuery(name = "Assignement.findByIdcommodity", query = "SELECT a FROM Assignement a WHERE a.idcommodity = :idcommodity"),
  @NamedQuery(name = "Assignement.findByIdowner", query = "SELECT a FROM Assignement a WHERE a.idowner = :idowner"),
  @NamedQuery(name = "Assignement.findByUserlock", query = "SELECT a FROM Assignement a WHERE a.userlock = :userlock"),
  @NamedQuery(name = "Assignement.findByCreatedbyuser", query = "SELECT a FROM Assignement a WHERE a.createdbyuser = :createdbyuser"),
  @NamedQuery(name = "Assignement.findByPtassigment", query = "SELECT a FROM Assignement a WHERE a.ptassigment = :ptassigment"),
  @NamedQuery(name = "Assignement.findByMpttax", query = "SELECT a FROM Assignement a WHERE a.mpttax = :mpttax"),
  @NamedQuery(name = "Assignement.findByDaysclaimedactive", query = "SELECT a FROM Assignement a WHERE a.daysclaimedactive = :daysclaimedactive")})
public class Assignement implements Serializable {

  @Size(max = 15)
  @Column(name = "lizenz")
  private String lizenz;
  @Column(name = "originalKonvertiertDurchAirline")
  private Integer originalKonvertiertDurchAirline;

  @Column(name = "langstrecke")
  private Boolean langstrecke;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idassignement")
  private Integer idassignement;
  @Column(name = "creation")
  @Temporal(TemporalType.TIMESTAMP)
  private Date creation;
  @Column(name = "expires")
  @Temporal(TemporalType.TIMESTAMP)
  private Date expires;
  @Size(max = 2500)
  @Column(name = "commodity")
  private String commodity;
  @Column(name = "idaircraft")
  private Integer idaircraft;
  @Size(max = 45)
  @Column(name = "from_icao")
  private String fromIcao;
  @Size(max = 45)
  @Column(name = "to_icao")
  private String toIcao;
  @Size(max = 45)
  @Column(name = "location_icao")
  private String locationIcao;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "pay")
  private Double pay;
  @Column(name = "type")
  private Integer type;
  @Column(name = "ammount")
  private Integer ammount;
  @Column(name = "distance")
  private Integer distance;
  @Column(name = "active")
  private Integer active;
  @Column(name = "bearing")
  private Integer bearing;
  @Size(max = 250)
  @Column(name = "gruppe")
  private String gruppe;
  @Column(name = "idgroup")
  private Integer idgroup;
  @Column(name = "pilotfee")
  private Integer pilotfee;
  @Size(max = 250)
  @Column(name = "units")
  private String units;
  @Column(name = "direct")
  private Integer direct;
  @Column(name = "noext")
  private Integer noext;
  @Lob
  @Size(max = 2147483647)
  @Column(name = "comment")
  private String comment;
  @Column(name = "idcommodity")
  private Integer idcommodity;
  @Column(name = "idowner")
  private Integer idowner;
  @Column(name = "userlock")
  private Integer userlock;
  @Size(max = 250)
  @Column(name = "createdbyuser")
  private String createdbyuser;
  @Size(max = 250)
  @Column(name = "ptassigment")
  private String ptassigment;
  @Column(name = "mpttax")
  private Integer mpttax;
  @Column(name = "daysclaimedactive")
  private Integer daysclaimedactive;
  @Column(name = "idAirline")
  private Integer idAirline;
  @Column(name = "idRoute")
  private Integer idRoute;
  @Column(name = "oeffentlich")
  private Integer oeffentlich;
  @Column(name = "bonusoeffentlich")
  private Double bonusoeffentlich;
  @Column(name = "bonusclosed")
  private Double bonusclosed;
  @Size(max = 250)
  @Column(name = "nameairline")
  private String nameairline;
  @Size(max = 250)
  @Column(name = "fromName")
  private String fromName;
  @Size(max = 250)
  @Column(name = "toName")
  private String toName;
  @Column(name = "konvertiert")
  private Boolean konvertiert;
  @Size(max = 250)
  @Column(name = "flugrouteName")
  private String flugrouteName;
  @Size(max = 250)
  @Column(name = "fromAirportLandCity")
  private String fromAirportLandCity;
  @Size(max = 250)
  @Column(name = "toAirportLandCity")
  private String toAirportLandCity;
  @Size(max = 250)
  @Column(name = "airlineLogo")
  private String airlineLogo;
  @Size(max = 250)
  @Column(name = "ceoAirline")
  private String ceoAirline;
  @Column(name = "routenArt")
  private Integer routenArt;
  @Column(name = "isBusinessClass")
  private Integer isBusinessClass;
  @Column(name = "gepaeck")
  private Integer gepaeck;
  @Column(name = "provision")
  private Double provision;
  @Column(name = "idTerminal")
  private Integer idTerminal;
  @Column(name = "idFBO")
  private Integer idFBO;
  @Size(max = 45)
  @Column(name = "icaoCodeFluggesellschaft")
  private String icaoCodeFluggesellschaft;
  @Column(name = "gewichtPax")
  private Integer gewichtPax;
  @Column(name = "verlaengert")
  private Boolean verlaengert;
  @Column(name = "gesplittet")
  private Boolean gesplittet;
  @Column(name = "idjob")
  private Integer idjob;

  public Assignement() {
  }

  public Assignement(Integer idassignement) {
    this.idassignement = idassignement;
  }

  public Integer getIdassignement() {
    return idassignement;
  }

  public void setIdassignement(Integer idassignement) {
    this.idassignement = idassignement;
  }

  public Date getCreation() {
    return creation;
  }

  public void setCreation(Date creation) {
    this.creation = creation;
  }

  public Date getExpires() {
    return expires;
  }

  public void setExpires(Date expires) {
    this.expires = expires;
  }

  public String getCommodity() {
    return commodity;
  }

  public void setCommodity(String commodity) {
    this.commodity = commodity;
  }

  public Integer getIdaircraft() {
    return idaircraft;
  }

  public void setIdaircraft(Integer idaircraft) {
    this.idaircraft = idaircraft;
  }

  public String getFromIcao() {
    return fromIcao;
  }

  public void setFromIcao(String fromIcao) {
    this.fromIcao = fromIcao;
  }

  public String getToIcao() {
    return toIcao;
  }

  public void setToIcao(String toIcao) {
    this.toIcao = toIcao;
  }

  public String getLocationIcao() {
    return locationIcao;
  }

  public void setLocationIcao(String locationIcao) {
    this.locationIcao = locationIcao;
  }

  public Double getPay() {
    return pay;
  }

  public void setPay(Double pay) {
    this.pay = pay;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Integer getAmmount() {
    return ammount;
  }

  public void setAmmount(Integer ammount) {
    this.ammount = ammount;
  }

  public Integer getDistance() {
    return distance;
  }

  public void setDistance(Integer distance) {
    this.distance = distance;
  }

  public Integer getActive() {
    return active;
  }

  public void setActive(Integer active) {
    this.active = active;
  }

  public Integer getBearing() {
    return bearing;
  }

  public void setBearing(Integer bearing) {
    this.bearing = bearing;
  }

  public String getGruppe() {
    return gruppe;
  }

  public void setGruppe(String gruppe) {
    this.gruppe = gruppe;
  }

  public Integer getIdgroup() {
    return idgroup;
  }

  public void setIdgroup(Integer idgroup) {
    this.idgroup = idgroup;
  }

  public Integer getPilotfee() {
    return pilotfee;
  }

  public void setPilotfee(Integer pilotfee) {
    this.pilotfee = pilotfee;
  }

  public String getUnits() {
    return units;
  }

  public void setUnits(String units) {
    this.units = units;
  }

  public Integer getDirect() {
    return direct;
  }

  public void setDirect(Integer direct) {
    this.direct = direct;
  }

  public Integer getNoext() {
    return noext;
  }

  public void setNoext(Integer noext) {
    this.noext = noext;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Integer getIdcommodity() {
    return idcommodity;
  }

  public void setIdcommodity(Integer idcommodity) {
    this.idcommodity = idcommodity;
  }

  public Integer getIdowner() {
    return idowner;
  }

  public void setIdowner(Integer idowner) {
    this.idowner = idowner;
  }

  public Integer getUserlock() {
    return userlock;
  }

  public void setUserlock(Integer userlock) {
    this.userlock = userlock;
  }

  public String getCreatedbyuser() {
    return createdbyuser;
  }

  public void setCreatedbyuser(String createdbyuser) {
    this.createdbyuser = createdbyuser;
  }

  public String getPtassigment() {
    return ptassigment;
  }

  public void setPtassigment(String ptassigment) {
    this.ptassigment = ptassigment;
  }

  public Integer getMpttax() {
    return mpttax;
  }

  public void setMpttax(Integer mpttax) {
    this.mpttax = mpttax;
  }

  public Integer getDaysclaimedactive() {
    return daysclaimedactive;
  }

  public void setDaysclaimedactive(Integer daysclaimedactive) {
    this.daysclaimedactive = daysclaimedactive;
  }

  public Integer getIdAirline() {
    return idAirline;
  }

  public void setIdAirline(Integer idAirline) {
    this.idAirline = idAirline;
  }

  public Integer getIdRoute() {
    return idRoute;
  }

  public void setIdRoute(Integer idRoute) {
    this.idRoute = idRoute;
  }

  public Integer getOeffentlich() {
    return oeffentlich;
  }

  public void setOeffentlich(Integer oeffentlich) {
    this.oeffentlich = oeffentlich;
  }

  public Double getBonusoeffentlich() {
    return bonusoeffentlich;
  }

  public void setBonusoeffentlich(Double bonusoeffentlich) {
    this.bonusoeffentlich = bonusoeffentlich;
  }

  public Double getBonusclosed() {
    return bonusclosed;
  }

  public void setBonusclosed(Double bonusclosed) {
    this.bonusclosed = bonusclosed;
  }

  public String getNameairline() {
    return nameairline;
  }

  public void setNameairline(String nameairline) {
    this.nameairline = nameairline;
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

  public Boolean getKonvertiert() {
    return konvertiert;
  }

  public void setKonvertiert(Boolean konvertiert) {
    this.konvertiert = konvertiert;
  }

  public String getFlugrouteName() {
    return flugrouteName;
  }

  public void setFlugrouteName(String flugrouteName) {
    this.flugrouteName = flugrouteName;
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

  public String getAirlineLogo() {
    return airlineLogo;
  }

  public void setAirlineLogo(String airlineLogo) {
    this.airlineLogo = airlineLogo;
  }

  public String getCeoAirline() {
    return ceoAirline;
  }

  public void setCeoAirline(String ceoAirline) {
    this.ceoAirline = ceoAirline;
  }

  public Integer getRoutenArt() {
    return routenArt;
  }

  public void setRoutenArt(Integer routenArt) {
    this.routenArt = routenArt;
  }

  public Integer getIsBusinessClass() {
    return isBusinessClass;
  }

  public void setIsBusinessClass(Integer isBusinessClass) {
    this.isBusinessClass = isBusinessClass;
  }

  public Integer getGepaeck() {
    return gepaeck;
  }

  public void setGepaeck(Integer gepaeck) {
    this.gepaeck = gepaeck;
  }

  public Double getProvision() {
    return provision;
  }

  public void setProvision(Double provision) {
    this.provision = provision;
  }

  public Integer getIdTerminal() {
    return idTerminal;
  }

  public void setIdTerminal(Integer idTerminal) {
    this.idTerminal = idTerminal;
  }

  public Integer getIdFBO() {
    return idFBO;
  }

  public void setIdFBO(Integer idFBO) {
    this.idFBO = idFBO;
  }

  public String getIcaoCodeFluggesellschaft() {
    return icaoCodeFluggesellschaft;
  }

  public void setIcaoCodeFluggesellschaft(String icaoCodeFluggesellschaft) {
    this.icaoCodeFluggesellschaft = icaoCodeFluggesellschaft;
  }

  public Integer getGewichtPax() {
    return gewichtPax;
  }

  public void setGewichtPax(Integer gewichtPax) {
    this.gewichtPax = gewichtPax;
  }

  public Boolean getVerlaengert() {
    return verlaengert;
  }

  public void setVerlaengert(Boolean verlaengert) {
    this.verlaengert = verlaengert;
  }

  public Boolean getGesplittet() {
    return gesplittet;
  }

  public void setGesplittet(Boolean gesplittet) {
    this.gesplittet = gesplittet;
  }

  public Integer getIdjob() {
    return idjob;
  }

  public void setIdjob(Integer idjob) {
    this.idjob = idjob;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idassignement != null ? idassignement.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Assignement)) {
      return false;
    }
    Assignement other = (Assignement) object;
    if ((this.idassignement == null && other.idassignement != null) || (this.idassignement != null && !this.idassignement.equals(other.idassignement))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Assignement[ idassignement=" + idassignement + " ]";
  }

  public Boolean getLangstrecke() {
    return langstrecke;
  }

  public void setLangstrecke(Boolean langstrecke) {
    this.langstrecke = langstrecke;
  }

  public String getLizenz() {
    return lizenz;
  }

  public void setLizenz(String lizenz) {
    this.lizenz = lizenz;
  }

  public Integer getOriginalKonvertiertDurchAirline() {
    return originalKonvertiertDurchAirline;
  }

  public void setOriginalKonvertiertDurchAirline(Integer originalKonvertiertDurchAirline) {
    this.originalKonvertiertDurchAirline = originalKonvertiertDurchAirline;
  }
  
}
