import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class WeatherApp extends JFrame {

    private JTextField cityField;

    // Auto-suggestion
    private JComboBox<String> suggestionBox;
    private DefaultComboBoxModel<String> model;

    private JLabel resultLabel;
    private JLabel iconLabel;
    private JTextArea historyArea;
    private JComboBox<String> unitBox;

    private JLabel backgroundLabel;
    private JPanel overlayPanel;

    public WeatherApp() {

        setTitle("Weather Information App");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // =========================
        // BACKGROUND LAYER
        // =========================
        backgroundLabel = new JLabel();
        backgroundLabel.setLayout(new BorderLayout());

        setContentPane(backgroundLabel);

        // Default background
        setBackgroundImage("images/day.jpg");

        // =========================
        // OVERLAY PANEL
        // =========================
        overlayPanel = new JPanel(new BorderLayout());
        overlayPanel.setOpaque(false);

        backgroundLabel.add(overlayPanel, BorderLayout.CENTER);

        // =========================
        // TITLE SECTION
        // =========================
        JLabel titleLabel = new JLabel(
                "Real-Time Weather Information App",
                JLabel.CENTER
        );

        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        titleLabel.setBorder(
                BorderFactory.createEmptyBorder(15, 10, 15, 10)
        );

        overlayPanel.add(titleLabel, BorderLayout.NORTH);

        // =========================
        // CENTER WEATHER PANEL
        // =========================
        JPanel centerPanel = new JPanel(new BorderLayout());

        centerPanel.setBackground(
                new Color(255, 255, 255, 210)
        );

        centerPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        resultLabel = new JLabel(
                "Enter a city name to view weather",
                JLabel.CENTER
        );

        resultLabel.setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        resultLabel.setForeground(Color.BLACK);

        iconLabel = new JLabel();
        iconLabel.setHorizontalAlignment(JLabel.CENTER);

        centerPanel.add(resultLabel, BorderLayout.CENTER);
        centerPanel.add(iconLabel, BorderLayout.EAST);

        overlayPanel.add(centerPanel, BorderLayout.CENTER);

        // =========================
        // SEARCH HISTORY PANEL
        // =========================
        historyArea = new JTextArea(15, 15);

        historyArea.setEditable(false);

        historyArea.setFont(
                new Font("Monospaced", Font.PLAIN, 13)
        );

        historyArea.setBackground(
                new Color(255, 255, 255, 220)
        );

        historyArea.setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(historyArea);

        JPanel historyPanel = new JPanel(new BorderLayout());

        historyPanel.setBackground(
                new Color(255, 255, 255, 200)
        );

        JLabel historyLabel = new JLabel(
                "Search History",
                JLabel.CENTER
        );

        historyLabel.setForeground(Color.BLACK);

        historyPanel.add(historyLabel, BorderLayout.NORTH);
        historyPanel.add(scrollPane, BorderLayout.CENTER);

        overlayPanel.add(historyPanel, BorderLayout.WEST);

        // =========================
        // INPUT PANEL
        // =========================
        JPanel inputPanel = new JPanel();

        inputPanel.setBackground(
                new Color(255, 255, 255, 200)
        );

        JLabel cityLabel = new JLabel("City:");
        cityLabel.setForeground(Color.BLACK);

        // =========================
        // AUTO-SUGGESTION SETUP
        // =========================
        model = new DefaultComboBoxModel<>();

        suggestionBox = new JComboBox<>(model);

        suggestionBox.setEditable(true);

        // Get editable text field
        cityField = (JTextField)
                suggestionBox.getEditor().getEditorComponent();

        cityField.setColumns(15);

        // Suggested cities
        String[] cities = {
                "London",
                "New York",
                "Los Angeles",
                "Tokyo",
                "Paris",
                "Berlin",
                "Lagos",
                "Dubai",
                "Toronto",
                "Port Harcourt",
                "Abuja",
                "Cape Town",
                "Sydney"
        };

        for (String city : cities) {
            model.addElement(city);
        }

        JButton searchButton = new JButton("Get Weather");

        searchButton.setBackground(
                new Color(0, 153, 255)
        );

        searchButton.setForeground(Color.WHITE);

        searchButton.setFocusPainted(false);

        String[] units = {"Celsius", "Fahrenheit"};

        unitBox = new JComboBox<>(units);

        inputPanel.add(cityLabel);

        // ADD COMBO BOX NOT cityField
        inputPanel.add(suggestionBox);

        inputPanel.add(unitBox);
        inputPanel.add(searchButton);

        overlayPanel.add(inputPanel, BorderLayout.SOUTH);

        // =========================
        // BUTTON ACTION
        // =========================
        searchButton.addActionListener(e -> searchWeather());

        // ENTER KEY SUPPORT
        cityField.addActionListener(e -> searchWeather());

        setVisible(true);
    }

    // =========================
    // SEARCH WEATHER
    // =========================
    private void searchWeather() {

        String city = cityField.getText().trim();

        if (city.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a city name.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // Loading message
        resultLabel.setText("Loading weather data...");

        WeatherData data = WeatherService.getWeather(city);

        if (data != null) {

            double temperature = data.getTemperature();

            String unitSymbol = "°C";

            // Fahrenheit conversion
            if (unitBox.getSelectedItem().equals("Fahrenheit")) {

                temperature = (temperature * 9 / 5) + 32;

                unitSymbol = "°F";
            }

            // Display weather info
            resultLabel.setText(
                    "<html><center>"
                            + "<h2>" + city + "</h2>"
                            + "Temperature: "
                            + String.format("%.2f", temperature)
                            + unitSymbol + "<br><br>"

                            + "Humidity: "
                            + data.getHumidity()
                            + "%<br><br>"

                            + "Wind Speed: "
                            + data.getWindSpeed()
                            + " m/s<br><br>"

                            + "Condition: "
                            + data.getCondition()

                            + "</center></html>"
            );

            // Update weather icon
            updateWeatherIcon(data.getCondition());

            // Dynamic background
            updateBackground();

            // Add history
            historyArea.append(
                    city
                            + " searched at "
                            + LocalDateTime.now()
                            + "\n"
            );

        } else {

            resultLabel.setText("Weather data not available.");

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to retrieve weather data.\nCheck internet or city name.",
                    "Weather Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================
    // WEATHER ICON
    // =========================
    private void updateWeatherIcon(String condition) {

        String c = condition.toLowerCase();

        String path = "images/cloudy.png";

        if (c.contains("clear")) {

            path = "images/sunny.png";

        } else if (c.contains("cloud")) {

            path = "images/cloudy.png";

        } else if (c.contains("rain")) {

            path = "images/rain.png";

        } else if (c.contains("thunder")) {

            path = "images/thunderstorm.png";

        } else if (c.contains("snow")) {

            path = "images/snow.png";

        } else if (
                c.contains("mist")
                        || c.contains("fog")
                        || c.contains("haze")
        ) {

            path = "images/mist.png";
        }

        try {

            ImageIcon icon = new ImageIcon(path);

            Image scaledImage = icon.getImage().getScaledInstance(
                    100,
                    100,
                    Image.SCALE_SMOOTH
            );

            iconLabel.setIcon(
                    new ImageIcon(scaledImage)
            );

        } catch (Exception e) {

            System.out.println(
                    "Icon loading failed: " + e.getMessage()
            );
        }
    }

    // =========================
    // DYNAMIC BACKGROUND
    // =========================
    private void updateBackground() {

        int hour = LocalTime.now().getHour();

        // Night background
        if (hour >= 18 || hour <= 5) {

            setBackgroundImage("images/night.jpg");

        } else {

            setBackgroundImage("images/day.jpg");
        }
    }

    // =========================
    // SET BACKGROUND IMAGE
    // =========================
    private void setBackgroundImage(String imagePath) {

        try {

            ImageIcon backgroundIcon = new ImageIcon(imagePath);

            Image img = backgroundIcon.getImage().getScaledInstance(
                    700,
                    500,
                    Image.SCALE_SMOOTH
            );

            backgroundLabel.setIcon(
                    new ImageIcon(img)
            );

            revalidate();
            repaint();

        } catch (Exception e) {

            System.out.println(
                    "Background image error: "
                            + e.getMessage()
            );
        }
    }

    // =========================
    // MAIN METHOD
    // =========================
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {

                new WeatherApp();

            } catch (Exception e) {

                e.printStackTrace();
            }
        });
    }
}