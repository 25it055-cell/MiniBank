package minibank.service;

public class BankSession implements AutoCloseable {
    private final String sessionId;
    private final String operator;
    private boolean active = true;

    public BankSession(String sessionId, String operator) {
        this.sessionId = sessionId;
        this.operator = operator;
        System.out.println("[SESSION OPEN] Secure banking session '" + sessionId + "' initiated by " + operator);
    }

    public void executeOperation(String operation) {
        if (!active) {
            throw new IllegalStateException("Session is already closed");
        }
        System.out.println("[SESSION OP] Executing: " + operation + " in session " + sessionId);
    }

    @Override
    public void close() {
        this.active = false;
        System.out.println("[SESSION CLOSE] Banking session '" + sessionId + "' cleanly closed. Resources released.");
    }

    public String getSessionId() {
        return sessionId;
    }
}
