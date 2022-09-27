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

import de.klees.data.Airport;
import de.klees.data.Flugzeugboerse;
import de.klees.data.Flugzeugboersetemplates;
import de.klees.data.views.ViewFlugzeugeMietKauf;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author Stefan Klees
 */
public class flugzeugBoerseBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private Flugzeugboerse selectedAnzeige;
  private Flugzeugboersetemplates selectedVorlage;
  private Flugzeugboersetemplates importTemplate;
  private List<Flugzeugboerse> anzeigenListe;
  private List<Flugzeugboerse> positionenListe;

  private String FlugzeugType;
  private String FlugzeugKennung;

  private boolean isLoaded;

  private int UserID;
  private int AngebotArt;

  private int aktivePositionen;
  private boolean zeigeBilder;

  /**
   * Creates a new instance of flugzeugBoerseBean
   */
  public flugzeugBoerseBean() {
    UserID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
    AngebotArt = -1;
    FlugzeugType = "";
    isLoaded = false;
    aktivePositionen = -1;
    zeigeBilder=true;
  }

  @EJB
  FlugzeugFacade facadeAnzeigen;

  public List<Flugzeugboerse> getAnzeigenListe() {
    if (!isLoaded) {
      positionenListe = facadeAnzeigen.getAnzeigen(UserID);
      isLoaded = true;
    }
    return positionenListe;
  }

  public List<Flugzeugboersetemplates> getVorlagenList() {
    return facadeAnzeigen.getVorlagenUser(UserID);
  }

  public List<Flugzeugboersetemplates> getVorlagenGeteilt() {
    return facadeAnzeigen.getVorlagenGeteilt();
  }

  public List<ViewFlugzeugeMietKauf> getFlugzeugeUser() {
    if (selectedAnzeige != null) {
      return facadeAnzeigen.getUserFlugzeuge(UserID);
    } else {
      return null;
    }
  }

  public List<Flugzeugboerse> getFlugzeugtypen() {
    return facadeAnzeigen.getAnzeigenFlugzeuge();
  }

  public List<Flugzeugboerse> getAngebote() {
    return facadeAnzeigen.getAngebote(AngebotArt, FlugzeugType);
  }

  public void onChangeAnzeige() {

    switch (aktivePositionen) {
      case -1:
        positionenListe = facadeAnzeigen.getAnzeigen(UserID);
        break;
      case 0:
        positionenListe = facadeAnzeigen.getAnzeigenFilter(UserID, false);
        break;
      case 1:
        positionenListe = facadeAnzeigen.getAnzeigenFilter(UserID, true);
        break;
      default:
        break;
    }

  }

  public void onNeueAnzeige() {

    long ablauf = new Date().getTime() + (14 * 24 * 60 * 60 * 1000L);

    Flugzeugboerse anz = new Flugzeugboerse();
    anz.setIduser(UserID);
    anz.setAblaufam(new Date(ablauf));
    anz.setIdmietkauf(-1);
    anz.setFlugzeugtyp("");
    anz.setText("");
    anz.setVerkaufspreis(0.0);
    anz.setLeasingpreis(0.0);
    anz.setFinanzierungspreis(0.0);
    anz.setLeasing(false);
    anz.setVerkauf(true);
    anz.setFinanzierung(false);
    anz.setFreigabe(false);
    anz.setGebuehrenbezahlt(false);
    facadeAnzeigen.neueAnzeige(anz);
    selectedAnzeige = anz;
    NewMessage("Angebot wurde gespeichert!");

    onChangeAnzeige();
  }

  public void onAnzeigeVerlaengern() {
    if (selectedAnzeige != null) {
      long ablauf = new Date().getTime() + (14 * 24 * 60 * 60 * 1000L);
      selectedAnzeige.setAblaufam(new Date(ablauf));
      facadeAnzeigen.editAnzeige(selectedAnzeige);
      NewMessage("Angebot wurde verlängert");

      onChangeAnzeige();
    }
  }

  public void onEditAnzeige() {
    if (selectedAnzeige != null) {
      if (selectedAnzeige.getIdmietkauf() > 0) {
        selectedAnzeige.setFlugzeugtyp(facadeAnzeigen.readFlugzeugMietKauf(selectedAnzeige.getIdmietkauf()).getType());
      } else {
        selectedAnzeige.setFlugzeugtyp("");
      }
      selectedAnzeige.setText(clearText(selectedAnzeige.getText()));
      facadeAnzeigen.editAnzeige(selectedAnzeige);
      NewMessage("Anzeige gespeichert");
      onChangeAnzeige();
    } else {
      NewMessage("Keine Anzeige gewählt");
    }
  }

  public void onDeletAnzeige() {
    facadeAnzeigen.removeAnzeige(selectedAnzeige);
    selectedAnzeige = null;
    onChangeAnzeige();
    NewMessage("Angebot wurde gelöscht!");
  }

  public void onNeueVorlage() {
    Flugzeugboersetemplates vorlage = new Flugzeugboersetemplates();
    vorlage.setBeschreibung("");
    vorlage.setBezeichnung("Neue Vorlage für Flugzeugbörse");
    vorlage.setIduser(UserID);
    vorlage.setLeasing(false);
    vorlage.setTausch(false);
    vorlage.setVerkauf(false);
    vorlage.setFinanzierung(false);

    facadeAnzeigen.neueVorlage(vorlage);
    setSelectedVorlage(vorlage);

    NewMessage("Neue Vorlage angelegt.");
  }

  public void onEditVorlage() {
    selectedVorlage.setBeschreibung(clearText(selectedVorlage.getBeschreibung()));
    facadeAnzeigen.editVorlage(selectedVorlage);
    NewMessage("Vorlage gespeichert.");
  }

  public void onLoescheVorlage() {
    facadeAnzeigen.removeVorlage(selectedVorlage);
    NewMessage("Vorlage gelöscht.");
  }

  public void loadTemplate() {

    ViewFlugzeugeMietKauf fgz = facadeAnzeigen.readFlugzeugMietKauf(selectedAnzeige.getIdmietkauf());

    if (fgz != null) {

      Airport airport = facadeAnzeigen.getAirportInfo(fgz.getAktuellePositionICAO());

      if (airport != null) {

        DecimalFormat df = new DecimalFormat("#,##0");
        SimpleDateFormat dfdateTime = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        SimpleDateFormat dfdateCCheck = new SimpleDateFormat("MM.yyyy");
        DecimalFormat dfkomma = new DecimalFormat("#,##0.00");

        try {
          String contact = facadeAnzeigen.readUser(UserID).getName1();
          String location = airport.getIcao() + " - " + airport.getLand() + " " + airport.getStadt();
          String miles = df.format(fgz.getGesamtEntfernung());
          String constructionyear = String.valueOf(fgz.getBaujahr());
          String airframe = df.format(fgz.getGesamtAlterDesFlugzeugsMinuten() / 60);
          String nextccheck = dfdateCCheck.format(fgz.getNaechsterTerminCcheck());
          String conditions = dfkomma.format(fgz.getZustand());
          String aircraftimage = "";

          String retailprice = df.format(selectedAnzeige.getVerkaufspreis());
          String leasingprice = df.format(selectedAnzeige.getLeasingpreis());
          String financingprice = df.format(selectedAnzeige.getFinanzierungspreis());

          String aircraft = fgz.getType();
          String fuellOB = df.format(fgz.getAktuelleTankfuellung());
          String expires = dfdateTime.format(selectedAnzeige.getAblaufam());

          if (!fgz.getEigenesBildURL().equals("")) {
            aircraftimage = fgz.getEigenesBildURL();
          } else {
            aircraftimage = fgz.getSymbolUrl();
          }

          String rendered = importTemplate.getBeschreibung()
                  .replaceAll("#contact#", contact)
                  .replaceAll("#location#", location)
                  .replaceAll("#miles#", miles)
                  .replaceAll("#constructionyear#", constructionyear)
                  .replaceAll("#airframe#", airframe)
                  .replaceAll("#nextccheck#", nextccheck)
                  .replaceAll("#conditions#", conditions)
                  .replaceAll("#aircraftimage#", aircraftimage)
                  .replaceAll("#aircraft#", aircraft)
                  .replaceAll("#conditions#", conditions)
                  .replaceAll("#retailprice#", retailprice)
                  .replaceAll("#leasingprice#", leasingprice)
                  .replaceAll("#financingprice#", financingprice)
                  .replaceAll("#fuellOB#", fuellOB)
                  .replaceAll("#expires#", expires);

          selectedAnzeige.setText(rendered);
        } catch (Exception e) {
          NewMessage("Ein Fehler beim import ist aufgetreten");
        }
      }
    }

  }

  public void onRowSelect(SelectEvent event) {
  }

  public void onRowUnselect(UnselectEvent event) {
  }

  private void NewMessage(String neueNachricht) {
    FacesMessage msg = new FacesMessage(neueNachricht);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  // Setter and Getter
  private String clearText(String inText) {
    String ClearText = inText.replaceAll("<script>", "");
    ClearText = ClearText.replaceAll("</script>", "");
    ClearText = ClearText.replaceAll("<css>", "");
    ClearText = ClearText.replaceAll("</css>", "");
    ClearText = ClearText.replaceAll("<refresh", "");
    ClearText = ClearText.replaceAll("<meta", "");
    ClearText = ClearText.replaceAll("<iframe", "");
    ClearText = ClearText.replaceAll("</iframe>", "");

    return ClearText;
  }

  public String getFlugzeugBild(int id) {
    if (id > 0) {
      ViewFlugzeugeMietKauf fg = facadeAnzeigen.readFlugzeugMietKauf(id);
      if (fg != null) {
        FlugzeugKennung = fg.getRegistrierung();
        if (fg.getEigenesBildURL().equals("")) {
          return fg.getSymbolUrl();
        } else {
          return fg.getEigenesBildURL();
        }
      } else {
        return "";
      }
    } else {
      return "";
    }
  }

  public Flugzeugboerse getSelectedAnzeige() {
    return selectedAnzeige;
  }

  public void setSelectedAnzeige(Flugzeugboerse selectedAnzeige) {
    this.selectedAnzeige = selectedAnzeige;
  }

  public Flugzeugboersetemplates getSelectedVorlage() {
    return selectedVorlage;
  }

  public void setSelectedVorlage(Flugzeugboersetemplates selectedVorlage) {
    this.selectedVorlage = selectedVorlage;
  }

  public Flugzeugboersetemplates getImportTemplate() {
    return importTemplate;
  }

  public void setImportTemplate(Flugzeugboersetemplates importTemplate) {
    this.importTemplate = importTemplate;
  }

  public int getAngebotArt() {
    return AngebotArt;
  }

  public void setAngebotArt(int AngebotArt) {
    this.AngebotArt = AngebotArt;
  }

  public String getFlugzeugType() {
    return FlugzeugType;
  }

  public void setFlugzeugType(String FlugzeugType) {
    this.FlugzeugType = FlugzeugType;
  }

  public int getAnzahlEintraege() {
    return facadeAnzeigen.getAngebote(-1, "").size();
  }

  public String getFlugzeugKennung() {
    return FlugzeugKennung;
  }

  public void setFlugzeugKennung(String FlugzeugKennung) {
    this.FlugzeugKennung = FlugzeugKennung;
  }

  public int getAktivePositionen() {
    return aktivePositionen;
  }

  public void setAktivePositionen(int aktivePositionen) {
    this.aktivePositionen = aktivePositionen;
  }

  public boolean isZeigeBilder() {
    return zeigeBilder;
  }

  public void setZeigeBilder(boolean zeigeBilder) {
    this.zeigeBilder = zeigeBilder;
  }

}
