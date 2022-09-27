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

package de.klees.beans.takeoff;

import java.io.Serializable;
import java.lang.Integer;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "Abrechnungspositionen")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Abrechnungspositionen.findAll", query = "SELECT a FROM Abrechnungspositionen a"),
  @NamedQuery(name = "Abrechnungspositionen.findByIdAirline", query = "SELECT a FROM Abrechnungspositionen a WHERE a.idAirline = :idAirline"),
  @NamedQuery(name = "Abrechnungspositionen.findByEntfernung", query = "SELECT a FROM Abrechnungspositionen a WHERE a.entfernung = :entfernung"),
  @NamedQuery(name = "Abrechnungspositionen.findByIdowner", query = "SELECT a FROM Abrechnungspositionen a WHERE a.idowner = :idowner"),
  @NamedQuery(name = "Abrechnungspositionen.findByIdPilot", query = "SELECT a FROM Abrechnungspositionen a WHERE a.idPilot = :idPilot"),
  @NamedQuery(name = "Abrechnungspositionen.findByIdUser", query = "SELECT a FROM Abrechnungspositionen a WHERE a.idUser = :idUser"),
  @NamedQuery(name = "Abrechnungspositionen.findByGesamtbetrag", query = "SELECT a FROM Abrechnungspositionen a WHERE a.gesamtbetrag = :gesamtbetrag"),
  @NamedQuery(name = "Abrechnungspositionen.findByBonusOeffentlich", query = "SELECT a FROM Abrechnungspositionen a WHERE a.bonusOeffentlich = :bonusOeffentlich"),
  @NamedQuery(name = "Abrechnungspositionen.findByBonusClosed", query = "SELECT a FROM Abrechnungspositionen a WHERE a.bonusClosed = :bonusClosed"),
  @NamedQuery(name = "Abrechnungspositionen.findByPassagiere", query = "SELECT a FROM Abrechnungspositionen a WHERE a.passagiere = :passagiere"),
  @NamedQuery(name = "Abrechnungspositionen.findByAirlineBonus", query = "SELECT a FROM Abrechnungspositionen a WHERE a.airlineBonus = :airlineBonus"),
  @NamedQuery(name = "Abrechnungspositionen.findByFlugesellschaftName", query = "SELECT a FROM Abrechnungspositionen a WHERE a.flugesellschaftName = :flugesellschaftName")})
public class Abrechnungspositionen implements Serializable {

  @Column(name = "idPilot")
  private Integer idPilot;
  @Column(name = "idUser")
  private Integer idUser;
  @Column(name = "passagiere")
  private Integer passagiere;
  @Column(name = "cargo")
  private Integer cargo;

  private static final long serialVersionUID = 1L;
  @Column(name = "idAirline")
  @Id
  private Integer idAirline;
  @Column(name = "entfernung")
  private Integer entfernung;
  @Column(name = "idowner")
  private Integer idowner;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "gesamtbetrag")
  private Double gesamtbetrag;
  @Column(name = "bonusOeffentlich")
  private Double bonusOeffentlich;
  @Column(name = "bonusClosed")
  private Double bonusClosed;
  @Column(name = "airlineBonus")
  private Double airlineBonus;
  @Size(max = 250)
  @Column(name = "flugesellschaftName")
  private String flugesellschaftName;

  public Abrechnungspositionen() {
  }

  public Integer getIdAirline() {
    return idAirline;
  }

  public void setIdAirline(Integer idAirline) {
    this.idAirline = idAirline;
  }

  public Integer getEntfernung() {
    return entfernung;
  }

  public void setEntfernung(Integer entfernung) {
    this.entfernung = entfernung;
  }

  public Integer getIdowner() {
    return idowner;
  }

  public void setIdowner(Integer idowner) {
    this.idowner = idowner;
  }


  public Double getGesamtbetrag() {
    return gesamtbetrag;
  }

  public void setGesamtbetrag(Double gesamtbetrag) {
    this.gesamtbetrag = gesamtbetrag;
  }

  public Double getBonusOeffentlich() {
    return bonusOeffentlich;
  }

  public void setBonusOeffentlich(Double bonusOeffentlich) {
    this.bonusOeffentlich = bonusOeffentlich;
  }

  public Double getBonusClosed() {
    return bonusClosed;
  }

  public void setBonusClosed(Double bonusClosed) {
    this.bonusClosed = bonusClosed;
  }


  public Double getAirlineBonus() {
    return airlineBonus;
  }

  public void setAirlineBonus(Double airlineBonus) {
    this.airlineBonus = airlineBonus;
  }

  public String getFlugesellschaftName() {
    return flugesellschaftName;
  }

  public void setFlugesellschaftName(String flugesellschaftName) {
    this.flugesellschaftName = flugesellschaftName;
  }

  public Integer getIdPilot() {
    return idPilot;
  }

  public void setIdPilot(Integer idPilot) {
    this.idPilot = idPilot;
  }

  public Integer getIdUser() {
    return idUser;
  }

  public void setIdUser(Integer idUser) {
    this.idUser = idUser;
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
  
}
