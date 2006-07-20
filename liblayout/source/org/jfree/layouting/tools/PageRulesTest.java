/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/liblayout/
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2005, by Object Refinery Limited and Contributors.
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
 * PageRulesTest.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: PageRulesTest.java,v 1.1 2006/07/11 14:03:35 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.tools;

import java.io.File;

import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.layouter.style.resolver.ResolverFactory;
import org.jfree.layouting.input.style.StyleKeyRegistry;
import org.jfree.layouting.input.style.StyleSheet;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceCreationException;
import org.jfree.resourceloader.ResourceKeyCreationException;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.util.Log;

/**
 * Creation-Date: 23.05.2006, 15:42:33
 *
 * @author Thomas Morgner
 */
public class PageRulesTest
{
  public static void main(String[] args) throws ResourceKeyCreationException,
          ResourceCreationException, ResourceLoadingException
  {
    LibLayoutBoot.getInstance().start();
    Log.error ("Start..." + StyleKeyRegistry.getRegistry().getKeyCount());

    ResolverFactory.getInstance().registerDefaults();
    
    Log.debug ("---------------------------------------------------------");
    final File defaultStyleURL = new File
            ("/home/src/jfreereport/head/liblayout/resource/colspan.css");

    final ResourceManager manager = new ResourceManager();
    manager.registerDefaults();
    Resource res = manager.createDirectly(defaultStyleURL, StyleSheet.class);
    StyleSheet s = (StyleSheet) res.getResource();

  }
}
