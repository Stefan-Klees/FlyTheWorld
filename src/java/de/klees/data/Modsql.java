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
@Table(name = "modsql")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Modsql.findAll", query = "SELECT m FROM Modsql m")})
public class Modsql implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idmodsql")
  private Integer idmodsql;
  @Size(max = 250)
  @Column(name = "Bezeichnung")
  private String bezeichnung;
  @Lob
  @Size(max = 65535)
  @Column(name = "modsqlbefehl")
  private String modsqlbefehl;

  public Modsql() {
  }

  public Modsql(Integer idmodsql) {
    this.idmodsql = idmodsql;
  }

  public Integer getIdmodsql() {
    return idmodsql;
  }

  public void setIdmodsql(Integer idmodsql) {
    this.idmodsql = idmodsql;
  }

  public String getBezeichnung() {
    return bezeichnung;
  }

  public void setBezeichnung(String bezeichnung) {
    this.bezeichnung = bezeichnung;
  }

  public String getModsqlbefehl() {
    return modsqlbefehl;
  }

  public void setModsqlbefehl(String modsqlbefehl) {
    this.modsqlbefehl = modsqlbefehl;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idmodsql != null ? idmodsql.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Modsql)) {
      return false;
    }
    Modsql other = (Modsql) object;
    if ((this.idmodsql == null && other.idmodsql != null) || (this.idmodsql != null && !this.idmodsql.equals(other.idmodsql))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Modsql[ idmodsql=" + idmodsql + " ]";
  }
  
}
