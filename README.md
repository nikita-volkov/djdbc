# Summary

JDBC is notorious for its unwieldiness. The API is highly intertwined and often inconsistent. This is the reason why most applications based on JDBC are prone to end up being a code spaghetti and a torture to maintain. 

"djdbc" is a library providing the solutions to problems of JDBC using just a few simple abstractions nudging the user towards the declarative style of programming. No frills involved: no metaprogramming, reflection or magical annotations. Just a few simple abstractions applying the best practices of isolation of concerns to make your persistence layer comprehensible and maintainable, without losing the ability to apply the extended features of JDBC.

This library is in major part inspired by ["hasql"](https://github.com/nikita-volkov/hasql), a PostgreSQL integration library for the Haskell functional programming language. Having such a background "djdbc" incorporates some great practices from the functional programming world, while still targeting the Java audience, as well as any other JVM language.

# Demo

An exhaustive demonstration of how the library is supposed to be used is presented in [the `demodb` package](https://github.com/nikita-volkov/djdbc/tree/master/src/test/java/djdbc/demodb) and [the accompanying `DemoDBTests` test suite](https://github.com/nikita-volkov/djdbc/blob/master/src/test/java/djdbc/DemoDBTests.java).
