package com.runemate.passive.bots.tutorialisland;

import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Players;

public class GetContextItems {

    public static Context context;

    public static Context GetContextItems() {
        return GetContextItems(false);
    }

    public static Context GetContextItems(boolean refresh){
        if (refresh){
            Context cont = new Context();

            context = cont;

            System.out.println("---CONTEXT---\n" +
                    "altChatText: " + cont.altChatText + "\n" +
                    "Continue: " + String.valueOf(cont.contButton != null) + "\n" +
                    "ChatText: " + cont.chatText + "\n" +
                    "ChatTitle: " + cont.chatTitle + "\n" +
                    "Position: (" + cont.position().getX() + "," + cont.position().getY() + ")\n");

            return cont;
        }

        return context;
    }

    public static InterfaceComponent CompUi() {

        Object[] interfaces = Interfaces.newQuery().results().toArray();
        for (int i = 0; i < interfaces.length; i++) {
            InterfaceComponent comp = (InterfaceComponent) interfaces[i];

            if (comp.getText() != null || comp.getName() != null) {
                if (comp.getText() != null && comp.getText() != "") {
                    if (comp.getId() == 17235969) {
                        return comp;
                    }
                }
            }
        }
        return null;
    }

    public static String CompText (){
        InterfaceComponent compUi = CompUi();
        if (compUi != null){
            return FixedText(compUi.getText());
        }

        return "";
    }

    public static String FixedText (String text){
        if (text == null){
            text = "";
        }

        return text;
    }

    public static class Context {
        Player currentPlayer = Players.getLocal();
        public Coordinate position(){
            if (currentPlayer != null){
                return currentPlayer.getPosition();
            }

            return new Coordinate(0,0);
        }

        String altChatText = CompText();
        String chatText = FixedText(ChatDialog.getText());
        String chatTitle = FixedText(ChatDialog.getTitle());
        ChatDialog.Continue contButton = ChatDialog.getContinue();
    }
}
