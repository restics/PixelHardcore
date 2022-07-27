package com.pixelizedgaming.hardcorepixelmon.events;

import com.pixelizedgaming.hardcorepixelmon.DamageSourceWhiteOut;
import com.pixelizedgaming.hardcorepixelmon.HardcorePixelmon;
import com.pixelizedgaming.hardcorepixelmon.config.PixelHardCoreConfig;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.battles.BattleEndCause;
import com.pixelmonmod.pixelmon.api.battles.BattleResults;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.api.events.battles.TurnEndEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.ServerPropertiesProvider;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.system.CallbackI;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = HardcorePixelmon.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerWhiteOutEvent {
    private static final Logger LOGGER = LogManager.getLogger();


    static{
        Pixelmon.EVENT_BUS.register(new PlayerWhiteOutEvent());
    }

    @SubscribeEvent
    public void onBattleEnd(BattleEndEvent e){
        double WHITE_OUT_BAL_MULTIPLIER = PixelHardCoreConfig.white_out_deduction.get();
        boolean WHITE_OUT_KILL_TOGGLE = PixelHardCoreConfig.whiteout_mode.get();
        boolean isPvpBattle = e.getPlayers().size() > 1;


        if (e.cause == BattleEndCause.NORMAL && WHITE_OUT_KILL_TOGGLE) {
            for (Map.Entry<BattleParticipant, BattleResults> entry : e.results.entrySet()) {
                LivingEntity participantEntity = entry.getKey().getEntity();
                if (!(participantEntity instanceof ServerPlayerEntity)) continue;
                ServerPlayerEntity p = (ServerPlayerEntity) participantEntity;
                LOGGER.info("Battle Result  : " + entry.getValue());
                if (entry.getValue() == BattleResults.DEFEAT && !isPvpBattle){
                    p.attackEntityFrom(DamageSourceWhiteOut.WHITE_OUT.setDamageBypassesArmor().setDamageIsAbsolute().setDamageAllowedInCreativeMode(), Float.MAX_VALUE);
                    StorageProxy.getParty(p).setBalance(Math.round(StorageProxy.getParty(p).getBalance().doubleValue() * WHITE_OUT_BAL_MULTIPLIER));
                }
            }
        }


    }

    @SubscribeEvent
    public void onTurnEnd(TurnEndEvent e){
        if (!PixelHardCoreConfig.nuzlocke_mode.get()) return;

        for(BattleParticipant p : e.bcb.participants){
            if (p.getEntity() instanceof ServerPlayerEntity){
                ServerPlayerEntity playerEntity = (ServerPlayerEntity) p.getEntity();
                for(int slot = 0; slot < 5; slot++){
                    Pokemon poke = StorageProxy.getParty(playerEntity).get(slot);
                    if (poke == null) continue;
                    if (poke.getHealth() <= 0){
                        LOGGER.debug(p.getFaintedPokemon().getPokemonName() + " has fainted!");
                        playerEntity.dropItem(poke.getHeldItem().copy(), false);
                        StorageProxy.getParty(playerEntity).set(slot,null);
                        playerEntity.sendMessage(new StringTextComponent(TextFormatting.RED + "Because Nuzlocke Rules are turned on, your " + poke.getDisplayName() + TextFormatting.RED + " was released due to fainting."), playerEntity.getUniqueID());
                    }
                }
            }
        }
    }



}
