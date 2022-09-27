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
@Table(name = "hangarbelegung")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Hangarbelegung.findAll", query = "SELECT h FROM Hangarbelegung h")})
public class Hangarbelegung implements Serializable {

  @Column(name = "idServicePaket")
  private Integer idServicePaket;

  @Size(max = 45)
  @Column(name = "kennung")
  private String kennung;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idhangarbelegung")
  private Integer idhangarbelegung;
  @Column(name = "iduserfboid")
  private Integer iduserfboid;
  @Column(name = "quadratmeter")
  private Integer quadratmeter;
  @Size(max = 250)
  @Column(name = "flugzeugtyp")
  private String flugzeugtyp;
  @Column(name = "serviceart")
  private Integer serviceart;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "wert")
  private Double wert;
  @Column(name = "dauerminuten")
  private Integer dauerminuten;
  @Column(name = "ablaufzeit")
  @Temporal(TemporalType.TIMESTAMP)
  private Date ablaufzeit;

  public Hangarbelegung() {
  }

  public Hangarbelegung(Integer idhangarbelegung) {
    this.idhangarbelegung = idhangarbelegung;
  }

  public Integer getIdhangarbelegung() {
    return idhangarbelegung;
  }

  public void setIdhangarbelegung(Integer idhangarbelegung) {
    this.idhangarbelegung = idhangarbelegung;
  }

  public Integer getIduserfboid() {
    return iduserfboid;
  }

  public void setIduserfboid(Integer iduserfboid) {
    this.iduserfboid = iduserfboid;
  }

  public Integer getQuadratmeter() {
    return quadratmeter;
  }

  public void setQuadratmeter(Integer quadratmeter) {
    this.quadratmeter = quadratmeter;
  }

  public String getFlugzeugtyp() {
    return flugzeugtyp;
  }

  public void setFlugzeugtyp(String flugzeugtyp) {
    this.flugzeugtyp = flugzeugtyp;
  }

  public Integer getServiceart() {
    return serviceart;
  }

  public void setServiceart(Integer serviceart) {
    this.serviceart = serviceart;
  }

  public Double getWert() {
    return wert;
  }

  public void setWert(Double wert) {
    this.wert = wert;
  }

  public Integer getDauerminuten() {
    return dauerminuten;
  }

  public void setDauerminuten(Integer dauerminuten) {
    this.dauerminuten = dauerminuten;
  }

  public Date getAblaufzeit() {
    return ablaufzeit;
  }

  public void setAblaufzeit(Date ablaufzeit) {
    this.ablaufzeit = ablaufzeit;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idhangarbelegung != null ? idhangarbelegung.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Hangarbelegung)) {
      return false;
    }
    Hangarbelegung other = (Hangarbelegung) object;
    if ((this.idhangarbelegung == null && other.idhangarbelegung != null) || (this.idhangarbelegung != null && !this.idhangarbelegung.equals(other.idhangarbelegung))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Hangarbelegung[ idhangarbelegung=" + idhangarbelegung + " ]";
  }

  public String getKennung() {
    return kennung;
  }

  public void setKennung(String kennung) {
    this.kennung = kennung;
  }

  public Integer getIdServicePaket() {
    return idServicePaket;
  }

  public void setIdServicePaket(Integer idServicePaket) {
    this.idServicePaket = idServicePaket;
  }
  
}
