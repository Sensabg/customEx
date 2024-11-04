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
