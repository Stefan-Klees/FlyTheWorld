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

package de.klees.beans.luftrettung;

import de.klees.beans.system.CONF;
import de.klees.beans.system.loginMB;
import de.klees.data.AcarsRettungseinsatz;
import de.klees.data.Bank;
import de.klees.data.Feinabstimmung;
import de.klees.data.Fluglogbuch;
import de.klees.data.Flugzeugemietkauf;
import de.klees.data.Rettungseinsaetze;
import de.klees.data.Rettungsstationen;
import de.klees.data.Benutzer;
import de.klees.data.Yaacarskopf;
import de.klees.data.Yaacarspositionen;
import de.klees.data.views.ViewFlugzeugeMietKauf;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Stefan Klees
 */
public class NewTerminalLuftrettung implements Serializable {

  private static final long serialVersionUID = 1L;

  // DAO
  private List<Rettungsstationen> stationenList;
  private List<Rettungseinsaetze> EinsatzList;
  private Rettungsstationen selectedStation;
  private ViewFlugzeugeMietKauf MeinGemietetesFlugzeug;
  private Rettungseinsaetze selectedEinsatz;
  private AcarsRettungseinsatz lfdAuftrag;
  private Benutzer currentUser;

  // Tankstelle
  private Feinabstimmung Config;
  private String TankstellenGrafik;
  private double TankstelleSumme;
  private int TankstelleMengeLbs;
  private int TankMengeKilo;
  private double TankMengeProzent;
  private int TankBisFuellstandKG;
  private int maxTankkapazitaet;
  private int aktuelleTankfuellung;
  private boolean tankstelleGesperrt;

  //Ablaufsteuerung
  private int UserID;
  private boolean StartOn;
  private int interval;
  private boolean istAuftragVorhanden;
  private int idFlugzeug;
  private boolean isLoadedStationen;
  private boolean istLoadedEinsatzListe;
  private String meldung;
  private int AcarsUeberwachungTimer;

  //ACARS Variablen (Info etc.)
  private Yaacarskopf acp;
  private boolean acarsFlugInUse;
  private boolean acarsFlugBeendet;

  private double GewichtPassagiereTakeOff;
  private double GewichtGepaeckTakeOff;
  private double GewichtCargoTakeOff;
  private double GewichtTreibstoff;
  private double GewichtTotal;
  private int currentFlightMiles;

  // Maps Variablen
  private String frmStationsName;
  private double frmFlugzeugBreitenGrad;
  private double frmFlugzeugLaengenGrad;
  private double frmStationBreitenGrad;
  private double frmStationLaengenGrad;
  private String frmStationWPBezeichnung;
  private double frmUnfallBreitenGrad;
  private double frmUnfallLaengenGrad;
  private String frmUnfallWPBezeichnung;
  private double frmZielBreitenGrad;
  private double frmZielLaengenGrad;
  private String frmZielWPBezeichnung;
  private double frmAktPosBreitenGrad;
  private double frmAktPosLaengenGrad;

  private String frmPosTextUnfall;
  private String frmPostTextStation;
  private String frmPostTextZiel;
  private String frmPosTextAktuellePosition;

  private int Zoom;
  private String txtEinsatzMeldung;
  private String txtKurs;
  private String txtEntfernung;
  private String txtRestzeit;
  private String txtAktuellerKurs;
  private int Geschwindigkeit;
  private int FlugKurs;
  private String IconFuerAircraft;

  private final DecimalFormat dfkurs = new DecimalFormat("###000");
  private final DecimalFormat dfmiles = new DecimalFormat("#,###0");

  @EJB
  RettungsstationenFacade rettungsFacade;

  public NewTerminalLuftrettung() {
    isLoadedStationen = false;
    istLoadedEinsatzListe = false;
    istAuftragVorhanden = false;
    tankstelleGesperrt = false;
    StartOn = false;
    interval = 5; //5
    frmAktPosBreitenGrad = 0.0;
    frmAktPosLaengenGrad = 0.0;
    meldung = "";
    AcarsUeberwachungTimer = 10;
    Zoom = 10;
    txtEinsatzMeldung = "Warte auf Einsatz....";
    FlugKurs = 0;
    IconFuerAircraft = "http://www.ftw-sim.de/images/FTW/helis/plane_" + FlugKurs + ".png";

    UserID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
  }

  public void Config() {
    Config = rettungsFacade.getConfig();
  }

  public boolean isAcarsFlugInUse() {

    //Prüfen ob es eine andere Art von Flug gibt
    Yaacarskopf flight = rettungsFacade.readYAACARSFlight(UserID);

    if (flight != null) {
      // Laufenden YAACARS-Flug ermitteln
      //"missionsart"
      //1 = Normaler FTW-Flug
      //2 = Charter
      //3 = Rettung
      //4 = Missionen
      if (flight.getMissionsart() == 3) {
        acarsFlugInUse = false;
      } else {
        acarsFlugInUse = true;
      }
    } else {
      acarsFlugInUse = false;
    }
    return acarsFlugInUse;
  }

  public void onStart() {
    Config();

    onPruefeAufLaufendenEinsatz();

    if (!istAuftragVorhanden) {
      idFlugzeug = selectedStation.getIdFlugzeugMietKauf();
      MeinGemietetesFlugzeug(idFlugzeug);

      if (MeinGemietetesFlugzeug != null) {
        StartOn = true;
        onResetTankstelle();

        frmStationsName = selectedStation.getNameRettungsstatione();
        frmStationBreitenGrad = selectedStation.getBreitengrad();
        frmStationLaengenGrad = selectedStation.getLaengengrad();

        istAuftragVorhanden = false;

      }
    }

  }

  public List<Rettungsstationen> getStationen() {
    if (!isLoadedStationen) {
      stationenList = rettungsFacade.getAlleStationen();
      isLoadedStationen = true;
    }

    return stationenList;
  }

  public List<Rettungseinsaetze> getEinsaetze() {
    if (!istLoadedEinsatzListe) {
      EinsatzList = rettungsFacade.getAlleEinSaetze(selectedStation.getIdRettungsstationen());
      istLoadedEinsatzListe = true;
    }

    return EinsatzList;

  }

