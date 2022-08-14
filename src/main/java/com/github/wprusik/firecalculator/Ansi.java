package com.github.wprusik.firecalculator;

public enum Ansi {
    RESET( "\u001B[0m"),
    BLACK("\u001B[30m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m"),
    CYAN("\u001B[36m"),
    WHITE("\u001B[37m"),

    BOLD("\u001B[1m"),
    UNDERLINE("\u001b[4m"),

    FIRE_LOGO("""
             ▄▄▄▄▄▄▄▄▄▄▄  ▄▄▄▄▄▄▄▄▄▄▄     ▄▄▄▄▄▄▄▄▄▄▄     ▄▄▄▄▄▄▄▄▄▄▄\s
            ▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌   ▐░░░░░░░░░░░▌   ▐░░░░░░░░░░░▌
            ▐░█▀▀▀▀▀▀▀▀▀  ▀▀▀▀█░█▀▀▀▀    ▐░█▀▀▀▀▀▀▀█░▌   ▐░█▀▀▀▀▀▀▀▀▀\s
            ▐░▌               ▐░▌        ▐░▌       ▐░▌   ▐░▌         \s
            ▐░█▄▄▄▄▄▄▄▄▄      ▐░▌        ▐░█▄▄▄▄▄▄▄█░▌   ▐░█▄▄▄▄▄▄▄▄▄\s
            ▐░░░░░░░░░░░▌     ▐░▌        ▐░░░░░░░░░░░▌   ▐░░░░░░░░░░░▌
            ▐░█▀▀▀▀▀▀▀▀▀      ▐░▌        ▐░█▀▀▀▀█░█▀▀    ▐░█▀▀▀▀▀▀▀▀▀\s
            ▐░▌               ▐░▌        ▐░▌     ▐░▌     ▐░▌         \s
            ▐░▌ ▄         ▄▄▄▄█░█▄▄▄▄  ▄ ▐░▌      ▐░▌  ▄ ▐░█▄▄▄▄▄▄▄▄▄\s
            ▐░▌▐░▌       ▐░░░░░░░░░░░▌▐░▌▐░▌       ▐░▌▐░▌▐░░░░░░░░░░░▌
             ▀  ▀         ▀▀▀▀▀▀▀▀▀▀▀  ▀  ▀         ▀  ▀  ▀▀▀▀▀▀▀▀▀▀▀\s
                                                                     \s""".indent(11), false),

    FIRE_SUBLOGO("""
             ██████╗ █████╗ ██╗      ██████╗██╗   ██╗██╗      █████╗ ████████╗ ██████╗ ██████╗\s
            ██╔════╝██╔══██╗██║     ██╔════╝██║   ██║██║     ██╔══██╗╚══██╔══╝██╔═══██╗██╔══██╗
            ██║     ███████║██║     ██║     ██║   ██║██║     ███████║   ██║   ██║   ██║██████╔╝
            ██║     ██╔══██║██║     ██║     ██║   ██║██║     ██╔══██║   ██║   ██║   ██║██╔══██╗
            ╚██████╗██║  ██║███████╗╚██████╗╚██████╔╝███████╗██║  ██║   ██║   ╚██████╔╝██║  ██║
             ╚═════╝╚═╝  ╚═╝╚══════╝ ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝
                                                                                              \s""", false);

    private final String code;
    private final boolean requiresAnsiSupport;

    Ansi(String code) {
        this.code = code;
        this.requiresAnsiSupport = true;
    }

    Ansi(String code, boolean requiresAnsiSupport) {
        this.code = code;
        this.requiresAnsiSupport = requiresAnsiSupport;
    }

    public String code() {
        return Settings.ANSI_ENABLED || !requiresAnsiSupport ? code : "";
    }
}
