package org.jfree.report.dev.beans.dochandler;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.Tag;
import org.jfree.report.dev.beans.BeanInfoDoclet;
import org.jfree.report.dev.beans.GeneratorIndexedPropertyDescriptor;
import org.jfree.report.dev.beans.GeneratorPropertyDescriptor;

public class PropertiesHandler
{
  private ClassDoc classDoc;
  private HashMap readMethods;
  private HashMap writeMethods;
  private HashMap indexReadMethods;
  private HashMap indexWriteMethods;
  private TreeSet properties;
  private TreeSet boundProperties;
  private TreeSet constrainedProperties;

  private TreeSet expertProperties;
  private TreeSet hiddenProperties;
  private TreeSet preferredProperties;
  private HashMap displayNames;
  private HashMap shortDescriptions;

  private static final int SIMPLE_GETTER = 0;
  private static final int SIMPLE_SETTER = 1;
  private static final int INDEXED_GETTER = 2;
  private static final int INDEXED_SETTER = 3;
  private GeneratorPropertyDescriptor[] propertyDescriptors;

  public PropertiesHandler(ClassDoc classDoc)
  {
    this.classDoc = classDoc;
    readMethods = new HashMap();
    writeMethods = new HashMap();
    indexReadMethods = new HashMap();
    indexWriteMethods = new HashMap();
    properties = new TreeSet();
    boundProperties = new TreeSet();
    constrainedProperties = new TreeSet();

    expertProperties = new TreeSet();
    hiddenProperties = new TreeSet();
    preferredProperties = new TreeSet();
    shortDescriptions = new HashMap();
    displayNames = new HashMap();

    extractMethods();

    ArrayList propertyDescriptors = new ArrayList();
    Iterator it = properties.iterator();

    while (it.hasNext())
    {
      String propertyName = (String) it.next();
      MethodDoc readMethod = (MethodDoc) readMethods.get(propertyName);
      MethodDoc writeMethod = (MethodDoc) writeMethods.get(propertyName);
      MethodDoc idxReadMethod = (MethodDoc) indexReadMethods.get(propertyName);
      MethodDoc idxWriteMethod = (MethodDoc) indexWriteMethods.get(propertyName);

      GeneratorPropertyDescriptor gpsd;
      if (idxReadMethod != null || idxWriteMethod != null)
      {
        GeneratorIndexedPropertyDescriptor gipsd = new GeneratorIndexedPropertyDescriptor();
        if (idxReadMethod != null)
        {
          gipsd.setIndexedReadMethod(idxReadMethod.name());
        }
        if (idxWriteMethod != null)
        {
          gipsd.setIndexedWriteMethod(idxWriteMethod.name());
        }
        gpsd = gipsd;
      }
      else
      {
        gpsd = new GeneratorPropertyDescriptor();
      }

      gpsd.setName(propertyName);
      gpsd.setBound(boundProperties.contains(propertyName));
      gpsd.setConstrained(constrainedProperties.contains(propertyName));
      gpsd.setConstrained(hiddenProperties.contains(propertyName));
      gpsd.setConstrained(preferredProperties.contains(propertyName));
      gpsd.setConstrained(expertProperties.contains(propertyName));

      gpsd.setDisplayName((String) displayNames.get(propertyName));
      gpsd.setShortDescription((String) shortDescriptions.get(propertyName));
      if (readMethod != null)
      {
        gpsd.setReadMethod(readMethod.name());
      }
      if (writeMethod != null)
      {
        gpsd.setWriteMethod(writeMethod.name());
      }
      propertyDescriptors.add(gpsd);
    }
    this.propertyDescriptors = (GeneratorPropertyDescriptor[])
        propertyDescriptors.toArray(new GeneratorPropertyDescriptor[propertyDescriptors.size()]);
  }

  public GeneratorPropertyDescriptor[] getPropertyDescriptors()
  {
    return propertyDescriptors;
  }

