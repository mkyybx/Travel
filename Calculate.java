import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
public class Calculate {
	public static ArrayList<city> unselected;//未选城市
	public static ArrayList<city> selected;//已选城市
	public static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//创建sdf
	public static Statement st = Main.st;//公共语句
	public static ResultSet result = Main.result;//公共语句
	//字符转换时间
	public static long time(String s) throws Exception {
		return sdf.parse(s).getTime();
	}
	//转换成SQL能识别的24小时时间
	public static String sqlTime (long time) {
		while (time > 24*3600000)
			time -= 24*3600000;
		return Long.toString(time / 3600000 + 8).concat(Long.toString(time % 3600000 / 60000).concat("00"));
	}
	//算两段时间差time1-time2
	public static long division (long time1, long time2) {
		time1 = time1 % (24*3600000);
		time2 = time2 % (24*3600000);
		if (time1 > time2)
			return time1-time2;
		else return 24*3600000 + time1 - time2;
	}
	public static void CMain() {
		//链接变量
		unselected = MainFrameBlank.unselected;
		selected = MainFrameBlank.selected;
		//判断isOrdered，暂时先按有顺序处理
		try {
		Dij(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static int Dij(boolean isTime) throws Exception{
		int cityNum = unselected.size() + selected.size();//所有城市总数
		
		long startTime = MainFrameBlank.startTime*3600000;
		for (int i = 0; i < selected.size(); i++) {//找i到i+1的最短路径
			
			Result[] r = new Result[cityNum + 1];
			for (int j = 1; j <= cityNum; j++)
				r[j] = new Result();
			for (int j = 0; j < unselected.size(); j++) {
				r[unselected.get(j).cityId].city = unselected.get(j);
			}
			for (int j = 0; j < selected.size(); j++)
				r[selected.get(j).cityId].city = selected.get(j);
			
		r[selected.get(i).cityId].previousCity=-1;
		r[selected.get(i).cityId].minTime=startTime;
		r[selected.get(i).cityId].isShort=true;
		int idCity = selected.get(i).cityId;//当前城市ID
		
		while (true) {//每一次dij
			long minTime = Long.MAX_VALUE;//最短时间下个城市
			int nextCity = 0;
			
			result = st.executeQuery("select * from transport, city where arrivecity = cityname and departcity = '" + r[idCity].city.name + "'");
			r[idCity].departTime = r[idCity].minTime + selected.get(i).stayTime*3600000;
			while (result.next()) {
				long temp = division(time(result.getString("departtime")),r[idCity].departTime) + r[idCity].departTime + division(time(result.getString("arrivetime")),time(result.getString("departtime")));
				//System.out.println(r[result.getInt("idcity")].city.name + ":" + r[result.getInt("idcity")].minTime);
				if (r[result.getInt("idcity")].minTime > temp) {
					r[result.getInt("idcity")].minTime = temp;
					r[result.getInt("idcity")].previousCity = idCity;
					r[result.getInt("idcity")].arriveId = result.getInt("id");
				}
			}
			for (int j = 1; j <= cityNum; j++) {
				if (!r[j].isShort) {
					if (r[j].minTime < minTime) {
						minTime = r[j].minTime;
						nextCity = j;
					}
				}
			}
			//System.out.println(nextCity);
			r[nextCity].isShort = true;
			r[r[nextCity].previousCity].departId = r[nextCity].arriveId;
			r[r[nextCity].previousCity].nextCity = nextCity;
			if (r[nextCity].city.name.equals(selected.get(i + 1).name)) {
				int temp = nextCity;//selected.get(i).cityId;
				while (r[temp].previousCity != -1) {
					System.out.println(r[temp].city.name + ":第" + r[temp].minTime / 24 / 3600000 + "天" + r[temp].minTime % (24*3600000) / 3600000 + "时" + r[temp].minTime % 3600000 + "分到达，停留" + r[temp].city.stayTime + "小时 " );
					temp = r[temp].previousCity;
				}
				startTime = r[nextCity].minTime;
				
				break;
			}
			else {
				idCity = nextCity;
				minTime = Long.MAX_VALUE;
				nextCity = 0;
			}
		}
		}
		return 1;
	}
}

class Result {
	public long minTime = Long.MAX_VALUE;//最早到达时间
	public int previousCity;
	public int nextCity = -1;
	public long departTime;
	public int arriveId;
	public int departId;
	public boolean isShort = false;
	public city city;
	
}


