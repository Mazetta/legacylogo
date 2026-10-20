package io.github.lassebq.legacylogo.mixin;

import static io.github.lassebq.legacylogo.Mod.MODID;
import io.github.lassebq.legacylogo.Mod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;

@Mixin(LogoRenderer.class)
public class LogoDrawerMixin {
    private static final ResourceLocation OLD_LOGO_TEXTURE = ResourceLocation.tryBuild(MODID, "textures/gui/title/minecraft.png");
    private static final ResourceLocation OLD_EDITION_TEXTURE = ResourceLocation.tryBuild(MODID, "textures/gui/title/edition.png");
    @Shadow
    private boolean showEasterEgg;
    @Shadow
    private boolean keepLogoThroughFade;

    @Inject(method = "renderLogo(Lnet/minecraft/client/gui/GuiGraphics;IFI)V", at = @At("HEAD"), cancellable = true)
    public void draw(GuiGraphics context, int screenWidth, float alpha, int y, CallbackInfo ci) {
        if(Mod.config.getPixelatedStyle()) {
            context.setColor(1.0F, 1.0F, 1.0F, this.keepLogoThroughFade ? 1.0F : alpha);
            int i = screenWidth / 2 - 137;
            if(showEasterEgg) {
                context.blit(OLD_LOGO_TEXTURE, i + 0, 30, 0, 0, 99, 44);
                context.blit(OLD_LOGO_TEXTURE, i + 99, 30, 129, 0, 27, 44);
                context.blit(OLD_LOGO_TEXTURE, i + 99 + 26, 30, 126, 0, 3, 44);
                context.blit(OLD_LOGO_TEXTURE, i + 99 + 26 + 3, 30, 99, 0, 26, 44);
                context.blit(OLD_LOGO_TEXTURE, i + 155, 30, 0, 45, 155, 44);
            } else {
                context.blit(OLD_LOGO_TEXTURE, i + 0, 30, 0, 0, 155, 44);
                context.blit(OLD_LOGO_TEXTURE, i + 155, 30, 0, 45, 155, 44);
            }
            if(!Mod.config.getRemoveJavaEdition()) {
                int k = y + 44 - 7;
                context.blit(OLD_EDITION_TEXTURE, i + 88, k, 0.0F, 0.0F, 98, 14, 128, 16);
                context.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
            ci.cancel();
        }
    }

    @WrapWithCondition(
        method = "renderLogo(Lnet/minecraft/client/gui/GuiGraphics;IFI)V", at = @At(value = "INVOKE:LAST", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V")
    )
    private boolean renderJavaEdition(GuiGraphics context, ResourceLocation texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        return !Mod.config.getRemoveJavaEdition();
    }
}