  private void extractMethods()
  {
    MethodDoc[] methodDocs = classDoc.methods();
    // search for event methods (add*, remove*, get*)
    for (int i = 0; i < methodDocs.length; i++)
    {
      MethodDoc methodDoc = methodDocs[i];
      final int accessorType = getAccessorType(methodDoc);
      if (accessorType == -1)
      {
        System.out.println ("No Valid PropertyAccessor: " + methodDoc);
        continue;
      }
      System.out.println ("PropertyAccessor: " + methodDoc);
      String propertyName = getPropertyName(methodDoc);
      properties.add(propertyName);
      switch(accessorType)
      {
        case SIMPLE_GETTER:
          {
            readMethods.put(propertyName, methodDoc);
            break;
          }
        case SIMPLE_SETTER:
          {
            writeMethods.put(propertyName, methodDoc);
            break;
          }
        case INDEXED_GETTER:
          {
            indexReadMethods.put(propertyName, methodDoc);
            break;
          }
        case INDEXED_SETTER:
          {
            indexWriteMethods.put(propertyName, methodDoc);
            break;
          }
      }

      updateFlags(methodDoc, propertyName);
    }
  }

  private void updateFlags (MethodDoc methodDoc, String propertyName)
  {
    if (methodDoc.tags("expert").length != 0)
    {
      expertProperties.add(propertyName);
    }
    if (methodDoc.tags("hidden").length != 0)
    {
      expertProperties.add(propertyName);
    }
    if (methodDoc.tags("preferred").length != 0)
    {
      expertProperties.add(propertyName);
    }
    if (methodDoc.tags("bound").length != 0)
    {
      expertProperties.add(propertyName);
    }
    if (methodDoc.tags("constrained").length != 0)
    {
      expertProperties.add(propertyName);
    }
    final Tag[] shortDescrTags = methodDoc.tags("shortDescription");
    if (shortDescrTags.length != 0)
    {
      shortDescriptions.put(propertyName, shortDescrTags[0].text());
    }

    final Tag[] displayNameTags = methodDoc.tags("displayName");
    if (displayNameTags.length != 0)
    {
      displayNames.put(propertyName, displayNameTags[0].text());
    }
  }

  private static int getAccessorType(MethodDoc method)
  {
    if (method.isPublic() == false || method.isStatic())
    {
      return -1;
    }

    boolean readMethod = false;
    boolean writeMethod = false;
    Tag[] tags = method.tags("property");
    for (int i = 0; i < tags.length; i++)
    {
      String[] tagContent = BeanInfoDoclet.parseTag(tags[i]);
      if (tagContent.length == 2)
      {
        if (tagContent[0].equals("read"))
        {
          readMethod = true;
        }
        if (tagContent[0].equals("write"))
        {
          writeMethod = true;
        }
      }
    }

    Parameter[] parameters = method.parameters();
    if (readMethod ||
        method.name().startsWith("get") ||
        method.name().startsWith("is"))
    {
      if (parameters.length == 0)
      {
        return SIMPLE_GETTER;
      }
      else if (parameters.length == 1)
      {
        // need an int value as index parameter
        if (parameters[0].type().asClassDoc() != null)
        {
          return -1;
        }
        if (parameters[0].typeName().equals("int") == false)
        {
          return -1;
        }

        return INDEXED_GETTER;
      }
    }

    if (writeMethod || method.name().startsWith("set"))
    {
      if (parameters.length == 1)
      {
        return SIMPLE_SETTER;
      }
      else if (parameters.length == 2)
      {
        // need an int value as index parameter
        if (parameters[0].type().asClassDoc() != null)
        {
          return -1;
        }
        if (parameters[0].typeName().equals("int") == false)
        {
          return -1;
        }
        return INDEXED_SETTER;
      }
    }
    return -1;
  }

  private String getPropertyName(MethodDoc md)
  {
    Tag[] tags = md.tags("property");
    for (int i = 0; i < tags.length; i++)
    {
      String[] tagContent = BeanInfoDoclet.parseTag(tags[i]);
      if (tagContent.length == 2)
      {
        if (tagContent[0].equals("read"))
        {
          return tagContent[1];
        }
        if (tagContent[0].equals("write"))
        {
          return tagContent[1];
        }
      }
    }

    final String methodName = md.name();
    if (methodName.startsWith("get") ||
        methodName.startsWith("set"))
    {
      return Introspector.decapitalize(methodName.substring(3));
    }
    if (methodName.startsWith("is"))
    {
      return Introspector.decapitalize(methodName.substring(2));
    }
    return null;
  }
}
