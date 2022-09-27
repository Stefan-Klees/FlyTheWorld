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
package de.klees.beans.airline.manager;

import de.klees.data.Fluggesellschaft;
import de.klees.data.Fluggesellschaftmanager;
import de.klees.data.Benutzer;
import de.klees.data.views.ViewFluggesellschaftManager;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 *
 * @author Stefan Klees
 * @param <T>
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

  public void createManager(Fluggesellschaftmanager entity) {
    getEntityManager().persist(entity);
  }

  public void edit(T entity) {
    getEntityManager().merge(entity);
  }

  public void saveManager(Fluggesellschaftmanager entity) {
    getEntityManager().merge(entity);
  }

  public void saveUser(Benutzer entity) {
    getEntityManager().merge(entity);
  }

  public void remove(T entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public void deleteManager(Fluggesellschaftmanager entity) {
    getEntityManager().remove(getEntityManager().merge(entity));
  }

  public T find(Object id) {
    return getEntityManager().find(entityClass, id);
  }

  public List<ViewFluggesellschaftManager> getAllManager(int fgid) {
    return getEntityManager().createQuery("SELECT m from ViewFluggesellschaftManager m WHERE m.fluggesellschaftid = :fgid", ViewFluggesellschaftManager.class)
            .setParameter("fgid", fgid)
            .getResultList();
  }

  public Fluggesellschaftmanager findManager(int mid) {
    try {
      return getEntityManager().createQuery("SELECT m from Fluggesellschaftmanager m WHERE m.idfluggesellschaftmanager = :mid", Fluggesellschaftmanager.class)
              .setParameter("mid", mid)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public Fluggesellschaft readFG(int fgid) {
    try {
      return getEntityManager().createQuery("SELECT f from Fluggesellschaft f WHERE f.idFluggesellschaft = :fgid", Fluggesellschaft.class)
              .setParameter("fgid", fgid)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public Fluggesellschaftmanager readManager(int uid, int fgid) {
    try {
      return getEntityManager().createQuery("SELECT m FROM Fluggesellschaftmanager m WHERE m.fluggesellschaftid = :fgid AND m.userid = :uid", Fluggesellschaftmanager.class)
              .setParameter("uid", uid)
              .setParameter("fgid", fgid)
              .getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }
  
  
  public boolean deleteManager(int id) {
    if (getEntityManager().createNativeQuery("delete from Fluggesellschaft_manager where idfluggesellschaftmanager = " + id).executeUpdate() > 0) {
      return true;
    }
    return false;
  }

  public Benutzer getUserDaten(String benutzername) {
    try {
      return getEntityManager().createQuery("SELECT u FROM User u WHERE u.name = :benutzername", Benutzer.class)
              .setParameter("benutzername", benutzername)
              .getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

}
