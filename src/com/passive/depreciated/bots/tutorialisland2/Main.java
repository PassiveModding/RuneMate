package com.runemate.passive.bots.tutorialisland2;

import com.runemate.game.api.client.embeddable.EmbeddableUI;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.TaskBot;
import com.runemate.passive.bots.tutorialisland2.ui.UIBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

public class Main extends TaskBot implements EmbeddableUI {
    public BotInformationStore store;
    private SimpleObjectProperty<Node> botInterfaceProperty;
    public UIBase configUI;

    public Main(){
        setEmbeddableUI(this);
    }

    @Override
    public void onPause(){
        store.counter.stop();
    }

    @Override
    public void onResume(){
        store.counter.start();
    }

    @Override
    public void onStop(){
        runAllTime = false;
        store.run = false;
        Execution.delay(12000);
        this.store = new BotInformationStore();
    }

    public boolean runAllTime = false;
    public Thread t1;

    @Override
    public void onStart (String... args){
        System.out.println("Running bot");
        setLoopDelay(100, 2000);
        store = new BotInformationStore();
        add();

        runAllTime = true;
        t1 = new Thread(() -> updateUIContents());
        t1.start();
    }


    public void updateUIContents() {
        try {
            while (runAllTime){
                store.Update_Counts();
                Thread.sleep(250);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("InterruptedException caught in lambda", e);
        }
    }


    @Override
    public ObjectProperty<? extends Node> botInterfaceProperty() {
        if (botInterfaceProperty == null) {
            botInterfaceProperty = new SimpleObjectProperty<>(configUI = new UIBase(this));
        }
        return botInterfaceProperty;
    }
}
