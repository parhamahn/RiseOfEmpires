package helpers;

import java.util.List;
import java.util.Random;

public class RandomNameGenerator {
    private static String[] names = new String[]{"Animals", "Trashs", "Cheeks", "Thieves", "Kids"};
    private static String[] verbs = new String[]{"Red", "Black", "Ugly", "Cool", "Tiny", "Great", "Little"};

    public static String generateRandomName() {
        Random random = new Random();
        String name = names[random.nextInt(names.length)];
        String verb = verbs[random.nextInt(verbs.length)];
        return verb + " " + name;
    }
}
