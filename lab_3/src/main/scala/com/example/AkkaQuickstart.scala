//#full-example
package com.example


import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import com.example.GreeterMain.SayHello

//#greeter-actor
object Greeter {
  final case class Greet(whom: String, replyTo: ActorRef[Greeted]) //Сообщение String + ссылка на на актора-получателя
  final case class Greeted(whom: String, from: ActorRef[Greet]) //Сообщение String + ссылка на на актора-отправителя

  def apply(): Behavior[Greet] = Behaviors.receive { (context, message) =>
    context.log.info("Hello {}!", message.whom)
    //#greeter-send-messages
    //Отправка сообщения Greeted обратно отправителю с информацией о том, кто был поприветствован
    message.replyTo ! Greeted(message.whom, context.self)
    //#greeter-send-messages
    Behaviors.same
  }
}
//#greeter-actor

//актор, отвечающий на сообщения
object GreeterBot {

  def apply(max: Int): Behavior[Greeter.Greeted] = {
    bot(0, max)
  }
 //Обработка сообщений Greeted
  private def bot(greetingCounter: Int, max: Int): Behavior[Greeter.Greeted] =
    Behaviors.receive { (context, message) =>
      val n = greetingCounter + 1
      context.log.info("Greeting {} for {}", n, message.whom)  //Логирование кол-ва приветствий
      if (n == max) {  //Если счетчик уперся в max, то остановка актора
        Behaviors.stopped
      } else {
        //Отправка нового приветсвия актору Greeter
        message.from ! Greeter.Greet(message.whom, context.self)
        bot(n, max) //Рекурсивная обработка сообщений
      }
    }
}
//#greeter-bot

 //актор-координатор остальных акторов 
object GreeterMain {

  final case class SayHello(name: String) //Сообщение с именемц
//Поведение актора 
  def apply(): Behavior[SayHello] =
    Behaviors.setup { context =>

       //Создание актора Greeter и ссылки на него
      val greeter = context.spawn(Greeter(), "greeter")

      //Поведение обработки входящего сообщения 
      Behaviors.receiveMessage { message =>

        //Создание нового GreeterBot для обработки приветствий
        val replyTo = context.spawn(GreeterBot(max = 3), message.name)

        //Отправка приветствия от основного актора к Greeter с указанием нового бота как получателя ответа.
        greeter ! Greeter.Greet(message.name, replyTo)
        Behaviors.same  //Поведение после обработки сообщения не меняется
      }
    }
}
//#greeter-main

//#main-class
object AkkaQuickstart extends App {
  //#actor-system
  val greeterMain: ActorSystem[GreeterMain.SayHello] = ActorSystem(GreeterMain(), "AkkaQuickStart") //Создание системы акторов 
  //#actor-system

  //#main-send-messages
  greeterMain ! SayHello("Charles")   //Отправка сообщения SayHello с именем "Чарли" в систему акторов
  //#main-send-messages
}
//#main-class
//#full-example