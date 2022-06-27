package Base;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Window.Type;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

import Project.Login;

public interface Base {
	
	public void design();
	public void action();
	
	ArrayList<ArrayList<String>> member = new ArrayList<>();
	
	ArrayList<ArrayList<String>> location = new ArrayList<>();
	ArrayList<ArrayList<String>> location2 = new ArrayList<>();
	
	JPanel pitem2 = new JPanel(new GridLayout(0, 1));
	
	DateTimeFormatter df1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	DateTimeFormatter df2 = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	default void SetFrame(JFrame f, String title, int ex, int w, int h) {
		f.setTitle(title);
		f.setDefaultCloseOperation(ex);
		f.setSize(w, h);
		f.setLocationRelativeTo(null);
		f.setResizable(false);
		f.getContentPane().setBackground(Login.back);
	}
	
	default void SetDial(JDialog d, String title, int ex, int w, int h) {
		d.setTitle(title);
		d.setDefaultCloseOperation(ex);
		d.setSize(w, h);
		d.setLocationRelativeTo(null);
		d.setResizable(false);
		d.setType(Type.UTILITY);
		d.getContentPane().setBackground(Login.back);
		d.setModal(true);
	}
	
	default <Any> Any get(JComponent comp, Set...sets) {
		
		comp.setBackground(Login.back);
		comp.setForeground(Login.fore);
		
		if (comp instanceof JButton) {
			comp.setBackground(new Color(0, 123, 255));
			comp.setForeground(Color.white);
		}
		
		for (Set set : sets) {
			set.set(comp);
		}
		
		return (Any) comp;
		
	}
	
	default Set set(boolean tf) {
		return c->c.setEnabled(tf);
	}
	
	default Set set(Border border) {
		return c->c.setBorder(border);
	}
	
	default Set set(int x, int y) {
		return c->c.setPreferredSize(new Dimension(x, y));
	}
	
	default Set set(int font) {
		return c->c.setFont(new Font("", 1, font));
	}
	
	default Set setf(Color color) {
		return c->c.setForeground(color);
	}
	
	default Set setb(Color color) {
		return c->c.setBackground(color);
	}
	
	default Integer intnum(String txt) {
		return Integer.parseInt(txt);
	}
	
	default void popup(JPopupMenu pop, JComponent comp, int row, int col) {
		
		pop.removeAll();
		JPanel in = new JPanel(new GridLayout(1, 2));
		JPanel pitem1 = new JPanel(new GridLayout(0, 1));
		
		JScrollPane scl1 = get(new JScrollPane(pitem1, 22, 31), set(90, 250));
		JScrollPane scl2 = get(new JScrollPane(pitem2, 22, 31), set(90, 250));
		
		pop.add(in);
		in.add(scl1);
		in.add(scl2);
		
		pitem1.removeAll();
		pitem2.removeAll();
		
		Query("SELECT * FROM busticketbooking.location;", location);
		for (int i = 0; i < location.size(); i++) {
			
			JButton btn = new JButton(location.get(i).get(1));
			
			int n = i;
			btn.addActionListener(e->{
				
				pitem2.removeAll();
				Query("select * from location2 where location_no = ?", location2, location.get(n).get(0));
				
				for (int j = 0; j < location2.size(); j++) {
					
					JButton btnn = new JButton(location2.get(j).get(1));
					int k = j;
					
					btnn.addActionListener(e2->{
						
						if (comp instanceof JTextField) {
							((JTextField)comp).setText(btn.getText() + " " + btnn.getText());
							comp.setName(location2.get(k).get(0));
						}else {
							((JTable)comp).setValueAt(btn.getText() + " " + btnn.getText(), row, col);
						}
						
					});
					
					pitem2.add(btnn);
					
				}
				
				pitem2.revalidate();
				pitem2.repaint();
				
			});
			
			pitem1.add(btn);
			
		}
		
	}
	
