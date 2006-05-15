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
 * HtmlStreamingNormalizer.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   -;
 *
 * $Id: HtmlStreamingNormalizer.java,v 1.3 2006/04/23 15:18:18 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */
package org.jfree.layouting.output.streaming.html;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Properties;

import org.jfree.layouting.StreamingLayoutProcess;
import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.model.LayoutElement;
import org.jfree.layouting.model.LayoutTextNode;
import org.jfree.layouting.model.box.BoxSpecification;
import org.jfree.layouting.normalizer.NormalizationException;
import org.jfree.layouting.normalizer.streaming.StreamingNormalizer;
import org.jfree.resourceloader.Resource;
import org.jfree.resourceloader.ResourceException;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.util.DefaultConfiguration;
import org.jfree.util.Log;
import org.jfree.xmlns.writer.DefaultTagDescription;
import org.jfree.xmlns.writer.XmlWriter;

/**
 * Creation-Date: 02.01.2006, 19:52:34
 *
 * @author Thomas Morgner
 */
public class HtmlStreamingNormalizer implements StreamingNormalizer
{
  private XmlWriter xmlWriter;
  private Writer writer;
  private StreamingLayoutProcess layoutProcess;

  public HtmlStreamingNormalizer (final OutputStream outstream,
                                  final StreamingLayoutProcess layoutProcess)
          throws NormalizationException
  {
    try
    {
      this.writer = new OutputStreamWriter(outstream, "UTF-8");
    }
    catch (UnsupportedEncodingException e)
    {
      throw new NormalizationException("Blah!", e);
    }
    this.layoutProcess = layoutProcess;
  }


  public void startDocument ()
  {
    try
    {
      ResourceManager resourceManager = layoutProcess.getResourceManager();
      final DefaultTagDescription dtd = new DefaultTagDescription();
      try
      {
        final Resource resource = resourceManager.createDirectly
                ("res://org/jfree/layouting/output/streaming/html/htmltags.properties", Properties.class);
        final Properties props = (Properties) resource.getResource();
        final DefaultConfiguration conf = new DefaultConfiguration();
        conf.putAll(props);
        dtd.configure(conf, "");
      }
      catch (ResourceException e)
      {
        // ignore ...
        Log.info ("Unable to load HTML tag descriptions.");
      }


      this.xmlWriter = new XmlWriter(writer, dtd, "  ");
      this.xmlWriter.writeXmlDeclaration("UTF-8");
      this.xmlWriter.writeTag(null, "html", XmlWriter.OPEN);
      this.xmlWriter.writeTag(null, "head", XmlWriter.OPEN);
      this.xmlWriter.writeTag(null, "title", XmlWriter.OPEN);
      this.xmlWriter.writeText("Title");
      this.xmlWriter.writeCloseTag();
      this.xmlWriter.writeCloseTag();
      this.xmlWriter.writeTag(null, "body", XmlWriter.OPEN);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public void startElement (LayoutElement element)
          throws NormalizationException
  {
    try
    {
      if (element.getName() == null)
      {
        // this is an anonymous/generated element. Ignore it.
        xmlWriter.writeComment("Annonymous element");
        return;
      }

      xmlWriter.writeComment(element.getNamespace() + ":" + element.getName());
      BoxSpecification boxSpecification = element.getLayoutContext().getBoxSpecification();
      DisplayRole displayRole = boxSpecification.getDisplayRole();
      if (displayRole == DisplayRole.BLOCK)
      {
        xmlWriter.writeTag(null, "div", XmlWriter.OPEN);
      }
      else
      {
        xmlWriter.writeTag(null, "span", XmlWriter.OPEN);
      }
      Log.debug (element.getName() + " DisplayRole: " + displayRole + " DM " + boxSpecification.getDisplayModel()); 
    }
    catch (IOException ne)
    {
      throw new NormalizationException("Failed to write", ne);
    }
  }

  public void addText (LayoutTextNode text)
          throws NormalizationException
  {
    final String s = new String(text.getData(), text.getOffset(), text.getLength());
    try
    {
      String strimed = s.trim();
      if (strimed.length() > 0)
      {
        xmlWriter.writeText(strimed);
      }
    }
    catch (IOException e)
    {
      throw new NormalizationException("Failed to write", e);
    }
    text.clearFromParent();
  }

  public void addReplacedElement (LayoutElement element)
          throws NormalizationException
  {
    // we ignore that one, as all information leading to that element should
    // be included in the 'startElement' call already.
    try
    {
      xmlWriter.writeComment("replaced:element:" + element);
    }
    catch (IOException e)
    {
      throw new NormalizationException("Failed to write", e);
    }
  }

  public void endElement (final LayoutElement element)
          throws NormalizationException
  {
    if (element.getName() == null)
    {
      // this is an anonymous/generated element. Ignore it.
      element.clearFromParent();
      return;
    }

    try
    {
      xmlWriter.writeCloseTag();
    }
    catch (IOException e)
    {
      throw new NormalizationException("Failed to write", e);
    }
    element.clearFromParent();
  }

  public void endDocument ()
          throws IOException
  {
    this.xmlWriter.writeCloseTag();
    this.xmlWriter.writeCloseTag();
    this.xmlWriter.close();
  }
}
