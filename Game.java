        package riz;
        import java.awt.*;
        import java.awt.event.*;
        import javax.swing.*;

        public class Game extends JFrame {
            public static void main(String[] args) {
                new Game();}

            private JButton[] buttons;
            private JButton backButton; 
            private ImageIcon[] piksur;
            private int[] buttonNumbers;
            private int unangpindot = -1;
            private int sikondpindot = -1;
            private int numPairs;
            private int pairs = 0;
            private Timer timer;
            private int secondsRemaining;
            private JLabel timerLabel;
            
            public Game() {
                super("Memory Jim");
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setExtendedState(JFrame.MAXIMIZED_BOTH);
                //setSize(800, 600);
                setUndecorated(true);
                setLocationRelativeTo(null);
                setResizable(true);
                

                JPanel panel = new JPanel(new GridLayout(4, 4));        
                add(panel); 

                // game mode choosing
                Object[] options = { "Easy", "Intermediate", "Hard" };
                UIManager.put("OptionPane.questionIcon", new ImageIcon("x15.png"));
                int choice = JOptionPane.showOptionDialog(null, "Choose a difficulty level:", "Memory jim",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, options, options[0]);   
                              
                // backbutton for the game mode choosing
                backButton = new JButton("Back");
                backButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                 timer.stop();   
                 dispose(); 
                 new Game(); 
                 }
                 });
                 add(backButton, BorderLayout.SOUTH);

                 timerLabel = new JLabel("Time remaining: " + secondsRemaining + " seconds");
                 add(timerLabel, BorderLayout.NORTH); 
                 timerLabel.setHorizontalAlignment(JLabel.CENTER); 
    
                 if (choice == 0) {
                    secondsRemaining = 70;
                    numPairs = 6;
                } else if (choice == 1) {
                    secondsRemaining = 50;
                    numPairs = 8;
                } else if (choice == 2) {
                    secondsRemaining = 65;
                    numPairs = 12;
                } else {
                    System.exit(0);
                }
                timer = new Timer(1000, new TimerListener());
                timer.start();
                
                  

                piksur = new ImageIcon[numPairs];   
                piksur[0] = new ImageIcon("x1.png");    
                piksur[1] = new ImageIcon("x2.png");
                piksur[2] = new ImageIcon("x3.png");
                piksur[3] = new ImageIcon("x4.png");
                piksur[4] = new ImageIcon("x5.png");
                piksur[5] = new ImageIcon("x6.png");
                if (numPairs == 8) {
                    piksur[6] = new ImageIcon("x7.png");
                    piksur[7] = new ImageIcon("x8.png");
                }
                if (numPairs == 12) {
                    piksur[6] = new ImageIcon("x9.png");
                    piksur[7] = new ImageIcon("x10.png");
                    piksur[8] = new ImageIcon("x11.png");
                    piksur[9] = new ImageIcon("x12.png");
                    piksur[10] = new ImageIcon("x13.png");
                    piksur[11] = new ImageIcon("x14.png");
                }

                buttons = new JButton[numPairs * 2];
                buttonNumbers = new int[numPairs * 2];
                

                // Generate random order for the buttons
                int[] randomnumbers = new int[numPairs * 2];
                for (int i = 0; i < numPairs * 2; i++) {
                    randomnumbers[i] = i;
                }
                // Shuffle the indices randomly
                for (int i = 0; i < numPairs * 2; i++) {
                    int j = (int) (Math.random() * numPairs * 2);
                    int temp = randomnumbers[i];
                    randomnumbers[i] = randomnumbers[j];
                    randomnumbers[j] = temp;
                }

                // Assign each button a number and image based on the shuffled indices
                for (int i = 0; i < numPairs * 2; i++) {
                    int imageIndex = randomnumbers[i] % numPairs;
                    buttonNumbers[randomnumbers[i]] = imageIndex + 1;
                    buttons[randomnumbers[i]] = new JButton();
                    buttons[randomnumbers[i]].setBackground(Color.darkGray);
                    buttons[randomnumbers[i]].addActionListener(new ButtonListener(randomnumbers[i]));
                    panel.add(buttons[randomnumbers[i]]);
                }

                setVisible(true);
            }
            //timer logic
            private class TimerListener implements ActionListener {
                public void actionPerformed(ActionEvent e) {
                    secondsRemaining--;
                    timerLabel.setText("Time remaining: " + secondsRemaining + " seconds");
                    if (secondsRemaining == 0) {
                        timer.stop();
                        ImageIcon icon = new ImageIcon("x15.png");
                        UIManager.put("OptionPane.informationIcon", icon);
                        int choice = JOptionPane.showOptionDialog(null, "TIMES UP! BETTER LUCK NEXT TIME.", "TIME'S UP",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                                null, new Object[] { "Play again", "Quit" }, "Play again");
                        if (choice == 0) {
                            dispose();
                            new Game();
                        } else {
                            System.exit(0);
                        }
                    }
                }
            }

            private class ButtonListener implements ActionListener {
                private int index;

                public ButtonListener(int index) {
                    this.index = index;
                }
                //logic
                    public void actionPerformed(ActionEvent e) {
                        if (unangpindot == -1 && sikondpindot == -1) {
                        unangpindot = index;
                        flipCard(buttons[index], piksur[buttonNumbers[index] - 1]);
                    } else if (unangpindot != -1 && sikondpindot == -1 && unangpindot != index) {
                        sikondpindot = index;
                        flipCard(buttons[index], piksur[buttonNumbers[index] - 1]);
                        if (buttonNumbers[unangpindot] == buttonNumbers[sikondpindot]) {
                            buttons[unangpindot].setEnabled(false);
                            buttons[sikondpindot].setEnabled(false);
                            pairs++;
                            if (pairs == numPairs) {
                                timer.stop();
                                ImageIcon icon = new ImageIcon("x16.png");
                                UIManager.put("OptionPane.informationIcon", icon);
                                int choice = JOptionPane.showOptionDialog(null, "CONGRATS YOU WON!", "YOU WIN",
                                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                                        null, new Object[] { "Play again", "Quit" }, "Play again");
                                if (choice == 0) {
                                    dispose(); 
                                    new Game();
                                } else { 
                                    System.exit(0);
                                }
                            }
                            unangpindot = -1;
                            sikondpindot = -1;
                        } else {
                            Timer timer = new Timer(480, new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    flipCard(buttons[unangpindot], null);
                                    flipCard(buttons[sikondpindot], null);
                                    unangpindot = -1;
                                    sikondpindot = -1;
                                }
                            });
                            timer.setRepeats(false);
                            timer.start();
                        }
                    }
                }
            }
            //flipping card logic 
            //timer animation
            private void flipCard(final JButton button, final ImageIcon image) {
                Timer timer1 = new Timer(20, new ActionListener() {
                    private double angle = 0;

                    public void actionPerformed(ActionEvent e) {
                        angle += Math.PI / 10;
                        if (angle >= Math.PI / 2) {
                            ((Timer) e.getSource()).stop();
                            if (image != null) {
                                button.setIcon(image);
                            } else {
                                button.setIcon(null);
                            }
                            Timer timer2 = new Timer(20, new ActionListener() {
                                private double angle = Math.PI / 2;

                                public void actionPerformed(ActionEvent e) {
                                    angle -= Math.PI / 10;
                                    if (angle <= 0) {
                                        ((Timer) e.getSource()).stop();
                                    }
                                    button.setMargin(new Insets((int) (10 * Math.sin(angle)), 0, 0, 0));
                                }
                            });
                            timer2.start();
                        }
                        button.setMargin(new Insets((int) (10 * Math.sin(angle)), 0, 0, 0));
                    }
                });
                timer1.start();
            }
        }
