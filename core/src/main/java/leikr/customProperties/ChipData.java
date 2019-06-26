package leikr.customProperties;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

/**
 *
 * @author tor
 */
public class ChipData {

    public CustomProgramProperties cpp;
    private final String ICON_PATH;
    AssetManager assetManager;

    public ChipData(String programTitle, AssetManager am) {
        this.assetManager = am;
        cpp = new CustomProgramProperties(programTitle);
        ICON_PATH = programTitle + "/Art/icon.png";
        assetManager.load(ICON_PATH, Texture.class);
        assetManager.finishLoading();
    }

    public ChipData(String title, String auth, String type, String vers, int pla, String about, AssetManager am) {
        cpp = new CustomProgramProperties(title, auth, type, vers, pla, about);
        this.assetManager = am;
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

    public String getPlayers() {
        return cpp.PLAYERS;
    }

    public String getAbout() {
        return cpp.ABOUT;
    }
}
