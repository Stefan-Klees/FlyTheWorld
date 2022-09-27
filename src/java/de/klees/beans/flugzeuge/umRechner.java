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

package de.klees.beans.flugzeuge;

import java.io.Serializable;

/**
 *
 * @author Stefan Klees
 */
public class umRechner implements Serializable {

  private static final long serialVersionUID = 1L;

  /*
  mph -> kts (1 mile = 0,868976 Nmi)
kmh -> kts (1km = 0,539957 Nmi)
---
m <-> ft ( 1m = 3,28084ft )
miles -> Nmi (1 mile = 0,868976 Nmi)
km -> Nmi (1km = 0,539957 Nmi)
---
lbs(pound) <-> kg ( 1lb = 0,453592 kg)
USG <-> kg (1 USG = 2,74584kg 100LL, 1 USG = 3.06618 Jet A1)
   */
//******* Variablen fuer Umrechner
  private double mph2kts;
  private final double mph2ktsFaktor = 0.868976;
  private double kmh2kts;
  private final double kmh2ktsFaktor = 0.539957;
  private double meter2fuss;
  private final double meter2fussFaktor = 3.28084;
  private double fuss2meter;
  private final double fuss2meterFaktor = 0.304799990246;
  private double milen2nautmilen;
  private final double milen2nautmilenFaktor = 0.868976;
  private double km2nautmilen;
  private final double km2nautmilenFaktor = 0.539957;
  private double lbs2kg;
  private final double lbs2kgFaktor = 0.453592;
  private double usg2kg;
  private final double usg2kgFaktor_100LL = 2.74584;
  private final double usg2kgFaktor_JetA = 3.06618;

  private double frmMph2KtsIn;
  private double frmkmh2ktsIn;
  private double frmmeter2fussIn;
  private double frmfuss2meterIn;
  private double frmmilen2nautmilenIn;
  private double frmkm2nautmilenIn;
  private double frmlbs2kgIn;
  private double frmusg2kgIn;

  public umRechner() {
  }

  /*
  ***********************  Getter and Setter
   */

 /*
  ***************** Variablen Umrechner Beginn
   */
  public double getMph2kts() {
    return getFrmMph2KtsIn() * mph2ktsFaktor;
  }

  public double getKmh2kts() {
    return getFrmkmh2ktsIn() * kmh2ktsFaktor;
  }

  public double getMeter2fuss() {
    return getFrmmeter2fussIn() * meter2fussFaktor;
  }

  public double getFuss2meter() {
    return getFrmfuss2meterIn() * fuss2meterFaktor;
  }

  public double getMilen2nautmilen() {
    return getFrmmilen2nautmilenIn() * milen2nautmilenFaktor;
  }

  public double getKm2nautmilen() {
    return getFrmkm2nautmilenIn() * km2nautmilenFaktor;
  }

  public double getLbs2kg() {
    return getFrmlbs2kgIn() * lbs2kgFaktor;
  }

  public double getUsg2kg100LL() {
    return frmusg2kgIn * usg2kgFaktor_100LL;
  }

  public double getUsg2kgJetA() {
    return frmusg2kgIn * usg2kgFaktor_JetA;
  }

  public double getFrmMph2KtsIn() {
    return frmMph2KtsIn;
  }

  public void setFrmMph2KtsIn(double frmMph2KtsIn) {
    this.frmMph2KtsIn = frmMph2KtsIn;
  }

  public double getFrmkmh2ktsIn() {
    return frmkmh2ktsIn;
  }

  public void setFrmkmh2ktsIn(double frmkmh2ktsIn) {
    this.frmkmh2ktsIn = frmkmh2ktsIn;
  }

  public double getFrmmeter2fussIn() {
    return frmmeter2fussIn;
  }

  public double getFrmfuss2meterIn() {
    return frmfuss2meterIn;
  }

  public void setFrmfuss2meterIn(double frmfuss2meterIn) {
    this.frmfuss2meterIn = frmfuss2meterIn;
  }

  public void setFrmmeter2fussIn(double frmmeter2fussIn) {
    this.frmmeter2fussIn = frmmeter2fussIn;
  }

  public double getFrmmilen2nautmilenIn() {
    return frmmilen2nautmilenIn;
  }

  public void setFrmmilen2nautmilenIn(double frmmilen2nautmilenIn) {
    this.frmmilen2nautmilenIn = frmmilen2nautmilenIn;
  }

  public double getFrmkm2nautmilenIn() {
    return frmkm2nautmilenIn;
  }

  public void setFrmkm2nautmilenIn(double frmkm2nautmilenIn) {
    this.frmkm2nautmilenIn = frmkm2nautmilenIn;
  }

  public double getFrmlbs2kgIn() {
    return frmlbs2kgIn;
  }

  public void setFrmlbs2kgIn(double frmlbs2kgIn) {
    this.frmlbs2kgIn = frmlbs2kgIn;
  }

  public double getFrmusg2kgIn() {
    return frmusg2kgIn;
  }

  public void setFrmusg2kgIn(double frmusg2kgIn) {
    this.frmusg2kgIn = frmusg2kgIn;
  }

  /*
  ***************** Variablen Umrechner Ende
   */
}
