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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketOpenWindow;
import wonderfulpanic.nochatclosing.NoChatClosing;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerWindow implements INetHandlerPlayClient{
	@Shadow
	private Minecraft gameController;
	@Inject(at=@At("TAIL"),
		method="handleOpenWindow(Lnet/minecraft/network/play/server/SPacketOpenWindow;)V")
	public void injectOpenWindow(SPacketOpenWindow packet,CallbackInfo info){
		NoChatClosing.openedByServer=true;
	}
	@Inject(at=@At("HEAD"),
		method="handleCloseWindow(Lnet/minecraft/network/play/server/SPacketCloseWindow;)V",
		cancellable=true)
	public void injectCloseWindow(SPacketCloseWindow packet,CallbackInfo info){
		PacketThreadUtil.checkThreadAndEnqueue(packet,this,gameController);
		if(NoChatClosing.openedByServer)
			NoChatClosing.openedByServer=false;
		else
			info.cancel();
	}
}