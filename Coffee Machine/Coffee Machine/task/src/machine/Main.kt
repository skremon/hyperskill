package machine

import java.util.*

class CoffeeMachine {
    private fun printState() {
        println("The coffee machine has:")
        println("$water of water")
        println("$milk of milk")
        println("$beans of coffee beans")
        println("$cups of disposable cups")
        println("$money of money")
    }

    private fun need(water: Int = 0, milk: Int = 0, beans: Int = 0, cups: Int = 0, money: Int = 0) : String {
        val errorMsg = StringBuilder()

        if (water > this.water) {
            errorMsg.append("Sorry, not enough water!\n")
        }
        if (milk > this.milk) {
            errorMsg.append("Sorry, not enough milk!\n")
        }
        if (beans > this.beans) {
            errorMsg.append("Sorry, not enough beans!\n")
        }
        if (cups > this.cups) {
            errorMsg.append("Sorry, not enough cups!\n")
        }

        return if (errorMsg.isEmpty()) {
            this.water -= water
            this.milk -= milk
            this.beans -= beans
            this.cups -= cups
            this.money += money
            "I have enough resources, making you a coffee"
        } else {
            errorMsg.toString()
        }
    }

    fun takeMoney(): Int {
        val take = money
        money = 0
        return take
    }

    private fun makeEspresso() : String {
        return need(250, 0, 16, 1, 4)
    }

    private fun makeLatte() : String {
        return need(350, 75, 20, 1, 7)
    }

    private fun makeCappuccino() : String {
        return need(200, 100, 12, 1, 6)
    }

    private fun addWater(ml: Int) {
        water += ml
    }

    private fun addMilk(ml: Int) {
        milk += ml
    }

    private fun addBeans(gm: Int) {
        beans += gm
    }

    private fun addCups(n: Int) {
        cups += n
    }

    fun processCmd(cmd: String) : State {
        when (state) {
            State.READY -> {
                when (cmd) {
                    "buy" -> {
                        println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ")
                        state = State.BUY
                    }
                    "fill" -> {
                        println("Write how many ml of water do you want to add: ")
                        state = State.FILL_WATER
                    }
                    "take" -> {
                        println("I gave you ${takeMoney()}")
                        reset()
                    }
                    "remaining" -> {
                        printState()
                        reset()
                    }
                    "exit" -> state = State.STOPPED
                }
            }
            State.BUY -> {
                when (cmd) {
                    "1" -> println(makeEspresso())
                    "2" -> println(makeLatte())
                    "3" -> println(makeCappuccino())
                    "back" -> {}
                }
                reset()
            }
            State.FILL_WATER -> {
                try {
                    addWater(cmd.toInt())
                    println("Write how many ml of milk do you want to add: ")
                    state = State.FILL_MILK
                } catch (e: NumberFormatException) { println(e.toString())}
            }
            State.FILL_MILK -> {
                try {
                    addMilk(cmd.toInt())
                    println("Write how many grams of coffee beans do you want to add: ")
                    state = State.FILL_BEANS
                } catch (e: NumberFormatException) { println(e.toString())}

            }
            State.FILL_BEANS -> {
                try {
                    addBeans(cmd.toInt())
                    println("Write how many disposable cups of coffee do you want to add: ")
                    state = State.FILL_CUPS
                } catch (e: NumberFormatException) { println(e.toString())}
            }
            State.FILL_CUPS -> {
                try {
                    addCups(cmd.toInt())
                    reset()
                } catch (e: NumberFormatException) { println(e.toString())}
            }
            State.STOPPED -> {}
        }
        return state
    }

    fun reset() {
        print("Write action (buy, fill, take, remaining, exit): ")
        state = State.READY
    }

    enum class State {
        READY,
        BUY,
        FILL_WATER,
        FILL_MILK,
        FILL_BEANS,
        FILL_CUPS,
        STOPPED
    }

    private var water = 400
    private var milk = 540
    private var beans = 120
    private var cups = 9
    private var money = 550

    private var state : State = State.READY
}

fun main() {
    val coffeeMachine = CoffeeMachine()
    coffeeMachine.reset()
    val scanner = Scanner(System.`in`)

    while (scanner.hasNext()) {
        if (coffeeMachine.processCmd(scanner.nextLine()) == CoffeeMachine.State.STOPPED) {
            break
        }
    }
}
