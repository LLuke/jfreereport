/**
 * ===================================================
 * JCommon-Serializer : a free serialization framework
 * ===================================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/jcommon-serializer/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2006, by Object Refinery Limited and Pentaho Corporation.
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
 * JCommonSerializerInfo.java
 * ------------
 * (C) Copyright 2006, by Object Refinery Limited and Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: JCommonSerializerInfo.java,v 1.2 2006/04/17 16:03:24 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.serializer;

import java.util.Arrays;

import org.jfree.ui.about.ProjectInfo;
import org.jfree.ui.about.Contributor;
import org.jfree.JCommonInfo;

/**
 * Creation-Date: 23.10.2005, 18:49:39
 *
 * @author Thomas Morgner
 */
public class JCommonSerializerInfo extends ProjectInfo
{
  /**
   * The info singleton.
   */
  private static JCommonSerializerInfo singleton;

  /**
   * Returns the single instance of this class.
   *
   * @return The single instance of information about the JCommon library.
   */
  public static synchronized JCommonSerializerInfo getInstance() {
      if (singleton == null) {
          singleton = new JCommonSerializerInfo();
      }
      return singleton;
  }


  /**
   * Constructs an empty project info object.
   */
  private JCommonSerializerInfo() {
      final JCommonInfo info = JCommonInfo.getInstance();

      setName("JCommon-Serializer");
      setVersion("0.1.0");
      setInfo("http://www.jfree.org/jfreereport/jcommon-serializer/");
      setCopyright ("(C)opyright 2006, by Pentaho Corporation, Object Refinery Limited and Contributors");

      setLicenceName(info.getLicenceName());
      setLicenceText(info.getLicenceText());

      setContributors(Arrays.asList(new Contributor[]{
          new Contributor("David Gilbert", "david.gilbert@object-refinery.com"),
          new Contributor("Thomas Morgner", "tmorgner@pentaho.org"),
      }));
  }

}
