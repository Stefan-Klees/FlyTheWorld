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
@Table(name = "viewFluggesellschaftManager")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ViewFluggesellschaftManager.findAll", query = "SELECT v FROM ViewFluggesellschaftManager v")})
public class ViewFluggesellschaftManager implements Serializable {

  @Column(name = "allowFBO")
  private Boolean allowFBO;
  @Column(name = "allowFBOHinzufuegen")
  private Boolean allowFBOHinzufuegen;
  @Column(name = "allowFBOBearbeiten")
  private Boolean allowFBOBearbeiten;
  @Column(name = "allowFBOLoeschen")
  private Boolean allowFBOLoeschen;
  @Column(name = "allowFBOTankstelleVerwalten")
  private Boolean allowFBOTankstelleVerwalten;

  @Column(name = "geschaeftsfuehrer")
  private Boolean geschaeftsfuehrer;

  @Size(max = 250)
  @Column(name = "bezeichnung")
  private String bezeichnung;

  @Column(name = "allowRoutenLoeschen")
  private Boolean allowRoutenLoeschen;

  @Column(name = "allowPilotenBearbeiten")
  private Boolean allowPilotenBearbeiten;

  private static final long serialVersionUID = 1L;
  @Basic(optional = false)
  @NotNull
  @Column(name = "idfluggesellschaftmanager")
  @Id
  private int idfluggesellschaftmanager;
  @Column(name = "userid")
  private Integer userid;
  @Column(name = "fluggesellschaftid")
  private Integer fluggesellschaftid;
  @Column(name = "allowFlugzeuge")
  private Boolean allowFlugzeuge;
  @Column(name = "allowFlugzeugeBearbeiten")
  private Boolean allowFlugzeugeBearbeiten;
  @Column(name = "allowFlugzeugeKaufen")
  private Boolean allowFlugzeugeKaufen;
  @Column(name = "allowFlugzeugeVerkaufen")
  private Boolean allowFlugzeugeVerkaufen;
  @Column(name = "allowBank")
  private Boolean allowBank;
  @Column(name = "allowBankBuchhaltung")
  private Boolean allowBankBuchhaltung;
  @Column(name = "allowBankTransfer")
  private Boolean allowBankTransfer;
  @Column(name = "allowPiloten")
  private Boolean allowPiloten;
  @Column(name = "allowPilotenEntlassen")
  private Boolean allowPilotenEntlassen;
  @Column(name = "allowPilotenEinstellen")
  private Boolean allowPilotenEinstellen;
  @Column(name = "allowRouten")
  private Boolean allowRouten;
  @Column(name = "allowRoutenErstellen")
  private Boolean allowRoutenErstellen;
  @Column(name = "allowRoutenBearbeiten")
  private Boolean allowRoutenBearbeiten;
  @Column(name = "allowFluggesellschaftBearbeiten")
  private Boolean allowFluggesellschaftBearbeiten;
  @Column(name = "allowFluggesellschaftLoeschen")
  private Boolean allowFluggesellschaftLoeschen;
  @Column(name = "allowManager")
  private Boolean allowManager;
  @Column(name = "allowManagerEinstellen")
  private Boolean allowManagerEinstellen;
  @Column(name = "allowManagerLoeschen")
  private Boolean allowManagerLoeschen;
  @Column(name = "allowManagerBearbeiten")
  private Boolean allowManagerBearbeiten;
  @Column(name = "allowFlugzeugeTransfer")
  private Boolean allowFlugzeugeTransfer;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 80)
  @Column(name = "BenutzerName")
  private String benutzerName;

  public ViewFluggesellschaftManager() {
  }

  public int getIdfluggesellschaftmanager() {
    return idfluggesellschaftmanager;
  }

  public void setIdfluggesellschaftmanager(int idfluggesellschaftmanager) {
    this.idfluggesellschaftmanager = idfluggesellschaftmanager;
  }

  public Integer getUserid() {
    return userid;
  }

  public void setUserid(Integer userid) {
    this.userid = userid;
  }

  public Integer getFluggesellschaftid() {
    return fluggesellschaftid;
  }

  public void setFluggesellschaftid(Integer fluggesellschaftid) {
    this.fluggesellschaftid = fluggesellschaftid;
  }

  public Boolean getAllowFlugzeuge() {
    return allowFlugzeuge;
  }

  public void setAllowFlugzeuge(Boolean allowFlugzeuge) {
    this.allowFlugzeuge = allowFlugzeuge;
  }

  public Boolean getAllowFlugzeugeBearbeiten() {
    return allowFlugzeugeBearbeiten;
  }

  public void setAllowFlugzeugeBearbeiten(Boolean allowFlugzeugeBearbeiten) {
    this.allowFlugzeugeBearbeiten = allowFlugzeugeBearbeiten;
  }

  public Boolean getAllowFlugzeugeKaufen() {
    return allowFlugzeugeKaufen;
  }

  public void setAllowFlugzeugeKaufen(Boolean allowFlugzeugeKaufen) {
    this.allowFlugzeugeKaufen = allowFlugzeugeKaufen;
  }

  public Boolean getAllowFlugzeugeVerkaufen() {
    return allowFlugzeugeVerkaufen;
  }

  public void setAllowFlugzeugeVerkaufen(Boolean allowFlugzeugeVerkaufen) {
    this.allowFlugzeugeVerkaufen = allowFlugzeugeVerkaufen;
  }

  public Boolean getAllowBank() {
    return allowBank;
  }

  public void setAllowBank(Boolean allowBank) {
    this.allowBank = allowBank;
  }

  public Boolean getAllowBankBuchhaltung() {
    return allowBankBuchhaltung;
  }

  public void setAllowBankBuchhaltung(Boolean allowBankBuchhaltung) {
    this.allowBankBuchhaltung = allowBankBuchhaltung;
  }

  public Boolean getAllowBankTransfer() {
    return allowBankTransfer;
  }

  public void setAllowBankTransfer(Boolean allowBankTransfer) {
    this.allowBankTransfer = allowBankTransfer;
  }

  public Boolean getAllowPiloten() {
    return allowPiloten;
  }

  public void setAllowPiloten(Boolean allowPiloten) {
    this.allowPiloten = allowPiloten;
  }

  public Boolean getAllowPilotenEntlassen() {
    return allowPilotenEntlassen;
  }

  public void setAllowPilotenEntlassen(Boolean allowPilotenEntlassen) {
    this.allowPilotenEntlassen = allowPilotenEntlassen;
  }

  public Boolean getAllowPilotenEinstellen() {
    return allowPilotenEinstellen;
  }

  public void setAllowPilotenEinstellen(Boolean allowPilotenEinstellen) {
    this.allowPilotenEinstellen = allowPilotenEinstellen;
  }

  public Boolean getAllowRouten() {
    return allowRouten;
  }

  public void setAllowRouten(Boolean allowRouten) {
    this.allowRouten = allowRouten;
  }

  public Boolean getAllowRoutenErstellen() {
    return allowRoutenErstellen;
  }

  public void setAllowRoutenErstellen(Boolean allowRoutenErstellen) {
    this.allowRoutenErstellen = allowRoutenErstellen;
  }

  public Boolean getAllowRoutenBearbeiten() {
    return allowRoutenBearbeiten;
  }

  public void setAllowRoutenBearbeiten(Boolean allowRoutenBearbeiten) {
    this.allowRoutenBearbeiten = allowRoutenBearbeiten;
  }

  public Boolean getAllowFluggesellschaftBearbeiten() {
    return allowFluggesellschaftBearbeiten;
  }

  public void setAllowFluggesellschaftBearbeiten(Boolean allowFluggesellschaftBearbeiten) {
    this.allowFluggesellschaftBearbeiten = allowFluggesellschaftBearbeiten;
  }

  public Boolean getAllowFluggesellschaftLoeschen() {
    return allowFluggesellschaftLoeschen;
  }

  public void setAllowFluggesellschaftLoeschen(Boolean allowFluggesellschaftLoeschen) {
    this.allowFluggesellschaftLoeschen = allowFluggesellschaftLoeschen;
  }

  public Boolean getAllowManager() {
    return allowManager;
  }

  public void setAllowManager(Boolean allowManager) {
    this.allowManager = allowManager;
  }

  public Boolean getAllowManagerEinstellen() {
    return allowManagerEinstellen;
  }

  public void setAllowManagerEinstellen(Boolean allowManagerEinstellen) {
    this.allowManagerEinstellen = allowManagerEinstellen;
  }

  public Boolean getAllowManagerLoeschen() {
    return allowManagerLoeschen;
  }

  public void setAllowManagerLoeschen(Boolean allowManagerLoeschen) {
    this.allowManagerLoeschen = allowManagerLoeschen;
  }

  public Boolean getAllowManagerBearbeiten() {
    return allowManagerBearbeiten;
  }

  public void setAllowManagerBearbeiten(Boolean allowManagerBearbeiten) {
    this.allowManagerBearbeiten = allowManagerBearbeiten;
  }

  public Boolean getAllowFlugzeugeTransfer() {
    return allowFlugzeugeTransfer;
  }

  public void setAllowFlugzeugeTransfer(Boolean allowFlugzeugeTransfer) {
    this.allowFlugzeugeTransfer = allowFlugzeugeTransfer;
  }

  public String getBenutzerName() {
    return benutzerName;
  }

  public void setBenutzerName(String benutzerName) {
    this.benutzerName = benutzerName;
  }

  public Boolean getAllowPilotenBearbeiten() {
    return allowPilotenBearbeiten;
  }

  public void setAllowPilotenBearbeiten(Boolean allowPilotenBearbeiten) {
    this.allowPilotenBearbeiten = allowPilotenBearbeiten;
  }

  public Boolean getAllowRoutenLoeschen() {
    return allowRoutenLoeschen;
  }

  public void setAllowRoutenLoeschen(Boolean allowRoutenLoeschen) {
    this.allowRoutenLoeschen = allowRoutenLoeschen;
  }

  public String getBezeichnung() {
    return bezeichnung;
  }

  public void setBezeichnung(String bezeichnung) {
    this.bezeichnung = bezeichnung;
  }

  public Boolean getGeschaeftsfuehrer() {
    return geschaeftsfuehrer;
  }

  public void setGeschaeftsfuehrer(Boolean geschaeftsfuehrer) {
    this.geschaeftsfuehrer = geschaeftsfuehrer;
  }

  public Boolean getAllowFBO() {
    return allowFBO;
  }

  public void setAllowFBO(Boolean allowFBO) {
    this.allowFBO = allowFBO;
  }

  public Boolean getAllowFBOHinzufuegen() {
    return allowFBOHinzufuegen;
  }

  public void setAllowFBOHinzufuegen(Boolean allowFBOHinzufuegen) {
    this.allowFBOHinzufuegen = allowFBOHinzufuegen;
  }

  public Boolean getAllowFBOBearbeiten() {
    return allowFBOBearbeiten;
  }

  public void setAllowFBOBearbeiten(Boolean allowFBOBearbeiten) {
    this.allowFBOBearbeiten = allowFBOBearbeiten;
  }

  public Boolean getAllowFBOLoeschen() {
    return allowFBOLoeschen;
  }

  public void setAllowFBOLoeschen(Boolean allowFBOLoeschen) {
    this.allowFBOLoeschen = allowFBOLoeschen;
  }

  public Boolean getAllowFBOTankstelleVerwalten() {
    return allowFBOTankstelleVerwalten;
  }

  public void setAllowFBOTankstelleVerwalten(Boolean allowFBOTankstelleVerwalten) {
    this.allowFBOTankstelleVerwalten = allowFBOTankstelleVerwalten;
  }
  
}
