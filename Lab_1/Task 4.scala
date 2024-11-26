package Lab_1

@main def test() =
    val x = maxElofColl(Seq(1, 2, 3, 4, 54, 73, 65, 72))
    println(x)

def maxElofColl(n:Seq[Int]):Int =
    val maxEl = n.reduce(_ max _)
    maxEl
