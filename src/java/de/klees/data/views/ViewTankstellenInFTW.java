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
import javax.persistence.Lob;
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
@Table(name = "viewTankstellenInFTW")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ViewTankstellenInFTW.findAll", query = "SELECT v FROM ViewTankstellenInFTW v")
  , @NamedQuery(name = "ViewTankstellenInFTW.findByIduserfbo", query = "SELECT v FROM ViewTankstellenInFTW v WHERE v.iduserfbo = :iduserfbo")
  , @NamedQuery(name = "ViewTankstellenInFTW.findByName", query = "SELECT v FROM ViewTankstellenInFTW v WHERE v.name = :name")
  , @NamedQuery(name = "ViewTankstellenInFTW.findByPreisAVGAS", query = "SELECT v FROM ViewTankstellenInFTW v WHERE v.preisAVGAS = :preisAVGAS")
  , @NamedQuery(name = "ViewTankstellenInFTW.findByBestandAVGASkg", query = "SELECT v FROM ViewTankstellenInFTW v WHERE v.bestandAVGASkg = :bestandAVGASkg")
  , @NamedQuery(name = "ViewTankstellenInFTW.findByPreisJETA", query = "SELECT v FROM ViewTankstellenInFTW v WHERE v.preisJETA = :preisJETA")
  , @NamedQuery(name = "ViewTankstellenInFTW.findByBestandJETAkg", query = "SELECT v FROM ViewTankstellenInFTW v WHERE v.bestandJETAkg = :bestandJETAkg")
  , @NamedQuery(name = "ViewTankstellenInFTW.findByFlughaftenKlasse", query = "SELECT v FROM ViewTankstellenInFTW v WHERE v.flughaftenKlasse = :flughaftenKlasse")})
public class ViewTankstellenInFTW implements Serializable {

  private static final long serialVersionUID = 1L;
  @Basic(optional = false)
  @NotNull
  @Column(name = "iduserfbo")
  @Id
  private int iduserfbo;
  @Lob
  @Size(max = 65535)
  @Column(name = "flughafen")
  private String flughafen;
  @Size(max = 250)
  @Column(name = "name")
  private String name;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "preisAVGAS")
  private Double preisAVGAS;
  @Column(name = "bestandAVGASkg")
  private Integer bestandAVGASkg;
  @Column(name = "preisJETA")
  private Double preisJETA;
  @Column(name = "bestandJETAkg")
  private Integer bestandJETAkg;
  @Column(name = "flughaftenKlasse")
  private Integer flughaftenKlasse;

  public ViewTankstellenInFTW() {
  }

  public int getIduserfbo() {
    return iduserfbo;
  }

  public void setIduserfbo(int iduserfbo) {
    this.iduserfbo = iduserfbo;
  }

  public String getFlughafen() {
    return flughafen;
  }

  public void setFlughafen(String flughafen) {
    this.flughafen = flughafen;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Double getPreisAVGAS() {
    return preisAVGAS;
  }

  public void setPreisAVGAS(Double preisAVGAS) {
    this.preisAVGAS = preisAVGAS;
  }

  public Integer getBestandAVGASkg() {
    return bestandAVGASkg;
  }

  public void setBestandAVGASkg(Integer bestandAVGASkg) {
    this.bestandAVGASkg = bestandAVGASkg;
  }

  public Double getPreisJETA() {
    return preisJETA;
  }

  public void setPreisJETA(Double preisJETA) {
    this.preisJETA = preisJETA;
  }

  public Integer getBestandJETAkg() {
    return bestandJETAkg;
  }

  public void setBestandJETAkg(Integer bestandJETAkg) {
    this.bestandJETAkg = bestandJETAkg;
  }

  public Integer getFlughaftenKlasse() {
    return flughaftenKlasse;
  }

  public void setFlughaftenKlasse(Integer flughaftenKlasse) {
    this.flughaftenKlasse = flughaftenKlasse;
  }
  
}
