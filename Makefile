PROJECT_NAME=Jlox
PROJECT_PATH=src/
CLASS_PATH=./dist/class
LIB_PATH=./lib

FILES=$(shell find ${PROJECT_PATH} -name "*.java")

run: build
	java -cp ${LIB_PATH} -cp ${CLASS_PATH} ${PROJECT_NAME}/${PROJECT_NAME} ${ARGS}

build:
	javac -d ${CLASS_PATH} ${FILES}
	echo -e '\033[0;32mBuilding finished\033[0m'
