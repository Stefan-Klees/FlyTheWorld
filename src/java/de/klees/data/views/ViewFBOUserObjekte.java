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
@Table(name = "viewFBOUserObjekte")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ViewFBOUserObjekte.findAll", query = "SELECT v FROM ViewFBOUserObjekte v"),
  @NamedQuery(name = "ViewFBOUserObjekte.findByIdUser", query = "SELECT v FROM ViewFBOUserObjekte v WHERE v.idUser = :idUser"),
  @NamedQuery(name = "ViewFBOUserObjekte.findByUserName", query = "SELECT v FROM ViewFBOUserObjekte v WHERE v.userName = :userName"),
  @NamedQuery(name = "ViewFBOUserObjekte.findByName", query = "SELECT v FROM ViewFBOUserObjekte v WHERE v.name = :name"),
  @NamedQuery(name = "ViewFBOUserObjekte.findByIcao", query = "SELECT v FROM ViewFBOUserObjekte v WHERE v.icao = :icao"),
  @NamedQuery(name = "ViewFBOUserObjekte.findByObjektName", query = "SELECT v FROM ViewFBOUserObjekte v WHERE v.objektName = :objektName"),
  @NamedQuery(name = "ViewFBOUserObjekte.findByAnzahlRouten", query = "SELECT v FROM ViewFBOUserObjekte v WHERE v.anzahlRouten = :anzahlRouten"),
  @NamedQuery(name = "ViewFBOUserObjekte.findByAnzahlStellplaetze", query = "SELECT v FROM ViewFBOUserObjekte v WHERE v.anzahlStellplaetze = :anzahlStellplaetze"),
  @NamedQuery(name = "ViewFBOUserObjekte.findByTankstelle", query = "SELECT v FROM ViewFBOUserObjekte v WHERE v.tankstelle = :tankstelle"),
  @NamedQuery(name = "ViewFBOUserObjekte.findByFbo", query = "SELECT v FROM ViewFBOUserObjekte v WHERE v.fbo = :fbo"),
  @NamedQuery(name = "ViewFBOUserObjekte.findByMietPreis", query = "SELECT v FROM ViewFBOUserObjekte v WHERE v.mietPreis = :mietPreis"),
  @NamedQuery(name = "ViewFBOUserObjekte.findByAnzahlPersonal", query = "SELECT v FROM ViewFBOUserObjekte v WHERE v.anzahlPersonal = :anzahlPersonal"),
  @NamedQuery(name = "ViewFBOUserObjekte.findByServicehangar", query = "SELECT v FROM ViewFBOUserObjekte v WHERE v.servicehangar = :servicehangar"),
  @NamedQuery(name = "ViewFBOUserObjekte.findByTankstelleMaxFuelKG", query = "SELECT v FROM ViewFBOUserObjekte v WHERE v.tankstelleMaxFuelKG = :tankstelleMaxFuelKG"),
  @NamedQuery(name = "ViewFBOUserObjekte.findByAbfertigungsTerminal", query = "SELECT v FROM ViewFBOUserObjekte v WHERE v.abfertigungsTerminal = :abfertigungsTerminal"),
  @NamedQuery(name = "ViewFBOUserObjekte.findByPreisAVGAS", query = "SELECT v FROM ViewFBOUserObjekte v WHERE v.preisAVGAS = :preisAVGAS"),
  @NamedQuery(name = "ViewFBOUserObjekte.findByEinkaufsPreisAVGAS", query = "SELECT v FROM ViewFBOUserObjekte v WHERE v.einkaufsPreisAVGAS = :einkaufsPreisAVGAS"),
  @NamedQuery(name = "ViewFBOUserObjekte.findByBestandAVGASkg", query = "SELECT v FROM ViewFBOUserObjekte v WHERE v.bestandAVGASkg = :bestandAVGASkg"),
  @NamedQuery(name = "ViewFBOUserObjekte.findByPreisJETA", query = "SELECT v FROM ViewFBOUserObjekte v WHERE v.preisJETA = :preisJETA"),
  @NamedQuery(name = "ViewFBOUserObjekte.findByBestandJETAkg", query = "SELECT v FROM ViewFBOUserObjekte v WHERE v.bestandJETAkg = :bestandJETAkg"),
  @NamedQuery(name = "ViewFBOUserObjekte.findByEinkaufsPreisJETA", query = "SELECT v FROM ViewFBOUserObjekte v WHERE v.einkaufsPreisJETA = :einkaufsPreisJETA"),
  @NamedQuery(name = "ViewFBOUserObjekte.findByMietbeginn", query = "SELECT v FROM ViewFBOUserObjekte v WHERE v.mietbeginn = :mietbeginn"),
  @NamedQuery(name = "ViewFBOUserObjekte.findByLetzteMietzahlung", query = "SELECT v FROM ViewFBOUserObjekte v WHERE v.letzteMietzahlung = :letzteMietzahlung"),
  @NamedQuery(name = "ViewFBOUserObjekte.findByFaelligkeitNaechsteMiete", query = "SELECT v FROM ViewFBOUserObjekte v WHERE v.faelligkeitNaechsteMiete = :faelligkeitNaechsteMiete"),
  @NamedQuery(name = "ViewFBOUserObjekte.findByPreisArbeitseinheit", query = "SELECT v FROM ViewFBOUserObjekte v WHERE v.preisArbeitseinheit = :preisArbeitseinheit"),
  @NamedQuery(name = "ViewFBOUserObjekte.findByIduserfbo", query = "SELECT v FROM ViewFBOUserObjekte v WHERE v.iduserfbo = :iduserfbo")})
