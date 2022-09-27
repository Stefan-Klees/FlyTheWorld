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

package de.klees.beans.fbo.userobjekte;

import java.util.Date;

/**
 *
 * @author Stefan Klees
 */
public class AngebotsListeFuel {

  private int AngebotNr;
  private Date LieferDatum;
  private int Menge;
  private double Preis;
  private String FuelArt;
  private int ObjektID;

  AngebotsListeFuel(int AngebotNr, Date LieferDatum, int Menge, double Preis, String FuelArt, int ObjektID) {
    this.AngebotNr = AngebotNr;
    this.LieferDatum = LieferDatum;
    this.Menge = Menge;
    this.Preis = Preis;
    this.FuelArt = FuelArt;
    this.ObjektID = ObjektID;
  }

  public int getAngebotNr() {
    return AngebotNr;
  }

  public void setAngebotNr(int AngebotNr) {
    this.AngebotNr = AngebotNr;
  }

  public Date getLieferDatum() {
    return LieferDatum;
  }

  public void setLieferDatum(Date LieferDatum) {
    this.LieferDatum = LieferDatum;
  }

  public int getMenge() {
    return Menge;
  }

  public void setMenge(int Menge) {
    this.Menge = Menge;
  }

  public double getPreis() {
    return Preis;
  }

  public void setPreis(double Preis) {
    this.Preis = Preis;
  }

  public String getFuelArt() {
    return FuelArt;
  }

  public void setFuelArt(String FuelArt) {
    this.FuelArt = FuelArt;
  }

  public int getObjektID() {
    return ObjektID;
  }

  public void setObjektID(int ObjektID) {
    this.ObjektID = ObjektID;
  }

}
