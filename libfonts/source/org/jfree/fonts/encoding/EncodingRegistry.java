/**
 * ===========================================
 * LibFonts : a free Java font reading library
 * ===========================================
 *
 * Project Info:  http://www.jfree.org/jfreereport/libfonts/
 * Project Lead:  Thomas Morgner;
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
 * EncodingSupport.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id$
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.fonts.encoding;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.jfree.fonts.LibFontBoot;
import org.jfree.fonts.encoding.manual.BuiltInJavaEncoding;
import org.jfree.fonts.encoding.manual.Ascii;
import org.jfree.fonts.encoding.manual.Utf16LE;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.resourceloader.factory.property.PropertiesResourceFactory;
import org.jfree.util.ExtendedConfiguration;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * A global registry for all supported encodings. This offers the option to fall
 * back to Java - which is enabled by default.
 *
 * @author Thomas Morgner.
 */
public final class EncodingRegistry
{
  /**
   * Implementation doc: This class uses several sources to load the encodings.
   * The primary source are the known manually written encodings; they provide
   * the base set (namely UFT-16LE and ISO-8859-1).
   *
   * The 'encodingsMapping' hashmap contains the name of the encoding along
   * with the classname. For now, we do not cache these encodings; we should
   * to it, but we dont.
   *
   * The second set are the generated mappings; the hashmap contains the name
   * of the encoding along with the resourcekey pointing to the .ser files.
   * These files must be serialized objects of the type "EncodingData". For
   * now, only External8BitEncodingData objects are accepted.
   *
   * And finally, we use the JDK for all not-known encodings.
   */

  /**
   * the string that should be encoded. This is a simple space, and should be
   * supported by all encodings - but will cause InvalidEncodingExceptions if
   * the encoding does not exist.
   */
  private static final String TEST_STRING = " ";

  private HashMap encodingsMapping;
  private HashMap generatedMapping;
  private HashMap fallbackMapping;
  private ResourceManager manager;

  private static EncodingRegistry instance;

  public synchronized static EncodingRegistry getInstance()
  {
    if (instance == null)
    {
      instance = new EncodingRegistry();
      instance.registerDefaults();
    }
    return instance;
  }

  private EncodingRegistry()
  {
    this.manager = new ResourceManager();
    this.manager.registerFactoryCache();
    this.manager.registerDataCache();
    this.manager.registerDefaultLoaders();
    this.manager.registerFactory(new EncodingFactory());
    this.manager.registerFactory(new PropertiesResourceFactory());

    this.encodingsMapping = new HashMap();
    this.generatedMapping = new HashMap();
    this.fallbackMapping = new HashMap();
  }

  private void registerDefaults()
  {
    ExtendedConfiguration config =
            LibFontBoot.getInstance().getExtendedConfig();

    Iterator encodings = config.findPropertyKeys
            ("org.jfree.fonts.encoding.manual.");
    while (encodings.hasNext())
    {
      final String key = (String) encodings.next();
      final String encodingClass = config.getConfigProperty(key);
      final Object maybeEncoding = ObjectUtilities.loadAndInstantiate
              (encodingClass, EncodingRegistry.class);
      if (maybeEncoding instanceof Encoding)
      {
        // ok, loaded perfectly ..
        final Encoding encoding = (Encoding) maybeEncoding;
        final String name = encoding.getName();
        Log.debug ("Manual mapping added. " + name);
        encodingsMapping.put(name.toLowerCase(), encodingClass);
      }
    }

    Iterator generateDirs = config.findPropertyKeys
            ("org.jfree.fonts.encoding.generated.");
    while (generateDirs.hasNext())
    {
      final String key = (String) generateDirs.next();
      final String dataLocation = config.getConfigProperty(key);
      try
      {
        final ResourceKey resKey = manager.createKey(dataLocation);
        final Resource res = manager.create(resKey, null, Properties.class);
        final Properties props = (Properties) res.getResource();
        final Iterator vals = props.entrySet().iterator();
        while (vals.hasNext())
        {
          final Map.Entry entry = (Map.Entry) vals.next();
          final String encName = (String) entry.getKey();
          final String encPath = (String) entry.getValue();
          final ResourceKey encKey = manager.deriveKey(resKey, encPath);
          Log.debug ("Generated mapping added. " +  encName);
          generatedMapping.put(encName.toLowerCase(), encKey);
        }
      }
      catch (Exception e)
      {
        // ignore that key, it is behaving badly ..
        e.printStackTrace();
      }
    }

  }

  /**
   * Returns <code>true</code> if the encoding is valid, and <code>false</code>
   * otherwise.
   *
   * @param encoding the encoding (name).
   * @return A boolean.
   */
  public boolean isSupportedEncoding(final String encoding)
  {
    if (encoding == null)
    {
      throw new NullPointerException();
    }

    final String key = encoding.toLowerCase();
    if (encodingsMapping.containsKey(key))
    {
      return true;
    }

    if (generatedMapping.containsKey(key))
    {
      return true;
    }

    return isEncodingSupportedJVM(encoding);
  }

  private boolean isEncodingSupportedJVM(final String encoding)
  {
    try
    {
      TEST_STRING.getBytes(encoding);
      fallbackMapping.put(encoding, Boolean.TRUE);
      return true;
    }
    catch (Exception ue)
    {
      // There is a known JDK bug in JDK 1.5 with Cp903 encoding
      Log.info(new Log.SimpleMessage
              ("Encoding ", encoding, " is not supported."));
      return false;
    }
  }


  /**
   * Helper method to read the platform default encoding from the VM's system
   * properties.
   *
   * @return the contents of the system property "file.encoding".
   */
  public static String getPlatformDefaultEncoding()
  {
    return LibFontBoot.getInstance().getGlobalConfig().getConfigProperty
            ("file.encoding", "Cp1252");
  }

  public Encoding getEncoding(String name)
  {
    String key = name.toLowerCase();
    String manual = (String) encodingsMapping.get(key);
    if (manual != null)
    {
      // we tested the instantiation during the initialization,
      // so we can be sure that no errors appear here
      return (Encoding) ObjectUtilities.loadAndInstantiate
              (manual, EncodingRegistry.class);
    }

    ResourceKey generated = (ResourceKey) generatedMapping.get(key);
    if (generated != null)
    {

      try
      {
        final Resource res = manager.create(generated, null, Encoding.class);
        final Object o = res.getResource();
        if (o instanceof EncodingCore)
        {
          return new ExternalEncoding(name, (EncodingCore) o, res);
        }
      }
      catch (Exception e)
      {
        // fall back ...
        e.printStackTrace();
      }
    }
    if (isEncodingSupportedJVM(name))
    {
      return new BuiltInJavaEncoding(name, true);
    }
    else
    {
      return new Ascii();
    }
  }


  public static void main(String[] args) throws EncodingException
  {
    LibFontBoot.getInstance().start();
    final EncodingRegistry reg = EncodingRegistry.getInstance();
    final Encoding encoding = reg.getEncoding("MacRoman");
    System.out.println ("MacRoman: " + encoding);
    byte[] text = new byte[] { 'A', 'b', 'c' };
    final ByteBuffer byteBuffer = new ByteBuffer(text);
    final CodePointBuffer cp = encoding.decode(byteBuffer, null);
    System.out.println (Utf16LE.getInstance().encodeString(cp));

  }
}
