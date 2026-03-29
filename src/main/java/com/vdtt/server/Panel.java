package com.vdtt.server;

import com.vdtt.util.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Panel extends JPanel implements ActionListener {
    private final JButton baotri;

    public Panel() {
        setLayout(new GridBagLayout()); // Sử dụng GridBagLayout

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(5, 5, 5, 5);

        baotri = createButton("Bảo Trì Máy Chủ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(baotri, gbc);

        //show console panel
//        consolePanel = new ConsolePanel();
//        gbc.gridx = 0;
//        gbc.gridy = 3;
//        gbc.gridwidth = 3; // Đặt chiều rộng để nó chiếm cả 3 cột
//        add(consolePanel, gbc);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(this);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(52, 152, 219));
        button.setFocusPainted(false);
        //button.setBorder(new RoundRectangleBorder(new Color(52, 152, 219), 2, 20));
        return button;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == baotri) {
            Server.maintenance((byte) 1);
            sendTB("Tiến Hành Bảo Trì !\n");
        }
    }

    private void sendTB(String tb) {
        JOptionPane.showMessageDialog(null, tb, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        Log.error(tb);
    }
}