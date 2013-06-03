package net.frcdb.scoring.gui.comp.table;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import net.frcdb.scoring.match.MatchDatabase;

/**
 *
 * @author tim
 */
public class MatchesTable extends JPanel {

	private JTable table;

	public MatchesTable() {
		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		table = new JTable();
		table.setFont(Font.decode("Arial 18"));
		JScrollPane scroller = new JScrollPane(table);

		add(scroller, BorderLayout.CENTER);

		JButton reload = new JButton("Refresh");
		reload.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				MatchDatabase.get().load();
				update();
			}
		});
		add(reload, BorderLayout.SOUTH);

		update();
	}

	public void update() {
		table.setModel(new MatchesModel());
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Matches Results");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.add(new MatchesTable());

		frame.pack();
		frame.setSize(400, 300);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
