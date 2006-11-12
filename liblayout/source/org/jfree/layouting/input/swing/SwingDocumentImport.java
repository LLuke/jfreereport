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
 * SwingDocumentImport.java
 * ------------
 * (C) Copyright 2006, by Pentaho Corporation.
 *
 * Original Author:  Thomas Morgner;
 * Contributor(s):   Cedric Pronzato;
 *
 * $Id: SwingDocumentImport.java,v 1.6 2006/11/09 14:28:49 taqua Exp $
 *
 * Changes
 * -------
 *
 *
 */

package org.jfree.layouting.input.swing;

import org.jfree.layouting.DefaultLayoutProcess;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.input.swing.converter.CharacterConverter;
import org.jfree.layouting.input.swing.converter.ColorConverter;
import org.jfree.layouting.input.swing.converter.FontConverter;
import org.jfree.layouting.input.swing.converter.ParagraphConverter;
import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.layouter.feed.InputFeedException;
import org.jfree.layouting.modules.output.html.StreamingHtmlOutputProcessor;
import org.jfree.layouting.normalizer.content.NormalizationException;
import org.jfree.layouting.util.NullOutputStream;

import javax.swing.text.*;
import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

/**
 * Right now, we do not convert Swing-styles into CSS styles. Hey, we should,
 * but we don't.
 * <p/>
 * todo parse styles
 */
public class SwingDocumentImport
{
  public static final String NAMESPACE = "http://www.w3.org/1999/xhtml";
  public static final String STYLE_ATTRIBUTE = "style";
  public static final String PARENT_STYLE_ATTRIBUTE = "parent";

  private Map styleNames;
  private InputFeed feed;
  protected static Map styleConstantsMap = new HashMap();

  static
  {
    // font
    final FontConverter fontConverter = new FontConverter();
    styleConstantsMap.put(StyleConstants.FontFamily, fontConverter);
    styleConstantsMap.put(StyleConstants.FontSize, fontConverter);
    styleConstantsMap.put(StyleConstants.Bold, fontConverter);
    styleConstantsMap.put(StyleConstants.Italic, fontConverter);

    // text
    final ParagraphConverter paragraphConverter = new ParagraphConverter();
    styleConstantsMap.put(StyleConstants.Alignment, paragraphConverter);
    styleConstantsMap.put(StyleConstants.LeftIndent, paragraphConverter);
    styleConstantsMap.put(StyleConstants.RightIndent, paragraphConverter);
    styleConstantsMap.put(StyleConstants.SpaceAbove, paragraphConverter);
    styleConstantsMap.put(StyleConstants.SpaceBelow, paragraphConverter);
    styleConstantsMap.put(StyleConstants.FirstLineIndent, paragraphConverter);

    // character
    final CharacterConverter characterConverter = new CharacterConverter();
    styleConstantsMap.put(StyleConstants.Underline, characterConverter);
    styleConstantsMap.put(StyleConstants.StrikeThrough, characterConverter);
    styleConstantsMap.put(StyleConstants.BidiLevel, characterConverter);
    styleConstantsMap.put(StyleConstants.Superscript, characterConverter);
    styleConstantsMap.put(StyleConstants.Subscript, characterConverter);

    // color
    final ColorConverter colorConverter = new ColorConverter();
    styleConstantsMap.put(StyleConstants.Foreground, colorConverter);
    styleConstantsMap.put(StyleConstants.Background, colorConverter);

  }

  public SwingDocumentImport()
  {
    styleNames = new HashMap();
  }

  public InputFeed getFeed()
  {
    return feed;
  }

  public void setFeed(InputFeed feed)
  {
    this.feed = feed;
  }

  private String convertStyleName(String name)
  {
    //todo implement me
    return name;
  }

  public String normalizeStyleName(String name)
  {
    if (name == null)
    {
      throw new IllegalArgumentException("The style name must not be null");
    }

    String o = (String) styleNames.get(name);
    if (o == null)
    {
      o = convertStyleName(name);
      if (o == null)
      {
        throw new IllegalStateException("Unable to convert style name");
      }

      final Object res = styleNames.put(name, o);
      if (res != null)
      {
        throw new IllegalStateException("Style name clash during convertion");
      }
    }
    return o;
  }