  public String getVisuelleRout() {
    String ergebnis = "";
    String vonPosition = "";
    String nachPosition = "";

    vonPosition = frmStationBreitenGrad + ", " + frmStationLaengenGrad;
    nachPosition = frmUnfallBreitenGrad + ", " + frmUnfallLaengenGrad;
    ergebnis = ergebnis + addLines(vonPosition, nachPosition);

    vonPosition = frmUnfallBreitenGrad + ", " + frmUnfallLaengenGrad;
    nachPosition = frmZielBreitenGrad + ", " + frmZielLaengenGrad;
    ergebnis = ergebnis + addLines(vonPosition, nachPosition);

    vonPosition = frmZielBreitenGrad + ", " + frmZielLaengenGrad;
    nachPosition = frmStationBreitenGrad + ", " + frmStationLaengenGrad;
    ergebnis = ergebnis + addLines(vonPosition, nachPosition);

    ergebnis = "var multiCoords1 = [\n"
            + ergebnis
            + "    ];\n"
            + "    var plArray = [];\n"
            + "    for(var i=0; i<multiCoords1.length; i++) {\n"
            + "        plArray.push(L.polyline(multiCoords1[i]).addTo(mapEinsatz));\n"
            + "    }\n"
            + "    L.polylineDecorator(multiCoords1, {\n"
            + "        patterns: [\n"
            + "            {offset: 25, repeat: 50, symbol: L.Symbol.arrowHead({pixelSize: 10, pathOptions: {fillOpacity: 0.5, weight: 2}})}\n"
            + "        ]\n"
            + "    }).addTo(mapEinsatz);";

    return ergebnis;

  }

  private String addLines(String Von, String Nach) {
    return "[[" + Von + "], [" + Nach + "]],\n";
  }

  /*
  ****************** Gemietetes Flugzeug BEGINN
   */
  public void MeinGemietetesFlugzeug(int MietKauf) {

    MeinGemietetesFlugzeug = rettungsFacade.getGemietetesFlugzeug(MietKauf);
    aktuelleTankfuellung = MeinGemietetesFlugzeug.getAktuelleTankfuellung();
    maxTankkapazitaet = MeinGemietetesFlugzeug.getTreibstoffkapazitaet();

    frmFlugzeugBreitenGrad = MeinGemietetesFlugzeug.getPositiionBreitenGrad();
    frmFlugzeugLaengenGrad = MeinGemietetesFlugzeug.getPositionLaengenGrad();

  }

  /*
  ****************** Gemietetes Flugzeug ENDE
   */
 /*
  *********** Auftragsbereich BEGINN
   */
  public void onPruefeAufLaufendenEinsatz() {

    Config();

    // Prüfung auf laufenden Einsatz
    lfdAuftrag = rettungsFacade.getLaufenderAuftrag(UserID);

    if (lfdAuftrag != null) {

      StartOn = false;

      Rettungsstationen lfdStation = rettungsFacade.getRettungsstationEinsatz(lfdAuftrag.getIdselectedstation());
      Rettungseinsaetze lfdEinsatz = rettungsFacade.getRettungsEinsatz(lfdAuftrag.getIdselectedauftrag());
      acp = rettungsFacade.readYAACARSFlight(UserID);

      selectedStation = lfdStation;
      selectedEinsatz = lfdEinsatz;

      istAuftragVorhanden = true;
      idFlugzeug = lfdAuftrag.getIdselectedheli();

      MeinGemietetesFlugzeug(idFlugzeug);

      onEinsatzDaten();

      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Flight-ON", "true");
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FlightLogo", "http://www.ftw-sim.de/images/FTW/helis/plane_" + 1 + ".png");
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FlugzeugType", "Air-Rescue \n" + MeinGemietetesFlugzeug.getType() );

    }

  }

  public void onNachAuftragSuchen() {
    if (!istAuftragVorhanden) {
      onEinsatzWaehlen();
    }
  }

  public void onAuftragAbbrechen() {

    istAuftragVorhanden = false;
    isLoadedStationen = false;
    istLoadedEinsatzListe = false;
    MeinGemietetesFlugzeug = null;
    selectedEinsatz = null;
    selectedStation = null;

    onEinsatzLoeschen();

  }

  private void onEinsatzWaehlen() {

    if (getEinsaetze() != null) {
      int MaxEinsaetze = getEinsaetze().size() - 1;
      int index = CONF.zufallszahl(0, MaxEinsaetze);
      selectedEinsatz = getEinsaetze().get(index);
      onEinsatzDaten();
      istAuftragVorhanden = true;
      onEinsatzSpeichern();
      onPruefeAufLaufendenEinsatz();
      txtEinsatzMeldung = "Aktueller Einsatzstatus.....";
      StartOn = false;
    }

  }

