package com.pixelizedgaming.hardcorepixelmon.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class PixelHardCoreConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Double> white_out_deduction;
    public static final ForgeConfigSpec.ConfigValue<Boolean> nuzlocke_mode;
    public static final ForgeConfigSpec.ConfigValue<Boolean> whiteout_mode;
    public static final ForgeConfigSpec.ConfigValue<Boolean> catcher_mode;

    static{
        BUILDER.push("PixelHardcore Config");

        white_out_deduction = BUILDER.comment("Defines how much percentage of the player's balance should be kept after whiting out. Default is 0.5.").define("White Out Deduction", 0.5);

        nuzlocke_mode = BUILDER.comment("Toggles Nuzlocke mode. A fainted pokemon in Nuzlocke mode is immediately released, with items dropping to the floor.").define("Nuzlocke Mode", false);

        whiteout_mode = BUILDER.comment("Toggles White Out mode. Whiting out with this mode enabled kills the player.").define("White Out Mode", true);

        catcher_mode = BUILDER.comment("Toggles Catcher mode. Catcher mode disables catches above the player's max level.").define("Catcher Mode", true);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }


}
