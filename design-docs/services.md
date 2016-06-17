# Services

## Receiving Messages
All messages that an actor rec

## onComplete
On a successful table action, the services will respond with log messages following this format:

    If the response is a sequence:
        Success(rows)
        log.info( "[$action] Rows Received: $rows" )
    If the response is a row count:
        Success(rowsAffected)
        log.info( "[$action] Rows Affected: $rows" )
    If the response is a row:
        Success(row)
        log.info( "[$action] Row Found: $row" )

Address failures as follows:

    log.error( "[$action] Failure: $msg" )