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
package de.klees.beans.airline.flugrouten;

import de.klees.beans.system.CONF;
import de.klees.beans.system.loginMB;
import de.klees.data.Airport;
import de.klees.data.Assignement;
import de.klees.data.Bank;
import de.klees.data.FlugesellschaftPiloten;
import de.klees.data.Fluggesellschaft;
import de.klees.data.Fluggesellschaftmanager;
import de.klees.data.Flugrouten;
import de.klees.data.Sitzkonfiguration;
import de.klees.data.Benutzer;
import de.klees.data.views.ViewFBOUserObjekte;
import de.klees.data.views.ViewFlugzeugeMietKauf;
import de.klees.data.views.ViewRoutenAuftraege;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author Stefan Klees
 */
public class airrouteBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<Flugrouten> flugroutenList;
  private List<ViewRoutenAuftraege> auftragsListe;
  private List<ViewRoutenAuftraege> selectedAssignments;

  private Flugrouten currentFlugroute;
  private Flugrouten selectedFlugrouten;
  private List<Flugrouten> listSelectedFlugrouten;
  private Fluggesellschaft FlugGesellschaft;

  private int AirlineiD;
  private String FluggesellschaftICAO;
  private boolean rueckflugrouteErstellen;
  private boolean routeIstOeffentlich;
  private String SuchText;
  private String AirLineName;
  private String AirLineIcao;
  private double vonLongitude, vonLatitude, nachLongitude, nachLatitude;
  private int Distanz[];
  private String vonIcao;
  private String nachIcao;
  private String vonName;
  private String toName;
  private String fromAirportLandCity;
  private String toAirportLandCity;

  private int frmAbfertigungsTerminal;
  private int frmFBO;
  private String frmName;
  private String frmVonIcao;
  private String frmNachIcao;
  private String frmBemerkung;
  private int frmidFluggesellschaft;
  private int frmMaxPaxgroesse;
  private boolean frmOeffentlich;
  private boolean frmBusinessLounge;
  private double frmBonusFuerAirlinePiloten;
  private double frmBonusFuerPiloten;
  private String frmpassengersTitle;
  private int frmRoutenArt;
  private double frmProvision;
  private int frmidTerminalDep;
  private int frmidTerminalArr;
  private int frmidFbo;
  private boolean frmEcoAktiv;
  private int frmFlugzeugID;
  private int frmMaxPax;
  private int frmMaxBusiness;
  private int frmMaxCargo;
  private boolean frmmo;
  private boolean frmdi;
  private boolean frmmi;
  private boolean frmdo;
  private boolean frmfr;
  private boolean frmsa;
  private boolean frmso;
  private boolean frmausfuehrungPerDatum;
  private Date frmausfuehrungAm;
  private Date frmletzteAusfuehrungAm;
  private String frmflugzeugLizenz;
  private int frmroutenType;
  private Date frmnaechsteAusfuehrungAm;
  private int frmwiederholungen;
  private int frmausfuehrungsZaehler;
  private int frmPilot;

  private int MaxRouten;
  private int MaxEntfernung;
  private int MinEntfernung;
  private int vorhandeneRouten;

  private final int UserID;
  private final int FluggesellschaftID;
  private final int ManagerID;

  private boolean allowRouten;
  private boolean allowRoutenBearbeiten;
  private boolean allowRoutenErstellen;
  private boolean allowRoutenLoeschen;

  private boolean isLoaded;
  private Map<String, String> routenArt;

  private int AuslastungPax;
  private int AuslastungCargo;
  private int AuslastungBC;

  private int selPilot;
  private String selZielflughafen;
  private String selAbflughafen;
  private boolean viewAuftragsliste;

  public airrouteBean() {
    isLoaded = false;
    frmProvision = 0.0;
    frmBonusFuerAirlinePiloten = 0.0;
    frmBonusFuerPiloten = 0.0;
    frmEcoAktiv = true;
    frmBusinessLounge = false;
    viewAuftragsliste = true;
    frmOeffentlich = true;
    frmausfuehrungPerDatum = false;
    frmausfuehrungAm = new Date();
    frmletzteAusfuehrungAm = frmausfuehrungAm;
    selPilot = -1;
    selZielflughafen = "";
    selAbflughafen = "";

    routenArt = new HashMap<>();
    routenArt.put(loginMB.onSprache("Fluggesellschaften_cbo_RoutenArtPax"), "1");
    routenArt.put(loginMB.onSprache("Fluggesellschaften_cbo_RoutenArtCargo"), "2");
    routenArt.put(loginMB.onSprache("Fluggesellschaften_cbo_RoutenArtPax") + " + " + loginMB.onSprache("Fluggesellschaften_cbo_RoutenArtCargo"), "3");

    FluggesellschaftID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftID");
    UserID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
    ManagerID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ManagerID");

  }

  @EJB
  FlugroutenFacade facadeFlugrouten;

  public List<Flugrouten> getListFlugrouten() {

    if (!isLoaded) {
      setAirLineIcao((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftICAO"));
      setAirLineName((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftName"));
      isLoaded = true;

      if (selPilot > 0) {
        flugroutenList = facadeFlugrouten.findAllByPilotID(selPilot, FluggesellschaftID);
      } else {
        flugroutenList = facadeFlugrouten.findAllByAirlineID(FluggesellschaftID);
      }

    }

    return flugroutenList;
  }

  public List<ViewFBOUserObjekte> getTerminals() {
    return facadeFlugrouten.findAllTerminals(FlugGesellschaft.getUserid());
  }

  public List<ViewFBOUserObjekte> getFBOs() {
    return facadeFlugrouten.findAllFBOs(FlugGesellschaft.getUserid());
  }

  public List<ViewRoutenAuftraege> getAuftraege() {

    if (viewAuftragsliste) {
      auftragsListe = facadeFlugrouten.getAuftraege("%" + selAbflughafen + "%", FluggesellschaftID, selPilot, "%" + selZielflughafen + "%");
      viewAuftragsliste = false;
    }

    return auftragsListe;
  }

  public List<ViewRoutenAuftraege> getPiloten() {
    return facadeFlugrouten.getAllPiloten(FluggesellschaftID);

//    if (!selAbflughafen.equals("")) {
//      return facadeFlugrouten.getPiloten(selAbflughafen, FluggesellschaftID);
//    } else {
//      return null;
//    }
  }

  public List<ViewRoutenAuftraege> getZiele() {
    if (!selAbflughafen.equals("")) {
      return facadeFlugrouten.getZielflughaefen(selAbflughafen, FluggesellschaftID);
    } else {
      return null;
    }
  }

  public List<Flugrouten> getFlugroutenGroup() {
    return facadeFlugrouten.findAllByAirlineIDGroupRoute(FluggesellschaftID);

  }

  public List<ViewFlugzeugeMietKauf> getAirlineFlugzeug() {
    return facadeFlugrouten.findAllAirlineFlugzeuge(FluggesellschaftID);
  }

  public List<FlugesellschaftPiloten> getPilotenFluggesellschaft() {
    return facadeFlugrouten.getFluggesellschaftPiloten(FluggesellschaftID);
  }

  public List<Flugrouten> getMeineFlugrouten() {
    return facadeFlugrouten.findMeineRouten(UserID);
  }

  public void onFlugroutenFuerPilot() {
    isLoaded = false;
  }

  public void onAlleAuftraegeZeigen() {
    auftragsListe = facadeFlugrouten.getAlleAuftraege(FluggesellschaftID);
    viewAuftragsliste = false;
  }

  public void onChangeFilterAuftraege() {
    viewAuftragsliste = true;
  }

  public void onRoutenAuslastung() {

    if (currentFlugroute != null) {

      @SuppressWarnings("unchecked")
      List<Object[]> auslastung = facadeFlugrouten.RoutenAuslastung(currentFlugroute.getVonIcao(), currentFlugroute.getNachicao(), currentFlugroute.getIdFluggesellschaft());

      try {
        AuslastungPax = Integer.valueOf(String.valueOf(auslastung.get(0)[0]));
        AuslastungCargo = Integer.valueOf(String.valueOf(auslastung.get(0)[1]));
        AuslastungBC = Integer.valueOf(String.valueOf(auslastung.get(0)[2]));

      } catch (NullPointerException | NumberFormatException e) {
        AuslastungPax = 0;
        AuslastungCargo = 0;
        AuslastungBC = 0;
      }
    }

  }

  public void pruefeFlugroute() {
    isLoaded = false;

    vonIcao = facadeFlugrouten.getFBOIcao(frmFBO);

    if (vonIcao != null) {
      MaxRouten = facadeFlugrouten.getAnzahlMaxRouten(UserID, vonIcao);
      vorhandeneRouten = facadeFlugrouten.getAnzahlRouten(FlugGesellschaft.getUserid(), vonIcao);

      if (vorhandeneRouten <= MaxRouten) {
        createFlugroute();
      } else {
        NewMessage(loginMB.onSprache("Fluggesellschaften_Routen_msg_MaximaleRoutenEreicht") + vonIcao);
      }
    }

  }

  public void onAlleRoutenAktiveDeativ() {
    for (Flugrouten fg : flugroutenList) {
      if (fg.getAktiv()) {
        fg.setAktiv(false);
      } else {
        fg.setAktiv(true);
      }
      facadeFlugrouten.edit(fg);
    }
  }

  public void onRoutenAuslastungRoutenDeativieren() {
    for (Flugrouten fg : flugroutenList) {
      if (fg.getVonIcao().equals(currentFlugroute.getVonIcao()) && fg.getNachicao().equals(currentFlugroute.getNachicao())) {
        fg.setAktiv(false);
      }
      facadeFlugrouten.edit(fg);
    }
  }

  public void onRoutenAuslastungRoutenAktivieren() {
    for (Flugrouten fg : flugroutenList) {
      if (fg.getVonIcao().equals(currentFlugroute.getVonIcao()) && fg.getNachicao().equals(currentFlugroute.getNachicao())) {
        fg.setAktiv(true);
      }
      facadeFlugrouten.edit(fg);
    }
  }

  public void onRouteAktivierenDeaktivieren() {
    if (currentFlugroute.getAktiv()) {
      currentFlugroute.setAktiv(false);
    } else {
      currentFlugroute.setAktiv(true);
    }
    facadeFlugrouten.edit(currentFlugroute);
  }

  public void onRouteAktivierenDeaktivierenDurchPilot() {
    if (currentFlugroute.getAktiv()) {
      currentFlugroute.setAktiv(false);
    } else {
      currentFlugroute.setAktiv(true);
      currentFlugroute.setAusfuehrungsZaehler(0);
    }
    facadeFlugrouten.edit(currentFlugroute);
  }

  public void onBusinessAktivierenDeaktivieren() {
    if (currentFlugroute.getUseBusinessLounge()) {
      currentFlugroute.setUseBusinessLounge(false);
    } else {
      currentFlugroute.setUseBusinessLounge(true);
    }
    facadeFlugrouten.edit(currentFlugroute);
  }

  public void zaehleFlugrouten() {

    if (currentFlugroute != null) {
      vonIcao = currentFlugroute.getVonIcao();
      MaxRouten = facadeFlugrouten.getAnzahlMaxRouten(FlugGesellschaft.getUserid(), vonIcao);
      vorhandeneRouten = facadeFlugrouten.getAnzahlRouten(FlugGesellschaft.getUserid(), vonIcao);
      NewMessage("Es stehen insgesamt " + MaxRouten + " Routen in " + vonIcao + ", zur Verfügung,  davon sind " + vorhandeneRouten + " Routen verplant");
    } else {
      NewMessage("Keine Flugroute ausgewählt, Bitte Flugroute auswählen");
    }
  }

  public void saveFlugroute() {
    isLoaded = false;
    boolean Check = true;

    maxWerteWaehlen();
    // 1= Pax, 2 = Cargo, 3 = Pax + Cargo

    if (currentFlugroute.getRoutenArt().equals(3) || currentFlugroute.getRoutenArt().equals(1)) {
      if (frmMaxPax + frmMaxBusiness <= 0) {
        Check = false;
        NewMessage("Flugroute kann nicht gespeichert werden, falscher Flugzeugtype");
      }
    }

    if (Check) {
      if (currentFlugroute.getRoutenArt().equals(2)) {
        if (frmMaxPax + frmMaxBusiness > 0) {
          Check = false;
          NewMessage("Flugroute kann nicht gespeichert werden, falscher Flugzeugtype");
        }
      }
    }

    if (Check) {

      // UTC Problem bei der letzten Ausfuehrung
      if (!currentFlugroute.getAusfuehrungPerDatum()) {
        long aufschlag = 12 * 60 * 60 * 1000;
        long ablauf = currentFlugroute.getLetzteAusfuehrungAm().getTime();
        currentFlugroute.setLetzteAusfuehrungAm(new Date(ablauf + aufschlag));
      }

      vonIcao = currentFlugroute.getVonIcao().trim();
      nachIcao = currentFlugroute.getNachicao().trim();

      Airport airport = facadeFlugrouten.readAirport(vonIcao);

      vonLongitude = airport.getLongitude();
      vonLatitude = airport.getLatitude();
      vonName = airport.getName();
      fromAirportLandCity = airport.getStadt() + " " + airport.getLand();

      airport = facadeFlugrouten.readAirport(nachIcao);

      nachLatitude = airport.getLatitude();
      nachLongitude = airport.getLongitude();
      toName = airport.getName();
      toAirportLandCity = airport.getStadt() + " " + airport.getLand();

      Distanz = CONF.DistanzBerechnung(vonLongitude, vonLatitude, nachLongitude, nachLatitude);

      //Nullwerte bei Bonus1,Bonus2 und Provision abfangen
      if (currentFlugroute.getBonusFuerAirlinePiloten() != null) {
        //********** Negativ Bonus verboten
        if (currentFlugroute.getBonusFuerAirlinePiloten() < 0.0) {
          currentFlugroute.setBonusFuerAirlinePiloten(0.0);
          NewMessage(loginMB.onSprache("Fluggesellschaften_Routen_msg_ungueltigerBonus"));
        }
        if (currentFlugroute.getBonusFuerAirlinePiloten() > 100.0) {
          currentFlugroute.setBonusFuerAirlinePiloten(100.0);
          NewMessage(loginMB.onSprache("Fluggesellschaften_Routen_msg_ungueltigerBonus"));
        }
      } else {
        currentFlugroute.setBonusFuerAirlinePiloten(0.0);
      }

      if (currentFlugroute.getBonusFuerPiloten() != null) {
        if (currentFlugroute.getBonusFuerPiloten() < 0.0) {
          currentFlugroute.setBonusFuerPiloten(0.0);
          NewMessage(loginMB.onSprache("Fluggesellschaften_Routen_msg_ungueltigerBonus"));
        }
        if (currentFlugroute.getBonusFuerPiloten() > 100.0) {
          currentFlugroute.setBonusFuerPiloten(100.0);
          NewMessage(loginMB.onSprache("Fluggesellschaften_Routen_msg_ungueltigerBonus"));
        }
      } else {
        currentFlugroute.setBonusFuerPiloten(0.0);
      }

      if (currentFlugroute.getProvision() != null) {
        if (currentFlugroute.getProvision() < 0.0) {
          currentFlugroute.setProvision(0.0);
        }
        if (currentFlugroute.getProvision() > 100.0) {
          currentFlugroute.setProvision(100.0);
        }

      } else {
        currentFlugroute.setProvision(0.0);
      }

      currentFlugroute.setDistance(Distanz[0]);

      if (Distanz[0] >= 500) {
        currentFlugroute.setLangstrecke(true);
      } else {
        currentFlugroute.setLangstrecke(false);
      }

      currentFlugroute.setIdUserFBO(facadeFlugrouten.readFboObjectTerminal(vonIcao, currentFlugroute.getIdFluggesellschaft()).getIduserfbo());
      currentFlugroute.setDirect(Distanz[1]);
      currentFlugroute.setToName(toName);
      currentFlugroute.setFromName(vonName);
      currentFlugroute.setVonIcao(vonIcao.toUpperCase());
      currentFlugroute.setNachicao(nachIcao.toUpperCase());
      currentFlugroute.setToAirportLandCity(toAirportLandCity);
      currentFlugroute.setFromAirportLandCity(fromAirportLandCity);
      currentFlugroute.setEcoAktiv(currentFlugroute.getEcoAktiv());
      currentFlugroute.setAusfuehrungsZaehler(0);

      currentFlugroute.setMaxBusiness(frmMaxBusiness);
      currentFlugroute.setMaxCargo(frmMaxCargo);
      currentFlugroute.setMaxPax(frmMaxPax);
      currentFlugroute.setFlugzeugLizenz(frmflugzeugLizenz);
      currentFlugroute.setAktiv(false);

      if (!currentFlugroute.getAusfuehrungPerDatum()) {
        currentFlugroute.setIdPilot(-1);
      }

      if (currentFlugroute.getAusfuehrungPerDatum()) {
        currentFlugroute.setMo(false);
        currentFlugroute.setDi(false);
        currentFlugroute.setMi(false);
        currentFlugroute.setDon(false);
        currentFlugroute.setFr(false);
        currentFlugroute.setSa(false);
        currentFlugroute.setSo(false);
        currentFlugroute.setWiederholungen(1);
      }

      // Langstrecke definieren
      currentFlugroute.setLangstrecke(true);

      if (currentFlugroute.getFlugzeugLizenz().equals("PPL-A") || currentFlugroute.getFlugzeugLizenz().equals("CPL")) {
        currentFlugroute.setLangstrecke(false);
      }

      if (currentFlugroute.getFlugzeugLizenz().equals("MPL") && currentFlugroute.getDistance() <= 500) {
        currentFlugroute.setLangstrecke(false);
      }

      if (currentFlugroute.getRoutenArt() == 2 && (frmMaxBusiness > 0 || frmMaxPax > 0)) {
        NewMessage("Flugroute kann nicht gespeichert werden, falscher Flugzeugtype");
        Check = false;
      }

      if (currentFlugroute.getName().equals("Random Job")) {
        NewMessage(loginMB.onSprache("Fluggesellschaften_Routen_msg_ungueltigerRoutenname"));
        Check = false;
      }

      if (Check) {
        facadeFlugrouten.edit(currentFlugroute);
        NewMessage(loginMB.onSprache("Fluggesellschaften_Routen_msg_RouteGespeichert"));
      }

    }

  }

  public void beforCreate() {
    currentFlugroute = null;
  }

  public void createFlugroute() {
    isLoaded = false;
    boolean Check = true;

    FluggesellschaftICAO = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftICAO");

    if (frmVonIcao.equals(frmNachIcao)) {
      Check = false;
      NewMessage("Abflug und Ankunft sind gleich, Route wird nicht angelegt.");
    }

    if (Check) {
      //Terminal-Auslesen
      try {
        frmidTerminalDep = facadeFlugrouten.readFboObjectTerminal(frmVonIcao, FlugGesellschaft.getIdFluggesellschaft()).getIduserfbo();
        frmidTerminalArr = facadeFlugrouten.readFboObjectTerminal(frmNachIcao, FlugGesellschaft.getIdFluggesellschaft()).getIduserfbo();
      } catch (NullPointerException e) {
        Check = false;
        NewMessage("Terminals nicht gefunden.");
      }
    }

    if (Check) {
      if (frmFlugzeugID <= 0) {
        Check = false;
        NewMessage("Kein Flugzeug ausgewählt");
      }
    }

    if (Check) {
      if (facadeFlugrouten.readAirport(frmVonIcao.trim()) == null || facadeFlugrouten.readAirport(frmNachIcao.trim()) == null) {
        Check = false;
      }
    }

    if (Check) {
      Flugrouten newFlugroute = new Flugrouten();

      newFlugroute.setIdFbo(-1);
      newFlugroute.setIdFlugzeugMietKauf(frmFlugzeugID);
      newFlugroute.setIdFluggesellschaft(FluggesellschaftID);
      // ID des Abgehenden Terminals als Erkennung beim löschen des Terminals eintragen.
      newFlugroute.setIdUserFBO(frmidTerminalDep);
      newFlugroute.setIdTerminalDep(frmidTerminalDep);
      newFlugroute.setIdTerminalArr(frmidTerminalArr);
      newFlugroute.setBemerkung(frmBemerkung);
      newFlugroute.setNachicao(frmNachIcao.trim());
      newFlugroute.setVonIcao(frmVonIcao.trim());
      newFlugroute.setName(frmName);
      newFlugroute.setOeffentlich(frmOeffentlich);
      newFlugroute.setBonusFuerAirlinePiloten(frmBonusFuerAirlinePiloten);
      newFlugroute.setBonusFuerPiloten(frmBonusFuerPiloten);
      newFlugroute.setDirect(0);
      newFlugroute.setDistance(0);
      newFlugroute.setFromAirportLandCity("");
      newFlugroute.setFromName("");
      newFlugroute.setPassengersTitle(frmpassengersTitle);
      newFlugroute.setRoutenArt(frmRoutenArt);
      newFlugroute.setToAirportLandCity("");
      newFlugroute.setToName("");
      newFlugroute.setProvision(frmProvision);

      newFlugroute.setUseBusinessLounge(frmBusinessLounge);
      newFlugroute.setAktiv(true);
      newFlugroute.setLetzterFlug(new Date());
      newFlugroute.setUmsatz(0.0);
      newFlugroute.setUmsatzmenge(0);
      newFlugroute.setErzeugteMenge(0);
      newFlugroute.setEcoAktiv(frmEcoAktiv);
      newFlugroute.setAusfuehrungAm(frmausfuehrungAm);
      newFlugroute.setAusfuehrungPerDatum(frmausfuehrungPerDatum);
      newFlugroute.setAusfuehrungsZaehler(0);
      newFlugroute.setMo(frmmo);
      newFlugroute.setDi(frmdi);
      newFlugroute.setMi(frmmi);
      newFlugroute.setDon(frmdo);
      newFlugroute.setFr(frmfr);
      newFlugroute.setSa(frmsa);
      newFlugroute.setSo(frmso);
      newFlugroute.setLetzteAusfuehrungAm(frmletzteAusfuehrungAm);
      newFlugroute.setMaxPaxgroesse(0);
      newFlugroute.setNaechsteAusfuehrungAm(new Date());
      newFlugroute.setIdPilot(frmPilot);

      if (frmwiederholungen <= 0) {
        frmwiederholungen = 1;
      }
      newFlugroute.setWiederholungen(frmwiederholungen);
      newFlugroute.setMaxBusiness(frmMaxBusiness);
      newFlugroute.setMaxCargo(frmMaxCargo);
      newFlugroute.setMaxPax(frmMaxPax);
      newFlugroute.setFlugzeugLizenz(frmflugzeugLizenz);
      newFlugroute.setRoutenType(-1);
      newFlugroute.setLangstrecke(true);
      facadeFlugrouten.create(newFlugroute);
      setSelectedFlugrouten(newFlugroute);

      saveFlugroute();

      NewMessage(loginMB.onSprache("Fluggesellschaften_Routen_msg_NeueRoute"));

    }

  }

  public void onCopyRoute() {

    if (currentFlugroute != null) {
      isLoaded = false;

      vonIcao = currentFlugroute.getVonIcao();
      nachIcao = currentFlugroute.getNachicao();

      currentFlugroute.setUmsatz(0.0);
      currentFlugroute.setUmsatzmenge(0);
      currentFlugroute.setAusfuehrungsZaehler(0);
      facadeFlugrouten.create(currentFlugroute);
      NewMessage(loginMB.onSprache("Fluggesellschaften_Routen_msg_RouteKopiert"));

    }

  }

  public void onReset() {

    if (currentFlugroute != null) {
      currentFlugroute.setUmsatz(0.0);
      currentFlugroute.setUmsatzmenge(0);
      currentFlugroute.setErzeugteMenge(0);
      currentFlugroute.setLetzterFlug(new Date());
      facadeFlugrouten.edit(currentFlugroute);
      NewMessage(loginMB.onSprache("Fluggesellschaften_Routen_msg_RoutenUmsatzReset"));
    }

  }

  public void onResetAll() {
    for (Flugrouten froute : getListFlugrouten()) {
      froute.setUmsatz(0.0);
      froute.setUmsatzmenge(0);
      froute.setErzeugteMenge(0);
      froute.setLetzterFlug(new Date());
      facadeFlugrouten.edit(froute);
    }

    NewMessage(loginMB.onSprache("Fluggesellschaften_Routen_msg_RoutenUmsatzReset"));
  }

  public void onEdit() {
    if (currentFlugroute != null) {
      vonIcao = currentFlugroute.getVonIcao();
      nachIcao = currentFlugroute.getNachicao();
    } else {
      NewMessage("Flugroute lässt sich nicht bearbeiten");
    }

  }

  public void deleteFlugroute() {
    isLoaded = false;
    //Flugroute und Assignments löschen
    //Regress für gelöschte Assignments
    if (currentFlugroute != null) {
      double Betrag = 0.0;

      for (Assignement assi : facadeFlugrouten.listAuftraegeUeberRoutenID(currentFlugroute.getIdFlugrouten())) {
        Betrag = Betrag + assi.getPay();
      }
      //Buchung des Regress
      if (Betrag > 0) {
        Fluggesellschaft fg = facadeFlugrouten.readFG(currentFlugroute.getIdFluggesellschaft());
        String VerwendungsZweck = "Route has been deleted - claims for recourse: " + currentFlugroute.getName();
        String Auftraggeber = "**** FTW BANK *****";
        String AuftraggeberKonto = "500-1000001";
        String EmpfaengerKonto = fg.getBankKonto();
        String Empfaenger = fg.getBankKontoName();

        Betrag = (Betrag * 0.5) * -1;

        SaveBankbuchung(EmpfaengerKonto, Empfaenger, AuftraggeberKonto, Auftraggeber,
                new Date(), Betrag, EmpfaengerKonto, Empfaenger, new Date(), VerwendungsZweck, fg.getUserid(), -1, -1, -1, -1, -1, -1, "", -1, -1, -1, -1);

      }

      if (facadeFlugrouten.onDeleteFlugrouteUndAuftraege(currentFlugroute) > 0) {
        NewMessage(loginMB.onSprache("Fluggesellschaften_Routen_msg_RouteGeloescht"));
        currentFlugroute = null;
      }
    }
  }

  public void onRefresh() {
    FlugGesellschaft = facadeFlugrouten.readFG(FluggesellschaftID);

    frmBonusFuerAirlinePiloten = 0.0;
    frmBonusFuerPiloten = 0.0;
    frmProvision = 0.0;

    try {
      frmBonusFuerAirlinePiloten = FlugGesellschaft.getStdBonus2();
      frmBonusFuerPiloten = FlugGesellschaft.getStdBonus1();
      frmProvision = FlugGesellschaft.getStdProvision();

    } catch (NullPointerException e) {
      NewMessage("Error by Bonus");
    }

    //System.out.println("de.klees.beans.airline.airlineBean.onRowSelect() FG ID " + FlugGesellschaft.getIdFluggesellschaft());
    if (FlugGesellschaft.getIdFluggesellschaft().equals(ManagerID)) {
      //System.out.println("de.klees.beans.airline.airlineBean.onRowSelect() Berechtigungen auslesen");

      Fluggesellschaftmanager mg = facadeFlugrouten.readManager(UserID, FlugGesellschaft.getIdFluggesellschaft());

      setAllowRouten(mg.getAllowRouten());
      setAllowRoutenBearbeiten(mg.getAllowRoutenBearbeiten());
      setAllowRoutenErstellen(mg.getAllowRoutenErstellen());
      setAllowRoutenLoeschen(mg.getAllowRoutenLoeschen());

    } else {

      setAllowRouten(true);
      setAllowRoutenBearbeiten(true);
      setAllowRoutenErstellen(true);
      setAllowRoutenLoeschen(true);

    }

  }

  public void onAuftraegeZurueckholen() {

    for (ViewRoutenAuftraege assi : selectedAssignments) {
      facadeFlugrouten.onAuftraegeZurueckholen(assi.getIdassignement());
    }
    setSelectedAssignments(null);
    onChangeFilterAuftraege();
  }

  public void onAuftraegeZuweisen() {

    for (ViewRoutenAuftraege assi : selectedAssignments) {
      facadeFlugrouten.onAuftraegeZuweisen(assi.getIdassignement(), selPilot);
    }
    setSelectedAssignments(null);
    onChangeFilterAuftraege();
  }

  public void maxWerteWaehlen() {

    frmMaxCargo = 0;
    frmMaxPax = 0;
    frmMaxBusiness = 0;
    frmflugzeugLizenz = "";
    ViewFlugzeugeMietKauf fzg = null;

    if (currentFlugroute != null) {
      fzg = facadeFlugrouten.readFlugzeugMietKauf(currentFlugroute.getIdFlugzeugMietKauf());
    } else {
      fzg = facadeFlugrouten.readFlugzeugMietKauf(frmFlugzeugID);
    }

    if (fzg != null) {
      frmflugzeugLizenz = fzg.getLizenz();
      if (fzg.getIdSitzKonfiguration() > 0) {
        Sitzkonfiguration sk = facadeFlugrouten.readSitzkonfiguration(fzg.getIdSitzKonfiguration());
        frmMaxCargo = sk.getCargo();
        frmMaxPax = sk.getSitzeEconomy();
        frmMaxBusiness = sk.getSitzeBusiness();
      } else {
        frmMaxCargo = fzg.getCargo();
        frmMaxPax = fzg.getSitzeEconomy();
        frmMaxBusiness = fzg.getSitzeBusinessClass();
      }
    }
  }

  public void checkRoutenArt() {

    if (frmFlugzeugID > 0 && frmRoutenArt == 2) {
      ViewFlugzeugeMietKauf mietFlz = facadeFlugrouten.readFlugzeugMietKauf(frmFlugzeugID);

      if (mietFlz.getSitzeEconomy() > 0 || mietFlz.getSitzeBusinessClass() > 0) {
        frmRoutenArt = 3;
        frmMaxCargo = mietFlz.getCargo();
        frmMaxBusiness = mietFlz.getSitzeBusinessClass();
        frmMaxPax = mietFlz.getSitzeEconomy();

        if (mietFlz.getIdSitzKonfiguration() > 0) {
          Sitzkonfiguration sk = facadeFlugrouten.readSitzkonfiguration(mietFlz.getIdSitzKonfiguration());
          frmMaxCargo = sk.getCargo();
          frmMaxBusiness = sk.getSitzeBusiness();
          frmMaxPax = sk.getSitzeEconomy();
        }
        NewMessage("Mit diesem Flugzeug können keine Cargo-Jobs erstellt werden, wähle Passagiere + Cargo");
      }
    } else if (frmRoutenArt == 2) {
      frmBusinessLounge = false;
      frmEcoAktiv = false;
    }

    if (currentFlugroute != null) {
      ViewFlugzeugeMietKauf mietFlz = facadeFlugrouten.readFlugzeugMietKauf(currentFlugroute.getIdFlugzeugMietKauf());

      if (mietFlz != null) {
        currentFlugroute.setMaxCargo(mietFlz.getCargo());
        currentFlugroute.setMaxBusiness(mietFlz.getSitzeBusinessClass());
        currentFlugroute.setMaxPax(mietFlz.getSitzeEconomy());

        if (mietFlz.getIdSitzKonfiguration() > 0) {
          Sitzkonfiguration sk = facadeFlugrouten.readSitzkonfiguration(mietFlz.getIdSitzKonfiguration());
          currentFlugroute.setMaxCargo(sk.getCargo());
          currentFlugroute.setMaxBusiness(sk.getSitzeBusiness());
          currentFlugroute.setMaxPax(sk.getSitzeEconomy());
        }

        if (currentFlugroute.getRoutenArt() == 2) {
          if (mietFlz.getSitzeEconomy() > 0 || mietFlz.getSitzeBusinessClass() > 0) {
            currentFlugroute.setRoutenArt(3);
            currentFlugroute.setMaxCargo(mietFlz.getCargo());
            currentFlugroute.setMaxBusiness(mietFlz.getSitzeBusinessClass());
            currentFlugroute.setMaxPax(mietFlz.getSitzeEconomy());
            if (mietFlz.getIdSitzKonfiguration() > 0) {
              Sitzkonfiguration sk = facadeFlugrouten.readSitzkonfiguration(mietFlz.getIdSitzKonfiguration());
              currentFlugroute.setMaxCargo(sk.getCargo());
              currentFlugroute.setMaxBusiness(sk.getSitzeBusiness());
              currentFlugroute.setMaxPax(sk.getSitzeEconomy());
            }
            NewMessage("Mit diesem Flugzeug können keine Cargo-Jobs erstellt werden, wähle Passagiere + Cargo");
          }
        }

        if (currentFlugroute.getRoutenArt().equals(1) || currentFlugroute.getRoutenArt().equals(3)) {
          if (mietFlz.getSitzeEconomy() + mietFlz.getSitzeBusinessClass() <= 0) {
            currentFlugroute.setRoutenArt(2);
            currentFlugroute.setMaxCargo(mietFlz.getCargo());
            currentFlugroute.setMaxBusiness(mietFlz.getSitzeBusinessClass());
            currentFlugroute.setMaxPax(mietFlz.getSitzeEconomy());
            if (mietFlz.getIdSitzKonfiguration() > 0) {
              Sitzkonfiguration sk = facadeFlugrouten.readSitzkonfiguration(mietFlz.getIdSitzKonfiguration());
              currentFlugroute.setMaxCargo(sk.getCargo());
              currentFlugroute.setMaxBusiness(sk.getSitzeBusiness());
              currentFlugroute.setMaxPax(sk.getSitzeEconomy());
            }

            NewMessage("Mit diesem Flugzeug können nur Cargo-Jobs erstellt werden, wähle Cargo");
          }
        }

        if (currentFlugroute.getRoutenArt() == 2) {
          currentFlugroute.setMaxCargo(mietFlz.getCargo());
          currentFlugroute.setMaxBusiness(mietFlz.getSitzeBusinessClass());
          currentFlugroute.setMaxPax(mietFlz.getSitzeEconomy());
          if (mietFlz.getIdSitzKonfiguration() > 0) {
            Sitzkonfiguration sk = facadeFlugrouten.readSitzkonfiguration(mietFlz.getIdSitzKonfiguration());
            currentFlugroute.setMaxCargo(sk.getCargo());
            currentFlugroute.setMaxBusiness(sk.getSitzeBusiness());
            currentFlugroute.setMaxPax(sk.getSitzeEconomy());
          }

          currentFlugroute.setUseBusinessLounge(false);
          currentFlugroute.setEcoAktiv(false);
        }
      }
    }
  }

  public void onRowSelect(SelectEvent event) {

    if (currentFlugroute != null) {
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FlugroutenID", getSelectedFlugrouten().getIdFlugrouten());
    }
  }

  public void onRowUnselect(UnselectEvent event) {
  }

  private void NewMessage(String neueNachricht) {
    FacesMessage msg = new FacesMessage(neueNachricht);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  public void onSucheStarten() {
    isLoaded = false;
  }

  public String onChangeRouten() {
    isLoaded = false;

    return "fluggesellschaft_routen.xhtml";
  }

  public void clearFelder() {

    if (currentFlugroute != null) {
      currentFlugroute.setMo(false);
      currentFlugroute.setDi(false);
      currentFlugroute.setMi(false);
      currentFlugroute.setDon(false);
      currentFlugroute.setFr(false);
      currentFlugroute.setSa(false);
      currentFlugroute.setSa(false);
    }

    frmmo = false;
    frmdi = false;
    frmmi = false;
    frmdo = false;
    frmfr = false;
    frmsa = false;
    frmso = false;

  }

  /*
  Setter and Getter
   */
  public String getUserName(int id) {
    Benutzer u = facadeFlugrouten.readUser(id);
    if (u != null) {
      return u.getName1();
    } else {
      return "";
    }
  }

  public String getFlugzeugType(int id) {
    if (id > 0) {
      return facadeFlugrouten.readFlugzeugMietKauf(id).getType();
    } else {
      return "";
    }

  }

  public Flugrouten getSelectedFlugrouten() {
    return currentFlugroute;
  }

  public void setSelectedFlugrouten(Flugrouten selectedFlugrouten) {
    this.currentFlugroute = selectedFlugrouten;
  }

  public boolean isRueckflugrouteErstellen() {
    return rueckflugrouteErstellen;
  }

  public void setRueckflugrouteErstellen(boolean rueckflugrouteErstellen) {
    this.rueckflugrouteErstellen = rueckflugrouteErstellen;
  }

  public String getSuchText() {
    return SuchText;
  }

  public void setSuchText(String SuchText) {
    this.SuchText = SuchText;
  }

  public String getAirLineName() {
    return AirLineName;
  }

  public void setAirLineName(String AirLineName) {
    this.AirLineName = AirLineName;
  }

  public String getAirLineIcao() {
    return AirLineIcao;
  }

  public void setAirLineIcao(String AirLineIcao) {
    this.AirLineIcao = AirLineIcao;
  }

  public boolean isRouteIstOeffentlich() {
    return routeIstOeffentlich;
  }

  public void setRouteIstOeffentlich(boolean routeIstOeffentlich) {
    this.routeIstOeffentlich = routeIstOeffentlich;
  }

  public Map<String, String> getRoutenArt() {
    return routenArt;
  }

  public void setRoutenArt(Map<String, String> routenArt) {
    this.routenArt = routenArt;
  }

  public int getFrmAbfertigungsTerminal() {
    return frmAbfertigungsTerminal;
  }

  public void setFrmAbfertigungsTerminal(int frmAbfertigungsTerminal) {
    this.frmAbfertigungsTerminal = frmAbfertigungsTerminal;
  }

  public int getFrmFBO() {
    return frmFBO;
  }

  public void setFrmFBO(int frmFBO) {
    this.frmFBO = frmFBO;
  }

  public String getAbfertigungsTerminal(int ID) {
    if (ID == -300) {
      return "FTW Terminal";
    }
    return facadeFlugrouten.getFBOBezeichnung(ID);
  }

  public String getFBO(int ID) {
    return facadeFlugrouten.getFBOBezeichnung(ID);
  }

  public String getFrmName() {
    return frmName;
  }

  public void setFrmName(String frmName) {
    this.frmName = frmName;
  }

  public String getFrmVonIcao() {
    return frmVonIcao;
  }

  public void setFrmVonIcao(String frmVonIcao) {
    this.frmVonIcao = frmVonIcao;
  }

  public String getFrmNachIcao() {
    return frmNachIcao;
  }

  public void setFrmNachIcao(String frmNachIcao) {
    this.frmNachIcao = frmNachIcao;
  }

  public String getFrmBemerkung() {
    return frmBemerkung;
  }

  public void setFrmBemerkung(String frmBemerkung) {
    this.frmBemerkung = frmBemerkung;
  }

  public int getFrmidFluggesellschaft() {
    return frmidFluggesellschaft;
  }

  public void setFrmidFluggesellschaft(int frmidFluggesellschaft) {
    this.frmidFluggesellschaft = frmidFluggesellschaft;
  }

  public int getFrmMaxPaxgroesse() {
    return frmMaxPaxgroesse;
  }

  public void setFrmMaxPaxgroesse(int frmMaxPaxgroesse) {
    this.frmMaxPaxgroesse = frmMaxPaxgroesse;
  }

  public boolean isFrmOeffentlich() {
    return frmOeffentlich;
  }

  public void setFrmOeffentlich(boolean frmOeffentlich) {
    this.frmOeffentlich = frmOeffentlich;
  }

  public boolean isFrmBusinessLounge() {
    return frmBusinessLounge;
  }

  public void setFrmBusinessLounge(boolean frmBusinessLounge) {
    this.frmBusinessLounge = frmBusinessLounge;
  }

  public double getFrmBonusFuerAirlinePiloten() {
    return frmBonusFuerAirlinePiloten;
  }

  public void setFrmBonusFuerAirlinePiloten(double frmBonusFuerAirlinePiloten) {
    this.frmBonusFuerAirlinePiloten = frmBonusFuerAirlinePiloten;
  }

  public double getFrmBonusFuerPiloten() {
    return frmBonusFuerPiloten;
  }

  public void setFrmBonusFuerPiloten(double frmBonusFuerPiloten) {
    this.frmBonusFuerPiloten = frmBonusFuerPiloten;
  }

  public String getFrmpassengersTitle() {
    return frmpassengersTitle;
  }

  public void setFrmpassengersTitle(String frmpassengersTitle) {
    this.frmpassengersTitle = frmpassengersTitle;
  }

  public int getFrmRoutenArt() {
    return frmRoutenArt;
  }

  public void setFrmRoutenArt(int frmRoutenArt) {
    this.frmRoutenArt = frmRoutenArt;
  }

  public double getFrmProvision() {
    return frmProvision;
  }

  public void setFrmProvision(double frmProvision) {
    this.frmProvision = frmProvision;
  }

  public int getFrmidFbo() {
    return frmidFbo;
  }

  public void setFrmidFbo(int frmidFbo) {
    this.frmidFbo = frmidFbo;
  }

  public int getMaxRouten() {
    return MaxRouten;
  }

  public void setMaxRouten(int MaxRouten) {
    this.MaxRouten = MaxRouten;
  }

  public int getVorhandeneRouten() {
    return vorhandeneRouten;
  }

  public void setVorhandeneRouten(int vorhandeneRouten) {
    this.vorhandeneRouten = vorhandeneRouten;
  }

  public boolean isAllowRouten() {
    return allowRouten;
  }

  public void setAllowRouten(boolean allowRouten) {
    this.allowRouten = allowRouten;
  }

  public boolean isAllowRoutenBearbeiten() {
    return allowRoutenBearbeiten;
  }

  public void setAllowRoutenBearbeiten(boolean allowRoutenBearbeiten) {
    this.allowRoutenBearbeiten = allowRoutenBearbeiten;
  }

  public boolean isAllowRoutenErstellen() {
    return allowRoutenErstellen;
  }

  public void setAllowRoutenErstellen(boolean allowRoutenErstellen) {
    this.allowRoutenErstellen = allowRoutenErstellen;
  }

  public boolean isAllowRoutenLoeschen() {
    return allowRoutenLoeschen;
  }

  public void setAllowRoutenLoeschen(boolean allowRoutenLoeschen) {
    this.allowRoutenLoeschen = allowRoutenLoeschen;
  }

  public int getAuslastungPax() {
    return AuslastungPax;
  }

  public int getAuslastungCargo() {
    return AuslastungCargo;
  }

  public int getAuslastungBC() {
    return AuslastungBC;
  }

  public double getGesamtAuslastung() {

    int summeUmsatz = 0;
    int summeErzeugt = 0;
    double ergebnis = 0.0;

    if (currentFlugroute != null) {

      for (Flugrouten fg : flugroutenList) {
        if (fg.getVonIcao().equals(currentFlugroute.getVonIcao()) && fg.getNachicao().equals(currentFlugroute.getNachicao())) {
          summeUmsatz = summeUmsatz + fg.getUmsatzmenge();
          summeErzeugt = summeErzeugt + fg.getErzeugteMenge();
        }
      }

    }

    try {
      ergebnis = (summeUmsatz / summeErzeugt) * 100;
    } catch (ArithmeticException e) {
      ergebnis = 0.0;
    }

    return ergebnis;
  }

  public int getSelPilot() {
    return selPilot;
  }

  public void setSelPilot(int selPilot) {
    this.selPilot = selPilot;
  }

  public String getSelZielflughafen() {
    return selZielflughafen;
  }

  public void setSelZielflughafen(String selZielflughafen) {
    this.selZielflughafen = selZielflughafen;
  }

  public String getSelAbflughafen() {
    return selAbflughafen;
  }

  public void setSelAbflughafen(String selAbflughafen) {
    this.selAbflughafen = selAbflughafen;
  }

  public List<ViewRoutenAuftraege> getSelectedAssignments() {
    return selectedAssignments;
  }

  public void setSelectedAssignments(List<ViewRoutenAuftraege> selectedAssignments) {
    this.selectedAssignments = selectedAssignments;
  }

  public boolean isFrmEcoAktiv() {
    return frmEcoAktiv;
  }

  public void setFrmEcoAktiv(boolean frmEcoAktiv) {
    this.frmEcoAktiv = frmEcoAktiv;
  }

  public List<Flugrouten> getListSelectedFlugrouten() {
    return listSelectedFlugrouten;
  }

  public void setListSelectedFlugrouten(List<Flugrouten> listSelectedFlugrouten) {
    this.listSelectedFlugrouten = listSelectedFlugrouten;
  }

  public int getFrmFlugzeugID() {
    return frmFlugzeugID;
  }

  public void setFrmFlugzeugID(int frmFlugzeugID) {
    this.frmFlugzeugID = frmFlugzeugID;
  }

  public int getFrmMaxPax() {
    return frmMaxPax;
  }

  public void setFrmMaxPax(int frmMaxPax) {
    this.frmMaxPax = frmMaxPax;
  }

  public int getFrmMaxCargo() {
    return frmMaxCargo;
  }

  public void setFrmMaxCargo(int frmMaxCargo) {
    this.frmMaxCargo = frmMaxCargo;
  }

  public int getFrmMaxBusiness() {
    return frmMaxBusiness;
  }

  public void setFrmMaxBusiness(int frmMaxBusiness) {
    this.frmMaxBusiness = frmMaxBusiness;
  }

  public boolean isFrmmo() {
    return frmmo;
  }

  public void setFrmmo(boolean frmmo) {
    this.frmmo = frmmo;
  }

  public boolean isFrmdi() {
    return frmdi;
  }

  public void setFrmdi(boolean frmdi) {
    this.frmdi = frmdi;
  }

  public boolean isFrmmi() {
    return frmmi;
  }

  public void setFrmmi(boolean frmmi) {
    this.frmmi = frmmi;
  }

  public boolean isFrmdo() {
    return frmdo;
  }

  public void setFrmdo(boolean frmdo) {
    this.frmdo = frmdo;
  }

  public boolean isFrmfr() {
    return frmfr;
  }

  public void setFrmfr(boolean frmfr) {
    this.frmfr = frmfr;
  }

  public boolean isFrmsa() {
    return frmsa;
  }

  public void setFrmsa(boolean frmsa) {
    this.frmsa = frmsa;
  }

  public boolean isFrmso() {
    return frmso;
  }

  public void setFrmso(boolean frmso) {
    this.frmso = frmso;
  }

  public boolean isFrmausfuehrungPerDatum() {
    return frmausfuehrungPerDatum;
  }

  public void setFrmausfuehrungPerDatum(boolean frmausfuehrungPerDatum) {
    this.frmausfuehrungPerDatum = frmausfuehrungPerDatum;
  }

  public Date getFrmausfuehrungAm() {
    return frmausfuehrungAm;
  }

  public void setFrmausfuehrungAm(Date frmausfuehrungAm) {
    this.frmausfuehrungAm = frmausfuehrungAm;
  }

  public Date getFrmletzteAusfuehrungAm() {
    return frmletzteAusfuehrungAm;
  }

  public void setFrmletzteAusfuehrungAm(Date frmletzteAusfuehrungAm) {
    this.frmletzteAusfuehrungAm = frmletzteAusfuehrungAm;
  }

  public String getFrmflugzeugLizenz() {
    return frmflugzeugLizenz;
  }

  public void setFrmflugzeugLizenz(String frmflugzeugLizenz) {
    this.frmflugzeugLizenz = frmflugzeugLizenz;
  }

  public int getFrmroutenType() {
    return frmroutenType;
  }

  public void setFrmroutenType(int frmroutenType) {
    this.frmroutenType = frmroutenType;
  }

  public Date getFrmnaechsteAusfuehrungAm() {
    return frmnaechsteAusfuehrungAm;
  }

  public void setFrmnaechsteAusfuehrungAm(Date frmnaechsteAusfuehrungAm) {
    this.frmnaechsteAusfuehrungAm = frmnaechsteAusfuehrungAm;
  }

  public int getFrmwiederholungen() {
    return frmwiederholungen;
  }

  public void setFrmwiederholungen(int frmwiederholungen) {
    this.frmwiederholungen = frmwiederholungen;
  }

  public int getFrmausfuehrungsZaehler() {
    return frmausfuehrungsZaehler;
  }

  public void setFrmausfuehrungsZaehler(int frmausfuehrungsZaehler) {
    this.frmausfuehrungsZaehler = frmausfuehrungsZaehler;
  }

  public int getFrmidTerminalDep() {
    return frmidTerminalDep;
  }

  public void setFrmidTerminalDep(int frmidTerminalDep) {
    this.frmidTerminalDep = frmidTerminalDep;
  }

  public int getFrmidTerminalArr() {
    return frmidTerminalArr;
  }

  public void setFrmidTerminalArr(int frmidTerminalArr) {
    this.frmidTerminalArr = frmidTerminalArr;
  }

  public int getFrmPilot() {
    return frmPilot;
  }

  public void setFrmPilot(int frmPilot) {
    this.frmPilot = frmPilot;
  }

  /**
   *
   * @param Bankonto
   * @param Kontoname
   * @param AbsenderKontoNr
   * @param AbsenderKontoName
   * @param AusfuehrungsDatum
   * @param Betrag
   * @param EmpfaengerKontoNr
   * @param EmpfaengerKontoName
   * @param Ueberweisungsdatum
   * @param VerwendungsZweck
   * @param UserID
   * @param AirportID
   * @param FluggesellschaftID
   * @param FlugzeugBesitzerID
   * @param IndustrieID
   * @param LeasinggesellschaftID
   * @param TransportID
   * @param icao
   * @param objektID
   * @param FlugzeugID
   * @param kostenstelle
   * @param pilotID
   */
  private void SaveBankbuchung(String Bankkonto, String KontoName, String AbsenderKontoNr, String AbsenderKontoName, Date AusfuehrungsDatum, double Betrag, String EmpfaengerKontoNr,
          String EmpfaengerKontoName, Date UeberweisungsDatum, String VerwendungsZweck,
          int userid, int AirportID, int FluggesellschaftID, int FlugzeugBesitzerID, int IndustrieID, int LeasinggesellschaftID, int TransportID,
          String icao, int objektID, int FlugzeugID, int Kostenstelle, int pilotID) {

    Bank newBuchung = new Bank();

    newBuchung.setBankKonto(Bankkonto);
    newBuchung.setKontoName(KontoName);
    newBuchung.setAbsenderKonto(AbsenderKontoNr);
    newBuchung.setAbsenderName(AbsenderKontoName);
    newBuchung.setEmpfaengerKonto(EmpfaengerKontoNr);
    newBuchung.setEmpfaengerName(EmpfaengerKontoName);
    newBuchung.setAusfuehrungsDatum(AusfuehrungsDatum);
    newBuchung.setUeberweisungsDatum(UeberweisungsDatum);
    newBuchung.setVerwendungsZweck(VerwendungsZweck);
    newBuchung.setBetrag(Betrag);

    newBuchung.setAirportID(AirportID);
    newBuchung.setFluggesellschaftID(FluggesellschaftID);
    newBuchung.setFlugzeugBesitzerID(FlugzeugBesitzerID);
    newBuchung.setIndustrieID(IndustrieID);
    newBuchung.setLeasinggesellschaftID(LeasinggesellschaftID);
    newBuchung.setTransportID(TransportID);
    newBuchung.setUserID(userid);
    newBuchung.setIcao(icao);
    newBuchung.setObjektID(objektID);
    newBuchung.setFlugzeugID(FlugzeugID);
    newBuchung.setKostenstelle(Kostenstelle);
    newBuchung.setPilotID(pilotID);

    facadeFlugrouten.createBankbuchung(newBuchung);

  }

}
