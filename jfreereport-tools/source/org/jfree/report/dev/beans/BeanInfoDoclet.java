package org.jfree.report.dev.beans;

import java.beans.FeatureDescriptor;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;
import org.jfree.report.dev.beans.dochandler.BeanClassDocletHandler;
import org.jfree.report.dev.beans.templates.TemplateParserFrontend;
import org.jfree.report.dev.beans.templates.Template;
import org.jfree.report.dev.beans.templates.Context;

public class BeanInfoDoclet extends Doclet
{
  private static final String GET_PREFIX = "get";

  public BeanInfoDoclet()
  {
  }

  public static boolean start(RootDoc root)
  {
    TemplateParserFrontend tpf = new TemplateParserFrontend();
    ClassDoc[] classes = root.classes();
    for (int i = 0; i < classes.length; i++)
    {
      ClassDoc cd = classes[i];

      // no inner classes ...
      if (cd.containingClass() != null)
      {
        continue;
      }
      // only if explicitly enabled ...
      if (getBooleanTagContent(cd, "generatedoc", false) == false)
      {
        continue;
      }

      BeanClassDocletHandler handler = new BeanClassDocletHandler();
      handler.setClassDoc(cd);

      // let's go
      //buildEventSetDescriptors(generatorBeanInfo, cd);

      // write content ...
      PrintWriter writer = new PrintWriter(System.out);
      try
      {
        Context context = new Context();
        context.put(null, handler.getBeanInfo());
        context.put("beaninfo", handler.getBeanInfo());
        Template template = tpf.performParsing();
        template.print(writer, context);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      writer.flush();
    }
    return true;
  }


  public static String buildFirstSentence (Tag[] tags)
  {
    if (tags.length == 0)
    {
      return null;
    }
    StringBuffer b = new StringBuffer();
    for (int i = 0; i < tags.length; i++)
    {
      b.append(tags[i].toString());
      b.append(" ");
    }
    return b.toString();
  }

  public static void fillFeatureDescriptor(FeatureDescriptor fd,
                                     Doc docElement)
  {
    String name = getTagContent(docElement, "name", null);
    if (name != null)
    {
      fd.setName(name);
    }
    String displayName = getTagContent(docElement, "displayname", null);
    if (displayName != null)
    {
      fd.setDisplayName(displayName);
    }
    String shortDescr = getTagContent(docElement, "shortDescription", null);
    if (shortDescr != null)
    {
      fd.setShortDescription(shortDescr);
    }
    fd.setExpert(getBooleanTagContent(docElement, "expert", false));
    fd.setHidden(getBooleanTagContent(docElement, "hidden", false));
    fd.setPreferred(getBooleanTagContent(docElement, "preferred", false));
  }

  /**
   *
   * @param docElement
   * @param tagName
   * @param defaultValue
   * @return
   */
  public static String getTagContent(Doc docElement, String tagName, String defaultValue)
  {
    Tag[] tags = docElement.tags(tagName);

    for (int i = 0; i < tags.length; i++)
    {
      String txt = tags[i].text();
      if (txt == null)
      {
        System.err.println(docElement.position() + ": Empty tag content for tag '" + tagName + "'");
        continue;
      }
      String trimmedText = txt.trim();
      if (trimmedText.length() == 0)
      {
        System.err.println(docElement.position() + ": Empty tag content for tag '" + tagName + "'");
        continue;
      }
      return trimmedText;
    }

    return defaultValue;
  }

  public static boolean getBooleanTagContent(Doc docElement, String tagName, boolean defaultValue)
  {
    Tag[] tags = docElement.tags(tagName);

    for (int i = 0; i < tags.length; i++)
    {
      String txt = tags[i].text();
      if (txt == null)
      {
        System.err.println(docElement.position() + ": Empty tag content for tag '" + tagName + "'");
        continue;
      }
      String trimmedText = txt.trim();
      if (trimmedText.length() == 0)
      {
        System.err.println(docElement.position() + ": Empty tag content for tag '" + tagName + "'");
        continue;
      }
      if (trimmedText.equals("true"))
      {
        return true;
      }
      if (trimmedText.equals("false"))
      {
        return false;
      }
      System.err.println(docElement.position() + ": Invalid tag content for tag '" + tagName + "'");
    }
    return defaultValue;
  }

  public static String getName(String fqName)
  {
    String name = fqName;
    while (name.indexOf('.') >= 0)
    {
      name = name.substring(name.indexOf('.') + 1);
    }
    return name;
  }

  public static String[] parseTag(Tag t)
  {
    StringTokenizer strtok = new StringTokenizer(t.text());
    int size = strtok.countTokens();
    String[] tokens = new String[size];
    for (int i = 0; i < size; i++)
    {
      tokens[i] = strtok.nextToken();
    }
    return tokens;
  }


}
