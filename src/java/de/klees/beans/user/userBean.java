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

package de.klees.beans.user;

import de.klees.beans.system.CONF;
import de.klees.beans.system.loginMB;
import de.klees.data.Bank;
import de.klees.beans.system.ReportList;
import de.klees.data.FboUserObjekte;
import de.klees.data.Fluggesellschaft;
import de.klees.data.Benutzer;
import de.klees.data.UserFavoriten;
import de.klees.data.UserTyperatings;
import de.klees.data.Yaacarskopf;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Stefan Klees
 */
public class userBean implements Serializable {

  private static final long serialVersionUID = 1L;
  private Benutzer currentUser;
  private UserFavoriten userFavorit;
  private UserTyperatings selectedUserTypeRating;
  private String passwort;
  private String passwortKey;
  private String Theme;

  private List<Benutzer> UserList;
  private List<Benutzer> SelectedUsers;
  private final Calendar c = Calendar.getInstance();
  private String suchText;
  private final Map<String, String> Rollen;
  private boolean demoUser;
  private String UserNameNeu;
  private String UserEmailNeu;
  private String RangAbzeichenURL;
  private String Rang;
  private String frmTypeRating;
  private int frmTyperatingStunden;
  private boolean frmTypeRatingErfuellt;
  private String UserLanguage;
  private String ManagerInFG;
  private String neuesPasswort;

  private UploadedFile BannerFile;

  boolean isLoaded;
  boolean showUserEdit;

  private ArrayList<ReportList> report = new ArrayList<>();
  private final SimpleDateFormat ZeitFormatVoll = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @EJB
  private UserFacade userFacade;

