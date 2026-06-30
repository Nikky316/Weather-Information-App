public class WeatherData {

    // =========================
    // WEATHER DATA FIELDS
    // =========================
    private double temperature;
    private int humidity;
    private double windSpeed;
    private String condition;

    // OpenWeather icon code
    private String iconCode;

    // =========================
    // CONSTRUCTOR
    // =========================
    public WeatherData(
            double temperature,
            int humidity,
            double windSpeed,
            String condition,
            String iconCode
    ) {

        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.condition = condition;
        this.iconCode = iconCode;
    }

    // =========================
    // GETTERS
    // =========================
    public double getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public String getCondition() {
        return condition;
    }

    public String getIconCode() {
        return iconCode;
    }

    // =========================
    // SETTERS
    // =========================
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setIconCode(String iconCode) {
        this.iconCode = iconCode;
    }

    // =========================
    // toString METHOD
    // =========================
    @Override
    public String toString() {

        return "WeatherData {" +
                "temperature=" + temperature +
                ", humidity=" + humidity +
                ", windSpeed=" + windSpeed +
                ", condition='" + condition + '\'' +
                ", iconCode='" + iconCode + '\'' +
                '}';
    }
}