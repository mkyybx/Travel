

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.jar.Attributes.Name;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.xml.crypto.Data;
import javax.xml.transform.Result;

public class Login extends JFrame
{
	public static java.sql.ResultSet result = Main.result;//链接
	
	private JTextField jtfname=new JTextField(15);//输入帐号框
	private JPasswordField jtfpwd=new JPasswordField(15);//输入密码框
	static Login lg;//总的面板
	
	public static void login()
	{
		lg = new Login();
		lg.pack();
		lg.setLocationRelativeTo(null);
		//lg.setDefaultCloseOperation(EXIT_ON_CLOSE);
		lg.setVisible(true);
	}
	
	public Login()
	{
		setTitle("登陆");
		
		JPanel login=new JPanel(new GridLayout(0,1));
		
		JLabel jlbname=new JLabel("帐号：",JLabel.CENTER);
		JLabel jlbpwd=new JLabel("密码：",JLabel.CENTER);
		
		JPanel ptop=new JPanel();//上层的面板
		ptop.add(jlbname);
		ptop.add(jtfname);
		ptop.add(jlbpwd);
		ptop.add(jtfpwd);
		ptop.setLayout(new GridLayout(2,2));
		
		JButton signin=new JButton("登陆");
		signin.addActionListener(new ActionListener() //登陆按钮添加监视器
		{
			public void actionPerformed(ActionEvent e) 
			{
				Main.NAME=jtfname.getText();//获得帐号密码
				Main.PASSWORD=new String(jtfpwd.getPassword());
				if(Main.NAME.equals("")||Main.PASSWORD.equals(""))
					JOptionPane.showMessageDialog(null , "帐号密码不能为空！");
				else 
				{
				
				MessageDigest md;
				StringBuffer buf = new StringBuffer("");
				try 
				{
					md = MessageDigest.getInstance("MD5");
					md.update(Main.PASSWORD.getBytes());
					byte [] b=md.digest();
					int i;
					for (int offset = 0; offset < b.length; offset++) 
					{
						i = b[offset];
						if(i<0) 
							i+= 256;
						if(i<16)
							buf.append("0");
						buf.append(Integer.toHexString(i));
					} 
				} 
				catch (NoSuchAlgorithmException e1) 
				{
					e1.printStackTrace();
				}
				String s=buf.toString();
				
				try 
				{
					result = Main.st.executeQuery("select * from users where user='"+Main.NAME+"';" );
					if (!result.next())//验证是否注册
						JOptionPane.showMessageDialog(null , "您还未注册！请先注册");
					else if (!s.equals(result.getString(2)))//验证密码是否正确
						JOptionPane.showMessageDialog(null , "密码错误！请重新输入");
					else//登陆成功
					{
						if (result.getInt("state") == 0) {
							lg.dispatchEvent(new WindowEvent(lg, WindowEvent.WINDOW_CLOSING));
							//lg.dispose();
							MainFrameBlank.MFBMain();
						}
						else {
							try {
								Search.Inquiry();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							lg.dispatchEvent(new WindowEvent(lg, WindowEvent.WINDOW_CLOSING));
							//lg.dispose();
						}
					}
				} 
				catch (SQLException exp) 
				{
					exp.printStackTrace();
				}
			}
			}
		});
		JButton signup=new JButton("注册");
		signup.addActionListener(new ActionListener() //注册按钮添加监视器
		{
			public void actionPerformed(ActionEvent e) 
			{
				Main.NAME=jtfname.getText();
				Main.PASSWORD=new String(jtfpwd.getPassword());
				
				if(Main.NAME.equals("")||Main.PASSWORD.equals(""))
					JOptionPane.showMessageDialog(null , "帐号密码不能为空！");
				else 
				{
				//login.setVisible(false);
				//System.out.println("insert into users values('"+Main.NAME+"','"+Main.PASSWORD+"',NULL);");
				try 
				{
					result = Main.st.executeQuery("select * from users where user='"+Main.NAME+"';" );
					if(result.next())//提示帐号已注册
					{
						JOptionPane.showMessageDialog(null , "此帐号已被注册！请重新输入");
						Main.NAME=jtfname.getText();
						Main.PASSWORD=new String(jtfpwd.getPassword());
					}
					else//可注册的帐号密码
					{
						MessageDigest md;
						try 
						{
							md = MessageDigest.getInstance("MD5");
							md.update(Main.PASSWORD.getBytes());
							byte[] b=md.digest();
							
							int i;
							StringBuffer buf = new StringBuffer("");
							for (int offset = 0; offset < b.length; offset++) 
							{
								i = b[offset];
								if(i<0) 
									i+= 256;
								if(i<16)
									buf.append("0");
								buf.append(Integer.toHexString(i));
							} 
							
							int r = Main.st.executeUpdate("insert into users values('"+Main.NAME+"','"+buf+"',0,null,null,null);");
							JOptionPane.showMessageDialog(null , "注册成功！请登陆");
						} 
						catch (NoSuchAlgorithmException e1) 
						{
							e1.printStackTrace();
						}
					}
				} 
				catch (SQLException e1) 
				{
					e1.printStackTrace();
				}
				}
			}
		});
		JButton exit=new JButton("取消");
		exit.addActionListener(new ActionListener() //取消按钮添加监视器
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				System.exit(0);
			}
		});
		
		JPanel pbuttom=new JPanel();//登陆，注册，取消三个按钮的jpanle
		pbuttom.add(signin);
		pbuttom.add(signup);
		pbuttom.add(exit);
		
		login.add(ptop);
		login.add(pbuttom);
		
		add(login);
	}
}

class Search extends JFrame//查询状态框的类
{
	static JPanel P=new JPanel(new GridLayout(0,1));
	static Search s;
	
