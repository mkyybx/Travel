
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Showmap extends JFrame 
{
	static Showmap smap = new Showmap();//地图的jframe
	float [] mapx=new float[13];//各个城市的x轴比例
	float [] mapy=new float[13];//各个城市的y轴比例
	
	public static void ShowmapMain()
	{
		smap.setSize(1000,600);
		smap.setLocationRelativeTo(null);
		smap.setDefaultCloseOperation(EXIT_ON_CLOSE);
		smap.setVisible(true);
	}
	
	public Showmap()
	{
		try 
		{
			for(int i=0;i<13;i++)	
			{
				Main.result = Main.st.executeQuery("select * from city where idcity=" +(int)(i+1));
				while (Main.result.next())//得到各个城市的xy比例
					{
						mapx[i]=((float)(Main.result.getInt(4))/1000);
						mapy[i]=((float)Main.result.getInt(5)/709);
						System.out.println(mapx[i]);
					}
			}
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
		
		setTitle("地图");
		setLayout(new BorderLayout());
		add(new MapPanel(),BorderLayout.CENTER);
		JPanel Pb=new JPanel();
		JButton back=new JButton("返回");//返回时释放内存
		back.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				smap.dispose();
			}
		});
		Pb.add(back);
		add(Pb,BorderLayout.SOUTH);
	}
	
	class MapPanel extends JPanel//地图的类
	{
		protected void paintComponent(Graphics g)
		{
			super.paintComponents(g);
			
			ImageIcon image=new ImageIcon("img/map.jpg");
			Dimension size=this.getSize();//使图像比例随窗口变化
			g.drawImage(image.getImage(), 0, 0, size.width,size.height,null);
			
			int x=getWidth();
			int y=getHeight();
			
			int [] mx=new int[mapx.length];//各城市x的坐标
			int [] my=new int[mapy.length];//各城市y的坐标
			for(int i=0;i<13;i++)//各城市xy的坐标
			{
				mx[i]=(int) (mapx[i]*x);
				my[i]=(int) (mapy[i]*y);
			}
			
			g.setColor(Color.BLUE);
			for(int i=0;i<13;i++)//显示各城市的名称
			{
				try 
				{
					Main.result = Main.st.executeQuery("select * from city where idcity=" +(int)(i+1));
					while (Main.result.next())
					{
						g.drawString(Main.result.getString(2), mx[i], my[i]);
					}
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
			
			g.setColor(Color.black);
			int [] mxc=new int[Main.MAPID.length];
			int [] myc=new int[Main.MAPID.length];
			for(int i=0;i<Main.MAPID.length;i++)//输出各个行车路线
			{
				mxc[i]=(int) (mx[Main.MAPID[i]-1]);
				myc[i]=(int) (my[Main.MAPID[i]-1]);
			}
			g.drawPolyline(mxc, myc, mxc.length);
		}
	}
}
