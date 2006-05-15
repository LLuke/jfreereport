package org.jfree.layouting.output.streaming.oowriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Stack;

import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.normalizer.ContentGenerator;
import org.jfree.layouting.normalizer.common.display.ContentBox;
import org.jfree.layouting.normalizer.common.display.ContentNode;
import org.jfree.layouting.normalizer.common.display.ContentText;
import org.jfree.xmlns.common.AttributeList;
import org.jfree.xmlns.writer.XmlWriter;

public class OOWriterContentGenerator implements ContentGenerator
{
  private ContentBox lastBox;
  private Stack flows;
  private XmlWriter xmlWriter;
  private static final String OFFICE_NS = "urn:oasis:names:tc:opendocument:xmlns:office:1.0";

  public OOWriterContentGenerator (final OutputStream outstream)
          throws UnsupportedEncodingException
  {
    this.flows = new Stack();
    this.xmlWriter = new XmlWriter(new OutputStreamWriter(outstream, "UTF-8"));

    this.xmlWriter.addNamespace(OFFICE_NS, "office");
    this.xmlWriter.addNamespace
            ("urn:oasis:names:tc:opendocument:xmlns:style:1.0", "style");
    this.xmlWriter.addNamespace
            ("urn:oasis:names:tc:opendocument:xmlns:text:1.0", "text");
    this.xmlWriter.addNamespace
            ("urn:oasis:names:tc:opendocument:xmlns:table:1.0", "table");
    this.xmlWriter.addNamespace
            ("urn:oasis:names:tc:opendocument:xmlns:drawing:1.0", "draw");
    this.xmlWriter.addNamespace
            ("urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0", "fo");
    this.xmlWriter.addNamespace("http://www.w3.org/1999/xlink", "xlink");
    this.xmlWriter.addNamespace("http://purl.org/dc/elements/1.1/", "dc");
    this.xmlWriter.addNamespace("urn:oasis:names:tc:opendocument:xmlns:meta:1.0", "meta");
    this.xmlWriter.addNamespace
            ("urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0", "number");
    this.xmlWriter.addNamespace
            ("urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0", "svg");
    this.xmlWriter.addNamespace
            ("urn:oasis:names:tc:opendocument:xmlns:chart:1.0", "chart");
    this.xmlWriter.addNamespace("urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0", "dr3d");
    this.xmlWriter.addNamespace("http://www.w3.org/1998/Math/MathML", "math");
    this.xmlWriter.addNamespace("urn:oasis:names:tc:opendocument:xmlns:form:1.0", "form");
    this.xmlWriter.addNamespace
            ("urn:oasis:names:tc:opendocument:xmlns:script:1.0", "script");
    this.xmlWriter.addNamespace("http://openoffice.org/2004/office", "ooo");
    this.xmlWriter.addNamespace("http://openoffice.org/2004/writer", "ooow");
    this.xmlWriter.addNamespace("http://openoffice.org/2004/calc", "oooc");
    this.xmlWriter.addNamespace("http://www.w3.org/2001/xml-events", "dom");
    this.xmlWriter.addNamespace("http://www.w3.org/2002/xforms", "xforms");
    this.xmlWriter.addNamespace("http://www.w3.org/2001/XMLSchema", "xsd");
    this.xmlWriter.addNamespace("http://www.w3.org/2001/XMLSchema-instance", "xsi");
  }

  public void documentStarted ()
  {
    try
    {
      this.xmlWriter.writeXmlDeclaration("UTF-8");
      this.xmlWriter
              .writeTag(OFFICE_NS, "document-content", "version", "1.0", XmlWriter.OPEN);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Receives notification, that a new flow has started. A new flow is started for each
   * flowing or absolutly positioned element.
   *
   * @param box
   */
  public void flowStarted (ContentBox box)
  {
    try
    {
      this.xmlWriter.writeTag(null, "flow", XmlWriter.OPEN);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
//    flows.push(lastBox);
//    lastBox = null;
//    try
//    {
//      xmlWriter.writeTag(null, "Flow", XmlWriter.OPEN);
//    }
//    catch (IOException e)
//    {
//      e.printStackTrace();
//    }
  }

  public void nodeStarted (ContentBox box)
  {
    try
    {
      AttributeList al = new AttributeList();
      al.setAttribute(null, "display-role", String.valueOf(box.getDisplayRole()));
      al.setAttribute(null, "id", String.valueOf(System.identityHashCode(box)));
      al.setAttribute(null, "type", String.valueOf(box.getLayoutElement()));
      this.xmlWriter.writeTag(null, "box", al, XmlWriter.OPEN);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
//
//  private ContentBox getBlockContext (ContentNode node)
//  {
//    if (node == null)
//    {
//      return null;
//    }
//    if (node instanceof ContentBox)
//    {
//      ContentBox box = (ContentBox) node;
//      if (box.getDisplayRole() != DisplayRole.INLINE)
//      {
//        return box;
//      }
//    }
//    return getBlockContext(node.getParent());
//  }

  public void nodeProcessable (ContentNode node)
  {
    try
    {
      this.xmlWriter.writeTag(null, "processable", XmlWriter.CLOSE);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
//    try
//    {
//      final ContentBox parent = getBlockContext(node);
//
//      if (this.lastBox != parent)
//      {
//        if (lastBox != null)
//        {
//          this.xmlWriter.writeCloseTag();
//          this.lastBox = null;
//        }
//
//        AttributeList al = new AttributeList();
//        al.setAttribute(null, "display-role", String.valueOf(parent.getDisplayRole()));
//        al.setAttribute(null, "id", String.valueOf(System.identityHashCode(parent)));
//        al.setAttribute(null, "id-parent", String.valueOf(System.identityHashCode(lastBox)));
//        this.xmlWriter.writeTag
//                (null, "paragraph", al, XmlWriter.OPEN);
//      }
//      if (node instanceof ContentText)
//      {
//        ContentText ct = (ContentText) node;
//        this.xmlWriter.writeText(ct.getText());
//      }
//      else
//      {
//        this.xmlWriter.writeText(String.valueOf(node));
//      }
//      this.lastBox = parent;
//    }
//    catch (IOException e)
//    {
//      e.printStackTrace();
//    }
  }

  public void nodeFinished (ContentBox box)
  {
    try
    {
      this.xmlWriter.writeComment("node finished");
      this.xmlWriter.writeCloseTag();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public void flowFinished (ContentBox box)
  {
//    try
//    {
//      if (lastBox != null)
//      {
//        this.xmlWriter.writeCloseTag();
//        this.lastBox = null;
//      }
//
//      this.xmlWriter.writeComment(String.valueOf(box));
//      this.xmlWriter.writeCloseTag();
//    }
//    catch (IOException e)
//    {
//      e.printStackTrace();
//    }
//    lastBox = (ContentBox) flows.pop();
    try
    {
      this.xmlWriter.writeComment("flow finished");
      this.xmlWriter.writeCloseTag();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public void documentFinished ()
  {
    try
    {
//      if (lastBox != null)
//      {
//        this.xmlWriter.writeCloseTag();
//        this.lastBox = null;
//      }
//
      xmlWriter.writeCloseTag();
      xmlWriter.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

}
