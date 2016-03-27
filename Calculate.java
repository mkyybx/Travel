package Travel;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JOptionPane;
public class Calculate {
	public static ArrayList<city> unselected;//未选城市
	public static ArrayList<city> selected;//已选城市
	public static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//创建sdf
	public static Statement st = Main.st;//公共语句
	public static ResultSet result = Main.result;//公共语句
	public static int cityNum = MainFrameBlank.all.size();//城市个数
	
	public static long minValue;//用于剪枝，记录排列时最优数值
	public static Lock signalminValue;
	public static Lock[] signalresult = new Lock[cityNum + 1];
	public static StringBuilder s;//用于记录提示语
	public static StringBuilder sqls;//用于写入数据库，数据格式：时间,城市/车次以#结束
	
	
	//多线程部分
	public static ExecutorService executor = Executors.newFixedThreadPool(4);//线程池
	
	//转成sql能识别的时间格式,包含了日期信息
	public static synchronized String sqltime(long time) {
		return Long.toString(time / 3600000).concat(Long.toString(((time % 3600000) / 60000 / 10)));
	}
	//字符转换时间
	public static synchronized long time(String s) throws Exception {
		return sdf.parse(s).getTime() + 8 * 3600000;
	}
	//时间转文字
	public static synchronized String stime(long time) {
		return "第" + (time / (24 * 3600000) + 1) + "天" + time % (24 * 3600000) / 3600000 + "时" + (time % 3600000) / 60000 + "分";
	}
	//算两段时间差time1-time2
	public static synchronized long division (long time1, long time2) {
		time1 = time1 % (24*3600000);
		time2 = time2 % (24*3600000);
		if (time1 > time2)
			return time1-time2;
		else if (time1 < time2)
			return 24*3600000 + time1 - time2;
		else return 0;
	}
	//排列
	static void arrange(ArrayList<city> a, int begin) throws Exception{
		if (a.size() - begin == 2) {
			ArrayList<city> temp = new ArrayList<city>();
			temp.addAll(a);
			executor.execute(new Dij(temp ,MainFrameBlank.strategy == 1 ? true : false,false));
			//Dij(temp ,MainFrameBlank.strategy == 1 ? true : false,false);
		}
		else {
			for (int i = begin; i < a.size() - 1; i++) {
				city temp = a.get(i);
				a.set(i, a.get(begin));
				a.set(begin, temp);
				arrange(a, begin + 1);
				temp = a.get(i);
				a.set(i, a.get(begin));
				a.set(begin, temp);
			}
		}
	}
	
