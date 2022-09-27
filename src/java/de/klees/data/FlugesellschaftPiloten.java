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
@Table(name = "flugesellschaft_piloten")
@XmlRootElement
@NamedQueries({

  @NamedQuery(name = "FlugesellschaftPiloten.findByCallnameOrderBy", query = "SELECT f FROM FlugesellschaftPiloten f WHERE f.idflugesellschaft = :idflugesellschaft and f.callname LIKE :callname order by f.callname"),
  @NamedQuery(name = "FlugesellschaftPiloten.findByIduserAndIDFluggesellschaft", query = "SELECT f FROM FlugesellschaftPiloten f WHERE f.iduser = :iduser and f.idflugesellschaft = :idflugesellschaft"),  
  @NamedQuery(name = "FlugesellschaftPiloten.findAll", query = "SELECT f FROM FlugesellschaftPiloten f"),
  @NamedQuery(name = "FlugesellschaftPiloten.findByIdflugesellschaftPiloten", query = "SELECT f FROM FlugesellschaftPiloten f WHERE f.idflugesellschaftPiloten = :idflugesellschaftPiloten"),
  @NamedQuery(name = "FlugesellschaftPiloten.findByIduser", query = "SELECT f FROM FlugesellschaftPiloten f WHERE f.iduser = :iduser"),
  @NamedQuery(name = "FlugesellschaftPiloten.findByCallname", query = "SELECT f FROM FlugesellschaftPiloten f WHERE f.callname = :callname"),
  @NamedQuery(name = "FlugesellschaftPiloten.findByEmail", query = "SELECT f FROM FlugesellschaftPiloten f WHERE f.email = :email"),
  @NamedQuery(name = "FlugesellschaftPiloten.findByBonus", query = "SELECT f FROM FlugesellschaftPiloten f WHERE f.bonus = :bonus"),
  @NamedQuery(name = "FlugesellschaftPiloten.findByStatus", query = "SELECT f FROM FlugesellschaftPiloten f WHERE f.status = :status"),
  @NamedQuery(name = "FlugesellschaftPiloten.findByKilometer", query = "SELECT f FROM FlugesellschaftPiloten f WHERE f.kilometer = :kilometer"),
  @NamedQuery(name = "FlugesellschaftPiloten.findByZeit", query = "SELECT f FROM FlugesellschaftPiloten f WHERE f.zeit = :zeit"),
  @NamedQuery(name = "FlugesellschaftPiloten.findByPassagiere", query = "SELECT f FROM FlugesellschaftPiloten f WHERE f.passagiere = :passagiere"),
  @NamedQuery(name = "FlugesellschaftPiloten.findByWaren", query = "SELECT f FROM FlugesellschaftPiloten f WHERE f.waren = :waren"),
  @NamedQuery(name = "FlugesellschaftPiloten.findByUmsatz", query = "SELECT f FROM FlugesellschaftPiloten f WHERE f.umsatz = :umsatz")})
public class FlugesellschaftPiloten implements Serializable {

  @Column(name = "darfkonvertieren")
  private Boolean darfkonvertieren;

  @Size(max = 80)
  @Column(name = "flusi")
  private String flusi;

  @Column(name = "kostenstelle")
  private Integer kostenstelle;

  @Column(name = "idflugesellschaft")
  private Integer idflugesellschaft;

  @Column(name = "letzterFlug")
  @Temporal(TemporalType.TIMESTAMP)
  private Date letzterFlug;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idflugesellschaft_piloten")
  private Integer idflugesellschaftPiloten;
  @Column(name = "iduser")
  private Integer iduser;
  @Size(max = 250)
  @Column(name = "callname")
  private String callname;
  // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
  @Size(max = 250)
  @Column(name = "email")
  private String email;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "bonus")
  private Double bonus;
  @Column(name = "status")
  private Integer status;
  @Column(name = "kilometer")
  private Integer kilometer;
  @Column(name = "zeit")
  private Integer zeit;
  @Column(name = "passagiere")
  private Integer passagiere;
  @Column(name = "waren")
  private Integer waren;
  @Column(name = "umsatz")
  private Double umsatz;

  public FlugesellschaftPiloten() {
  }

  public FlugesellschaftPiloten(Integer idflugesellschaftPiloten) {
    this.idflugesellschaftPiloten = idflugesellschaftPiloten;
  }

  public Integer getIdflugesellschaftPiloten() {
    return idflugesellschaftPiloten;
  }

  public void setIdflugesellschaftPiloten(Integer idflugesellschaftPiloten) {
    this.idflugesellschaftPiloten = idflugesellschaftPiloten;
  }

  public Integer getIduser() {
    return iduser;
  }

  public void setIduser(Integer iduser) {
    this.iduser = iduser;
  }

  public String getCallname() {
    return callname;
  }

  public void setCallname(String callname) {
    this.callname = callname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Double getBonus() {
    return bonus;
  }

  public void setBonus(Double bonus) {
    this.bonus = bonus;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getKilometer() {
    return kilometer;
  }

  public void setKilometer(Integer kilometer) {
    this.kilometer = kilometer;
  }

  public Integer getZeit() {
    return zeit;
  }

  public void setZeit(Integer zeit) {
    this.zeit = zeit;
  }

  public Integer getPassagiere() {
    return passagiere;
  }

  public void setPassagiere(Integer passagiere) {
    this.passagiere = passagiere;
  }

  public Integer getWaren() {
    return waren;
  }

  public void setWaren(Integer waren) {
    this.waren = waren;
  }

  public Double getUmsatz() {
    return umsatz;
  }

  public void setUmsatz(Double umsatz) {
    this.umsatz = umsatz;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idflugesellschaftPiloten != null ? idflugesellschaftPiloten.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof FlugesellschaftPiloten)) {
      return false;
    }
    FlugesellschaftPiloten other = (FlugesellschaftPiloten) object;
    if ((this.idflugesellschaftPiloten == null && other.idflugesellschaftPiloten != null) || (this.idflugesellschaftPiloten != null && !this.idflugesellschaftPiloten.equals(other.idflugesellschaftPiloten))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.FlugesellschaftPiloten[ idflugesellschaftPiloten=" + idflugesellschaftPiloten + " ]";
  }

  public Date getLetzterFlug() {
    return letzterFlug;
  }

  public void setLetzterFlug(Date letzterFlug) {
    this.letzterFlug = letzterFlug;
  }

  public Integer getIdflugesellschaft() {
    return idflugesellschaft;
  }

  public void setIdflugesellschaft(Integer idflugesellschaft) {
    this.idflugesellschaft = idflugesellschaft;
  }

  public Integer getKostenstelle() {
    return kostenstelle;
  }

  public void setKostenstelle(Integer kostenstelle) {
    this.kostenstelle = kostenstelle;
  }

  public String getFlusi() {
    return flusi;
  }

  public void setFlusi(String flusi) {
    this.flusi = flusi;
  }

  public Boolean getDarfkonvertieren() {
    return darfkonvertieren;
  }

  public void setDarfkonvertieren(Boolean darfkonvertieren) {
    this.darfkonvertieren = darfkonvertieren;
  }
  
}
