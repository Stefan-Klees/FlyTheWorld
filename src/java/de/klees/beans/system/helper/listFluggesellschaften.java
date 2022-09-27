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

package de.klees.beans.system.helper;

/**
 *
 * @author Stefan Klees
 */
public class listFluggesellschaften {
  
  private int idFluggesellschaft;
  private String fluggesellschaftBankKonto;
  private String fluggesellschaftName;
  private String BesitzerName;
  private int BesitzerID;

  public int getIdFluggesellschaft() {
    return idFluggesellschaft;
  }

  public void setIdFluggesellschaft(int idFluggesellschaft) {
    this.idFluggesellschaft = idFluggesellschaft;
  }

  public String getFluggesellschaftName() {
    return fluggesellschaftName;
  }

  public void setFluggesellschaftName(String fluggesellschaftName) {
    this.fluggesellschaftName = fluggesellschaftName;
  }

  public String getBesitzerName() {
    return BesitzerName;
  }

  public void setBesitzerName(String BesitzerName) {
    this.BesitzerName = BesitzerName;
  }

  public int getBesitzerID() {
    return BesitzerID;
  }

  public void setBesitzerID(int BesitzerID) {
    this.BesitzerID = BesitzerID;
  }

  public String getFluggesellschaftBankKonto() {
    return fluggesellschaftBankKonto;
  }

  public void setFluggesellschaftBankKonto(String fluggesellschaftBankKonto) {
    this.fluggesellschaftBankKonto = fluggesellschaftBankKonto;
  }
  
  
}