	public static void CMain() {
		try {
		//链接变量
		unselected = MainFrameBlank.unselected;
		selected = MainFrameBlank.selected;
		//初始化
		minValue = Long.MAX_VALUE;//用于剪枝，记录排列时最优数值
		signalminValue = new ReentrantLock();
		s = null;//用于记录提示语
		sqls = null;//用于写入数据库，数据格式：时间,城市/车次以#结束
		for (int i = 1; i <= cityNum; i++)
			signalresult[i] = new ReentrantLock();
		selected.add(MainFrameBlank.finalCity);
		if (MainFrameBlank.isOrdered) {			
			if (MainFrameBlank.strategy == 1)
				Dij(selected, true, true);
			else if (MainFrameBlank.strategy == 2)
				Dij(selected, false, true);				
		}
		else {
			minValue = Long.MAX_VALUE;
			//排列，递归调用
			ArrayList<city> copySelected = new ArrayList<city>();
			copySelected.addAll(selected);
			arrange(copySelected,1);
			executor.awaitTermination(15, TimeUnit.SECONDS);		
		}
		selected.remove(selected.size() - 1);
		//结果处理
		
		int choice = JOptionPane.showConfirmDialog(null, s, "确认",JOptionPane.YES_NO_OPTION);
		if (choice == 0) {
			//向数据库写入真实出发时间，行程数据
			st.executeUpdate("update users set state = 1, starttime = " + System.currentTimeMillis() + ", route = '" + sqls + "', prompt = '" + s + "' where user = '" + Main.NAME + "'");
			MainFrameBlank.frame.dispose();
			Thread.sleep(500);
			Login.lg.setVisible(true);
			Main.windowLock.unlock();
			//下一步
		}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void Dij(ArrayList<city> selected, boolean isTime, boolean isOrdered) throws Exception{
		
		StringBuilder s = new StringBuilder();
		StringBuilder sqls = new StringBuilder();
		int totalPrice = 0;//最后用于计算总价
		cityNum = MainFrameBlank.all.size();//所有城市总数
		long startTime = MainFrameBlank.startTime*3600000;
		boolean canTerminate = false;//用于排列运算时剪枝，当算到某一节点时已经比最小值大了，直接pass
		for (int i = 0; i < selected.size() - 1; i++) {//找i到i+1的最短路径
			if (canTerminate) {
				signalminValue.unlock();
				break;
			}
			Result[] r = new Result[cityNum + 1];
			for (int j = 0; j <= cityNum; j++) {
				r[j] = new Result();
			}
			for (int j = 0; j < cityNum; j++) {
				r[MainFrameBlank.all.get(j).cityId].city = MainFrameBlank.all.get(j);
			}
			//初始化出发城市
			r[selected.get(i).cityId].previousCity=0;
			r[selected.get(i).cityId].minTime=startTime;
			r[selected.get(i).cityId].minPrice=0;
			r[selected.get(i).cityId].isShort=true;
			int idCity = selected.get(i).cityId;//当前城市ID
		
			while (true) {//每一次dij
				long minTime = Long.MAX_VALUE;//最短时间下个城市
				int minPrice = Integer.MAX_VALUE;//最短金钱下个城市
				int nextCity = 0;
				if (selected.get(i).cityId == idCity)
					r[idCity].departTime = r[idCity].minTime + r[idCity].city.stayTime*3600000;
				else r[idCity].departTime = r[idCity].minTime;
				
				signalresult[idCity].lock();
				
				
				result = Main.buffer[idCity];//st[(count++) % 4].executeQuery("select departtime, arrivetime, price, number, idcity from transport, city where arrivecity = cityname and departcity = '" + r[idCity].city.name + "'");
				result.first();
				
				do {//对每个城市松弛
					
					if (!r[result.getInt("idcity")].isShort) {
						if (isTime) {
							long temp = division(time(result.getString("departtime")),r[idCity].departTime) + r[idCity].departTime + division(time(result.getString("arrivetime")),time(result.getString("departtime")));
							if (r[result.getInt("idcity")].minTime > temp) {
								r[result.getInt("idcity")].minPrice = result.getInt("price") + r[idCity].minPrice;
								r[result.getInt("idcity")].minTime = temp;
								r[result.getInt("idcity")].previousCity = idCity;
								r[result.getInt("idcity")].previousDepartTime = temp - division(time(result.getString("arrivetime")),time(result.getString("departtime")));
								r[result.getInt("idcity")].arriveNo = result.getString("number");
							}
						}
						else {
							int tempMoney = result.getInt("price") + r[idCity].minPrice;
							if (r[result.getInt("idcity")].minPrice > tempMoney) {
								long temp = division(time(result.getString("departtime")),r[idCity].departTime) + r[idCity].departTime + division(time(result.getString("arrivetime")),time(result.getString("departtime")));
								r[result.getInt("idcity")].minPrice = tempMoney;
								r[result.getInt("idcity")].minTime = temp;
								r[result.getInt("idcity")].previousCity = idCity;
								r[result.getInt("idcity")].previousDepartTime = temp - division(time(result.getString("arrivetime")),time(result.getString("departtime")));
								r[result.getInt("idcity")].arriveNo = result.getString("number");
							}
								
						}
					}
				} while (result.next());
				
				signalresult[idCity].unlock();
				
				for (int j = 1; j <= cityNum; j++) {
					if (!r[j].isShort) {
						if (!isTime) {
							if (r[j].minPrice < minPrice) {
								minPrice = r[j].minPrice;
								nextCity = j;
							}
						}
						else {
							if (r[j].minTime < minTime) {
								minTime = r[j].minTime;
								nextCity = j;
							}
						}
					}
				}
				r[nextCity].isShort = true;
				if (!isOrdered) {
					
					signalminValue.lock();
					if (isTime) {
						if (r[nextCity].minTime > minValue) {
							canTerminate = true;
							break;
						}
					}
					else {
						if (r[nextCity].minPrice > minValue) {
							canTerminate = true;
							break;
						}
					}
					signalminValue.unlock();
				}
				
				if (r[nextCity].city.name.equals(selected.get(i + 1).name)) {
					int temp = nextCity;//selected.get(i).cityId;
					while (temp != 0) {
						r[r[temp].previousCity].nextCity = temp;
						r[r[temp].previousCity].departNo = r[temp].arriveNo;
						r[temp].minPrice = r[temp].minPrice - r[r[temp].previousCity].minPrice;
						//System.out.println(r[temp].city.name + ":第" + r[temp].minTime / 24 / 3600000 + "天" + r[temp].minTime % (24*3600000) / 3600000 + "时" + r[temp].minTime % 3600000 / 60000 + "分到达，停留" + r[temp].city.stayTime + "小时 " );
						temp = r[temp].previousCity;
					}
					temp = selected.get(i).cityId;
					while (temp != 0 && temp != selected.get(i + 1).cityId) {
						int tempPrice = 0;
						s.append(stime(r[r[temp].nextCity].previousDepartTime) + "乘坐" + r[temp].departNo + "从" + r[temp].city.name + "出发，");
						sqls.append(sqltime(r[temp].minTime) + "," + temp + "," + sqltime(r[r[temp].nextCity].previousDepartTime) + "," + r[temp].departNo + ",");
						while (r[r[temp].nextCity].departNo != null && r[r[temp].nextCity].departNo.equals(r[temp].departNo)) {
							temp = r[temp].nextCity;
							tempPrice += r[temp].minPrice;
						}
						temp = r[temp].nextCity;
						tempPrice += r[temp].minPrice;
						s.append(stime(r[temp].minTime) + "到达" + r[temp].city.name + "，票价" + tempPrice + "元" + "\n");
						totalPrice += tempPrice;
					}
					startTime = r[nextCity].minTime;
					s.append("\n");
					if (i + 1 == selected.size() - 1)
						sqls.append(sqltime(r[selected.get(i + 1).cityId].minTime) + "," + selected.get(i + 1).cityId + "#");
					break;
				}
				else {
					idCity = nextCity;
					
				}
				
				
			}
			
			
		}
		if (!canTerminate) {
			s.append("总票价：" + totalPrice + "元");
			signalminValue.lock();
			if (isOrdered || (!isOrdered && ((!isTime && totalPrice < minValue) || (isTime && startTime <minValue)))) {
				minValue = isTime ? startTime : totalPrice;
				Calculate.s = s;
				Calculate.sqls = sqls;
			}
			signalminValue.unlock();
			
		}
		
	}
}

class Result {
	public long minTime = Long.MAX_VALUE;//最早到达时间
	public int minPrice = Integer.MAX_VALUE;//最少金钱
	public int previousCity = 0;
	public int nextCity = 0;
	public long previousDepartTime;
	public long departTime;
	public String arriveNo;
	public String departNo;
	public boolean isShort = false;
	public city city;
	
}

class ReturnResult {
	String route;
	long time;
	int money;
	
	ReturnResult(String route, long time, int money) {
		this.route = route;
		this.time = time;
		this.money = money;
	}
}

class Dij implements Runnable {
	ArrayList<city> selected;
	boolean isTime;
	boolean isOrdered;
	public void run(){
		try {
		Calculate.Dij(selected, isTime, isOrdered);
		System.out.println("子线程" + Thread.currentThread() + "执行完毕");
		} catch (Exception ex) {
			System.out.println("子线程" + Thread.currentThread() + "执行出错");
			ex.printStackTrace();
		}
	}
	Dij(ArrayList<city> selected, boolean isTime, boolean isOrdered) {
		this.selected = selected;
		this.isTime = isTime;
		this.isOrdered = isOrdered;
	}
}


