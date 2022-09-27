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
@Table(name = "flugzeugboerse")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Flugzeugboerse.findAll", query = "SELECT f FROM Flugzeugboerse f")})
public class Flugzeugboerse implements Serializable {

  @Size(max = 250)
  @Column(name = "flugzeugtyp")
  private String flugzeugtyp;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idflugzeugboerse")
  private Integer idflugzeugboerse;
  @Column(name = "iduser")
  private Integer iduser;
  @Basic(optional = false)
  @NotNull
  @Column(name = "idmietkauf")
  private int idmietkauf;
  @Column(name = "freigabe")
  private Boolean freigabe;
  @Lob
  @Size(max = 65535)
  @Column(name = "text")
  private String text;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "verkaufspreis")
  private Double verkaufspreis;
  @Column(name = "leasingpreis")
  private Double leasingpreis;
  @Column(name = "finanzierungspreis")
  private Double finanzierungspreis;
  @Column(name = "ablaufam")
  @Temporal(TemporalType.TIMESTAMP)
  private Date ablaufam;
  @Column(name = "leasing")
  private Boolean leasing;
  @Column(name = "verkauf")
  private Boolean verkauf;
  @Column(name = "finanzierung")
  private Boolean finanzierung;
  @Column(name = "gebuehrenbezahlt")
  private Boolean gebuehrenbezahlt;

  public Flugzeugboerse() {
  }

  public Flugzeugboerse(Integer idflugzeugboerse) {
    this.idflugzeugboerse = idflugzeugboerse;
  }

  public Flugzeugboerse(Integer idflugzeugboerse, int idmietkauf) {
    this.idflugzeugboerse = idflugzeugboerse;
    this.idmietkauf = idmietkauf;
  }

  public Integer getIdflugzeugboerse() {
    return idflugzeugboerse;
  }

  public void setIdflugzeugboerse(Integer idflugzeugboerse) {
    this.idflugzeugboerse = idflugzeugboerse;
  }

  public Integer getIduser() {
    return iduser;
  }

  public void setIduser(Integer iduser) {
    this.iduser = iduser;
  }

  public int getIdmietkauf() {
    return idmietkauf;
  }

  public void setIdmietkauf(int idmietkauf) {
    this.idmietkauf = idmietkauf;
  }

  public Boolean getFreigabe() {
    return freigabe;
  }

  public void setFreigabe(Boolean freigabe) {
    this.freigabe = freigabe;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Double getVerkaufspreis() {
    return verkaufspreis;
  }

  public void setVerkaufspreis(Double verkaufspreis) {
    this.verkaufspreis = verkaufspreis;
  }

  public Double getLeasingpreis() {
    return leasingpreis;
  }

  public void setLeasingpreis(Double leasingpreis) {
    this.leasingpreis = leasingpreis;
  }

  public Double getFinanzierungspreis() {
    return finanzierungspreis;
  }

  public void setFinanzierungspreis(Double finanzierungspreis) {
    this.finanzierungspreis = finanzierungspreis;
  }

  public Date getAblaufam() {
    return ablaufam;
  }

  public void setAblaufam(Date ablaufam) {
    this.ablaufam = ablaufam;
  }

  public Boolean getLeasing() {
    return leasing;
  }

  public void setLeasing(Boolean leasing) {
    this.leasing = leasing;
  }

  public Boolean getVerkauf() {
    return verkauf;
  }

  public void setVerkauf(Boolean verkauf) {
    this.verkauf = verkauf;
  }

  public Boolean getFinanzierung() {
    return finanzierung;
  }

  public void setFinanzierung(Boolean finanzierung) {
    this.finanzierung = finanzierung;
  }

  public Boolean getGebuehrenbezahlt() {
    return gebuehrenbezahlt;
  }

  public void setGebuehrenbezahlt(Boolean gebuehrenbezahlt) {
    this.gebuehrenbezahlt = gebuehrenbezahlt;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idflugzeugboerse != null ? idflugzeugboerse.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Flugzeugboerse)) {
      return false;
    }
    Flugzeugboerse other = (Flugzeugboerse) object;
    if ((this.idflugzeugboerse == null && other.idflugzeugboerse != null) || (this.idflugzeugboerse != null && !this.idflugzeugboerse.equals(other.idflugzeugboerse))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Flugzeugboerse[ idflugzeugboerse=" + idflugzeugboerse + " ]";
  }

  public String getFlugzeugtyp() {
    return flugzeugtyp;
  }

  public void setFlugzeugtyp(String flugzeugtyp) {
    this.flugzeugtyp = flugzeugtyp;
  }
  
}
