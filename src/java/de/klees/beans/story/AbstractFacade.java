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

package de.klees.beans.story;

import de.klees.data.Airport;
import de.klees.data.Story;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Stefan Klees
 */
public abstract class AbstractFacade<T> {

  private Class<T> entityClass;

  public AbstractFacade(Class<T> entityClass) {
    this.entityClass = entityClass;
  }

  protected abstract EntityManager getEntityManager();

  public void create(T entity) {
    getEntityManager().persist(entity);
  }

  public void edit(T entity) {
    getEntityManager().merge(entity);
  }

  public void remove(T entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  @SuppressWarnings("unchecked")
  public T find(Object id) {
    return getEntityManager().find(entityClass, id);
  }

  public List<Story> findAll() {
    return getEntityManager().createQuery("SELECT s FROM Story s ORDER BY s.bezeichnung, s.sprache", Story.class).getResultList();
  }

  public List<Story> findAllFiltered(String Bezeichnung) {
    Bezeichnung = "%" + Bezeichnung + "%";
    return getEntityManager().createQuery("SELECT s FROM Story s WHERE s.bezeichnung like :bezeichnung ORDER BY s.sprache", Story.class)
            .setParameter("bezeichnung", Bezeichnung)
            .getResultList();
  }

  public List<Story> findAlldistinct() {
    return getEntityManager().createQuery("SELECT s FROM Story s GROUP BY s.bezeichnung", Story.class).getResultList();
  }

  public List<Airport> getBundesLaender(String laender) {
    return getEntityManager().createQuery("SELECT a FROM Airport a where a.land = :land GROUP BY a.bundesland", Airport.class)
            .setParameter("land", laender)
            .getResultList();

  }

}
