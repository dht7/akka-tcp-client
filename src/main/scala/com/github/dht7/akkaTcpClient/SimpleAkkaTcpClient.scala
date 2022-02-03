package com.github.dht7.akkaTcpClient

import akka.actor.{ActorRef, ActorSystem}
import akka.util.ByteString

import java.net.InetSocketAddress
import scala.io.StdIn
import scala.util.control.Breaks._

object SimpleAkkaTcpClient {

  def main(args: Array[String]): Unit = {

    val hostname: String = "127.0.0.1" // Change hostname and port to your server address
    val port: Int = 9000
    val actorSystemName: String = "simple-actor-system"
    val clientActorName: String = "tcp-client-actor"

    val inetSocketAddress: InetSocketAddress = new InetSocketAddress(hostname, port)
    val actorSystem: ActorSystem = ActorSystem.create(actorSystemName)

    val listener: ActorRef = actorSystem.actorOf(Listener.props())
    val tcpClientActor = actorSystem.actorOf(TcpClientActor.props(inetSocketAddress, listener), clientActorName)

    Thread.sleep(5000)

    println("Type a message and press enter to send")
    println("Type stop to break loop")

    breakable{
      while(true){
        val input: String = StdIn.readLine()

        if(input == "stop"){
          tcpClientActor ! "close"
          break
        }

        tcpClientActor ! ByteString(input + "\n")
        Thread.sleep(5000)
      }
    }

    Thread.sleep(10000)
    actorSystem.terminate()

  }

}
