package Lab_1

@main def patternMatchingTest() =
    val coll = Seq(1, 2.2, 31, "as4", 65.44, 72, ())
    println(patternMatching(coll))

//Функция, сопоставляющая тип c элементом коллекции
def patternMatching(n:Seq[Any]):Seq[String] =
    n.map{ i => i match
        case _:Int => s"integer: $i"
        case _:Double => s"double: $i"
        case _:String => s"string: $i"
        case _ => s"Unknown $i"
    }


