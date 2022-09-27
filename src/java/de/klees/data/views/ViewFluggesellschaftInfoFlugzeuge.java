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
@Table(name = "viewFluggesellschaftInfoFlugzeuge")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ViewFluggesellschaftInfoFlugzeuge.findAll", query = "SELECT v FROM ViewFluggesellschaftInfoFlugzeuge v")})
public class ViewFluggesellschaftInfoFlugzeuge implements Serializable {

  @Column(name = "naechsterTerminCcheck")
  @Temporal(TemporalType.DATE)
  private Date naechsterTerminCcheck;

  @Size(max = 250)
  @Column(name = "flugzeugArt")
  private String flugzeugArt;

  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "zustand")
  private Double zustand;

  @Column(name = "istInDerLuft")
  private Boolean istInDerLuft;

  @Column(name = "pfand")
  private Integer pfand;

  private static final long serialVersionUID = 1L;
  @Basic(optional = false)
  @NotNull
  @Column(name = "idFluggesellschaft")
  private int idFluggesellschaft;
  @Size(max = 250)
  @Column(name = "type")
  private String type;
  @Size(max = 45)
  @Column(name = "registrierung")
  private String registrierung;
  @Basic(optional = false)
  @NotNull
  @Column(name = "idMietKauf")
  @Id
  private int idMietKauf;
  @Size(max = 25)
  @Column(name = "aktuellePositionICAO")
  private String aktuellePositionICAO;
  @Size(max = 250)
  @Column(name = "name")
  private String name;
  @Size(max = 250)
  @Column(name = "land")
  private String land;
  @Size(max = 250)
  @Column(name = "bundesland")
  private String bundesland;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "mietpreisTrocken")
  private Double mietpreisTrocken;
  @Column(name = "istGesperrt")
  private Boolean istGesperrt;
  @Column(name = "istMietbar")
  private Boolean istMietbar;

  public ViewFluggesellschaftInfoFlugzeuge() {
  }

  public int getIdFluggesellschaft() {
    return idFluggesellschaft;
  }

  public void setIdFluggesellschaft(int idFluggesellschaft) {
    this.idFluggesellschaft = idFluggesellschaft;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getRegistrierung() {
    return registrierung;
  }

  public void setRegistrierung(String registrierung) {
    this.registrierung = registrierung;
  }

  public int getIdMietKauf() {
    return idMietKauf;
  }

  public void setIdMietKauf(int idMietKauf) {
    this.idMietKauf = idMietKauf;
  }

  public String getAktuellePositionICAO() {
    return aktuellePositionICAO;
  }

  public void setAktuellePositionICAO(String aktuellePositionICAO) {
    this.aktuellePositionICAO = aktuellePositionICAO;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLand() {
    return land;
  }

  public void setLand(String land) {
    this.land = land;
  }

  public String getBundesland() {
    return bundesland;
  }

  public void setBundesland(String bundesland) {
    this.bundesland = bundesland;
  }

  public Double getMietpreisTrocken() {
    return mietpreisTrocken;
  }

  public void setMietpreisTrocken(Double mietpreisTrocken) {
    this.mietpreisTrocken = mietpreisTrocken;
  }

  public Boolean getIstGesperrt() {
    return istGesperrt;
  }

  public void setIstGesperrt(Boolean istGesperrt) {
    this.istGesperrt = istGesperrt;
  }

  public Boolean getIstMietbar() {
    return istMietbar;
  }

  public void setIstMietbar(Boolean istMietbar) {
    this.istMietbar = istMietbar;
  }

  public Integer getPfand() {
    return pfand;
  }

  public void setPfand(Integer pfand) {
    this.pfand = pfand;
  }

  public Boolean getIstInDerLuft() {
    return istInDerLuft;
  }

  public void setIstInDerLuft(Boolean istInDerLuft) {
    this.istInDerLuft = istInDerLuft;
  }

  public Double getZustand() {
    return zustand;
  }

  public void setZustand(Double zustand) {
    this.zustand = zustand;
  }

  public String getFlugzeugArt() {
    return flugzeugArt;
  }

  public void setFlugzeugArt(String flugzeugArt) {
    this.flugzeugArt = flugzeugArt;
  }

  public Date getNaechsterTerminCcheck() {
    return naechsterTerminCcheck;
  }

  public void setNaechsterTerminCcheck(Date naechsterTerminCcheck) {
    this.naechsterTerminCcheck = naechsterTerminCcheck;
  }
  
}
