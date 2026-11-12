package com.randomgundrops.client;

import com.randomgundrops.Config;
import com.randomgundrops.Network;
import com.randomgundrops.SettingsPacket;
import java.util.Random;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/** The settings menu that opens when you press M. */
public class DropsScreen extends Screen {
    private boolean enabled = Config.ENABLED.get();
    private boolean perBlock = Config.PER_BLOCK.get();
    private boolean keepNormal = Config.KEEP_NORMAL.get();
    private int chance = Config.CHANCE.get();
    private long salt = Config.SALT.get();

    public DropsScreen() {
        super(Component.literal("Random Gun Drops"));
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int y = this.height / 4 - 4;

        addRenderableWidget(CycleButton.onOffBuilder(enabled)
                .create(cx - 100, y, 200, 20, Component.literal("Random gun drops"), (b, v) -> enabled = v));

        addRenderableWidget(CycleButton.booleanBuilder(
                        Component.literal("Same gun per block type"), Component.literal("New random gun every break"))
                .withInitialValue(perBlock)
                .create(cx - 100, y + 24, 200, 20, Component.literal("Gun choice"), (b, v) -> perBlock = v));

        addRenderableWidget(new ChanceSlider(cx - 100, y + 48, 200, 20, chance));

        addRenderableWidget(CycleButton.onOffBuilder(keepNormal)
                .create(cx - 100, y + 72, 200, 20, Component.literal("Also drop normal items"), (b, v) -> keepNormal = v));

        addRenderableWidget(Button.builder(Component.literal("Reroll which gun each block gives"), b -> {
            salt = new Random().nextLong();
            b.setMessage(Component.literal("Rerolled! (press Save)"));
        }).bounds(cx - 100, y + 96, 200, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Save & close"), b -> {
            Network.CHANNEL.sendToServer(new SettingsPacket(enabled, perBlock, chance, keepNormal, salt));
            onClose();
        }).bounds(cx - 100, y + 128, 98, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Cancel"), b -> onClose())
                .bounds(cx + 2, y + 128, 98, 20).build());
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g);
        g.drawCenteredString(this.font, this.title, this.width / 2, this.height / 4 - 28, 0xFFFFFF);
        super.render(g, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private class ChanceSlider extends AbstractSliderButton {
        ChanceSlider(int x, int y, int w, int h, int initialPercent) {
            super(x, y, w, h, Component.empty(), initialPercent / 100.0);
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            setMessage(Component.literal("Drop chance: " + (int) Math.round(this.value * 100) + "%"));
        }

        @Override
        protected void applyValue() {
            chance = (int) Math.round(this.value * 100);
        }
    }
}
