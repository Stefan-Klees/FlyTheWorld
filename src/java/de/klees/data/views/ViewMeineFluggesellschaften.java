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
@Table(name = "viewMeineFluggesellschaften")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ViewMeineFluggesellschaften.findAll", query = "SELECT v FROM ViewMeineFluggesellschaften v")})
public class ViewMeineFluggesellschaften implements Serializable {

  private static final long serialVersionUID = 1L;

  @Basic(optional = false)
  @NotNull
  @Column(name = "idFluggesellschaft")
  private int idFluggesellschaft;

  @Size(max = 250)
  @Column(name = "logoURL")
  private String logoURL;

  @Column(name = "waren")
  private Integer waren;
  @Size(max = 250)
  @Column(name = "flughafenName")
  private String flughafenName;

  @Column(name = "iduser")
  private Integer iduser;
  @Size(max = 250)
  @Column(name = "name")
  private String name;
  @Size(max = 10)
  @Column(name = "icaoCode")
  private String icaoCode;
  @Size(max = 45)
  @Column(name = "icao")
  private String icao;
  @Size(max = 250)
  @Column(name = "callname")
  private String callname;
  // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
  @Size(max = 250)
  @Column(name = "email")
  private String email;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "bonus")
  private Double bonus;
  @Column(name = "meilen")
  private Integer meilen;
  @Column(name = "zeit")
  private Integer zeit;
  @Column(name = "passagiere")
  private Integer passagiere;
  @Column(name = "umsatz")
  private Double umsatz;
  @Column(name = "letzterFlug")
  @Temporal(TemporalType.TIMESTAMP)
  private Date letzterFlug;
  @Size(max = 250)
  @Column(name = "besitzerName")
  private String besitzerName;
  @Basic(optional = false)
  @NotNull
  @Column(name = "idPilot")
  @Id
  private int idPilot;

  public ViewMeineFluggesellschaften() {
  }

  public Integer getIduser() {
    return iduser;
  }

  public void setIduser(Integer iduser) {
    this.iduser = iduser;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIcaoCode() {
    return icaoCode;
  }

  public void setIcaoCode(String icaoCode) {
    this.icaoCode = icaoCode;
  }

  public String getIcao() {
    return icao;
  }

  public void setIcao(String icao) {
    this.icao = icao;
  }

  public String getCallname() {
    return callname;
  }

  public void setCallname(String callname) {
    this.callname = callname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Double getBonus() {
    return bonus;
  }

  public void setBonus(Double bonus) {
    this.bonus = bonus;
  }

  public Integer getMeilen() {
    return meilen;
  }

  public void setMeilen(Integer meilen) {
    this.meilen = meilen;
  }

  public Integer getZeit() {
    return zeit;
  }

  public void setZeit(Integer zeit) {
    this.zeit = zeit;
  }

  public Integer getPassagiere() {
    return passagiere;
  }

  public void setPassagiere(Integer passagiere) {
    this.passagiere = passagiere;
  }

  public Double getUmsatz() {
    return umsatz;
  }

  public void setUmsatz(Double umsatz) {
    this.umsatz = umsatz;
  }

  public Date getLetzterFlug() {
    return letzterFlug;
  }

  public void setLetzterFlug(Date letzterFlug) {
    this.letzterFlug = letzterFlug;
  }

  public String getBesitzerName() {
    return besitzerName;
  }

  public void setBesitzerName(String besitzerName) {
    this.besitzerName = besitzerName;
  }

  public int getIdPilot() {
    return idPilot;
  }

  public void setIdPilot(int idPilot) {
    this.idPilot = idPilot;
  }

  public Integer getWaren() {
    return waren;
  }

  public void setWaren(Integer waren) {
    this.waren = waren;
  }

  public String getFlughafenName() {
    return flughafenName;
  }

  public void setFlughafenName(String flughafenName) {
    this.flughafenName = flughafenName;
  }

  public String getLogoURL() {
    return logoURL;
  }

  public void setLogoURL(String logoURL) {
    this.logoURL = logoURL;
  }

  public int getIdFluggesellschaft() {
    return idFluggesellschaft;
  }

  public void setIdFluggesellschaft(int idFluggesellschaft) {
    this.idFluggesellschaft = idFluggesellschaft;
  }

}
