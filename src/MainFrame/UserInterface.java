package MainFrame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.mathworks.toolbox.javabuilder.MWException;
import com.steema.teechart.TChart;
import com.steema.teechart.editors.ChartEditor;
import com.steema.teechart.styles.Line;
import com.steema.teechart.styles.Series;
import OmniDriver.OmniDriver;
import calibration.PLS_Algorithm;
import computation.Reflectivity_Absorbance;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import preprocess.MSC_MATLAB;
import preprocess.SG_MATLAB;
import preprocess.SNV_MATLAB;
import serialCommunication.ChipBoardReset;
import serialCommunication.SerialCommunication;

@SuppressWarnings("serial")
public class UserInterface extends JFrame implements ActionListener{
		private JPanel contentPane=(JPanel) getContentPane();
		private TChart tchart=new TChart();
		private TChart biruetChart=new TChart();
		private TChart waterChart=new TChart();
		private TChart carbamideChart=new TChart();
		private JButton EditButton=new JButton();
		private JButton RunButton=new JButton();
		private JButton FeaturesButton=new JButton();
		private JButton StopButton=new JButton();
		private JButton autoRunButton=new JButton();
		private JButton chipResetButton=new JButton();
		private JMenuBar menuBar=new JMenuBar();
		private JMenu fileMenu=new JMenu();
		private JMenu figureMenu=new JMenu();		
		private JMenu helpMenu=new JMenu();
		private JMenu languages=new JMenu();
		private JMenu dataProcess=new JMenu();
		private JMenuItem featuresMenuItems=new JMenuItem();
		private JMenuItem chipController=new JMenuItem();
		private JMenuItem editMenuItems=new JMenuItem();
		private JMenuItem openFile=new JMenuItem();
		private JMenuItem export=new JMenuItem();
		private JMenuItem chinese=new JMenuItem();
		private JMenuItem english=new JMenuItem();
		private JMenuItem model =new JMenuItem();
		private JMenuItem absorbanceSelection=new JMenuItem();
		private JMenuItem algorithmsSelection=new JMenuItem();
		private JFileChooser fileChooser=new JFileChooser();
		private JFileChooser modelChooser=new JFileChooser();
		private JFileChooser absorbanceChooser=new JFileChooser();
		//����ʱ��
		private JLabel TimeLable=new JLabel();     
		private JTextField TimeText=new JTextField();             
		//ƽ������
		private JLabel AverageLable=new JLabel();
		private JTextField AverageText=new JTextField();
		//ƽ����
		private JLabel SmoothLable=new JLabel();
		private JTextField SmoothText=new JTextField();	
		//�Զ����м��ʱ��
		private JLabel intervalLable=new JLabel();
		private JTextField intervalText=new JTextField();
		//У�����
		private JLabel loopLable=new JLabel();
		private JTextField loopText=new JTextField();
		//�㷨ѡ��
		private JLabel preprocessLable=new JLabel();
		private String[] preAlgorithms={"None","SNV","SG","MSC"};		
		private JComboBox<String> preprocessBox=new JComboBox<String>(preAlgorithms);
		private JLabel calibrationLable=new JLabel();
		private String[] calibrationAlgorithms={"None","PLS","PCA"};
		private JComboBox<String> calibrationBox=new JComboBox<String>(calibrationAlgorithms);
		private JLabel windowLable=new JLabel("���ڿ��");
		private JLabel polynomialLable=new JLabel("����ʽ����");
		private JLabel pcLable=new JLabel("���ɷ���");
		private JLabel derLable=new JLabel("��������");
		private JTextField pcText=new JTextField();
		private Integer[] windowWidthValues={7,9,11,13,15,17};
		private Integer[] polynomialOrderValues={2,3,4,5};
		private Integer[] derValues={1,2};
		private JComboBox<Integer> windowWidth=new JComboBox<Integer>(windowWidthValues);
		private JComboBox<Integer> polynomialOrder=new JComboBox<Integer>(polynomialOrderValues);
		private JComboBox<Integer> der=new JComboBox<Integer>(derValues);
		//���ս����Ũ��	����	
		private JLabel biruetLable=new JLabel("������");
		private JLabel waterLable=new JLabel("ˮ��");
		private JLabel carbamideLable=new JLabel("����");
		private JTextArea biruet=new JTextArea();
		private JTextArea water=new JTextArea();
		private JTextArea carbamide=new JTextArea();
		private JScrollPane jspB=new JScrollPane(biruet,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		private JScrollPane jspW=new JScrollPane(water,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		private JScrollPane jspC=new JScrollPane(carbamide,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//���߼�
		private Series[] series=null;
		//�û��������
		int usec=0;                                             //����ʱ��
		int smoothness=0;                                       //ƽ����
		int numberOfScansToAverage=0;	                        //ƽ��ɨ�����
		int timeInterval=0;                                     //�Զ�����ʱ����
		int loop=0;                                             //У�����
		int loopCounter=0;                                      //У�����ѭ��������
		//��������
		OmniDriver OD=null;
		Connection con=null;
		String dateTime="";                                     //ɨ��ʵʱʱ��
		int sampleNo=0;                                         //��Ʒ���		
		double[] B_LightIntensities=null;                       //�������׵Ĺ�ǿ
		double[] R_LightIntensities=null;                       //�ο����׵Ĺ�ǿ
		double[] S_LightIntensities=null;                       //��Ʒ���׵Ĺ�ǿ
		double[] Wavelengths=null;                              //����
		double[] reflectivity=null;                             //������
		double[] absorbance=null;                               //�����
		double[][] model_absorbance=null;                       //ģ�������
		double[][] model_chemicalValue=null;                    //ģ�ͻ�ѧֵ
		double[] selectedAbsorbance=null;                       //���ֶ�ѡ���������ݷ����Ĺ���
		double[][] afterPreprocess=null;                        //����Ԥ����������
		double[][] concentration=null;                          //������ԪУ�������õ���Ũ�Ⱦ���
		ArrayList<Double> biruetList=new ArrayList<>();         //������ԪУ�������õ���������Ũ��
		ArrayList<Double> waterList=new ArrayList<>();          //������ԪУ�������õ���ˮ��Ũ��
		ArrayList<Double> carbamideList=new ArrayList<>();      //������ԪУ�������õ�������Ũ��
		ArrayList<Double> X=new ArrayList<>();                  //Ũ��ͼͨ�ú�����
		boolean isRunned=false;                                 //�жϹ������Ƿ��Ѿ���ʼ����
		boolean isStoped=false;                                 //�жϹ������Ƿ��Ѿ�ֹͣ����
		boolean isAutoRun=false;                                //�Ƿ�Ϊ�Զ�����״̬
		File[] openFiles=null;                                  //ѡ��򿪵��ļ���
		byte[] stop={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x10,(byte) 0xff,0x65};   //��ָֹ��
		byte[] forward={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x11,(byte) 0xff,0x65};   //ǰ��ָ��
		byte[] backward={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x01,(byte) 0xff,0x65};   //����ָ��
		byte[] positionCheck={0x01,0x03,0x00,0x00,0x00,0x02,(byte) 0xff,0x65};        //�����λ�Լ�ָ��
		
		//�Զ����а�ť����ɫ
		Color color=null;

		public UserInterface() throws IOException{
			 setDefaultCloseOperation(EXIT_ON_CLOSE);
			 init();			 
			 /*-------�Զ���ʱɨ��-------*/
			 /*
			 if(isRunned!=false){
				 try {
					Thread.sleep(TimeInterval);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				 while(isStoped==false){
					 //�ٴ�ɨ��õ����µĹ�ǿ�Ͳ���
					 this.S_LightIntensities=this.OD.getLigthIntensities();
				     this.Wavelengths=this.OD.getWaveLengths();
				     //�ٴμ��㷴���ʺ������
				     Reflectivity_Absorbance RA=new Reflectivity_Absorbance(this.R_LightIntensities,this.B_LightIntensities,this.S_LightIntensities);
					 this.reflectivity=RA.getReflectivity();
					 this.absorbance=RA.getAbsorbance();
					 */
				     /*---�����ݴ��浽�ļ�---*/
			 /*
				     dataStored(this.S_LightIntensities, "S_LightIntensities.txt");
				     dataStored(this.Wavelengths, "Wavelengths.txt");
				     dataStored(this.reflectivity, "Reflectivity.txt");
					 dataStored(this.absorbance, "Absorbance.txt");
					 //�������������»�ͼ		       
				        tchart.getSeries(0).add(this.Wavelengths, this.absorbance);
				        tchart.repaint();
					 try {
						Thread.sleep(TimeInterval);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}				 				 
				 }
			 }
			 */
		}
		/**
		 * ��ʼ��
		 * @throws FileNotFoundException 
		 */
		public void init(){
			//������������С
			setTitle("���׻滭");
			//int width = Toolkit.getDefaultToolkit().getScreenSize().width; //�õ���ǰ��Ļ�ֱ��ʵĿ�
			//int height = Toolkit.getDefaultToolkit().getScreenSize().height;//�õ���ǰ��Ļ�ֱ��ʵĸ�.			
			setSize(1600,900);
			setResizable(false);
			//��������
			contentPane.setLayout(null);
			contentPane.setSize(getMaximumSize());
			/*---���ò˵�---*/
			menuBar.setBounds(new Rectangle(0, 0, 300, 30));
			//������ͼ�˵�
			figureMenu.setText("��ͼ");
			figureMenu.setFont(new Font("����", Font.PLAIN, 12));
			figureMenu.setForeground(Color.black);	
			editMenuItems.setText("�༭ͼ��");
			editMenuItems.setFont(new Font("����", Font.PLAIN, 12));
			editMenuItems.setForeground(Color.black);
			editMenuItems.addActionListener(this);			
			//�����ļ��˵�
			fileMenu.setText("�ļ�");
			fileMenu.setFont(new Font("����", Font.PLAIN, 12));
			fileMenu.setForeground(Color.black);
			openFile.setText("��");
			openFile.setFont(new Font("����", Font.PLAIN, 12));
			openFile.setForeground(Color.black);
			openFile.addActionListener(this);
			export.setText("��������");
			export.setFont(new Font("����", Font.PLAIN, 12));
			export.setForeground(Color.black);
			export.addActionListener(this);
			//���ð����˵�
			helpMenu.setText("����");
			helpMenu.setFont(new Font("����", Font.PLAIN, 12));
			helpMenu.setForeground(Color.black);
			featuresMenuItems.setText("��������������");
			featuresMenuItems.setFont(new Font("����", Font.PLAIN, 12));
			featuresMenuItems.setForeground(Color.black);
			featuresMenuItems.addActionListener(this);
			chipController.setText("�������");
			chipController.setFont(new Font("����", Font.PLAIN, 12));
			chipController.setForeground(Color.black);
			chipController.addActionListener(this);
			//�������Բ˵�
			languages.setText("����");
			languages.setFont(new Font("����", Font.PLAIN, 12));
			languages.setForeground(Color.black);
			chinese.setText("����");
			chinese.setFont(new Font("����", Font.PLAIN, 12));
			chinese.setForeground(Color.black);
			chinese.addActionListener(this);
			english.setText("English");
			english.setFont(new Font("Arial", Font.PLAIN, 12));
			english.setForeground(Color.black);
			english.addActionListener(this);
			//�������ݴ���˵�
			dataProcess.setText("���ݴ���");
			dataProcess.setFont(new Font("����", Font.PLAIN, 12));
			dataProcess.setForeground(Color.black);
			model.setText("����ģ��");
			model.setFont(new Font("����", Font.PLAIN, 12));
			model.setForeground(Color.black);
			model.addActionListener(this);
			absorbanceSelection.setText("ѡ�����");
			absorbanceSelection.setFont(new Font("����", Font.PLAIN, 12));
			absorbanceSelection.setForeground(Color.black);
			absorbanceSelection.addActionListener(this);
			algorithmsSelection.setText("ѡ���㷨");
			algorithmsSelection.setFont(new Font("����", Font.PLAIN, 12));
			algorithmsSelection.setForeground(Color.black);
			algorithmsSelection.addActionListener(this);
			//��Ӳ˵��Ͳ˵���Ŀ	
			menuBar.add(fileMenu);
			menuBar.add(figureMenu);
			menuBar.add(helpMenu);
			menuBar.add(languages);
			menuBar.add(dataProcess);
			fileMenu.add(openFile);
			fileMenu.add(export);
			figureMenu.add(editMenuItems);
			helpMenu.add(featuresMenuItems);
			helpMenu.add(chipController);
			languages.add(chinese);
			languages.add(english);
			dataProcess.add(model);
			dataProcess.add(absorbanceSelection);
			dataProcess.add(algorithmsSelection);
			//���û���ʱ�����
			TimeLable.setBounds(new Rectangle(0, 30, 85, 50));
			TimeLable.setText("����ʱ�䣺����");
			TimeLable.setFont(new Font("����", Font.PLAIN, 12));
			TimeLable.setForeground(Color.black);
			TimeText.setBounds(new Rectangle(85, 30, 40, 50));
			TimeText.setText("16");
			//����ƽ����������
			AverageLable.setBounds(new Rectangle(200, 30, 50, 50));
			AverageLable.setText("ƽ������");
			AverageLable.setFont(new Font("����", Font.PLAIN, 12));
			AverageLable.setForeground(Color.black);
			AverageText.setBounds(new Rectangle(250, 30, 40, 50));
			AverageText.setText("160");
			//����ƽ���Ƚ���
			SmoothLable.setBounds(new Rectangle(300, 30, 40, 50));
			SmoothLable.setText("ƽ����");
			SmoothLable.setFont(new Font("����", Font.PLAIN, 12));
			SmoothLable.setForeground(Color.black);
			SmoothText.setBounds(new Rectangle(340, 30, 20, 50));
			SmoothText.setText("1");
			//�����Զ�����ʱ��������
			intervalLable.setBounds(new Rectangle(1040, 10, 70, 40));
			intervalLable.setText("ʱ����(s)");
			intervalLable.setFont(new Font("����", Font.PLAIN, 12));
			intervalLable.setForeground(Color.black);
			intervalText.setBounds(new Rectangle(1110, 10, 50, 40));
			//����У���������
			loopLable.setBounds(new Rectangle(1170, 10, 50, 40));
			loopLable.setText("У�����");
			loopLable.setFont(new Font("����", Font.PLAIN, 12));
			loopLable.setForeground(Color.black);
			loopText.setBounds(new Rectangle(1220, 10, 50, 40));
			//�����㷨ѡ�����
			preprocessLable.setBounds(new Rectangle(370, 30, 60, 20));
			preprocessLable.setText("Ԥ�����㷨");
			preprocessLable.setFont(new Font("����", Font.PLAIN, 12));
			preprocessLable.setForeground(Color.black);         
			preprocessBox.setBounds(new Rectangle(450, 30, 60, 20));
			preprocessBox.addActionListener(new ActionListener() {
			      public void actionPerformed(ActionEvent e) {
			    	  if(e.getSource()==preprocessBox){
			    			if(preprocessBox.getSelectedItem()=="SG"){
			    				windowLable.setBounds(520, 30, 60, 20);
			    				windowWidth.setBounds(580, 30, 50, 20);
			    				windowWidth.setEditable(false);
			    				polynomialLable.setBounds(640, 30, 80, 20);
			    				polynomialOrder.setBounds(720, 30, 50, 20);
			    				polynomialOrder.setEditable(false);
			    				derLable.setBounds(520,0,60,20);
			    				der.setBounds(580,0,50,20);
			    				der.setEditable(false);
			    				contentPane.add(windowLable);
			    				contentPane.add(windowWidth);
			    				contentPane.add(polynomialLable);
			    				contentPane.add(polynomialOrder);
			    				contentPane.add(derLable);
			    				contentPane.add(der);
			    				validate();
			    				repaint();
			    			}
			    			 else {
			   	    		  contentPane.remove(windowLable);
			   	    		  contentPane.remove(windowWidth);
			   	    		  contentPane.remove(polynomialLable);
			   	    		  contentPane.remove(polynomialOrder);
			   	    		  contentPane.remove(derLable);
			   	    		  contentPane.remove(der);
			   	    		  repaint();
			   	    	  }
			      }	    	 
			      }
			    });
			calibrationLable.setBounds(new Rectangle(370, 60, 80, 20));
			calibrationLable.setText("��ԪУ���㷨");
			calibrationLable.setFont(new Font("����", Font.PLAIN, 12));
			calibrationLable.setForeground(Color.black);
			calibrationBox.setBounds(new Rectangle(450, 60, 60, 20));
			calibrationBox.addActionListener(new ActionListener() {
			      public void actionPerformed(ActionEvent e) {
			    	  if(e.getSource()==calibrationBox){
			    			if(calibrationBox.getSelectedIndex()==1){
			    				pcLable.setBounds(520, 60, 60, 20);
                                pcText.setBounds(580, 60, 60, 20);
			    				contentPane.add(pcLable);
			    				contentPane.add(pcText);
			    				validate();
			    				repaint();
			    			}
			    			 else {
			   	    		  contentPane.remove(pcLable);
			   	    		  contentPane.remove(pcText);
			   	    		  repaint();
			   	    	  }
			      }	    	 
			      }
			    });
			
			//�������ս������
			biruetLable.setBounds(800, 0, 50, 30);
			waterLable.setBounds(800, 35, 50, 30);
			carbamideLable.setBounds(800, 70, 50, 30);
			jspB.setBounds(new Rectangle(860, 0, 150,30));
			jspW.setBounds(860, 35, 150,30);
			jspC.setBounds(860, 70, 150,30);
			//����ͼ�񲿷�
			//--------����ͼ-----------//
			tchart.setGraphics3D(null);
			//��������ͼ�����һ������
			tchart.getLegend().setVisible(false);
			tchart.setBounds(new Rectangle(0, 108, 900, 700));
			Series realS=new Line();
			tchart.removeAllSeries();
			tchart.addSeries(realS);			
			tchart.getAspect().setView3D(false);
			tchart.getChart().getTitle().setText("����ͼ");
			tchart.getChart().getTitle().getFont().setSize(20);
			tchart.getChart().getTitle().getFont().setColor(Color.blue);
			tchart.getAxes().getLeft().getTitle().setText("�����");
			tchart.getAxes().getLeft().getTitle().getFont().setSize(20);
			tchart.getAxes().getLeft().getTitle().getFont().setColor(Color.blue);;
			tchart.getAxes().getBottom().getTitle().setText("����");
			tchart.getAxes().getBottom().getTitle().getFont().setSize(20);
			tchart.getAxes().getBottom().getTitle().getFont().setColor(Color.blue);;
			//-----------Ũ��ͼ--------------//
			//������
			biruetChart.setGraphics3D(null);
			biruetChart.getLegend().setVisible(false);
			biruetChart.setBounds(new Rectangle(1000, 108, 500, 200));
			Series biruetPoints=new Line();
			biruetChart.removeAllSeries();
			biruetChart.addSeries(biruetPoints);
			biruetChart.getAspect().setView3D(false);
			biruetChart.getChart().getTitle().setText("������Ũ��ͼ");
			biruetChart.getChart().getTitle().getFont().setSize(20);
			biruetChart.getChart().getTitle().getFont().setColor(Color.blue);
			biruetChart.getAxes().getLeft().getTitle().setText("Ũ��");
			biruetChart.getAxes().getLeft().getTitle().getFont().setSize(20);
			biruetChart.getAxes().getLeft().getTitle().getFont().setColor(Color.blue);;
			biruetChart.getAxes().getBottom().getTitle().setText("����");
			biruetChart.getAxes().getBottom().getTitle().getFont().setSize(20);
			biruetChart.getAxes().getBottom().getTitle().getFont().setColor(Color.blue);;
			//ˮ��
			waterChart.setGraphics3D(null);
			waterChart.getLegend().setVisible(false);
			waterChart.setBounds(new Rectangle(1000, 350, 500, 200));
			Series waterPoints=new Line();
			waterChart.removeAllSeries();
			waterChart.addSeries(waterPoints);
			waterChart.getAspect().setView3D(false);
			waterChart.getChart().getTitle().setText("ˮ��Ũ��ͼ");
			waterChart.getChart().getTitle().getFont().setSize(20);
			waterChart.getChart().getTitle().getFont().setColor(Color.blue);
			waterChart.getAxes().getLeft().getTitle().setText("Ũ��");
			waterChart.getAxes().getLeft().getTitle().getFont().setSize(20);
			waterChart.getAxes().getLeft().getTitle().getFont().setColor(Color.blue);;
			waterChart.getAxes().getBottom().getTitle().setText("����");
			waterChart.getAxes().getBottom().getTitle().getFont().setSize(20);
			waterChart.getAxes().getBottom().getTitle().getFont().setColor(Color.blue);;
			//����
			carbamideChart.setGraphics3D(null);
			carbamideChart.getLegend().setVisible(false);
			carbamideChart.setBounds(new Rectangle(1000, 592, 500, 200));
			Series carbamidePoints=new Line();
			carbamideChart.removeAllSeries();
			carbamideChart.addSeries(carbamidePoints);
			carbamideChart.getAspect().setView3D(false);
			carbamideChart.getChart().getTitle().setText("����Ũ��ͼ");
			carbamideChart.getChart().getTitle().getFont().setSize(20);
			carbamideChart.getChart().getTitle().getFont().setColor(Color.blue);
			carbamideChart.getAxes().getLeft().getTitle().setText("Ũ��");
			carbamideChart.getAxes().getLeft().getTitle().getFont().setSize(20);
			carbamideChart.getAxes().getLeft().getTitle().getFont().setColor(Color.blue);;
			carbamideChart.getAxes().getBottom().getTitle().setText("����");
			carbamideChart.getAxes().getBottom().getTitle().getFont().setSize(20);
			carbamideChart.getAxes().getBottom().getTitle().getFont().setColor(Color.blue);;	
	        //����"����"��ť
	         RunButton.setBounds(new Rectangle(1040, 50, 60, 50));
	         RunButton.setText("����");
	         RunButton.setFont(new Font("����", Font.PLAIN, 12));
	         RunButton.setForeground(Color.black);
	         RunButton.addMouseListener(new java.awt.event.MouseAdapter(){
	          	public void mouseClicked(java.awt.event.MouseEvent evt){
	          		try {
						RunButtonMouseClicked(evt);
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
	          	}
	          });      
	       //����"�Զ�����"��ť	  
	         color=autoRunButton.getBackground();
	         autoRunButton.setBounds(new Rectangle(1110, 50, 100, 50));
	         autoRunButton.setText("�Զ�����");
	         autoRunButton.setFont(new Font("����", Font.PLAIN, 12));
	         autoRunButton.setForeground(Color.black);
	         autoRunButton.addMouseListener(new java.awt.event.MouseAdapter(){
	         	public void mouseClicked(java.awt.event.MouseEvent evt){	         		
						autoRunButtonMouseClicked(evt);					
	         	}
	         });   
	       //����"��ֹ"��ť
	         StopButton.setBounds(new Rectangle(1220, 50, 60, 50));
	         StopButton.setText("��ֹ");
	         StopButton.setFont(new Font("����", Font.PLAIN, 12));
	         StopButton.setForeground(Color.black);
	         StopButton.addMouseListener(new java.awt.event.MouseAdapter(){
	         	public void mouseClicked(java.awt.event.MouseEvent evt){
	         		try {
						StopButtonMouseClicked(evt);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	         	}
	         });
	       //����"�����λ"��ť
	         chipResetButton.setBounds(new Rectangle(1300, 50, 100, 50));
	         chipResetButton.setText("�����λ");
	         chipResetButton.setFont(new Font("����", Font.PLAIN, 12));
	         chipResetButton.setForeground(Color.black);
	         chipResetButton.addMouseListener(new java.awt.event.MouseAdapter(){
	         	public void mouseClicked(java.awt.event.MouseEvent evt){
	         		try {
						chipResetButtonMouseClicked(evt);
					} catch (InterruptedException | NoSuchPortException | PortInUseException | UnsupportedCommOperationException e) {
						e.printStackTrace();
					}
	         	}
	         });
			//��ӳɷ�
			contentPane.add(tchart);
			contentPane.add(biruetChart);
			contentPane.add(waterChart);
			contentPane.add(carbamideChart);
			contentPane.add(EditButton);
			contentPane.add(TimeLable);
			contentPane.add(TimeText);
			contentPane.add(AverageLable);
			contentPane.add(AverageText);
			contentPane.add(SmoothLable);
			contentPane.add(SmoothText);
			contentPane.add(RunButton);
			contentPane.add(autoRunButton);
			contentPane.add(FeaturesButton);
			contentPane.add(StopButton);
			contentPane.add(chipResetButton);
			contentPane.add(menuBar);
			contentPane.add(biruetLable);
			contentPane.add(waterLable);
			contentPane.add(carbamideLable);
			contentPane.add(jspB);
			contentPane.add(jspW);
			contentPane.add(jspC);
			contentPane.add(preprocessLable);
			contentPane.add(preprocessBox);
			contentPane.add(calibrationLable);
			contentPane.add(calibrationBox);
			contentPane.add(intervalLable);
			contentPane.add(intervalText);
			contentPane.add(loopLable);
			contentPane.add(loopText);
		}
		/**
		 * ʵ�֡����С���ť����:1)��Ƭ������ָ��;2)�õ����ݲ����棻3����ͼ
		 * @param evt
		 * @throws IOException 
		 * @throws InterruptedException 
		 */
		public void RunButtonMouseClicked(java.awt.event.MouseEvent evt) throws IOException, InterruptedException{
			//�ж��û������Ƿ����
			try{
					if(TimeText.getText().equalsIgnoreCase("")){
						JOptionPane.showMessageDialog(this, "û���������ʱ��","����",JOptionPane.ERROR_MESSAGE);
						return;
					}else if(Integer.parseInt(TimeText.getText())<1||Integer.parseInt(TimeText.getText())%1!=0){
						JOptionPane.showMessageDialog(this, "����ʱ�����Ϊ����С��100������","����",JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(AverageText.getText().equalsIgnoreCase("")){
						JOptionPane.showMessageDialog(this, "û������ƽ������","����",JOptionPane.ERROR_MESSAGE);
						return;
					}else if(Integer.parseInt(AverageText.getText())<1||Integer.parseInt(AverageText.getText())%1!=0){
						JOptionPane.showMessageDialog(this, "����ʱ�����Ϊ����С��1������","����",JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(SmoothText.getText().equalsIgnoreCase("")){
						JOptionPane.showMessageDialog(this, "û������ƽ����","����",JOptionPane.ERROR_MESSAGE);
						return;
					}else if(Integer.parseInt(SmoothText.getText())<0||Integer.parseInt(SmoothText.getText())%1!=0){
						JOptionPane.showMessageDialog(this, "����ʱ�����Ϊ����С��0������","����",JOptionPane.ERROR_MESSAGE);
						return;
					}					
			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(this, "���붼����Ϊ������������С��������ĸ","����",JOptionPane.ERROR_MESSAGE);
			}
			/*
			//�������ݿ�
			try {
				this.con=DBConnection.getConnection();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			*/			
			//�õ��û�����
			this.usec=Integer.parseInt(TimeText.getText())*1000;
			this.numberOfScansToAverage=Integer.parseInt(AverageText.getText());
			this.smoothness=Integer.parseInt(SmoothText.getText());
			//����������,���ȵõ�����			
			this.OD=new OmniDriver();
			this.OD.OmniOperations(this.usec, this.numberOfScansToAverage, this.smoothness);
			this.Wavelengths=this.OD.getWaveLengths();
			/*-----��Ƭ������ָ��õ���ͬ�Ĺ���---*/
			/*-----2017.8.30�������ܣ�����У���������һ�λ�ȡ�������ݣ����ֻ��ȡ��Ʒ���ף����������У���������ѭ��*/
			//��һ��

//�����M 2017/8/30 17:10:19
//�ж��Ƿ����Զ�����,�����Զ����У�loopcounter=1
			if(isAutoRun==false) {
			loopCounter=1;
			}
			if(loopCounter==1) {
			//���ȷ���ָ�ǰ��10s,ֹͣ1�룻�õ���������
			SerialCommunication.portWrite(forward);
			Thread.sleep(1*500);
			SerialCommunication.portWrite(forward);
			Thread.sleep(10*1000);
			SerialCommunication.portWrite(stop);
			Thread.sleep(1*1000);
			this.B_LightIntensities=this.OD.getLigthIntensities();
			//Ȼ�󷢳�ָ��:����5�룬ֹͣ1�룻�õ��ο�����
			SerialCommunication.portWrite(backward);
			Thread.sleep(5*1000);
			SerialCommunication.portWrite(stop);
			Thread.sleep(1*1000);
			this.R_LightIntensities=this.OD.getLigthIntensities();
			//��󷢳�ָ�����5�룬ֹͣ1�룻�õ���Ʒ����
			SerialCommunication.portWrite(backward);
			Thread.sleep(5*1000);
			SerialCommunication.portWrite(stop);
			Thread.sleep(1*1000);
			this.S_LightIntensities=this.OD.getLigthIntensities();		
			}
			//��������
			else{
				this.S_LightIntensities=this.OD.getLigthIntensities();	
			}
			/*-----����õ������ʺ������-----*/
			Reflectivity_Absorbance RA=new Reflectivity_Absorbance(this.R_LightIntensities,this.B_LightIntensities,this.S_LightIntensities);
			this.reflectivity=RA.getReflectivity();			
			
			double[] originalAbsorbance=null;
			originalAbsorbance=RA.getAbsorbance();	
			//��������������޳�����
			this.absorbance=new double[512];
			int absorbance_index=0;
			for(int i=0;i<512;i++){
				this.absorbance[absorbance_index]=originalAbsorbance[i];
				absorbance_index++;
			}
			
			/*---����ɨ�����,ɨ��ʵʱʱ�����������ݵ����ݿ�---*/
			/*
			try {
				this.sampleNo=DBProcess.selectLastScannedSampleNo(this.con)+1;
				DBProcess.InsertScans(this.con, this.sampleNo, this.dateTime);
				DBProcess.StoreAbsorbance(this.con, this.sampleNo, this.absorbance);
			} catch (Exception e) {
				e.printStackTrace();
			}
			*/
			/*---ʵʱ���ݴ��浽�ļ�---*/
			//-------����txt-------//
			File absorbanceFile=new File("./Absorbance(txt)/");
			File[] absorbanceFiles=absorbanceFile.listFiles();
			//��¼ɨ��ʵʱʱ��
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			this.dateTime=df.format(new Date());
			String txtFileName=(absorbanceFiles.length+1)+"��, "+this.dateTime+".txt";
			txtFileName=txtFileName.replaceAll(":", "-");		
			dataStored(this.S_LightIntensities,"./Sample_LightIntensity/" +txtFileName); 
			dataStored(this.R_LightIntensities,"./Reference_LightIntensity/" +txtFileName); 
			dataStored(this.B_LightIntensities,"./Background_LightIntensity/" +txtFileName); 
			dataStored(this.reflectivity,"./Reflectivity/" +txtFileName);
			dataStored(this.absorbance,"./Absorbance(txt)/" +txtFileName); 
			dataStored(this.Wavelengths, "Wavelengths.txt");
			//dataStored(this.reflectivity, "Reflectivity.txt");	
			//------����excel------//
			//��ȡ��������
			SimpleDateFormat today = new SimpleDateFormat("yyyy-MM-dd");
			String todayDate=today.format(new Date());
			String excelFileName=todayDate+".xlsx";
			dataStoredExcel(this.B_LightIntensities, excelFileName, "./Background_LightIntensity/",false);
			dataStoredExcel(this.R_LightIntensities, excelFileName, "./Reference_LightIntensity/",false);
			dataStoredExcel(this.S_LightIntensities, excelFileName, "./Sample_LightIntensity/",false);	
			dataStoredExcel(this.reflectivity, excelFileName, "./Reflectivity/",false);
			dataStoredExcel(this.absorbance, excelFileName, "./Absorbance(excel)/",false);
			System.out.println("-------------111111------");			
			/*
			//����ȶ��ⰴɨ��˳�򴢴浽�ļ���
			//String A_path="Absorbance_sample "+sampleNo+" in "+dateTime+".txt ";
			dateTime=dateTime.replaceAll(":", ".");
			String A_path="./Absorbance/sample "+" in "+dateTime+".txt ";
			dataStored(this.absorbance, A_path);
			*/
			
			//------------�滭����ͼ------------------//
	        tchart.getSeries(0).add(this.Wavelengths, this.absorbance);
	        //���ù�����״̬
	        this.isRunned=true;	               
	        System.out.println("-------------2222222------");  
	        
		    /*-----------------------------���ݴ���------------------------------------------------*/	
	        /*---�ж�ѡ���Ԥ�����㷨---*/
	        //SNV����
	        if(preprocessBox.getSelectedItem().toString().equalsIgnoreCase("SNV")){		    
				try {
					SNV_MATLAB snv=new SNV_MATLAB(this.absorbance);
					this.afterPreprocess=snv.getSNVResult();
					System.out.println("-------------33333------");
				} catch (MWException | IOException e1) {
					e1.printStackTrace();
				}
	        }
	        //SG
	        else if(preprocessBox.getSelectedItem().toString().equalsIgnoreCase("SG")){
	        	try {
	        		SG_MATLAB sg=new SG_MATLAB(this.absorbance,(int)der.getSelectedItem(),(int)windowWidth.getSelectedItem(),(int)polynomialOrder.getSelectedItem());
					this.afterPreprocess=sg.getSGResult();
					System.out.println("-------------33333------");
				} catch (MWException | IOException e1) {
					e1.printStackTrace();
				}
	        }
	        //MSC
	        else if(preprocessBox.getSelectedItem().toString().equalsIgnoreCase("MSC")){
	        	 //�ж��Ƿ��Ѿ�����ģ�� ����Ҫ��ȡģ�͵�����Ⱦ���
				 if(this.model_absorbance==null){
						JOptionPane.showMessageDialog(this, "����ѡ��ģ�ͣ��Ӷ���ȡ����Ⱦ���","����",JOptionPane.ERROR_MESSAGE);
						return;
				 }
	        	 try {
					MSC_MATLAB msc=new MSC_MATLAB(this.absorbance, this.model_absorbance);
					this.afterPreprocess=msc.getMSCResult();
					System.out.println("-------------33333------");
				} catch (MWException e) {
					e.printStackTrace();
				}
	        }
	        /*---�ж�ѡ��Ķ�ԪУ���㷨---*/	        
			//PLS����
	        if(calibrationBox.getSelectedItem().toString().equalsIgnoreCase("PLS")){
	        	/*
	        	 //�ж��Ƿ��Ѿ�����ģ�� (PLS�㷨���£�B��֪������������ģ��)
				 if(this.model_absorbance==null){
						JOptionPane.showMessageDialog(this, "ʹ�ý�ģ�㷨ǰ��������ģ��","����",JOptionPane.ERROR_MESSAGE);
						return;
				 }
				 */
				 //�ж��Ƿ�Ԥ����������û��Ԥ������Ҫ��ʵʱ���������һά����ת��Ϊ��ά����
				 if(this.afterPreprocess==null){
					 this.afterPreprocess=new double[1][this.absorbance.length];
					 for(int i=0;i<this.absorbance.length;i++){
						 this.afterPreprocess[0][i]=this.absorbance[i];
					 }
				 }
				try {
					PLS_Algorithm pls=new PLS_Algorithm(this.afterPreprocess);
					this.concentration=pls.getConcentration();
					System.out.println("-------------4444444------");
				} catch (FileNotFoundException | MWException e1) {
					e1.printStackTrace();
				}
	        }
	        //PCA
	        else if(calibrationBox.getSelectedItem().toString().equalsIgnoreCase("PCA")){
	        	
	        }
			//------------------��ʾ���-----------------------------//
	        /*----ͳһ��ʾ----
	        if(this.concentration!=null){
	        StringBuffer resultDisplay=new StringBuffer();
	        for(int row=0;row<this.concentration.length;row++){
	        	for(int column=0;column<this.concentration[0].length;column++){
	        		resultDisplay.append(this.concentration[row][column]+", ");
	        	}
	        	resultDisplay.append("\n");
	        }
				finalResult.setText(resultDisplay.toString());			
	        }
	        */
	        /*---������ʾ���ҷ��ഢ�治ͬŨ��----*/
	        if(this.concentration!=null){
	        	switch (this.concentration[0].length) {
				case 1:
					//��ʾ
					biruet.setText(resultDisplay(this.concentration, 1));	
					//����
					if(this.biruetList.size()==100){
			        	this.biruetList.remove(0);
			        	this.biruetList.add(this.concentration[0][0]);
			        }else this.biruetList.add(this.concentration[0][0]);
					break;
				case 2:
					//������
					biruet.setText(resultDisplay(this.concentration, 1));
					if(this.biruetList.size()==100){
			        	this.biruetList.remove(0);
			        	this.biruetList.add(this.concentration[0][0]);
			        }else this.biruetList.add(this.concentration[0][0]);
					//ˮ��
					water.setText(resultDisplay(this.concentration, 2));
					if(this.waterList.size()==100){
			        	this.waterList.remove(0);
			        	this.waterList.add(this.concentration[0][1]);
			        }else this.waterList.add(this.concentration[0][1]);
					break;
				case 3:
					//������
				    biruet.setText(resultDisplay(this.concentration, 1));
				    if(this.biruetList.size()==100){
			        	this.biruetList.remove(0);
			        	this.biruetList.add(this.concentration[0][0]);
			        }else this.biruetList.add(this.concentration[0][0]);
				    //ˮ��
				    water.setText(resultDisplay(this.concentration, 2));
				    if(this.waterList.size()==100){
			        	this.waterList.remove(0);
			        	this.waterList.add(this.concentration[0][1]);
			        }else this.waterList.add(this.concentration[0][1]);
				    //����
				    carbamide.setText(resultDisplay(this.concentration, 3));
				    if(this.carbamideList.size()==100){
			        	this.carbamideList.remove(0);
			        	this.carbamideList.add(this.concentration[0][2]);
			        }else this.carbamideList.add(this.concentration[0][2]);
				    break;
				}	        	
	        }
	        /*----------------����Ũ������------------------*/
	        if(this.concentration!=null){
	        	for(int i=0;i<this.concentration.length;i++){
	        		if(i==0){
	        			dataStoredExcel(this.concentration[i],excelFileName, "./Concentration/",true);
	        		}
	        		else{
	        			dataStoredExcel(this.concentration[i],excelFileName, "./Concentration/",false);
	        		}
	        	}
	        }	        
	        //�Ƿ񱣴汾����������,�Ƿ���ı��ι����ļ����ļ���
	        //�Զ�����״̬Ĭ�ϱ������ݣ��������ļ���
	        if(this.isAutoRun==false){
	        if(preprocessBox.getSelectedItem().toString().equalsIgnoreCase("None")==false&&this.concentration.length!=0){
	        FileRename_UI FRUI=new FileRename_UI(txtFileName, preprocessBox.getSelectedItem().toString(),this.concentration.length);
	        FRUI.setVisible(true);	 
		}
	        }
	        //-------------�滭Ũ��ͼ---------------//
	        //�ֱ�滭��ͬŨ��ͼ
	        this.biruetChart.getSeries(0).add(getX_axis(this.biruetList.size()),this.biruetList);
	        this.waterChart.getSeries(0).add(getX_axis(this.waterList.size()),this.waterList);
	        this.carbamideChart.getSeries(0).add(getX_axis(this.carbamideList.size()),this.carbamideList);	        
		}
		/**
		 * ���ò�ͬŨ��ͼ�Ķ�Ӧ��������
		 * @param X_length ��������ĳ���
		 * @return
		 */
		public ArrayList<Double> getX_axis(int X_length){
			ArrayList<Double> X_axis=new ArrayList<>();
			for(double i=0;i<(double)X_length;i++){
				X_axis.add(i+1);
			}
			return X_axis;
		}
		/**
		 * ����Ũ�Ƚ��������ʾ
		 * @param concentration Ũ�Ⱦ���
		 * @param column Ũ�Ⱦ��������
		 * @return ����һ�����ɷֵ�Ũ��
		 */
		public String resultDisplay(double[][] concentration, int column){
			String result="";
			for(int i=0;i<concentration.length;i++){
				result=result+concentration[i][column-1]+"\n";
			}
			return  result;
		}
		/**
		 * ʵ�֡��Զ����С���ť���� (�Զ����м��ʱ�䣺10s):1)��Ƭ������ָ��;2)�õ����ݲ����棻3����ͼ
		 * @param evt
		 * @throws IOException 
		 * @throws InterruptedException 
		 */
		public void autoRunButtonMouseClicked(final java.awt.event.MouseEvent evt){
		///*----------------------timer����--------------
		  this.isAutoRun=true;
		  autoRunButton.setBackground(Color.yellow);
		  System.out.println(autoRunButton.getBackground());
          final Timer timer=new Timer();
          TimerTask tt=new TimerTask() {			
			@Override
			public void run() {												
              try {
            	//������������ڻ����У����������ü�����
            	loop=Integer.parseInt(loopText.getText());
            	if(loopCounter>=loop) {
            		loopCounter=0;
            	}
            	isRunned=true;
            	//У�����ѭ��������+1
            	loopCounter++;
            	System.out.println("loopcounter: "+loopCounter);
				RunButtonMouseClicked(evt);
				//�Ƿ�ֹͣ��ʱ��
		          if(isStoped==true){
		        	  timer.cancel();
		        	  isStoped=false;
		          }
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}							
			}
		};
		//����������ʱ����������ʱ��40s����ʱ����ʱ��0����ʱ��ʵ������ʱ��=��ʱ����������ʱ��40+�ֶ������ÿ�������е�ʱ����
		this.timeInterval=Integer.parseInt(intervalText.getText())*1000;
        timer.schedule(tt, 0, 40*1000+this.timeInterval);
		//*/
		
		/*----------------do while ����-------------------------
			this.isAutoRun=true;	
			do{
				try {
					RunButtonMouseClicked(evt);
					Thread.sleep(40*1000+this.timeInterval*1000);
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}while(this.isStoped==true);
		*/	
		}
		/**
		 * �������±��ļ���������
		 * @param data ����
		 * @param fileName ���±��ļ���
		 * @throws IOException
		 */
		public void dataStored(double[] data, String fileName) throws IOException{
			//�����ļ�
			File file=new File(fileName);
			if(!file.exists()){
				file.createNewFile();	
			}
			//���ݴ���
			FileWriter FW=new FileWriter(file);
			for(double d:data){	
				//����2λС��������������
				//BigDecimal b = new BigDecimal(LightIntensity);
				//double LI=b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 				
				//FW.write(Double.toString(LI)+"\r\n");
				FW.write(Double.toString(d)+"\r\n");
			}     
			FW.close();
		}
		/**
		 * ����Excel������������
		 * @param data ����
		 * @param fileName Excel �ļ���
		 * @param folderName �ļ�����
		 * @param isNewConcentration �ж��Ƿ����µ�Ũ�Ƚ��룬����ǣ���Ҫ���һ�У�����ǰ��Ũ����������
		 * @throws IOException
		 */
		public void dataStoredExcel(double[] data, String fileName,String folderName,boolean isNewConcentration) throws IOException{
			String excelFileName="";
			//�ж��ļ��Ƿ��Ѿ�����
			File file=new File(folderName);
			File[] absorbanceFiles=file.listFiles();
			//�����δ����,�Ǿ��½�excel�ļ�
			if(absorbanceFiles[absorbanceFiles.length-1].getName().equalsIgnoreCase(fileName)==false){
				XSSFWorkbook excel=new XSSFWorkbook();				
				@SuppressWarnings("unused")
				XSSFSheet sheet=excel.createSheet();
				excelFileName=folderName+fileName;
		       FileOutputStream FOS=new FileOutputStream(excelFileName);
		       excel.write(FOS);
			}
			else excelFileName=folderName+absorbanceFiles[absorbanceFiles.length-1].getName();
			//��ȡExcel�ļ�
			InputStream is = new FileInputStream(excelFileName);
			XSSFWorkbook Excel=new XSSFWorkbook(is);			
			XSSFSheet Sheet=Excel.getSheetAt(0);
			XSSFRow	row=Sheet.createRow(Sheet.getLastRowNum()+1);
			//�ж������Ƿ����µ�Ũ��
			if(isNewConcentration==true){
			//��һ��	
			row=Sheet.createRow(Sheet.getLastRowNum()+2);
			//��¼Ũ������ʱ��
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String cTime=sdf.format(new Date());
			XSSFCell timeCell=row.createCell(0);	
			timeCell.setCellValue(cTime);
			row=Sheet.createRow(Sheet.getLastRowNum()+1);
			}
			//д������
			for(int i=0;i<data.length;i++){
			XSSFCell cell=row.createCell(i);
			cell.setCellValue(data[i]);
			}
			//�����ļ�
			FileOutputStream FOS=new FileOutputStream(excelFileName);
			Excel.write(FOS);
		}
		/**
		 * ʵ�����в˵���Ŀ�Ĺ���
		 * @param e
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
		 /*---ʵ�� ���༭ͼ�� �˵���Ŀ����---*/
		 if(e.getSource()==editMenuItems){
			 ChartEditor.editChart(tchart.getChart());
		 }
		 /*---ʵ�� ������������������ �˵���Ŀ����---*/
		 else if(e.getSource()==featuresMenuItems){
			//�жϹ������Ƿ��Ѿ�����
			if(isRunned==false){
			JOptionPane.showMessageDialog(this, "�������й�����" ,"����",JOptionPane.ERROR_MESSAGE); 
			return;
			}
			try {
				this.OD.OmniFeatures();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			String features="�����ǵ�������: "+this.OD.getName()+"/n"+"���к���: "+this.OD.getID()+"/n"+"�汾��: "+this.OD.getVersion();
			JOptionPane.showMessageDialog(this, features ,"��������",JOptionPane.INFORMATION_MESSAGE); 
		}
		 /*---ʵ�� ��������ԡ� �˵���Ŀ����---*/
		 else if(e.getSource()==chipController){
			 ChipController_UI CCUI=new ChipController_UI();
			 CCUI.setVisible(true);
		 }
		 /*---ʵ�� �����ļ��� �˵���Ŀ����---*/
		 else if(e.getSource()==openFile){
			//�жϹ������Ƿ���������
			if(isRunned==true){
			JOptionPane.showMessageDialog(this, "���ȵ��ֹͣ��ť�رչ����ǣ��ٴ��ļ�" ,"����",JOptionPane.ERROR_MESSAGE);	
			return;
			}
			//�������·�� 
			File file=new File("./Absorbance(txt)");     
			fileChooser.setCurrentDirectory(file);
			//��ѡ��file only
			fileChooser.setMultiSelectionEnabled(true);
		    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		    FileNameExtensionFilter fileFilter=new FileNameExtensionFilter("���±��ļ�(.txt)", "txt");
		    fileChooser.setFileFilter(fileFilter);
		    fileChooser.setDialogTitle("��ѡ���ļ�");
		    int result=fileChooser.showDialog(null, "��");
		    if(result==JFileChooser.APPROVE_OPTION){
		    this.openFiles=fileChooser.getSelectedFiles();		    
		    }
		    //ѭ������滭���б�ѡ��Ĺ��׵�����
		    if(this.openFiles.length!=0){
		    	//�½���Ӧ�����Ĺ�������
			    series=new Series[this.openFiles.length];
			    tchart.removeAllSeries();
			    for(int s=0;s<series.length;s++){
			    	series[s]=new Line();
			    	tchart.addSeries(series[s]);
			    }
		    	String openFilePath="";		    	
		    for(int n=0;n<this.openFiles.length;n++){
		    File selectedFile=this.openFiles[n];		   
		    openFilePath=selectedFile.getPath();
		    //��ȡ���±��ļ��е�����
		    double[] Y_axis=new double[512];
		    int index=0;
		    try {
				Scanner s=new Scanner(selectedFile);
				while(s.hasNextDouble()){
					Y_axis[index]=s.nextDouble();
					index++;					
				}
				s.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}   
		    //ȷ�ϲ�������
		    int numberofwavelengths=0;
		    try {
		    	File file_test=new File("Wavelengths.txt");
				Scanner s_test=new Scanner(file_test);
				while(s_test.hasNextDouble()){
					s_test.nextDouble();
					numberofwavelengths++;
				}
				s_test.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}   
		    //��ȡ��������
		    double[] X_axis=new double[numberofwavelengths];
		    int index1=0;
		    try {
		    	File file2=new File("Wavelengths.txt");
				Scanner s=new Scanner(file2);
				while(s.hasNextDouble()){
					X_axis[index1]=s.nextDouble();	
					index1++;
				}
				s.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		    //���ݶ�ȡ�ļ��Ĳ�ͬ�����ò�ͬ��Y�����
		    if(openFilePath.contains("length")){
		    	JOptionPane.showMessageDialog(this, "����ѡ�񲨳��ļ�" ,"ѡ���ļ�����",JOptionPane.ERROR_MESSAGE);
		    	return;
		    }else if(openFilePath.contains("tivity")){
		    	tchart.getAxes().getLeft().getTitle().setText("������");
		    }else if(openFilePath.contains("sities")){
		    	tchart.getAxes().getLeft().getTitle().setText("��ǿ");
		    }		    
		    //���±��ƹ�������	    		    
		    tchart.getSeries(n).add(X_axis, Y_axis);
		    }
		    tchart.repaint();	
		 }
		 }
		 /*---ʵ�� ������������ �˵���Ŀ����---*/
		 else if(e.getSource()==export){
			 if(this.openFiles==null){
					JOptionPane.showMessageDialog(this, "�����ڴ��ļ�������ѡ���ļ�" ,"����",JOptionPane.ERROR_MESSAGE);	
					return;
					}
			 Export_UI eUI=new Export_UI(this.openFiles);
			 eUI.setVisible(true);
		 }
		 /*---ʵ�� ������ģ�͡� �˵���Ŀ����---*/
		 else if(e.getSource()==model){
			    //�������·�� 
				File file=new File("./Model/");     
				modelChooser.setCurrentDirectory(file);
				modelChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			    FileNameExtensionFilter fileFilter=new FileNameExtensionFilter("Excel�ļ�(.xlsx)", "xlsx");
			    modelChooser.setFileFilter(fileFilter);
			    modelChooser.setDialogTitle("��ѡ���ļ�");
			    int result=modelChooser.showDialog(null, "��");
			    String openFilePath="";
			    if(result==JFileChooser.APPROVE_OPTION){
			    openFilePath=modelChooser.getSelectedFile().getPath();		    
			    }
			    if(openFilePath.isEmpty()==false){
			    File selectedFile=new File(openFilePath);
			    /*---��ȡģ������---*/
			    try {
					InputStream is = new FileInputStream(selectedFile);
					XSSFWorkbook excel=new XSSFWorkbook(is);	
					//read sheet, index of sheet in excel starts from 1,while in java starts from 0					
					XSSFSheet sheet = excel.getSheetAt(0);
					//�����ҳ����ַ�����һ�е������������ֻ�ѧֵ�������
					int index=0;
					XSSFRow rowTest=sheet.getRow(0);
					for(int i=0;i<rowTest.getLastCellNum();i++){
					XSSFCell cellTest=rowTest.getCell(i);
					if(cellTest.getCellType()==Cell.CELL_TYPE_STRING){
						index=i;
						break;
					}	
					}

					this.model_absorbance=new double[sheet.getLastRowNum()+1][sheet.getRow(0).getLastCellNum()-1-index];
					this.model_chemicalValue=new double[sheet.getLastRowNum()+1][index];
					for(int rowNo=0;rowNo<sheet.getLastRowNum()+1;rowNo++){
						XSSFRow row=sheet.getRow(rowNo);
							for(int cellNo=0;cellNo<row.getLastCellNum();cellNo++){
								//��ȡģ�ͻ�ѧֵ
								if(cellNo<index){
									XSSFCell cell=row.getCell(cellNo);
									this.model_chemicalValue[rowNo][cellNo]=cell.getNumericCellValue();								
								}
								//��ȡģ�������
								else if(cellNo>index){
									XSSFCell cell=row.getCell(cellNo);							
									this.model_absorbance[rowNo][cellNo-index-1]=cell.getNumericCellValue();							
								}																		
							}				
					}	
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			    System.out.println(this.model_absorbance.length+"_"+this.model_absorbance[0].length+"   "+this.model_chemicalValue.length+"_"+this.model_chemicalValue[0].length);
			    System.out.println("ģ�Ͷ�ȡ���");////
			    }			   
		 }
		 /*---ʵ�֡�ѡ����ס��˵���Ŀ����---*/
		 else if(e.getSource()==absorbanceSelection){
			//�������·�� 
				File file=new File("./Absorbance(txt)");     
				absorbanceChooser.setCurrentDirectory(file);
				absorbanceChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			    FileNameExtensionFilter fileFilter=new FileNameExtensionFilter("���±��ļ�(.txt)", "txt");
			    absorbanceChooser.setFileFilter(fileFilter);
			    absorbanceChooser.setDialogTitle("��ѡ���ļ�");
			    int result=absorbanceChooser.showDialog(null, "��");
			    File selectedFile=null;
			    if(result==JFileChooser.APPROVE_OPTION){
			    selectedFile=absorbanceChooser.getSelectedFile();	    
			    }
			  //��ȡ���±��ļ��еĹ���
			  //���ж�����ĳ���
			    int points=0;
			    try{
			    	Scanner s=new Scanner(selectedFile);
				while(s.hasNextDouble()){
					s.nextDouble();
					points++;					
				}
				s.close();
			    }catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				this.selectedAbsorbance=new double[points];
			    int index=0;			    
			    try {
					Scanner s=new Scanner(selectedFile);			 
			 //��ȡ����
					while(s.hasNextDouble()){
						this.selectedAbsorbance[index]=s.nextDouble();
						index++;					
					}
					s.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			    System.out.println("������ѡ "+this.selectedAbsorbance.length);		
			    //System.out.println(this.selectedAbsorbance[0]+"   "+this.selectedAbsorbance[this.selectedAbsorbance.length-2]);	
			  //��ȡ��������
			    double[] X_axis=new double[512];
			    int index1=0;
			    try {
			    	File file2=new File("Wavelengths.txt");
					Scanner s=new Scanner(file2);
					while(s.hasNextDouble()){
						X_axis[index1]=s.nextDouble();
						index1++;					
					}
					s.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}			    			   		    
			    //���±��ƹ�������	    		    
			    tchart.getSeries(0).add(X_axis, this.selectedAbsorbance);  
			    tchart.repaint();
			    System.out.println("�ѻ���");	    
		 }
		 /*---ʵ�֡�ѡ���㷨���˵���Ŀ����---*/
		 else if(e.getSource()==algorithmsSelection){
			 //�ж��Ƿ��Ѿ�����ģ��
			 if(this.model_absorbance==null){
					JOptionPane.showMessageDialog(this, "��������ģ��","����",JOptionPane.ERROR_MESSAGE);
					return;
			 }
			//�ж��Ƿ��Ѿ�ѡ�����
			 if(this.selectedAbsorbance==null){
					JOptionPane.showMessageDialog(this, "����ѡ�����","����",JOptionPane.ERROR_MESSAGE);
					return;
			 }
			AlgorithmsSelection_UI ASUI=new AlgorithmsSelection_UI(this.selectedAbsorbance, this.model_chemicalValue, this.model_absorbance);
			 ASUI.setVisible(true);
		 }
		 
		 /*---ʵ�֡����ԡ��˵���Ŀ����---*/
		 else if(e.getSource()==chinese||e.getSource()==english){
			 //����
			if(e.getSource()==chinese){
				setTitle("���׻滭");
				figureMenu.setText("��ͼ");
				editMenuItems.setText("�༭ͼ��");		
				fileMenu.setText("�ļ�");
				openFile.setText("��");
				helpMenu.setText("����");
				featuresMenuItems.setText("��������������");
				dataProcess.setText("���ݴ���");
				model.setText("����ģ��");
				algorithmsSelection.setText("ѡ���㷨");
				languages.setText("����");
				chinese.setText("����");
				english.setText("English");
				TimeLable.setText("����ʱ�䣺����");
				AverageLable.setText("ƽ������");
				SmoothLable.setText("ƽ����");
				tchart.getChart().getTitle().setText("����ͼ");
				tchart.getAxes().getLeft().getTitle().setText("�����");
				tchart.getAxes().getBottom().getTitle().setText("����");		
		        RunButton.setText("����");
		        StopButton.setText("��ֹ");
		        chipResetButton.setText("�����λ");
		        windowLable.setText("���ڿ��");
		        polynomialLable.setText("����ʽ����");
		        derLable.setText("��������");
		        pcLable.setText("���ɷ���");
		        biruetLable.setText("������");
		        waterLable.setText("ˮ��");
		        carbamideLable.setText("����");
		        chipController.setText("�������");
		        absorbanceSelection.setText("ѡ�����");
		        intervalLable.setText("ʱ����");
		        export.setText("��������");
		        autoRunButton.setText("�Զ�����");
		        preprocessLable.setText("Ԥ�����㷨");
		        calibrationLable.setText("��ԪУ���㷨");
		        biruetChart.getChart().getTitle().setText("������Ũ��");
		        biruetChart.getAxes().getLeft().getTitle().setText("Ũ��");
		        biruetChart.getAxes().getBottom().getTitle().setText("����");
		        waterChart.getChart().getTitle().setText("ˮ��Ũ��");
		        waterChart.getAxes().getLeft().getTitle().setText("Ũ��");
		        waterChart.getAxes().getBottom().getTitle().setText("����");
		        carbamideChart.getChart().getTitle().setText("����Ũ��");
		        carbamideChart.getAxes().getLeft().getTitle().setText("Ũ��");
		        carbamideChart.getAxes().getBottom().getTitle().setText("����");
		        repaint();
			}
			//English
			else if(e.getSource()==english){
				setTitle("Spectrum Drawing");
				figureMenu.setText("Figure");
				editMenuItems.setText("Edit");	
				fileMenu.setText("File");
				openFile.setText("Open");
				helpMenu.setText("Help");
				featuresMenuItems.setText("Features");
				dataProcess.setText("Data Processing");
				model.setText("Import model");
				algorithmsSelection.setText("algorithm");
				languages.setText("Languages");
				chinese.setText("����");
				english.setText("English");
				TimeLable.setText("Integration:ms");
				AverageLable.setText("Average");
				SmoothLable.setText("Smooth");
				tchart.getChart().getTitle().setText("Spectrum");
				tchart.getAxes().getLeft().getTitle().setText("Absorbance");
				tchart.getAxes().getBottom().getTitle().setText("Wavelength");	
		        RunButton.setText("Run");
		        StopButton.setText("Stop");
		        chipResetButton.setText("Chip Reset");
		        windowLable.setText("Window");
		        polynomialLable.setText("Order");
		        derLable.setText("Derivative");
		        pcLable.setText("No_of_lv");
		        biruetLable.setText("Biuret");
		        waterLable.setText("Water");
		        carbamideLable.setText("Urea");
		        chipController.setText("Motor testing");
		        absorbanceSelection.setText("Choose spectrum");
		        intervalLable.setText("Interval");
		        export.setText("Export");
		        autoRunButton.setText("Auto Run");
		        preprocessLable.setText("Preprocess");
		        calibrationLable.setText("Calibration");
		        biruetChart.getChart().getTitle().setText("Biuret concentration");
		        biruetChart.getAxes().getLeft().getTitle().setText("Concentration");
		        biruetChart.getAxes().getBottom().getTitle().setText("Times");
		        waterChart.getChart().getTitle().setText("Water concentration");
		        waterChart.getAxes().getLeft().getTitle().setText("Concentration");
		        waterChart.getAxes().getBottom().getTitle().setText("Times");
		        carbamideChart.getChart().getTitle().setText("Urea concentration");
		        carbamideChart.getAxes().getLeft().getTitle().setText("Concentration");
		        carbamideChart.getAxes().getBottom().getTitle().setText("Times");

		        repaint();
			}
		 }
		}	
		/**
		 * ʵ��"��ֹ"��ť����
		 * @param evt
		 * @throws InterruptedException 
		 */
		public void StopButtonMouseClicked(java.awt.event.MouseEvent evt) throws InterruptedException{
			//�жϹ������Ƿ��Ѿ�����
			if(this.isRunned==false){
			JOptionPane.showMessageDialog(this, "�������й�����" ,"����",JOptionPane.ERROR_MESSAGE); 
			return;
			}
			//���ù�����״̬
			this.isStoped=true;
			autoRunButton.setBackground(color);
			/*
			//�ӳ�24���ù��ػ�������ȫ����λ���ٹرչ�����
			Thread.sleep(24*1000);
			this.OD.closeSpectrometers();
			*/
			/*
			//�Ͽ����ݿ�����
			try {
				DBConnection.ConnectionClose(this.con);
			} catch (Exception e) {
				e.printStackTrace();
			}
			*/
		}
		/**
		 * ʵ��"�����λ"��ť����
		 * @param evt
		 * @throws InterruptedException 
		 * @throws UnsupportedCommOperationException 
		 * @throws PortInUseException 
		 * @throws NoSuchPortException 
		 */
		public void chipResetButtonMouseClicked(java.awt.event.MouseEvent evt) throws InterruptedException, NoSuchPortException, PortInUseException, UnsupportedCommOperationException{
			ChipBoardReset CBR=new ChipBoardReset();
			if(CBR.getPortStatus().equalsIgnoreCase("closed")){
		        CBR.close();
		        System.out.println("------------��λ�ɹ����˿ڹر�---------------------");
		        JOptionPane.showMessageDialog(null, "��λ�ɹ�");
				}
		}
/**
 * ��������ʵ�����û�����
 * @param args
 */
		public static void main(String args[]){	
			UserInterface userInterface = null;
			try {
				 userInterface= new UserInterface();
			} catch (IOException e) {
				e.printStackTrace();
			}
			userInterface.setVisible(true);
		}
}

