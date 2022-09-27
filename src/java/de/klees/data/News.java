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

package de.klees.data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Stefan Klees
 */
@Entity
@Table(name = "news")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "News.findAll", query = "SELECT n FROM News n order by n.datum DESC"),
  @NamedQuery(name = "News.findByIdnews", query = "SELECT n FROM News n WHERE n.idnews = :idnews"),
  @NamedQuery(name = "News.findByDatum", query = "SELECT n FROM News n WHERE n.datum = :datum"),
  @NamedQuery(name = "News.findByVerfasser", query = "SELECT n FROM News n WHERE n.verfasser = :verfasser")})
public class News implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "idnews")
  private Integer idnews;
  @Column(name = "datum")
  @Temporal(TemporalType.TIMESTAMP)
  private Date datum;
  @Size(max = 80)
  @Column(name = "verfasser")
  private String verfasser;
  @Lob
  @Size(max = 2147483647)
  @Column(name = "text")
  private String text;

  public News() {
  }

  public News(Integer idnews) {
    this.idnews = idnews;
  }

  public Integer getIdnews() {
    return idnews;
  }

  public void setIdnews(Integer idnews) {
    this.idnews = idnews;
  }

  public Date getDatum() {
    return datum;
  }

  public void setDatum(Date datum) {
    this.datum = datum;
  }

  public String getVerfasser() {
    return verfasser;
  }

  public void setVerfasser(String verfasser) {
    this.verfasser = verfasser;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (idnews != null ? idnews.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof News)) {
      return false;
    }
    News other = (News) object;
    if ((this.idnews == null && other.idnews != null) || (this.idnews != null && !this.idnews.equals(other.idnews))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "de.klees.data.News[ idnews=" + idnews + " ]";
  }
  
}
