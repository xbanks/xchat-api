# XChat Messaging API Coding Standard

## Tests
### Testing actors

We will be using the [Akka TestKit](http://doc.akka.io/docs/akka/current/scala/testing.html#Asynchronous_Integration_Testing_with_TestKit) framework to test services.
Service Tests will inherit from, in this order:
* `TestKit(ActorSystem("service-test-system"))`
* [`WordSpec`](http://www.scalatest.org/user_guide/selecting_a_style)
* `BeforeAndAfterAll`
