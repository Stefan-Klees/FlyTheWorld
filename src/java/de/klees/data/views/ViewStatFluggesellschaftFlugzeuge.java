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

package de.klees.data.views;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Stefan Klees
 */
@Entity
@Table(name = "viewStatFluggesellschaftFlugzeuge")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ViewStatFluggesellschaftFlugzeuge.findAll", query = "SELECT v FROM ViewStatFluggesellschaftFlugzeuge v")})
public class ViewStatFluggesellschaftFlugzeuge implements Serializable {

  private static final long serialVersionUID = 1L;
  @Size(max = 250)
  @Column(name = "name")
  @Id
  private String name;
  @Basic(optional = false)
  @NotNull
  @Column(name = "Anzahl")
  private long anzahl;

  public ViewStatFluggesellschaftFlugzeuge() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getAnzahl() {
    return anzahl;
  }

  public void setAnzahl(long anzahl) {
    this.anzahl = anzahl;
  }
  
}
