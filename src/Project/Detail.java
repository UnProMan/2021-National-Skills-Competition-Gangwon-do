package Project;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import Base.Base;

public class Detail extends JDialog implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout(0, 10)), set(new EmptyBorder(10, 10, 10, 10)));
	JPanel p2 = get(new JPanel(new GridLayout(0, 1, 0, 5)), set(new EmptyBorder(5, 5, 5, 5)));
	JScrollPane scl = new JScrollPane(p2, 22, 31);
	
	JLabel lab1= get(new JLabel("상세설명"), set(25));
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	String no;
	
	public Detail(String no) {
		
		this.no = no;
		
		SetDial(this, "상세설명", DISPOSE_ON_CLOSE, 400, 500);
		design();
		action();
		setVisible(true);
		
	}

	@Override
	public void design() {
		
		add(p1);
		p1.add(lab1, "North");
		p1.add(scl);
		
		Query("select * from recommend_info where recommend_no = ? order by title", list, no);
		
		for (int i = 0; i < list.size(); i++) {
			
			JLabel img= DBimg(list.get(i).get(0), list.get(i).get(1), 330, 150);
			p2.add(img);
			
			if (!list.get(i).get(2).contentEquals("")) {
				
				JTextArea txt = get(new JTextArea());
				JScrollPane scl = get(new JScrollPane(txt, 20 ,31), set(200, 150));
				txt.setText(list.get(i).get(2));
				txt.setLineWrap(true);
				
				p2.add(scl);
				
			}
			
		}
		
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}

}
