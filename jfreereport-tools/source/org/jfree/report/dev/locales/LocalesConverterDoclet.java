package org.jfree.report.dev.locales;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.ListResourceBundle;
import java.util.Properties;
import javax.swing.KeyStroke;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.RootDoc;
import org.jfree.util.ObjectUtilities;
import org.jfree.base.BaseBoot;

public class LocalesConverterDoclet extends Doclet
{
  public LocalesConverterDoclet ()
  {
  }

  private static Properties namesMap;

  public static boolean start (final RootDoc root)
  {
    BaseBoot.getInstance().start();

    namesMap = new Properties();
    namesMap.setProperty("JFreeReportResources", "jfreereport-resources");
    namesMap.setProperty("ConfigResources", "config-resources");
    namesMap.setProperty("ConverterResources", "converter-resources");
    namesMap.setProperty("CSVExportResources", "csv-export-resources");
    namesMap.setProperty("HtmlExportResources", "html-export-resources");
    namesMap.setProperty("PDFExportResources", "pdf-export-resources");
    namesMap.setProperty("PlainTextExportResources", "plaintext-export-resources");
    namesMap.setProperty("PrintExportResources", "print-export-resources");
    namesMap.setProperty("XLSExportResources", "xls-export-resources");
    namesMap.setProperty("DemoResources", "demo-resources");


    final String target = readTargetOptions(root.options());
    final String mergeDir = readMergeSourceOptions(root.options());
    final File targetFile = new File (target);
    final File mergeFile;
    if (mergeDir != null)
    {
      mergeFile = new File (mergeDir);
    }
    else
    {
      mergeFile = null;
    }

    final ClassDoc listResClassDoc_ = root.classNamed(ListResourceBundle.class.getName());
    final ClassDoc[] classes = root.classes();
    for (int i = 0; i < classes.length; i++)
    {
      final ClassDoc currentClass = classes[i];
      if (currentClass.subclassOf(listResClassDoc_) == false)
      {
        continue;
      }

      // try to instantiate the class ...
      final String className = currentClass.qualifiedTypeName();
      final String baseName;
      final int localesSeparator = className.lastIndexOf("_");
      if (localesSeparator != -1)
      {
        final int secondSeparator = className.lastIndexOf("_", localesSeparator - 1);
        if (secondSeparator != -1)
        {
          baseName = className.substring(0, secondSeparator);
        }
        else
        {
          baseName = className.substring(0, localesSeparator);
        }
      }
      else
      {
        baseName = null;
      }


      try
      {
        final ListResourceBundle lrb = tryLoad(className);
        if (lrb == null)
        {
          System.err.println("Error loading resource: " + className);
          continue;
        }
        final ListResourceBundle baseLrb = tryLoad(baseName);
        EditableProperties editableProperties = new EditableProperties();
        buildProperties(lrb, baseLrb, editableProperties);

        // write the property file ...
        final String packageName = getPackage(currentClass.qualifiedTypeName());
        final String simpleName = getName(currentClass.qualifiedTypeName());

        final String packagePath = packageName.replace('.', File.separatorChar);
        final File targetPath = new File(targetFile, packagePath);
        if (mergeFile != null)
        {
          final File mergePath = new File(mergeFile, packagePath);
          final String child = convertName(simpleName);
          if (child != null)
          {
            final File mergeProperties = new File (mergePath, child + ".properties");
            if (mergeProperties.exists() && mergeProperties.canRead() && mergeProperties.isFile())
            {
              // now try merge
              editableProperties = tryToMerge (editableProperties, mergeProperties);
            }
            else
            {
              System.err.println("The file '" + mergeProperties + "' is not valid.");
            }
          }
        }

        editableProperties.sort();

        targetPath.mkdirs();

        final File propertyFile = new File (targetPath, convertName(simpleName) + ".properties");
        final FileOutputStream fout = new FileOutputStream(propertyFile);
        editableProperties.write(fout);
        fout.close();
      }
      catch (Exception e)
      {
        System.err.println("Class " + className + " produced an error: " + e.toString());
      }
    }
    return true;
  }

  private static String convertName (final String className)
  {
    final String baseName;
    final String localeSpec;
    final int localesSeparator = className.lastIndexOf("_");
    if (localesSeparator != -1)
    {
      final int secondSeparator = className.lastIndexOf("_", localesSeparator - 1);
      if (secondSeparator != -1)
      {
        baseName = className.substring(0, secondSeparator);
        localeSpec = className.substring(secondSeparator);
      }
      else
      {
        baseName = className.substring(0, localesSeparator);
        localeSpec = className.substring(localesSeparator);
      }
      return namesMap.getProperty(baseName, baseName) + localeSpec;
    }
    else
    {
      return namesMap.getProperty(className, className);
    }
  }

  private static void buildProperties (final ListResourceBundle lrb,
                                       final ListResourceBundle baseLrb,
                                       final EditableProperties editableProperties)
  {
    final Enumeration keys = lrb.getKeys();
    while (keys.hasMoreElements())
    {
      final String key = (String) keys.nextElement();
      final Object o = lrb.getObject(key);
      if (baseLrb != null)
      {
        final Object baseObject = baseLrb.getObject(key);
        if (ObjectUtilities.equal(o, baseObject))
        {
          continue;
        }
      }

      if (o instanceof String)
      {
        editableProperties.setProperty(key, (String) o);
      }
      else if (o instanceof KeyStroke)
      {
        final KeyStroke keystroke = (KeyStroke) o;
        editableProperties.setProperty(key, getKeyStrokeString(keystroke));
      }
      else if (o instanceof Integer)
      {
        final Integer intVal = (Integer) o;
        editableProperties.setProperty(key, getKeyCode(intVal.intValue()));
      }
      else if (o instanceof Character)
      {
        editableProperties.setProperty(key, String.valueOf(o));
      }
    }
  }