	default JTextField gettxt(JTextField txt, String title, Set...sets ) {
		
		txt = new JTextField() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				if (!this.getText().isBlank()) {
					return;
				}
				
				g.setColor(Color.gray);
				g.drawString(title, this.getInsets().left, this.getInsets().top + g.getFontMetrics().getMaxAscent() + 5);
				
			}
		};
		
		txt.setBackground(Login.back);
		txt.setForeground(Login.fore);
		txt.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
		
		for (Set set : sets) {
			set.set(txt);
		}
		
		return txt;
		
	}
	
	default JPasswordField getpass(JPasswordField txt, String title, Set...sets ) {
		
		txt = new JPasswordField() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				if (!this.getText().isBlank()) {
					return;
				}
				
				g.setColor(Color.gray);
				g.drawString(title, this.getInsets().left, this.getInsets().top + g.getFontMetrics().getMaxAscent() + 5);
				
			}
		};
		
		txt.setEchoChar('*');
		txt.setBackground(Login.back);
		txt.setForeground(Login.fore);
		txt.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
		
		for (Set set : sets) {
			set.set(txt);
		}
		
		return txt;
		
	}
	
	default void change(JComponent comp) {
		comp.setBackground(Login.back);
		comp.setForeground(Login.fore);
		
		Login.back = Login.back.equals(Color.white) ? Color.DARK_GRAY : Color.white;
		Login.fore = Login.fore.equals(Color.black) ? Color.white : Color.black;
		
	}
	
	default void tema(JComponent comp) {
		comp.setBackground(Login.back);
		comp.setForeground(Login.fore);
		for (Component c : comp.getComponents()) {
			if (c instanceof JButton == false) {
				c.setBackground(Login.back);
				c.setForeground(Login.fore);
			}
		}
	}
	
	default JLabel DBimg(String no, String title, int x, int y, Set...sets ) {
		
		JLabel comp = new JLabel(new ImageIcon(getimg(no, title).getImage().getScaledInstance(x, y, 4)));
		
		for (Set set : sets) {
			set.set(comp);
		}
		
		return comp;
		
	}
	
	default JLabel getimg(String path, int x, int y, Set...sets ) {
		
		JLabel comp = new JLabel(new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(x, y, 4)));
		
		for (Set set : sets) {
			set.set(comp);
		}
		
		return comp;
		
	}
	
	default void tblcenter(JTable tbl) {
		
		for (int i = 0; i < tbl.getRowCount(); i++) {
			for (int j = 0; j < tbl.getColumnCount(); j++) {
				
				TableCellRenderer cell = tbl.getCellRenderer(i, j);
				Component c = tbl.prepareRenderer(cell, i, j);
				
				((JLabel)c).setHorizontalAlignment(JLabel.CENTER);
				c.setBackground(Login.back);
				c.setForeground(Login.fore);
				
			}
		}
		
		tbl.setAutoCreateRowSorter(true);
		tbl.setSelectionMode(0);
		
		UIManager.getLookAndFeelDefaults().put("Table.ascendingSortIcon", stringicon("↑"));
		UIManager.getLookAndFeelDefaults().put("Table.descendingSortIcon", stringicon("↓"));
		
	}
	
	default Icon stringicon(String txt) {
		
		Icon icon = new Icon() {
			
			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				g.drawString(txt, x, y + 4);
			}
			
			@Override
			public int getIconWidth() {
				return 0;
			}
			
			@Override
			public int getIconHeight() {
				return 0;
			}
		};
		
		return icon;
		
	}
	
	default String file(String txt) {
		return "지급파일/images/" + txt;
	}
	
	default void Query(String sql, ArrayList<ArrayList<String>> list, String...v) {
		
		try {
			
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/busticketbooking?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
			PreparedStatement s = c.prepareStatement(sql);
			
			for (int i = 0; i < v.length; i++) {
				s.setString(i + 1, v[i]);
			}
			
			list.clear();
			ResultSet rs = s.executeQuery();
			ResultSetMetaData rsm = rs.getMetaData();
			
			while (rs.next()) {
				ArrayList row = new ArrayList();
				for (int i = 1; i <= rsm.getColumnCount(); i++) {
					row.add(rs.getString(i));
				}
				list.add(row);
			}
			
			s.close();
			c.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	default void Update(String sql, String...v) {
		
		try {
			
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/busticketbooking?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
			PreparedStatement s = c.prepareStatement(sql);
			
			for (int i = 0; i < v.length; i++) {
				s.setString(i + 1, v[i]);
			}
			
			s.executeUpdate();
			s.close();
			c.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	default ImageIcon getimg(String no, String title) {
		
		try {
			
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/busticketbooking?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
			PreparedStatement s = c.prepareStatement("select img from recommend_info where recommend_no = ? and title = ?");
			
			s.setString(1, no);
			s.setString(2, title);
			
			ResultSet rs = s.executeQuery();
			
			rs.next();
			
			var img = rs.getBinaryStream(1);
			
			return new ImageIcon(img.readAllBytes());
				
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return null;
		
	}
	
	default void Saveimg(String path, String no, String title) {
		
		try {
			
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost/busticketbooking?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
			PreparedStatement s = c.prepareStatement("update recommend_info set img = ? where recommend_no = ? and title = ?");
			
			s.setBytes(1, new FileInputStream(new File(path)).readAllBytes());
			s.setString(2, no);
			s.setString(3, title);
			
			s.executeUpdate();
			s.close();
			c.close();
			
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
}
