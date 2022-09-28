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

package de.klees.beans.news;

import de.klees.beans.system.CONF;
import de.klees.beans.system.loginMB;
import de.klees.data.News;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
public class newsBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<News> newsList;
  private News currentNews;
  private boolean isLoaded;
  private boolean isNewNews;

  private String newsText;
  private Date newsDate;
  private String newsVerfasser;
  private String Pdf;

  @EJB
  NewsFacade newsFacade;

  /**
   * Creates a new instance of newsBean
   */
  public newsBean() {
    isLoaded = false;
  }

  public List<News> getNewsList() {

    if (!isLoaded) {
      newsList = newsFacade.findAll();
      isLoaded = true;
    }

    return newsList;
  }

 
  
  public void onCreate() {
    News newNews = new News();
    newNews.setDatum(newsDate);
    newNews.setText(newsText);
    newNews.setVerfasser(newsVerfasser);
    newsFacade.create(newNews);
    NewMessage(loginMB.onSprache("newsSaved"));
    isLoaded = false;

  }

  public void onEdit() {
    newsFacade.edit(currentNews);
    NewMessage(loginMB.onSprache("newsSaved"));
    isLoaded = false;
  }

  public void onDelete() {
    newsFacade.remove(currentNews);
    NewMessage(loginMB.onSprache("recorddeleted"));
    isLoaded = false;
  }

  public void onRowSelect(SelectEvent event) {
  }

  public void onRowUnselect(UnselectEvent event) {
  }

  private void NewMessage(String neueNachricht) {
    FacesMessage msg = new FacesMessage(neueNachricht);
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }
  @SuppressWarnings("unchecked")
  public Collection<File> getPdfs() {
    Collection<File> dateien = new ArrayList<>();
    scannFiles(new File(CONF.getLocalWWWDir() + "/images/FTW/herald/."), dateien);
    Collections.sort((List) dateien);
    return dateien;
}
  
  //Directory Scann
  
  private void scannFiles(File file, Collection<File> scanned) {
    File[] datei = file.listFiles();
    if (datei != null) {
        for (File dateiname : datei) {
            scanned.add(dateiname);
            scannFiles(dateiname, scanned);
        }
    }
}
  
  
  /*
  Setter and Getter
   */
  public News getSelectedNews() {
    return currentNews;
  }

  public void setSelectedNews(News SelectedNews) {
    this.currentNews = SelectedNews;
  }

  public boolean isIsLoaded() {
    return isLoaded;
  }

  public void setIsLoaded(boolean isLoaded) {
    this.isLoaded = isLoaded;
  }

  public String getNewsText() {
    return newsText;
  }

  public void setNewsText(String newsText) {
    this.newsText = newsText;
  }

  public Date getNewsDate() {
    return newsDate;
  }

  public void setNewsDate(Date newsDate) {
    this.newsDate = newsDate;
  }

  public String getNewsVerfasser() {
    return newsVerfasser;
  }

  public void setNewsVerfasser(String newsVerfasser) {
    this.newsVerfasser = newsVerfasser;
  }

  public boolean isIsNewNews() {
    return isNewNews;
  }

  public String getPdf() {
    return Pdf;
  }

  public void setPdf(String Pdf) {
    this.Pdf = Pdf;
  }

  
  
  
}
