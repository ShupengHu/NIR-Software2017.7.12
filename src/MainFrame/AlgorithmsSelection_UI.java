package MainFrame;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mathworks.toolbox.javabuilder.MWException;

import calibration.PLS_Algorithm;
import preprocess.SG_MATLAB;
import preprocess.SNV_MATLAB;

@SuppressWarnings("serial")
public class AlgorithmsSelection_UI extends JFrame{
	private JPanel contentPane=(JPanel) getContentPane();
	private String[] preAlgorithms={"None","SNV","SG","MSC"};
	private String[] calibrationAlgorithms={"None","PLS","PCA"};	
	private Integer[] windowWidthValues={7,9,11,13,15,17};
	private Integer[] polynomialOrderValues={2,3,4,5};
	private Integer[] derValues={1,2};
	private JComboBox<String> firstBox=new JComboBox<String>(preAlgorithms);
	private JComboBox<String> secondBox=new JComboBox<String>(preAlgorithms);
	private JComboBox<String> thirdBox=new JComboBox<String>(preAlgorithms);
	private JComboBox<String> calibrationBox=new JComboBox<String>(calibrationAlgorithms);
	private JLabel firstLable=new JLabel("预处理算法1");
	private JLabel secondLable=new JLabel("预处理算法2");
	private JLabel thirdLable=new JLabel("预处理算法3");
	private JLabel calibrationLable=new JLabel("多元校正算法");
	private JLabel windowLable=new JLabel("窗口宽度");
	private JLabel polynomialLable=new JLabel("多项式阶数");
	private JLabel derLable=new JLabel("导数阶数");
	private JLabel pcLable=new JLabel("主成分数");
	private JTextField pcText=new JTextField();
	private JComboBox<Integer> windowWidth=new JComboBox<Integer>(windowWidthValues);
	private JComboBox<Integer> polynomialOrder=new JComboBox<Integer>(polynomialOrderValues);
	private JComboBox<Integer> der=new JComboBox<Integer>(derValues);
	private JButton button=new JButton("确定");
	private JTextArea finalResult=new JTextArea();
	private JScrollPane jsp=new JScrollPane(finalResult,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	double[] absorbance=null;
	double[][] model_chemicalValue=null;
	double[][] model_absorbance=null;
	double[][] afterPreprocess=null;
	double[][] concentration=null;
	
public AlgorithmsSelection_UI(double[] selectedAbsorbance,double[][] model_chemicalValue, double[][] model_absorbance){
	this.absorbance=selectedAbsorbance;
	this.model_chemicalValue=model_chemicalValue;
	this.model_absorbance=model_absorbance;
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	init();
	//关闭时不退出上面的窗口
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
}
public void init(){	
	int width=Toolkit.getDefaultToolkit().getScreenSize().width;
	int height=Toolkit.getDefaultToolkit().getScreenSize().height;
	this.setBounds(width/3,height/3,1000,500);
	contentPane.setSize(getMaximumSize());
	contentPane.setLayout(null);
	firstLable.setBounds(0, 60, 100, 50);
	secondLable.setBounds(0, 140, 100, 50);
	thirdLable.setBounds(0, 220, 100, 50);
	calibrationLable.setBounds(0, 300, 100, 50);
	firstBox.setBounds(110, 60, 100, 50);
	firstBox.setEditable(false);  
	firstBox.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	    	  if(e.getSource()==firstBox){
	    			if(firstBox.getSelectedItem()=="SG"){
	    				windowLable.setBounds(220, 60, 60, 50);
	    				windowWidth.setBounds(280, 60, 100, 50);
	    				windowWidth.setEditable(false);
	    				polynomialLable.setBounds(380, 60, 80, 50);
	    				polynomialOrder.setBounds(460, 60, 100, 50);
	    				polynomialOrder.setEditable(false);
	    				derLable.setBounds(560,60,60,50);
	    				der.setBounds(620,60,100,50);
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
	secondBox.setBounds(110, 140, 100, 50);
	secondBox.setEditable(false);  
	secondBox.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	    	  if(e.getSource()==secondBox){
	    			if(secondBox.getSelectedItem()=="SG"){
	    				windowLable.setBounds(220, 140, 60, 50);
	    				windowWidth.setBounds(280, 140, 100, 50);
	    				windowWidth.setEditable(false);
	    				polynomialLable.setBounds(380, 140, 80, 50);
	    				polynomialOrder.setBounds(460, 140, 100, 50);
	    				polynomialOrder.setEditable(false);
	    				derLable.setBounds(560,140,60,50);
	    				der.setBounds(620,140,100,50);
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
	thirdBox.setBounds(110, 220, 100, 50);
	thirdBox.setEditable(false); 
	thirdBox.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	    	  if(e.getSource()==thirdBox){
	    			if(thirdBox.getSelectedItem()=="SG"){
	    				windowLable.setBounds(220, 220, 60, 50);
	    				windowWidth.setBounds(280, 220, 100, 50);
	    				windowWidth.setEditable(false);
	    				polynomialLable.setBounds(380, 220, 80, 50);
	    				polynomialOrder.setBounds(460, 220, 100, 50);
	    				polynomialOrder.setEditable(false);
	    				derLable.setBounds(560,220,60,50);
	    				der.setBounds(620,220,100,50);
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
	calibrationBox.setBounds(110, 300, 100, 50);
	calibrationBox.setEditable(false); 
	calibrationBox.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	    	  if(e.getSource()==calibrationBox){
	    			if(calibrationBox.getSelectedItem()=="PLS"){
	    				pcLable.setBounds(220, 300, 60, 50);
                        pcText.setBounds(280, 300, 100, 50);
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
	button.setBounds(new Rectangle(500, 400, 70, 50));
	button.addMouseListener(new java.awt.event.MouseAdapter(){
      	public void mouseClicked(java.awt.event.MouseEvent evt){
      		try {
				ButtonMouseClicked(evt);
			} catch (IOException | MWException e) {
				e.printStackTrace();
			}
      	}
      });
	jsp.setBounds(735, 0, 230, 400);
	contentPane.add(jsp);
	contentPane.add(firstLable);
	contentPane.add(secondLable);
	contentPane.add(thirdLable);
	contentPane.add(calibrationLable);
    contentPane.add(button);
    contentPane.add(firstBox);
    contentPane.add(secondBox);
    contentPane.add(thirdBox);
    contentPane.add(calibrationBox);
}
public void ButtonMouseClicked(java.awt.event.MouseEvent evt) throws IOException, MWException{
	String algorithm =firstBox.getSelectedItem().toString();
	//SNV
	if(algorithm.equalsIgnoreCase("SNV")){
		SNV_MATLAB snv=new SNV_MATLAB(this.absorbance);
		this.afterPreprocess=snv.getSNVResult();
		System.out.println("-------------33333------");
	}
	//SG
	else if(algorithm.equalsIgnoreCase("SG")){
		SG_MATLAB sg=new SG_MATLAB(this.absorbance,(int)der.getSelectedItem(),(int)windowWidth.getSelectedItem(),(int)polynomialOrder.getSelectedItem());
		this.afterPreprocess=sg.getSGResult();
		System.out.println("-------------33333------");
	}
	//MSC
	else if(algorithm.equalsIgnoreCase("MSC")){
		
	}
	if(secondBox.getSelectedItem()!="None"){			
		}
	if(thirdBox.getSelectedItem()!="None"){	
		}
	if(calibrationBox.getSelectedItem()=="PLS"){
		//判断是否预处理过。如果没有预处理，需要将实时光谱吸光度一维数组转化为二维数组
		 if(this.afterPreprocess==null){
			 this.afterPreprocess=new double[1][this.absorbance.length];
			 for(int i=0;i<this.absorbance.length;i++){
				 this.afterPreprocess[0][i]=this.absorbance[i];
			 }
		 }
		PLS_Algorithm pls=new PLS_Algorithm(this.afterPreprocess);
		this.concentration=pls.getConcentration();
		System.out.println("-------------4444444------");
	}	
	//显示结果
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
	//储存浓度数据
		InputStream is = new FileInputStream("Offline Concentration.xlsx");
		XSSFWorkbook Excel=new XSSFWorkbook(is);			
		XSSFSheet Sheet=Excel.getSheetAt(0);
		for(int n=0;n<this.concentration.length;n++){
		XSSFRow	row=Sheet.createRow(Sheet.getLastRowNum()+1);				
		for(int m=0;m<this.concentration[n].length;m++){
		//BigDecimal bb = new BigDecimal(this.Result[n1][m]);
		//double B=bb.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue(); 		
		XSSFCell cell=row.createCell(m);
		cell.setCellValue(this.concentration[n][m]);
		}
		}
		FileOutputStream FOS=new FileOutputStream("Offline Concentration.xlsx");
		Excel.write(FOS);
}
}
