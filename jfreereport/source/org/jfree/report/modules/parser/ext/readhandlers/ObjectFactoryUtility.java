package org.jfree.report.modules.parser.ext.readhandlers;

import org.jfree.xml.ElementDefinitionException;
import org.jfree.xml.factory.objects.ClassFactory;
import org.jfree.xml.factory.objects.ObjectDescription;

public class ObjectFactoryUtility
{
  private ObjectFactoryUtility ()
  {
  }

  public static ObjectDescription findDescription (final ClassFactory cf,
                                                   final Class c)
          throws ElementDefinitionException
  {
    final ObjectDescription directMatch = cf.getDescriptionForClass(c);
    if (directMatch != null)
    {
      return directMatch;
    }
    final ObjectDescription indirectMatch = cf.getSuperClassObjectDescription(c, null);
    if (indirectMatch != null)
    {
      return indirectMatch;
    }
    throw new ElementDefinitionException
            ("No object description found for '" + c + "'");
  }
}
