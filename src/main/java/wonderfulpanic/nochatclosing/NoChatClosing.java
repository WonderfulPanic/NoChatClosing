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
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import wonderfulpanic.nochatclosing.mixins.MixinGuiChatAccessor;

@Mod(modid="nochatclosing",name="NoChatClosing",version="0.0.1",
	clientSideOnly=true,acceptedMinecraftVersions="*",acceptableRemoteVersions="*")
public class NoChatClosing{
	private static Supplier<GuiScreen>returnTask;
	private static boolean closedByPlayer,openedByServer;
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		MinecraftForge.EVENT_BUS.register(this);
	}
	@SubscribeEvent
	public void guiOpen(GuiOpenEvent event){
		if(event.getGui()==null){
			openedByServer=false;
			if(closedByPlayer){
				closedByPlayer=false;
				return;
			}
			if(returnTask!=null){
				event.setGui(returnTask.get());
				returnTask=null;
			}else
				preserveWindow();
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
		}else if(screen instanceof GuiInventory)
			returnTask=()->new GuiInventory(minecraft.player);
	}
	public static void closedByPlayer(){
		closedByPlayer=true;
	}
	public static boolean isWindowOpenedByServer(){
		return openedByServer;
	}
	public static void openedByServer(){
		openedByServer=true;
	}
}