package service;

import domain.Transfer;

import java.util.List;

public class OperationList {
    private List<Transfer> transfers;

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