	public Search() throws Exception
	{
		//初始化行程信息
		Main.result = Main.st.executeQuery("select * from users where user='"+Main.NAME+"'");
		Main.result.next();
		String prompt = Main.result.getString("prompt");//读取路线描述
		//数据数组
		String temp = Main.result.getString("route");
		//int[][] route = new int
		
		JPanel line=new JPanel();//显示线路的jpanel
		JLabel l=new JLabel("路线为：",JLabel.RIGHT);
		JButton li = new JButton("显示路线");
		line.add(l);
		line.add(li);
		
		li.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.showMessage(prompt, null, false);
			}
		});
		
		 JPanel Pmap=new JPanel();//显示地图的jpanel
		 JButton smap=new JButton("显示地图");
		 smap.addActionListener(new ActionListener()
		 {
			public void actionPerformed(ActionEvent arg0) 
			{
				if(!Showmap.smap.isShowing())//按下时调用Showmap.ShowmapMain()显示地图
				{
					s.setVisible(false);
					Showmap.smap.setVisible(true);
					//s.dispatchEvent(new WindowEvent(s, WindowEvent.WINDOW_CLOSING));
				}
//				s.setVisible(false);
//				Showmap.smap.repaint();
//				Showmap.smap.setVisible(true);
			}
		 });
		 
		 JPanel Stime=new JPanel(new GridLayout(1,0));//显示时间的jpanel
		 Date date=new Date();
		 JLabel Ltime=new JLabel("实时时间：",JLabel.CENTER);
		 //SimpleDateFormat t = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//时间显示格式
		 Main.result = Main.st.executeQuery("select * from users where user='"+Main.NAME+"';" );
		 Main.result.next();
		 String  starttime= Main.result.getString("starttime");
		 long systemt=System.currentTimeMillis()-Long.parseLong(starttime);
		 long second=systemt/1000%60;
		 long minute=systemt/1000/60%60;
		 long hour=systemt/1000/60/60%24;
		 JLabel Time=new JLabel(hour+":"+minute+":"+second);
		 javax.swing.Timer timer=new javax.swing.Timer(1000,new ActionListener() //定时器
		 {
			public void actionPerformed(ActionEvent e) 
			{
				try {
					Main.result = Main.st.executeQuery("select * from users where user='"+Main.NAME+"';" );
					Main.result.next();
					String  starttime= Main.result.getString("starttime");
					long systemt=System.currentTimeMillis()-Long.parseLong(starttime);
					long second=systemt/1000%60;
					long minute=systemt/1000/60%60;
					long hour=systemt/1000/60/60%24;
					Time.setText(hour+":"+minute+":"+second);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
//				System.out.println(date.toString());
				repaint();
			}
		 });
		 timer.start();//开启定时器
		 Stime.add(Ltime);
		 Stime.add(Time);
		 
		 JPanel Pstate=new JPanel(new GridLayout(1,0));//状态的jpanel
		 JLabel st=new JLabel("您现在位于为：",JLabel.RIGHT);
		 JLabel state=new JLabel("       ");
		 javax.swing.Timer timers=new javax.swing.Timer(1000,new ActionListener() //定时器
		 {
			public void actionPerformed(ActionEvent e) 
			{
				try {
					Main.result = Main.st.executeQuery("select * from users where user='"+Main.NAME+"';" );
					Main.result.next();
					String  in= Main.result.getString("route");
					String [] stateroute=in.split(",");//string以“，”为分隔符转化为string数组
					//System.out.println(stateroute[3]);
					
					Main.result = Main.st.executeQuery("select * from users where user='"+Main.NAME+"';" );
					Main.result.next();
					String  starttime= Main.result.getString("starttime");
					long systemt=System.currentTimeMillis()-Long.parseLong(starttime);
					long hour=systemt/1000/60/60%24;
					int i=0;
					for(;i<stateroute.length-3;i+=2)
					{
						hour+=Long.parseLong(stateroute[0])/10;
						if(i%2==0&&Long.parseLong(stateroute[i])/10<=hour&&Long.parseLong(stateroute[i+2])/10>hour)
						{
							if(Character.isDigit(stateroute[i+1].toCharArray()[0]))
							{
								Main.result = Main.st.executeQuery("select * from city where idcity='"+stateroute[i+1]+"';" );
								Main.result.next();
								state.setText(Main.result.getString("cityname"));
							}
							else 
							{
								state.setText("车次"+stateroute[i+1]);
							}
							break;
						}
					}
					repaint();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
			}
		 });
		 timers.start();//开启定时器
		 Pstate.add(st);
		 Pstate.add(state);
		 
		 JPanel change=new JPanel();//更改行程的jpanel
		 JButton c=new JButton("更改行程");
		 c.addActionListener(new ActionListener() 
		 {
			public void actionPerformed(ActionEvent e) 
			{
				MainFrameBlank.MFBMain();
				s.setVisible(false);
//				if(!MainFrameBlank.frame.isShowing())//调用MainFrameBlank.MFBMain()更改行程
//				{	
//					MainFrameBlank.MFBMain();
//					s.setVisible(false);
//				}
			}
		 });
		 change.add(c);
		 
		 Pmap.add(smap);
		 P.add(line);
		 P.add(Pmap);
		 P.add(Stime);
		 P.add(change);
		 P.add(Pstate);
		 add(P);
	}
	
	public static void Inquiry() throws Exception
	{
		s=new Search();
		s.setTitle("查询状态框");
		s.pack();
		s.repaint();
		s.setLocationRelativeTo(null);
		s.setDefaultCloseOperation(EXIT_ON_CLOSE);
		s.setVisible(true);
	}
}
