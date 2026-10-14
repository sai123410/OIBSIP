import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class NumberGuessingGame extends JFrame {

    private final Random random = new Random();

    private JComboBox<String> difficultyBox;
    private JTextField guessField;
    private JLabel messageLabel;
    private JLabel attemptsLabel;
    private JLabel scoreLabel;
    private JButton guessButton;
    private JButton playAgainButton;

    private int secretNumber;
    private int maxAttempts;
    private int attempts;
    private int score = 0;
    private int range;

    public NumberGuessingGame() {
        setTitle("Number Guessing Game");
        setSize(420, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        difficultyBox = new JComboBox<>(
                new String[]{"Easy", "Medium", "Hard"});

        guessField = new JTextField();

        guessButton = new JButton("Guess");
        playAgainButton = new JButton("Play Again");

        messageLabel = new JLabel("Choose difficulty and enter a guess.");
        attemptsLabel = new JLabel();
        scoreLabel = new JLabel();

        panel.add(new JLabel("Difficulty:"));
        panel.add(difficultyBox);

        panel.add(new JLabel("Enter number:"));
        panel.add(guessField);

        panel.add(guessButton);
        panel.add(playAgainButton);

        panel.add(new JLabel("Game status:"));
        panel.add(messageLabel);

        panel.add(new JLabel("Attempts left:"));
        panel.add(attemptsLabel);

        panel.add(new JLabel("Score (wins):"));
        panel.add(scoreLabel);

        add(panel);

        difficultyBox.addActionListener(e -> startNewRound());
        guessButton.addActionListener(e -> checkGuess());
        playAgainButton.addActionListener(e -> startNewRound());

        startNewRound();
    }

    private void startNewRound() {
        String difficulty = (String) difficultyBox.getSelectedItem();

        switch (difficulty) {
            case "Easy":
                range = 50;
                maxAttempts = 10;
                break;
            case "Medium":
                range = 100;
                maxAttempts = 7;
                break;
            default:
                range = 200;
                maxAttempts = 5;
                break;
        }

        secretNumber = random.nextInt(range) + 1;
        attempts = 0;

        guessField.setText("");
        guessField.setEnabled(true);
        guessButton.setEnabled(true);

        messageLabel.setText("Guess a number from 1 to " + range);
        attemptsLabel.setText(String.valueOf(maxAttempts));
        scoreLabel.setText(String.valueOf(score));
    }

    private void checkGuess() {
        String input = guessField.getText().trim();

        int guess;

        try {
            guess = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            messageLabel.setText("Enter a valid whole number.");
            return;
        }

        if (guess < 1 || guess > range) {
            messageLabel.setText("Enter a number from 1 to " + range);
            return;
        }

        attempts++;

        if (guess == secretNumber) {
            messageLabel.setText("Correct! You won!");
            score++;
            endRound();
        } else if (guess < secretNumber) {
            messageLabel.setText("Too Low! Try again.");
        } else {
            messageLabel.setText("Too High! Try again.");
        }

        int remaining = maxAttempts - attempts;
        attemptsLabel.setText(String.valueOf(remaining));

        if (guess != secretNumber && remaining == 0) {
            messageLabel.setText("You Lost! Number: " + secretNumber);
            endRound();
        }

        scoreLabel.setText(String.valueOf(score));
        guessField.setText("");
    }

    private void endRound() {
        guessButton.setEnabled(false);
        guessField.setEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new NumberGuessingGame().setVisible(true));
    }
}