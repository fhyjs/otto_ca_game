package org.eu.hanana.reimu.game.ottoca.core;

import org.eu.hanana.reimu.thrunner.core.util.assets.IAssets;
import org.eu.hanana.reimu.thrunner.core.util.assets.ResourceLocation;

import java.io.IOException;
import java.util.List;

public class OttoGameAssets implements IAssets {
    @Override
    public byte[] readAssetAsBytes(ResourceLocation path) throws IOException {
        return new byte[0];
    }

    @Override
    public List<ResourceLocation> getAllAssetNames() {
        return List.of();
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public int getAssetSize(ResourceLocation path) throws IOException {
        return 0;
    }

    @Override
    public void reload() {

    }

    @Override
    public void dispose() {

    }
}
