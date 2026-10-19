package io.github.lassebq.oldmclogo.mixin;

import static io.github.lassebq.oldmclogo.Mod.MODID;

import io.github.lassebq.oldmclogo.Mod;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LogoRenderer.class)
public class LogoDrawerMixin {
    private static final Identifier OLD_LOGO_TEXTURE =
            Identifier.tryBuild(MODID, "textures/gui/title/minecraft.png");
    private static final Identifier OLD_EDITION_TEXTURE =
            Identifier.tryBuild(MODID, "textures/gui/title/edition.png");

    @Shadow
    private boolean showEasterEgg;

    @Shadow
    private boolean keepLogoThroughFade;

    @Inject(
            method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IFI)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void draw(GuiGraphicsExtractor context, int screenWidth, float alpha, int y, CallbackInfo ci) {
        if (Mod.config.getPixelatedStyle()) {
            int logoColor = ARGB.white(this.keepLogoThroughFade ? 1.0F : alpha);
            int editionColor = ARGB.white(1.0F);

            int i = screenWidth / 2 - 137;
            int logoY = y;

            if (this.showEasterEgg) {
                context.blit(RenderPipelines.GUI_TEXTURED, OLD_LOGO_TEXTURE, i, logoY, 0.0F, 0.0F, 99, 44, 256, 256, logoColor);
                context.blit(RenderPipelines.GUI_TEXTURED, OLD_LOGO_TEXTURE, i + 99, logoY, 129.0F, 0.0F, 27, 44, 256, 256, logoColor);
                context.blit(RenderPipelines.GUI_TEXTURED, OLD_LOGO_TEXTURE, i + 125, logoY, 126.0F, 0.0F, 3, 44, 256, 256, logoColor);
                context.blit(RenderPipelines.GUI_TEXTURED, OLD_LOGO_TEXTURE, i + 128, logoY, 99.0F, 0.0F, 26, 44, 256, 256, logoColor);
                context.blit(RenderPipelines.GUI_TEXTURED, OLD_LOGO_TEXTURE, i + 155, logoY, 0.0F, 45.0F, 155, 44, 256, 256, logoColor);
            } else {
                context.blit(RenderPipelines.GUI_TEXTURED, OLD_LOGO_TEXTURE, i, logoY, 0.0F, 0.0F, 155, 44, 256, 256, logoColor);
                context.blit(RenderPipelines.GUI_TEXTURED, OLD_LOGO_TEXTURE, i + 155, logoY, 0.0F, 45.0F, 155, 44, 256, 256, logoColor);
            }

            if (!Mod.config.getRemoveJavaEdition()) {
                int editionY = logoY + 44 - 7;
                context.blit(RenderPipelines.GUI_TEXTURED, OLD_EDITION_TEXTURE, i + 88, editionY, 0.0F, 0.0F, 98, 14, 128, 16, editionColor);
            }

            ci.cancel();
        }
    }
}