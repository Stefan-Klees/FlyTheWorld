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
public class lqList {
  
  private String Bezeichnung;
  private Date Datum;
  private double Betrag;

  public String getBezeichnung() {
    return Bezeichnung;
  }

  public void setBezeichnung(String Bezeichnung) {
    this.Bezeichnung = Bezeichnung;
  }

  public Date getDatum() {
    return Datum;
  }

  public void setDatum(Date Datum) {
    this.Datum = Datum;
  }

  public double getBetrag() {
    return Betrag;
  }

  public void setBetrag(double Betrag) {
    this.Betrag = Betrag;
  }
  
  
  
}