  public void onEinsatzDaten() {

    txtEinsatzMeldung = "Aktueller Einsatzstatus.....";

    onAktuellePositionACARSAuslesen();

    if (selectedEinsatz != null && selectedStation != null) {

      frmUnfallBreitenGrad = selectedEinsatz.getBreitengradUnfall();
      frmUnfallLaengenGrad = selectedEinsatz.getLaengengradUnfall();
      frmUnfallWPBezeichnung = selectedEinsatz.getBezeichnungunfall();

      frmZielBreitenGrad = selectedEinsatz.getBreitengradZiel();
      frmZielLaengenGrad = selectedEinsatz.getLaengengradZiel();
      frmZielWPBezeichnung = selectedEinsatz.getBezeichnungziel();

      frmStationsName = selectedStation.getNameRettungsstatione();
      frmStationWPBezeichnung = selectedStation.getNameRettungsstatione();
      frmStationBreitenGrad = selectedStation.getBreitengrad();
      frmStationLaengenGrad = selectedStation.getLaengengrad();

      frmPosTextUnfall = "<h2>" + "Einsatzort" + "</h2>"
              + "Waypoint: " + frmUnfallWPBezeichnung
              + "<br>Latitude: " + frmUnfallLaengenGrad
              + "<br>Longitude: " + frmUnfallBreitenGrad;

      frmPostTextZiel = "<h2>" + "Einsatzziel" + "</h2>"
              + "Waypoint: " + frmZielWPBezeichnung
              + "<br>Latitude: " + frmZielLaengenGrad
              + "<br>Longitude: " + frmZielBreitenGrad;

      frmPostTextStation = "<h2>" + "Einsatzstart" + "</h2>"
              + "Waypoint: " + frmStationWPBezeichnung
              + "<br>Latitude: " + frmStationLaengenGrad
              + "<br>Longitude: " + frmStationBreitenGrad;

      frmPosTextAktuellePosition = "<h2>Aktuelle Position</h2>"
              + "Latitude: " + frmAktPosLaengenGrad
              + "<br>Longitude: " + frmAktPosBreitenGrad;

      frmFlugzeugBreitenGrad = MeinGemietetesFlugzeug.getPositiionBreitenGrad();
      frmFlugzeugLaengenGrad = MeinGemietetesFlugzeug.getPositionLaengenGrad();

      GewichtTreibstoff = MeinGemietetesFlugzeug.getAktuelleTankfuellung();

      GewichtPassagiereTakeOff = 0.0;
      for (int i = 0; i < 3; i++) {
        GewichtPassagiereTakeOff = GewichtPassagiereTakeOff + CONF.zufallszahl(65, 85);
      }

      GewichtCargoTakeOff = 0;
      GewichtGepaeckTakeOff = 0;

      GewichtTotal = GewichtPassagiereTakeOff + GewichtGepaeckTakeOff + GewichtCargoTakeOff + GewichtTreibstoff;
    }

  }

  /*
   Neuen Einsatz erzeugen
   YAACARS Daten schreiben
   */
  private void onEinsatzSpeichern() {

    currentUser = rettungsFacade.readUser(UserID);

    AcarsRettungseinsatz einsatz = new AcarsRettungseinsatz();

    einsatz.setAircraft(MeinGemietetesFlugzeug.getType());
    einsatz.setAlternate("RETE");
    einsatz.setAltitude("");
    einsatz.setCargo(0);
    einsatz.setDeparture("RETE");
    einsatz.setDestination("RETE");
    einsatz.setFlightnumber("RET0001");
    einsatz.setIdftwUser(UserID);
    einsatz.setIdselectedauftrag(selectedEinsatz.getIdRettungseinsaetze());
    einsatz.setIdselectedheli(MeinGemietetesFlugzeug.getIdMietKauf());
    einsatz.setIdselectedstation(selectedStation.getIdRettungsstationen());
    einsatz.setPax(4);
    einsatz.setRoute("");
    einsatz.setRules("VFR");
    einsatz.setStandorterreicht(false);
    einsatz.setUnfallorterreicht(false);
    einsatz.setKlinikerreicht(false);

    rettungsFacade.createAuftrag(einsatz);

    int fgnr;
    int fgmax = 9999;

    Random FlightNumber = new Random();
    DecimalFormat flight = new DecimalFormat("####0000");

    Yaacarskopf arcarsFlight = new Yaacarskopf();

    arcarsFlight.setAlternateicao("RETT");
    arcarsFlight.setAlternatelatitude(selectedEinsatz.getBreitengradZiel());
    arcarsFlight.setAlternatelongitude(selectedEinsatz.getLaengengradZiel());

    arcarsFlight.setArrivalicao("RETT");
    arcarsFlight.setArrivallatitude(selectedEinsatz.getBreitengradUnfall());
    arcarsFlight.setArrivallongitude(selectedEinsatz.getLaengengradUnfall());

    arcarsFlight.setDepartureicao("RETT");
    arcarsFlight.setDeparturelatitude(selectedStation.getBreitengrad());
    arcarsFlight.setDeparturelongitude(selectedStation.getLaengengrad());

    arcarsFlight.setBlockzeit(0);
    arcarsFlight.setCargogewicht(0);
    arcarsFlight.setFlugerstelltam(new Date());

    do {
      fgnr = FlightNumber.nextInt(fgmax);
    } while (rettungsFacade.isFlightNummerExist("RET" + flight.format(fgnr)));

    arcarsFlight.setFlugnummer("RET" + flight.format(fgnr));
    arcarsFlight.setFlugroute("");
    //"flugstatus"
    //0 = angelegt
    //1 = gestartet
    //2 = beendet
    //
    //"missionsart"
    //1 = Normaler FTW-Flug
    //2 = Charter
    //3 = Rettung
    //4 = Missionen
    arcarsFlight.setFlugstatus(0);
    arcarsFlight.setMissionsart(3);
    arcarsFlight.setMissionsid(einsatz.getIdrettungseinsatz());

    arcarsFlight.setFlugzeugid(MeinGemietetesFlugzeug.getIdMietKauf());
    arcarsFlight.setFlugzeugkennung(MeinGemietetesFlugzeug.getRegistrierung());
    arcarsFlight.setFlugzeugtype(MeinGemietetesFlugzeug.getType());
    arcarsFlight.setGeflogenemeilen(0);
    arcarsFlight.setGeflogenezeit(0);
    arcarsFlight.setLetztepositionlatitude(MeinGemietetesFlugzeug.getPositiionBreitenGrad());
    arcarsFlight.setLetztepositionlongitude(MeinGemietetesFlugzeug.getPositionLaengenGrad());

    arcarsFlight.setPaxanzahl(3);
    arcarsFlight.setPaxgewicht(CONF.zufallszahl(180, 260));

    arcarsFlight.setProtokollversion("");
    arcarsFlight.setResttankmengekg(MeinGemietetesFlugzeug.getAktuelleTankfuellung());
    arcarsFlight.setTankmengekg(MeinGemietetesFlugzeug.getAktuelleTankfuellung());
    arcarsFlight.setUserhash(currentUser.getPasswort());
    arcarsFlight.setUserid(currentUser.getIdUser());
    arcarsFlight.setUsermessage("Luftrettung");
    arcarsFlight.setUsername(currentUser.getName1());
    arcarsFlight.setVerbrauchtetankmengekg(0);
    arcarsFlight.setFreitext("");

    rettungsFacade.createYAACARSFlight(arcarsFlight);

  }