  public AttributeSet convertAttributes(AttributeSet attr, Element context)
      throws InputFeedException
  {
    final SimpleAttributeSet cssAttr = new SimpleAttributeSet();
    final Enumeration attributeNames = attr.getAttributeNames();
    while (attributeNames.hasMoreElements())
    {
      final Object key = attributeNames.nextElement();
      final Object value = attr.getAttribute(key);

      final Converter converter = (Converter) styleConstantsMap.get(key);
      if (converter != null)
      {
        final AttributeSet attributeSet = converter.convertToCSS(key, value, cssAttr, context);
        if (attributeSet != null)
        {
          cssAttr.addAttributes(attributeSet);
        }
        else
        {
          debugAttribut("No convertion for ", key, value);
          cssAttr.addAttribute(key, value);
        }
      }
      else
      {
        debugAttribut("No converter for ", key, value);
        cssAttr.addAttribute(key, value);
      }
    }

    return cssAttr;
  }

  private void debugAttribut(String name, Object key, Object value)
  {
    System.out.println(name + "attribute [" + key.getClass().getName() + "] " + key + " = " + value + " [" + value.getClass().getName() + "]");
  }

  protected void handleElement(Element element)
      throws BadLocationException, InputFeedException
  {
    if (element == null)
    {
      return;
    }


    System.out.println("Stating Element: " + element.getName());
    feed.startElement(NAMESPACE, element.getName());
    final AttributeSet as = element.getAttributes();

    final AttributeSet cssAttr = convertAttributes(as, element);

    final Enumeration attributeNames = cssAttr.getAttributeNames();
    while (attributeNames.hasMoreElements())
    {
      final Object key = attributeNames.nextElement();
      final Object value = cssAttr.getAttribute(key);

      if (key == StyleConstants.NameAttribute)
      {
        continue;
      }

      if (key == StyleConstants.ResolveAttribute)
      {
        // style name
        if (value instanceof Style)
        {
          final Style style = (Style) value;
          final String styleName = (String) styleNames.get(style.getName());
          feed.setAttribute(NAMESPACE, STYLE_ATTRIBUTE, styleName);
          continue;
        }
      }

      debugAttribut("Element ", key, value);
      feed.setAttribute(NAMESPACE, key.toString(), value);
    }

    final String text = getElementText(element);
    if (text != null || !"".equals(text))
    {
      if (element.isLeaf())
      {
        System.out.println("'" + text + "'");
        feed.addContent(text);
      }
    }


    final int size = element.getElementCount();
    for (int i = 0; i < size; i++)
    {
      final Element e = element.getElement(i);
      handleElement(e);
    }

    feed.endElement();
  }

  /**
   * Returns the text content of an element.
   *
   * @param element The element containing text.
   * @return The text.
   * @throws BadLocationException If the text position is invalid.
   */
  protected String getElementText(Element element)
      throws BadLocationException
  {
    final Document document = element.getDocument();
    final String text = document.getText
        (element.getStartOffset(),
            element.getEndOffset() - element.getStartOffset());
    return text;
  }


