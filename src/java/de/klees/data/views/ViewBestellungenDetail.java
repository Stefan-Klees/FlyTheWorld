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
@Table(name = "viewBestellungenDetail")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ViewBestellungenDetail.findAll", query = "SELECT v FROM ViewBestellungenDetail v")})
public class ViewBestellungenDetail implements Serializable {

  @Size(max = 250)
  @Column(name = "bankkonto")
  private String bankkonto;

  @Column(name = "art")
  private Integer art;

  private static final long serialVersionUID = 1L;
  @Basic(optional = false)
  @NotNull
  @Column(name = "idBestellungen")
  @Id
  private int idBestellungen;
  @Column(name = "userID")
  private Integer userID;
  @Column(name = "menge")
  private Integer menge;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "bestellsumme")
  private Double bestellsumme;
  @Size(max = 45)
  @Column(name = "icao")
  private String icao;
  @Size(max = 250)
  @Column(name = "name")
  private String name;
  @Column(name = "lieferdatum")
  @Temporal(TemporalType.DATE)
  private Date lieferdatum;
  @Column(name = "bestandAVGASkg")
  private Integer bestandAVGASkg;
  @Column(name = "bestandJETAkg")
  private Integer bestandJETAkg;

  public ViewBestellungenDetail() {
  }

  public int getIdBestellungen() {
    return idBestellungen;
  }

  public void setIdBestellungen(int idBestellungen) {
    this.idBestellungen = idBestellungen;
  }

  public Integer getUserID() {
    return userID;
  }

  public void setUserID(Integer userID) {
    this.userID = userID;
  }

  public Integer getMenge() {
    return menge;
  }

  public void setMenge(Integer menge) {
    this.menge = menge;
  }

  public Double getBestellsumme() {
    return bestellsumme;
  }

  public void setBestellsumme(Double bestellsumme) {
    this.bestellsumme = bestellsumme;
  }

  public String getIcao() {
    return icao;
  }

  public void setIcao(String icao) {
    this.icao = icao;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getLieferdatum() {
    return lieferdatum;
  }

  public void setLieferdatum(Date lieferdatum) {
    this.lieferdatum = lieferdatum;
  }

  public Integer getBestandAVGASkg() {
    return bestandAVGASkg;
  }

  public void setBestandAVGASkg(Integer bestandAVGASkg) {
    this.bestandAVGASkg = bestandAVGASkg;
  }

  public Integer getBestandJETAkg() {
    return bestandJETAkg;
  }

  public void setBestandJETAkg(Integer bestandJETAkg) {
    this.bestandJETAkg = bestandJETAkg;
  }

  public Integer getArt() {
    return art;
  }

  public void setArt(Integer art) {
    this.art = art;
  }

  public String getBankkonto() {
    return bankkonto;
  }

  public void setBankkonto(String bankkonto) {
    this.bankkonto = bankkonto;
  }
  
}
