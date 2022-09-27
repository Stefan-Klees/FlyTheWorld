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
public class listKostenstellen {
  
  private int kostellenNummer;
  private String kostenstellenBezeichnung;

  public int getKostellenNummer() {
    return kostellenNummer;
  }

  public void setKostellenNummer(int kostellenNummer) {
    this.kostellenNummer = kostellenNummer;
  }

  public String getKostenstellenBezeichnung() {
    return kostenstellenBezeichnung;
  }

  public void setKostenstellenBezeichnung(String kostenstellenBezeichnung) {
    this.kostenstellenBezeichnung = kostenstellenBezeichnung;
  }
  
  
  
  
}