  private void onEinsatzLoeschen() {

    Yaacarskopf yaacarsFlight = rettungsFacade.readYAACARSFlight(UserID);

    rettungsFacade.removeAktuellerEinsatz(UserID);
    rettungsFacade.removeYAACARSFlight(yaacarsFlight.getIdyaacarskopf());
    rettungsFacade.removeYAACARSPositionen(yaacarsFlight.getIdyaacarskopf());

    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Flight-ON", "false");
  }

  public void onAcarsUeberwachung() {

    meldung = "";
    onAktuellePositionACARSAuslesen();
    onUnfallOrt();
    onKlinikOrt();
    onStationsOrt();

  }

  //// ********************* Flugdaten
  public void onAktuellePositionACARSAuslesen() {
    Yaacarskopf akt = rettungsFacade.readYAACARSFlight(UserID);
    if (akt != null) {
      if (akt.getFlugstatus() > 0) {
        Yaacarspositionen aktpos = rettungsFacade.readLetzteYAACARSPosition(akt.getIdyaacarskopf());
        if (aktpos != null) {
          frmAktPosLaengenGrad = aktpos.getLongitude();
          frmAktPosBreitenGrad = aktpos.getLatitude();
          currentFlightMiles = aktpos.getGeflogeneentfernung();
          Geschwindigkeit = aktpos.getTas();
          txtAktuellerKurs = dfkurs.format(aktpos.getHeading());

          FlugKurs = aktpos.getHeading();
        }
      } else {
        frmAktPosLaengenGrad = akt.getLetztepositionlongitude();
        frmAktPosBreitenGrad = akt.getLetztepositionlatitude();
      }
    }

    try {
      if (MeinGemietetesFlugzeug.getFlugzeugArt().equals("Hubschrauber")) {
        IconFuerAircraft = "http://www.ftw-sim.de/images/FTW/helis/plane_" + FlugKurs + ".png";
      } else {
        IconFuerAircraft = "http://www.ftw-sim.de/images/FTW/kleinflugzeug/plane_" + FlugKurs + ".png";
      }
    } catch (NullPointerException e) {
      IconFuerAircraft = "http://www.ftw-sim.de/images/FTW/kleinflugzeug/plane_" + FlugKurs + ".png";
    }

  }

  public void onUnfallOrt() {

    if (OrtOK(frmAktPosLaengenGrad, frmAktPosBreitenGrad, frmUnfallLaengenGrad, frmUnfallBreitenGrad)) {
      meldung = "Unfallort erreicht";
    }

    if (!lfdAuftrag.getUnfallorterreicht()) {
      if (OrtOK(frmAktPosLaengenGrad, frmAktPosBreitenGrad, frmUnfallLaengenGrad, frmUnfallBreitenGrad)) {
        lfdAuftrag.setUnfallorterreicht(true);
        rettungsFacade.editAuftrag(lfdAuftrag);
        NewMessage("Unfallort erreicht: Patient wird eingeladen");
        meldung = "Unfallort erreicht: Patient wird eingeladen";
      }
    }

    if (frmAktPosLaengenGrad == 0.0) {
      txtEntfernung = "No data available....";
      txtKurs = "---";
    } else if (!lfdAuftrag.getUnfallorterreicht()) {
      int ergebnis[] = CONF.DistanzBerechnung(frmAktPosLaengenGrad, frmAktPosBreitenGrad, frmUnfallLaengenGrad, frmUnfallBreitenGrad);

      txtEntfernung = dfmiles.format(ergebnis[0]) + " Mile(s)";
      txtKurs = dfkurs.format(ergebnis[1]);
      txtRestzeit = RestZeit(ergebnis[0]);

    }

  }

  public void onKlinikOrt() {

    if (OrtOK(frmAktPosLaengenGrad, frmAktPosBreitenGrad, frmZielLaengenGrad, frmZielBreitenGrad)) {
      meldung = "Übergabepunkt erreicht";
    }

    if (!lfdAuftrag.getKlinikerreicht() && lfdAuftrag.getUnfallorterreicht()) {
      if (OrtOK(frmAktPosLaengenGrad, frmAktPosBreitenGrad, frmZielLaengenGrad, frmZielBreitenGrad)) {
        lfdAuftrag.setKlinikerreicht(true);
        rettungsFacade.editAuftrag(lfdAuftrag);

        NewMessage("Klinik erreicht: Patient wird von den Ärzten übernommen");
        meldung = "Klinik erreicht: Patient wird von den Ärzten übernommen";
      }
    }

    if (!lfdAuftrag.getKlinikerreicht() && lfdAuftrag.getUnfallorterreicht()) {
      int ergebnis[] = CONF.DistanzBerechnung(frmAktPosLaengenGrad, frmAktPosBreitenGrad, frmZielLaengenGrad, frmZielBreitenGrad);

      txtEntfernung = dfmiles.format(ergebnis[0]) + " Mile(s)";
      txtKurs = dfkurs.format(ergebnis[1]);
      txtRestzeit = RestZeit(ergebnis[0]);
    }

  }

  public void onStationsOrt() {

    if (OrtOK(frmAktPosLaengenGrad, frmAktPosBreitenGrad, frmStationLaengenGrad, frmStationBreitenGrad)) {
      meldung = "Du stehst an deiner Homebase";
    }

    if (!lfdAuftrag.getStandorterreicht() && lfdAuftrag.getKlinikerreicht()) {
      if (OrtOK(frmAktPosLaengenGrad, frmAktPosBreitenGrad, frmStationLaengenGrad, frmStationBreitenGrad)) {
        lfdAuftrag.setStandorterreicht(true);
        rettungsFacade.editAuftrag(lfdAuftrag);
        NewMessage("Du hast den Einsatz erfolgreich beendet, YAACARS Flug beenden und Einsatz abschließen.");
        meldung = "Du hast den Einsatz erfolgreich beendet, YAACARS Flug beenden und Einsatz abschließen.<br>Check den Helikopter für den nächsten Einsatz.";
      }

      int ergebnis[] = CONF.DistanzBerechnung(frmAktPosLaengenGrad, frmAktPosBreitenGrad, frmStationLaengenGrad, frmStationBreitenGrad);

      txtEntfernung = dfmiles.format(ergebnis[0]) + " Mile(s)";
      txtKurs = dfkurs.format(ergebnis[1]);
      txtRestzeit = RestZeit(ergebnis[0]);

    }

//    if (!lfdAuftrag.getStandorterreicht() && lfdAuftrag.getKlinikerreicht()) {
//    }
  }

