package com.jrefinery.report.ext.junit;

/*
 * File is generated by 'Unit Tests Generator' developed under
 * 'Web Test Tools' project at http://sf.net/projects/wttools/
 * Copyright (C) 2001 "Artur Hefczyc" <kobit@users.sourceforge.net>
 * to all 'Web Test Tools' subprojects.
 *
 * No rigths to files and no responsibility for code generated
 * by this tool are belonged to author of 'unittestsgen' utility.
 *
 * $Id:$
 * $Author:$
 * $Date:$
 */

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * TestSuite that runs all the tests.
 * You can run unit tests in many ways, however this file is designed
 * performing tests in the following way:<br/>
 * <pre>
 *   java -cp "jar/thisjarfile.jar;lib/junit.jar" com.jrefinery.report.ext.junit.TestAll
 * </pre>
 *
 */
public class TestAll
{

  public static void main (String[] args)
  {
    junit.textui.TestRunner.run (suite ());
  } // end of main(Stringp[ args)

  public static Test suite ()
  {
    TestSuite suite = new TestSuite ("All JUnit Tests");
    suite.addTest (com.jrefinery.report.ext.junit.base.DateFunctionElementTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.FunctionCollectionTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.LabelElementTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.DateElementTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.CursorTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.ReportProcessingExceptionTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.NumberFunctionElementTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.ItemBandTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.JFreeReportTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.StringFunctionElementTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.WaitingImageObserverTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.ImageReferenceTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.GeneralElementTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.ReportStateListTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.ReportHeaderTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.MultilineTextElementTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.StringElementTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.GroupHeaderTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.PageFooterTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.LineShapeElementTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.JFreeReportInfoTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.RectangleShapeElementTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.ReportProcessorTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.GroupListTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.TextElementTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.ImageElementTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.NumberElementTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.PageHeaderTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.ItemFactoryTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.GroupTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.ReportFooterTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.GroupFooterTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.io.ReportGeneratorTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.io.ParserUtilTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.io.BandFactoryTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.io.ReportDefinitionExceptionTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.io.ElementFactoryTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.io.FontFactoryTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.io.GroupFactoryTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.io.ReportDefinitionContentHandlerTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.io.FunctionFactoryTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.io.ReportFactoryTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.demo.FontChangeFunctionTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.demo.HugeJFreeReportDemoTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.demo.JFreeReportDemoTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.demo.SampleData1TestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.demo.SampleData2TestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.demo.SampleData3TestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.demo.SampleData4TestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.demo.SampleData5TestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.demo.SampleData6TestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.demo.SampleReport1TestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.demo.resources.DemoResources_deTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.demo.resources.DemoResourcesTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.util.HashNMapTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.util.KeyedQueueTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.util.LogTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.util.FloatingButtonEnablerTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.util.ReadOnlyListTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.util.ExceptionDialogTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.util.ReadOnlyListIteratorTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.util.SystemOutLogTargetTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.util.ReadOnlyIteratorTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.util.ReportPropertiesTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.event.ReportEventTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.event.ReportListenerAdapterTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.function.ElementVisibilitySwitchFunctionTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.function.PageFunctionTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.function.ItemCountFunctionTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.function.GroupCountFunctionTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.function.TotalGroupSumFunctionTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.function.ItemPercentageFunctionTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.function.ItemSumFunctionTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.function.FunctionInitializeExceptionTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.function.ReportPropertyFunctionTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.targets.OutputTargetExceptionTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.targets.PDFOutputTargetTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.targets.BandCursorTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.targets.G2OutputTargetTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.filter.ImageLoadFilterTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.filter.NumberFormatFilterTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.filter.DecimalFormatParserTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.filter.DateFormatParserTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.filter.URLFilterTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.filter.NumberFormatParserTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.filter.FormatFilterTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.filter.StaticDataSourceTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.filter.ReportDataSourceTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.filter.FunctionDataSourceTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.filter.ImageRefFilterTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.filter.FormatParserTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.filter.SimpleDateFormatFilterTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.filter.EmptyDataSourceTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.filter.SimpleDateFormatParserTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.filter.StringFilterTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.filter.DecimalFormatFilterTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.filter.DateFormatFilterTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.resources.JFreeReportResourcesTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.resources.JFreeReportResources_deTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.preview.PreviewFrameTestCase.suite ());
    suite.addTest (com.jrefinery.report.ext.junit.base.preview.ReportPaneTestCase.suite ());
    return suite;
  } // end of suite()
} // end of com.jrefinery.report.ext.junit.TestAll
