package kurosio.kurosioats.data;

public class ResultPlayer {

    private final String mcid;
    private final String wish;
    private final String location;
    private final boolean win;

    public ResultPlayer(String mcid, String wish, String location, boolean win) {
        this.mcid = mcid;
        this.wish = wish;
        this.location = location;
        this.win = win;
    }

    public String getMcid() {
        return mcid;
    }

    public String getWish() {
        return wish;
    }

    public String getLocation() {
        return location;
    }

    public boolean isWin() {
        return win;
    }

}