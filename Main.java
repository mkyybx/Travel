import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class Main 
{
	public static int[] MAPID = new int[1];
	public static Statement st;//公共语句
	public static ResultSet result;//公共语句
	public static PrintWriter log;//日志写入对象
	public static Date dateinit = new java.util.Date();//启动时间
	public static String NAME;//登陆名
	public static String PASSWORD;//登陆密码
	
	//重置为当前时间的函数
	public static Date renewTime() 
	{
		return new Date(System.currentTimeMillis());
	}
	
	//显示错误信息函数
	public static void showMessage(String s) {
		JOptionPane.showMessageDialog(null, s, "错误",JOptionPane.ERROR_MESSAGE);
	}
	public static void showMessage(String s, java.awt.Frame f) {
		JOptionPane.showMessageDialog(f, s, "错误",JOptionPane.ERROR_MESSAGE);
	}
	public static void showMessage(String s, java.awt.Frame f, boolean isError) {
		if (isError)
			JOptionPane.showMessageDialog(f, s, "错误",JOptionPane.ERROR_MESSAGE);
		else JOptionPane.showMessageDialog(f, s, "提示",JOptionPane.INFORMATION_MESSAGE);
	}
	
	//记录日志函数
	public static void log(String s) {
		log.print(renewTime().toString() + ":");
		log.println(s);
		log.flush();
	}
	
	//重绘函数
	public static void repaint(javax.swing.JPanel p) {
		p.updateUI();
		p.repaint();
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
			showMessage("日志无法写入，请检查travel.log的可写性！");
			System.exit(0);
		}
		log("系统启动！");
		
		//加载MySQL驱动
		Class.forName("com.mysql.jdbc.Driver");
		log("驱动加载成功！");
		//建立连接
		try 
		{
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/travel","root","huang");
			st = connection.createStatement();
		}
		catch (Exception e) 
		{
			showMessage("数据库连接错误");
			log("建立数据库连接失败");
			System.exit(0);
		}
		log("建立数据库连接成功！");
		//读取城市信息
		result = st.executeQuery("select * from city order by cityabbr asc");
		MainFrameBlank.unselected = new ArrayList();
		MainFrameBlank.selected = new ArrayList();
		MainFrameBlank.all = new ArrayList();
		while (result.next()) 
			MainFrameBlank.unselected.add(new city(result.getString(3).charAt(0),result.getString(2),Integer.parseInt(result.getString(1))));
		MainFrameBlank.all.addAll(MainFrameBlank.unselected);
		//test
		int []a = {1,2,3,4,5,6};
		arrange(a, 1);
		//test
		//test
		MainFrameBlank.MFBMain();
		//test
		//Login.login();//登录，交给下一个函数
		
		log.close();
	}
}

class city {
	public String name;
	public char shortName;
	public long stayTime;
	public int cityId;
	
	public String toString() {
		return (shortName + name.concat("(" + Long.toString(stayTime) + ")"));
	}
	
	city(char shortName, String name, int cityId) {
		this.shortName = shortName;
		this.name = name;
		this.cityId = cityId;
	}
}
