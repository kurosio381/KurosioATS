package kurosio.kurosioats.data;

public class WarpData {

    private final String name;
    private final String world;

    private final double x;
    private final double y;
    private final double z;

    private final float yaw;
    private final float pitch;


    public WarpData(
            String name,
            String world,
            double x,
            double y,
            double z,
            float yaw,
            float pitch
    ) {

        this.name = name;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;

    }


    public String getName() {
        return name;
    }


    public String getWorld() {
        return world;
    }


    public double getX() {
        return x;
    }


    public double getY() {
        return y;
    }


    public double getZ() {
        return z;
    }


    public float getYaw() {
        return yaw;
    }


    public float getPitch() {
        return pitch;
    }

}