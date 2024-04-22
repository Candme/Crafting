package Chris;

import java.util.*;

import java.io.*;
import java.nio.file.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
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
    JProgressBar statsExperienceBar;
    JTextArea statsExperienceText;
    
    public GUI()
    {
        currentGUI = this;
        Border buttonBorder = BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(0), BorderFactory.createEmptyBorder(5, 10, 5, 10));
        Color bgColor = new Color(50, 50, 65);
        Color ftColor = new Color(200, 200, 205);
        Font monoFont = new Font(Font.MONOSPACED, 0, 12);

        TitledBorder title = new TitledBorder(BorderFactory.createBevelBorder(1), "Log");
        title.setTitleColor(ftColor);

        JFrame frame = new JFrame("Chris's Text Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 360);

        JPanel window = new JPanel();
        window.setLayout(new BorderLayout());
        window.setBackground(bgColor);
        window.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(ftColor, 3), BorderFactory.createBevelBorder(0)));
        frame.add(window);

        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        main.setBackground(bgColor);
        mainOutput = new JTextArea("Version 0.2.4: The Combat Update\nType '?' or 'help' for help.\nThis program does not automatically save.\n");
        mainOutput.setEditable(false);
        mainOutput.setBackground(bgColor);
        mainOutput.setBorder(BorderFactory.createEmptyBorder());
        mainOutput.setFont(monoFont);
        mainOutput.setForeground(ftColor);
        JPanel mainInput = new JPanel();
        mainInput.setLayout(new BorderLayout());
        mainInput.setBackground(bgColor);
        mainInputText = new JTextField();
        mainInputText.setForeground(ftColor);
        JButton mainInputButton = new JButton("Submit");
        mainInputText.setBackground(bgColor);
        mainInputText.addActionListener(new ActionListener() { 
          public void actionPerformed(ActionEvent e) { 
            takeInput();
          } 
        } );
        mainInputText.setFont(monoFont);
        mainInputText.setForeground(ftColor);
        mainInputButton.addActionListener(new ActionListener() { 
          public void actionPerformed(ActionEvent e) { 
            takeInput();
          } 
        } );
        mainInputButton.setBackground(ftColor);
        mainInputButton.setForeground(bgColor);
        mainInputButton.setBorder(buttonBorder);
        mainInputText.setBorder(BorderFactory.createCompoundBorder( BorderFactory.createEmptyBorder(0, 0, 0, 5), BorderFactory.createLineBorder(ftColor)));
        mainInput.add(mainInputText, BorderLayout.CENTER);
        mainInput.add(mainInputButton, BorderLayout.EAST);
        mainInput.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        JScrollPane scrollArea = new JScrollPane(mainOutput);
        scrollArea.setBackground(bgColor);
        scrollArea.setForeground(ftColor);
        title.setTitle("Log");
        scrollArea.setBorder(title);
        main.add(scrollArea, BorderLayout.CENTER);
        main.add(mainInput, BorderLayout.SOUTH);
        main.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        title = new TitledBorder(BorderFactory.createBevelBorder(1), "Level");
        title.setTitleColor(ftColor);
        
        JPanel stats = new JPanel();
        stats.setLayout(new BorderLayout());
        stats.setBackground(bgColor);
        JPanel statsTop = new JPanel();
        statsTop.setBackground(bgColor);
        statsTop.setLayout(new BoxLayout(statsTop, BoxLayout.Y_AXIS));
        JPanel statsExperience = new JPanel();
        statsExperience.setLayout(new BoxLayout(statsExperience, BoxLayout.Y_AXIS));
        statsExperience.setBackground(bgColor);
        statsExperienceBar = new JProgressBar(0, 50);
        statsExperienceBar.setValue(0);
        statsExperienceText = new JTextArea("1");
        statsExperienceText.setForeground(ftColor);
        statsExperienceBar.setForeground(new Color(0, 200, 0));
        statsExperienceBar.setBackground(bgColor);
            statsExperienceBar.setBorder(BorderFactory.createBevelBorder(0));
        statsExperienceText.setBackground(bgColor);
        statsExperienceText.setEditable(false);
        statsExperienceText.setFont(monoFont);
        statsExperience.add(statsExperienceBar);
        statsExperience.add(statsExperienceText);
        statsExperience.setForeground(ftColor);
        title = new TitledBorder(BorderFactory.createBevelBorder(1), "Level");
        title.setTitleColor(ftColor);
        statsExperience.setBorder(title);
        JPanel statsHealth = new JPanel();
        statsHealth.setBackground(bgColor);
        statsHealth.setForeground(ftColor);
        statsHealth.setLayout(new BoxLayout(statsHealth, BoxLayout.Y_AXIS));
        statsHealthBar = new JProgressBar(0, 3);
        statsHealthBar.setValue(3);
        statsHealthBar.setForeground(new Color(200, 0, 0));
        statsHealthText = new JTextArea("2/3");
        statsHealthText.setForeground(ftColor);
        statsHealthBar.setBackground(bgColor);
        statsHealthBar.setBorder(BorderFactory.createBevelBorder(0));
        statsHealthText.setBackground(bgColor);
        statsHealthText.setEditable(false);
        statsHealthText.setFont(monoFont);
        statsHealth.add(statsHealthBar);
        statsHealth.add(statsHealthText);
        title = new TitledBorder(BorderFactory.createBevelBorder(1), "Health");
        title.setTitleColor(ftColor);
        statsHealth.setBorder(title);
        JPanel statsCredits = new JPanel();
        statsCredits.setBackground(bgColor);
        statsCredits.setForeground(ftColor);
        statsCredits.setLayout(new BoxLayout(statsCredits, BoxLayout.Y_AXIS));
        statsCreditsText = new JTextArea("₡ 72");
        statsCreditsText.setBackground(bgColor);
        statsCreditsText.setEditable(false);
        statsCreditsText.setFont(monoFont);
        statsCreditsText.setForeground(ftColor);
        statsCredits.add(statsCreditsText);
        title = new TitledBorder(BorderFactory.createBevelBorder(1), "Credits");
        title.setTitleColor(ftColor);
        statsCredits.setBorder(title);
        statsTop.add(statsExperience);
        statsTop.add(statsHealth);
        statsTop.add(statsCredits);
        statsInventory = new JTextArea("1x Test Item");
        statsInventory.setEditable(false);
        statsInventory.setBackground(bgColor);
        statsInventory.setBorder(BorderFactory.createEmptyBorder());
        statsInventory.setFont(monoFont);
        statsInventory.setForeground(ftColor);
        JScrollPane scrollArea2 = new JScrollPane(statsInventory);
        scrollArea2.setBackground(bgColor);
        scrollArea2.setForeground(ftColor);
        title = new TitledBorder(BorderFactory.createBevelBorder(1), "Inventory");
        title.setTitleColor(ftColor);
            scrollArea2.setBorder(title);
        
        stats.add(statsTop, BorderLayout.NORTH);
        stats.add(scrollArea2, BorderLayout.CENTER);
        stats.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));

        stats.setMinimumSize(new Dimension(1000, 1));
        
        window.add(main, BorderLayout.CENTER);
        window.add(stats, BorderLayout.WEST);

        frame.pack();
        frame.setVisible(true);

        update();
    }

    public static void update()
    {
        currentGUI.statsInventory.setText(Inventory.current.toString());
        currentGUI.statsCreditsText.setText("₡ " + Inventory.current.getCreditsText());
        currentGUI.statsHealthText.setText(Inventory.current.health + "/" + Inventory.current.maxHealth);
        currentGUI.statsHealthBar.setMaximum(Inventory.current.maxHealth);
        currentGUI.statsHealthBar.setValue(Inventory.current.health);
    currentGUI.statsExperienceText.setText(Inventory.current.level + ": " + Inventory.current.experience + "/" + Inventory.current.maxExperience);
        currentGUI.statsExperienceBar.setMaximum(Inventory.current.maxExperience);
        currentGUI.statsExperienceBar.setValue(Inventory.current.experience);
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
        System.out.println();
    }
    
    public static void println(String s)
    {
        currentGUI.mainOutput.setText(currentGUI.mainOutput.getText() + s + "\n");
        System.out.println(s);
    }

    public static void print(String s)
    {
        currentGUI.mainOutput.setText(currentGUI.mainOutput.getText() + s);
        System.out.print(s);
    }

    public static void clear()
    {
        currentGUI.mainOutput.setText("");
        System.out.println();
    }

    
}