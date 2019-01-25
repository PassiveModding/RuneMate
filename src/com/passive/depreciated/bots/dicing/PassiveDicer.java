package com.runemate.passive.bots.dicing;

import com.runemate.game.api.client.embeddable.EmbeddableUI;
import com.runemate.game.api.script.framework.task.TaskBot;
import com.runemate.passive.bots.dicing.ui.PassiveDicerUi;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

public class PassiveDicer extends TaskBot implements EmbeddableUI {
    private  SimpleObjectProperty<Node> botInterfaceProperty;
    public  PassiveDicerUi configUI;

    public PassiveDicer(){
        setEmbeddableUI(this);
    }

    @Override
    public void onStart (String... args){
        System.out.println("Running bot");
        setLoopDelay(100, 2000);
        add(new DelayStart(), new LureData(), new SendWin(), new Dice(), new ConfirmTrade(), new DoTrade(), new OpenTrade(), new Advertise());
        //super.onStart(args);
    }

    @Override
    public ObjectProperty<? extends Node> botInterfaceProperty() {
        if (botInterfaceProperty == null) {
            // Initializing configUI in this manor is known as Lazy Instantiation
            botInterfaceProperty = new SimpleObjectProperty<>(configUI = new PassiveDicerUi(this));
        }
        return botInterfaceProperty;
    }
}
