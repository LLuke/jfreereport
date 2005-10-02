package org.jfree.report.ext.junit.base.basic.util;

import org.jfree.report.util.CSVTokenizer;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

/**
 * @author Rob Edgeler
 * @version 0.1
 */
public class CSVTokenizerTest extends TestCase
{

  public CSVTokenizerTest(String name)
  {
    super(name);
  }

  public void testHasMoreTokens()
  {
    CSVTokenizer tokeniser = new CSVTokenizer("", CSVTokenizer.SEPARATOR_COMMA,
            CSVTokenizer.DOUBLE_QUATE);
    assertTrue("Should have no more tokens.", (!tokeniser.hasMoreTokens()));

    tokeniser = new CSVTokenizer("a,b,c", CSVTokenizer.SEPARATOR_COMMA,
            CSVTokenizer.DOUBLE_QUATE);
    assertEquals("Should count tokens correctly", 3, tokeniser.countTokens());
    assertEquals("a", tokeniser.nextToken());
    assertEquals("b", tokeniser.nextToken());
    assertEquals("c", tokeniser.nextToken());

    tokeniser = new CSVTokenizer(",b,c", CSVTokenizer.SEPARATOR_COMMA,
            CSVTokenizer.DOUBLE_QUATE);
    assertEquals("Should count tokens correctly", 3, tokeniser.countTokens());
    assertEquals("", tokeniser.nextToken());
    assertEquals("b", tokeniser.nextToken());
    assertEquals("c", tokeniser.nextToken());

    tokeniser = new CSVTokenizer("a,,c", CSVTokenizer.SEPARATOR_COMMA,
            CSVTokenizer.DOUBLE_QUATE);
    assertEquals("Should count tokens correctly", 3, tokeniser.countTokens());
    assertEquals("a", tokeniser.nextToken());
    assertEquals("", tokeniser.nextToken());
    assertEquals("c", tokeniser.nextToken());

    tokeniser = new CSVTokenizer("a,b,", CSVTokenizer.SEPARATOR_COMMA,
            CSVTokenizer.DOUBLE_QUATE);
    assertEquals("Should count tokens correctly", 3, tokeniser.countTokens());
    assertEquals("a", tokeniser.nextToken());
    assertEquals("b", tokeniser.nextToken());
    assertEquals("", tokeniser.nextToken());

    tokeniser = new CSVTokenizer(",,", CSVTokenizer.SEPARATOR_COMMA,
            CSVTokenizer.DOUBLE_QUATE);
    assertEquals("Should count tokens correctly", 3, tokeniser.countTokens());
    assertEquals("", tokeniser.nextToken());
    assertEquals("", tokeniser.nextToken());
    assertEquals("", tokeniser.nextToken());

    tokeniser = new CSVTokenizer("\"\",\"\",\"\"", CSVTokenizer.SEPARATOR_COMMA,
            CSVTokenizer.DOUBLE_QUATE);
    assertEquals("Should count tokens correctly", 3, tokeniser.countTokens());
    assertEquals("", tokeniser.nextToken());
    assertEquals("", tokeniser.nextToken());
    assertEquals("", tokeniser.nextToken());
  }

  /** @return a <code>TestSuite</code> */
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.setName("Test for CSVTokenizer.");
    suite.addTest(new CSVTokenizerTest("testHasMoreTokens"));
    return suite;
  }
}
