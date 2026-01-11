#!/bin/bash

# Senet Game Runner Script

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}╔════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║          Senet Game - Compilation & Runner             ║${NC}"
echo -e "${BLUE}╚════════════════════════════════════════════════════════╝${NC}"
echo ""

# Compile
echo -e "${GREEN}[1/2] Compiling Java files...${NC}"
mkdir -p bin
javac -d bin src/**/*.java src/*.java

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Compilation successful!${NC}"
    echo ""

    # Run
    echo -e "${GREEN}[2/2] Starting Senet Game...${NC}"
    echo ""
    java -cp bin Main "$@"
else
    echo "✗ Compilation failed!"
    exit 1
fi
