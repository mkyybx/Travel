import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JOptionPane;
public class Calculate {
	public static ArrayList<city> unselected;//未选城市
	public static ArrayList<city> selected;//已选城市
	public static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//创建sdf
	public static Statement st = Main.st;//公共语句
	public static ResultSet result = Main.result;//公共语句
	public static long minValue = Long.MAX_VALUE;//用于剪枝，记录排列时最优数值
	//转成sql能识别的时间格式
	public static String sqltime(long time) {
		return Long.toString(((time % (24 * 3600000)) / 3600000 + 8)).concat(Long.toString(((time % 3600000) / 60000))).concat("00");
	}
	//字符转换时间
	public static long time(String s) throws Exception {
		return sdf.parse(s).getTime() + 8 * 3600000;
	}
	//时间转文字
	public static String stime(long time) {
		return "第" + (time / (24 * 3600000) + 1) + "天" + time % (24 * 3600000) / 3600000 + "时" + (time % 3600000) / 60000 + "分";
	}
	//算两段时间差time1-time2
	public static long division (long time1, long time2) {
		time1 = time1 % (24*3600000);
		time2 = time2 % (24*3600000);
		if (time1 > time2)
			return time1-time2;
		else if (time1 < time2)
			return 24*3600000 + time1 - time2;
		else return 0;
	}
	//排列
	static void arrange(ArrayList<city> a, int begin) {
		if (a.size() - begin == 2) 
			Dij(a,)
		else {
			for (int i = begin; i < a.length - 1; i++) {
				int temp = a[i];
				a[i] = a[begin];
				a[begin] = temp;
				arrange(a, begin + 1);
				temp = a[i];
				a[i] = a[begin];
				a[begin] = temp;
			}
		}
	}
	
	public static void CMain() {
		
		//链接变量
		unselected = MainFrameBlank.unselected;
		selected = MainFrameBlank.selected;
		StringBuilder s = null;
		try {
			selected.add(MainFrameBlank.finalCity);
			if (MainFrameBlank.isOrdered) {
				
				if (MainFrameBlank.strategy == 1)
					s = Dij(selected, true, true);
				else if (MainFrameBlank.strategy == 2)
					s = Dij(selected, false, true);
				
			}
			else {
				minValue = Long.MAX_VALUE;
				//排列，递归调用
				ArrayList<city> copySelected = new ArrayList<city>();
				
			}
			selected.remove(selected.size() - 1);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//结果处理
		int choice = JOptionPane.showConfirmDialog(null, s, "确认",JOptionPane.YES_NO_OPTION);
		if (choice == 0) {
			//下一步
		}
	}
	
	public static StringBuilder Dij(ArrayList<city> selected, boolean isTime, boolean isOrdered) throws Exception{
		StringBuilder s = new StringBuilder();
		int totalPrice = 0;//最后用于计算总价
		int cityNum = MainFrameBlank.all.size();//所有城市总数
		long startTime = MainFrameBlank.startTime*3600000;
		boolean canTerminate = false;//用于排列运算时剪枝，当算到某一节点时已经比最小值大了，直接pass
		for (int i = 0; i < selected.size() - 1; i++) {//找i到i+1的最短路径
			if (canTerminate)
				break;
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
				if (!isTime)
					result = st.executeQuery("select * from transport, city where arrivecity = cityname and departcity = '" + r[idCity].city.name + "' order by price");
				else result = st.executeQuery("SELECT * FROM transport, city where arrivecity = cityname and departcity = '" + r[idCity].city.name + "' and departtime >= " + sqltime(r[idCity].departTime) + " union all (SELECT * FROM transport, city where arrivecity = cityname and departcity = '" + r[idCity].city.name + "' and departtime < " + sqltime(r[idCity].departTime) + " order by departtime)");
				while (result.next()) {//对每个城市松弛
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
				}
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
					break;
				}
				else {
					idCity = nextCity;
				}
			}
		}
		s.append("总票价：" + totalPrice + "元");
		if (!isOrdered && !isTime)
			minValue = totalPrice;
		else if (!isOrdered && isTime)
			minValue = startTime;
		Main.showMessage(stime(startTime));
		return s;
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


