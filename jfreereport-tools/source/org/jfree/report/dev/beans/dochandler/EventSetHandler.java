package org.jfree.report.dev.beans.dochandler;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.Tag;
import org.jfree.report.dev.beans.BeanInfoDoclet;
import org.jfree.report.dev.beans.GeneratorEventSetDescriptor;

public class EventSetHandler
{
  private static final String ADD_PREFIX = "add";
  private static final String REMOVE_PREFIX = "remove";
  private static final String LISTENER_SUFFIX = "Listener";

  private HashMap addMethods;
  private HashMap removeMethods;
  private TreeSet properties;

  private TreeSet expertProperties;
  private TreeSet hiddenProperties;
  private TreeSet preferredProperties;

  private GeneratorEventSetDescriptor[] eventSetDescriptors;
  private int defaultIndex;
  private String defaultEvent;
  private HashMap shortDescriptions;
  private HashMap displayNames;

  public EventSetHandler (ClassDoc cd)
  {
    defaultIndex = -1;
    defaultEvent = null;
    parseClassDefinitionEvents(cd);

    properties = new TreeSet();
    expertProperties = new TreeSet();
    hiddenProperties = new TreeSet();
    preferredProperties = new TreeSet();

    addMethods = new HashMap();
    removeMethods = new HashMap();
    shortDescriptions = new HashMap();
    displayNames = new HashMap();

    extractMethods(cd);

    ArrayList eventDescriptors = new ArrayList();
    Iterator it = properties.iterator();

    while (it.hasNext())
    {
      String eventName = (String) it.next();
      MethodDoc addMethod = (MethodDoc) addMethods.get(eventName);
      MethodDoc removeMethod = (MethodDoc) removeMethods.get(eventName);
      if (addMethod == null || removeMethod == null)
      {
        System.err.println ("No add/remove method for event " + eventName +
            " add: " + addMethod + "; remove: " + removeMethod);
        continue;
      }
      Parameter listenerParam = addMethod.parameters()[0];
      ClassDoc listener = listenerParam.type().asClassDoc();
      String[] listenerMethods = getEventMethods(listener);

      GeneratorEventSetDescriptor gesd = new GeneratorEventSetDescriptor();
      gesd.setAddMethod(addMethod.name());
      gesd.setRemoveMethod(removeMethod.name());
      gesd.setName(eventName);
      gesd.setListenerMethods(listenerMethods);
      gesd.setListenerType(listener.qualifiedTypeName());
      gesd.setExpert(expertProperties.contains(eventName));
      gesd.setHidden(hiddenProperties.contains(eventName));
      gesd.setPreferred(preferredProperties.contains(eventName));
      gesd.setDisplayName((String) displayNames.get(eventName));
      gesd.setShortDescription((String) shortDescriptions.get(eventName));
      eventDescriptors.add (gesd);

      if (defaultIndex != -1)
      {
        continue;
      }

      if (eventName.equals(defaultEvent))
      {
        defaultIndex = eventDescriptors.size() - 1;
      }
    }
    eventSetDescriptors = (GeneratorEventSetDescriptor[])
        eventDescriptors.toArray(new GeneratorEventSetDescriptor[eventDescriptors.size()]);
  }

  private void parseClassDefinitionEvents(ClassDoc cd)
  {
    Tag[] eventTags = cd.tags("defaultevent");
    for (int i = 0; i < eventTags.length; i++)
    {
      String[] tagContent = BeanInfoDoclet.parseTag(eventTags[i]);
      if (tagContent.length > 0)
      {
        defaultEvent = tagContent[0];
      }
    }
  }

  public int getDefaultIndex()
  {
    return defaultIndex;
  }

  public GeneratorEventSetDescriptor[] getEventSetDescriptors()
  {
    return eventSetDescriptors;
  }

