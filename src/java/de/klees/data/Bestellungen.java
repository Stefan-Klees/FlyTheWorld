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
@Table(name = "Bestellungen")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Bestellungen.findAll", query = "SELECT b FROM Bestellungen b")
  , @NamedQuery(name = "Bestellungen.findByIdBestellungen", query = "SELECT b FROM Bestellungen b WHERE b.idBestellungen = :idBestellungen")
  , @NamedQuery(name = "Bestellungen.findByObjektID", query = "SELECT b FROM Bestellungen b WHERE b.objektID = :objektID")
  , @NamedQuery(name = "Bestellungen.findByMenge", query = "SELECT b FROM Bestellungen b WHERE b.menge = :menge")
  , @NamedQuery(name = "Bestellungen.findByArt", query = "SELECT b FROM Bestellungen b WHERE b.art = :art")
  , @NamedQuery(name = "Bestellungen.findByBestellsumme", query = "SELECT b FROM Bestellungen b WHERE b.bestellsumme = :bestellsumme")
  , @NamedQuery(name = "Bestellungen.findByName", query = "SELECT b FROM Bestellungen b WHERE b.name = :name")
  , @NamedQuery(name = "Bestellungen.findByBankkonto", query = "SELECT b FROM Bestellungen b WHERE b.bankkonto = :bankkonto")
  , @NamedQuery(name = "Bestellungen.findByAusfuehrungsdatum", query = "SELECT b FROM Bestellungen b WHERE b.ausfuehrungsdatum = :ausfuehrungsdatum")
  , @NamedQuery(name = "Bestellungen.findByTankstelle", query = "SELECT b FROM Bestellungen b WHERE b.tankstelle = :tankstelle")})
public class Bestellungen implements Serializable {

  @Column(name = "userID")
  private Integer userID;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idBestellungen")
  private Integer idBestellungen;
  @Column(name = "objektID")
  private Integer objektID;
  @Column(name = "menge")
  private Integer menge;
  @Column(name = "art")
  private Integer art;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "bestellsumme")
  private Double bestellsumme;
  @Size(max = 250)
  @Column(name = "name")
  private String name;
  @Size(max = 250)
  @Column(name = "bankkonto")
  private String bankkonto;
  @Column(name = "ausfuehrungsdatum")
  @Temporal(TemporalType.DATE)
  private Date ausfuehrungsdatum;
  @Column(name = "tankstelle")
  private Boolean tankstelle;

  public Bestellungen() {
  }

  public Bestellungen(Integer idBestellungen) {
    this.idBestellungen = idBestellungen;
  }

  public Integer getIdBestellungen() {
    return idBestellungen;
  }

  public void setIdBestellungen(Integer idBestellungen) {
    this.idBestellungen = idBestellungen;
  }

  public Integer getObjektID() {
    return objektID;
  }

  public void setObjektID(Integer objektID) {
    this.objektID = objektID;
  }

  public Integer getMenge() {
    return menge;
  }

  public void setMenge(Integer menge) {
    this.menge = menge;
  }

  public Integer getArt() {
    return art;
  }

  public void setArt(Integer art) {
    this.art = art;
  }

  public Double getBestellsumme() {
    return bestellsumme;
  }

  public void setBestellsumme(Double bestellsumme) {
    this.bestellsumme = bestellsumme;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBankkonto() {
    return bankkonto;
  }

  public void setBankkonto(String bankkonto) {
    this.bankkonto = bankkonto;
  }

  public Date getAusfuehrungsdatum() {
    return ausfuehrungsdatum;
  }

  public void setAusfuehrungsdatum(Date ausfuehrungsdatum) {
    this.ausfuehrungsdatum = ausfuehrungsdatum;
  }

  public Boolean getTankstelle() {
    return tankstelle;
  }

  public void setTankstelle(Boolean tankstelle) {
    this.tankstelle = tankstelle;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idBestellungen != null ? idBestellungen.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Bestellungen)) {
      return false;
    }
    Bestellungen other = (Bestellungen) object;
    if ((this.idBestellungen == null && other.idBestellungen != null) || (this.idBestellungen != null && !this.idBestellungen.equals(other.idBestellungen))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Bestellungen[ idBestellungen=" + idBestellungen + " ]";
  }

  public Integer getUserID() {
    return userID;
  }

  public void setUserID(Integer userID) {
    this.userID = userID;
  }
  
}
