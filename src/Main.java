public class Main {

    public static void main(String[] args) {
        try {
            FilteringUtility filteringUtility = new FilteringUtility(args);
            filteringUtility.UtilityStart();
        } catch (Exception e) {
            System.err.println("--Ошибка " + e.getMessage());
            e.printStackTrace();
        }
    }
}