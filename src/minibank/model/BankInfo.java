package minibank.model;

import java.io.Serializable;

public record BankInfo(String name, String branch) implements Serializable {
    private static final long serialVersionUID = 1L;

    public String getFormattedHeader() {
        return "========================================================\n" +
               "           " + name + " - " + branch + " Branch\n" +
               "          Next-Gen Object Oriented Banking System\n" +
               "========================================================";
    }
}