  public userBean() {
    isLoaded = false;

    Rollen = new HashMap<>();
    Rollen.put("User", "Users");
    Rollen.put("Member of Development", "DOB");
    Rollen.put("Administrator", "Administratoren");
    suchText = "%";
    UserNameNeu = "";
    neuesPasswort = "";

    showUserEdit = false;

    setTheme((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Theme"));
  }

  private void ReportSchreiben(String text) {
    ReportList rep = new ReportList();
    rep.setZeit(new Date());
    rep.setMeldung(text);
    report.add(rep);
  }

  private void ReportAusgeben() {
    String ausgabe = "---------------------------------- FTW Reporting User BEGINN --------------------------------------------------------------" + "\n";
    for (ReportList lst : report) {
      ausgabe = ausgabe + ZeitFormatVoll.format(lst.getZeit()) + " - " + lst.getMeldung() + "\n";
    }
    ausgabe = ausgabe + "------------------------------- FTW Reporting User ENDE -----------------------------------------------------------------" + "\n";
    System.out.println(ausgabe);
    report.clear();
  }

  public void userDatenUndRangabzeichen() {

    int UserID = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID");
    currentUser = userFacade.findUserById(UserID);

    int flugzeit = currentUser.getFlugzeitenFG() / 60;

    if (flugzeit < 100) {
      Rang = "Second Officer";
      RangAbzeichenURL = CONF.getDomainURL()+ "/images/FTW/icons/rangabzeichen/secondofficer.png";

      if (currentUser.getRolle().equals("Administratoren")) {
        RangAbzeichenURL = CONF.getDomainURL()+ "/images/FTW/icons/rangabzeichen/secondofficer_admin.png";
      }

    } else if (flugzeit >= 100 && flugzeit < 250) {
      Rang = "First Officer";
      RangAbzeichenURL = CONF.getDomainURL()+ "/images/FTW/icons/rangabzeichen/firstofficer.png";

      if (currentUser.getRolle().equals("Administratoren")) {
        RangAbzeichenURL = CONF.getDomainURL()+ "/images/FTW/icons/rangabzeichen/firstofficer_admin.png";
      }

    } else if (flugzeit >= 250 && flugzeit < 500) {
      Rang = "Senior First Officer";
      RangAbzeichenURL = CONF.getDomainURL()+ "/images/FTW/icons/rangabzeichen/seniorfirstofficer.png";

      if (currentUser.getRolle().equals("Administratoren")) {
        RangAbzeichenURL = CONF.getDomainURL()+ "/images/FTW/icons/rangabzeichen/seniorfirstofficer_admin.png";
      }
      if (currentUser.getRolle().equals("DOB")) {
        RangAbzeichenURL = CONF.getDomainURL()+ "/images/FTW/icons/rangabzeichen/seniorfirstofficer_mod.png";
      }

    } else if (flugzeit >= 500 && flugzeit < 750) {
      Rang = "Captain";
      RangAbzeichenURL = CONF.getDomainURL()+ "/images/FTW/icons/rangabzeichen/captain.png";

      if (currentUser.getRolle().equals("Administratoren")) {
        RangAbzeichenURL = CONF.getDomainURL()+ "/images/FTW/icons/rangabzeichen/captain_admin.png";
      }
      if (currentUser.getRolle().equals("DOB")) {
        RangAbzeichenURL = CONF.getDomainURL()+ "/images/FTW/icons/rangabzeichen/captain_mod.png";
      }

    } else if (flugzeit >= 750) {
      Rang = "Senior Captain";
      RangAbzeichenURL = CONF.getDomainURL()+ "/images/FTW/icons/rangabzeichen/seniorcaptain.png";

      if (currentUser.getRolle().equals("Administratoren")) {
        RangAbzeichenURL = CONF.getDomainURL()+ "/images/FTW/icons/rangabzeichen/seniorcaptain_admin.png";
      }
      if (currentUser.getRolle().equals("DOB")) {
        RangAbzeichenURL = CONF.getDomainURL()+ "/images/FTW/icons/rangabzeichen/seniorcaptain_mod.png";
      }

    }

  }

  public List<Benutzer> getUser() {
    if (!isLoaded) {
      UserList = userFacade.findAll(suchText);
      isLoaded = true;
    }
    return UserList;
  }

  public List<UserTyperatings> getTypeRatings() {
    if (currentUser != null) {
      return userFacade.findAllTypratingByUserID(currentUser.getIdUser());
    }
    return null;
  }

  public List<UserTyperatings> getUserTypeRatings() {
    return userFacade.findAllTypratingByUserID((int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID"));
  }

  public List<UserFavoriten> getUserFavoriten() {
    return userFacade.UserFavoriten((int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserID"));
  }

  public void onDeleteFavorit() {
    userFacade.removeUserFavorit(userFavorit);
  }

  public void onEditUser() {
    if (SelectedUsers.size() > 0) {
      if (SelectedUsers.size() == 1) {
        currentUser = SelectedUsers.get(0);
        setSelectedUser(currentUser);
        showUserEdit = true;
      } else {
        currentUser = null;
        setSelectedUser(currentUser);
        showUserEdit = false;
      }
    }
  }

  public void saveUserSettings() {
//    String theme = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("Theme");
//    String Rolle = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserRolle");

    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Sound", currentUser.getSound());
    currentUser.setTheme(getTheme());

    String ClearText = currentUser.getDisplayname().replaceAll("script", "");
    ClearText = ClearText.replaceAll("css", "");
    ClearText = ClearText.replaceAll("stylesheet", "");
    ClearText = ClearText.replaceAll("style", "");
    ClearText = ClearText.replaceAll("refresh", "");
    ClearText = ClearText.replaceAll("<meta", "");
    ClearText = ClearText.replaceAll("iframe", "");
    ClearText = ClearText.replaceAll("<div>", "");
    ClearText = ClearText.replaceAll("</div>", "");

    currentUser.setDisplayname(ClearText);

    currentUser.setSprache(FacesContext.getCurrentInstance().getViewRoot().getLocale().toString());

    userFacade.edit(currentUser);

    if (currentUser.getDisplayname().trim().equals("")) {
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("DisplayName", currentUser.getName1());
    } else {
      FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("DisplayName", currentUser.getDisplayname());
    }

    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Sprache", currentUser.getSprache());
    loginMB.setResBundleLoaded(false);

    NewMessage(loginMB.onSprache("Benutzer_Einstellungen_msg_einstellungenGespeichert") + " : " + currentUser.getName1());

  }

  public void saveUser() {

    if (currentUser != null) {
      if (currentUser.getBanner().equals("")) {
        currentUser.setBanner(CONF.getDomainURL()+ "/images/FTW/banner.png");
      }
      userFacade.edit(currentUser);
      NewMessage("FTW - User gespeichert    " + currentUser.getName1());
      isLoaded = false;
    }

  }

  public void onDeletUsers() {
    for (Benutzer sel : SelectedUsers) {
      
      currentUser = sel;
      setSelectedUser(currentUser);

      deletUser();
      
      
    }
    
    
    
  }

  public void deletUser() {

    String Rolle = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("UserRolle");

    int deleteID = currentUser.getIdUser();
    int Zeilen = 0;

    if (!Rolle.equals("DOB")) {
      NewMessage("Du hast keine Berechtigung zum Löschen von Usern");
    } else {
      Zeilen = userFacade.BankKontoLoeschen(currentUser.getBankKonto());
      ReportSchreiben("Es wurden " + Zeilen + " Bankbuchungen gelöscht");

      // Fluggesellschaften auslesen
      List<Fluggesellschaft> userFGs = userFacade.getFluggesellschaften(deleteID);

      for (Fluggesellschaft fgs : userFGs) {
        //Piloten löschen
        Zeilen = userFacade.FGPilotenLoeschen(fgs.getIdFluggesellschaft());
        ReportSchreiben("Es wurden " + Zeilen + " Piloten der Fluggesellschaft gelöscht");
        //Zugeordnete Piloten von Flugzeugen löschen
        Zeilen = userFacade.ZugeordnetePilotenAusFlugzeugenLoeschen(fgs.getIdFluggesellschaft());
        ReportSchreiben("Es wurden " + Zeilen + " Piloten aus den Flugzeugen gelöscht");
        //Manager entlassen
        Zeilen = userFacade.ManagerEntlassen(fgs.getIdFluggesellschaft());
        ReportSchreiben("Es wurden " + Zeilen + " Manager entlassen");
      }

      //Fluggesellschaften löschen
      Zeilen = userFacade.FluggesellschaftLoeschen(deleteID);
      ReportSchreiben("Es wurden " + Zeilen + " Fluggesellschaften gelöscht");

      //FBO-Objekte auslesen
      List<FboUserObjekte> userFBOs = userFacade.getUserFBOs(deleteID);

      for (FboUserObjekte fbos : userFBOs) {
        //Routenobjekte löschen
        Zeilen = userFacade.RoutenLoeschen(fbos.getIduserfbo());
        ReportSchreiben("Es wurden " + Zeilen + " Routen gelöscht");
      }

      //FBO's löschen
      Zeilen = userFacade.fboLoeschen(deleteID);
      ReportSchreiben("Es wurden " + Zeilen + " FBO Objekte gelöscht");

      //Fluglogbuch anpassen (-300)
      Zeilen = userFacade.FluglogbuchAnpassen(deleteID);
      ReportSchreiben("Es wurden " + Zeilen + " Logbucheinträge geändert");

      //Typeratings löschen
      Zeilen = userFacade.TypeRatingsLoeschen(deleteID);
      ReportSchreiben("Es wurden " + Zeilen + " Typeratings gelöscht");

      //Flugzeuge zurueck ans System
      Zeilen = userFacade.FlugzeugeZurueckAnSystem(deleteID);
      ReportSchreiben("Es wurden " + Zeilen + " Flugzeuge ans System übertragen");

      //Mails Löschen
      Zeilen = userFacade.mailsLoeschen(deleteID);
      ReportSchreiben("Es wurden " + Zeilen + " Mails gelöscht");

      //User entgültig löschen
      Zeilen = userFacade.deleteUser(deleteID);
      ReportSchreiben(Zeilen + " FTW User mit ID: " + deleteID + " wurde gelöscht");

      ReportAusgeben();

      currentUser = null;
    }

    isLoaded = false;
  }

  public void onManagerKuendigen() {
    if (userFacade.onManagerKuendigen(currentUser.getFluggesellschaftManagerID(), currentUser.getIdUser()) > 0) {

      currentUser.setFluggesellschaftManagerID(-1);
      userFacade.edit(currentUser);

      NewMessage(loginMB.onSprache("Benutzer_Einstellungen_msg_ManagerKuendigen"));
    }

  }

  public void addUser() {

    if (!userFacade.ifExistUser(UserNameNeu)) {

      Benutzer newUser = new Benutzer();
      //newUser.setIdUser(0);
      newUser.setIsActive(true);
      newUser.setGesperrt(false);
      newUser.setOnline(false);
      newUser.setBanlists("");
      newUser.setBanner(CONF.getDomainURL()+ "/images/FTW/banner.png");
      newUser.setCreated(c.getTime());
      newUser.setDateformat("dd.MM.yyyy");
      newUser.setEmail(UserEmailNeu);
      newUser.setLastlogon(c.getTime());
      newUser.setLizenz("PPL-A");
      newUser.setName1(getUserNameNeu());
      newUser.setPasswort("");
      newUser.setFunktion("");
      newUser.setIconSize(32);
      newUser.setStandort("");
      newUser.setZeitZone("Europe/Berlin");
      newUser.setSprache(UserLanguage);
      newUser.setRangabzeichen(CONF.getDomainURL()+ "/images/FTW/icons/rangabzeichen/secondofficer.png");
      newUser.setFlightcargo(0);
      newUser.setFlightmiles(0);
      newUser.setFlightpaxes(0);
      newUser.setFlights(0);
      newUser.setFlighttime(0);
      newUser.setFlightrettung(0);
      newUser.setFluggesellschaftManagerID(-1);
      newUser.setFlugzeitenFG(0);
      newUser.setRolle("Users");
      newUser.setTheme("hot-sneaks");
      newUser.setUrl("");
      newUser.setAllowAdminOpen(false);
      newUser.setAllowBenutzerEdit(false);
      newUser.setAllowFlughafenEdit(false);
      newUser.setAllowFlugzeugeEdit(false);
      newUser.setAllowGeschichtenEdit(false);
      newUser.setAllowNewsEdit(false);
      newUser.setAllowToolsOpen(false);
      newUser.setAllowAirportDispatcher(false);
      newUser.setAllowRettungsstationEdit(false);
      newUser.setAllowTestUser(false);
      newUser.setDisplayname(UserNameNeu);
      newUser.setSound(false);
      newUser.setSummenFensterAnzeigen(true);
      newUser.setMapLayer("OpenStreetMap.Mapnik");
      newUser.setTabellenzeilen(15);

      int anzahl = 1000000;
      int Nummer;

      Nummer = (int) (Math.random() * anzahl) + (anzahl * 2);

      neuesPasswort = UserNameNeu + String.valueOf(Nummer);

      try {
        newUser.setPasswort(de.klees.beans.system.SHA_CRYPT.SHA512Crypt(getNeuesPasswort()));
      } catch (NoSuchAlgorithmException e) {
        NewMessage("Passwort konnte nicht gesetzt werden");
      }

      String Key = "";

      do {
        Key = UUID.randomUUID().toString();
      } while (userFacade.ifExistReadKey(Key));

      newUser.setReadaccesskey(Key);

      do {
        Key = UUID.randomUUID().toString();
      } while (userFacade.ifExistWriteKey(Key));

      newUser.setWriteaccesskey(Key);

      userFacade.create(newUser);
      setSelectedUser(newUser);

      NewMessage("Neuen Benutzer angelegt");

      // Typeratings eintragen
      UserTyperatings newType = new UserTyperatings();

      newType.setErfuellt(true);
      newType.setMinutenGeflogen(60);
      newType.setTypeRating("SPA");
      newType.setUserID(this.currentUser.getIdUser());

      userFacade.createTyperating(newType);

      NewMessage("Typerating SPA angelegt");

      createBankkonto();

      suchText = "%" + getUserNameNeu() + "%";
      isLoaded = false;

    } else {
      NewMessage("Username ist schon vergeben");
    }
  }

  //************* Bankkonto erstellen
  public void createBankkonto() {

    String Kontonummer = getNeueKontoNummer();

    Bank newBankkonto = new Bank();
    newBankkonto.setBankKonto(Kontonummer);
    newBankkonto.setKontoName(this.currentUser.getName1());
    newBankkonto.setAbsenderKonto("500-1000001");
    newBankkonto.setAbsenderName("**** FTW BANK *****");
    newBankkonto.setEmpfaengerName(currentUser.getName1());
    newBankkonto.setEmpfaengerKonto(Kontonummer);
    newBankkonto.setVerwendungsZweck("Kontoeröffnung");
    newBankkonto.setUeberweisungsDatum(new Date());
    newBankkonto.setAusfuehrungsDatum(new Date());
    newBankkonto.setBetrag(5000.00);

    //************ ID's Beginn
    newBankkonto.setUserID(currentUser.getIdUser());
    newBankkonto.setAirportID(-1);
    newBankkonto.setFluggesellschaftID(-1);
    newBankkonto.setFlugzeugBesitzerID(-1);
    newBankkonto.setIndustrieID(-1);
    newBankkonto.setLeasinggesellschaftID(-1);
    newBankkonto.setTransportID(-1);
    newBankkonto.setIcao("");
    newBankkonto.setObjektID(-1);
    newBankkonto.setKostenstelle(-1);
    newBankkonto.setFlugzeugID(-1);
    newBankkonto.setPilotID(-1);

    //************ ID's Ende
    userFacade.createBankkonto(newBankkonto);

    NewMessage("Bankkonto wurde angelegt");

    currentUser.setBankKonto(Kontonummer);
    userFacade.edit(currentUser);

    NewMessage("Bankkonto wurde zugewiesen");

  }

  public void passwortChange() {
    try {
      passwortKey = de.klees.beans.system.SHA_CRYPT.SHA512Crypt(getPasswort());
      currentUser.setPasswort(passwortKey);
      userFacade.edit(currentUser);
      NewMessage("gespeichert    " + currentUser.getName1());

      //YAACARS Kopf, neuen Hash eintragen.
      Yaacarskopf yaacarsFlight = userFacade.getYAACARSFlight(currentUser.getIdUser());
      if (yaacarsFlight != null) {
        yaacarsFlight.setUserhash(currentUser.getPasswort());
        userFacade.saveYAACARSKopf(yaacarsFlight);
      }

    } catch (NoSuchAlgorithmException ex) {
      NewMessage("Passwort konnte nicht gespeichert werden!");
    }

  }

  public void onRowSelect(SelectEvent event) {

    if (SelectedUsers.size() > 0) {
      currentUser = SelectedUsers.get(0);
      setSelectedUser(currentUser);

    }

    showUserEdit = false;
  }

  public void onRowUnselect(UnselectEvent event) {
  }

  private void NewMessage(String neueNachricht) {
    FacesMessage msg = new FacesMessage(neueNachricht);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  /*
  ************* Typerating Beginn
   */
  public void addTyperating() {
    UserTyperatings newType = new UserTyperatings();
    newType.setUserID(currentUser.getIdUser());
    newType.setTypeRating(frmTypeRating);
    newType.setErfuellt(frmTypeRatingErfuellt);
    newType.setMinutenGeflogen(frmTyperatingStunden * 60);

    userFacade.createTyperating(newType);

    NewMessage("Typrating wurde angelegt");
  }

  public void onDeleteType() {
    userFacade.removeTyperating(selectedUserTypeRating);
    NewMessage("Typrating wurde gelöscht");
  }

  /*
  ************* Typerating Ende
   */
 /*
  *********** Grafikupload BEGINN
   */
  public UploadedFile getBannerFile() {
    return BannerFile;
  }

  public void setBannerFile(UploadedFile BannerFile) {
    this.BannerFile = BannerFile;
  }

  public void onBannerUpload(FileUploadEvent event) {
    BannerFile = event.getFile();
    if (BannerFile != null) {
      try {
        BufferedImage image;
        image = ImageIO.read(event.getFile().getInputstream());
        String Datei = CONF.getLocalWWWDir() +  "/images/FTW/banner/banner(" + currentUser.getName1() + ")-" + currentUser.getIdUser() + ".png";
        ImageIO.write(image, "png", new File(Datei));
        currentUser.setBanner(CONF.getDomainURL()+ "/images/FTW/banner/banner(" + currentUser.getName1() + ")-" + currentUser.getIdUser() + ".png");
        saveUser();
      } catch (IOException e) {
        System.out.println("de.klees.beans.system.loginMB.onBannerUpload() " + e.getMessage());
      }

    }

  }

  /*
  *********** Grafikupload ENDE
   */
  public void onUserLastLoginGleichCreated() {
    UserList = userFacade.getCreateAndLastloginGleich();
  }

  public void onUserLastLogin2Pruefung() {
    UserList = userFacade.getLastloginPruefung2();
  }

  public void userPasswoerterPruefen() {

    List<Benutzer> userlist = userFacade.getAllUsers();

    for (Benutzer u : userlist) {

      try {
        if (u.getPasswort().equals(de.klees.beans.system.SHA_CRYPT.SHA512Crypt(u.getName1() + "1234"))) {
          u.setGesperrt(true);
          userFacade.edit(u);
          ReportSchreiben("User: " + u.getName1() + " Anzahl der Fluege = " + u.getFlights());
        }

      } catch (NoSuchAlgorithmException e) {

      }
    }
    ReportAusgeben();
  }

  /*
  Setter and Getter
   */
  private String getNeueKontoNummer() {
    String KontoNummer = "";
    int anzahl = 1000000;
    int Nummer;

    Nummer = (int) (Math.random() * anzahl) + (anzahl * 2);
    KontoNummer = "500-" + String.valueOf(Nummer);

    //Test
    //KontoNummer="500-2004606";
    while (userFacade.ifExistBankKonto(KontoNummer)) {
      Nummer = (int) (Math.random() * anzahl) + (anzahl * 2);
      KontoNummer = "500-" + String.valueOf(Nummer);
    }
    return KontoNummer;
  }

  public Benutzer getSelectedUser() {
    return currentUser;
  }

  public void setSelectedUser(Benutzer currentUser) {
    this.currentUser = currentUser;
  }

  public String getSuchText() {
    return suchText;
  }

  public void setSuchText(String suchText) {
    this.suchText = suchText;
  }

  public String getPasswort() {
    return passwort;
  }

  public void setPasswort(String passwort) {
    this.passwort = passwort;
  }

  public Map<String, String> getRollen() {
    return Rollen;
  }

  public boolean isDemoUser() {
    return demoUser;
  }

  public void setDemoUser(boolean demoUser) {
    this.demoUser = demoUser;
  }

  public String getTheme() {
    return Theme;
  }

  public void setTheme(String Theme) {
    this.Theme = Theme;
  }

  public String getUserNameNeu() {
    return UserNameNeu;
  }

  public void setUserNameNeu(String UserNameNeu) {
    this.UserNameNeu = UserNameNeu;
  }

  public String getRangAbzeichenURL() {
    return RangAbzeichenURL;
  }

  public void setRangAbzeichenURL(String RangAbzeichenURL) {
    this.RangAbzeichenURL = RangAbzeichenURL;
  }

  public String getRang() {
    return Rang;
  }

  public void setRang(String Rang) {
    this.Rang = Rang;
  }

  public String getFrmTypeRating() {
    return frmTypeRating;
  }

  public void setFrmTypeRating(String frmTypeRating) {
    this.frmTypeRating = frmTypeRating;
  }

  public int getFrmTyperatingStunden() {
    return frmTyperatingStunden;
  }

  public void setFrmTyperatingStunden(int frmTyperatingStunden) {
    this.frmTyperatingStunden = frmTyperatingStunden;
  }

  public boolean isFrmTypeRatingErfuellt() {
    return frmTypeRatingErfuellt;
  }

  public void setFrmTypeRatingErfuellt(boolean frmTypeRatingErfuellt) {
    this.frmTypeRatingErfuellt = frmTypeRatingErfuellt;
  }

  public UserTyperatings getSelectedUserTypeRating() {
    return selectedUserTypeRating;
  }

  public void setSelectedUserTypeRating(UserTyperatings selectedUserTypeRating) {
    this.selectedUserTypeRating = selectedUserTypeRating;
  }

  public String getUserEmailNeu() {
    return UserEmailNeu;
  }

  public void setUserEmailNeu(String UserEmailNeu) {
    this.UserEmailNeu = UserEmailNeu;
  }

  public String getUserLanguage() {
    return UserLanguage;
  }

  public void setUserLanguage(String UserLanguage) {
    this.UserLanguage = UserLanguage;
  }

  public String getManagerInFG() {
    ManagerInFG = userFacade.getFluggesellschaftName(currentUser.getFluggesellschaftManagerID());
    return ManagerInFG;
  }

  public void setManagerInFG(String ManagerInFG) {
    this.ManagerInFG = ManagerInFG;
  }

  public UserFavoriten getUserFavorit() {
    return userFavorit;
  }

  public void setUserFavorit(UserFavoriten userFavorit) {
    this.userFavorit = userFavorit;
  }

  public String getNeuesPasswort() {
    return neuesPasswort;
  }

  public void setNeuesPasswort(String neuesPasswort) {
    this.neuesPasswort = neuesPasswort;
  }

  public List<Benutzer> getSelectedUsers() {
    return SelectedUsers;
  }

  public void setSelectedUsers(List<Benutzer> SelectedUsers) {
    this.SelectedUsers = SelectedUsers;
  }

  public boolean isShowUserEdit() {
    return showUserEdit;
  }

  public void setShowUserEdit(boolean showUserEdit) {
    this.showUserEdit = showUserEdit;
  }

}
