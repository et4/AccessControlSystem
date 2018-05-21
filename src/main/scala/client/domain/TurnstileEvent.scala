package client.domain

trait TurnstileEvent

case class EntranceEvent(eventName: String = "entrance") extends TurnstileEvent

case class ExitEvent(eventName: String = "exit") extends TurnstileEvent
