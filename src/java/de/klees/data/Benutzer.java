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
import javax.persistence.Lob;
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
@Table(name = "benutzer")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Benutzer.findAll", query = "SELECT b FROM Benutzer b"),
  @NamedQuery(name = "Benutzer.findByIdUser", query = "SELECT b FROM Benutzer b WHERE b.idUser = :idUser"),
  @NamedQuery(name = "Benutzer.findByFluggesellschaftManagerID", query = "SELECT b FROM Benutzer b WHERE b.fluggesellschaftManagerID = :fluggesellschaftManagerID"),
  @NamedQuery(name = "Benutzer.findByCreated", query = "SELECT b FROM Benutzer b WHERE b.created = :created"),
  @NamedQuery(name = "Benutzer.findByLastlogon", query = "SELECT b FROM Benutzer b WHERE b.lastlogon = :lastlogon"),
  @NamedQuery(name = "Benutzer.findByName1", query = "SELECT b FROM Benutzer b WHERE b.name1 = :name1"),
  @NamedQuery(name = "Benutzer.findByEmail", query = "SELECT b FROM Benutzer b WHERE b.email = :email"),
  @NamedQuery(name = "Benutzer.findByMapLayer", query = "SELECT b FROM Benutzer b WHERE b.mapLayer = :mapLayer"),
  @NamedQuery(name = "Benutzer.findByCashmoney", query = "SELECT b FROM Benutzer b WHERE b.cashmoney = :cashmoney"),
  @NamedQuery(name = "Benutzer.findByLizenz", query = "SELECT b FROM Benutzer b WHERE b.lizenz = :lizenz"),
  @NamedQuery(name = "Benutzer.findByUrl", query = "SELECT b FROM Benutzer b WHERE b.url = :url"),
  @NamedQuery(name = "Benutzer.findByDateformat", query = "SELECT b FROM Benutzer b WHERE b.dateformat = :dateformat"),
  @NamedQuery(name = "Benutzer.findByReadaccesskey", query = "SELECT b FROM Benutzer b WHERE b.readaccesskey = :readaccesskey"),
  @NamedQuery(name = "Benutzer.findByWriteaccesskey", query = "SELECT b FROM Benutzer b WHERE b.writeaccesskey = :writeaccesskey"),
  @NamedQuery(name = "Benutzer.findByPasswort", query = "SELECT b FROM Benutzer b WHERE b.passwort = :passwort"),
  @NamedQuery(name = "Benutzer.findByTheme", query = "SELECT b FROM Benutzer b WHERE b.theme = :theme"),
  @NamedQuery(name = "Benutzer.findByGesperrt", query = "SELECT b FROM Benutzer b WHERE b.gesperrt = :gesperrt"),
  @NamedQuery(name = "Benutzer.findByIsActive", query = "SELECT b FROM Benutzer b WHERE b.isActive = :isActive"),
  @NamedQuery(name = "Benutzer.findByRolle", query = "SELECT b FROM Benutzer b WHERE b.rolle = :rolle"),
  @NamedQuery(name = "Benutzer.findByFlightmiles", query = "SELECT b FROM Benutzer b WHERE b.flightmiles = :flightmiles"),
  @NamedQuery(name = "Benutzer.findByFlights", query = "SELECT b FROM Benutzer b WHERE b.flights = :flights"),
  @NamedQuery(name = "Benutzer.findByFlighttime", query = "SELECT b FROM Benutzer b WHERE b.flighttime = :flighttime"),
  @NamedQuery(name = "Benutzer.findByFlightpaxes", query = "SELECT b FROM Benutzer b WHERE b.flightpaxes = :flightpaxes"),
  @NamedQuery(name = "Benutzer.findByFlightcargo", query = "SELECT b FROM Benutzer b WHERE b.flightcargo = :flightcargo"),
  @NamedQuery(name = "Benutzer.findByFlightrettung", query = "SELECT b FROM Benutzer b WHERE b.flightrettung = :flightrettung"),
  @NamedQuery(name = "Benutzer.findBySprache", query = "SELECT b FROM Benutzer b WHERE b.sprache = :sprache"),
  @NamedQuery(name = "Benutzer.findByOnline", query = "SELECT b FROM Benutzer b WHERE b.online = :online"),
  @NamedQuery(name = "Benutzer.findByBanner", query = "SELECT b FROM Benutzer b WHERE b.banner = :banner"),
  @NamedQuery(name = "Benutzer.findByAllowBenutzerEdit", query = "SELECT b FROM Benutzer b WHERE b.allowBenutzerEdit = :allowBenutzerEdit"),
  @NamedQuery(name = "Benutzer.findByAllowFlugzeugeEdit", query = "SELECT b FROM Benutzer b WHERE b.allowFlugzeugeEdit = :allowFlugzeugeEdit"),
  @NamedQuery(name = "Benutzer.findByAllowFlughafenEdit", query = "SELECT b FROM Benutzer b WHERE b.allowFlughafenEdit = :allowFlughafenEdit"),
  @NamedQuery(name = "Benutzer.findByAllowGeschichtenEdit", query = "SELECT b FROM Benutzer b WHERE b.allowGeschichtenEdit = :allowGeschichtenEdit"),
  @NamedQuery(name = "Benutzer.findByAllowNewsEdit", query = "SELECT b FROM Benutzer b WHERE b.allowNewsEdit = :allowNewsEdit"),
  @NamedQuery(name = "Benutzer.findByAllowToolsOpen", query = "SELECT b FROM Benutzer b WHERE b.allowToolsOpen = :allowToolsOpen"),
  @NamedQuery(name = "Benutzer.findByAllowAdminOpen", query = "SELECT b FROM Benutzer b WHERE b.allowAdminOpen = :allowAdminOpen"),
  @NamedQuery(name = "Benutzer.findByAllowRettungsstationEdit", query = "SELECT b FROM Benutzer b WHERE b.allowRettungsstationEdit = :allowRettungsstationEdit"),
  @NamedQuery(name = "Benutzer.findByAllowAirportDispatcher", query = "SELECT b FROM Benutzer b WHERE b.allowAirportDispatcher = :allowAirportDispatcher"),
  @NamedQuery(name = "Benutzer.findByAllowTestUser", query = "SELECT b FROM Benutzer b WHERE b.allowTestUser = :allowTestUser"),
  @NamedQuery(name = "Benutzer.findByBankKonto", query = "SELECT b FROM Benutzer b WHERE b.bankKonto = :bankKonto"),
  @NamedQuery(name = "Benutzer.findByZeitZone", query = "SELECT b FROM Benutzer b WHERE b.zeitZone = :zeitZone"),
  @NamedQuery(name = "Benutzer.findByFunktion", query = "SELECT b FROM Benutzer b WHERE b.funktion = :funktion"),
  @NamedQuery(name = "Benutzer.findByIconSize", query = "SELECT b FROM Benutzer b WHERE b.iconSize = :iconSize"),
  @NamedQuery(name = "Benutzer.findByStandort", query = "SELECT b FROM Benutzer b WHERE b.standort = :standort"),
  @NamedQuery(name = "Benutzer.findByRangabzeichen", query = "SELECT b FROM Benutzer b WHERE b.rangabzeichen = :rangabzeichen"),
  @NamedQuery(name = "Benutzer.findByFlugzeitenFG", query = "SELECT b FROM Benutzer b WHERE b.flugzeitenFG = :flugzeitenFG"),
  @NamedQuery(name = "Benutzer.findByDisplayname", query = "SELECT b FROM Benutzer b WHERE b.displayname = :displayname"),
  @NamedQuery(name = "Benutzer.findBySound", query = "SELECT b FROM Benutzer b WHERE b.sound = :sound"),
  @NamedQuery(name = "Benutzer.findBySummenFensterAnzeigen", query = "SELECT b FROM Benutzer b WHERE b.summenFensterAnzeigen = :summenFensterAnzeigen"),
  @NamedQuery(name = "Benutzer.findByTabellenzeilen", query = "SELECT b FROM Benutzer b WHERE b.tabellenzeilen = :tabellenzeilen")})
