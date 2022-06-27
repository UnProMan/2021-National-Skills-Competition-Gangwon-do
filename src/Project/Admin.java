package Project;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Base.Base;

public class Admin extends JFrame implements Base{
	
	JTabbedPane tp = get(new JTabbedPane());
	int index = 0;
	
	public static int combo = 0;
	
	public Admin() {
		
		SetFrame(this, "관리자", DISPOSE_ON_CLOSE, 900, 600);
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
		
		add(tp);
		tp.setTabPlacement(2);
		chang();
		
	}
	
	public void chang() {
		
		tp.removeAll();
		
		tp.add("사용자 관리", new UserManager());
		tp.add("추천 여행지 관리", new Recommend());
		tp.add("일정 관리", new Schedule());
		tp.add("예매 관리", new ReserveManager());
		tp.add("테마", null);
		tp.add("로그아웃", null);
		
		if (Login.back.equals(Color.white)) {
			tp.setBackgroundAt(4, Color.DARK_GRAY);
			tp.setForegroundAt(4, Color.white);
		}else {
			tp.setBackgroundAt(4, Color.white);
			tp.setForegroundAt(4, Color.black);
		}
		
		for (int i = 0; i < 6; i++) {
			if (i != 4) {
				tp.setBackgroundAt(i, Login.back);
				tp.setForegroundAt(i, Login.fore);
			}
		}
		
		tp.setSelectedIndex(index);
		
	}
	
	@Override
	public void action() {
		
		tp.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				
				if (tp.getSelectedIndex() == 4) {
					
					change(tp);
					Admin.this.getContentPane().setBackground(Login.back);
					chang();
					
				}else if (tp.getSelectedIndex() ==5) {
					dispose();
					new Login();
				}
				
			}
		});
	
		tp.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				index = tp.getSelectedIndex();
			}
		});
		
	}

}
