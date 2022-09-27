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
@Table(name = "Aircraft")
@XmlRootElement
@NamedQueries({

  @NamedQuery(name = "Aircraft.findBySystemflugzeugAndModel", query = "SELECT a FROM Aircraft a "
          + "WHERE a.systemflugzeug = :systemflugzeug "
          + "and a.model LIKE :model "
          + " order by a.model "),
  
  @NamedQuery(name = "Aircraft.findByModel", query = "SELECT a FROM Aircraft a WHERE a.model LIKE :model order by a.model"),

  
  @NamedQuery(name = "Aircraft.findByAirline", query = "SELECT a FROM Aircraft a WHERE a.model LIKE :model and a.idAirline = :idAirline order by a.model"),

  
  @NamedQuery(name = "Aircraft.findByOwnerofaircraft", query = "SELECT a FROM Aircraft a "
          + "WHERE a.ownerofaircraft = :ownerofaircraft and "
          + "a.model LIKE :model order by a.model "),
  
  @NamedQuery(name = "Aircraft.findByForsale", query = "SELECT a FROM Aircraft a "
          + "WHERE a.forsale = :forsale and "
          + "a.model LIKE :model order by a.model "),
  
  @NamedQuery(name = "Aircraft.findByMyRentedAircraftsOnTakeoffIcao", query = "SELECT a FROM Aircraft a "
          + "WHERE a.location = :location "
          + "and a.userlock = :userlock "
          + "and a.iduserforuserlock = :iduserforuserlock"),


  @NamedQuery(name = "Aircraft.findAllMyRentedAircrafts", query = "SELECT a FROM Aircraft a "
          + "WHERE a.userlock = :userlock "
          + "and a.iduserforuserlock = :iduserforuserlock"),

  
  @NamedQuery(name = "Aircraft.findByLocationRentable", query = "SELECT a FROM Aircraft a WHERE a.location = :location "
          + "and a.userlock = :userlock "
          + "and a.rentable = :rentable"),  
  
  @NamedQuery(name = "Aircraft.findByRentable", query = "SELECT a FROM Aircraft a WHERE a.rentable = :rentable "
          + "and a.iduserforuserlock = :iduserforuserlock "
          + "and a.location LIKE :location order by a.model "),
  
  
  
  @NamedQuery(name = "Aircraft.findAll", query = "SELECT a FROM Aircraft a"),
  @NamedQuery(name = "Aircraft.findByIdAircraft", query = "SELECT a FROM Aircraft a WHERE a.idAircraft = :idAircraft"),
  @NamedQuery(name = "Aircraft.findByRegistration", query = "SELECT a FROM Aircraft a WHERE a.registration = :registration"),
  @NamedQuery(name = "Aircraft.findByHome", query = "SELECT a FROM Aircraft a WHERE a.home = :home"),
  @NamedQuery(name = "Aircraft.findByLocation", query = "SELECT a FROM Aircraft a WHERE a.location = :location"),
  @NamedQuery(name = "Aircraft.findByMake", query = "SELECT a FROM Aircraft a WHERE a.make = :make"),

  @NamedQuery(name = "Aircraft.findByUserlock", query = "SELECT a FROM Aircraft a WHERE a.userlock = :userlock"),
  @NamedQuery(name = "Aircraft.findByModelid", query = "SELECT a FROM Aircraft a WHERE a.modelid = :modelid"),
  @NamedQuery(name = "Aircraft.findBySeats", query = "SELECT a FROM Aircraft a WHERE a.seats = :seats"),
  @NamedQuery(name = "Aircraft.findByLockedsince", query = "SELECT a FROM Aircraft a WHERE a.lockedsince = :lockedsince"),
  @NamedQuery(name = "Aircraft.findByRentalpricedry", query = "SELECT a FROM Aircraft a WHERE a.rentalpricedry = :rentalpricedry"),
  @NamedQuery(name = "Aircraft.findByRentalpricewet", query = "SELECT a FROM Aircraft a WHERE a.rentalpricewet = :rentalpricewet"),
  @NamedQuery(name = "Aircraft.findByInitialfuel", query = "SELECT a FROM Aircraft a WHERE a.initialfuel = :initialfuel"),
  @NamedQuery(name = "Aircraft.findBySellprice", query = "SELECT a FROM Aircraft a WHERE a.sellprice = :sellprice"),
  @NamedQuery(name = "Aircraft.findByPrivatesale", query = "SELECT a FROM Aircraft a WHERE a.privatesale = :privatesale"),
  @NamedQuery(name = "Aircraft.findBySelltoid", query = "SELECT a FROM Aircraft a WHERE a.selltoid = :selltoid"),
  @NamedQuery(name = "Aircraft.findByMaxrenttime", query = "SELECT a FROM Aircraft a WHERE a.maxrenttime = :maxrenttime"),
  @NamedQuery(name = "Aircraft.findByDepartedfrom", query = "SELECT a FROM Aircraft a WHERE a.departedfrom = :departedfrom"),
  @NamedQuery(name = "Aircraft.findByBonus", query = "SELECT a FROM Aircraft a WHERE a.bonus = :bonus"),

  @NamedQuery(name = "Aircraft.findByLessor", query = "SELECT a FROM Aircraft a WHERE a.lessor = :lessor"),
  @NamedQuery(name = "Aircraft.findByEquipment", query = "SELECT a FROM Aircraft a WHERE a.equipment = :equipment"),
  @NamedQuery(name = "Aircraft.findByModelprice", query = "SELECT a FROM Aircraft a WHERE a.modelprice = :modelprice"),
  @NamedQuery(name = "Aircraft.findByMaxweight", query = "SELECT a FROM Aircraft a WHERE a.maxweight = :maxweight"),
  @NamedQuery(name = "Aircraft.findByEmptyweight", query = "SELECT a FROM Aircraft a WHERE a.emptyweight = :emptyweight"),
  @NamedQuery(name = "Aircraft.findByLastcheck", query = "SELECT a FROM Aircraft a WHERE a.lastcheck = :lastcheck"),
  @NamedQuery(name = "Aircraft.findByTotalenginetime", query = "SELECT a FROM Aircraft a WHERE a.totalenginetime = :totalenginetime"),
  @NamedQuery(name = "Aircraft.findByEngines", query = "SELECT a FROM Aircraft a WHERE a.engines = :engines"),
  @NamedQuery(name = "Aircraft.findByEngineprice", query = "SELECT a FROM Aircraft a WHERE a.engineprice = :engineprice"),
  @NamedQuery(name = "Aircraft.findByGph", query = "SELECT a FROM Aircraft a WHERE a.gph = :gph"),
  @NamedQuery(name = "Aircraft.findByCruise", query = "SELECT a FROM Aircraft a WHERE a.cruise = :cruise"),
  @NamedQuery(name = "Aircraft.findByAdvertise", query = "SELECT a FROM Aircraft a WHERE a.advertise = :advertise"),
  @NamedQuery(name = "Aircraft.findByCrew", query = "SELECT a FROM Aircraft a WHERE a.crew = :crew"),
  @NamedQuery(name = "Aircraft.findByAirframe", query = "SELECT a FROM Aircraft a WHERE a.airframe = :airframe"),
  @NamedQuery(name = "Aircraft.findByAircraftcondition", query = "SELECT a FROM Aircraft a WHERE a.aircraftcondition = :aircraftcondition"),
  @NamedQuery(name = "Aircraft.findByAllowfix", query = "SELECT a FROM Aircraft a WHERE a.allowfix = :allowfix"),
  @NamedQuery(name = "Aircraft.findByLastfix", query = "SELECT a FROM Aircraft a WHERE a.lastfix = :lastfix"),
  @NamedQuery(name = "Aircraft.findByFuelltype", query = "SELECT a FROM Aircraft a WHERE a.fuelltype = :fuelltype"),
  @NamedQuery(name = "Aircraft.findBySystemflugzeug", query = "SELECT a FROM Aircraft a WHERE a.systemflugzeug = :systemflugzeug"),
  

  @NamedQuery(name = "Aircraft.findByIduserforuserlock", query = "SELECT a FROM Aircraft a WHERE a.iduserforuserlock = :iduserforuserlock")})
