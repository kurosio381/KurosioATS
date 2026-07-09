package kurosio.kurosioats.data;

public class ReceptionPlayer {

    private final String mcid;
    private final String wish;

    public ReceptionPlayer(String mcid, String wish) {
        this.mcid = mcid;
        this.wish = wish;
    }

    public String getMcid() {
        return mcid;
    }

    public String getWish() {
        return wish;
    }

}