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
@Table(name = "Flugzeug_symbole")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Flugzeugsymbole.findAll", query = "SELECT f FROM Flugzeugsymbole f")
  , @NamedQuery(name = "Flugzeugsymbole.findByIdFlugzeugsymbole", query = "SELECT f FROM Flugzeugsymbole f WHERE f.idFlugzeugsymbole = :idFlugzeugsymbole")
  , @NamedQuery(name = "Flugzeugsymbole.findByUrlFlugzeugsymbol", query = "SELECT f FROM Flugzeugsymbole f WHERE f.urlFlugzeugsymbol = :urlFlugzeugsymbol")
  , @NamedQuery(name = "Flugzeugsymbole.findByFlugzeugModell", query = "SELECT f FROM Flugzeugsymbole f WHERE f.flugzeugModell = :flugzeugModell")})
public class Flugzeugsymbole implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idFlugzeugsymbole")
  private Integer idFlugzeugsymbole;
  @Size(max = 250)
  @Column(name = "urlFlugzeugsymbol")
  private String urlFlugzeugsymbol;
  @Size(max = 250)
  @Column(name = "flugzeugModell")
  private String flugzeugModell;

  public Flugzeugsymbole() {
  }

  public Flugzeugsymbole(Integer idFlugzeugsymbole) {
    this.idFlugzeugsymbole = idFlugzeugsymbole;
  }

  public Integer getIdFlugzeugsymbole() {
    return idFlugzeugsymbole;
  }

  public void setIdFlugzeugsymbole(Integer idFlugzeugsymbole) {
    this.idFlugzeugsymbole = idFlugzeugsymbole;
  }

  public String getUrlFlugzeugsymbol() {
    return urlFlugzeugsymbol;
  }

  public void setUrlFlugzeugsymbol(String urlFlugzeugsymbol) {
    this.urlFlugzeugsymbol = urlFlugzeugsymbol;
  }

  public String getFlugzeugModell() {
    return flugzeugModell;
  }

  public void setFlugzeugModell(String flugzeugModell) {
    this.flugzeugModell = flugzeugModell;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idFlugzeugsymbole != null ? idFlugzeugsymbole.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Flugzeugsymbole)) {
      return false;
    }
    Flugzeugsymbole other = (Flugzeugsymbole) object;
    if ((this.idFlugzeugsymbole == null && other.idFlugzeugsymbole != null) || (this.idFlugzeugsymbole != null && !this.idFlugzeugsymbole.equals(other.idFlugzeugsymbole))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Flugzeugsymbole[ idFlugzeugsymbole=" + idFlugzeugsymbole + " ]";
  }
  
}
