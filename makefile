##==============================================================================
## CS 349 Assignment 02, Excavator
##
## @description: Makefile for Excavator.java
## @author: Ah Hoe Lai
## @userid: ahlai
## @version: 1.0 25/10/2015
##==============================================================================

all:
	@echo "Compiling..."
	javac *.java

run: all
	@echo "Running..."
	java Excavator

clean:
	rm *.class
