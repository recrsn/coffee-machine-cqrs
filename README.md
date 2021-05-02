# Coffee Machine

1. It will be serving some beverages.
2. Each beverage will be made using some ingredients.
3. Assume time to prepare a beverage is the same for all cases.
4. The quantity of ingredients used for each beverage can vary. Also, the same ingredient (ex:
   water) can be used for multiple beverages.
5. There would be N ( N is an integer ) outlet from which beverages can be served.
6. Maximum N beverages can be served in parallel.
7. Any beverage can be served only if all the ingredients are available in terms of quantity.
8. There would be an indicator that would show which all ingredients are running low. We need some methods to refill
   them.

## Requirements

1. Java 16

### Running the code

1. Run `./gradlew build` to generate and executable JAR
2. Run `java -jar app/build/libs/app.jar`

## Usage

```
Commands:
---------
init:	initialize a coffee machine
	Arguments:
	----------
	slots:	total number of slots

list-recipes:	list available recipes

make-coffee:	make coffee
	Arguments:
	----------
	recipe:	coffee recipe

fill-ingredient:	refill ingredient
	Arguments:
	----------
	name:	ingredient name
	quantity:	quantity

help:	show help
```

### Sample conversation

```
init 3
OK
fill-ingredient WATER 10
OK
fill-ingredient COFFEE 10
OK
fill-ingredient MILK 1
OK
make-coffee mocha
Order 63c8981d-fe86-471b-9b2f-9ece323b84b1 assigned slot slot-coffee-machine-1-2
MILK in store store-coffee-machine-1 running low
Order 63c8981d-fe86-471b-9b2f-9ece323b84b1 is brewing
Order 63c8981d-fe86-471b-9b2f-9ece323b84b1 is ready
OK
```
