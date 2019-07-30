## Bitcoin Network
This library is intended to act as the networking library for the project.
It is a library because I want to separate out networking in a manner that
will allow the tools used to call the network to change in a decoupled
fashion from the application and its UI.

### Dependency Injection
Dependency Injection is done without dagger in this library to prevent
downstream dependency conflicts

### Tools
In order to actually make the calls, this library uses Retrofit, Okhttp,
and Rxjava.