  private String RestZeit(int Entfernung) {
    try {
      return dfmiles.format(Entfernung / (Geschwindigkeit / 60)) + " minutes";
    } catch (Exception ex) {
      return "---";
    }
  }

  private boolean OrtOK(double laengAkt, double breitAkt, double einsatzLaeng, double einsatzBreit) {

    // Änderung 05.05.2020, alter Wert = 0.0025
    double pruefWertPositiv = 0.0025;
    double pruefWertNegativ = -0.0025;

    boolean BreitenGradOk = false;
    boolean LaengenGradOk = false;

    double difLaengengrad = einsatzLaeng - laengAkt;
    double difBreitengrad = einsatzBreit - breitAkt;

    if (difLaengengrad > 0) {
      if (difLaengengrad < pruefWertPositiv) {
        LaengenGradOk = true;
      }
    }

    if (difBreitengrad > 0) {
      if (difBreitengrad < pruefWertPositiv) {
        BreitenGradOk = true;
      }
    }

    if (difLaengengrad < 0) {
      if (difLaengengrad > pruefWertNegativ) {
        LaengenGradOk = true;
      }
    }

    if (difBreitengrad < 0) {
      if (difBreitengrad > pruefWertNegativ) {
        BreitenGradOk = true;
      }
    }

    if (BreitenGradOk && LaengenGradOk) {
      return true;
    }

    return false;

  }

  public void onAbrechnung() {

    if (lfdAuftrag.getUnfallorterreicht() & lfdAuftrag.getStandorterreicht() & lfdAuftrag.getKlinikerreicht()) {

      acp = rettungsFacade.readYAACARSFlight(UserID);

      if (acp != null) {
        //"flugstatus"
        //0 = angelegt
        //1 = gestartet
        //2 = beendet
        //
        if (acp.getFlugstatus() == 2) {
          double betrag = CONF.zufallszahl(8000, 15000);

          //betrag = betrag + (currentFlightMiles * 100.0);
          //********** auf vollen Betrag runden
          betrag = Math.round(betrag * selectedEinsatz.getPreisfaktor());

          onAktuellePositionACARSAuslesen();

          //************* Fluglog schreiben
          createLogbuchEintrag(betrag);

          //************* Flugzeuglog schreiben
          aircraftUpdate();

          //************* Userlog schreiben
          userUpdate();

          //*********************** Bankbuchung
          String UserName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserName");
          String UserKonto = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("BankKonto");

          String VerwendungsZweck = loginMB.onSprache("Heliport_msg_Bankbuchung_Bezahlung") + " " + selectedEinsatz.getKurzbezeichnung();

          SaveBankbuchung(UserKonto, UserName, "300-1000001", "*** FTW Buchungssystem ***",
                  new Date(), betrag, UserKonto, UserName,
                  new Date(), VerwendungsZweck, UserID, -1, -1, -1, -1, -1, -1, "", -1, MeinGemietetesFlugzeug.getIdMietKauf(), 0, UserID);

          onAuftragAbbrechen();
        } else {
          NewMessage("YAACARS Flug noch nicht beendet");
        }

      } else {
        NewMessage("Keine Flugdaten vorhanden");
      }

    } else {
      NewMessage("Einsatzziele nicht erreicht");
    }

  }

  /*
  *********** Auftragsbereich ENDE
   */
 /*
  *********** Statistik Start
   */
  private void userUpdate() {

    Benutzer currentUser = rettungsFacade.readUser(UserID);

    int FlightMiles = currentUser.getFlightmiles();
    int Flights = currentUser.getFlights();
    int FlightTimeCur = currentUser.getFlighttime();
    int FlightPaxes = currentUser.getFlightpaxes();
    int FlightCargo = currentUser.getFlightcargo();
    int FlightRettung = currentUser.getFlightrettung();

    FlightMiles = FlightMiles + currentFlightMiles;
    Flights = Flights + 1;
    FlightTimeCur = FlightTimeCur + acp.getGeflogenezeit();
    FlightPaxes = FlightPaxes + 3;
    FlightCargo = FlightCargo + 0;
    FlightRettung = FlightRettung + 1;

    currentUser.setFlightmiles(FlightMiles);
    currentUser.setFlights(Flights);
    currentUser.setFlighttime(FlightTimeCur);
    currentUser.setFlightpaxes(FlightPaxes);
    currentUser.setFlightcargo(FlightCargo);
    currentUser.setFlightrettung(FlightRettung);
    rettungsFacade.editUser(currentUser);
  }

