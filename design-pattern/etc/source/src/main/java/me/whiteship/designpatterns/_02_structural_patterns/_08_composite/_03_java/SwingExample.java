package me.whiteship.designpatterns._02_structural_patterns._08_composite._03_java;

import javax.swing.*;

public class SwingExample {

    public static void main(String[] args) {
        // JFrame, JTextField, JButton 모두 Component를 추상 클래스로 가지고 있다.
        JFrame frame = new JFrame();

        JTextField textField = new JTextField();
        textField.setBounds(200, 200, 200, 40);
        frame.add(textField);

        JButton button = new JButton("click");
        button.setBounds(200, 100, 60, 40);
        button.addActionListener(e -> textField.setText("Hello Swing"));
        frame.add(button);

        frame.setSize(600, 400);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}
