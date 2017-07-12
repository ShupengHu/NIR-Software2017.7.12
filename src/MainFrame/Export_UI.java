package MainFrame;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@SuppressWarnings("serial")
public class Export_UI extends JFrame{
	private JPanel contentPane=(JPanel) getContentPane();
	private JTextField fileText=new JTextField("Exmaple.xlsx");
	private JLabel fileLable=new JLabel("�ļ���");
	private JButton button=new JButton("ȷ��");
	private File[] files=null;
public Export_UI(File[] openFiles){
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	init();
	//�ر�ʱ���˳�����Ĵ���
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.files=openFiles;
}
public void init(){	
	int width=Toolkit.getDefaultToolkit().getScreenSize().width;
	int height=Toolkit.getDefaultToolkit().getScreenSize().height;
	this.setBounds(width/3,height/3,1000,500);
	contentPane.setSize(getMaximumSize());
	contentPane.setLayout(null);	
	fileLable.setBounds(0, 60, 100, 50);
	fileText.setBounds(110, 60, 800, 50);
	button.setBounds(new Rectangle(500, 400, 70, 50));
	button.addMouseListener(new java.awt.event.MouseAdapter(){
      	public void mouseClicked(java.awt.event.MouseEvent evt){
      		try {
				ButtonMouseClicked(evt);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
      	}
      });
	contentPane.add(fileLable);
    contentPane.add(fileText);
    contentPane.add(button);
}
public void ButtonMouseClicked(java.awt.event.MouseEvent evt) throws IOException{
	String fileName=fileText.getText();
	Scanner s=null;
	//����excel
	XSSFWorkbook excel=new XSSFWorkbook();
	XSSFSheet sheet=excel.createSheet();	
	/*---��ȡÿ��txt�ļ������ε�����excel�ļ�---*/
	for(int i=0;i<this.files.length;i++){
		//������Ӧ��excel row
		XSSFRow row=sheet.createRow(i);
		//��ȡtxt�ļ�
		s=new Scanner(files[i]);
		int index=0;
		while(s.hasNextDouble()){
			XSSFCell cell=row.createCell(index);
			cell.setCellValue(s.nextDouble());
			index++;					
		}		
	}
	//����excel�ļ�
	FileOutputStream FOS=new FileOutputStream(fileName);
	excel.write(FOS);
	dispose();
}
}
