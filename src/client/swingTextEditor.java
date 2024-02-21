package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.CountDownLatch;


public class swingTextEditor extends JFrame {
    private JTextArea textArea;
    private JButton saveButton;
    private String fileText;
    private CountDownLatch latch = new CountDownLatch(1);

    public swingTextEditor() {
        super("Text Editor");
        initializeComponents();
        readFileIntoTextArea();
        setupLayout();
        addListeners();
    }

    private void initializeComponents() {
        textArea = new JTextArea(20, 50);
        saveButton = new JButton("Save");
    }

    private void readFileIntoTextArea() {
            textArea.setText(fileText);
    }

    private void setupLayout() {
        JScrollPane scrollPane = new JScrollPane(textArea);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);

        Container contentPane = getContentPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack(); // Adjusts the window to fit its contents
        setLocationRelativeTo(null); // Centers the window on the screen
    }

    private void addListeners() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("hej hej3");
                fileText = textArea.getText();
                dispose();
                latch.countDown();
                System.out.println(latch.getCount());
            }
        });
    }

    public String openTextEditor(String fileText) throws InterruptedException {
        this.fileText = fileText;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(true);
            }
        });
        latch.await();
        return fileText;
    }
}