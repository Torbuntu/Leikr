package leikr.customProperties;

import org.mini2Dx.core.assets.AssetManager;
import org.mini2Dx.core.graphics.Texture;


/**
 *
 * @author tor
 */
public class ChipData {

    public CustomProgramProperties cpp;
    private final String ICON_PATH;
    private final String directory;
    AssetManager assetManager;

    public ChipData(String programTitle, AssetManager am) {
        this.assetManager = am;
        directory = programTitle;
        cpp = new CustomProgramProperties("Programs/" + programTitle);
        ICON_PATH = "Programs/" + programTitle + "/Art/icon.png";
        assetManager.load(ICON_PATH, Texture.class);
        assetManager.finishLoading();
    }

    public ChipData(String title, String auth, String type, String vers, int pla, String about, AssetManager am, String directory) {
        cpp = new CustomProgramProperties(title, auth, type, vers, pla, about);
        this.assetManager = am;
        this.directory = directory;
        ICON_PATH = "./Data/Images/start_new.png";
        assetManager.load(ICON_PATH, Texture.class);
        assetManager.finishLoading();
    }

    public Texture getIcon() {
        return assetManager.get(ICON_PATH, Texture.class);
    }

    public String getTitle() {
        return cpp.TITLE;
    }

    public String getAuthor() {
        return cpp.AUTHOR;
    }

    public String getType() {
        return cpp.TYPE;
    }

    public String getVersion() {
        return cpp.VERSION;
    }

    public int getPlayers() {
        return cpp.PLAYERS;
    }

    public String getAbout() {
        return cpp.ABOUT;
    }

    public String getDirectory() {
        return directory;
    }
}
