/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://jfreereport.pentaho.org/
 *
 * (C) Copyright 2006, by Pentaho Corporation and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * DefaultSessionProvider.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.report.ext.modules.data.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

/**
 * A simple default implementation that opens the default configuration to
 * create a default session factory to get a default session. (A lot of
 * defaults, so it may or may not fit into real world applications. This
 * provider does not deal with transactions or optimizes caching, loading
 * or what else could be optimized in Hibernate-Queries.)
 *
 * @author Thomas Morgner
 */
public class DefaultSessionProvider implements SessionProvider
{
  private SessionFactory sessionFactory;

  public DefaultSessionProvider()
  {
    // Create the SessionFactory from hibernate.cfg.xml
    sessionFactory = new Configuration().configure().buildSessionFactory();
  }

  public Session getSession() throws HibernateException
  {
    return sessionFactory.openSession();
  }
}
