package org.javacord.examplebot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.util.logging.FallbackLoggerConfiguration;
import org.javacord.examplebot.command.CopyAvatarCommand;
import org.javacord.examplebot.command.UserInfoCommand;

public class Main {

    private static Logger logger = LogManager.getLogger(Main.class);

    /**
     * The entrance point of our program.
     *
     * @param args The arguments for the program. The first element should be the bot's token.
     */
    public static void main(String[] args) {

    	if(!(args.length==2 && "-t".equals(args[0]) && args[1].length()>0))
    	{
    		System.err.println("Parameter: -t [token]");
    		System.exit(1);
    	}    
    	
        // Enable debugging, if no slf4j logger was found
        FallbackLoggerConfiguration.setDebug(true);

        // The token is the first argument of the program
        String token = args[1];

        // We login blocking, just because it is simpler and doesn't matter here
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

        // Print the invite url of the bot
        logger.info("You can invite me by using the following url: " + api.createBotInvite());

        // Add listeners
        api.addMessageCreateListener(new CopyAvatarCommand());
        api.addMessageCreateListener(new UserInfoCommand());

        // Log a message, if the bot joined or left a server
        api.addServerJoinListener(event -> logger.info("Joined server " + event.getServer().getName()));
        api.addServerLeaveListener(event -> logger.info("Left server " + event.getServer().getName()));
    }

}