  /**
   * Processes the style definitions of a styled document. Style definitions are
   * declared once in the document and are reused by styled elements.
   *
   * @param document The source document.
   * @throws InputFeedException If a problem occured with the feed.
   */
  protected void processStyleElements(DefaultStyledDocument document)
      throws InputFeedException
  {
    final Enumeration names = document.getStyleNames();
    while (names.hasMoreElements())
    {
      String styleName = (String) names.nextElement();
      Style s = document.getStyle(styleName);

      System.out.println("Processing style: " + styleName);

      if (s == null)
      {
        continue;
      }
      /*if (s.getAttributeCount() <= 1 &&
          s.isDefined(StyleConstants.NameAttribute))
      {
        continue;
      }*/

      final String convertedStyleName = convertStyleName(styleName);
      styleNames.put(styleName, convertedStyleName);

      final AttributeSet cssAttr = convertAttributes(s, null);

      final Enumeration attributeNames = cssAttr.getAttributeNames();
      while (attributeNames.hasMoreElements())
      {
        final Object key = attributeNames.nextElement();
        final Object value = cssAttr.getAttribute(key);

        if (key == StyleConstants.NameAttribute)
        {
          continue;
        }


        if(key == StyleConstants.ResolveAttribute)
        {
          // parent style
          if (value instanceof Style)
          {
            final Style style = (Style) value;
            final String parentStyleName = (String)styleNames.get(style.getName());
            if(parentStyleName != null)
            {
              feed.startMetaNode();
              debugAttribut("Style parent ", PARENT_STYLE_ATTRIBUTE, parentStyleName);
              feed.setMetaNodeAttribute(PARENT_STYLE_ATTRIBUTE, parentStyleName);
              feed.endMetaNode();
              continue;
            }
            else
            {
              //todo default style
              System.out.println("Parent style name not found: "+style.getName());
              continue;
            }
          }
        }

        feed.startMetaNode();
        debugAttribut("Style ", key, value);
        feed.setMetaNodeAttribute(key.toString(), value);
        feed.endMetaNode();
      }

    }
  }

  /**
   * Processes the document properties. These properties defined once for the
   * whole document.
   *
   * @param document The document source.
   * @throws InputFeedException If a problem occured with the feed.
   */
  protected void processDocumentProperties(DefaultStyledDocument document)
      throws InputFeedException
  {
    //final Object title = document.getProperty(DefaultStyledDocument.TitleProperty);
    final Dictionary documentProperties = document.getDocumentProperties();
    final Enumeration keys = documentProperties.keys();
    while (keys.hasMoreElements())
    {
      final Object key = keys.nextElement();
      final Object value = documentProperties.get(key);

      if (key instanceof String)
      {
        debugAttribut("Document Property ", key, value);
        feed.addDocumentAttribute((String) key, value);
      } // ingnoring non String properties
      else
      {
        debugAttribut("Ignoring document Property ", key, value);
      }
    }
    //todo copy XhtmlInputDriver code for HMLT headers
  }

  public void parseDocument(DefaultStyledDocument doc, InputFeed feed)
      throws BadLocationException, InputFeedException
  {
    setFeed(feed);
    feed.startDocument();
    feed.startMetaInfo();
    processDocumentProperties(doc);
    processStyleElements(doc);
    feed.endMetaInfo();

    handleElement(doc.getDefaultRootElement());
    feed.endDocument();
  }


  public static void main(String[] args)
      throws IOException, NormalizationException, BadLocationException,
      InputFeedException
  {
    //final URL initialPage = new URL("http://www.google.com");
    //final URL initialPage = new URL("http://www.tug.org/tex-archive/obsolete/info/RTF/RTF-Spec.rtf");
    final URL initialPage = new URL("http://interglacial.com/rtf/rtf_book_examples/example_documents/p060_landscape_a4.rtf");
    //final URL initialPage = new URL("file:///d:/temp/1.rtf");
    initialPage.getContent();
    final JEditorPane pane = new JEditorPane(initialPage);
    pane.setEditable(false);
    final JFrame frame = new JFrame("HTML Viewer");
    frame.setSize(800, 600);
    // JDK 1.2.2 has no EXIT_ON_CLOSE ..
    frame.addWindowListener(new WindowAdapter() {
      /**
       * Invoked when a window has been closed.
       **/
      public void windowClosed(WindowEvent e)
      {
        System.exit(0);
      }
    });


    JScrollPane scroll = new JScrollPane(pane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    frame.getContentPane().add(scroll);
    frame.setVisible(true);

    LibLayoutBoot.getInstance().start();
    OutputStream out = new NullOutputStream();


    long startTime = System.currentTimeMillis();

    final LayoutProcess process =
        new DefaultLayoutProcess(new StreamingHtmlOutputProcessor(out, null));


    SwingDocumentImport imprt = new SwingDocumentImport();
    imprt.parseDocument((DefaultStyledDocument) pane.getDocument(), process.getInputFeed());

    long endTime = System.currentTimeMillis();

    System.out.println("Done!: " + (endTime - startTime));
  }
}
