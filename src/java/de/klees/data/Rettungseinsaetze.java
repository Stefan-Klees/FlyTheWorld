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
@Table(name = "Rettungseinsaetze")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Rettungseinsaetze.findAll", query = "SELECT r FROM Rettungseinsaetze r")})
public class Rettungseinsaetze implements Serializable {

  @Size(max = 100)
  @Column(name = "bezeichnungunfall")
  private String bezeichnungunfall;

  @Size(max = 100)
  @Column(name = "bezeichnungziel")
  private String bezeichnungziel;
  @Column(name = "preisfaktor")
  private Double preisfaktor;

  @Column(name = "idRettungsstation")
  private Integer idRettungsstation;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idRettungseinsaetze")
  private Integer idRettungseinsaetze;
  @Size(max = 240)
  @Column(name = "kurzbezeichnung")
  private String kurzbezeichnung;
  @Lob
  @Size(max = 2147483647)
  @Column(name = "einsatzmeldung")
  private String einsatzmeldung;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "breitengradUnfall")
  private Double breitengradUnfall;
  @Column(name = "laengengradUnfall")
  private Double laengengradUnfall;
  @Column(name = "breitengradZiel")
  private Double breitengradZiel;
  @Column(name = "laengengradZiel")
  private Double laengengradZiel;
  @Column(name = "basisEinsatzPreis")
  private Double basisEinsatzPreis;

  public Rettungseinsaetze() {
  }

  public Rettungseinsaetze(Integer idRettungseinsaetze) {
    this.idRettungseinsaetze = idRettungseinsaetze;
  }

  public Integer getIdRettungseinsaetze() {
    return idRettungseinsaetze;
  }

  public void setIdRettungseinsaetze(Integer idRettungseinsaetze) {
    this.idRettungseinsaetze = idRettungseinsaetze;
  }

  public String getKurzbezeichnung() {
    return kurzbezeichnung;
  }

  public void setKurzbezeichnung(String kurzbezeichnung) {
    this.kurzbezeichnung = kurzbezeichnung;
  }

  public String getEinsatzmeldung() {
    return einsatzmeldung;
  }

  public void setEinsatzmeldung(String einsatzmeldung) {
    this.einsatzmeldung = einsatzmeldung;
  }

  public Double getBreitengradUnfall() {
    return breitengradUnfall;
  }

  public void setBreitengradUnfall(Double breitengradUnfall) {
    this.breitengradUnfall = breitengradUnfall;
  }

  public Double getLaengengradUnfall() {
    return laengengradUnfall;
  }

  public void setLaengengradUnfall(Double laengengradUnfall) {
    this.laengengradUnfall = laengengradUnfall;
  }

  public Double getBreitengradZiel() {
    return breitengradZiel;
  }

  public void setBreitengradZiel(Double breitengradZiel) {
    this.breitengradZiel = breitengradZiel;
  }

  public Double getLaengengradZiel() {
    return laengengradZiel;
  }

  public void setLaengengradZiel(Double laengengradZiel) {
    this.laengengradZiel = laengengradZiel;
  }

  public Double getBasisEinsatzPreis() {
    return basisEinsatzPreis;
  }

  public void setBasisEinsatzPreis(Double basisEinsatzPreis) {
    this.basisEinsatzPreis = basisEinsatzPreis;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idRettungseinsaetze != null ? idRettungseinsaetze.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Rettungseinsaetze)) {
      return false;
    }
    Rettungseinsaetze other = (Rettungseinsaetze) object;
    if ((this.idRettungseinsaetze == null && other.idRettungseinsaetze != null) || (this.idRettungseinsaetze != null && !this.idRettungseinsaetze.equals(other.idRettungseinsaetze))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Rettungseinsaetze[ idRettungseinsaetze=" + idRettungseinsaetze + " ]";
  }

  public Integer getIdRettungsstation() {
    return idRettungsstation;
  }

  public void setIdRettungsstation(Integer idRettungsstation) {
    this.idRettungsstation = idRettungsstation;
  }

  public String getBezeichnungziel() {
    return bezeichnungziel;
  }

  public void setBezeichnungziel(String bezeichnungziel) {
    this.bezeichnungziel = bezeichnungziel;
  }

  public Double getPreisfaktor() {
    return preisfaktor;
  }

  public void setPreisfaktor(Double preisfaktor) {
    this.preisfaktor = preisfaktor;
  }

  public String getBezeichnungunfall() {
    return bezeichnungunfall;
  }

  public void setBezeichnungunfall(String bezeichnungunfall) {
    this.bezeichnungunfall = bezeichnungunfall;
  }
  
}
