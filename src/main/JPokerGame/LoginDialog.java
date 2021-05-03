package JPokerGame;

import Common.JPokerInterface;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginDialog extends JDialog implements ActionListener {

	private final JLabel loginLabel = new JLabel("Login Name");
	private final JLabel passwordLabel = new JLabel("Password");

	private final JTextField loginField = new JTextField(30);
	private final JPasswordField passwordField = new JPasswordField();

	private final JButton loginButton = new JButton("Login");
	private final JButton registerButton = new JButton("Register");

	private final TitledBorder loginPanelBorder = new TitledBorder(new LineBorder(Color.GREEN), "Login");
	private final EmptyBorder emptyBorder = new EmptyBorder(20, 20, 20, 20);

	private final RegisterDialog registerDialog;

	private final JPokerInterface gameProvider;

	public LoginDialog(final JFrame parent, boolean modal, RegisterDialog registerDialog,
			JPokerInterface gameProvider) {
		super(parent, modal);

		this.registerDialog = registerDialog;
		this.gameProvider = gameProvider;

		setTitle("Login");
		setBackground(new Color(220, 220, 220));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		add(getMainPanel());
		pack();
	}

	private JComponent getLoginPanel() {
		loginButton.addActionListener(this);
		registerButton.addActionListener(this);

		JPanel loginPanel = new JPanel();
		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.LINE_AXIS));
		loginPanel.add(loginButton);
		loginPanel.add(Box.createHorizontalGlue());
		loginPanel.add(registerButton);
		loginPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		return loginPanel;
	}

	private JComponent wrapLabelField(JLabel label, JTextField field) {
		JPanel loginWrapper = new JPanel(new GridLayout(0, 1));
		loginWrapper.add(label);
		loginWrapper.add(field);

		return loginWrapper;
	}

	private JComponent getMainPanel() {
		JPanel mainPanel = new JPanel(new GridLayout(0, 1));

		mainPanel.setBorder(new CompoundBorder(new CompoundBorder(emptyBorder, loginPanelBorder), emptyBorder));
		mainPanel.add(wrapLabelField(loginLabel, loginField));
		mainPanel.add(wrapLabelField(passwordLabel, passwordField));
		mainPanel.add(getLoginPanel());

		mainPanel.setPreferredSize(new Dimension(400, 300));

		return mainPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginButton) {
			if (loginField.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "Login name should not be empty", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				new Thread(() -> {
					try {
						// Using deprecated method for simplicity
						if (gameProvider.login(loginField.getText(), passwordField.getText())) {
							setVisible(false);
							((JPokerClient) getOwner()).setUsername(loginField.getText());
							getParent().setVisible(true);
						} else {
							JOptionPane.showMessageDialog(null, "Incorrect Password", "Information",
									JOptionPane.INFORMATION_MESSAGE);
						}
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, e1.getCause(), "Information",
								JOptionPane.INFORMATION_MESSAGE);
						e1.printStackTrace();
					}
				}).start();
			}
		}
		if (e.getSource() == registerButton) {
			registerDialog.setVisible(true);
			registerDialog.requestFocus();
		}
	}

}
