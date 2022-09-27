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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Stefan Klees
 */
@Entity
@Table(name = "Flugzeuge_fluggesellschaft")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Flugzeugefluggesellschaft.findAll", query = "SELECT f FROM Flugzeugefluggesellschaft f")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByIdFluggesellschaftFlugzeug", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.idFluggesellschaftFlugzeug = :idFluggesellschaftFlugzeug")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByIdFlugzeug", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.idFlugzeug = :idFlugzeug")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByFlugzeugBesitzer", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.flugzeugBesitzer = :flugzeugBesitzer")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByRegistrierung", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.registrierung = :registrierung")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByHeimatICAO", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.heimatICAO = :heimatICAO")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByAktuellePositionICAO", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.aktuellePositionICAO = :aktuellePositionICAO")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByIstGesperrt", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.istGesperrt = :istGesperrt")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByIstGesperrtSeit", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.istGesperrtSeit = :istGesperrtSeit")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByMietpreisTrocken", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.mietpreisTrocken = :mietpreisTrocken")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByMietpreisNass", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.mietpreisNass = :mietpreisNass")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByAktuelleTankfuellung", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.aktuelleTankfuellung = :aktuelleTankfuellung")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByVerkaufsPreis", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.verkaufsPreis = :verkaufsPreis")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByIstPrivatverkauf", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.istPrivatverkauf = :istPrivatverkauf")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByVerkaufAnUserID", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.verkaufAnUserID = :verkaufAnUserID")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByMaxMietzeitStunden", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.maxMietzeitStunden = :maxMietzeitStunden")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByAbgeflogenVonICAO", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.abgeflogenVonICAO = :abgeflogenVonICAO")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByBonusFuerRueckfuehrung", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.bonusFuerRueckfuehrung = :bonusFuerRueckfuehrung")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByLeasinggeberUserID", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.leasinggeberUserID = :leasinggeberUserID")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByLetzterCheckMinuten", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.letzterCheckMinuten = :letzterCheckMinuten")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByGesamtMaschinenLaufzeitMinuten", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.gesamtMaschinenLaufzeitMinuten = :gesamtMaschinenLaufzeitMinuten")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByGesamtAlterDesFlugzeugsMinuten", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.gesamtAlterDesFlugzeugsMinuten = :gesamtAlterDesFlugzeugsMinuten")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByZustandDesFlugzeugs", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.zustandDesFlugzeugs = :zustandDesFlugzeugs")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByIstCheckDurchUserErlaubt", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.istCheckDurchUserErlaubt = :istCheckDurchUserErlaubt")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByIstMietbar", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.istMietbar = :istMietbar")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByIstZuVerkaufen", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.istZuVerkaufen = :istZuVerkaufen")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByIdUserDerFlugzeugGesperrtHat", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.idUserDerFlugzeugGesperrtHat = :idUserDerFlugzeugGesperrtHat")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByGesamtFlugzeit", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.gesamtFlugzeit = :gesamtFlugzeit")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByGesamtEntfernung", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.gesamtEntfernung = :gesamtEntfernung")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByGesamtFluege", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.gesamtFluege = :gesamtFluege")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByGesamtTransportiertePassagiere", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.gesamtTransportiertePassagiere = :gesamtTransportiertePassagiere")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByGesamtTransportiertesCargo", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.gesamtTransportiertesCargo = :gesamtTransportiertesCargo")
  , @NamedQuery(name = "Flugzeugefluggesellschaft.findByIsAktive", query = "SELECT f FROM Flugzeugefluggesellschaft f WHERE f.isAktive = :isAktive")})