public class Benutzer implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idUser")
  private Integer idUser;
  @Column(name = "fluggesellschaftManagerID")
  private Integer fluggesellschaftManagerID;
  @Column(name = "created")
  @Temporal(TemporalType.TIMESTAMP)
  private Date created;
  @Column(name = "lastlogon")
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastlogon;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 80)
  @Column(name = "name1")
  private String name1;
  // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
  @Size(max = 120)
  @Column(name = "email")
  private String email;
  @Size(max = 250)
  @Column(name = "mapLayer")
  private String mapLayer;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "cashmoney")
  private Double cashmoney;
  @Size(max = 80)
  @Column(name = "lizenz")
  private String lizenz;
  @Size(max = 45)
  @Column(name = "url")
  private String url;
  @Size(max = 45)
  @Column(name = "dateformat")
  private String dateformat;
  @Lob
  @Size(max = 65535)
  @Column(name = "banlists")
  private String banlists;
  @Size(max = 80)
  @Column(name = "readaccesskey")
  private String readaccesskey;
  @Size(max = 45)
  @Column(name = "writeaccesskey")
  private String writeaccesskey;
  @Size(max = 255)
  @Column(name = "passwort")
  private String passwort;
  @Size(max = 250)
  @Column(name = "theme")
  private String theme;
  @Column(name = "gesperrt")
  private Boolean gesperrt;
  @Column(name = "isActive")
  private Boolean isActive;
  @Size(max = 128)
  @Column(name = "rolle")
  private String rolle;
  @Column(name = "flightmiles")
  private Integer flightmiles;
  @Column(name = "flights")
  private Integer flights;
  @Column(name = "flighttime")
  private Integer flighttime;
  @Column(name = "flightpaxes")
  private Integer flightpaxes;
  @Column(name = "flightcargo")
  private Integer flightcargo;
  @Column(name = "flightrettung")
  private Integer flightrettung;
  @Size(max = 45)
  @Column(name = "sprache")
  private String sprache;
  @Column(name = "online")
  private Boolean online;
  @Size(max = 250)
  @Column(name = "banner")
  private String banner;
  @Column(name = "allowBenutzerEdit")
  private Boolean allowBenutzerEdit;
  @Column(name = "allowFlugzeugeEdit")
  private Boolean allowFlugzeugeEdit;
  @Column(name = "allowFlughafenEdit")
  private Boolean allowFlughafenEdit;
  @Column(name = "allowGeschichtenEdit")
  private Boolean allowGeschichtenEdit;
  @Column(name = "allowNewsEdit")
  private Boolean allowNewsEdit;
  @Column(name = "allowToolsOpen")
  private Boolean allowToolsOpen;
  @Column(name = "allowAdminOpen")
  private Boolean allowAdminOpen;
  @Column(name = "allowRettungsstationEdit")
  private Boolean allowRettungsstationEdit;
  @Column(name = "allowAirportDispatcher")
  private Boolean allowAirportDispatcher;
  @Column(name = "allowTestUser")
  private Boolean allowTestUser;
  @Size(max = 80)
  @Column(name = "bankKonto")
  private String bankKonto;
  @Size(max = 80)
  @Column(name = "zeitZone")
  private String zeitZone;
  @Size(max = 250)
  @Column(name = "funktion")
  private String funktion;
  @Column(name = "iconSize")
  private Integer iconSize;
  @Size(max = 45)
  @Column(name = "standort")
  private String standort;
  @Size(max = 250)
  @Column(name = "rangabzeichen")
  private String rangabzeichen;
  @Column(name = "flugzeitenFG")
  private Integer flugzeitenFG;
  @Size(max = 250)
  @Column(name = "displayname")
  private String displayname;
  @Column(name = "sound")
  private Boolean sound;
  @Column(name = "summenFensterAnzeigen")
  private Boolean summenFensterAnzeigen;
  @Column(name = "tabellenzeilen")
  private Integer tabellenzeilen;

  public Benutzer() {
  }

  public Benutzer(Integer idUser) {
    this.idUser = idUser;
  }

  public Benutzer(Integer idUser, String name1) {
    this.idUser = idUser;
    this.name1 = name1;
  }

  public Integer getIdUser() {
    return idUser;
  }

  public void setIdUser(Integer idUser) {
    this.idUser = idUser;
  }

  public Integer getFluggesellschaftManagerID() {
    return fluggesellschaftManagerID;
  }

  public void setFluggesellschaftManagerID(Integer fluggesellschaftManagerID) {
    this.fluggesellschaftManagerID = fluggesellschaftManagerID;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Date getLastlogon() {
    return lastlogon;
  }

  public void setLastlogon(Date lastlogon) {
    this.lastlogon = lastlogon;
  }

  public String getName1() {
    return name1;
  }

  public void setName1(String name1) {
    this.name1 = name1;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getMapLayer() {
    return mapLayer;
  }

  public void setMapLayer(String mapLayer) {
    this.mapLayer = mapLayer;
  }

  public Double getCashmoney() {
    return cashmoney;
  }

  public void setCashmoney(Double cashmoney) {
    this.cashmoney = cashmoney;
  }

  public String getLizenz() {
    return lizenz;
  }

  public void setLizenz(String lizenz) {
    this.lizenz = lizenz;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getDateformat() {
    return dateformat;
  }

  public void setDateformat(String dateformat) {
    this.dateformat = dateformat;
  }

  public String getBanlists() {
    return banlists;
  }

  public void setBanlists(String banlists) {
    this.banlists = banlists;
  }

  public String getReadaccesskey() {
    return readaccesskey;
  }

  public void setReadaccesskey(String readaccesskey) {
    this.readaccesskey = readaccesskey;
  }

  public String getWriteaccesskey() {
    return writeaccesskey;
  }

  public void setWriteaccesskey(String writeaccesskey) {
    this.writeaccesskey = writeaccesskey;
  }

  public String getPasswort() {
    return passwort;
  }

  public void setPasswort(String passwort) {
    this.passwort = passwort;
  }

  public String getTheme() {
    return theme;
  }

  public void setTheme(String theme) {
    this.theme = theme;
  }

  public Boolean getGesperrt() {
    return gesperrt;
  }

  public void setGesperrt(Boolean gesperrt) {
    this.gesperrt = gesperrt;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public String getRolle() {
    return rolle;
  }

  public void setRolle(String rolle) {
    this.rolle = rolle;
  }

  public Integer getFlightmiles() {
    return flightmiles;
  }

  public void setFlightmiles(Integer flightmiles) {
    this.flightmiles = flightmiles;
  }

  public Integer getFlights() {
    return flights;
  }

  public void setFlights(Integer flights) {
    this.flights = flights;
  }

  public Integer getFlighttime() {
    return flighttime;
  }

  public void setFlighttime(Integer flighttime) {
    this.flighttime = flighttime;
  }

  public Integer getFlightpaxes() {
    return flightpaxes;
  }

  public void setFlightpaxes(Integer flightpaxes) {
    this.flightpaxes = flightpaxes;
  }

  public Integer getFlightcargo() {
    return flightcargo;
  }

  public void setFlightcargo(Integer flightcargo) {
    this.flightcargo = flightcargo;
  }

  public Integer getFlightrettung() {
    return flightrettung;
  }

  public void setFlightrettung(Integer flightrettung) {
    this.flightrettung = flightrettung;
  }

  public String getSprache() {
    return sprache;
  }

  public void setSprache(String sprache) {
    this.sprache = sprache;
  }

  public Boolean getOnline() {
    return online;
  }

  public void setOnline(Boolean online) {
    this.online = online;
  }

  public String getBanner() {
    return banner;
  }

  public void setBanner(String banner) {
    this.banner = banner;
  }

  public Boolean getAllowBenutzerEdit() {
    return allowBenutzerEdit;
  }

  public void setAllowBenutzerEdit(Boolean allowBenutzerEdit) {
    this.allowBenutzerEdit = allowBenutzerEdit;
  }

  public Boolean getAllowFlugzeugeEdit() {
    return allowFlugzeugeEdit;
  }

  public void setAllowFlugzeugeEdit(Boolean allowFlugzeugeEdit) {
    this.allowFlugzeugeEdit = allowFlugzeugeEdit;
  }

  public Boolean getAllowFlughafenEdit() {
    return allowFlughafenEdit;
  }

  public void setAllowFlughafenEdit(Boolean allowFlughafenEdit) {
    this.allowFlughafenEdit = allowFlughafenEdit;
  }

  public Boolean getAllowGeschichtenEdit() {
    return allowGeschichtenEdit;
  }

  public void setAllowGeschichtenEdit(Boolean allowGeschichtenEdit) {
    this.allowGeschichtenEdit = allowGeschichtenEdit;
  }

  public Boolean getAllowNewsEdit() {
    return allowNewsEdit;
  }

  public void setAllowNewsEdit(Boolean allowNewsEdit) {
    this.allowNewsEdit = allowNewsEdit;
  }

  public Boolean getAllowToolsOpen() {
    return allowToolsOpen;
  }

  public void setAllowToolsOpen(Boolean allowToolsOpen) {
    this.allowToolsOpen = allowToolsOpen;
  }

  public Boolean getAllowAdminOpen() {
    return allowAdminOpen;
  }

  public void setAllowAdminOpen(Boolean allowAdminOpen) {
    this.allowAdminOpen = allowAdminOpen;
  }

  public Boolean getAllowRettungsstationEdit() {
    return allowRettungsstationEdit;
  }

  public void setAllowRettungsstationEdit(Boolean allowRettungsstationEdit) {
    this.allowRettungsstationEdit = allowRettungsstationEdit;
  }

  public Boolean getAllowAirportDispatcher() {
    return allowAirportDispatcher;
  }

  public void setAllowAirportDispatcher(Boolean allowAirportDispatcher) {
    this.allowAirportDispatcher = allowAirportDispatcher;
  }

  public Boolean getAllowTestUser() {
    return allowTestUser;
  }

  public void setAllowTestUser(Boolean allowTestUser) {
    this.allowTestUser = allowTestUser;
  }

  public String getBankKonto() {
    return bankKonto;
  }

  public void setBankKonto(String bankKonto) {
    this.bankKonto = bankKonto;
  }

  public String getZeitZone() {
    return zeitZone;
  }

  public void setZeitZone(String zeitZone) {
    this.zeitZone = zeitZone;
  }

  public String getFunktion() {
    return funktion;
  }

  public void setFunktion(String funktion) {
    this.funktion = funktion;
  }

  public Integer getIconSize() {
    return iconSize;
  }

  public void setIconSize(Integer iconSize) {
    this.iconSize = iconSize;
  }

  public String getStandort() {
    return standort;
  }

  public void setStandort(String standort) {
    this.standort = standort;
  }

  public String getRangabzeichen() {
    return rangabzeichen;
  }

  public void setRangabzeichen(String rangabzeichen) {
    this.rangabzeichen = rangabzeichen;
  }

  public Integer getFlugzeitenFG() {
    return flugzeitenFG;
  }

  public void setFlugzeitenFG(Integer flugzeitenFG) {
    this.flugzeitenFG = flugzeitenFG;
  }

  public String getDisplayname() {
    return displayname;
  }

  public void setDisplayname(String displayname) {
    this.displayname = displayname;
  }

  public Boolean getSound() {
    return sound;
  }

  public void setSound(Boolean sound) {
    this.sound = sound;
  }

  public Boolean getSummenFensterAnzeigen() {
    return summenFensterAnzeigen;
  }

  public void setSummenFensterAnzeigen(Boolean summenFensterAnzeigen) {
    this.summenFensterAnzeigen = summenFensterAnzeigen;
  }

  public Integer getTabellenzeilen() {
    return tabellenzeilen;
  }

  public void setTabellenzeilen(Integer tabellenzeilen) {
    this.tabellenzeilen = tabellenzeilen;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idUser != null ? idUser.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Benutzer)) {
      return false;
    }
    Benutzer other = (Benutzer) object;
    if ((this.idUser == null && other.idUser != null) || (this.idUser != null && !this.idUser.equals(other.idUser))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Benutzer[ idUser=" + idUser + " ]";
  }
  
}