public class Aircraft implements Serializable {

  @Column(name = "isBusinessClass")
  private Boolean isBusinessClass;

  @Column(name = "SeatsBusinessClass")
  private Integer seatsBusinessClass;

  @Size(max = 250)
  @Column(name = "fsxDownloadUrl")
  private String fsxDownloadUrl;

  @Size(max = 250)
  @Column(name = "xplaneDownloadUrl")
  private String xplaneDownloadUrl;
  @Size(max = 250)
  @Column(name = "p3dDownloadUrl")
  private String p3dDownloadUrl;
  @Size(max = 250)
  @Column(name = "payware1DownloadUrl")
  private String payware1DownloadUrl;
  @Size(max = 250)
  @Column(name = "payware2DownloadUrl")
  private String payware2DownloadUrl;
  @Size(max = 250)
  @Column(name = "payware3DownloadUrl")
  private String payware3DownloadUrl;
  @Size(max = 250)
  @Column(name = "picture1Url")
  private String picture1Url;
  @Size(max = 250)
  @Column(name = "picture2Url")
  private String picture2Url;
  @Size(max = 250)
  @Column(name = "picture3Url")
  private String picture3Url;

  @Column(name = "lizenz")
  private Integer lizenz;

  @Column(name = "allowfix")
  private Boolean allowfix;

