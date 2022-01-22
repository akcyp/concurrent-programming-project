package com.cpproject.ui;
import javax.swing.*;
import java.util.function.Function;

public class InputPanel {
    JFrame f = new JFrame();
    InputPanel(String name, Integer defaultValue, Function<Integer, String> callback){
        String value = JOptionPane.showInputDialog(this.f,"Enter " + name, defaultValue.toString());
        if (value != null) {
            if (value.matches("^[0-9]+$")) {
                String err = callback.apply(Integer.parseInt(value));
                if (err != null) {
                    JOptionPane.showMessageDialog(null, err);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid value");
            }
        }
    }
}
