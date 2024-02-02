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

package wonderfulpanic.nochatclosing;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wonderfulpanic.nochatclosing.mixins.MixinGuiChatAccessor;

@Mod(modid="nochatclosing",name="NoChatClosing",version="1.2",
	clientSideOnly=true,acceptedMinecraftVersions="*",acceptableRemoteVersions="*")
public class NoChatClosing{
	private static Supplier<GuiScreen>returnTask;
	private static boolean inGuiInput;
	public static boolean openedByServer;
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		MinecraftForge.EVENT_BUS.register(this);
	}
	@SubscribeEvent
	public void guiOpen(GuiOpenEvent event){
		boolean close=event.getGui()==null;
		if(close)
			openedByServer=false;
		if(inGuiInput)
			return;
		if(close){
			if(returnTask==null)
				preserveWindow();
			else{
				event.setGui(returnTask.get());
				returnTask=null;
			}
		}else
			preserveWindow();
	}
	public static void preserveWindow(){
		Minecraft minecraft=Minecraft.getMinecraft();
		GuiScreen screen=minecraft.currentScreen;
		if(screen instanceof GuiChat){
			GuiTextField field=((MixinGuiChatAccessor)screen).getInputField();
			String msg=(field==null)?"":field.getText();
			returnTask=()->new GuiChat(msg);
		}
	}
	public static void wrapPlayerInput(Runnable run){
		inGuiInput=true;
		run.run();
		inGuiInput=false;
	}
}