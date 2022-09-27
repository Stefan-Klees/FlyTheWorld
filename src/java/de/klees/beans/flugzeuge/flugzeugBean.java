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

import de.klees.beans.system.CONF;
import de.klees.data.Feinabstimmung;
import de.klees.data.Fluggesellschaft;
import de.klees.data.Flugzeuge;
import de.klees.data.Flugzeugefluggesellschaft;
import de.klees.data.Flugzeugemietkauf;
import de.klees.data.Flugzeugemodelle;
import de.klees.data.Flugzeugsymbole;
import de.klees.data.Sitzkonfiguration;
import de.klees.data.views.ViewFlugzeugVerbrauch;
import de.klees.data.views.ViewFlugzeugeMietKauf;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Calendar;
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
public class flugzeugBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<Flugzeuge> flugzeugListe;
  private List<Flugzeugemodelle> modellListe;
  private List<Flugzeugefluggesellschaft> fluggesellschaftFlugzeuge;
  private List<Flugzeugsymbole> flugzeugSymbole;

  private List<Sitzkonfiguration> ListKonfiguration;
  private Sitzkonfiguration currentKonfig;

  private Flugzeuge currentFlugzeug;
  private ViewFlugzeugeMietKauf currentFTWFlugzeug;

  private Fluggesellschaft FlugGesellschaft;

  private List<ViewFlugzeugeMietKauf> ftwMietKaufFlugzeuge;

  private Feinabstimmung config;

  private Flugzeugsymbole selectedSymbol;
  private Flugzeugemodelle selectedModel;

  private boolean isLoadedFlugzeugliste;
  private boolean isLoadedUserFlugzeuge;
  private boolean isLoadedFluggesellschaftFlugzeuge;
  private boolean isLoadedftwMietKaufFlugzeuge;

  private Map<String, String> TreibstoffArt;
  private Map<String, String> AntriebsArt;
  private Map<String, String> TriebwerksType;

  private String SucheBezeichnung;

  private String frmflugzeugTyp;
  private String frmflugzeugHersteller;
  private int frmerstflug;
  private int frmproduziertBis;
  private String frmhersteller;
  private String frmherstellerICAO;
  private String frmIcaoFlugzeugcode;
  private double frmverkaufspreis;
  private double frmherstellerpreis;
  private int frmsitzeEconomy;
  private int frmsitzeBusinessClass;
  private int frmmaxAnzahlAnZuBelegendenSitzen;
  private int frmbesatzung;
  private int frmflugbegleiter;
  private int frmpayload;
  private int frmcargo;
  private int frmhoechstAbfluggewicht;
  private int frmleergewicht;
  private int frmantriebsart;
  private String frmtriebwerkstype;
  private double frmtriebwerkspreis;
  private int frmanzahltriebwerke;
  private int frmtreibstoffkapazitaet;
  private int frmtreibstoffArt;
  private int frmverbrauchStunde;
  private int frmreisegeschwindigkeitTAS;
  private int frmhoechstgeschwindigkeitTAS;
  private int frmsteigleistung;
  private int frmstartstreckeBeiMTOW;
  private int frmmaxLandegewicht;
  private int frmmaxReichweite;
  private int frmmaxFlughoehe;
  private int frmmindestLandebahnLaenge;
  private int frmvApp;
  private int frmvlSpeed;
  private int frmlaenge;
  private int frmspannweite;
  private String frmlizenz;
  private String frmtypeRating;
  private double frmTypeRatingKostenStd;
  private int frmTypeRatingMinStd;
  private int frmAirframe;
  private String frmBemerkung;
  private boolean frminProduktion;
  private String frmsymbolUrl;
  private String frmxplaneFreeDownloadUrl;
  private String frmfsxFreeDownloadUrl;
  private String frmfs9FreeDownloadUrl;
  private String frmfs9PaywareDownloadUrl;
  private String frmp3dFreeDownloadUrl;
  private String frmxplanePaywareDownloadUrl;
  private String frmfsxPaywareDownloadUrl;
  private String frmp3dPayware3DownloadUrl;

  private String frmbilderUrl;
  private double frmstandardMietpreis;
  private int frmMaximumZeroFullWeight;
  private boolean frmauslieferung;
  private String frmFlugzeugArt;
  private double frmFixKosten;
  private double frmKalkulatorischerStundensatz;
  private boolean frmLangstreckenflugzeug;

  private double frmUmbaukosten;
  private int frmUmbauzeit;
  private boolean frmNurFuerUmbau;
  private int frmidUmbauModel;

  private boolean CreatedFromUser;

  //******* Varialblen fuer Bereitstellung
  private int brtAnzahl;
  private String brtKennung = "";
  private double brtNassMiete;
  private double brtTrockenMiete;
  private double brtVerkaufspreis;
  private String brtStehtInIcao;
  private int brtMietzeit;
  private int brtPfand;
  private int brtZellenstunden;
  private int brtBaujahr;
  private double brtZustand;
  private int brtTankfuellung;
  private boolean brtMietbar;
  private boolean brtKaufbar;

  //******* Varialblen fuer Modelle
  private String mdlModellbezeichnung;

  private int UserID;

  private Calendar ccheck = Calendar.getInstance();

  private final DecimalFormat nummer = new DecimalFormat("####0000");

  private List<Flugzeuge> FlugzeugListeOrdered;

  public flugzeugBean() {
    isLoadedFlugzeugliste = false;
    isLoadedftwMietKaufFlugzeuge = false;
    CreatedFromUser = false;
    SucheBezeichnung = "%";
    frmauslieferung = true;

    TreibstoffArt = new HashMap<>();
    TreibstoffArt.put("AVGAS 100LL", "1");
    TreibstoffArt.put("JETA", "2");

    AntriebsArt = new HashMap<>();
    AntriebsArt.put("Kolbenmotor", "1");
    AntriebsArt.put("Turboprop", "2");
    AntriebsArt.put("Strahltriebwerk", "3");

    brtKaufbar = true;
    brtMietbar = true;
    brtMietzeit = 1440;
    brtAnzahl = 1;
    brtZustand = 100;
    frmidUmbauModel = -1;
    UserID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");

    //    
    // Fälligkeit nächster C-Check
    //
    int tag = 1;
    int monat = ccheck.get(Calendar.MONTH);
    int jahr = ccheck.get(Calendar.YEAR);

    monat = monat + 6;

    if (monat > 12) {
      jahr = jahr + 1;
      monat = (monat - 12);
    }

    ccheck.set(jahr, monat, tag);

  }

  @EJB
  FlugzeugFacade facadeFlugzeug;

  public List<Flugzeuge> getFlugzeuge() {
    if (!isLoadedFlugzeugliste) {
      isLoadedFlugzeugliste = true;

      if (!frmNurFuerUmbau) {
        flugzeugListe = facadeFlugzeug.findAll();
      } else {
        flugzeugListe = facadeFlugzeug.findAllUmbau();
      }

    }
    return flugzeugListe;
  }

  public List<Flugzeuge> getFlugzeugeOrdered() {
    if (!isLoadedFlugzeugliste) {
      isLoadedFlugzeugliste = true;
      FlugzeugListeOrdered = facadeFlugzeug.findAllOrdered();
    }

    return FlugzeugListeOrdered;

  }

  public void onRefresh() {
    isLoadedFlugzeugliste = false;
  }

  public List<Flugzeuge> getFlugzeugeFuerUserbearbeitung() {
    if (!isLoadedFlugzeugliste) {
      isLoadedFlugzeugliste = true;
      flugzeugListe = facadeFlugzeug.findAllFuerUserbearbeitung();
    }
    return flugzeugListe;
  }

  public List<Flugzeugemodelle> getFlugzeugModell() {
    return facadeFlugzeug.findAllFlugzeugModelle();
  }

  public List<Flugzeugsymbole> getFlugzeugSymbole() {
    return facadeFlugzeug.findAllFlugzeugSymbole();
  }

  public void onSelectBoxChangeListener() {

  }

  public void onCreate() {

    if (facadeFlugzeug.ifExistFlugzeug(frmflugzeugTyp)) {
      NewMessage("Flugzeug bereits vorhanden : " + frmflugzeugTyp);
    } else {

      Flugzeuge newFlugzeug = new Flugzeuge();
      newFlugzeug.setHersteller(frmflugzeugHersteller);
      newFlugzeug.setIcaoFlugzeugcode(frmIcaoFlugzeugcode);
      newFlugzeug.setType(frmflugzeugTyp);
      newFlugzeug.setAntriebsart(frmantriebsart);
      newFlugzeug.setAnzahltriebwerke(frmanzahltriebwerke);
      newFlugzeug.setErstflug(frmerstflug);
      newFlugzeug.setProduziertBis(frmproduziertBis);
      newFlugzeug.setBesatzung(frmbesatzung);
      newFlugzeug.setFlugBegleiter(frmflugbegleiter);
      newFlugzeug.setBilderUrl(frmbilderUrl);
      newFlugzeug.setFsxFreeDownloadUrl(frmfsxFreeDownloadUrl);
      newFlugzeug.setFsxPaywareDownloadUrl(frmfsxPaywareDownloadUrl);
      newFlugzeug.setHerstellerICAO(frmherstellerICAO);
      newFlugzeug.setHerstellerpreis(frmherstellerpreis);
      newFlugzeug.setHoechstAbfluggewicht(frmhoechstAbfluggewicht);
      newFlugzeug.setHoechstgeschwindigkeitTAS(frmhoechstgeschwindigkeitTAS);
      newFlugzeug.setLaenge(frmlaenge);
      newFlugzeug.setLeergewicht(frmleergewicht);
      newFlugzeug.setLizenz(frmlizenz);
      newFlugzeug.setTypeRating(frmtypeRating);
      newFlugzeug.setTypeRatingKostenStd(frmTypeRatingKostenStd);
      newFlugzeug.setTypeRatingMinStd(frmTypeRatingMinStd);
      newFlugzeug.setMaxFlughoehe(frmmaxFlughoehe);
      newFlugzeug.setMaxLandegewicht(frmmaxLandegewicht);
      newFlugzeug.setMaxReichweite(frmmaxReichweite);
      newFlugzeug.setMindestLandebahnLaenge(frmmindestLandebahnLaenge);
      newFlugzeug.setPayload(frmpayload);
      newFlugzeug.setCargo(frmcargo);
      newFlugzeug.setReisegeschwindigkeitTAS(frmreisegeschwindigkeitTAS);
      newFlugzeug.setSitzeBusinessClass(frmsitzeBusinessClass);
      newFlugzeug.setSitzeEconomy(frmsitzeEconomy);
      newFlugzeug.setMaxAnzahlZuBelgenderSitze(frmmaxAnzahlAnZuBelegendenSitzen);
      newFlugzeug.setSpannweite(frmspannweite);
      newFlugzeug.setStartstreckeBeiMTOW(frmstartstreckeBeiMTOW);
      newFlugzeug.setSteigleistung(frmsteigleistung);
      newFlugzeug.setSymbolUrl(frmsymbolUrl);
      newFlugzeug.setTreibstoffArt(frmtreibstoffArt);
      newFlugzeug.setTreibstoffkapazitaet(frmtreibstoffkapazitaet);
      newFlugzeug.setTriebwerkspreis(frmtriebwerkspreis);
      newFlugzeug.setVApp(frmvApp);
      newFlugzeug.setVerbrauchStunde(frmverbrauchStunde);
      newFlugzeug.setVerkaufspreis(frmverkaufspreis);
      newFlugzeug.setVlSpeed(frmvlSpeed);
      newFlugzeug.setAirframe(0);
      newFlugzeug.setBemerkungen(frmBemerkung);
      newFlugzeug.setAuslieferung(frmauslieferung);
      newFlugzeug.setFlugzeugArt(frmFlugzeugArt);

      newFlugzeug.setP3dFreeDownloadUrl(frmp3dFreeDownloadUrl);
      newFlugzeug.setP3dPayware3DownloadUrl(frmp3dPayware3DownloadUrl);

      newFlugzeug.setXplaneFreeDownloadUrl(frmxplaneFreeDownloadUrl);
      newFlugzeug.setXplanePaywareDownloadUrl(frmxplanePaywareDownloadUrl);

      newFlugzeug.setFs9FreeDownloadUrl(frmfs9FreeDownloadUrl);
      newFlugzeug.setFs9PaywareDownloadUrl(frmfs9PaywareDownloadUrl);

      newFlugzeug.setIsUserEdit(CreatedFromUser);
      newFlugzeug.setInProduktion(frminProduktion);

      newFlugzeug.setStandardMietpreis(frmstandardMietpreis);
      newFlugzeug.setAuslieferung(false);
      newFlugzeug.setFixkosten(0.0);
      newFlugzeug.setKalkulatorischerStundensatz(frmKalkulatorischerStundensatz);
      newFlugzeug.setLangstreckenflugzeug(frmLangstreckenflugzeug);

      newFlugzeug.setUmbauzeit(frmUmbauzeit);
      newFlugzeug.setUmbaukosten(frmUmbaukosten);
      newFlugzeug.setIdUmbauModel(frmidUmbauModel);
      newFlugzeug.setNurFuerUmbau(frmNurFuerUmbau);

      facadeFlugzeug.createFlugzeug(newFlugzeug);
      setSelectedFlugzeug(newFlugzeug);
      setSucheBezeichnung(getSelectedFlugzeug().getType());

      NewMessage("Flugzeug erfolgreich angelegt");
      isLoadedFlugzeugliste = false;
    }
  }

  public void onCreateFromUser() {
    CreatedFromUser = true;
    isLoadedFlugzeugliste = false;
    onCreate();
  }

  public void onCreateModell() {
    Flugzeugemodelle newModel = new Flugzeugemodelle();
    newModel.setModellName(mdlModellbezeichnung);
    newModel.setIsAktive(true);
    facadeFlugzeug.createModell(newModel);

    NewMessage("Neues Modell wurde angelegt und steht zur Verfügung");

  }

  public void onSave() {

    if (currentFlugzeug != null) {

      if (currentFlugzeug.getNurFuerUmbau()) {
        currentFlugzeug.setIdUmbauModel(-1);
      }

      facadeFlugzeug.editFlugzeug(currentFlugzeug);
      NewMessage("Flugzeug gespeichert");
      isLoadedFlugzeugliste = false;
    }

  }

  public void onSaveAdmin() {

    if (currentFlugzeug != null) {

      if (currentFlugzeug.getNurFuerUmbau()) {
        currentFlugzeug.setIdUmbauModel(-1);
      }

      currentFlugzeug.setIsUserEdit(false);
      facadeFlugzeug.editFlugzeug(currentFlugzeug);
      NewMessage("Flugzeug gespeichert");
      isLoadedFlugzeugliste = false;

    }
  }

  public void onDelete() {
    if (currentFlugzeug != null) {
      if (!facadeFlugzeug.ifExistMietKaufFlugzeugAdmin(currentFlugzeug.getIdFlugzeug())) {
        facadeFlugzeug.removeFlugzeug(currentFlugzeug);
        NewMessage("Flugzeug gelöscht");
        isLoadedFlugzeugliste = false;
      } else {
        NewMessage("Flugzeug darf nicht gelöscht werden da schon Flugzeuge in der FTW-Welt vorhanden sind");
      }

    }

  }

  public void onBereitstellung() {

    Flugzeugemietkauf FgMietKauf = new Flugzeugemietkauf();

    FgMietKauf.setIdFlugzeug(currentFlugzeug.getIdFlugzeug());
    FgMietKauf.setAbgeflogenVonICAO(brtStehtInIcao.toUpperCase());
    FgMietKauf.setAktuellePositionICAO(brtStehtInIcao.toUpperCase());
    FgMietKauf.setHeimatICAO(brtStehtInIcao.toUpperCase());
    FgMietKauf.setBonusFuerRueckfuehrung(0.0); //Pfand in FTW
    FgMietKauf.setIdflugzeugBesitzer(-300); // -300 = FTW Aircraft Stock
    FgMietKauf.setIdFluggesellschaft(-1);
    FgMietKauf.setIdIndustrie(-1);
    FgMietKauf.setIdUserDerFlugzeugGesperrtHat(-1);
    FgMietKauf.setGesamtEntfernung(0);
    FgMietKauf.setGesamtFluege(0);
    FgMietKauf.setGesamtFlugzeit(0);
    FgMietKauf.setGesamtMaschinenLaufzeitMinuten(0);
    FgMietKauf.setGesamtTransportiertePassagiere(0);
    FgMietKauf.setGesamtTransportiertesCargo(0);
    FgMietKauf.setMaschinenLaufzeitMinuten(0);
    FgMietKauf.setIdMietKauf(-1);
    FgMietKauf.setIdUserDerFlugzeugGesperrtHat(-1);
    FgMietKauf.setIsAktive(true);
    FgMietKauf.setIstCheckDurchUserErlaubt(false);
    FgMietKauf.setIstGesperrt(false);
    FgMietKauf.setIstGesperrtBis(null);
    FgMietKauf.setIstGesperrtSeit(null);
    FgMietKauf.setIstMietbar(brtMietbar);
    FgMietKauf.setIstPrivatverkauf(false);
    FgMietKauf.setIstZuVerkaufen(brtKaufbar);
    FgMietKauf.setLeasinggeberUserID(-1);
    FgMietKauf.setLetzterCheckMinuten(0);
    FgMietKauf.setMaxMietzeitMinuten(brtMietzeit);
    FgMietKauf.setMietpreisTrocken(brtTrockenMiete);
    FgMietKauf.setRegistrierung(brtKennung);
    FgMietKauf.setLeasingAnUserID(-1);
    FgMietKauf.setVerkaufsPreis(brtVerkaufspreis);
    FgMietKauf.setZustandDesFlugzeugs(100);
    FgMietKauf.setBankkontoBesitzer("500-1000002");
    FgMietKauf.setLetzterSpritPreis(0.0);
    FgMietKauf.setMaschinenLaufzeitMinuten(0);
    FgMietKauf.setIdSitzKonfiguration(-1);
    FgMietKauf.setIstInDerLuft(false);
    FgMietKauf.setPfand(brtPfand);
    FgMietKauf.setKostenstelle(0);
    FgMietKauf.setGesamtAlterDesFlugzeugsMinuten(brtZellenstunden * 60);
    FgMietKauf.setAktuelleTankfuellung(brtTankfuellung);
    FgMietKauf.setZustand(brtZustand);
    FgMietKauf.setBaujahr(brtBaujahr);
    FgMietKauf.setNurFuerAuftraegeDerFluggesellschaft(false);
    FgMietKauf.setEigenesBildURL("");
    FgMietKauf.setPositiionBreitenGrad(0.0);
    FgMietKauf.setPositionLaengenGrad(0.0);
    FgMietKauf.setFlugzeugUmgebaut(false);

    FgMietKauf.setNaechsterTerminCcheck(ccheck.getTime());

    if (brtKennung.equals("")) {
      int kmax = 9999;
      int kennnr;
      String Kennung;

      for (int i = 0; i < brtAnzahl; i++) {

        do {
          kennnr = CONF.zufallszahl(1, kmax);
          Kennung = "F-TW" + nummer.format(kennnr);

        } while (facadeFlugzeug.ifExistKennung(Kennung));

        FgMietKauf.setRegistrierung(Kennung);
        facadeFlugzeug.createFlugzeugMietKauf(FgMietKauf);

        NewMessage("Flugzeug wurde Bereitgestellt Kennung : " + Kennung);
      }
    } else {
      FgMietKauf.setRegistrierung(brtKennung);
      facadeFlugzeug.createFlugzeugMietKauf(FgMietKauf);
      NewMessage("Flugzeug wurde Bereitgestellt Kennung : " + brtKennung);
    }

    isLoadedFlugzeugliste = false;
  }

  public void onCopy() {

    Flugzeuge copyFlugzeug = new Flugzeuge();
    copyFlugzeug = currentFlugzeug;
    copyFlugzeug.setType("Kopie : " + currentFlugzeug.getType());

    facadeFlugzeug.createFlugzeug(copyFlugzeug);
    NewMessage("Flugzeug kopiert");
    isLoadedFlugzeugliste = false;
  }

  public void onSuche() {

  }

  public void onClean() {
    frmflugzeugHersteller = "";
    frmIcaoFlugzeugcode = "";
    frmflugzeugTyp = "";
    frmantriebsart = 0;
    frmanzahltriebwerke = 0;
    frmerstflug = 0;
    frmproduziertBis = 0;
    frmbesatzung = 0;
    frmbilderUrl = "";
    frmfsxFreeDownloadUrl = "";
    frmfsxPaywareDownloadUrl = "";
    frmherstellerICAO = "";
    frmherstellerpreis = 0;
    frmhoechstAbfluggewicht = 0;
    frmhoechstgeschwindigkeitTAS = 0;
    frmlaenge = 0;
    frmleergewicht = 0;
    frmlizenz = "";
    frmtypeRating = "";
    frmmaxFlughoehe = 0;
    frmmaxLandegewicht = 0;
    frmmaxReichweite = 0;
    frmmindestLandebahnLaenge = 0;
    frmp3dFreeDownloadUrl = "";
    frmp3dPayware3DownloadUrl = "";
    frmpayload = 0;
    frmreisegeschwindigkeitTAS = 0;
    frmsitzeBusinessClass = 0;
    frmsitzeEconomy = 0;
    frmmaxAnzahlAnZuBelegendenSitzen = 0;
    frmspannweite = 0;
    frmstartstreckeBeiMTOW = 0;
    frmsteigleistung = 0;
    frmsymbolUrl = "";
    frmtreibstoffArt = 0;
    frmtreibstoffkapazitaet = 0;
    frmtriebwerkspreis = 0;
    frmvApp = 0;
    frmverbrauchStunde = 0;
    frmverkaufspreis = 0;
    frmvlSpeed = 0;
    frmxplaneFreeDownloadUrl = "";
    frmxplanePaywareDownloadUrl = "";
    frmfs9FreeDownloadUrl = "";
    frmfs9PaywareDownloadUrl = "";
    frminProduktion = false;
    frmFixKosten = 0.0;
    frmKalkulatorischerStundensatz = 0.0;
    frmLangstreckenflugzeug = false;

    frmidUmbauModel = -1;
    frmUmbaukosten = 0.0;
    frmUmbauzeit = 0;
    frmNurFuerUmbau = false;
  }

  public void onFlugzeugWert() {

    //Parameter fuer erweiterte Berechnungen laden
    config = facadeFlugzeug.readConfig();

    // um die Maximale Nutzungsdauer zu Berechnen wird die gewöhnliche Nutzungsdauer mit 2 mulitpliziert
    int KolbenmotorGewoehnlichNutzungsdauer = 12000;
    int TurboPropGewoehnlichNutzungsdauer = 16000;
    int TurbineGewoehnlichNutzungsdauer = 20000;
    int AktuellesJahr = Calendar.getInstance().get(Calendar.YEAR);

    double ZellenStunden = brtZellenstunden;
    double Alter = AktuellesJahr - brtBaujahr;
    double MaximaleNutzungsdauer = 0;
    double Wert = currentFlugzeug.getVerkaufspreis();
    double Zustand = brtZustand;

    double MietFaktor = 0.0005;

    // Enfernt durch Toffi (Peter) am 01.02.2018, neues Mietpreismodell
    // double Mietpreis = Wert * MietFaktor;
    if (null != currentFlugzeug.getAntriebsart()) {
      switch (currentFlugzeug.getAntriebsart()) {
        case 1:
          MaximaleNutzungsdauer = KolbenmotorGewoehnlichNutzungsdauer * 2;
          break;
        case 2:
          MaximaleNutzungsdauer = TurboPropGewoehnlichNutzungsdauer * 2;
          break;
        case 3:
          MaximaleNutzungsdauer = TurbineGewoehnlichNutzungsdauer * 2;
          break;
        default:
          MaximaleNutzungsdauer = 0;
          break;
      }

    }

    ZellenStunden = (ZellenStunden / config.getFaktorFuerZellenstunden());

    // Kalkulierter Marktpreis(mit Zellenstunden) =  FTW-Neupreis - ((Zellenstunden/ Maximale Nutzungsdauer )  * Kalkulierter Neupreis)
    double KalkulierterMarktPreisMitZellenstunden = Wert - ((ZellenStunden / MaximaleNutzungsdauer) * Wert);

    // Marktpreis mit Alter = Kalkulierter Marktpreis mit Zellenstunden - (Kalkulierter Marktpreis mit Zellenstunden*(Alter/100))
    double MarktpreisMitAlter = KalkulierterMarktPreisMitZellenstunden - (KalkulierterMarktPreisMitZellenstunden * (Alter / 100.));

    // Verkaufspreis = Zustand  / 100 * Kalkulierter Marktpreis mit Alter
    double Verkaufspreis = (Zustand / 100.) * MarktpreisMitAlter;

    // Enfernt durch Toffi (Peter) am 01.02.2018, neues Mietpreismodell
    // brtTrockenMiete = Mietpreis;
    brtVerkaufspreis = Verkaufspreis;

    //
    // *** Neuberechnung Mietpreis trocken
    //
//modell_alter = aktuelles_jahr - erstflug_jahr
//multiplier = 1,5
//
//if (modell_alter) <= 100
//{
// ((payload*reisegeschwindigkeit/500)*(1-(0,2*1,015^modell_alter-0,2)))*multiplier
//}
//else
//{
// ((payload*reisegeschwindigkeit/500)*0,3*multiplier
//}     
//    
//    double multiPlier = 1.5;
//    double payload = currentFlugzeug.getPayload();
//    double reisegeschwindigkeit = currentFlugzeug.getReisegeschwindigkeitTAS();
//
//    double test = Math.pow(1.015, Alter);
//    
//    if(Alter <= 100){
//      brtTrockenMiete =( (payload * reisegeschwindigkeit / 500.0) * (1.00 - (0.2 * test - 0.2) ) ) * multiPlier;
//    }else{
//      
//    }
//    
//    System.out.println("de.klees.beans.flugzeuge.flugzeugBean.onFlugzeugWert() ^ " + test);
  }

  /*
  ********************** Sitzkonfiguration
   */
  public List<Sitzkonfiguration> getSitzKonfiguration() {

    try {
      if (currentFlugzeug.getIdFlugzeug() != null) {
        //System.out.println("getSitzKonfiguration : " + currentFlugzeug.getIdFlugzeug());
        ListKonfiguration = facadeFlugzeug.getSitzKonfiguration(currentFlugzeug.getIdFlugzeug());
      }

    } catch (NullPointerException e) {

    }

    return ListKonfiguration;
  }

  public void onNeueSitzKonfig() {
    Sitzkonfiguration newKonfig = new Sitzkonfiguration();

    newKonfig.setAbzugNettoGewicht(0);
    newKonfig.setZuschlagNettoGewicht(0);
    newKonfig.setBezeichnung("Neu Konfiguration, Daten ergänzen");
    newKonfig.setBildURL("");
    newKonfig.setFesterUmbau(false);
    newKonfig.setIdFlugzeug(currentFlugzeug.getIdFlugzeug());
    newKonfig.setPreis(0);
    newKonfig.setSitzeBusiness(0);
    newKonfig.setSitzeEconomy(0);
    newKonfig.setCrew(0);
    newKonfig.setFlugbegleiter(0);
    newKonfig.setCargo(currentFlugzeug.getCargo());
    newKonfig.setMaxPayload(currentFlugzeug.getPayload());
    newKonfig.setUmbauzeitMinuten(0);
    newKonfig.setZusatzTank(false);
    newKonfig.setDow(currentFlugzeug.getLeergewicht());
    newKonfig.setTankkapazitaet(currentFlugzeug.getTreibstoffkapazitaet());
    newKonfig.setFaktor(1.0);

    facadeFlugzeug.SitzkonfigurationNeu(newKonfig);

    NewMessage("Neuer Datensatz wurde angelegt, Daten ergänzen");

  }

  public void onKMPBerechnung() {
    if (currentFlugzeug != null) {

      double Exitlimit = currentFlugzeug.getMaxAnzahlZuBelgenderSitze();
      double kmpFaktor = 0.0;

      if (currentKonfig.getSitzeEconomy() > 0 && currentKonfig.getSitzeBusiness() > 0) {
        kmpFaktor = (Exitlimit / (double) currentKonfig.getSitzeEconomy());
      }

      if (currentKonfig.getSitzeEconomy() > 0 && currentKonfig.getSitzeBusiness() == 0) {
        kmpFaktor = (Exitlimit / (double) currentKonfig.getSitzeEconomy());
      }

      if (currentKonfig.getSitzeBusiness() > 0 && currentKonfig.getSitzeEconomy() == 0) {
        kmpFaktor = (Exitlimit / (double) currentKonfig.getSitzeBusiness());
      }

      //Cargo
      if (currentKonfig.getSitzeEconomy() <= 0 && currentKonfig.getSitzeBusiness() <= 0) {
        kmpFaktor = 1.0;
      }

      if (kmpFaktor > 2.0) {
        kmpFaktor = 2.0;
      }

      if (Exitlimit > 100 && Exitlimit <= 200) {
        double fk = 70.0 / Exitlimit * (double) currentKonfig.getSitzeEconomy();
        double zuschlag = ((kmpFaktor - 1.0) / 100.0 * fk);
        kmpFaktor = 1.0 + zuschlag;
      }

      if (Exitlimit > 200 && Exitlimit <= 300) {
        double fk = 50 / Exitlimit * (double) currentKonfig.getSitzeEconomy();
        double zuschlag = ((kmpFaktor - 1.0) / 100.0 * fk);
        kmpFaktor = 1.0 + zuschlag;
      }

      if (Exitlimit > 300) {
        double fk = 30 / Exitlimit * (double) currentKonfig.getSitzeEconomy();
        double zuschlag = ((kmpFaktor - 1.0) / 100.0 * fk);
        kmpFaktor = 1.0 + zuschlag;
      }

      currentKonfig.setFaktor(Math.round(kmpFaktor * 100.0) / 100.0);

    }

  }

  public void onSaveSitzKonfig() {
    facadeFlugzeug.SitzkonfigurationSpeichern(currentKonfig);
    NewMessage("Datensatz gespeichert");
  }

  public void onDeletSitzKonfig() {
    facadeFlugzeug.SitzkonfigurationLoeschen(currentKonfig);
    NewMessage("Datensatz gelöscht");
  }

  public Sitzkonfiguration getSelectedKonfig() {
    return currentKonfig;
  }

  public void setSelectedKonfig(Sitzkonfiguration selectedKonfig) {
    this.currentKonfig = selectedKonfig;
  }

  /*
  ********************** Sitzkonfiguration ENDE
   */
  public void berechneKalkulatorischerStundensatz() {
    currentFlugzeug.setKalkulatorischerStundensatz(getCcheckPreis());
  }

  public double getCcheckPreis() {

    double frmCheckWert = 0.0;
    Flugzeuge stammfg = currentFlugzeug;

    switch (stammfg.getLizenz()) {
      case "PPL-A":
        frmCheckWert = stammfg.getVerkaufspreis() * 0.10;
        frmCheckWert = frmCheckWert / 150;
        break;
      case "CPL":
        frmCheckWert = stammfg.getVerkaufspreis() * 0.10;
        frmCheckWert = frmCheckWert / 170;
        break;
      case "MPL":
        frmCheckWert = stammfg.getVerkaufspreis() * 0.10;
        frmCheckWert = frmCheckWert / 250;
        break;
      case "ATPL":
        frmCheckWert = stammfg.getVerkaufspreis() * 0.10;
        frmCheckWert = frmCheckWert / 400;
        break;
      default:
        return frmCheckWert;
    }
    return frmCheckWert;

  }

  /*
  ********************** MOD Tools BEGINN
   */
  //Modell
  public void onSaveModell() {
    facadeFlugzeug.editModell(selectedModel);
    NewMessage("Modell gespeichert");
  }

  public void onDeleteModell() {
    facadeFlugzeug.removeModell(selectedModel);
    NewMessage("Modell gelöscht");
  }

  public void onNewModell() {
    Flugzeugemodelle fgm = new Flugzeugemodelle();
    fgm.setModellName(frmflugzeugTyp);
    facadeFlugzeug.createModell(fgm);
    NewMessage("Modell erfolgreich angelegt");
  }

  //Symbole
  public void onSymbolURL() {
    frmsymbolUrl = "http://www.ftw-sim.de/images/FTW/Flugzeugsymbole/";
  }

  public void onSaveSymbol() {
    facadeFlugzeug.editSymbol(selectedSymbol);
    NewMessage("Symbol gespeichert");
  }

  public void onDeleteSymbol() {
    facadeFlugzeug.removeSymbol(selectedSymbol);
    NewMessage("Symbol gelöscht");
  }

  public void onNewSymbol() {
    Flugzeugsymbole fgs = new Flugzeugsymbole();
    fgs.setFlugzeugModell(frmflugzeugTyp);
    fgs.setUrlFlugzeugsymbol(frmsymbolUrl);

    facadeFlugzeug.createSymbol(fgs);
    NewMessage("Symbol erfolgreich angelegt");
  }

  // ACARS
  public void onAcarsAufraeumen() {
    // Positionsdaten aufräumen
    long AktDate = new Date().getTime();
    long Tage = (7 * 24 * 60 * 60 * 1000);

    long PruefDatum = (AktDate - Tage) / 1000;

    //NewMessage("Positionstabelle wurde aufgeräumt, Zeilen entfernt: " + facadeFlugzeug.onAcarasAufraeumen(PruefDatum));
  }

  // *************************************************** MOD Tools ENDE
  public void onRowSelect(SelectEvent event) {
    setBrtStehtInIcao(currentFlugzeug.getHerstellerICAO());
    setBrtVerkaufspreis(currentFlugzeug.getVerkaufspreis());
    //setBrtTrockenMiete(currentFlugzeug.getStandardMietpreis());
    setBrtBaujahr(currentFlugzeug.getErstflug());
    setBrtTrockenMiete(currentFlugzeug.getKalkulatorischerStundensatz());
    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("inBetriebID", getSelectedFlugzeug().getIdFlugzeug());
    //System.out.println(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("inBetriebID"));
  }

  public void onRowUnselect(UnselectEvent event) {
  }

  public void onViewRowSelect(SelectEvent event) {

  }

  public void onViewRowUnselect(UnselectEvent event) {

  }

  private void NewMessage(String neueNachricht) {
    FacesMessage msg = new FacesMessage(neueNachricht);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  /*
  ******************** Setter and Getter
   */
  public long getAnzahlFreigabe(int id) {
    return facadeFlugzeug.CountAllMietkaufByID(id);
  }

  public Flugzeuge getSelectedFlugzeug() {
    return currentFlugzeug;
  }

  public void setSelectedFlugzeug(Flugzeuge selectedFlugzeug) {
    this.currentFlugzeug = selectedFlugzeug;
  }

  public String getFrmflugzeugTyp() {
    return frmflugzeugTyp;
  }

  public void setFrmflugzeugTyp(String frmflugzeugTyp) {
    this.frmflugzeugTyp = frmflugzeugTyp;
  }

  public String getFrmflugzeugHersteller() {
    return frmflugzeugHersteller;
  }

  public void setFrmflugzeugHersteller(String frmflugzeugHersteller) {
    this.frmflugzeugHersteller = frmflugzeugHersteller;
  }

  public String getSucheBezeichnung() {
    return SucheBezeichnung;
  }

  public void setSucheBezeichnung(String SucheBezeichnung) {
    this.SucheBezeichnung = SucheBezeichnung;
  }

  public int getFrmerstflug() {
    return frmerstflug;
  }

  public void setFrmerstflug(int frmerstflug) {
    this.frmerstflug = frmerstflug;
  }

  public String getFrmhersteller() {
    return frmhersteller;
  }

  public void setFrmhersteller(String frmhersteller) {
    this.frmhersteller = frmhersteller;
  }

  public String getFrmherstellerICAO() {
    return frmherstellerICAO;
  }

  public void setFrmherstellerICAO(String frmherstellerICAO) {
    this.frmherstellerICAO = frmherstellerICAO;
  }

  public double getFrmverkaufspreis() {
    return frmverkaufspreis;
  }

  public void setFrmverkaufspreis(double frmverkaufspreis) {
    this.frmverkaufspreis = frmverkaufspreis;
  }

  public double getFrmherstellerpreis() {
    return frmherstellerpreis;
  }

  public void setFrmherstellerpreis(double frmherstellerpreis) {
    this.frmherstellerpreis = frmherstellerpreis;
  }

  public int getFrmsitzeEconomy() {
    return frmsitzeEconomy;
  }

  public void setFrmsitzeEconomy(int frmsitzeEconomy) {
    this.frmsitzeEconomy = frmsitzeEconomy;
  }

  public int getFrmsitzeBusinessClass() {
    return frmsitzeBusinessClass;
  }

  public void setFrmsitzeBusinessClass(int frmsitzeBusinessClass) {
    this.frmsitzeBusinessClass = frmsitzeBusinessClass;
  }

  public int getFrmbesatzung() {
    return frmbesatzung;
  }

  public void setFrmbesatzung(int frmbesatzung) {
    this.frmbesatzung = frmbesatzung;
  }

  public int getFrmpayload() {
    return frmpayload;
  }

  public void setFrmpayload(int frmpayload) {
    this.frmpayload = frmpayload;
  }

  public int getFrmhoechstAbfluggewicht() {
    return frmhoechstAbfluggewicht;
  }

  public void setFrmhoechstAbfluggewicht(int frmhoechstAbfluggewicht) {
    this.frmhoechstAbfluggewicht = frmhoechstAbfluggewicht;
  }

  public int getFrmleergewicht() {
    return frmleergewicht;
  }

  public void setFrmleergewicht(int frmleergewicht) {
    this.frmleergewicht = frmleergewicht;
  }

  public int getFrmantriebsart() {
    return frmantriebsart;
  }

  public void setFrmantriebsart(int frmantriebsart) {
    this.frmantriebsart = frmantriebsart;
  }

  public String getFrmtriebwerkstype() {
    return frmtriebwerkstype;
  }

  public void setFrmtriebwerkstype(String frmtriebwerkstype) {
    this.frmtriebwerkstype = frmtriebwerkstype;
  }

  public double getFrmtriebwerkspreis() {
    return frmtriebwerkspreis;
  }

  public void setFrmtriebwerkspreis(double frmtriebwerkspreis) {
    this.frmtriebwerkspreis = frmtriebwerkspreis;
  }

  public int getFrmanzahltriebwerke() {
    return frmanzahltriebwerke;
  }

  public void setFrmanzahltriebwerke(int frmanzahltriebwerke) {
    this.frmanzahltriebwerke = frmanzahltriebwerke;
  }

  public int getFrmtreibstoffkapazitaet() {
    return frmtreibstoffkapazitaet;
  }

  public void setFrmtreibstoffkapazitaet(int frmtreibstoffkapazitaet) {
    this.frmtreibstoffkapazitaet = frmtreibstoffkapazitaet;
  }

  public int getFrmtreibstoffArt() {
    return frmtreibstoffArt;
  }

  public void setFrmtreibstoffArt(int frmtreibstoffArt) {
    this.frmtreibstoffArt = frmtreibstoffArt;
  }

  public int getFrmverbrauchStunde() {
    return frmverbrauchStunde;
  }

  public void setFrmverbrauchStunde(int frmverbrauchStunde) {
    this.frmverbrauchStunde = frmverbrauchStunde;
  }

  public int getFrmreisegeschwindigkeitTAS() {
    return frmreisegeschwindigkeitTAS;
  }

  public void setFrmreisegeschwindigkeitTAS(int frmreisegeschwindigkeitTAS) {
    this.frmreisegeschwindigkeitTAS = frmreisegeschwindigkeitTAS;
  }

  public int getFrmhoechstgeschwindigkeitTAS() {
    return frmhoechstgeschwindigkeitTAS;
  }

  public void setFrmhoechstgeschwindigkeitTAS(int frmhoechstgeschwindigkeitTAS) {
    this.frmhoechstgeschwindigkeitTAS = frmhoechstgeschwindigkeitTAS;
  }

  public int getFrmsteigleistung() {
    return frmsteigleistung;
  }

  public void setFrmsteigleistung(int frmsteigleistung) {
    this.frmsteigleistung = frmsteigleistung;
  }

  public int getFrmstartstreckeBeiMTOW() {
    return frmstartstreckeBeiMTOW;
  }

  public void setFrmstartstreckeBeiMTOW(int frmstartstreckeBeiMTOW) {
    this.frmstartstreckeBeiMTOW = frmstartstreckeBeiMTOW;
  }

  public int getFrmmaxLandegewicht() {
    return frmmaxLandegewicht;
  }

  public void setFrmmaxLandegewicht(int frmmaxLandegewicht) {
    this.frmmaxLandegewicht = frmmaxLandegewicht;
  }

  public int getFrmmaxReichweite() {
    return frmmaxReichweite;
  }

  public void setFrmmaxReichweite(int frmmaxReichweite) {
    this.frmmaxReichweite = frmmaxReichweite;
  }

  public int getFrmmaxFlughoehe() {
    return frmmaxFlughoehe;
  }

  public void setFrmmaxFlughoehe(int frmmaxFlughoehe) {
    this.frmmaxFlughoehe = frmmaxFlughoehe;
  }

  public int getFrmmindestLandebahnLaenge() {
    return frmmindestLandebahnLaenge;
  }

  public void setFrmmindestLandebahnLaenge(int frmmindestLandebahnLaenge) {
    this.frmmindestLandebahnLaenge = frmmindestLandebahnLaenge;
  }

  public int getFrmvApp() {
    return frmvApp;
  }

  public void setFrmvApp(int frmvApp) {
    this.frmvApp = frmvApp;
  }

  public int getFrmvlSpeed() {
    return frmvlSpeed;
  }

  public void setFrmvlSpeed(int frmvlSpeed) {
    this.frmvlSpeed = frmvlSpeed;
  }

  public int getFrmlaenge() {
    return frmlaenge;
  }

  public void setFrmlaenge(int frmlaenge) {
    this.frmlaenge = frmlaenge;
  }

  public int getFrmspannweite() {
    return frmspannweite;
  }

  public void setFrmspannweite(int frmspannweite) {
    this.frmspannweite = frmspannweite;
  }

  public String getFrmsymbolUrl() {
    return frmsymbolUrl;
  }

  public void setFrmsymbolUrl(String frmsymbolUrl) {
    this.frmsymbolUrl = frmsymbolUrl;
  }

  public String getFrmxplaneFreeDownloadUrl() {
    return frmxplaneFreeDownloadUrl;
  }

  public void setFrmxplaneFreeDownloadUrl(String frmxplaneFreeDownloadUrl) {
    this.frmxplaneFreeDownloadUrl = frmxplaneFreeDownloadUrl;
  }

  public String getFrmfsxFreeDownloadUrl() {
    return frmfsxFreeDownloadUrl;
  }

  public void setFrmfsxFreeDownloadUrl(String frmfsxFreeDownloadUrl) {
    this.frmfsxFreeDownloadUrl = frmfsxFreeDownloadUrl;
  }

  public String getFrmp3dFreeDownloadUrl() {
    return frmp3dFreeDownloadUrl;
  }

  public void setFrmp3dFreeDownloadUrl(String frmp3dFreeDownloadUrl) {
    this.frmp3dFreeDownloadUrl = frmp3dFreeDownloadUrl;
  }

  public String getFrmxplanePaywareDownloadUrl() {
    return frmxplanePaywareDownloadUrl;
  }

  public void setFrmxplanePaywareDownloadUrl(String frmxplanePaywareDownloadUrl) {
    this.frmxplanePaywareDownloadUrl = frmxplanePaywareDownloadUrl;
  }

  public String getFrmfsxPaywareDownloadUrl() {
    return frmfsxPaywareDownloadUrl;
  }

  public void setFrmfsxPaywareDownloadUrl(String frmfsxPaywareDownloadUrl) {
    this.frmfsxPaywareDownloadUrl = frmfsxPaywareDownloadUrl;
  }

  public String getFrmp3dPayware3DownloadUrl() {
    return frmp3dPayware3DownloadUrl;
  }

  public void setFrmp3dPayware3DownloadUrl(String frmp3dPayware3DownloadUrl) {
    this.frmp3dPayware3DownloadUrl = frmp3dPayware3DownloadUrl;
  }

  public String getFrmbilderUrl() {
    return frmbilderUrl;
  }

  public void setFrmbilderUrl(String frmbilderUrl) {
    this.frmbilderUrl = frmbilderUrl;
  }

  public Map<String, String> getTreibstoffArt() {
    return TreibstoffArt;
  }

  public void setTreibstoffArt(Map<String, String> TreibstoffArt) {
    this.TreibstoffArt = TreibstoffArt;
  }

  public Map<String, String> getAntriebsArt() {
    return AntriebsArt;
  }

  public void setAntriebsArt(Map<String, String> AntriebsArt) {
    this.AntriebsArt = AntriebsArt;
  }

  public Map<String, String> getTriebwerksType() {
    return TriebwerksType;
  }

  public void setTriebwerksType(Map<String, String> TriebwerksType) {
    this.TriebwerksType = TriebwerksType;
  }

  public int getFrmmaxAnzahlAnZuBelegendenSitzen() {
    return frmmaxAnzahlAnZuBelegendenSitzen;
  }

  public void setFrmmaxAnzahlAnZuBelegendenSitzen(int frmmaxAnzahlAnZuBelegendenSitzen) {
    this.frmmaxAnzahlAnZuBelegendenSitzen = frmmaxAnzahlAnZuBelegendenSitzen;
  }

  public String getFrmIcaoFlugzeugcode() {
    return frmIcaoFlugzeugcode;
  }

  public void setFrmIcaoFlugzeugcode(String frmIcaoFlugzeugcode) {
    this.frmIcaoFlugzeugcode = frmIcaoFlugzeugcode;
  }

  public String getFrmlizenz() {
    return frmlizenz;
  }

  public void setFrmlizenz(String frmlizenz) {
    this.frmlizenz = frmlizenz;
  }

  public String getFrmtypeRating() {
    return frmtypeRating;
  }

  public void setFrmtypeRating(String frmtypeRating) {
    this.frmtypeRating = frmtypeRating;
  }

  public int getBrtAnzahl() {
    return brtAnzahl;
  }

  public void setBrtAnzahl(int brtAnzahl) {
    this.brtAnzahl = brtAnzahl;
  }

  public String getBrtKennung() {
    return brtKennung;
  }

  public void setBrtKennung(String brtKennung) {
    this.brtKennung = brtKennung;
  }

  public double getBrtNassMiete() {
    return brtNassMiete;
  }

  public void setBrtNassMiete(double brtNassMiete) {
    this.brtNassMiete = brtNassMiete;
  }

  public double getBrtTrockenMiete() {
    return brtTrockenMiete;
  }

  public void setBrtTrockenMiete(double brtTrockenMiete) {
    this.brtTrockenMiete = brtTrockenMiete;
  }

  public double getBrtVerkaufspreis() {
    return brtVerkaufspreis;
  }

  public void setBrtVerkaufspreis(double brtVerkaufspreis) {
    this.brtVerkaufspreis = brtVerkaufspreis;
  }

  public String getBrtStehtInIcao() {
    return brtStehtInIcao;
  }

  public void setBrtStehtInIcao(String brtStehtInIcao) {
    this.brtStehtInIcao = brtStehtInIcao;
  }

  public ViewFlugzeugeMietKauf getSelectedFTWFlugzeug() {
    return currentFTWFlugzeug;
  }

  public void setSelectedFTWFlugzeug(ViewFlugzeugeMietKauf selectedFTWFlugzeug) {
    this.currentFTWFlugzeug = selectedFTWFlugzeug;
  }

  public String getMdlModellbezeichnung() {
    return mdlModellbezeichnung;
  }

  public void setMdlModellbezeichnung(String mdlModellbezeichnung) {
    this.mdlModellbezeichnung = mdlModellbezeichnung;
  }

  public String getFrmfs9FreeDownloadUrl() {
    return frmfs9FreeDownloadUrl;
  }

  public void setFrmfs9FreeDownloadUrl(String frmfs9FreeDownloadUrl) {
    this.frmfs9FreeDownloadUrl = frmfs9FreeDownloadUrl;
  }

  public String getFrmfs9PaywareDownloadUrl() {
    return frmfs9PaywareDownloadUrl;
  }

  public void setFrmfs9PaywareDownloadUrl(String frmfs9PaywareDownloadUrl) {
    this.frmfs9PaywareDownloadUrl = frmfs9PaywareDownloadUrl;
  }

  public int getFrmproduziertBis() {
    return frmproduziertBis;
  }

  public void setFrmproduziertBis(int frmproduziertBis) {
    this.frmproduziertBis = frmproduziertBis;
  }

  public boolean isFrminProduktion() {
    return frminProduktion;
  }

  public void setFrminProduktion(boolean frminProduktion) {
    this.frminProduktion = frminProduktion;
  }

  public int getBrtMietzeit() {
    return brtMietzeit;
  }

  public void setBrtMietzeit(int brtMietzeit) {
    this.brtMietzeit = brtMietzeit;
  }

  public int getBrtPfand() {
    return brtPfand;
  }

  public void setBrtPfand(int brtPfand) {
    this.brtPfand = brtPfand;
  }

  public int getFrmflugbegleiter() {
    return frmflugbegleiter;
  }

  public void setFrmflugbegleiter(int frmflugbegleiter) {
    this.frmflugbegleiter = frmflugbegleiter;
  }

  public int getFrmcargo() {
    return frmcargo;
  }

  public void setFrmcargo(int frmcargo) {
    this.frmcargo = frmcargo;
  }

  public int getFrmAirframe() {
    return frmAirframe;
  }

  public void setFrmAirframe(int frmAirframe) {
    this.frmAirframe = frmAirframe;
  }

  public String getFrmBemerkung() {
    return frmBemerkung;
  }

  public void setFrmBemerkung(String frmBemerkung) {
    this.frmBemerkung = frmBemerkung;
  }

  public int getVerbrauch() {
    if (currentFlugzeug != null) {
      ViewFlugzeugVerbrauch verbrauch = facadeFlugzeug.getVerbrauch(currentFlugzeug.getIdFlugzeug());
      if (verbrauch != null) {
        double menge = (verbrauch.getVerbrauch().doubleValue() / verbrauch.getMinuten().doubleValue()) * 60;
        return (int) menge;
      } else {
        return 0;
      }
    } else {
      return 0;
    }
  }

  public void onVerbrauchUebernehmen() {
    currentFlugzeug.setVerbrauchStunde(getVerbrauch());
  }

  public double getFrmstandardMietpreis() {
    return frmstandardMietpreis;
  }

  public void setFrmstandardMietpreis(double frmstandardMietpreis) {
    this.frmstandardMietpreis = frmstandardMietpreis;
  }

  public int getFrmMaximumZeroFullWeight() {
    return frmMaximumZeroFullWeight;
  }

  public void setFrmMaximumZeroFullWeight(int frmMaximumZeroFullWeight) {
    this.frmMaximumZeroFullWeight = frmMaximumZeroFullWeight;
  }

  public int getBrtZellenstunden() {
    return brtZellenstunden;
  }

  public void setBrtZellenstunden(int brtZellenstunden) {
    this.brtZellenstunden = brtZellenstunden;
  }

  public int getBrtBaujahr() {
    return brtBaujahr;
  }

  public void setBrtBaujahr(int brtBaujahr) {
    this.brtBaujahr = brtBaujahr;
  }

  public int getBrtTankfuellung() {
    return brtTankfuellung;
  }

  public void setBrtTankfuellung(int brtTankfuellung) {
    this.brtTankfuellung = brtTankfuellung;
  }

  public double getBrtZustand() {
    return brtZustand;
  }

  public void setBrtZustand(double brtZustand) {
    this.brtZustand = brtZustand;
  }

  public boolean isBrtMietbar() {
    return brtMietbar;
  }

  public void setBrtMietbar(boolean brtMietbar) {
    this.brtMietbar = brtMietbar;
  }

  public boolean isBrtKaufbar() {
    return brtKaufbar;
  }

  public void setBrtKaufbar(boolean brtKaufbar) {
    this.brtKaufbar = brtKaufbar;
  }

  public double getFrmTypeRatingKostenStd() {
    return frmTypeRatingKostenStd;
  }

  public void setFrmTypeRatingKostenStd(double frmTypeRatingKostenStd) {
    this.frmTypeRatingKostenStd = frmTypeRatingKostenStd;
  }

  public int getFrmTypeRatingMinStd() {
    return frmTypeRatingMinStd;
  }

  public void setFrmTypeRatingMinStd(int frmTypeRatingMinStd) {
    this.frmTypeRatingMinStd = frmTypeRatingMinStd;
  }

  public Flugzeugsymbole getSelectedSymbol() {
    return selectedSymbol;
  }

  public void setSelectedSymbol(Flugzeugsymbole selectedSymbol) {
    this.selectedSymbol = selectedSymbol;
  }

  public Flugzeugemodelle getSelectedModel() {
    return selectedModel;
  }

  public void setSelectedModel(Flugzeugemodelle selectedModel) {
    this.selectedModel = selectedModel;
  }

  public boolean isFrmauslieferung() {
    return frmauslieferung;
  }

  public void setFrmauslieferung(boolean frmauslieferung) {
    this.frmauslieferung = frmauslieferung;
  }

  public String getFrmFlugzeugArt() {
    return frmFlugzeugArt;
  }

  public void setFrmFlugzeugArt(String frmFlugzeugArt) {
    this.frmFlugzeugArt = frmFlugzeugArt;
  }

  public double getFrmFixKosten() {
    return frmFixKosten;
  }

  public boolean isFrmLangstreckenflugzeug() {
    return frmLangstreckenflugzeug;
  }

  public void setFrmLangstreckenflugzeug(boolean frmLangstreckenflugzeug) {
    this.frmLangstreckenflugzeug = frmLangstreckenflugzeug;
  }

  public void setFrmFixKosten(double frmFixKosten) {
    this.frmFixKosten = frmFixKosten;
  }

  public double getFrmKalkulatorischerStundensatz() {
    return frmKalkulatorischerStundensatz;
  }

  public void setFrmKalkulatorischerStundensatz(double frmKalkulatorischerStundensatz) {
    this.frmKalkulatorischerStundensatz = frmKalkulatorischerStundensatz;
  }

  public void berechneFixKosten() {

    double faktor = 300000.0;

    if (currentFlugzeug != null) {
      currentFlugzeug.setFixkosten((currentFlugzeug.getHoechstAbfluggewicht() * currentFlugzeug.getHoechstAbfluggewicht()) / faktor);
    } else {
      currentFlugzeug.setFixkosten(currentFlugzeug.getFixkosten());
    }
  }

  public double getFrmUmbaukosten() {
    return frmUmbaukosten;
  }

  public void setFrmUmbaukosten(double frmUmbaukosten) {
    this.frmUmbaukosten = frmUmbaukosten;
  }

  public int getFrmUmbauzeit() {
    return frmUmbauzeit;
  }

  public void setFrmUmbauzeit(int frmUmbauzeit) {
    this.frmUmbauzeit = frmUmbauzeit;
  }

  public boolean isFrmNurFuerUmbau() {
    return frmNurFuerUmbau;
  }

  public void setFrmNurFuerUmbau(boolean frmNurFuerUmbau) {
    this.frmNurFuerUmbau = frmNurFuerUmbau;
  }

  public int getFrmidUmbauModel() {
    return frmidUmbauModel;
  }

  public void setFrmidUmbauModel(int frmidUmbauModel) {
    this.frmidUmbauModel = frmidUmbauModel;
  }

}
