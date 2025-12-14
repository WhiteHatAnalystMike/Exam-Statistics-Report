import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This program reads exam grades from a file and prints a report.
 *
 * The program queries the user for a file name.
 * It parses the file and computes the scores of students who took the final exam
 * and the grade they received.
 * The program then prints a histogram of the distribution of exam scores,
 * as well as average, standard deviation, and median statistics.
 *
 * @author Michael Koranteng
 */
public class ExamStatisticsReport {

    public static void main(String[] args) {
        Scanner inputScanner = new Scanner(System.in);
        File file;
        ArrayList<Integer> scores = new ArrayList<>();
        ArrayList<String> gradeLabels = new ArrayList<>();
        ArrayList<Integer> gradeCounts = new ArrayList<>();

        while (true) {
            System.out.print("Enter the name of the scores file: ");
            String fileName = inputScanner.nextLine();
            file = new File(fileName);
            if (file.exists() && file.isFile()) {
                break;
            } else {
                System.out.println("Sorry, I cannot open: " + fileName);
            }
        }

        System.out.println("\nResults from the Midterm Exam\n");
        System.out.printf("%-20s%5s  %s\n", "Student", "Score", "Grade");
        System.out.printf("%-20s%5s  %s\n", "-------", "-----", "-----");

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                Scanner lineScanner = new Scanner(line);
                try {
                    String surname = lineScanner.next();
                    String givenName = lineScanner.next();
                    int score = lineScanner.nextInt();

                    scores.add(score);
                    String letterGrade = getLetterGrade(score);
                    System.out.printf("%-20s%5d  %s\n", surname + ", " + givenName, score, letterGrade);

                    int index = gradeLabels.indexOf(letterGrade);
                    if (index == -1) {
                        gradeLabels.add(letterGrade);
                        gradeCounts.add(1);
                    } else {
                        gradeCounts.set(index, gradeCounts.get(index) + 1);
                    }

                } catch (Exception e) {
                    System.err.println("Error parsing line, " + line + " skipping");
                }
                lineScanner.close();
            }
        } catch (FileNotFoundException e) {
            System.err.println("Unexpected error opening file.");
            return;
        }

        System.out.println("\nDistribution of Grades on the Midterm Exam\n");

        String[] displayGrades = {"A", "A-", "B+", "B", "B-", "C", "D+", "D", "F"};
        for (String label : displayGrades) {
            int idx = gradeLabels.indexOf(label);
            int count = (idx != -1) ? gradeCounts.get(idx) : 0;
            System.out.printf("%-3s: %s\n", label, "*".repeat(count));
        }

        System.out.println();

        double average = computeAverage(scores);
        double stdDev = computeStandardDeviation(scores, average);
        selectionSort(scores);
        double median = computeMedian(scores);

        System.out.printf("The average score is:  %.1f\n", average);
        System.out.printf("The standard deviation is:  %.1f\n", stdDev);
        System.out.printf("The median score is:  %.1f\n", median);
    }

    /**
     * Returns letter grade based on score using +/- scale.
     */
    private static String getLetterGrade(int score) {
        if (score >= 97) return "A";
        if (score >= 93) return "A";
        if (score >= 90) return "A-";
        if (score >= 87) return "B+";
        if (score >= 83) return "B";
        if (score >= 80) return "B-";
        if (score >= 77) return "C+";
        if (score >= 73) return "C";
        if (score >= 70) return "C";
        if (score >= 67) return "D+";
        if (score >= 63) return "D";
        if (score >= 60) return "D";
        return "F";
    }

    /**
     * Calculates the average score.
     */
    private static double computeAverage(ArrayList<Integer> scores) {
        double sum = 0;
        for (int score : scores) sum += score;
        return sum / scores.size();
    }

    /**
     * Calculates the sample standard deviation.
     */
    private static double computeStandardDeviation(ArrayList<Integer> scores, double average) {
        double sum = 0;
        for (int score : scores) {
            sum += (score - average) * (score - average);
        }
        return Math.sqrt(sum / (scores.size() - 1));
    }

    /**
     * Sorts the list using selection sort.
     */
    private static void selectionSort(ArrayList<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(j) < list.get(minIndex)) {
                    minIndex = j;
                }
            }
            int temp = list.get(i);
            list.set(i, list.get(minIndex));
            list.set(minIndex, temp);
        }
    }

    /**
     * Computes the median of a sorted list.
     */
    private static double computeMedian(ArrayList<Integer> sortedScores) {
        int n = sortedScores.size();
        if (n % 2 == 1) {
            return sortedScores.get(n / 2);
        } else {
            return (sortedScores.get(n / 2 - 1) + sortedScores.get(n / 2)) / 2.0;
        }
    }
}
