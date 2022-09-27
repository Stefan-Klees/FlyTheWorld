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

import de.klees.beans.system.helper.listFluggesellschaften;
import de.klees.beans.system.helper.listKostenstellen;
import de.klees.data.FlugesellschaftPiloten;
import de.klees.data.Fluggesellschaft;
import de.klees.data.FluggesellschaftBonusSystem;
import de.klees.data.Fluggesellschaftmanager;
import de.klees.data.Flugrouten;
import de.klees.data.Flugzeuge;
import de.klees.data.Kostenstellen;
import de.klees.data.Mail;
import de.klees.data.Benutzer;
import de.klees.data.Yaacarskopf;
import de.klees.data.views.ViewFBOUserObjekte;
import de.klees.data.views.ViewFluggesellschaftManager;
import de.klees.data.views.ViewFluggesellschaftUmsatzPiloten;
import de.klees.data.views.ViewFlughaefenNachStartsLandungenMitPaxCargo;
import de.klees.data.views.ViewFlugzeugeMietKauf;
import de.klees.data.views.ViewFlugzeuglog;
import de.klees.data.views.ViewKostenstellenAuswertung;
import de.klees.data.views.ViewLetzteFluege;
import de.klees.data.views.ViewLogbuchLast20Day;
import de.klees.data.views.ViewMeineFluggesellschaften;
import de.klees.data.views.ViewStatFluggesellschaftVermoegen;
import de.klees.data.views.ViewStatUserFirmenvermoegen;
import de.klees.data.views.ViewStatUsersFlugzeuge;
import de.klees.data.views.ViewUserFluglogBuch;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author Stefan Klees
 */
