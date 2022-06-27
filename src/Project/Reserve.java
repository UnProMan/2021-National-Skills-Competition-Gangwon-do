package Project;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Base.Base;
import Joption.Err;
import Joption.Jop;

public class Reserve extends JDialog implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout(0, 10)), set(new EmptyBorder(20, 20, 20, 20)));
	
	JLabel lab1 = get(new JLabel("예매조회"), set(30));
	
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
	JScrollPane scl = new JScrollPane(tbl);
	
	JPopupMenu pop = new JPopupMenu();
	JMenuItem item1 = new JMenuItem("취소");
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	public Reserve() {
		
		SetDial(this, "예매조회", DISPOSE_ON_CLOSE, 700, 500);
		design();
		action();
		setVisible(true);
		
	}

	@Override
	public void design() {
		
		pop.add(item1);
		
		add(p1);
		
		p1.add(lab1, "North");
		p1.add(scl);
		
		table();
		tblcenter(tbl);
		
	}
	
	public void table() {
		
		model.setNumRows(0);
		Query("select (select concat(l.name , ' ', l2.name) from location l, location2 l2 where l2.location_no = l.no and l2.no = s.departure_location2_no), (select concat(l.name , ' ', l2.name) from location l, location2 l2 where l2.location_no = l.no and l2.no = s.arrival_location2_no), s.date, s.elapsed_time, r.no from reservation r, schedule s where r.schedule_no = s.no and r.user_no = ? order by date", list, member.get(0).get(0));
		
		for (int i = 0; i < list.size(); i++) {
			
			LocalDateTime dep = LocalDateTime.of(LocalDate.parse(list.get(i).get(2).split(" ")[0]), LocalTime.parse(list.get(i).get(2).split(" ")[1]));
			LocalTime el = LocalTime.parse(list.get(i).get(3));
			LocalDateTime fin = dep.plusHours(el.getHour()).plusMinutes(el.getMinute());
			
			model.addRow(new Object[] {i + 1, list.get(i).get(0), list.get(i).get(1), fin.format(df2), dep.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))});
			
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
					pop.show(tbl, e.getX(), e.getY());
				}
				
			}
		});
		
		item1.addActionListener(e->{
			
			int row = intnum(tbl.getValueAt(tbl.getSelectedRow(), 0).toString()) -1;
			String s[] = list.get(row).get(2).split(" ");
			
			if (LocalDateTime.of(LocalDate.parse(s[0]), LocalTime.parse(s[1])).isBefore(LocalDateTime.now())) {
				new Err("취소 불가능한 일정입니다.");
			}else {
				
				new Jop("예매 취소가 완료되었습니다.");
				
				int t = LocalTime.parse(list.get(row).get(3)).getHour();
				String point = "";
				
				if (t <= 1) {
					point = "100";
				}else if (t <= 2) {
					point = "300";
				}else if (t <= 3) {
					point = "500";
				}else {
					point = "700";
				}
				
				Update("update user set point = point + ? where no = ?", point, member.get(0).get(0));
				Update("delete from reservation where no = ?", list.get(row).get(4));
				Query("select * from user where no = ?", member, member.get(0).get(0));
				
				table();
				
			}
			
		});
		
	}

}
