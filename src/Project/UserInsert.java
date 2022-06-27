package Project;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Base.Base;
import Joption.Err;
import Joption.Jop;

public class UserInsert extends JDialog implements Base{
	
	JPanel p1 = get(new JPanel(new GridLayout(0, 1, 0, 20)), set(new EmptyBorder(70, 40, 70, 40)));
	
	JLabel lab1 = get(new JLabel("계정 정보"), set(25));
	
	JTextField txt1 = gettxt(new JTextField(), "Name");
	JTextField txt2 = gettxt(new JTextField(), "Id");
	JPasswordField txt3 = getpass(new JPasswordField(), "Password");
	JPasswordField txt4 = getpass(new JPasswordField(), "Password");
	JTextField txt5 = gettxt(new JTextField(), "E-mail");
	
	JButton btn1 = get(new JButton("회원가입"));
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	public UserInsert() {
		
		SetDial(this, "계정 등록하기", DISPOSE_ON_CLOSE, 450, 500);
		design();
		action();
		setVisible(true);
		
	}

	@Override
	public void design() {
		
		add(p1);
		
		p1.add(lab1);
		p1.add(txt1);
		p1.add(txt2);
		p1.add(txt3);
		p1.add(txt4);
		p1.add(txt5);
		p1.add(btn1);
		
	}

	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			
			for (Component comp : p1.getComponents()) {
				if (comp instanceof JTextField && ((JTextField) comp).getText().isBlank()) {
					new Err("공란을 확인해주세요.");
					return;
				}
				if (comp instanceof JPasswordField && ((JPasswordField) comp).getText().isBlank()) {
					new Err("공란을 확인해주세요.");
					return;
				}
			}
			
			if (!txt3.getText().contentEquals(txt4.getText())) {
				new Err("PW확인이 일치하지 않습니다.");
			}else if (Pattern.matches(".*[~!@#[$]%^&[*]\\(\\)_[+]<>?\\\\[|]].*", txt3.getText()) == false) {
				new Err("특수문자를 포함해주세요.");
			}else {
				
				Query("select * from user where id = ?", list, txt2.getText());
				
				if (!list.isEmpty()) {
					new Err("Id가 중복되었습니다.");
					return;
				}
				
				Query("select * from user where email = ?", list, txt5.getText());
				
				if (!list.isEmpty()) {
					new Err("E-mail이 중복되었습니다.");
					return;
				}
				
				new Jop("회원가입이 완료되었습니다.");
				
				Update("insert into user values(null, ?,?,?,?,?)", txt2.getText(), txt3.getText(), txt1.getText(), txt5.getText(), "1000");
				
				dispose();
				
			}
			
		});
		
	}

}
