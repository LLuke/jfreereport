package com.jrefinery.report.junit;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.util.ArrayList;
import junit.framework.*;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;
import com.jrefinery.report.JFreeReport;
import com.jrefinery.report.Group;
import com.jrefinery.report.OutputTarget;
import com.jrefinery.report.G2OutputTarget;
import com.jrefinery.report.function.Function;
import com.jrefinery.report.function.GroupCountFunction;

/**
 * This test case has been compiled in response to a bug report by Steven Feinstein:
 * <p>
 * http://www.object-refinery.com/phorum-3.3.2a/read.php?f=7&i=12&t=12
 * <p>
 * A new group is not being generated for the last row of data in a report.
 */
public class Bug1 extends TestCase {

    JFreeReport report;
    OutputTarget target;

    /**
     * Returns the tests as a test suite.
     */
    public static Test suite() {
        return new TestSuite(Bug1.class);
    }

    /**
     * Constructs a new set of tests.
     * @param The name of the tests.
     */
    public Bug1(String name) {
        super(name);
    }

    /**
     * Common test setup.
     */
    protected void setUp() {

        String[][] values = new String[][]  { {"A", "1"}, {"A", "2"}, {"B", "3"} };
        String[] columns = new String[]  { "Letter", "Number" };
        TableModel data = new DefaultTableModel(values, columns);

        this.report = new JFreeReport("Test Report", data);
        ArrayList fields = new ArrayList();
        fields.add("Letter");
        this.report.addGroup(new Group("Letter Group", fields));
        this.report.addFunction(new GroupCountFunction("f1", "Letter Group"));

        BufferedImage buffer = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D)buffer.getGraphics();
        this.target = new G2OutputTarget(g2, new PageFormat());

    }

    /**
     * Counts the number of groups.
     */
    public void testGroupCount() {

        this.report.processReport(target, false);

        Function function = this.report.getFunctions().get("f1");
        Integer value = (Integer)function.getValue();
        this.assertEquals(new Integer(2), value);

    }

}