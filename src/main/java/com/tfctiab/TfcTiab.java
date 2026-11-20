package com.tfctiab;

import com.tfctiab.config.TfcTiabConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(TfcTiab.MOD_ID)
public final class TfcTiab
{
    public static final String MOD_ID = "tfc_tiab";

    public TfcTiab(ModContainer container)
    {
        container.registerConfig(ModConfig.Type.SERVER, TfcTiabConfig.SERVER_SPEC);
    }
}
