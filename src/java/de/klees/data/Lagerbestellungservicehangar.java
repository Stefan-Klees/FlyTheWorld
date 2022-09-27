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
@Table(name = "lagerbestellungservicehangar")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Lagerbestellungservicehangar.findAll", query = "SELECT l FROM Lagerbestellungservicehangar l")})
public class Lagerbestellungservicehangar implements Serializable {

  @Column(name = "direktverkauf")
  private Boolean direktverkauf;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idlagerbestellungservicehangar")
  private Integer idlagerbestellungservicehangar;
  @Column(name = "idfbouserobjekt")
  private Integer idfbouserobjekt;
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
  @Column(name = "lieferkosten")
  private Double lieferkosten;
  @Column(name = "menge")
  private Double menge;
  @Column(name = "zustellungam")
  @Temporal(TemporalType.TIMESTAMP)
  private Date zustellungam;

  public Lagerbestellungservicehangar() {
  }

  public Lagerbestellungservicehangar(Integer idlagerbestellungservicehangar) {
    this.idlagerbestellungservicehangar = idlagerbestellungservicehangar;
  }

  public Integer getIdlagerbestellungservicehangar() {
    return idlagerbestellungservicehangar;
  }

  public void setIdlagerbestellungservicehangar(Integer idlagerbestellungservicehangar) {
    this.idlagerbestellungservicehangar = idlagerbestellungservicehangar;
  }

  public Integer getIdfbouserobjekt() {
    return idfbouserobjekt;
  }

  public void setIdfbouserobjekt(Integer idfbouserobjekt) {
    this.idfbouserobjekt = idfbouserobjekt;
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

  public Double getLieferkosten() {
    return lieferkosten;
  }

  public void setLieferkosten(Double lieferkosten) {
    this.lieferkosten = lieferkosten;
  }

  public Double getMenge() {
    return menge;
  }

  public void setMenge(Double menge) {
    this.menge = menge;
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
    hash += (idlagerbestellungservicehangar != null ? idlagerbestellungservicehangar.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Lagerbestellungservicehangar)) {
      return false;
    }
    Lagerbestellungservicehangar other = (Lagerbestellungservicehangar) object;
    if ((this.idlagerbestellungservicehangar == null && other.idlagerbestellungservicehangar != null) || (this.idlagerbestellungservicehangar != null && !this.idlagerbestellungservicehangar.equals(other.idlagerbestellungservicehangar))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Lagerbestellungservicehangar[ idlagerbestellungservicehangar=" + idlagerbestellungservicehangar + " ]";
  }

  public Boolean getDirektverkauf() {
    return direktverkauf;
  }

  public void setDirektverkauf(Boolean direktverkauf) {
    this.direktverkauf = direktverkauf;
  }
  
}
