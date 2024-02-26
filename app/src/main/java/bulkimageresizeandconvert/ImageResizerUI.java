package bulkimageresizeandconvert;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class ImageResizerUI {

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Image Resizer and Converter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(400, 300));

        // Panel for input fields
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0, 2, 10, 10)); // rows, cols, hgap, vgap
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField sourceField = new JTextField();
        JTextField targetField = new JTextField();
        JTextField widthField = new JTextField();
        JTextField heightField = new JTextField();
        JComboBox<String> formatBox = new JComboBox<>(new String[] { "jpg", "jpeg", "png", "gif", "tiff", "pdf" });

        inputPanel.add(new JLabel("Source Directory:"));
        inputPanel.add(sourceField);
        inputPanel.add(new JLabel("Target Directory:"));
        inputPanel.add(targetField);
        inputPanel.add(new JLabel("Width:"));
        inputPanel.add(widthField);
        inputPanel.add(new JLabel("Height:"));
        inputPanel.add(heightField);
        inputPanel.add(new JLabel("Format:"));
        inputPanel.add(formatBox);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton chooseSourceButton = new JButton("Choose Source");
        JButton chooseTargetButton = new JButton("Choose Target");
        JButton convertButton = new JButton("Convert");

        chooseSourceButton.addActionListener((ActionEvent e) -> chooseDirectory(sourceField, frame));
        chooseTargetButton.addActionListener((ActionEvent e) -> chooseDirectory(targetField, frame));
        convertButton.addActionListener((ActionEvent e) -> {
            try {
                String sourceDirPath = sourceField.getText();
                String targetDirPath = targetField.getText();

                if (sourceDirPath.isEmpty() || targetDirPath.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please select both source and target directories.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int width = Integer.parseInt(widthField.getText());
                int height = Integer.parseInt(heightField.getText());
                String format = (String) formatBox.getSelectedItem();

                File sourceDir = new File(sourceDirPath);
                File[] files = sourceDir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (isSupportedFileForConversion(file)) {
                            ImageProcessor.resizeAndConvertImages(file, targetDirPath, format, width, height);
                        }
                    }
                    JOptionPane.showMessageDialog(frame, "Conversion Completed Successfully", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "No files found in the source directory.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error during conversion: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(chooseSourceButton);
        buttonPanel.add(chooseTargetButton);
        buttonPanel.add(convertButton);

        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void chooseDirectory(JTextField field, JFrame frame) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            field.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private static boolean isSupportedFileForConversion(File file) {
        String[] supportedExtensions = new String[] { "jpg", "jpeg", "png", "gif", "tiff", "pdf" };
        String fileName = file.getName().toLowerCase();
        for (String extension : supportedExtensions) {
            if (fileName.endsWith("." + extension)) {
                return true;
            }
        }
        return false;
    }
}
