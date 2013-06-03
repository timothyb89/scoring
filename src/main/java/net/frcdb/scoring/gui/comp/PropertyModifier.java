package net.frcdb.scoring.gui.comp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.frcdb.scoring.game.Property;
import net.frcdb.scoring.game.PropertyValue;
import net.frcdb.scoring.match.Match;
import net.frcdb.scoring.match.MatchAdapter;
import net.frcdb.scoring.match.MatchAlliance;
import net.frcdb.scoring.match.MatchManager;

/**
 *
 * @author tim
 */
public class PropertyModifier extends JPanel {

	private MatchAlliance alliance;
	private Property property;
	private PropertyValue value;

	private JLabel text;
	private JSpinner editor;

	public PropertyModifier(Property property) {
		this.property = property;

		MatchManager.get().addListener(matchListener);
		
		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		text = new JLabel(property.getName() + ": ");
		text.setHorizontalAlignment(SwingConstants.RIGHT);
		text.setPreferredSize(new Dimension(100, 25));
		add(text, BorderLayout.WEST);

		//add(new JPanel(), BorderLayout.CENTER);

		value = new PropertyValue(property.getName(), property.getConstraints().getMinimum());

		SpinnerNumberModel model = new SpinnerNumberModel(
				property.getConstraints().getMinimum(), property.getConstraints().getMinimum(),
				property.getConstraints().getMaximum(), 1);
		editor = new JSpinner(model);
		editor.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				value.setValue((Integer) editor.getValue());
				
				MatchManager.get().notifyPropertyChanged(alliance, value);
			}

		});
		add(editor, BorderLayout.CENTER);
	}

	public MatchAlliance getAlliance() {
		return alliance;
	}

	public void setAlliance(MatchAlliance alliance) {
		this.alliance = alliance;
	}
	
	private MatchAdapter matchListener = new MatchAdapter() {

		@Override
		public void onMatchPropertyModified(
				Match match, MatchAlliance a, PropertyValue p) {
			if (alliance == a && property.getName().equals(p.getName())) {
				value.setValue(p.getValue());
				editor.setValue(p.getValue());
			}
		}
		
	};

}
