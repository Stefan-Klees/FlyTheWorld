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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Stefan Klees
 */
@Entity
@Table(name = "fboUserObjekte")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "FboUserObjekte.findAll", query = "SELECT f FROM FboUserObjekte f")
  , @NamedQuery(name = "FboUserObjekte.findByIduserfbo", query = "SELECT f FROM FboUserObjekte f WHERE f.iduserfbo = :iduserfbo")
  , @NamedQuery(name = "FboUserObjekte.findByIdfboObjekt", query = "SELECT f FROM FboUserObjekte f WHERE f.idfboObjekt = :idfboObjekt")
  , @NamedQuery(name = "FboUserObjekte.findByIdUser", query = "SELECT f FROM FboUserObjekte f WHERE f.idUser = :idUser")
  , @NamedQuery(name = "FboUserObjekte.findByName", query = "SELECT f FROM FboUserObjekte f WHERE f.name = :name")
  , @NamedQuery(name = "FboUserObjekte.findByIcao", query = "SELECT f FROM FboUserObjekte f WHERE f.icao = :icao")
  , @NamedQuery(name = "FboUserObjekte.findByPreisAVGAS", query = "SELECT f FROM FboUserObjekte f WHERE f.preisAVGAS = :preisAVGAS")
  , @NamedQuery(name = "FboUserObjekte.findByEinkaufsPreisAVGAS", query = "SELECT f FROM FboUserObjekte f WHERE f.einkaufsPreisAVGAS = :einkaufsPreisAVGAS")
  , @NamedQuery(name = "FboUserObjekte.findByBestandAVGASkg", query = "SELECT f FROM FboUserObjekte f WHERE f.bestandAVGASkg = :bestandAVGASkg")
  , @NamedQuery(name = "FboUserObjekte.findByPreisJETA", query = "SELECT f FROM FboUserObjekte f WHERE f.preisJETA = :preisJETA")
  , @NamedQuery(name = "FboUserObjekte.findByBestandJETAkg", query = "SELECT f FROM FboUserObjekte f WHERE f.bestandJETAkg = :bestandJETAkg")
  , @NamedQuery(name = "FboUserObjekte.findByEinkaufsPreisJETA", query = "SELECT f FROM FboUserObjekte f WHERE f.einkaufsPreisJETA = :einkaufsPreisJETA")
  , @NamedQuery(name = "FboUserObjekte.findByMietbeginn", query = "SELECT f FROM FboUserObjekte f WHERE f.mietbeginn = :mietbeginn")
  , @NamedQuery(name = "FboUserObjekte.findByLetzteMietzahlung", query = "SELECT f FROM FboUserObjekte f WHERE f.letzteMietzahlung = :letzteMietzahlung")
  , @NamedQuery(name = "FboUserObjekte.findByFaelligkeitNaechsteMiete", query = "SELECT f FROM FboUserObjekte f WHERE f.faelligkeitNaechsteMiete = :faelligkeitNaechsteMiete")
  , @NamedQuery(name = "FboUserObjekte.findByPreisArbeitseinheit", query = "SELECT f FROM FboUserObjekte f WHERE f.preisArbeitseinheit = :preisArbeitseinheit")
  , @NamedQuery(name = "FboUserObjekte.findByBankkonto", query = "SELECT f FROM FboUserObjekte f WHERE f.bankkonto = :bankkonto")
  , @NamedQuery(name = "FboUserObjekte.findByKontoName", query = "SELECT f FROM FboUserObjekte f WHERE f.kontoName = :kontoName")})
public class FboUserObjekte implements Serializable {

  @Size(max = 250)
  @Column(name = "grafikLink")
  private String grafikLink;

  @Column(name = "idfluggesellschaft")
  private Integer idfluggesellschaft;

  @Column(name = "kostenstelle")
  private Integer kostenstelle;

  @Column(name = "terminalGebuehrInProzent")
  private Double terminalGebuehrInProzent;

