package service;

import domain.Transfer;
import exception.ErrorMessages;
import exception.MoneyTransferException;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TransferService implements Constants{
    private List<Transfer> transfers;
    private static final String BANKS_ACCOUNTS_FILE= Constants.BANKS_ACCOUNTS_FILE_PATH;
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss "));

    public TransferService() {
        transfers = new ArrayList<>();
    }


    public boolean transfer(Transfer transfer) {

        if (transfer.getSender().equals(transfer.getRecipient())) {
            return false;
        }

        if (transfer.getAmount() <= 0 ) {
            return false;
        }

        transfers.add(transfer);
        return true;
    }

    public boolean processFile(List<String> lines, File outputFile, List<String> l, File file) throws MoneyTransferException {
        Map<String, Integer> accounts = new HashMap<>();

        try (PrintWriter writer = new PrintWriter(outputFile); BufferedReader reader = new BufferedReader(new FileReader(file))) {
            File newDatabase = new File(BANKS_ACCOUNTS_FILE);

            for (String line : l) {
                String[] parts1 = line.split(" ");
                String account = parts1[0];
                int amountFirst = Integer.parseInt(parts1[1]);
                accounts.put(account, amountFirst);
            }
            for (String line : lines) {
                String[] parts = line.split(" ");

                String senderNumber = "";
                String recipientNumber = "" ;
                int amount = 0 ;
                Transfer transfer = null;
                if(parts.length == 3 && parts[0].matches("\\s*\\d{5}-\\d{5}") && parts[1].matches("\\d{5}-\\d{5}") && parts[2].matches("\\d+")){
                    senderNumber = parts[0] ;
                    recipientNumber = parts[1];
                    amount = Integer.parseInt(parts[2]);
                    transfer = new Transfer(senderNumber, recipientNumber, amount);
                }
                if (transfer != null) {
                    if (transfer(transfer) && accounts.containsKey(senderNumber) && accounts.containsKey(recipientNumber) && accounts.get(senderNumber) >= amount) {
                        writer.println(date + Constants.SUCCESSFUL_TRANSACTION + transfer);
                        accounts.put(senderNumber, accounts.get(senderNumber) - amount);
                        accounts.put(recipientNumber, accounts.get(recipientNumber) + amount);

                        String fileLine;
                        StringBuilder newFileContent = new StringBuilder();
                        while ((fileLine = reader.readLine()) != null) {
                            String[] fileParts = fileLine.split(" ");
                            String fileAccountNumber = fileParts[0];
                            int fileBalance = Integer.parseInt(fileParts[1]);

                            if (fileAccountNumber.equals(senderNumber)) {
                                accounts.put(senderNumber, accounts.get(senderNumber) - amount);

                            }
                            else if (fileAccountNumber.equals(recipientNumber)) {
                                accounts.put(recipientNumber, accounts.get(recipientNumber) + amount);
                            }
                        }
                    } else {
                        writer.println(date + ErrorMessages.REPORT_TRANSACTION_ERROR_MESSAGE + transfer);
                    }
                }
            }

            if (!newDatabase.renameTo(file)) {
                System.out.println(ErrorMessages.RENAME_ERROR_MESSAGE);
            }

            FileWriter fileWriter = new FileWriter(newDatabase);
            for (Map.Entry<String, Integer> entry: accounts.entrySet()){
                fileWriter.write(entry.getKey() + " " + entry.getValue()+"\n");

            }
            fileWriter.flush();
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new MoneyTransferException(ErrorMessages.PARSING_ERROR_MESSAGE, e);

        }



    }

}
