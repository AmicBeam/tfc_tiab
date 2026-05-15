package com.limachi.tfctiab.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class TfcTiabConfig
{
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final Server SERVER;

    static
    {
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        SERVER = new Server(builder);
        SERVER_SPEC = builder.build();
    }

    public static final class Server
    {
        public final ForgeConfigSpec.IntValue averageRandomTickInterval;

        private Server(ForgeConfigSpec.Builder builder)
        {
            builder.push("acceleration");
            averageRandomTickInterval = builder
                .comment("Average extra ticks between Time In A Bottle random tick attempts. Defaults to Time In A Bottle's vanilla value.")
                .defineInRange("averageRandomTickInterval", 1365, 1, 100000);
            builder.pop();
        }
    }

    private TfcTiabConfig() {}
}
