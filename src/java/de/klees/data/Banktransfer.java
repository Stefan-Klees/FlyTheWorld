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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Stefan Klees
 */
@Entity
@Table(name = "banktransfer")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Banktransfer.findAll", query = "SELECT b FROM Banktransfer b")})
public class Banktransfer implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "primanota")
  private Integer primanota;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 80)
  @Column(name = "bankKonto")
  private String bankKonto;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 250)
  @Column(name = "kontoName")
  private String kontoName;
  @Column(name = "userID")
  private Integer userID;
  @Column(name = "empfaengerUserID")
  private Integer empfaengerUserID;
  @Size(max = 80)
  @Column(name = "absenderKonto")
  private String absenderKonto;
  @Size(max = 250)
  @Column(name = "absenderName")
  private String absenderName;
  @Size(max = 80)
  @Column(name = "empfaengerKonto")
  private String empfaengerKonto;
  @Size(max = 250)
  @Column(name = "empfaengerName")
  private String empfaengerName;
  @Column(name = "ausfuehrungsDatum")
  @Temporal(TemporalType.TIMESTAMP)
  private Date ausfuehrungsDatum;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "betrag")
  private Double betrag;
  @Lob
  @Size(max = 16777215)
  @Column(name = "verwendungsZweck")
  private String verwendungsZweck;

  public Banktransfer() {
  }

  public Banktransfer(Integer primanota) {
    this.primanota = primanota;
  }

  public Banktransfer(Integer primanota, String bankKonto, String kontoName) {
    this.primanota = primanota;
    this.bankKonto = bankKonto;
    this.kontoName = kontoName;
  }

  public Integer getPrimanota() {
    return primanota;
  }

  public void setPrimanota(Integer primanota) {
    this.primanota = primanota;
  }

  public String getBankKonto() {
    return bankKonto;
  }

  public void setBankKonto(String bankKonto) {
    this.bankKonto = bankKonto;
  }

  public String getKontoName() {
    return kontoName;
  }

  public void setKontoName(String kontoName) {
    this.kontoName = kontoName;
  }

  public Integer getUserID() {
    return userID;
  }

  public void setUserID(Integer userID) {
    this.userID = userID;
  }

  public Integer getEmpfaengerUserID() {
    return empfaengerUserID;
  }

  public void setEmpfaengerUserID(Integer empfaengerUserID) {
    this.empfaengerUserID = empfaengerUserID;
  }

  public String getAbsenderKonto() {
    return absenderKonto;
  }

  public void setAbsenderKonto(String absenderKonto) {
    this.absenderKonto = absenderKonto;
  }

  public String getAbsenderName() {
    return absenderName;
  }

  public void setAbsenderName(String absenderName) {
    this.absenderName = absenderName;
  }

  public String getEmpfaengerKonto() {
    return empfaengerKonto;
  }

  public void setEmpfaengerKonto(String empfaengerKonto) {
    this.empfaengerKonto = empfaengerKonto;
  }

  public String getEmpfaengerName() {
    return empfaengerName;
  }

  public void setEmpfaengerName(String empfaengerName) {
    this.empfaengerName = empfaengerName;
  }

  public Date getAusfuehrungsDatum() {
    return ausfuehrungsDatum;
  }

  public void setAusfuehrungsDatum(Date ausfuehrungsDatum) {
    this.ausfuehrungsDatum = ausfuehrungsDatum;
  }

  public Double getBetrag() {
    return betrag;
  }

  public void setBetrag(Double betrag) {
    this.betrag = betrag;
  }

  public String getVerwendungsZweck() {
    return verwendungsZweck;
  }

  public void setVerwendungsZweck(String verwendungsZweck) {
    this.verwendungsZweck = verwendungsZweck;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (primanota != null ? primanota.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Banktransfer)) {
      return false;
    }
    Banktransfer other = (Banktransfer) object;
    if ((this.primanota == null && other.primanota != null) || (this.primanota != null && !this.primanota.equals(other.primanota))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Banktransfer[ primanota=" + primanota + " ]";
  }
  
}
