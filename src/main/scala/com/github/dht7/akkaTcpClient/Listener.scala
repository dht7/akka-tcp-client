package com.github.dht7.akkaTcpClient

import akka.actor.{Actor, Props}

object Listener {
  def props(): Props =
    Props(new Listener())
}

class Listener extends Actor{
  override def receive: Receive = {
    case message: String =>
      println(message)
  }
}
