import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HorseBettingGUI {
    private static final int INITIAL_WAGE = 100000;
    private static int currentWage = 100000;
    private static int yourBet = 0;
    private JFrame frame;
    private JLabel wageLabel;
    private JLabel betLabel;
    private ButtonGroup horseGroup;
    private JCheckBox hardcoreModeCheckbox;
    private List<Horse> horses;
    private String selectedHorseName;

    public HorseBettingGUI() {
        // Fetch horse data
        HorseBettingDataFetcher dataFetcher = new HorseBettingDataFetcher();
        horses = dataFetcher.fetchHorseData();

        frame = new JFrame("HORSE BETTER 1738x - Java Project");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1152, 768);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title and Subtitle
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        JLabel titleLabel = new JLabel("HORSE BETTER 1738x", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        JLabel subtitleLabel = new JLabel("'Life is Short, Bet on Virtual Horses'", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Center Panel for Horse Select and Wages
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 20));

        // Horses
        JPanel horsePanel = new JPanel(new GridLayout(6, 1, 10, 10));
        horsePanel.setBorder(BorderFactory.createTitledBorder("Select Horse"));
        horseGroup = new ButtonGroup();

        // Create a map to store the selected horse
        Map<Horse, JButton> selectedHorseMap = new HashMap<>();

        for (Horse horse : horses) {
            // Create a JButton with the horse information
            JButton horseButton = new JButton(
                horse.getHorseName() + " (" + horse.getHorseNum() + ") [1 / " + horse.getOddBets() + "]" + " [" + horse.getColor() + "]"
            );
            horseButton.setFont(new Font("Verdana", Font.BOLD, 20));
            // Set the horizontal alignment of the text to left-aligned
            horseButton.setHorizontalAlignment(SwingConstants.LEFT);

            horseButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton clickedButton = (JButton) e.getSource();
                    if (selectedHorseMap.containsValue(clickedButton)) {
                        // Deselect the clicked button
                        clickedButton.setBackground(null);
                        selectedHorseMap.remove(horse);
                    } else {
                        // Deselect the previously selected button
                        if (!selectedHorseMap.isEmpty()) {
                            JButton prevButton = selectedHorseMap.values().iterator().next();
                            prevButton.setBackground(null);
                            selectedHorseMap.clear();
                        }
                        // Select the clicked button
                        clickedButton.setBackground(new Color(173, 216, 230));
                        selectedHorseMap.put(horse, clickedButton);
                    }
                    selectedHorseName = horse.getHorseName();
                }
            });

            horsePanel.add(horseButton);
        }
        centerPanel.add(horsePanel);

        // Wages and Bet Amount
        JPanel wagerPanel = new JPanel(new BorderLayout(20, 20));

        // Current Wage and Your Bet Display
        JPanel wagerDisplayPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        wagerDisplayPanel.setBorder(BorderFactory.createTitledBorder("Wages and Bet Amount"));

        wageLabel = new JLabel("Current Wage: $" + currentWage, JLabel.CENTER);
        wageLabel.setFont(new Font("Arial", Font.BOLD, 20));
        betLabel = new JLabel("Your Bet: $" + yourBet, JLabel.CENTER);
        betLabel.setFont(new Font("Arial", Font.BOLD, 20));
        wagerDisplayPanel.add(wageLabel);
        wagerDisplayPanel.add(betLabel);

        wagerPanel.add(wagerDisplayPanel, BorderLayout.NORTH);

        // Create the checkbox
        hardcoreModeCheckbox = new JCheckBox("Hardcore Mode");
        hardcoreModeCheckbox.setFont(new Font("Arial", Font.BOLD, 16));

        // Add the checkbox to the panel (assuming wagerPanel uses a suitable layout manager)
        wagerPanel.add(hardcoreModeCheckbox);

        // Ensure the panel is visible and repainted
        wagerPanel.setVisible(true);
        wagerPanel.repaint();

        // Buttons Panel for predefined bet amounts
        JPanel betButtonPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        int[] betAmounts = {1, 10, 50, 100, 500, 1000, 10000};
        for (int amount : betAmounts) {
            JButton betButton = new JButton("$" + amount);
            betButton.setFont(new Font("Arial", Font.BOLD, 16));
            betButton.setBackground(new Color(240, 240, 240));
            betButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (amount > currentWage) {
                        JOptionPane.showMessageDialog(frame, "You cannot bet more than your current wage.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        yourBet += amount;
                        currentWage -= amount;
                        updateLabels();
                    }
                }
            });
            betButtonPanel.add(betButton);
        }

        // Reset Button
        JButton resetButton = new JButton("RESET");
        resetButton.setFont(new Font("Arial", Font.BOLD, 16));
        resetButton.setBackground(new Color(255, 69, 0));
        resetButton.setForeground(Color.WHITE);
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Reset yourBet to 0
                yourBet = 0;
                // Reset currentWage to its initial value
                currentWage = INITIAL_WAGE;
                // Reset selected horse and update labels
                resetState();
            }
        });
        betButtonPanel.add(resetButton);

        // All In Button
        JButton allInButton = new JButton("ALL IN");
        allInButton.setFont(new Font("Arial", Font.BOLD, 16));
        allInButton.setBackground(new Color(34, 139, 34));
        allInButton.setForeground(Color.WHITE);
        allInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                yourBet += currentWage;
                currentWage = 0;
                updateLabels();
            }
        });
        betButtonPanel.add(allInButton);

        wagerPanel.add(betButtonPanel, BorderLayout.CENTER);

        // Start Bet Button
        JButton startBetButton = new JButton("START BET");
        startBetButton.setFont(new Font("Arial", Font.BOLD, 16));
        startBetButton.setBackground(new Color(0, 128, 0));
        startBetButton.setForeground(Color.WHITE);
        startBetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (yourBet > 0 && selectedHorseName != null) {
                    startRace();
                } else {
                    JOptionPane.showMessageDialog(frame, "Please place a bet and select a horse before starting the race.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        wagerPanel.add(startBetButton, BorderLayout.SOUTH);

        centerPanel.add(wagerPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Add main panel to frame
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void startRace() {
        // Check if a horse is selected
        if (selectedHorseName == null) {
            // Handle the case where no horse is selected
            return;
        }
    
        double[] odds = new double[horses.size()];
        for (int i = 0; i < horses.size(); i++) {
            odds[i] = 1.0 / horses.get(i).getOddBets();
        }
    
        double total = 0;
        for (double odd : odds) {
            total += odd;
        }
    
        double random = Math.random() * total;
        double cumulative = 0;
        int winningIndex = -1;
    
        for (int i = 0; i < odds.length; i++) {
            cumulative += odds[i];
            if (random <= cumulative) {
                winningIndex = i;
                break;
            }
        }
    
        Horse winningHorse = horses.get(winningIndex);
    
        int earnings = 0;
        if (selectedHorseName.equals(winningHorse.getHorseName())) {
            // Calculate earnings based on the bet and the winning odds
            earnings = (int) (yourBet * winningHorse.getOddBets());
            currentWage += earnings; // Add earnings to current wage
        } else {
            earnings = -yourBet; // You lose your bet
            currentWage += earnings; // Deduct the lost bet from current wage
        }
    
        // Reset your bet
        yourBet = 0;
    
        // Update the labels to reflect the new state
        updateLabels();
    
        // Display the race result
        RaceResultGUI resultGUI = new RaceResultGUI(this);
        resultGUI.updateResult(winningHorse.getHorseName(), earnings, horses);
    }
    
    public void resetState() {
        // Calculate the difference between the current wage and the bet
        int difference = currentWage - yourBet;

        // Resetting the bet to 0
        yourBet = 0;

        // Resetting selected horse and button backgrounds
        selectedHorseName = null;
        for (AbstractButton button : Collections.list(horseGroup.getElements())) {
            button.setSelected(false);
            button.setBackground(null);
        }

        // Resetting the hardcore mode checkbox
        if (hardcoreModeCheckbox != null) {
            hardcoreModeCheckbox.setSelected(false);
        }

        // Update the bet label to reflect the new bet
        betLabel.setText("Your Bet: $" + yourBet);

        // Update the current wage label to reflect the new current wage
        currentWage = difference;
        wageLabel.setText("Current Wage: $" + currentWage);
    }

    private void updateLabels() {
        wageLabel.setText("Current Wage: $" + currentWage);
        betLabel.setText("Your Bet: $" + yourBet);
    }

    public String getYourHorseName() {
        return selectedHorseName;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HorseBettingGUI::new);
    }
}