  private void aircraftUpdate() {

    Flugzeugemietkauf MeinFlugzeug = rettungsFacade.getFlugzeug(MeinGemietetesFlugzeug.getIdMietKauf());

    int FlightMiles = MeinGemietetesFlugzeug.getGesamtEntfernung();
    int Flights = MeinGemietetesFlugzeug.getGesamtFluege();
    int FlightTimeCur = MeinGemietetesFlugzeug.getGesamtFlugzeit();
    int FlightPaxes = MeinGemietetesFlugzeug.getGesamtTransportiertePassagiere();
    int FlightCargo = MeinGemietetesFlugzeug.getGesamtTransportiertesCargo();
    int TotalEngineTime = MeinGemietetesFlugzeug.getMaschinenLaufzeitMinuten();
    int LastCheck = MeinGemietetesFlugzeug.getLetzterCheckMinuten();
    int AirFrame = MeinGemietetesFlugzeug.getGesamtMaschinenLaufzeitMinuten();
    int GesamtAlter = MeinGemietetesFlugzeug.getGesamtAlterDesFlugzeugsMinuten();
    int Tankinhalt = MeinGemietetesFlugzeug.getAktuelleTankfuellung() - Math.round(acp.getVerbrauchtetankmengekg());

    FlightMiles = FlightMiles + currentFlightMiles;
    Flights = Flights + 1;
    FlightTimeCur = FlightTimeCur + acp.getGeflogenezeit();
    FlightPaxes = FlightPaxes + 3;
    FlightCargo = FlightCargo + 0;
    TotalEngineTime = TotalEngineTime + acp.getGeflogenezeit();
    AirFrame = AirFrame + acp.getGeflogenezeit();
    LastCheck = LastCheck + acp.getGeflogenezeit();
    GesamtAlter = GesamtAlter + acp.getGeflogenezeit();

    MeinFlugzeug.setGesamtEntfernung(FlightMiles);
    MeinFlugzeug.setGesamtFluege(Flights);
    MeinFlugzeug.setGesamtFlugzeit(FlightTimeCur);
    MeinFlugzeug.setGesamtTransportiertePassagiere(FlightPaxes);
    MeinFlugzeug.setGesamtTransportiertesCargo(FlightCargo);
    MeinFlugzeug.setMaschinenLaufzeitMinuten(TotalEngineTime);
    MeinFlugzeug.setGesamtMaschinenLaufzeitMinuten(AirFrame);
    MeinFlugzeug.setGesamtAlterDesFlugzeugsMinuten(GesamtAlter);
    MeinFlugzeug.setLetzterCheckMinuten(LastCheck);
    MeinFlugzeug.setAktuelleTankfuellung(Tankinhalt);
    MeinFlugzeug.setIstInDerLuft(false);

    rettungsFacade.editMeinFlugzeug(MeinFlugzeug);

  }

  private void createLogbuchEintrag(double Betrag) {

    Fluglogbuch newLog = new Fluglogbuch();

    //"missionsart"
    //1 = Normaler FTW-Flug
    //2 = Charter
    //3 = Rettung
    //4 = Missionen
    newLog.setAcarsFlugNummer(acp.getFlugnummer());
    newLog.setMissionsart(3);
    newLog.setBodenpersonalgebuehr(0.0);
    newLog.setBonus1(0.0);
    newLog.setBonus2(0.0);
    newLog.setBuchungsgebuehr(Betrag);
    newLog.setCargo(0);
    newLog.setCrewgebuehren(0.0);
    newLog.setFlugDatum(new Date());
    newLog.setFromicao(acp.getDepartureicao().toUpperCase());
    newLog.setToicao(acp.getArrivalicao().toUpperCase());
    newLog.setIdAirline(-1);
    newLog.setIdaircraft(MeinGemietetesFlugzeug.getIdMietKauf());
    newLog.setIduser(UserID);
    newLog.setLandegebuehr(0.0);
    newLog.setMietgebuehr(0.0);
    newLog.setMiles(currentFlightMiles);
    newLog.setFlugzeitMinuten(acp.getGeflogenezeit());
    newLog.setPax(3);
    newLog.setSicherheitsgebuehr(0.0);
    newLog.setSummeabrechnung(Betrag);
    newLog.setSummekosten(0.0);
    newLog.setTreibstoffkosten(0.0);
    newLog.setTreibstoffverbrauchkg(Math.round(acp.getVerbrauchtetankmengekg()));
    newLog.setIdArrivalTerminal(-1);
    newLog.setBetragAriivalTerminal(0.0);
    newLog.setIdDepartureTerminal(-1);
    newLog.setBetragDepartureTerminal(0.0);
    newLog.setTocaoFlughafenName("Rettungsstation");
    newLog.setFromIcaoFlughafenName("Rettungsstation");
    newLog.setProvision(0.0);
    newLog.setFixkosten(0.0);

    rettungsFacade.createLogbuchEintrag(newLog);
  }

  /*
  *********** Statistik ENDE
   */
 /*
  ************************ Tankstelle BEGINN
   */
  private void YAACARSFlugAnpassen(int Tankfuellung) {
    // YAACARS Flug Anpassen, wenn gestartet
    // Nach dem Tanken muss auch die Tankmenge im YAACARSKopf angepasst werden.
    Yaacarskopf yaacarsFlight = rettungsFacade.readYAACARSFlight(UserID);
    if (yaacarsFlight != null) {
      yaacarsFlight.setTankmengekg(Tankfuellung);
      yaacarsFlight.setResttankmengekg(Tankfuellung);
      rettungsFacade.editYAACARSFlight(yaacarsFlight);
    }
  }

  public void onResetTankstelle() {
    TankMengeProzent = 0.0;
    TankMengeKilo = 0;
    TankBisFuellstandKG = 0;
    aktuelleTankfuellung = MeinGemietetesFlugzeug.getAktuelleTankfuellung();
    maxTankkapazitaet = MeinGemietetesFlugzeug.getTreibstoffkapazitaet();
    TankstellenGrafik = "http://www.ftw-sim.de/images/FTW/images/tankstelle.png";

    MeinGemietetesFlugzeug(idFlugzeug);

    berechneTankfuellung();

  }

  public void berechneTankfuellung() {
    double PreisAVGAS = Config.getPreisAVGASkg();
    double PreisJETA = Config.getPreisJETAkg();

    // FTW - Tankstelle
    if (MeinGemietetesFlugzeug.getTreibstoffArt() == 1) {
      setTankstelleSumme((double) TankMengeKilo * PreisAVGAS);
      setTankstelleMengeLbs((int) getKG2Libs(TankMengeKilo));
    } else {
      setTankstelleSumme((double) TankMengeKilo * PreisJETA);
      setTankstelleMengeLbs((int) getKG2Libs(TankMengeKilo));
    }

  }

  public void ontankenStart() {
    onResetTankstelle();
  }

  public void onTankenBerechneProzentTankfüllung() {

    TankBisFuellstandKG = (int) (maxTankkapazitaet / 100.0 * TankMengeProzent);

    TankMengeKilo = TankBisFuellstandKG - aktuelleTankfuellung;

    onTankenBerechneKilo();
  }

