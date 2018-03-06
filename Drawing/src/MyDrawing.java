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
 static Canvas can=new MyCanvas(); // 부모타입
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
  p2=new JPanel(){ // 여백주기
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
  
  can=new MyCanvas(); // 도화지 역할을 하는 컴포넌트 MyCanvas는 can을 상속 받는 자식->원이 갑자기 생김
  can.setSize(600, 600); // 도화지 크기
  can.setBackground(Color.white); // 도화지 배경색 주기
  p2.add(can);
  
  //리스너 부착 -------------------
  MyHandler my=new MyHandler();
  can.addMouseMotionListener((MouseMotionListener) my); // 캔버스 객체에 마우스모션리스너를 부착한다.
  //can.addMouseListener((MouseListener) my); // 캔버스 객체에 마우스리스너를 부착한다.
  btR.addActionListener((ActionListener) my);
  btBK.addActionListener(my);
  btG.addActionListener(my);
  btB.addActionListener(my);
  btOpen.addActionListener(my);
    
  //pt버튼(PaintToolFrame 클래스꺼)에도 리스너를 부착하자
  pt.btPlus.addActionListener(my);
  pt.btMinus.addActionListener(my);
  pt.btClear.addActionListener(my);
  //pt.btAllClear.addActionListener(my);
  pt.btColor.addActionListener(my);
  pt.btClose.addActionListener(my);
    
  //이번에는 메인에서 안하고 생성자에서 한다
  setSize(500,500);
  setVisible(true);
  //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 // drawing(181, 200, 15, 15, 255, 255, 0);
  
 }
 
 /*이벤트소스: 캔버스
  * 이벤트: MouseEvent
  * 이벤트 핸들러: MouseMotionListener를 구현
  * */
 
 class MyHandler implements MouseMotionListener, ActionListener, MouseListener{
  
  public void mouseDragged(MouseEvent e){
	  
   //setTitle("Drag");
   //마우스를 드래그한 지점의 x좌표,y좌표를 얻어와서 can의 x,y 좌표값에 전달한다.
   int xx=e.getX(); int yy=e.getY();
   //setTitle("xx="+xx+", yy"+yy);
   ((MyCanvas)can).x=xx; ((MyCanvas)can).y=yy;
   x=xx;y=yy;
   
   //paint()는 JVM이 호출해주는 메소드으로 변경x, repaint을 써서 재사용하자
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
    // 새로운 'paintToolJframe' 생성해서 창 열기
    //PaintToolFrame pt = new PaintToolFrame(); 여기서 코드 넣으면 매번 프레임 생성 ㅜ
    //pt.setSize(400, 400);
    pt.pack(); // 크기를 압축해서 보여줌
    pt.setLocation(getWidth(),0); //x축만큼 오른쪽으로 창 이동
    pt.setVisible(true);
   }else if(o==pt.btPlus){
    can2.w +=10; can2.h+=10;
    w=can2.w;h=can2.h;
   }else if(o==pt.btMinus){
    if(can2.w>3){ // 버튼을 계속 누르면 아예 안나옴. 최소한의 크기 설정
     can2.w -= 10; can2.h -= 10; 
     w=can2.w;h=can2.h;
    }
   }else if(o==pt.btClear)
   {
	   can2.cr=Color.white;
	    color=Color.white;
    // 드래그한 지점만 부분 지우기
    can2.cr=can.getBackground();
   }
   /*else if(o==pt.btAllClear)
   {
    // 캔버스를 모두 지우기
    // Graphics 클래스의 clearRect(x,y,w,h)
    //Graphics g=can2.getGraphics();
    //g.clearRect(0, 0, can.getWidth(), can.getHeight()); 
   }*/else if(o==pt.btColor){
    // (Swing에 있음) JColorChooser를 띄워서 선택한 색상으로 그려지도록
    Color selCr = JColorChooser.showDialog(null, "색선정", Color.blue); // null=스크린 중앙에 화면 나옴
    can2.cr=selCr; 
    color=selCr;
   }else if(o==pt.btClose){
    // pt만 닫혀지도록
    //pt.setVisible(false);-> 눈에 보이지 않는것일뿐임
    pt.dispose(); // 시스템 자원을 반납해줌
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
  new MyDrawing(); // 생성자 불러오기
  new MultiChatClient();
 }*/
}
