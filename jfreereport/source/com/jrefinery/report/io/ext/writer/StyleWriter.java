/**
 * Date: Jan 13, 2003
 * Time: 6:40:10 PM
 *
 * $Id: StyleWriter.java,v 1.1 2003/01/13 21:39:22 taqua Exp $
 */
package com.jrefinery.report.io.ext.writer;

import com.jrefinery.report.targets.style.ElementStyleSheet;
import com.jrefinery.report.targets.style.StyleKey;
import com.jrefinery.report.io.ext.StylesHandler;
import com.jrefinery.report.io.ext.StyleSheetHandler;
import com.jrefinery.report.io.ext.factory.objects.ClassFactoryCollector;
import com.jrefinery.report.io.ext.factory.objects.ObjectDescription;
import com.jrefinery.report.io.ext.factory.objects.ObjectFactoryException;

import java.io.Writer;
import java.io.IOException;
import java.util.List;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

public class StyleWriter extends AbstractXMLDefinitionWriter
{
  private ElementStyleSheet elementStyleSheet;

  public StyleWriter(ReportWriter reportWriter, ElementStyleSheet elementStyleSheet)
  {
    super(reportWriter);
    this.elementStyleSheet = elementStyleSheet;
  }

  public void write(Writer writer) throws IOException, ReportWriterException
  {
    List parents = elementStyleSheet.getParents();
    for (int p = 0; p < parents.size(); p++)
    {
      ElementStyleSheet parent = (ElementStyleSheet) parents.get(p);
      writeTag(writer, StyleSheetHandler.EXTENDS_TAG, "name", parent.getName(), CLOSE);
    }

    Enumeration keys = elementStyleSheet.getDefinedPropertyNames();
    while (keys.hasMoreElements())
    {
      StyleKey key = (StyleKey) keys.nextElement();
      Object value = elementStyleSheet.getStyleProperty(key);
      if (value != null)
      {
        writeKeyValue(writer, key, value);
      }
    }
  }

  private void writeKeyValue (Writer w, StyleKey key, Object o)
    throws IOException, ReportWriterException
  {
    ClassFactoryCollector cc = getReportWriter().getClassFactoryCollector();
    ObjectDescription od = cc.getDescriptionForClass(key.getValueType());
    if (od == null)
    {
      od = cc.getDescriptionForClass(o.getClass());
      if (od == null)
      {
        throw new ReportWriterException("Unable to find object description for key: "+ key.getName());
      }
    }

    try
    {
      od.setParameterFromObject(o);
    }
    catch (ObjectFactoryException e)
    {
      throw new ReportWriterException ("Unable to fill the parameters.", e);
    }

    Properties p = new Properties();
    p.setProperty("name", key.getName());
    if ((key.getValueType().equals(o.getClass())) == false)
    {
      p.setProperty("class", o.getClass().getName());
    }

    List parameterNames = getParameterNames(od);
    if (isBasicKey(parameterNames, od))
    {
      writeTag(w, StyleSheetHandler.BASIC_KEY_TAG, p, OPEN);
      w.write(normalize((String) od.getParameter("value")));
      writeCloseTag(w, StyleSheetHandler.BASIC_KEY_TAG);
    }
    else
    {
      for (int i = 0; i < parameterNames.size(); i++)
      {
        String parameterName = (String) parameterNames.get(i);
        Object object = od.getParameter(parameterName);
        if (object == null)
        {
          throw new NullPointerException("ParameterObject for " + parameterName + " is null");
        }
        ObjectDescription subObjectDesc =
            cc.getDescriptionForClass(od.getParameterDefinition(parameterName));
        if (subObjectDesc == null)
          throw new ReportWriterException("No object description");

        writeTag(w, StyleSheetHandler.COMPOUND_KEY_TAG, p, OPEN);
        ObjectWriter objWriter =
            new ObjectWriter(getReportWriter(), object, subObjectDesc );

        objWriter.write(w);

        writeCloseTag(w, StyleSheetHandler.COMPOUND_KEY_TAG);
      }
    }
  }

  private boolean isBasicKey(List parameters, ObjectDescription od)
  {
    if (parameters.size() == 1)
    {
      String param = (String) parameters.get(0);
      if (param.equals("value"))
      {
        if (od.getParameterDefinition("value").equals(String.class))
        {
          return true;
        }
      }
    }
    return false;
  }

  private ArrayList getParameterNames (ObjectDescription d)
  {
    ArrayList list = new ArrayList();
    Iterator it = d.getParameterNames();
    while (it.hasNext())
    {
      String name = (String) it.next();
      list.add(name);
    }
    return list;
  }
}
