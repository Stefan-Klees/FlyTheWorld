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
@Table(name = "yaacarskopf")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Yaacarskopf.findAll", query = "SELECT y FROM Yaacarskopf y")})
public class Yaacarskopf implements Serializable {

  @Column(name = "flugOK")
  private Boolean flugOK;

  @Lob
  @Size(max = 65535)
  @Column(name = "clientdaten")
  private String clientdaten;

  @Lob
  @Size(max = 65535)
  @Column(name = "freitext")
  private String freitext;

  @Size(max = 250)
  @Column(name = "username")
  private String username;

  @Column(name = "missionsid")
  private Integer missionsid;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idyaacarskopf")
  private Integer idyaacarskopf;
  @Column(name = "userid")
  private Integer userid;
  @Size(max = 250)
  @Column(name = "userhash")
  private String userhash;
  @Size(max = 250)
  @Column(name = "protokollversion")
  private String protokollversion;
  @Size(max = 15)
  @Column(name = "flugnummer")
  private String flugnummer;
  @Column(name = "missionsart")
  private Integer missionsart;
  @Column(name = "flugerstelltam")
  @Temporal(TemporalType.TIMESTAMP)
  private Date flugerstelltam;
  @Column(name = "flugstatus")
  private Integer flugstatus;
  @Size(max = 250)
  @Column(name = "flugzeugtype")
  private String flugzeugtype;
  @Column(name = "flugzeugid")
  private Integer flugzeugid;
  @Size(max = 45)
  @Column(name = "flugzeugkennung")
  private String flugzeugkennung;
  @Lob
  @Size(max = 65535)
  @Column(name = "flugroute")
  private String flugroute;
  @Size(max = 15)
  @Column(name = "departureicao")
  private String departureicao;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "departurelongitude")
  private Double departurelongitude;
  @Column(name = "departurelatitude")
  private Double departurelatitude;
  @Size(max = 15)
  @Column(name = "arrivalicao")
  private String arrivalicao;
  @Column(name = "arrivallongitude")
  private Double arrivallongitude;
  @Column(name = "arrivallatitude")
  private Double arrivallatitude;
  @Size(max = 15)
  @Column(name = "alternateicao")
  private String alternateicao;
  @Column(name = "alternatelongitude")
  private Double alternatelongitude;
  @Column(name = "alternatelatitude")
  private Double alternatelatitude;
  @Column(name = "paxanzahl")
  private Integer paxanzahl;
  @Column(name = "paxgewicht")
  private Integer paxgewicht;
  @Column(name = "cargogewicht")
  private Integer cargogewicht;
  @Column(name = "tankmengekg")
  private Integer tankmengekg;
  @Column(name = "resttankmengekg")
  private Integer resttankmengekg;
  @Column(name = "verbrauchtetankmengekg")
  private Integer verbrauchtetankmengekg;
  @Column(name = "geflogenemeilen")
  private Integer geflogenemeilen;
  @Column(name = "geflogenezeit")
  private Integer geflogenezeit;
  @Column(name = "blockzeit")
  private Integer blockzeit;
  @Lob
  @Size(max = 65535)
  @Column(name = "usermessage")
  private String usermessage;
  @Column(name = "letztepositionlatitude")
  private Double letztepositionlatitude;
  @Column(name = "letztepositionlongitude")
  private Double letztepositionlongitude;

  public Yaacarskopf() {
  }

  public Yaacarskopf(Integer idyaacarskopf) {
    this.idyaacarskopf = idyaacarskopf;
  }

  public Integer getIdyaacarskopf() {
    return idyaacarskopf;
  }

  public void setIdyaacarskopf(Integer idyaacarskopf) {
    this.idyaacarskopf = idyaacarskopf;
  }

  public Integer getUserid() {
    return userid;
  }

  public void setUserid(Integer userid) {
    this.userid = userid;
  }

  public String getUserhash() {
    return userhash;
  }

  public void setUserhash(String userhash) {
    this.userhash = userhash;
  }

  public String getProtokollversion() {
    return protokollversion;
  }

  public void setProtokollversion(String protokollversion) {
    this.protokollversion = protokollversion;
  }

  public String getFlugnummer() {
    return flugnummer;
  }

  public void setFlugnummer(String flugnummer) {
    this.flugnummer = flugnummer;
  }

  public Integer getMissionsart() {
    return missionsart;
  }

  public void setMissionsart(Integer missionsart) {
    this.missionsart = missionsart;
  }


  public Date getFlugerstelltam() {
    return flugerstelltam;
  }

  public void setFlugerstelltam(Date flugerstelltam) {
    this.flugerstelltam = flugerstelltam;
  }

  public Integer getFlugstatus() {
    return flugstatus;
  }

  public void setFlugstatus(Integer flugstatus) {
    this.flugstatus = flugstatus;
  }

  public String getFlugzeugtype() {
    return flugzeugtype;
  }

  public void setFlugzeugtype(String flugzeugtype) {
    this.flugzeugtype = flugzeugtype;
  }

  public Integer getFlugzeugid() {
    return flugzeugid;
  }

  public void setFlugzeugid(Integer flugzeugid) {
    this.flugzeugid = flugzeugid;
  }

  public String getFlugzeugkennung() {
    return flugzeugkennung;
  }

  public void setFlugzeugkennung(String flugzeugkennung) {
    this.flugzeugkennung = flugzeugkennung;
  }

  public String getFlugroute() {
    return flugroute;
  }

  public void setFlugroute(String flugroute) {
    this.flugroute = flugroute;
  }

  public String getDepartureicao() {
    return departureicao;
  }

  public void setDepartureicao(String departureicao) {
    this.departureicao = departureicao;
  }

  public Double getDeparturelongitude() {
    return departurelongitude;
  }

  public void setDeparturelongitude(Double departurelongitude) {
    this.departurelongitude = departurelongitude;
  }

  public Double getDeparturelatitude() {
    return departurelatitude;
  }

  public void setDeparturelatitude(Double departurelatitude) {
    this.departurelatitude = departurelatitude;
  }

  public String getArrivalicao() {
    return arrivalicao;
  }

  public void setArrivalicao(String arrivalicao) {
    this.arrivalicao = arrivalicao;
  }

  public Double getArrivallongitude() {
    return arrivallongitude;
  }

  public void setArrivallongitude(Double arrivallongitude) {
    this.arrivallongitude = arrivallongitude;
  }

  public Double getArrivallatitude() {
    return arrivallatitude;
  }

  public void setArrivallatitude(Double arrivallatitude) {
    this.arrivallatitude = arrivallatitude;
  }

  public String getAlternateicao() {
    return alternateicao;
  }

  public void setAlternateicao(String alternateicao) {
    this.alternateicao = alternateicao;
  }

  public Double getAlternatelongitude() {
    return alternatelongitude;
  }

  public void setAlternatelongitude(Double alternatelongitude) {
    this.alternatelongitude = alternatelongitude;
  }

  public Double getAlternatelatitude() {
    return alternatelatitude;
  }

  public void setAlternatelatitude(Double alternatelatitude) {
    this.alternatelatitude = alternatelatitude;
  }

  public Integer getPaxanzahl() {
    return paxanzahl;
  }

  public void setPaxanzahl(Integer paxanzahl) {
    this.paxanzahl = paxanzahl;
  }

  public Integer getPaxgewicht() {
    return paxgewicht;
  }

  public void setPaxgewicht(Integer paxgewicht) {
    this.paxgewicht = paxgewicht;
  }

  public Integer getCargogewicht() {
    return cargogewicht;
  }

  public void setCargogewicht(Integer cargogewicht) {
    this.cargogewicht = cargogewicht;
  }

  public Integer getTankmengekg() {
    return tankmengekg;
  }

  public void setTankmengekg(Integer tankmengekg) {
    this.tankmengekg = tankmengekg;
  }

  public Integer getResttankmengekg() {
    return resttankmengekg;
  }

  public void setResttankmengekg(Integer resttankmengekg) {
    this.resttankmengekg = resttankmengekg;
  }

  public Integer getVerbrauchtetankmengekg() {
    return verbrauchtetankmengekg;
  }

  public void setVerbrauchtetankmengekg(Integer verbrauchtetankmengekg) {
    this.verbrauchtetankmengekg = verbrauchtetankmengekg;
  }

  public Integer getGeflogenemeilen() {
    return geflogenemeilen;
  }

  public void setGeflogenemeilen(Integer geflogenemeilen) {
    this.geflogenemeilen = geflogenemeilen;
  }

  public Integer getGeflogenezeit() {
    return geflogenezeit;
  }

  public void setGeflogenezeit(Integer geflogenezeit) {
    this.geflogenezeit = geflogenezeit;
  }

  public Integer getBlockzeit() {
    return blockzeit;
  }

  public void setBlockzeit(Integer blockzeit) {
    this.blockzeit = blockzeit;
  }

  public String getUsermessage() {
    return usermessage;
  }

  public void setUsermessage(String usermessage) {
    this.usermessage = usermessage;
  }

  public Double getLetztepositionlatitude() {
    return letztepositionlatitude;
  }

  public void setLetztepositionlatitude(Double letztepositionlatitude) {
    this.letztepositionlatitude = letztepositionlatitude;
  }

  public Double getLetztepositionlongitude() {
    return letztepositionlongitude;
  }

  public void setLetztepositionlongitude(Double letztepositionlongitude) {
    this.letztepositionlongitude = letztepositionlongitude;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idyaacarskopf != null ? idyaacarskopf.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Yaacarskopf)) {
      return false;
    }
    Yaacarskopf other = (Yaacarskopf) object;
    if ((this.idyaacarskopf == null && other.idyaacarskopf != null) || (this.idyaacarskopf != null && !this.idyaacarskopf.equals(other.idyaacarskopf))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Yaacarskopf[ idyaacarskopf=" + idyaacarskopf + " ]";
  }

  public Integer getMissionsid() {
    return missionsid;
  }

  public void setMissionsid(Integer missionsid) {
    this.missionsid = missionsid;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFreitext() {
    return freitext;
  }

  public void setFreitext(String freitext) {
    this.freitext = freitext;
  }

  public String getClientdaten() {
    return clientdaten;
  }

  public void setClientdaten(String clientdaten) {
    this.clientdaten = clientdaten;
  }

  public Boolean getFlugOK() {
    return flugOK;
  }

  public void setFlugOK(Boolean flugOK) {
    this.flugOK = flugOK;
  }
  
}
