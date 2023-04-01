package me.jordqn.wynnchat;

public enum Channel {
    ALL(""),
    PARTY("/p "),
    GUILD("/g ");

    private final String cmdPrefix;
    Channel(final String cmdPrefix) { this.cmdPrefix = cmdPrefix; }

    public String getCommandPrefix() {
        return cmdPrefix;
    }
}
