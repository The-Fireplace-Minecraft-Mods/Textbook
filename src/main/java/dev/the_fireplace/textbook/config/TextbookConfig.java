package dev.the_fireplace.textbook.config;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.lib.api.io.interfaces.access.StorageReadBuffer;
import dev.the_fireplace.lib.api.io.interfaces.access.StorageWriteBuffer;
import dev.the_fireplace.lib.api.lazyio.injectables.ConfigStateManager;
import dev.the_fireplace.lib.api.lazyio.interfaces.Config;
import dev.the_fireplace.textbook.TextbookConstants;
import dev.the_fireplace.textbook.domain.config.ConfigValues;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Implementation("dev.the_fireplace.textbook.domain.config.ConfigValues")
@Singleton
public final class TextbookConfig implements Config, ConfigValues
{
    private final ConfigValues defaultConfig;

    private int pageByteLimit;

    @Inject
    public TextbookConfig(ConfigStateManager configStateManager, @Named("default") ConfigValues defaultConfig) {
        this.defaultConfig = defaultConfig;
        configStateManager.initialize(this);
    }

    @Override
    public String getId() {
        return TextbookConstants.MODID;
    }

    @Override
    public void readFrom(StorageReadBuffer buffer) {
        pageByteLimit = buffer.readInt("pageByteLimit", defaultConfig.getPageByteLimit());
    }

    @Override
    public void writeTo(StorageWriteBuffer buffer) {
        buffer.writeInt("pageByteLimit", pageByteLimit);
    }

    @Override
    public int getPageByteLimit() {
        return this.pageByteLimit;
    }

    public void setPageByteLimit(int pageByteLimit) {
        this.pageByteLimit = pageByteLimit;
    }
}
