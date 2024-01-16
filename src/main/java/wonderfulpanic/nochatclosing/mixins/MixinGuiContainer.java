/*
 * Copyright (C) 2024 WonderfulPanic
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package wonderfulpanic.nochatclosing.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import wonderfulpanic.nochatclosing.NoChatClosing;

@Mixin(GuiContainer.class)
public abstract class MixinGuiContainer{
	@Redirect(at=@At(
		value="INVOKE",
		target="Lnet/minecraft/client/entity/EntityPlayerSP;closeScreen()V"),
		method="keyTyped")
	public void closeScreen(EntityPlayerSP player){
		NoChatClosing.closedByPlayer();
		player.closeScreen();
	}
}