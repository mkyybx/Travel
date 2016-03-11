import java.io.*;
import java.sql.*;
import javax.swing.JOptionPane;
import java.util.Date;

<<<<<<< HEAD
=======

>>>>>>> origin/master
public class Main 
{
	public static Statement st;//公共语句
	public static ResultSet result;//公共语句
	public static String[] city;//城市数组
	public static PrintWriter log;//日志写入对象
	public static Date dateinit = new java.util.Date();//启动时间
	public static String NAME;//登陆名
	public static String PASSWORD;//登陆密码
	
	public static Date renewTime() 
	{
		return new Date(System.currentTimeMillis());
	}
	public static void main(String args[]) throws Exception 
	{
		//记录日志
		try 
		{
			log = new PrintWriter("event.log");
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, "日志无法写入，请检查travel.log的可写性！", "错误",JOptionPane.PLAIN_MESSAGE);
			System.exit(0);
		}
		log.println(renewTime().toString() + ":系统启动！");
		
		//加载MySQL驱动
		Class.forName("com.mysql.jdbc.Driver");
		log.println(renewTime().toString() + ":驱动加载成功！");
		//建立连接
		try 
		{
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/travel","root","huang");
			st = connection.createStatement();
		}
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, "数据库连接错误", "错误",JOptionPane.PLAIN_MESSAGE);
			log.println(renewTime().toString() + ":建立数据库连接失败");
			System.exit(0);
		}
		log.println(renewTime().toString() + ":建立数据库连接成功！");
		//读取城市信息
		result = st.executeQuery("select * from city");
		result.last();
		city = new String[result.getRow()];
		result.first();
		for (int i = 0; i < city.length; i++) 
		{
			city[i] = result.getString(2);
			result.next();
		}
		//test
		MainFrameBlank.MFBMain();
		//test
		//Login.login();//登录，交给下一个函数
		
		log.close();
	}
}
