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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Stefan Klees
 */
@Entity
@Table(name = "missionen")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Missionen.findAll", query = "SELECT m FROM Missionen m")})
public class Missionen implements Serializable {

  @Column(name = "briefingkomplettanzeigen")
  private Boolean briefingkomplettanzeigen;

  @Column(name = "freigabe")
  private Boolean freigabe;

  @Column(name = "initialetankfuellung")
  private Integer initialetankfuellung;

  @Size(max = 100)
  @Column(name = "land")
  private String land;

  @Size(max = 250)
  @Column(name = "ersteller")
  private String ersteller;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idmissionen")
  private Integer idmissionen;
  @Column(name = "idflugzeug")
  private Integer idflugzeug;
  @Size(max = 500)
  @Column(name = "kurzbezeichnung")
  private String kurzbezeichnung;
  @Lob
  @Size(max = 65535)
  @Column(name = "missionstext")
  private String missionstext;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "bezahlung")
  private Double bezahlung;
  @Column(name = "faktorfuerbezahlung")
  private Double faktorfuerbezahlung;
  @Column(name = "anzahlwaypoints")
  private Integer anzahlwaypoints;

  public Missionen() {
  }

  public Missionen(Integer idmissionen) {
    this.idmissionen = idmissionen;
  }

  public Integer getIdmissionen() {
    return idmissionen;
  }

  public void setIdmissionen(Integer idmissionen) {
    this.idmissionen = idmissionen;
  }

  public Integer getIdflugzeug() {
    return idflugzeug;
  }

  public void setIdflugzeug(Integer idflugzeug) {
    this.idflugzeug = idflugzeug;
  }

  public String getKurzbezeichnung() {
    return kurzbezeichnung;
  }

  public void setKurzbezeichnung(String kurzbezeichnung) {
    this.kurzbezeichnung = kurzbezeichnung;
  }

  public String getMissionstext() {
    return missionstext;
  }

  public void setMissionstext(String missionstext) {
    this.missionstext = missionstext;
  }

  public Double getBezahlung() {
    return bezahlung;
  }

  public void setBezahlung(Double bezahlung) {
    this.bezahlung = bezahlung;
  }

  public Double getFaktorfuerbezahlung() {
    return faktorfuerbezahlung;
  }

  public void setFaktorfuerbezahlung(Double faktorfuerbezahlung) {
    this.faktorfuerbezahlung = faktorfuerbezahlung;
  }

  public Integer getAnzahlwaypoints() {
    return anzahlwaypoints;
  }

  public void setAnzahlwaypoints(Integer anzahlwaypoints) {
    this.anzahlwaypoints = anzahlwaypoints;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idmissionen != null ? idmissionen.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Missionen)) {
      return false;
    }
    Missionen other = (Missionen) object;
    if ((this.idmissionen == null && other.idmissionen != null) || (this.idmissionen != null && !this.idmissionen.equals(other.idmissionen))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Missionen[ idmissionen=" + idmissionen + " ]";
  }

  public String getErsteller() {
    return ersteller;
  }

  public void setErsteller(String ersteller) {
    this.ersteller = ersteller;
  }

  public String getLand() {
    return land;
  }

  public void setLand(String land) {
    this.land = land;
  }

  public Integer getInitialetankfuellung() {
    return initialetankfuellung;
  }

  public void setInitialetankfuellung(Integer initialetankfuellung) {
    this.initialetankfuellung = initialetankfuellung;
  }

  public Boolean getFreigabe() {
    return freigabe;
  }

  public void setFreigabe(Boolean freigabe) {
    this.freigabe = freigabe;
  }

  public Boolean getBriefingkomplettanzeigen() {
    return briefingkomplettanzeigen;
  }

  public void setBriefingkomplettanzeigen(Boolean briefingkomplettanzeigen) {
    this.briefingkomplettanzeigen = briefingkomplettanzeigen;
  }
  
}
