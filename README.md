# SimpleEdits

SimpleEdits is a Minecraft mod that adds tools for editing blocks in the game. It provides a selection wand for replacing blocks, a block state changer for modifying certain block states, and a water drain wand for removing water from an area.

## Features

- **Selection Wand**: Allows players to select and replace blocks within a specified area.
- **Block State Changer**: Enables players to change the state of certain blocks, such as rotating stairs or changing slab types.
- **Water Drain Wand**: Drains water in a selected area.
- **Chisel**: Allows players to chisel blocks that have a chisel variant (ex. sandstone, tuff, stone).

## Installation

1. Ensure you have Minecraft 1.20 or 1.21.1 installed. And the corresponding version of Fabric.
2. Download the SimpleEdits mod from the [releases page](https://github.com/1rom11/SimpleEdits/releases).
3. Follow this tutorial to install the mod: [How to install Fabric mods](https://www.youtube.com/watch?v=JhReN8KykY0).

## Usage

### Selection Wand

- **Usage**:
  1. Right-click on a block to set the first position.
  2. Right-click on another block to set the second position.
  3. It fills the area with the block type you have selected (default is set to air).

- **Commands**:
  - `/undo`: Undo the last block replacement.
  - `/block <block_type>`: Set the block type for replacement.
  - `/clearpos`: Clear the selected positions.

### Block State Changer

- **Usage**: Right-click on a block to change its state (ex. rotation, pos). Works with stairs and slabs only!

### Water Drain Wand

- **Usage**: Use the wand on a block adjacent to water to drain the water in the selected area.
- **Note**: The wand should only be used for small patches of water! A large body of water may cause lag!

### Chisel
- **Usage**: Right-click on a block to chisel it. Works with blocks that have a chisel variant (ex. sandstone, tuff, stone).
- **Note**: The chisel will only work on blocks that have a chisel variant!

### Shape Wand
- **Usage**: Right Click on a area to fill it with the shape you have selected.
- **Commands**:
  - `/shape <shape>`: Set the shape for the wand.
  - `/block <block_type>`: Set the block type for the shape.
  - `/radius <radius>`: Set the radius for the shape.
  - `/undoshape`: Undo the last shape fill.

## Development

### Prerequisites

- Java 21
- Gradle

### Building the Project

1. Clone the repository:
   ```sh
   git clone https://github.com/1rom11/SimpleEdits.git
2. Change to the project directory:
   ```sh
   cd SimpleEdits
3. Build the project:
   ```sh
    ./gradlew build
4. The compiled JAR file will be located in the `build/libs` directory.