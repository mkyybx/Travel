import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
public class Calculate {
	public static ArrayList unselected;//未选城市
	public static ArrayList selected;//已选城市
	public static void CMain() {
		unselected = MainFrameBlank.unselected;
		selected = MainFrameBlank.selected;
		//判断isOrdered，暂时先按有顺序处理
	}
	
	public static int Dij(boolean isTime) {
		Result[] result = new Result[unselected.size() + selected.size()];
		
		return 1;
	}
	class Result {
		public int minTime;
		public int previousCity;
		public int arriveTime;
		public int departTime;
		public int arriveId;
		public int departId;
	}
}


