/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leikr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import org.mini2Dx.core.assets.FallbackFileHandleResolver;
import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.BasicGameScreen;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.ui.UiContainer;
import org.mini2Dx.ui.UiThemeLoader;
import org.mini2Dx.ui.style.UiTheme;

/**
 *
 * @author tor
 */
public class MenuScreen extends BasicGameScreen {

    public static int ID = 0;

    AssetManager assetManager;
    UiContainer uiContainer;

    @Override
    public void initialise(GameContainer gc) {
        //Create fallback file resolver so we can use the default mini2Dx-ui theme
        FileHandleResolver fileHandleResolver = new FallbackFileHandleResolver(new ClasspathFileHandleResolver(), new InternalFileHandleResolver());

        //Create asset manager for loading resources
        assetManager = new AssetManager(fileHandleResolver);

        //Add mini2Dx-ui theme loader
        assetManager.setLoader(UiTheme.class, new UiThemeLoader(fileHandleResolver));

        //Load default theme
        assetManager.load(UiTheme.DEFAULT_THEME_FILENAME, UiTheme.class);

        uiContainer = new UiContainer(gc, assetManager);
        
        Gdx.input.setInputProcessor(uiContainer);
    }

    @Override
    public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta) {
        if(!assetManager.update()) {
                //Wait for asset manager to finish loading assets
                return;
        }
        if(!uiContainer.isThemeApplied()) {
                uiContainer.setTheme(assetManager.get(UiTheme.DEFAULT_THEME_FILENAME, UiTheme.class));
        }
        uiContainer.update(delta);
    }

    @Override
    public void interpolate(GameContainer gc, float alpha) {
        uiContainer.interpolate(alpha);
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
    }

    @Override
    public int getId() {
        return ID;
    }

}
