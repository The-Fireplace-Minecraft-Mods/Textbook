package dev.the_fireplace.textbook.config;

import dev.the_fireplace.annotateddi.api.di.Implementation;
import dev.the_fireplace.textbook.domain.config.ConfigValues;

@Implementation(name = "default")
public final class DefaultConfigValues implements ConfigValues
{
    @Override
    public int getPageByteLimit() {
        return 0;
    }
}
