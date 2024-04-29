import javax.swing.*;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class SmartTextEditor extends JFrame implements ActionListener {
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private JLabel statusLabel;
    private Highlighter highlighter;
    private HighlightPainter painter;

    public SmartTextEditor() {
        setTitle("Smart Text Editor");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
        
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(this);
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(this);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);
        
        JMenu editMenu = new JMenu("Edit");
        JMenuItem wordCountMenuItem = new JMenuItem("Word Count");
        wordCountMenuItem.addActionListener(this);
        editMenu.add(wordCountMenuItem);
        menuBar.add(editMenu);
        
        setJMenuBar(menuBar);
        
        statusLabel = new JLabel(" ");
        add(statusLabel, BorderLayout.SOUTH);
        
        highlighter = textArea.getHighlighter();
        painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Open")) {
            openFile();
        } else if (command.equals("Save")) {
            saveFile();
        } else if (command.equals("Word Count")) {
            countWords();
        }
    }

    private void openFile() {
        fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                textArea.read(reader, null);
                statusLabel.setText("File opened: " + selectedFile.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveFile() {
        fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))) {
                textArea.write(writer);
                statusLabel.setText("File saved: " + selectedFile.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void countWords() {
        String text = textArea.getText();
        String[] words = text.split("\\s+");
        int wordCount = words.length;
        statusLabel.setText("Word count: " + wordCount);
        
        // Highlight occurrences of a specific word, e.g., "smart"
        String wordToHighlight = "smart";
        int index = text.indexOf(wordToHighlight);
        while (index >= 0) {
            try {
                highlighter.addHighlight(index, index + wordToHighlight.length(), painter);
                index = text.indexOf(wordToHighlight, index + wordToHighlight.length());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SmartTextEditor editor = new SmartTextEditor();
            editor.setVisible(true);
        });
    }
}
