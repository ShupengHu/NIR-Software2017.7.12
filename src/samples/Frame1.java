package samples;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.metal.*;
import javax.swing.BorderFactory;

import com.steema.teechart.*;
import com.steema.teechart.editors.*;
import com.steema.teechart.events.ChartPaintAdapter;
import com.steema.teechart.events.ChartPaintListener;
import com.steema.teechart.styles.*;
import com.steema.teechart.drawing.Color;

import com.sun.java.swing.plaf.motif.*;
//import com.sun.java.swing.plaf.gtk.*;
import com.sun.java.swing.plaf.windows.*;


/**
 * <p>Title: Project Testing </p>
 *
 * <p>Description: Project Testing </p>
 *
 * <p>Copyright: Copyright (c) 2004-2006 by Steema Software</p>
 *
 * <p>Company: Steema Software</p>
 *
 * @author db
 * @version 1.0
 */

public class Frame1 extends JFrame {

    static final long serialVersionUID = 11565439472837495L;

    JPanel contentPane;
    Border border1 = BorderFactory.createEtchedBorder(EtchedBorder.RAISED,
            Color.green, new Color(0, 133, 0));
    JComboBox jComboBox1 = new JComboBox();
    JButton jButton1 = new JButton();
    TChart tChart2 = new TChart();  //显示图形的地方
    JLabel jLabel2 = new JLabel();
    JComboBox cbStyles = new JComboBox();
    JCheckBox cbView3D = new JCheckBox();
    JCheckBox cbLegend = new JCheckBox();
    JButton jButton16 = new JButton();

