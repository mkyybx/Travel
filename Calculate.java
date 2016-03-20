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
	public static void CMain() {
		
		//链接变量
		unselected = MainFrameBlank.unselected;
		selected = MainFrameBlank.selected;
		
		StringBuilder s = null;
		
		//判断isOrdered，暂时先按有顺序处理
		try {
			if (MainFrameBlank.strategy == 1)
				s = Dij(true);
			else if (MainFrameBlank.strategy == 2)
				s = Dij(false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//结果处理
		int choice = JOptionPane.showConfirmDialog(null, s, "确认",JOptionPane.YES_NO_OPTION);
		if (choice == 0) {
			//下一步
		}
	}
	
	public static StringBuilder Dij(boolean isTime) throws Exception{
		StringBuilder s = new StringBuilder();
		int totalPrice = 0;
		int cityNum = unselected.size() + selected.size();//所有城市总数
		long startTime = MainFrameBlank.startTime*3600000;
		for (int i = 0; i < selected.size() - 1; i++) {//找i到i+1的最短路径
			
			Result[] r = new Result[cityNum + 1];
			for (int j = 0; j <= cityNum; j++)
				r[j] = new Result();
			for (int j = 0; j < unselected.size(); j++) {
				r[unselected.get(j).cityId].city = unselected.get(j);
			}
			for (int j = 0; j < selected.size(); j++)
				r[selected.get(j).cityId].city = selected.get(j);
			
			//初始化出发城市
			r[selected.get(i).cityId].previousCity=0;
			r[selected.get(i).cityId].minTime=startTime;
			r[selected.get(i).cityId].isShort=true;
			int idCity = selected.get(i).cityId;//当前城市ID
		
			while (true) {//每一次dij
				long minTime = Long.MAX_VALUE;//最短时间下个城市
				int nextCity = 0;
			
				result = st.executeQuery("select * from transport, city where arrivecity = cityname and departcity = '" + r[idCity].city.name + "'");
				if (selected.get(i).cityId == idCity)
					r[idCity].departTime = r[idCity].minTime + r[idCity].city.stayTime*3600000;
				else r[idCity].departTime = r[idCity].minTime;
				while (result.next()) {//对每个城市松弛
					if (!r[result.getInt("idcity")].isShort) {
						if (isTime) {
							long temp = division(time(result.getString("departtime")),r[idCity].departTime) + r[idCity].departTime + division(time(result.getString("arrivetime")),time(result.getString("departtime")));
							if (r[result.getInt("idcity")].minTime > temp) {
								r[result.getInt("idcity")].minPrice = result.getInt("price");
								r[result.getInt("idcity")].minTime = temp;
								r[result.getInt("idcity")].previousCity = idCity;
								r[result.getInt("idcity")].previousDepartTime = temp - division(time(result.getString("arrivetime")),time(result.getString("departtime")));
								r[result.getInt("idcity")].arriveNo = result.getString("number");
							}
						}
						else {
							int tempMoney = result.getInt("price");
							long temp = division(time(result.getString("departtime")),r[idCity].departTime) + r[idCity].departTime + division(time(result.getString("arrivetime")),time(result.getString("departtime")));
							if (r[result.getInt("idcity")].minPrice > tempMoney) {
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
							if (r[j].minTime < minTime) {
								minTime = r[j].minTime;
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
			
				if (r[nextCity].city.name.equals(selected.get(i + 1).name)) {
					int temp = nextCity;//selected.get(i).cityId;
					while (temp != 0) {
						r[r[temp].previousCity].nextCity = temp;
						r[r[temp].previousCity].departNo = r[temp].arriveNo;
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
				
					break;
				}
				else {
					idCity = nextCity;
				}
			}
		}
		s.append("\n总票价：" + totalPrice + "元");
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


