package org.jfree.layouting.input.swing;

import java.util.Enumeration;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.CSS;

import org.jfree.layouting.layouter.feed.InputFeed;

/**
 * Right now, we do not convert Swing-styles into CSS styles. Hey, we should,
 * but we don't.
 *
 * todo parse styles 
 */
public class SwingDocumentImport
{
  public static final CSS css = new CSS();

  protected void handleAttributes (final InputFeed db,
                                   final AttributeSet attr)
  {
    Enumeration attributeNames = attr.getAttributeNames();
    while (attributeNames.hasMoreElements())
    {
      Object name = attributeNames.nextElement();
      if ((name instanceof StyleConstants.ParagraphConstants) ||
              (name instanceof StyleConstants.CharacterConstants) ||
              (name instanceof StyleConstants.FontConstants) ||
              (name instanceof StyleConstants.ColorConstants))
      {
        // convert from Swing-Constant to CSS
      }
    }
  }


  private void handleElement (Element element,
                                     InputFeed db)
          throws BadLocationException
  {
    if (element.isLeaf())
    {
      Document document = element.getDocument();
      String text = document.getText
              (element.getStartOffset(),
                      element.getEndOffset() - element.getStartOffset());
      db.addContent(text);
      return;
    }

    db.startElement(element.getName());
    final AttributeSet as = element.getAttributes();
    final Enumeration ase = as.getAttributeNames();
    while (ase.hasMoreElements())
    {
      Object key = ase.nextElement();
      final Object value = as.getAttribute(key);
      System.out.println(key + " = " + value);
    }

    final int size = element.getElementCount();
    for (int i = 0; i < size; i++)
    {
      final Element e = element.getElement(i);
      handleElement(e, db);
    }

    db.endElement();
  }

  private void handleRootElement (Element rootElement,
                                         InputFeed db)
          throws BadLocationException
  {
    final int size = rootElement.getElementCount();
    for (int i = 0; i < size; i++)
    {
      final Element e = rootElement.getElement(i);
      if (e.getName().equals("head"))
      {
        handleHeadElement(e, db);
      }
    }
    for (int i = 0; i < size; i++)
    {
      final Element e = rootElement.getElement(i);
      if (e.getName().equals("body"))
      {
        final int bodySize = e.getElementCount();
        for (int j = 0; j < bodySize; j++)
        {
          handleElement(e.getElement(j), db);
        }
      }
    }

  }

  private void handleHeadElement (Element element, InputFeed db)
  {
    // todo implement me
    final Document doc = element.getDocument();
    if (doc instanceof DefaultStyledDocument == false)
    {
      // nothing to do for us ..
      return;
    }
    final DefaultStyledDocument sdoc = (DefaultStyledDocument) doc;
    final Enumeration names = sdoc.getStyleNames();
    while (names.hasMoreElements())
    {
      String styleName = (String) names.nextElement();
      Style s = sdoc.getStyle(styleName);
      if (s.getAttributeCount() <= 1 &&
              s.isDefined(StyleConstants.NameAttribute))
      {
        continue;
      }

      // now add the style to the document-builder
      // do this by converting the styles into a string,
      // and then by reparsing the string into a CSS-Dom

      // yes, it is ugly, yes, it is slow, but yes,
      // it is standard and will be supported on all JDKs
      // at no extra costs..
    }

  }

  public void parseDocument (Document doc, InputFeed feed)
          throws BadLocationException
  {
    handleRootElement(doc.getDefaultRootElement(), feed);
  }
}
