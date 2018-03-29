package client

trait Permission

case class PermissionPresent() extends Permission

case class PermissionAbsent() extends Permission

case class PermissionDefault() extends Permission



