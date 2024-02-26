package bulkimageresizeandconvert;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessor {

    public static void resizeAndConvertImages(File inputFile, String targetDir, String outputFormat, int targetWidth,
            int targetHeight) throws IOException {
        String inputFormat = getFileExtension(inputFile.getName()).toLowerCase();
        File outputFile = new File(targetDir, getBaseName(inputFile.getName()) + "." + outputFormat);

        switch (inputFormat) {
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
            case "tiff":
                Thumbnails.of(inputFile)
                        .size(targetWidth, targetHeight)
                        .outputFormat(outputFormat)
                        .toFile(outputFile);
                break;
            case "pdf":
                convertPdfToImageAndResize(inputFile, outputFile, outputFormat, targetWidth, targetHeight);
                break;
            default:
                throw new IllegalArgumentException("Unsupported format: " + inputFormat);
        }
    }

    private static void convertPdfToImageAndResize(File inputFile, File outputFile, String outputFormat, int width,
            int height) throws IOException {
        try (PDDocument document = PDDocument.load(inputFile)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 300); // Render first page
            BufferedImage resizedImage = resizeImage(bim, width, height);
            ImageIO.write(resizedImage, outputFormat, outputFile);
        }
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();
        return outputImage;
    }

    private static String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else
            return "";
    }

    private static String getBaseName(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }
}
