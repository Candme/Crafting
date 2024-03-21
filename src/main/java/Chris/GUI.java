package Chris;

import java.util.*;

import java.io.*;
import java.nio.file.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

import Chris.ItemSystem.*;
import Chris.ItemSystem.ItemRegistry;

import Chris.ItemSystem.ItemTypes.*;
import Chris.ItemSystem.GatheringAreas.*;

public class GUI
{
    public static GUI currentGUI;
    
    JTextArea statsCreditsText;
    JTextArea statsInventory;
    JTextField mainInputText;
    JTextArea mainOutput;
    JProgressBar statsHealthBar;
    JTextArea statsHealthText;
    
    public GUI()
    {
        currentGUI = this;
        Border buttonBorder = BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(0), BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JFrame window = new JFrame("Version 0.1.0G");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        window.setSize(500, 360);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBackground(new Color(236, 236, 236));

        Color bgColor = window.getBackground();

        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        main.setBackground(bgColor);
        mainOutput = new JTextArea("Type '?' or 'help' for help.\nThis program does not automatically save.\n");
        mainOutput.setEditable(false);
        mainOutput.setBorder(BorderFactory.createEmptyBorder());
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
        JScrollPane scrollArea = new JScrollPane(mainOutput);
        scrollArea.setBorder( BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), "Log"));
        main.add(scrollArea, BorderLayout.CENTER);
        main.add(mainInput, BorderLayout.SOUTH);
        main.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel stats = new JPanel();
        stats.setLayout(new BorderLayout());
        stats.setBackground(bgColor);
        JPanel statsTop = new JPanel();
        statsTop.setBackground(bgColor);
        statsTop.setLayout(new BoxLayout(statsTop, BoxLayout.Y_AXIS));
        JPanel statsExperience = new JPanel();
        statsExperience.setLayout(new BoxLayout(statsExperience, BoxLayout.Y_AXIS));
        statsExperience.setBackground(bgColor);
        JProgressBar statsExperienceBar = new JProgressBar(0, 50);
        statsExperienceBar.setValue(0);
        JTextArea statsExperienceText = new JTextArea("1");
        statsExperienceBar.setForeground(new Color(0, 200, 0));
        statsExperienceBar.setBackground(bgColor);
            statsExperienceBar.setBorder(BorderFactory.createBevelBorder(0));
        statsExperienceText.setBackground(bgColor);
        statsExperienceText.setEditable(false);
        statsExperience.add(statsExperienceBar);
        statsExperience.add(statsExperienceText);
        statsExperience.setBorder( BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(1), "Level NYI"));
        JPanel statsHealth = new JPanel();
        statsHealth.setBackground(bgColor);
        statsHealth.setLayout(new BoxLayout(statsHealth, BoxLayout.Y_AXIS));
        statsHealthBar = new JProgressBar(0, 3);
        statsHealthBar.setValue(3);
        statsHealthBar.setForeground(new Color(200, 0, 0));
        statsHealthText = new JTextArea("2/3");
        statsHealthBar.setBackground(bgColor);
        statsHealthBar.setBorder(BorderFactory.createBevelBorder(0));
        statsHealthText.setBackground(bgColor);
        statsHealthText.setEditable(false);
        statsHealth.add(statsHealthBar);
        statsHealth.add(statsHealthText);
        statsHealth.setBorder( BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(1), "Health"));
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

    public static void update()
    {
        currentGUI.statsInventory.setText(Inventory.current.toString());
        currentGUI.statsCreditsText.setText("₡ " + Inventory.current.getCreditsText());
        currentGUI.statsHealthText.setText(Inventory.current.health + "/" + Inventory.current.maxHealth);
        currentGUI.statsHealthBar.setMaximum(Inventory.current.maxHealth);
        currentGUI.statsHealthBar.setValue(Inventory.current.health);
    }

    public static void takeInput()
    {
        String input = currentGUI.mainInputText.getText();
        currentGUI.mainInputText.setText("");
        Runner.processInput(input);
        update();
    }

    public static void println()
    {
        currentGUI.mainOutput.setText(currentGUI.mainOutput.getText() + "\n");
    }
    
    public static void println(String s)
    {
        currentGUI.mainOutput.setText(currentGUI.mainOutput.getText() + s + "\n");
    }

    public static void print(String s)
    {
        currentGUI.mainOutput.setText(currentGUI.mainOutput.getText() + s);
    }

    public static void clear()
    {
        currentGUI.mainOutput.setText("");
    }

    
}