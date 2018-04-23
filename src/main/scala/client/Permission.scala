package client

trait Permission

case object PermissionPresent extends Permission

case object PermissionAbsent extends Permission

case object PermissionDefault extends Permission

