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
@Table(name = "viewKostenstellenAuswertung")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ViewKostenstellenAuswertung.findAll", query = "SELECT v FROM ViewKostenstellenAuswertung v")})
public class ViewKostenstellenAuswertung implements Serializable {

  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 80)
  @Column(name = "bankKonto")
  private String bankKonto;

  private static final long serialVersionUID = 1L;
  @Column(name = "kostenstelle")
  private Integer kostenstelle;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "betrag")
  private Double betrag;
  @Size(max = 250)
  @Column(name = "bezeichnung")
  private String bezeichnung;
  @Column(name = "fluggesellschaftID")
  private Integer fluggesellschaftID;
  @Size(max = 10)
  @Column(name = "datumFormatiert")
  private String datumFormatiert;
  @Basic(optional = false)
  @NotNull
  @Column(name = "primanota")
  @Id
  private int primanota;
  @Column(name = "ueberweisungsDatum")
  @Temporal(TemporalType.TIMESTAMP)
  private Date ueberweisungsDatum;
  @Column(name="quartal")
  private String quartal;
  @Column(name="monatlich")
  private String monatlich;

    

   
  

  public ViewKostenstellenAuswertung() {
  }

  public Integer getKostenstelle() {
    return kostenstelle;
  }

  public void setKostenstelle(Integer kostenstelle) {
    this.kostenstelle = kostenstelle;
  }

  public Double getBetrag() {
    return betrag;
  }

  public void setBetrag(Double betrag) {
    this.betrag = betrag;
  }

  public String getBezeichnung() {
    return bezeichnung;
  }

  public void setBezeichnung(String bezeichnung) {
    this.bezeichnung = bezeichnung;
  }

  public Integer getFluggesellschaftID() {
    return fluggesellschaftID;
  }

  public void setFluggesellschaftID(Integer fluggesellschaftID) {
    this.fluggesellschaftID = fluggesellschaftID;
  }

  public String getDatumFormatiert() {
    return datumFormatiert;
  }

  public void setDatumFormatiert(String datumFormatiert) {
    this.datumFormatiert = datumFormatiert;
  }

  public int getPrimanota() {
    return primanota;
  }

  public void setPrimanota(int primanota) {
    this.primanota = primanota;
  }

  public Date getUeberweisungsDatum() {
    return ueberweisungsDatum;
  }

  public void setUeberweisungsDatum(Date ueberweisungsDatum) {
    this.ueberweisungsDatum = ueberweisungsDatum;
  }
  
  public String getQuartal() {
    return quartal;
  }

  public void setQuartal(String quartal) {
    this.quartal = quartal;
  }
  
  public String getMonatlich() {
        return monatlich;
    }

    public void setMonatlich(String monatlich) {
        this.monatlich = monatlich;
    }

  public String getBankKonto() {
    return bankKonto;
  }

  public void setBankKonto(String bankKonto) {
    this.bankKonto = bankKonto;
  }

}
