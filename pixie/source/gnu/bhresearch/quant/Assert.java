// Assert.java -- simple assertions.
//
// Copyright (c) 1997-1998 David R Harris.
// You can redistribute this work and/or modify it under the terms of the
// GNU Library General Public License version 2, as published by the Free
// Software Foundation. No warranty is implied. See Lgpl.htm for details.

package gnu.bhresearch.quant;

/**
 Simple assertions - a convenient way to throw an exception if a condition
 is not true. Assertion checking can be switched on or off. This class is
 written so that as much code as possible can be optimised away when they
 are switched off.

 <P>All members are static. Use as, eg: <PRE>
 Assert.assert( x > 0 );
 Assert.assert( x > 0, "Sample.test: x must be > 0" );

 if (Assert.getLevel() > 1 && x <= 0)
 Assert.failed( "Sample.test: x must be > 0" );

 if (Assert.isEnabled && x <= 0)
 Assert.failed( "Sample.test: x must be > 0" );

 </PRE>The last example is the most likely to be optimised away when
 assertions are switched off.

 <P>Assert.failed() throws an exception of type Assert.FailedException
 which can be caught if desired.
 */

public final class Assert
{
  /** Report assertion failures. */
  public static class FailedException extends RuntimeException
  {
    FailedException ()
    {
      this (null);
    }

    FailedException (String message)
    {
      super ((message == null) ?
              "Assertion failed" : "Assertion failed: " + message);
    }
  }

  /** Set to false to remove assertions as completely as possible. This is
   final static so that it can be evaluated at compile time. It is public
   because an accessor function might not get inlined. Both of these
   are to help optimisers. */
  public final static boolean isEnabled = true;

  private static int level = isEnabled ? 1 : 0;

  /** Control the verbosity and speed of assertions. The exact meaning
   is undefined, except that values of 0 or less mean assertion checking
   is switched off. Unlike isEnabled, it can change at runtime. */
  public static int getLevel ()
  {
    return isEnabled ? level : 0;
  }

  /** Control the verbosity and speed of assertions. The exact meaning
   is undefined, except that values of 0 or less switch assertion
   checking off. It can be changed at runtime. */
  public static void setLevel (int l)
  {
    level = l;
  }

  /** Check assertion, throw with an error message if not true. */
  public static void assert (boolean c, String message)
  {
    if (isEnabled && !c && level > 0)
      throw new FailedException (message);
  }

  /** Check assertion, throw exception if not true. */
  public static void assert (boolean c)
  {
    if (isEnabled && !c && level > 0)
      throw new FailedException ();
  }

  /** To call when a user assertion fails. It acts like
   assert( false, message ). */
  public static void failed (String message)
  {
    throw new FailedException (message);
  }

  /** To call when a user assertion fails. It acts like assert( false ). */
  public static void failed ()
  {
    throw new FailedException ();
  }

  /** No instances of this class allowed. */
  private Assert ()
  {
  }
}
