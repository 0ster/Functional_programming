package Lab_2

import scala.util.{Try, Success, Failure}
import scala.concurrent.Future
import scala.io.StdIn.readLine
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

@main def goodEnoughPasswordtest2() =
    val password1 = "123dgA"
    val password2 = "HelloK4ao!"
    val password3 = "hhhhhhh!"
    println(goodEnoughPassword2(password1))
    println(goodEnoughPassword2(password2))
    println(goodEnoughPassword2(password3))

def goodEnoughPassword2(password: String): Either[String, String] = Try {
    val error = Seq(
        (password.length >= 8) -> "Длина пароля должна быть >= 8 символов",
        password.exists(_.isUpper) -> "В пароле нет заглавных букв",
        password.exists(_.isLower) -> "В пароле нет букв в нижнем регистре",
        password.exists(_.isDigit) -> "В пароле нет цифр",
        password.exists("!@#$%^&*()_+".contains(_)) -> "В пароле нет ни одного специального символа"
    ).collect { case (false, message) => message }

    if (error.isEmpty) Left("Пароль подходит")
    else Right(error.mkString(", "))
} match {
    case Success(res) => res
    case Failure(_)   => Right("Произошла ошибка")
}

@main def printPassword() =
    val resultPassword = Await.result(readPassword(), Duration.Inf)
    println("Успешный пароль: " + resultPassword)

def readPassword():Future[String] = Future{
    println("Введите пароль: ")
    val password = readLine()

    goodEnoughPassword2(password) match {
        case Left(_) => password
        case Right(errors) =>
            println(errors)
            Await.result(readPassword(), Duration.Inf)
    }
}


