import java.awt.Color;
import java.io.BufferedReader;
import java.io.CharConversionException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JScrollBar;
import java.awt.BorderLayout;
// 키보드로 전송문자열 입력받아 서버로 전송하는 스레드
class WriteThread{
	Socket socket;
	ClientFrame cf;
	String str;
	String id;

	public WriteThread(ClientFrame cf) 
	{
		this.cf  = cf;
		this.socket= cf.socket;
		
	}
	public void sendMsg() {
		//키보드로부터 읽어오기 위한 스트림객체 생성
		BufferedReader br=
		new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw=null;
		try{
			//서버로 문자열 전송하기 위한 스트림객체 생성
			pw=new PrintWriter(socket.getOutputStream(),true);
			//첫번째 데이터는 id 이다. 상대방에게 id와 함께 내 IP를 전송한다.
			if(cf.isFirst==true){
				InetAddress iaddr=socket.getLocalAddress();				
				String ip = iaddr.getHostAddress();				
				getId();
				//System.out.println("ip:"+ip+"id:"+id);
				//str = "["+id+"] is login ("+ip+")";
				str=id;//
				cf.id=id;
				pw.println(id);
				System.out.println(id + "!?!");
			
			}
			else{
				if(!cf.txtF.getText().equals(""))
				{
				str= "["+id+"] "+cf.txtF.getText()+"\0";
				}
			}
			//입력받은 문자열 서버로 보내기
			//System.out.println(str);
			if(cf.drag_flag==1)
			{
				String [] split = cf.drainwg_data.split(":");
				cf.drawing_tool.drawing(Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]), Integer.parseInt(split[5]), Integer.parseInt(split[6]), Integer.parseInt(split[7]));
				cf.drawing_tool.can.repaint();
				pw.println(cf.drainwg_data);
			}
			else
			{
				
				if (!cf.txtF.getText().equals("")) 
				{
					pw.println(str);
				}
			}
		
		}catch(IOException ie){
			System.out.println(ie.getMessage());
		}finally{
			try{
				if(br!=null) br.close();
				//if(pw!=null) pw.close();
				//if(socket!=null) socket.close();
			}catch(IOException ie){
				System.out.println(ie.getMessage());
			}
		}
	}	
	public void getId(){		
		id = Id.getId(); 
	}
}
//서버가 보내온 문자열을 전송받는 스레드
class ReadThread extends Thread{
	Socket socket;
	ClientFrame cf;
	String[] a = {"고기","양파","삼겹살"}; 
	public ReadThread(Socket socket, ClientFrame cf) {
		this.cf = cf;
		this.socket=socket;
	}
	public void run() {
		synchronized (this)
		{
		BufferedReader br=null;
		try{
			System.out.println("무언가를 받았다!!!!!");
			//서버로부터 전송된 문자열 읽어오기 위한 스트림객체 생성
			br=new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			WriteThread wr= new WriteThread(cf);
			String id;
			
			while(true)
			{
				System.out.println("In while");
				//소켓으로부터 문자열 읽어옴
				String str=br.readLine();
				id=cf.id;
				System.out.println("my id : "+cf.id);
				//String str = "1111111";
				if(str==null){
					System.out.println("Connection break");
					break;
				}
				//전송받은 문자열 화면에 출력
				//System.out.println("무언가를 받았다!!!!!");
				//System.out.println("[server] :" + str);
				if(str.contains("i:"))
				{
					
					String [] split = str.split(":");
					cf.drawing_tool.drawing(Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]), Integer.parseInt(split[5]), Integer.parseInt(split[6]), Integer.parseInt(split[7]));
					//cf.txtA.append(str+"\n");
				}
				else if(str.contains("let's go"))
				{
					cf.txtA.append("본 게임은 캐치마인드 게임입니다."+"\n");
					cf.txtA.append("3점을 먼저 획득하면 승리합니다."+"\n");
					cf.txtA.append("가장 먼저 입장한 플레이어 부터 그립니다."+"\n");
					//cf.txtA.append(str+"\n");
					
				}
				else if(str.contains("win!!!!"))
				{
					cf.txtA.append(str+"\n");
					cf.text.setText("");
					cf.scrollPane.getVerticalScrollBar().setValue(cf.scrollPane.getVerticalScrollBar().getMaximum());
				}
				else if(str.contains("*"))
				{
					System.out.println(str);
					//str.replaceAll("*", "가 ");
					cf.txtA.append(str+ "정답을 맞췄어요*\n");
					cf.scrollPane.getVerticalScrollBar().setValue(cf.scrollPane.getVerticalScrollBar().getMaximum());
				}
				else
				{
					int flag=0;
					System.out.println(str +" /is str");
					if(str.contains(";"))
					{
						String[] msg = str.split(";");
						if(msg[1].equals(id))
						{
							if(msg.length==4)
							cf.text.setText(msg[2]);
							System.out.println(msg[2]);
							//if(Integer.parseInt(msg[3])!=0)
							//{
							//cf.txtA.append(id+ "정답입니다.\n");
							//}
						}
						else
						{
							cf.text.setText("");
						}
						if(msg.length==4)
						{
						System.out.println(msg[0]+" /is msg[0]");
						System.out.println(msg[1]+" /is msg[1]");
						System.out.println(msg[2]+" /is msg[2]");
						System.out.println(msg[3]+" /is msg[3]");
						cf.textField.setText("현재 점수 : "+msg[3]+"점");
						}
						if(msg[0].contains("\n"))
						{
							cf.txtA.append(msg[0]);
							cf.scrollPane.getVerticalScrollBar().setValue(cf.scrollPane.getVerticalScrollBar().getMaximum());
						}
						else
						{
						cf.txtA.append(msg[0]+"\n");
						cf.scrollPane.getVerticalScrollBar().setValue(cf.scrollPane.getVerticalScrollBar().getMaximum());
						}
					}
					else
					{
						//if(flag==0)
						System.out.println(str +" @@@@");
						
						//flag=0;
					}
				}
			}
		}catch(IOException ie){
			System.out.println(ie.getMessage());
			System.out.println("ioexception");
		}finally{
			try{
				if(br!=null) br.close();
				if(socket!=null) socket.close();
			}catch(IOException ie){}
		}
	}
	}
}
public class MultiChatClient {
	public static void main(String[] args) {
		Socket socket=null;
		ClientFrame cf;		
		try{
			socket=new Socket("192.168.174.130",9190);
			System.out.println("연결성공!");
			cf = new ClientFrame(socket);
			new ReadThread(socket, cf).start();
		}catch(IOException ie){
			System.out.println(ie.getMessage());
		}
	}
}