    public Frame1() {
        try {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            jbInit();

            // Look and feel
            jComboBox1.addItem("System look and feel");
            jComboBox1.addItem("Windows");
            jComboBox1.addItem("Motif");
            jComboBox1.addItem("GTK");
            jComboBox1.addItem("Metal");

            // Series Styles
            cbStyles.addItem("Line");
            cbStyles.addItem("Bar");
            cbStyles.addItem("Horiz. Bar");
            cbStyles.addItem("Area");
            cbStyles.addItem("Point");
            cbStyles.addItem("Pie");
            cbStyles.addItem("FastLine");
            cbStyles.addItem("HorizLine");
            cbStyles.addItem("HorizArea");
            cbStyles.addItem("Bubble");
            cbStyles.addItem("Gantt");
            cbStyles.addItem("Shape");
            cbStyles.addItem("Surface");
            cbStyles.addItem("Contour");
            cbStyles.addItem("WaterFall");
            cbStyles.addItem("ColorGrid");
            cbStyles.addItem("Vector3D");
            cbStyles.addItem("Tower");
            cbStyles.addItem("TriSurface");
            cbStyles.addItem("Point3D");
            cbStyles.addItem("Arrow");
            cbStyles.addItem("Gauge");
            cbStyles.addItem("Map");
            cbStyles.addItem("Polar");
            cbStyles.addItem("Radar");
            cbStyles.addItem("Smith");
            cbStyles.addItem("Pyramid");
            cbStyles.addItem("Donut");
            cbStyles.addItem("Bezier");
            cbStyles.addItem("Point Figure");
            cbStyles.addItem("Candle");
            cbStyles.addItem("Volume");
            cbStyles.addItem("Histogram");
            cbStyles.addItem("Funnel");
            cbStyles.addItem("BoxPlot");
            cbStyles.addItem("Horiz.BoxPlot");
            cbStyles.addItem("Error Bar");
            cbStyles.addItem("Error");
            cbStyles.addItem("High-Low");
            cbStyles.addItem("Calendar");
            cbStyles.addItem("WindRose");
            cbStyles.addItem("Clock");
            cbStyles.addItem("Line Point");
            cbStyles.addItem("BarJoin");
            cbStyles.addItem("ImageBar");
            cbStyles.addItem("Bar3D");

            // Settings for tChart2
            //tChart2.addChartPaintListener(new Frame1_tChart2_afterDrawAdapter(this));

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Component initialization.
     *
     * @throws java.lang.Exception
     */
    private void jbInit() throws Exception {
        contentPane = (JPanel) getContentPane();
        contentPane.setLayout(null);
        setSize(new Dimension(1000, 1000));
        setTitle("TeeChart for Java - Swing example application");
        contentPane.setPreferredSize(new Dimension(700, 500));
        jComboBox1.setBounds(new Rectangle(13, 8, 241, 23));
        jComboBox1.addActionListener(new Frame1_jComboBox1_actionAdapter(this));
        jButton1.setBounds(new Rectangle(376, 8, 103, 25));
        jButton1.setMnemonic('E');
        jButton1.setText("Edit Chart...");
        jButton1.addActionListener(new Frame1_jButton1_actionAdapter(this));
        tChart2.setGraphics3D(null);
        tChart2.setBounds(new Rectangle(6, 71, 500, 350));
        jLabel2.setDisplayedMnemonic('S');
        jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel2.setLabelFor(cbStyles);
        jLabel2.setText("Series Style :");
        jLabel2.setBounds(new Rectangle(10, 37, 74, 15));
        cbStyles.setBounds(new Rectangle(88, 34, 166, 22));
        cbStyles.addActionListener(new Frame1_cbStyles_actionAdapter(this));
        cbView3D.setSelected(true);
        cbView3D.setText("View 3D");
        cbView3D.setBounds(new Rectangle(269, 39, 83, 23));
        cbView3D.addActionListener(new Frame1_cbView3D_actionAdapter(this));
        cbLegend.setSelected(true);
        cbLegend.setText("Legend");
        cbLegend.setBounds(new Rectangle(373, 39, 83, 23));
        cbLegend.addActionListener(new Frame1_cbLegend_actionAdapter(this));
        jButton16.setBounds(new Rectangle(267, 8, 93, 25));
        jButton16.setActionCommand("About...");
        jButton16.setText("About...");
        jButton16.addActionListener(new Frame1_jButton16_actionAdapter(this));
        contentPane.add(jComboBox1);
        contentPane.add(cbStyles);
        contentPane.add(jButton16);
        contentPane.add(tChart2);
        contentPane.add(cbView3D);
        contentPane.add(cbLegend);
        contentPane.add(jButton1);
        contentPane.add(jLabel2);
    }

    public void tChart2_afterDrawPerformed(ChartPaintListener e) {
      // Example of use of the "AfterDraw" paint event:
        tChart2.getGraphics3D().rectangle(5, 5, 10, 10);
    }

    public void jComboBox1_actionPerformed(ActionEvent e) {
        try {
            switch (jComboBox1.getSelectedIndex()) {
            case 1:
                UIManager.setLookAndFeel(new WindowsLookAndFeel());
                break;
            case 2:
                UIManager.setLookAndFeel(new MotifLookAndFeel());
                break;
/*            case 3:
                UIManager.setLookAndFeel(new GTKLookAndFeel());
                break;*/
            case 4:
                UIManager.setLookAndFeel(new MetalLookAndFeel());
                break;
            default:
                UIManager.setLookAndFeel(UIManager.
                                         getSystemLookAndFeelClassName());
                break;
            }

            SwingUtilities.updateComponentTreeUI(this);
            this.pack();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void jButton1_actionPerformed(ActionEvent e) {
      // Show the TeeChart editor dialog:
        ChartEditor.editChart(tChart2.getChart());
    }

    public void cbStyles_actionPerformed(ActionEvent e) {

        tChart2.removeAllSeries();

        try {
            switch (cbStyles.getSelectedIndex()) {
            case 0:
                tChart2.addSeries(new Line());
                break;
            case 1:
                tChart2.addSeries(new Bar());
                break;
            case 2:
                tChart2.addSeries(new HorizBar());
                break;
            case 3:
                tChart2.addSeries(new Area());
                break;
            case 4:
                tChart2.addSeries(new Points());
                break;
            case 5:
                tChart2.addSeries(new Pie());
                break;
            case 6:
                tChart2.addSeries(new FastLine());
                break;
            case 7:
                tChart2.addSeries(new HorizLine());
                break;
            case 8:
                tChart2.addSeries(new HorizArea());
                break;
            case 9:
                tChart2.addSeries(new Bubble());
                break;
            case 10:
                tChart2.addSeries(new Gantt());
                break;
            case 11:
                tChart2.addSeries(new com.steema.teechart.styles.Shape());
                break;
            case 12:
                tChart2.addSeries(new Surface());
                break;
            case 13:
                tChart2.addSeries(new Contour());
                break;
            case 14:
                tChart2.addSeries(new Waterfall());
                break;
            case 15:
                tChart2.addSeries(new ColorGrid());
                break;
            case 16:
                tChart2.addSeries(new Vector3D());
                break;
            case 17:
                tChart2.addSeries(new Tower());
                break;
            case 18:
                tChart2.addSeries(new TriSurface());
                break;
            case 19:
                tChart2.addSeries(new Points3D());
                break;
            case 20:
                tChart2.addSeries(new Arrow());
                break;
            case 21:
                tChart2.addSeries(new Gauges());
                break;
            case 22:
                tChart2.addSeries(new Map());
                break;
            case 23:
                tChart2.addSeries(new Polar());
                break;
            case 24:
                tChart2.addSeries(new Radar());
                break;
            case 25:
                tChart2.addSeries(new Smith());
                break;
            case 26:
                tChart2.addSeries(new Pyramid());
                break;
            case 27:
                tChart2.addSeries(new Donut());
                break;
            case 28:
                tChart2.addSeries(new Bezier());
                break;
            case 29:
                tChart2.addSeries(new PointFigure());
                break;
            case 30:
                tChart2.addSeries(new Candle());
                break;
            case 31:
                tChart2.addSeries(new Volume());
                break;
            case 32:
                tChart2.addSeries(new Histogram());
                break;
            case 33:
                tChart2.addSeries(new Funnel());
                break;
            case 34:
                tChart2.addSeries(new com.steema.teechart.styles.Box());
                break;
            case 35:
                tChart2.addSeries(new HorizBox());
                break;
            case 36:
                tChart2.addSeries(new ErrorBar());
                break;
            case 37:
                tChart2.addSeries(new com.steema.teechart.styles.Error());
                break;
            case 38:
                tChart2.addSeries(new HighLow());
                break;
            case 39:
                tChart2.addSeries(new Calendar());
                break;
            case 40:
                tChart2.addSeries(new WindRose());
                break;
            case 41:
                tChart2.addSeries(new Clock());
                break;
            case 42:
                tChart2.addSeries(new LinePoint());
                break;
            case 43:
                tChart2.addSeries(new BarJoin());
                break;
            case 44:
                tChart2.addSeries(new ImageBar());
                break;
            case 45:
                tChart2.addSeries(new Bar3D());
                break;
            }

            //tChart2.getSeries(0).fillSampleValues();
            /*
            ArrayList<Double> X=new ArrayList<Double>();
            ArrayList<Double> Y=new ArrayList<Double>();
            File file1=new File("spectrums.txt");
            File file2=new File("wavelengths.txt");
            Scanner s1=new Scanner(file1);
            Scanner s2=new Scanner(file2);
            
            while(s1.hasNextDouble()){
            	Y.add(s1.nextDouble());
            	//System.out.println(s1.nextDouble());
            }
            while(s2.hasNextDouble()){
            	X.add(s2.nextDouble());
            	//System.out.println(s2.nextDouble());
            }
            //System.out.println(X.size());
            //System.out.println(Y.size());
            
            s1.close();
            s2.close();
            */
            double[] x={1,2,3};
            double[] y={1,2,3};
            double[] x1={4,4,4};
            tChart2.getSeries(0).add(x,y);
            tChart2.getSeries(1).add(x1,y);
            
            
           
            
            
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void cbView3D_actionPerformed(ActionEvent e) {
        tChart2.getAspect().setView3D(cbView3D.isSelected());
    }

    public void cbLegend_actionPerformed(ActionEvent e) {
        tChart2.getLegend().setVisible(cbLegend.isSelected());
    }

    public void jButton16_actionPerformed(ActionEvent e) {
        AboutDialog.showDialog();
    }
}


final class Frame1_jButton16_actionAdapter implements ActionListener {
    private Frame1 adaptee;
    Frame1_jButton16_actionAdapter(Frame1 adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jButton16_actionPerformed(e);
    }
}


/*final class Frame1_tChart2_afterDrawAdapter extends ChartPaintAdapter {
    private Frame1 adaptee;

    Frame1_tChart2_afterDrawAdapter(Frame1 adaptee) {
        this.adaptee = adaptee;
    }

    public void afterChartPaint(ChartPaintEvent e) {
        adaptee.tChart2_afterDrawPerformed(e);
    }
}*/


final class Frame1_cbLegend_actionAdapter implements ActionListener {
    private Frame1 adaptee;
    Frame1_cbLegend_actionAdapter(Frame1 adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.cbLegend_actionPerformed(e);
    }
}


final class Frame1_cbView3D_actionAdapter implements ActionListener {
    private Frame1 adaptee;
    Frame1_cbView3D_actionAdapter(Frame1 adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.cbView3D_actionPerformed(e);
    }
}


final class Frame1_cbStyles_actionAdapter implements ActionListener {
    private Frame1 adaptee;
    Frame1_cbStyles_actionAdapter(Frame1 adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.cbStyles_actionPerformed(e);
    }
}


final class Frame1_jButton1_actionAdapter implements ActionListener {
    private Frame1 adaptee;
    Frame1_jButton1_actionAdapter(Frame1 adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jButton1_actionPerformed(e);
    }
}


final class Frame1_jComboBox1_actionAdapter implements ActionListener {
    private Frame1 adaptee;
    Frame1_jComboBox1_actionAdapter(Frame1 adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jComboBox1_actionPerformed(e);
    }
}
