/*
 * Created by IntelliJ IDEA.
 * User: user
 * Date: 15.10.2002
 * Time: 21:41:08
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package com.jrefinery.report.util;

import java.io.OutputStream;
import java.io.IOException;

public class NullOutputStream extends OutputStream
{
  public NullOutputStream ()
  {
  }

  public void write(int i) throws IOException
  {
    // no i wont do anything here ...
  }

  public void write(byte[] bytes) throws IOException
  {
    // no i wont do anything here ...
  }

  public void write(byte[] bytes, int i, int i1) throws IOException
  {
    // no i wont do anything here ...
  }
}
