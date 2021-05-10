package JPokerGame.Dialog;

import Common.JPokerInterface;
import Common.JPokerUserTransferObject;
import JPokerGame.JPokerClient;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class RegisterDialog extends JDialog implements ActionListener {
    private final JLabel loginLabel = new JLabel("Login Name");
    private final JLabel passwordLabel = new JLabel("Password");
    private final JLabel confirmPasswordLabel = new JLabel("Confirm Password");

    private final JTextField loginField = new JTextField(30);
    private final JTextField passwordField = new JTextField(30);
    private final JTextField confirmPasswordField = new JTextField(30);

    private final JButton registerButton = new JButton("Register");
    private final JButton cancelButton = new JButton("Cancel");

    private final TitledBorder loginPanelBorder = new TitledBorder(new LineBorder(Color.GREEN), "Register");
    private final EmptyBorder emptyBorder = new EmptyBorder(20, 20, 20, 20);

    private final JPokerInterface gameProvider;

    public RegisterDialog(final JFrame parent, boolean modal, JPokerInterface gameProvider) {
        super(parent, modal);

        this.gameProvider = gameProvider;

        setTitle("Register");
        setBackground(new Color(220, 220, 220));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(getMainPanel());
        pack();
    }

    private JComponent getLoginPanel() {
        cancelButton.addActionListener(this);
        registerButton.addActionListener(this);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.LINE_AXIS));
        loginPanel.add(registerButton);
        loginPanel.add(Box.createHorizontalGlue());
        loginPanel.add(cancelButton);
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
        mainPanel.add(wrapLabelField(confirmPasswordLabel, confirmPasswordField));
        mainPanel.add(getLoginPanel());

        mainPanel.setPreferredSize(new Dimension(400, 400));

        return mainPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            if (loginField.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Login name should not be empty", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else if (!passwordField.getText().equals(confirmPasswordField.getText())) {
                JOptionPane.showMessageDialog(null, "Passwords do not match", "Information",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                RegisterWorker worker = new RegisterWorker(this);
                worker.execute();
            }
        }

        if (e.getSource() == cancelButton) {
            loginField.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");
            setVisible(false);
        }
    }

    private class RegisterWorker extends SwingWorker<Boolean, Void> {
        private final JDialog dialog;
        private Throwable exceptionCause = null;

        public RegisterWorker(JDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        protected Boolean doInBackground() {
            try {
                return gameProvider.register(loginField.getText(), passwordField.getText());

            } catch (Exception e1) {
                e1.printStackTrace();
                exceptionCause = e1.getCause();
            }
            return false;
        }

        @Override
        protected void done() {
            if (exceptionCause != null)
                JOptionPane.showMessageDialog(null, exceptionCause, "Information",
                        JOptionPane.INFORMATION_MESSAGE);
            else
                dialog.setVisible(false);
        }
    }

}
