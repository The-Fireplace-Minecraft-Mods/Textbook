package dev.the_fireplace.textbook.api;

import dev.the_fireplace.lib.api.client.interfaces.*;
import net.minecraft.client.gui.screen.Screen;

import java.util.List;
import java.util.function.Consumer;

public interface ConfigScreenBuilderNew
{
    void startCategory(String translationKey, Object... translationParameters);

    void startSubCategory(String translationKey, Object... translationParameters);

    void endSubCategory();

    OptionBuilder<String> addStringField(
            String optionTranslationBase,
            String currentValue,
            String defaultValue,
            Consumer<String> saveFunction
    );

    DropdownOptionBuilder<String> addStringDropdown(
            String optionTranslationBase,
            String currentValue,
            String defaultValue,
            Iterable<String> dropdownEntries,
            Consumer<String> saveFunction
    );

    <T extends Enum<T>> OptionBuilder<T> addEnumDropdown(
            String optionTranslationBase,
            T currentValue,
            T defaultValue,
            T[] dropdownEntries,
            Consumer<T> saveFunction
    );

    OptionBuilder<List<String>> addStringListField(
            String optionTranslationBase,
            List<String> currentValue,
            List<String> defaultValue,
            Consumer<List<String>> saveFunction
    );

    NumericOptionBuilder<Float> addFloatField(
            String optionTranslationBase,
            float currentValue,
            float defaultValue,
            Consumer<Float> saveFunction
    );

    DecimalSliderOptionBuilder<Float> addFloatSlider(
            String optionTranslationBase,
            float currentValue,
            float defaultValue,
            Consumer<Float> saveFunction,
            float min,
            float max
    );

    OptionBuilder<List<Float>> addFloatListField(
            String optionTranslationBase,
            List<Float> currentValue,
            List<Float> defaultValue,
            Consumer<List<Float>> saveFunction
    );

    NumericOptionBuilder<Double> addDoubleField(
            String optionTranslationBase,
            double currentValue,
            double defaultValue,
            Consumer<Double> saveFunction
    );

    DecimalSliderOptionBuilder<Double> addDoubleSlider(
            String optionTranslationBase,
            double currentValue,
            double defaultValue,
            Consumer<Double> saveFunction,
            double min,
            double max
    );

    OptionBuilder<List<Double>> addDoubleListField(
            String optionTranslationBase,
            List<Double> currentValue,
            List<Double> defaultValue,
            Consumer<List<Double>> saveFunction
    );

    NumericOptionBuilder<Long> addLongField(
            String optionTranslationBase,
            long currentValue,
            long defaultValue,
            Consumer<Long> saveFunction
    );

    OptionBuilder<Long> addLongSlider(
            String optionTranslationBase,
            long currentValue,
            long defaultValue,
            Consumer<Long> saveFunction,
            long min,
            long max
    );

    OptionBuilder<List<Long>> addLongListField(
            String optionTranslationBase,
            List<Long> currentValue,
            List<Long> defaultValue,
            Consumer<List<Long>> saveFunction
    );

    NumericOptionBuilder<Integer> addIntField(
            String optionTranslationBase,
            int currentValue,
            int defaultValue,
            Consumer<Integer> saveFunction
    );

    OptionBuilder<Integer> addIntSlider(
            String optionTranslationBase,
            int currentValue,
            int defaultValue,
            Consumer<Integer> saveFunction,
            int min,
            int max
    );

    OptionBuilder<List<Integer>> addIntListField(
            String optionTranslationBase,
            List<Integer> currentValue,
            List<Integer> defaultValue,
            Consumer<List<Integer>> saveFunction
    );

    NumericOptionBuilder<Short> addShortField(
            String optionTranslationBase,
            short currentValue,
            short defaultValue,
            Consumer<Short> saveFunction
    );

    OptionBuilder<Short> addShortSlider(
            String optionTranslationBase,
            short currentValue,
            short defaultValue,
            Consumer<Short> saveFunction,
            short min,
            short max
    );

    OptionBuilder<List<Short>> addShortListField(
            String optionTranslationBase,
            List<Short> currentValue,
            List<Short> defaultValue,
            Consumer<List<Short>> saveFunction
    );

    NumericOptionBuilder<Byte> addByteField(
            String optionTranslationBase,
            byte currentValue,
            byte defaultValue,
            Consumer<Byte> saveFunction
    );

    OptionBuilder<Byte> addByteSlider(
            String optionTranslationBase,
            byte currentValue,
            byte defaultValue,
            Consumer<Byte> saveFunction,
            byte min,
            byte max
    );

    OptionBuilder<List<Byte>> addByteListField(
            String optionTranslationBase,
            List<Byte> currentValue,
            List<Byte> defaultValue,
            Consumer<List<Byte>> saveFunction
    );


    Screen build();
}