public class Flugzeugefluggesellschaft implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idFluggesellschaftFlugzeug")
  private Integer idFluggesellschaftFlugzeug;
  @Column(name = "idFlugzeug")
  private Integer idFlugzeug;
  @Column(name = "flugzeugBesitzer")
  private Integer flugzeugBesitzer;
  @Size(max = 45)
  @Column(name = "registrierung")
  private String registrierung;
  @Size(max = 25)
  @Column(name = "heimatICAO")
  private String heimatICAO;
  @Size(max = 25)
  @Column(name = "aktuellePositionICAO")
  private String aktuellePositionICAO;
  @Column(name = "istGesperrt")
  private Boolean istGesperrt;
  @Column(name = "istGesperrtSeit")
  @Temporal(TemporalType.TIMESTAMP)
  private Date istGesperrtSeit;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "mietpreisTrocken")
  private Double mietpreisTrocken;
  @Column(name = "mietpreisNass")
  private Double mietpreisNass;
  @Column(name = "aktuelleTankfuellung")
  private Integer aktuelleTankfuellung;
  @Column(name = "verkaufsPreis")
  private Double verkaufsPreis;
  @Column(name = "istPrivatverkauf")
  private Boolean istPrivatverkauf;
  @Column(name = "verkaufAnUserID")
  private Integer verkaufAnUserID;
  @Column(name = "maxMietzeitStunden")
  private Integer maxMietzeitStunden;
  @Size(max = 250)
  @Column(name = "abgeflogenVonICAO")
  private String abgeflogenVonICAO;
  @Column(name = "bonusFuerRueckfuehrung")
  private Double bonusFuerRueckfuehrung;
  @Column(name = "leasinggeberUserID")
  private Integer leasinggeberUserID;
  @Column(name = "letzterCheckMinuten")
  private Integer letzterCheckMinuten;
  @Column(name = "gesamtMaschinenLaufzeitMinuten")
  private Integer gesamtMaschinenLaufzeitMinuten;
  @Column(name = "gesamtAlterDesFlugzeugsMinuten")
  private Integer gesamtAlterDesFlugzeugsMinuten;
  @Column(name = "zustandDesFlugzeugs")
  private Integer zustandDesFlugzeugs;
  @Column(name = "istCheckDurchUserErlaubt")
  private Boolean istCheckDurchUserErlaubt;
  @Column(name = "istMietbar")
  private Boolean istMietbar;
  @Column(name = "istZuVerkaufen")
  private Boolean istZuVerkaufen;
  @Column(name = "idUserDerFlugzeugGesperrtHat")
  private Integer idUserDerFlugzeugGesperrtHat;
  @Column(name = "gesamtFlugzeit")
  private Integer gesamtFlugzeit;
  @Column(name = "gesamtEntfernung")
  private Integer gesamtEntfernung;
  @Column(name = "gesamtFluege")
  private Integer gesamtFluege;
  @Column(name = "gesamtTransportiertePassagiere")
  private Integer gesamtTransportiertePassagiere;
  @Column(name = "gesamtTransportiertesCargo")
  private Integer gesamtTransportiertesCargo;
  @Column(name = "isAktive")
  private Boolean isAktive;

  public Flugzeugefluggesellschaft() {
  }

  public Flugzeugefluggesellschaft(Integer idFluggesellschaftFlugzeug) {
    this.idFluggesellschaftFlugzeug = idFluggesellschaftFlugzeug;
  }

  public Integer getIdFluggesellschaftFlugzeug() {
    return idFluggesellschaftFlugzeug;
  }

  public void setIdFluggesellschaftFlugzeug(Integer idFluggesellschaftFlugzeug) {
    this.idFluggesellschaftFlugzeug = idFluggesellschaftFlugzeug;
  }

  public Integer getIdFlugzeug() {
    return idFlugzeug;
  }

  public void setIdFlugzeug(Integer idFlugzeug) {
    this.idFlugzeug = idFlugzeug;
  }

  public Integer getFlugzeugBesitzer() {
    return flugzeugBesitzer;
  }

  public void setFlugzeugBesitzer(Integer flugzeugBesitzer) {
    this.flugzeugBesitzer = flugzeugBesitzer;
  }

  public String getRegistrierung() {
    return registrierung;
  }

  public void setRegistrierung(String registrierung) {
    this.registrierung = registrierung;
  }

  public String getHeimatICAO() {
    return heimatICAO;
  }

  public void setHeimatICAO(String heimatICAO) {
    this.heimatICAO = heimatICAO;
  }

  public String getAktuellePositionICAO() {
    return aktuellePositionICAO;
  }

  public void setAktuellePositionICAO(String aktuellePositionICAO) {
    this.aktuellePositionICAO = aktuellePositionICAO;
  }

  public Boolean getIstGesperrt() {
    return istGesperrt;
  }

  public void setIstGesperrt(Boolean istGesperrt) {
    this.istGesperrt = istGesperrt;
  }

  public Date getIstGesperrtSeit() {
    return istGesperrtSeit;
  }

  public void setIstGesperrtSeit(Date istGesperrtSeit) {
    this.istGesperrtSeit = istGesperrtSeit;
  }

  public Double getMietpreisTrocken() {
    return mietpreisTrocken;
  }

  public void setMietpreisTrocken(Double mietpreisTrocken) {
    this.mietpreisTrocken = mietpreisTrocken;
  }

  public Double getMietpreisNass() {
    return mietpreisNass;
  }

  public void setMietpreisNass(Double mietpreisNass) {
    this.mietpreisNass = mietpreisNass;
  }

  public Integer getAktuelleTankfuellung() {
    return aktuelleTankfuellung;
  }

  public void setAktuelleTankfuellung(Integer aktuelleTankfuellung) {
    this.aktuelleTankfuellung = aktuelleTankfuellung;
  }

  public Double getVerkaufsPreis() {
    return verkaufsPreis;
  }

  public void setVerkaufsPreis(Double verkaufsPreis) {
    this.verkaufsPreis = verkaufsPreis;
  }

  public Boolean getIstPrivatverkauf() {
    return istPrivatverkauf;
  }

  public void setIstPrivatverkauf(Boolean istPrivatverkauf) {
    this.istPrivatverkauf = istPrivatverkauf;
  }

  public Integer getVerkaufAnUserID() {
    return verkaufAnUserID;
  }

  public void setVerkaufAnUserID(Integer verkaufAnUserID) {
    this.verkaufAnUserID = verkaufAnUserID;
  }

  public Integer getMaxMietzeitStunden() {
    return maxMietzeitStunden;
  }

  public void setMaxMietzeitStunden(Integer maxMietzeitStunden) {
    this.maxMietzeitStunden = maxMietzeitStunden;
  }

  public String getAbgeflogenVonICAO() {
    return abgeflogenVonICAO;
  }

  public void setAbgeflogenVonICAO(String abgeflogenVonICAO) {
    this.abgeflogenVonICAO = abgeflogenVonICAO;
  }

  public Double getBonusFuerRueckfuehrung() {
    return bonusFuerRueckfuehrung;
  }

  public void setBonusFuerRueckfuehrung(Double bonusFuerRueckfuehrung) {
    this.bonusFuerRueckfuehrung = bonusFuerRueckfuehrung;
  }

  public Integer getLeasinggeberUserID() {
    return leasinggeberUserID;
  }

  public void setLeasinggeberUserID(Integer leasinggeberUserID) {
    this.leasinggeberUserID = leasinggeberUserID;
  }

  public Integer getLetzterCheckMinuten() {
    return letzterCheckMinuten;
  }

  public void setLetzterCheckMinuten(Integer letzterCheckMinuten) {
    this.letzterCheckMinuten = letzterCheckMinuten;
  }

  public Integer getGesamtMaschinenLaufzeitMinuten() {
    return gesamtMaschinenLaufzeitMinuten;
  }

  public void setGesamtMaschinenLaufzeitMinuten(Integer gesamtMaschinenLaufzeitMinuten) {
    this.gesamtMaschinenLaufzeitMinuten = gesamtMaschinenLaufzeitMinuten;
  }

  public Integer getGesamtAlterDesFlugzeugsMinuten() {
    return gesamtAlterDesFlugzeugsMinuten;
  }

  public void setGesamtAlterDesFlugzeugsMinuten(Integer gesamtAlterDesFlugzeugsMinuten) {
    this.gesamtAlterDesFlugzeugsMinuten = gesamtAlterDesFlugzeugsMinuten;
  }

  public Integer getZustandDesFlugzeugs() {
    return zustandDesFlugzeugs;
  }

  public void setZustandDesFlugzeugs(Integer zustandDesFlugzeugs) {
    this.zustandDesFlugzeugs = zustandDesFlugzeugs;
  }

  public Boolean getIstCheckDurchUserErlaubt() {
    return istCheckDurchUserErlaubt;
  }

  public void setIstCheckDurchUserErlaubt(Boolean istCheckDurchUserErlaubt) {
    this.istCheckDurchUserErlaubt = istCheckDurchUserErlaubt;
  }

  public Boolean getIstMietbar() {
    return istMietbar;
  }

  public void setIstMietbar(Boolean istMietbar) {
    this.istMietbar = istMietbar;
  }

  public Boolean getIstZuVerkaufen() {
    return istZuVerkaufen;
  }

  public void setIstZuVerkaufen(Boolean istZuVerkaufen) {
    this.istZuVerkaufen = istZuVerkaufen;
  }

  public Integer getIdUserDerFlugzeugGesperrtHat() {
    return idUserDerFlugzeugGesperrtHat;
  }

  public void setIdUserDerFlugzeugGesperrtHat(Integer idUserDerFlugzeugGesperrtHat) {
    this.idUserDerFlugzeugGesperrtHat = idUserDerFlugzeugGesperrtHat;
  }

  public Integer getGesamtFlugzeit() {
    return gesamtFlugzeit;
  }

  public void setGesamtFlugzeit(Integer gesamtFlugzeit) {
    this.gesamtFlugzeit = gesamtFlugzeit;
  }

  public Integer getGesamtEntfernung() {
    return gesamtEntfernung;
  }

  public void setGesamtEntfernung(Integer gesamtEntfernung) {
    this.gesamtEntfernung = gesamtEntfernung;
  }

  public Integer getGesamtFluege() {
    return gesamtFluege;
  }

  public void setGesamtFluege(Integer gesamtFluege) {
    this.gesamtFluege = gesamtFluege;
  }

  public Integer getGesamtTransportiertePassagiere() {
    return gesamtTransportiertePassagiere;
  }

  public void setGesamtTransportiertePassagiere(Integer gesamtTransportiertePassagiere) {
    this.gesamtTransportiertePassagiere = gesamtTransportiertePassagiere;
  }

  public Integer getGesamtTransportiertesCargo() {
    return gesamtTransportiertesCargo;
  }

  public void setGesamtTransportiertesCargo(Integer gesamtTransportiertesCargo) {
    this.gesamtTransportiertesCargo = gesamtTransportiertesCargo;
  }

  public Boolean getIsAktive() {
    return isAktive;
  }

  public void setIsAktive(Boolean isAktive) {
    this.isAktive = isAktive;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idFluggesellschaftFlugzeug != null ? idFluggesellschaftFlugzeug.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Flugzeugefluggesellschaft)) {
      return false;
    }
    Flugzeugefluggesellschaft other = (Flugzeugefluggesellschaft) object;
    if ((this.idFluggesellschaftFlugzeug == null && other.idFluggesellschaftFlugzeug != null) || (this.idFluggesellschaftFlugzeug != null && !this.idFluggesellschaftFlugzeug.equals(other.idFluggesellschaftFlugzeug))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Flugzeugefluggesellschaft[ idFluggesellschaftFlugzeug=" + idFluggesellschaftFlugzeug + " ]";
  }
  
}
