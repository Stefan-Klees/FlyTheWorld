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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "sitzkonfiguration")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Sitzkonfiguration.findAll", query = "SELECT s FROM Sitzkonfiguration s")})
public class Sitzkonfiguration implements Serializable {

  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "faktor")
  private Double faktor;

  @Column(name = "dow")
  private Integer dow;
  @Column(name = "tankkapazitaet")
  private Integer tankkapazitaet;

  @Column(name = "maxPayload")
  private Integer maxPayload;

  @Column(name = "flugbegleiter")
  private Integer flugbegleiter;
  @Column(name = "crew")
  private Integer crew;

  @Column(name = "cargo")
  private Integer cargo;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idsitzKonfiguration")
  private Integer idsitzKonfiguration;
  @Column(name = "idFlugzeug")
  private Integer idFlugzeug;
  @Size(max = 250)
  @Column(name = "bezeichnung")
  private String bezeichnung;
  @Size(max = 250)
  @Column(name = "bildURL")
  private String bildURL;
  @Column(name = "abzugNettoGewicht")
  private Integer abzugNettoGewicht;
  @Column(name = "zuschlagNettoGewicht")
  private Integer zuschlagNettoGewicht;
  @Column(name = "umbauzeitMinuten")
  private Integer umbauzeitMinuten;
  @Column(name = "sitzeEconomy")
  private Integer sitzeEconomy;
  @Column(name = "sitzeBusiness")
  private Integer sitzeBusiness;
  @Column(name = "zusatzTank")
  private Boolean zusatzTank;
  @Column(name = "festerUmbau")
  private Boolean festerUmbau;
  @Column(name = "preis")
  private Integer preis;

  public Sitzkonfiguration() {
  }

  public Sitzkonfiguration(Integer idsitzKonfiguration) {
    this.idsitzKonfiguration = idsitzKonfiguration;
  }

  public Integer getIdsitzKonfiguration() {
    return idsitzKonfiguration;
  }

  public void setIdsitzKonfiguration(Integer idsitzKonfiguration) {
    this.idsitzKonfiguration = idsitzKonfiguration;
  }

  public Integer getIdFlugzeug() {
    return idFlugzeug;
  }

  public void setIdFlugzeug(Integer idFlugzeug) {
    this.idFlugzeug = idFlugzeug;
  }

  public String getBezeichnung() {
    return bezeichnung;
  }

  public void setBezeichnung(String bezeichnung) {
    this.bezeichnung = bezeichnung;
  }

  public String getBildURL() {
    return bildURL;
  }

  public void setBildURL(String bildURL) {
    this.bildURL = bildURL;
  }

  public Integer getAbzugNettoGewicht() {
    return abzugNettoGewicht;
  }

  public void setAbzugNettoGewicht(Integer abzugNettoGewicht) {
    this.abzugNettoGewicht = abzugNettoGewicht;
  }

  public Integer getZuschlagNettoGewicht() {
    return zuschlagNettoGewicht;
  }

  public void setZuschlagNettoGewicht(Integer zuschlagNettoGewicht) {
    this.zuschlagNettoGewicht = zuschlagNettoGewicht;
  }

  public Integer getUmbauzeitMinuten() {
    return umbauzeitMinuten;
  }

  public void setUmbauzeitMinuten(Integer umbauzeitMinuten) {
    this.umbauzeitMinuten = umbauzeitMinuten;
  }

  public Integer getSitzeEconomy() {
    return sitzeEconomy;
  }

  public void setSitzeEconomy(Integer sitzeEconomy) {
    this.sitzeEconomy = sitzeEconomy;
  }

  public Integer getSitzeBusiness() {
    return sitzeBusiness;
  }

  public void setSitzeBusiness(Integer sitzeBusiness) {
    this.sitzeBusiness = sitzeBusiness;
  }

  public Boolean getZusatzTank() {
    return zusatzTank;
  }

  public void setZusatzTank(Boolean zusatzTank) {
    this.zusatzTank = zusatzTank;
  }

  public Boolean getFesterUmbau() {
    return festerUmbau;
  }

  public void setFesterUmbau(Boolean festerUmbau) {
    this.festerUmbau = festerUmbau;
  }

  public Integer getPreis() {
    return preis;
  }

  public void setPreis(Integer preis) {
    this.preis = preis;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idsitzKonfiguration != null ? idsitzKonfiguration.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Sitzkonfiguration)) {
      return false;
    }
    Sitzkonfiguration other = (Sitzkonfiguration) object;
    if ((this.idsitzKonfiguration == null && other.idsitzKonfiguration != null) || (this.idsitzKonfiguration != null && !this.idsitzKonfiguration.equals(other.idsitzKonfiguration))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Sitzkonfiguration[ idsitzKonfiguration=" + idsitzKonfiguration + " ]";
  }

  public Integer getCargo() {
    return cargo;
  }

  public void setCargo(Integer cargo) {
    this.cargo = cargo;
  }

  public Integer getFlugbegleiter() {
    return flugbegleiter;
  }

  public void setFlugbegleiter(Integer flugbegleiter) {
    this.flugbegleiter = flugbegleiter;
  }

  public Integer getCrew() {
    return crew;
  }

  public void setCrew(Integer crew) {
    this.crew = crew;
  }

  public Integer getMaxPayload() {
    return maxPayload;
  }

  public void setMaxPayload(Integer maxPayload) {
    this.maxPayload = maxPayload;
  }

  public Integer getDow() {
    return dow;
  }

  public void setDow(Integer dow) {
    this.dow = dow;
  }

  public Integer getTankkapazitaet() {
    return tankkapazitaet;
  }

  public void setTankkapazitaet(Integer tankkapazitaet) {
    this.tankkapazitaet = tankkapazitaet;
  }

  public Double getFaktor() {
    return faktor;
  }

  public void setFaktor(Double faktor) {
    this.faktor = faktor;
  }
  
}
