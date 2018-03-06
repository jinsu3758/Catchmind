import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MyDrawing extends JPanel 
{
 
 JPanel p1,p2;
 JButton btR, btG, btB, btBK, btOpen;
 static Canvas can=new MyCanvas(); // �θ�Ÿ��
 PaintToolFrame pt;
 static int Drag=0;
 static int x,y,w=15,h=15;
 static Color color = Color.BLACK;
 
  void drawing(int x,int y,int w, int h , int r,int g,int b)
 {
	  System.out.printf("%d,%d,%d,%d,%d,%d,%d\n",x,y,w,h,r,g,b);
	 ((MyCanvas)can).x=x;((MyCanvas)can).y=y;((MyCanvas)can).w=w;
	 ((MyCanvas)can).h=h;((MyCanvas)can).cr= new Color(r,g,b);
	 can.repaint();	  
	 can.update(can.getGraphics());
 }
 public MyDrawing(){
  //super("::MyDrawing::");
  setLayout(new BorderLayout());
  pt=new PaintToolFrame();
  p1=new JPanel(); add(p1, "North");
  p1.setBackground(new Color(0, 204, 204));
  p2=new JPanel(){ // �����ֱ�
   public Insets getInsets(){
    return new Insets(1,1,1,1);
   }
  }; add(p2, "Center");
  p2.setBackground(Color.lightGray);
  
  
  btBK=new JButton(); btBK.setPreferredSize(new Dimension(50,20)); p1.add(btBK);
  btBK.setBackground(new Color(0, 0, 0));
  btR=new JButton(); btR.setPreferredSize(new Dimension(50,20));p1.add(btR);
  btR.setBackground(new Color(255, 0, 0));
  btG=new JButton(); btG.setPreferredSize(new Dimension(50,20));p1.add(btG);
  btG.setBackground(new Color(0, 255, 0));
  btB=new JButton(); btB.setPreferredSize(new Dimension(50,20));p1.add(btB);
  btB.setBackground(new Color(0, 0, 255));
  btOpen=new JButton("Paint Tool"); p1.add(btOpen);
  btOpen.setBackground(new Color(204, 255, 255));
  
  can=new MyCanvas(); // ��ȭ�� ������ �ϴ� ������Ʈ MyCanvas�� can�� ��� �޴� �ڽ�->���� ���ڱ� ����
  can.setSize(600, 600); // ��ȭ�� ũ��
  can.setBackground(Color.white); // ��ȭ�� ���� �ֱ�
  p2.add(can);
  
  //������ ���� -------------------
  MyHandler my=new MyHandler();
  can.addMouseMotionListener((MouseMotionListener) my); // ĵ���� ��ü�� ���콺��Ǹ����ʸ� �����Ѵ�.
  //can.addMouseListener((MouseListener) my); // ĵ���� ��ü�� ���콺�����ʸ� �����Ѵ�.
  btR.addActionListener((ActionListener) my);
  btBK.addActionListener(my);
  btG.addActionListener(my);
  btB.addActionListener(my);
  btOpen.addActionListener(my);
    
  //pt��ư(PaintToolFrame Ŭ������)���� �����ʸ� ��������
  pt.btPlus.addActionListener(my);
  pt.btMinus.addActionListener(my);
  pt.btClear.addActionListener(my);
  //pt.btAllClear.addActionListener(my);
  pt.btColor.addActionListener(my);
  pt.btClose.addActionListener(my);
    
  //�̹����� ���ο��� ���ϰ� �����ڿ��� �Ѵ�
  setSize(500,500);
  setVisible(true);
  //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 // drawing(181, 200, 15, 15, 255, 255, 0);
  
 }
 
 /*�̺�Ʈ�ҽ�: ĵ����
  * �̺�Ʈ: MouseEvent
  * �̺�Ʈ �ڵ鷯: MouseMotionListener�� ����
  * */
 
 class MyHandler implements MouseMotionListener, ActionListener, MouseListener{
  
  public void mouseDragged(MouseEvent e){
	  
   //setTitle("Drag");
   //���콺�� �巡���� ������ x��ǥ,y��ǥ�� ���ͼ� can�� x,y ��ǥ���� �����Ѵ�.
   int xx=e.getX(); int yy=e.getY();
   //setTitle("xx="+xx+", yy"+yy);
   ((MyCanvas)can).x=xx; ((MyCanvas)can).y=yy;
   x=xx;y=yy;
   
   //paint()�� JVM�� ȣ�����ִ� �޼ҵ����� ����x, repaint�� �Ἥ ��������
   can.repaint();
   //can.update(can.getGraphics());
   
   //System.out.println("bbbbbbbbbbbb");
   
  }
  
  public void mouseMoved(MouseEvent e)
  {
  }
  
  public void actionPerformed(ActionEvent e){
   Object o=e.getSource();
   MyCanvas can2 = (MyCanvas)can;
   
   if(o==btR){
    can2.cr=Color.red;
    color=Color.red;
   }else if(o==btBK){
	    can2.cr=Color.BLACK;
	    color=Color.BLACK;
   }else if(o==btG){
    can2.cr=Color.GREEN;
    color=Color.GREEN;
   }else if(o==btB){
    can2.cr=Color.blue;
    color=Color.blue;
   }else if(o==btOpen){
    // ���ο� 'paintToolJframe' �����ؼ� â ����
    //PaintToolFrame pt = new PaintToolFrame(); ���⼭ �ڵ� ������ �Ź� ������ ���� ��
    //pt.setSize(400, 400);
    pt.pack(); // ũ�⸦ �����ؼ� ������
    pt.setLocation(getWidth(),0); //x�ุŭ ���������� â �̵�
    pt.setVisible(true);
   }else if(o==pt.btPlus){
    can2.w +=10; can2.h+=10;
    w=can2.w;h=can2.h;
   }else if(o==pt.btMinus){
    if(can2.w>3){ // ��ư�� ��� ������ �ƿ� �ȳ���. �ּ����� ũ�� ����
     can2.w -= 10; can2.h -= 10; 
     w=can2.w;h=can2.h;
    }
   }else if(o==pt.btClear)
   {
	   can2.cr=Color.white;
	    color=Color.white;
    // �巡���� ������ �κ� �����
    can2.cr=can.getBackground();
   }
   /*else if(o==pt.btAllClear)
   {
    // ĵ������ ��� �����
    // Graphics Ŭ������ clearRect(x,y,w,h)
    //Graphics g=can2.getGraphics();
    //g.clearRect(0, 0, can.getWidth(), can.getHeight()); 
   }*/else if(o==pt.btColor){
    // (Swing�� ����) JColorChooser�� ����� ������ �������� �׷�������
    Color selCr = JColorChooser.showDialog(null, "������", Color.blue); // null=��ũ�� �߾ӿ� ȭ�� ����
    can2.cr=selCr; 
    color=selCr;
   }else if(o==pt.btClose){
    // pt�� ����������
    //pt.setVisible(false);-> ���� ������ �ʴ°��ϻ���
    pt.dispose(); // �ý��� �ڿ��� �ݳ�����
   }
  }

@Override
public void mouseClicked(MouseEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void mouseEntered(MouseEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void mouseExited(MouseEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void mousePressed(MouseEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void mouseReleased(MouseEvent e) {
	// TODO Auto-generated method stub
	
}
 }
 
 
 /*public static void main(String[] args) 
 {
  new MyDrawing(); // ������ �ҷ�����
  new MultiChatClient();
 }*/
}
