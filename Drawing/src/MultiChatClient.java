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
// Ű����� ���۹��ڿ� �Է¹޾� ������ �����ϴ� ������
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
		//Ű����κ��� �о���� ���� ��Ʈ����ü ����
		BufferedReader br=
		new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw=null;
		try{
			//������ ���ڿ� �����ϱ� ���� ��Ʈ����ü ����
			pw=new PrintWriter(socket.getOutputStream(),true);
			//ù��° �����ʹ� id �̴�. ���濡�� id�� �Բ� �� IP�� �����Ѵ�.
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
			//�Է¹��� ���ڿ� ������ ������
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
//������ ������ ���ڿ��� ���۹޴� ������
class ReadThread extends Thread{
	Socket socket;
	ClientFrame cf;
	String[] a = {"���","����","����"}; 
	public ReadThread(Socket socket, ClientFrame cf) {
		this.cf = cf;
		this.socket=socket;
	}
	public void run() {
		synchronized (this)
		{
		BufferedReader br=null;
		try{
			System.out.println("���𰡸� �޾Ҵ�!!!!!");
			//�����κ��� ���۵� ���ڿ� �о���� ���� ��Ʈ����ü ����
			br=new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			WriteThread wr= new WriteThread(cf);
			String id;
			
			while(true)
			{
				System.out.println("In while");
				//�������κ��� ���ڿ� �о��
				String str=br.readLine();
				id=cf.id;
				System.out.println("my id : "+cf.id);
				//String str = "1111111";
				if(str==null){
					System.out.println("Connection break");
					break;
				}
				//���۹��� ���ڿ� ȭ�鿡 ���
				//System.out.println("���𰡸� �޾Ҵ�!!!!!");
				//System.out.println("[server] :" + str);
				if(str.contains("i:"))
				{
					
					String [] split = str.split(":");
					cf.drawing_tool.drawing(Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]), Integer.parseInt(split[5]), Integer.parseInt(split[6]), Integer.parseInt(split[7]));
					//cf.txtA.append(str+"\n");
				}
				else if(str.contains("let's go"))
				{
					cf.txtA.append("�� ������ ĳġ���ε� �����Դϴ�."+"\n");
					cf.txtA.append("3���� ���� ȹ���ϸ� �¸��մϴ�."+"\n");
					cf.txtA.append("���� ���� ������ �÷��̾� ���� �׸��ϴ�."+"\n");
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
					//str.replaceAll("*", "�� ");
					cf.txtA.append(str+ "������ ������*\n");
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
							//cf.txtA.append(id+ "�����Դϴ�.\n");
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
						cf.textField.setText("���� ���� : "+msg[3]+"��");
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
			System.out.println("���Ἲ��!");
			cf = new ClientFrame(socket);
			new ReadThread(socket, cf).start();
		}catch(IOException ie){
			System.out.println(ie.getMessage());
		}
	}
}


