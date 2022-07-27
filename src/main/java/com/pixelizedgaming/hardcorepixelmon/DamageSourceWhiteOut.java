package com.pixelizedgaming.hardcorepixelmon;

import net.minecraft.util.DamageSource;

public class DamageSourceWhiteOut extends DamageSource {
    public static DamageSource WHITE_OUT = new DamageSourceWhiteOut("whiteout.name");
    public DamageSourceWhiteOut(String damageTypeIn) {
        super(damageTypeIn);
    }


}