public class ViewFBOUserObjekte implements Serializable {

  private static final long serialVersionUID = 1L;

  @Basic(optional = false)
  @NotNull
  @Column(name = "iduserfbo")
  @Id
  private int iduserfbo;

  @Column(name = "servicehangarQM")
  private Integer servicehangarQM;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "servicehangarQMPreis")
  private Double servicehangarQMPreis;

  @Basic(optional = false)
  @NotNull
  @Column(name = "idFboObjekt")
  private int idFboObjekt;

  @Column(name = "spritlager")
  private Boolean spritlager;

  @Size(max = 250)
  @Column(name = "grafikLink")
  private String grafikLink;

  @Column(name = "idfluggesellschaft")
  private Integer idfluggesellschaft;

  @Column(name = "klasse")
  private Integer klasse;

  @Column(name = "businessLounge")
  private Boolean businessLounge;

  @Column(name = "kostenstelle")
  private Integer kostenstelle;

  @Column(name = "terminalGebuehrInProzent")
  private Double terminalGebuehrInProzent;

  @Column(name = "mahnStufe")
  private Integer mahnStufe;

  @Size(max = 45)
  @Column(name = "bankKonto")
  private String bankKonto;
  @Size(max = 250)
  @Column(name = "kontoName")
  private String kontoName;

  @Column(name = "terminalMaxPax")
  private Integer terminalMaxPax;

  @Column(name = "idUser")
  private Integer idUser;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 80)
  @Column(name = "userName")
  private String userName;
  @Size(max = 250)
  @Column(name = "name")
  private String name;
  @Size(max = 45)
  @Column(name = "icao")
  private String icao;
  @Size(max = 250)
  @Column(name = "objektName")
  private String objektName;
  @Column(name = "anzahlRouten")
  private Integer anzahlRouten;
  @Column(name = "anzahlStellplaetze")
  private Integer anzahlStellplaetze;
  @Column(name = "tankstelle")
  private Boolean tankstelle;
  @Column(name = "fbo")
  private Boolean fbo;
  @Column(name = "mietPreis")
  private Integer mietPreis;
  @Column(name = "anzahlPersonal")
  private Integer anzahlPersonal;
  @Column(name = "servicehangar")
  private Boolean servicehangar;
  @Column(name = "tankstelleMaxFuelKG")
  private Integer tankstelleMaxFuelKG;
  @Column(name = "abfertigungsTerminal")
  private Boolean abfertigungsTerminal;
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

  public ViewFBOUserObjekte() {
  }

  public Integer getIdUser() {
    return idUser;
  }

  public void setIdUser(Integer idUser) {
    this.idUser = idUser;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
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

  public String getObjektName() {
    return objektName;
  }

  public void setObjektName(String objektName) {
    this.objektName = objektName;
  }

  public Integer getAnzahlRouten() {
    return anzahlRouten;
  }

  public void setAnzahlRouten(Integer anzahlRouten) {
    this.anzahlRouten = anzahlRouten;
  }

  public Integer getAnzahlStellplaetze() {
    return anzahlStellplaetze;
  }

  public void setAnzahlStellplaetze(Integer anzahlStellplaetze) {
    this.anzahlStellplaetze = anzahlStellplaetze;
  }

  public Boolean getTankstelle() {
    return tankstelle;
  }

  public void setTankstelle(Boolean tankstelle) {
    this.tankstelle = tankstelle;
  }

  public Boolean getFbo() {
    return fbo;
  }

  public void setFbo(Boolean fbo) {
    this.fbo = fbo;
  }

  public Integer getMietPreis() {
    return mietPreis;
  }

  public void setMietPreis(Integer mietPreis) {
    this.mietPreis = mietPreis;
  }

  public Integer getAnzahlPersonal() {
    return anzahlPersonal;
  }

  public void setAnzahlPersonal(Integer anzahlPersonal) {
    this.anzahlPersonal = anzahlPersonal;
  }

  public Boolean getServicehangar() {
    return servicehangar;
  }

  public void setServicehangar(Boolean servicehangar) {
    this.servicehangar = servicehangar;
  }

  public Integer getTankstelleMaxFuelKG() {
    return tankstelleMaxFuelKG;
  }

  public void setTankstelleMaxFuelKG(Integer tankstelleMaxFuelKG) {
    this.tankstelleMaxFuelKG = tankstelleMaxFuelKG;
  }

  public Boolean getAbfertigungsTerminal() {
    return abfertigungsTerminal;
  }

  public void setAbfertigungsTerminal(Boolean abfertigungsTerminal) {
    this.abfertigungsTerminal = abfertigungsTerminal;
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

  public int getIduserfbo() {
    return iduserfbo;
  }

  public void setIduserfbo(int iduserfbo) {
    this.iduserfbo = iduserfbo;
  }

  public Integer getTerminalMaxPax() {
    return terminalMaxPax;
  }

  public void setTerminalMaxPax(Integer terminalMaxPax) {
    this.terminalMaxPax = terminalMaxPax;
  }

  public String getBankKonto() {
    return bankKonto;
  }

  public void setBankKonto(String bankKonto) {
    this.bankKonto = bankKonto;
  }

  public String getKontoName() {
    return kontoName;
  }

  public void setKontoName(String kontoName) {
    this.kontoName = kontoName;
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

  public Boolean getBusinessLounge() {
    return businessLounge;
  }

  public void setBusinessLounge(Boolean businessLounge) {
    this.businessLounge = businessLounge;
  }

  public Integer getKlasse() {
    return klasse;
  }

  public void setKlasse(Integer klasse) {
    this.klasse = klasse;
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

  public Boolean getSpritlager() {
    return spritlager;
  }

  public void setSpritlager(Boolean spritlager) {
    this.spritlager = spritlager;
  }

  public int getIdFboObjekt() {
    return idFboObjekt;
  }

  public void setIdFboObjekt(int idFboObjekt) {
    this.idFboObjekt = idFboObjekt;
  }

  public Integer getServicehangarQM() {
    return servicehangarQM;
  }

  public void setServicehangarQM(Integer servicehangarQM) {
    this.servicehangarQM = servicehangarQM;
  }

  public Double getServicehangarQMPreis() {
    return servicehangarQMPreis;
  }

  public void setServicehangarQMPreis(Double servicehangarQMPreis) {
    this.servicehangarQMPreis = servicehangarQMPreis;
  }

}