  @Column(name = "mahnStufe")
  private Integer mahnStufe;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "iduserfbo")
  private Integer iduserfbo;
  @Basic(optional = false)
  @NotNull
  @Column(name = "idfboObjekt")
  private int idfboObjekt;
  @Column(name = "idUser")
  private Integer idUser;
  @Size(max = 250)
  @Column(name = "name")
  private String name;
  @Size(max = 45)
  @Column(name = "icao")
  private String icao;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "preisAVGAS")
  private Double preisAVGAS;
  @Column(name = "einkaufsPreisAVGAS")
  private Double einkaufsPreisAVGAS;
  @Column(name = "bestandAVGASkg")
  private Integer bestandAVGASkg;
  @Column(name = "preisJETA")
  private Double preisJETA;
  @Column(name = "bestandJETAkg")
  private Integer bestandJETAkg;
  @Column(name = "einkaufsPreisJETA")
  private Double einkaufsPreisJETA;
  @Column(name = "mietbeginn")
  @Temporal(TemporalType.DATE)
  private Date mietbeginn;
  @Column(name = "letzteMietzahlung")
  @Temporal(TemporalType.DATE)
  private Date letzteMietzahlung;
  @Column(name = "faelligkeitNaechsteMiete")
  @Temporal(TemporalType.DATE)
  private Date faelligkeitNaechsteMiete;
  @Column(name = "preisArbeitseinheit")
  private Double preisArbeitseinheit;
  @Size(max = 45)
  @Column(name = "bankkonto")
  private String bankkonto;
  @Size(max = 250)
  @Column(name = "kontoName")
  private String kontoName;

  public FboUserObjekte() {
  }

  public FboUserObjekte(Integer iduserfbo) {
    this.iduserfbo = iduserfbo;
  }

  public FboUserObjekte(Integer iduserfbo, int idfboObjekt) {
    this.iduserfbo = iduserfbo;
    this.idfboObjekt = idfboObjekt;
  }

  public Integer getIduserfbo() {
    return iduserfbo;
  }

  public void setIduserfbo(Integer iduserfbo) {
    this.iduserfbo = iduserfbo;
  }

  public int getIdfboObjekt() {
    return idfboObjekt;
  }

  public void setIdfboObjekt(int idfboObjekt) {
    this.idfboObjekt = idfboObjekt;
  }

  public Integer getIdUser() {
    return idUser;
  }

  public void setIdUser(Integer idUser) {
    this.idUser = idUser;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIcao() {
    return icao;
  }

  public void setIcao(String icao) {
    this.icao = icao;
  }

  public Double getPreisAVGAS() {
    return preisAVGAS;
  }

  public void setPreisAVGAS(Double preisAVGAS) {
    this.preisAVGAS = preisAVGAS;
  }

  public Double getEinkaufsPreisAVGAS() {
    return einkaufsPreisAVGAS;
  }

  public void setEinkaufsPreisAVGAS(Double einkaufsPreisAVGAS) {
    this.einkaufsPreisAVGAS = einkaufsPreisAVGAS;
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

  public Double getEinkaufsPreisJETA() {
    return einkaufsPreisJETA;
  }

  public void setEinkaufsPreisJETA(Double einkaufsPreisJETA) {
    this.einkaufsPreisJETA = einkaufsPreisJETA;
  }

  public Date getMietbeginn() {
    return mietbeginn;
  }

  public void setMietbeginn(Date mietbeginn) {
    this.mietbeginn = mietbeginn;
  }

  public Date getLetzteMietzahlung() {
    return letzteMietzahlung;
  }

  public void setLetzteMietzahlung(Date letzteMietzahlung) {
    this.letzteMietzahlung = letzteMietzahlung;
  }

  public Date getFaelligkeitNaechsteMiete() {
    return faelligkeitNaechsteMiete;
  }

  public void setFaelligkeitNaechsteMiete(Date faelligkeitNaechsteMiete) {
    this.faelligkeitNaechsteMiete = faelligkeitNaechsteMiete;
  }

  public Double getPreisArbeitseinheit() {
    return preisArbeitseinheit;
  }

  public void setPreisArbeitseinheit(Double preisArbeitseinheit) {
    this.preisArbeitseinheit = preisArbeitseinheit;
  }

  public String getBankkonto() {
    return bankkonto;
  }

  public void setBankkonto(String bankkonto) {
    this.bankkonto = bankkonto;
  }

  public String getKontoName() {
    return kontoName;
  }

  public void setKontoName(String kontoName) {
    this.kontoName = kontoName;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (iduserfbo != null ? iduserfbo.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof FboUserObjekte)) {
      return false;
    }
    FboUserObjekte other = (FboUserObjekte) object;
    if ((this.iduserfbo == null && other.iduserfbo != null) || (this.iduserfbo != null && !this.iduserfbo.equals(other.iduserfbo))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.FboUserObjekte[ iduserfbo=" + iduserfbo + " ]";
  }

  public Integer getMahnStufe() {
    return mahnStufe;
  }

  public void setMahnStufe(Integer mahnStufe) {
    this.mahnStufe = mahnStufe;
  }

  public Double getTerminalGebuehrInProzent() {
    return terminalGebuehrInProzent;
  }

  public void setTerminalGebuehrInProzent(Double terminalGebuehrInProzent) {
    this.terminalGebuehrInProzent = terminalGebuehrInProzent;
  }

  public Integer getKostenstelle() {
    return kostenstelle;
  }

  public void setKostenstelle(Integer kostenstelle) {
    this.kostenstelle = kostenstelle;
  }

  public Integer getIdfluggesellschaft() {
    return idfluggesellschaft;
  }

  public void setIdfluggesellschaft(Integer idfluggesellschaft) {
    this.idfluggesellschaft = idfluggesellschaft;
  }

  public String getGrafikLink() {
    return grafikLink;
  }

  public void setGrafikLink(String grafikLink) {
    this.grafikLink = grafikLink;
  }
  
}
