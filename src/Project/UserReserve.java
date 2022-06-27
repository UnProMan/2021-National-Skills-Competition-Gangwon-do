package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Base.Base;

public class UserReserve extends JDialog implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout()), set(new EmptyBorder(10, 10, 10, 10)));
	JPanel p2;
	
	Vector v1;
	Vector v2 = new Vector(Arrays.asList("no, 출발지, 도착지, 도착시간, 출발날짜".split(", ")));
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
	JScrollPane scl = get(new JScrollPane(tbl), set(0, 220));
	
	JLabel lab1 = get(new JLabel("사용자 예매 정보"), set(25));
	String no;
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<ArrayList<String>> temp = new ArrayList<>();
	
	public UserReserve(String no) {
		
		this.no = no;
		
		SetDial(this, "예매 정보", DISPOSE_ON_CLOSE, 800, 600);
		design();
		action();
		setVisible(true);
		
	}

	@Override
	public void design() {
		
		add(p1);
		
		p1.add(lab1, "North");
		p1.add(scl, "South");
		
		Query("select (select concat(l.name , ' ', l2.name) from location l, location2 l2 where l2.location_no = l.no and l2.no = s.departure_location2_no), (select concat(l.name , ' ', l2.name) from location l, location2 l2 where l2.location_no = l.no and l2.no = s.arrival_location2_no), s.date, s.elapsed_time, r.no from reservation r, schedule s where r.schedule_no = s.no and r.user_no = ? order by date", list, no);
		
		for (int i = 0; i < list.size(); i++) {
			
			LocalDateTime dep = LocalDateTime.of(LocalDate.parse(list.get(i).get(2).split(" ")[0]), LocalTime.parse(list.get(i).get(2).split(" ")[1]));
			LocalTime el = LocalTime.parse(list.get(i).get(3));
			LocalDateTime fin = dep.plusHours(el.getHour()).plusMinutes(el.getMinute());
			
			model.addRow(new Object[] {i + 1, list.get(i).get(0), list.get(i).get(1), fin.format(df2), dep.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))});
			
		}
		tblcenter(tbl);
		
		p1.add(p2 = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				this.setBackground(Login.back);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				g2.setColor(Login.fore);
				g2.drawLine(20, 30, 20, 250);
				g2.drawLine(20, 40, 700, 40);
				g2.drawLine(20, 145, 700, 145);
				g2.drawLine(20, 250, 700, 250);
				
				Query("select count(r.no), month(date), rank() over(order by count(r.no) desc) from reservation r, schedule s where r.schedule_no = s.no and r.user_no = ? group by month(date) order by month(date);", temp, no);
				
				int y[] = {40, 145, 240};
				int nums[] = {210, 105, 10};
				
				for (int i = 0; i < temp.size(); i++) {
					
					g2.setColor(new Color(0, 123, 255));
					g2.fillRect(165 + (i * 200), y[intnum(temp.get(i).get(2))- 1], 100, nums[intnum(temp.get(i).get(2)) -1]);
					
					g2.setColor(Login.fore);
					g2.drawString(temp.get(i).get(1) + "월", 205 + (i * 200), 270);
					g2.fillOval(205 + (i * 200), y[intnum(temp.get(i).get(2)) -1] - 5, 10, 10);
					g2.drawString(temp.get(2-i).get(0), 2, 40 + (i * 108));
					
				}
				
				for (int i = 0; i < temp.size() -1; i++) {
					g2.drawLine(205 + (i * 200), y[intnum(temp.get(i).get(2))-1], 205 + ((i +1) * 200), y[intnum(temp.get(i+1).get(2)) -1]);
				}
				
			}
		});
		
	}
	
	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}

}
