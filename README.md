# Sample input plugin for Embulk

Embulk input plugin sample.

## Overview

* **Plugin type**: input
* **Resume supported**: yes
* **Cleanup supported**: yes
* **Guess supported**: no

## Configuration


## Example

```yaml
in:
  type: sample
out:
  type: stdout
```

```
*************************** 1 ***************************
   string (   string) : string
  boolean (  boolean) : true
     long (     long) : 10
   double (   double) : 3.14
timestamp (timestamp) : 2016-01-01 00:00:00 UTC
     json (     json) : json
*************************** 2 ***************************
   string (   string) : string
  boolean (  boolean) : true
     long (     long) : 10
   double (   double) : 3.14
timestamp (timestamp) : 2016-01-01 00:00:00 UTC
     json (     json) : json
```

## Build

```
$ ./gradlew gem  # -t to watch change of files and rebuild continuously
```
