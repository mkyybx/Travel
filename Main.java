
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main 
{
	public static int[] MAPID = new int[1];
	public static Statement st;//公共语句
	public static ResultSet result;//公共语句
	public static PrintWriter log;//日志写入对象
	public static Date dateinit = new java.util.Date();//启动时间
	public static String NAME;//登陆名
	public static String PASSWORD;//登陆密码
	public static ResultSet[] buffer;//数据缓存，出发城市，到达城市，出发时间，
	public static Lock windowLock = new ReentrantLock();
	
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
	
	static void arrange(ArrayList<city> a, int begin, int[] b) throws Exception{
		if (a.size() - begin == 2) {
			Calculate.arrange(MainFrameBlank.strategy == 1 ? true : false, b, 1, new ReturnResult(MainFrameBlank.startTime, 0));
			for (int i = 0; i < b.length; i++) {
				System.out.print(Calculate.sequence[i]);
			}
			System.out.println("\n" + Calculate.minValue);
			ArrayList<city> c = new ArrayList<city>();
			c.addAll(a);
			Calculate.Dij(c, false, true, 0, 0);
			Calculate.minValue = Long.MAX_VALUE;
			Calculate.minValueAux = Long.MAX_VALUE;
		}
		else {
			for (int i = begin; i < a.size() - 1; i++) {
				city temp = a.get(i);
				a.set(i,a.get(begin));
				a.set(begin,temp);
				arrange(a, begin + 1, b);
				temp = a.get(i);
				a.set(i,a.get(begin));
				a.set(begin,temp);
			}
		}
	}
	
	public static void main(String args[]) throws Exception 
	{
		
		int begin = 1;
		int []a = new int[5];
		for(int i = 0; i < a.length; i++)
			a[i] = i;
		//arrange(a,1);
		
		
		
		
		
		
		
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
		
		//初始化buffer
		result = st.executeQuery("select * from city order by idcity asc");
		Connection[] tempconnection = new Connection[MainFrameBlank.all.size() + 1]; 
		Statement[] tempst = new Statement[MainFrameBlank.all.size() + 1];
		buffer = new ResultSet[MainFrameBlank.all.size() + 1];
		while(result.next()) {
			tempconnection[result.getInt("idCity")] = DriverManager.getConnection("jdbc:mysql://localhost/travel","root","huang");
			tempst[result.getInt("idCity")] = tempconnection[result.getInt("idCity")].createStatement();
			buffer[result.getInt("idCity")]=tempst[result.getInt("idCity")].executeQuery("select departtime, arrivetime, price, number, idcity from transport, city where arrivecity = cityname and departcity = '" + result.getString("cityname") + "'");
		}
		//Login.login();//登录，交给下一个函数
		//test
		MainFrameBlank.MFBMain();
		//test
		
		
		//log.close();
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
