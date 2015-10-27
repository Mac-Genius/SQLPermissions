package io.github.mac_genius.sqlpermissions.Commands;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.Prompt;

/**
 * Created by Mac on 10/26/2015.
 */
public class MenuPrompt extends FixedSetPrompt {


    @Override
    protected Prompt acceptValidatedInput(ConversationContext conversationContext, String s) {
        return null;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return null;
    }
}
