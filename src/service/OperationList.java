package service;

import domain.Transfer;

import java.util.List;

public class OperationList {
    private List<Transfer> transfers;
    public static final String OPERATION_LIST = "Вывод списка всех операций: ";
    public static final String SELECT_FILE_PARSING = "1 - Парсинг файлов";
    public static final String SELECT_OPERATION_LIST = "2 - Вывод списка всех операций";
    public static final String SELECT_EXIT = "3 - Выход";
    public static final String EXIT = " Выход";
    public static final String PARSING = "Выполняется парсинг файлов...";
    public static final String RESULT = "Парсинг файлов завершен. Результаты парсинга будет находится в папке output после завершения программы";
    public OperationList(List<Transfer> transfers) {
        this.transfers = transfers;
    }

    public void print() {
        System.out.println("Список всех операций:");
        for (Transfer transfer : transfers) {
            String sender = transfer.getSender();
            String receiver = transfer.getRecipient();
            int score = transfer.getAmount();
            System.out.println(sender + " -> " + receiver + ": " + score);
        }
    }

    public String getOperations() {
        StringBuilder builder = new StringBuilder();
        for (Transfer transfer : transfers) {
            String sender = transfer.getSender();
            String receiver = transfer.getRecipient();
            int score = transfer.getAmount();
            builder.append(sender).append(" -> ").append(receiver).append(": ").append(score).append("\n");
        }
        return builder.toString();
    }
}
