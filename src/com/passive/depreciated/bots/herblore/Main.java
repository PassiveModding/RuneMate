package com.runemate.passive.bots.herblore;

import com.runemate.game.api.client.embeddable.EmbeddableUI;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.TaskBot;
import com.runemate.passive.bots.herblore.ui.UIBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

public class Main extends TaskBot implements EmbeddableUI {
    /*
    public String GrimyRanarrWeed = "grimy ranarr weed";
    public String RanarrWeed = "Ranarr weed";
    public String VialOfWater = "Vial of water";
    public String UnfinishedPotion = "Ranarr potion (unf)";
    public boolean cleanHerbs = false;
    */
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
        add(new Delay(this), new Finaliser(this), new GetStuff(this), new CleanHerbs(this), new MakeFinishedPots(this), new MakePots(this), new Fallback(this));

        runAllTime = true;
        t1 = new Thread(() -> updateUIContents());
        t1.start();
    }


    public void updateUIContents() {
        try {
            while (runAllTime){
                store.Update_Counts();
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            // handle: log or throw in a wrapped RuntimeException
            throw new RuntimeException("InterruptedException caught in lambda", e);
        }
    }


    @Override
    public ObjectProperty<? extends Node> botInterfaceProperty() {
        if (botInterfaceProperty == null) {
            // Initializing configUI in this manor is known as Lazy Instantiation
            botInterfaceProperty = new SimpleObjectProperty<>(configUI = new UIBase(this));
        }
        return botInterfaceProperty;
    }
}
