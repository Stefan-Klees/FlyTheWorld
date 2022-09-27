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
@Table(name = "flugzeugtransfer")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Flugzeugtransfer.findAll", query = "SELECT f FROM Flugzeugtransfer f")})
public class Flugzeugtransfer implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idflugzeugtransfer")
  private Integer idflugzeugtransfer;
  @Column(name = "idFlugzeugMietKauf")
  private Integer idFlugzeugMietKauf;
  @Column(name = "beginndatum")
  @Temporal(TemporalType.TIMESTAMP)
  private Date beginndatum;
  @Column(name = "enddatum")
  @Temporal(TemporalType.TIMESTAMP)
  private Date enddatum;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "betrag")
  private Double betrag;
  @Column(name = "entfernung")
  private Integer entfernung;
  @Size(max = 15)
  @Column(name = "zielicao")
  private String zielicao;
  @Column(name = "iduser")
  private Integer iduser;
  @Size(max = 250)
  @Column(name = "bankkonto")
  private String bankkonto;
  @Size(max = 250)
  @Column(name = "kontoname")
  private String kontoname;

  public Flugzeugtransfer() {
  }

  public Flugzeugtransfer(Integer idflugzeugtransfer) {
    this.idflugzeugtransfer = idflugzeugtransfer;
  }

  public Integer getIdflugzeugtransfer() {
    return idflugzeugtransfer;
  }

  public void setIdflugzeugtransfer(Integer idflugzeugtransfer) {
    this.idflugzeugtransfer = idflugzeugtransfer;
  }

  public Integer getIdFlugzeugMietKauf() {
    return idFlugzeugMietKauf;
  }

  public void setIdFlugzeugMietKauf(Integer idFlugzeugMietKauf) {
    this.idFlugzeugMietKauf = idFlugzeugMietKauf;
  }

  public Date getBeginndatum() {
    return beginndatum;
  }

  public void setBeginndatum(Date beginndatum) {
    this.beginndatum = beginndatum;
  }

  public Date getEnddatum() {
    return enddatum;
  }

  public void setEnddatum(Date enddatum) {
    this.enddatum = enddatum;
  }

  public Double getBetrag() {
    return betrag;
  }

  public void setBetrag(Double betrag) {
    this.betrag = betrag;
  }

  public Integer getEntfernung() {
    return entfernung;
  }

  public void setEntfernung(Integer entfernung) {
    this.entfernung = entfernung;
  }

  public String getZielicao() {
    return zielicao;
  }

  public void setZielicao(String zielicao) {
    this.zielicao = zielicao;
  }

  public Integer getIduser() {
    return iduser;
  }

  public void setIduser(Integer iduser) {
    this.iduser = iduser;
  }

  public String getBankkonto() {
    return bankkonto;
  }

  public void setBankkonto(String bankkonto) {
    this.bankkonto = bankkonto;
  }

  public String getKontoname() {
    return kontoname;
  }

  public void setKontoname(String kontoname) {
    this.kontoname = kontoname;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idflugzeugtransfer != null ? idflugzeugtransfer.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Flugzeugtransfer)) {
      return false;
    }
    Flugzeugtransfer other = (Flugzeugtransfer) object;
    if ((this.idflugzeugtransfer == null && other.idflugzeugtransfer != null) || (this.idflugzeugtransfer != null && !this.idflugzeugtransfer.equals(other.idflugzeugtransfer))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Flugzeugtransfer[ idflugzeugtransfer=" + idflugzeugtransfer + " ]";
  }
  
}
