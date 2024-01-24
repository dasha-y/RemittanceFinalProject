import domain.Transfer;
import exception.ErrorMessages;
import exception.MoneyTransferException;
import service.Constants;
import service.OperationList;
import service.TransferService;
import util.FileUtils;
import util.ReportUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException, MoneyTransferException {

        Random random = new Random();

        File file = new File(Constants.BANKS_ACCOUNTS_FILE_PATH);
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
        System.out.println(Constants.FILE_PATH_REQUEST);
        String directoryPath = scanner.nextLine();
        File directory = new File(directoryPath);
        if (!directory.isDirectory()) {
            System.out.println(ErrorMessages.FILE_PARSING_DIRECTORY_ERROR_MESSAGE);
            return;
        }

        File[] inputFiles = FileUtils.getFilesWithExtension(directory, "txt");
        if (inputFiles.length == 0 || inputFiles == null) {
            System.out.println(ErrorMessages.FILE_PARSING_IS_EMPTY_ERROR_MESSAGE);
            return;
        }
        File outputDir = new File(Constants.REPORT_FILE_PATH);
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        TransferService transferService = new TransferService();


        List<Transfer> transfers = new ArrayList<>() ;

        OperationList operationList = new OperationList (transfers);
        while (true) {
            System.out.println("Выберите операцию: ");
            System.out.println(OperationList.SELECT_FILE_PARSING);
            System.out.println(OperationList.SELECT_OPERATION_LIST);
            System.out.println(OperationList.SELECT_EXIT);
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    System.out.println(OperationList.PARSING);
                    for (File inputFile : inputFiles) {

                        List<String> lines = FileUtils.readLines(inputFile);
                        List<String> l = FileUtils.readLines(file);
                        String fileName = inputFile.getName();
                        File outputFile = new File(outputDir, fileName);

                        if (transferService.processFile(lines, outputFile, l, file)) {
                            String reportContent = LocalDate.now() + " - Файл " + fileName + Constants.SUCCESSFUL_PROCESSING;
                            ReportUtils.createReportFile(new File(outputDir, "report.txt"), reportContent);
                        } else {
                            String reportContent = LocalDate.now() + " - Файл " + fileName + ErrorMessages.FILE_PARSING_ERROR_MESSAGE;
                            ReportUtils.createReportFile(new File(outputDir, "report.txt"), reportContent);
                        }

                    }

                    System.out.println(OperationList.RESULT);
                    break;
                case 2:
                    System.out.println(OperationList.OPERATION_LIST);
                    operationList.print();
                    break;
                case 3:
                    System.out.println(OperationList.EXIT);
                    return;
                default:
                    System.out.println(ErrorMessages.WRONG_CHOICE_ERROR_MESSAGE);
                    break;
            }
        }

    }
}