package MainFrame;

import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import serialCommunication.SerialCommunication;

@SuppressWarnings("serial")
public class ChipController_UI extends JFrame{
	byte[] stop={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x10,(byte) 0xff,0x65};   //��ָֹ��
	byte[] forward={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x11,(byte) 0xff,0x65};   //ǰ��ָ��
	byte[] backward={0x01,0x0f,0x00,0x00,0x00,0x08,0x01,0x01,(byte) 0xff,0x65};   //����ָ��
	private JPanel contentPane=(JPanel) getContentPane();
	private JButton forwardButton=new JButton("ǰ��");
	private JButton stopButton=new JButton("ֹͣ");
	private JButton backwardButton=new JButton("����");
	
public ChipController_UI(){
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	init();
	//�ر�ʱ���˳�����Ĵ���
	this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
}
public void init(){	
	this.setBounds(500,300,500,300);
	contentPane.setSize(getMaximumSize());
	contentPane.setLayout(null);	
	forwardButton.setBounds(new Rectangle(100, 150, 70, 50));
	forwardButton.addMouseListener(new java.awt.event.MouseAdapter(){
      	public void mouseClicked(java.awt.event.MouseEvent evt){     		
				forwardButtonMouseClicked(evt);			
      	}
      });
	backwardButton.setBounds(new Rectangle(200, 150, 70, 50));
	backwardButton.addMouseListener(new java.awt.event.MouseAdapter(){
      	public void mouseClicked(java.awt.event.MouseEvent evt){     		
				backwardButtonMouseClicked(evt);			
      	}
      });
	stopButton.setBounds(new Rectangle(300, 150, 70, 50));
	stopButton.addMouseListener(new java.awt.event.MouseAdapter(){
      	public void mouseClicked(java.awt.event.MouseEvent evt){     		
				stopButtonMouseClicked(evt);			
      	}
      });
	contentPane.add(forwardButton);
    contentPane.add(backwardButton);
    contentPane.add(stopButton);
}
public void forwardButtonMouseClicked(java.awt.event.MouseEvent evt){
	SerialCommunication.portWrite(forward);
}
public void backwardButtonMouseClicked(java.awt.event.MouseEvent evt){
	SerialCommunication.portWrite(backward);
}
public void stopButtonMouseClicked(java.awt.event.MouseEvent evt){
	SerialCommunication.portWrite(stop);
}
}