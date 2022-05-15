package Logic.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class SystemTerminal extends Actor {

    private boolean visible = false;
private Table table;
    public SystemTerminal(int x, int y, Texture t) {


    }


    public void setVisible(boolean visible) {
       table.setVisible(visible);
    }
}
