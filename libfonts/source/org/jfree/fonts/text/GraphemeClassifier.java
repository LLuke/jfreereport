/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/liblayout/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
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
 * $Id: GraphemeClassifier.java,v 1.1 2007/04/03 14:13:56 taqua Exp $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.fonts.text;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

import org.jfree.fonts.tools.ByteTable;
import org.jfree.util.Log;

/**
 * Creation-Date: 11.06.2006, 17:11:16
 *
 * @author Thomas Morgner
 */
public final class GraphemeClassifier
{
  public static final int OTHER = 0;

  public static final int CR = 0x01;
  public static final int LF = 0x02;
  public static final int CONTROL = 0x03;

  public static final int EXTEND = 4;

  public static final int L   = 0x08; // 0000 0000 1000;
  public static final int LV  = 0x18; // 0000 0001 1000;
  public static final int V   = 0x38; // 0000 0011 1000;
  public static final int T   = 0x68; // 0000 0110 1000;
  public static final int LVT = 0x48; // 0000 0100 1000;

  public static final int ANY_HANGUL_MASK = 0x8;
  public static final int   V_OR_LV_MASK = 0x18;
  public static final int   V_OR_T_MASK = 0x28;
  public static final int LVT_OR_T_MASK = 0x48;

  private ByteTable classificationData;
  private static GraphemeClassifier classifier;

  private GraphemeClassifier()
  {
    InputStream in = getClass().getResourceAsStream("/org/jfree/fonts/text/generated/grapheme-classification.ser");
    if (in != null)
    {
      try
      {
        final ObjectInputStream oin = new ObjectInputStream(in);
        classificationData = (ByteTable) oin.readObject();
        oin.close();
        in = null;
      }
      catch(Exception e)
      {
        Log.warn ("Unable to load the pre-generated classification data.", e);
      }
      finally
      {
        if (in != null)
        {
          try
          {
            in.close();
          }
          catch (IOException e)
          {
            // ignore ..
          }
        }
      }
    }
  }

  public int getGraphemeClassification(final int codePoint)
  {
    if (classificationData != null)
    {
      final int row = codePoint >> 8;
      final int col = codePoint & 0xFF;
      return classificationData.getByte(row, col, (byte) OTHER);
    }
    if (codePoint == 0x0D)
    {
      return CR;
    }
    if (codePoint == 0x0A)
    {
      return LF;
    }
    return OTHER;
  }

  public static synchronized GraphemeClassifier getClassifier()
  {
    if (classifier == null)
    {
      classifier = new GraphemeClassifier();
    }
    return classifier;
  }
}