  private static EditableProperties tryToMerge
          (final EditableProperties editableProperties,
           final File mergeProperties)
  {
    System.out.println ("Try to merge properties with " + mergeProperties);

    try
    {
      final EditableProperties ep = new EditableProperties();
      final FileInputStream fin = new FileInputStream(mergeProperties);
      ep.load(fin);
      fin.close();

      final String[] keys = editableProperties.getKeys();
      for (int i = 0; i < keys.length; i++)
      {
        final String key = keys[i];
        if (ep.containsKey(key))
        {
          ep.setProperty(key, editableProperties.getProperty(key));
        }
      }
      return ep;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    // return unmodified instance ...
    return editableProperties;
  }

  private static String readMergeSourceOptions (final String[][] options)
  {
    String targetName = null;
    for (int i = 0; i < options.length; i++)
    {
      final String[] opt = options[i];
      if (opt[0].equals("-merge"))
      {
        targetName = opt[1];
      }
    }
    return targetName;
  }

  private static ListResourceBundle tryLoad (final String baseName)
  {
    try
    {
      if (baseName == null)
      {
        return null;
      }
      return (ListResourceBundle)
              ObjectUtilities.loadAndInstantiate(baseName, LocalesConverterDoclet.class);
    }
    catch (Exception e)
    {
      return null;
    }
  }

  public static String getPackage (final String fqName)
  {
    final int dot = fqName.lastIndexOf('.');
    if (dot >= 0)
    {
      return fqName.substring(0, dot);
    }
    return fqName;
  }

  public static String getName (final String fqName)
  {
    final int dot = fqName.lastIndexOf('.');
    if (dot >= 0)
    {
      return fqName.substring(dot + 1);
    }
    return fqName;
  }

  /**
   * "<modifiers>* <key>" modifiers := shift | control | meta | alt | button1 | button2 |
   * button3 key := KeyEvent keycode name, i.e. the name following "VK_".   *
   *
   * @param k
   * @return
   */
  private static String getKeyStrokeString (final KeyStroke k)
  {
    final StringBuffer b = new StringBuffer();
    if ((k.getModifiers() & InputEvent.SHIFT_MASK) != 0)
    {
      b.append("shift ");
    }
    if ((k.getModifiers() & InputEvent.CTRL_MASK) != 0)
    {
      b.append("control ");
    }
    if ((k.getModifiers() & InputEvent.ALT_MASK) != 0)
    {
      b.append("alt ");
    }
    if ((k.getModifiers() & InputEvent.ALT_GRAPH_MASK) != 0)
    {
      b.append("altGraph ");
    }
    if ((k.getModifiers() & InputEvent.META_MASK) != 0)
    {
      b.append("meta ");
    }
    if ((k.getModifiers() & InputEvent.BUTTON1_MASK) != 0)
    {
      b.append("button1 ");
    }
    if ((k.getModifiers() & InputEvent.BUTTON2_MASK) != 0)
    {
      b.append("button2 ");
    }
    if ((k.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
    {
      b.append("button3 ");
    }

    b.append(getKeyCode(k.getKeyCode()));

    if (k.isOnKeyRelease())
    {
      b.append(" released");
    }
    return b.toString();
  }

  private static String getKeyCode (final int key)
  {
    try
    {
      final Field[] fields = KeyEvent.class.getFields();
      for (int i = 0; i < fields.length; i++)
      {
        final Field f = fields[i];
        final int modifiers = f.getModifiers();
        if (Modifier.isPublic(modifiers) &&
                Modifier.isStatic(modifiers) &&
                Modifier.isFinal(modifiers) &&
                f.getType().equals(Integer.TYPE))
        {
          final Integer keyCode = (Integer) f.get(null);
          if (key == keyCode.intValue())
          {
            return f.getName();
          }
        }
      }
    }
    catch (Exception nsfe)
    {
      // ignore ...
    }
    return ("VK_UNDEFINED");
  }

  private static String readTargetOptions (final String[][] options)
  {
    String targetName = ".";
    for (int i = 0; i < options.length; i++)
    {
      final String[] opt = options[i];
      if (opt[0].equals("-target"))
      {
        targetName = opt[1];
      }
    }
    return targetName;
  }

  public static int optionLength (final String option)
  {
    if (option.equals("-target"))
    {
      return 2;
    }
    if (option.equals("-merge"))
    {
      return 2;
    }
    return 0;
  }

  public static boolean validOptions (final String options[][],
                                      final DocErrorReporter reporter)
  {
    boolean foundTagOption = false;
    boolean foundMergeOption = false;
    for (int i = 0; i < options.length; i++)
    {
      final String[] opt = options[i];
      if (opt[0].equals("-target"))
      {
        if (foundTagOption)
        {
          reporter.printError("Only one -target option allowed.");
          return false;
        }
        else
        {
          foundTagOption = true;
        }
      }
      if (opt[0].equals("-merge"))
      {
        if (foundMergeOption)
        {
          reporter.printError("Only one -target option allowed.");
          return false;
        }
        else
        {
          foundMergeOption = true;
        }
      }
    }
    if (!foundTagOption)
    {
      reporter.printError("Usage: javadoc -target directory -doclet " +
              LocalesConverterDoclet.class.getName() + "...");
    }
    return foundTagOption;
  }

}