  @Column(name = "userlock")
  private Boolean userlock;
  @Column(name = "privatesale")
  private Boolean privatesale;
  @Column(name = "rentable")
  private Boolean rentable;
  @Column(name = "forsale")
  private Boolean forsale;

  @Column(name = "systemflugzeug")
  private Boolean systemflugzeug;

  @Column(name = "idAirline")
  private Integer idAirline;

  @Column(name = "flighttime")
  private Integer flighttime;
  @Column(name = "flightmiles")
  private Integer flightmiles;
  @Column(name = "flights")
  private Integer flights;
  @Column(name = "flightpaxes")
  private Integer flightpaxes;
  @Column(name = "flightcargo")
  private Integer flightcargo;

  @Column(name = "fuelconsumption")
  private Integer fuelconsumption;

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idAircraft")
  private Integer idAircraft;
  @Size(max = 45)
  @Column(name = "registration")
  private String registration;
  @Size(max = 25)
  @Column(name = "home")
  private String home;
  @Size(max = 25)
  @Column(name = "location")
  private String location;
  @Size(max = 250)
  @Column(name = "make")
  private String make;
  @Size(max = 250)
  @Column(name = "model")
  private String model;
  @Column(name = "modelid")
  private Integer modelid;
  @Column(name = "seats")
  private Integer seats;
  @Column(name = "lockedsince")
  @Temporal(TemporalType.TIMESTAMP)
  private Date lockedsince;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "rentalpricedry")
  private Double rentalpricedry;
  @Column(name = "rentalpricewet")
  private Double rentalpricewet;
  @Column(name = "initialfuel")
  private Integer initialfuel;
  @Column(name = "sellprice")
  private Double sellprice;
  @Column(name = "selltoid")
  private Integer selltoid;
  @Column(name = "maxrenttime")
  private Integer maxrenttime;
  @Size(max = 250)
  @Column(name = "departedfrom")
  private String departedfrom;
  @Column(name = "bonus")
  private Double bonus;
  @Column(name = "ownerofaircraft")
  private Integer ownerofaircraft;
  @Column(name = "lessor")
  private Integer lessor;
  @Column(name = "equipment")
  private Integer equipment;
  @Column(name = "modelprice")
  private Double modelprice;
  @Column(name = "maxweight")
  private Integer maxweight;
  @Column(name = "emptyweight")
  private Integer emptyweight;
  @Column(name = "lastcheck")
  private Integer lastcheck;
  @Column(name = "totalenginetime")
  private Integer totalenginetime;
  @Column(name = "engines")
  private Integer engines;
  @Column(name = "engineprice")
  private Double engineprice;
  @Column(name = "gph")
  private Integer gph;
  @Column(name = "cruise")
  private Integer cruise;
  @Column(name = "advertise")
  private Integer advertise;
  @Column(name = "crew")
  private Integer crew;
  @Column(name = "airframe")
  private Integer airframe;
  @Column(name = "aircraftcondition")
  private Integer aircraftcondition;
  @Column(name = "lastfix")
  private Integer lastfix;
  @Column(name = "fuelltype")
  private Integer fuelltype;
  @Column(name = "iduserforuserlock")
  private Integer iduserforuserlock;

  public Aircraft() {
  }

  public Aircraft(Integer idAircraft) {
    this.idAircraft = idAircraft;
  }

  public Integer getIdAircraft() {
    return idAircraft;
  }

  public void setIdAircraft(Integer idAircraft) {
    this.idAircraft = idAircraft;
  }

  public String getRegistration() {
    return registration;
  }

  public void setRegistration(String registration) {
    this.registration = registration;
  }

  public String getHome() {
    return home;
  }

  public void setHome(String home) {
    this.home = home;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getMake() {
    return make;
  }

  public void setMake(String make) {
    this.make = make;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }


  public Integer getModelid() {
    return modelid;
  }

  public void setModelid(Integer modelid) {
    this.modelid = modelid;
  }

  public Integer getSeats() {
    return seats;
  }

  public void setSeats(Integer seats) {
    this.seats = seats;
  }

  public Date getLockedsince() {
    return lockedsince;
  }

  public void setLockedsince(Date lockedsince) {
    this.lockedsince = lockedsince;
  }

  public Double getRentalpricedry() {
    return rentalpricedry;
  }

  public void setRentalpricedry(Double rentalpricedry) {
    this.rentalpricedry = rentalpricedry;
  }

  public Double getRentalpricewet() {
    return rentalpricewet;
  }

  public void setRentalpricewet(Double rentalpricewet) {
    this.rentalpricewet = rentalpricewet;
  }

  public Integer getInitialfuel() {
    return initialfuel;
  }

  public void setInitialfuel(Integer initialfuel) {
    this.initialfuel = initialfuel;
  }

  public Double getSellprice() {
    return sellprice;
  }

  public void setSellprice(Double sellprice) {
    this.sellprice = sellprice;
  }


  public Integer getSelltoid() {
    return selltoid;
  }

  public void setSelltoid(Integer selltoid) {
    this.selltoid = selltoid;
  }

  public Integer getMaxrenttime() {
    return maxrenttime;
  }

  public void setMaxrenttime(Integer maxrenttime) {
    this.maxrenttime = maxrenttime;
  }

  public String getDepartedfrom() {
    return departedfrom;
  }

  public void setDepartedfrom(String departedfrom) {
    this.departedfrom = departedfrom;
  }

  public Double getBonus() {
    return bonus;
  }

  public void setBonus(Double bonus) {
    this.bonus = bonus;
  }

  public Integer getOwnerofaircraft() {
    return ownerofaircraft;
  }

  public void setOwnerofaircraft(Integer ownerofaircraft) {
    this.ownerofaircraft = ownerofaircraft;
  }

  public Integer getLessor() {
    return lessor;
  }

  public void setLessor(Integer lessor) {
    this.lessor = lessor;
  }

  public Integer getEquipment() {
    return equipment;
  }

  public void setEquipment(Integer equipment) {
    this.equipment = equipment;
  }

  public Double getModelprice() {
    return modelprice;
  }

  public void setModelprice(Double modelprice) {
    this.modelprice = modelprice;
  }

  public Integer getMaxweight() {
    return maxweight;
  }

  public void setMaxweight(Integer maxweight) {
    this.maxweight = maxweight;
  }

  public Integer getEmptyweight() {
    return emptyweight;
  }

  public void setEmptyweight(Integer emptyweight) {
    this.emptyweight = emptyweight;
  }

  public Integer getLastcheck() {
    return lastcheck;
  }

  public void setLastcheck(Integer lastcheck) {
    this.lastcheck = lastcheck;
  }

  public Integer getTotalenginetime() {
    return totalenginetime;
  }

  public void setTotalenginetime(Integer totalenginetime) {
    this.totalenginetime = totalenginetime;
  }

  public Integer getEngines() {
    return engines;
  }

  public void setEngines(Integer engines) {
    this.engines = engines;
  }

  public Double getEngineprice() {
    return engineprice;
  }

  public void setEngineprice(Double engineprice) {
    this.engineprice = engineprice;
  }

  public Integer getGph() {
    return gph;
  }

  public void setGph(Integer gph) {
    this.gph = gph;
  }

  public Integer getCruise() {
    return cruise;
  }

  public void setCruise(Integer cruise) {
    this.cruise = cruise;
  }

  public Integer getAdvertise() {
    return advertise;
  }

  public void setAdvertise(Integer advertise) {
    this.advertise = advertise;
  }

  public Integer getCrew() {
    return crew;
  }

  public void setCrew(Integer crew) {
    this.crew = crew;
  }

  public Integer getAirframe() {
    return airframe;
  }

  public void setAirframe(Integer airframe) {
    this.airframe = airframe;
  }

  public Integer getAircraftcondition() {
    return aircraftcondition;
  }

  public void setAircraftcondition(Integer aircraftcondition) {
    this.aircraftcondition = aircraftcondition;
  }


  public Integer getLastfix() {
    return lastfix;
  }

  public void setLastfix(Integer lastfix) {
    this.lastfix = lastfix;
  }

  public Integer getFuelltype() {
    return fuelltype;
  }

  public void setFuelltype(Integer fuelltype) {
    this.fuelltype = fuelltype;
  }



  public Integer getIduserforuserlock() {
    return iduserforuserlock;
  }

  public void setIduserforuserlock(Integer iduserforuserlock) {
    this.iduserforuserlock = iduserforuserlock;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idAircraft != null ? idAircraft.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Aircraft)) {
      return false;
    }
    Aircraft other = (Aircraft) object;
    if ((this.idAircraft == null && other.idAircraft != null) || (this.idAircraft != null && !this.idAircraft.equals(other.idAircraft))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.Aircraft[ idAircraft=" + idAircraft + " ]";
  }

  public Integer getFuelconsumption() {
    return fuelconsumption;
  }

  public void setFuelconsumption(Integer fuelconsumption) {
    this.fuelconsumption = fuelconsumption;
  }

  public Integer getFlighttime() {
    return flighttime;
  }

  public void setFlighttime(Integer flighttime) {
    this.flighttime = flighttime;
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

  public Integer getIdAirline() {
    return idAirline;
  }

  public void setIdAirline(Integer idAirline) {
    this.idAirline = idAirline;
  }

  public Boolean getSystemflugzeug() {
    return systemflugzeug;
  }

  public void setSystemflugzeug(Boolean systemflugzeug) {
    this.systemflugzeug = systemflugzeug;
  }

  public Boolean getUserlock() {
    return userlock;
  }

  public void setUserlock(Boolean userlock) {
    this.userlock = userlock;
  }

  public Boolean getPrivatesale() {
    return privatesale;
  }

  public void setPrivatesale(Boolean privatesale) {
    this.privatesale = privatesale;
  }

  public Boolean getRentable() {
    return rentable;
  }

  public void setRentable(Boolean rentable) {
    this.rentable = rentable;
  }

  public Boolean getForsale() {
    return forsale;
  }

  public void setForsale(Boolean forsale) {
    this.forsale = forsale;
  }

  public Boolean getAllowfix() {
    return allowfix;
  }

  public void setAllowfix(Boolean allowfix) {
    this.allowfix = allowfix;
  }

  public Integer getLizenz() {
    return lizenz;
  }

  public void setLizenz(Integer lizenz) {
    this.lizenz = lizenz;
  }

  public String getXplaneDownloadUrl() {
    return xplaneDownloadUrl;
  }

  public void setXplaneDownloadUrl(String xplaneDownloadUrl) {
    this.xplaneDownloadUrl = xplaneDownloadUrl;
  }

  public String getP3dDownloadUrl() {
    return p3dDownloadUrl;
  }

  public void setP3dDownloadUrl(String p3dDownloadUrl) {
    this.p3dDownloadUrl = p3dDownloadUrl;
  }

  public String getPayware1DownloadUrl() {
    return payware1DownloadUrl;
  }

  public void setPayware1DownloadUrl(String payware1DownloadUrl) {
    this.payware1DownloadUrl = payware1DownloadUrl;
  }

  public String getPayware2DownloadUrl() {
    return payware2DownloadUrl;
  }

  public void setPayware2DownloadUrl(String payware2DownloadUrl) {
    this.payware2DownloadUrl = payware2DownloadUrl;
  }

  public String getPayware3DownloadUrl() {
    return payware3DownloadUrl;
  }

  public void setPayware3DownloadUrl(String payware3DownloadUrl) {
    this.payware3DownloadUrl = payware3DownloadUrl;
  }

  public String getPicture1Url() {
    return picture1Url;
  }

  public void setPicture1Url(String picture1Url) {
    this.picture1Url = picture1Url;
  }

  public String getPicture2Url() {
    return picture2Url;
  }

  public void setPicture2Url(String picture2Url) {
    this.picture2Url = picture2Url;
  }

  public String getPicture3Url() {
    return picture3Url;
  }

  public void setPicture3Url(String picture3Url) {
    this.picture3Url = picture3Url;
  }

  public String getFsxDownloadUrl() {
    return fsxDownloadUrl;
  }

  public void setFsxDownloadUrl(String fsxDownloadUrl) {
    this.fsxDownloadUrl = fsxDownloadUrl;
  }
  public Integer getSeatsBusinessClass() {
    return seatsBusinessClass;
  }

  public void setSeatsBusinessClass(Integer seatsBusinessClass) {
    this.seatsBusinessClass = seatsBusinessClass;
  }

  public Boolean getIsBusinessClass() {
    return isBusinessClass;
  }

  public void setIsBusinessClass(Boolean isBusinessClass) {
    this.isBusinessClass = isBusinessClass;
  }
  
}
