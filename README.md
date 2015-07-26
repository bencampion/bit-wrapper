# Bit Wrapper

[![Build Status](https://travis-ci.org/bencampion/bit-wrapper.svg?branch=master)](https://travis-ci.org/bencampion/bit-wrapper) [![Coverage Status](http://coveralls.io/repos/bencampion/bit-wrapper/badge.svg?branch=master&service=github)](http://coveralls.io/github/bencampion/bit-wrapper?branch=master)

Bit wrapper is an declarative framework written in Java for parsing binary data in a `byte[]` or `ByteBuffer`. It uses annotated interfaces to describe the fields in a bitstream and generates implementations of these interfaces at runtime that can be used to access the fields. Bit Wrapper is lazy; it doesn't copy data from the underlying `byte[]` or `ByteBuffer` until it needs to.

* [API documentation](https://bencampion.github.io/bit-wrapper/apidocs/)

## Examples

### IPv6 packet header

Bit Wrapper uses _views_ to describe binary data. A view is an interface with annotated methods. Here's a view for an [IPv6 packet header](https://en.wikipedia.org/wiki/IPv6_packet#Fixed_header):

```java
interface IPv6Packet {

    @Bits(offset = 4, length = 8)
    TrafficClass trafficClass();

    @Bits(offset = 12, length = 20)
    int flowLabel();

    @Bytes(offset = 4, length = 2)
    int payloadLength();

    @Bytes(offset = 6, length = 1)
    byte nextHeader();

    @Bytes(offset = 7, length = 1)
    short hopLimit();

    @Bytes(offset = 8, length = 16)
    InetAddress sourceAddress();

    @Bytes(offset = 24, length = 16)
    InetAddress destinationAddress();
}
```

The `@Bits` and `@Bytes` annotations describe the bytes that the method should return. `offset` is a zero-based index into the bitstream and `length` is the number of bits or bytes following the index.

The return type of the method describes the data type of the field. Note that the length of `payloadLength()` is only two bytes despite the return type being an `int`. Bit Wrapper will left pad the bytes with zeros if it is not long enough to fit into an `int`. This allows the field to be treated as an unsigned 16-bit integer. 

The `TrafficClass` type is just another view:

```java
interface TrafficClass {

    @Bits(length = 6)
    byte diffServe();

    @Bits(offset = 6, length = 2)
    byte ecn();
}

```

`diffServe()` doesn't have an explicit `offset` because it uses the default offset of `0`. For nested views the offset is relative to start of the view, not the start of the underlying bitstream.

Once a view has been defined, it can be used to access data in a `byte[]` by wrapping it around the array:


```java
byte[] bytes = ... // array containing the packet header
BitWrapper wrapper = BitWrapper.create();
IPv6Header header = wrapper.wrap(bytes, IPv6Header.class);
```

### PNG files

This example shows how Bit Wrapper can be used to parse an entire PNG image. A PNG consists of an [8 byte header](http://www.w3.org/TR/PNG/#5PNG-file-signature) followed by a series of [chunks](http://www.w3.org/TR/PNG/#5Chunk-layout). Here's a view for single chunk:

```java
interface PngChunk {

    @Bytes(length = 4)
    int length();

    @Bytes(offset = 4, length = 4)
    String type();

    @Bytes(offset = 8, lengthExp = "length()")
    ByteBuffer data();

    @Bytes(offsetExp = "length() + 8", length = 4)
    byte[] crc();
    
    default int chunkSize() {
        return length() + 12;
    }
}
```

This uses the `offsetExp` and `lengthExp` attributes to allow the offsets and lengths to be derived from other fields in the view. The view also has a default method to provide an easy way to access the size of the entire chunk.

Bit Wrapper respects the position attribute of a `ByteBuffer` when wrapping it so that the first byte of the view corresponds to the position of the buffer. Changing the position of the buffer each time it is wrapped can be used to iterate through all of chunks in the file:

```java
ByteBuffer buf = ... // buffer containing a complete PNG image
BitWrapper wrapper = BitWrapper.create();
buf.position(8); // skip 8 byte PNG header
while (buf.hasRemaining()) {
    PngChunk chunk = wrapper.wrap(buf, PngChunk.class);
    System.out.println(chunk.type());
    buf.setPosition(buf.getPosition() + chunk.chunkSize());
}
```

## Decoder functions

Bit Wrapper uses _decoders_ to convert bytes into Java types. A decoder for type `T` is just an implementation of `Function<ByteBuffer, T>`. Bit Wrapper provides functions for the following Java types:

* `boolean` / `Boolean`
* `byte` / `Byte`
* `short` / `Short`
* `int` / `Integer`
* `long` / `Long`
* `float` / `Float`
* `double` / `Double`
* `char` / `Character`
* `byte[]`
* `ByteBuffer`
* `String`
* `InetAddress`

These functions are automatically loaded when creating `BitWapper` instances. Adding support for additional Java types is just a matter of defining a function and telling the wrapper to use it. Here's an example of how to add a decoder that converts a 32-bit Unix timestamp into an `Instant`:

```java
Function<ByteBuffer, Instant> unixTime = buf -> Instant.ofEpochSecond(buf.getInt());
BitWrapper wrapper = BitWrapper.builder().addDecoder(Instance.class unixTime).build();
```

New decoders can be created by composing existing decoders functions. The Unix timestamp decoder in the previous example could also be written like this:

```java
Function<ByteBuffer, Instant> unixTime = new IntegerDecoder().andThen(Instant::ofEpochSecond);
```

The behaviour of the provided functions can be overridden by creating a new function with the desired behaviour. Here's an example of how to replace the default ASCII `String` decoder with a UTF-8 one:

```java
Function<ByteBuffer, Instant> utf8 = new StringDecoder(StandardCharsets.UTF_8);
BitWrapper wrapper = BitWrapper.builder().addDecoder(String.class utf8).build();
```

## Under the hood

Bit Wrapper doesn't copy data when it wraps a `byte[]` or `ByteBuffer` nor are the results of method calls on views cached. If the underlying data is changed after wrapping then these changes will be visible the next time a method is invoked on the view.

If a method is annotated with `@Bytes` and the return type is `ByteBuffer`, then the returned buffer will be a view of the underlying data too. This allows the returned buffer to be wrapped again without needing to copy any of the underlying data. For simplicity reasons, a `ByteBuffer` returned from a `@Bit` annotated methods is always a copy of the data.
