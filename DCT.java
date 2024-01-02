import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DCT {
    public static void main(String[] args) {
        // Create and set up the window
        JFrame frame = new JFrame("DCT Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);

        // Create UI components
        JTextArea resultArea = new JTextArea(10, 40);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Layout using GridBagLayout
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Adding components to the frame
        frame.add(scrollPane, gbc);

        // Set the frame to be initially invisible
        frame.setVisible(false);

        // Process file and update UI
        processFile(frame, resultArea);
    }

    private static void processFile(JFrame frame, JTextArea resultArea) {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            double[][] matrix = readMatrixFromFile(file.getAbsolutePath());
            if (matrix != null) {
                int N = matrix.length;

                // Perform DCT (rows then columns)
                double[][] dctRowFirst = performDCTRowsFirst(matrix, N);
                resultArea.append("DCT (Rows then Columns):\n");
                appendMatrixToTextArea(resultArea, dctRowFirst);

                // Perform DCT (columns then rows)
                double[][] dctColumnFirst = performDCTColFirst(matrix, N);
                resultArea.append("\nDCT (Columns then Rows):\n");
                appendMatrixToTextArea(resultArea, dctColumnFirst);

                // Make the frame visible
                frame.setVisible(true);
            }
        }
    }
    
    // Helper Function to print results to the UI
    private static double[][] readMatrixFromFile(String fileName) {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            int N = scanner.nextInt();
            double[][] matrix = new double[N][N];
    
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (!scanner.hasNextDouble()) {
                        throw new IllegalArgumentException("Invalid matrix format in file.");
                    }
                    matrix[i][j] = scanner.nextDouble();
                }
            }
            return matrix;
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Error reading matrix: " + e.getMessage());
        }
        return null;
    }
    

    private static void appendMatrixToTextArea(JTextArea textArea, double[][] matrix) {
        for (double[] row : matrix) {
            for (double val : row) {
                textArea.append(String.format("%4.0f ", val));
            }
            textArea.append("\n");
        }
    }

    // DCT that goes through the row first.
    private static double[][] performDCTRowsFirst(double[][] matrix, int N) {
        int i, j, k, l;
        double[][] dct = new double[N][N];
  
        double ci, cj, dct1, sum;
        for (i = 0; i < N; i++) 
        {
            for (j = 0; j < N; j++) 
            {
                // ci and cj depends on frequency as well as
                // number of row and columns of specified matrix
                if (i == 0)
                    ci = 1 / Math.sqrt(N);
                else
                    ci = Math.sqrt(2) / Math.sqrt(N);
                     
                if (j == 0)
                    cj = 1 / Math.sqrt(N);
                else
                    cj = Math.sqrt(2) / Math.sqrt(N);
  
                // sum will temporarily store the sum of 
                // cosine signals
                sum = 0;
                for (k = 0; k < N; k++) 
                {
                    for (l = 0; l < N; l++) 
                    {
                        dct1 = matrix[k][l] * 
                               Math.cos((2 * k + 1) * i * Math.PI / (2 * N)) * 
                               Math.cos((2 * l + 1) * j * Math.PI / (2 * N));
                        sum = sum + dct1;
                    }
                }
                dct[i][j] = ci * cj * sum;
            }
        }

        
        return dct;
    }

    private static double[][] performDCTColFirst(double[][] matrix, int N) {
        int i, j, k, l;
        double[][] dct = new double[N][N];
    
        double ci, cj, dct1, sum;
    
        for (j = 0; j < N; j++) {  // Outer loop for columns first
            for (i = 0; i < N; i++) {  // Inner loop for rows
                // ci and cj depends on frequency as well as
                // number of row and columns of specified matrix
                if (i == 0)
                    ci = 1 / Math.sqrt(N);
                else
                    ci = Math.sqrt(2) / Math.sqrt(N);
    
                if (j == 0)
                    cj = 1 / Math.sqrt(N);
                else
                    cj = Math.sqrt(2) / Math.sqrt(N);
    
                // sum will temporarily store the sum of 
                // cosine signals
                sum = 0;
                for (k = 0; k < N; k++) {
                    for (l = 0; l < N; l++) {
                        dct1 = matrix[k][l] * 
                               Math.cos((2 * k + 1) * i * Math.PI / (2 * N)) * 
                               Math.cos((2 * l + 1) * j * Math.PI / (2 * N));
                        sum = sum + dct1;
                    }
                }
                dct[i][j] = ci * cj * sum;
            }
        }
    
        return dct;
    }
    

  

}
