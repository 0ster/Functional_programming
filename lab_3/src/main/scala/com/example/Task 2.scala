package lab_3.src.main.scala.com.example

import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.util.Random

object ActorServer:
    // Опишем сообщение, которое принимает актор: два числа и ссылка на актора для ответа
    case class Message(a: Int, b: Int, replyTo: ActorRef[Int])
    
    def apply(): Behavior[Message] = Behaviors.receive { (context, message) =>
        context.log.info(s"Received numbers: ${message.a} and ${message.b}")
        message.replyTo ! (message.a + message.b) // Отправляем результат обратно
        Behaviors.same
    }

object ActorClient:
    def apply(server: ActorRef[ActorServer.Message]): Behavior[Int] = Behaviors.setup { context =>
        def generationAndSending(): Unit = {
            val a = Random.between(0, 100)
            val b = Random.between(0, 100)
            context.log.info(s"Sending numbers: $a and $b to the server" + '\n')
            server ! ActorServer.Message(a, b, context.self)
            Thread.sleep(1000)
        }

        generationAndSending()
        
        //смена поведения
        Behaviors.receiveMessage { message =>
            context.log.info(s"Received result: $message" + '\n')
            generationAndSending() // Генерация нового сообщения
            Behaviors.same
        }
    }

object AddingSystem:
    def apply(): Behavior[Nothing] = Behaviors.setup[Nothing] { context =>
        val server = context.spawn(ActorServer(), "server") // Создаем актора сервера
        val client = context.spawn(ActorClient(server), "client") // Создаем актора клиента

        Behaviors.empty
    }

@main def AddingMain(): Unit = {
    val system = ActorSystem(AddingSystem(), "system")
}
