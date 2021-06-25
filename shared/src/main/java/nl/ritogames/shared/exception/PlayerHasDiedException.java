package nl.ritogames.shared.exception;

public class PlayerHasDiedException extends Exception {
    private String individualId;

    public String getIndividualId() {
        return individualId;
    }

    public void setIndividualId(String individualId) {
        this.individualId = individualId;
    }
}
