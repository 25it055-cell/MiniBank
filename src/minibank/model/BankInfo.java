package minibank.model;

import java.io.Serializable;

/**
 * Record representing Bank Information (Practical 1 Part B).
 * Java auto-generates constructor, getters (name(), branch()), equals, hashCode and toString.
 */
public record BankInfo(String name, String branch) implements Serializable {
    private static final long serialVersionUID = 1L;

    public String getFormattedHeader() {
        return "========================================================\n" +
               "           " + name + " - " + branch + " Branch\n" +
               "          Next-Gen Object Oriented Banking System\n" +
               "========================================================";
    }
}

