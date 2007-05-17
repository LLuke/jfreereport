package org.jfree.formula;

import java.util.ArrayList;
import java.util.HashSet;

import org.jfree.formula.common.TestFormulaContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class OpenFormulaConformance
{
  public static final String SMALL_GROUP = "ABS; ACOS; AND;"
      + "ASIN; ATAN; ATAN2; AVERAGE; CHOOSE; COLUMNS; COS; COUNT; COUNTA;"
      + "COUNTBLANK; COUNTIF; DATE; DAVERAGE; DAY; DCOUNT; DCOUNTA; DDB;"
      + "DEGREES; DGET; DMAX; DMIN; DPRODUCT; DSTDEV; DSTDEVP; DSUM; DVAR;"
      + "DVARP; EVEN; EXACT; EXP; FACT; FALSE; FIND; FV; HLOOKUP; HOUR; IF; INDEX;"
      + "INT; IRR; ISBLANK; ISERR; ISERROR; ISLOGICAL; ISNA; ISNONTEXT; ISNUMBER;"
      + "ISTEXT; LEFT; LEN; LN; LOG; LOG10; LOWER; MATCH; MAX; MID; MIN; MINUTE;"
      + "MOD; MONTH; N; NA; NOT; NOW; NPER; NPV; ODD; OR; PI; PMT; POWER;"
      + "PRODUCT; PROPER; PV; RADIANS; RATE; REPLACE; REPT; RIGHT; ROUND;"
      + "ROWS; SECOND; SIN; SLN; SQRT; STDEV; STDEVP; SUBSTITUTE; SUM; SUMIF;"
      + "SYD; T; TAN; TIME; TODAY; TRIM; TRUE; TRUNC; UPPER; VALUE; VAR; VARP;"
      + "VLOOKUP; WEEKDAY; YEAR";

  private FormulaContext context;

  private HashSet<String> implementedFunctions;

  @BeforeClass(alwaysRun = true)
  public void setup()
  {
    context = new TestFormulaContext();
    LibFormulaBoot.getInstance().start();

    implementedFunctions = new HashSet<String>();
    final String[] functionNames = context.getFunctionRegistry()
        .getFunctionNames();

    for (int i = 0; i < functionNames.length; i++)
    {
      implementedFunctions.add(functionNames[i]);
    }
  }

  @Test
  public void isInSmallGroup()
  {
    isInGroup("SmallGroup", SMALL_GROUP);
  }

  private void isInGroup(String groupName, String groupFunctions)
  {
    final ArrayList<String> functionsNotInGroup = new ArrayList<String>();
    final String[] split = groupFunctions.split(";");
    for (int i = 0; i < split.length; i++)
    {
      final String func = split[i].trim();

      if (!implementedFunctions.contains(func))
      {
        functionsNotInGroup.add(func);
      }
    }

    Assert.assertEquals(functionsNotInGroup.size(), 0, "The following "
        + functionsNotInGroup.size()
        + " functions are not yet implemented for the " + groupName
        + " conformance: " + functionsNotInGroup.toString());
  }
}
