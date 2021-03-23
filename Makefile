PROJECT_NAME=Jlox

run: build
	java -cp ./dist/class ${PROJECT_NAME} ${ARGS}

build:
	javac src/${PROJECT_NAME}.java -d dist/class
	echo -e '\033[0;32mBuilding finished\033[0m'
