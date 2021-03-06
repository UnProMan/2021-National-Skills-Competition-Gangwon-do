package Project;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Base.Base;
import Joption.Err;
import Joption.Jop;

public class UserManager extends JPanel implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout()));
	JPanel p2 = get(new JPanel(new FlowLayout(FlowLayout.RIGHT)));
	JPanel p3 = get(new JPanel(new FlowLayout(FlowLayout.RIGHT)));
	
	JButton btn1 = get(new JButton("사용자 조회"));
	JButton btn2 = get(new JButton("저장"), set(120, 30));
	JButton btn3 = get(new JButton("삭제"), set(120, 30));
	
	JTextField txt1 = gettxt(new JTextField(), "성명", set(180, 30));
	JLabel lab1= get(new JLabel("사용자 관리"), set(25));
	
	Vector v1;
	Vector v2 = new Vector(Arrays.asList("순번, 아이디, 비밀번호, 성명, 이메일, 포인트, 예매수".split(", ")));
	DefaultTableModel model = new DefaultTableModel(v1, v2) {
		public boolean isCellEditable(int row, int column) {
			if (column == 0) {
				return false;
			}else {
				return true;
			}
		};
		public java.lang.Class<?> getColumnClass(int columnIndex) {
			if (columnIndex == 0 || columnIndex ==  6) {
				return Integer.class;
			}else {
				return String.class;
			}
		};
	};
	JTable tbl = new JTable(model);
	JScrollPane scl = new JScrollPane(tbl);
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	JPopupMenu pop = new JPopupMenu();
	JMenuItem item1 = new JMenuItem("예매 조회");
	
	public UserManager() {
		
		setLayout(new BorderLayout(0, 10));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setBackground(Login.back);
		
		design();
		action();
		
	}

	@Override
	public void design() {
		
		pop.add(item1);
		
		add(p1, "North");
		add(scl);
		add(p3, "South");
		
		p1.add(lab1);
		p1.add(p2, "East");
		
		p2.add(txt1);
		p2.add(btn1);
		
		p3.add(btn2);
		p3.add(btn3);
		
		table();
		tblcenter(tbl);
		
	}

	public void table() {
		
		model.setNumRows(0);
		
		Query("SELECT u.*, count(user_no) FROM user u left join reservation r on r.user_no = u.no where name like ? group by user_no order by u.no;", list, "%" + txt1.getText() + "%");
		
		for (int i = 0; i < list.size(); i++) {
			model.addRow(new Object[] {i + 1, list.get(i).get(1), list.get(i).get(2), list.get(i).get(3), list.get(i).get(4), list.get(i).get(5), list.get(i).get(6)});
		}
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void action() {
		
		tbl.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				if (tbl.getSelectedRow() == -1) return;
				
				if (e.getButton() ==3) {
					pop.show(tbl, e.getX(), e.getY());
				}
				
			}
		});
		
		btn1.addActionListener(e->{
			table();
		});
		
		btn2.addActionListener(e->{
			
			boolean b = false;
			
			for (int i = 0; i < tbl.getRowCount(); i++) {
				
				int r = intnum(tbl.getValueAt(i, 0).toString()) -1;
				
				for (int j = 1; j < 6; j++) {
					if (!tbl.getValueAt(i, j).toString().contentEquals(list.get(r).get(j))) {
						
						 b = true;
						 Update("update user set id = ?, pwd = ?, name = ?, email =?, point = ? where no =?", tbl.getValueAt(i, 1).toString(), tbl.getValueAt(i, 2).toString(), tbl.getValueAt(i, 3).toString(), tbl.getValueAt(i, 4).toString(), tbl.getValueAt(i, 5).toString(), list.get(r).get(0));
						
					}
				}
				
			}
			
			if (b) {
				new Jop("수정내용을 저장 안료하였습니다.");
				table();
			}
			
		});
		
		btn3.addActionListener(e->{
			
			if (tbl.getSelectedRow() == -1) {
				new Err("레코드를 선택해주세요.");
			}else {
				
				new Jop("삭제를 완료하였습니다.");
				
				int r = intnum(tbl.getValueAt(tbl.getSelectedRow(), 0).toString()) -1;
				
				Update("delete from user where no = ?", list.get(r).get(0));
				table();
				
			}
			
		});
		
		item1.addActionListener(e->{
			int r = intnum(tbl.getValueAt(tbl.getSelectedRow(), 0).toString()) -1;
			new UserReserve(list.get(r).get(0));
		});
		
	}

}