public class statistikBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private Kostenstellen selectedKostenstelle;
  private ViewMeineFluggesellschaften selectedFlugesellschaft;

  private Fluggesellschaft selectedAllFluggesellschaft;

  private List<Fluggesellschaft> fluggesellschaften;
  private List<ViewFlugzeugeMietKauf> fluggesellschaftFlugzeuge;
  private List<FlugesellschaftPiloten> fluggesellschaftPiloten;
  //  private List<ViewFluggesellschaftPiloten> fluggesellschaftPiloten;
  private List<ViewFluggesellschaftManager> fluggesellschaftManager;
  private List<Flugrouten> fluggesellschaftRouten;

  private double FGSummen[];
  private double traffikIndex;

  private List<ViewUserFluglogBuch> userFlugLogbuch;
  private List<ViewUserFluglogBuch> filteredLogs;
  private List<ViewKostenstellenAuswertung> kostenstellenAuswertung;
  private List<ViewMeineFluggesellschaften> meineFluggesellschaften;
  private ViewFluggesellschaftUmsatzPiloten umsatzPiloten;

  private Benutzer userDaten;

  private final int UserID;
  private int OwnerID;

  //Kostenstelleauswertung
  private ArrayList<listKostenstellen> KostenstellenListe = new ArrayList<>();
  private ArrayList<listFluggesellschaften> FluggesellschaftsListe = new ArrayList<>();

  private int kostenstellenAuswertungModus;
  private String frmBezeichnung;
  private int frmKostenStelle;
  private boolean isLoaded;
  private int frmFluggesellschaftID;
  private String frmFluggesellschaftBankKonto;
  private Date frmvonDatum;
  private Date frmbisDatum;
  private int frmvonKostenstelle;
  private int frmbisKostenstelle;
  private double kstSummeErtraege;
  private double kstSummeAufwaende;
  private double kstErgebnis;
  private double kstNullBetrag;
  private final Calendar c = Calendar.getInstance();
  private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
  private Date bd;
  private Date vd;
  private int Platz;
  private String BonusAbzeichen;
  private int UserIDForAdmin;

  private boolean loadingLetzteFluege;

  public statistikBean() {

    this.UserID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
    UserIDForAdmin = 0;
    c.add(Calendar.DAY_OF_MONTH, -30);
    isLoaded = false;
    kstNullBetrag = 0.0;
    frmbisDatum = new Date();
    frmvonDatum = c.getTime();
    frmFluggesellschaftBankKonto = "";
    Platz = 0;
    loadingLetzteFluege = false;
  }

  @EJB
  SystemFacade facadeSystem;

  public List<ViewStatFluggesellschaftVermoegen> getFluggesellschaftVermoegen() {
    return facadeSystem.getFluggesellschaftVermoegen();
  }

  public List<ViewFlugzeugeMietKauf> getFluggesellschaftFlugzeuge() {
    return fluggesellschaftFlugzeuge;
  }

  public List<Fluggesellschaft> getAllFluggesellschaften() {
    return facadeSystem.getAllFluggesellschaften();
  }

  public List<FlugesellschaftPiloten> getFluggesellschaftPiloten() {
    return fluggesellschaftPiloten;
  }

  public List<ViewFluggesellschaftManager> getFluggesellschaftManager() {
    return fluggesellschaftManager;
  }

  public List<Flugrouten> getFluggesellschaftRouten() {
    return fluggesellschaftRouten;
  }

  public List<ViewFBOUserObjekte> getFluggesellschaftFBO() {
    if (selectedAllFluggesellschaft != null) {
      return facadeSystem.getFluggesellschaftFBOs(selectedAllFluggesellschaft.getUserid(), selectedAllFluggesellschaft.getIdFluggesellschaft(), "FBO");
    }
    return facadeSystem.getFluggesellschaftFBOs(-1, -1, "");
  }

  public List<ViewFBOUserObjekte> getFluggesellschaftTerminals() {
    if (selectedAllFluggesellschaft != null) {
      return facadeSystem.getFluggesellschaftFBOs(selectedAllFluggesellschaft.getUserid(), selectedAllFluggesellschaft.getIdFluggesellschaft(), "Terminal");
    }
    return facadeSystem.getFluggesellschaftFBOs(-1, -1, "");
  }

  public List<ViewFBOUserObjekte> getFluggesellschaftTankstellen() {
    if (selectedAllFluggesellschaft != null) {
      return facadeSystem.getFluggesellschaftFBOs(selectedAllFluggesellschaft.getUserid(), selectedAllFluggesellschaft.getIdFluggesellschaft(), "Tankstelle");
    }
    return facadeSystem.getFluggesellschaftFBOs(-1, -1, "");
  }

  public List<ViewStatUsersFlugzeuge> getUsersFlugzeuge() {
    return facadeSystem.getUsersFlugzeuge();
  }

  public List<ViewStatUserFirmenvermoegen> getUserFirmenvermoegen() {
    return facadeSystem.getUsersFirmenVermoegen();
  }

  public List<Benutzer> getBestUserNachMeilen() {
    Platz = 0;
    return facadeSystem.getBestNachMeilen();
  }

  public List<Benutzer> getBestUserNachPax() {
    Platz = 0;
    return facadeSystem.getBestNachPax();
  }

  public List<Benutzer> getBestUserNachCargo() {
    Platz = 0;
    return facadeSystem.getBestNachCargo();
  }

  public List<Benutzer> getBestUserNachFluegen() {
    Platz = 0;
    return facadeSystem.getBestNachFluegen();
  }

  public List<Benutzer> getBestUserNachZeit() {
    Platz = 0;
    return facadeSystem.getBestNachZeit();
  }

  public int getKostenstellenAuswertungModus() {
    return kostenstellenAuswertungModus;
  }

  public void setKostenstellenAuswertungModus(int kostenstellenAuswertungModus) {
    this.kostenstellenAuswertungModus = kostenstellenAuswertungModus;
  }

  @SuppressWarnings("unchecked")
  public List<Object[]> getFluggesellschaftTop100NachPax() {
    Platz = 0;
    long difftage = 2592000000L; //30 * 24 * 60 * 60 * 1000;
    long bisDatum = Calendar.getInstance().getTimeInMillis();
    long vonDatum = bisDatum - difftage;
    return facadeSystem.getFluggesellschaftenNachPaxTop100(df.format(new Date(vonDatum)), df.format(new Date(bisDatum + 86400000L)));
  }

  @SuppressWarnings("unchecked")
  public List<Object[]> getFluggesellschaftTop100NachPax7Tage() {
    Platz = 0;
    long difftage = 604800000L; //7 * 24 * 60 * 60 * 1000;
    long bisDatum = Calendar.getInstance().getTimeInMillis();
    long vonDatum = bisDatum - difftage;
    return facadeSystem.getFluggesellschaftenNachPaxTop100(df.format(new Date(vonDatum)), df.format(new Date(bisDatum + 86400000L)));
  }

  @SuppressWarnings("unchecked")
  public List<Object[]> getFluggesellschaftTop100NachCargo() {
    Platz = 0;
    long difftage = 2592000000L; //30 * 24 * 60 * 60 * 1000;
    long bisDatum = Calendar.getInstance().getTimeInMillis();
    long vonDatum = bisDatum - difftage;
    return facadeSystem.getFluggesellschaftenNachCargoTop100(df.format(new Date(vonDatum)), df.format(new Date(bisDatum + 86400000L)));
  }

  @SuppressWarnings("unchecked")
  public List<Object[]> getFluggesellschaftTop100NachCargo7Tage() {
    Platz = 0;
    long difftage = 604800000L; //7 * 24 * 60 * 60 * 1000;
    long bisDatum = Calendar.getInstance().getTimeInMillis();
    long vonDatum = bisDatum - difftage;
    return facadeSystem.getFluggesellschaftenNachCargoTop100(df.format(new Date(vonDatum)), df.format(new Date(bisDatum + 86400000L)));
  }

  @SuppressWarnings("unchecked")
  public List<Object[]> getUserTop20Fluege() {
    Platz = 0;
    return facadeSystem.getUserFluegeTop20(df.format(frmvonDatum), df.format(frmbisDatum));
  }

  @SuppressWarnings("unchecked")
  public List<Object[]> getUserTop20FluegeNachZeit() {
    Platz = 0;
    return facadeSystem.getUserFluegeNachZeitTop20(df.format(frmvonDatum), df.format(frmbisDatum));
  }

  public int getPlatzierung() {
    Platz++;
    return Platz;
  }

  public List<ViewFlughaefenNachStartsLandungenMitPaxCargo> getFlughaefenNachStartsLandungen() {
    Platz = 0;
    return facadeSystem.getFlughaefenNachStartsLandungen();
  }

  public List<ViewFlughaefenNachStartsLandungenMitPaxCargo> getFlughaefenNachStartsLandungenNachPax() {
    Platz = 0;
    return facadeSystem.getFlughaefenNachStartsLandungenNachPax();
  }

  public void onLetzteFluege(){
    loadingLetzteFluege = true;
  }
  
  public List<ViewLetzteFluege> getLetzteFluege() {
    if (loadingLetzteFluege) {
      return facadeSystem.getLetzteFluege();
    } else {
      return null;
    }

  }

  public List<Yaacarskopf> getAktuelleFluege() {

    return facadeSystem.getAktuelleYAACARSFluege();
  }

  public List<ViewMeineFluggesellschaften> getMeineFluggesellschaften() {
    if (!isLoaded) {
      isLoaded = true;
      meineFluggesellschaften = facadeSystem.getMeineFluggesellschaften(UserID);
    }
    return meineFluggesellschaften;
  }

  public List<ViewLogbuchLast20Day> getLast20Logbuch() {
    long difftage = 1728000000L; //(20 * 24 * 60 * 60 * 1000);
    long vonDatum = Calendar.getInstance().getTimeInMillis();

    //System.out.println("beginn Datum :" + new Date(vonDatum - difftage));
    return facadeSystem.getLast20Logbuch(new Date(vonDatum - difftage), new Date());
  }

  public List<ViewUserFluglogBuch> getUserFlugLogBuch() {
    if (!isLoaded) {
      isLoaded = true;
      userFlugLogbuch = facadeSystem.getUserFlugLogBuch(UserID);
    }
    return userFlugLogbuch;
  }

  public List<ViewFlugzeuglog> getFlugzeuglogForAdmin() {
    if (UserIDForAdmin > 0) {
      return facadeSystem.getFlugzeugLog(UserIDForAdmin);
    }
    return null;
  }

  public List<ViewUserFluglogBuch> getUserFlugLogBuchForAdmin() {
    if (UserIDForAdmin > 0) {
      if (!isLoaded) {
        isLoaded = true;
        userFlugLogbuch = facadeSystem.getUserFlugLogBuch(UserIDForAdmin);
      }
    }
    return userFlugLogbuch;
  }

  public List<ViewKostenstellenAuswertung> getKostenstellenAuswertung() {

    if (!frmFluggesellschaftBankKonto.equals("") && kostenstellenAuswertungModus != -1) {
      //kostenstellenAuswertung = facadeSystem.getKostenstellenAuswertung(frmFluggesellschaftID, frmvonKostenstelle, frmbisKostenstelle, frmvonDatum, frmbisDatum, kostenstellenAuswertungModus);
      kostenstellenAuswertung = facadeSystem.getKostenstellenAuswertung(frmFluggesellschaftBankKonto, frmvonKostenstelle, frmbisKostenstelle, frmvonDatum, frmbisDatum, kostenstellenAuswertungModus);

      kstSummeErtraege = 0.0;
      kstSummeAufwaende = 0.0;

      if (kostenstellenAuswertung != null) {
        for (ViewKostenstellenAuswertung kst : kostenstellenAuswertung) {
          if (kst.getBetrag() > 0) {
            kstSummeErtraege += kst.getBetrag();
          } else {
            kstSummeAufwaende += kst.getBetrag();
          }
        }

        kstErgebnis = kstSummeErtraege + kstSummeAufwaende;
        return kostenstellenAuswertung;
      }
    }

    return null;

  }

  public List<Fluggesellschaft> getFluggesellschaften() {
    return facadeSystem.getFluggesellschaften(UserID);
  }

  public List<listFluggesellschaften> getFluggesellschaftenFuerKostenstellenAuswertung() {
    FluggesellschaftsListe.clear();
    listFluggesellschaften ls = null;

    //Eigene Fluggesellschaften auslesen
    for (Fluggesellschaft fg : facadeSystem.getFluggesellschaften(UserID)) {
      ls = new listFluggesellschaften();
      ls.setIdFluggesellschaft(fg.getIdFluggesellschaft());
      ls.setFluggesellschaftBankKonto(fg.getBankKonto());
      ls.setBesitzerID(fg.getUserid());
      ls.setBesitzerName(fg.getBesitzerName());
      ls.setFluggesellschaftName(fg.getName());
      FluggesellschaftsListe.add(ls);
    }

    //Managed Fluggesellschaft suchen
    Benutzer user = facadeSystem.findUserByID(UserID);
    if (user != null) {
      if (user.getFluggesellschaftManagerID() > 0) {
        Fluggesellschaftmanager mg = facadeSystem.readManagerDaten(UserID, user.getFluggesellschaftManagerID());
        if (mg != null) {
          if (mg.getAllowBank()) {
            Fluggesellschaft fg = facadeSystem.getFluggesellschaft(mg.getFluggesellschaftid());
            ls = new listFluggesellschaften();
            ls.setIdFluggesellschaft(fg.getIdFluggesellschaft());
            ls.setFluggesellschaftBankKonto(fg.getBankKonto());
            ls.setBesitzerID(fg.getUserid());
            ls.setBesitzerName(fg.getBesitzerName());
            ls.setFluggesellschaftName(fg.getName());
            FluggesellschaftsListe.add(ls);
          }
        }
      }
    }
    return FluggesellschaftsListe;
  }

  public List<Kostenstellen> getKostenstellenFuerKostenstellenAuswertung() {

    Fluggesellschaft fg = facadeSystem.getFluggesellschaftUeberBankkonto(frmFluggesellschaftBankKonto);

    if (fg != null) {
      return facadeSystem.getKostenstellen(fg.getUserid());
    }

    return null;
  }

  // ****************************************************************    Grafikchart
  private LineChartModel dateModel;

  public LineChartModel getDataModel() {
    createDateModel();
    return dateModel;
  }

  private void createDateModel() {
    dateModel = new LineChartModel();
    LineChartSeries series1 = new LineChartSeries();
    series1.setLabel("My Airline");

    series1.set("2018-01-01", 51);
    series1.set("2018-01-06", 22);
    series1.set("2018-01-12", 65);
    series1.set("2018-01-18", 74);
    series1.set("2018-01-24", 24);
    series1.set("2018-01-30", 51);

//        LineChartSeries series2 = new LineChartSeries();
//        series2.setLabel("Series 2");
// 
//        series2.set("2018-01-01", 32);
//        series2.set("2018-01-06", 73);
//        series2.set("2018-01-12", 24);
//        series2.set("2018-01-18", 12);
//        series2.set("2018-01-24", 74);
//        series2.set("2018-01-30", 62);
    dateModel.addSeries(series1);
//        dateModel.addSeries(series2);

    dateModel.setTitle("Zoom for Details, doubleclick unzoom");
    dateModel.setZoom(true);
    dateModel.getAxis(AxisType.Y).setLabel("Values");
    DateAxis axis = new DateAxis("Dates");
    axis.setTickAngle(-50);
    axis.setMax("2018-02-01");
    axis.setTickFormat("%b %#d, %y");

    dateModel.getAxes().put(AxisType.X, axis);
  }

  // ****************************************************************    Grafikchart ENDE
  /*
    ***************************** Kostenstellenverwaltung Beginn
   */
  public List<Kostenstellen> getKostenstellenFuerFluggesellschaft() {

    try {
      int fgid = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftID");
      return facadeSystem.getKostenstellen(facadeSystem.getFluggesellschaft(fgid).getUserid());

    } catch (NullPointerException e) {
      return null;
    }

  }

  public List<Kostenstellen> getKostenstellen() {
    return facadeSystem.getKostenstellen(UserID);
  }

  public void createKostenStelleFuerFluggesellschaft() {
    int fgid = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftID");
    Kostenstellen kst = new Kostenstellen();

    kst.setBezeichnung(frmBezeichnung);
    kst.setUserid(facadeSystem.getFluggesellschaft(fgid).getUserid());
    kst.setKostenstelle(frmKostenStelle);

    facadeSystem.createKostenStelle(kst);
  }

  public void createKostenStelle() {
    Kostenstellen kst = new Kostenstellen();

    kst.setBezeichnung(frmBezeichnung);
    kst.setUserid(UserID);
    kst.setKostenstelle(frmKostenStelle);

    facadeSystem.createKostenStelle(kst);
  }

  public void deleteKostenstelle() {
    facadeSystem.deleteKostenStelle(selectedKostenstelle);
  }

  public String getFrmBezeichnung() {
    return frmBezeichnung;
  }

  public void setFrmBezeichnung(String frmBezeichnung) {
    this.frmBezeichnung = frmBezeichnung;
  }

  public int getFrmKostenStelle() {
    return frmKostenStelle;
  }

  public void setFrmKostenStelle(int frmKostenStelle) {
    this.frmKostenStelle = frmKostenStelle;
  }

  public Kostenstellen getSelectedKostenstelle() {
    return selectedKostenstelle;
  }

  public void setSelectedKostenstelle(Kostenstellen selectedKostenstelle) {
    this.selectedKostenstelle = selectedKostenstelle;
  }

  /*
    ***************************** Kostenstellenverwaltung ENDE
   */
  public List<ViewUserFluglogBuch> getFilteredLogs() {
    return filteredLogs;
  }

  public void setFilteredLogs(List<ViewUserFluglogBuch> filteredLogs) {
    this.filteredLogs = filteredLogs;
  }

  public int getFrmFluggesellschaftID() {
    return frmFluggesellschaftID;
  }

  public void setFrmFluggesellschaftID(int frmFluggesellschaftID) {
    this.frmFluggesellschaftID = frmFluggesellschaftID;
  }

  public Date getFrmvonDatum() {
    return frmvonDatum;
  }

  public void setFrmvonDatum(Date frmvonDatum) {
    this.frmvonDatum = frmvonDatum;
  }

  public Date getFrmbisDatum() {
    return frmbisDatum;
  }

  public void setFrmbisDatum(Date frmbisDatum) {
    this.frmbisDatum = frmbisDatum;
  }

  public int getFrmvonKostenstelle() {
    return frmvonKostenstelle;
  }

  public void setFrmvonKostenstelle(int frmvonKostenstelle) {
    this.frmvonKostenstelle = frmvonKostenstelle;
  }

  public int getFrmbisKostenstelle() {
    return frmbisKostenstelle;
  }

  public void setFrmbisKostenstelle(int frmbisKostenstelle) {
    this.frmbisKostenstelle = frmbisKostenstelle;
  }

  public double getKstSummeErtraege() {
    return kstSummeErtraege;
  }

  public void setKstSummeErtraege(double kstSummeErtraege) {
    this.kstSummeErtraege = kstSummeErtraege;
  }

  public double getKstSummeAufwaende() {
    return kstSummeAufwaende;
  }

  public void setKstSummeAufwaende(double kstSummeAufwaende) {
    this.kstSummeAufwaende = kstSummeAufwaende;
  }

  public double getKstErgebnis() {
    return kstErgebnis;
  }

  public void setKstErgebnis(double kstErgebnis) {
    this.kstErgebnis = kstErgebnis;
  }

  public double getKstNullBetrag() {
    return kstNullBetrag;
  }

  // Piloten können sich aus eine Fluggesellschaft löschen
  public ViewMeineFluggesellschaften getSelectedFlugesellschaft() {
    return selectedFlugesellschaft;
  }

  public void setSelectedFlugesellschaft(ViewMeineFluggesellschaften selectedFlugesellschaft) {
    this.selectedFlugesellschaft = selectedFlugesellschaft;
  }

  public String BonusBezeichnungStatus(int uid, int fgid) {

    BonusAbzeichen = "";

    FlugesellschaftPiloten pilot = facadeSystem.readPilot(fgid, uid);

    String BoniBezeichnung = "";
    DecimalFormat df = new DecimalFormat("#,##0.00");

    if (pilot != null) {

      List<FluggesellschaftBonusSystem> bonuslist = facadeSystem.getBoniList(fgid);

      for (FluggesellschaftBonusSystem boni : bonuslist) {
        if (pilot.getZeit() >= boni.getZeit()) {
          //bonusProzent = boni.getBonus();
          BoniBezeichnung = boni.getBonusname() + " Bonus % (" + df.format(boni.getBonus()) + ")";
          BonusAbzeichen = boni.getUrlabzeichen();
        }
      }

    }

    return BoniBezeichnung;
  }

  public void onRemovePilot() {

    String Betreff = " *** Pilot " + findUserByID(selectedFlugesellschaft.getIduser()).getName1() + " ist aus der Fluggesellschaft : " + selectedFlugesellschaft.getName() + " ausgetreten *** ";
    String Nachricht = "Du hast den User " + findUserByID(selectedFlugesellschaft.getIduser()).getName1() + " als Piloten verloren ";

    saveMail(findUserByID(UserID).getName1(), selectedFlugesellschaft.getBesitzerName(), Betreff, Nachricht);

    facadeSystem.onRemovePilotVonDerFluggesellschaft(selectedFlugesellschaft.getIdPilot());

    facadeSystem.onRemoveFromErlaubteFlugzeuge(UserID, selectedFlugesellschaft.getIdFluggesellschaft());

  }

  public String BonusBezeichnungStatus(int uid) {

    FlugesellschaftPiloten pilot = facadeSystem.readPilot(selectedAllFluggesellschaft.getIdFluggesellschaft(), uid);

    String BoniBezeichnung = "";

    if (pilot != null) {

      List<FluggesellschaftBonusSystem> bonuslist = facadeSystem.getBoniList(selectedAllFluggesellschaft.getIdFluggesellschaft());

      for (FluggesellschaftBonusSystem boni : bonuslist) {
        if (pilot.getZeit() >= boni.getZeit()) {
          //bonusProzent = boni.getBonus();
          BoniBezeichnung = boni.getBonusname();
          BonusAbzeichen = boni.getUrlabzeichen();
        }
      }

    }

    return BoniBezeichnung;
  }

  private Benutzer getUserDaten(String name) {
    userDaten = facadeSystem.findUserByName(name);
    return userDaten;
  }

  public String getUserName(int userid) {
    return facadeSystem.getUserName(userid);
  }

  private Benutzer findUserByID(int ID) {
    userDaten = facadeSystem.findUserByID(ID);
    return userDaten;
  }

  // Piloten können sich aus eine Fluggesellschaft löschen ********************* ENDE
  public Fluggesellschaft getSelectedAllFluggesellschaft() {
    return selectedAllFluggesellschaft;
  }

  public void setSelectedAllFluggesellschaft(Fluggesellschaft selectedAllFluggesellschaft) {
    this.selectedAllFluggesellschaft = selectedAllFluggesellschaft;

    if (selectedAllFluggesellschaft != null) {
      umsatzPiloten = facadeSystem.getUmsatzPiloten(this.selectedAllFluggesellschaft.getIdFluggesellschaft());
    }
  }

  public void onReadFlugzeuge() {
    if (selectedAllFluggesellschaft != null) {
      fluggesellschaftFlugzeuge = facadeSystem.getFluggesellschaftFlugzeuge(selectedAllFluggesellschaft.getIdFluggesellschaft(), selectedAllFluggesellschaft.getUserid());
    }
  }

  public void onReadFluggesellschaftPiloten() {
    if (selectedAllFluggesellschaft != null) {
      fluggesellschaftPiloten = facadeSystem.getFluggesellschaftPiloten(selectedAllFluggesellschaft.getIdFluggesellschaft());
    }
  }

  public void onReadFluggesellschaftManager() {
    if (selectedAllFluggesellschaft != null) {
      fluggesellschaftManager = facadeSystem.getFluggesellschaftManager(selectedAllFluggesellschaft.getIdFluggesellschaft());
    }
  }

  public void onReadFluggesellschaftRouten() {
    if (selectedAllFluggesellschaft != null) {
      fluggesellschaftRouten = facadeSystem.getFluggesellschaftRouten(selectedAllFluggesellschaft.getIdFluggesellschaft());
    }

  }

  public int getUmsatzPax() {
    if (selectedAllFluggesellschaft != null) {
      return selectedAllFluggesellschaft.getGeflogeneJobs();
    }
    return 0;
  }

  public int getUmsatzCaro() {
    if (selectedAllFluggesellschaft != null) {
      return selectedAllFluggesellschaft.getGeflogenesCargo();
    }
    return 0;
  }

  public double[] getFGTableSummen(int fgid) {

    FGSummen = new double[2];
    FGSummen[0] = 0.0;
    FGSummen[1] = 0.0;

    if (fgid > 0) {
      List<ViewFlugzeugeMietKauf> flugzeuge = facadeSystem.getFlugzeugeFluggesellscahft(fgid);

      int zaehler = 0;
      double gesPrz = 0.0;
      double Flottenwert = 0.0;

      for (ViewFlugzeugeMietKauf fgz : flugzeuge) {
        zaehler = zaehler + 1;
        gesPrz = gesPrz + fgz.getZustand();

        Flottenwert = Flottenwert + onFlugzeugWert(fgz);

      }

      FGSummen[0] = gesPrz / zaehler;
      FGSummen[1] = Flottenwert;

    }

    return FGSummen;
  }

  public double[] getFGSummen() {

    FGSummen = new double[2];
    FGSummen[0] = 0.0;
    FGSummen[1] = 0.0;

    if (selectedAllFluggesellschaft != null) {
      List<ViewFlugzeugeMietKauf> flugzeuge = facadeSystem.getFlugzeugeFluggesellscahft(selectedAllFluggesellschaft.getIdFluggesellschaft());

      int ownerID = selectedAllFluggesellschaft.getUserid();
      int zaehler = 0;
      double gesPrz = 0.0;
      double Flottenwert = 0.0;

      for (ViewFlugzeugeMietKauf fgz : flugzeuge) {
        // nur FLugzeuge die der FG auch gehoeren
        if (fgz.getIdflugzeugBesitzer() == ownerID) {
          zaehler = zaehler + 1;
          gesPrz = gesPrz + fgz.getZustand();

          Flottenwert = Flottenwert + onFlugzeugWert(fgz);
        }

      }

      FGSummen[0] = gesPrz / zaehler;
      FGSummen[1] = Flottenwert;

    }

    return FGSummen;
  }

  public void setFGSummen(double[] FGSummen) {
    this.FGSummen = FGSummen;
  }

  public double getBankSaldo() {
    if (selectedAllFluggesellschaft != null) {
      return facadeSystem.getBankSaldo(selectedAllFluggesellschaft.getBankKonto());
    }
    return 0.0;
  }

  public double onFlugzeugWert(ViewFlugzeugeMietKauf currentFlugzeug) {

    Flugzeuge stammfg = null;

    stammfg = facadeSystem.readFlugzeugbyID(currentFlugzeug.getIdFlugzeug());

    if (stammfg != null) {

      // um die Maximale Nutzungsdauer zu Berechnen wird die gewöhnliche Nutzungsdauer mit 2 mulitpliziert
      int KolbenmotorGewoehnlichNutzungsdauer = 12000;
      int TurboPropGewoehnlichNutzungsdauer = 16000;
      int TurbineGewoehnlichNutzungsdauer = 20000;
      int AktuellesJahr = Calendar.getInstance().get(Calendar.YEAR);

      double ZellenStunden = currentFlugzeug.getGesamtAlterDesFlugzeugsMinuten() / 60;
      double Alter = AktuellesJahr - currentFlugzeug.getBaujahr();
      double MaximaleNutzungsdauer = 0;
      double Wert = stammfg.getVerkaufspreis();
      double Zustand = currentFlugzeug.getZustand();

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

      // Kalkulierter Marktpreis(mit Zellenstunden) =  FTW-Neupreis - ((Zellenstunden/ Maximale Nutzungsdauer )  * Kalkulierter Neupreis)
      double KalkulierterMarktPreisMitZellenstunden = Wert - ((ZellenStunden / MaximaleNutzungsdauer) * Wert);

      // Marktpreis mit Alter = Kalkulierter Marktpreis mit Zellenstunden - (Kalkulierter Marktpreis mit Zellenstunden*(Alter/100))
      double MarktpreisMitAlter = KalkulierterMarktPreisMitZellenstunden - (KalkulierterMarktPreisMitZellenstunden * (Alter / 100.));

      // Verkaufspreis = Zustand  / 100 * Kalkulierter Marktpreis mit Alter
      double Verkaufspreis = (Zustand / 100.) * MarktpreisMitAlter;

      return Verkaufspreis;

    }
    return 0.0;
  }

  public double getTraffikIndexPax() {
    double geflogen = 0.0;
    double erzeugt = 0.0;
    traffikIndex = 0.0;

    if (selectedAllFluggesellschaft != null) {
      Fluggesellschaft fg = facadeSystem.getFluggesellschaft(selectedAllFluggesellschaft.getIdFluggesellschaft());
      geflogen = fg.getGeflogeneJobs();
      erzeugt = fg.getErzeugteJobs();

      return 100 / erzeugt * geflogen;
    }
    return 0.0;
  }

  public double getTraffikIndexCargo() {
    double geflogen = 0.0;
    double erzeugt = 0.0;
    traffikIndex = 0.0;

    if (selectedAllFluggesellschaft != null) {
      Fluggesellschaft fg = facadeSystem.getFluggesellschaft(selectedAllFluggesellschaft.getIdFluggesellschaft());
      geflogen = fg.getGeflogenesCargo();
      erzeugt = fg.getErzeugtesCargo();

      return 100 / erzeugt * geflogen;
    }
    return 0.0;
  }

  public String getBonusAbzeichen() {
    return BonusAbzeichen;
  }

  public void setBonusAbzeichen(String BonusAbzeichen) {
    this.BonusAbzeichen = BonusAbzeichen;
  }

  public int getUserIDForAdmin() {
    return UserIDForAdmin;
  }

  public void setUserIDForAdmin(int UserIDForAdmin) {
    this.UserIDForAdmin = UserIDForAdmin;
  }

  public String getFrmFluggesellschaftBankKonto() {
    return frmFluggesellschaftBankKonto;
  }

  public void setFrmFluggesellschaftBankKonto(String frmFluggesellschaftBankKonto) {
    this.frmFluggesellschaftBankKonto = frmFluggesellschaftBankKonto;
  }

  public void saveMail(String Von, String An, String Betreff, String Nachricht) {

    //Absendermail speichern
    Benutzer absender = getUserDaten(Von);
    Benutzer empfaenger = getUserDaten(An);

    Mail nm = new Mail();
    if (!An.equals(Von)) {
      nm.setUserID(absender.getIdUser());
      nm.setAnID(empfaenger.getIdUser());
      nm.setAnUser(empfaenger.getName1());
      nm.setBetreff(Betreff);
      nm.setDatumZeit(new Date());
      nm.setGelesen(false);
      nm.setKategorie("Posteingang");
      nm.setVonID(absender.getIdUser());
      nm.setVonUser(absender.getName1());
      nm.setNachrichtText(CONF.NoScript(Nachricht));
      facadeSystem.saveUserMail(nm);
    }

    //Empfaengermail speichern
    if (!An.equals(Von)) {
      nm = new Mail();
      nm.setUserID(empfaenger.getIdUser());
      nm.setAnID(empfaenger.getIdUser());
      nm.setAnUser(empfaenger.getName1());
      nm.setBetreff(Betreff);
      nm.setDatumZeit(new Date());
      nm.setGelesen(false);
      nm.setKategorie("Posteingang");
      nm.setVonID(absender.getIdUser());
      nm.setVonUser(absender.getName1());
      nm.setNachrichtText(CONF.NoScript(Nachricht));
      facadeSystem.saveUserMail(nm);
    }

  }
}
