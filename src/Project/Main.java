package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import Base.Base;
import Joption.Err;

public class Main extends JFrame implements Base{
	
	JPanel p1;
	JPanel p2 = get(new JPanel(new FlowLayout(FlowLayout.RIGHT)));
	JPanel p3 = get(new JPanel(), set(new EmptyBorder(80, 0, 0, 0)));
	JPanel p4 = get(new JPanel(new BorderLayout(0, 10)), set(780, 100));
	JPanel p5 = get(new JPanel(new FlowLayout(FlowLayout.LEFT)));
	JPanel p6 = get(new JPanel(null), set(0, 300));
	JPanel cal = get(new JPanel(new GridLayout(3, 3)), set(180, 80));
	
	JLabel lab1 = get(new JLabel("예매"), set(25));
	JLabel lab2 = get(new JLabel("추천 여행지"), set(25));
	
	JButton btn1 = get(new JButton("테마"), setb(Color.DARK_GRAY), setf(Color.white));
	JButton btn2 = get(new JButton("계정"));
	JButton btn3 = get(new JButton("에매"));
	JButton btn4 = get(new JButton("로그아웃"));
	JButton btn5 = get(new JButton("<html>←<br>→"));
	JButton btn6 = get(new JButton("조회"), set(150, 30));
	
	JButton up[] = new JButton[3];
	JButton down[] = new JButton[3];
	
	JTextField txt1 = gettxt(new JTextField(), "출발지", set(180, 30));
	JTextField txt2 = gettxt(new JTextField(), "도착지", set(180, 30));
	JTextField txt3 = gettxt(new JTextField(), "날짜", set(180, 30));
	
	ArrayList<ArrayList<String>> imgs = new ArrayList<>();
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	JPopupMenu pop1 = new JPopupMenu();
	JPopupMenu pop2 = new JPopupMenu();
	JPopupMenu pop3 = new JPopupMenu();
	
	JMenuItem item1 = new JMenuItem("상세설명");
	JMenuItem item2 = new JMenuItem("예매");
	
	LocalDate date = LocalDate.now();
	
	boolean b = false;
	int index= 0;
	
