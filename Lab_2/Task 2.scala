package Lab_2

import scala.compiletime.ops.string

def goodEnoughPassword(password:String): String = {
    val verification: Seq[Boolean] = Seq(
        password.length >=8,
        password.exists(_.isUpper),
        password.exists(_.isLower),
        password.exists(_.isDigit),
        password.exists("!@#$%^&*()_+".contains(_)),
        )
    if (verification.reduce(_ && _)) "Безопасно" else "Небезопасно"
}

@main def goodEnoughPasswordtest() =
    val password1 = "123dgA"
    val password2 = "HelloK4ao!"
    println(goodEnoughPassword(password1))
    println(goodEnoughPassword(password2))
