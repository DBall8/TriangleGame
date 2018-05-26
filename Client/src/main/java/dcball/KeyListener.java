package dcball;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class KeyListener {

    private boolean up = false;
    private boolean down = false;
    private float mousex = 0;
    private float mousey = 0;

    public float getMouseX() {
        return mousex;
    }

    public float getMouseY() {
        return mousey;
    }

    public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }


    public KeyListener(Scene scene){
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                keyDown(event);
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                keyUp(event);
            }
        });

        scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseMoved(event);
            }
        });
    }

    private void mouseMoved(MouseEvent me){
        mousex = (float)me.getSceneX();
        mousey = (float)me.getSceneY();
    }

    private void keyDown(KeyEvent ke){
        switch(ke.getCode()){
            case W:
                if(!up) up = true;
                break;
            case S:
                if(!down) down = true;
                break;
        }
    }

    private void keyUp(KeyEvent ke){
        switch(ke.getCode()){
            case W:
                up = false;
                break;
            case S:
                down = false;
                break;
        }
    }

}
