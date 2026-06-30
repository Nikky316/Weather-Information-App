import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SearchHistory {

    // Stores search history entries
    private static ArrayList<String> history =
            new ArrayList<>();

    // Date & time formatter
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // =========================
    // ADD SEARCH ENTRY
    // =========================
    public static void add(String city) {

        String entry =
                city + " searched at "
                        + LocalDateTime.now().format(formatter);

        history.add(entry);
    }

    // =========================
    // GET HISTORY
    // =========================
    public static ArrayList<String> getHistory() {

        return history;
    }

    // =========================
    // CLEAR HISTORY
    // =========================
    public static void clearHistory() {

        history.clear();
    }
}