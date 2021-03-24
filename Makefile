PROJECT_NAME=Jlox
PROJECT_PATH=src/
FILES=$(shell find ${PROJECT_PATH} -name "*.java")

run: build
	java -cp ./dist/class ${PROJECT_NAME}/${PROJECT_NAME} ${ARGS}

build:
	javac -d dist/class ${FILES}
	echo -e '\033[0;32mBuilding finished\033[0m'
