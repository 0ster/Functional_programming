package Lab_1

def compose(f:String => Int,k:Int =>Int, g:Int => Double):String=>Double =
    x => g(k(f(x)))

@main def composeTest() =
    val stringToInt: String => Int = x => x.toInt
    val multiByThree: Int => Int = x => x * 3
    val divIntoTwo: Int => Double = x => x / 2.0
    println(compose(stringToInt,multiByThree,divIntoTwo)("7"))

