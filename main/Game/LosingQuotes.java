package Game;

import java.util.Random;

public class LosingQuotes {

    static String[] quotes = new String[] {
            "It's only game, why you have to be mad? - Ilya Bryzgalov",
            "Maybe you should try checkers instead. - Magnus Carlsen",
            "First time?",
            "The restart option was there for a reason...",
            "I should not have done that.. I should NOT have done that! - You, just a minute ago",
            "They say you learn from your mistakes; you're about to become a genius.",
            "By design it's impossible to have a negative ELO rating. You might have to be an exception.",
            "I hate to break it to you, but you've been 'outsmarted' by a bunch of lines and circles.",
            "Congratulations! You've earned yourself a spot in the 'Hall of Lame'.",
            "It seems like you've taken a detour on the 'highway to victory'.",
            "Your gameplay was so 'brilliant', even the AI got confused.",
            "The good news is you're getting really good at setting up my winning moves. The bad news is you're still losing.",
            "Congratulations on your impeccable strategy of 'strategic incompetence'!",
            "You know what they say, losing is just winning in disguise. Keep up the good work!"
        };

    public static String getRandomQuote() {
        Random rand = new Random();
        return quotes[rand.nextInt(quotes.length)];
    }

}
