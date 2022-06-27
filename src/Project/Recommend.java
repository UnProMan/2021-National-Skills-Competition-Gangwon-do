package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import Base.Base;
import Joption.Err;

public class Recommend extends JPanel implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout()));
	JPanel p2 = get(new JPanel(new BorderLayout()), set(0, 300));
	JPanel p3 = get(new JPanel(new GridLayout(1, 0)), set(new EmptyBorder(0, 0, 50, 0)));
	JPanel p4 = get(new JPanel(new FlowLayout(0)));
	JPanel p5 = get(new JPanel(new FlowLayout(0)));
	
	JPanel in = new JPanel(new GridLayout(0, 1));
	JScrollPane scl = get(new JScrollPane(in,22 ,31), set(90, 250));
	
	JPopupMenu pop1 = new JPopupMenu();
	JPopupMenu pop2 = new JPopupMenu();
	JPopupMenu pop3 = new JPopupMenu();
	
	JMenuItem item1 = new JMenuItem("이미지 설정");
	JMenuItem item2 = new JMenuItem("설명설정");
	JMenuItem item3 = new JMenuItem("삭제");
	JMenuItem item4 = new JMenuItem("설명 텍스트 입력");
	
	ArrayList<ArrayList<String>> imgs = new ArrayList<>();
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<ArrayList<String>> temp = new ArrayList<>();
	
	JLabel lab1 = get(new JLabel("추천 여행지 관리"), set(25));
	JLabel lab2 = get(new JLabel("설명 설정"), set(25));
	
	JButton btn1 = get(new JButton("추가"));
	
	int imgindex = 1;
	int select = 1;
	String title = "";
	
	public Recommend() {
		
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setBackground(Login.back);
		
		design();
		action();
		
	}

	@Override
	public void design() {
		
		pop1.add(scl);
		pop2.add(item1);
		pop2.add(item2);
		pop3.add(item3);
		pop3.add(item4);
		
		add(p1);
		add(p2, "South");
		
		p1.add(lab1, "North");
		p1.add(p3);
		
		p2.add(p4, "North");
		p2.add(p5);
		
		p4.add(lab2);
		p4.add(btn1);
		
		cu();
		lo();
		category();
		
	}
	
	public void cu() {
		
		p3.removeAll();
		
		Query("select * from recommend_info ri, recommend r, location l where ri.recommend_no = r.no and r.location_no = l.no group by recommend_no order by recommend_no, title", imgs);
		
		JPanel p;
		for (int i = 0; i < imgs.size(); i++) {
			
			p3.add(p = get(new JPanel(new BorderLayout()), set(new TitledBorder(new LineBorder(Login.fore), imgs.get(i).get(7), 0, 2, new Font("", 1, 12), Login.fore))));
			JLabel lab = DBimg(imgs.get(i).get(0), imgs.get(i).get(1), 150, 150);
			p.add(lab);
			
			int j = i;
			p.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					
					if (e.getClickCount() == 2) {
						pop1.show(lab, e.getX(), e.getY());
						imgindex = j + 1;
					}
					
					if (e.getButton() ==3) {
						pop2.show(lab, e.getX(), e.getY());
						imgindex = j + 1;
					}
					
				}
			});
			
		}
		
		revalidate();
		repaint();
		
	}
	
	public void lo() {
		
		Query("SELECT * FROM busticketbooking.location;", list);
		
		for (int i = 0; i < list.size(); i++) {
			
			JButton btn = new JButton(list.get(i).get(1));
			int j = i;
			
			btn.addActionListener(e->{
				Update("update recommend set location_no = ? where no = ?", (j + 1) + "", imgindex + "" );
				cu();
			});
			
			in.add(btn);
			
		}
		
	}
	
	public void category() {
		
		p5.removeAll();
		
		Query("select * from recommend_info where recommend_no = ?", temp, select + "");
		
		for (int i = 0; i < temp.size(); i++) {
			
			JLabel image = DBimg(temp.get(i).get(0), temp.get(i).get(1), 130, 130, set(new LineBorder(Login.fore)));
			
			int j = i;
			
			image.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					
					if (e.getButton() == 3) {
						pop3.show(image, e.getX(), e.getY());
						title = temp.get(j).get(1);
					}
					
				}
			});
			p5.add(image);
			
		}
		
		revalidate();
		repaint();
		
	}
	
	public void action() {

		item1.addActionListener(e->{
			
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & png images", "jpg,png".split(","));
			fc.setFileFilter(filter);
			fc.setMultiSelectionEnabled(false);
			
			int a=  fc.showOpenDialog(Recommend.this);
			
			if (a == 0) {
				
				File file = fc.getSelectedFile();
				String st1 = imgs.get(imgindex -1).get(7).contentEquals("1") ? "busan" : imgs.get(imgindex -1).get(7).contentEquals("2") ? "gangwondo" : imgs.get(imgindex -1).get(7).contentEquals("3") ? "gyeongju" : imgs.get(imgindex -1).get(7).contentEquals("4") ? "seoul" : "jeollanam-do";
				String path = file("recommend/" + st1 + "/");	
				
				Saveimg(file.toString(), imgindex + "", imgs.get(imgindex-1).get(1));
				try {
					ImageIO.write(ImageIO.read(file), "jpg", new File(path + imgs.get(imgindex-1).get(1) + ".jpg"));
				} catch (Exception e2) {
				}
				
				cu();
				category();
				
			}
			
		});
		
		item2.addActionListener(e->{
			select = imgindex;
			category();
		});
		
		item3.addActionListener(e->{
			
			Update("delete from recommend_info where recommend_no = ? and title = ?", select + "", title);
			
			try {
				
				String st1 = select == 1 ? "busan" : select == 2 ? "gangwondo" : select == 3 ? "gyeongju" : select == 4 ? "seoul" : "jeollanam-do";
				String path = file("recommend/" + st1 + "/");
				
				new File(path + title + ".jpg").delete();
				
			} catch (Exception e2) {
			}
			
			cu();
			category();
		});
		
		item4.addActionListener(e->{
			
			String a = JOptionPane.showInputDialog(null, "설명 텍스트를 입력해주세요.", "입력", JOptionPane.QUESTION_MESSAGE);
			
			if (a != null) {
				Update("update recommend_info set descrption = ? where recommend_no = ? and title = ?", a, select + "", title);
			}
			
		});
		
		btn1.addActionListener(e->{
			
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & png images", "jpg,png".split(","));
			fc.setFileFilter(filter);
			fc.setMultiSelectionEnabled(false);
			
			int a=  fc.showOpenDialog(Recommend.this);
			
			if (a == 0) {
				
				File file = fc.getSelectedFile();
				String s[] = file.toString().split("\\\\");
				
				String name = s[s.length-1].replace(".jpg", "").replace(".png", "");
				
				Query("select * from recommend_info wheretitle = ?", list, name);
				
				if (!list.isEmpty()) {
					new Err("중복된 제목입니다.");
				}else {
					Update("insert into recommend_info values(?,?,?, 'd')", select + "", name, "");
					Saveimg(file.toString(), select + "", name);
					category();
				}
				
			}
			
		});
		
	}

}
