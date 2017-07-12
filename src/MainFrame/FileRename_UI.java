package MainFrame;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@SuppressWarnings("serial")
public class FileRename_UI extends JFrame{
	private JPanel contentPane=(JPanel) getContentPane();
	private JTextField fileText=new JTextField();
	private JLabel fileLable=new JLabel("�ļ���");
	private JButton button1=new JButton("ȷ��");
	private JButton button2=new JButton("������");
	private String FileName ="";
	private String preprocessName="";
	private int concentration_length=0;
public FileRename_UI(String fileName, String preprocessName, int concentraiton_length){
	this.FileName=fileName;
	this.preprocessName=preprocessName;
	this.concentration_length=concentraiton_length;
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	init();
	//�ر�ʱ���˳�����Ĵ���
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
}
public void init(){	
	int width=Toolkit.getDefaultToolkit().getScreenSize().width;
	int height=Toolkit.getDefaultToolkit().getScreenSize().height;
	this.setBounds(width/3,height/3,1000,500);
	contentPane.setSize(getMaximumSize());
	contentPane.setLayout(null);	
	fileLable.setBounds(0, 60, 100, 50);
	fileText.setBounds(110, 60, 800, 50);
	fileText.setText(this.FileName);
	button1.setBounds(new Rectangle(500, 400, 70, 50));
	button1.addMouseListener(new java.awt.event.MouseAdapter(){
      	public void mouseClicked(java.awt.event.MouseEvent evt){      		
				Button1MouseClicked(evt);			
      	}
      });
	button2.setBounds(new Rectangle(600, 400, 70, 50));
	button2.addMouseListener(new java.awt.event.MouseAdapter(){
      	public void mouseClicked(java.awt.event.MouseEvent evt){      		
				try {
					Button2MouseClicked(evt);
				} catch (IOException e) {
					e.printStackTrace();
				}			
      	}
      });
	contentPane.add(fileLable);
    contentPane.add(fileText);
    contentPane.add(button1);
    contentPane.add(button2);
}
public void Button1MouseClicked(java.awt.event.MouseEvent evt) {
	String expectedName=fileText.getText();
	if(expectedName.equals(this.FileName)){
		dispose();
	}else {
		File originalfile=new File("./Absorbance/"+this.FileName);
		File newFile=new File("./Absorbance/"+expectedName);
		originalfile.renameTo(newFile);
	}
	dispose();
}
public void Button2MouseClicked(java.awt.event.MouseEvent evt) throws IOException {
	//ɾ�����ε�txt�����ļ�
	File file=new File("./Absorbance/"+this.FileName);
	file.delete();
	//ɾ�������б�����excel�Ĺ���
	InputStream is = new FileInputStream("Absorbance.xlsx");
	XSSFWorkbook excel=new XSSFWorkbook(is);
	XSSFSheet sheet=excel.getSheetAt(0);
	XSSFRow	row=sheet.getRow(sheet.getLastRowNum());
	sheet.removeRow(row);
	//����excel�޸�
	FileOutputStream FOS=new FileOutputStream("Absorbance.xlsx");
	excel.write(FOS);
	//ɾ�����α����Ԥ��������
	if(this.preprocessName.equalsIgnoreCase("SNV")){
		is = new FileInputStream("AfterSNV.xlsx");
		excel=new XSSFWorkbook(is);
		sheet=excel.getSheetAt(0);
		row=sheet.getRow(sheet.getLastRowNum());
		sheet.removeRow(row);
		//����excel�޸�
		FOS=new FileOutputStream("AfterSNV.xlsx");
		excel.write(FOS);
	}
	else if(this.preprocessName.equalsIgnoreCase("SG")){
		is = new FileInputStream("AfterSG.xlsx");
		excel=new XSSFWorkbook(is);
		sheet=excel.getSheetAt(0);
		row=sheet.getRow(sheet.getLastRowNum());
		sheet.removeRow(row);
		//����excel�޸�
		FOS=new FileOutputStream("AfterSG.xlsx");
		excel.write(FOS);
	}
	else if(this.preprocessName.equalsIgnoreCase("MSC")){
		is = new FileInputStream("AfterMSC.xlsx");
		excel=new XSSFWorkbook(is);
		sheet=excel.getSheetAt(0);
		row=sheet.getRow(sheet.getLastRowNum());
		sheet.removeRow(row);
		//����excel�޸�
		FOS=new FileOutputStream("AfterMSC.xlsx");
		excel.write(FOS);
	}
	//ɾ�����α����Ũ������
	is = new FileInputStream("Concentration.xlsx");
	excel=new XSSFWorkbook(is);
	sheet=excel.getSheetAt(0);
	//����concentration������������ɾ����Ӧ����������
	for(int i=0; i<this.concentration_length;i++){
	row=sheet.getRow(sheet.getLastRowNum());
	sheet.removeRow(row);
	}
	//����excel�޸�
	FOS=new FileOutputStream("Concentration.xlsx");
	excel.write(FOS);
	
	dispose();
}
}