  public void onTankenBerechneKilo() {

    int inhalt = aktuelleTankfuellung + TankMengeKilo;

    if (inhalt > maxTankkapazitaet) {
      inhalt = maxTankkapazitaet;
    }

    if (inhalt < 0) {
      inhalt = 0;
    }

    TankBisFuellstandKG = inhalt;

    onTankenBerechneBisFüllstandKg();
  }

  public void onTankenBerechneBisFüllstandKg() {

    boolean tanken = true;

    if (TankBisFuellstandKG < 0) {
      tanken = false;
      NewMessage("Füllstand muss größer 0 sein");
      onResetTankstelle();
    }

    if (tanken) {
      if (TankBisFuellstandKG >= aktuelleTankfuellung) {
        TankMengeKilo = TankBisFuellstandKG - aktuelleTankfuellung;
      } else {
        TankMengeKilo = aktuelleTankfuellung - TankBisFuellstandKG;
        TankMengeKilo = TankMengeKilo - (TankMengeKilo * 2);

        if (TankMengeKilo + aktuelleTankfuellung < 0) {
          TankMengeKilo = TankMengeKilo - (aktuelleTankfuellung * 2);
        }
      }

      if (aktuelleTankfuellung + TankMengeKilo > maxTankkapazitaet) {
        TankMengeKilo = maxTankkapazitaet - aktuelleTankfuellung;
        TankBisFuellstandKG = maxTankkapazitaet;
      }

      TankMengeProzent = 100.0 / maxTankkapazitaet * TankBisFuellstandKG;

      //******************************* Summenbildung
      berechneTankfuellung();
    }
  }

  public void onTankenBezahlen() {
    onTankenBerechneKilo();
    Flugzeugemietkauf flugzeug = rettungsFacade.getFlugzeug(MeinGemietetesFlugzeug.getIdMietKauf());
    flugzeug.setAktuelleTankfuellung(TankMengeKilo + aktuelleTankfuellung);
    rettungsFacade.editMeinFlugzeug(flugzeug);
    YAACARSFlugAnpassen(flugzeug.getAktuelleTankfuellung());
    onResetTankstelle();
  }

  public boolean isTankstelleGesperrt() {

    tankstelleGesperrt = false;

    //"flugstatus"
    //0 = angelegt
    //1 = gestartet
    //2 = beendet
    //
    Yaacarskopf yaacarsFlight = rettungsFacade.readYAACARSFlight(UserID);
    if (yaacarsFlight != null) {
      if (yaacarsFlight.getFlugstatus() > 0) {
        tankstelleGesperrt = true;
      } else {
        tankstelleGesperrt = false;
      }
    } else {
      tankstelleGesperrt = false;
    }

    return tankstelleGesperrt;
  }

