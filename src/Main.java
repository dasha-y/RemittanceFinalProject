import domain.Transfer;
import service.OperationList;
import service.TransferService;
import util.FileUtils;
import util.ReportUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {

        Random random = new Random();

        File file = new File("src/resources/input/database.txt");
        FileWriter writer = new FileWriter(file);
        for (int i = 1; i < 10; i++) {
//                String accountNumber = String.format("%05d-%05d", random.nextInt(100000) + 1, random.nextInt(100000)+1);
//                int money = random.nextInt(100);
            String accountNumber = "00005-0001" + i;
            int money = 100;
            writer.write(accountNumber + " " + money + "\n");
        }
        writer.close();


        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите путь к каталогу, который содержит файлы для парсинга:");
        String directoryPath = scanner.nextLine();
        File directory = new File(directoryPath);
        if (!directory.isDirectory()) {
            System.out.println("Указанный путь не является директорией.");
            return;
        }

        File[] inputFiles = FileUtils.getFilesWithExtension(directory, "txt");
        if (inputFiles.length == 0 || inputFiles == null) {
            System.out.println("Входные файлы не найдены");
            return;
        }
        File outputDir = new File("src/resources/output/report.txt");
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        TransferService transferService = new TransferService();


        List<Transfer> transfers = new ArrayList<>() ;

        OperationList operationList = new OperationList (transfers);
        while (true) {
            System.out.println("Выберите операцию: ");
            System.out.println("1 - Парсинг файлов");
            System.out.println("2 - Вывод списка всех операций");
            System.out.println("3 - Выход");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("Выполняется парсинг файлов...");
                    for (File inputFile : inputFiles) {

                        List<String> lines = FileUtils.readLines(inputFile);
                        List<String> l = FileUtils.readLines(file);
                        String fileName = inputFile.getName();
                        File outputFile = new File(outputDir, fileName);

                        if (transferService.processFile(lines, outputFile, l, file)) {
                            String reportContent = new Date() + " - Файл " + fileName + " - успешно обработан\n";
                            ReportUtils.createReportFile(new File(outputDir, "report.txt"), reportContent);
                        } else {
                            String reportContent = new Date() + " - Файл " + fileName + " - ошибка во время обработки\n";
                            ReportUtils.createReportFile(new File(outputDir, "report.txt"), reportContent);
                        }

                    }

                    System.out.println("Парсинг файлов завершен. Результаты парсинга будет находится в папке output после завершения программы");
                    break;
                case 2:
                    System.out.println("Вывод списка всех операций: ");
                    operationList.print();
                    break;
                case 3:
                    System.out.println("Выход.");
                    return;
                default:
                    System.out.println("Неверный выбор.");
                    break;
            }
        }
        //scanner.close();

    }
}