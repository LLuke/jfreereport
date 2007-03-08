/**
 * ================================================
 * LibLoader : a free Java resource loading library
 * ================================================
 *
 * Project Info:  http://jfreereport.pentaho.org/libloader/
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
 *
 * ------------
 * $Id$
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 */

package org.jfree.resourceloader;

import java.net.URL;
import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

/**
 * Todo: Document me!
 *
 * @author Thomas Morgner
 * @since 08.03.2007
 */
public class TestResourceKey extends TestCase
{
  public TestResourceKey()
  {
  }

  public TestResourceKey(String string)
  {
    super(string);
  }


  protected void setUp()
      throws Exception
  {
    LibLoaderBoot.getInstance().start();
  }

  public void testResourceKeyCreation ()
      throws ResourceKeyCreationException
  {
    ResourceManager manager = new ResourceManager();
    manager.registerDefaults();

    final ResourceKey key = manager.createKey
        ("res://org/jfree/resourceloader/test1.properties");
    assertNotNull(key);
    final ResourceKey key1 = manager.deriveKey(key, "test2.properties");
    assertNotNull(key1);
  }

  public void testURLKeyCreation ()
      throws ResourceKeyCreationException
  {
    ResourceManager manager = new ResourceManager();
    manager.registerDefaults();

    final URL url = TestResourceKey.class.getResource
        ("/org/jfree/resourceloader/test1.properties");
    assertNotNull(url);
    final ResourceKey key = manager.createKey(url);
    assertNotNull(key);
    final ResourceKey key1 = manager.deriveKey(key, "test2.properties");
    assertNotNull(key1);
  }

  public void testFileKeyCreation ()
      throws ResourceKeyCreationException, IOException
  {
    ResourceManager manager = new ResourceManager();
    manager.registerDefaults();

    final File f1 = File.createTempFile("junit-test", ".tmp");
    f1.deleteOnExit();
    final File f2 = File.createTempFile("junit-test", ".tmp");
    f2.deleteOnExit();

    assertNotNull(f1);
    final ResourceKey key = manager.createKey(f1);
    assertNotNull(key);
    final ResourceKey key1 = manager.deriveKey(key, f2.getName());
    assertNotNull(key1);
  }

  public void testMixedKeyDerivation ()
      throws ResourceKeyCreationException, IOException
  {
    final File f1 = File.createTempFile("junit-test", ".tmp");
    f1.deleteOnExit();
    assertNotNull(f1);

    final URL url = TestResourceKey.class.getResource
        ("/org/jfree/resourceloader/test1.properties");
    assertNotNull(url);

    ResourceManager manager = new ResourceManager();
    manager.registerDefaults();
    final ResourceKey key = manager.createKey(f1);
    assertNotNull(key);

    final ResourceKey key2 = manager.deriveKey(key, url.toString());
    assertNotNull(key2);
  }
}
