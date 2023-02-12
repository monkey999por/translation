package tools.cmd;

public class InnerCommandBean {

    private String command;
    private String description;

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public InnerCommandBean(String command, String description) {
        this.command = command;
        this.description = description;
    }


}
