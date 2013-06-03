package net.frcdb.scoring.gui.picker;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;
import net.frcdb.scoring.game.Alliance;
import net.frcdb.scoring.team.Team;
import net.frcdb.scoring.team.TeamDatabase;

/**
 *
 * @author tim
 */
public class TeamChooser extends JPanel {

	private TeamPicker picker;
	private Alliance alliance;
	private List<Team> pool;

	private TeamListModel selectedModel;
	private JList selected;
	private JTextField selector;

	public TeamChooser(TeamPicker picker, Alliance alliance, List<Team> pool) {
		this.picker = picker;
		this.alliance = alliance;
		this.pool = pool;

		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(alliance.getName()));

		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setBackground(alliance.getColor());
		wrapper.setBorder(BorderFactory.createEtchedBorder());
		selectedModel = new TeamListModel();
		selected = new JList(selectedModel);
		wrapper.add(selected, BorderLayout.CENTER);
		add(wrapper, BorderLayout.CENTER);

		selector = new JTextField();
		selector.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (selector.getText().isEmpty()) {
						picker.finish();
						return;
					}

					Team team = TeamDatabase.get().getTeam(selector.getText());
					pool.remove(team);
					selectedModel.add(team);
					selector.setText("");

					picker.firePoolChanged();
				}
			}

		});

		add(selector, BorderLayout.SOUTH);
	}

	public void focus() {
		
	}

	public void updatePool() {
		AutoCompleteDecorator.decorate(selector, pool, true, teamConverter);
	}

	public Alliance getAlliance() {
		return alliance;
	}

	public List<Team> getSelectedTeams() {
		return selectedModel.getTeams();
	}

	private ObjectToStringConverter teamConverter = new ObjectToStringConverter() {

		@Override
		public String[] getPossibleStringsForItem(Object item) {
			if (item == null) {
				return null;
			}

			Team team = (Team) item;
			return new String[] {team.getName(), String.valueOf(team.getNumber())};
		}

		@Override
		public String getPreferredStringForItem(Object item) {
			if (item == null) {
				return null;
			}

			Team team = (Team) item;

			return team.getName();
		}

	};

}
