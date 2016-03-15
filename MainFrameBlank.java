import java.sql.*;
import java.util.ArrayList;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrameBlank extends JFrame {
	public static JFrame frame;
	public static boolean isOrdered;//是否按顺序旅游
	public static int startTime;//出发时间
	public static int limitedTime;//限制的时间
	public static char strategy;//旅行策略：1、时间最短2、金钱最少3、时间限定金钱最少
	public static ArrayList unselected;//未选城市
	public static ArrayList selected;//已选城市
	public static boolean isNumber(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (!Character.isDigit(s.charAt(i)))
				return false;
		}
		return true;	
	}
	public static void MFBMain() {
		frame = new MainFrameBlank();
		frame.setTitle("旅行规划系统Powered by 沐晓枫&茶叶");
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	MainFrameBlank() {
		setLayout(new GridLayout(5,1));
		
		JPanel pu = new JPanel(new GridLayout(2,3));//上面的panel
		//pu的东东
		JLabel l1 = new JLabel("已选城市：",JLabel.CENTER);
		JLabel l2 = new JLabel("可选城市：",JLabel.CENTER);
		JRadioButton jrbisordered=new JRadioButton("是否按顺序旅游");
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
			public void actionPerformed(ActionEvent e) {
				try {
					if(e.getSource().equals(jbtr)) {
						selected.add((city)jldepart.getSelectedValue());
						unselected.remove(jldepart.getSelectedIndex());
					}
					else if(e.getSource().equals(jbtl)) {
						unselected.add((city)jlarrive.getSelectedValue());
						selected.remove(jlarrive.getSelectedIndex());
					}
					else if (e.getSource().equals(jbtu)) {
						Object temp = selected.get(jlarrive.getSelectedIndex() - 1);
						selected.set(jlarrive.getSelectedIndex() - 1, jlarrive.getSelectedValue());
						selected.set(jlarrive.getSelectedIndex(), temp);
					}
					else if (e.getSource().equals(jbtd)) {
						Object temp = selected.get(jlarrive.getSelectedIndex() + 1);
						selected.set(jlarrive.getSelectedIndex() + 1, jlarrive.getSelectedValue());
						selected.set(jlarrive.getSelectedIndex(), temp);
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
		pu.add(button);
		//结束
		pu.add(l1);
		pu.add(jrbisordered);
		pu.add(l2);
		pu.add(jldepart);
		pu.add(jlarrive);
		//结束
		
		JPanel pm = new JPanel();//中间的panel
		//pm的东东
		JRadioButton jrb1 = new JRadioButton("最短时间",true);
		JRadioButton jrb2 = new JRadioButton("最便宜",false);
		JRadioButton jrb3 = new JRadioButton("规定时间内最便宜",false);
		JTextField jtf1 = new JTextField(3);
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
		
		JPanel stay = new JPanel(new GridLayout(1,4));//停留时间的panel
		//stay的东东
		JLabel l4 = new JLabel("请输入停留时间:");
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
		stay.add(l4);
		stay.add(jtfstay);
		stay.add(l5);
		stay.add(jbtstay);
		//结束
		
		JPanel startTime = new JPanel(new GridLayout(1,3));//输入出发时间用panel
		//startTime的东东
		JLabel jlbstart1 = new JLabel("请输入出发时间(不输入默认为当前时间)：");
		JTextField jtfstart = new JTextField(2);
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
					MainFrameBlank.startTime = Integer.parseInt(Main.renewTime().toString().substring(11, 13));
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
				if (selected.size() <= 1) {
					Main.showMessage("旅行城市至少需要两个(包括一个出发城市)");
					pass = false;
				}
				if (pass)
					Calculate.CMain();
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
		
		this.add(pu);
		this.add(pm);
		this.add(pd);
		this.add(button);
		this.add(stay);
		this.add(startTime);
		JLabel jlprompt = new JLabel("PS：已选城市第一个为出发城市，选中已选城市可以添加停留时间，默认不停留");
		this.add(jlprompt);//新加的，你排一下版
	}
}

