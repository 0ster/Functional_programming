package Lab_1

@main def maxElofCollTest() =
    val coll = Seq(1, 2, 3, 4, 54, 73, 65, 72)
    println(maxElofColl(coll))

def maxElofColl(n:Seq[Int]):Int =
    val maxEl = n.reduce(_ max _)
    maxEl
