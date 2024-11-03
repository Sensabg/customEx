import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.OutputStream;
import java.io.PrintStream;

import org.xbill.DNS.TextParseException;

public class DNSGUI extends JFrame {
    private JTextArea outputArea;
    private JTextField domainField;
    private JComboBox<String> typeComboBox;

    public DNSGUI() {
        setTitle("DNS Lookup Tool");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        domainField = new JTextField(20);
        typeComboBox = new JComboBox<>(new String[]{"A", "MX", "NS", "TXT", "SOA", "CNAME", "ALL"});

        JButton lookupButton = new JButton("Lookup");

        lookupButton.addActionListener(e -> {
            try {
                performLookup();
            } catch (TextParseException ex) {
                outputArea.append("Error: Invalid domain format.\n");
            }
        });

        inputPanel.add(new JLabel("Domain:"));
        inputPanel.add(domainField);
        inputPanel.add(new JLabel("Type:"));
        inputPanel.add(typeComboBox);
        inputPanel.add(lookupButton);

        add(inputPanel, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        System.setOut(new PrintStream(new TextAreaOutputStream(outputArea)));

        inputPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "lookupAction");
        inputPanel.getActionMap().put("lookupAction", new AbstractAction() {
            @Override
            public boolean accept(Object sender) {
                return super.accept(sender);
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                lookupButton.doClick();
            }
        });
    }

    private void performLookup() throws TextParseException {
        String domain = domainField.getText().trim();
        String type = (String) typeComboBox.getSelectedItem();

        if (domain.isEmpty() || type == null) {
            JOptionPane.showMessageDialog(this, "Please enter a domain and select a type.");
            return;
        }

        outputArea.setText("");
        // Run the DNS lookup in a separate thread to prevent freezing
        new Thread(() -> {
            try {
                new DNS(domain, type, outputArea);
            } catch (TextParseException e) {
                outputArea.append("Error: Invalid domain format.\n");
            } catch (Exception e) {
                outputArea.append("An error occurred during DNS lookup: " + e.getMessage() + "\n");
            }
        }).start();
    }

    static class TextAreaOutputStream extends OutputStream {
        private final JTextArea textArea;

        public TextAreaOutputStream(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) {
            textArea.append(String.valueOf((char) b));
        }

        @Override
        public void write(byte[] b, int off, int len) {
            textArea.append(new String(b, off, len));
        }
    }
}
