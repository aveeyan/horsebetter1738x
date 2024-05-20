public class Horse {
    private int horseID;
    private String horseName;
    private int horseNum;
    private String color;
    private float oddBets;

    public Horse(int horseID, String horseName, int horseNum, String color, float oddBets) {
        this.horseID = horseID;
        this.horseName = horseName;
        this.horseNum = horseNum;
        this.color = color;
        this.oddBets = oddBets;
    }

    public int getHorseID() {
        return horseID;
    }

    public String getHorseName() {
        return horseName;
    }

    public int getHorseNum() {
        return horseNum;
    }

    public String getColor() {
        return color.toLowerCase();
    }

    public String getPngPath() {
        return "./src/images/running-horse-" + getColor() + ".png";
    }

    public float getOddBets() {
        return oddBets;
    }

    @Override
    public String toString() {
        return "HorseNum: " + horseNum + ", HorseName: " + horseName + ", Odds: " + oddBets;
    }
}
