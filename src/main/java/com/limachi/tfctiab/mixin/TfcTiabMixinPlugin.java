package com.limachi.tfctiab.mixin;

import java.util.List;
import java.util.Set;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.forgespi.language.IModInfo;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public final class TfcTiabMixinPlugin implements IMixinConfigPlugin
{
    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig()
    {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
    {
        if (mixinClassName.contains(".firmalife."))
        {
            return isModLoadedEarly("firmalife");
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins()
    {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    private static boolean isModLoadedEarly(String modId)
    {
        try
        {
            final LoadingModList loadingModList = FMLLoader.getLoadingModList();
            return loadingModList != null && loadingModList.getModFiles().stream()
                .flatMap(file -> file.getMods().stream())
                .map(IModInfo::getModId)
                .anyMatch(modId::equals);
        }
        catch (Throwable ignored)
        {
            return false;
        }
    }
}
