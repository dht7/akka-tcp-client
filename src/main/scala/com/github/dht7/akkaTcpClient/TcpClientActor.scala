package com.github.dht7.akkaTcpClient

import akka.actor.{Actor, ActorRef, Props}

import java.net.InetSocketAddress
import akka.io.{IO, Tcp}
import akka.util.ByteString

object TcpClientActor {
  def props(remote: InetSocketAddress, replies: ActorRef): Props =
    Props(new TcpClientActor(remote, replies))
}

class TcpClientActor(remote: InetSocketAddress, listener: ActorRef) extends Actor{

  import Tcp._
  import context.system

  val manager: ActorRef = IO(Tcp)
  manager ! Connect(remote)

  override def receive: Receive = {
    case CommandFailed(_: Connect) =>
      listener ! "Connection failed"
      context.stop(self)
    case connected @ Connected(remote, local) =>
      listener ! "Connected"

      val connection = sender()
      connection ! Register(self)
      listener ! "Registered"

      context.become {
        case data: ByteString =>
          listener ! "Sending message: " + data.utf8String.trim
          connection ! Write(data)
        case CommandFailed(w: Write) =>
          listener ! "Write operation failed"
        case Received(data) =>
          listener ! "Received message: " + data.utf8String.trim
        case "close" =>
          listener ! "Close command received"
          connection ! Close
        case _: ConnectionClosed =>
          listener ! "Connection closed"
          context.stop(self)
      }
  }
}
