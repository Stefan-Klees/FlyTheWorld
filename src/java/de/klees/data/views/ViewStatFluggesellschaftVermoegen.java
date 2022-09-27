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
@Table(name = "viewStatFluggesellschaftVermoegen")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ViewStatFluggesellschaftVermoegen.findAll", query = "SELECT v FROM ViewStatFluggesellschaftVermoegen v")})
public class ViewStatFluggesellschaftVermoegen implements Serializable {

  @Column(name = "userid")
  private Integer userid;

  private static final long serialVersionUID = 1L;
  @Basic(optional = false)
  @NotNull
  @Column(name = "idFluggesellschaft")
  @Id
  private int idFluggesellschaft;
  @Size(max = 250)
  @Column(name = "nameFluggesellschaft")
  private String nameFluggesellschaft;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "betrag")
  private Double betrag;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 80)
  @Column(name = "nameUser")
  private String nameUser;

  public ViewStatFluggesellschaftVermoegen() {
  }

  public int getIdFluggesellschaft() {
    return idFluggesellschaft;
  }

  public void setIdFluggesellschaft(int idFluggesellschaft) {
    this.idFluggesellschaft = idFluggesellschaft;
  }

  public String getNameFluggesellschaft() {
    return nameFluggesellschaft;
  }

  public void setNameFluggesellschaft(String nameFluggesellschaft) {
    this.nameFluggesellschaft = nameFluggesellschaft;
  }

  public Double getBetrag() {
    return betrag;
  }

  public void setBetrag(Double betrag) {
    this.betrag = betrag;
  }

  public String getNameUser() {
    return nameUser;
  }

  public void setNameUser(String nameUser) {
    this.nameUser = nameUser;
  }

  public Integer getUserid() {
    return userid;
  }

  public void setUserid(Integer userid) {
    this.userid = userid;
  }
  
}
