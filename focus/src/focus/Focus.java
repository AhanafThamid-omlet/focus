package focus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
public class Focus {

    private static final Random random = new Random();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== NUMBER ORDER GAME ===");

        boolean playAgain = true;
        while (playAgain) {
            playRound(scanner);

            System.out.print("\nPlay again? (y/n): ");
            String answer = scanner.nextLine().trim().toLowerCase();
            playAgain = answer.equals("y") || answer.equals("yes");
        }

        System.out.println("Thanks for playing!");
        scanner.close();
    }

    private static void playRound(Scanner scanner) {
        // Random amount of numbers (5 to 10), unique, from 1-100
        int count = random.nextInt(6) + 5;
        List<Integer> numbers = new ArrayList<>();
        while (numbers.size() < count) {
            int n = random.nextInt(100) + 1;
            if (!numbers.contains(n)) {
                numbers.add(n);
            }
        }
        Collections.shuffle(numbers);

        // Random order: ascending or descending
        boolean ascending = random.nextBoolean();

        System.out.println("\nHere are your numbers:");
        System.out.println(numbers);
        System.out.println("\nOrder them " + (ascending ? "INCREMENTALLY (smallest to largest)": "DECREMENTALLY (largest to smallest)") + ".");
        System.out.println("Type the numbers separated by spaces, then press Enter.");

        long startTime = System.nanoTime(); // timer starts now
        List<Integer> guess = readGuess(scanner, numbers);
        double seconds = (System.nanoTime() - startTime) / 1_000_000_000.0;

        // Build the correct answer
        List<Integer> correct = new ArrayList<>(numbers);
        Collections.sort(correct);
        if (!ascending) {
            Collections.reverse(correct);
        }

        if (guess.equals(correct)) {
            System.out.println("\n Right! Well done!");
        } else {
            System.out.println("\n Wrong!");
            System.out.println("Correct answer: " + correct);
        }

        System.out.println("  Time taken: " + formatTime(seconds));
    }

    // Formats seconds as "12.3 seconds" or "1 min 5.2 sec"
    private static String formatTime(double seconds) {
        if (seconds < 60) {
            return String.format("%.1f seconds", seconds);
        }
        int minutes = (int) (seconds / 60);
        double remaining = seconds - minutes * 60;
        return String.format("%d min %.1f sec", minutes, remaining);
    }

    // Keeps asking until the player enters a valid set of the given numbers
    private static List<Integer> readGuess(Scanner scanner, List<Integer> original) {
        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            String[] parts = line.split("[\\s,]+");

            List<Integer> guess = new ArrayList<>();
            boolean valid = true;
            for (String part : parts) {
                try {
                    guess.add(Integer.parseInt(part));
                } catch (NumberFormatException e) {
                    valid = false;
                    break;
                }
            }

            if (!valid || line.isEmpty()) {
                System.out.println("Please enter numbers only, separated by spaces.");
                continue;
            }

            if (guess.size() != original.size()) {
                System.out.println("You must enter exactly " + original.size() + " numbers.");
                continue;
            }
            List<Integer> a = new ArrayList<>(guess);
            List<Integer> b = new ArrayList<>(original);
            Collections.sort(a);
            Collections.sort(b);
            if (!a.equals(b)) {
                System.out.println("Use only the numbers shown above.");
                continue;
            }
            return guess;
        }
    }
}
