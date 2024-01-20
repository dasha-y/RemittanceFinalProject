package domain;

public class Transfer {
    private int amount;
    private String sender;
    private String recipient;

    public Transfer( String sender, String recipient, int amount) {
        this.amount = amount;
        this.sender = sender;
        this.recipient = recipient;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "amount=" + amount +
                ", sender=" + sender +
                ", recipient=" + recipient +
                '}';
    }
}
