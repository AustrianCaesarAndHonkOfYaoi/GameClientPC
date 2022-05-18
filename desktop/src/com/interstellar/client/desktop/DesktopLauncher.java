package com.interstellar.client.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.interstellar.client.MainGame;

import java.io.File;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        Graphics.DisplayMode displayMode = LwjglApplicationConfiguration.getDesktopDisplayMode();

        config.height = 1080;
        config.width = 1920;
        config.y = 0;
        config.x = 0;
        config.resizable = false;
        config.title = "Interstellar";
        config.addIcon("Icon.jpg", Files.FileType.Internal);


        new LwjglApplication(new MainGame(), config);
    }
}
