package org.jfree.layouting.input.xhtml;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import org.jfree.layouting.DefaultStreamingLayoutProcess;
import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.StreamingLayoutProcess;
import org.jfree.layouting.util.NullOutputStream;
import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.output.streaming.html.HtmlOutputProcessor;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class XhtmlInputDriver
{
  private InputFeed feed;

  public XhtmlInputDriver (InputFeed feed)
  {
    this.feed = feed;
  }

  public void processDomTree (Document doc)
  {
    Element rootElement = doc.getDocumentElement();
    if (rootElement.getTagName().equalsIgnoreCase("html"))
    {
      // go the long way ...
      processFullDocument(rootElement);
    }
    else
    {
      // the short way will be enough
      feed.startDocument();
      processBodyElement(rootElement);
      feed.endDocument();
    }
  }

  private void processFullDocument (Element rootElement)
  {
    feed.startDocument();
    feed.startMetaInfo();
    NodeList headList = rootElement.getElementsByTagName("head");
    for (int i = 0; i < headList.getLength(); i++)
    {
      Element headerElement = (Element) headList.item(i);
      NodeList titles = headerElement.getElementsByTagName("title");
      for (int t = 0; t < titles.getLength(); t++)
      {
        Element title = (Element) titles.item(t);
        feed.addMetaAttribute("title", getCData(title));
      }

      NodeList metas = headerElement.getChildNodes();
      for (int t = 0; t < metas.getLength(); t++)
      {
        Node n = metas.item(t);
        if (n instanceof Element == false)
        {
          continue;
        }

        Element meta = (Element) metas.item(t);
        if (meta.getTagName().equalsIgnoreCase("title"))
        {
          continue;
        }

        feed.startMetaNode(meta.getTagName());
        NamedNodeMap nnm = meta.getAttributes();
        for (int ac = 0; ac < nnm.getLength(); ac++)
        {
          Attr attr = (Attr) nnm.item(ac);
          feed.setMetaNodeAttribute(attr.getName(), attr.getValue());
        }
        feed.setMetaNodeAttribute("#pcdata", getCData(meta));
        feed.endMetaNode();
      }
    }
    feed.endMetaInfo();

    processBodyElement(rootElement);
    feed.endDocument();
  }

  private String getCData (Element element)
  {
    final StringBuffer buffer = new StringBuffer();
    final NodeList nl = element.getChildNodes();
    for (int i = 0; i < nl.getLength(); i++)
    {
      final Node n = nl.item(i);
      if (n instanceof Text)
      {
        Text text = (Text) n;
        buffer.append(text.getData());
      }
    }
    return buffer.toString();
  }

  private void processBodyElement (Element element)
  {
    feed.startElement(element.getTagName());

    NamedNodeMap nnm = element.getAttributes();
    for (int ac = 0; ac < nnm.getLength(); ac++)
    {
      Attr attr = (Attr) nnm.item(ac);
      feed.setAttribute(attr.getName(), attr.getValue());
    }

    NodeList childs = element.getChildNodes();
    for (int t = 0; t < childs.getLength(); t++)
    {
      Node n = childs.item(t);
      if (n instanceof Element)
      {
        Element childElement = (Element) n;
        processBodyElement(childElement);
      }
      else if (n instanceof Text)
      {
        Text tx = (Text) n;
        feed.addContent(tx.getData());
      }
    }

    // process all other elements ...
    feed.endElement();
  }

  public static void main (String[] args)
          throws IOException
  {
    LibLayoutBoot.getInstance().start();

    OutputStream out = new NullOutputStream();

    URL url = new URL ("file:///home/src/jfreereport/head/liblayout/styletest/simple.html");
    XhtmlResourceFactoryModule module = new XhtmlResourceFactoryModule();
    XhtmlDocument doc = module.createDocument(url.openStream(), url, url.toExternalForm(), "text/html");

    long startTime = System.currentTimeMillis();
    for (int i = 0; i < 10; i++)
    {
      final StreamingLayoutProcess process =
              new DefaultStreamingLayoutProcess(new HtmlOutputProcessor(out));
      XhtmlInputDriver idrDriver = new XhtmlInputDriver(process.getInputFeed());
      idrDriver.processDomTree(doc.getDocument());
    }
    long endTime = System.currentTimeMillis();

    System.out.println("Done!: " + (endTime -startTime));
  }
}
