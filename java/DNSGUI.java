import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.OutputStream;
import java.io.PrintStream;
import org.xbill.DNS.TextParseException;

public class DNSGUI extends JFrame {
    private JTextPane outputArea;
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

        outputArea = new JTextPane();
        outputArea.setEditable(false);
        outputArea.setBackground(Color.BLACK);
        outputArea.setForeground(Color.WHITE);

        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        System.setOut(new PrintStream(new TextAreaOutputStream(outputArea)));

        inputPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "lookupAction");
        inputPanel.getActionMap().put("lookupAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lookupButton.doClick();
            }
        });
    }

    private void performLookup() throws TextParseException {
        String domain = domainField.getText().trim();
        domain = cleanDomain(domain);
        String type = (String) typeComboBox.getSelectedItem();

        if (domain.isEmpty() || type == null) {
            JOptionPane.showMessageDialog(this, "Please enter a valid domain");
            return;
        }

        outputArea.setText("");
        String finalDomain = domain;
        new Thread(() -> {
            try {
                new DNS(finalDomain, type, outputArea);
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
            throw new RuntimeException(e);
        }
    }

    static class TextAreaOutputStream extends OutputStream {
        private final JTextPane textArea;

        public TextAreaOutputStream(JTextPane textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) {
            SwingUtilities.invokeLater(() -> {
                try {
                    StyledDocument doc = textArea.getStyledDocument();
                    Style style = textArea.addStyle("Style", null);
                    StyleConstants.setForeground(style, Color.WHITE);
                    doc.insertString(doc.getLength(), String.valueOf((char) b), style);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void write(byte[] b, int off, int len) {
            String text = new String(b, off, len);
            SwingUtilities.invokeLater(() -> {
                try {
                    StyledDocument doc = textArea.getStyledDocument();
                    Style style = textArea.addStyle("Style", null);
                    StyleConstants.setForeground(style, Color.WHITE);
                    doc.insertString(doc.getLength(), text, style);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private String cleanDomain(String domain) {
        final String HTTP = "http://";
        final String HTTPS = "https://";

        if (domain.startsWith(HTTPS)) {
            return domain.substring(HTTPS.length()).trim().toLowerCase();
        } else if (domain.startsWith(HTTP)) {
            return domain.substring(HTTP.length()).trim().toLowerCase();
        }
        return domain.trim().toLowerCase();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DNSGUI gui = new DNSGUI();
            gui.setVisible(true);
        });
    }
}
