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
package de.klees.beans.assignement;

import java.io.Serializable;

/**
 *
 * @author Stefan Klees
 */
public class AgentJobList implements Serializable {

  private static final long serialVersionUID = 1L;

  private String VonICAO;
  private String NachICAO;
  private String NachBezeichnung;
  private String NachLand;
  private int Entfernung;
  private int Kurs;
  private int Klasse;
  private int Pax;
  private int Cargo;
  private int Verguetung;
  private String Bezeichnung;
  private int GesamtGepaeck;
  private int GewichtPax;
  private String CharterKurzbezeichnung;
  private String Lizenz;
  private int idFlugzeug;
  private String Sprache;
  private String FlugzeugArt;
  private double Flugzeit;
  private int SpritKG;
  private int MaxAbfluggewicht;
  private int MaxPayload;
  private int MaxCargo;

  public int getIdFlugzeug() {
    return idFlugzeug;
  }

  public void setIdFlugzeug(int idFlugzeug) {
    this.idFlugzeug = idFlugzeug;
  }

  
  public String getVonICAO() {
    return VonICAO;
  }

  public void setVonICAO(String VonICAO) {
    this.VonICAO = VonICAO;
  }

  public String getNachICAO() {
    return NachICAO;
  }

  public void setNachICAO(String NachICAO) {
    this.NachICAO = NachICAO;
  }

  public String getNachBezeichnung() {
    return NachBezeichnung;
  }

  public void setNachBezeichnung(String NachBezeichnung) {
    this.NachBezeichnung = NachBezeichnung;
  }

  public int getEntfernung() {
    return Entfernung;
  }

  public void setEntfernung(int Entfernung) {
    this.Entfernung = Entfernung;
  }

  public int getKurs() {
    return Kurs;
  }

  public void setKurs(int Kurs) {
    this.Kurs = Kurs;
  }

  public int getPax() {
    return Pax;
  }

  public void setPax(int Pax) {
    this.Pax = Pax;
  }

  public int getCargo() {
    return Cargo;
  }

  public void setCargo(int Cargo) {
    this.Cargo = Cargo;
  }

  public int getVerguetung() {
    return Verguetung;
  }

  public void setVerguetung(int Verguetung) {
    this.Verguetung = Verguetung;
  }

  public String getBezeichnung() {
    return Bezeichnung;
  }

  public void setBezeichnung(String Bezeichnung) {
    this.Bezeichnung = Bezeichnung;
  }

  public String getNachLand() {
    return NachLand;
  }

  public void setNachLand(String NachLand) {
    this.NachLand = NachLand;
  }

  public int getKlasse() {
    return Klasse;
  }

  public void setKlasse(int Klasse) {
    this.Klasse = Klasse;
  }

  public int getGesamtGepaeck() {
    return GesamtGepaeck;
  }

  public void setGesamtGepaeck(int GesamtGepaeck) {
    this.GesamtGepaeck = GesamtGepaeck;
  }

  public int getGewichtPax() {
    return GewichtPax;
  }

  public void setGewichtPax(int GewichtPax) {
    this.GewichtPax = GewichtPax;
  }

  public String getCharterKurzbezeichnung() {
    return CharterKurzbezeichnung;
  }

  public void setCharterKurzbezeichnung(String CharterKurzbezeichnung) {
    this.CharterKurzbezeichnung = CharterKurzbezeichnung;
  }

  public String getLizenz() {
    return Lizenz;
  }

  public void setLizenz(String Lizenz) {
    this.Lizenz = Lizenz;
  }

  public String getSprache() {
    return Sprache;
  }

  public void setSprache(String Sprache) {
    this.Sprache = Sprache;
  }

  public String getFlugzeugArt() {
    return FlugzeugArt;
  }

  public void setFlugzeugArt(String FlugzeugArt) {
    this.FlugzeugArt = FlugzeugArt;
  }

  public double getFlugzeit() {
    return Flugzeit;
  }

  public void setFlugzeit(double Flugzeit) {
    this.Flugzeit = Flugzeit;
  }


  public int getSpritKG() {
    return SpritKG;
  }

  public void setSpritKG(int SpritKG) {
    this.SpritKG = SpritKG;
  }

  public int getMaxAbfluggewicht() {
    return MaxAbfluggewicht;
  }

  public void setMaxAbfluggewicht(int MaxAbfluggewicht) {
    this.MaxAbfluggewicht = MaxAbfluggewicht;
  }

  public int getMaxPayload() {
    return MaxPayload;
  }

  public void setMaxPayload(int MaxPayload) {
    this.MaxPayload = MaxPayload;
  }

  public int getMaxCargo() {
    return MaxCargo;
  }

  public void setMaxCargo(int MaxCargo) {
    this.MaxCargo = MaxCargo;
  }

}
