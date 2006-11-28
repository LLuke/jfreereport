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
 * EncodingGenerator.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: EncodingGenerator.java,v 1.1 2006/04/29 15:12:58 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.fonts.encoding.generator;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import org.jfree.fonts.LibFontBoot;
import org.jfree.fonts.encoding.External8BitEncodingData;
import org.jfree.util.DefaultConfiguration;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;

/**
 * A simple sourcecode generator.
 *
 * @author Thomas Morgner
 */
public class EncodingGenerator
{
  private File targetDirectory;
  private DefaultConfiguration propertySet;
  private File sourceDirectory;
  public static final Integer ZERO = new Integer(0);

  public EncodingGenerator(final File targetDirectory,
                           final File sourceDirectory) throws IOException
  {
    if (targetDirectory == null)
    {
      throw new NullPointerException();
    }
    if (targetDirectory.isDirectory() == false)
    {
      throw new IllegalArgumentException("Not a directory: " + targetDirectory);
    }
    if (targetDirectory.canWrite() == false)
    {
      throw new IllegalArgumentException("Not writable: " + targetDirectory);
    }
    if (sourceDirectory == null)
    {
      throw new NullPointerException();
    }
    if (sourceDirectory.isDirectory() == false)
    {
      throw new IllegalArgumentException("Not a directory: " + sourceDirectory);
    }
    if (sourceDirectory.canRead() == false)
    {
      throw new IllegalArgumentException("Not readable: " + sourceDirectory);
    }

    this.sourceDirectory = sourceDirectory;
    this.targetDirectory = targetDirectory;

    InputStream propIn = ObjectUtilities.getResourceRelativeAsStream
            ("encodings.properties", EncodingGenerator.class);
    propertySet = new DefaultConfiguration();
    propertySet.load(propIn);
  }

  public void generatedFormatA(final Properties specifications,
                               final BufferedReader input,
                               final OutputStream output)
          throws IOException
  {
    if (input == null)
    {
      throw new NullPointerException();
    }
    if (output == null)
    {
      throw new NullPointerException();
    }
    if (specifications == null)
    {
      throw new NullPointerException();
    }

    final Integer[] data = new Integer[256];
    data[0] = ZERO;

    String s = input.readLine();
    while (s != null)
    {
      if (s.startsWith("#"))
      {
        s = input.readLine();
        continue;
      }

      StringTokenizer strtok = new StringTokenizer(s);
      if (strtok.hasMoreTokens() == false)
      {
        s = input.readLine();
        continue;
      }
      final String sourceChar = strtok.nextToken().trim();
      if (sourceChar.length() == 0)
      {
        s = input.readLine();
        continue;
      }

      final Integer sourceVal = Integer.decode(sourceChar);
      if (strtok.hasMoreTokens())
      {
        final String targetChar = strtok.nextToken();
        if (targetChar.startsWith("#") == false)
        {
          data[sourceVal.intValue()] = Integer.decode(targetChar);
        }
      }

      s = input.readLine();
    }

    int[] indices = new int[256];
    int[] values = new int[256];

    int index = 0;
    int prevIdx = 0;
    for (int i = 1; i < data.length; i++)
    {
      Integer integer = data[i];
      if (integer == null)
      {
        // no entry ... this means, we use the standard mapping ..
        continue;
      }

      Integer prev = data[prevIdx];
      values[index] = integer.intValue() - prev.intValue();
      indices[index] = i - prevIdx;

      prevIdx = i;
      index += 1;
    }

    ObjectOutputStream oout = new ObjectOutputStream(output);
    oout.writeObject(new External8BitEncodingData(indices, values));
    oout.flush();
  }

