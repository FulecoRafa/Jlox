PROJECT_NAME=Jlox
PROJECT_PATH=src/
CLASS_PATH=./dist/class
LIB_PATH=./lib
OUTPUT_DIR=src/Jlox/Compiler

FILES=$(shell find ${PROJECT_PATH} -name "*.java")

run: build
	java -cp ${LIB_PATH} -cp ${CLASS_PATH} ${PROJECT_NAME}/${PROJECT_NAME} ${ARGS}

jar: build
	jar -c -f ${PROJECT_NAME}.jar -e ${PROJECT_NAME}/${PROJECT_NAME} -C ${CLASS_PATH} ${PROJECT_NAME}

build:
	javac -d ${CLASS_PATH} ${FILES}
	echo -e '\033[0;32mBuilding finished\033[0m'

generate: build
	java -cp ${LIB_PATH} -cp ${CLASS_PATH} tool/GenerateAst ${OUTPUT_DIR}
