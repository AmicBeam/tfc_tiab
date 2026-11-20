package com.tfctiab.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class TfcTiabConfig
{
    public static final ModConfigSpec SERVER_SPEC;
    public static final Server SERVER;

    static
    {
        final ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        SERVER = new Server(builder);
        SERVER_SPEC = builder.build();
    }

    public static final class Server
    {
        public final ModConfigSpec.IntValue averageRandomTickInterval;

        private Server(ModConfigSpec.Builder builder)
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
