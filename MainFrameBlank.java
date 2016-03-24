import java.sql.*;
import java.util.ArrayList;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrameBlank extends JFrame {
	public static JFrame frame;
	public static boolean isOrdered;//是否按顺序旅游
	public static long startTime;//出发时间
	public static long limitedTime;//限制的时间
	public static char strategy;//旅行策略：1、时间最短2、金钱最少3、时间限定金钱最少
	public static ArrayList<city> unselected;//未选城市
	public static ArrayList<city> selected;//已选城市
	public static ArrayList<city> all;//全部城市
	public static city finalCity;
	public static boolean isNumber(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (!Character.isDigit(s.charAt(i)))
				return false;
		}
		return true;	
	}
	public static void MFBMain() {
		frame = new MainFrameBlank();
		frame.setTitle("旅行规划系统Powered by 沐晓枫&按键三&老鹰飞了");
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	MainFrameBlank() {
		
		//setLayout(new GridLayout(0,1));
		setLayout(new BorderLayout());
		
		JPanel pu = new JPanel(new BorderLayout());//上面的panel
		
		JPanel putop = new JPanel(new GridLayout(0,3));
		JPanel pubuttom = new JPanel(new GridLayout(0,3));
		
		
		
		JPanel puu=new JPanel(new GridLayout(2,0));//修改的
		JPanel pub=new JPanel();
		JLabel jlfinal = new JLabel("出发城市",JLabel.RIGHT);
		pub.add(jlfinal);
		JComboBox jcbfinal = new JComboBox(all.toArray());
		pub.add(jcbfinal);
		puu.add(pub);
		puu.add(putop);
		
		
		
		//pu的东东
		JLabel l1 = new JLabel("已选城市：",JLabel.CENTER);
		JLabel l2 = new JLabel("可选城市：",JLabel.CENTER);
		JRadioButton jrbisordered=new JRadioButton("是否按顺序旅游",true);
		
		JList jldepart = new JList(unselected.toArray());
		JList jlarrive = new JList(selected.toArray());
		jldepart.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jlarrive.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		JPanel button = new JPanel(new GridLayout(4,1));//中间按钮button
		//button的东东
		JButton jbtr = new JButton(">>");
		JButton jbtl = new JButton("<<");
		JButton jbtu = new JButton("↑");
		JButton jbtd = new JButton("↓");
		ActionListener listListener = new ActionListener() {
			city temp;
			public void actionPerformed(ActionEvent e) {
				try {
					if(e.getSource().equals(jbtr)) {
						temp = (city)jldepart.getSelectedValue();
						if (!temp.equals(null)) {
							selected.add(temp);
							unselected.remove(jldepart.getSelectedIndex());
						}
					}
					else if(e.getSource().equals(jbtl)) {
						temp = (city)jlarrive.getSelectedValue();
						if (!temp.equals(null)) {
							unselected.add(temp);
							selected.remove(jlarrive.getSelectedIndex());
						}
					}
					else if (e.getSource().equals(jbtu)) {
						Object temp = selected.get(jlarrive.getSelectedIndex() - 1);
						selected.set(jlarrive.getSelectedIndex() - 1, (city)jlarrive.getSelectedValue());
						selected.set(jlarrive.getSelectedIndex(), (city)temp);
					}
					else if (e.getSource().equals(jbtd)) {
						Object temp = selected.get(jlarrive.getSelectedIndex() + 1);
						selected.set(jlarrive.getSelectedIndex() + 1, (city)jlarrive.getSelectedValue());
						selected.set(jlarrive.getSelectedIndex(), (city)temp);
					}
					jldepart.setListData(unselected.toArray());
					jlarrive.setListData(selected.toArray());
				}catch(Exception ex) {
					
				}
			}
		};
		button.add(jbtd);
		button.add(jbtu);
		button.add(jbtl);
		button.add(jbtr);
		jbtd.addActionListener(listListener);
		jbtu.addActionListener(listListener);
		jbtl.addActionListener(listListener);
		jbtr.addActionListener(listListener);
		//结束
		putop.add(l2);
		putop.add(jrbisordered);
		putop.add(l1);
		pubuttom.add(new JScrollPane(jldepart));
		pubuttom.add(button);
		pubuttom.add(new JScrollPane(jlarrive));
		//结束
		
		JPanel pm = new JPanel();//中间的panel
		//pm的东东
		JRadioButton jrb1 = new JRadioButton("最短时间",true);
		JRadioButton jrb2 = new JRadioButton("最便宜",false);
		JRadioButton jrb3 = new JRadioButton("规定时间内最便宜",false);
		JTextField jtf1 = new JTextField(10);
		JLabel l3 = new JLabel("小时");
		ActionListener buttonListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jrb3.isSelected()) {
					pm.add(jtf1);
					pm.add(l3);
				}
				else {
					pm.remove(jtf1);
					pm.remove(l3);
				}
				Main.repaint(pm);
			}
		};
		jrb1.addActionListener(buttonListener);
		jrb2.addActionListener(buttonListener);
		jrb3.addActionListener(buttonListener);
		ButtonGroup group = new ButtonGroup();
		group.add(jrb1);
		group.add(jrb2);
		group.add(jrb3);
		pm.add(jrb1);
		pm.add(jrb2);
		pm.add(jrb3);
		//结束
		
		JPanel stay = new JPanel();//停留时间的panel
		//stay的东东
		JLabel l4 = new JLabel("请输入停留时间:",JLabel.RIGHT);
		JTextField jtfstay = new JTextField(3);
		JLabel l5 = new JLabel("小时");
		JButton jbtstay = new JButton("确定");
		jbtstay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jtfstay.getText().equals(""))
					Main.showMessage("停留时间不能为空");
				else if (!isNumber(jtfstay.getText()))
					Main.showMessage("限制时间有误(不是数字)");
				else {
				try {
					((city)(selected.get(jlarrive.getSelectedIndex()))).stayTime = Integer.parseInt(jtfstay.getText());
					jldepart.setListData(unselected.toArray());
					jlarrive.setListData(selected.toArray());
					
				} catch (Exception ex) {
					
				}
			}
		}});
		JPanel stayl=new JPanel(new GridLayout(1,3));
		stayl.add(l4);
		stayl.add(jtfstay);
		stayl.add(l5);
		stay.add(stayl);
		stay.add(jbtstay);
		//结束
		
		JPanel startTime = new JPanel();//输入出发时间用panel
		//startTime的东东
		JLabel jlbstart1 = new JLabel("请输入出发时间(不输入默认为当前时间)：");
		JTextField jtfstart = new JTextField(10);
		JLabel jlbstart2 = new JLabel("时");
		startTime.add(jlbstart1);
		startTime.add(jtfstart);
		startTime.add(jlbstart2);
		//结束
		
		JPanel pd = new JPanel();//下面的panel
		//pd的东东
		JButton jbtok = new JButton("确定");
		JButton jbtcancel = new JButton("取消");
		jbtok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean pass = false;
				//记录是否按顺序旅行
				if (jrbisordered.isSelected())
					isOrdered = true;
				else isOrdered = false;
				//起始时间输入过滤
				if (jtfstart.getText().equals("")) {
					MainFrameBlank.startTime = (Integer.parseInt(Main.renewTime().toString().substring(11, 13)) + 1) % 24;
					pass = true;
				}
				else if (jtfstart.getText().length()>2) {
					Main.showMessage("起始时间有误(数据过长)");
					pass = false;
				}
				else if (!isNumber(jtfstart.getText())) {
					Main.showMessage("起始时间有误(不是数字)");
					pass = false;
				}
				else {
					pass = true;
					MainFrameBlank.startTime = Integer.parseInt(jtfstart.getText());
					if (MainFrameBlank.startTime < 0 || MainFrameBlank.startTime >= 24) {
						Main.showMessage("起始时间有误(不是时间)");
						pass = false;
					}
				}
				//策略输入过滤
				if (jrb1.isSelected())
					strategy = 1;
				else if (jrb2.isSelected())
					strategy = 2;
				else if (jrb3.isSelected()) {
					strategy = 3;
					if (jtf1.getText().equals("")) {
						Main.showMessage("限制时间不能为空");
						pass = false;
					}
					else if (!isNumber(jtf1.getText())) {
						Main.showMessage("限制时间有误(不是数字)");
						pass = false;
					}
					else {
						limitedTime = Integer.parseInt(jtf1.getText());
						pass = true;
					}
				}
				//列表过滤
				if (selected.size() < 1) {
					Main.showMessage("至少需要有一个出发城市");
					pass = false;
				}
				else if (selected.get(selected.size() - 1).equals(jcbfinal.getSelectedItem())) {
					Main.showMessage("不可以在一个城市绕圈");
					pass = false;
				}
				
				if (pass) {
					finalCity = (city)jcbfinal.getSelectedItem();
					Calculate.CMain();
				}
			}
		});
		jbtcancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		pd.add(jbtok);
		pd.add(jbtcancel);
		//结束
		
		JPanel test1=new JPanel(new BorderLayout());
		pu.add(puu,BorderLayout.NORTH);//修改新
		pu.add(pubuttom,BorderLayout.CENTER);
		test1.add(pu,BorderLayout.CENTER);
		test1.add(pm,BorderLayout.SOUTH);
		JPanel test2=new JPanel(new GridLayout(0,1));
		test2.add(stay);
		test2.add(startTime);
		test2.add(pd);
		JLabel jlprompt = new JLabel("PS：已选城市第一个为出发城市，选中已选城市可以添加停留时间，默认不停留");
      test2.add(jlprompt);
		
		
		this.add(test1,BorderLayout.CENTER);
		this.add(test2,BorderLayout.SOUTH);
		
		
//		this.add(pu);
//		this.add(pm);
//		this.add(button);
//		this.add(stay);
//		this.add(startTime);
//		this.add(pd);
//		JLabel jlprompt = new JLabel("PS：已选城市第一个为出发城市，选中已选城市可以添加停留时间，默认不停留");
//		this.add(jlprompt);//新加的，你排一下版
	}
}
