package Joption;

import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import Base.Base;

public class Jop extends JDialog implements Base{
	
	JPanel p1= get(new JPanel());
	JButton btn1 = get(new JButton("확인"));
	JLabel lab;
	Icon icon = UIManager.getIcon("OptionPane.informationIcon");
	
	String txt;
	
	public Jop(String txt) {
		
		this.txt = txt;
		
		SetDial(this, "안내", DISPOSE_ON_CLOSE, 350, 150);
		design();
		action();
		setVisible(true);
		
	}

	@Override
	public void design() {
		
		add(lab = get(new JLabel(txt, icon, JLabel.CENTER)));
		add(p1, "South");
		
		p1.add(btn1);
		btn1.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		
	}

	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			dispose();
		});
		
	}

}
