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

import de.klees.beans.user.UserFacade;
import de.klees.data.Mail;
import de.klees.data.Benutzer;
import de.klees.data.UserTyperatings;
import de.klees.data.WartungsTabelle;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author stefan
 */
//@SessionScoped
//@Named("loginMB")
public class loginMB implements Serializable {

  private static final long serialVersionUID = 1L;

  private Boolean DemoUser = false;
  private Integer loginfehler = 0;
  private Benutzer CurrentUser;

  private String username;
  private String password;
  private String Theme;
  private Boolean adminuser;
  private Boolean moduser;
  private String Rolle;
  private String SprachCode;

  private boolean isTippOn;
  private boolean ArrivalsOn;
  private boolean DeparturesOn;

  private boolean isDlgLoggedUserOn;

  private boolean allowFlugzeugEdit;
  private boolean allowBenutzerEdit;
  private boolean allowFlughafenEdit;
  private boolean allowGeschichtenEdit;
  private boolean allowNewsEdit;
  private boolean allowRettungsstationEdit;
  private boolean allowAirportDispatcher;
  private boolean allowTestuser;

  private boolean allowToolsOpen;
  private boolean allowAdminOpen;

  private boolean summenFensterAnzeigen;

  private boolean newMenue;

  private boolean istGesperrt;

  private boolean Discord;

  private String systemmeldungText;
  private String ftwVersion;
  private String Rang;
  private String RangAbzeichenURL;
  private String Lizenz;

  private int iconSize;

  private Map<String, String> Sprache;
  private Locale locale;
  private final Calendar c = Calendar.getInstance();
  private final DecimalFormat df = new DecimalFormat("#,##0");
  private final SimpleDateFormat datf = new SimpleDateFormat("dd.MM.yyyy");

  private boolean eingeloggt;
  private boolean wartungsTabelleAnzeigen;

  private static boolean resBundleLoaded;
  private static ResourceBundle messages;

  private String mapLayer;
  private int tabellenzeilen;

  private String HeraldUrl;
  private boolean heraldMeldung;

  private boolean supportUser;

  @EJB
  private UserFacade facadeUser;

  /*
   * 
   */
  public loginMB() {
    this.Theme = "flick";
    isTippOn = false;
    DeparturesOn = false;
    ArrivalsOn = false;
    isDlgLoggedUserOn = false;
    DemoUser = false;
    moduser = false;
    iconSize = 16;
    eingeloggt = false;
    wartungsTabelleAnzeigen = true;
    locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
    resBundleLoaded = false;
    mapLayer = "OpenTopoMap";
    HeraldUrl = "";
    heraldMeldung = false;

  }

//  @PostConstruct
//    public void init() {
//        locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
//    }
  public List<WartungsTabelle> getWartungsMeldungen() {

    return facadeUser.readWartungsTabelle();
  }

  public Locale getLocale() {
    return locale;
  }

  public String getLanguage() {
    return locale.getLanguage();
  }

