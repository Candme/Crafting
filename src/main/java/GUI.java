import java.util.*;

import java.io.*;
import java.nio.file.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

import Color.*;

import ItemSystem.*;
import ItemSystem.ItemRegistry;

import ItemSystem.ItemTypes.*;
import ItemSystem.GatheringAreas.*;

public class GUI
{
    JTextArea statsCreditsText;
    JTextArea statsInventory;
    JTextField mainInputText;
    JTextArea mainOutput;
    
    public GUI()
    {
        Border buttonBorder = BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(0), BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JFrame window = new JFrame("The thing");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        window.setSize(500, 360);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBackground(new Color(150, 150, 180));

        Color bgColor = window.getBackground();

        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        main.setBackground(bgColor);
        //text input & button on bottom, text output above.
        mainOutput = new JTextArea("Type '?' or 'help' for help.\nThis program does not automatically save.\n");
        mainOutput.setEditable(false);
        mainOutput.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), "Log"));
        mainOutput.setBackground(bgColor);
        JPanel mainInput = new JPanel();
        mainInput.setLayout(new BorderLayout());
        mainInput.setBackground(bgColor);
        mainInputText = new JTextField();
        JButton mainInputButton = new JButton("Submit");
        mainInputText.setBackground(bgColor);
        mainInputText.addActionListener(new ActionListener() { 
          public void actionPerformed(ActionEvent e) { 
            takeInput();
          } 
        } );
        mainInputButton.addActionListener(new ActionListener() { 
          public void actionPerformed(ActionEvent e) { 
            takeInput();
          } 
        } );
        mainInputButton.setBackground(bgColor);
        mainInputButton.setBorder(BorderFactory.createCompoundBorder( BorderFactory.createEmptyBorder(0, 5, 0, 0), buttonBorder));
        mainInput.add(mainInputText, BorderLayout.CENTER);
        mainInput.add(mainInputButton, BorderLayout.EAST);
        mainInput.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        main.add(mainOutput, BorderLayout.CENTER);
        main.add(mainInput, BorderLayout.SOUTH);
        main.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel stats = new JPanel();
        stats.setLayout(new BorderLayout());
        stats.setBackground(bgColor);
        //exp, health (find out how to do bars), credits, inv
        JPanel statsTop = new JPanel();
        statsTop.setBackground(bgColor);
        statsTop.setLayout(new BoxLayout(statsTop, BoxLayout.Y_AXIS));
        JPanel statsExperience = new JPanel();
        statsExperience.setLayout(new BoxLayout(statsExperience, BoxLayout.Y_AXIS));
        statsExperience.setBackground(bgColor);
        JProgressBar statsExperienceBar = new JProgressBar(0, 50);
        statsExperienceBar.setValue(17);
        JTextArea statsExperienceText = new JTextArea("1");
        statsExperienceBar.setForeground(new Color(0, 200, 0));
        statsExperienceBar.setBackground(bgColor);
        statsExperienceText.setBackground(bgColor);
        statsExperienceText.setEditable(false);
        statsExperience.add(statsExperienceBar);
        statsExperience.add(statsExperienceText);
        statsExperience.setBorder( BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(1), "Level NYI"));
        JPanel statsHealth = new JPanel();
        statsHealth.setBackground(bgColor);
        statsHealth.setLayout(new BoxLayout(statsHealth, BoxLayout.Y_AXIS));
        JProgressBar statsHealthBar = new JProgressBar(0, 3);
        statsHealthBar.setValue(2);
        statsHealthBar.setForeground(new Color(200, 0, 0));
        JTextArea statsHealthText = new JTextArea("2/3");
        statsHealthBar.setBackground(bgColor);
        statsHealthText.setBackground(bgColor);
        statsHealthText.setEditable(false);
        statsHealth.add(statsHealthBar);
        statsHealth.add(statsHealthText);
        statsHealth.setBorder( BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(1), "Health NYI"));
        JPanel statsCredits = new JPanel();
        statsCredits.setBackground(bgColor);
        statsCredits.setLayout(new BoxLayout(statsCredits, BoxLayout.Y_AXIS));
        statsCreditsText = new JTextArea("₡ 72");
        statsCreditsText.setBackground(bgColor);
        statsCreditsText.setEditable(false);
        statsCredits.add(statsCreditsText);
        statsCredits.setBorder( BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(1), "Credits"));
        statsTop.add(statsExperience);
        statsTop.add(statsHealth);
        statsTop.add(statsCredits);
        statsInventory = new JTextArea("1x Test Item");
        statsInventory.setEditable(false);
        statsInventory.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(1), "Inventory"));
        statsInventory.setBackground(bgColor);
        stats.add(statsTop, BorderLayout.NORTH);
        stats.add(statsInventory, BorderLayout.CENTER);
        stats.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));

        window.add(main, BorderLayout.CENTER);
        window.add(stats, BorderLayout.WEST);

        window.pack();
        window.setVisible(true);

        update();
    }

    public void update()
    {
        statsInventory.setText(Inventory.current.toString());
        statsCreditsText.setText("₡ " + Inventory.current.getCreditsText());
    }

    public void takeInput()
    {
        String input = mainInputText.getText();
        mainInputText.setText("");
        Main.processInput(input);
        update();
    }

    public void println()
    {
        mainOutput.setText(mainOutput.getText() + "\n");
    }
    
    public void println(String s)
    {
        mainOutput.setText(mainOutput.getText() + s + "\n");
    }

    public void print(String s)
    {
        mainOutput.setText(mainOutput.getText() + s);
    }

    public void clear()
    {
        mainOutput.setText("");
    }
}