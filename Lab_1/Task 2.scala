package Lab_1

@main def multyHelloTest() =
    multiHello(10)

def multiHello(n:Int) =
    for (i <- 1 to 10)
        if (i % 2 == 0) then println(s"Hello $n") else println(s"Hello ${n-i}")