  /*
  ************************ Tankstelle ENDE
   */
  private void NewMessage(String neueNachricht) {
    FacesMessage msg = new FacesMessage(neueNachricht);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  /*
  *********** Setter and Getter
   */
  public Rettungsstationen getSelectedStation() {
    return selectedStation;
  }

  public void setSelectedStation(Rettungsstationen selectedStation) {
    this.selectedStation = selectedStation;
  }

  public String getTankstellenGrafik() {
    if (MeinGemietetesFlugzeug != null) {
      return MeinGemietetesFlugzeug.getSymbolUrl();
    } else {
      return "";
    }

  }

  public void setTankstellenGrafik(String TankstellenGrafik) {
    this.TankstellenGrafik = TankstellenGrafik;
  }

  public double getTankstelleSumme() {
    return TankstelleSumme;
  }

  public void setTankstelleSumme(double TankstelleSumme) {
    this.TankstelleSumme = TankstelleSumme;
  }

  public int getTankstelleMengeLbs() {
    return TankstelleMengeLbs;
  }

  public void setTankstelleMengeLbs(int TankstelleMengeLbs) {
    this.TankstelleMengeLbs = TankstelleMengeLbs;
  }

  public double getKG2Libs(int Gewicht) {
    return (double) Gewicht * 2.204622915;
  }

  public double getLibs2KG(int Gewicht) {
    return (double) Gewicht * 0.453592309685;
  }

  public int getTankMengeKilo() {
    return TankMengeKilo;
  }

  public void setTankMengeKilo(int TankMengeKilo) {
    this.TankMengeKilo = TankMengeKilo;
  }

  public double getTankMengeProzent() {
    return TankMengeProzent;
  }

  public void setTankMengeProzent(double TankMengeProzent) {
    this.TankMengeProzent = TankMengeProzent;
  }

  public int getTankBisFuellstandKG() {
    return TankBisFuellstandKG;
  }

  public void setTankBisFuellstandKG(int TankBisFuellstandKG) {
    this.TankBisFuellstandKG = TankBisFuellstandKG;
  }

  public int getMaxTankkapazitaet() {
    return maxTankkapazitaet;
  }

  public void setMaxTankkapazitaet(int maxTankkapazitaet) {
    this.maxTankkapazitaet = maxTankkapazitaet;
  }

  public boolean isStartOn() {
    return StartOn;
  }

  public void setStartOn(boolean StartOn) {
    this.StartOn = StartOn;
  }

  public int getAktuelleTankfuellung() {
    return aktuelleTankfuellung;
  }

  public void setAktuelleTankfuellung(int aktuelleTankfuellung) {
    this.aktuelleTankfuellung = aktuelleTankfuellung;
  }

  public boolean isIstAuftragVorhanden() {
    return istAuftragVorhanden;
  }

  public void setIstAuftragVorhanden(boolean istAuftragVorhanden) {
    this.istAuftragVorhanden = istAuftragVorhanden;
  }

  public int getInterval() {
    return interval;
  }

  public Rettungseinsaetze getSelectedEinsatz() {
    return selectedEinsatz;
  }

  public void setSelectedEinsatz(Rettungseinsaetze selectedEinsatz) {
    this.selectedEinsatz = selectedEinsatz;
  }

  public String getFrmStationsName() {
    return frmStationsName;
  }

  public void setFrmStationsName(String frmStationsName) {
    this.frmStationsName = frmStationsName;
  }

  public double getFrmFlugzeugBreitenGrad() {
    return frmFlugzeugBreitenGrad;
  }

  public void setFrmFlugzeugBreitenGrad(double frmFlugzeugBreitenGrad) {
    this.frmFlugzeugBreitenGrad = frmFlugzeugBreitenGrad;
  }

  public double getFrmFlugzeugLaengenGrad() {
    return frmFlugzeugLaengenGrad;
  }

  public void setFrmFlugzeugLaengenGrad(double frmFlugzeugLaengenGrad) {
    this.frmFlugzeugLaengenGrad = frmFlugzeugLaengenGrad;
  }

  public double getFrmUnfallBreitenGrad() {
    return frmUnfallBreitenGrad;
  }

  public void setFrmUnfallBreitenGrad(double frmUnfallBreitenGrad) {
    this.frmUnfallBreitenGrad = frmUnfallBreitenGrad;
  }

  public double getFrmUnfallLaengenGrad() {
    return frmUnfallLaengenGrad;
  }

  public void setFrmUnfallLaengenGrad(double frmUnfallLaengenGrad) {
    this.frmUnfallLaengenGrad = frmUnfallLaengenGrad;
  }

  public double getFrmZielBreitenGrad() {
    return frmZielBreitenGrad;
  }

  public void setFrmZielBreitenGrad(double frmZielBreitenGrad) {
    this.frmZielBreitenGrad = frmZielBreitenGrad;
  }

  public double getFrmZielLaengenGrad() {
    return frmZielLaengenGrad;
  }

  public void setFrmZielLaengenGrad(double frmZielLaengenGrad) {
    this.frmZielLaengenGrad = frmZielLaengenGrad;
  }

  public double getFrmStationBreitenGrad() {
    return frmStationBreitenGrad;
  }

  public void setFrmStationBreitenGrad(double frmStationBreitenGrad) {
    this.frmStationBreitenGrad = frmStationBreitenGrad;
  }

  public double getFrmStationLaengenGrad() {
    return frmStationLaengenGrad;
  }

  public double getFrmAktPosBreitenGrad() {
    return frmAktPosBreitenGrad;
  }

  public double getFrmAktPosLaengenGrad() {
    return frmAktPosLaengenGrad;
  }

  public void setFrmStationLaengenGrad(double frmStationLaengenGrad) {
    this.frmStationLaengenGrad = frmStationLaengenGrad;
  }

  public String getFrmPosTextUnfall() {
    return frmPosTextUnfall;
  }

  public String getFrmPostTextStation() {
    return frmPostTextStation;
  }

  public String getFrmPostTextZiel() {
    return frmPostTextZiel;
  }

  public String getFrmPosTextAktuellePosition() {
    return frmPosTextAktuellePosition;
  }

  public void setFrmPosTextAktuellePosition(String frmPosTextAktuellePosition) {
    this.frmPosTextAktuellePosition = frmPosTextAktuellePosition;
  }

  public double getGewichtPassagiereTakeOff() {
    return GewichtPassagiereTakeOff;
  }

  public double getGewichtGepaeckTakeOff() {
    return GewichtGepaeckTakeOff;
  }

  public double getGewichtCargoTakeOff() {
    return GewichtCargoTakeOff;
  }

  public double getGewichtTreibstoff() {
    return GewichtTreibstoff;
  }

  public double getGewichtTotal() {
    return GewichtTotal;
  }

  public AcarsRettungseinsatz getLfdAuftrag() {
    return lfdAuftrag;
  }

  public String getMeldung() {
    return meldung;
  }

  public int getAcarsUeberwachungTimer() {
    return AcarsUeberwachungTimer;
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

    rettungsFacade.createBankbuchung(newBuchung);

  }

  public int getZoom() {
    return Zoom;
  }

  public void setZoom(int Zoom) {
    this.Zoom = Zoom;
  }

  public String getTxtEinsatzMeldung() {
    return txtEinsatzMeldung;
  }

  public void setTxtEinsatzMeldung(String txtEinsatzMeldung) {
    this.txtEinsatzMeldung = txtEinsatzMeldung;
  }

  public String getTxtKurs() {
    return txtKurs;
  }

  public void setTxtKurs(String txtKurs) {
    this.txtKurs = txtKurs;
  }

  public String getTxtEntfernung() {
    return txtEntfernung;
  }

  public void setTxtEntfernung(String txtEntfernung) {
    this.txtEntfernung = txtEntfernung;
  }

  public String getTxtRestzeit() {
    return txtRestzeit;
  }

  public void setTxtRestzeit(String txtRestzeit) {
    this.txtRestzeit = txtRestzeit;
  }

  public String getTxtAktuellerKurs() {
    return txtAktuellerKurs;
  }

  public void setTxtAktuellerKurs(String txtAktuellerKurs) {
    this.txtAktuellerKurs = txtAktuellerKurs;
  }

  public Yaacarskopf getAcp() {
    return acp;
  }

  public boolean isAcarsFlugBeendet() {

    acarsFlugBeendet = false;

    //"flugstatus"
    //0 = angelegt
    //1 = gestartet
    //2 = beendet
    Yaacarskopf flight = rettungsFacade.readYAACARSFlight(UserID);

    if (flight != null) {
      if (flight.getFlugstatus() == 2) {
        acarsFlugBeendet = true;
      }
    }

    return acarsFlugBeendet;
  }

  public void setAcarsFlugBeendet(boolean acarsFlugBeendet) {
    this.acarsFlugBeendet = acarsFlugBeendet;
  }

  public int getFlugKurs() {
    return FlugKurs;
  }

  public void setFlugKurs(int FlugKurs) {
    this.FlugKurs = FlugKurs;
  }

  public String getIconFuerAircraft() {
    return IconFuerAircraft;
  }

  public void setIconFuerAircraft(String IconFuerAircraft) {
    this.IconFuerAircraft = IconFuerAircraft;
  }

}