  private void extractMethods(ClassDoc cd)
  {
    MethodDoc[] methodDocs = cd.methods();
    // search for event methods (add*, remove*, get*)
    for (int i = 0; i < methodDocs.length; i++)
    {
      MethodDoc methodDoc = methodDocs[i];
      if (isEventSetMethod(methodDoc) == false)
      {
        continue;
      }

      String name = methodDoc.name();
      if (name.startsWith(ADD_PREFIX))
      {
        String eventName = Introspector.decapitalize
            (name.substring(ADD_PREFIX.length(), name.length() - LISTENER_SUFFIX.length()));
        addMethods.put (eventName, methodDoc);
        properties.add(eventName);
        updateFlags(methodDoc, eventName);
      }
      else if (name.startsWith(REMOVE_PREFIX))
      {
        String eventName = Introspector.decapitalize
            (name.substring(REMOVE_PREFIX.length(), name.length() - LISTENER_SUFFIX.length()));
        removeMethods.put (eventName, methodDoc);
        properties.add(eventName);
        updateFlags(methodDoc, eventName);
      }
      else
      {
        continue;
      }
      //
    }

    // second pass: search for specified listeners ..
    // these listeners replace any automaticly found listener spec

    // search for event methods (add*, remove*, get*)
    for (int i = 0; i < methodDocs.length; i++)
    {
      MethodDoc methodDoc = methodDocs[i];
      if (isEventSetMethod(methodDoc) == false)
      {
        continue;
      }

      Tag[] tags = methodDoc.tags("event");
      for (int ti = 0; ti < tags.length; ti++)
      {
        String[] parsedTags = BeanInfoDoclet.parseTag(tags[ti]);
        if (parsedTags.length != 2 && parsedTags.length != 3)
        {
          System.err.println(tags[ti].position() + ": Invalid number of arguments for tag @event");
          continue;
        }
        String type = parsedTags[0];
        String eventName = parsedTags[1];
        if (parsedTags.length == 3 && parsedTags[2].equals("default"))
        {
          if (defaultEvent != null)
          {
            defaultEvent = eventName;
          }
          else
          {
            System.err.println(tags[ti].position() +
                ": default event for event type '" + eventName + "' already specified.");
          }
        }
        if (type.equals("add"))
        {
          addMethods.put (eventName, methodDoc);
          properties.add(eventName);
          updateFlags(methodDoc, eventName);
        }
        else if (type.equals("remove"))
        {
          removeMethods.put (eventName, methodDoc);
          properties.add(eventName);
          updateFlags(methodDoc, eventName);
        }
        else if (type.equals("disable"))
        {
          properties.remove(eventName);
          removeMethods.remove(eventName);
          addMethods.remove(eventName);
        }
      }
    }

  }

  private void updateFlags (MethodDoc methodDoc, String eventName)
  {
    if (methodDoc.tags("expert").length != 0)
    {
      expertProperties.add(eventName);
    }
    if (methodDoc.tags("hidden").length != 0)
    {
      expertProperties.add(eventName);
    }
    if (methodDoc.tags("preferred").length != 0)
    {
      expertProperties.add(eventName);
    }
    final Tag[] shortDescrTags = methodDoc.tags("shortDescription");
    if (shortDescrTags.length != 0)
    {
      shortDescriptions.put(eventName, shortDescrTags[0].text());
    }

    final Tag[] displayNameTags = methodDoc.tags("displayName");
    if (displayNameTags.length != 0)
    {
      displayNames.put(eventName, displayNameTags[0].text());
    }
  }



  private static String[] getEventMethods (ClassDoc listener)
  {
    MethodDoc[] methods = listener.methods();
    ArrayList listenerMethods = new ArrayList();
    for (int i = 0; i < methods.length; i++)
    {
      Parameter[] params = methods[i].parameters();
      if (params.length != 1)
      {
        continue;
      }
      ClassDoc classDoc = params[0].type().asClassDoc();
      if (classDoc == null)
      {
        continue;
      }
      if (classDoc.subclassOf(classDoc.findClass(EventObject.class.getName())))
      {
        listenerMethods.add (methods[i].name());
      }
    }
    return (String[]) listenerMethods.toArray(new String[listenerMethods.size()]);
  }

  private static boolean isEventSetMethod(MethodDoc method)
  {
    Parameter[] parameters = method.parameters();
    if (parameters.length != 1)
    {
      return false;
    }
    ClassDoc param = parameters[0].type().asClassDoc();
    if (param == null)
    {
      return false;
    }
    if (parameters[0].type().dimension().equals("") == false)
    {
      return false;
    }
    if (param.subclassOf(param.findClass(EventListener.class.getName())) == false)
    {
      return false;
    }
    if (param.name().endsWith(LISTENER_SUFFIX))
    {
      return true;
    }
    return false;
  }


}
