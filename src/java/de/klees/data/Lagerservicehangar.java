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
@Table(name = "lagerservicehangar")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Lagerservicehangar.findAll", query = "SELECT l FROM Lagerservicehangar l")})
public class Lagerservicehangar implements Serializable {

  @Column(name = "menge")
  private Double menge;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idlagerservicehangar")
  private Integer idlagerservicehangar;
  @Column(name = "idfbouserobjekt")
  private Integer idfbouserobjekt;
  @Column(name = "idfboobjekt")
  private Integer idfboobjekt;
  @Column(name = "iduser")
  private Integer iduser;
  @Column(name = "paketart")
  private Integer paketart;
  @Size(max = 250)
  @Column(name = "paketname")
  private String paketname;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "paketekpreis")
  private Double paketekpreis;
  @Column(name = "paketvkpreis")
  private Double paketvkpreis;
  @Column(name = "paketvkmax")
  private Double paketvkmax;
  @Column(name = "eingelagertam")
  @Temporal(TemporalType.TIMESTAMP)
  private Date eingelagertam;
  @Column(name = "verkauftam")
  @Temporal(TemporalType.TIMESTAMP)
  private Date verkauftam;
  @Column(name = "verkauftanuserid")
  private Integer verkauftanuserid;
  @Column(name = "bestellungoffen")
  private Boolean bestellungoffen;
  @Column(name = "zustellungam")
  @Temporal(TemporalType.TIMESTAMP)
  private Date zustellungam;

  public Lagerservicehangar() {
  }

  public Lagerservicehangar(Integer idlagerservicehangar) {
    this.idlagerservicehangar = idlagerservicehangar;
  }

  public Integer getIdlagerservicehangar() {
    return idlagerservicehangar;
  }

  public void setIdlagerservicehangar(Integer idlagerservicehangar) {
    this.idlagerservicehangar = idlagerservicehangar;
  }

  public Integer getIdfbouserobjekt() {
    return idfbouserobjekt;
  }

  public void setIdfbouserobjekt(Integer idfbouserobjekt) {
    this.idfbouserobjekt = idfbouserobjekt;
  }

  public Integer getIdfboobjekt() {
    return idfboobjekt;
  }

  public void setIdfboobjekt(Integer idfboobjekt) {
    this.idfboobjekt = idfboobjekt;
  }

  public Integer getIduser() {
    return iduser;
  }

  public void setIduser(Integer iduser) {
    this.iduser = iduser;
  }

  public Integer getPaketart() {
    return paketart;
  }

  public void setPaketart(Integer paketart) {
    this.paketart = paketart;
  }

  public String getPaketname() {
    return paketname;
  }

  public void setPaketname(String paketname) {
    this.paketname = paketname;
  }

  public Double getPaketekpreis() {
    return paketekpreis;
  }

  public void setPaketekpreis(Double paketekpreis) {
    this.paketekpreis = paketekpreis;
  }

  public Double getPaketvkpreis() {
    return paketvkpreis;
  }

  public void setPaketvkpreis(Double paketvkpreis) {
    this.paketvkpreis = paketvkpreis;
  }

  public Double getPaketvkmax() {
    return paketvkmax;
  }

  public void setPaketvkmax(Double paketvkmax) {
    this.paketvkmax = paketvkmax;
  }

  public Date getEingelagertam() {
    return eingelagertam;
  }

  public void setEingelagertam(Date eingelagertam) {
    this.eingelagertam = eingelagertam;
  }

  public Date getVerkauftam() {
    return verkauftam;
  }

  public void setVerkauftam(Date verkauftam) {
    this.verkauftam = verkauftam;
  }

  public Integer getVerkauftanuserid() {
    return verkauftanuserid;
  }

  public void setVerkauftanuserid(Integer verkauftanuserid) {
    this.verkauftanuserid = verkauftanuserid;
  }

  public Boolean getBestellungoffen() {
    return bestellungoffen;
  }

  public void setBestellungoffen(Boolean bestellungoffen) {
    this.bestellungoffen = bestellungoffen;
  }

  public Date getZustellungam() {
    return zustellungam;
  }

  public void setZustellungam(Date zustellungam) {
    this.zustellungam = zustellungam;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idlagerservicehangar != null ? idlagerservicehangar.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Lagerservicehangar)) {
      return false;
    }
    Lagerservicehangar other = (Lagerservicehangar) object;
    if ((this.idlagerservicehangar == null && other.idlagerservicehangar != null) || (this.idlagerservicehangar != null && !this.idlagerservicehangar.equals(other.idlagerservicehangar))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Lagerservicehangar[ idlagerservicehangar=" + idlagerservicehangar + " ]";
  }

  public Double getMenge() {
    return menge;
  }

  public void setMenge(Double menge) {
    this.menge = menge;
  }
  
}
