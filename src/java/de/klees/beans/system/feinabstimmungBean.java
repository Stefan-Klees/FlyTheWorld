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

package de.klees.beans.system;

import de.klees.data.Feinabstimmung;
import de.klees.data.Systemmeldung;
import de.klees.data.WartungsTabelle;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Stefan Klees
 */
public class feinabstimmungBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private Feinabstimmung currentData;
  private Systemmeldung currentMeldung;
  private boolean frmAktiv;
  private String frmMessage;
  private String wartungsMeldung;

  public feinabstimmungBean() {
  }

  @EJB
  SystemFacade abstimmungsFacade;

  public Feinabstimmung getDaten() {
    setSelectedData(abstimmungsFacade.getDaten());
    return getSelectedData();
  }

  public void onSave() {
    abstimmungsFacade.feinabstimmungEdit(currentData);
    NewMessage("Daten gespeichert");

  }

  public void onUpdateModus() {
    currentMeldung = abstimmungsFacade.getUpdateStatus();
  }

  public void onSaveSystemMeldung() {
    WartungsTabelle wt = new WartungsTabelle();
    wt.setDatumUhrzeit(new Date());
    wt.setMeldung(wartungsMeldung);
    abstimmungsFacade.createWartungsMeldung(wt);
  }

  public void onSaveUpdateModus() {
    if (currentMeldung != null) {
      currentMeldung.setAktiv(frmAktiv);
      currentMeldung.setMeldung(frmMessage);
      abstimmungsFacade.UpdateModusEdit(currentMeldung);
    }
  }

  private void NewMessage(String neueNachricht) {
    FacesMessage msg = new FacesMessage(neueNachricht);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  public Feinabstimmung getSelectedData() {
    return currentData;
  }

  public void setSelectedData(Feinabstimmung selectedData) {
    this.currentData = selectedData;
  }

  public boolean isFrmAktiv() {
    if (currentMeldung != null) {
      return currentMeldung.getAktiv();
    } else {
      return frmAktiv;
    }

  }

  public void setFrmAktiv(boolean frmAktiv) {
    this.frmAktiv = frmAktiv;
  }

  public String getFrmMessage() {
    if (currentMeldung != null) {
      return abstimmungsFacade.getUpdateStatus().getMeldung();
    } else {
      return frmMessage;
    }
  }

  public void setFrmMessage(String frmMessage) {
    this.frmMessage = frmMessage;
  }

  public String getWartungsMeldung() {
    return wartungsMeldung;
  }

  public void setWartungsMeldung(String wartungsMeldung) {
    this.wartungsMeldung = wartungsMeldung;
  }

}
