package Lab_1

@main def splitbyIndexTest() =
    val coll = Seq(1, 2, 3, 4, 54, 13, 65, 72)
    println(splitByIndex(coll))

def splitByIndex(n:Seq[Int]):(Seq[Int], Seq[Int]) =
    (n.zipWithIndex.filter(_._2 % 2 == 0).map(_._1),n.zipWithIndex.filter(_._2 %2 != 0).map(_._1))
