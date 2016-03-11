<<<<<<< HEAD
﻿import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.jar.Attributes.Name;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class Login extends JFrame
{
	private JTextField jtfname=new JTextField(20);
	private JPasswordField jtfpwd=new JPasswordField(20);
	
	public static void login()
	{
		Login lg=new Login();
		lg.setTitle("登陆");
		lg.pack();
		lg.setLocationRelativeTo(null);
		lg.setDefaultCloseOperation(EXIT_ON_CLOSE);
		lg.setVisible(true);
	}
	
	public Login()
	{
		JLabel jlbname=new JLabel("帐号：",JLabel.CENTER);
		JLabel jlbpwd=new JLabel("密码：",JLabel.CENTER);
		
		JPanel ptop=new JPanel();
		ptop.add(jlbname);
		ptop.add(jtfname);
		ptop.add(jlbpwd);
		ptop.add(jtfpwd);
		ptop.setLayout(new GridLayout(2,2));
		
		JButton cfm=new JButton("确定");
		cfm.addActionListener(new ActionListener() 
		{
			
			public void actionPerformed(ActionEvent e) 
			{
				Main.NAME=jtfname.getText();
				Main.PASSWORD=jtfpwd.getText();
				System.out.println(Main.NAME+" "+Main.PASSWORD);
			}
		});
		JButton exit=new JButton("取消");
		exit.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				System.exit(0);
			}
		});
		
		JPanel pbuttom=new JPanel();
		pbuttom.add(cfm);
		pbuttom.add(exit);
		pbuttom.setLayout(new GridLayout(1,2));
		
		setLayout(new BorderLayout());
		add(ptop,BorderLayout.NORTH);
		add(pbuttom,BorderLayout.SOUTH);
	}
}
=======
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.jar.Attributes.Name;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class Login extends JFrame
{
	private JTextField jtfname=new JTextField(20);
	private JPasswordField jtfpwd=new JPasswordField(20);
	
	public static void login()
	{
		Login lg=new Login();
		lg.setTitle("登陆");
		lg.pack();
		lg.setLocationRelativeTo(null);
		lg.setDefaultCloseOperation(EXIT_ON_CLOSE);
		lg.setVisible(true);
	}
	
	public Login()
	{
		JLabel jlbname=new JLabel("帐号：",JLabel.CENTER);
		JLabel jlbpwd=new JLabel("密码：",JLabel.CENTER);
		
		JPanel ptop=new JPanel();
		ptop.add(jlbname);
		ptop.add(jtfname);
		ptop.add(jlbpwd);
		ptop.add(jtfpwd);
		ptop.setLayout(new GridLayout(2,2));
		
		JButton cfm=new JButton("确定");
		cfm.addActionListener(new ActionListener() 
		{
			
			public void actionPerformed(ActionEvent e) 
			{
				Main.NAME=jtfname.getText();
				Main.PASSWORD=jtfpwd.getText();
				System.out.println(Main.NAME+" "+Main.PASSWORD);
			}
		});
		JButton exit=new JButton("取消");
		exit.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				System.exit(0);
			}
		});
		
		JPanel pbuttom=new JPanel();
		pbuttom.add(cfm);
		pbuttom.add(exit);
		pbuttom.setLayout(new GridLayout(1,2));
		
		setLayout(new BorderLayout());
		add(ptop,BorderLayout.NORTH);
		add(pbuttom,BorderLayout.SOUTH);
	}
}
>>>>>>> origin/master
