package service;

import domain.Transfer;

import java.io.*;
import java.util.*;

public class TransferService {
    private List<Transfer> transfers;

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

    public boolean processFile(List<String> lines, File outputFile, List<String> l, File file) {
        Map<String, Integer> accounts = new HashMap<>();

        try (PrintWriter writer = new PrintWriter(outputFile); BufferedReader reader = new BufferedReader(new FileReader(file))) {
//                String[] parts1=null;
            File newDatabase = new File("src/resources/input/database.txt");

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
                        writer.println(new Date() + " - Перевод выполнен успешно: " + transfer);
                        //fileWriter.write("что то написали");
                        accounts.put(senderNumber, accounts.get(senderNumber) - amount);
                        accounts.put(recipientNumber, accounts.get(recipientNumber) + amount);
                        // Переписываем файл
                        String fileLine;
                        StringBuilder newFileContent = new StringBuilder();
                        while ((fileLine = reader.readLine()) != null) {
                            String[] fileParts = fileLine.split(" ");
                            String fileAccountNumber = fileParts[0];
                            int fileBalance = Integer.parseInt(fileParts[1]);

                            // Если номер счета уже использовался, изменяем сумму на счету
                            if (fileAccountNumber.equals(senderNumber)) {
                                accounts.put(senderNumber, accounts.get(senderNumber) - amount);
                                //fileBalance -= amount;
                                //fileWriter.write(fileAccountNumber+" "+fileBalance);
                            }
                            else if (fileAccountNumber.equals(recipientNumber)) {
                                accounts.put(recipientNumber, accounts.get(recipientNumber) + amount);
                                //fileBalance += amount;
                                //fileWriter.write(fileAccountNumber+" "+fileBalance);
                            }

                            else{
                                //fileWriter.write(fileLine);
                            }
                            //bufferedWriter.write(newFileContent.toString());
                            //bufferedWriter.flush();
//                            if(file.delete()){
//                                file.createNewFile();
//                            }
//                            if (!file.delete()) {
//                                System.out.println("Не удалось удалить файл");
//
//                            }


                        }

//                                for (int i = 0; i < parts1.length; i++) {
//                                    if (parts1[i].equals(senderNumber) ){
//                                        bufferedWriter.write(parts1[i] + " " + (accounts.get(senderNumber) - amount));
//                                    } else if ( parts1[i].equals(recipientNumber)) {
//                                        bufferedWriter.write(parts1[i] + " " + (accounts.get(recipientNumber) + amount));
//                                    } else{
//                                        bufferedWriter.write(line);
//                                    }
//                                }
                        //writer2.write(accounts.get(senderNumber) + " "+ accounts.get(amount));
                        //writer2.write(accounts.get(recipientNumber) + " "+ accounts.get(amount));

                    } else {
                        writer.println(new Date() + " - Ошибка при переводе: " + transfer);
                    }
                }

            }
            //fileWriter.write("\nend of file");

            if (!newDatabase.renameTo(file)) {
                System.out.println("Не удалось переименовать файл");
            }

            FileWriter fileWriter = new FileWriter(newDatabase);
            for (Map.Entry<String, Integer> entry: accounts.entrySet()){
                fileWriter.write(entry.getKey() + " " + entry.getValue()+"\n");

            }
            fileWriter.flush();
            fileWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }



    }

}
