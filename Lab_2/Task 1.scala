package Lab_2

def f(x: Double): Double =
    math.sin(x) * math.exp(-x)

def integral(f:Double => Double, l:Double, r:Double, steps:Int):Double =
    val h = (r-l)/steps
    (1 to steps).map(i => f(l+i*h)*h).sum
    
@main def integralTest() =
    println(integral(f, 1, 4, 500))