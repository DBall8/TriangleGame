package dcball.Singletons;

public class Settings {

    private final boolean DEBUG = false;

    public final int FRAMERATE = 60;
    public int WIDTH = 0;
    public int HEIGHT = 0;

    private Settings(){}

    private static class SingeletonHelper{
        private static final Settings _instance = new Settings();
    }

    public static Settings getInstance(){ return SingeletonHelper._instance; }

    public static boolean isDebug(){ return  SingeletonHelper._instance.DEBUG; }
}
