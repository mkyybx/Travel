
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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


public class Login extends JFrame
{
	private JTextField jtfname=new JTextField(15);//输入帐号框
	private JPasswordField jtfpwd=new JPasswordField(15);//输入密码框
	static Login lg=new Login();//总的面板
	
	public static void login()
	{
		lg.pack();
		lg.setLocationRelativeTo(null);
		lg.setDefaultCloseOperation(EXIT_ON_CLOSE);
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
				try 
				{
					Main.result = Main.st.executeQuery("select * from users where user=\""+Main.NAME+"\";" );
					if (!Main.result.next())//验证时候注册
						JOptionPane.showMessageDialog(null , "您还未注册！请先注册");
					else if (!Main.PASSWORD.equals(Main.result.getString(2)))//验证密码是否正确
					{
						System.out.println(Main.result.getString(2));
						JOptionPane.showMessageDialog(null , "密码错误！请重新输入");
					}
					else//登陆成功
					{
						login.removeAll();
						setTitle("查询状态框");
						try 
						{
							Main.result = Main.st.executeQuery("select * from users where user=\""+Main.NAME+"\";" );
							while (Main.result.next())
							{
								if(Main.result.getString(3) == null)//无行程就调用MainFrameBlank.MFBMain()函数
								{
									MainFrameBlank.MFBMain();
								}
								else
								{
									
								}
							}
						} 
						catch (SQLException ex) 
						{
							ex.printStackTrace();
						}
						Search ps=new Search();//查询状态框
						login.add(ps);
						//lg.add(login);
						lg.pack();
						lg.repaint();
					}
				} 
				catch (SQLException exp) 
				{
					exp.printStackTrace();
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
				//login.setVisible(false);
				//System.out.println("insert into users values(\""+Main.NAME+"\",\""+Main.PASSWORD+"\",NULL);");
				try 
				{
					Main.result = Main.st.executeQuery("select * from users where user=\""+Main.NAME+"\";" );
					if(Main.result.next())//提示帐号已注册
					{
						JOptionPane.showMessageDialog(null , "此帐号已被注册！请重新输入");
						Main.NAME=jtfname.getText();
						Main.PASSWORD=new String(jtfpwd.getPassword());
					}
					else//可注册的帐号密码
					{
						int r = Main.st.executeUpdate("insert into users values(\""+Main.NAME+"\",\""+Main.PASSWORD+"\",NULL);");
						JOptionPane.showMessageDialog(null , "注册成功！请登陆");
					}
				} 
				catch (SQLException e1) 
				{
					e1.printStackTrace();
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

class Search extends JPanel//查询状态框的类
{
	static JPanel P=new JPanel(new GridLayout(0,1));
	
	public Search()
	{
		JPanel line=new JPanel(new GridLayout(1,0));//显示线路的jpanel
		JLabel l=new JLabel("路线为：",JLabel.RIGHT);
		JLabel li=new JLabel("asfdaesfwefweffdwe",JLabel.LEFT);
		line.add(l);
		line.add(li);
		
		 JPanel Pmap=new JPanel();//显示地图的jpanel
		 JButton smap=new JButton("显示地图");
		 smap.addActionListener(new ActionListener()
		 {
			public void actionPerformed(ActionEvent arg0) 
			{
				if(!Showmap.smap.isShowing())//按下时调用Showmap.ShowmapMain()显示地图
					Showmap.ShowmapMain();
			}
		 });
		 
		 JPanel Stime=new JPanel(new GridLayout(1,0));//显示时间的jpanel
		 Date date=new Date();
		 JLabel Ltime=new JLabel("实时时间：",JLabel.CENTER);
		 SimpleDateFormat t = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//时间显示格式
		 JLabel Time=new JLabel(t.format(date)+"      ",JLabel.LEFT);
		 javax.swing.Timer timer=new javax.swing.Timer(1000,new ActionListener() //定时器
		 {
			public void actionPerformed(ActionEvent e) 
			{
				Date date=new Date();//定时显示时间
				Time.setText(t.format(date)+"      ");
//				System.out.println(date.toString());
				Login.lg.pack();
				repaint();
			}
		 });
		 timer.start();//开启定时器
		 Stime.add(Ltime);
		 Stime.add(Time);
		 
		 JPanel Pstate=new JPanel(new GridLayout(1,0));//状态的jpanel
		 JLabel st=new JLabel("您的状态为：",JLabel.RIGHT);
		 JLabel state=new JLabel();
		 javax.swing.Timer timers=new javax.swing.Timer(1000,new ActionListener() //定时器
		 {
			public void actionPerformed(ActionEvent e) 
			{
				Date date=new Date();//实时刷新
				state.setText(t.format(date)+"      ");
				Login.lg.pack();
				repaint();
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
				if(!MainFrameBlank.frame.isShowing())//调用MainFrameBlank.MFBMain()更改行程
					MainFrameBlank.MFBMain();
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
}
