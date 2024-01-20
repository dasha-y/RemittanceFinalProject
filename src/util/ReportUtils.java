package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ReportUtils {
    public static void createReportFile(File file, String content) {
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