  public void generateAll () throws IOException
  {
    final Iterator keys = propertySet.keySet().iterator();
    while (keys.hasNext())
    {
      String key = (String) keys.next();
      if (key.startsWith("encodings.") == false)
      {
        Log.info("Encoding prefix not found.");
        continue;
      }
      if (key.endsWith(".Filename") == false)
      {
        continue;
      }
      try
      {
        generate(key.substring(0, key.length() - 8));
      }
      catch(Exception e)
      {
        Log.warn ("Failed to generate Encoding " + key, e);
      }
    }
  }

  public void generate(String prefix) throws IOException
  {
    String filename = propertySet.getConfigProperty(prefix + "Filename");
    String className = propertySet.getConfigProperty(prefix + "ClassName");
    String encoding = propertySet.getConfigProperty(prefix + "Encoding");

    if (filename == null)
    {
      Log.info ("Not generating Encoding: " + prefix + ": Filename missing");
      return;
    }
    if (className == null)
    {
      Log.info ("Not generating Encoding: " + prefix + ": ClassName missing");
      return;
    }
    if (encoding == null)
    {
      Log.info ("Not generating Encoding: " + prefix + ": Encoding missing");
      return;
    }

    final Properties properties = new Properties();
    properties.setProperty("name", className);
    properties.setProperty("source", filename);
    properties.setProperty("encoding", encoding);

    final File sourceFile = new File(sourceDirectory, className + ".ser");
    final File targetFile = new File(targetDirectory, filename);

    System.out.println("Generated Java File:  " + sourceFile);
    System.out.println("Source Specification: " + targetFile);
    final BufferedOutputStream bw =
            new BufferedOutputStream(new FileOutputStream(sourceFile));

    final BufferedReader input = new BufferedReader(new FileReader(targetFile));
    generatedFormatA(properties, input, bw);
    input.close();
    bw.close();
  }

  public void generatePropertyIndex ()
          throws IOException
  {
    final Properties props = new Properties();
    final Iterator keys = propertySet.keySet().iterator();
    while (keys.hasNext())
    {
      String key = (String) keys.next();
      if (key.startsWith("encodings.") == false)
      {
        Log.info("Encoding prefix not found.");
        continue;
      }
      if (key.endsWith(".Filename") == false)
      {
        continue;
      }
      try
      {
        generateEncodingEntry(props, key.substring(0, key.length() - 8));
      }
      catch(Exception e)
      {
        Log.warn ("Failed to generate Encoding " + key, e);
      }
    }

    final File sourceFile = new File(sourceDirectory, "encodings.properties");
    final FileOutputStream fout = new FileOutputStream(sourceFile);
    props.store(fout, "Generated on " + new Date());
    fout.close();
  }

  private void generateEncodingEntry (Properties props, String prefix)
  {
    String className = propertySet.getConfigProperty(prefix + "ClassName");
    String encoding = propertySet.getConfigProperty(prefix + "Encoding");

    if (className == null)
    {
      Log.info ("Not generating Encoding: " + prefix + ": ClassName missing");
      return;
    }
    if (encoding == null)
    {
      Log.info ("Not generating Encoding: " + prefix + ": Encoding missing");
      return;
    }
    props.setProperty(encoding, className + ".ser");
  }

  public static void main(String[] args) throws IOException
  {
    LibFontBoot.getInstance().start();
    final File encodingsDir =
            new File("/home/src/jfreereport/head/libfonts/encodings").getAbsoluteFile();
    final File sourceDir = new File
            ("/home/src/jfreereport/head/libfonts/source/org/jfree/fonts/encoding/generated/")
            .getAbsoluteFile();
    EncodingGenerator gen = new EncodingGenerator (encodingsDir, sourceDir);
    gen.generateAll();
    gen.generatePropertyIndex();
  }

}
//      byteSequence.append("    ");
//      byteSequence.append("BYTE_TO_CHAR[");
//      byteSequence.append(i);
//      byteSequence.append("] = (char) ");
//      byteSequence.append(integer);
//      byteSequence.append(";\n");
// /home/src/jfreereport/head/libfonts/encodings/VENDORS/MISC/KOI8-R.TXT