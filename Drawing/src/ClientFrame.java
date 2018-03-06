import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import java.awt.Scrollbar;
import javax.swing.SwingConstants;
import javax.swing.JScrollBar;


class Id extends JFrame implements ActionListener,KeyListener, MouseMotionListener{
	static JTextField tf=new JTextField(8);
	
	JButton btn = new JButton("입력");
	
	
	WriteThread wt;	
	ClientFrame cf;
	public Id(){}
	public Id(WriteThread wt, ClientFrame cf) {
		super("Catch Mind");		
		getContentPane().setBackground(new Color(0, 204, 204));
		this.wt = wt;
		this.cf = cf;
	
		
		setLayout(new FlowLayout());
		add(new JLabel("아이디 : "));
		tf.addKeyListener(this);
		btn.setPreferredSize(new Dimension(60,23));
		btn.setBackground(new Color(204, 255, 255));
		
		add(tf);
		add(btn);
		
		
		btn.addActionListener(this);
		
		setBounds(300, 300, 370, 90);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {		
		wt.sendMsg();	
		cf.isFirst = false;
		cf.setVisible(true);
		this.dispose();
	}
	static public String getId(){
		return tf.getText();
	}
	@Override
	public void keyPressed(KeyEvent e) 
	{
		 if ( e.getKeyCode()==KeyEvent.VK_ENTER){
			  btn.doClick();
		  }
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}

public class ClientFrame extends JFrame implements ActionListener, KeyListener, MouseMotionListener{
	JTextArea txtA = new JTextArea();
	JTextField txtF = new JTextField(25);
	Font f1 = new Font("돋움", Font.PLAIN, 25);
	Font f2 = new Font("돋움", Font.PLAIN, 20);
	JButton btnTransfer = new JButton("전송");
	JButton btnExit = new JButton("닫기");
	boolean isFirst=true;
	JPanel p1 = new JPanel();
	JPanel p2 = new JPanel();	
	JPanel p1_1 = new JPanel();
	JPanel p1_2 = new JPanel();	
	JTextField text = new JTextField(""); 
	JScrollPane scrollPane=null;	
	//private JScrollPane scrollPane;
	Socket socket;
	WriteThread wt;
	JTextField textField = new JTextField();
	static MyDrawing drawing_tool= new MyDrawing();
	static String drainwg_data=null;
	static int drag_flag=0;
	static String id;
	JTextArea ta = new JTextArea();
	//JScrollPane sp = new JScrollPane(txtA);
	
		
	public ClientFrame(Socket socket) 
	{
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setBackground(new Color(204, 255, 255));
		textField.setFont(new Font("굴림", Font.PLAIN, 25));
		textField.setEditable(false);
		textField.setText("\uD604\uC7AC \uC810\uC218 : 0\uC810");
		textField.setColumns(9);
		
		
		getContentPane().setBackground(new Color(0, 204, 204));
		setTitle("Catch Mind (by HHJ)");
		this.socket = socket;
		wt = new WriteThread(this);
		new Id(wt, this);
		//MyDrawing drawing_tool = new MyDrawing();
		drawing_tool.setBackground(new Color(0, 204, 204));
		drawing_tool.can.addMouseMotionListener(this);
		
		
		p1 = new JPanel()
		{ // 여백주기
			   public Insets getInsets()
			   {
			    return new Insets(10,0,10,16);
			   }
		}; 
		//p1.setBackground(Color.lightGray);		
		p1.setLayout(new BorderLayout());
		p1_2.setBackground(new Color(0, 204, 204));
		
		p1_2.add(textField);
		text.setFont(f2);
		text.setSize(new Dimension(30,10));
		p1_2.add(text);
		
		p1.add("North", p1_2);
		txtA.setTabSize(5);
		
		
		//scrollPane = new JScrollPane(txtA, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		txtA.setFont(new Font("Monospaced", Font.PLAIN, 20));
		txtA.setEditable(false);
		p1.add("Center",txtA);
		//p1.add(sp);;
		//p1.add("Center",scrollPane);
		p1.setBackground(new Color(0, 204, 204));
		
		scrollPane = new JScrollPane(txtA);
	    scrollPane.setSize(new Dimension(200,300));
	    //p1.add(scrollPane);
	    p1.add(txtA);
		Border lineBorder = BorderFactory.createLineBorder(Color.lightGray, 6);
	    Border emptyBorder = BorderFactory.createEmptyBorder(7, 7, 7, 7);
	    txtA.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
	    txtF.setFont(f2);
	    txtF.addKeyListener(this);
		p1_1.add(txtF);
		btnTransfer.setBackground(new Color(204, 255, 255));
		p1_1.add(btnTransfer);
		p1_1.setBackground(new Color(0, 204, 204));
		//p1_1.add(btnExit);
		p1.add("South",p1_1);
		
		getContentPane().add("East", p1);
		
		
		//p2.setBackground(Color.lightGray);
		
		p2 = new JPanel()
		{ // 여백주기
			   public Insets getInsets()
			   {
			    return new Insets(5,10,10,10);
			   }
		}; 
		p2.setBackground(new Color(0, 204, 204));
		p2.add(drawing_tool);
		
		getContentPane().add("West",p2);
		
		
		//메세지를 전송하는 클래스 생성.
		
		//btnTransfer.addKeyListener(this);
		btnTransfer.addActionListener(this);
		//btnExit.addActionListener(this);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(100, 100, 1130,730);
		setVisible(false);
		//putDrawing(146, 234, 7, 7, 0, 0, 255);
	}
	
	public void getDrawing()
	{
		drainwg_data = new String("i"+":"+drawing_tool.x+":"+drawing_tool.y+":"+drawing_tool.w+":"+drawing_tool.h+":"+drawing_tool.color.getRed()+":"+drawing_tool.color.getGreen()+":"+drawing_tool.color.getBlue());
		System.out.println(drainwg_data);
		btnTransfer.doClick();
		drag_flag=1;
		wt.sendMsg();
		drag_flag=0;
		
		//return drawing_tool.Drag;
	}
	
	/*public void putDrawing(int x,int y,int w, int h , int r,int g,int b)
	{
		if(drawing_tool!=null)
		{
			drawing_tool.drawing(x,y,w,h,r,g,b);
		}
		else
			System.out.println("not yet ready");
	}*/
	
	public void actionPerformed(ActionEvent e){
		String id = Id.getId();
		if(e.getSource()==btnTransfer){//전송버튼 눌렀을 경우
			//메세지 입력없이 전송버튼만 눌렀을 경우
			if(txtF.getText().equals("")){
				return;
			}			
			//txtA.append("["+id+"] "+ txtF.getText()+"\n");
			wt.sendMsg();
			txtF.setText("");
		}else{
			this.dispose();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) 
	{
		  if (e.getKeyCode()==KeyEvent.VK_ENTER){
			 // System.out.println("1111111111");
			  btnTransfer.doClick();
		  }
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		getDrawing();
		//System.out.println("aaaaaaaaaaaaa");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
		// TODO Auto-generated method stub
		
	}
}