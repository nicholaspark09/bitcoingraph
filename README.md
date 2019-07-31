## Bitcoin Graph
What is this application? It is a demonstration of a bitcoin graph
with prices over a given time period. 

## Setup
- cd into the directory you'd like to start the project
- git clone https://github.com/nicholaspark09/bitcoingraph.git
- git checkout develop && git pull origin develop
- ./gradlew clean

### Networking
`Bitcoinnetwork` is a networking module I created to separate out
the networking and the logic therein. The application itself interacts
with `bitcoinnetwork` but strictly through interfaces. This allows
the implementation details to change in `bitcoinnetwork` without affecting
the users of it. The endpoint that the application uses may be changed
by adding a string in `strings.xml` of `endpoint`. 

### Architecture
The application follows the MVVM architecture. The viewmodel is used to 
temporarily persist the values retrieved by the networking layer. When
the user interacts with the UI, the viewmodel is notified and makes a call
to the network. Using rxjava and LiveData the `events` stream from out to 
the UI starting from the networking layer and ending in the UI. In using
ViewModels, which persist through orientation changes, the data is persisted
temporarily allowing the user to rotate the phone and maintain state.

### Testing
I did not have enough time to get to Espresso tests. I did, however,
have enough time to put in some unit tests and integration tests that 
even test the network call.