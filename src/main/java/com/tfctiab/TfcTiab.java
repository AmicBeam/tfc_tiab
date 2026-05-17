package com.tfctiab;

import com.tfctiab.config.TfcTiabConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(TfcTiab.MOD_ID)
public final class TfcTiab
{
    public static final String MOD_ID = "tfc_tiab";

    public TfcTiab()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, TfcTiabConfig.SERVER_SPEC);
    }
}
