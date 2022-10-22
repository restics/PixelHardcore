package com.pixelizedgaming.hardcorepixelmon.events;

import com.pixelizedgaming.hardcorepixelmon.HardcorePixelmon;
import com.pixelizedgaming.hardcorepixelmon.config.PixelHardCoreConfig;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.item.pokeball.PokeBallRegistry;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = HardcorePixelmon.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PokemonCatchEvent {

    private static final Logger LOGGER = LogManager.getLogger();

    static{
        Pixelmon.EVENT_BUS.register(new PokemonCatchEvent());
    }

    @SubscribeEvent
    public void onPokeCatch(CaptureEvent e){
        if (!(e instanceof CaptureEvent.StartCapture || e instanceof CaptureEvent.StartRaidCapture)) return;
        Pokemon pokemon = e.getPokemon().getPokemon();
        LOGGER.debug("Player attempted pokemon catch");
        //if poke level greater than max player level catch is cancelled
        if (pokemon.getPokemonLevel() > findMaxLevel(StorageProxy.getParty(e.player).getAll()) && PixelHardCoreConfig.catcher_mode.get()){
            LOGGER.debug("Attempted catch of higher level pokemon in Catching Mode");
            if (e.pokeball.getBallType().equals(PokeBallRegistry.MASTER_BALL.get())) {
                e.setCanceled(true);
                TranslationTextComponent text = new TranslationTextComponent("pixelmon.hardcore.catcher", pokemon.getDisplayName());
                e.player.sendMessage(text, e.player.getUUID());
            }


        }
    }

    /**
     * Finds the maximum level limit,
     * determined by the max level of online players.
     * @param pStorage - Party Storage to calculate maximum level of
     * @return max - integer with max level
     */
    private double findMaxLevel(Pokemon[] pStorage) {
        int max = 0;
        for (Pokemon p: pStorage){
            if (p == null) continue;
            if (max < p.getPokemonLevel()){
                max = p.getPokemonLevel();
            }
        }
        return max;

    }

}

