package org.jfree.report.ext.junit.bugs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class SerializationBug
{
  private static void testSerialization (Object in)
          throws IOException, ClassNotFoundException
  {
    final ByteArrayOutputStream bo = new ByteArrayOutputStream();

    final ObjectOutputStream oout = new ObjectOutputStream(bo);
    oout.writeObject(in);
    oout.close();
    final ByteArrayInputStream bin = new ByteArrayInputStream(bo.toByteArray());
    final ObjectInputStream oin = new ObjectInputStream(bin);
    final Object o = oin.readObject();

  }

  private static final String DECIMALFORMAT_DEFAULT_PATTERN =
    "#,###.###################################################" +
    "#########################################################" +
    "#########################################################" +
    "#########################################################" +
    "#########################################################" +
    "#########################################################" +
    "####";

  public static void main (String[] args)
          throws IOException, ClassNotFoundException
  {
    final SimpleDateFormat dfmt = new SimpleDateFormat("");
    System.out.println(dfmt.toPattern());

    final DecimalFormat dcfmt = new DecimalFormat(DECIMALFORMAT_DEFAULT_PATTERN);
    System.out.println(dcfmt.toPattern());

    testSerialization(dfmt);
    testSerialization(dcfmt);
  }
}
