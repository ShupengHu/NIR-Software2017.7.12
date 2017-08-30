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
		//积分时间
		private JLabel TimeLable=new JLabel();     
		private JTextField TimeText=new JTextField();             
		//平均次数
		private JLabel AverageLable=new JLabel();
		private JTextField AverageText=new JTextField();
		//平滑度
		private JLabel SmoothLable=new JLabel();
		private JTextField SmoothText=new JTextField();	
		//自动运行间隔时间
		private JLabel intervalLable=new JLabel();
		private JTextField intervalText=new JTextField();
		//校正间隔
		private JLabel loopLable=new JLabel();
		private JTextField loopText=new JTextField();
		//算法选择
		private JLabel preprocessLable=new JLabel();
		private String[] preAlgorithms={"None","SNV","SG","MSC"};		
		private JComboBox<String> preprocessBox=new JComboBox<String>(preAlgorithms);
		private JLabel calibrationLable=new JLabel();
		private String[] calibrationAlgorithms={"None","PLS","PCA"};
		private JComboBox<String> calibrationBox=new JComboBox<String>(calibrationAlgorithms);
		private JLabel windowLable=new JLabel("窗口宽度");
		private JLabel polynomialLable=new JLabel("多项式阶数");
		private JLabel pcLable=new JLabel("主成分数");
		private JLabel derLable=new JLabel("导数阶数");
		private JTextField pcText=new JTextField();
		private Integer[] windowWidthValues={7,9,11,13,15,17};
		private Integer[] polynomialOrderValues={2,3,4,5};
		private Integer[] derValues={1,2};
		private JComboBox<Integer> windowWidth=new JComboBox<Integer>(windowWidthValues);
		private JComboBox<Integer> polynomialOrder=new JComboBox<Integer>(polynomialOrderValues);
		private JComboBox<Integer> der=new JComboBox<Integer>(derValues);
		//最终结果：浓度	界面	
		private JLabel biruetLable=new JLabel("缩二脲");
		private JLabel waterLable=new JLabel("水分");
		private JLabel carbamideLable=new JLabel("尿素");
		private JTextArea biruet=new JTextArea();
		private JTextArea water=new JTextArea();
		private JTextArea carbamide=new JTextArea();
		private JScrollPane jspB=new JScrollPane(biruet,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		private JScrollPane jspW=new JScrollPane(water,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		private JScrollPane jspC=new JScrollPane(carbamide,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//曲线集
		private Series[] series=null;
		//用户输入参数
		int usec=0;                                             //积分时间
		int smoothness=0;                                       //平滑度
		int numberOfScansToAverage=0;	                        //平均扫描次数
		int timeInterval=0;                                     //自动运行时间间隔
		int loop=0;                                             //校正间隔
		int loopCounter=0;                                      //校正间隔循环计数器
		//其他参数
		OmniDriver OD=null;
		Connection con=null;
		String dateTime="";                                     //扫描实时时间
		int sampleNo=0;                                         //样品序号		
		double[] B_LightIntensities=null;                       //背景光谱的光强
		double[] R_LightIntensities=null;                       //参考光谱的光强
		double[] S_LightIntensities=null;                       //样品光谱的光强
		double[] Wavelengths=null;                              //波长
		double[] reflectivity=null;                             //反射率
		double[] absorbance=null;                               //吸光度
		double[][] model_absorbance=null;                       //模型吸光度
		double[][] model_chemicalValue=null;                    //模型化学值
		double[] selectedAbsorbance=null;                       //被手动选择用于数据分析的光谱
		double[][] afterPreprocess=null;                        //经过预处理后的数据
		double[][] concentration=null;                          //经过多元校正处理后得到的浓度矩阵
		ArrayList<Double> biruetList=new ArrayList<>();         //经过多元校正处理后得到的缩二脲浓度
		ArrayList<Double> waterList=new ArrayList<>();          //经过多元校正处理后得到的水分浓度
		ArrayList<Double> carbamideList=new ArrayList<>();      //经过多元校正处理后得到的尿素浓度
		ArrayList<Double> X=new ArrayList<>();                  //浓度图通用横坐标
		boolean isRunned=false;                                 //判断光谱仪是否已经开始工作
		boolean isStoped=false;                                 //判断光谱仪是否已经停止工作
		boolean isAutoRun=false;                                //是否为自动运行状态
		File[] openFiles=null;                                  //选择打开的文件集
		byte[] stop={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x10,(byte) 0xff,0x65};   //静止指令
		byte[] forward={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x11,(byte) 0xff,0x65};   //前进指令
		byte[] backward={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x01,(byte) 0xff,0x65};   //后退指令
		byte[] positionCheck={0x01,0x03,0x00,0x00,0x00,0x02,(byte) 0xff,0x65};        //电机复位自检指令
		
		//自动运行按钮背景色
		Color color=null;

		public UserInterface() throws IOException{
			 setDefaultCloseOperation(EXIT_ON_CLOSE);
			 init();			 
			 /*-------自动定时扫描-------*/
			 /*
			 if(isRunned!=false){
				 try {
					Thread.sleep(TimeInterval);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				 while(isStoped==false){
					 //再次扫描得到最新的光强和波长
					 this.S_LightIntensities=this.OD.getLigthIntensities();
				     this.Wavelengths=this.OD.getWaveLengths();
				     //再次计算反射率和吸光度
				     Reflectivity_Absorbance RA=new Reflectivity_Absorbance(this.R_LightIntensities,this.B_LightIntensities,this.S_LightIntensities);
					 this.reflectivity=RA.getReflectivity();
					 this.absorbance=RA.getAbsorbance();
					 */
				     /*---新数据储存到文件---*/
			 /*
				     dataStored(this.S_LightIntensities, "S_LightIntensities.txt");
				     dataStored(this.Wavelengths, "Wavelengths.txt");
				     dataStored(this.reflectivity, "Reflectivity.txt");
					 dataStored(this.absorbance, "Absorbance.txt");
					 //利用新数据重新画图		       
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
		 * 初始化
		 * @throws FileNotFoundException 
		 */
		public void init(){
			//设置整体界面大小
			setTitle("光谱绘画");
			//int width = Toolkit.getDefaultToolkit().getScreenSize().width; //得到当前屏幕分辨率的宽
			//int height = Toolkit.getDefaultToolkit().getScreenSize().height;//得到当前屏幕分辨率的高.			
			setSize(1600,900);
			setResizable(false);
			//设置容器
			contentPane.setLayout(null);
			contentPane.setSize(getMaximumSize());
			/*---设置菜单---*/
			menuBar.setBounds(new Rectangle(0, 0, 300, 30));
			//设置视图菜单
			figureMenu.setText("视图");
			figureMenu.setFont(new Font("宋体", Font.PLAIN, 12));
			figureMenu.setForeground(Color.black);	
			editMenuItems.setText("编辑图像");
			editMenuItems.setFont(new Font("宋体", Font.PLAIN, 12));
			editMenuItems.setForeground(Color.black);
			editMenuItems.addActionListener(this);			
			//设置文件菜单
			fileMenu.setText("文件");
			fileMenu.setFont(new Font("宋体", Font.PLAIN, 12));
			fileMenu.setForeground(Color.black);
			openFile.setText("打开");
			openFile.setFont(new Font("宋体", Font.PLAIN, 12));
			openFile.setForeground(Color.black);
			openFile.addActionListener(this);
			export.setText("批量导出");
			export.setFont(new Font("宋体", Font.PLAIN, 12));
			export.setForeground(Color.black);
			export.addActionListener(this);
			//设置帮助菜单
			helpMenu.setText("帮助");
			helpMenu.setFont(new Font("宋体", Font.PLAIN, 12));
			helpMenu.setForeground(Color.black);
			featuresMenuItems.setText("光谱仪特征参数");
			featuresMenuItems.setFont(new Font("宋体", Font.PLAIN, 12));
			featuresMenuItems.setForeground(Color.black);
			featuresMenuItems.addActionListener(this);
			chipController.setText("电机调试");
			chipController.setFont(new Font("宋体", Font.PLAIN, 12));
			chipController.setForeground(Color.black);
			chipController.addActionListener(this);
			//设置语言菜单
			languages.setText("语言");
			languages.setFont(new Font("宋体", Font.PLAIN, 12));
			languages.setForeground(Color.black);
			chinese.setText("中文");
			chinese.setFont(new Font("宋体", Font.PLAIN, 12));
			chinese.setForeground(Color.black);
			chinese.addActionListener(this);
			english.setText("English");
			english.setFont(new Font("Arial", Font.PLAIN, 12));
			english.setForeground(Color.black);
			english.addActionListener(this);
			//设置数据处理菜单
			dataProcess.setText("数据处理");
			dataProcess.setFont(new Font("宋体", Font.PLAIN, 12));
			dataProcess.setForeground(Color.black);
			model.setText("导入模型");
			model.setFont(new Font("宋体", Font.PLAIN, 12));
			model.setForeground(Color.black);
			model.addActionListener(this);
			absorbanceSelection.setText("选择光谱");
			absorbanceSelection.setFont(new Font("宋体", Font.PLAIN, 12));
			absorbanceSelection.setForeground(Color.black);
			absorbanceSelection.addActionListener(this);
			algorithmsSelection.setText("选择算法");
			algorithmsSelection.setFont(new Font("宋体", Font.PLAIN, 12));
			algorithmsSelection.setForeground(Color.black);
			algorithmsSelection.addActionListener(this);
			//添加菜单和菜单项目	
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
			//设置积分时间界面
			TimeLable.setBounds(new Rectangle(0, 30, 85, 50));
			TimeLable.setText("积分时间：毫秒");
			TimeLable.setFont(new Font("宋体", Font.PLAIN, 12));
			TimeLable.setForeground(Color.black);
			TimeText.setBounds(new Rectangle(85, 30, 40, 50));
			TimeText.setText("16");
			//设置平均次数界面
			AverageLable.setBounds(new Rectangle(200, 30, 50, 50));
			AverageLable.setText("平均次数");
			AverageLable.setFont(new Font("宋体", Font.PLAIN, 12));
			AverageLable.setForeground(Color.black);
			AverageText.setBounds(new Rectangle(250, 30, 40, 50));
			AverageText.setText("160");
			//设置平滑度界面
			SmoothLable.setBounds(new Rectangle(300, 30, 40, 50));
			SmoothLable.setText("平滑度");
			SmoothLable.setFont(new Font("宋体", Font.PLAIN, 12));
			SmoothLable.setForeground(Color.black);
			SmoothText.setBounds(new Rectangle(340, 30, 20, 50));
			SmoothText.setText("1");
			//设置自动运行时间间隔界面
			intervalLable.setBounds(new Rectangle(1040, 10, 70, 40));
			intervalLable.setText("时间间隔(s)");
			intervalLable.setFont(new Font("宋体", Font.PLAIN, 12));
			intervalLable.setForeground(Color.black);
			intervalText.setBounds(new Rectangle(1110, 10, 50, 40));
			//设置校正间隔界面
			loopLable.setBounds(new Rectangle(1170, 10, 50, 40));
			loopLable.setText("校正间隔");
			loopLable.setFont(new Font("宋体", Font.PLAIN, 12));
			loopLable.setForeground(Color.black);
			loopText.setBounds(new Rectangle(1220, 10, 50, 40));
			//设置算法选择界面
			preprocessLable.setBounds(new Rectangle(370, 30, 60, 20));
			preprocessLable.setText("预处理算法");
			preprocessLable.setFont(new Font("宋体", Font.PLAIN, 12));
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
			calibrationLable.setText("多元校正算法");
			calibrationLable.setFont(new Font("宋体", Font.PLAIN, 12));
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
			
			//设置最终结果界面
			biruetLable.setBounds(800, 0, 50, 30);
			waterLable.setBounds(800, 35, 50, 30);
			carbamideLable.setBounds(800, 70, 50, 30);
			jspB.setBounds(new Rectangle(860, 0, 150,30));
			jspW.setBounds(860, 35, 150,30);
			jspC.setBounds(860, 70, 150,30);
			//设置图像部分
			//--------光谱图-----------//
			tchart.setGraphics3D(null);
			//消除光谱图左边那一列数据
			tchart.getLegend().setVisible(false);
			tchart.setBounds(new Rectangle(0, 108, 900, 700));
			Series realS=new Line();
			tchart.removeAllSeries();
			tchart.addSeries(realS);			
			tchart.getAspect().setView3D(false);
			tchart.getChart().getTitle().setText("光谱图");
			tchart.getChart().getTitle().getFont().setSize(20);
			tchart.getChart().getTitle().getFont().setColor(Color.blue);
			tchart.getAxes().getLeft().getTitle().setText("吸光度");
			tchart.getAxes().getLeft().getTitle().getFont().setSize(20);
			tchart.getAxes().getLeft().getTitle().getFont().setColor(Color.blue);;
			tchart.getAxes().getBottom().getTitle().setText("波长");
			tchart.getAxes().getBottom().getTitle().getFont().setSize(20);
			tchart.getAxes().getBottom().getTitle().getFont().setColor(Color.blue);;
			//-----------浓度图--------------//
			//缩二脲
			biruetChart.setGraphics3D(null);
			biruetChart.getLegend().setVisible(false);
			biruetChart.setBounds(new Rectangle(1000, 108, 500, 200));
			Series biruetPoints=new Line();
			biruetChart.removeAllSeries();
			biruetChart.addSeries(biruetPoints);
			biruetChart.getAspect().setView3D(false);
			biruetChart.getChart().getTitle().setText("缩二脲浓度图");
			biruetChart.getChart().getTitle().getFont().setSize(20);
			biruetChart.getChart().getTitle().getFont().setColor(Color.blue);
			biruetChart.getAxes().getLeft().getTitle().setText("浓度");
			biruetChart.getAxes().getLeft().getTitle().getFont().setSize(20);
			biruetChart.getAxes().getLeft().getTitle().getFont().setColor(Color.blue);;
			biruetChart.getAxes().getBottom().getTitle().setText("次数");
			biruetChart.getAxes().getBottom().getTitle().getFont().setSize(20);
			biruetChart.getAxes().getBottom().getTitle().getFont().setColor(Color.blue);;
			//水分
			waterChart.setGraphics3D(null);
			waterChart.getLegend().setVisible(false);
			waterChart.setBounds(new Rectangle(1000, 350, 500, 200));
			Series waterPoints=new Line();
			waterChart.removeAllSeries();
			waterChart.addSeries(waterPoints);
			waterChart.getAspect().setView3D(false);
			waterChart.getChart().getTitle().setText("水分浓度图");
			waterChart.getChart().getTitle().getFont().setSize(20);
			waterChart.getChart().getTitle().getFont().setColor(Color.blue);
			waterChart.getAxes().getLeft().getTitle().setText("浓度");
			waterChart.getAxes().getLeft().getTitle().getFont().setSize(20);
			waterChart.getAxes().getLeft().getTitle().getFont().setColor(Color.blue);;
			waterChart.getAxes().getBottom().getTitle().setText("次数");
			waterChart.getAxes().getBottom().getTitle().getFont().setSize(20);
			waterChart.getAxes().getBottom().getTitle().getFont().setColor(Color.blue);;
			//尿素
			carbamideChart.setGraphics3D(null);
			carbamideChart.getLegend().setVisible(false);
			carbamideChart.setBounds(new Rectangle(1000, 592, 500, 200));
			Series carbamidePoints=new Line();
			carbamideChart.removeAllSeries();
			carbamideChart.addSeries(carbamidePoints);
			carbamideChart.getAspect().setView3D(false);
			carbamideChart.getChart().getTitle().setText("尿素浓度图");
			carbamideChart.getChart().getTitle().getFont().setSize(20);
			carbamideChart.getChart().getTitle().getFont().setColor(Color.blue);
			carbamideChart.getAxes().getLeft().getTitle().setText("浓度");
			carbamideChart.getAxes().getLeft().getTitle().getFont().setSize(20);
			carbamideChart.getAxes().getLeft().getTitle().getFont().setColor(Color.blue);;
			carbamideChart.getAxes().getBottom().getTitle().setText("次数");
			carbamideChart.getAxes().getBottom().getTitle().getFont().setSize(20);
			carbamideChart.getAxes().getBottom().getTitle().getFont().setColor(Color.blue);;	
	        //设置"运行"按钮
	         RunButton.setBounds(new Rectangle(1040, 50, 60, 50));
	         RunButton.setText("运行");
	         RunButton.setFont(new Font("宋体", Font.PLAIN, 12));
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
	       //设置"自动运行"按钮	  
	         color=autoRunButton.getBackground();
	         autoRunButton.setBounds(new Rectangle(1110, 50, 100, 50));
	         autoRunButton.setText("自动运行");
	         autoRunButton.setFont(new Font("宋体", Font.PLAIN, 12));
	         autoRunButton.setForeground(Color.black);
	         autoRunButton.addMouseListener(new java.awt.event.MouseAdapter(){
	         	public void mouseClicked(java.awt.event.MouseEvent evt){	         		
						autoRunButtonMouseClicked(evt);					
	         	}
	         });   
	       //设置"终止"按钮
	         StopButton.setBounds(new Rectangle(1220, 50, 60, 50));
	         StopButton.setText("终止");
	         StopButton.setFont(new Font("宋体", Font.PLAIN, 12));
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
	       //设置"电机复位"按钮
	         chipResetButton.setBounds(new Rectangle(1300, 50, 100, 50));
	         chipResetButton.setText("电机复位");
	         chipResetButton.setFont(new Font("宋体", Font.PLAIN, 12));
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
			//添加成分
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
		 * 实现“运行”按钮功能:1)向单片机输入指令;2)得到数据并储存；3）画图
		 * @param evt
		 * @throws IOException 
		 * @throws InterruptedException 
		 */
		public void RunButtonMouseClicked(java.awt.event.MouseEvent evt) throws IOException, InterruptedException{
			//判断用户输入是否合理
			try{
					if(TimeText.getText().equalsIgnoreCase("")){
						JOptionPane.showMessageDialog(this, "没有输入积分时间","错误",JOptionPane.ERROR_MESSAGE);
						return;
					}else if(Integer.parseInt(TimeText.getText())<1||Integer.parseInt(TimeText.getText())%1!=0){
						JOptionPane.showMessageDialog(this, "积分时间必须为不能小于100的整数","错误",JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(AverageText.getText().equalsIgnoreCase("")){
						JOptionPane.showMessageDialog(this, "没有输入平均次数","错误",JOptionPane.ERROR_MESSAGE);
						return;
					}else if(Integer.parseInt(AverageText.getText())<1||Integer.parseInt(AverageText.getText())%1!=0){
						JOptionPane.showMessageDialog(this, "积分时间必须为不能小于1的整数","错误",JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(SmoothText.getText().equalsIgnoreCase("")){
						JOptionPane.showMessageDialog(this, "没有输入平滑度","错误",JOptionPane.ERROR_MESSAGE);
						return;
					}else if(Integer.parseInt(SmoothText.getText())<0||Integer.parseInt(SmoothText.getText())%1!=0){
						JOptionPane.showMessageDialog(this, "积分时间必须为不能小于0的整数","错误",JOptionPane.ERROR_MESSAGE);
						return;
					}					
			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(this, "输入都必须为整数，不能是小数或是字母","错误",JOptionPane.ERROR_MESSAGE);
			}
			/*
			//开启数据库
			try {
				this.con=DBConnection.getConnection();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			*/			
			//得到用户输入
			this.usec=Integer.parseInt(TimeText.getText())*1000;
			this.numberOfScansToAverage=Integer.parseInt(AverageText.getText());
			this.smoothness=Integer.parseInt(SmoothText.getText());
			//操作光谱仪,首先得到波长			
			this.OD=new OmniDriver();
			this.OD.OmniOperations(this.usec, this.numberOfScansToAverage, this.smoothness);
			this.Wavelengths=this.OD.getWaveLengths();
			/*-----向单片机输入指令，得到不同的光谱---*/
			/*-----2017.8.30新增功能：增加校正间隔，第一次获取所有数据，其后只获取样品光谱，根据输入的校正间隔次数循环*/
			//第一次

//胡澍芃 2017/8/30 17:10:19
//判断是否是自动运行,不是自动运行，loopcounter=1
			if(isAutoRun==false) {
			loopCounter=1;
			}
			if(loopCounter==1) {
			//首先发出指令：前进10s,停止1秒；得到背景光谱
			SerialCommunication.portWrite(forward);
			Thread.sleep(1*500);
			SerialCommunication.portWrite(forward);
			Thread.sleep(10*1000);
			SerialCommunication.portWrite(stop);
			Thread.sleep(1*1000);
			this.B_LightIntensities=this.OD.getLigthIntensities();
			//然后发出指令:后退5秒，停止1秒；得到参考光谱
			SerialCommunication.portWrite(backward);
			Thread.sleep(5*1000);
			SerialCommunication.portWrite(stop);
			Thread.sleep(1*1000);
			this.R_LightIntensities=this.OD.getLigthIntensities();
			//最后发出指令：后退5秒，停止1秒；得到样品光谱
			SerialCommunication.portWrite(backward);
			Thread.sleep(5*1000);
			SerialCommunication.portWrite(stop);
			Thread.sleep(1*1000);
			this.S_LightIntensities=this.OD.getLigthIntensities();		
			}
			//其他次数
			else{
				this.S_LightIntensities=this.OD.getLigthIntensities();	
			}
			/*-----计算得到反射率和吸光度-----*/
			Reflectivity_Absorbance RA=new Reflectivity_Absorbance(this.R_LightIntensities,this.B_LightIntensities,this.S_LightIntensities);
			this.reflectivity=RA.getReflectivity();			
			
			double[] originalAbsorbance=null;
			originalAbsorbance=RA.getAbsorbance();	
			//光谱吸光度数据剔除处理
			this.absorbance=new double[512];
			int absorbance_index=0;
			for(int i=0;i<512;i++){
				this.absorbance[absorbance_index]=originalAbsorbance[i];
				absorbance_index++;
			}
			
			/*---储存扫描次数,扫描实时时间和吸光度数据到数据库---*/
			/*
			try {
				this.sampleNo=DBProcess.selectLastScannedSampleNo(this.con)+1;
				DBProcess.InsertScans(this.con, this.sampleNo, this.dateTime);
				DBProcess.StoreAbsorbance(this.con, this.sampleNo, this.absorbance);
			} catch (Exception e) {
				e.printStackTrace();
			}
			*/
			/*---实时数据储存到文件---*/
			//-------存入txt-------//
			File absorbanceFile=new File("./Absorbance(txt)/");
			File[] absorbanceFiles=absorbanceFile.listFiles();
			//记录扫描实时时间
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			this.dateTime=df.format(new Date());
			String txtFileName=(absorbanceFiles.length+1)+"号, "+this.dateTime+".txt";
			txtFileName=txtFileName.replaceAll(":", "-");		
			dataStored(this.S_LightIntensities,"./Sample_LightIntensity/" +txtFileName); 
			dataStored(this.R_LightIntensities,"./Reference_LightIntensity/" +txtFileName); 
			dataStored(this.B_LightIntensities,"./Background_LightIntensity/" +txtFileName); 
			dataStored(this.reflectivity,"./Reflectivity/" +txtFileName);
			dataStored(this.absorbance,"./Absorbance(txt)/" +txtFileName); 
			dataStored(this.Wavelengths, "Wavelengths.txt");
			//dataStored(this.reflectivity, "Reflectivity.txt");	
			//------存入excel------//
			//获取当日日期
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
			//吸光度额外按扫描顺序储存到文件夹
			//String A_path="Absorbance_sample "+sampleNo+" in "+dateTime+".txt ";
			dateTime=dateTime.replaceAll(":", ".");
			String A_path="./Absorbance/sample "+" in "+dateTime+".txt ";
			dataStored(this.absorbance, A_path);
			*/
			
			//------------绘画光谱图------------------//
	        tchart.getSeries(0).add(this.Wavelengths, this.absorbance);
	        //设置光谱仪状态
	        this.isRunned=true;	               
	        System.out.println("-------------2222222------");  
	        
		    /*-----------------------------数据处理------------------------------------------------*/	
	        /*---判断选择的预处理算法---*/
	        //SNV处理
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
	        	 //判断是否已经输入模型 。需要读取模型的吸光度矩阵
				 if(this.model_absorbance==null){
						JOptionPane.showMessageDialog(this, "请先选择模型，从而读取吸光度矩阵","错误",JOptionPane.ERROR_MESSAGE);
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
	        /*---判断选择的多元校正算法---*/	        
			//PLS处理
	        if(calibrationBox.getSelectedItem().toString().equalsIgnoreCase("PLS")){
	        	/*
	        	 //判断是否已经输入模型 (PLS算法更新：B已知，不必再输入模型)
				 if(this.model_absorbance==null){
						JOptionPane.showMessageDialog(this, "使用建模算法前请先输入模型","错误",JOptionPane.ERROR_MESSAGE);
						return;
				 }
				 */
				 //判断是否预处理过。如果没有预处理，需要将实时光谱吸光度一维数组转化为二维数组
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
			//------------------显示结果-----------------------------//
	        /*----统一显示----
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
	        /*---分类显示并且分类储存不同浓度----*/
	        if(this.concentration!=null){
	        	switch (this.concentration[0].length) {
				case 1:
					//显示
					biruet.setText(resultDisplay(this.concentration, 1));	
					//储存
					if(this.biruetList.size()==100){
			        	this.biruetList.remove(0);
			        	this.biruetList.add(this.concentration[0][0]);
			        }else this.biruetList.add(this.concentration[0][0]);
					break;
				case 2:
					//缩二脲
					biruet.setText(resultDisplay(this.concentration, 1));
					if(this.biruetList.size()==100){
			        	this.biruetList.remove(0);
			        	this.biruetList.add(this.concentration[0][0]);
			        }else this.biruetList.add(this.concentration[0][0]);
					//水分
					water.setText(resultDisplay(this.concentration, 2));
					if(this.waterList.size()==100){
			        	this.waterList.remove(0);
			        	this.waterList.add(this.concentration[0][1]);
			        }else this.waterList.add(this.concentration[0][1]);
					break;
				case 3:
					//缩二脲
				    biruet.setText(resultDisplay(this.concentration, 1));
				    if(this.biruetList.size()==100){
			        	this.biruetList.remove(0);
			        	this.biruetList.add(this.concentration[0][0]);
			        }else this.biruetList.add(this.concentration[0][0]);
				    //水分
				    water.setText(resultDisplay(this.concentration, 2));
				    if(this.waterList.size()==100){
			        	this.waterList.remove(0);
			        	this.waterList.add(this.concentration[0][1]);
			        }else this.waterList.add(this.concentration[0][1]);
				    //尿素
				    carbamide.setText(resultDisplay(this.concentration, 3));
				    if(this.carbamideList.size()==100){
			        	this.carbamideList.remove(0);
			        	this.carbamideList.add(this.concentration[0][2]);
			        }else this.carbamideList.add(this.concentration[0][2]);
				    break;
				}	        	
	        }
	        /*----------------储存浓度数据------------------*/
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
	        //是否保存本次所得数据,是否更改本次光谱文件的文件名
	        //自动运行状态默认保存数据，不更改文件名
	        if(this.isAutoRun==false){
	        if(preprocessBox.getSelectedItem().toString().equalsIgnoreCase("None")==false&&this.concentration.length!=0){
	        FileRename_UI FRUI=new FileRename_UI(txtFileName, preprocessBox.getSelectedItem().toString(),this.concentration.length);
	        FRUI.setVisible(true);	 
		}
	        }
	        //-------------绘画浓度图---------------//
	        //分别绘画不同浓度图
	        this.biruetChart.getSeries(0).add(getX_axis(this.biruetList.size()),this.biruetList);
	        this.waterChart.getSeries(0).add(getX_axis(this.waterList.size()),this.waterList);
	        this.carbamideChart.getSeries(0).add(getX_axis(this.carbamideList.size()),this.carbamideList);	        
		}
		/**
		 * 设置不同浓度图的对应横坐标轴
		 * @param X_length 横坐标轴的长度
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
		 * 最终浓度结果分类显示
		 * @param concentration 浓度矩阵
		 * @param column 浓度矩阵的列数
		 * @return 返回一种主成分的浓度
		 */
		public String resultDisplay(double[][] concentration, int column){
			String result="";
			for(int i=0;i<concentration.length;i++){
				result=result+concentration[i][column-1]+"\n";
			}
			return  result;
		}
		/**
		 * 实现“自动运行”按钮功能 (自动运行间隔时间：10s):1)向单片机输入指令;2)得到数据并储存；3）画图
		 * @param evt
		 * @throws IOException 
		 * @throws InterruptedException 
		 */
		public void autoRunButtonMouseClicked(final java.awt.event.MouseEvent evt){
		///*----------------------timer方法--------------
		  this.isAutoRun=true;
		  autoRunButton.setBackground(Color.yellow);
		  System.out.println(autoRunButton.getBackground());
          final Timer timer=new Timer();
          TimerTask tt=new TimerTask() {			
			@Override
			public void run() {												
              try {
            	//如果计数器大于或等于校正间隔，重置计数器
            	loop=Integer.parseInt(loopText.getText());
            	if(loopCounter>=loop) {
            		loopCounter=0;
            	}
            	isRunned=true;
            	//校正间隔循环计数器+1
            	loopCounter++;
            	System.out.println("loopcounter: "+loopCounter);
				RunButtonMouseClicked(evt);
				//是否停止计时器
		          if(isStoped==true){
		        	  timer.cancel();
		        	  isStoped=false;
		          }
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}							
			}
		};
		//设置启动计时器基本运行时间40s，延时启动时间0；计时器实际运行时间=计时器基本运行时间40+手动输入的每两次运行的时间间隔
		this.timeInterval=Integer.parseInt(intervalText.getText())*1000;
        timer.schedule(tt, 0, 40*1000+this.timeInterval);
		//*/
		
		/*----------------do while 方法-------------------------
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
		 * 创建记事本文件储存数据
		 * @param data 数据
		 * @param fileName 记事本文件名
		 * @throws IOException
		 */
		public void dataStored(double[] data, String fileName) throws IOException{
			//创建文件
			File file=new File(fileName);
			if(!file.exists()){
				file.createNewFile();	
			}
			//数据储存
			FileWriter FW=new FileWriter(file);
			for(double d:data){	
				//保留2位小数并且四舍五入
				//BigDecimal b = new BigDecimal(LightIntensity);
				//double LI=b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 				
				//FW.write(Double.toString(LI)+"\r\n");
				FW.write(Double.toString(d)+"\r\n");
			}     
			FW.close();
		}
		/**
		 * 创建Excel工作表储存数据
		 * @param data 数据
		 * @param fileName Excel 文件名
		 * @param folderName 文件夹名
		 * @param isNewConcentration 判断是否是新的浓度进入，如果是，需要多空一行，和以前的浓度数据区别开
		 * @throws IOException
		 */
		public void dataStoredExcel(double[] data, String fileName,String folderName,boolean isNewConcentration) throws IOException{
			String excelFileName="";
			//判断文件是否已经创建
			File file=new File(folderName);
			File[] absorbanceFiles=file.listFiles();
			//如果尚未创建,那就新建excel文件
			if(absorbanceFiles[absorbanceFiles.length-1].getName().equalsIgnoreCase(fileName)==false){
				XSSFWorkbook excel=new XSSFWorkbook();				
				@SuppressWarnings("unused")
				XSSFSheet sheet=excel.createSheet();
				excelFileName=folderName+fileName;
		       FileOutputStream FOS=new FileOutputStream(excelFileName);
		       excel.write(FOS);
			}
			else excelFileName=folderName+absorbanceFiles[absorbanceFiles.length-1].getName();
			//读取Excel文件
			InputStream is = new FileInputStream(excelFileName);
			XSSFWorkbook Excel=new XSSFWorkbook(is);			
			XSSFSheet Sheet=Excel.getSheetAt(0);
			XSSFRow	row=Sheet.createRow(Sheet.getLastRowNum()+1);
			//判断输入是否是新的浓度
			if(isNewConcentration==true){
			//空一行	
			row=Sheet.createRow(Sheet.getLastRowNum()+2);
			//记录浓度生成时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String cTime=sdf.format(new Date());
			XSSFCell timeCell=row.createCell(0);	
			timeCell.setCellValue(cTime);
			row=Sheet.createRow(Sheet.getLastRowNum()+1);
			}
			//写入数据
			for(int i=0;i<data.length;i++){
			XSSFCell cell=row.createCell(i);
			cell.setCellValue(data[i]);
			}
			//保存文件
			FileOutputStream FOS=new FileOutputStream(excelFileName);
			Excel.write(FOS);
		}
		/**
		 * 实现所有菜单项目的功能
		 * @param e
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
		 /*---实现 “编辑图像” 菜单项目功能---*/
		 if(e.getSource()==editMenuItems){
			 ChartEditor.editChart(tchart.getChart());
		 }
		 /*---实现 “光谱仪特征参数” 菜单项目功能---*/
		 else if(e.getSource()==featuresMenuItems){
			//判断光谱仪是否已经运行
			if(isRunned==false){
			JOptionPane.showMessageDialog(this, "请先运行光谱仪" ,"错误",JOptionPane.ERROR_MESSAGE); 
			return;
			}
			try {
				this.OD.OmniFeatures();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			String features="光谱仪的名字是: "+this.OD.getName()+"/n"+"序列号是: "+this.OD.getID()+"/n"+"版本是: "+this.OD.getVersion();
			JOptionPane.showMessageDialog(this, features ,"特征参数",JOptionPane.INFORMATION_MESSAGE); 
		}
		 /*---实现 “电机调试” 菜单项目功能---*/
		 else if(e.getSource()==chipController){
			 ChipController_UI CCUI=new ChipController_UI();
			 CCUI.setVisible(true);
		 }
		 /*---实现 “打开文件” 菜单项目功能---*/
		 else if(e.getSource()==openFile){
			//判断光谱仪是否正在运行
			if(isRunned==true){
			JOptionPane.showMessageDialog(this, "请先点击停止按钮关闭光谱仪，再打开文件" ,"错误",JOptionPane.ERROR_MESSAGE);	
			return;
			}
			//设置相对路径 
			File file=new File("./Absorbance(txt)");     
			fileChooser.setCurrentDirectory(file);
			//多选，file only
			fileChooser.setMultiSelectionEnabled(true);
		    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		    FileNameExtensionFilter fileFilter=new FileNameExtensionFilter("记事本文件(.txt)", "txt");
		    fileChooser.setFileFilter(fileFilter);
		    fileChooser.setDialogTitle("请选择文件");
		    int result=fileChooser.showDialog(null, "打开");
		    if(result==JFileChooser.APPROVE_OPTION){
		    this.openFiles=fileChooser.getSelectedFiles();		    
		    }
		    //循环输出绘画所有被选择的光谱的曲线
		    if(this.openFiles.length!=0){
		    	//新建对应数量的光谱曲线
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
		    //读取记事本文件中的数据
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
		    //确认波长个数
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
		    //读取波长数据
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
		    //根据读取文件的不同，设置不同的Y轴界面
		    if(openFilePath.contains("length")){
		    	JOptionPane.showMessageDialog(this, "不能选择波长文件" ,"选择文件错误",JOptionPane.ERROR_MESSAGE);
		    	return;
		    }else if(openFilePath.contains("tivity")){
		    	tchart.getAxes().getLeft().getTitle().setText("反射率");
		    }else if(openFilePath.contains("sities")){
		    	tchart.getAxes().getLeft().getTitle().setText("光强");
		    }		    
		    //重新编制光谱曲线	    		    
		    tchart.getSeries(n).add(X_axis, Y_axis);
		    }
		    tchart.repaint();	
		 }
		 }
		 /*---实现 “批量导出” 菜单项目功能---*/
		 else if(e.getSource()==export){
			 if(this.openFiles==null){
					JOptionPane.showMessageDialog(this, "请先在打开文件功能中选择文件" ,"错误",JOptionPane.ERROR_MESSAGE);	
					return;
					}
			 Export_UI eUI=new Export_UI(this.openFiles);
			 eUI.setVisible(true);
		 }
		 /*---实现 “导入模型” 菜单项目功能---*/
		 else if(e.getSource()==model){
			    //设置相对路径 
				File file=new File("./Model/");     
				modelChooser.setCurrentDirectory(file);
				modelChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			    FileNameExtensionFilter fileFilter=new FileNameExtensionFilter("Excel文件(.xlsx)", "xlsx");
			    modelChooser.setFileFilter(fileFilter);
			    modelChooser.setDialogTitle("请选择文件");
			    int result=modelChooser.showDialog(null, "打开");
			    String openFilePath="";
			    if(result==JFileChooser.APPROVE_OPTION){
			    openFilePath=modelChooser.getSelectedFile().getPath();		    
			    }
			    if(openFilePath.isEmpty()==false){
			    File selectedFile=new File(openFilePath);
			    /*---读取模型数据---*/
			    try {
					InputStream is = new FileInputStream(selectedFile);
					XSSFWorkbook excel=new XSSFWorkbook(is);	
					//read sheet, index of sheet in excel starts from 1,while in java starts from 0					
					XSSFSheet sheet = excel.getSheetAt(0);
					//首先找出有字符的那一列的列数，来区分化学值和吸光度
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
								//读取模型化学值
								if(cellNo<index){
									XSSFCell cell=row.getCell(cellNo);
									this.model_chemicalValue[rowNo][cellNo]=cell.getNumericCellValue();								
								}
								//读取模型吸光度
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
			    System.out.println("模型读取完毕");////
			    }			   
		 }
		 /*---实现“选择光谱”菜单项目功能---*/
		 else if(e.getSource()==absorbanceSelection){
			//设置相对路径 
				File file=new File("./Absorbance(txt)");     
				absorbanceChooser.setCurrentDirectory(file);
				absorbanceChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			    FileNameExtensionFilter fileFilter=new FileNameExtensionFilter("记事本文件(.txt)", "txt");
			    absorbanceChooser.setFileFilter(fileFilter);
			    absorbanceChooser.setDialogTitle("请选择文件");
			    int result=absorbanceChooser.showDialog(null, "打开");
			    File selectedFile=null;
			    if(result==JFileChooser.APPROVE_OPTION){
			    selectedFile=absorbanceChooser.getSelectedFile();	    
			    }
			  //读取记事本文件中的光谱
			  //先判断数组的长度
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
			 //读取数据
					while(s.hasNextDouble()){
						this.selectedAbsorbance[index]=s.nextDouble();
						index++;					
					}
					s.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			    System.out.println("光谱已选 "+this.selectedAbsorbance.length);		
			    //System.out.println(this.selectedAbsorbance[0]+"   "+this.selectedAbsorbance[this.selectedAbsorbance.length-2]);	
			  //读取波长数据
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
			    //重新编制光谱曲线	    		    
			    tchart.getSeries(0).add(X_axis, this.selectedAbsorbance);  
			    tchart.repaint();
			    System.out.println("已绘制");	    
		 }
		 /*---实现“选择算法”菜单项目功能---*/
		 else if(e.getSource()==algorithmsSelection){
			 //判断是否已经输入模型
			 if(this.model_absorbance==null){
					JOptionPane.showMessageDialog(this, "请先输入模型","错误",JOptionPane.ERROR_MESSAGE);
					return;
			 }
			//判断是否已经选择光谱
			 if(this.selectedAbsorbance==null){
					JOptionPane.showMessageDialog(this, "请先选择光谱","错误",JOptionPane.ERROR_MESSAGE);
					return;
			 }
			AlgorithmsSelection_UI ASUI=new AlgorithmsSelection_UI(this.selectedAbsorbance, this.model_chemicalValue, this.model_absorbance);
			 ASUI.setVisible(true);
		 }
		 
		 /*---实现“语言”菜单项目功能---*/
		 else if(e.getSource()==chinese||e.getSource()==english){
			 //中文
			if(e.getSource()==chinese){
				setTitle("光谱绘画");
				figureMenu.setText("视图");
				editMenuItems.setText("编辑图像");		
				fileMenu.setText("文件");
				openFile.setText("打开");
				helpMenu.setText("帮助");
				featuresMenuItems.setText("光谱仪特征参数");
				dataProcess.setText("数据处理");
				model.setText("导入模型");
				algorithmsSelection.setText("选择算法");
				languages.setText("语言");
				chinese.setText("中文");
				english.setText("English");
				TimeLable.setText("积分时间：毫秒");
				AverageLable.setText("平均次数");
				SmoothLable.setText("平滑度");
				tchart.getChart().getTitle().setText("光谱图");
				tchart.getAxes().getLeft().getTitle().setText("吸光度");
				tchart.getAxes().getBottom().getTitle().setText("波长");		
		        RunButton.setText("运行");
		        StopButton.setText("终止");
		        chipResetButton.setText("电机复位");
		        windowLable.setText("窗口宽度");
		        polynomialLable.setText("多项式阶数");
		        derLable.setText("导数阶数");
		        pcLable.setText("主成分数");
		        biruetLable.setText("缩二脲");
		        waterLable.setText("水分");
		        carbamideLable.setText("尿素");
		        chipController.setText("电机调试");
		        absorbanceSelection.setText("选择光谱");
		        intervalLable.setText("时间间隔");
		        export.setText("批量导出");
		        autoRunButton.setText("自动运行");
		        preprocessLable.setText("预处理算法");
		        calibrationLable.setText("多元校正算法");
		        biruetChart.getChart().getTitle().setText("缩二脲浓度");
		        biruetChart.getAxes().getLeft().getTitle().setText("浓度");
		        biruetChart.getAxes().getBottom().getTitle().setText("次数");
		        waterChart.getChart().getTitle().setText("水分浓度");
		        waterChart.getAxes().getLeft().getTitle().setText("浓度");
		        waterChart.getAxes().getBottom().getTitle().setText("次数");
		        carbamideChart.getChart().getTitle().setText("尿素浓度");
		        carbamideChart.getAxes().getLeft().getTitle().setText("浓度");
		        carbamideChart.getAxes().getBottom().getTitle().setText("次数");
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
				chinese.setText("中文");
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
		 * 实现"终止"按钮功能
		 * @param evt
		 * @throws InterruptedException 
		 */
		public void StopButtonMouseClicked(java.awt.event.MouseEvent evt) throws InterruptedException{
			//判断光谱仪是否已经运行
			if(this.isRunned==false){
			JOptionPane.showMessageDialog(this, "请先运行光谱仪" ,"错误",JOptionPane.ERROR_MESSAGE); 
			return;
			}
			//设置光谱仪状态
			this.isStoped=true;
			autoRunButton.setBackground(color);
			/*
			//延迟24秒让工控机运行完全，复位后再关闭光谱仪
			Thread.sleep(24*1000);
			this.OD.closeSpectrometers();
			*/
			/*
			//断开数据库连接
			try {
				DBConnection.ConnectionClose(this.con);
			} catch (Exception e) {
				e.printStackTrace();
			}
			*/
		}
		/**
		 * 实现"电机复位"按钮功能
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
		        System.out.println("------------复位成功，端口关闭---------------------");
		        JOptionPane.showMessageDialog(null, "复位成功");
				}
		}
/**
 * 主方法：实例化用户界面
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

