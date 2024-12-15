package net.romit.simpleedits;

import net.fabricmc.api.ModInitializer;

import net.romit.simpleedits.item.ModItemGroups;
import net.romit.simpleedits.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleEdits implements ModInitializer {
	public static final String MOD_ID = "simpleedits";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		LOGGER.info("Initialized!");
	}
}