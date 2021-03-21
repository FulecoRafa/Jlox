run: build

build:
	javac src/Jlox.java -d dist/class
	echo -e '\033[0;32mBuilding finished'
