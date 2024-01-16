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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import wonderfulpanic.nochatclosing.NoChatClosing;

@Mixin(GuiChat.class)
public abstract class MixinGuiChat{
	@Redirect(at=@At(
		value="INVOKE",
		target="Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V"),
		method="keyTyped")
	public void closeChat(Minecraft minecraft,GuiScreen gui){
		if(gui==null)
			NoChatClosing.closedByPlayer();
		minecraft.displayGuiScreen(gui);//not null in case if some modification want to open another gui
	}
}