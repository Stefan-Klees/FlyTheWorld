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
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Stefan Klees
 */
@Entity
@Table(name = "ViewRESTUser")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ViewRESTUser.findAll", query = "SELECT v FROM ViewRESTUser v")
  , @NamedQuery(name = "ViewRESTUser.findByReadaccesskey", query = "SELECT v FROM ViewRESTUser v WHERE v.readaccesskey = :readaccesskey")
  , @NamedQuery(name = "ViewRESTUser.findByBenutzername", query = "SELECT v FROM ViewRESTUser v WHERE v.benutzername = :benutzername")
  , @NamedQuery(name = "ViewRESTUser.findByFlugmeilen", query = "SELECT v FROM ViewRESTUser v WHERE v.flugmeilen = :flugmeilen")
  , @NamedQuery(name = "ViewRESTUser.findByFlugzeitMinuten", query = "SELECT v FROM ViewRESTUser v WHERE v.flugzeitMinuten = :flugzeitMinuten")
  , @NamedQuery(name = "ViewRESTUser.findByFluegeGesamt", query = "SELECT v FROM ViewRESTUser v WHERE v.fluegeGesamt = :fluegeGesamt")
  , @NamedQuery(name = "ViewRESTUser.findByPassagiere", query = "SELECT v FROM ViewRESTUser v WHERE v.passagiere = :passagiere")
  , @NamedQuery(name = "ViewRESTUser.findByCargo", query = "SELECT v FROM ViewRESTUser v WHERE v.cargo = :cargo")
  , @NamedQuery(name = "ViewRESTUser.findByLetzerLogin", query = "SELECT v FROM ViewRESTUser v WHERE v.letzerLogin = :letzerLogin")
  , @NamedQuery(name = "ViewRESTUser.findByBankSaldo", query = "SELECT v FROM ViewRESTUser v WHERE v.bankSaldo = :bankSaldo")})
public class ViewRESTUser implements Serializable {

  private static final long serialVersionUID = 1L;
  @Size(max = 80)
  @Column(name = "readaccesskey")
  @Id
  private String readaccesskey;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 80)
  @Column(name = "Benutzername")
  private String benutzername;
  @Column(name = "Flugmeilen")
  private Integer flugmeilen;
  @Column(name = "FlugzeitMinuten")
  private Integer flugzeitMinuten;
  @Column(name = "FluegeGesamt")
  private Integer fluegeGesamt;
  @Column(name = "Passagiere")
  private Integer passagiere;
  @Column(name = "Cargo")
  private Integer cargo;
  @Column(name = "LetzerLogin")
  @Temporal(TemporalType.TIMESTAMP)
  private Date letzerLogin;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "BankSaldo")
  private Double bankSaldo;

  public ViewRESTUser() {
  }

  public String getReadaccesskey() {
    return readaccesskey;
  }

  public void setReadaccesskey(String readaccesskey) {
    this.readaccesskey = readaccesskey;
  }

  public String getBenutzername() {
    return benutzername;
  }

  public void setBenutzername(String benutzername) {
    this.benutzername = benutzername;
  }

  public Integer getFlugmeilen() {
    return flugmeilen;
  }

  public void setFlugmeilen(Integer flugmeilen) {
    this.flugmeilen = flugmeilen;
  }

  public Integer getFlugzeitMinuten() {
    return flugzeitMinuten;
  }

  public void setFlugzeitMinuten(Integer flugzeitMinuten) {
    this.flugzeitMinuten = flugzeitMinuten;
  }

  public Integer getFluegeGesamt() {
    return fluegeGesamt;
  }

  public void setFluegeGesamt(Integer fluegeGesamt) {
    this.fluegeGesamt = fluegeGesamt;
  }

  public Integer getPassagiere() {
    return passagiere;
  }

  public void setPassagiere(Integer passagiere) {
    this.passagiere = passagiere;
  }

  public Integer getCargo() {
    return cargo;
  }

  public void setCargo(Integer cargo) {
    this.cargo = cargo;
  }

  public Date getLetzerLogin() {
    return letzerLogin;
  }

  public void setLetzerLogin(Date letzerLogin) {
    this.letzerLogin = letzerLogin;
  }

  public Double getBankSaldo() {
    return bankSaldo;
  }

  public void setBankSaldo(Double bankSaldo) {
    this.bankSaldo = bankSaldo;
  }
  
}
