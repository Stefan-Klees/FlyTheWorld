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

package de.klees.beans.maps;

/**
 *
 * @author Stefan Klees
 */
public class marker  {
  
  
  private String Longitude;
  private String Latitude;
  private String Tooltip;
  private String jsCode;

  
  
  public String getLongitude() {
    return Longitude;
  }

  public void setLongitude(String Longitude) {
    this.Longitude = Longitude;
  }

  public String getLatitude() {
    return Latitude;
  }

  public void setLatitude(String Latitude) {
    this.Latitude = Latitude;
  }

  public String getTooltip() {
    return Tooltip;
  }

  public void setTooltip(String Tooltip) {
    this.Tooltip = Tooltip;
  }

  public String getJsCode() {
    return jsCode;
  }

  public void setJsCode(String jsCode) {
    this.jsCode = jsCode;
  }
  
  
  
}
