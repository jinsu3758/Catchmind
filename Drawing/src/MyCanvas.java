import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

public class MyCanvas extends Canvas {
	public MyCanvas() {
	}
 
 //처음에 까만색 점 안찍히게 하기 위해서 x,y -값 지정
 int x=-50; int y=-50;
 public int w=7, h=7;
 Color cr=Color.black;
 
 @Override
 public void paint(Graphics g){
  g.setColor(cr);
  g.fillOval(x, y, w, h); // x, y 지점에 70,70 크기의 원 그리기
 }
 
 @Override
 public void update(Graphics g){
  paint(g);
 }
 
}