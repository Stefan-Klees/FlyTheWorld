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
@Table(name = "flugzeugboersetemplates")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Flugzeugboersetemplates.findAll", query = "SELECT f FROM Flugzeugboersetemplates f")})
public class Flugzeugboersetemplates implements Serializable {

  @Column(name = "finanzierung")
  private Boolean finanzierung;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idflugzeugboersetemplates")
  private Integer idflugzeugboersetemplates;
  @Column(name = "iduser")
  private Integer iduser;
  @Size(max = 250)
  @Column(name = "bezeichnung")
  private String bezeichnung;
  @Lob
  @Size(max = 65535)
  @Column(name = "beschreibung")
  private String beschreibung;
  @Column(name = "tausch")
  private Boolean tausch;
  @Column(name = "verkauf")
  private Boolean verkauf;
  @Column(name = "leasing")
  private Boolean leasing;

  public Flugzeugboersetemplates() {
  }

  public Flugzeugboersetemplates(Integer idflugzeugboersetemplates) {
    this.idflugzeugboersetemplates = idflugzeugboersetemplates;
  }

  public Integer getIdflugzeugboersetemplates() {
    return idflugzeugboersetemplates;
  }

  public void setIdflugzeugboersetemplates(Integer idflugzeugboersetemplates) {
    this.idflugzeugboersetemplates = idflugzeugboersetemplates;
  }

  public Integer getIduser() {
    return iduser;
  }

  public void setIduser(Integer iduser) {
    this.iduser = iduser;
  }

  public String getBezeichnung() {
    return bezeichnung;
  }

  public void setBezeichnung(String bezeichnung) {
    this.bezeichnung = bezeichnung;
  }

  public String getBeschreibung() {
    return beschreibung;
  }

  public void setBeschreibung(String beschreibung) {
    this.beschreibung = beschreibung;
  }

  public Boolean getTausch() {
    return tausch;
  }

  public void setTausch(Boolean tausch) {
    this.tausch = tausch;
  }

  public Boolean getVerkauf() {
    return verkauf;
  }

  public void setVerkauf(Boolean verkauf) {
    this.verkauf = verkauf;
  }

  public Boolean getLeasing() {
    return leasing;
  }

  public void setLeasing(Boolean leasing) {
    this.leasing = leasing;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idflugzeugboersetemplates != null ? idflugzeugboersetemplates.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Flugzeugboersetemplates)) {
      return false;
    }
    Flugzeugboersetemplates other = (Flugzeugboersetemplates) object;
    if ((this.idflugzeugboersetemplates == null && other.idflugzeugboersetemplates != null) || (this.idflugzeugboersetemplates != null && !this.idflugzeugboersetemplates.equals(other.idflugzeugboersetemplates))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Flugzeugboersetemplates[ idflugzeugboersetemplates=" + idflugzeugboersetemplates + " ]";
  }

  public Boolean getFinanzierung() {
    return finanzierung;
  }

  public void setFinanzierung(Boolean finanzierung) {
    this.finanzierung = finanzierung;
  }
  
}