	public Main() {
		
		SetFrame(this, "버스예매", DISPOSE_ON_CLOSE, 1000, 600);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new Login();
			}
		});
		
	}

	@Override
	public void design() {
		
		pop2.add(cal);
		pop3.add(item1);
		pop3.add(item2);
		
		add(p1 = get(new JPanel(new BorderLayout()) {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon(file("main.jpg")).getImage(), 0, 0, 1000, 300, null);
			}
		}));
		add(p6, "South");
		
		p1.add(p2, "North");
		p1.add(p3);
		
		p2.add(btn1);
		p2.add(btn2);
		p2.add(btn3);
		p2.add(btn4);
		
		p3.add(p4);
		
		p4.add(lab1, "North");
		p4.add(p5);
		
		p5.add(txt1);
		p5.add(btn5);
		p5.add(txt2);
		p5.add(txt3);
		p5.add(btn6);
		
		p2.setOpaque(false);
		p3.setOpaque(false);

		cu();
		cal();
		
		
	}
	
	public void cu() {
		
		p6.removeAll();
		
		p6.add(lab2);
		lab2.setBounds(40, 40, 150, 30);
		
		Query("select * from recommend_info ri, recommend r, location l where ri.recommend_no = r.no and r.location_no = l.no group by recommend_no order by title, recommend_no", imgs);
		
		JPanel p;
		for (int i = 0; i < imgs.size(); i++) {
			
			p6.add(p = get(new JPanel(new BorderLayout()), set(new TitledBorder(new LineBorder(Login.fore), imgs.get(i).get(7), 0, 2, new Font("", 1, 12), Login.fore))));
			
			JLabel lab = DBimg(imgs.get(i).get(0), imgs.get(i).get(1), 150, 150);
			p.add(lab);
			p.setBounds(40 + (i * 185), i%2!=0 ? 100 : 80, 150, 150);
			
			int j = i;
			
			p.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == 3) {
						pop3.show(lab, e.getX(), e.getY());
						index = j;
					}
				}
			});
			
		}
		
		revalidate();
		repaint();
		
	}
	
	public void cal() {
		
		cal.removeAll();
		cal.setBackground(Login.back);
		
		for (int i = 0; i < up.length; i++) {
			
			up[i] = new JButton("▲");
			int j = i;
			
			up[i].addActionListener(e->{
				if (j == 0) {
					date = date.plusYears(1);
				}else if (j == 1) {
					date = date.plusMonths(1);
				}else {
					date = date.plusDays(1);
				}
				cal();
			});
			cal.add(up[i]);
		}
		
		JLabel y = get(new JLabel(date.getYear() + "", JLabel.CENTER));
		JLabel m = get(new JLabel(date.getMonthValue() + "", JLabel.CENTER));
		JLabel d = get(new JLabel(date.getDayOfMonth() + "", JLabel.CENTER));
		
		cal.add(y);
		cal.add(m);
		cal.add(d);
		
		for (int i = 0; i < down.length; i++) {
			
			down[i] = new JButton("▼");
			int j = i;
			
			down[i].addActionListener(e->{
				if (j == 0) {
					date  = date.plusYears(-1);
				}else if (j == 1) {
					date = date.plusMonths(-1);
				}else {
					date = date.plusDays(-1);
				}
				cal();
			});
			cal.add(down[i]);
		}
		
		repaint();
		revalidate();
		
	}
	
	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			
			change(btn1);
			
			tema(p1);
			tema(p2);
			tema(p3);
			tema(p4);
			tema(p5);
			tema(p6);
			cu();
			cal();
			
		});
		
		btn2.addActionListener(e->{
			
			String a = JOptionPane.showInputDialog(null, "비밀번호를 입력해주세요.", "입력", JOptionPane.QUESTION_MESSAGE);
			if (a.contentEquals(member.get(0).get(2))) {
				new Accent();
			}
			
		});
		
		btn3.addActionListener(e->{
			new Reserve();
		});
		
		btn4.addActionListener(e->{
			dispose();
			new Login();
		});
		
		btn5.addActionListener(e->{
			
			if (txt1.getText().isBlank() || txt2.getText().isBlank()) {
				return;
			}
			
			String txt = txt2.getText();
			String name = txt2.getName();
			
			txt2.setText(txt1.getText());
			txt2.setName(txt1.getName());
			
			txt1.setText(txt);
			txt1.setName(name);
			
		});
		
		btn6.addActionListener(e->{
			
			if (txt1.getText().isBlank() || txt2.getText().isBlank() || txt3.getText().isBlank()) {
				new Err("출발지, 도착지, 날짜 중 공란이 있습니다.");
			}else {
				
				Query("select * from schedule where departure_location2_no = ? and arrival_location2_no = ? and left(date , 10) = ?", list, txt1.getName(), txt2.getName(), txt3.getText());
				
				if (list.isEmpty()) {
					new Err("예매 가능한 일정이없습니다.");
				}else {
					new Ticket(list, txt1.getText(), txt2.getText());
				}
				
			}
			
		});
		
		txt1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3) {
					popup(pop1, txt1, 0, 0);
					pop1.show(txt1, 0, 20);
				}
			}
		});
		
		txt2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3) {
					popup(pop1, txt2, 0, 0);
					pop1.show(txt2, 0, 20);
				}
			}
		});
		
		txt3.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3) {
					pop2.show(txt3, 0, 20);
					b = true;
				}
			}
		});
		
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (b) {
					txt3.setText(date.toString());
					b = false;
				}
			}
		});
		
		item1.addActionListener(e->{
			new Detail(imgs.get(index).get(0));
		});
		
		item2.addActionListener(e->{
			
			popup(pop1, txt2, 0, 0);
			pop1.show(txt2, 0, 20);
			
			Query("select * from location2 where location_no = ?", location2, imgs.get(index).get(5));
			
			pitem2.removeAll();
			for (int i = 0; i < location2.size(); i++) {
				
				JButton btn = new JButton(location2.get(i).get(1));
				
				int n = i;
				btn.addActionListener(e2->{
					txt2.setText(imgs.get(index).get(7) + " " + btn.getText());
					txt2.setName(location2.get(n).get(0));
				});
				
				pitem2.add(btn);
				
			}
			
			pitem2.revalidate();
			pitem2.repaint();
			
		});
		
	}

}
