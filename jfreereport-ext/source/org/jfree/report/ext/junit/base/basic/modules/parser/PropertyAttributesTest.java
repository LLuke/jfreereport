package org.jfree.report.ext.junit.base.basic.modules.parser;

import junit.framework.TestCase;
import org.jfree.xml.parser.RootXmlReadHandler;
import org.jfree.report.modules.parser.base.ReportParser;
import org.jfree.report.util.PropertyLookupParser;

public class PropertyAttributesTest extends TestCase
{
  private class StringLookupParser extends PropertyLookupParser
  {
    private RootXmlReadHandler rootXmlHandler;

    public StringLookupParser (final RootXmlReadHandler rootXmlHandler)
    {
      this.rootXmlHandler = rootXmlHandler;
    }

    protected Object performInitialLookup (final String name)
    {
      return rootXmlHandler.getHelperObject(name);
    }
  }


  public PropertyAttributesTest (final String s)
  {
    super(s);
  }

  public void testSimple ()
  {
    final RootXmlReadHandler rootXmlReadHandler = new ReportParser();
    rootXmlReadHandler.setHelperObject("property", "ARRGH");
    rootXmlReadHandler.setHelperObject("property2", "..");

    final StringLookupParser parser = new StringLookupParser(rootXmlReadHandler);
    final String result =
            parser.translateAndLookup("${property}");
    assertEquals("ARRGH", result);

    final String result2 =
            parser.translateAndLookup("${property}${property2}");
    assertEquals("ARRGH..", result2);
  }

  public void testEscapes ()
  {
    final RootXmlReadHandler rootXmlReadHandler = new ReportParser();
    rootXmlReadHandler.setHelperObject("property", "ARRGH");
    rootXmlReadHandler.setHelperObject("property$", "..");

    final StringLookupParser parser = new StringLookupParser(rootXmlReadHandler);

    final String result =
            parser.translateAndLookup( "${\\property}");
    assertEquals("ARRGH", result);

    final String result2 =
            parser.translateAndLookup( "${property}\\$${property\\$}");
    assertEquals("ARRGH$..", result2);
  }


  public void testEvilUserString ()
  {
    final RootXmlReadHandler rootXmlReadHandler = new ReportParser();
    rootXmlReadHandler.setHelperObject("property", "ARRGH");
    rootXmlReadHandler.setHelperObject("property$", "..");

    final StringLookupParser parser = new StringLookupParser(rootXmlReadHandler);

    final String result =
            parser.translateAndLookup("$\\{\\property}");
    assertEquals("${property}", result);

    final String result2 =
            parser.translateAndLookup( "\\${property}\\$${property\\$}");
    assertEquals("${property}$..", result2);
  }
}
