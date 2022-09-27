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

package de.klees.beans.tankstelle;

import de.klees.beans.system.loginMB;
import de.klees.beans.takeoff.TakeoffFacade;
import de.klees.data.Bank;
import de.klees.data.FboUserObjekte;
import de.klees.data.Feinabstimmung;
import de.klees.data.Fluggesellschaft;
import de.klees.data.Fluggesellschaftmanager;
import de.klees.data.Flugzeugemietkauf;
import de.klees.data.Benutzer;
import de.klees.data.views.ViewFBOUserObjekte;
import de.klees.data.views.ViewFlugzeugeMietKauf;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Stefan Klees
 */
@Named(value = "tankstelleBean")
@ViewScoped
public class tankstelleBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private ViewFlugzeugeMietKauf selectedFlugzeug;
  private Flugzeugemietkauf MeinFlugzeug;
  private Feinabstimmung config;
  private Fluggesellschaft fg;

  private String Abflugflughafen;

  // ************** Tankstelle
  private List<ViewFBOUserObjekte> Tankstellen;
  private String TankstellenName;
  private String TankstellenGrafik;
  private int TankstelleTreibstoffArt;
  private int TankstelleSpritMenge;
  private int TankstelleMaxSpritMenge;
  private double TankstelleSumme;
  private int TankstelleMengeLbs;
  private boolean TankenBezahlt;
  private boolean TankenFreigeben;

  private double TankMengeProzent;
  private int TankMengeKilo;
  private int TankBisFuellstandKG;
  private int maxTankkapazitaet;
  private int TankRestMengeSprit;
  private int Spritverbrauch;
  private double SpritPreis;
  private boolean istPrivateTankstelleVorhanden;
  private int TreibstoffArt;

  private int objektID;

  private boolean AbrechnungsFehler;
  private boolean EnttankenLaeuft;

  private int UserID;

  private final DecimalFormat dfWaehrung = new DecimalFormat("#,##0.00");

  /**
   * Creates a new instance of tankstelleBean
   */
  public tankstelleBean() {

    UserID = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
  }

  @EJB
  TakeoffFacade facadeTankstelle;

  private void aircraftUpdate(String Art) {

    MeinFlugzeug = facadeTankstelle.findeMeinFlugzeug(selectedFlugzeug.getIdMietKauf());

    if (Art.equals("Tanken")) {
      MeinFlugzeug.setAktuelleTankfuellung(TankMengeKilo + TankRestMengeSprit);
      MeinFlugzeug.setLetzterSpritPreis(SpritPreis);
    }

    facadeTankstelle.editMeinFlugzeug(MeinFlugzeug);

  }

  /*
  *******************************************************  Tanken BEGINN
   */
  public void onResetTankstelle() {
    TankMengeProzent = 0;
    TankMengeKilo = 0;
    TankBisFuellstandKG = 0;
    config = facadeTankstelle.readConfig();
    berechneTankfuellung();
  }

  public void onTankenBezahlen() {

    setTankenBezahlt(true);
    FboUserObjekte fboobjekt = null;
    Benutzer user = null;

    AbrechnungsFehler = false;

    String EmpfaegerName = "**** FTW OIL *****";
    String EmpfaengerKonto = "500-1000003";
    String FlugzeugReg = selectedFlugzeug.getRegistrierung();
    int objektid = -1;
    String icao = "";
    int Kostenstelle = 0;
    String VerwendungsZweck = "";

    if (!EnttankenLaeuft) {

      EnttankenLaeuft = true;

      if (TankstelleSumme > 0) {

        onTankenBerechneKilo();

        if (objektID > 0) {
          fboobjekt = facadeTankstelle.getUserFBOObjekt(objektID);
          user = facadeTankstelle.findByUserName(fboobjekt.getIdUser());

          EmpfaegerName = fboobjekt.getKontoName();
          EmpfaengerKonto = fboobjekt.getBankkonto();
        }

        String UserName = fg.getName();
        String UserKonto = fg.getBankKonto();

        if (getTankstelleTreibstoffArt() == 1) {
          VerwendungsZweck = "*** FTW OIL *** AVGAS : " + dfWaehrung.format(config.getPreisAVGASkg()) + " € - " + TankMengeKilo + " (kg) - Reg: " + FlugzeugReg;
          SpritPreis = config.getPreisAVGASkg();
          if (objektID > 0) {
            // es wurde nicht bei FTW-Oil getankt
            SpritPreis = fboobjekt.getPreisAVGAS();
            VerwendungsZweck = fboobjekt.getName() + " *** AVGAS *** " + TankMengeKilo + " (kg) " + dfWaehrung.format(SpritPreis) + " € - Reg: " + FlugzeugReg;
            // Spritmengen verbuchen
            fboobjekt.setBestandAVGASkg(fboobjekt.getBestandAVGASkg() - TankMengeKilo);
            facadeTankstelle.FboBestandVerbuchen(fboobjekt);
            objektid = fboobjekt.getIdfboObjekt();
            icao = fboobjekt.getIcao();
            Kostenstelle = fboobjekt.getKostenstelle();
          }

        } else {
          VerwendungsZweck = "*** FTW OIL *** JETA : " + dfWaehrung.format(config.getPreisJETAkg()) + " € - " + TankMengeKilo + " (kg) - Reg: " + FlugzeugReg;
          SpritPreis = config.getPreisJETAkg();
          if (objektID > 0) {
            // es wurde nicht bei FTW-Oil getankt
            SpritPreis = fboobjekt.getPreisJETA();
            VerwendungsZweck = fboobjekt.getName() + " *** JETA *** " + TankMengeKilo + " (kg) - " + dfWaehrung.format(SpritPreis) + " € - Reg: " + FlugzeugReg;
            // Spritmengen verbuchen
            fboobjekt.setBestandJETAkg(fboobjekt.getBestandJETAkg() - TankMengeKilo);
            facadeTankstelle.FboBestandVerbuchen(fboobjekt);
            objektid = fboobjekt.getIdfboObjekt();
            icao = fboobjekt.getIcao();
            Kostenstelle = fboobjekt.getKostenstelle();
          }
        }

        //Tankstelle Kundenzahlung
        SaveBankbuchung(UserKonto, UserName, UserKonto, UserName, new Date(), TankstelleSumme - (TankstelleSumme * 2), EmpfaengerKonto, EmpfaegerName,
                new Date(), VerwendungsZweck, UserID, -1, -1, -1, -1, -1, -1, icao, objektid, -1, Kostenstelle, -1);

        //Gegenbuchung beim Tankstellenbesitzer
        if (objektID > 0) {
          SaveBankbuchung(EmpfaengerKonto, EmpfaegerName, UserKonto, UserName, new Date(), TankstelleSumme, EmpfaengerKonto, EmpfaegerName,
                  new Date(), VerwendungsZweck, fboobjekt.getIdUser(), -1, -1, -1, -1, -1, -1, icao, objektid, -1, Kostenstelle, -1);
        } else {
          SaveBankbuchung(EmpfaengerKonto, EmpfaegerName, UserKonto, UserName, new Date(), TankstelleSumme, EmpfaengerKonto, EmpfaegerName,
                  new Date(), VerwendungsZweck, UserID, -1, -1, -1, -1, -1, -1, "", -1, -1, -1, -1);

        }

        aircraftUpdate("Tanken");

        onResetTankstelle();

      } else {
        boolean enttankt = false;

        onTankenBerechneKilo();

        // Wenn die Tanksumme im minus ist 
        // Flugzeug soll entankt werden
        if (selectedFlugzeug.getIdflugzeugBesitzer() == UserID || selectedFlugzeug.getLeasingAnUserID() == UserID) {
          if (TankstelleSumme < 0) {

            double PreisAVGAS = config.getPreisAVGASkg();
            double PreisJetA = config.getPreisJETAkg();;

            onTankLeeren();

            if (getTankstelleTreibstoffArt() == 1) {
              // 0.80 Euro pro Kilo für das Entleeren
              SpritPreis = (PreisAVGAS - 0.80);
              VerwendungsZweck = "*** FTW OIL *** AVGAS : (" + dfWaehrung.format(SpritPreis) + " €) " + TankMengeKilo + " (kg) - Reg: " + FlugzeugReg;
            } else {
              // 0.80 Euro pro Kilo für das Entleeren
              SpritPreis = (PreisJetA - 0.80);
              VerwendungsZweck = "*** FTW OIL *** JETA : (" + dfWaehrung.format(SpritPreis) + " €) " + TankMengeKilo + " (kg) - Reg: " + FlugzeugReg;
            }

            TankMengeKilo = TankMengeKilo - (TankMengeKilo * 2);

            Double Betrag = SpritPreis * TankMengeKilo;

            onTankenRueckerstattung(Betrag, VerwendungsZweck);

            enttankt = true;
          }
        }

        // Managern erlauben das Flugzeug zu enttanken, wenn Flugzeug im Besitz der FG ist
        // Gutschrift erhält Tankstellenbesitzer
        // ManagerID Auslesen
        if (!enttankt) {
          user = facadeTankstelle.findByUserName(UserID);
          if (user != null) {
            if (user.getFluggesellschaftManagerID() > 0) {
              //Fluggesellschaft auslesen
              Fluggesellschaft fg = facadeTankstelle.readFluggesellschaft(user.getFluggesellschaftManagerID());
              if (fg != null) {
                //Managerberechtigung auslesen
                Fluggesellschaftmanager fgm = facadeTankstelle.readManager(UserID, fg.getIdFluggesellschaft());
                if (fgm != null) {
                  if (fgm.getAllowFlugzeugeBearbeiten()) {

                    double PreisAVGAS = config.getPreisAVGASkg();
                    double PreisJetA = config.getPreisJETAkg();;

                    // Tank leeren
                    onTankLeeren();

                    if (getTankstelleTreibstoffArt() == 1) {
                      // 0.80 Euro pro Kilo für das Entleeren
                      SpritPreis = (PreisAVGAS - 0.80);
                      VerwendungsZweck = "*** FTW OIL *** AVGAS : (" + dfWaehrung.format(SpritPreis) + " €) " + TankMengeKilo + " (kg) - Reg: " + FlugzeugReg;
                    } else {
                      // 0.80 Euro pro Kilo für das Entleeren
                      SpritPreis = (PreisJetA - 0.80);
                      VerwendungsZweck = "*** FTW OIL *** JETA : (" + dfWaehrung.format(SpritPreis) + " €) " + TankMengeKilo + " (kg) - Reg: " + FlugzeugReg;
                    }

                    EmpfaegerName = facadeTankstelle.getKontoName(selectedFlugzeug.getBankkontoBesitzer());
                    EmpfaengerKonto = selectedFlugzeug.getBankkontoBesitzer();

                    TankMengeKilo = TankMengeKilo - (TankMengeKilo * 2);

                    Double Betrag = (SpritPreis * TankMengeKilo);

                    SaveBankbuchung(EmpfaengerKonto, EmpfaegerName, "500-1000003", "**** FTW OIL *****", new Date(), Betrag, EmpfaengerKonto, EmpfaegerName,
                            new Date(), VerwendungsZweck, -1, -1, -1, -1, -1, -1, -1, "", -1, -1, 0, -1);

                    enttankt = true;

                  }
                }
              }
            }
          }

        }
      }
    }

  }

  public void onTankLeeren() {
    MeinFlugzeug = facadeTankstelle.findeMeinFlugzeug(selectedFlugzeug.getIdMietKauf());

    if (MeinFlugzeug.getAktuelleTankfuellung() > 0) {

      // TankMengeKilo ist ein negativer Wert, deshalb +
      if (selectedFlugzeug.getTreibstoffArt() == 1) {
        MeinFlugzeug.setAktuelleTankfuellung(MeinFlugzeug.getAktuelleTankfuellung() + TankMengeKilo);
        setTankenBezahlt(false);
        setTankstelleSumme(0);
        setTankstelleSpritMenge(0);
        setTankstelleMengeLbs(0);
        setTankRestMengeSprit(0);
      } else {
        MeinFlugzeug.setAktuelleTankfuellung(MeinFlugzeug.getAktuelleTankfuellung() + TankMengeKilo);
        setTankenBezahlt(false);
        setTankstelleSumme(0);
        setTankstelleSpritMenge(0);
        setTankstelleMengeLbs(0);
        setTankRestMengeSprit(0);
      }
      facadeTankstelle.editMeinFlugzeug(MeinFlugzeug);
      NewMessage(loginMB.onSprache("Terminal_msg_FlugzeugWurdeEnttankt"));
    }

  }

  public void onTankenRueckerstattung(double Betrag, String VerwendungsZweck) {
    AbrechnungsFehler = false;
    String UserName = fg.getName();
    String UserKonto = fg.getBankKonto();

    SaveBankbuchung(UserKonto, UserName, "500-1000003", "**** FTW OIL *****", new Date(), Betrag, UserKonto, UserName,
            new Date(), VerwendungsZweck, UserID, -1, -1, -1, -1, -1, -1, selectedFlugzeug.getAktuellePositionICAO().trim(), -1, -1, -1, -1);
  }

  public void berechneTankfuellung() {

    AbrechnungsFehler = false;

    double PreisAVGAS = config.getPreisAVGASkg();
    double PreisJETA = config.getPreisJETAkg();
    int BestandAVGAS = 0;
    int BestandJETA = 0;

    TankstellenName = "";
    TankstellenGrafik = "http://www.ftw-sim.de/images/FTW/images/tankstelle.png";
    TankstelleSumme = 0.0;

    TankenFreigeben = false;

    if (objektID > 0) {

      TankenFreigeben = true;
      setIstPrivateTankstelleVorhanden(true);

      FboUserObjekte fboobjekt = facadeTankstelle.getUserFBOObjekt(objektID);

      PreisAVGAS = fboobjekt.getPreisAVGAS();
      PreisJETA = fboobjekt.getPreisJETA();

      BestandAVGAS = fboobjekt.getBestandAVGASkg();
      BestandJETA = fboobjekt.getBestandJETAkg();

      //1 = AVGAS, 2 = JETA
      if (TankstelleTreibstoffArt == 1 && BestandAVGAS <= 0) {
        TankenFreigeben = false;
      } else if (TankstelleTreibstoffArt == 2 && BestandJETA <= 0) {
        TankenFreigeben = false;
      }
      
      TankstellenName = fboobjekt.getName();
      TankstellenGrafik = fboobjekt.getGrafikLink();

      if (fboobjekt.getGrafikLink() != null) {
        if (TankstellenGrafik.equals("")) {
          TankstellenGrafik = "http://www.ftw-sim.de/images/FTW/images/tankstelle.png";
        }
      } else {
        TankstellenGrafik = "http://www.ftw-sim.de/images/FTW/images/tankstelle.png";
      }

      if (getTankstelleTreibstoffArt() == 1) {
        if (TankMengeKilo > fboobjekt.getBestandAVGASkg()) {
          setTankMengeKilo(fboobjekt.getBestandAVGASkg());
        }
      }

      if (getTankstelleTreibstoffArt() == 2) {
        if (TankMengeKilo > fboobjekt.getBestandJETAkg()) {
          setTankMengeKilo(fboobjekt.getBestandJETAkg());
        }
      }
      if (getTankstelleTreibstoffArt() == 1) {
        setTankstelleSumme((double) TankMengeKilo * PreisAVGAS);
        setTankstelleMengeLbs((int) getKG2Libs(TankMengeKilo));
      } else {
        setTankstelleSumme((double) TankMengeKilo * PreisJETA);
        setTankstelleMengeLbs((int) getKG2Libs(TankMengeKilo));
      }
    }

    // FTW - Tankstelle
    if (objektID == -1) {
      TankenFreigeben = true;
      if (getTankstelleTreibstoffArt() == 1) {
        setTankstelleSumme((double) TankMengeKilo * PreisAVGAS);
        setTankstelleMengeLbs((int) getKG2Libs(TankMengeKilo));
      } else {
        setTankstelleSumme((double) TankMengeKilo * PreisJETA);
        setTankstelleMengeLbs((int) getKG2Libs(TankMengeKilo));
      }

    }

  }

  public void ontankenStart() {
    EnttankenLaeuft = false;
    Abflugflughafen = selectedFlugzeug.getAktuellePositionICAO();
    TreibstoffArt = selectedFlugzeug.getTreibstoffArt();
    TankstelleTreibstoffArt = selectedFlugzeug.getTreibstoffArt();
    TankRestMengeSprit = selectedFlugzeug.getAktuelleTankfuellung();
    TankstelleMaxSpritMenge = selectedFlugzeug.getTreibstoffkapazitaet() - TankRestMengeSprit;
    maxTankkapazitaet = selectedFlugzeug.getTreibstoffkapazitaet();

    fg = facadeTankstelle.readFluggesellschaft((int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("FluggesellschaftID"));

    onResetTankstelle();
    berechneTankfuellung();
  }

  public List<ViewFBOUserObjekte> getTankstellen() {
    Tankstellen = facadeTankstelle.getTankstellen(Abflugflughafen);
    if (Tankstellen.isEmpty()) {
      setIstPrivateTankstelleVorhanden(false);
    } else {
      setIstPrivateTankstelleVorhanden(onTankMengenPruefen());
    }

    try {
      if (facadeTankstelle.getAirportByIcao(Abflugflughafen).getLuftversorgung()) {
        setIstPrivateTankstelleVorhanden(true);
      }
    } catch (NullPointerException e) {
    }

    return Tankstellen;
  }

  public void onTankenBerechneProzentTankfüllung() {
    int inhalt = selectedFlugzeug.getAktuelleTankfuellung();
    TankBisFuellstandKG = (int) (maxTankkapazitaet / 100.0 * TankMengeProzent);

    TankMengeKilo = TankBisFuellstandKG - inhalt;

    onTankenBerechneKilo();
  }

  public void onTankenBerechneKilo() {
    int inhalt = selectedFlugzeug.getAktuelleTankfuellung();

    inhalt = inhalt + TankMengeKilo;

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

    int inhalt = selectedFlugzeug.getAktuelleTankfuellung();
    boolean tanken = true;

    if (TankBisFuellstandKG < 0) {
      tanken = false;
      NewMessage("Füllstand muss größer 0 sein");
      onResetTankstelle();

    }

    if (tanken) {
      if (TankBisFuellstandKG >= inhalt) {
        TankMengeKilo = TankBisFuellstandKG - inhalt;
      } else {
        TankMengeKilo = inhalt - TankBisFuellstandKG;
        TankMengeKilo = TankMengeKilo - (TankMengeKilo * 2);

        if (TankMengeKilo + inhalt < 0) {
          TankMengeKilo = TankMengeKilo - (inhalt * 2);
        }

      }

      if (inhalt + TankMengeKilo > maxTankkapazitaet) {
        TankMengeKilo = maxTankkapazitaet - inhalt;
        TankBisFuellstandKG = maxTankkapazitaet;
      }

      TankMengeProzent = 100.0 / maxTankkapazitaet * TankBisFuellstandKG;

      //******************************* Summenbildung
      berechneTankfuellung();
    }
  }

  /**
   * Liefert true zurueck wenn private Tankstellen von beiden Sorten Sprit haben <br>
   * False entweder keine Tankstelle vorhanden oder eine Spritsorte ist 0
   *
   * @param keiner
   */
  private boolean onTankMengenPruefen() {
    @SuppressWarnings("unchecked")
    List<Object[]> Bestaende = facadeTankstelle.getTankstellenBestaende(Abflugflughafen);

    if (Bestaende != null) {
      if ((double) Bestaende.get(0)[0] > 0 && (double) Bestaende.get(0)[1] > 0) {
        return true;
      }
    }

    return false;
  }

  /*
  *******************************************************  Tanken Ende  
   */
  private void NewMessage(String neueNachricht) {
    FacesMessage msg = new FacesMessage(neueNachricht);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  /*
  Setter and Getter
   */
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

  public int getTankBisFuellstandKG() {
    return TankBisFuellstandKG;
  }

  public void setTankBisFuellstandKG(int TankBisFuellstandKG) {
    this.TankBisFuellstandKG = TankBisFuellstandKG;
  }

  public int getTankstelleTreibstoffArt() {
    return TankstelleTreibstoffArt;
  }

  public void setTankstelleTreibstoffArt(int TankstelleTreibstoffArt) {
    this.TankstelleTreibstoffArt = TankstelleTreibstoffArt;
  }

  public int getTankstelleSpritMenge() {
    return TankstelleSpritMenge;
  }

  public void setTankstelleSpritMenge(int TankstelleSpritMenge) {
    this.TankstelleSpritMenge = TankstelleSpritMenge;
  }

  public int getTankstelleMaxSpritMenge() {
    return TankstelleMaxSpritMenge;
  }

  public void setTankstelleMaxSpritMenge(int TankstelleMaxSpritMenge) {
    this.TankstelleMaxSpritMenge = TankstelleMaxSpritMenge;
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

  public boolean isTankenBezahlt() {
    return TankenBezahlt;
  }

  public void setTankenBezahlt(boolean TankenBezahlt) {
    this.TankenBezahlt = TankenBezahlt;
  }

  public double getTankMengeProzent() {
    return TankMengeProzent;
  }

  public void setTankMengeProzent(double TankMengeProzent) {
    this.TankMengeProzent = TankMengeProzent;
  }

  public int getTankRestMengeSprit() {
    return TankRestMengeSprit;
  }

  public void setTankRestMengeSprit(int TankRestMengeSprit) {
    this.TankRestMengeSprit = TankRestMengeSprit;
  }

  public String getTankstellenName() {
    return TankstellenName;
  }

  public void setTankstellenName(String TankstellenName) {
    this.TankstellenName = TankstellenName;
  }

  public String getTankstellenGrafik() {
    return TankstellenGrafik;
  }

  public void setTankstellenGrafik(String TankstellenGrafik) {
    this.TankstellenGrafik = TankstellenGrafik;
  }

  public int getMaxTankkapazitaet() {
    return maxTankkapazitaet;
  }

  public void setMaxTankkapazitaet(int maxTankkapazitaet) {
    maxTankkapazitaet = maxTankkapazitaet;
  }

  public boolean isTankenFreigeben() {
    return TankenFreigeben;
  }

  public void setTankenFreigeben(boolean TankenFreigeben) {
    this.TankenFreigeben = TankenFreigeben;
  }

  public boolean isIstPrivateTankstelleVorhanden() {
    return istPrivateTankstelleVorhanden;
  }

  public void setIstPrivateTankstelleVorhanden(boolean istPrivateTankstelleVorhanden) {
    this.istPrivateTankstelleVorhanden = istPrivateTankstelleVorhanden;
  }

  public int getTreibstoffArt() {
    return TreibstoffArt;
  }

  public void setTreibstoffArt(int TreibstoffArt) {
    this.TreibstoffArt = TreibstoffArt;
  }

  public String getAbflugflughafen() {
    return Abflugflughafen;
  }

  public void setAbflugflughafen(String Abflugflughafen) {
    this.Abflugflughafen = Abflugflughafen;
  }

  public ViewFlugzeugeMietKauf getSelectedFlugzeug() {
    return selectedFlugzeug;
  }

  public void setSelectedFlugzeug(ViewFlugzeugeMietKauf selectedFlugzeug) {
    this.selectedFlugzeug = selectedFlugzeug;
  }

  public int getObjektID() {
    return objektID;
  }

  public void setObjektID(int objektID) {
    this.objektID = objektID;
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

    facadeTankstelle.createBankbuchung(newBuchung);

  }

}
