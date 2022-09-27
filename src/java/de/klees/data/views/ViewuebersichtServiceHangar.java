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
@Table(name = "view_uebersichtServiceHangar")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ViewuebersichtServiceHangar.findAll", query = "SELECT v FROM ViewuebersichtServiceHangar v")})
public class ViewuebersichtServiceHangar implements Serializable {

  private static final long serialVersionUID = 1L;
  @Size(max = 250)
  @Column(name = "Betreiber")
  private String betreiber;
  @Basic(optional = false)
  @NotNull
  @Column(name = "iduserfbo")
  @Id
  private int iduserfbo;
  @Size(max = 45)
  @Column(name = "icao")
  private String icao;
  @Size(max = 250)
  @Column(name = "HangarName")
  private String hangarName;
  @Size(max = 250)
  @Column(name = "besitzerName")
  private String besitzerName;
  @Column(name = "servicehangar")
  private Boolean servicehangar;
  @Column(name = "servicehangarQM")
  private Integer servicehangarQM;
  @Size(max = 80)
  @Column(name = "land")
  private String land;
  @Size(max = 250)
  @Column(name = "logoURL")
  private String logoURL;

  public ViewuebersichtServiceHangar() {
  }

  public String getBetreiber() {
    return betreiber;
  }

  public void setBetreiber(String betreiber) {
    this.betreiber = betreiber;
  }

  public int getIduserfbo() {
    return iduserfbo;
  }

  public void setIduserfbo(int iduserfbo) {
    this.iduserfbo = iduserfbo;
  }

  public String getIcao() {
    return icao;
  }

  public void setIcao(String icao) {
    this.icao = icao;
  }

  public String getHangarName() {
    return hangarName;
  }

  public void setHangarName(String hangarName) {
    this.hangarName = hangarName;
  }

  public String getBesitzerName() {
    return besitzerName;
  }

  public void setBesitzerName(String besitzerName) {
    this.besitzerName = besitzerName;
  }

  public Boolean getServicehangar() {
    return servicehangar;
  }

  public void setServicehangar(Boolean servicehangar) {
    this.servicehangar = servicehangar;
  }

  public Integer getServicehangarQM() {
    return servicehangarQM;
  }

  public void setServicehangarQM(Integer servicehangarQM) {
    this.servicehangarQM = servicehangarQM;
  }

  public String getLand() {
    return land;
  }

  public void setLand(String land) {
    this.land = land;
  }

  public String getLogoURL() {
    return logoURL;
  }

  public void setLogoURL(String logoURL) {
    this.logoURL = logoURL;
  }

}
