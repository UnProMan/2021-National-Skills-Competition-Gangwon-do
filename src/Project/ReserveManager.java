package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Base.Base;
import Joption.Err;
import Joption.Jop;

public class ReserveManager extends JPanel implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout()));
	JPanel p2 = get(new JPanel(new FlowLayout(0)), set(280, 35));
	JPanel p3= get(new JPanel());
	JPanel p4 = get(new JPanel(new BorderLayout()), set(0, 200));
	JPanel p5 = get(new JPanel(new FlowLayout(FlowLayout.RIGHT)));
	
	JLabel lab1 = get(new JLabel("예매 관리"), set(25));
	JLabel lab2 = get(new JLabel("<가장 예매가 많은 일정 TOP 6>"));
	
	JComboBox com1 = get(new JComboBox("2차원 영역형, 방사형".split(", ")));
	
	JButton btn1 = get(new JButton("저장"), set(120, 30));
	JButton btn2 = get(new JButton("삭제"), set(120, 30));
	
	Vector v1;
	Vector v2 = new Vector(Arrays.asList("순번, 예매자, 출발지, 도착지, 출발날짜, 도착시간".split(", ")));
	DefaultTableModel model = new DefaultTableModel(v1, v2) {
		public boolean isCellEditable(int row, int column) {
			return false;
		};
		public java.lang.Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == 0) {
				return Integer.class;
			}else {
				return String.class;
			}
		};
	};
	JTable tbl = new JTable(model);
	JScrollPane scl = new JScrollPane(tbl);
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<ArrayList<String>> temp = new ArrayList<>();
	ArrayList<ArrayList<String>> area = new ArrayList<>();
	
	ArrayList<ArrayList<String>> lo1 = new ArrayList<>();
	ArrayList<ArrayList<String>> lo2 = new ArrayList<>();
	
	JPopupMenu pop = new JPopupMenu();
	
	public ReserveManager() {
		
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setBackground(Login.back);
		
		design();
		action();
		
		com1.setSelectedIndex(Admin.combo);
		
	}

	@Override
	public void design() {
		
		add(p1, "North");
		add(p3);
		add(p4, "South");
		
		p1.add(p2, "West");
		p1.add(lab2);
		
		p2.add(lab1);
		p2.add(com1);
		
		p4.add(scl);
		p4.add(p5, "South");
		
		p5.add(btn1);
		p5.add(btn2);
		
		table();
		tblcenter(tbl);
		
	}
	
	public void table() {
		
		model.setNumRows(0);
		
		Query("select u.name, (select concat(l.name , ' ', l2.name) from location l, location2 l2 where l2.location_no = l.no and l2.no = s.departure_location2_no), (select concat(l.name , ' ', l2.name) from location l, location2 l2 where l2.location_no = l.no and l2.no = s.arrival_location2_no), s.date, s.elapsed_time, r.no from reservation r, schedule s, user u where r.schedule_no = s.no and r.user_no = u.no order by r.no", list);
		
		for (int i = 0; i < list.size(); i++) {
			
			LocalDateTime dep = LocalDateTime.of(LocalDate.parse(list.get(i).get(3).split(" ")[0]), LocalTime.parse(list.get(i).get(3).split(" ")[1]));
			LocalTime el = LocalTime.parse(list.get(i).get(4));
			LocalDateTime fin = dep.plusHours(el.getHour()).plusMinutes(el.getMinute());
			
			model.addRow(new Object[] {i + 1, list.get(i).get(0), list.get(i).get(1), list.get(i).get(2), list.get(i).get(3), fin.format(df2)});
		}
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void action() {
		
		tbl.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				if (tbl.getSelectedRow() == -1) return;
				
				if (e.getButton() == 3) {
					if (tbl.getSelectedColumn() == 2 || tbl.getSelectedColumn() == 3) {
						popup(pop, tbl, tbl.getSelectedRow(), tbl.getSelectedColumn());
						pop.show(tbl, e.getX(), e.getY());
					}
				}
				
			}
		});
		
		btn1.addActionListener(e->{
			
			int n = 0;
			
			for (int i = 0; i < tbl.getRowCount(); i++) {
				
				int r = intnum(tbl.getValueAt(i, 0).toString()) -1;
				
				if (!tbl.getValueAt(i, 2).toString().contentEquals(list.get(r).get(1)) || !tbl.getValueAt(i, 3).toString().contentEquals(list.get(r).get(2))) {
					
					Query("select l2.no from location2 l2, location l where l2.location_no = l.no and l.name = ? and l2.name = ?", lo1, tbl.getValueAt(i, 2).toString().split(" ")[0], tbl.getValueAt(i, 2).toString().split(" ")[1]);
					Query("select l2.no from location2 l2, location l where l2.location_no = l.no and l.name = ? and l2.name = ?", lo2, tbl.getValueAt(i, 3).toString().split(" ")[0], tbl.getValueAt(i, 3).toString().split(" ")[1]);
					
					Query("select * from schedule where departure_location2_no	= ? and arrival_location2_no = ? and date = ?", temp, lo1.get(0).get(0), lo2.get(0).get(0), list.get(r).get(3));
					
					if (temp.isEmpty()) {
						new Err("없는 배차입니다.");
					}else {
						n = 1;
						Update("update reservation set schedule_no = ? where no = ?", temp.get(0).get(0), list.get(r).get(5));
					}
					
				}
				
			}
			
			if (n == 1) {
				new Jop("수정 내용을 저장 완료하였습니다.");
			}
			table();
			
		});
		
		btn2.addActionListener(e->{
			
			if (tbl.getSelectedRow() != -1) {
				
				new Jop("삭제를 완료하였습니다.");
				
				int r = intnum(tbl.getValueAt(tbl.getSelectedRow(), 0).toString()) -1;
				
				Update("delete from reservation where no = ?", list.get(r).get(5));
				
				table();
				
			}
			
		});
		
		com1.addActionListener(e->{
			remove(p3);
			if (com1.getSelectedIndex() == 0) {
				chart1();
			}else {
				chart2();
			}
			Admin.combo = com1.getSelectedIndex();
		});
		
	}
	
	void chart1() {
		
		p3= new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				g2.setColor(Login.fore);
				g2.drawLine(50, 20, 50, 250);
				for (int i = 0; i < 5; i++) {
					g2.drawLine(50, 20 + (i * 58), 600, 20 + (i * 58));
				}
				
				Polygon p = new Polygon();
				Query("select *, count(schedule_no) from reservation group by schedule_no order by count(schedule_no) desc, schedule_no limit 6;", area);
				
				int max = intnum(area.get(0).get(3));
				
				for (int i = 0; i < area.size(); i++) {
					
					String s = (max - (i * 2) <= 0 ? 0 : max - (i * 2)) + "";
					
					g2.drawString(s, 30, 20 + (i * 58));
					g2.drawString(area.get(i).get(2), 45 + (i * 108), 275);
					p.addPoint(50 + (i * 110), 20 + ((max - intnum(area.get(i).get(3))) * 29));
					
				}
				
				p.addPoint(600, 252);
				p.addPoint(50, 252);
				g2.setColor(new Color(0, 123, 255));
				g2.fillPolygon(p);
				
			}
		};
		
		p3.setBackground(Login.back);
		add(p3);
		
		repaint();
		revalidate();
		
	}
	
	void chart2() {
		
		p3 = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				g2.setColor(Login.fore);
				
				int po[][] = new int[6][2];
				Polygon p[] = {new Polygon(), new Polygon(),new Polygon(),new Polygon(),new Polygon(),new Polygon()};
				
				for (int i = 0; i < 5; i++) {
					for (int j = 0; j < 6; j++) {
						
						int x = (int) (380 + (100 - i * 20) * Math.cos(j * Math.PI / 3 + (Math.PI / 6)));
						int y = (int) (150  + (100 - i * 20) * Math.sin(j * Math.PI / 3 + (Math.PI / 6)));
						
						p[i].addPoint(x, y);
						
					}
					g2.drawPolygon(p[i]);
				}
				
				for (int i = 0; i < 6; i++) {
					po[i][0] = (int) (375 + 110 * Math.cos(i * Math.PI / 3 - (Math.PI / 2)));
					po[i][1] = (int) (150 + 110 * Math.sin(i * Math.PI / 3 - (Math.PI / 2)));
				}
				
				Query("select *, count(schedule_no) from reservation group by schedule_no order by count(schedule_no) desc, schedule_no limit 6;",area);
			
				int max = intnum(area.get(0).get(3));
				int bx = po[0][0] / max;
				int by = po[0][1] / max;
				int num[] = new int[6];
				
				for (int i = 0; i < area.size(); i++) {
					
					String s = (max - i * 2 <= 0 ? "" : max - i * 2) + "";
					
					g2.drawString(s, 370, 50 + (i * 20));
					g2.drawString(area.get(i).get(2), po[i][0], po[i][1]);
					num[i] = intnum(area.get(i).get(3));
					
				}
				
				g2.setColor(new Color(0, 123, 255));
				Polygon poly = new Polygon();
				for (int i = 0; i < num.length; i++) {
					
					int x = (int) (380 + (100 - (num[0] - num[i]) / 2.0 * 20) * Math.cos(i * Math.PI / 3 - (Math.PI / 2)));
					int y = (int) (150 + (100 - (num[0] - num[i]) / 2.0 * 20) * Math.sin(i * Math.PI / 3 - (Math.PI / 2)));
					poly.addPoint(x, y);
					
				}	
				
				g2.drawPolygon(poly);
				
			}
		};
		
		p3.setBackground(Login.back);
		add(p3);
		
		repaint();
		revalidate();
		
	}
	
}
