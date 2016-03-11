<<<<<<< HEAD
﻿import java.sql.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrameBlank extends JFrame {
	public static boolean isOrdered;//是否按顺序旅游
	public static void MFBMain() {
		JFrame frame = new MainFrameBlank();
		frame.setTitle("旅行规划系统Powered by 沐晓枫&茶叶");
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	MainFrameBlank() {
		setLayout(new GridLayout(5,1));
		JPanel pu = new JPanel(new GridLayout(2,3));//上面的panel
		JPanel pm = new JPanel();//中间的panel
		JPanel pd = new JPanel();//下面的panel
		JPanel button = new JPanel(new GridLayout(4,1));//中间按钮button
		JPanel stay = new JPanel(new GridLayout(1,4));//停留时间的panel
		this.add(pu);
		this.add(pm);
		this.add(pd);
		this.add(button);
		this.add(stay);
		JButton jbtr = new JButton(">>");
		JButton jbtl = new JButton("<<");
		JButton jbtu = new JButton("↑");
		JButton jbtd = new JButton("↓");
		button.add(jbtd);
		button.add(jbtu);
		button.add(jbtl);
		button.add(jbtr);
		JLabel l1 = new JLabel("已选城市：",JLabel.CENTER);
		JLabel l2 = new JLabel("可选城市：",JLabel.CENTER);
		JRadioButton jrbisordered=new JRadioButton("是否按顺序旅游");
		JButton jbtok = new JButton("确定");
		JLabel jlprompt = new JLabel("PS：已选城市第一个为出发城市，选中已选城市可以添加停留时间，默认不停留");
		JList jldepart = new JList((Object[])Main.city);
		JList jlarrive = new JList();
		jldepart.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jlarrive.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jbtok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jrbisordered.isSelected())
					isOrdered = true;
				else isOrdered = false;
				//if ()
			}
		});
		JButton jbtcancel = new JButton("取消");
		jbtcancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		JRadioButton jrb1 = new JRadioButton("最短时间",true);
		JRadioButton jrb2 = new JRadioButton("最便宜",false);
		JRadioButton jrb3 = new JRadioButton("规定时间内最便宜",false);
		JTextField jtf1 = new JTextField(2);
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
				pm.updateUI();
				pm.repaint();
			}
		};
		jrb1.addActionListener(buttonListener);
		jrb2.addActionListener(buttonListener);
		jrb3.addActionListener(buttonListener);
		ButtonGroup group = new ButtonGroup();
		group.add(jrb1);
		group.add(jrb2);
		group.add(jrb3);
		JLabel l4 = new JLabel("请输入停留时间:");
		JTextField jtfstay = new JTextField();
		JLabel l5 = new JLabel("小时");
		JButton jbtstay = new JButton("确定");
		stay.add(l4);
		stay.add(jtfstay);
		stay.add(l5);
		stay.add(jbtstay);
		this.add(jlprompt);//新加的，你排一下版
		pu.add(l1);
		pu.add(jrbisordered);
		pu.add(l2);
		pu.add(jldepart);
		pu.add(button);
		pu.add(jlarrive);
		pm.add(jrb1);
		pm.add(jrb2);
		pm.add(jrb3);
		pd.add(jbtok);
		pd.add(jbtcancel);
	}
}
=======
import java.sql.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrameBlank extends JFrame {
	public static void MFBMain() {
		JFrame frame = new MainFrameBlank();
		frame.setTitle("旅行规划系统Powered by 沐晓枫&茶叶");
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	MainFrameBlank() {
		setLayout(new GridLayout(3,1));
		JPanel pu = new JPanel(new GridLayout(2,3));//上面的panel
		JPanel pm = new JPanel();//中间的panel
		JPanel pd = new JPanel();//下面的panel
		JPanel button = new JPanel(new GridLayout(4,1));//中间按钮button
		this.add(pu);
		this.add(pm);
		this.add(pd);
		this.add(button);
		JButton jbtr = new JButton(">>");
		JButton jbtl = new JButton("<<");
		JButton jbtu = new JButton("↑");
		JButton jbtd = new JButton("↓");
		button.add(jbtd);
		button.add(jbtu);
		button.add(jbtl);
		button.add(jbtr);
		JLabel l1 = new JLabel("已选城市：",JLabel.CENTER);
		JLabel l2 = new JLabel("可选城市：",JLabel.CENTER);
		JRadioButton jrbisordered=new JRadioButton("是否已选城市顺序旅游");
		JButton jbtok = new JButton("确定");
		JList jldepart = new JList((Object[])Main.city);
		JList jlarrive = new JList((Object[])Main.city);
		//jbtok.addActionListener(new ActionListener() {
		//	public void actionPerformed(ActionEvent e) {
		//		if (jcbdepart.getSelectedIndex() == jcbarrive.getSelectedIndex())
		//			JOptionPane.showMessageDialog(null, "出发地和到达地不能相同！", "错误",JOptionPane.PLAIN_MESSAGE);
				
		//	}
		//});
		JButton jbtcancel = new JButton("取消");
		jbtcancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		JRadioButton jrb1 = new JRadioButton("最短时间",true);
		JRadioButton jrb2 = new JRadioButton("最便宜",false);
		JRadioButton jrb3 = new JRadioButton("规定时间内最便宜",false);
		JTextField jtf1 = new JTextField(2);
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
				pm.updateUI();
				pm.repaint();
			}
		};
		jrb1.addActionListener(buttonListener);
		jrb2.addActionListener(buttonListener);
		jrb3.addActionListener(buttonListener);
		ButtonGroup group = new ButtonGroup();
		group.add(jrb1);
		group.add(jrb2);
		group.add(jrb3);
		pu.add(l1);
		pu.add(jrbisordered);
		pu.add(l2);
		pu.add(jldepart);
		pu.add(button);
		pu.add(jlarrive);
		pm.add(jrb1);
		pm.add(jrb2);
		pm.add(jrb3);
		pd.add(jbtok);
		pd.add(jbtcancel);
	}
}
>>>>>>> origin/master
