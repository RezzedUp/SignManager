package com.rezzedup.signmanager;

import com.rezzedup.signmanager.clipboard.Clipboard;
import com.rezzedup.signmanager.event.ClipboardReminder;
import com.rezzedup.signmanager.event.SignColorListener;
import com.rezzedup.signmanager.event.SignCopyPasteListener;
import com.rezzedup.signmanager.event.SignExploitListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SignManager extends JavaPlugin
{
    private final Map<UUID, Clipboard> clipboards = new HashMap<>();

    @Override
    public void onEnable()
    {
        saveDefaultConfig();
        
        Debug.setEnabled(getConfig().getBoolean("debug"));
        
        getCommand("sign").setExecutor(new SignCommand(this));

        new BuildPermission(this);
        new SignCopyPasteListener(this);
        new ClipboardReminder(this);
        new SignColorListener(this);
        
        if (getConfig().getBoolean("sign-crash.prevent-exploits"))
        {
            new SignExploitListener(this);
        }

        Send.message(Send.Mode.NORMAL, "Loaded SignManager &6by RezzedUp");
    }

    @Override
    public void onDisable()
    {
        Send.message(Send.Mode.NORMAL, "Unloaded.");
    }

    public Clipboard getClipboard(Player player)
    {
        return getClipboard(player.getUniqueId());
    }

    public Clipboard getClipboard(UUID uuid)
    {
        if (clipboards.containsKey(uuid))
        {
            return clipboards.get(uuid);
        }
        else
        {
            clipboards.put(uuid, new Clipboard());
            return getClipboard(uuid);
        }
    }

    public boolean hasClipboard(Player player)
    {
        return hasClipboard(player.getUniqueId());
    }

    public boolean hasClipboard(UUID uuid)
    {
        return clipboards.containsKey(uuid);
    }
}
