package org.jfree.report.ext.junit.base.basic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.awt.print.PageFormat;

import junit.framework.TestCase;
import org.jfree.report.CustomPageDefinition;

public class CustomPageDefinitionTest extends TestCase
{
  public CustomPageDefinitionTest (final String s)
  {
    super(s);
  }

  public void testSerializeEmpty ()
          throws IOException, ClassNotFoundException
  {
    final CustomPageDefinition cpd = new CustomPageDefinition();

    final ByteArrayOutputStream bo = new ByteArrayOutputStream();
    final ObjectOutputStream out = new ObjectOutputStream(bo);
    out.writeObject(cpd);
    out.close();

    final ObjectInputStream oin = new ObjectInputStream
      (new ByteArrayInputStream(bo.toByteArray()));
    final Object e2 = oin.readObject();
    assertNotNull(e2); // cannot assert equals, as this is not implemented.
    assertEquals(cpd, e2);
  }

  public void testSerializeFilled ()
          throws IOException, ClassNotFoundException
  {
    final CustomPageDefinition cpd = new CustomPageDefinition();
    cpd.addPageFormat(new PageFormat(), 0, 0);
    cpd.addPageFormat(new PageFormat(), 0, 400);
    cpd.addPageFormat(new PageFormat(), 400, 0);
    cpd.addPageFormat(new PageFormat(), 400, 400);

    final ByteArrayOutputStream bo = new ByteArrayOutputStream();
    final ObjectOutputStream out = new ObjectOutputStream(bo);
    out.writeObject(cpd);
    out.close();

    final ObjectInputStream oin = new ObjectInputStream
      (new ByteArrayInputStream(bo.toByteArray()));
    final Object e2 = oin.readObject();
    assertNotNull(e2); // cannot assert equals, as this is not implemented.
    assertEquals(cpd, e2);
  }


}
