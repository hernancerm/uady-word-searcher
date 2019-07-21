package hercerm.iindices.core;

import java.util.Scanner;

/**
 * Not formally a REPL, but this class behaves as one. It contains the flow control
 * (loop) and input reading side of a REPL. Evaluation and printing is done by
 * {@link hercerm.iindices.core.REPLCommands}.
 *
 * @version 24.05.19
 * @author Hernán J. Cervera Manzanilla
 */
public class REPL {

    private final REPLCommands commands;

    /**
     * Constructs a REPL with the provided implementation for its
     * supported command set.
     *
     * @param commands implementation of supported commands.
     */
    public REPL(REPLCommands commands) {
        this.commands = commands;
    }

    /**
     * Starts the REPL. As expected, its UI is text based.
     */
    public void start() {
        Scanner input = new Scanner(System.in);

        System.out.println("[WordSearcher v1. | Ready to process commands.]");
        System.out.println("Type \'help\' for a list of commands.");
        System.out.println();

        while(true) {
            System.out.print("> ");

            String command = input.nextLine();
            switch(command.split(" ")[0]) {
                case "add":
                    commands.add(command);
                    break;
                case "search":
                    commands.search(command);
                    break;
                case "clear":
                    commands.clear();
                    break;
                case "exit":
                    commands.exit();
                    return;
                case "help":
                    commands.help();
                    break;
                default:
                    commands.unsupported();
                    break;
            }
        }
    }
}
