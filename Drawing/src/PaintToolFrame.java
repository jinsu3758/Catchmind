import java.awt.Color;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PaintToolFrame extends JFrame{

 JButton btPlus, btMinus, btClear, btAllClear, btClose, btColor;
 JPanel p;
 
 public PaintToolFrame(){
  super("::PaintTool::");
  Container c=getContentPane();
  
  p=new JPanel();
  p.setBackground(new Color(0, 204, 204));
  c.add(p, "Center");

  this.setBackground(new Color(0, 204, 204));
  p.add(btPlus=new JButton("크게"));
  p.add(btMinus=new JButton("작게"));
  p.add(btClear=new JButton("지우기"));
  //p.add(btAllClear=new JButton("모두지우기"));
  p.add(btColor=new JButton("색상"));
  p.add(btClose=new JButton("닫기"));
  btPlus.setBackground(new Color(204, 255, 255));
  btMinus.setBackground(new Color(204, 255, 255));
  btClear.setBackground(new Color(204, 255, 255));
  //btAllClear.setBackground(new Color(204, 255, 255));
  btColor.setBackground(new Color(204, 255, 255));
  btClose.setBackground(new Color(204, 255, 255));
  
  
  
 }
 
}