  public void setLanguage(String language) {
    locale = new Locale(language);
    FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public void tipOfTheDayOnOff() {
    isTippOn = !isTippOn;
  }

  public void onSystemMeldungenOff() {
    wartungsTabelleAnzeigen = false;
  }

  public void departuresOnOff() {
    DeparturesOn = !DeparturesOn;
  }

  public void arrivalsOnOff() {
    ArrivalsOn = !ArrivalsOn;
  }

  public String[] getZeitZonen() {
    return TimeZone.getAvailableIDs();
  }

  public static String onSprache(String key) {
    messages = ResourceBundle.getBundle("de.klees.beans.system.resources/sprache", new Locale((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Sprache")));
    return messages.getString(key);
  }

  public String login() {

    FacesContext fc = FacesContext.getCurrentInstance();
    HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();

    try {

      request.login(username, password);

      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ip-Adresse", request.getRemoteAddr());
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user-agent", request.getHeader("user-agent"));
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("session-id", fc.getExternalContext().getSessionId(false));

      setNewMenue(true);

      Benutzer userdaten = getUserdaten();

      setTheme(userdaten.getTheme());
      setRolle(userdaten.getRolle());
      setIconSize(userdaten.getIconSize());

      //************************** Sprache setzen
      setLanguage(CurrentUser.getSprache());
      FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(CurrentUser.getSprache()));
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Sprache", CurrentUser.getSprache());

      //************************* Map Layer auslesen
      setMapLayer(CurrentUser.getMapLayer());
      //**************************
      LastLoging();

      RangAbzeichen();

      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Rangabzeichen", RangAbzeichenURL);
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Rang", Rang);
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Lizenz", Lizenz);
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Rolle", "");

      ftwVersion = facadeUser.readConfig().getFtwVersion();

      if (CurrentUser.getFunktion().equals("Support")) {
        supportUser = true;
      } else {
        supportUser = false;
      }

      if (request.isUserInRole("Administratoren")) {
        setAdminuser(true);
        setEingeloggt(true);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Rolle", "Admin");
        return "/users/index.xhtml?faces-redirect=true";
      } else if (request.isUserInRole("Users")) {
        setAdminuser(false);
        setEingeloggt(true);
        return "/users/index.xhtml?faces-redirect=true";
      } else if (request.isUserInRole("DOB")) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Rolle", "M.O.D.");
        setAdminuser(true);
        setModuser(true);
        setEingeloggt(true);
        return "/users/index.xhtml?faces-redirect=true";
      }
      setAdminuser(false);
      setEingeloggt(true);
      return "/users/index.xhtml?faces-redirect=true";

    } catch (ServletException e) {

      fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Es trat ein Fehler beim Anmelden auf, bitte prüfen Sie Passwort und Benutzer", null));

    }
    return "/logginerror?faces-redirect=true";
  }

  public String logout() {

    //System.out.println("de.klees.beans.system.loginMB.logout() : " + username);
    if (CurrentUser != null) {
      //Userdaten auffrischen, evtl. wurde in den Einstellungen was verändert
      CurrentUser = getUserdaten();
      Banner();
    }

    FacesContext fc = FacesContext.getCurrentInstance();
    HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
    HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);

    if (session != null) {
//      System.out.println("de.klees.beans.system.loginMB.logout() Session invalidate: " + session.getAttribute("UserName"));
//
//      System.out.println("de.klees.beans.system.loginMB.logout() IP:  " + request.getRemoteAddr());
//      System.out.println("de.klees.beans.system.loginMB.logout() LoginName: " + request.getRemoteUser());

      session.invalidate();
      return "/logout?faces-redirect=true";
    }
    return "";
  }

  private void Banner() {

    //http://www.street68.de/images/ftw-banner/banner.png
    try {

      String httpURL = CurrentUser.getBanner();
      URL imageUrl;
      HttpURLConnection httpconnection;
      HttpsURLConnection httpsconnection;
      imageUrl = new URL(httpURL);
      BufferedImage image;
      Graphics g;

      if (httpURL.toLowerCase().contains("https")) {
        httpsconnection = (HttpsURLConnection) imageUrl.openConnection();
        image = ImageIO.read(httpsconnection.getInputStream());
        httpsconnection.disconnect();

      } else {
        httpconnection = (HttpURLConnection) imageUrl.openConnection();
        image = ImageIO.read(httpconnection.getInputStream());
        httpconnection.disconnect();
      }

      g = image.getGraphics();
      Font f = new Font("Serif", Font.BOLD, 9);
      //g.setFont(g.getFont().deriveFont(15f));
      g.setFont(f);
      g.setColor(Color.BLUE);

      g.drawString("(" + CurrentUser.getIdUser() + ")  " + CurrentUser.getName1(), 45, 9);
      g.drawString(String.valueOf(datf.format(CurrentUser.getCreated())), 45, 21);
      g.drawString(String.valueOf(df.format(CurrentUser.getFlights())), 45, 33);
      g.drawString(String.valueOf(df.format(CurrentUser.getFlighttime() / 60)), 45, 45);
      g.drawString(String.valueOf(df.format(CurrentUser.getFlightmiles())), 45, 58);

      g.drawString(String.valueOf(df.format(CurrentUser.getFlightpaxes())), 165, 33);
      g.drawString(String.valueOf(df.format(CurrentUser.getFlightcargo())) + " (kg)", 165, 45);
      g.drawString(Rang, 165, 58);

      g.dispose();

      String Datei = "/opt/glassfish4/" + CurrentUser.getIdUser() + ".png";

      ImageIO.write(image, "png", new File(Datei));

    } catch (IOException | NullPointerException e) {

//      System.out.println("Beim Schreiben des Banners ist ein Fehler aufgetreten");
//      System.out.println("Banner URL = " + CurrentUser.getBanner());
//      System.out.println("Username = " + CurrentUser.getName());
      String Betreff = "Beim Schreiben des Banners ist ein Fehler aufgetreten";
      String Nachricht = "<strong>Beim Schreiben des Banners ist ein Fehler aufgetreten</strong><br>"
              + "Banner URL: " + CurrentUser.getBanner() + "<br>"
              + "UserName: " + CurrentUser.getName1() + "<br>"
              + "UserID: " + CurrentUser.getIdUser() + "<br>"
              + "Registriert seit: " + datf.format(CurrentUser.getCreated()) + "<br><br>"
              + e.getMessage() + "<br>";

      saveMail(CurrentUser.getName1(), "Stefan.Klees", Betreff, Nachricht);

    }

  }

  private void RangAbzeichen() {

    if (CurrentUser != null) {

      int flugzeitFG = CurrentUser.getFlugzeitenFG() / 60;
      int flugzeit = CurrentUser.getFlighttime() / 60;

      Rang = "Second Officer";
      RangAbzeichenURL = CONF.getDomainURL() + "/images/FTW/icons/rangabzeichen/secondofficer.png";
      Lizenz = "PPL-A";

      if (flugzeit >= 100) {
        // HPA Typerating hinzufügen
        UserTyperatings typerating = facadeUser.findTypeRating(CurrentUser.getIdUser(), "HPA");

        if (typerating != null) {

        } else {
          System.out.println("de.klees.beans.system.loginMB.RangAbzeichen() Typerating HPA erhalten");
          typerating = new UserTyperatings();
          typerating.setErfuellt(true);
          typerating.setMinutenGeflogen(0);
          typerating.setUserID(CurrentUser.getIdUser());
          typerating.setTypeRating("HPA");
          facadeUser.saveTyperating(typerating);
        }
      }

      if (flugzeit < 100) {
        Lizenz = "PPL-A";
      } else if (flugzeit >= 100 && flugzeit < 250) {
        Lizenz = "CPL";
      } else if (flugzeit >= 250 && flugzeit < 750) {
        Lizenz = "MPL";
      } else if (flugzeit >= 750) {
        Lizenz = "ATPL";
      }

      CurrentUser.setLizenz(Lizenz);

      facadeUser.edit(CurrentUser);

      if (flugzeitFG < 100) {
        Rang = "Second Officer";
        if (CurrentUser.getRolle().equals("Administratoren")) {
          RangAbzeichenURL = CONF.getDomainURL() + "/images/FTW/icons/rangabzeichen/secondofficer_admin.png";
        }

      } else if (flugzeitFG >= 100 && flugzeitFG < 250) {
        Rang = "First Officer";
        RangAbzeichenURL = CONF.getDomainURL() + "/images/FTW/icons/rangabzeichen/firstofficer.png";
        if (CurrentUser.getRolle().equals("Administratoren")) {
          RangAbzeichenURL = CONF.getDomainURL() + "/images/FTW/icons/rangabzeichen/firstofficer_admin.png";
        }
        if (CurrentUser.getRolle().equals("DOB")) {
          RangAbzeichenURL = CONF.getDomainURL() + "/images/FTW/icons/rangabzeichen/firstofficer_mod.png";
        }

      } else if (flugzeitFG >= 250 && flugzeitFG < 500) {
        Rang = "Senior First Officer";
        RangAbzeichenURL = CONF.getDomainURL() + "/images/FTW/icons/rangabzeichen/seniorfirstofficer.png";
        if (CurrentUser.getRolle().equals("Administratoren")) {
          RangAbzeichenURL = CONF.getDomainURL() + "/images/FTW/icons/rangabzeichen/seniorfirstofficer_admin.png";
        }
        if (CurrentUser.getRolle().equals("DOB")) {
          RangAbzeichenURL = CONF.getDomainURL() + "/images/FTW/icons/rangabzeichen/seniorfirstofficer_mod.png";
        }

      } else if (flugzeitFG >= 500 && flugzeitFG < 750) {
        Rang = "Captain";
        RangAbzeichenURL = CONF.getDomainURL() + "/images/FTW/icons/rangabzeichen/captain.png";
        if (CurrentUser.getRolle().equals("Administratoren")) {
          RangAbzeichenURL = CONF.getDomainURL() + "/images/FTW/icons/rangabzeichen/captain_admin.png";
        }
        if (CurrentUser.getRolle().equals("DOB")) {
          RangAbzeichenURL = CONF.getDomainURL() + "/images/FTW/icons/rangabzeichen/captain_mod.png";
        }

      } else if (flugzeitFG >= 750) {
        Rang = "Senior Captain";
        RangAbzeichenURL = CONF.getDomainURL() + "/images/FTW/icons/rangabzeichen/seniorcaptain.png";
        if (CurrentUser.getRolle().equals("Administratoren")) {
          RangAbzeichenURL = CONF.getDomainURL() + "/images/FTW/icons/rangabzeichen/seniorcaptain_admin.png";
        }
        if (CurrentUser.getRolle().equals("DOB")) {
          RangAbzeichenURL = CONF.getDomainURL() + "/images/FTW/icons/rangabzeichen/seniorcaptain_mod.png";
        }
      }
    }
  }

  public String willBack() {
    return "/users/index.xhtml?faces-redirect=true";
  }

  private void LastLoging() {
    CurrentUser.setLastlogon(c.getTime());
    facadeUser.edit(CurrentUser);

  }

  @SuppressWarnings("unchecked")
  public List<HttpSession> getSessions() {
    List<HttpSession> sessionsList = new ArrayList<>(SessionCounterListener.sessions);
    List<HttpSession> tempList = new ArrayList<>();

    long aktZeit = new Date().getTime();

    for (int i = 0; i < sessionsList.size(); i++) {
      try {
        if (!sessionsList.get(i).getAttribute("UserName").equals("")) {
          // inaktive Sessions nicht mehr anzeigen, nach Aktivität wird wieder angezeigt.
          if ((aktZeit - sessionsList.get(i).getLastAccessedTime()) < 300000) {
            tempList.add(sessionsList.get(i));
          }
        }
        //Collections.sort((List)tempList);
      } catch (NullPointerException | IllegalStateException e) {
        //System.out.println("de.klees.beans.system.loginMB.getSessions() Zeile 284 " + e.getMessage() );
      }
    }

    return tempList;

  }

  public void onSessionText(String username) {

    for (HttpSession skt : getSessions()) {

      if (skt.getAttribute("UserName").equals(username)) {
        System.out.println("de.klees.beans.system.loginMB.SessionText() " + skt.getAttribute("UserName") + " " + skt.getId());
        skt.invalidate();
      }

    }

  }

  public void onDiscord() {
    if (isDiscord()) {
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Discord-ON", "false");
      setDiscord(false);
    } else {
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Discord-ON", "true");
      setDiscord(true);
    }

  }

  public void onSummenFensterAnzeigen() {
    summenFensterAnzeigen = true;
  }

  public void onSummenFensterVerbergen() {
    summenFensterAnzeigen = false;
  }

  /*
  ************************************************************************* Setter and Getter
   */
  public Benutzer getUserdaten() {
    CurrentUser = facadeUser.findUser(username);

    if (CurrentUser != null) {

      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("UserID", CurrentUser.getIdUser());
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Sound", CurrentUser.getSound());
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("UserName", CurrentUser.getName1());

      if (CurrentUser.getDisplayname().equals("")) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("DisplayName", CurrentUser.getName1());
      } else {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("DisplayName", CurrentUser.getDisplayname());
      }

      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("DisplayName", CurrentUser.getDisplayname());

      if (CurrentUser.getDisplayname().trim().equals("")) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("DisplayName", CurrentUser.getName1());
      }

      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("BankKonto", CurrentUser.getBankKonto());
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("UserRolle", CurrentUser.getRolle());
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("UserFunktion", CurrentUser.getFunktion());
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("FluggesellschaftID", 0);
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ManagerID", CurrentUser.getFluggesellschaftManagerID());
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ZeitZone", getTimeZone());

      setAllowBenutzerEdit(CurrentUser.getAllowBenutzerEdit());
      setAllowFlughafenEdit(CurrentUser.getAllowFlughafenEdit());
      setAllowFlugzeugEdit(CurrentUser.getAllowFlugzeugeEdit());
      setAllowGeschichtenEdit(CurrentUser.getAllowGeschichtenEdit());
      setAllowNewsEdit(CurrentUser.getAllowNewsEdit());

      setAllowAdminOpen(CurrentUser.getAllowAdminOpen());
      setAllowToolsOpen(CurrentUser.getAllowToolsOpen());

      setAllowRettungsstationEdit(CurrentUser.getAllowRettungsstationEdit());
      setAllowAirportDispatcher(CurrentUser.getAllowAirportDispatcher());
      setAllowTestuser(CurrentUser.getAllowTestUser());

      setIstGesperrt(CurrentUser.getGesperrt());

      setSummenFensterAnzeigen(CurrentUser.getSummenFensterAnzeigen());
      setTabellenzeilen(CurrentUser.getTabellenzeilen());

    }
    return CurrentUser;
  }

  /*
  **************** Getter and Setter
   */
  public double getBankSaldo() {
    return facadeUser.BankSaldo((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("BankKonto"));
  }

  public boolean getSystemMeldung() {
    if (facadeUser.getSystemMeldung().getAktiv()) {
      setSystemmeldungText(facadeUser.getSystemMeldung().getMeldung());
      return true;
    } else {
      return false;
    }
  }

  public boolean getHeraldMeldung() {
    if (facadeUser.getSystemMeldung().getHeraldaktiv()) {
      return true;
    } else {
      return false;
    }

  }

  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * @param password the password to set
   */
  public void setPassword(String password) {
    this.password = password;
  }

  public Boolean getDemoUser() {
    return DemoUser;
  }

  public void setDemoUser(Boolean DemoUser) {
    this.DemoUser = DemoUser;
  }

  public String getTheme() {
    return Theme;
  }

  public void setTheme(String Theme) {
//    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("UserName", CurrentUser.getName());
//    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("UserID", CurrentUser.getIdUser());
    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Theme", Theme);
    this.Theme = Theme;
  }

  public Benutzer getCurrentUser() {
    return CurrentUser;
  }

  public void setCurrentUser(Benutzer CurrentUser) {
    this.CurrentUser = CurrentUser;
  }

  public Boolean getAdminuser() {
    return adminuser;
  }

  public void setAdminuser(Boolean adminuser) {
    this.adminuser = adminuser;
  }

  public Map<String, String> getSprache() {
    return Sprache;
  }

  public void setSprache(Map<String, String> Sprache) {
    this.Sprache = Sprache;
  }

  public String getRolle() {
    return Rolle;
  }

  public void setRolle(String Rolle) {
    this.Rolle = Rolle;
  }

  public boolean isIsTippOn() {
    return isTippOn;
  }

  public void setIsTippOn(boolean isTippOn) {
    this.isTippOn = isTippOn;
  }

  public boolean isAllowFlugzeugEdit() {
    return allowFlugzeugEdit;
  }

  public void setAllowFlugzeugEdit(boolean allowFlugzeugEdit) {
    this.allowFlugzeugEdit = allowFlugzeugEdit;
  }

  public boolean isAllowBenutzerEdit() {
    return allowBenutzerEdit;
  }

  public void setAllowBenutzerEdit(boolean allowBenutzerEdit) {
    this.allowBenutzerEdit = allowBenutzerEdit;
  }

  public boolean isAllowFlughafenEdit() {
    return allowFlughafenEdit;
  }

  public void setAllowFlughafenEdit(boolean allowFlughafenEdit) {
    this.allowFlughafenEdit = allowFlughafenEdit;
  }

  public boolean isAllowGeschichtenEdit() {
    return allowGeschichtenEdit;
  }

  public void setAllowGeschichtenEdit(boolean allowGeschichtenEdit) {
    this.allowGeschichtenEdit = allowGeschichtenEdit;
  }

  public boolean isAllowToolsOpen() {
    return allowToolsOpen;
  }

  public void setAllowToolsOpen(boolean allowToolsOpen) {
    this.allowToolsOpen = allowToolsOpen;
  }

  public boolean isAllowAdminOpen() {
    return allowAdminOpen;
  }

  public void setAllowAdminOpen(boolean allowAdminOpen) {
    this.allowAdminOpen = allowAdminOpen;
  }

  public boolean isAllowNewsEdit() {
    return allowNewsEdit;
  }

  public void setAllowNewsEdit(boolean allowNewsEdit) {
    this.allowNewsEdit = allowNewsEdit;
  }

  public boolean isIsDlgLoggedUserOn() {
    return isDlgLoggedUserOn;
  }

  public void setIsDlgLoggedUserOn(boolean isDlgLoggedUserOn) {
    this.isDlgLoggedUserOn = isDlgLoggedUserOn;
  }

  public String getSystemmeldungText() {
    return facadeUser.getSystemMeldung().getMeldung();
  }

  public void setSystemmeldungText(String systemmeldungText) {
    this.systemmeldungText = systemmeldungText;
  }

  public String getFtwVersion() {
    return ftwVersion;
  }

  public String getTimeZone() {
    if (CurrentUser != null) {
      return CurrentUser.getZeitZone();
    } else {
      logout();
    }
    return "Europe/Berlin";
  }

  public boolean isIstGesperrt() {
    return istGesperrt;
  }

  public void setIstGesperrt(boolean istGesperrt) {
    this.istGesperrt = istGesperrt;
  }

  public Boolean getModuser() {
    return moduser;
  }

  public void setModuser(Boolean moduser) {
    this.moduser = moduser;
  }

  public boolean isArrivalsOn() {
    return ArrivalsOn;
  }

  public void setArrivalsOn(boolean ArrivalsOn) {
    this.ArrivalsOn = ArrivalsOn;
  }

  public boolean isDeparturesOn() {
    return DeparturesOn;
  }

  public void setDeparturesOn(boolean DeparturesOn) {
    this.DeparturesOn = DeparturesOn;
  }

  public boolean isNewMenue() {
    return newMenue;
  }

  public void setNewMenue(boolean newMenue) {
    this.newMenue = newMenue;
  }

  public int getIconSize() {
    return iconSize;
  }

  public void setIconSize(int iconSize) {
    this.iconSize = iconSize;
  }

  public String getRang() {
    return Rang;
  }

  public void setRang(String Rang) {
    this.Rang = Rang;
  }

  public String getRangAbzeichenURL() {
    return RangAbzeichenURL;
  }

  public void setRangAbzeichenURL(String RangAbzeichenURL) {
    this.RangAbzeichenURL = RangAbzeichenURL;
  }

  public String getLizenz() {
    return Lizenz;
  }

  public void setLizenz(String Lizenz) {
    this.Lizenz = Lizenz;
  }

  public boolean isEingeloggt() {
    return eingeloggt;
  }

  public void setEingeloggt(boolean eingeloggt) {
    this.eingeloggt = eingeloggt;
  }

  public boolean isDiscord() {
    return Discord;
  }

  public void setDiscord(boolean Discord) {
    this.Discord = Discord;
  }

  public boolean isWartungsTabelleAnzeigen() {
    return wartungsTabelleAnzeigen;
  }

  public void setWartungsTabelleAnzeigen(boolean wartungsTabelleAnzeigen) {
    this.wartungsTabelleAnzeigen = wartungsTabelleAnzeigen;
  }

  public boolean isAllowRettungsstationEdit() {
    return allowRettungsstationEdit;
  }

  public void setAllowRettungsstationEdit(boolean allowRettungsstationEdit) {
    this.allowRettungsstationEdit = allowRettungsstationEdit;
  }

  public boolean isAllowAirportDispatcher() {
    return allowAirportDispatcher;
  }

  public void setAllowAirportDispatcher(boolean allowAirportDispatcher) {
    this.allowAirportDispatcher = allowAirportDispatcher;
  }

  public boolean isAllowTestuser() {
    return allowTestuser;
  }

  public void setAllowTestuser(boolean allowTestuser) {
    this.allowTestuser = allowTestuser;
  }

  public static boolean isResBundleLoaded() {
    return resBundleLoaded;
  }

  public static void setResBundleLoaded(boolean resBundleLoaded) {
    loginMB.resBundleLoaded = resBundleLoaded;
  }

  public boolean isSummenFensterAnzeigen() {
    return summenFensterAnzeigen;
  }

  public void setSummenFensterAnzeigen(boolean summenFensterAnzeigen) {
    this.summenFensterAnzeigen = summenFensterAnzeigen;
  }

  public String getMapLayer() {
    return mapLayer;
  }

  public void setMapLayer(String mapLayer) {
    this.mapLayer = mapLayer;
  }

  public int getTabellenzeilen() {
    return tabellenzeilen;
  }

  public void setTabellenzeilen(int tabellenzeilen) {
    this.tabellenzeilen = tabellenzeilen;
  }

  public String getHeraldUrl() {
    return facadeUser.getSystemMeldung().getHeraldurl();
  }

  public void setHeraldUrl(String HeraldUrl) {
    this.HeraldUrl = HeraldUrl;
  }

  private Benutzer getUserDaten(String name) {
    return facadeUser.findUser(name);
  }

  public void saveMail(String Von, String An, String Betreff, String Nachricht) {

    //Absendermail speichern
    Benutzer absender = getUserDaten(Von);
    Benutzer empfaenger = getUserDaten(An);

    Mail nm = new Mail();

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
    facadeUser.saveUserMail(nm);

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
      facadeUser.saveUserMail(nm);
    }

  }

  public boolean isSupportUser() {
    return supportUser;
  }

  public void setSupportUser(boolean supportUser) {
    this.supportUser = supportUser;
  }

}
