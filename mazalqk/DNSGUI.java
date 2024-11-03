import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.OutputStream;
import java.io.PrintStream;

import org.xbill.DNS.TextParseException;

public class DNSGUI extends JFrame {
    private JTextPane outputArea; // Changed to JTextPane
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
                appendColoredText("Error: Invalid domain format.\n", Color.RED);
            }
        });

        inputPanel.add(new JLabel("Domain:"));
        inputPanel.add(domainField);
        inputPanel.add(new JLabel("Type:"));
        inputPanel.add(typeComboBox);
        inputPanel.add(lookupButton);

        add(inputPanel, BorderLayout.NORTH);

        outputArea = new JTextPane(); // Initialize as JTextPane
        outputArea.setEditable(false);
        outputArea.setBackground(Color.BLACK); // Set background color to black
        outputArea.setForeground(Color.WHITE); // Set default text color to white

        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        System.setOut(new PrintStream(new TextAreaOutputStream(outputArea)));

        // Key binding for the Enter key
        inputPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "lookupAction");
        inputPanel.getActionMap().put("lookupAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lookupButton.doClick(); // Simulate button click
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

        outputArea.setText(""); // Clear output area

        // Run the DNS lookup in a separate thread to prevent GUI freezing
        new Thread(() -> {
            try {
                new DNS(domain, type, outputArea);
            } catch (TextParseException e) {
                appendColoredText("Error: Invalid domain format.\n", Color.RED);
            } catch (Exception e) {
                appendColoredText("An error occurred during DNS lookup: " + e.getMessage() + "\n", Color.RED);
            }
        }).start();
    }

    private void appendColoredText(String text, Color color) {
        StyledDocument doc = outputArea.getStyledDocument();
        Style style = outputArea.addStyle("Style", null);
        StyleConstants.setForeground(style, color);
        try {
            doc.insertString(doc.getLength(), text, style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void appendEqualsLine(String text) {
        appendColoredText(text, Color.GREEN); // Change the color of the equals line here
    }

    static class TextAreaOutputStream extends OutputStream {
        private final JTextPane textArea;

        public TextAreaOutputStream(JTextPane textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) {
            // Convert the byte to char and insert it into JTextPane
            SwingUtilities.invokeLater(() -> {
                try {
                    StyledDocument doc = textArea.getStyledDocument();
                    Style style = textArea.addStyle("Style", null);
                    StyleConstants.setForeground(style, Color.WHITE); // Set desired color
                    doc.insertString(doc.getLength(), String.valueOf((char) b), style);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void write(byte[] b, int off, int len) {
            // Convert the byte array to String and insert it into JTextPane
            String text = new String(b, off, len);
            SwingUtilities.invokeLater(() -> {
                try {
                    StyledDocument doc = textArea.getStyledDocument();
                    Style style = textArea.addStyle("Style", null);
                    StyleConstants.setForeground(style, Color.WHITE); // Set desired color
                    doc.insertString(doc.getLength(), text, style);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DNSGUI().setVisible(true));
    }
}
