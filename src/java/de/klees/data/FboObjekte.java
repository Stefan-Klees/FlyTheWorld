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
@Table(name = "fboObjekte")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "FboObjekte.findAll", query = "SELECT f FROM FboObjekte f")
  , @NamedQuery(name = "FboObjekte.findByIdObjekt", query = "SELECT f FROM FboObjekte f WHERE f.idObjekt = :idObjekt")
  , @NamedQuery(name = "FboObjekte.findByObjektName", query = "SELECT f FROM FboObjekte f WHERE f.objektName = :objektName")
  , @NamedQuery(name = "FboObjekte.findByMietPreis", query = "SELECT f FROM FboObjekte f WHERE f.mietPreis = :mietPreis")
  , @NamedQuery(name = "FboObjekte.findByAnzahlRouten", query = "SELECT f FROM FboObjekte f WHERE f.anzahlRouten = :anzahlRouten")
  , @NamedQuery(name = "FboObjekte.findByAnzahlStellplaetze", query = "SELECT f FROM FboObjekte f WHERE f.anzahlStellplaetze = :anzahlStellplaetze")
  , @NamedQuery(name = "FboObjekte.findByAnzahlPersonal", query = "SELECT f FROM FboObjekte f WHERE f.anzahlPersonal = :anzahlPersonal")
  , @NamedQuery(name = "FboObjekte.findByTankstelleMaxFuelKG", query = "SELECT f FROM FboObjekte f WHERE f.tankstelleMaxFuelKG = :tankstelleMaxFuelKG")
  , @NamedQuery(name = "FboObjekte.findByTankstelle", query = "SELECT f FROM FboObjekte f WHERE f.tankstelle = :tankstelle")
  , @NamedQuery(name = "FboObjekte.findByFbo", query = "SELECT f FROM FboObjekte f WHERE f.fbo = :fbo")
  , @NamedQuery(name = "FboObjekte.findByServicehangar", query = "SELECT f FROM FboObjekte f WHERE f.servicehangar = :servicehangar")
  , @NamedQuery(name = "FboObjekte.findByBusinessLounge", query = "SELECT f FROM FboObjekte f WHERE f.businessLounge = :businessLounge")})
public class FboObjekte implements Serializable {

  @Column(name = "servicehangarQM")
  private Integer servicehangarQM;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "servicehangarQMPreis")
  private Double servicehangarQMPreis;

  @Column(name = "spritlager")
  private Boolean spritlager;


  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idObjekt")
  private Integer idObjekt;
  @Size(max = 250)
  @Column(name = "objektName")
  private String objektName;
  @Column(name = "mietPreis")
  private Integer mietPreis;
  @Column(name = "anzahlRouten")
  private Integer anzahlRouten;
  @Column(name = "anzahlStellplaetze")
  private Integer anzahlStellplaetze;
  @Column(name = "anzahlPersonal")
  private Integer anzahlPersonal;
  @Column(name = "tankstelleMaxFuelKG")
  private Integer tankstelleMaxFuelKG;
  @Column(name = "tankstelle")
  private Boolean tankstelle;
  @Column(name = "fbo")
  private Boolean fbo;
  @Column(name = "servicehangar")
  private Boolean servicehangar;
  @Column(name = "businessLounge")
  private Boolean businessLounge;
  @Column(name = "klasse")
  private Integer klasse;
  @Column(name = "terminalMaxPax")
  private Integer terminalMaxPax;
  @Column(name = "abfertigungsTerminal")
  private Boolean abfertigungsTerminal;

  public FboObjekte() {
  }

  public FboObjekte(Integer idObjekt) {
    this.idObjekt = idObjekt;
  }

  public Integer getIdObjekt() {
    return idObjekt;
  }

  public void setIdObjekt(Integer idObjekt) {
    this.idObjekt = idObjekt;
  }

  public String getObjektName() {
    return objektName;
  }

  public void setObjektName(String objektName) {
    this.objektName = objektName;
  }

  public Integer getMietPreis() {
    return mietPreis;
  }

  public void setMietPreis(Integer mietPreis) {
    this.mietPreis = mietPreis;
  }

  public Integer getAnzahlRouten() {
    return anzahlRouten;
  }

  public void setAnzahlRouten(Integer anzahlRouten) {
    this.anzahlRouten = anzahlRouten;
  }

  public Integer getAnzahlStellplaetze() {
    return anzahlStellplaetze;
  }

  public void setAnzahlStellplaetze(Integer anzahlStellplaetze) {
    this.anzahlStellplaetze = anzahlStellplaetze;
  }

  public Integer getAnzahlPersonal() {
    return anzahlPersonal;
  }

  public void setAnzahlPersonal(Integer anzahlPersonal) {
    this.anzahlPersonal = anzahlPersonal;
  }

  public Integer getTankstelleMaxFuelKG() {
    return tankstelleMaxFuelKG;
  }

  public void setTankstelleMaxFuelKG(Integer tankstelleMaxFuelKG) {
    this.tankstelleMaxFuelKG = tankstelleMaxFuelKG;
  }

  public Boolean getTankstelle() {
    return tankstelle;
  }

  public void setTankstelle(Boolean tankstelle) {
    this.tankstelle = tankstelle;
  }

  public Boolean getFbo() {
    return fbo;
  }

  public void setFbo(Boolean fbo) {
    this.fbo = fbo;
  }

  public Boolean getServicehangar() {
    return servicehangar;
  }

  public void setServicehangar(Boolean servicehangar) {
    this.servicehangar = servicehangar;
  }

  public Boolean getBusinessLounge() {
    return businessLounge;
  }

  public void setBusinessLounge(Boolean businessLounge) {
    this.businessLounge = businessLounge;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idObjekt != null ? idObjekt.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof FboObjekte)) {
      return false;
    }
    FboObjekte other = (FboObjekte) object;
    if ((this.idObjekt == null && other.idObjekt != null) || (this.idObjekt != null && !this.idObjekt.equals(other.idObjekt))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.FboObjekte[ idObjekt=" + idObjekt + " ]";
  }

  public Boolean getAbfertigungsTerminal() {
    return abfertigungsTerminal;
  }

  public void setAbfertigungsTerminal(Boolean abfertigungsTerminal) {
    this.abfertigungsTerminal = abfertigungsTerminal;
  }

  public Integer getTerminalMaxPax() {
    return terminalMaxPax;
  }

  public void setTerminalMaxPax(Integer terminalMaxPax) {
    this.terminalMaxPax = terminalMaxPax;
  }

  public Integer getKlasse() {
    return klasse;
  }

  public void setKlasse(Integer klasse) {
    this.klasse = klasse;
  }

  public Boolean getSpritlager() {
    return spritlager;
  }

  public void setSpritlager(Boolean spritlager) {
    this.spritlager = spritlager;
  }

  public Integer getServicehangarQM() {
    return servicehangarQM;
  }

  public void setServicehangarQM(Integer servicehangarQM) {
    this.servicehangarQM = servicehangarQM;
  }

  public Double getServicehangarQMPreis() {
    return servicehangarQMPreis;
  }

  public void setServicehangarQMPreis(Double servicehangarQMPreis) {
    this.servicehangarQMPreis = servicehangarQMPreis;
  }
  
}
