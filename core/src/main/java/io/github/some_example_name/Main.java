package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class Main extends Game {
    public Music musicaFundo;

    @Override
    public void create() {
        musicaFundo = Gdx.audio.newMusic(Gdx.files.internal("HappyFarmLolfi.mp3"));
        musicaFundo.setLooping(true);   
        musicaFundo.setVolume(0.5f);    
        musicaFundo.play();
        setScreen(new GameScreen());
    }

    @Override
    public void dispose() {
        musicaFundo.dispose();
    }

    @Override
    public void pause() {
        musicaFundo.pause();
    }

    @Override
    public void resume() {
        musicaFundo.play();
    }
}
