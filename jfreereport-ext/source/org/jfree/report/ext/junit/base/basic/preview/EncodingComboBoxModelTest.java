/**
 * ========================================
 * JFreeReport : a free Java report library
 * ========================================
 *
 * Project Info:  http://www.object-refinery.com/jfreereport/index.html
 * Project Lead:  Thomas Morgner;
 *
 * (C) Copyright 2000-2003, by Simba Management Limited and Contributors.
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
 * ------------------------------
 * EncodingComboBoxModelTest.java
 * ------------------------------
 * (C)opyright 2003, by Thomas Morgner and Contributors.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   David Gilbert (for Simba Management Limited);
 *
 * $Id: EncodingComboBoxModelTest.java,v 1.6 2005/03/30 22:47:05 mimil Exp $
 *
 * Changes 
 * -------------------------
 * 29.06.2003 : Initial version
 * 31.03.2005 : Java 1.5 compatibility
 *  
 */

package org.jfree.report.ext.junit.base.basic.preview;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import javax.swing.JComboBox;

import junit.framework.TestCase;
import org.jfree.report.modules.gui.base.components.EncodingComboBoxModel;
import org.jfree.report.util.Log;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.util.ObjectUtilities;

public class EncodingComboBoxModelTest extends TestCase
{
  private class EncCBModelTestClass extends EncodingComboBoxModel
  {
    public EncCBModelTestClass()
    {
    }

    public Properties getDefaults ()
    {
      return EncodingComboBoxModel.getDefaultEncodings();
    }
  }

  public EncodingComboBoxModelTest()
  {
  }

  public EncodingComboBoxModelTest(final String s)
  {
    super(s);
  }

  public void testCreate()
  {
    new EncodingComboBoxModel();
    assertNotNull(EncodingComboBoxModel.createDefaultModel());
  }

  public void testSelectItemAllAvailable ()
  {
    String org = ReportConfiguration.getGlobalConfig().getConfigProperty
        (EncodingComboBoxModel.AVAILABLE_ENCODINGS);
    ReportConfiguration.getGlobalConfig().setConfigProperty
        (EncodingComboBoxModel.AVAILABLE_ENCODINGS, EncodingComboBoxModel.AVAILABLE_ENCODINGS_ALL);
    final EncodingComboBoxModel def = EncodingComboBoxModel.createDefaultModel();

    final int index = def.indexOf("Cp850");
    Log.debug ("Size: " + def.getSize());
    assertTrue(index > -1);

    final Object element = def.getElementAt(index);
    def.setSelectedItem(def.getElementAt(index));
    assertEquals(element, def.getSelectedItem());
    assertEquals(index, def.getSelectedIndex());

    final JComboBox cb = new JComboBox(def);
    cb.setSelectedIndex(index);
    assertEquals(element, cb.getSelectedItem());
    assertEquals(index, cb.getSelectedIndex());

    ReportConfiguration.getGlobalConfig().setConfigProperty
        (EncodingComboBoxModel.AVAILABLE_ENCODINGS, org);
  }

  public void testSelectItemFileAvailable ()
  {
    String org = ReportConfiguration.getGlobalConfig().getConfigProperty
        (EncodingComboBoxModel.AVAILABLE_ENCODINGS);
    ReportConfiguration.getGlobalConfig().setConfigProperty
        (EncodingComboBoxModel.AVAILABLE_ENCODINGS, EncodingComboBoxModel.AVAILABLE_ENCODINGS_FILE);
    final EncodingComboBoxModel def = EncodingComboBoxModel.createDefaultModel();

    final int index = def.indexOf("Cp850");
    Log.debug ("Size: " + def.getSize());
    assertTrue(index > -1);

    final Object element = def.getElementAt(index);
    def.setSelectedItem(def.getElementAt(index));
    assertEquals(element, def.getSelectedItem());
    assertEquals(index, def.getSelectedIndex());

    final JComboBox cb = new JComboBox(def);
    cb.setSelectedIndex(index);
    assertEquals(element, cb.getSelectedItem());
    assertEquals(index, cb.getSelectedIndex());

    ReportConfiguration.getGlobalConfig().setConfigProperty
        (EncodingComboBoxModel.AVAILABLE_ENCODINGS, org);
  }

  public void testSelectItemNoneAvailable ()
  {
    String org = ReportConfiguration.getGlobalConfig().getConfigProperty
        (EncodingComboBoxModel.AVAILABLE_ENCODINGS);
    ReportConfiguration.getGlobalConfig().setConfigProperty
        (EncodingComboBoxModel.AVAILABLE_ENCODINGS, EncodingComboBoxModel.AVAILABLE_ENCODINGS_NONE);
    final EncodingComboBoxModel def = EncodingComboBoxModel.createDefaultModel();

    final int index = def.indexOf("Cp850");
    assertTrue(index == -1);

    ReportConfiguration.getGlobalConfig().setConfigProperty
        (EncodingComboBoxModel.AVAILABLE_ENCODINGS, org);
  }

  public void testAllEncodingsDefined ()
  {
    final String encFile = ReportConfiguration.getGlobalConfig().getConfigProperty
        (EncodingComboBoxModel.ENCODINGS_DEFINITION_FILE, 
         EncodingComboBoxModel.ENCODINGS_DEFINITION_FILE_DEFAULT);
    final InputStream in = ObjectUtilities.getResourceAsStream
            (encFile, EncodingComboBoxModelTest.class);
    if (in == null)
    {
      fail ("The specified encodings definition file was not found: " + encFile);
    }
    else
    {
      try
      {
        final Properties defaultEncodings = new EncCBModelTestClass().getDefaults();
        final Properties encDef = new Properties();
        final BufferedInputStream bin = new BufferedInputStream(in);
        encDef.load(bin);
        bin.close();
        final Enumeration enumeration = defaultEncodings.keys();
        while (enumeration.hasMoreElements())
        {
          final String enc = (String) enumeration.nextElement();
          // if not set to "true"
          String defined = encDef.getProperty(enc);
          if (defined == null)
          {
            throw new IllegalStateException("Encoding not defined: " + enc);
          }
          if (defined.equalsIgnoreCase("true")== false && 
              defined.equalsIgnoreCase("false") == false)
          {
            throw new IllegalStateException("Encoding not invalid: " + enc);
          }
        }
      }
      catch (IOException e)
      {
        Log.warn(new Log.SimpleMessage
            ("There was an error while reading the encodings definition file: ", encFile), e);
      }
    }
  }

}
