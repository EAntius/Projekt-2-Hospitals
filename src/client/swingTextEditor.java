package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.CountDownLatch;


public class swingTextEditor extends JFrame {
    private JTextArea textArea;
    private JButton saveButton;
    private CountDownLatch latch = new CountDownLatch(1);

    public swingTextEditor() {
        super("Text Editor");
        initializeComponents();
        setupLayout();
        addListeners();
    }

    private void initializeComponents() {
        textArea = new JTextArea(20, 50);
        saveButton = new JButton("Save");
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
                dispose();
                latch.countDown();
            }
        });
    }

    public String openTextEditor(String fileText) throws InterruptedException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                textArea.setText(fileText);
                setVisible(true);
            }
        });
        latch.await();
        return textArea.getText();
    }
}